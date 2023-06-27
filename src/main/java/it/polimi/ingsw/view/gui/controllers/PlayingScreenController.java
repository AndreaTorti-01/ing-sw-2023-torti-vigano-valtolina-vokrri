package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.commonGoalCards.CommonGoalCard;
import it.polimi.ingsw.network.client.ClientImpl;
import it.polimi.ingsw.network.serializable.ChatMsg;
import it.polimi.ingsw.utils.Common;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.GuiApp;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.Bloom;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static it.polimi.ingsw.utils.Common.*;

/**
 * Controller for the playing screen.
 */
public class PlayingScreenController implements Initializable {
    /**
     * FXML file path.
     */
    private static final String fxmlPath = "/graphicalResources/fxml/";
    /**
     * The name of the second player.
     */
    public Text player1Name;
    /**
     * The name of the third player.
     */
    public Text player2Name;
    /**
     * The name of the fourth player.
     */
    public Text player3Name;
    /**
     * The first player score label.
     */
    public Text scoreName0;
    /**
     * The second player score label.
     */
    public Text scoreName1;
    /**
     * The third player score label.
     */
    public Text scoreName2;
    /**
     * The fourth player score label.
     */
    public Text scoreName3;
    /**
     * The first player score.
     */
    public Text score0;
    /**
     * The second player score.
     */
    public Text score1;
    /**
     * The third player score.
     */
    public Text score2;
    /**
     * The fourth player score.
     */
    public Text score3;
    /**
     * The personal goal card of the player.
     */
    public ImageView personalGoal;
    /**
     * The first common goal card in the game.
     */
    public ImageView commonGoal1;
    /**
     * The second common goal card in the game.
     */
    public ImageView commonGoal2;
    /**
     * The points associated with the first common goal card.
     */
    public ImageView commonPoint1;
    /**
     * The points associated with the second common goal card.
     */
    public ImageView commonPoint2;
    /**
     * The first card picked by the player.
     */
    public ImageView picked0;
    /**
     * The second card picked by the player.
     */
    public ImageView picked1;
    /**
     * The third card picked by the player.
     */
    public ImageView picked2;
    /**
     * The text label that indicates whose player turn it is.
     */
    public Text turn;
    /**
     * The text area where the cat is displayed.
     */
    public TextArea ChatTextArea;
    /**
     * The text field where the player can write a message.
     */
    public TextField messageSpace;
    /**
     * The button that sends the message.
     */
    public Button sendMessage;
    /**
     * The background image of the playing scene.
     */
    public GridPane gridPaneBG;
    /**
     * The root of the FXML scene.
     */
    private Parent root;
    /**
     * The gui instance.
     */
    private Gui gui;
    /**
     * The index of the picked card to switch.
     */
    private int orderSwitcher = -1;
    /**
     * The board controller.
     */
    @FXML
    private BoardController boardController;
    /**
     * The first shelf controller.
     */
    @FXML
    private Shelf0Controller shelf0Controller;
    /**
     * The second shelf controller.
     */
    @FXML
    private Shelf1Controller shelf1Controller;
    /**
     * The third shelf controller.
     */
    @FXML
    private Shelf2Controller shelf2Controller;
    /**
     * The fourth shelf controller.
     */
    @FXML
    private Shelf3Controller shelf3Controller;
    /**
     * The size of the client chat considering also help and error messages .
     */
    private int localChatSize = 0;

    /**
     * Prints the provided message to the chat text area.
     * Only used to print help and error messages.
     *
     * @param message the message to be printed.
     */
    public void printOnChat(String message) {
        Platform.runLater(() -> ChatTextArea.appendText(message + "\n"));
    }

    /**
     * Handles the chat messages whenever the send button is clicked.
     */
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

    /**
     * Refreshes the chat text area whenever a new message is received.
     */
    public void refreshChat() {
        for (int i = localChatSize; i < gui.getModelView().getMessages().size(); i++) {
            ChatMsg message = gui.getModelView().getMessages().get(i);
            if (message.isPublic() || !message.isPublic() && (message.getRecipientPlayer().equals(gui.getPlayerName()) || message.getSenderPlayer().equals(gui.getPlayerName()))) {
                Platform.runLater(() -> ChatTextArea.appendText("[" + message.getSenderPlayer() + "] : " + message.getMessage() + "\n"));
                localChatSize++;
            }
        }
    }

    /**
     * FXML standard method: initializes the background image and the gui.
     *
     * @param url            ignored.
     * @param resourceBundle ignored.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gridPaneBG.setStyle("-fx-background-image: url('/graphicalResources/misc/sfondo_parquet.jpg'); -fx-background-size: stretch; -fx-background-repeat: no-repeat; -fx-background-position: center center;");

        gui = (Gui) ClientImpl.getView();
        picked0.setImage(null);
        picked1.setImage(null);
        picked2.setImage(null);
        ChatTextArea.setEditable(false);
    }

    /**
     * Updates the graphics of the scene.
     */
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
                            player1Name.setText(player.getName());
                            scoreName1.setText(player.getName());
                            score1.setText(" " + player.getScore());
                            plID++;
                        }
                        case 2 -> {
                            shelf2Controller.updateGraphics(player.getShelf().getItems());
                            player2Name.setText(player.getName());
                            scoreName2.setText(player.getName());
                            score2.setText(" " + player.getScore());
                            plID++;
                        }
                        case 3 -> {
                            shelf3Controller.updateGraphics(player.getShelf().getItems());
                            player3Name.setText(player.getName());
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

    /**
     * Gets the board controller.
     *
     * @return the board controller.
     */
    public BoardController getBoardController() {
        return boardController;
    }

    /**
     * Gets the first shelf controller.
     *
     * @return the first shelf controller.
     */
    public Shelf0Controller getShelf0Controller() {
        return shelf0Controller;
    }

    /**
     * Displays the picked cards.
     *
     * @param cards the cards to be displayed.
     */
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

    /**
     * Reorders the picked cards switching the card at index 0 with the card at index orderSwitcher.
     *
     * @param mouseEvent ignored.
     */
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

    /**
     * Reorders the picked cards switching the card at index 1 with the card at index orderSwitcher.
     *
     * @param mouseEvent ignored.
     */
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

    /**
     * Reorders the picked cards switching the card at index 2 with the card at index orderSwitcher.
     *
     * @param mouseEvent ignored
     */
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

    /**
     * Changes the current scene.
     */
    public void changeScene() {
        GuiApp.changeScene(GuiApp.getEndScreenRoot());
    }
}
