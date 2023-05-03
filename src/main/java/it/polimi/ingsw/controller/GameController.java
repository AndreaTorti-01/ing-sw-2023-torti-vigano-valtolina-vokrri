package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.PersonalGoalCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.commonGoalCards.CommonGoalCard;
import it.polimi.ingsw.network.serializable.MoveMsg;

import java.util.LinkedList;
import java.util.List;

public class GameController {
    private final Game model;
    int currentPlayer;
    private final List<String> playerNames;

    public GameController(Game model) {
        this.model = model;
        this.playerNames = new LinkedList<>();
    }

    public void makeMove(MoveMsg move) {
        ItemCard card;
        for (List coords : move.getPickedCards()) {
            card = model.getBoard().popCard((int) coords.get(0), (int) coords.get(1));
            model.getCurrentPlayer().getShelf().insert(move.getColumn(), card);
        }
    }

    public void addPlayer(String playerName) {
        this.playerNames.add(playerName);
        if (playerNames.size() == 3) {
            this.model.startGame(playerNames);
        }
    }


    /**
     * This method checks if the pattern of the personal goal card is satisfied
     *
     * @param shelf            the shelf to check for the pattern in
     * @param personalGoalCard the pattern to check
     * @return true if the pattern is satisfied, false otherwise
     */
    private boolean checkPersonalGoalCardPattern(Shelf shelf, PersonalGoalCard personalGoalCard) {
        return personalGoalCard.checkPattern(shelf);
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

    private void calculateWinner() {
        Player winner = null;
        int maxScore = 0;
        for (Player player : model.getPlayers()) {
            if (player.getScore() > maxScore) {
                maxScore = player.getScore();
                winner = player;
            }
        }
        model.setWinner(winner);
    }
}