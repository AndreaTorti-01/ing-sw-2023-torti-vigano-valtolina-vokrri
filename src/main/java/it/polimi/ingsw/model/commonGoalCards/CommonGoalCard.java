package it.polimi.ingsw.model.commonGoalCards;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.utils.Constants;

import java.io.Serial;
import java.io.Serializable;
import java.util.Stack;

public class CommonGoalCard implements Serializable {
    @Serial
    private static final long serialVersionUID = -4000317221403330726L;
    private final Stack<Integer> assignedPoints;
    private final CommonGoalCardStrat strategy;

    /**
     * Creates a new CommonGoalCard with a random type and a stack of points based on the number of players.
     *
     * @param numberOfPlayers the number of players in the game in order to create the stack accordingly, must be in range 2-4
     */
    public CommonGoalCard(int numberOfPlayers, CommonGoalCardStrat strategy) {
        if (numberOfPlayers < Constants.minNumberOfPlayers || numberOfPlayers > Constants.maxNumberOfPlayers)
            throw new IllegalArgumentException(
                    "provided number of players (" + numberOfPlayers + ") is out of range " + Constants.minNumberOfPlayers + "-" + Constants.maxNumberOfPlayers
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

    public CommonGoalCardType getType() {
        return strategy.getType();
    }

    /**
     * @return the value on top of the stack or 0 if it's empty.
     */
    public int peekPoints() {
        // if stack is empty all previous available points were taken and thus there's no more points to take.
        if (assignedPoints.isEmpty()) return 0;
        return assignedPoints.peek();
    }

    /**
     * @return and deletes the value on top of the stack or 0 if it's empty.
     */
    public int popPoints() {
        // if stack is empty all previous available points were taken and thus there's no more points to take.
        if (assignedPoints.isEmpty()) return 0;
        return assignedPoints.pop();
    }

    public boolean checkPattern(Shelf shelf) {
        return strategy.checkPattern(shelf);
    }

}



