package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.gui.Gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class PlayingScreenController implements Initializable {

    private static final String fxmlPath = "/graphicalResources/fxml/";
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Gui gui;
    @FXML
    private BoardController boardController;
    @FXML
    private Shelf0Controller shelf0Controller;
    @FXML
    private Shelf1Controller shelf1Controller;
    @FXML
    private Shelf2Controller shelf2Controller;
    @FXML
    private Shelf3Controller shelf3Controller;

    public Parent loadResource(String fxmlName, Parent root) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath + fxmlName));
        try {
            root = loader.load();
        } catch (IOException e) {
            System.exit(1);
        }
        return root;
    }

    public void changeScene(ActionEvent actionEvent) throws IOException {
        root = loadResource("EndScreen.fxml", root);
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gui = (Gui) it.polimi.ingsw.network.client.ClientImpl.getView();
    }

    public void updateGraphics() {

        for (Player player : gui.getModelView().getPlayers()) {
            int plID = 1;
            if (!gui.getPlayerName().equals(player.getName())) {
                switch (plID) {
                    case 1: {
                        shelf1Controller.updateGraphics(player.getShelf().getItems());
                        //TODO setting name
                        break;
                    }
                    case 2: {
                        shelf2Controller.updateGraphics(player.getShelf().getItems());
                        //TODO setting name
                        break;
                    }
                    case 3: {
                        shelf3Controller.updateGraphics(player.getShelf().getItems());
                        //TODO setting name
                        break;
                    }
                }
                plID++;
            }
        }
    }

    public BoardController getBoardController() {
        return boardController;
    }

    public Shelf0Controller getShelf0Controller() {
        return shelf0Controller;
    }

    public Shelf1Controller getShelf1Controller() {
        return shelf1Controller;
    }

    public Shelf2Controller getShelf2Controller() {
        return shelf2Controller;
    }

    public Shelf3Controller getShelf3Controller() {
        return shelf3Controller;
    }
}
