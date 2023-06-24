package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Shelf;
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

public class TuiRaw extends ObservableImpl implements RunnableView {
    private final Object lock = new Object();
    private final Scanner scanner = new Scanner(System.in);
    boolean gaveNumber;
    private String playerName = "";
    private GameViewMsg modelView;
    private State state = State.ASK_NAME;
    private volatile boolean running;


    public TuiRaw(ClientImpl client) {
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

        while (getState() == State.ASK_NAME) {
            synchronized (lock) {
                try {
                    new Thread(this::askPlayerName).start();
                    lock.wait();
                } catch (InterruptedException e) {
                    System.err.println("Interrupted while waiting for server: " + e.getMessage());
                }
            }
        }

        while (getState() == State.ASK_NUMBER) {
            synchronized (lock) {
                try {
                    new Thread(this::askPlayerNumber).start();
                    lock.wait();
                } catch (InterruptedException e) {
                    System.err.println("Interrupted while waiting for server: " + e.getMessage());
                }
            }
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

        // create a thread passing the function that will handle the input
        running = true;
        Thread inputHandler = new Thread(this::acceptInput);
        inputHandler.start();

        while (!(getState() == State.ENDED)) {
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
     * updates the view with the new model state
     *
     * @param modelView which contains a representation of the model state
     */
    @Override
    public void updateView(GameViewMsg modelView) {
        this.modelView = modelView;

        // check the name input is valid
        if (getState() == State.ASK_NAME && !playerName.equals("") && modelView.getNameError()) {
            this.playerName = "";
            setState(State.ASK_NAME);
        }

        // the game is waiting for players
        if (!playerName.equals("") && modelView.getGameStatus().equals(Game.Status.WAITING)) {
            if (playerName.equals(modelView.getPlayers().get(0).getName()) && !gaveNumber) {
                setState(State.ASK_NUMBER); // I am lobby leader
            } else setState(State.WAITING_FOR_PLAYERS); // I am not lobby leader
        }
        // the game has started
        else if (modelView.getGameStatus().equals(Game.Status.STARTED)) {
            if (modelView.getCurrentPlayer().getName().equals(this.playerName))
                setState(State.PLAY); // it's my turn
            else setState(State.WAITING_FOR_TURN); // it's not my turn
            printGameStatus();
            if (getState() == State.PLAY) {
                System.out.println("It's your turn!");
            }
        }
        // the game ends
        else if (modelView.getGameStatus().equals(Game.Status.ENDED)) {
            printEndScreen(modelView.getWinner().getName());
            setState(State.ENDED);
        }
    }

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
                        if (state.equals(State.PLAY)) {
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
        gaveNumber = true;
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
        printBoard(modelView.getBoard(), modelView.getBoardValid());
        System.out.println("\n");
        printShelves();
        printPersonalGoalCards();
        printCommonGoalCards();
        printScores();
        printChat();
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
        for (Player p : modelView.getPlayers()) {
            if (p.getName().equals(playerName)) {
                StringBuilder output = new StringBuilder();

                for (int i = 0; i < numberOfRows; i++) {
                    output.append("| ");
                    for (int j = 0; j < numberOfColumns; j++) {
                        if (p.getPersonalGoalCard().getPattern()[i][j] != null)
                            output.append(p.getPersonalGoalCard().getPattern()[i][j].toString().charAt(0)).append(" ");
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
    }

    private void printChat() {
        if (modelView.getMessages().size() == 0) return;
        System.out.println("Chat: ");
        for (ChatMsg message : modelView.getMessages()) {
            if (message.isPublic() || !message.isPublic() && (message.getRecipientPlayer().equals(playerName) || message.getSenderPlayer().equals(playerName)))
                System.out.println(message);
        }
    }

    private void printEndScreen(String winnerName) {
        printScores();
        System.out.println("The winner is " + winnerName + "!");
    }

    /**
     * takes care of notifying observers
     *
     * @return the name of the player
     */
    private void askPlayerName() {
        // Ask the name of the player
        System.out.println("  >>  Enter your name:  ");
        this.playerName = scanLine();
        notifyObservers(playerName);
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
        for (Player p : modelView.getPlayers()) {
            Shelf shelf = p.getShelf();
            System.out.println(p.getName() + "'s Shelf:");
            StringBuilder output = new StringBuilder();

            for (int i = 0; i < numberOfRows; i++) {
                output.append("| ");
                for (int j = 0; j < numberOfColumns; j++) {
                    if (shelf.getItems()[i][j] != null)
                        output.append(shelf.getItems()[i][j].toString()).append(" ");
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

    private void printScores() {
        System.out.println("Scores:");
        for (Player p : modelView.getPlayers()) {
            System.out.println(p.getName() + "'s points: " + p.getScore());
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

    private String scanLine() {
        String ret;
        do {
            ret = scanner.nextLine();
        } while (ret.equals("") && running);
        return ret;
    }

    private enum State {
        ASK_NAME, ASK_NUMBER, WAITING_FOR_PLAYERS, WAITING_FOR_TURN, PLAY, ENDED
    }
}
