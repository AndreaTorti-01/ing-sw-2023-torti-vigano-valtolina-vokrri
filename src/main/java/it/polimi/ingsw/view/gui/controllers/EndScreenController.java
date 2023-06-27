package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.network.client.ClientImpl;
import it.polimi.ingsw.view.gui.Gui;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the end screen.
 */
public class EndScreenController implements Initializable {
    /**
     * The name of the first player.
     */
    public Text player0Name;
    /**
     * The name of the second player.
     */
    public Text player1Name;
    /**
     * The name of the third player.
     */
    public Text player2Name;
    /**
     * The name of the fourth player.
     */
    public Text player3Name;
    /**
     * The score of the first player.
     */
    public Text player0Score;
    /**
     * The score of the second player.
     */
    public Text player1Score;
    /**
     * The score of the third player.
     */
    public Text player2Score;
    /**
     * The score of the fourth player.
     */
    public Text player3Score;
    /**
     * The title of the end screen
     */
    public Text winnerTitle;
    /**
     * The background image of the end scene.
     */
    public GridPane gridPaneBG;
    /**
     * The gui instance.
     */
    private Gui gui;

    /**
     * FXML standard method: initializes the background image.
     *
     * @param url            ignored.
     * @param resourceBundle ignored.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gridPaneBG.setStyle("-fx-background-image: url('/graphicalResources/misc/sfondo_parquet.jpg'); -fx-background-size: stretch; -fx-background-repeat: no-repeat; -fx-background-position: center center;");
    }

    /**
     * Updates the graphics of the end screen.
     */
    public void updateGraphics() {
        gui = (Gui) ClientImpl.getView();

        Platform.runLater(
                () -> {
                    player0Name.setText(gui.getModelView().getPlayers().get(0).getName());
                    player1Name.setText(gui.getModelView().getPlayers().get(1).getName());
                    player2Name.setText("");
                    player3Name.setText("");

                    player0Score.setText(" " + gui.getModelView().getPlayers().get(0).getScore());
                    player1Score.setText(" " + gui.getModelView().getPlayers().get(1).getScore());
                    player2Score.setText("");
                    player3Score.setText("");

                    if (gui.getModelView().getWinner().getName().equals(gui.getPlayerName()))
                        winnerTitle.setText("You won the game!");
                    else winnerTitle.setText(gui.getModelView().getWinner().getName() + " won the game!");

                    switch (gui.getModelView().getPlayers().size()) {
                        case 3 -> {
                            player2Name.setText(gui.getModelView().getPlayers().get(2).getName());
                            player2Score.setText(" " + gui.getModelView().getPlayers().get(2).getScore());
                        }
                        case 4 -> {
                            player2Name.setText(gui.getModelView().getPlayers().get(2).getName());
                            player3Name.setText(gui.getModelView().getPlayers().get(3).getName());
                            player2Score.setText(" " + gui.getModelView().getPlayers().get(2).getScore());
                            player3Score.setText(" " + gui.getModelView().getPlayers().get(3).getScore());
                        }
                    }
                }
        );
    }

}
