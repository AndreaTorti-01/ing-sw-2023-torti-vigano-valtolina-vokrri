package it.polimi.ingsw.model;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

public class CommonGoalCardStrat_EIGHT_EQUAL implements CommonGoalCardStrat {
    @Override
    public boolean checkCard(Shelf shelf) {
        final int cardsToCheck = 8;

        // for every type of itemCard checks if there are more than 8 cards with that type
        for (ItemType currentType : ItemType.values()) {
            int counter = 0;

            for (int row = 0; row < numberOfRows; row++) {
                for (int column = 0; column < numberOfColumns; column++) {
                    ItemCard currentCard = shelf.getCardAt(row, column);

                    // if there's no card in that position continue.
                    if (currentCard == null) continue;

                    // if the card has the same current type increment counter
                    if (currentCard.getType().equals(currentType)) counter++;
                    // if there are 8 cards with the same type return true
                    if (counter == cardsToCheck) return true;
                }
            }
        }
        // if there are no more than 8 cards for every type return false
        return false;
    }

    @Override
    public CommonGoalCardType getType() {
        return CommonGoalCardType.EIGHT_EQUAL;
    }
}
