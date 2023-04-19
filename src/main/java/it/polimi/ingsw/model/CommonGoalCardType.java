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

    // TODO: remove?
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
        return values.get(new Random().nextInt(Constants.numberOfItemCardTypes));
    }

    // mapping from the type to the corresponding class
    public static CommonGoalCardStrat getStrategyFromType(CommonGoalCardType type) {
        return switch (type) {
            case CROSS, DIAGONAL_FIVE, TWO_SQUARES, EQUAL_CORNERS -> new CommonGoalCardStrat_SHAPE(type);
            case EIGHT_EQUAL -> new CommonGoalCardStrat_EIGHT_EQUAL();
            case FOUR_LINES_MAX_THREE_TYPES, THREE_COLUMNS_MAX_THREE_TYPES ->
                    new CommonGoalCardStrat_MAX_THREE_TYPES(type);
            case FOUR_QUARTETS, SIX_PAIRS -> new CommonGoalCardStrat_AGGREGATE(type);
            case STAIR -> new CommonGoalCardStrat_STAIR();
            case TWO_RAINBOW_COLUMNS, TWO_RAINBOW_LINES -> new CommonGoalCardStrat_RAINBOWS(type);
            default -> throw new IllegalArgumentException("invalid type");
        };
    }
}
