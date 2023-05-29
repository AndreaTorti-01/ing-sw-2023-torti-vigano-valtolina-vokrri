package it.polimi.ingsw.view.gui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeScreenController implements StdController {
    private static final String fxmlPath = "/graphicalResources/fxml/";
    public ImageView nickname_label;
    public TextField insert_nickname;
    private Stage stage;
    private Scene scene;
    private Parent root;

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
