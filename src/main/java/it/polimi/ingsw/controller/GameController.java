package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static it.polimi.ingsw.utils.Constants.*;

public class GameController {

    int currentPlayer;

    public void playTurn() {
        throw new UnsupportedOperationException();
    }

    public boolean[][] getTakeableCards() {
        throw new UnsupportedOperationException();
    }

    public void takeCard(boolean[][] choices) {
        throw new UnsupportedOperationException();
    }

    // restituisce matrice di carte prelevabili in quel turno sia per correttezza a livello della board che della shelf del player giocante
    public boolean[] getValidColumns() {
        throw new UnsupportedOperationException();
    }

    public void insertCard(int column) {
        throw new UnsupportedOperationException();
    }

    /**
     * This method checks if the pattern of the personal goal card is satisfied
     *
     * @param shelf            the shelf to check for the pattern in
     * @param personalGoalCard the pattern to check
     * @return true if the pattern is satisfied, false otherwise
     */
    private boolean checkPersonalGoalCardPattern(Shelf shelf, PersonalGoalCard personalGoalCard) {
        for (int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {
                ItemCard currentCard = shelf.getCardAt(row, column);
                ItemType checker = personalGoalCard.getTypeAt(row, column);

                if (currentCard != null && !currentCard.getType().equals(checker)) return false;
                if (currentCard == null && checker != null) return false;
            }
        }
        return true;
    }

    /**
     * This method checks if the pattern of the common goal card is satisfied
     *
     * @param shelf          the shelf to check for the pattern in
     * @param commonGoalCard the pattern to check
     * @return true if the pattern is satisfied, false otherwise
     */
    private boolean checkCommonGoalCardPattern(Shelf shelf, CommonGoalCard commonGoalCard) {
        switch (commonGoalCard.getType()) {
            // well done!
            case CROSS -> {
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


            // Well Done?
            case SIX_PAIRS -> {
                // the number of pairs to find
                final int numberOfPairsToCheck = 6;

                List<ItemCard> heads;
                ItemCard[][] shelfCopy = shelf.getDeepCopy();

                // number of pairs found
                int pairCounter = 0;
                for (int row = 0; row < numberOfRows && pairCounter < numberOfPairsToCheck; row++) {
                    for (int column = 0; column < numberOfColumns && pairCounter < numberOfPairsToCheck; column++) {
                        // if the current card is null, no pattern possible
                        if (shelfCopy[row][column] == null) continue;

                        // retrieves the card on top, bottom, left and right
                        heads = this.getHeads(shelfCopy, row, column);

                        // if heads contains cards the shelf has a pair of that type
                        if (!heads.isEmpty()) {
                            pairCounter++;
                            // removes the original card from the shelf copy
                            shelfCopy[row][column] = null;
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

            // Well Done?
            case DIAGONAL_FIVE -> {
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

            // TODO: reimplement
            case TWO_SQUARES -> {
                for (ItemType type : ItemType.values()) {
                    Mask testedMask = new Mask(shelf, type);
                    Set<Square> squares = new HashSet<>();
                    for (int i = 0; i < 6; i++) {
                        for (int j = 0; j < 5; j++) {
                            if (testedMask.matrixMask[i][j] == 1 && testedMask.matrixMask[i + 1][j] == 1 && testedMask.matrixMask[i][j + 1] == 1 && testedMask.matrixMask[i + 1][j + 1] == 1) {
                                Square foundSquare = new Square();
                                foundSquare.el1_x = i;
                                foundSquare.el1_y = j;
                                foundSquare.el2_x = i;
                                foundSquare.el2_x = j + 1;
                                foundSquare.el3_x = i;
                                foundSquare.el3_x = j + 1;
                                foundSquare.el4_x = i + 1;
                                foundSquare.el4_x = j + 1;
                                squares.add(foundSquare);
                            }
                            for (Square e : squares) {
                                for (Square sq : squares) {
                                    if (e.el1_x != sq.el1_x && e.el1_y != sq.el1_y && e.el2_x != sq.el2_x && e.el2_y != sq.el2_y &&
                                            e.el3_x != sq.el3_x && e.el3_y != sq.el3_y && e.el4_x != sq.el4_x && e.el4_y != sq.el4_y)
                                        return true;
                                }
                            }
                        }
                    }
                }
                return false;
            }

            // Well Done!
            case THREE_COLUMNS_MAX_THREE_TYPES -> {
                // counts the number of columns that have a maximum of three different types of cards
                int counter = 0;
                final int numberOfColumnsToCheck = 3;
                final int maxNumberOfTypesInColumn = 3;

                outer:
                for (int column = 0; column < numberOfColumns && counter < numberOfColumnsToCheck; column++) {
                    if (shelf.getCardAt(0, column) == null) continue;

                    Set<ItemType> typesInCurrentColumn = new HashSet<>();
                    for (int row = 0; row < numberOfRows; row++) {
                        ItemCard currentCard = shelf.getCardAt(row, column);

                        // if there's no card in that position continue.
                        // for security reasons
                        if (currentCard == null) continue;

                        // if a card has a different type from the one already in the set and the set reached
                        // the maximum number of possible types skips that row
                        if (!typesInCurrentColumn.contains(currentCard.getType()) && typesInCurrentColumn.size() == maxNumberOfTypesInColumn)
                            continue outer;
                        typesInCurrentColumn.add(currentCard.getType());
                    }
                    // if it reaches the end of the column, that column satisfies the required pattern
                    counter++;
                }

                // if there are four or more columns that satisfy the pattern, return true
                return counter >= numberOfColumnsToCheck;
            }

            // Well Done!
            case FOUR_LINES_MAX_THREE_TYPES -> {
                // counts the number of rows that have a maximum of three different types of cards
                int counter = 0;
                final int numberOfRowsToCheck = 4;
                final int maxNumberOfTypesInRow = 3;

                outer:
                for (int row = 0; row < numberOfRows && counter < numberOfRowsToCheck; row++) {

                    // if any position of the row is empty, is impossible to satisfy the required pattern,
                    // so skips to the next row
                    for (int column = 0; column < numberOfColumns; column++) {
                        if (shelf.getCardAt(row, column) == null) {
                            continue outer;
                        }
                    }

                    Set<ItemType> typesInCurrentRow = new HashSet<>();
                    for (int column = 0; column < numberOfColumns; column++) {
                        ItemCard currentCard = shelf.getCardAt(row, column);

                        // if there's no card in that position continue.
                        // for security reasons
                        if (currentCard == null) continue outer;

                        // if a card has a different type from the one already in the set and the set reached
                        // the maximum number of possible types skips that row
                        if (!typesInCurrentRow.contains(currentCard.getType()) && typesInCurrentRow.size() == maxNumberOfTypesInRow)
                            continue outer;
                        typesInCurrentRow.add(currentCard.getType());
                    }
                    // if it reaches the end of the row, that row satisfies the required pattern
                    counter++;
                }

                // if there are four or more rows that satisfy the pattern, return true
                return counter >= numberOfRowsToCheck;
            }

            // Well Done!
            case TWO_RAINBOW_COLUMNS -> {
                // counts the number of columns that have cards all with different types
                int counter = 0;
                // two columns to check for the specified pattern
                final int numberOfColumnsToCheck = 2;

                // tag for efficiency
                outer:
                for (int column = 0; column < numberOfColumns && counter < numberOfColumnsToCheck; column++) {
                    // if the first position of the column is empty, is impossible to have all the types of cards in that column
                    if (shelf.getCardAt(0, column) == null) continue;

                    Set<ItemType> typesInCurrentColumn = new HashSet<>();
                    for (int row = 0; row < numberOfRows; row++) {
                        ItemCard currentCard = shelf.getCardAt(row, column);

                        // if there's no card in that position continue.
                        // for security reasons
                        if (currentCard == null) continue outer;

                        // if a card has the same type of another one from the same column skip that column
                        if (typesInCurrentColumn.contains(currentCard.getType())) continue outer;
                        typesInCurrentColumn.add(currentCard.getType());
                    }
                    // if the set has all the different types of cards, increments counter
                    if (typesInCurrentColumn.size() == numberOfCardTypes) counter++;
                }

                // if there are two columns that satisfy the pattern, return true
                return counter == numberOfColumnsToCheck;
            }

            // Well Done!
            case TWO_RAINBOW_LINES -> {
                // counts the number of lines that have cards all with different types
                int counter = 0;
                // two rows to check for the specified pattern
                final int numberOfRowsToCheck = 2;

                // tag for efficiency
                outer:
                for (int row = 0; row < numberOfRows && counter < numberOfRowsToCheck; row++) {
                    // if any position of the row is empty, is impossible to have all types of cards
                    // except one in that row
                    for (int column = 0; column < numberOfColumns; column++) {
                        if (shelf.getCardAt(row, column) == null) continue outer;
                    }

                    Set<ItemType> typesInCurrentColumn = new HashSet<>();
                    for (int column = 0; column < numberOfColumns; column++) {
                        ItemCard currentCard = shelf.getCardAt(row, column);

                        // if there's no card in that position continue.
                        // for security reasons
                        if (currentCard == null) continue;

                        // if a card has the same type of another one from the same row skip that row
                        if (typesInCurrentColumn.contains(currentCard.getType())) continue outer;
                        typesInCurrentColumn.add(currentCard.getType());
                    }
                    // if the set has all the different types of cards except for one type, increments counter
                    if (typesInCurrentColumn.size() == numberOfCardTypes - 1) counter++;
                }

                // if there are two rows that satisfy the pattern return true
                return counter == numberOfRowsToCheck;
            }

            // Well Done!
            case EQUAL_CORNERS -> {
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

            // Well Done!
            case EIGHT_EQUAL -> {
                final int cardsToCheck = 8;

                // for every type of itemCard checks if there are more than 8 cards with that type
                for (ItemType currentType : ItemType.values()) {
                    int counter = 0;

                    for (int row = 0; row < numberOfRows; row++) {
                        for (int column = 0; column < numberOfColumns; column++) {
                            ItemCard currentCard = shelf.getCardAt(row, column);

                            // if there's no card in that position continue.
                            if (currentCard == null) continue;

                            // if the card has the same current type increment counter
                            if (currentCard.getType().equals(currentType)) counter++;
                            // if there are 8 cards with the same type return true
                            if (counter == cardsToCheck) return true;
                        }
                    }
                }
                // if there are no more than 8 cards for every type return false
                return false;
            }

            // Well Done?
            case FOUR_QUARTETS -> {
                // the number of quartets to find
                final int numberOfQuartetsToCheck = 4;
                // the number of adjacent cards with the same type
                final int aggregationSize = 4;

                List<ItemCard> heads;
                ItemCard[][] shelfCopy = shelf.getDeepCopy();

                int quartetsCounter = 0;
                for (int row = 0; row < numberOfRows && quartetsCounter < numberOfQuartetsToCheck; row++) {
                    for (int column = 0; column < numberOfColumns && quartetsCounter < numberOfQuartetsToCheck; column++) {
                        // if the current card is null, no pattern possible
                        if (shelfCopy[row][column] == null) continue;

                        // retrieves the card on top, bottom, left and right
                        heads = this.getHeads(shelfCopy, row, column);

                        if (!heads.isEmpty()) {
                            // removes the original card from the shelf copy
                            shelfCopy[row][column] = null;
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

            // Well Done?
            case STAIR -> {
                // the length of the stair is at most equal to the number of columns or the number of rows
                int stairLength = Math.min(numberOfColumns, numberOfRows);

                // the expected height of the column for the first step of the stair (which always has height of one)
                int nextExpectedHeight = 1;

                // checks for the stair pattern from left to right
                for (int column = 0; column < numberOfColumns; column++) {
                    // if the current step has the same height as the expected one,
                    // increment the expected height for the next iteration
                    if (shelf.getColumnLength(column) == nextExpectedHeight) {
                        nextExpectedHeight++;
                    }
                    // the current step has not the same height as the expected one,
                    // so reset the counter to one
                    else nextExpectedHeight = 1;

                    // if it reaches the top of the stair, the pattern is satisfied
                    if (nextExpectedHeight - 1 == stairLength) return true;
                }

                nextExpectedHeight = 1;
                // checks for the stair pattern from right to left
                for (int column = numberOfColumns - 1; column >= 0; column--) {
                    // if the current step has the same height as the expected one,
                    // increment the expected height for the next iteration
                    if (shelf.getColumnLength(column) == nextExpectedHeight) {
                        nextExpectedHeight++;
                    }
                    // the current step has not the same height as the expected one,
                    // so reset the counter to one
                    else nextExpectedHeight = 1;

                    // if it reaches the top of the stair, the pattern is satisfied
                    if (nextExpectedHeight - 1 == stairLength) return true;
                }

                // if no stair pattern is found return false
                return false;
            }
        }

        // if no pattern is satisfied return false
        return false;
    }

    /**
     * Recursively removes all adjacent cards with the same type from the shelf copy
     * and returns the number of them
     *
     * @param shelfCopy a deep copy of the shelf
     * @param hotCard   the center card from where to delete all
     * @return the number of adjacent cards with the same type
     */
    private int headLiminate(ItemCard[][] shelfCopy, ItemCard hotCard) {
        List<ItemCard> heads;
        int adjacentToHead = 0;

        // TODO: Un pochino inefficiente, si può migliorare?
        // finds the hotCard in the shelf
        for (int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {
                if (hotCard.equals(shelfCopy[row][column])) {
                    // retrieves all the heads starting from the current position
                    heads = this.getHeads(shelfCopy, row, column);

                    // removes the current card from the shelf
                    shelfCopy[row][column] = null;
                    // removes all adjacent cards from the shelf
                    if (!heads.isEmpty()) {
                        for (ItemCard head : heads) {
                            adjacentToHead += headLiminate(shelfCopy, head);
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
     * Retrieves all the adjacent cards with the same type as the one in the (row, column) position in the shelf
     *
     * @param shelfCopy a deep copy of the shelf
     * @param row       the row of the current card
     * @param column    the column of the current card
     * @return a list of all adjacent cards with the same type
     */
    private ArrayList<ItemCard> getHeads(ItemCard[][] shelfCopy, int row, int column) {
        ArrayList<ItemCard> heads = new ArrayList<>();
        ItemCard currentCard = shelfCopy[row][column];

        // retrieves the current card type
        ItemType currentType;
        if (currentCard == null) return heads;
        else currentType = currentCard.getType();

        // checks the upper card
        // and inserts it in the list if it has the same type of the specified one
        if (row + 1 < numberOfRows) {
            ItemCard upperCard = shelfCopy[row + 1][column];
            if (upperCard != null && currentType.equals(upperCard.getType()))
                heads.add(upperCard);
        }

        // checks the right card
        // and inserts it in the list if has the same type of the specified one
        if (column + 1 < numberOfColumns) {
            ItemCard rightCard = shelfCopy[row][column + 1];
            if (rightCard != null && currentType.equals(rightCard.getType()))
                heads.add(rightCard);
        }

        // checks the lower card
        // and inserts it in the list if it has the same type of the specified one
        if (row - 1 >= 0) {
            ItemCard lowerCard = shelfCopy[row - 1][column];
            if (lowerCard != null && currentType.equals(lowerCard.getType()))
                heads.add(lowerCard);
        }

        // checks the left card
        // and inserts it in the list if it has the same type of the specified one
        if (column - 1 >= 0) {
            ItemCard leftCard = shelfCopy[row][column - 1];
            if (leftCard != null && currentType.equals(leftCard.getType()))
                heads.add(leftCard);
        }

        return heads;
    }

    private static class Square {
        int el1_x;
        int el1_y;
        int el2_x;
        int el2_y;
        int el3_x;
        int el3_y;
        int el4_x;
        int el4_y;
    }

    private static class Mask {
        int[][] matrixMask = new int[6][5];
        int[][] matrixAdjacent = new int[6][5];

        Mask(Shelf shelf, ItemType type) {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 5; j++)
                    if (shelf.getCardAt(i, j).getType().equals(type)) this.matrixMask[i][j] = 1;
            }
        }
    }
}