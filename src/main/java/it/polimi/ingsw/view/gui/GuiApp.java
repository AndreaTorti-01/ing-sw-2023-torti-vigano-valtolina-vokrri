package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.controllers.*;
import javafx.application.Application;
import javafx.application.Platform;
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
    private static Shelf0Controller shelf0Controller;
    private static Parent welcomeScreenRoot, endScreenRoot, playingScreenRoot;
    private final Parent root = new Parent() {
    };

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;

        FXMLLoader loader;


        loader = new FXMLLoader(getClass().getResource(fxmlPath + "WelcomeScreen.fxml"));
        try {
            welcomeScreenRoot = loader.load();
            welcomeScreenController = loader.getController();
        } catch (IOException e) {
            System.exit(1);
        }

        loader = new FXMLLoader(getClass().getResource(fxmlPath + "EndScreen.fxml"));
        try {
            endScreenRoot = loader.load();
            endScreenController = loader.getController();
        } catch (IOException e) {
            System.exit(1);
        }

        loader = new FXMLLoader(getClass().getResource(fxmlPath + "PlayingScreen.fxml"));
        try {
            playingScreenRoot = loader.load();
            playingScreenController = loader.getController();
        } catch (IOException e) {
            System.exit(1);
        }

        boardController = playingScreenController.getBoardController();
        shelf0Controller = playingScreenController.getShelf0Controller();

        //to simply resolve synchronization problems between gui and guiApp
        createdController = true;

        Scene scene = new Scene(welcomeScreenRoot);
        mainStage.setScene(scene);
        mainStage.setMinWidth(1600);
        mainStage.setMinHeight(900);
        mainStage.setResizable(true);
        mainStage.setMaximized(false);
        mainStage.setFullScreen(false);
        mainStage.setFullScreenExitHint("");
        mainStage.setTitle("MyShelfie ~ gc_33");
        mainStage.show();

        //stops gui if closed
        mainStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public static Parent getEndScreenRoot() {
        return endScreenRoot;
    }
    public static Parent getPlayingScreenRoot() {
        return playingScreenRoot;
    }
    public static void changeScene(Parent root) {
        Platform.runLater(() -> mainStage.getScene().setRoot(root));
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
    public static Shelf0Controller getShelf0Controller() {
        return shelf0Controller;
    }
    public static boolean controllersAvailable() {
        return createdController;
    }

}
