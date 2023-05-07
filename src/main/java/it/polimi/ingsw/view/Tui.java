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

        System.err.println("updated view!");
    }

    /**
     * takes care of notifying observers
     */
    private void askPlayerNumber() {
        Scanner scanner = new Scanner(System.in);
        Integer playerNumber = 0;
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
        List<ItemCard> picked = new ArrayList<>();
        List<List<Integer>> pickedCoords = new ArrayList<>();
        MoveMsg msg;

        System.out.println("You can peek cards from the board");
        int count = 0;
        boolean useCol = false;
        boolean useRow = false;
        int firstRow = 0;
        int firstCol = 0;
        int secondRC = 0;
        boolean valid;

        //defining the maximum takeable card number
        int maxCards = 0;
        int[] freeSlotsNumber = new int[numberOfColumns];


        for (Player p : modelView.getPlayers())
            if (p.getName().equals(playerName)) {
                me = p;
                break;
            }
        if (me == null) throw new NullPointerException();

        for (int j = 0; j < numberOfColumns && maxCards < 3; j++) {
            int freeSlots = 0;
            for (int i = 0; i < numberOfRows && freeSlots < 3; i++) {
                if (modelView.getShelfOf(me)[i][j] == null) freeSlots++;
                if (freeSlots > maxCards) maxCards = freeSlots;
                freeSlotsNumber[j] = freeSlots;
            }
        }

        //asking for card coordinates
        System.out.println("You can take up to " + maxCards + " cards");

        while (count <= maxCards) {
            System.out.print("Card " + (count + 1) + "-> enter ROW number:  ");
            int row = scanner.nextInt();
            System.out.print("Card " + (count + 1) + "-> enter COLUMN number:  ");
            int column = scanner.nextInt();

            //checking coordinate and card validity
            valid = isTakeable(modelView, row, column);

            if (valid && picked.contains(modelView.getBoard()[row][column])) valid = false;

            if (valid && ((useCol && column != firstCol) || (useRow && row != firstRow))) valid = false;

            if (valid && picked.size() == 1) { //only contains the first one, will have to define if to use row or col straight line
                if (row != firstRow && column != firstCol) valid = false;
                if (isAdjacent(row, column, firstRow, firstCol)) {
                    useCol = column == firstCol;
                    useRow = row == firstRow;
                }
                if (useCol)
                    secondRC = firstRow; // Col known, row unknown. we only have to know the Row coordinate of the seconda card to check future adjacent card's constraints
                else secondRC = firstCol; // Row known, col unknown, same as previous
            }

            if (valid && picked.size() == 2) {
                if (useCol && column != firstCol) valid = false; //must be in line
                if (useRow && row != firstRow) valid = false;
                if (useRow && !isAdjacent(row, column, firstRow, firstCol) && !isAdjacent(row, column, firstRow, secondRC))
                    valid = false; //must even be adjacent to the first or second card
                if (useCol && !isAdjacent(row, column, firstRow, firstCol) && !isAdjacent(row, column, secondRC, firstCol))
                    valid = false;
            }

            if (valid) {
                if (picked.size() == 0) {
                    firstCol = column;
                    firstRow = row;
                }
                picked.add(modelView.getBoard()[row][column]);
                ArrayList<Integer> coords = new ArrayList<Integer>();
                coords.add(row);
                coords.add(column);
                pickedCoords.add(coords);
                count++;
            } else printError("Card " + row + column + " is not valid!");

            if (count < maxCards) {
                System.out.println("You can pick " + (maxCards - count) + " more cards");
                System.out.print("do you want to pick another one?");
                boolean choice = askBoolean();
                if (!choice) break;
            }
        }

        int shelfCol;
        System.out.println("You can put the cards in your shelf! choose a column: ");
        do {
            shelfCol = scanner.nextInt();
            if (shelfCol < 0 || shelfCol >= numberOfColumns) printError("Invalid column number");
            else if (picked.size() > freeSlotsNumber[shelfCol]) printError("Not enough free slots in this column");
        } while (picked.size() > freeSlotsNumber[shelfCol]);

        msg = new MoveMsg(pickedCoords, shelfCol);

        notifyObservers(msg);
    }

    private boolean isAdjacent(int row, int column, int row2, int column2) {
        if (row == row2 && (column == column2 + 1 || column == column2 - 1)) return true;
        return column == column2 && (row == row2 + 1 || row == row2 - 1);
    }

    private boolean isTakeable(GameViewMsg gameViewMsg, int row, int column) {
        boolean free = false;

        if (row < 0 || row >= numberOfRows || column < 0 || column >= numberOfColumns) return false;

        if (row == 0 || row == numberOfRows - 1) free = true;
        else if (column == 0 || column == numberOfColumns - 1) free = true;
        else if (gameViewMsg.getBoard()[row - 1][column] == null || gameViewMsg.getBoard()[row + 1][column] == null || gameViewMsg.getBoard()[row][column - 1] == null || gameViewMsg.getBoard()[row][column + 1] == null)
            free = true;

        return gameViewMsg.getBoardValid()[row][column] && gameViewMsg.getBoard()[row][column] != null && free;
    }

    private void printGameStatus() {
        printBoard(modelView.getBoard(), modelView.getBoardValid());
        System.out.println("\n");
        printShelves();
    }


    private void printEndScreen(String winnerName) {


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
        System.out.println(error);
    }

    private enum State {
        ASK_NAME, ASK_NUMBER, WAITING_FOR_PLAYERS, WAITING_FOR_TURN, PLAY
    }
}
