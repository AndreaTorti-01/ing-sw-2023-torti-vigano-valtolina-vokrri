package it.polimi.ingsw.view;

import it.polimi.ingsw.model.GameStatus;
import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.serializable.GameView;
import it.polimi.ingsw.network.serializable.MoveMsg;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.utils.Constants.*;

public class Tui extends Observable implements RunnableView {
    String playerName;
    Player me;
    GameView modelView;

    public void run() {
        Scanner scanner = new Scanner(System.in);

        //asks the player name
        System.out.println("Insert your name: ");
        playerName = scanner.nextLine();
        notifyObservers(playerName);

        clearScreen();

        // while true loop
        // waits for user input, if it is user turn
        // sends input to Client
        while (true) {

            if (modelView.getGameStatus() == GameStatus.waiting) {
                // TODO: recuperare nome del winner
                printWaitingScreen();
            } else if (modelView.getGameStatus() == GameStatus.started) {
                printGameStatus(modelView);
                //checking if it's my turn
                if(modelView.getCurrentPlayer().getName().equals(playerName))
                    // pickCards automatically sends updates to client (the message contains the chosen cards' coordinates)
                    pickCards(modelView);

                System.out.println("press c to enter the game chat");
            } else {
                printEndScreen(modelView.getWinner().getName());
            }

        }
    }

    @Override
    public void updateView(GameView modelView) {
        this.modelView = modelView;
    }

    private void clearScreen() {
        // Clear the console screen
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void pickCards(GameView gameView) {
        Scanner scanner = new Scanner(System.in);
        List<ItemCard> picked = new ArrayList<ItemCard>();
        List<List<Integer>> pickedCoords = new ArrayList<List<Integer>>();
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


        for (Player p : gameView.getPlayers())
            if (p.getName().equals(playerName)) {
                me = p;
                break;
            }
        if (me == null) throw new NullPointerException();

        for (int j = 0; j < numberOfColumns && maxCards < 3; j++) {
            int freeSlots = 0;
            for (int i = 0; i < numberOfRows && freeSlots < 3; i++) {
                if (gameView.getShelfOf(me)[i][j] == null) freeSlots++;
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
            valid = isTakeable(gameView, row, column);

            if (valid && picked.contains(gameView.getBoard()[row][column])) valid = false;

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
                picked.add(gameView.getBoard()[row][column]);
                ArrayList<Integer> coords = new ArrayList<Integer>();
                coords.add(row);
                coords.add(column);
                pickedCoords.add(coords);
                count++;
            } else
                printError("Card " + row + column + " is not valid!");

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

    private boolean isTakeable(GameView gameView, int row, int column) {
        boolean free = false;

        if (row < 0 || row >= numberOfRows || column < 0 || column >= numberOfColumns)
            return false;

        if (row == 0 || row == numberOfRows - 1)
            free = true;
        else if (column == 0 || column == numberOfColumns - 1)
            free = true;
        else if (gameView.getBoard()[row - 1][column] == null || gameView.getBoard()[row + 1][column] == null || gameView.getBoard()[row][column - 1] == null || gameView.getBoard()[row][column + 1] == null)
            free = true;

        return gameView.getBoardValid()[row][column] && gameView.getBoard()[row][column] != null && free;
    }

    private void printBoard(ItemCard[][] board, boolean[][] boardValid) {
        StringBuilder output = new StringBuilder();

        output.append("---------------------\n");
        for (int i = 0; i < Constants.boardSize; i++) {
            output.append("| ");
            for (int j = 0; j < Constants.boardSize; j++) {
                if (boardValid[i][j]) {
                    ItemCard card = board[i][j];
                    if (card != null)
                        output.append(card).append(" ");
                    else
                        output.append("* ");
                } else {
                    output.append("- ");
                }

            }
            output.append("|\n");
        }
        output.append("---------------------");

        System.out.print(output);
    }

    private void printShelf(ItemCard[][] shelf) {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < Constants.numberOfRows; i++) {
            output.append("| ");
            for (int j = 0; j < Constants.numberOfColumns; j++) {
                if (shelf[i][j] != null) output.append(shelf[i][j].toString()).append(" ");
                else output.append("* ");
            }
            output.append("|\n");
        }
        output.append("------------");

        System.out.print(output);
    }

    private void printGameStatus(GameView gameView) {
        // Print the game status, including the main board and the shelves
        clearScreen();

        // Print the active player name
        System.out.println(ANSI_PURPLE + "\t\t\t\t\t  >>  " + ANSI_GREEN + gameView.getCurrentPlayer().getName() + ANSI_PURPLE + "  <<" + ANSI_RESET);

        // Print the board
        printBoard(gameView.getBoard(), gameView.getBoardValid());

        // Print the shelf of the active player
        printShelf(gameView.getShelfOf(gameView.getCurrentPlayer()));

        // Print the bag status
        System.out.println(ANSI_PURPLE + "\t\t\t\t\t  >>  " + ANSI_GREEN + "BAG: " + ANSI_YELLOW + gameView.isBagEmpty() + ANSI_PURPLE + "  <<" + ANSI_RESET);

        // Print the common goal cards type and points on top of the stack
        for (int i = 0; i < gameView.getCommonGoalCards().size(); i++) {
            System.out.println(ANSI_PURPLE + "\t\t\t\t\t  >>  " + ANSI_GREEN + "GOAL " + i + ": " + ANSI_YELLOW + gameView.getCommonGoalCards().get(i).getType() + " " + gameView.getCommonGoalCards().get(i).peekPoints() + ANSI_PURPLE + "  <<" + ANSI_RESET);
        }

        // Print the personal goal card pattern
        System.out.println(ANSI_PURPLE + "\t\t\t\t\t  >>  " + ANSI_GREEN + "GOAL: " + ANSI_YELLOW + gameView.getCurrentPlayer().getPersonalGoalCard() + ANSI_PURPLE + "  <<" + ANSI_RESET);
    }

    private void printWaitingScreen() {
        // Print the loading screen
        clearScreen();
        System.out.println(ANSI_YELLOW + "\t\t\t\t\t\t\t  >>  WELCOME TO  <<" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "\t\t\t\t\t\t\t  >>  MY SHELFIE  <<" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "\n \n \n \t\t\t\t\t\t\t  developed by gc-33" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "\n \t Torti Andrea - Valtolina Cristiano - Viganò Diego - Vokrri Fabio" + ANSI_RESET);
        System.out.print("\n\n");
        System.out.println(ANSI_PURPLE + "\t\t\t\t\t\t  >>  Press ENTER to start  <<" + ANSI_RESET);
        System.out.print("\n\n");
        System.out.println("\t\t\t\t\t\t Waiting for other players...");

        Scanner in = new Scanner(System.in);
        String check = in.nextLine(); //only to be sure that a key (enter) is pressed
    }

    private void printEndScreen(String winnerName) {
        // Print the end screen, showing the winner
        clearScreen();
        System.out.println(ANSI_PURPLE + "\t\t\t\t\t\t\t  >>  GAME OVER  <<");
        System.out.println(ANSI_YELLOW + "\t\t\t\t\t\t  >>  WINNER: " + ANSI_GREEN + winnerName + ANSI_YELLOW + "  <<" + ANSI_RESET);

        System.out.print("\n\n");
        System.out.println(ANSI_PURPLE + "\t\t\t\t\t\t  >>  Press ENTER to restart  <<" + ANSI_RESET);

        Scanner in = new Scanner(System.in);
        String check = in.nextLine(); //only to be sure that a key (enter) is pressed
    }

    private void printError(String error) {
        // Print an error message
        System.out.println(ANSI_RED + error + ANSI_RESET);
    }

    private String askPlayerName() {
        // Ask the name of the player
        Scanner in = new Scanner(System.in);
        System.out.println("  >>  Enter your name:  ");
        return in.nextLine();
    }

    private int[] askPlayerChoice() {
        // Ask the player to choose a card
        int[] coordinates = new int[2];
        Scanner in = new Scanner(System.in);
        System.out.println("  >>  Type the row number:");
        coordinates[0] = Integer.parseInt(in.next());
        System.out.println("  >>  Type the column number:");
        coordinates[1] = Integer.parseInt(in.next());
        return coordinates;
    }

    private int askPlayerInsertion() {
        // Ask the player to insert a card
        Scanner in = new Scanner(System.in);
        System.out.println("  >>  Type a column number:");
        return Integer.parseInt(in.next());
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


}
