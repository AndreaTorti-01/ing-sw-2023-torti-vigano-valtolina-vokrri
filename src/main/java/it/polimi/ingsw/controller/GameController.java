package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PersonalGoalCard;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.commonGoalCards.CommonGoalCard;
import it.polimi.ingsw.network.client.RmiClient;
import it.polimi.ingsw.utils.Observer;

public class GameController {
    int currentPlayer;
    private Game model;
    private RmiClient client;

    public GameController(Game model, RmiClient client) {
        this.model = model;
        this.client = client;
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
}