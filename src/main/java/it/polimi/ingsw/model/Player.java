package it.polimi.ingsw.model;

import java.io.Serial;
import java.io.Serializable;

public class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = 8215888944172497532L;
    private final String name;
    private final Shelf shelf;
    private PersonalGoalCard personalGoalCard;
    private boolean[] hasAchievedCommonGoalCard;
    private boolean hasAchievedPersonalGoalCard;
    private int score;

    public Player(String name) {
        this.name = name;
        this.shelf = new Shelf();
        this.score = 0;
        this.hasAchievedCommonGoalCard = new boolean[2];
        this.hasAchievedPersonalGoalCard = false;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public PersonalGoalCard getPersonalGoalCard() {
        return this.personalGoalCard;
    }

    public void setPersonalGoalCard(PersonalGoalCard personalGoalCard) {
        this.personalGoalCard = personalGoalCard;
    }

    public Shelf getShelf() {
        return this.shelf;
    }

    public String getName() {
        return name;
    }

    public boolean hasAchievedCommonGoalCard(int index) {
        if (index >= 0 && index < 2) return hasAchievedCommonGoalCard[index];
        else {
            System.err.println("invalid CGC index!");
            return false;
        }
    }

    public void setAchievedCommonGoalCard(int index) {
        if (index >= 0 && index < 2) hasAchievedCommonGoalCard[index] = true;
        else {
            System.err.println("invalid CGC index!");
        }
    }

    public boolean hasAchievedPersonalGoalCard() {
        return hasAchievedPersonalGoalCard;
    }

    public void setAchievedPersonalGoalCard() {
        this.hasAchievedPersonalGoalCard = true;
    }
}
