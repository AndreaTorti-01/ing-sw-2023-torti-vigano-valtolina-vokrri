package it.polimi.ingsw.view;

import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.utils.Observable;
import it.polimi.ingsw.utils.Observer;

import java.util.ArrayList;
import java.util.Scanner;

import java.util.Scanner;

public class Tui extends Observable<String> implements Observer<GameView, String>, Runnable {
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
        ArrayList<String> players= new ArrayList<String>();

        //starts the game
        // Clear the console screen
        System.out.print("\033[H\033[2J");
        System.out.flush();

        players = askNames();

        while (true) {
            // Game loop
        }
    }

    private void printBoard() {
        // Print the board
    }
    private void printShelf() {
        // Print the shelf
    }
    private void printGameStatus(){
        // Print the game status, including the main board and the shelves
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
        setChanged();
        notifyObservers("name " + name);

        int i = 2;
        boolean done = false;

        while(!done && i <= 4){
            //ask for other players

            System.out.print("[Player " + i + " ]:  ");
            name = askPlayerName();
            setChanged();
            notifyObservers("name " + name);

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

    private boolean askBoolean(){
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
    public void update(GameView o, String arg) {

    }
}
