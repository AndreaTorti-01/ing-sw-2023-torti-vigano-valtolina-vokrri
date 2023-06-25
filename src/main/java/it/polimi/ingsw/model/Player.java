package it.polimi.ingsw.model;

import java.io.Serial;
import java.io.Serializable;

/**
 * A class representing the Player.
 * This class Contains the corresponding Shelf and Personal Goal Card.
 */
public class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = 8215888944172497532L;
    /**
     * The name of this player.
     */
    private final String name;
    /**
     * The shelf of this player.
     */
    private final Shelf shelf;
    /**
     * The Personal Goal Card of this player.
     */
    private PersonalGoalCard personalGoalCard;
    /**
     * The Common Goal Cards this player has achieved.
     */
    private boolean[] hasAchievedCommonGoalCard;
    /**
     * True if this player has achieved the Personal Goal Card.
     */
    private boolean hasAchievedPersonalGoalCard;
    /**
     * The score of this player.
     */
    private int score;

    /**
     * Creates a new Player with the provided name.
     *
     * @param name the name of the player.
     */
    public Player(String name) {
        this.name = name;
        this.shelf = new Shelf();
        this.score = 0;
        this.hasAchievedCommonGoalCard = new boolean[2];
        this.hasAchievedPersonalGoalCard = false;
    }

    /**
     * @return the player's score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the player's score to the provided one.
     *
     * @param score the score to be set.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * @return the player's Personal Goal Card.
     */
    public PersonalGoalCard getPersonalGoalCard() {
        return this.personalGoalCard;
    }

    /**
     * Sets the player's Personal Goal Card to the provided one.
     *
     * @param personalGoalCard the Personal Goal Card to the provided one.
     */
    public void setPersonalGoalCard(PersonalGoalCard personalGoalCard) {
        this.personalGoalCard = personalGoalCard;
    }

    /**
     * @return the player's shelf.
     */
    public Shelf getShelf() {
        return this.shelf;
    }

    /**
     * @return the player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param index must be between boundaries (0-1).
     * @return true if the player has achieved the Common Goal Card at the provided index.
     */
    public boolean hasAchievedCommonGoalCard(int index) {
        if (index >= 0 && index < 2) return hasAchievedCommonGoalCard[index];
        else {
            System.err.println("invalid CGC index!");
            return false;
        }
    }

    /**
     * Sets the Common Goal Card as achieved at the provided index.
     *
     * @param index the index of the Common Goal Card to be set as achieved.
     */
    public void setAchievedCommonGoalCard(int index) {
        if (index >= 0 && index < 2) hasAchievedCommonGoalCard[index] = true;
        else System.err.println("invalid CGC index!");
    }
}
