package it.polimi.ingsw.model.commonGoalCards;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.Shelf;

import java.io.Serial;
import java.util.List;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

public class CommonGoalCardStrat_AGGREGATE implements CommonGoalCardStrat {

    @Serial
    private static final long serialVersionUID = 3039026104293719772L;
    private final int aggregatesToFind;
    private final int aggregateSize;
    private final CommonGoalCardType type;

    /**
     * Creates a new AGGREGATE Common Goal Card Strategy of the provided type.
     *
     * @param cardType the type of the Common Goal Card Strategy.
     *                 Must be {@code SIX_PAIRS} or {@code FOUR_QUARTETS}.
     * @throws IllegalArgumentException if the provided type is not {@code SIX_PAIRS} or {@code FOUR_QUARTETS}
     */
    public CommonGoalCardStrat_AGGREGATE(CommonGoalCardType cardType) throws IllegalArgumentException {
        type = cardType;
        if (cardType.equals(CommonGoalCardType.SIX_PAIRS)) {
            aggregatesToFind = 6;
            aggregateSize = 2;
        } else if (cardType.equals(CommonGoalCardType.FOUR_QUARTETS)) {
            aggregatesToFind = 4;
            aggregateSize = 4;
        } else throw new IllegalArgumentException(
                "The type of Common Goal Card Strategy must be " + CommonGoalCardType.SIX_PAIRS.name() + " or " + CommonGoalCardType.FOUR_QUARTETS.name()
        );
    }

    /**
     * Checks if the pattern of the AGGREGATE Common Goal Card type is satisfied in the provided shelf.
     *
     * @param shelf the shelf to check the pattern in.
     * @return true if the pattern is satisfied, false otherwise.
     */
    public boolean checkPattern(Shelf shelf) {
        List<ItemCard> heads;
        Shelf shelfCopy = shelf.getCopy();

        // number of pairs found
        int aggregatesFound = 0;

        for (int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {

                // if the current card is null, no pattern is possible
                if (shelfCopy.getCardAt(row, column) != null) {

                    // retrieves the card on top, bottom, left and right
                    heads = this.getHeads(shelfCopy, row, column);

                    // if heads contains cards the shelf has a pair of that type
                    if (!heads.isEmpty()) {
                        // removes the original card from the shelf copy
                        shelfCopy.setCardAt(row, column, null);
                        // removes all the adjacent cards with the same type
                        int sizeFound = 1 + heads.size(); //initial cell + adj
                        for (ItemCard head : heads) {
                            sizeFound = sizeFound + headLiminate(shelfCopy, head);
                        }
                        heads.clear();
                        if (sizeFound >= aggregateSize) aggregatesFound++;
                    }
                }
            }
        }
        // if the pattern is found the provided number of times,
        // the pattern is satisfied
        return aggregatesFound >= aggregatesToFind;
    }

    /**
     * @return the type of this Common Goal Card ({@code SIX_PAIRS} or {@code FOUR_QUARTETS})
     */
    @Override
    public CommonGoalCardType getType() {
        return type;
    }
}
