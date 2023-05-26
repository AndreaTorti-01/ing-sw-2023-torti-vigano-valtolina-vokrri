package it.polimi.ingsw.view.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class Gui extends Application {
    Stage mainStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        mainStage = primaryStage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphicalResources/fxml/scena1.fxml"));

        Parent root = loader.load();

        mainStage.setTitle("MyShelfie");
        mainStage.setScene(new javafx.scene.Scene(root));
        mainStage.show();

    }
}
