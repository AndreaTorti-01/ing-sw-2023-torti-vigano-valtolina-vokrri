package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public enum CommonGoalCardType {
    SIX_PAIRS,
    DIAGONAL_FIVE,
    FOUR_QUARTETS,
    FOUR_LINES_MAX_THREE_TYPES,
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

    public static ArrayList<CommonGoalCardType> getValues() {
        return values;
    }

    /**
     * Gets a new random type from the common goal card type enumeration
     *
     * @return a random value from the enumeration
     */
    public static CommonGoalCardType getRandomType() {
        // can return the same type multiple times.
        // the controller will take care of destroying duplicates
        return values.get(random.nextInt(values.size()));
    }
}
