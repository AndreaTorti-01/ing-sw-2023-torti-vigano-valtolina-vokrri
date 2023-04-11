package it.polimi.ingsw.model;

import java.util.HashSet;
import java.util.Set;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

public class CommonGoalCardStrat_FOUR_LINES_MAX_THREE_TYPES implements CommonGoalCardStrat {
    @Override
    public boolean checkPattern(Shelf shelf) {
        // counts the number of rows that have a maximum of three different types of cards
        int counter = 0;
        final int numberOfRowsToCheck = 4;
        final int maxNumberOfTypesInRow = 3;

        outer:
        for (int row = 0; row < numberOfRows && counter < numberOfRowsToCheck; row++) {

            // if any position of the row is empty, is impossible to satisfy the required pattern,
            // so skips to the next row
            for (int column = 0; column < numberOfColumns; column++) {
                if (shelf.getCardAt(row, column) == null) {
                    continue outer;
                }
            }

            Set<ItemType> typesInCurrentRow = new HashSet<>();
            for (int column = 0; column < numberOfColumns; column++) {
                ItemCard currentCard = shelf.getCardAt(row, column);

                // if there's no card in that position continue.
                // for security reasons
                if (currentCard == null) continue outer;

                // if a card has a different type from the one already in the set and the set reached
                // the maximum number of possible types skips that row
                if (!typesInCurrentRow.contains(currentCard.type()) && typesInCurrentRow.size() == maxNumberOfTypesInRow)
                    continue outer;
                typesInCurrentRow.add(currentCard.type());
            }
            // if it reaches the end of the row, that row satisfies the required pattern
            counter++;
        }

        // if there are four or more rows that satisfy the pattern, return true
        return counter >= numberOfRowsToCheck;
    }

    @Override
    public CommonGoalCardType getType() {
        return CommonGoalCardType.FOUR_LINES_MAX_THREE_TYPES;
    }
}
