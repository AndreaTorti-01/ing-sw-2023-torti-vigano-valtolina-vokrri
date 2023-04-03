package it.polimi.ingsw.model;

import java.util.List;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

public class CommonGoalCardStrat_SIX_PAIRS implements CommonGoalCardStrat {
    @Override
    public boolean checkPattern(Shelf shelf) {
        // the number of pairs to find
        final int numberOfPairsToCheck = 6;

        List<ItemCard> heads;
        Shelf shelfCopy = shelf.getCopy();

        // number of pairs found
        int pairCounter = 0;
        for (int row = 0; row < numberOfRows && pairCounter < numberOfPairsToCheck; row++) {
            for (int column = 0; column < numberOfColumns && pairCounter < numberOfPairsToCheck; column++) {
                // if the current card is null, no pattern possible
                if (shelfCopy.getCardAt(row, column) == null) continue;

                // retrieves the card on top, bottom, left and right
                heads = this.getHeads(shelfCopy, row, column);

                // if heads contains cards the shelf has a pair of that type
                if (!heads.isEmpty()) {
                    pairCounter++;
                    // removes the original card from the shelf copy
                    shelfCopy.setCardAt(row, column, null);
                    // removes all the adjacent cards with the same type
                    for (ItemCard head : heads) {
                        headLiminate(shelfCopy, head);
                    }
                }
            }
        }

        // if the pattern is found the specified number of times,
        // the pattern is satisfied
        return pairCounter >= numberOfPairsToCheck;
    }

    @Override
    public CommonGoalCardType getType() {
        return CommonGoalCardType.SIX_PAIRS;
    }
}
