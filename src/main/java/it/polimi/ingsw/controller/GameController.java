package it.polimi.ingsw.controller;

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
    // etc...
}
