package it.polimi.ingsw.model;

import java.util.HashSet;
import java.util.Set;

import static it.polimi.ingsw.utils.Constants.*;

// implementa TWO_RAINBOW_COLUMNS e TWO_RAINBOW_LINES
public class CommonGoalCardStrat_RAINBOW implements CommonGoalCardStrat {

    final int numberOfColumnsToCheck;
    final int numberOfRowsToCheck;
    final int numberOfTypesToCheck;

    public CommonGoalCardStrat_RAINBOW(int numberOfColumnsToCheck, int numberOfRowsToCheck, int numberOfTypesToCheck) {
        this.numberOfColumnsToCheck = numberOfColumnsToCheck;
        this.numberOfRowsToCheck = numberOfRowsToCheck;
        this.numberOfTypesToCheck = numberOfTypesToCheck;
    }

    // TODO fix this
    @Override
    public boolean checkPattern(Shelf shelf) {
        // counts the number of columns that have cards all with different types
        int counter = 0;
        // counts the number of rows that have cards all with different types
        int counter2 = 0;

        // tag for efficiency
        outer:
        for (int column = 0; column < numberOfColumns && counter < numberOfColumnsToCheck; column++) {
            // if the first position of the column is empty, is impossible to have all the types of cards in that column
            if (shelf.getCardAt(0, column) == null) continue;

            Set<ItemType> typesInCurrentColumn = new HashSet<>();
            for (int row = 0; row < numberOfRows; row++) {
                ItemCard currentCard = shelf.getCardAt(row, column);

                // if there's no card in that position continue.
                // for security reasons
                if (currentCard == null) continue outer;

                // if a card has the same type of another one from the same column skip that column
                if (typesInCurrentColumn.contains(currentCard.getType())) continue outer;
                typesInCurrentColumn.add(currentCard.getType());
            }
            // if the set has all the different types of cards, increments counter
            if (typesInCurrentColumn.size() == numberOfCardTypes) counter++;
        }

        // if there are two columns that satisfy the pattern, return true
        return counter == numberOfColumnsToCheck;
    }

    @Override
    public CommonGoalCardType getType() {
        if (numberOfColumnsToCheck == 2 && numberOfRowsToCheck == 0 && numberOfTypesToCheck == 6) {
            return CommonGoalCardType.TWO_RAINBOW_COLUMNS;
        } else if (numberOfColumnsToCheck == 0 && numberOfRowsToCheck == 2 && numberOfTypesToCheck == 6) {
            return CommonGoalCardType.TWO_RAINBOW_LINES;
        } else {
            return null;
        }
    }
}
