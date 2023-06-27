package it.polimi.ingsw.model.commonGoalCards;

import it.polimi.ingsw.model.ItemCards.ItemType;
import it.polimi.ingsw.model.Shelf;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

/**
 * A class representing the RAINBOWS Common Goal Card Strategy.
 */
public class CommonGoalCardStrat_RAINBOWS implements CommonGoalCardStrat {

    @Serial
    private static final long serialVersionUID = -7349223966947005027L;
    /**
     * The number of columns to find in the shelf.
     */
    private final int numOfRowsToFind;  // number of rainbow rows
    /**
     * The number of lines to find in the shelf.
     */
    private final int numOfColsToFind; // number of rainbow columns
    /**
     * The type of the Common Goal Card Strategy.
     */
    private final CommonGoalCardType type;

    /**
     * Creates a new RAINBOWS Common Goal Card Strategy of the provided type.
     *
     * @param cardType the type of the Common Goal Card Strategy.
     *                 Must be {@code TWO_RAINBOW_COLUMNS} or {@code TWO_RAINBOW_LINES}.
     * @throws IllegalArgumentException if the provided type is not {@code TWO_RAINBOW_COLUMNS} or {@code TWO_RAINBOW_LINES}.
     */
    public CommonGoalCardStrat_RAINBOWS(CommonGoalCardType cardType) throws IllegalArgumentException {
        type = cardType;
        if (cardType.equals(CommonGoalCardType.TWO_RAINBOW_COLUMNS)) {
            numOfRowsToFind = 0;
            numOfColsToFind = 2;
        } else if (cardType.equals(CommonGoalCardType.TWO_RAINBOW_LINES)) {
            numOfRowsToFind = 2;
            numOfColsToFind = 0;
        } else throw new IllegalArgumentException(
                "The type of Common Goal Card Strategy must be " + CommonGoalCardType.TWO_RAINBOW_COLUMNS.name() + " or " + CommonGoalCardType.TWO_RAINBOW_LINES.name()

        );
    }

    /**
     * Checks if the pattern of the RAINBOWS Common Goal Card type is satisfied in the provided shelf.
     *
     * @param shelf the shelf to check the pattern in.
     * @return true if the pattern is satisfied, false otherwise.
     */
    public boolean checkPattern(Shelf shelf) {
        int foundColumns = 0;
        int foundRows = 0;
        List<ItemType> types = new ArrayList<>();

        // checking if the rows are rainbow
        for (int i = 0; i < numberOfRows && foundRows < numOfRowsToFind; i++) {
            boolean validRow = true;

            for (int j = 0; j < numberOfColumns && validRow; j++) {
                if (shelf.getCardAt(i, j) != null && !types.contains(shelf.getCardAt(i, j).getType()))
                    types.add(shelf.getCardAt(i, j).getType());
                else
                    validRow = false;
            }

            if (validRow) foundRows++;
            types.clear();
        }

        // checking if the columns are rainbow
        for (int j = 0; j < numberOfColumns && foundColumns < numOfColsToFind; j++) {
            boolean validColumn = true;

            for (int i = 0; i < numberOfRows && validColumn; i++) {
                if (shelf.getCardAt(i, j) != null && !types.contains(shelf.getCardAt(i, j).getType()))
                    types.add(shelf.getCardAt(i, j).getType());
                else
                    validColumn = false;
            }

            if (validColumn) foundColumns++;
            types.clear();
        }

        return (foundRows >= numOfRowsToFind) && (foundColumns >= numOfColsToFind);
    }

    /**
     * Gets the type of this Common Goal Type.
     *
     * @return the type of this Common Goal Card ({@code TWO_RAINBOW_COLUMNS} or {@code TWO_RAINBOW_LINES}).
     */
    public CommonGoalCardType getType() {
        return type;
    }
}
