package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.serializable.GameViewMsg;
import it.polimi.ingsw.network.serializable.MoveMsg;
import it.polimi.ingsw.utils.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.utils.Constants.*;
import static it.polimi.ingsw.utils.Constants.isTakeable;

public class Tui_Max extends Observable implements RunnableView {
    Game.Status gameStatus = Game.Status.WAITING;
    private String playerName;
    private Player me;
    private GameViewMsg modelView;
    private boolean iAmLobbyLeader = false;

    public Tui_Max() {
        System.err.println("warning: created non observable tui");
    }

    public Tui_Max(Client client) {
        this.addObserver(client);
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        /*
         * these functions automatically call the notifyObservers method with their arguments
         * askPlayerName() returns a string, saved as a class variable to be used in the future to check
         * 1) if it's my turn
         * 2) if i'm the lobby leader
         * askPlayerNumber() can already handle non-integer format exceptions
         * the player number is notified to observers to automatically start the game if the lobby reaches the requested number of players+
         */
        playerName = askPlayerName();
        // wait for 0.5 seconds
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (iAmLobbyLeader) askNumberOfPlayers();

        clearConsole();

        while (modelView == null) {
            printWaitingScreen();
        }

        // while true loop
        // waits for user input, if it is user turn
        // sends input to Client
        //noinspection InfiniteLoopStatement
        while (true) {

            if (modelView.getGameStatus() == Game.Status.WAITING) {
                printWaitingScreen();
                //doesn't return until the game is still in waiting status

            } else if (modelView.getGameStatus() == Game.Status.STARTED) {
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
        if (playerName.equals(modelView.getPlayers().get(0).getName()))
            iAmLobbyLeader = true;
        System.err.println("updated view!");
    }

    /**
     * Public method used to run internal private functions for testing purposes
     */
    private void ScreenOutTest() {
        System.out.println("ScreenOutTest");
        printEndScreen("Fabio");

        //printWaitingScreen();
        clearConsole();
    }

    private void waitForCommands() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        boolean validCommand = false;

        //compared the textual input with the possible commands
        for (Command command : Command.values()) {
            if (input.equals(command.getCommandName())) {
                validCommand = true;
                break;
            }
        }
        if (!validCommand)
            printError("Invalid command");
        else
            handleCommand(input);
    }

    private void handleCommand(String input) {
        //TODO handle the command types
    }

    private void askNumberOfPlayers() {
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
    }

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
            if (pickedNum < maxCards && pickedNum != 0) {
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
        System.out.println(ANSI_YELLOW +
                "\n\n" +
                "\t\t\t\t╔══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╦══╗\n" +
                "\t\t\t\t╚══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╩══╝" +
                ANSI_RESET + "\n\n");
    }

    private void printWaitingScreen() {
        int iter = 0;


        //clear the console screen
        clearConsole();


        System.out.println("\n" + ANSI_YELLOW +

                "\t\t\t\t\t\t\t\t\t\t\t\t█   █ █▀▀ █   █▀▀ █▀▀█ █▀▄▀█ █▀▀ 　 ▀▀█▀▀ █▀▀█\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t█ █ █ █▀▀ █   █   █  █ █ ▀ █ █▀▀ 　   █   █  █\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t█▄▀▄█ ▀▀▀ ▀▀▀ ▀▀▀ ▀▀▀▀ ▀   ▀ ▀▀▀ 　   █   ▀▀▀▀\n\n\n");

        printMyShelfie();

        System.out.println("\n\n\n" + ANSI_CYAN + "\n \n \n \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t  developed by gc-33" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "\n \t\t\t\t\t\t\t\t\t Torti Andrea - Valtolina Cristiano - Viganò Diego - Vokrri Fabio" + ANSI_RESET);

        System.out.print("""



                \t\t\t\t█   █ █▀▀█ ▀ ▀▀█▀▀ ▀ █▀▀▄ █▀▀▀ 　 █▀▀ █▀▀█ █▀▀█ 　 █▀▀█ ▀▀█▀▀ █  █ █▀▀ █▀▀█ 　 █▀▀█ █   █▀▀█ █  █ █▀▀ █▀▀█ █▀▀
                \t\t\t\t█ █ █ █▄▄█ █   █   █ █  █ █ ▀█ 　 █▀▀ █  █ █▄▄▀ 　 █  █   █   █▀▀█ █▀▀ █▄▄▀ 　 █  █ █   █▄▄█ █▄▄█ █▀▀ █▄▄▀ ▀▀█
                \t\t\t\t█▄▀▄█ ▀  ▀ ▀   ▀   ▀ ▀  ▀ ▀▀▀▀ 　 ▀   ▀▀▀▀ ▀ ▀▀ 　 ▀▀▀▀   ▀   ▀  ▀ ▀▀▀ ▀ ▀▀ 　 █▀▀▀ ▀▀▀ ▀  ▀ ▄▄▄█ ▀▀▀ ▀ ▀▀ ▀▀▀""");
        while (this.gameStatus.equals(Game.Status.WAITING)) {
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
        if (playerName.equals(winnerName)) {
            System.out.println(ANSI_PURPLE +
                    "\t\t\t\t\t\t\t\t\t\t\t\t▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t█▀▄▀█▀▄▄▀█░▄▄▀█░▄▄▄█░▄▄▀█░▄▄▀█▄░▄█░██░█░██░▄▄▀█▄░▄██▄██▀▄▄▀█░▄▄▀█░▄▄\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t█░█▀█░██░█░██░█░█▄▀█░▀▀▄█░▀▀░██░██░██░█░██░▀▀░██░███░▄█░██░█░██░█▄▄▀\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t██▄███▄▄██▄██▄█▄▄▄▄█▄█▄▄█▄██▄██▄███▄▄▄█▄▄█▄██▄██▄██▄▄▄██▄▄██▄██▄█▄▄▄\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\n\n\n\n" +
                    ANSI_YELLOW +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t ▄▄   ▄▄ ▄▄▄▄▄▄▄ ▄▄   ▄▄    ▄     ▄ ▄▄▄▄▄▄▄ ▄▄    ▄    ▄▄ \n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t█  █ █  █       █  █ █  █  █ █ ▄ █ █       █  █  █ █  █  █\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t█  █▄█  █   ▄   █  █ █  █  █ ██ ██ █   ▄   █   █▄█ █  █  █\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t█       █  █ █  █  █▄█  █  █       █  █ █  █       █  █  █\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t█▄     ▄█  █▄█  █       █  █       █  █▄█  █  ▄    █  █▄▄█\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t  █   █ █       █       █  █   ▄   █       █ █ █   █   ▄▄ \n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t  █▄▄▄█ █▄▄▄▄▄▄▄█▄▄▄▄▄▄▄█  █▄▄█ █▄▄█▄▄▄▄▄▄▄█▄█  █▄▄█  █▄▄█\n" + ANSI_RESET);


        } else {
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

    private void printCat() {
        System.out.print(" " + ANSI_GREEN_BACKGROUND + " C " + ANSI_RESET + " ║");
    }

    private void printBook() {
        System.out.print(" " + ANSI_WHITE_BACKGROUND + " B " + ANSI_RESET + " ║");
    }

    private void printGame() {
        System.out.print(" " + ANSI_YELLOW_BACKGROUND + " G " + ANSI_RESET + " ║");
    }

    private void printPlant() {
        System.out.print(" " + ANSI_PURPLE_BACKGROUND + " P " + ANSI_RESET + " ║");
    }

    private void printTrophies() {
        System.out.print(" " + ANSI_CYAN_BACKGROUND + " T " + ANSI_RESET + " ║");
    }

    private void printFrame() {
        System.out.print(" " + ANSI_BLUE_BACKGROUND + " F " + ANSI_RESET + " ║");
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
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t╔═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╗");

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


    public enum Command {
        CHAT("/chat"),
        PRIVATECHAT("/privatechat"),
        QUIT("/quit"),
        HELP("/help");


        private final String commandName;

        Command(String identifier) {
            this.commandName = identifier;
        }

        public String getCommandName() {
            return this.commandName;
        }

        public void printList() {
            System.out.println("List of commands:");
            for (Tui.Command command : Tui.Command.values()) {
                printCommandInfo(command);
            }
        }

        public void printCommandInfo(Tui.Command command) {
            switch (command) {
                case CHAT -> System.out.println("Type /chat <message> to send a message to the other players");
                case PRIVATECHAT ->
                        System.out.println("Type /privatechat <player> <message> to send a private message to a player");
                case QUIT -> System.out.println("Type /quit to quit the game");
                case HELP -> System.out.println("Type /help to see the list of commands");

            }
        }
    }
}
