package it.polimi.ingsw.model;

import java.util.HashSet;
import java.util.Set;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

public class CommonGoalCardStrat_MAX_THREE_TYPES implements CommonGoalCardStrat {

    private final int numberOfColumnsToCheck;

    private final int numberOfLinesToCheck;
    private final int maxNumberOfTypes;
    private final CommonGoalCardType type;

    public CommonGoalCardStrat_MAX_THREE_TYPES(CommonGoalCardType cardType) throws RuntimeException {
        type = cardType;
        maxNumberOfTypes = 3;
        if (cardType.equals(CommonGoalCardType.FOUR_LINES_MAX_THREE_TYPES)) {
            numberOfColumnsToCheck = 0;
            numberOfLinesToCheck = 4;

        } else if (cardType.equals(CommonGoalCardType.THREE_COLUMNS_MAX_THREE_TYPES)) {
            numberOfColumnsToCheck = 3;
            numberOfLinesToCheck = 0;
        } else throw new RuntimeException("WRONG COMMONCARD TYPE");
    }

    public boolean checkPattern(Shelf shelf) {
        int counterOfLines = 0;
        int counterOfColumns = 0;
        // counts the number of rows that have a maximum of three different types of cards

        outer:
        for (int row = 0; row < numberOfRows && counterOfLines < numberOfLinesToCheck; row++) {

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
                if (!typesInCurrentRow.contains(currentCard.getType()) && typesInCurrentRow.size() == maxNumberOfTypes)
                    continue outer;
                typesInCurrentRow.add(currentCard.getType());
            }
            // if it reaches the end of the row, that row satisfies the required pattern
            counterOfLines++;
        }

        outer1:
        for (int column = 0; column < numberOfColumns && counterOfColumns < numberOfColumnsToCheck; column++) {
            if (shelf.getCardAt(0, column) == null) continue;

            Set<ItemType> typesInCurrentColumn = new HashSet<>();
            for (int row = 0; row < numberOfRows; row++) {
                ItemCard currentCard = shelf.getCardAt(row, column);

                // if there's no card in that position continue.
                // for security reasons
                if (currentCard == null) continue;

                // if a card has a different type from the one already in the set and the set reached
                // the maximum number of possible types skips that row
                if (!typesInCurrentColumn.contains(currentCard.getType()) && typesInCurrentColumn.size() == maxNumberOfTypes)
                    continue outer1;
                typesInCurrentColumn.add(currentCard.getType());
            }
            // if it reaches the end of the column, that column satisfies the required pattern
            counterOfColumns++;
        }

        return (counterOfLines >= numberOfLinesToCheck && counterOfColumns >= numberOfColumnsToCheck);

    }


    public CommonGoalCardType getType() {
        return type;
    }
}
