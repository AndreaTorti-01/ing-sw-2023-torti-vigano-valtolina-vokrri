package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.Gui.State;
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
import static it.polimi.ingsw.utils.Constants.numberOfRows;

public class BoardController implements Initializable {
    @FXML
    public GridPane gridBoard;
    public int clickedRow = -1;
    public int clickedColumn = -1;
    private static Gui gui;
    private List<List<Integer>> pickedCoords = new ArrayList<>();
    private int pickedNum = 0;

    private boolean sentPicked = false;


    private void updateBoardGraphics(ItemCard[][] tileMatrix) {
        resetSelection();

        for (int i = 0; i < numberOfBoardRows; i++) {
            for (int j = 0; j < numberOfBoardColumns; j++) {
                if (tileMatrix[i][j] != null) {
                    ItemCard itemCard = tileMatrix[i][j];

                    ImageView imageView = (ImageView) gridBoard.getChildren().get(i * numberOfBoardColumns + j);
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
        sentPicked = true;
        this.clickedRow = -1;
        this.clickedColumn = -1;
        Platform.runLater(() -> {
            for (int i = 0; i < numberOfBoardRows; i++) {
                for (int j = 0; j < numberOfBoardColumns; j++) {
                    ImageView imageView = (ImageView) gridBoard.getChildren().get(i * numberOfBoardColumns + j);
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



        if (gui.getState().equals(State.PLAY) && pickedCoords.size() < 3 && !sentPicked) {

            ImageView clickedImageView = (ImageView) mouseEvent.getSource();
            int clickedIndex = gridBoard.getChildren().indexOf(clickedImageView);
            clickedColumn = clickedIndex / numberOfBoardColumns;
            clickedRow = clickedIndex % numberOfBoardColumns;

            System.out.println("Clicked on row: " + clickedRow + " column: " + clickedColumn);

            //starts picking the cards (specular to pickcards)
            Player me = null;
            int maxCards = 0;

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
                }
            }
            if (maxCards > 3) maxCards = 3;

            if(pickedNum < maxCards) {
                //checking coordinate validity
                if(!isTakeable(gui.getModelView(), clickedRow, clickedColumn, pickedCoords)) {
                    System.out.println("non takable");
                }
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
            if(pickedCoords.size() == maxCards) {
                sentPicked = true;
            }
        }
    }

    public void updateGraphics() {
        pickedNum = 0;
        sentPicked = false;
        pickedCoords = new ArrayList<>();

        Platform.runLater(() -> {
            ItemCard[][] tileMatrix = gui.getModelView().getBoard();

            //prints the board for debugging
            for (int i = 0; i < numberOfBoardRows; i++) {
                for (int j = 0; j < numberOfBoardColumns; j++)
                    if(tileMatrix[i][j] == null)
                        System.out.print("x");
                    else
                        switch (tileMatrix[i][j].getType()) {
                            case CATS -> System.out.print("C ");
                            case BOOKS -> System.out.print("B ");
                            case PLANTS -> System.out.print("P ");
                            case TROPHIES -> System.out.print("T ");
                            case FRAMES -> System.out.print("F ");
                            case GAMES -> System.out.print("G ");
                        }
                System.out.println();
            }
            //PRINTS THE SHELVES FOR DEBUGGING
            System.out.println("SHELVES:");
            for (Player p : gui.getModelView().getPlayers()) {
                System.out.println(p.getName() + ":");
                for (int i = 0; i < numberOfRows; i++) {
                    for (int j = 0; j < numberOfColumns; j++)
                        if (gui.getModelView().getShelfOf(p)[i][j] == null)
                            System.out.print("x");
                        else
                            switch (gui.getModelView().getShelfOf(p)[i][j].getType()) {
                                case CATS -> System.out.print("C ");
                                case BOOKS -> System.out.print("B ");
                                case PLANTS -> System.out.print("P ");
                                case TROPHIES -> System.out.print("T ");
                                case FRAMES -> System.out.print("F ");
                                case GAMES -> System.out.print("G ");
                            }
                    System.out.println();
                }
            }

            for (int i = 0; i < numberOfBoardRows; i++) {
                for (int j = 0; j < numberOfBoardColumns; j++) {
                    ImageView imageView = (ImageView) gridBoard.getChildren().get(j * numberOfBoardColumns + i);
                    if (tileMatrix[i][j] != null) {
                        Image newImage = new Image(getTilePath(tileMatrix[i][j]));
                        imageView.setImage(newImage);
                    }else{
                        Image newImage = new Image("/graphicalResources/itemTiles/Empty.png");
                        imageView.setImage(newImage);
                    }
                }
            }
        });
    }
}

