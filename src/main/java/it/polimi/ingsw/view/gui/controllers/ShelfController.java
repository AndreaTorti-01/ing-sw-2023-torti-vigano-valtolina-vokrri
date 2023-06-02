package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.Shelf;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.Bloom;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.ingsw.utils.Constants.*;

public class ShelfController implements Initializable {
    @FXML
    public GridPane Shelf0;
    @FXML
    public GridPane Shelf1;
    @FXML
    public GridPane Shelf2;
    @FXML
    public GridPane Shelf3;
    private final GridPane[] shelves = new GridPane[4];
    public int selectedColumn = -1;

    public void updateShelfGraphics(Shelf newShelf, int shelfIdentifier) {
        resetBloom();

        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                if (newShelf.getCardAt(i, j) != null) {
                    ItemCard itemCard = newShelf.getCardAt(i, j);

                    ImageView imageView = (ImageView) shelves[shelfIdentifier].getChildren().get(i * numberOfColumns + j);
                    Image newImage = new Image(getTilePath(itemCard));
                    imageView.setImage(newImage);
                }
            }
        }
    }

    public int getSelectedColumn() {
        return selectedColumn;
    }

    private String getTilePath(ItemCard itemCard) {
        String tilesPath = "/graphicalResources/itemTiles/";
        switch (itemCard.getType()) {
            case CATS -> tilesPath += "Gatti1.";
            case BOOKS -> tilesPath += "Libri1.";
            case PLANTS -> tilesPath += "Piante1.";
            case TROPHIES -> tilesPath += "Trofei1.";
            case FRAMES -> tilesPath += "Cornici1.";
            case GAMES -> tilesPath += "Giochi1.";
        }
        switch (itemCard.getSprite()) {
            case 0 -> tilesPath += "1.png";
            case 1 -> tilesPath += "2.png";
            case 2 -> tilesPath += "3.png";
        }
        return tilesPath;
    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        shelves[0] = Shelf0;
        shelves[1] = Shelf1;
        shelves[2] = Shelf2;
        shelves[3] = Shelf3;
    }
    public void bloomColumn(int column){
        for (int i = 0; i < numberOfRows; i++) {
            ImageView imageView = (ImageView) Shelf0.getChildren().get(i * numberOfColumns + column);
            imageView.setEffect(new Bloom());
        }
    }

    public void setReady(){
        selectedColumn = -1;
    }
    public void resetBloom() {
        selectedColumn = -1;

        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                ImageView imageView = (ImageView) Shelf0.getChildren().get(i * numberOfColumns + j);
                imageView.setEffect(null);
            }
        }
    }
}