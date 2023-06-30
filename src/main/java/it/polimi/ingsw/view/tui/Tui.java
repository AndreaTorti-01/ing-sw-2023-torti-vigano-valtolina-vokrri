package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.client.ClientImpl;
import it.polimi.ingsw.network.serializable.ChatMsg;
import it.polimi.ingsw.network.serializable.CheatMsg;
import it.polimi.ingsw.network.serializable.GameViewMsg;
import it.polimi.ingsw.network.serializable.MoveMsg;
import it.polimi.ingsw.utils.ObservableImpl;
import it.polimi.ingsw.view.RunnableView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.utils.Common.isTakeable;
import static it.polimi.ingsw.utils.Constants.*;
import static it.polimi.ingsw.view.tui.TerminalPrintables.*;

/**
 * The TUI view of the game.
 */
public class Tui extends ObservableImpl implements RunnableView {
    /**
     * A lock to synchronize all the methods that change the state of this view.
     */
    private final Object lock = new Object();
    /**
     * The scanner used to read the input.
     */
    private final Scanner scanner = new Scanner(System.in);
    /**
     * Whether the player gave the number of players that will play the game or not.
     */
    boolean gaveNumber;
    /**
     * The name of the player.
     */
    private String playerName = "";
    /**
     * The model view of the game.
     */
    private GameViewMsg modelView;
    /**
     * The state of the view.
     */
    private State state = State.ASK_NAME;
    /**
     * Whether the view is running or not.
     */
    private volatile boolean running;

    /**
     * Creates a new Textual User Interface view associated with the provided client.
     *
     * @param client the client associated with this view.
     */
    public Tui(Client client) {
        this.addObserver((ClientImpl) client);
    }

    /**
     * Gets the state of the view.
     *
     * @return the state of the view.
     */
    private State getState() {
        synchronized (lock) {
            return state;
        }
    }

    /**
     * Sets the state of the view to the provided one.
     *
     * @param state the new state of the view.
     */
    private void setState(State state) {
        synchronized (lock) {
            this.state = state;
            System.err.println("state changed to " + state);
            lock.notifyAll();
        }
    }

    /**
     * The main game loop.
     */
    @Override
    public void run() {

        while (getState() == Tui.State.ASK_NAME) {
            synchronized (lock) {
                try {
                    new Thread(this::askPlayerName).start();
                    lock.wait();
                } catch (InterruptedException e) {
                    System.err.println("Interrupted while waiting for server: " + e.getMessage());
                }
            }
        }

        while (getState() == Tui.State.ASK_NUMBER) {
            synchronized (lock) {
                try {
                    new Thread(this::askPlayerNumber).start();
                    lock.wait();
                } catch (InterruptedException e) {
                    System.err.println("Interrupted while waiting for server: " + e.getMessage());
                }
            }
        }

        while (getState() == Tui.State.WAITING_FOR_PLAYERS) {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    System.err.println("Interrupted while waiting for server: " + e.getMessage());
                }
            }
        }

        // create a thread passing the function that will handle the input
        running = true;
        Thread inputHandler = new Thread(this::acceptInput);
        inputHandler.start();

        while (!(getState() == Tui.State.ENDED)) {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    System.err.println("Interrupted while waiting for server: " + e.getMessage());
                }
            }
        }

        // wait for the input handler to terminate and close the game
        System.out.println("Press enter to close the game");
        running = false;
        try {
            inputHandler.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.exit(0);
    }

    /**
     * Updates the view with the provided model state.
     *
     * @param modelView the model view that contains a representation of the model state.
     */
    @Override
    public void updateView(GameViewMsg modelView) {
        this.modelView = modelView;

        // check the name input is valid
        if (getState() == Tui.State.ASK_NAME && !playerName.equals("") && modelView.getNameError()) {
            this.playerName = "";
            setState(Tui.State.ASK_NAME);
        }

        // the game is waiting for players
        if (!playerName.equals("") && modelView.getGameStatus().equals(Game.Status.WAITING)) {
            if (playerName.equals(modelView.getPlayers().get(0).getName()) && !gaveNumber) {
                setState(Tui.State.ASK_NUMBER); // I am lobby leader
            } else setState(Tui.State.WAITING_FOR_PLAYERS); // I am not lobby leader
        }
        // the game has started
        else if (modelView.getGameStatus().equals(Game.Status.STARTED)) {
            if (modelView.getCurrentPlayer().getName().equals(this.playerName))
                setState(Tui.State.PLAY); // it's my turn
            else setState(Tui.State.WAITING_FOR_TURN); // it's not my turn
            printGameStatus();
            if (getState() == Tui.State.PLAY) {
                System.out.println("It's your turn!");
            }
        }
        // the game ends
        else if (modelView.getGameStatus().equals(Game.Status.ENDED)) {
            printEndScreen(modelView.getWinner().getName());
            setState(Tui.State.ENDED);
        }
    }

    /**
     * Accepts input from the user and notifies the observers with the chosen message.
     */
    private void acceptInput() {
        while (running) {
            // scan for command
            String line = scanLine();
            // check if running
            if (!running) break;
            // check if it's a command
            if (line.charAt(0) == '/') {
                String command = line.split(" ")[0];

                // check which command it is
                switch (command) {
                    case "/chat" -> {
                        // check that it has 2 arguments
                        if (line.split(" ").length >= 2) {
                            String chatMessage = line.split(" ", 2)[1];
                            notifyObservers(new ChatMsg(null, playerName, true, chatMessage));
                        } else {
                            System.out.println("Invalid arguments: Type /chat <message> to send a message to all players");
                        }
                    }

                    case "/privatechat" -> {
                        // check that it has 3 arguments
                        if (line.split(" ").length >= 3) {
                            String destPlayer = line.split(" ")[1];
                            List<String> playerNames = new ArrayList<>();
                            for (Player player : modelView.getPlayers()) {
                                playerNames.add(player.getName());
                            }

                            // check that the dest player exists and is not the player himself
                            if (playerNames.contains(destPlayer) && !destPlayer.equals(playerName)) {
                                String chatMessage = line.split(" ", 3)[2];
                                notifyObservers(new ChatMsg(destPlayer, playerName, false, "[private]" + chatMessage));
                            } else if (destPlayer.equals(playerName)) {
                                System.out.println("You can't send a private message to yourself, touch some grass instead :)");
                            } else {
                                System.out.println("Invalid player name");
                            }
                        } else {
                            System.out.println("Invalid arguments: Type /privatechat <player> <message> to send a private message to a player");
                        }
                    }

                    case "/pick" -> {
                        // check if it's the player's turn
                        if (state.equals(Tui.State.PLAY)) {
                            pickCards();
                        } else System.out.println("Picking cards is not an option right now.");
                    }

                    case "/cheat" -> {
                        if (modelView.getCurrentPlayer().getName().equals(playerName)) {
                            notifyObservers(new CheatMsg(playerName));
                        } else System.out.println("You can't cheat if it's not your turn!");
                    }

                    case "/help" -> {
                        System.out.println("Type /chat <message> to send a message to all players");
                        System.out.println("Type /privatechat <player> <message> to send a private message to a player");
                        System.out.println("Type /pick to start the card picking process");
                        System.out.println("Type /help to see this list again");
                    }

                    default -> System.out.println("Invalid command: type /help for a list of commands");
                }
            } else System.out.println("Invalid command: type /help for a list of commands");
        }
    }

    /**
     * Asks the player the number of players that will play the game.
     */
    private void askPlayerNumber() {
        int playerNumber = 0;
        boolean valid = false;
        while (!valid) {
            System.out.println("How many players are playing? (2-4)");
            try {
                playerNumber = Integer.parseInt(scanLine());
                if (playerNumber >= 2 && playerNumber <= 4) valid = true;
                else printError("Invalid number of players");
            } catch (NumberFormatException e) {
                printError("Invalid number or non-numeric input");
            }
        }
        gaveNumber = true;
        notifyObservers(playerNumber);
        printWaitingForPlayers();
    }

    /**
     * Asks the player to pick the cards from the board.
     * The cards will be in the exact order.
     * Notifies the observers of the picked cards.
     */
    private void pickCards() {
        List<List<Integer>> pickedCoords = new ArrayList<>();

        System.out.println("You can pick cards from the board");
        int pickedNum = 0; //number of already picked cards
        boolean validChoice = false;
        int shelfCol = 0; //column of the shelf where the player is moving cards to
        int maxCards = 0; //maximum cards that can be picked
        int[] freeSlotsNumber = new int[numberOfColumns]; //number of max cards that can be inserted in each column
        List<ItemCard> pickedCards = new ArrayList<>(); //list of picked cards

        Player me = null;
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
            boolean valid = false;
            int row = 0;
            while (!valid)
                try {
                    row = Integer.parseInt(scanLine());
                    if (row >= 0 && row < numberOfBoardRows) valid = true;
                    else printError("Invalid number");
                } catch (NumberFormatException e) {
                    printError("Invalid number or non-numeric input");
                }

            System.out.print("Card " + (pickedNum + 1) + "-> enter COLUMN number:  ");
            valid = false;
            int column = 0;
            while (!valid)
                try {
                    column = Integer.parseInt(scanLine());
                    if (column >= 0 && column < numberOfBoardColumns) valid = true;
                    else printError("Invalid number");
                } catch (NumberFormatException e) {
                    printError("Invalid number or non-numeric input");
                }

            //checking coordinate validity
            if (isTakeable(modelView, row, column, pickedCoords)) {
                List<Integer> coords = new ArrayList<>();
                coords.add(row);
                coords.add(column);
                pickedCoords.add(coords);
                pickedCards.add(modelView.getBoard()[row][column]);
                pickedNum++;
            } else printError("Invalid coordinates!, retry");

            //ask if the player wants to pick another card
            if (pickedNum < maxCards && pickedNum != 0) {
                System.out.println("Do you want to pick another card?");
                if (!askBoolean()) break;
            }

        }

        System.out.println("These are the cards you picked: ");
        printPickedCards(pickedCards);
        System.out.println("Chose the the order: 1 - 2 - 3");

        int orderId = 0; // not redundant
        List<List<Integer>> tmp = new ArrayList<>();
        List<Integer> used = new ArrayList<>();
        while (tmp.size() < pickedCoords.size()) {

            do {
                try {
                    orderId = Integer.parseInt(scanLine());
                } catch (NumberFormatException e) {
                    printError("Invalid number or non-numeric input");
                }
            } while ((orderId < 1 || orderId > pickedCoords.size()));

            if (!used.contains(orderId)) {
                used.add(orderId);
                tmp.add(pickedCoords.get(orderId - 1));
            } else
                printError("You already used this number! retry");
        }
        pickedCoords = tmp;

        System.out.println("Chose a shelf column to move the cards to: ");
        while (!validChoice) {
            try {
                shelfCol = scanner.nextInt();
                if (shelfCol < 0 || shelfCol >= numberOfColumns) printError("Invalid column! retry");
                else if (freeSlotsNumber[shelfCol] < pickedNum) printError("Not enough space! retry");
                else validChoice = true;
            } catch (NumberFormatException e) {
                printError("Invalid number or non-numeric input");
            }

        }

        //notifying observers
        notifyObservers(new MoveMsg(pickedCoords, shelfCol));
    }

    /**
     * Prints the cards that the player picked.
     *
     * @param pickedCards the cards picked by the player.
     */
    private void printPickedCards(List<ItemCard> pickedCards) {
        for (ItemCard card : pickedCards) {
            switch (card.getType()) {
                case CATS -> printCat();
                case BOOKS -> printBook();
                case GAMES -> printGame();
                case PLANTS -> printPlant();
                case TROPHIES -> printTrophies();
                case FRAMES -> printFrame();
            }
            System.out.print("    ");
        }
    }

    /**
     * Prints the current Game state.
     */
    private void printGameStatus() {
        // Prints the title, boards and shelves
        clearConsole();
        printSeparee();
        printMyShelfie();
        printBoard(modelView.getBoard(), modelView.getLayout());
        System.out.println("\n");
        printShelves();
        printCommonGoalCards();
        printPersonalGoalCards();
        printScores();
        printChat();
        printSeparee();
    }

    /**
     * Prints the Common Goal Cards.
     */
    private void printCommonGoalCards() {
        System.out.println("Common goal cards: ");
        for (int i = 0; i < modelView.getCommonGoalCards().size(); i++) {
            System.out.print((i + 1) + ": " + modelView.getCommonGoalCards().get(i).getType().toString() + "    " + "    " + "    " + "");
        }
        System.out.println();
    }

    /**
     * Prints the Personal Goal Cards.
     */
    private void printPersonalGoalCards() {
        System.out.println("Personal goal cards: ");

        for (Player player : modelView.getPlayers()) {
            if (player.getName().equals(playerName)) {
                StringBuilder output = new StringBuilder();

            }
        }

        Player playingPlayer = null;
        for (Player p : modelView.getPlayers()) {
            if (p.getName().equals(playerName)) playingPlayer = p;
        }

        if (playingPlayer != null) {
            System.out.print("    " + "    " + "    " + "╔═════╦═════╦═════╦═════╦═════╗");
            System.out.print("\n");

            for (int i = 0; i < numberOfRows; i++) {
                System.out.print("    " + "    " + "    " + "║");
                for (int j = 0; j < numberOfColumns; j++) {
                    if (playingPlayer.getPersonalGoalCard().getPattern()[i][j] == null) printEmpty();
                    else {
                        switch (playingPlayer.getPersonalGoalCard().getPattern()[i][j]) {
                            case CATS -> printCat();
                            case BOOKS -> printBook();
                            case GAMES -> printGame();
                            case PLANTS -> printPlant();
                            case TROPHIES -> printTrophies();
                            case FRAMES -> printFrame();
                        }
                    }
                }
                System.out.print(" " + i);
                System.out.print("\n");
                if (i != numberOfRows - 1)
                    System.out.println("    " + "    " + "    " + "╠═════╬═════╬═════╬═════╬═════╣");
            }
            System.out.print("    " + "    " + "    " + "╚═════╩═════╩═════╩═════╩═════╝");
            System.out.print("\n");
            System.out.print("    " + "    " + "    " + "   0     1     2     3     4  ");
            System.out.println();
        } else System.err.println("Error: player not found");
    }

    /**
     * Prints the chat messages.
     */
    private void printChat() {
        if (modelView.getMessages().size() == 0) return;
        System.out.println("Chat: ");
        for (ChatMsg message : modelView.getMessages()) {
            if (message.isPublic() || !message.isPublic() && (message.getRecipientPlayer().equals(playerName) || message.getSenderPlayer().equals(playerName)))
                System.out.println(ANSI_BLUE + message.getSenderPlayer() + ": " + ANSI_RESET + ANSI_GREY + message.getMessage() + ANSI_RESET);
        }
    }

    /**
     * Prints the end screen with the provided winner name.
     *
     * @param winnerName the name of the winner.
     */
    private void printEndScreen(String winnerName) {
        printSeparee();
        if (modelView.getWinner().getName().equals(playerName)) printWon();
        else printLost();
        System.out.println("The winner is " + winnerName + "!");
        printScores();
        printSeparee();
    }

    /**
     * Asks the player's name.
     */
    private void askPlayerName() {
        // Ask the name of the player
        System.out.println("  >>  Enter your name:  ");
        this.playerName = scanLine();
        notifyObservers(playerName);
    }

    /**
     * Asks the player a true/false question.
     *
     * @return true if the player answered "y", false otherwise.
     */
    private boolean askBoolean() {
        System.out.print("  >>  (y/n) ");

        while (true) {
            String input = scanLine();
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

    /**
     * Prints the shelves of every player.
     */
    private void printShelves() {
        int numOfPlayers = modelView.getPlayers().size();

        for (Player p : modelView.getPlayers()) {
            System.out.print("    " + "    " + "    " + "" + p.getName());
            for (int spaceCounter = 0; spaceCounter < 31 - p.getName().length(); spaceCounter++) System.out.print(" ");
            System.out.print("  ");
        }
        System.out.print("\n");

        for (int nop = 0; nop < numOfPlayers; nop++) {
            System.out.print("    " + "    " + "    " + "╔═════╦═════╦═════╦═════╦═════╗  ");
        }
        System.out.print("\n");

        for (int i = 0; i < numberOfRows; i++) {
            for (Player p : modelView.getPlayers()) {
                System.out.print("    " + "    " + "    " + "║");
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
                System.out.print(" " + i);
            }
            System.out.print("\n");
            if (i != numberOfRows - 1) {
                for (Player p : modelView.getPlayers())
                    if (i != numberOfRows - 1)
                        System.out.print("    " + "    " + "    " + "╠═════╬═════╬═════╬═════╬═════╣  ");
                System.out.print("\n");
            }
        }
        for (int nop = 0; nop < numOfPlayers; nop++)
            System.out.print("    " + "    " + "    " + "╚═════╩═════╩═════╩═════╩═════╝  ");
        System.out.print("\n");
        for (int nop = 0; nop < numOfPlayers; nop++)
            System.out.print("    " + "    " + "    " + "   0     1     2     3     4");
        System.out.print("\n");
    }

    /**
     * Prints the scores of every player.
     */
    private void printScores() {
        System.out.println("Scores:");
        for (Player p : modelView.getPlayers()) {
            if (modelView.getPlayers().indexOf(p) != modelView.getPlayers().size() - 1)
                System.out.print("    " + "    " + "╠════> " + p.getName() + ": " + p.getScore() + "    " + "");
            else
                System.out.print("    " + "    " + "╚════> " + p.getName() + ": " + p.getScore() + "    " + "");

            for (int i = 0; i < p.getScore(); i++)
                System.out.print("#");
            System.out.println();
        }
    }

    /**
     * Prints the current board.
     *
     * @param board  the board to be printed.
     * @param layout the board layout.
     */
    private void printBoard(ItemCard[][] board, boolean[][] layout) {

        int boardSize = 9;
        System.out.println("    " + "    " + "    " + "    " + "    " + "    " + "    " + "    " + "    " + "    " + "    " + "╔═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╗");

        for (int i = 0; i < boardSize; i++) {
            System.out.print("    " + "    " + "    " + "    " + "    " + "    " + "    " + "    " + "    " + "    " + "    " + "║");
            for (int j = 0; j < boardSize; j++) {
                if (!layout[i][j]) {
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
                System.out.println("    " + "    " + "    " + "    " + "    " + "    " + "    " + "    " + "    " + "    " + "    " + "╠═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╣");
        }
        System.out.println("    " + "    " + "    " + "    " + "    " + "    " + "    " + "    " + "    " + "    " + "    " + "╚═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╝");
        System.out.println("    " + "    " + "    " + "    " + "    " + "    " + "    " + "    " + "    " + "    " + "    " + "   0     1     2     3     4     5     6     7     8");
    }

    /**
     * Prints the provided error.
     *
     * @param error the error to be printed.
     */
    private void printError(String error) {
        // Print an error message
        System.out.println(ANSI_PURPLE + error + ANSI_RESET);
    }

    /**
     * Scans the input of the user.
     *
     * @return the input provided by the user.
     */
    private String scanLine() {
        String ret;
        do {
            ret = scanner.nextLine();
        } while (ret.equals("") && running);
        return ret;
    }

    /**
     * The View's possible states.
     */
    private enum State {
        ASK_NAME, ASK_NUMBER, WAITING_FOR_PLAYERS, WAITING_FOR_TURN, PLAY, ENDED
    }
}
