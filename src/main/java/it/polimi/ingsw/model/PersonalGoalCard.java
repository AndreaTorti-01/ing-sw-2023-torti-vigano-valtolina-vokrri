package it.polimi.ingsw.model;

import it.polimi.ingsw.Card;
import it.polimi.ingsw.ItemType;

public class PersonalGoalCard implements Card {
    private ItemType[][] pattern;

    public PersonalGoalCard() {
    }

    public ItemType[][] getPattern() {
        return pattern;
    }
}
