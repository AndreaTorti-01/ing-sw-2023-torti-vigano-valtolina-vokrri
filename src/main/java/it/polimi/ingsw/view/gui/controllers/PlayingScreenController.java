package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.commonGoalCards.CommonGoalCard;
import it.polimi.ingsw.network.serializable.ChatMsg;
import it.polimi.ingsw.utils.Common;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.GuiApp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.Bloom;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static it.polimi.ingsw.utils.Common.*;


public class PlayingScreenController implements Initializable {

    private static final String fxmlPath = "/graphicalResources/fxml/";
    public Text playerName2;
    public Text playerName1;
    public Text scoreName0;
    public Text score0;
    public ImageView commonGoal1;
    public ImageView commonGoal2;
    public ImageView personalGoal;
    public TextArea ChatTextArea;
    public TextField messageSpace;
    public Button sendMessage;
    public ScrollBar chatScrollBar;
    public Text score3;
    public Text scoreName3;
    public Text score2;
    public Text scoreName2;
    public Text score1;
    public Text scoreName1;
    public Text turn;
    public Text playerName3;
    public ImageView commonPoint2;
    public ImageView commonPoint1;
    public ImageView picked0;
    public ImageView picked1;
    public ImageView picked2;
    public GridPane gridPaneBG;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Gui gui;
    private int orderSwitcher = -1;
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
    private int localChatSize = 0;

    public Parent loadResource(String fxmlName, Parent root) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath + fxmlName));
        try {
            root = loader.load();
        } catch (IOException e) {
            System.exit(1);
        }
        return root;
    }

    public void printOnChat(String message) {
        Platform.runLater(() -> ChatTextArea.appendText(message + "\n"));
    }

    @FXML
    private void handleMessage() {
        //gestisce l'invio di messaggi alla gui
        String message = messageSpace.getText();
        if (!message.equals("")) {
            System.out.println("Message sent");
            gui.setMessage(message);
            messageSpace.clear();
        }
    }

    public void refreshChat() {

        for (int i = localChatSize; i < gui.getModelView().getMessages().size(); i++) {
            ChatMsg message = gui.getModelView().getMessages().get(i);
            if (message.isPublic() || !message.isPublic() && (message.getRecipientPlayer().equals(gui.getPlayerName()) || message.getSenderPlayer().equals(gui.getPlayerName()))) {
                Platform.runLater(() -> ChatTextArea.appendText("[" + message.getSenderPlayer() + "] : " + message.getMessage() + "\n"));
                localChatSize++;
            }
        }
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
        gridPaneBG.setStyle("-fx-background-image: url('/graphicalResources/misc/sfondo_parquet.jpg'); -fx-background-size: stretch; -fx-background-repeat: no-repeat; -fx-background-position: center center;");

        gui = (Gui) it.polimi.ingsw.network.client.ClientImpl.getView();
        picked0.setImage(null);
        picked1.setImage(null);
        picked2.setImage(null);
        ChatTextArea.setEditable(false);
    }

    public void updateGraphics() {

        Platform.runLater(() -> {
            if (gui.getPlayerName().equals(gui.getModelView().getCurrentPlayer().getName()))
                turn.setText("It's your turn!");
            else turn.setText("It's " + gui.getModelView().getCurrentPlayer().getName() + "'s turn!");

            int plID = 1;

            for (Player player : gui.getModelView().getPlayers()) {
                if (!gui.getPlayerName().equals(player.getName())) {
                    switch (plID) {
                        case 1 -> {
                            shelf1Controller.updateGraphics(player.getShelf().getItems());
                            playerName1.setText(player.getName());
                            scoreName1.setText(player.getName());
                            score1.setText(" " + player.getScore());
                            plID++;
                        }
                        case 2 -> {
                            shelf2Controller.updateGraphics(player.getShelf().getItems());
                            playerName2.setText(player.getName());
                            scoreName2.setText(player.getName());
                            score2.setText(" " + player.getScore());
                            plID++;
                        }
                        case 3 -> {
                            shelf3Controller.updateGraphics(player.getShelf().getItems());
                            playerName3.setText(player.getName());
                            scoreName3.setText(player.getName());
                            score3.setText(" " + player.getScore());
                            plID++;
                        }
                    }
                } else {
                    scoreName0.setText(player.getName());
                    score0.setText(" " + player.getScore());
                }
            }
            List<CommonGoalCard> commonCards = gui.getModelView().getCommonGoalCards();

            commonGoal1.setImage(new Image(getCommonGoalCardPath(commonCards.get(0).getType())));
            commonGoal2.setImage(new Image(getCommonGoalCardPath(commonCards.get(1).getType())));
            commonPoint1.setImage(new Image(getCommonGoalCardPointPath(commonCards.get(0).peekPoints())));
            commonPoint2.setImage(new Image(getCommonGoalCardPointPath(commonCards.get(1).peekPoints())));

            personalGoal.setImage(new Image(getPersonalGoalCardPath(gui.getModelView().getPlayerByName(gui.getPlayerName()).getPersonalGoalCard().getID())));
        });
    }

    public BoardController getBoardController() {
        return boardController;
    }

    public Shelf0Controller getShelf0Controller() {
        return shelf0Controller;
    }

    public void showPickedTypes(List<ItemCard> cards) {
        Platform.runLater(
                () -> {
                    switch (cards.size()) {
                        case 0 -> {
                            picked0.setImage(null);
                            picked1.setImage(null);
                            picked2.setImage(null);
                        }
                        case 1 -> {
                            picked0.setImage(new Image(Common.getTilePath(cards.get(0))));
                            picked1.setImage(null);
                            picked2.setImage(null);
                        }
                        case 2 -> {
                            picked0.setImage(new Image(Common.getTilePath(cards.get(0))));
                            picked1.setImage(new Image(Common.getTilePath(cards.get(1))));
                            picked2.setImage(null);
                        }
                        case 3 -> {
                            picked0.setImage(new Image(Common.getTilePath(cards.get(0))));
                            picked1.setImage(new Image(Common.getTilePath(cards.get(1))));
                            picked2.setImage(new Image(Common.getTilePath(cards.get(2))));
                        }
                    }
                }
        );
    }

    public void reorderInput0(MouseEvent mouseEvent) {
        if (gui.getPickedCoords().size() == 0) return;
        if (orderSwitcher == -1) {
            orderSwitcher = 0;
            picked0.setEffect(new Bloom());
        } else {
            List<List<Integer>> pickedCoords = gui.getPickedCoords();
            List<Integer> tmpCoords = pickedCoords.get(orderSwitcher);
            pickedCoords.set(orderSwitcher, pickedCoords.get(0));
            pickedCoords.set(0, tmpCoords);
            gui.setPicked(pickedCoords);

            picked0.setEffect(null);
            picked1.setEffect(null);
            picked2.setEffect(null);
            orderSwitcher = -1;
        }
    }

    public void reorderInput1(MouseEvent mouseEvent) {
        if (gui.getPickedCoords().size() == 0) return;
        if (orderSwitcher == -1) {
            orderSwitcher = 1;
            picked1.setEffect(new Bloom());
        } else {
            List<List<Integer>> pickedCoords = gui.getPickedCoords();
            List<Integer> tmpCoords = pickedCoords.get(orderSwitcher);
            pickedCoords.set(orderSwitcher, pickedCoords.get(1));
            pickedCoords.set(1, tmpCoords);
            gui.setPicked(pickedCoords);

            picked0.setEffect(null);
            picked1.setEffect(null);
            picked2.setEffect(null);
            orderSwitcher = -1;
        }
    }

    public void reorderInput2(MouseEvent mouseEvent) {
        if (gui.getPickedCoords().size() == 0) return;
        if (orderSwitcher == -1) {
            orderSwitcher = 2;
            picked2.setEffect(new Bloom());
        } else {
            List<List<Integer>> pickedCoords = gui.getPickedCoords();
            List<Integer> tmpCoords = pickedCoords.get(orderSwitcher);
            pickedCoords.set(orderSwitcher, pickedCoords.get(2));
            pickedCoords.set(2, tmpCoords);
            gui.setPicked(pickedCoords);

            picked0.setEffect(null);
            picked1.setEffect(null);
            picked2.setEffect(null);
            orderSwitcher = -1;
        }
    }

    public void changeScene() {
        GuiApp.changeScene(GuiApp.getEndScreenRoot());
    }
}
