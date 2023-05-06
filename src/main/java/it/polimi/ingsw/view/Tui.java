package it.polimi.ingsw.view;

import it.polimi.ingsw.model.GameStatus;
import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.serializable.GameViewMsg;
import it.polimi.ingsw.network.serializable.MoveMsg;
import it.polimi.ingsw.network.serializable.TuiCommands;
import it.polimi.ingsw.utils.Observable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.utils.Constants.*;

public class Tui extends Observable implements RunnableView {
    String playerName;
    Player me;
    GameViewMsg modelView;


    GameStatus gameStatus = GameStatus.WAITING;

    public Tui() {
        System.err.println("warning: created non observable tui");
    }

    public Tui(Client client) {
        this.addObserver(client);
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        //asks the player name
        playerName = askPlayerName();
        notifyObservers(playerName);

        clearConsole();

        while (modelView == null) {
            printWaitingScreen();
        }

        // while true loop
        // waits for user input, if it is user turn
        // sends input to Client
        while (true) {

            if (modelView.getGameStatus() == GameStatus.WAITING) {
                printWaitingScreen();
                //doesn't return until the game is still in waiting status

            } else if (modelView.getGameStatus() == GameStatus.STARTED) {
                printGameStatus();
                //checking if it's my turn
                if (modelView.getCurrentPlayer().getName().equals(playerName)) pickCards();
                //pickcards automatically calls the notifyObservers method with move message
                //TODO: if we develop the game chat, its method will take place here!
            } else {
                printEndScreen(modelView.getWinner().getName());
            }

        }
    }

    @Override
    public void updateView(GameViewMsg modelView) {
        this.modelView = modelView;
        this.gameStatus = modelView.getGameStatus();
        System.err.println("updated view!");
    }

    /**
     * Public method used to run internal private functions for testing purposes
     */
    private void ScreenOutTest() {
        System.out.println("ScreenOutTest");
        //printBoard(new ItemCard[9][9], new boolean[9][9]);
        printEndScreen("Diego");

        //printWaitingScreen();
        clearConsole();
    }

    private void waitForCommands() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        boolean validCommand = false;
        //compared the textual input with the possible commands
        for(TuiCommands command : TuiCommands.values()){
            if(scanner.equals(command.getCommandName())){
                validCommand = true;
                break;
            }
        }
        if(!validCommand)
            printError("Invalid command");
        else
            handleCommand(input);
    }

    private void handleCommand(String input) {
        //TODO handle the command types
    }

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

    private void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException ex) {
        }
    }

    private void pickCards() {
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
        // Prints the title, boards and shelves
        clearConsole();
        printMyShelfie();
        System.out.println("\n\n");
        printBoard(modelView.getBoard(), modelView.getBoardValid());
        System.out.println("\n\n");
        printShelves();
        System.out.println("\n\n");
    }

    private void printSeparee() {
        System.out.println(ANSI_YELLOW + "\n\n" + "\t\t\t\t╔══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╗\n" + "\t\t\t\t╚══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╝" + ANSI_RESET + "\n\n");
    }

    private void printWaitingScreen() {
        int iter = 0;


        //clear the console screen
        clearConsole();


        System.out.println("\n" + ANSI_YELLOW +

                "\t\t\t\t\t\t\t\t\t\t\t\t█   █ █▀▀ █   █▀▀ █▀▀█ █▀▄▀█ █▀▀ 　 ▀▀█▀▀ █▀▀█\n" + "\t\t\t\t\t\t\t\t\t\t\t\t█ █ █ █▀▀ █   █   █  █ █ ▀ █ █▀▀ 　   █   █  █\n" + "\t\t\t\t\t\t\t\t\t\t\t\t█▄▀▄█ ▀▀▀ ▀▀▀ ▀▀▀ ▀▀▀▀ ▀   ▀ ▀▀▀ 　   █   ▀▀▀▀\n\n\n");

        printMyShelfie();

        System.out.println("\n\n\n" + ANSI_CYAN + "\n \n \n \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t  developed by gc-33" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "\n \t\t\t\t\t\t\t\t\t Torti Andrea - Valtolina Cristiano - Viganò Diego - Vokrri Fabio" + ANSI_RESET);

        System.out.print("\n\n\n" + "\t\t\t\t█   █ █▀▀█ ▀ ▀▀█▀▀ ▀ █▀▀▄ █▀▀▀ 　 █▀▀ █▀▀█ █▀▀█ 　 █▀▀█ ▀▀█▀▀ █  █ █▀▀ █▀▀█ 　 █▀▀█ █   █▀▀█ █  █ █▀▀ █▀▀█ █▀▀\n" + "\t\t\t\t█ █ █ █▄▄█ █   █   █ █  █ █ ▀█ 　 █▀▀ █  █ █▄▄▀ 　 █  █   █   █▀▀█ █▀▀ █▄▄▀ 　 █  █ █   █▄▄█ █▄▄█ █▀▀ █▄▄▀ ▀▀█\n" + "\t\t\t\t█▄▀▄█ ▀  ▀ ▀   ▀   ▀ ▀  ▀ ▀▀▀▀ 　 ▀   ▀▀▀▀ ▀ ▀▀ 　 ▀▀▀▀   ▀   ▀  ▀ ▀▀▀ ▀ ▀▀ 　 █▀▀▀ ▀▀▀ ▀  ▀ ▄▄▄█ ▀▀▀ ▀ ▀▀ ▀▀▀");
        while (this.gameStatus.equals(GameStatus.WAITING)) {
            //waits 500 milliseconds
            try {
                TimeUnit.MILLISECONDS.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (iter == 0) {
                System.out.print(ANSI_YELLOW + "   ▄");
                iter = 1;
            } else if (iter == 1) {
                System.out.print(ANSI_RED + "   ▄");
                iter = 2;
            } else if (iter == 2) {
                System.out.print(ANSI_CYAN + "   ▄");
                iter = 3;
            } else {
                System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b");
                iter = 0;
            }
        }

    }

    private void printMyShelfie() {
        System.out.println(ANSI_YELLOW + "\t\t\t\t\t\t\t\t███╗   ███╗" + ANSI_PURPLE + "██╗   ██╗  " + ANSI_GREEN + " ██████╗" + ANSI_CYAN + "██╗  ██╗" + ANSI_RED + "███████╗" + ANSI_YELLOW + "██╗     " + ANSI_PURPLE + "███████╗" + ANSI_GREEN + "██╗" + ANSI_CYAN + "███████╗  " + ANSI_RED + "██╗\n" + ANSI_YELLOW + "\t\t\t\t\t\t\t\t████╗ ████║" + ANSI_PURPLE + "╚██╗ ██╔╝  " + ANSI_GREEN + "██╔════╝" + ANSI_CYAN + "██║  ██║" + ANSI_RED + "██╔════╝" + ANSI_YELLOW + "██║     " + ANSI_PURPLE + "██╔════╝" + ANSI_GREEN + "██║" + ANSI_CYAN + "██╔════╝  " + ANSI_RED + "██║\n" + ANSI_YELLOW + "\t\t\t\t\t\t\t\t██╔████╔██║" + ANSI_PURPLE + " ╚████╔╝   " + ANSI_GREEN + "╚█████╗ " + ANSI_CYAN + "███████║" + ANSI_RED + "█████╗  " + ANSI_YELLOW + "██║     " + ANSI_PURPLE + "█████╗  " + ANSI_GREEN + "██║" + ANSI_CYAN + "█████╗    " + ANSI_RED + "██║\n" + ANSI_YELLOW + "\t\t\t\t\t\t\t\t██║╚██╔╝██║" + ANSI_PURPLE + "  ╚██╔╝    " + ANSI_GREEN + " ╚═══██╗" + ANSI_CYAN + "██╔══██║" + ANSI_RED + "██╔══╝  " + ANSI_YELLOW + "██║     " + ANSI_PURPLE + "██╔══╝  " + ANSI_GREEN + "██║" + ANSI_CYAN + "██╔══╝    " + ANSI_RED + "╚═╝\n" + ANSI_YELLOW + "\t\t\t\t\t\t\t\t██║ ╚═╝ ██║" + ANSI_PURPLE + "   ██║     " + ANSI_GREEN + "██████╔╝" + ANSI_CYAN + "██║  ██║" + ANSI_RED + "███████╗" + ANSI_YELLOW + "███████╗" + ANSI_PURPLE + "██║     " + ANSI_GREEN + "██║" + ANSI_CYAN + "███████╗  " + ANSI_RED + "██╗\n" + ANSI_YELLOW + "\t\t\t\t\t\t\t\t╚═╝     ╚═╝" + ANSI_PURPLE + "   ╚═╝     " + ANSI_GREEN + "╚═════╝ " + ANSI_CYAN + "╚═╝  ╚═╝" + ANSI_RED + "╚══════╝" + ANSI_YELLOW + "╚══════╝" + ANSI_PURPLE + "╚═╝     " + ANSI_GREEN + "╚═╝" + ANSI_CYAN + "╚══════╝  " + ANSI_RED + "╚═╝" + ANSI_RESET);
    }

    private void printEndScreen(String winnerName) {
        // Print the end screen, showing the winner
        int iteration = 0;

        clearConsole();
        printMyShelfie();
        printSeparee();
        if(playerName.equals(winnerName)) {
            System.out.println(ANSI_PURPLE +"\t\t\t\t\t\t\t\t\t\t\t\t▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t█▀▄▀█▀▄▄▀█░▄▄▀█░▄▄▄█░▄▄▀█░▄▄▀█▄░▄█░██░█░██░▄▄▀█▄░▄██▄██▀▄▄▀█░▄▄▀█░▄▄\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t█░█▀█░██░█░██░█░█▄▀█░▀▀▄█░▀▀░██░██░██░█░██░▀▀░██░███░▄█░██░█░██░█▄▄▀\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t██▄███▄▄██▄██▄█▄▄▄▄█▄█▄▄█▄██▄██▄███▄▄▄█▄▄█▄██▄██▄██▄▄▄██▄▄██▄██▄█▄▄▄\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\n\n\n\n" +
                    ANSI_YELLOW+
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t ▄▄   ▄▄ ▄▄▄▄▄▄▄ ▄▄   ▄▄    ▄     ▄ ▄▄▄▄▄▄▄ ▄▄    ▄    ▄▄ \n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t█  █ █  █       █  █ █  █  █ █ ▄ █ █       █  █  █ █  █  █\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t█  █▄█  █   ▄   █  █ █  █  █ ██ ██ █   ▄   █   █▄█ █  █  █\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t█       █  █ █  █  █▄█  █  █       █  █ █  █       █  █  █\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t█▄     ▄█  █▄█  █       █  █       █  █▄█  █  ▄    █  █▄▄█\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t  █   █ █       █       █  █   ▄   █       █ █ █   █   ▄▄ \n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t  █▄▄▄█ █▄▄▄▄▄▄▄█▄▄▄▄▄▄▄█  █▄▄█ █▄▄█▄▄▄▄▄▄▄█▄█  █▄▄█  █▄▄█\n" + ANSI_RESET);


        }else {
            System.out.println(ANSI_PURPLE + "\t\t\t\t\t\t\t\t\t\t\t\t█▀▀ ▄▀█ █▀▄▀█ █▀▀   █▀█ █ █ █▀▀ █▀█   █\n" + "\t\t\t\t\t\t\t\t\t\t\t\t█▄█ █▀█ █ ▀ █ ██▄   █▄█ ▀▄▀ ██▄ █▀▄   ▄\n");
            System.out.println(ANSI_YELLOW + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t  >>  THE WINNER IS: " + ANSI_GREEN + winnerName + ANSI_YELLOW + "  <<" + ANSI_RESET);
        }
        printSeparee();
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

    private void printCat() {
        System.out.print(ANSI_GREEN + " █C█ " + ANSI_RESET + "║");
    }

    private void printBook() {
        System.out.print(ANSI_WHITE + " █B█ " + ANSI_RESET + "║");
    }

    private void printGame() {
        System.out.print(ANSI_YELLOW + " █G█ " + ANSI_RESET);
    }

    private void printPlant() {
        System.out.print(ANSI_PURPLE + " █P█ " + ANSI_RESET + "║");
    }

    private void printTrophies() {
        System.out.print(ANSI_CYAN + " █T█ " + ANSI_RESET + "║");
    }

    private void printFrame() {
        System.out.print(ANSI_BLUE + " █F█ " + ANSI_RESET + "║");
    }

    private void printEmpty() {
        System.out.print("     " + ANSI_RESET + "║");
    }

    private void printInvalid() {
        System.out.print(ANSI_GREY + " ░░░ " + ANSI_RESET + "║");
    }


    private void printShelves() {
        int numOfPlayers = modelView.getPlayers().size();

        for (Player p : modelView.getPlayers())
            System.out.print("\t\t\t\t\t\t" + p.getName());

        for (int nop = 0; nop < numOfPlayers; nop++)
            System.out.print("\t\t\t╔═════╦═════╦═════╦═════╦═════╗");

        System.out.print("\n");
        for (int i = 0; i < numberOfRows; i++) {
            for (Player p : modelView.getPlayers()) {
                System.out.print("\t\t\t║");
                for (int j = 0; j < numberOfColumns; j++) {
                    if (modelView.getShelfOf(p)[i][j] == null) printEmpty();
                    else {
                        switch (modelView.getShelfOf(p)[i][j].getType()) {
                            case CATS -> printCat();
                            case BOOKS -> printBook();
                            case GAMES -> printGame();
                            case PLANTS -> printPlant();
                            case TROPHIES -> printTrophies();
                            case FRAMES -> printFrame();
                        }
                    }
                }
            }
            System.out.print(" " + i + "\n");
        }
        for (int nop = 0; nop < numOfPlayers; nop++) {
            System.out.print("\t\t\t╚═════╩═════╩═════╩═════╩═════╝");
            System.out.print("\n");
            System.out.print("\t\t\t   0     1     2     3     4");
            System.out.print("\n");
        }


    }

    private void printBoard(ItemCard[][] board, boolean[][] boardValid) {

        int boardSize = 9;
        System.out.print("\t\t\t\t\t\t\t\t\t\t\t╔═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╗\n");

        for (int i = 0; i < boardSize; i++) {
            System.out.print("\t\t\t\t\t\t\t\t\t\t\t║");
            for (int j = 0; j < boardSize; j++) {
                if (!boardValid[i][j]) {
                    printInvalid();
                } else if (board[i][j] == null) printEmpty();
                else {
                    switch (board[i][j].getType()) {
                        case CATS -> printCat();
                        case BOOKS -> printBook();
                        case GAMES -> printGame();
                        case PLANTS -> printPlant();
                        case TROPHIES -> printTrophies();
                        case FRAMES -> printFrame();
                    }
                }
            }
            System.out.print(" " + i + "\n");
            if (i != boardSize - 1)
                System.out.println("\t\t\t\t\t\t\t\t\t\t\t╠═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╣");
        }
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t╚═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╝");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t   0     1     2     3     4     5     6     7     8");

    }
}
