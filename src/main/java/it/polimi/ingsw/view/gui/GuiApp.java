package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.controllers.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiApp extends Application {
    private static final String fxmlPath = "/graphicalResources/fxml/";
    private static boolean createdController = false;
    private static Stage mainStage;
    private static WelcomeScreenController welcomeScreenController;
    private static BoardController boardController;
    private static EndScreenController endScreenController;
    private static PlayingScreenController playingScreenController;
    private static ShelfController shelfController;
    private Parent root = new Parent() {
    };

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath + "WelcomeScreen.fxml"));
        try {
            root = loader.load(); // can throw IOException
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        welcomeScreenController = loader.getController();
        createdController = true;


        Scene scene = new Scene(root);
        mainStage.setScene(scene);
        mainStage.setResizable(true);
        mainStage.setMaximized(false);
        mainStage.setFullScreen(false);
        mainStage.setFullScreenExitHint("");
        mainStage.setTitle("MyShelfie ~ gc_33");
        mainStage.show();
    }

    public static WelcomeScreenController getWelcomeScreenController() {
        return welcomeScreenController;
    }
    public static BoardController getBoardController() {
        return boardController;
    }
    public static EndScreenController getEndScreenController() {
        return endScreenController;
    }
    public static PlayingScreenController getPlayingScreenController() {
        return playingScreenController;
    }
    public static ShelfController getShelfController() {
        return shelfController;
    }
    public static boolean controllersAvailable() {
        return createdController;
    }
    public static Stage getMainStage() {
        return mainStage;
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
