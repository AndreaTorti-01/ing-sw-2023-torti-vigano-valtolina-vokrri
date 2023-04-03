package it.polimi.ingsw.model;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

public class CommonGoalCardStrat_EQUAL_CORNERS implements CommonGoalCardStrat {
    @Override
    public boolean checkPattern(Shelf shelf) {
        ItemCard firstCornerItemCard = shelf.getCardAt(0, 0);
        // if there's no card in the first corner it's impossible to satisfy the pattern
        if (firstCornerItemCard == null) return false;

        ItemCard secondCornerItemCard = shelf.getCardAt(0, numberOfColumns - 1);
        // if there's no card in the second corner it's impossible to satisfy the pattern
        if (secondCornerItemCard == null) return false;

        ItemCard thirdCornerItemCard = shelf.getCardAt(numberOfRows - 1, 0);
        // if there's no card in the third corner it's impossible to satisfy the pattern
        if (thirdCornerItemCard == null) return false;

        ItemCard fourthCornerItemCard = shelf.getCardAt(numberOfRows - 1, numberOfColumns - 1);
        // if there's no card in the fourth corner it's impossible to satisfy the pattern
        if (fourthCornerItemCard == null) return false;

        // the pattern is satisfied if and only if the four cards at the corner have the same type
        return firstCornerItemCard.getType().equals(secondCornerItemCard.getType()) &&
                secondCornerItemCard.getType().equals(thirdCornerItemCard.getType()) &&
                thirdCornerItemCard.getType().equals(fourthCornerItemCard.getType());
    }

    @Override
    public CommonGoalCardType getType() {
        return CommonGoalCardType.EQUAL_CORNERS;
    }
}
