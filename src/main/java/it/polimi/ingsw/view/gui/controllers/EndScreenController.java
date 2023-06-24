package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.view.gui.Gui;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class EndScreenController implements Initializable {
    public Text Player1name;
    public Text Player2name;
    public Text Player3name;
    public Text Player4name;
    public Text score1;
    public Text score2;
    public Text score3;
    public Text score4;
    public Text title;
    public GridPane gridPaneBG;
    private Gui gui;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gridPaneBG.setStyle("-fx-background-image: url('/graphicalResources/oak_wood_planks.jpg'); -fx-background-size: stretch; -fx-background-repeat: no-repeat; -fx-background-position: center center;");
    }

    public void updateGraphics(){
        gui = (Gui) it.polimi.ingsw.network.client.ClientImpl.getView();

        Platform.runLater(
                () -> {
                    Player1name.setText(gui.getModelView().getPlayers().get(0).getName());
                    Player2name.setText(gui.getModelView().getPlayers().get(1).getName());
                    Player3name.setText("");
                    Player4name.setText("");
                    score1.setText(" " + gui.getModelView().getPlayers().get(0).getScore());
                    score2.setText(" " + gui.getModelView().getPlayers().get(1).getScore());
                    score3.setText("");
                    score4.setText("");
                    title.setText(gui.getModelView().getWinner().getName() + " won the game!");

                    switch (gui.getModelView().getPlayers().size()) {
                        case 3 -> {
                            Player3name.setText(gui.getModelView().getPlayers().get(2).getName());
                            score3.setText(" " + gui.getModelView().getPlayers().get(2).getScore());
                        }
                        case 4 -> {
                            Player3name.setText(gui.getModelView().getPlayers().get(2).getName());
                            Player4name.setText(gui.getModelView().getPlayers().get(3).getName());
                            score3.setText(" " + gui.getModelView().getPlayers().get(2).getScore());
                            score4.setText(" " + gui.getModelView().getPlayers().get(3).getScore());
                        }
                    }
                }
        );
    }

}
