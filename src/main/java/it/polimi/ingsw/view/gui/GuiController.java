package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.serializable.GameViewMsg;
import it.polimi.ingsw.utils.Observable;
import it.polimi.ingsw.view.RunnableView;
import it.polimi.ingsw.view.tui.Tui;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.Scanner;

import static it.polimi.ingsw.view.tui.TerminalPrintables.printWelcomeScreen;

public class GuiController extends Observable implements RunnableView {
    private final Object lock = new Object();
    private final Scanner scanner = new Scanner(System.in);
    boolean gaveNumber;
    private String playerName = "";
    private GameViewMsg modelView;
    private GuiController.State state = GuiController.State.ASK_NAME;
    public ImageView nickname_label;
    private Gui gui;
    private static final String fxmlPath = "/graphicalResources/fxml/";
    public TextField insert_nickname;

    public GuiController(Client client) {
        this.addObserver(client);
    }
    private GuiController.State getState() {
        synchronized (lock) {
            return state;
        }
    }

    private void setState(GuiController.State state) {
        synchronized (lock) {
            this.state = state;
            System.err.println("state changed to " + state);
            lock.notifyAll();
        }
    }

    @Override
    public void run() {
        Application.launch(Gui.class);

        // TODO: change scene
        printWelcomeScreen();
        this.playerName = getNickname();

        // waits for state to change
        while (getState() == GuiController.State.ASK_NAME) {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    System.err.println("Interrupted while waiting for server: " + e.getMessage());
                }
            }
        }

        //
        if (getState() == GuiController.State.ASK_NUMBER) {
            //TODO: ask for number
            gaveNumber = true;
        }

        while (getState() == GuiController.State.WAITING_FOR_PLAYERS) {
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
            while (getState() == GuiController.State.WAITING_FOR_TURN) {
                // TODO show the game scene
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        System.err.println("Interrupted while waiting for server: " + e.getMessage());
                    }
                }
            }

            while (getState() == GuiController.State.PLAY) {
                // TODO show the game scene
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
                setState(GuiController.State.ASK_NUMBER); // I am lobby leader
            else
                setState(GuiController.State.WAITING_FOR_PLAYERS); // I am not lobby leader
        }
        // the game has started
        else if (modelView.getGameStatus().equals(Game.Status.STARTED)) {
            if (modelView.getCurrentPlayer().getName().equals(this.playerName)) {
                setState(GuiController.State.PLAY); // it's my turn
            } else setState(GuiController.State.WAITING_FOR_TURN); // it's not my turn
        }
    }
    public String getNickname() {
        return insert_nickname.getText();
    }

    private enum State {
        ASK_NAME, ASK_NUMBER, WAITING_FOR_PLAYERS, WAITING_FOR_TURN, PLAY
    }

}
