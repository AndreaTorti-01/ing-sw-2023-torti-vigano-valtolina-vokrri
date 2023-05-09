package it.polimi.ingsw.view;

import it.polimi.ingsw.model.GameStatus;
import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.serializable.GameViewMsg;
import it.polimi.ingsw.network.serializable.MoveMsg;
import it.polimi.ingsw.utils.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.utils.Constants.*;

public class Tui extends Observable implements RunnableView {
    private final Object lock = new Object();
    GameStatus gameStatus = GameStatus.WAITING;
    private String playerName = "";
    private Player me;
    private GameViewMsg modelView;
    private State state = State.ASK_NAME;


    public Tui(Client client) {
        this.addObserver(client);
    }

    private State getState() {
        synchronized (lock) {
            return state;
        }
    }

    private void setState(State state) {
        synchronized (lock) {
            this.state = state;
            System.err.println("state changed to " + state);
            lock.notifyAll();
        }
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        // asks the player for his name
        this.playerName = askPlayerName();

        // waits for state to change
        while (getState() == State.ASK_NAME) {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    System.err.println("Interrupted while waiting for server: " + e.getMessage());
                }
            }
        }

        //
        if (getState() == State.ASK_NUMBER) {
            askPlayerNumber();
        }

        while (getState() == State.WAITING_FOR_PLAYERS) {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    System.err.println("Interrupted while waiting for server: " + e.getMessage());
                }
            }
        }
        //noinspection InfiniteLoopStatement
        while (true) {
            while (getState() == State.WAITING_FOR_TURN) {
                this.printGameStatus(); // TODO
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        System.err.println("Interrupted while waiting for server: " + e.getMessage());
                    }
                }
            }

            while (getState() == State.PLAY) {
                this.printGameStatus(); // TODO
                this.pickCards();
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        System.err.println("Interrupted while waiting for server: " + e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * updates the view with the new model state
     *
     * @param modelView which contains a representation of the model state
     */
    @Override
    public void updateView(GameViewMsg modelView) {
        this.modelView = modelView;

        // the game is waiting for players
        if (!playerName.equals("") && modelView.getGameStatus().equals(GameStatus.WAITING)) {
            if (playerName.equals(modelView.getPlayers().get(0).getName()))
                setState(State.ASK_NUMBER); // I am lobby leader
            else setState(State.WAITING_FOR_PLAYERS); // I am not lobby leader
        }
        // the game has started
        else if (modelView.getGameStatus().equals(GameStatus.STARTED)) {
            if (modelView.getCurrentPlayer().getName().equals(this.playerName)) {
                setState(State.PLAY); // it's my turn
            } else setState(State.WAITING_FOR_TURN); // it's not my turn
        }
    }

    /**
     * takes care of notifying observers
     */
    private void askPlayerNumber() {
        Scanner scanner = new Scanner(System.in);
        int playerNumber = 0;
        boolean valid = false;
        while (!valid) {
            System.out.println("How many players are playing? (2-4)");
            try {
                playerNumber = Integer.parseInt(scanner.nextLine());
                if (playerNumber >= 2 && playerNumber <= 4) valid = true;
                else printError("Invalid number of players");
            } catch (NumberFormatException e) {
                printError("Invalid number or non-numeric input");
            }
        }
        notifyObservers(playerNumber);
    }


    /**
     * takes care of notifying observer
     */
    private void pickCards() {
        Scanner scanner = new Scanner(System.in);
        List<List<Integer>> pickedCoords = new ArrayList<>();

        System.out.println("You can pick cards from the board");
        int pickedNum = 0; //number of already picked cards
        boolean validChoice = false;
        int shelfCol = 0; //column of the shelf where the player is moving cards to
        int maxCards = 0; //maximum cards that can be picked
        int[] freeSlotsNumber = new int[numberOfColumns]; //number of max cards that can be inserted in each column


        for (Player p : modelView.getPlayers())
            if (p.getName().equals(playerName)) {
                me = p;
                break;
            }
        if (me == null) throw new NullPointerException();

        for (int j = 0; j < numberOfColumns; j++) {
            int freeSlots = 0;
            for (int i = 0; i < numberOfRows && freeSlots < 3; i++) {
                if (modelView.getShelfOf(me)[i][j] == null) freeSlots++;
                if (freeSlots > maxCards) maxCards = freeSlots;
                freeSlotsNumber[j] = freeSlots;
            }
        }
        if (maxCards > 3) maxCards = 3;

        //asking for card coordinates
        System.out.println("You can take up to " + maxCards + " cards");

        while (pickedNum < maxCards) {
            System.out.print("Card " + (pickedNum + 1) + "-> enter ROW number:  ");
            int row = scanner.nextInt();
            System.out.print("Card " + (pickedNum + 1) + "-> enter COLUMN number:  ");
            int column = scanner.nextInt();

            //checking coordinate validity
            if (isTakeable(modelView, row, column, pickedCoords)) {
                List<Integer> coords = new ArrayList<>();
                coords.add(row);
                coords.add(column);
                pickedCoords.add(coords);
                pickedNum++;
            } else printError("Invalid coordinates!, retry");

            //ask if the player wants to pick another card
            if (pickedNum < maxCards) {
                System.out.println("Do you want to pick another card?");
                if (!askBoolean()) break;
            }

        }

        while (!validChoice) {
            System.out.println("Chose a shelf column to move the cards to: ");
            shelfCol = scanner.nextInt();
            if (shelfCol < 0 || shelfCol >= numberOfColumns) printError("Invalid column! retry");
            else if (freeSlotsNumber[shelfCol] < pickedNum) printError("Not enough space! retry");
            else validChoice = true;
        }
        //notifying observers
        notifyObservers(new MoveMsg(pickedCoords, shelfCol));
    }

    private boolean isAdjacent(int row, int column, int row2, int column2) {
        if (row == row2 && (column == column2 + 1 || column == column2 - 1)) return true;
        return column == column2 && (row == row2 + 1 || row == row2 - 1);
    }

    private boolean isTakeable(GameViewMsg gameViewMsg, int row, int column, List<List<Integer>> pickedCoords) {
        boolean free = false; //has a null adjacent card
        boolean valid = true; //is a valid card (not taken yet, in the same row or col as the others)
        boolean adjacent = false; //is adjacent to at least one of the other cards

        //out of bound check
        if (row < 0 || row >= numberOfRows || column < 0 || column >= numberOfColumns) return false;
        //non existing card check
        if (!gameViewMsg.getBoardValid()[row][column] || gameViewMsg.getBoard()[row][column] == null) return false;

        //checking if the card is adjacent to a free space (necessary to be taken
        if (row == 0 || row == numberOfRows - 1) free = true;
        else if (column == 0 || column == numberOfColumns - 1) free = true;
        else if (gameViewMsg.getBoard()[row - 1][column] == null || gameViewMsg.getBoard()[row + 1][column] == null || gameViewMsg.getBoard()[row][column - 1] == null || gameViewMsg.getBoard()[row][column + 1] == null)
            free = true;
        else if (!gameViewMsg.getBoardValid()[row - 1][column] || !gameViewMsg.getBoardValid()[row + 1][column] || !gameViewMsg.getBoardValid()[row][column - 1] || !gameViewMsg.getBoardValid()[row][column + 1])
            free = true;

        //the cards must be adjacent to each other (in line or in column) -> valid is used to check this condition
        if (pickedCoords.size() > 0) {
            boolean inRow = true;
            boolean inColumn = true;
            for (List<Integer> coords : pickedCoords) {
                if (row == coords.get(0) && column == coords.get(1)) valid = false; //already picked
                if (row != coords.get(0)) inRow = false;
                if (column != coords.get(1)) inColumn = false;
            }
            valid = valid && (inRow || inColumn);
        }

        //the card must be adjacent to at least one of the other cards
        if (pickedCoords.size() > 0) {
            for (List<Integer> coords : pickedCoords) {
                if (isAdjacent(row, column, coords.get(0), coords.get(1))) {
                    adjacent = true;
                    break;
                }
            }
        } else adjacent = true;


        return free && valid && adjacent;
    }

    private void printGameStatus() {
        printBoard(modelView.getBoard(), modelView.getBoardValid());
        System.out.println("\n");
        printShelves();
    }


    private void printEndScreen(String winnerName) {
        System.out.println("The winner is " + winnerName + "!");
    }

    /**
     * takes care of notifying observers
     *
     * @return the name of the player
     */
    private String askPlayerName() {
        // Ask the name of the player
        Scanner in = new Scanner(System.in);
        System.out.println("  >>  Enter your name:  ");
        String name = in.nextLine();
        notifyObservers(name);
        return name;
    }

    private boolean askBoolean() {
        Scanner in = new Scanner(System.in);
        System.out.print("  >>  (y/n)");

        while (true) {
            String input = in.nextLine();
            char c = Character.toLowerCase(input.charAt(0));
            if (c == 'y') {
                return true;
            } else if (c == 'n') {
                return false;
            } else {
                printError("Please enter a valid input  (y/n)");
            }
        }
    }

    private void printShelves() {
        for (Player p : modelView.getPlayers()) {
            Shelf shelf = p.getShelf();
            System.out.println(p.getName() + "'s Shelf:");
            StringBuilder output = new StringBuilder();

            for (int i = 0; i < numberOfRows; i++) {
                output.append("| ");
                for (int j = 0; j < numberOfColumns; j++) {
                    if (shelf.getItemsMatrix()[i][j] != null)
                        output.append(shelf.getItemsMatrix()[i][j].toString()).append(" ");
                    else output.append("* ");
                }
                output.append("|\n");
            }
            output.append("-------------\n");

            output.append("  ");
            for (int i = 0; i < numberOfColumns; i++) {
                output.append(i).append(" ");
            }
            output.append("\n\n");

            System.out.print(output);
        }
    }

    private void printBoard(ItemCard[][] board, boolean[][] boardValid) {
        StringBuilder output = new StringBuilder("The board:\n");

        output.append("   ");
        for (int i = 0; i < boardSize; i++) {
            output.append(i).append(" ");
        }
        output.append("\n");

        output.append(" ---------------------\n");
        for (int i = 0; i < boardSize; i++) {
            output.append(i).append("| ");
            for (int j = 0; j < boardSize; j++) {
                if (boardValid[i][j]) {
                    ItemCard card = board[i][j];
                    if (card != null) output.append(card).append(" ");
                    else output.append("* ");
                } else {
                    output.append("- ");
                }

            }
            output.append("|\n");
        }
        output.append(" ---------------------");

        System.out.print(output);
    }

    private void printError(String error) {
        // Print an error message
        System.out.println(ANSI_PURPLE + error + ANSI_RESET);
    }

    private enum State {
        ASK_NAME, ASK_NUMBER, WAITING_FOR_PLAYERS, WAITING_FOR_TURN, PLAY
    }
}
