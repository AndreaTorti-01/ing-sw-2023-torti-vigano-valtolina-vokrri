package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.Board;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.ingsw.utils.Constants.*;

public class BoardController implements Initializable {
    public GridPane Board;

    public void updateBoardGraphics(Board newBoard){
        for (int i = 0; i < numberOfBoardRows; i++) {
            for (int j = 0; j < numberOfBoardColumns; j++) {
                if(newBoard.getTileMatrix()[i][j] != null){
                    ItemCard itemCard = newBoard.getTileMatrix()[i][j];

                    ImageView imageView = (ImageView) Board.getChildren().get(i * numberOfBoardColumns + j);
                    Image newImage = new Image(getTilePath(itemCard));
                    imageView.setImage(newImage);
                }
            }
        }
    }

    private String getTilePath(ItemCard itemCard){
        String tilesPath = "/graphicalResources/itemTiles/";
        switch(itemCard.getType()){
            case CATS -> tilesPath += "Gatti1.";
            case BOOKS -> tilesPath += "Libri1.";
            case PLANTS -> tilesPath += "Piante1.";
            case TROPHIES -> tilesPath += "Trofei1.";
            case FRAMES -> tilesPath += "Cornici1.";
            case GAMES -> tilesPath += "Giochi1.";
        }
        switch (itemCard.getSprite()){
            case 0 -> tilesPath += "1.png";
            case 1 -> tilesPath += "2.png";
            case 2 -> tilesPath += "3.png";
        }
        return tilesPath;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
