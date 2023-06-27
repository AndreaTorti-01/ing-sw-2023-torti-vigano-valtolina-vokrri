package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.controllers.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * GUI App main class.
 */
public class GuiApp extends Application {
    /**
     * The path to the fxml files.
     */
    private static final String fxmlPath = "/graphicalResources/fxml/";
    /**
     * Whether the controllers have been created or not.
     */
    private static boolean createdController = false;
    /**
     * The main stage of the application.
     */
    private static Stage mainStage;
    /**
     * The controller of the Welcome Screen.
     */
    private static WelcomeScreenController welcomeScreenController;
    /**
     * The controller of the Board.
     */
    private static BoardController boardController;
    /**
     * The controller of the End Screen.
     */
    private static EndScreenController endScreenController;
    /**
     * The controller of the Playing Screen.
     */
    private static PlayingScreenController playingScreenController;
    /**
     * The controller of the player's shelf.
     */
    private static Shelf0Controller shelf0Controller;
    /**
     * The root of the fxml files.
     */
    private static Parent welcomeScreenRoot, endScreenRoot, playingScreenRoot;

    /**
     * Starts the application.
     *
     * @param args the arguments of the application.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Gets the End Screen fxml root.
     *
     * @return the End Screen fxml root.
     */
    public static Parent getEndScreenRoot() {
        return endScreenRoot;
    }

    /**
     * Gets the Playing Screen fxml root.
     *
     * @return the Playing Screen fxml root.
     */
    public static Parent getPlayingScreenRoot() {
        return playingScreenRoot;
    }

    /**
     * Changes the scene of the main stage.
     *
     * @param root the root of the new scene.
     */
    public static void changeScene(Parent root) {
        Platform.runLater(() -> mainStage.getScene().setRoot(root));
    }

    /**
     * Gets the Welcome Screen controller.
     *
     * @return the Welcome Screen controller.
     */
    public static WelcomeScreenController getWelcomeScreenController() {
        return welcomeScreenController;
    }

    /**
     * Gets the Board controller.
     *
     * @return the Board controller.
     */
    public static BoardController getBoardController() {
        return boardController;
    }

    /**
     * Gets the End Screen controller.
     *
     * @return the End Screen controller.
     */
    public static EndScreenController getEndScreenController() {
        return endScreenController;
    }

    /**
     * Gets the Playing Screen controller.
     *
     * @return the Playing Screen controller.
     */
    public static PlayingScreenController getPlayingScreenController() {
        return playingScreenController;
    }

    /**
     * Gets the controller of the player's shelf.
     *
     * @return the Shelf controller.
     */
    public static Shelf0Controller getShelf0Controller() {
        return shelf0Controller;
    }

    /**
     * Checks if the controllers have been created.
     *
     * @return true if the controllers have been created, false otherwise.
     */
    public static boolean controllersAvailable() {
        return createdController;
    }

    /**
     * Starts the application.
     *
     * @param stage the main stage of the application.
     * @throws IOException if the fxml files are not found.
     */
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
        mainStage.setTitle("MyShelfie");
        mainStage.show();

        //stops gui if closed
        mainStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }
}
