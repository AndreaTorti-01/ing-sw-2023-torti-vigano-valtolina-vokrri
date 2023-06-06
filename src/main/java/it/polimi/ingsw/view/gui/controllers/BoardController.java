package it.polimi.ingsw.view.gui.controllers;

import com.sun.media.jfxmedia.events.AudioSpectrumEvent;
import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.Board;
import javafx.fxml.Initializable;
import javafx.scene.effect.Bloom;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static it.polimi.ingsw.utils.Constants.*;

public class BoardController implements Initializable {
    public GridPane Board;
    public boolean isMyTurn = false;
    public int clickedRow = -1;
    public int clickedColumn = -1;


    public void updateBoardGraphics(ItemCard[][] tileMatrix){
        resetSelection();

        for (int i = 0; i < numberOfBoardRows; i++) {
            for (int j = 0; j < numberOfBoardColumns; j++) {
                if(tileMatrix[i][j] != null){
                    ItemCard itemCard = tileMatrix[i][j];

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

    public int getSelectedRow(){
        this.isMyTurn = true;
        return this.clickedRow;
    }
    public int getSelectedColumn() {
        this.isMyTurn = true;
        return this.clickedColumn;
    }

    public void setPicked(int row, int col){
        //sets the bloom effect ont he picked card
        this.clickedRow = -1;
        this.clickedColumn = -1;
        ImageView imageView = (ImageView) Board.getChildren().get(row * numberOfBoardColumns + col);
        imageView.setEffect(new Bloom());
    }

    public void setReady(){
        clickedRow = -1;
        clickedColumn = -1;
    }
    public void resetSelection(){
        this.clickedRow = -1;
        this.clickedColumn = -1;
        this.isMyTurn = false;

        for (int i = 0; i < numberOfBoardRows; i++) {
            for (int j = 0; j < numberOfBoardColumns; j++) {
                ImageView imageView = (ImageView) Board.getChildren().get(i * numberOfBoardColumns + j);
                imageView.setEffect(null);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void handleImageViewClick(MouseEvent mouseEvent) {
        if(isMyTurn) {
            ImageView imageView = (ImageView) mouseEvent.getSource();
            this.clickedColumn = GridPane.getColumnIndex(imageView);
            this.clickedRow = GridPane.getRowIndex(imageView);
        }
    }


}
