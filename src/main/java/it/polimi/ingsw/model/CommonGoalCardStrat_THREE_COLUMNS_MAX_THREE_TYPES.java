package it.polimi.ingsw.model;

import java.util.HashSet;
import java.util.Set;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

public class CommonGoalCardStrat_THREE_COLUMNS_MAX_THREE_TYPES implements CommonGoalCardStrat {
    @Override
    public boolean checkPattern(Shelf shelf) {
        // counts the number of columns that have a maximum of three different types of cards
        int counter = 0;
        final int numberOfColumnsToCheck = 3;
        final int maxNumberOfTypesInColumn = 3;

        outer:
        for (int column = 0; column < numberOfColumns && counter < numberOfColumnsToCheck; column++) {
            if (shelf.getCardAt(0, column) == null) continue;

            Set<ItemType> typesInCurrentColumn = new HashSet<>();
            for (int row = 0; row < numberOfRows; row++) {
                ItemCard currentCard = shelf.getCardAt(row, column);

                // if there's no card in that position continue.
                // for security reasons
                if (currentCard == null) continue;

                // if a card has a different type from the one already in the set and the set reached
                // the maximum number of possible types skips that row
                if (!typesInCurrentColumn.contains(currentCard.getType()) && typesInCurrentColumn.size() == maxNumberOfTypesInColumn)
                    continue outer;
                typesInCurrentColumn.add(currentCard.getType());
            }
            // if it reaches the end of the column, that column satisfies the required pattern
            counter++;
        }

        // if there are four or more columns that satisfy the pattern, return true
        return counter >= numberOfColumnsToCheck;
    }

    @Override
    public CommonGoalCardType getType() {
        return CommonGoalCardType.THREE_COLUMNS_MAX_THREE_TYPES;
    }
}
