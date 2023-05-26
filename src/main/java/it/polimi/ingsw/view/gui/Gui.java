package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.serializable.GameViewMsg;
import it.polimi.ingsw.view.RunnableView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class Gui extends Application{
    Stage mainStage;
    private static final String fxmlPath = "/graphicalResources/fxml/";
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        mainStage = primaryStage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphicalResources/fxml/scena2.fxml"));
        Parent root = loader.load();

        mainStage.setTitle("MyShelfie - gc_33");
        mainStage.setScene(new javafx.scene.Scene(root));
        mainStage.show();
    }

    /**
     * This method is used to show the stage on screen with the specified fxml scene
     * @param fxmlName is the name of the fxml file to show
     **/
    public void setWindow(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath + fxmlName));
            Parent root = loader.load();
            mainStage.setScene(new javafx.scene.Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
