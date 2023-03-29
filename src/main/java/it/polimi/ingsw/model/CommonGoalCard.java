package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.Constants;

import java.util.Stack;

public class CommonGoalCard extends GameObject {
    private final Stack<Integer> assignedPoints;
    private final CommonGoalCardType type;

    /**
     * Creates a new CommonGoalCard with a random type and a stack of points based on the number of players.
     *
     * @param numberOfPlayers the number of players in the game in order to create the stack accordingly, must be in range 2-4
     */
    public CommonGoalCard(int numberOfPlayers) {
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

        // assigns the card a random type
        type = CommonGoalCardType.getRandomType();
    }

    public CommonGoalCardType getType() {
        return type;
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
}



