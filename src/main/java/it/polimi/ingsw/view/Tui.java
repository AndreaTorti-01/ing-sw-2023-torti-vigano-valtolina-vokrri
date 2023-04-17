package it.polimi.ingsw.view;

import it.polimi.ingsw.messages.AddPlayerMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.TurnPlayedMessage;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.model.ItemCard;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Observable;
import it.polimi.ingsw.utils.Observer;

import java.util.ArrayList;
import java.util.Scanner;

public class Tui extends Observable<Message> implements Observer<GameView, Message>, Runnable {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    @Override
    public void run() {
        // Ask the names of players
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> players = new ArrayList<String>();

        //starts the game
        // Clear the console screen
        System.out.print("\033[H\033[2J");
        System.out.flush();


    }

    private void printBoard(ItemCard[][] board, boolean[][] boardValid) {
        String printString = "";

        printString += "---------------------\n";
        for (int i = 0; i < Constants.boardSize; i++) {
            printString += "| ";
            for (int j = 0; j < Constants.boardSize; j++) {
                if (boardValid[i][j]) {
                    ItemCard card = board[i][j];
                    if (card != null)
                        printString += card + " ";
                    else
                        printString += "* ";
                } else {
                    printString += "- ";
                }

            }
            printString += "|\n";
        }
        printString += "---------------------";

        System.out.print(printString);
    }

    private void printShelf(ItemCard[][] shelfOf) {
        String printString = "";

        for (int i = 0; i < Constants.numberOfRows; i++) {
            printString += "| ";
            for (int j = 0; j < Constants.numberOfColumns; j++) {
                if (shelfOf[i][j] != null) printString += shelfOf[i][j].toString() + " ";
                else printString += "* ";
            }
            printString += "|\n";
        }
        printString += "------------";

        System.out.print(printString);
    }

    private void printGameStatus(GameView gw) {
        // Print the game status, including the main board and the shelves

        // Print the active player name
        System.out.println(ANSI_PURPLE + "\t\t\t\t\t  >>  " + ANSI_GREEN + gw.getCurrentPlayer().getName() + ANSI_PURPLE + "  <<" + ANSI_RESET);

        // Print the board
        printBoard(gw.getBoard(), gw.getBoardValid());

        // Print the shelf of the active player
        printShelf(gw.getShelfOf(gw.getCurrentPlayer()));

        // Print the bag status
        System.out.println(ANSI_PURPLE + "\t\t\t\t\t  >>  " + ANSI_GREEN + "BAG: " + ANSI_YELLOW + gw.isBagEmpty() + ANSI_PURPLE + "  <<" + ANSI_RESET);

        // Print the common goal cards type and points on top of the stack
        for (int i = 0; i < gw.getCommonGoalCards().size(); i++) {
            System.out.println(ANSI_PURPLE + "\t\t\t\t\t  >>  " + ANSI_GREEN + "GOAL " + i + ": " + ANSI_YELLOW + gw.getCommonGoalCards().get(i).getType() + " " + gw.getCommonGoalCards().get(i).peekPoints() + ANSI_PURPLE + "  <<" + ANSI_RESET);
        }

        // Print the personal goal card pattern
        System.out.println(ANSI_PURPLE + "\t\t\t\t\t  >>  " + ANSI_GREEN + "GOAL: " + ANSI_YELLOW + gw.getCurrentPlayer().getPersonalGoalCard() + ANSI_PURPLE + "  <<" + ANSI_RESET);
    }

    private void printLoadingScreen() {
        // Print the loading screen, asking for player names etc...
    }

    private void printEndScreen(String winnerName) {
        // Print the end screen, showing the winner
        System.out.println(ANSI_PURPLE + "\t\t\t\t\t  >>  GAME OVER  <<");
        System.out.println(ANSI_YELLOW + "\t\t\t\t\t  >>  WINNER: " + ANSI_GREEN + winnerName + ANSI_YELLOW + "  <<" + ANSI_RESET);
    }

    private void printError(String error) {
        // Print an error message
        System.out.println(ANSI_RED + error + ANSI_RESET);
    }

    private ArrayList<String> askNames() {
        // Ask the names of the players
        String name;
        ArrayList<String> players = new ArrayList<String>();


        System.out.println("Insert Player Names:");

        System.out.print("[Player 1]:  ");
        name = askPlayerName();
        setChangedAndNotifyObservers(new AddPlayerMessage(name));

        int i = 2;
        boolean done = false;

        while (!done && i <= 4) {
            //ask for other players

            System.out.print("[Player " + i + " ]:  ");
            name = askPlayerName();
            setChanged();
            notifyObservers(new AddPlayerMessage(name));

            System.out.print("Do you want to add another player?");
            done = !askBoolean();
            i = i + 1;
        }
        // no number of player required
        return players;
    }

    private String askPlayerName() {
        // Ask the name of the player
        Scanner in = new Scanner(System.in);
        System.out.println("  >>  Enter name:");
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

    @Override
    public void update(GameView gw, Message msg) {
        if (msg instanceof TurnPlayedMessage) {
            // Update the game view
            // Print the game status
            printGameStatus(gw);
        }
    }

    private void setChangedAndNotifyObservers(Message msg) {
        setChanged();
        notifyObservers(msg);
    }
}
