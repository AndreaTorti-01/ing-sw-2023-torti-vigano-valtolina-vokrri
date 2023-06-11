package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.view.gui.Gui;
import javafx.application.Platform;
import javafx.fxml.FXML;
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
    private static Gui gui;
    private int pickedNum = 0;
    Player myID;


    public void updateShelfGraphics(ItemCard[][] newShelf, int shelfIdentifier) {
        resetBloom();
        shelves[shelfIdentifier].getChildren().clear();

        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                if (newShelf[i][j] != null) {
                    ItemCard itemCard = newShelf[i][j];

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
        gui = (Gui) it.polimi.ingsw.network.client.ClientImpl.getView();
        shelves[0] = Shelf0;
        shelves[1] = Shelf1;
        shelves[2] = Shelf2;
        shelves[3] = Shelf3;
    }

    public void setReady(int pickedSize){
        selectedColumn = -1;
        int maxCards = 0;
        pickedNum = pickedSize;

        if(myID == null) //removable? updategraphics is always called before setready
            for (Player p : gui.getModelView().getPlayers())
                if (p.getName().equals(gui.getPlayerName())) {
                    myID = p;
                    break;
                }
        if (myID == null) throw new NullPointerException();

        for (int j = 0; j < numberOfColumns; j++) {
            int freeSlots = 0;
            for (int i = 0; i < numberOfRows && freeSlots < 3; i++) {
                if (gui.getModelView().getShelfOf(myID)[i][j] == null) freeSlots++;
            }
            if (freeSlots >= pickedSize) {
                for(int i = 0; i < numberOfRows; i++){
                    if(gui.getModelView().getShelfOf(myID)[i][j] == null) {
                        ImageView imageView = (ImageView) Shelf0.getChildren().get(i * numberOfColumns + j);
                        imageView.setEffect(new Bloom()); //possibile che non si veda il bloom
                    }
                }
            }
        }
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

    public void updateGraphics() {

        if(myID == null)
            for (Player p : gui.getModelView().getPlayers())
                if (p.getName().equals(gui.getPlayerName())) {
                    myID = p;
                    break;
                }

        Platform.runLater(() -> {
            int j = 1;
            for (int i = 0; i < gui.getModelView().getPlayers().size(); i++) {
                if(!gui.getModelView().getPlayers().get(i).equals(myID)) {
                    updateShelfGraphics(gui.getModelView().getShelfOf(gui.getModelView().getPlayers().get(i)), j);
                    j++;
                }
            }

            updateShelfGraphics(gui.getModelView().getShelfOf(myID), 0);
        });
    }

    public void handleImageViewClick(MouseEvent mouseEvent) {
        //stores row and column of the clicked image

    }

}