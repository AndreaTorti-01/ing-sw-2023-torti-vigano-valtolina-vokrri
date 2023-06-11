package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.client.ClientImpl;
import it.polimi.ingsw.network.serializable.ChatMsg;
import it.polimi.ingsw.network.serializable.GameViewMsg;
import it.polimi.ingsw.network.serializable.MoveMsg;
import it.polimi.ingsw.utils.ObservableImpl;
import it.polimi.ingsw.view.RunnableView;
import it.polimi.ingsw.view.gui.controllers.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.util.List;
import java.util.Scanner;

public class Gui extends ObservableImpl implements RunnableView {
    private static final String fxmlPath = "/graphicalResources/fxml/";
    private final Object lock = new Object();
    private final Scanner scanner = new Scanner(System.in);
    boolean gaveNumber;
    private String playerName = "";
    private GameViewMsg modelView;
    private Gui.State state = Gui.State.ASK_NAME;
    private GuiApp gui;
    private WelcomeScreenController welcomeScreenController;
    private PlayingScreenController playingScreenController;
    private EndScreenController endScreenController;
    private BoardController boardController;
    private Shelf0Controller shelf0Controller;
    private FXMLLoader loader;
    private Parent root;
    private List<List<Integer>> pickedCoords;


    public Gui(Client client) {
        this.addObserver((ClientImpl) client);
    }

    public Gui.State getState() {
        synchronized (lock) {
            return state;
        }
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
        notifyObservers(playerName);
    }
    public void setPlayerNumber(int playerNumber) {
        gaveNumber = true;
        notifyObservers(playerNumber);
    }
    public void setPicked(List<List<Integer>> pickedCoords) {
        this.pickedCoords = pickedCoords;
        shelf0Controller.setReady(pickedCoords.size());
    }
    public void setMove(int shelfCol){
        boardController.resetSelection();
        notifyObservers(new MoveMsg(pickedCoords, shelfCol));
    }
    public void sendMsg(String destPlayer, String sourcePlayer, boolean isMsgPublic, String message){
        notifyObservers(new ChatMsg(destPlayer, sourcePlayer, isMsgPublic, message));
    }

    private void setState(Gui.State state) {
        synchronized (lock) {
            this.state = state;
            System.err.println("state changed to " + state);
            lock.notifyAll();
        }
    }

    public GameViewMsg getModelView() {
        return modelView;
    }

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
     * updates the view with the new model state
     *
     * @param modelView which contains a representation of the model state
     */
    @Override
    public void updateView(GameViewMsg modelView) {
        this.modelView = modelView;

        // the game is waiting for players
        System.out.println("notified");
        if (!playerName.equals("") && modelView.getGameStatus().equals(Game.Status.WAITING)) {
            if (playerName.equals(modelView.getPlayers().get(0).getName()) && !gaveNumber) {
                setState(Gui.State.ASK_NUMBER); // I am lobby leader
                System.out.println("asking number");
                while (!GuiApp.controllersAvailable())
                    sleep(100);
                welcomeScreenController = GuiApp.getWelcomeScreenController();
                welcomeScreenController.askNumber();
            }
            else {
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
            welcomeScreenController.changescene();



            playingScreenController = GuiApp.getPlayingScreenController();
            boardController = GuiApp.getBoardController();
            shelf0Controller = GuiApp.getShelf0Controller();

            boardController.updateGraphics();
            shelf0Controller.updateGraphics();
            System.out.println("shelf updated");
            playingScreenController.updateGraphics();

            if (modelView.getCurrentPlayer().getName().equals(this.playerName)) {
                setState(Gui.State.PLAY); // it's my turn
                //TODO playingScreenController.setMyTurn(); // only displays a message on the screen
            } else setState(Gui.State.WAITING_FOR_TURN); // it's not my turn
                //TODO playingScreenController.setTurnOf(modelView.getCurrentPlayer().getName()); // only displays a message on the screen
        }
    }

    public String getPlayerName() {
        return  playerName;
    }


    public enum State {
        ASK_NAME, ASK_NUMBER, WAITING_FOR_PLAYERS, WAITING_FOR_TURN, PLAY
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            System.err.println("Interrupted while sleeping: " + e.getMessage());
        }
    }

}
