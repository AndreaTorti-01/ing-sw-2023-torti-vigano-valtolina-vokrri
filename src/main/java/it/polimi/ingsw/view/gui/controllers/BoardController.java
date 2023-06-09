package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.Gui.State;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.effect.Bloom;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static it.polimi.ingsw.utils.Constants.*;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

public class BoardController implements Initializable {
    public GridPane Board;
    public int clickedRow = -1;
    public int clickedColumn = -1;
    private static Gui gui;
    private List<List<Integer>> pickedCoords;
    private int pickedNum = 0;


    private void updateBoardGraphics(ItemCard[][] tileMatrix) {
        resetSelection();

        for (int i = 0; i < numberOfBoardRows; i++) {
            for (int j = 0; j < numberOfBoardColumns; j++) {
                if (tileMatrix[i][j] != null) {
                    ItemCard itemCard = tileMatrix[i][j];

                    ImageView imageView = (ImageView) Board.getChildren().get(i * numberOfBoardColumns + j);
                    Image newImage = new Image(getTilePath(itemCard));
                    imageView.setImage(newImage);
                }
            }
        }
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

    public void resetSelection() {
        this.clickedRow = -1;
        this.clickedColumn = -1;
        Platform.runLater(() -> {
            for (int i = 0; i < numberOfBoardRows; i++) {
                for (int j = 0; j < numberOfBoardColumns; j++) {
                    ImageView imageView = (ImageView) Board.getChildren().get(i * numberOfBoardColumns + j);
                    imageView.setEffect(null);
                }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gui = (Gui) it.polimi.ingsw.network.client.ClientImpl.getView();
    }

    public void handleImageViewClick(MouseEvent mouseEvent) {
        //stores row and column of the clicked image



        if (gui.getState().equals(State.PLAY) && pickedCoords.size() < 3) {

            ImageView clickedImageView = (ImageView) mouseEvent.getSource();
            int clickedIndex = Board.getChildren().indexOf(clickedImageView);
            clickedRow = clickedIndex / numberOfBoardColumns;
            clickedColumn = clickedIndex % numberOfBoardColumns;

            //starts picking the cards (specular to pickcards)
            Player me = null;
            int maxCards = 0;
            int[] freeSlotsNumber = new int[numberOfColumns];

            for (Player p : gui.getModelView().getPlayers())
                if (p.getName().equals(gui.getPlayerName())) {
                    me = p;
                    break;
                }
            if (me == null) throw new NullPointerException();


            for (int j = 0; j < numberOfColumns; j++) {
                int freeSlots = 0;
                for (int i = 0; i < numberOfRows && freeSlots < 3; i++) {
                    if (gui.getModelView().getShelfOf(me)[i][j] == null) freeSlots++;
                    if (freeSlots > maxCards) maxCards = freeSlots;
                    freeSlotsNumber[j] = freeSlots;
                }
            }
            if (maxCards > 3) maxCards = 3;

            if(pickedNum < maxCards) {
                //checking coordinate validity
                if (clickedRow >= 0 && clickedRow < numberOfBoardRows && clickedColumn >= 0 && clickedColumn < numberOfBoardColumns & isTakeable(gui.getModelView(), clickedRow, clickedColumn, pickedCoords)) {
                    List<Integer> coords = new ArrayList<>();
                    coords.add(clickedRow);
                    coords.add(clickedColumn);
                    pickedCoords.add(coords);
                    pickedNum++;
                    //sets bloom effect
                    //no need of runLater because it is called by the gui thread
                    clickedImageView.setEffect(new Bloom());
                } else System.out.println("Invalid coordinates!, retry");
            }
            gui.setPicked(pickedCoords);
        }
    }
}

