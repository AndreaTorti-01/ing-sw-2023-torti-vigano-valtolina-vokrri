package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.commonGoalCards.CommonGoalCard;
import it.polimi.ingsw.network.serializable.ChatMsg;
import it.polimi.ingsw.network.serializable.MoveMsg;

import java.util.List;

public class GameController {
    private final Game model;

    public GameController(Game model) {
        this.model = model;
    }

    public void initGame(Integer playerNum) {
        this.model.initModel(playerNum);
    }

    public void makeMove(MoveMsg move) {
        ItemCard card;
        for (List<Integer> coords : move.getPickedCards()) {
            card = model.getBoard().popCard(coords.get(0), coords.get(1));
            model.getCurrentPlayer().getShelf().insert(move.getColumn(), card);
        }
        model.advancePlayerTurn();
    }

    public void addPlayer(String playerName) {
        this.model.addPlayer(playerName);
    }


    /**
     * This method checks if the pattern of the common goal card is satisfied
     *
     * @param shelf          the shelf to check for the pattern in
     * @param commonGoalCard the pattern to check
     * @return true if the pattern is satisfied, false otherwise
     */
    private boolean checkCommonGoalCardPattern(Shelf shelf, CommonGoalCard commonGoalCard) {
        return commonGoalCard.checkPattern(shelf);
    }

    public void addChatMessage(ChatMsg chatMsg) {
        this.model.addChatMessage(chatMsg);
    }
}