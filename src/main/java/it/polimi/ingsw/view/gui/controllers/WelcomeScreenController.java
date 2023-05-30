package it.polimi.ingsw.view.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    public TextField insert_nickname;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

    public void changeScene(javafx.event.ActionEvent actionEvent) throws IOException {
        root = loadResource("PlayingScreen.fxml", root);
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
