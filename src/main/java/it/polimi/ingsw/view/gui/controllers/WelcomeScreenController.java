package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.network.client.ClientImpl;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.GuiApp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the welcome screen.
 */
public class WelcomeScreenController implements Initializable {

    /**
     * The text field where the player inserts his name.
     */
    @FXML
    public TextField textField;
    /**
     * The button to enter the game.
     */
    @FXML
    public Button enterButton;
    /**
     * The button to set number of players to 2.
     */
    @FXML
    public Button button2;
    /**
     * The button to set number of players to 3.
     */
    @FXML
    public Button button3;
    /**
     * The button to set number of players to 4.
     */
    @FXML
    public Button button4;
    /**
     * The label that asks the player to insert his name.
     */
    @FXML
    public Label askerLabel;
    /**
     * The background image of the welcome scene.
     */
    @FXML
    public GridPane gridPaneBG;
    /**
     * Whether the player has inserted his name or not.
     */
    boolean insertedName = false;
    /**
     * The gui instance.
     */
    private Gui gui;
    /**
     * The name of the player.
     */
    private String playerName;
    /**
     * The number of players.
     */
    private int numberOfPlayers;

    /**
     * FXML standard method: initializes the background image and disables the buttons in order not to be displayed.
     *
     * @param url            ignored.
     * @param resourceBundle ignored.
     */
    @FXML
    @Override
    //initialize method is standard-called when the fxml file is loaded
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.gui = (Gui) ClientImpl.getView();
        //set scene background
        gridPaneBG.setStyle("-fx-background-image: url('/graphicalResources/home_my_shelfie.jpg'); -fx-background-size: stretch; -fx-background-repeat: no-repeat; -fx-background-position: center center;");

        enterButton.setText("Enter");
        askerLabel.setText("Insert your nickname");

        button2.setVisible(false);
        button3.setVisible(false);
        button4.setVisible(false);

        button2.setDisable(true);
        button3.setDisable(true);
        button4.setDisable(true);

        playerName = "";
        numberOfPlayers = 0;
    }

    /**
     * Lets the player insert another name.
     */
    public void resetAskName() {
        Platform.runLater(() -> {
            askerLabel.setText("Insert your nickname");
            textField.setVisible(true);
            textField.setDisable(false);

            enterButton.setVisible(true);
            enterButton.setDisable(false);
            playerName = "";
            insertedName = false;
        });
    }

    /**
     * Sets the player name and disables the text field and the button.
     *
     * @param actionEvent ignored.
     */
    public void ButtonAction(ActionEvent actionEvent) {

        System.out.println("ButtonAction called");
        if (!textField.getText().isEmpty() && !insertedName) {
            this.playerName = textField.getText();

            gui.setPlayerName(playerName);

            insertedName = true;
            textField.clear();
            askerLabel.setText("");

            textField.setVisible(false);
            textField.setDisable(true);

            enterButton.setVisible(false);
            enterButton.setDisable(true);
        }
    }

    /**
     * Asks the player to click the desired number of players.
     */
    public void askNumber() {
        Platform.runLater(() -> {
            askerLabel.setText("How many players?");
            button2.setVisible(true);
            button3.setVisible(true);
            button4.setVisible(true);

            button2.setDisable(false);
            button3.setDisable(false);
            button4.setDisable(false);
        });
    }

    /**
     * Sets the number of players to 2.
     *
     * @param actionEvent ignored.
     */
    public void Player2(ActionEvent actionEvent) {
        this.numberOfPlayers = 2;
        gui.setPlayerNumber(numberOfPlayers);
        waitForPlayers();
    }

    /**
     * Sets the number of players to 3.
     *
     * @param actionEvent ignored.
     */
    public void Player3(ActionEvent actionEvent) {
        this.numberOfPlayers = 3;
        gui.setPlayerNumber(numberOfPlayers);
        waitForPlayers();
    }

    /**
     * Sets the number of players to 4.
     *
     * @param actionEvent ignored.
     */
    public void Player4(ActionEvent actionEvent) {
        this.numberOfPlayers = 4;
        gui.setPlayerNumber(numberOfPlayers);
        waitForPlayers();
    }

    /**
     * Changes the scene to the playing screen.
     */
    public void changeScene() {
        GuiApp.changeScene(GuiApp.getPlayingScreenRoot());
    }

    /**
     * Displays a waiting screen by disabling all the buttons and labels.
     */
    public void waitForPlayers() {
        Platform.runLater(() -> {
            askerLabel.setText("Waiting for other players...");
            button2.setVisible(false);
            button3.setVisible(false);
            button4.setVisible(false);

            button2.setDisable(true);
            button3.setDisable(true);
            button4.setDisable(true);
        });
    }
}
