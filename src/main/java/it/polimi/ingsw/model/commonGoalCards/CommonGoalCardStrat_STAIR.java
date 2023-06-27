package it.polimi.ingsw.model.commonGoalCards;

import it.polimi.ingsw.model.Shelf;

import java.io.Serial;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

/**
 * A class representing the STAIR Common Goal Card Strategy.
 */
public class CommonGoalCardStrat_STAIR implements CommonGoalCardStrat {
    @Serial
    private static final long serialVersionUID = -6277214700657472564L;

    /**
     * Checks if the pattern of the STAIR Common Goal Card type is satisfied in the provided shelf.
     *
     * @param shelf the shelf to check the pattern in.
     * @return true if the pattern is satisfied, false otherwise.
     */
    @Override
    public boolean checkPattern(Shelf shelf) {
        // the length of the stair is at most equal to the number of columns or the number of rows
        int stairLength = Math.min(numberOfColumns, numberOfRows);

        // the stair can have the first step of height grater than one. So, for every iteration,
        // it removes the bottom part of the stair and checks for a step of height one,
        // from where to start searching for the pattern.
        //
        // Example of what it checks:
        // first iteration (row == 0):
        // C * * * *
        // B C * * *
        // B B C * *
        // B B B C *
        // B B B B C
        // B B B B B <- starts to check from this row
        //
        // second iteration (row == 1):
        // C * * * *
        // B C * * *
        // B B C * *
        // B B B C *
        // B B B B C <- starts to check from this row
        // B B B B B <- it's like it does not exist
        for (int row = 0; row < stairLength; row++) {
            // the expected height of the column for the first step of the stair (which always has height of one)
            int nextExpectedHeight = 1;

            // checks for the stair pattern from left to right
            for (int column = 0; column < numberOfColumns; column++) {
                // if the current step has the same height as the expected one,
                // increment the expected height for the next iteration
                if (shelf.getColumnLength(column) - row == nextExpectedHeight) {
                    nextExpectedHeight++;
                }
                // the current step has not the same height as the expected one,
                // so reset the counter to one
                else nextExpectedHeight = 1;

                // if it reaches the top of the stair, the pattern is satisfied
                if (nextExpectedHeight - 1 == stairLength) return true;
            }

            nextExpectedHeight = 1;
            // checks for the stair pattern from right to left
            for (int column = numberOfColumns - 1; column >= 0; column--) {
                // if the current step has the same height as the expected one,
                // increment the expected height for the next iteration
                if (shelf.getColumnLength(column) - row == nextExpectedHeight) {
                    nextExpectedHeight++;
                }
                // the current step has not the same height as the expected one,
                // so reset the counter to one
                else nextExpectedHeight = 1;

                // if it reaches the top of the stair, the pattern is satisfied
                if (nextExpectedHeight - 1 == stairLength) return true;
            }
        }

        // if no stair pattern is found return false
        return false;
    }

    /**
     * Gets the type of this Common Goal Type.
     *
     * @return the type of this Common Goal Card ({@code STAIR}).
     */
    @Override
    public CommonGoalCardType getType() {
        return CommonGoalCardType.STAIR;
    }
}