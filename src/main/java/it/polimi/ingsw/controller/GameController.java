package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utils.Constants;

import java.util.HashSet;
import java.util.Set;

// Fabio Vokrri 29/03/2023
// TODO: refactor checkPersonalGoalCardPattern method (DONE!)
// TODO: refactor checkCommonGoalCardPattern method
//          - refactor cross pattern checker (DONE!)
//          - refactor six pairs pattern checker
//          - refactor diagonal five pattern checker

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
        for (int row = 0; row < Constants.numberOfRows; row++) {
            for (int column = 0; column < Constants.numberOfColumns; column++) {
                ItemType checker = personalGoalCard.getTypeAt(row, column);
                if (!shelf.getCardAt(row, column).getType().equals(checker)) return false;
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
            case CROSS:
                // starts from second column and second row because we start checking from the center of the cross,
                // which has "length" of one in every diagonal direction
                for (int row = 1; row < Constants.numberOfRows - 1; row++) {
                    for (int column = 1; column < Constants.numberOfColumns - 1; column++) {
                        ItemType type = shelf.getCardAt(row, column).getType();
                        if (type != null &&
                                type.equals(shelf.getCardAt(row - 1, column - 1).getType()) &&
                                type.equals(shelf.getCardAt(row - 1, column + 1).getType()) &&
                                type.equals(shelf.getCardAt(row + 1, column - 1).getType()) &&
                                type.equals(shelf.getCardAt(row + 1, column + 1).getType())
                        ) return true;
                    }
                }
                return false;
            case SIX_PAIRS: {
                // non c'ho capito un cazzo :)
                int numberOfPairs = 0;
                for (ItemType type : ItemType.values()) {
                    Mask testedMask = new Mask(shelf, type);
                    numberOfPairs += numberOfPairsInMask(testedMask);
                }
                return numberOfPairs > 5;
            }

            case DIAGONAL_FIVE: {
                int h = 0;
                ItemType a = shelf.getCardAt(2, 2).getType();
                ItemType b = shelf.getCardAt(3, 2).getType();
                for (int i = -2; i < 3; i++) {
                    if (a.equals(shelf.getCardAt(2 + i, 2 + i).getType())) {
                        h++;
                    }
                }
                if (h == 5) return true;
                else h = 0;
                for (int i = -2; i < 3; i++) {
                    if (a.equals(shelf.getCardAt(2 - i, 2 - i).getType())) {
                        h++;
                    }
                }
                if (h == 5) return true;
                else h = 0;
                for (int i = -2; i < 3; i++) {
                    if (b.equals(shelf.getCardAt(3 + i, 2 + i).getType())) {
                        h++;
                    }
                }
                if (h == 5) return true;
                else h = 0;
                for (int i = -2; i < 3; i++) {
                    if (b.equals(shelf.getCardAt(3 - i, 2 - i).getType())) {
                        h++;
                    }
                }
                return h == 5;
            }
            /* Ho creato le classi maschera che ha un costruttore che genera una matrice maschera di 0/1 della shelf sulla base del tipo passato come parametro
              e una classe quadrato con attributi le coordinate delle quattro tessere che lo formano. a qusto punto con un po' di for annidati per oogni itemtypes
              creo la sua maschera e salvo su un set tutti i quadrati sulla maschera per poi con due cicli for che scorrono tutto il set cercare se ci sono due quadrati non
              sovrapposti e nel caso ritornare true altrimenti false
             */
            case TWO_SQUARES: {
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

            case FOUR_LINES_MAX_THREE_TYPES: {

            }
            case EQUAL_CORNERS: {
                ItemType firstCornerType = shelf.getCardAt(0, 0).getType();
                return shelf.getCardAt(0, 4).getType().equals(firstCornerType) &&
                        shelf.getCardAt(5, 4).getType().equals(firstCornerType) &&
                        shelf.getCardAt(5, 0).getType().equals(firstCornerType);
            }
            case FOUR_QUARTETS: {

            }

            /*case TWO_RAINBOW_COLUMNS: {
                Set<ItemType> control = new HashSet<>();
                for (ItemType type : ItemType.values()) {
                    control.add(type);
                }
                int j, m = 0;
                for (int i = 0; i < 5; i++) {
                    j = 5;
                    for (ItemType type : ItemType.values()) {
                        if (!control.contains(type)) control.add(type);
                    }
                    int k = 0;
                    do {
                        control.remove(shelf.getCardAt(i, j));
                        j = j - 1;
                        k++;
                    }
                    while (shelf.getCardAt(i, j) != null && j >= 0
                            && control.contains(shelf.getCardAt(i, j)));
                    if (k == 6) m++;
                }
                return m > 1;

            }*/
            case TWO_RAINBOW_COLUMNS: {
                // counts the number of columns that have cards all with different types
                int counter = 0;
                // two columns to check for the specified pattern
                final int numberOfColumnsToCheck = 2;

                // tag for efficiency
                outer:
                for (int column = 0; column < Constants.numberOfColumns && counter < numberOfColumnsToCheck; column++) {
                    // if the first position of the column is empty, is impossible to have all the types of cards in that column
                    if (shelf.getCardAt(0, column) == null) continue;

                    Set<ItemType> typesInCurrentColumn = new HashSet<>();
                    for (int row = 0; row < Constants.numberOfRows; row++) {
                        ItemType currentType = shelf.getCardAt(row, column).getType();
                        // if a card has the same type of another one from the same column skip that column
                        if (typesInCurrentColumn.contains(currentType)) {
                            // clears the types added to the set in order to check new column
                            typesInCurrentColumn.clear();
                            continue outer;
                        } else typesInCurrentColumn.add(currentType);
                    }
                    // if the set has all the different types of cards, increments counter
                    if (typesInCurrentColumn.size() == Constants.numberOfCardTypes) counter++;
                }

                // if there are two columns that satisfy the pattern return true
                return counter == numberOfColumnsToCheck;
            }
            /*case TWO_RAINBOW_LINES: {
                Set<ItemType> control = new HashSet<>();
                for (ItemType type : ItemType.values()) {
                    control.add(type);
                }
                int j, m = 0;
                for (int i = 0; i < 6; i++) {
                    j = 4;
                    for (ItemType type : ItemType.values()) {
                        if (!control.contains(type)) control.add(type);
                    }
                    int k = 0;
                    do {
                        control.remove(shelf.getCardAt(i, j));
                        j = j - 1;
                        k++;
                    }
                    while (shelf.getCardAt(i, j) != null && j >= 0
                            && control.contains(shelf.getCardAt(i, j)));
                    if (k == 5) m++;
                }
                return m > 1;

            }*/
            case TWO_RAINBOW_LINES: {
                // counts the number of lines that have cards all with different types
                int counter = 0;
                // two rows to check for the specified pattern
                final int numberOfRowsToCheck = 2;

                // tag for efficiency
                outer:
                for (int row = 0; row < Constants.numberOfRows && counter < numberOfRowsToCheck; row++) {
                    // if the first position of the row is empty, is impossible to have all types of cards
                    // (except one) in that row
                    if (shelf.getCardAt(row, 0) == null) continue;

                    Set<ItemType> typesInCurrentColumn = new HashSet<>();
                    for (int column = 0; column < Constants.numberOfColumns; column++) {
                        ItemType currentType = shelf.getCardAt(row, column).getType();
                        // if a card has the same type of another one from the same row skip that row
                        if (typesInCurrentColumn.contains(currentType)) {
                            // clears the types added to the set in order to check new row
                            typesInCurrentColumn.clear();
                            continue outer;
                        } else typesInCurrentColumn.add(currentType);
                    }
                    // if the set has all the different types of cards except for one type, increments counter
                    if (typesInCurrentColumn.size() == Constants.numberOfCardTypes - 1) counter++;
                }

                // if there are two rows that satisfy the pattern return true
                return counter == numberOfRowsToCheck;
            }

            case THREE_COLUMNS_MAX_THREE_TYPES: {

            }
            // TODO: to revise for better scalability
            //      statically typed item types values 
            case EIGHT_EQUAL: {
                int p = 0, b = 0, g = 0, f = 0, c = 0, t = 0;
                for (int row = 0; row < Constants.numberOfRows; row++) {
                    for (int column = 0; column < Constants.numberOfColumns; column++) {
                        if (shelf.getCardAt(row, column).getType().equals(ItemType.BOOKS)) b++;
                        else if (shelf.getCardAt(row, column).getType().equals(ItemType.PLANTS)) p++;
                        else if (shelf.getCardAt(row, column).getType().equals(ItemType.GAMES)) g++;
                        else if (shelf.getCardAt(row, column).getType().equals(ItemType.CATS)) c++;
                        else if (shelf.getCardAt(row, column).getType().equals(ItemType.TROPHIES)) t++;
                        else if (shelf.getCardAt(row, column).getType().equals(ItemType.FRAMES)) f++;
                    }
                }
                return p > 7 || b > 7 || g > 7 || c > 7 || t > 7 || f > 7;
            }
            case STAIR: {
                ItemCard[] increasing_stair = {
                        shelf.getCardAt(5, 0),
                        shelf.getCardAt(4, 1),
                        shelf.getCardAt(3, 2),
                        shelf.getCardAt(2, 3),
                        shelf.getCardAt(1, 4)
                };
                ItemCard[] decreasing_stair = {
                        shelf.getCardAt(1, 0),
                        shelf.getCardAt(2, 1),
                        shelf.getCardAt(3, 2),
                        shelf.getCardAt(4, 3),
                        shelf.getCardAt(5, 4)
                };
                ItemCard[] above_increasing_stair = {
                        shelf.getCardAt(6, 0),
                        shelf.getCardAt(5, 1),
                        shelf.getCardAt(4, 2),
                        shelf.getCardAt(3, 3),
                        shelf.getCardAt(2, 4)
                };
                ItemCard[] above_decreasing_stair = {
                        shelf.getCardAt(2, 0),
                        shelf.getCardAt(3, 1),
                        shelf.getCardAt(4, 2),
                        shelf.getCardAt(5, 3),
                        shelf.getCardAt(6, 4)
                };
                int k = 0, m = 0;
                for (int i = 0; i < 5; i++) {
                    if (above_decreasing_stair[i] != null) return false;
                    if (above_increasing_stair[i] != null) return false;
                    if (increasing_stair[i] != null) k++;
                    if (decreasing_stair[i] != null) m++;
                }
                return m > 4 || k > 4;
            }
        }
        return false;
    }

    //! CRISTIANO usa solo camelCase e non snake_case, per favore <3

    private int numberOfPairsInMask(Mask mask) {
        int count = 0, counter = 0, small = 0;
        foundFirstAdjacent(mask);
        for (int row = Constants.numberOfRows; row >= 0; row--) {
            for (int column = 0; column < Constants.numberOfColumns; column++) {
                if (hasAdjacent(mask, row, column)) {

                    if (mask.matrixAdjacent[row + 1][column] == 0 &&
                            mask.matrixAdjacent[row - 1][column] == 0 &&
                            mask.matrixAdjacent[row][column + 1] == 0 &&
                            mask.matrixAdjacent[row][column - 1] == 0
                    ) {
                        count++;
                        counter++;
                        mask.matrixAdjacent[row][column] = count;
                    } else if (hasToFix(mask, row, column)) {
                        counter--;
                        small = findSmallest(mask, row, column);
                        fixMatrixAdjacent(mask, mask.matrixAdjacent[row][column - 1], small);
                        fixMatrixAdjacent(mask, mask.matrixAdjacent[row - 1][column], small);
                        fixMatrixAdjacent(mask, mask.matrixAdjacent[row][column + 1], small);
                        fixMatrixAdjacent(mask, mask.matrixAdjacent[row + 1][column], small);
                        mask.matrixAdjacent[row][column] = small;

                    } else {
                        mask.matrixAdjacent[row][column] = findSmallest(mask, row, column);
                    }

                }
            }
        }
        return counter;
    }

    private boolean hasToFix(Mask mask, int row, int column) {
        return (mask.matrixAdjacent[row][column - 1] != 0 && mask.matrixAdjacent[row - 1][column] != 0 && mask.matrixAdjacent[row][column - 1] != mask.matrixAdjacent[row - 1][column]) ||
                (mask.matrixAdjacent[row][column - 1] != 0 && mask.matrixAdjacent[row][column + 1] != 0 && mask.matrixAdjacent[row][column - 1] != mask.matrixAdjacent[row][column + 1]) ||
                (mask.matrixAdjacent[row][column - 1] != 0 && mask.matrixAdjacent[row + 1][column] != 0 && mask.matrixAdjacent[row][column - 1] != mask.matrixAdjacent[row + 1][column]) ||
                (mask.matrixAdjacent[row][column + 1] != 0 && mask.matrixAdjacent[row - 1][column] != 0 && mask.matrixAdjacent[row][column + 1] != mask.matrixAdjacent[row - 1][column]) ||
                (mask.matrixAdjacent[row + 1][column] != 0 && mask.matrixAdjacent[row - 1][column] != 0 && mask.matrixAdjacent[row + 1][column] != mask.matrixAdjacent[row - 1][column]) ||
                (mask.matrixAdjacent[row][column + 1] != 0 && mask.matrixAdjacent[row + 1][column] != 0 && mask.matrixAdjacent[row][column + 1] != mask.matrixAdjacent[row + 1][column]);
    }

    private int findSmallest(Mask mask, int i, int j) {
        int small = 100; // cos'è 100?
        if (mask.matrixAdjacent[i][j - 1] < small) small = mask.matrixAdjacent[i][j - 1];
        if (mask.matrixAdjacent[i - 1][j] < small) small = mask.matrixAdjacent[i - 1][j];
        if (mask.matrixAdjacent[i][j + 1] < small) small = mask.matrixAdjacent[i][j + 1];
        if (mask.matrixAdjacent[i + 1][j] < small) small = mask.matrixAdjacent[i + 1][j];
        if (small == 100) small = 0;
        return small;
    }

    private boolean hasAdjacent(Mask mask, int i, int j) {
        return mask.matrixMask[i][j] == mask.matrixMask[i + 1][j] ||
                mask.matrixMask[i][j] == mask.matrixMask[i][j + 1] ||
                mask.matrixMask[i][j] == mask.matrixMask[i - 1][j] ||
                mask.matrixMask[i][j] == mask.matrixMask[i][j - 1];
    }

    private void fixMatrixAdjacent(Mask mask, int l, int m) {
        if (l == 0) return;
        else {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 5; j++) {
                    if (mask.matrixAdjacent[i][j] == l)
                        mask.matrixAdjacent[i][j] = m;
                }
            }
        }

    }

    private void foundFirstAdjacent(Mask mask) {
        for (int i = 6; i >= 0; i--) {
            for (int j = 0; j < 5; j++) {
                if (mask.matrixMask[i][j] == mask.matrixMask[i - 1][j] || mask.matrixMask[i][j] == mask.matrixMask[i][j + 1]) {
                    mask.matrixAdjacent[i][j] = 1;
                    return;
                }
            }
        }
        return;
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