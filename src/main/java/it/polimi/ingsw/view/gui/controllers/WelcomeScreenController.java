package it.polimi.ingsw.view.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeScreenController implements Initializable {
    @FXML
    private static final String fxmlPath = "/graphicalResources/fxml/";
    @FXML
    public ImageView nickname_label;
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
    private boolean insertedName = false;
    private int numberOfPlayers;
    private String PlayerName;
    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    @Override
    //initialize method is standard-called when the fxml file is loaded
    public void initialize(URL url, ResourceBundle resourceBundle) {
        enterButton.setText("Enter");
        askerLabel.setText("Insert your nickname");

        button2.setVisible(false);
        button3.setVisible(false);
        button4.setVisible(false);

        button2.setDisable(true);
        button3.setDisable(true);
        button4.setDisable(true);

        this.numberOfPlayers = 0;
        this.PlayerName = "";
    }
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public String getNickname() {
        return PlayerName;
    }
    public Parent loadResource(String fxmlName, Parent root) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath + fxmlName));
        try {
            root = loader.load();
        } catch (IOException e) {
            System.exit(1);
        }
        return root;
    }
    public void changeScene(String sceneName){
        root = loadResource(sceneName, root);
        stage = (Stage) nickname_label.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void ButtonAction(ActionEvent actionEvent) {
        if(!textField.getText().isEmpty() && !insertedName){
            this.PlayerName = textField.getText();
            insertedName = true;
            textField.clear();
            askerLabel.setText("How many players?");

            textField.setVisible(false);
            textField.setDisable(true);

            enterButton.setVisible(false);
            button2.setVisible(true);
            button3.setVisible(true);
            button4.setVisible(true);

            enterButton.setDisable(true);
            button2.setDisable(false);
            button3.setDisable(false);
            button4.setDisable(false);
        }
    }

    public void Player2(ActionEvent actionEvent) {
        this.numberOfPlayers = 2;
        waitingRoom();
    }

    public void Player3(ActionEvent actionEvent) {
        this.numberOfPlayers = 3;
        waitingRoom();
    }

    public void Player4(ActionEvent actionEvent) {
        this.numberOfPlayers = 4;
        waitingRoom();
    }

    public void waitingRoom() {
        button2.setVisible(false);
        button3.setVisible(false);
        button4.setVisible(false);

        button2.setDisable(true);
        button3.setDisable(true);
        button4.setDisable(true);

        askerLabel.setText("Waiting for other players...");
    }
}
