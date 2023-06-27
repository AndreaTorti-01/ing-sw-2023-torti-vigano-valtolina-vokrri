package it.polimi.ingsw.model.commonGoalCards;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.utils.Constants;

import java.io.Serial;
import java.io.Serializable;
import java.util.Stack;

/**
 * A class representing the Common Goal Card.
 */
public class CommonGoalCard implements Serializable {
    @Serial
    private static final long serialVersionUID = -4000317221403330726L;
    /**
     * The stack of points the players can get by completing the common goal cards.
     */
    private final Stack<Integer> assignedPoints;
    /**
     * The specific Common Goal Card implementation.
     */
    private final CommonGoalCardStrat strategy;

    /**
     * Creates a new CommonGoalCard with a random type and a stack of points based on the number of players.
     *
     * @param numberOfPlayers the number of players in the game in order to create the stack accordingly,
     *                        must be in range 2-4
     * @param strategy        the specific Common Goal Card strategy to implement.
     */
    public CommonGoalCard(int numberOfPlayers, CommonGoalCardStrat strategy) {
        if (numberOfPlayers < Constants.minNumberOfPlayers || numberOfPlayers > Constants.maxNumberOfPlayers)
            throw new IllegalArgumentException(
                    "Provided number of players (" + numberOfPlayers + ") is out of range " + Constants.minNumberOfPlayers + "-" + Constants.maxNumberOfPlayers
            );

        assignedPoints = new Stack<>();

        // inserts the points in the stack based on the number of players
        switch (numberOfPlayers) {
            case 2 -> {
                assignedPoints.push(4);
                assignedPoints.push(8);
            }
            case 3 -> {
                assignedPoints.push(4);
                assignedPoints.push(6);
                assignedPoints.push(8);
            }
            case 4 -> {
                assignedPoints.push(2);
                assignedPoints.push(4);
                assignedPoints.push(6);
                assignedPoints.push(8);
            }
        }

        this.strategy = strategy;
    }

    /**
     * Gets the type of this Common Goal Card.
     *
     * @return the type of the Common Goal Card.
     */
    public CommonGoalCardType getType() {
        return strategy.getType();
    }

    /**
     * Gets the point the player gets by completing the Common Goal Card, without popping it from the stack.
     *
     * @return the next point the player can get by completing the common goal card.
     */
    public int peekPoints() {
        // if stack is empty all previous available points were taken
        // and thus there's no more points to take.
        if (assignedPoints.isEmpty()) return 0;
        return assignedPoints.peek();
    }

    /**
     * Gets the point the player gets by completing the Common Goal Card and pops it from the stack.
     *
     * @return the point the player gets by completing the common goal card, popping it from the stack.
     */
    public int popPoints() {
        // if stack is empty all previous available points were taken and thus there's no more points to take.
        if (assignedPoints.isEmpty()) return 0;
        return assignedPoints.pop();
    }

    /**
     * Checks if the pattern in the provided shelf is satisfied.
     *
     * @param shelf the shelf to check the pattern in.
     * @return true if the pattern is satisfied, false otherwise.
     */
    public boolean checkPattern(Shelf shelf) {
        return strategy.checkPattern(shelf);
    }

}



