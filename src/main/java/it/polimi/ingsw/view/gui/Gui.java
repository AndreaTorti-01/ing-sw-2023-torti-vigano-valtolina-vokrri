package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.client.ClientImpl;
import it.polimi.ingsw.network.serializable.GameViewMsg;
import it.polimi.ingsw.utils.ObservableImpl;
import it.polimi.ingsw.view.RunnableView;
import it.polimi.ingsw.view.gui.controllers.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
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
    private ShelfController shelfController0, shelfController1, shelfController2, shelfController3;
    private FXMLLoader loader;
    private Parent root;



    public Gui(Client client) {
        this.addObserver((ClientImpl) client);
    }

    private Gui.State getState() {
        synchronized (lock) {
            return state;
        }
    }

    private void setState(Gui.State state) {
        synchronized (lock) {
            this.state = state;
            System.err.println("state changed to " + state);
            lock.notifyAll();
        }
    }

    @Override
    public void run() {

        Application.launch(GuiApp.class);

        loader = new FXMLLoader(getClass().getResource(fxmlPath + "welcomeScreen.fxml"));
        try {
            root = loader.load(); // can throw IOException
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        welcomeScreenController = loader.getController();


        this.playerName = getPlayerName(); // ASKING FOR THE NAME
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

            getNumberOfPlayers(); // ASKING FOR THE NUMBER OF PLAYERS
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
                // TODO ask for action (picking cards, placing cards, etc.
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
     * takes care of notifying observers
     *
     * @return the name of the player
     */
    public String getPlayerName(){
        String name = "";
        do{
            name = welcomeScreenController.getNickname();
        }while(name.equals(""));
        notifyObservers(name);
        return name;
    }
    /**
     * takes care of notifying observers
     */
    public void getNumberOfPlayers(){
        int numPlayers = 0;
        do{
            numPlayers = welcomeScreenController.getNumberOfPlayers();
        }while(numPlayers == 0);
        notifyObservers(numPlayers);
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
                setState(Gui.State.ASK_NUMBER); // I am lobby leader
            else
                setState(Gui.State.WAITING_FOR_PLAYERS); // I am not lobby leader
        }
        // the game has started
        else if (modelView.getGameStatus().equals(Game.Status.STARTED)) {
            //TODO: show the game scene
            if (modelView.getCurrentPlayer().getName().equals(this.playerName)) {
                setState(Gui.State.PLAY); // it's my turn
            } else setState(Gui.State.WAITING_FOR_TURN); // it's not my turn
        }
    }

    private enum State {
        ASK_NAME, ASK_NUMBER, WAITING_FOR_PLAYERS, WAITING_FOR_TURN, PLAY
    }

}
