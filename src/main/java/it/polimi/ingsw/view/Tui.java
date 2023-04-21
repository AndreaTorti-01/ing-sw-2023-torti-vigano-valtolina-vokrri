package it.polimi.ingsw.view;

import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.model.ItemCard;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Observable;
import it.polimi.ingsw.utils.Observer;

import java.util.ArrayList;
import java.util.Scanner;

public class Tui extends Observable implements Observer, Runnable {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    int playerNumber = 0;

    @Override
    public void run() {
        // Ask the names of players
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> players = new ArrayList<String>();

        //starts the game
        // Clear the console screen
        System.out.print("\033[H\033[2J");
        System.out.flush();

        printLoadingScreen();
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

        System.out.print(output.toString());
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

        System.out.print(output.toString());
    }

    private void printGameStatus(GameView gameView) {
        // Print the game status, including the main board and the shelves

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

    public void printLoadingScreen() {
        // Print the loading screen
        System.out.println(ANSI_YELLOW + "\t\t\t\t\t\t\t  >>  WELCOME TO  <<" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "\t\t\t\t\t\t\t  >>  MY SHELFIE  <<" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "\n \n \n \t\t\t\t\t\t\t  developed by gc-33" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "\n \t Torti Andrea - Valtolina Cristiano - Viganò Diego - Vokrri Fabio" + ANSI_RESET);
        System.out.print("\n\n");
        System.out.println(ANSI_PURPLE + "\t\t\t\t\t\t  >>  Press ENTER to start  <<" + ANSI_RESET);

        Scanner in = new Scanner(System.in);
        String check = in.nextLine(); //only to be sure that a key (enter) is pressed
    }

    public void printEndScreen(String winnerName) {
        // Print the end screen, showing the winner
        System.out.println(ANSI_PURPLE + "\t\t\t\t\t\t\t  >>  GAME OVER  <<");
        System.out.println(ANSI_YELLOW + "\t\t\t\t\t\t  >>  WINNER: " + ANSI_GREEN + winnerName + ANSI_YELLOW + "  <<" + ANSI_RESET);

        System.out.print("\n\n");
        System.out.println(ANSI_PURPLE + "\t\t\t\t\t\t  >>  Press ENTER to restart  <<" + ANSI_RESET);

        Scanner in = new Scanner(System.in);
        String check = in.nextLine(); //only to be sure that a key (enter) is pressed
    }

    void printError(String error) {
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
