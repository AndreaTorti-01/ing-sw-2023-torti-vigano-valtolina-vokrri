package it.polimi.ingsw.view.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiApp extends Application {
    private static final String fxmlPath = "/graphicalResources/fxml/";
    private Stage mainStage;
    private Parent root = new Parent() {
    };

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        root = loadResource("WelcomeScreen.fxml", root);

        Scene scene = new Scene(root);

        mainStage.setScene(scene);
        mainStage.setResizable(true);
        mainStage.setMaximized(false);
        mainStage.setFullScreen(false);
        mainStage.setFullScreenExitHint("");
        mainStage.setTitle("MyShelfie ~ gc_33");
        mainStage.show();
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
