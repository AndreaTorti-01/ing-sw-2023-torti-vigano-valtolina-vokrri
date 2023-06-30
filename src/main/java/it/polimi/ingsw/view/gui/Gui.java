package it.polimi.ingsw.view.gui;

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
import it.polimi.ingsw.view.gui.controllers.*;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * The GUI view of the game.
 */
public class Gui extends ObservableImpl implements RunnableView {
    /**
     * A lock to synchronize all the methods that change the state of this view.
     */
    private final Object lock = new Object();
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
    private Gui.State state = Gui.State.ASK_NAME;
    /**
     * The controller of the Welcome Screen.
     */
    private WelcomeScreenController welcomeScreenController;
    /**
     * The controller of the Playing Screen.
     */
    private PlayingScreenController playingScreenController;
    /**
     * The controller of the End Screen.
     */
    private EndScreenController endScreenController;
    /**
     * The board controller.
     */
    private BoardController boardController;
    /**
     * The controller of the player's shelf.
     */
    private Shelf0Controller shelf0Controller;
    /**
     * The coordinates of the cards picked by the player.
     */
    private List<List<Integer>> pickedCoords;

    /**
     * Creates a new GUI view associated with the provided client.
     *
     * @param client associated with this view.
     */
    public Gui(Client client) {
        this.addObserver((ClientImpl) client);
    }

    /**
     * Gets the state of the view.
     *
     * @return the state of the view.
     */
    public Gui.State getState() {
        synchronized (lock) {
            return state;
        }
    }

    /**
     * Sets the state of the view to the provided one.
     *
     * @param state the new state of the view.
     */
    private void setState(Gui.State state) {
        synchronized (lock) {
            this.state = state;
            System.err.println("state changed to " + state);
            lock.notifyAll();
        }
    }

    /**
     * Sends the provided message to the view and inserts it to the one sent during this game.
     *
     * @param message the message to be sent.
     */
    public void setMessage(String message) {
        if (message.charAt(0) == '/') {
            String command = message.split(" ")[0];

            // check which command it is
            switch (command) {
                case "/privatechat" -> {
                    // check that it has 3 arguments
                    if (message.split(" ").length >= 3) {
                        String destPlayer = message.split(" ")[1];
                        List<String> playerNames = new ArrayList<>();
                        for (Player player : modelView.getPlayers()) {
                            playerNames.add(player.getName());
                        }

                        // check that the dest player exists and is not the player himself
                        if (playerNames.contains(destPlayer) && !destPlayer.equals(playerName)) {
                            String chatMessage = message.split(" ", 3)[2];
                            notifyObservers(new ChatMsg(destPlayer, playerName, false, "[private] " + chatMessage));
                        }
                    } else
                        playingScreenController.printOnChat("Invalid arguments: Type /privatechat <player> <message> to send a private message to a player");
                }

                case "/cheat" -> {
                    if (modelView.getCurrentPlayer().getName().equals(playerName)) {
                        notifyObservers(new CheatMsg(playerName));
                    } else playingScreenController.printOnChat("You can't cheat if it's not your turn");
                }

                case "/help" -> {
                    playingScreenController.printOnChat("Type /chat <message> to send a message to all players");
                    playingScreenController.printOnChat("Type /privatechat <player> <message> to send a private message to a player");
                    playingScreenController.printOnChat("Type /help to see this list again");
                }

                default ->
                        playingScreenController.printOnChat("Invalid command: Type /help to see the list of available commands");
            }
        } else {
            notifyObservers(new ChatMsg(null, playerName, true, message));
        }
    }

    /**
     * Sets the number of players to the provided one.
     *
     * @param playerNumber the number of players to be set.
     */
    public void setPlayerNumber(int playerNumber) {
        gaveNumber = true;
        notifyObservers(playerNumber);
    }

    /**
     * Sets the picked card coordinates to the provided ones.
     *
     * @param pickedCoords the coordinates of the picked cards.
     */
    public void setPicked(List<List<Integer>> pickedCoords) {
        this.pickedCoords = pickedCoords;
        shelf0Controller.setReady(pickedCoords.size());
        List<ItemCard> pickedCards = new ArrayList<>();
        for (List<Integer> coords : pickedCoords) {
            pickedCards.add(modelView.getBoard()[coords.get(0)][coords.get(1)]);
        }
        playingScreenController.showPickedTypes(pickedCards);
    }

    /**
     * Inserts the picked cards into the provided column of the shelf.
     * Also resets the selection of the tiles on the board.
     *
     * @param column the column where to insert the cards.
     */
    public void setMove(int column) {
        boardController.resetSelection();
        notifyObservers(new MoveMsg(pickedCoords, column));
        pickedCoords.clear();
    }

    /**
     * Gets the model view of the game.
     *
     * @return the model view of the game.
     */
    public GameViewMsg getModelView() {
        return modelView;
    }

    /**
     * The main game loop.
     * Launches the JavaFX GUI application.
     */
    @Override
    public void run() {
        //TODO: pulire le stampe
        System.out.println("GUI started");

        //launching the GUI
        Application.launch(GuiApp.class);

        // waits for state to change
        while (getState() == Gui.State.ASK_NAME) {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    System.err.println("Interrupted while waiting for server: " + e.getMessage());
                }
            }
        }

        //
        if (getState() == Gui.State.ASK_NUMBER) {
            gaveNumber = true;
        }

        while (getState() == Gui.State.WAITING_FOR_PLAYERS) {
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
            while (getState() == Gui.State.WAITING_FOR_TURN) {
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        System.err.println("Interrupted while waiting for server: " + e.getMessage());
                    }
                }
            }

            while (getState() == Gui.State.PLAY) {
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
     * Updates the view with the provided model state.
     *
     * @param modelView the model view that contains a representation of the model state.
     */
    @Override
    public void updateView(GameViewMsg modelView) {
        this.modelView = modelView;

        // the game is waiting for players
        if (getState() == Gui.State.ASK_NAME && !playerName.equals("") && modelView.getNameError()) {
            this.playerName = "";
            setState(Gui.State.ASK_NAME);
            while (!GuiApp.controllersAvailable())
                sleep(100);
            welcomeScreenController = GuiApp.getWelcomeScreenController();
            welcomeScreenController.resetAskName();
        }
        System.out.println("notified");
        if (!playerName.equals("") && modelView.getGameStatus().equals(Game.Status.WAITING)) {
            if (playerName.equals(modelView.getPlayers().get(0).getName()) && !gaveNumber) {
                setState(Gui.State.ASK_NUMBER); // I am lobby leader
                System.out.println("asking number");
                while (!GuiApp.controllersAvailable())
                    sleep(100);
                welcomeScreenController = GuiApp.getWelcomeScreenController();
                welcomeScreenController.askNumber();
            } else {
                setState(Gui.State.WAITING_FOR_PLAYERS); // I am not lobby leader
                System.out.println("waiting for players");
                while (!GuiApp.controllersAvailable())
                    sleep(100);
                welcomeScreenController = GuiApp.getWelcomeScreenController();
                welcomeScreenController.waitForPlayers();
            }
        }
        // the game has started
        else if (modelView.getGameStatus().equals(Game.Status.STARTED)) {
            while (!GuiApp.controllersAvailable())
                sleep(100);
            welcomeScreenController = GuiApp.getWelcomeScreenController();
            welcomeScreenController.changeScene();


            playingScreenController = GuiApp.getPlayingScreenController();
            boardController = GuiApp.getBoardController();
            shelf0Controller = GuiApp.getShelf0Controller();

            boardController.updateGraphics();
            shelf0Controller.updateGraphics();
            playingScreenController.updateGraphics();
            playingScreenController.refreshChat();

            if (modelView.getCurrentPlayer().getName().equals(this.playerName)) {
                setState(Gui.State.PLAY); // it's my turn
            } else setState(Gui.State.WAITING_FOR_TURN); // it's not my turn
        }
        // the game ends
        else if (modelView.getGameStatus().equals(Game.Status.ENDED)) {
            playingScreenController.changeScene();
            endScreenController = GuiApp.getEndScreenController();
            endScreenController.updateGraphics();
            setState(Gui.State.ENDED);
        }
    }

    /**
     * Gets the name of the player.
     *
     * @return the name of the player.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Sets the name of the player to the provided one.
     *
     * @param playerName the name of the player.
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
        notifyObservers(playerName);
    }

    /**
     * Pauses the current thread for the provided amount of milliseconds.
     *
     * @param milliseconds the amount of milliseconds to wait.
     */
    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            System.err.println("Interrupted while sleeping: " + e.getMessage());
        }
    }

    /**
     * Gets the coordinates of the cards that have been picked by the player in the correct order.
     *
     * @return the coordinates of the cards that have been picked by the player in the correct order.
     */
    public List<List<Integer>> getPickedCoords() {
        return pickedCoords;
    }

    /**
     * The state of the view.
     */
    public enum State {
        /**
         * ASK_NAME state.
         */
        ASK_NAME,
        /**
         * ASK_NUMBER state.
         */
        ASK_NUMBER,
        /**
         * WAITING_FOR_PLAYERS state.
         */
        WAITING_FOR_PLAYERS,
        /**
         * WAITING_FOR_TURN state.
         */
        WAITING_FOR_TURN,
        /**
         * PLAY state.
         */
        PLAY,
        /**
         * ENDED state.
         */
        ENDED
    }

}
