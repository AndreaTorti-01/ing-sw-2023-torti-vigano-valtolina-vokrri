package it.polimi.ingsw.model;

import java.util.Stack;

public class CommonGoalCard extends GameObject {
    private final Stack<Integer> assignedPoints;
    private final CommonGoalCardType type;

    public CommonGoalCard(int numberOfPlayers) {
        if (numberOfPlayers < 2 || numberOfPlayers > 4)
            throw new IllegalArgumentException("The number of player given (" + numberOfPlayers + ") is not in range 2-4");

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

        // assigns the card a random type from the ones not already taken
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



