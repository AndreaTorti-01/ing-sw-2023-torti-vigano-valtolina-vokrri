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

/**
 * A class representing the Common Goal Card Strategy.
 * Used as a strategy pattern to retrieve a specific Common Goal Card.
 */
public interface CommonGoalCardStrat extends Serializable {
    /**
     * Checks if the provided shelf matches the pattern of the specific Common Goal Card.
     *
     * @param shelf the shelf to check the pattern in.
     * @return true if the pattern is satisfied, false otherwise.
     */
    boolean checkPattern(Shelf shelf);

    /**
     * Retrieves the type of the Common Goal Card.
     *
     * @return the type of the Common Goal Card.
     */
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
                if (hotCard == shelfCopy.getCardAt(row, column)) {
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
            if (upperCard != null && !upperCard.isMarked() &&currentType.equals(upperCard.getType())) {
                heads.add(upperCard);
                upperCard.setMarked(true);
            }
        }

        // checks the right card
        // and inserts it in the list if it has the same type of the provided one
        if (column + 1 < numberOfColumns) {
            ItemCard rightCard = shelfCopy.getCardAt(row, column + 1);
            if (rightCard != null && !rightCard.isMarked() && currentType.equals(rightCard.getType())) {
                heads.add(rightCard);
                rightCard.setMarked(true);
            }
        }

        // checks the lower card
        // and inserts it in the list if it has the same type of the provided one
        if (row - 1 >= 0) {
            ItemCard lowerCard = shelfCopy.getCardAt(row - 1, column);
            if (lowerCard != null && !lowerCard.isMarked() && currentType.equals(lowerCard.getType())) {
                heads.add(lowerCard);
                lowerCard.setMarked(true);
            }
        }

        // checks the left card
        // and inserts it in the list if it has the same type of the provided one
        if (column - 1 >= 0) {
            ItemCard leftCard = shelfCopy.getCardAt(row, column - 1);
            if (leftCard != null && !leftCard.isMarked() && currentType.equals(leftCard.getType())) {
                heads.add(leftCard);
                leftCard.setMarked(true);
            }
        }

        return heads;
    }
}
