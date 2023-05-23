package it.polimi.ingsw.view.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Gui extends Application {
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("scena1.fxml")));
        primaryStage.setTitle("My Shelfie");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}