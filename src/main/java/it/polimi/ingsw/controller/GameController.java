package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

public class GameController {

    int currentPlayer;

    public void playTurn() {
        throw new UnsupportedOperationException();
    }

    public boolean[][] getTakeableCards() {
        throw new UnsupportedOperationException();
    }

    public void takeCard(boolean[][] choices) {
        throw new UnsupportedOperationException();
    }

    // restituisce matrice di carte prelevabili in quel turno sia per correttezza a livello della board che della shelf del player giocante
    public boolean[] getValidColumns() {
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
        for (int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {
                ItemCard currentCard = shelf.getCardAt(row, column);
                ItemType checker = personalGoalCard.getTypeAt(row, column);

                if (currentCard != null && !currentCard.getType().equals(checker)) return false;
                if (currentCard == null && checker != null) return false;
            }
        }
        return true;
    }

    /**
     * This method checks if the pattern of the common goal card is satisfied
     *
     * @param shelf          the shelf to check for the pattern in
     * @param commonGoalCard the pattern to check
     * @return true if the pattern is satisfied, false otherwise
     */
    private boolean checkCommonGoalCardPattern(Shelf shelf, CommonGoalCard commonGoalCard) {
        return commonGoalCard.checkCard(shelf);
    }
}