package it.polimi.ingsw.model.commonGoalCards;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.ItemCards.ItemType;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.utils.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

public interface CommonGoalCardStrat extends Serializable {
    boolean checkPattern(Shelf shelf);

    CommonGoalCardType getType();

    /**
     * Recursively removes all adjacent cards with the same type from the shelf deep copy
     * and returns the number of items removed.
     *
     * @param shelfCopy a deep copy of the shelf.
     * @param hotCard   the center card from where to delete all the adjacent ones.
     * @return the number of adjacent cards with the same type removed from the shelf.
     */
    default int headLiminate(Shelf shelfCopy, ItemCard hotCard) {
        List<ItemCard> heads;
        int adjacentToHead = 0;

        if (hotCard == null) return 0;

        // finds the hotCard in the shelf
        for (int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {
                if (hotCard.equals(shelfCopy.getCardAt(row, column))) {
                    // retrieves all the heads starting from the current position
                    heads = this.getHeads(shelfCopy, row, column);

                    // removes the current card from the shelf
                    shelfCopy.setCardAt(row, column, null);
                    // removes all adjacent cards from the shelf
                    if (!heads.isEmpty()) {
                        for (ItemCard head : heads) {
                            adjacentToHead = adjacentToHead + headLiminate(shelfCopy, head);
                        }
                        // returns the number of all adjacent cards with the same type
                        return heads.size() + adjacentToHead;
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Retrieves all the adjacent cards with the same type as the one in the provided position of the provided shelf.
     *
     * @param shelfCopy a deep copy of the shelf.
     * @param row       must be between boundaries (provided in the {@link Constants} file).
     * @param column    must be between boundaries (provided in the {@link Constants} file).
     * @return a list of all adjacent cards with the same type.
     */
    default ArrayList<ItemCard> getHeads(Shelf shelfCopy, int row, int column) {
        ArrayList<ItemCard> heads = new ArrayList<>();
        ItemCard currentCard = shelfCopy.getCardAt(row, column);

        // retrieves the current card type
        ItemType currentType;
        if (currentCard == null) return heads;
        else currentType = currentCard.getType();

        // checks the upper card
        // and inserts it in the list if it has the same type of the provided one
        if (row + 1 < numberOfRows) {
            ItemCard upperCard = shelfCopy.getCardAt(row + 1, column);
            if (upperCard != null && currentType.equals(upperCard.getType())) heads.add(upperCard);
        }

        // checks the right card
        // and inserts it in the list if it has the same type of the provided one
        if (column + 1 < numberOfColumns) {
            ItemCard rightCard = shelfCopy.getCardAt(row, column + 1);
            if (rightCard != null && currentType.equals(rightCard.getType())) heads.add(rightCard);
        }

        // checks the lower card
        // and inserts it in the list if it has the same type of the provided one
        if (row - 1 >= 0) {
            ItemCard lowerCard = shelfCopy.getCardAt(row - 1, column);
            if (lowerCard != null && currentType.equals(lowerCard.getType())) heads.add(lowerCard);
        }

        // checks the left card
        // and inserts it in the list if it has the same type of the provided one
        if (column - 1 >= 0) {
            ItemCard leftCard = shelfCopy.getCardAt(row, column - 1);
            if (leftCard != null && currentType.equals(leftCard.getType())) heads.add(leftCard);
        }

        return heads;
    }
}
