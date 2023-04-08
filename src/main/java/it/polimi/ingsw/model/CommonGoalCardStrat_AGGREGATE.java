package it.polimi.ingsw.model;

import java.util.List;
import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

public class CommonGoalCardStrat_AGGREGATE implements CommonGoalCardStrat {

    private final int aggregatesToFind;
    private final int aggregateSize;
    private final CommonGoalCardType type;

    public CommonGoalCardStrat_AGGREGATE (CommonGoalCardType cardType) throws RuntimeException{
        type = cardType;
        if(cardType.equals(CommonGoalCardType.SIX_PAIRS)){
            aggregatesToFind = 6;
            aggregateSize = 2;
        } else if (cardType.equals(CommonGoalCardType.FOUR_QUARTETS)) {
            aggregatesToFind = 4;
            aggregateSize = 4;
        }else throw new RuntimeException("WRONG COMMONCARD TYPE");
    }

    public boolean checkPattern(Shelf shelf) {

        List<ItemCard> heads;
        Shelf shelfCopy = shelf.getCopy();

        // number of pairs found
        int aggregatesFound = 0;


        for (int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {

                // if the current card is null, no pattern possible and i skip the for loop
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
        // if the pattern is found the specified number of times,
        // the pattern is satisfied
        return aggregatesFound >= aggregatesToFind;
    }

    @Override
    public CommonGoalCardType getType() {
        return type;
    }
}
