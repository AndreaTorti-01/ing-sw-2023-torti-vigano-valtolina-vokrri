package it.polimi.ingsw.view.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiApp extends Application{
    private Stage mainStage;
    private Parent root = new Parent() {};
    private static final String fxmlPath = "/graphicalResources/fxml/";
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        root = loadResource("WelcomeScreen.fxml", root);

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setWidth(1600);
        stage.setHeight(900);
        stage.setResizable(false);
        stage.setMaximized(false);
        stage.setFullScreen(false);
        stage.setFullScreenExitHint("");
        stage.setTitle("MyShelfie ~ gc_33");
        stage.show();
    }


    public void changeScene(String fxmlName) {
        root = loadResource(fxmlName, root);
        mainStage.getScene().setRoot(root);
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
}
