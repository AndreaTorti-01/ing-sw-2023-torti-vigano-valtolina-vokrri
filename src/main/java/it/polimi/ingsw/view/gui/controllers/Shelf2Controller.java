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

public class Shelf2Controller implements Initializable {
    @FXML
    public GridPane shelf2;

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void updateGraphics(ItemCard[][] newShelf) {
        Platform.runLater(() -> {
            for (int i = 0; i < numberOfRows; i++) {
                for (int j = 0; j < numberOfColumns; j++) {
                    if (newShelf[i][j] != null) {
                        ItemCard itemCard = newShelf[i][j];
                        ImageView imageView = (ImageView) shelf2.getChildren().get(j * numberOfRows + i);
                        Image newImage = new Image(getTilePath(itemCard));
                        imageView.setImage(newImage);
                    } else {
                        ImageView imageView = (ImageView) shelf2.getChildren().get(j * numberOfRows + i);
                        Image newImage = new Image("/graphicalResources/itemTiles/Empty.png");
                        imageView.setImage(newImage);
                    }
                }
            }
        });
    }
}