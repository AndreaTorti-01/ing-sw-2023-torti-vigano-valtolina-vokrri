package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.CommonGoalCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PersonalGoalCard;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.utils.Observer;
import it.polimi.ingsw.view.Tui;

public class GameController implements Observer{

    int currentPlayer;
    Game model;
    Tui view;

    public GameController(Game model, Tui view) {
        this.model = model;
        this.view = view;
        view.addObserver(this);
    }

    public void playTurn() {
        throw new UnsupportedOperationException();
    }

    public void takeCard(boolean[][] choices) {
        throw new UnsupportedOperationException();
    }

    public void insertCard(int column) {
        throw new UnsupportedOperationException();
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


    @Override
    public void update(Object message) {
        throw new UnsupportedOperationException();
    }
}