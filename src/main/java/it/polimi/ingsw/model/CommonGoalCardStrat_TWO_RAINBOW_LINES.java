package it.polimi.ingsw.model;

import java.util.HashSet;
import java.util.Set;

import static it.polimi.ingsw.utils.Constants.*;

public class CommonGoalCardStrat_TWO_RAINBOW_LINES implements CommonGoalCardStrat {
    @Override
    public boolean checkPattern(Shelf shelf) {
        // counts the number of lines that have cards all with different types
        int counter = 0;
        // two rows to check for the specified pattern
        final int numberOfRowsToCheck = 2;

        // tag for efficiency
        outer:
        for (int row = 0; row < numberOfRows && counter < numberOfRowsToCheck; row++) {
            // if any position of the row is empty, is impossible to have all types of cards
            // except one in that row
            for (int column = 0; column < numberOfColumns; column++) {
                if (shelf.getCardAt(row, column) == null) continue outer;
            }

            Set<ItemType> typesInCurrentColumn = new HashSet<>();
            for (int column = 0; column < numberOfColumns; column++) {
                ItemCard currentCard = shelf.getCardAt(row, column);

                // if there's no card in that position continue.
                // for security reasons
                if (currentCard == null) continue;

                // if a card has the same type of another one from the same row skip that row
                if (typesInCurrentColumn.contains(currentCard.type())) continue outer;
                typesInCurrentColumn.add(currentCard.type());
            }
            // if the set has all the different types of cards except for one type, increments counter
            if (typesInCurrentColumn.size() == numberOfItemCardTypes - 1) counter++;
        }

        // if there are two rows that satisfy the pattern return true
        return counter == numberOfRowsToCheck;
    }

    @Override
    public CommonGoalCardType getType() {
        return CommonGoalCardType.TWO_RAINBOW_LINES;
    }
}
