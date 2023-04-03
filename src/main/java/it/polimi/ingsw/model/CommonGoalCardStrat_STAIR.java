package it.polimi.ingsw.model;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

public class CommonGoalCardStrat_STAIR implements CommonGoalCardStrat {
    @Override
    public boolean checkPattern(Shelf shelf) {
        // the length of the stair is at most equal to the number of columns or the number of rows
        int stairLength = Math.min(numberOfColumns, numberOfRows);

        // the expected height of the column for the first step of the stair (which always has height of one)
        int nextExpectedHeight = 1;

        // checks for the stair pattern from left to right
        for (int column = 0; column < numberOfColumns; column++) {
            // if the current step has the same height as the expected one,
            // increment the expected height for the next iteration
            if (shelf.getColumnLength(column) == nextExpectedHeight) {
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
            if (shelf.getColumnLength(column) == nextExpectedHeight) {
                nextExpectedHeight++;
            }
            // the current step has not the same height as the expected one,
            // so reset the counter to one
            else nextExpectedHeight = 1;

            // if it reaches the top of the stair, the pattern is satisfied
            if (nextExpectedHeight - 1 == stairLength) return true;
        }

        // if no stair pattern is found return false
        return false;
    }

    @Override
    public CommonGoalCardType getType() {
        return CommonGoalCardType.STAIR;
    }
}
