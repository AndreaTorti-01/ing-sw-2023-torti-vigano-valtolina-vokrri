package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.Player;
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

import static it.polimi.ingsw.utils.Constants.*;

public class Shelf0Controller implements Initializable {
    @FXML
    public GridPane shelf0;
    public int selectedColumn = -1;
    private static Gui gui;
    private Player myID;
    private final boolean[] availableCols = new boolean[numberOfColumns];
    private boolean sent = false;

    public int getSelectedColumn() {
        return selectedColumn;
    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gui = (Gui) it.polimi.ingsw.network.client.ClientImpl.getView();
    }

    public void setReady(int pickedSize){
        selectedColumn = -1;
        int maxCards = 0;
        sent = false;

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
                availableCols[j] = true;
            }else availableCols[j] = false;
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
            for(int i = 0; i < numberOfRows; i++) {
                for(int j = 0; j < numberOfColumns; j++) {
                    if(gui.getModelView().getShelfOf(myID)[i][j] != null) {
                        ItemCard itemCard = gui.getModelView().getShelfOf(myID)[i][j];
                        ImageView imageView = (ImageView) shelf0.getChildren().get(j * numberOfRows + i);
                        Image newImage = new Image(getTilePath(itemCard));
                        imageView.setImage(newImage);
                    }else {
                        ImageView imageView = (ImageView) shelf0.getChildren().get(j * numberOfRows + i);
                        imageView.setImage(new Image("graphicalResources/itemTiles/Empty.png"));
                    }
                }
            }
        });
    }

    public void handleImageViewClick(MouseEvent mouseEvent) {
        //gets the column of the clicked imageview
        if (gui.getState().equals(Gui.State.PLAY) && !sent) {

            ImageView clickedImageView = (ImageView) mouseEvent.getSource();
            int clickedIndex = shelf0.getChildren().indexOf(clickedImageView);
            selectedColumn = clickedIndex / numberOfRows;
            System.out.println("Selected column: " + selectedColumn);

            if (availableCols[selectedColumn]) {
                System.out.println("Available column");
                gui.setMove(selectedColumn);
                sent = true;
            }else System.out.println("Not available column");
        }
    }
}
