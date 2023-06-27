package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.ingsw.utils.Common.getTilePath;
import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

/**
 * The controller for the fourth player's shelf.
 */
public class Shelf3Controller implements Initializable {
    /**
     * The grid representing the shelf.
     */
    @FXML
    public GridPane shelf3;

    /**
     * FXML standard method.
     *
     * @param url            ignored.
     * @param resourceBundle ignored.
     */
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    /**
     * Updates the shelf graphics.
     *
     * @param newShelf the new shelf to be displayed.
     */
    public void updateGraphics(ItemCard[][] newShelf) {
        Platform.runLater(() -> {
            for (int row = 0; row < numberOfRows; row++) {
                for (int col = 0; col < numberOfColumns; col++) {
                    if (newShelf[row][col] != null) {
                        ItemCard itemCard = newShelf[row][col];
                        ImageView imageView = (ImageView) shelf3.getChildren().get(col * numberOfRows + row);
                        Image newImage = new Image(getTilePath(itemCard));
                        imageView.setImage(newImage);
                    } else {
                        ImageView imageView = (ImageView) shelf3.getChildren().get(col * numberOfRows + row);
                        Image newImage = new Image("/graphicalResources/itemTiles/Empty.png");
                        imageView.setImage(newImage);
                    }
                }
            }
        });
    }
}
