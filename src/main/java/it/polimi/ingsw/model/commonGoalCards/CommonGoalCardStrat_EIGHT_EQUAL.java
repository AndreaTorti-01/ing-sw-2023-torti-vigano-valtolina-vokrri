package it.polimi.ingsw.model.commonGoalCards;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.ItemCards.ItemType;
import it.polimi.ingsw.model.Shelf;

import java.io.Serial;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

/**
 * A class representing the EIGHT_EQUAL Common Goal Card Strategy.
 */
public class CommonGoalCardStrat_EIGHT_EQUAL implements CommonGoalCardStrat {
    @Serial
    private static final long serialVersionUID = -5460232929999583608L;

    /**
     * Checks if the pattern of the EIGHT EQUAL Common Goal Card type is satisfied in the provided shelf.
     *
     * @param shelf the shelf to check the pattern in.
     * @return true if the pattern is satisfied, false otherwise.
     */
    @Override
    public boolean checkPattern(Shelf shelf) {
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

    /**
     * Gets the type of this Common Goal Card.
     *
     * @return the type of this Common Goal Card ({@code EIGHT_EQUAL})
     */
    @Override
    public CommonGoalCardType getType() {
        return CommonGoalCardType.EIGHT_EQUAL;
    }
}
