package it.polimi.ingsw.model;

import java.util.*;

public class Player {
    int score;
    List<CommonGoalCard> cgList = new ArrayList<>();
    // possibilità dai aggiungere un attributo achieved direttamente alla card in questo modo. e metodo boolean IsAchieved()
    List<PersonalGoalCard> pgList = new ArrayList<>();
    // possibilità dai aggiungere un attributo achieved direttamente alla card in questo modo. e metodo boolean IsAchieved()

    Shelf playerShelf = new Shelf();

    public int getScore() { return score; }

    public Shelf getShelf() {
        return playerShelf;
    }
}
