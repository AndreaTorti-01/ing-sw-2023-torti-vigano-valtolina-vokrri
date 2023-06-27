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
     * The Common Goal Cards this player has achieved.
     */
    private final boolean[] hasAchievedCommonGoalCard;
    /**
     * The Personal Goal Card of this player.
     */
    private PersonalGoalCard personalGoalCard;
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
    }

    /**
     * Gets the score of this player.
     *
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
     * Gets the Personal Goal Card of this player.
     *
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
     * Gets the Shelf of this player.
     *
     * @return the player's shelf.
     */
    public Shelf getShelf() {
        return this.shelf;
    }

    /**
     * Gets the name of this player.
     *
     * @return the player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the Common Goal Cards this player has achieved.
     *
     * @param index must be between boundaries (0-1).
     * @return true if the player has achieved the Common Goal Card at the provided index.
     */
    public boolean hasAchievedCommonGoalCard(int index) {
        if (index < 0 || index > 1)
            throw new IllegalArgumentException("Index must be between 0 and 1");
        return hasAchievedCommonGoalCard[index];
    }

    /**
     * Sets the Common Goal Card as achieved at the provided index.
     *
     * @param index the index of the Common Goal Card to be set as achieved.
     */
    public void setAchievedCommonGoalCard(int index) {
        if (index < 0 || index > 1)
            throw new IllegalArgumentException("Index must be between 0 and 1");
        hasAchievedCommonGoalCard[index] = true;
    }
}
