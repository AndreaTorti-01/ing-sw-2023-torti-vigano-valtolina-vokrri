package it.polimi.ingsw.model.commonGoalCards;

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

    /**
     * @param type the type of Common Goal Card to get the strategy from.
     * @return the Common Goal Card Strategy corresponding to the provided type
     */
    public static CommonGoalCardStrat getStrategyFromType(CommonGoalCardType type) {
        return switch (type) {
            case CROSS, DIAGONAL_FIVE, TWO_SQUARES, EQUAL_CORNERS -> new CommonGoalCardStrat_SHAPE(type);
            case EIGHT_EQUAL -> new CommonGoalCardStrat_EIGHT_EQUAL();
            case FOUR_LINES_MAX_THREE_TYPES, THREE_COLUMNS_MAX_THREE_TYPES ->
                    new CommonGoalCardStrat_MAX_THREE_TYPES(type);
            case FOUR_QUARTETS, SIX_PAIRS -> new CommonGoalCardStrat_AGGREGATE(type);
            case STAIR -> new CommonGoalCardStrat_STAIR();
            case TWO_RAINBOW_COLUMNS, TWO_RAINBOW_LINES -> new CommonGoalCardStrat_RAINBOWS(type);
            default -> throw new IllegalArgumentException("Invalid Type");
        };
    }
}
