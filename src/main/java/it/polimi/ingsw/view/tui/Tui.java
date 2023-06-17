package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.client.ClientImpl;
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

public class Tui extends ObservableImpl implements RunnableView {
    private final Object lock = new Object();
    private final Scanner scanner = new Scanner(System.in);
    boolean gaveNumber;
    private String playerName = "";
    private GameViewMsg modelView;
    private State state = State.ASK_NAME;

    public Tui(Client client) {
        this.addObserver((ClientImpl) client);
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

        // asks the player for his name
        printWelcomeScreen();
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
            gaveNumber = true;
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
                printGameStatus(); // TODO complete
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        System.err.println("Interrupted while waiting for server: " + e.getMessage());
                    }
                }
            }

            while (getState() == State.PLAY) {
                printGameStatus(); // TODO complete
                pickCards();
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
        if (!playerName.equals("") && modelView.getGameStatus().equals(Game.Status.WAITING)) {
            if (playerName.equals(modelView.getPlayers().get(0).getName()) && !gaveNumber)
                setState(State.ASK_NUMBER); // I am lobby leader
            else
                setState(State.WAITING_FOR_PLAYERS); // I am not lobby leader
        }
        // the game has started
        else if (modelView.getGameStatus().equals(Game.Status.STARTED)) {
            if (modelView.getCurrentPlayer().getName().equals(this.playerName)) {
                setState(State.PLAY); // it's my turn
            } else setState(State.WAITING_FOR_TURN); // it's not my turn
        }
    }

    /**
     * takes care of notifying observers
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
        printWaitingForPlayers();
        notifyObservers(playerNumber);
    }

    /**
     * takes care of notifying observer
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
                orderId = scanner.nextInt();
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
            System.out.print("\t");
        }
    }

    private void printGameStatus() {
        // Prints the title, boards and shelves
        clearConsole();
        printSeparee();
        printMyShelfie();
        printSeparee();
        printBoard(modelView.getBoard(), modelView.getBoardValid());
        System.out.println("\n");
        printShelves();
        printSeparee();
        printCommonGoalCards();
        System.out.println();
        printPersonalGoalCards();
        printSeparee();
        printScores();
        printSeparee();
    }

    private void printCommonGoalCards() {
        System.out.println("Common goal cards: ");
        for (int i = 0; i < modelView.getCommonGoalCards().size(); i++) {
            System.out.print((i + 1) + ": " + modelView.getCommonGoalCards().get(i).getType().toString() + "\t\t\t");
        }
        System.out.println();
    }

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
            System.out.print("\t\t\t╔═════╦═════╦═════╦═════╦═════╗");
            System.out.print("\n");

            for (int i = 0; i < numberOfRows; i++) {
                System.out.print("\t\t\t║");
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
                    System.out.println("\t\t\t╠═════╬═════╬═════╬═════╬═════╣");
            }
            System.out.print("\t\t\t╚═════╩═════╩═════╩═════╩═════╝");
            System.out.print("\n");
            System.out.print("\t\t\t   0     1     2     3     4  ");
            System.out.println();
        } else System.err.println("Error: player not found");
    }

    private void printEndScreen(String winnerName) {
        System.out.println("The winner is " + winnerName + "!");
        printScores();
    }

    /**
     * takes care of notifying observers
     *
     * @return the name of the player
     */
    private String askPlayerName() {
        // Ask the name of the player
        System.out.println("  >>  Enter your name:  ");
        String name = scanLine();
        notifyObservers(name);
        return name;
    }

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

    private void printShelves() {
        int numOfPlayers = modelView.getPlayers().size();

        for (Player p : modelView.getPlayers())
            System.out.print("\t\t\t\t\t\t\t\t\t\t" + p.getName());
        System.out.print("\n");

        for (int nop = 0; nop < numOfPlayers; nop++)
            System.out.print("\t\t\t╔═════╦═════╦═════╦═════╦═════╗  ");
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
                System.out.print(" " + i);
            }
            System.out.print("\n");
            if (i != numberOfRows - 1) {
                for (Player p : modelView.getPlayers())
                    if (i != numberOfRows - 1)
                        System.out.print("\t\t\t╠═════╬═════╬═════╬═════╬═════╣  ");
                System.out.print("\n");
            }
        }
        for (int nop = 0; nop < numOfPlayers; nop++)
            System.out.print("\t\t\t╚═════╩═════╩═════╩═════╩═════╝  ");
        System.out.print("\n");
        for (int nop = 0; nop < numOfPlayers; nop++)
            System.out.print("\t\t\t   0     1     2     3     4");
        System.out.print("\n");
    }

    private void printScores() {
        System.out.println("Scores:");
        for (Player p : modelView.getPlayers()) {
            if (modelView.getPlayers().indexOf(p) != modelView.getPlayers().size() - 1)
                System.out.print("\t\t╠════> " + p.getName() + ": " + p.getScore() + "\t");
            else
                System.out.print("\t\t╚════> " + p.getName() + ": " + p.getScore() + "\t");

            for (int i = 0; i < p.getScore(); i++)
                System.out.print("★");
            System.out.println();
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

    private void printError(String error) {
        // Print an error message
        System.out.println(ANSI_PURPLE + error + ANSI_RESET);
    }

    private String scanLine() {
        String ret;
        do {
            ret = scanner.nextLine();
        } while (ret.equals(""));
        return ret;
    }

    private enum State {
        ASK_NAME, ASK_NUMBER, WAITING_FOR_PLAYERS, WAITING_FOR_TURN, PLAY
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
            for (Command command : Command.values()) {
                printCommandInfo(command);
            }
        }

        public void printCommandInfo(Command command) {
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
