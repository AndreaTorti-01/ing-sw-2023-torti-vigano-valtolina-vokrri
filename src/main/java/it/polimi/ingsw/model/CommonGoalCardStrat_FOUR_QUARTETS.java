package it.polimi.ingsw.model;

import java.util.List;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

public class CommonGoalCardStrat_FOUR_QUARTETS implements CommonGoalCardStrat {
    @Override
    public boolean checkPattern(Shelf shelf) {
        // the number of quartets to find
        final int numberOfQuartetsToCheck = 4;
        // the number of adjacent cards with the same type
        final int aggregationSize = 4;

        List<ItemCard> heads;
        Shelf shelfCopy = shelf.getCopy();

        int quartetsCounter = 0;
        for (int row = 0; row < numberOfRows && quartetsCounter < numberOfQuartetsToCheck; row++) {
            for (int column = 0; column < numberOfColumns && quartetsCounter < numberOfQuartetsToCheck; column++) {
                // if the current card is null, no pattern possible
                if (shelfCopy.getCardAt(row, column) == null) continue;

                // retrieves the card on top, bottom, left and right
                heads = this.getHeads(shelfCopy, row, column);

                if (!heads.isEmpty()) {
                    // removes the original card from the shelf copy
                    shelfCopy.setCardAt(row, column, null);
                    // calculates the number of adjacent cards with the same type
                    // and removes them from the shelf copy
                    int currentAggregationSize = 1 + heads.size();
                    for (ItemCard head : heads) {
                        currentAggregationSize += headLiminate(shelfCopy, head);
                    }

                    // if the current aggregation of cards has the exact required size,
                    // it found a quartet
                    if (currentAggregationSize == aggregationSize)
                        quartetsCounter++;
                }
            }
        }

        // if the pattern is found the specified number of times,
        // the pattern is satisfied
        return quartetsCounter >= numberOfQuartetsToCheck;
    }

    @Override
    public CommonGoalCardType getType() {
        return CommonGoalCardType.FOUR_QUARTETS;
    }
}
