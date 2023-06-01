package it.polimi.ingsw.view.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ShelfController implements Initializable {

    public GridPane grid;
    private int shelfIdentifier;

    public void Setup(int shelfIdentifier){
        this.shelfIdentifier = shelfIdentifier;
    }
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        grid.getCellBounds(0,0);
    }
}
