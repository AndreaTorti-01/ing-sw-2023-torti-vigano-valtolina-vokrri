package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.network.client.ClientImpl;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.Gui.State;
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

public class WelcomeScreenController implements Initializable {
    @FXML
    private static final String fxmlPath = "/graphicalResources/fxml/";
    @FXML
    public TextField textField;
    @FXML
    public Button enterButton;
    @FXML
    public Button button4;
    @FXML
    public Button button3;
    @FXML
    public Button button2;
    @FXML
    public Label askerLabel;
    @FXML
    public GridPane gridPaneBG;

    //other parameters
    private Gui gui;
    private String playerName;
    boolean insertedName = false;
    private int numberOfPlayers;


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
    public void resetAskName(){
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
    public void ButtonAction(ActionEvent actionEvent) {

        System.out.println("ButtonAction called");
        if(!textField.getText().isEmpty() && !insertedName){
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
    public void askNumber(){
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

    public void Player2(ActionEvent actionEvent) {
        this.numberOfPlayers = 2;
        gui.setPlayerNumber(numberOfPlayers);
        waitForPlayers();
    }

    public void Player3(ActionEvent actionEvent) {
        this.numberOfPlayers = 3;
        gui.setPlayerNumber(numberOfPlayers);
        waitForPlayers();
    }

    public void Player4(ActionEvent actionEvent) {
        this.numberOfPlayers = 4;
        gui.setPlayerNumber(numberOfPlayers);
        waitForPlayers();
    }
    public void changeScene(){
        GuiApp.changeScene(GuiApp.getPlayingScreenRoot());
    }
    public void waitForPlayers(){
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
