package it.polimi.ingsw.model;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

public class CommonGoalCardStrat_CROSS implements CommonGoalCardStrat {
    @Override
    public boolean checkCard(Shelf shelf) {
        // starts from second column and second row because we start checking from the center of the cross,
        // which has "length" of one in every diagonal direction
        for (int row = 1; row < numberOfRows - 1; row++) {
            for (int column = 1; column < numberOfColumns - 1; column++) {

                // if center position is empty the pattern is not satisfied
                ItemCard centerCard = shelf.getCardAt(row, column);
                if (centerCard == null) continue;

                ItemType currentType = centerCard.getType();

                // if left upper position is empty the pattern is not satisfied
                ItemCard leftUpperCard = shelf.getCardAt(row + 1, column - 1);
                if (leftUpperCard == null) continue;

                // if left lower position is empty the pattern is not satisfied
                ItemCard leftLowerCard = shelf.getCardAt(row - 1, column - 1);
                if (leftLowerCard == null) continue;

                // if right upper position is empty the pattern is not satisfied
                ItemCard rightUpperCard = shelf.getCardAt(row + 1, column + 1);
                if (rightUpperCard == null) continue;

                // if right lower position is empty the pattern is not satisfied
                ItemCard rightLowerCard = shelf.getCardAt(row - 1, column + 1);
                if (rightLowerCard == null) continue;

                // if all the cards in the cross have the same type, the pattern is satisfied
                if (currentType.equals(leftUpperCard.getType()) &&
                        currentType.equals(leftLowerCard.getType()) &&
                        currentType.equals(rightUpperCard.getType()) &&
                        currentType.equals(rightLowerCard.getType())
                ) return true;
            }
        }

        // if no cross found
        return false;
    }

    @Override
    public CommonGoalCardType getType() {
        return CommonGoalCardType.CROSS;
    }
}
