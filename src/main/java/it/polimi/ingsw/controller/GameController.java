package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.ItemType;
import it.polimi.ingsw.model.PersonalGoalCard;
import it.polimi.ingsw.model.Shelf;

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

    public void insertCard(int col) {
        throw new UnsupportedOperationException();
    }

    private boolean personalGoalCardCheck(Shelf shelf, PersonalGoalCard personalGoalCard) {
        int count = 0; // optimization: there are 6 cards to check

        for (int i = 0; i < 6 && count < 6; i++) {
            for (int j = 0; j < 5 && count < 6; j++) {
                ItemType check = personalGoalCard.getTypeAt(i, j);
                if (check != null) {
                    count++;
                    if (check != shelf.getCard(i, j).getType()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // cristiano sta facendo commonGoalCardCheck
}
