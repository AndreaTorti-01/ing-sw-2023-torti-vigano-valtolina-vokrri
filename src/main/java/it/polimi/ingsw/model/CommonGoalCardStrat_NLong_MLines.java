package it.polimi.ingsw.model;

import java.util.List;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

// implementa SIX_PAIRS e FOUR_QUARTETS
public class CommonGoalCardStrat_NLong_MLines implements CommonGoalCardStrat {

    private final int numberOfLines;
    private final int aggregationSize;

    public CommonGoalCardStrat_NLong_MLines(int numberOfLines, int aggregationSize) {
        this.numberOfLines = numberOfLines;
        this.aggregationSize = aggregationSize;
    }

    // TODO test this

    @Override
    public boolean checkPattern(Shelf shelf) {

        List<ItemCard> heads;
        Shelf shelfCopy = shelf.getCopy();

        int quartetsCounter = 0;
        for (int row = 0; row < numberOfRows && quartetsCounter < numberOfLines; row++) {
            for (int column = 0; column < numberOfColumns && quartetsCounter < numberOfLines; column++) {
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
        return quartetsCounter >= numberOfLines;
    }

    @Override
    public CommonGoalCardType getType() {

        if (numberOfLines == 4 && aggregationSize == 4)
            return CommonGoalCardType.FOUR_QUARTETS;
        else if (numberOfLines == 6 && aggregationSize == 2)
            return CommonGoalCardType.SIX_PAIRS;
        else
            return null;
    }
}
