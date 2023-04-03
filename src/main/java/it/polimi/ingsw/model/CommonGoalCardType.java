package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public enum CommonGoalCardType {
    CROSS,
    DIAGONAL_FIVE,
    EIGHT_EQUAL,
    EQUAL_CORNERS,
    FOUR_LINES_MAX_THREE_TYPES,
    FOUR_QUARTETS,
    SIX_PAIRS,
    STAIR,
    THREE_COLUMNS_MAX_THREE_TYPES,
    TWO_RAINBOW_COLUMNS,
    TWO_RAINBOW_LINES,
    TWO_SQUARES;

    private static final ArrayList<CommonGoalCardType> values = new ArrayList<>(
            Arrays.asList(CommonGoalCardType.values())
    );

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
        return values.get(new Random().nextInt(Constants.numberOfCardTypes));
    }
}
