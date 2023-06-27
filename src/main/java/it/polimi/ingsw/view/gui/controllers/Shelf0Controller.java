package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.client.ClientImpl;
import it.polimi.ingsw.view.gui.Gui;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.ingsw.utils.Common.getTilePath;
import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

/**
 * Controller for the shelf of the player associated with this view.
 */
public class Shelf0Controller implements Initializable {
    /**
     * The GUI instance.
     */
    private static Gui gui;
    /**
     * The available columns where the player can insert the picked cards.
     */
    private final boolean[] availableColumns = new boolean[numberOfColumns];
    /**
     * The grid representing the shelf.
     */
    @FXML
    public GridPane shelf0;
    /**
     * The column selected by the player.
     */
    public int selectedColumn = -1;
    /**
     * The player's ID.
     */
    private Player myID;
    /**
     * True if the player has already selected the column.
     */
    private boolean sent = false;

    /**
     * FXML standard method: initializes the GUI instance.
     *
     * @param url            ignored.
     * @param resourceBundle ignored.
     */
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gui = (Gui) ClientImpl.getView();
    }

    /**
     * Sets the shelf ready to be clicked.
     *
     * @param pickedSize the number of cards picked by the player.
     */
    public void setReady(int pickedSize) {
        selectedColumn = -1;
        sent = false;

        if (myID == null)
            for (Player player : gui.getModelView().getPlayers())
                if (player.getName().equals(gui.getPlayerName())) {
                    myID = player;
                    break;
                }
        if (myID == null) throw new NullPointerException();

        for (int col = 0; col < numberOfColumns; col++) {
            int freeSlots = 0;
            for (int row = 0; row < numberOfRows && freeSlots < 3; row++) {
                if (gui.getModelView().getShelfOf(myID)[row][col] == null) freeSlots++;
            }
            availableColumns[col] = freeSlots >= pickedSize;
        }
    }

    /**
     * Updates the graphics of the shelf.
     */
    public void updateGraphics() {

        if (myID == null)
            for (Player player : gui.getModelView().getPlayers())
                if (player.getName().equals(gui.getPlayerName())) {
                    myID = player;
                    break;
                }

        Platform.runLater(() -> {
            for (int row = 0; row < numberOfRows; row++) {
                for (int col = 0; col < numberOfColumns; col++) {
                    if (gui.getModelView().getShelfOf(myID)[row][col] != null) {
                        ItemCard itemCard = gui.getModelView().getShelfOf(myID)[row][col];
                        ImageView imageView = (ImageView) shelf0.getChildren().get(col * numberOfRows + row);
                        Image newImage = new Image(getTilePath(itemCard));
                        imageView.setImage(newImage);
                    } else {
                        ImageView imageView = (ImageView) shelf0.getChildren().get(col * numberOfRows + row);
                        imageView.setImage(new Image("graphicalResources/itemTiles/Empty.png"));
                    }
                }
            }
        });
    }

    /**
     * Handles the click on the shelf.
     *
     * @param mouseEvent the mouse event.
     */
    public void handleImageViewClick(MouseEvent mouseEvent) {
        //gets the column of the clicked imageview
        if (gui.getState().equals(Gui.State.PLAY) && !sent) {

            ImageView clickedImageView = (ImageView) mouseEvent.getSource();
            int clickedIndex = shelf0.getChildren().indexOf(clickedImageView);
            selectedColumn = clickedIndex / numberOfRows;
            System.out.println("Selected column: " + selectedColumn);

            if (availableColumns[selectedColumn]) {
                System.out.println("Available column");
                gui.setMove(selectedColumn);
                sent = true;
            } else System.out.println("Not available column");
        }
    }
}
