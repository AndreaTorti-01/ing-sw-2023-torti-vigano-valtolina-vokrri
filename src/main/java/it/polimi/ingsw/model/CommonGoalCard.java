package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

enum CommonGoalCardType {
    SIX_PAIRS,
    DIAGONAL_FIVE,
    FOUR_QUARTETS,
    FOUR_LINES_MAX_THREET_YPES,
    EQUAL_CORNERS,
    TWO_RAINBOW_COLUMNS,
    TWO_SQUARES,
    TWO_RAINBOW_LINES,
    THREE_COLUMNS_MAX_THREE_TYPES,
    CROSS,
    EIGHT_EQUAL,
    STAIR;

    private static final ArrayList<CommonGoalCardType> values = new ArrayList<>(
            Arrays.asList(CommonGoalCardType.values())
    );
    private static final Random random = new Random();

    /**
     * Gets a new random type from the common goal card type enumeration and deletes it from the values in order not to return the same value twice
     *
     * @return a random value from the enumeration
     */
    public static CommonGoalCardType getRandomType() {
        CommonGoalCardType randomValue = values.get(random.nextInt(values.size()));
        values.remove(randomValue);
        return randomValue;
    }
}

public class CommonGoalCard extends GameObject {
    private final Stack<Integer> assignedPoints;
    private final CommonGoalCardType type;

    public CommonGoalCard(int numberOfPlayers) {
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

    public int peekPoints() {
        return assignedPoints.peek();
    }

    public int popPoints() {
        return assignedPoints.pop();
    }
}



