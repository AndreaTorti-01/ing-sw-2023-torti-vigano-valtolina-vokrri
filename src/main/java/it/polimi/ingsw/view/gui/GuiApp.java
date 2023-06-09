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
    private static ShelfController shelfController;
    private static Parent welcomeScreenRoot, endScreenRoot, playingScreenRoot;
    private Parent root = new Parent() {
    };

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;

        FXMLLoader loader;

        loader = new FXMLLoader(getClass().getResource(fxmlPath + "Board.fxml"));
        try {
            root = loader.load();
            boardController = loader.getController();
        } catch (IOException e) {
            System.exit(1);
        }

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

        loader = new FXMLLoader(getClass().getResource(fxmlPath + "Shelf0.fxml"));
        try {
            Parent shelfRoot = loader.load();
            shelfController = loader.getController();
        } catch (IOException e) {
            System.exit(1);
        }

        //to simply resolve synchronization problems between gui and guiApp
        createdController = true;

        Scene scene = new Scene(welcomeScreenRoot);
        mainStage.setScene(scene);
        mainStage.setResizable(true);
        mainStage.setMaximized(false);
        mainStage.setFullScreen(false);
        mainStage.setFullScreenExitHint("");
        mainStage.setTitle("MyShelfie ~ gc_33");
        mainStage.show();
    }

    public static Parent getWelcomeScreenRoot() {
        return welcomeScreenRoot;
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
    public static ShelfController getShelfController() {
        return shelfController;
    }
    public static boolean controllersAvailable() {
        return createdController;
    }
    public static Stage getMainStage() {
        return mainStage;
    }
    public FXMLLoader loadResource(String fxmlName, Parent root, FXMLLoader loader) {
        loader = new FXMLLoader(getClass().getResource(fxmlPath + fxmlName));
        try {
            root = loader.load();
        } catch (IOException e) {
            System.exit(1);
        }
        return loader;
    }
}
