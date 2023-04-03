package it.polimi.ingsw.model;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

public class CommonGoalCardStrat_DIAGONAL_FIVE implements CommonGoalCardStrat {
    @Override
    public boolean checkCard(Shelf shelf) {
        // the length of the diagonal to check
        final int diagonalLength = 5;

        // checks for diagonals from left to right
        for (int row = 0; row < numberOfRows - diagonalLength + 1; row++) {
            for (int column = 0; column < numberOfColumns - diagonalLength + 1; column++) {
                ItemCard currentCard = shelf.getCardAt(row, column);

                // if there's no card in that position continue
                if (currentCard == null) continue;

                // checks if the cards in the diagonal have the same type as the currently selected card
                int counter = 1; // already one card on the diagonal, the one selected
                while (counter < diagonalLength) {
                    ItemCard diagonalCard = shelf.getCardAt(row + counter, column + counter);
                    // if there's no card in the diagonal break the loop and continue
                    if (diagonalCard == null) break;

                    // checks if the cards in the diagonal have the same type as the currently selected card
                    if (diagonalCard.getType().equals(currentCard.getType())) {
                        counter++;
                    }
                    // breaks the loop as soon as a card has different type from the one currently selected
                    else break;
                }

                // if the counter has the same length of the diagonal,
                // it means all the cards in that diagonal have the same type and the pattern is satisfied
                if (counter == diagonalLength) return true;
            }

            // checks for diagonals from right to left
            for (int column = numberOfColumns - 1; column >= diagonalLength - 1; column--) {
                ItemCard currentCard = shelf.getCardAt(row, column);

                // if there's no card in that position continue
                if (currentCard == null) continue;

                // checks if the cards in the diagonal have the same type as the currently selected card
                int counter = 1; // already one card on the diagonal, the one selected
                while (counter < diagonalLength) {
                    ItemCard diagonalCard = shelf.getCardAt(row - counter, column - counter);
                    // if there's no card in the diagonal break the loop and continue
                    if (diagonalCard == null) break;

                    if (diagonalCard.getType().equals(currentCard.getType())) {
                        counter++;
                    }
                    // breaks the loop as soon as a card has different type from the one currently selected
                    else break;
                }

                // if the counter has the same length of the diagonal,
                // it means all the cards in that diagonal have the same type and the pattern is satisfied
                if (counter == diagonalLength) return true;
            }
        }

        // if there are no cards with the same type in any of the diagonals the pattern is not satisfied
        return false;
    }

    @Override
    public CommonGoalCardType getType() {
        return CommonGoalCardType.DIAGONAL_FIVE;
    }
}
