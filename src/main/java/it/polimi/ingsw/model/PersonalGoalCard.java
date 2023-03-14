package it.polimi.ingsw.model;

public class PersonalGoalCard extends GameObject {
    private ItemType[][] pattern;

    public PersonalGoalCard() {
        // TODO implement PersonalGoalCard constructor
        // pattern randomici oppure ben definiti come nel gioco?
    }

    public ItemType[6][5] getPattern() {
        return pattern;
    }
}
