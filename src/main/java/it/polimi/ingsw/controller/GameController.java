package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.serializable.ChatMsg;
import it.polimi.ingsw.network.serializable.GameViewMsg;
import it.polimi.ingsw.network.serializable.MoveMsg;

import java.util.List;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

/**
 * The controller of the MVC pattern.
 */
public class GameController {
    /**
     * The state of the game.
     */
    private final Game model;

    /**
     * Creates a new game controller with the provided model.<p>
     * A new controller is created for every lobby.
     *
     * @param model the state of the game.
     */
    public GameController(Game model) {
        this.model = model;
    }

    /**
     * Initializes a new game with the provided number of players.
     *
     * @param playerNum the number of player the game should contain.
     */
    public void initGame(Integer playerNum) {
        this.model.initModel(playerNum);
    }

    /**
     * Makes the provided move and advances the turn.
     *
     * @param move the move to be made.
     */
    public boolean makeMove(MoveMsg move) {
        ItemCard card;
        GameViewMsg modelView = new GameViewMsg(model);
        int[] freeSlotsNumber = new int[numberOfColumns]; //number of max cards that can be inserted in each column
        int maxCards = 0; //maximum cards that can be picked
        Player me = model.getCurrentPlayer();

        for (int j = 0; j < numberOfColumns; j++) {
            int freeSlots = 0;
            for (int i = 0; i < numberOfRows && freeSlots < 3; i++) {
                if (modelView.getShelfOf(me)[i][j] == null) freeSlots++;
                if (freeSlots > maxCards) maxCards = freeSlots;
                freeSlotsNumber[j] = freeSlots;
            }
        }

        // check validity of the move
        if (move.getPickedCards().size() > 3) return false;
        for (List<Integer> coords : move.getPickedCards()) {
            if (coords.get(0) < 0 || coords.get(0) > numberOfRows || coords.get(1) < 0 || coords.get(1) > numberOfColumns)
                return false;
        }
        if (move.getColumn() < 0 || move.getColumn() > numberOfColumns || freeSlotsNumber[move.getColumn()] < move.getPickedCards().size())
            return false;

        // make the move
        for (List<Integer> coords : move.getPickedCards()) {
            card = model.getBoard().popCard(coords.get(0), coords.get(1));
            model.getCurrentPlayer().getShelf().insert(move.getColumn(), card);
        }

        model.advancePlayerTurn();
        return true;
    }

    /**
     * Adds a new player to the game with the provided name.
     *
     * @param playerName the name of the player.
     */
    public void addPlayer(String playerName) {
        this.model.addPlayer(playerName);
    }

    /**
     * Adds a new message to the chat with the provided information.
     *
     * @param chatMsg the message to be added.
     */
    public void addChatMessage(ChatMsg chatMsg) {
        this.model.addChatMessage(chatMsg);
    }

    /**
     * Makes the current player win by filling its board with random Item Cards.
     */
    public void cheat() {
        this.model.getCurrentPlayer().getShelf().fill();
        this.model.advancePlayerTurn();
    }
}