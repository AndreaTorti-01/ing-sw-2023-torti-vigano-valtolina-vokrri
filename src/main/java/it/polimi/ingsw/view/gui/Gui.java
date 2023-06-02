package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.client.ClientImpl;
import it.polimi.ingsw.network.serializable.GameViewMsg;
import it.polimi.ingsw.network.serializable.MoveMsg;
import it.polimi.ingsw.utils.ObservableImpl;
import it.polimi.ingsw.view.RunnableView;
import it.polimi.ingsw.view.gui.controllers.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.utils.Constants.*;
import static it.polimi.ingsw.utils.Constants.numberOfColumns;

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
    private ShelfController shelfController;
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

        loader = new FXMLLoader(getClass().getResource(fxmlPath + "WelcomeScreen.fxml"));
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
                updateGameScene();
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        System.err.println("Interrupted while waiting for server: " + e.getMessage());
                    }
                }
            }

            while (getState() == Gui.State.PLAY) {
                updateGameScene();
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
     * takes care of notifying observers
     *
     * @return the name of the player
     */

    private void pickCards() {
        List<List<Integer>> pickedCoords = new ArrayList<>();

        int pickedNum = 0; //number of already picked cards
        boolean validChoice = false;
        int shelfCol = 0; //column of the shelf where the player is moving cards to
        int maxCards = 0; //maximum cards that can be picked
        int[] freeSlotsNumber = new int[numberOfColumns]; //number of max cards that can be inserted in each column

        //notifying observers
        notifyObservers(new MoveMsg(pickedCoords, shelfCol));
    }

    private int getSelectedRow() {
        int row = -1;
        do {
            //sleep for 0.1 second
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            row = boardController.getSelectedRow();
        } while (row == -1);
        return row;
    }
    private int getSelectedColumn(){
        int col = -1;
        do{
            //sleep for 0.1 second
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            col = boardController.getSelectedColumn();
        }while(col == -1);
        return col;
    }
    public String getPlayerName(){
        String name = "";
        do{
            //sleep for 0.1 second
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
            //sleep for 0.1 second
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

            updateGameScene();

            if (modelView.getCurrentPlayer().getName().equals(this.playerName)) {
                setState(Gui.State.PLAY); // it's my turn
            } else setState(Gui.State.WAITING_FOR_TURN); // it's not my turn
        }
    }

    private void updateGameScene(){
        loader = new FXMLLoader(getClass().getResource(fxmlPath + "Board.fxml"));
        try {
            root = loader.load(); // can throw IOException
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boardController = loader.getController();
        //updates the board
        boardController.updateBoardGraphics(modelView.getBoard());

        loader = new FXMLLoader(getClass().getResource(fxmlPath + "Shelf0.fxml"));
        try {
            root = loader.load(); // can throw IOException
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        shelfController = loader.getController();
        //updates the shelfs
        int shelfNumber = 1;
        for(Player p : modelView.getPlayers()){
            if(p.getName().equals(playerName))
                shelfController.updateShelfGraphics(p.getShelf(), 0);
            else{
                shelfController.updateShelfGraphics(p.getShelf(), shelfNumber);
                shelfNumber = shelfNumber + 1;
            }

        }
    }

    private enum State {
        ASK_NAME, ASK_NUMBER, WAITING_FOR_PLAYERS, WAITING_FOR_TURN, PLAY
    }

}
