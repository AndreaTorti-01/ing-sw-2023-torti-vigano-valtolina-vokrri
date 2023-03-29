package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

import java.util.HashSet;
import java.util.Set;

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

    public void insertCard(int col) {
        throw new UnsupportedOperationException();
    }

    private boolean checkPersonalGoalCardPattern(Shelf shelf, PersonalGoalCard personalGoalCard) {
        int count = 0; // optimization: there are 6 cards to check

        for (int i = 0; i < 6 && count < 6; i++) {
            for (int j = 0; j < 5 && count < 6; j++) {
                ItemType check = personalGoalCard.getTypeAt(i, j);
                if (check != null) {
                    count++;
                    if (check != shelf.getCard(i, j).getType()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkCommonGoalCardPattern(Shelf shelf, CommonGoalCard commonGoalCard) {
        switch (commonGoalCard.getType()) {
            case CROSS:
                for (int i = 1; i < 5; i++) {
                    for (int j = 1; j < 4; j++) {
                        ItemType a = shelf.getCard(i, j).getType();
                        if (a.equals(shelf.getCard(i + 1, j + 1).getType()) &&
                                a.equals(shelf.getCard(i + 1, j - 1).getType()) &&
                                a.equals(shelf.getCard(i - 1, j + 1).getType()) &&
                                a.equals(shelf.getCard(i - 1, j - 1).getType()))
                            return true;
                    }
                }
                return false;
            case SIX_PAIRS: {
                int numberOfPair = 0;
                for (ItemType type : ItemType.values()) {
                    mask mask_tested = new mask(shelf, type);
                    numberOfPair = numberOfPair + number_of_pair_in_mask(mask_tested);
                }
                if (numberOfPair > 5) return true;
                else return false;
            }
            case DIAGONAL_FIVE: {
                int h = 0;
                ItemType a = shelf.getCard(2, 2).getType();
                ItemType b = shelf.getCard(3, 2).getType();
                for (int i = -2; i < 3; i++) {
                    if (a.equals(shelf.getCard(2 + i, 2 + i).getType())) {
                        h++;
                    }
                }
                if (h == 5) return true;
                else h = 0;
                for (int i = -2; i < 3; i++) {
                    if (a.equals(shelf.getCard(2 - i, 2 - i).getType())) {
                        h++;
                    }
                }
                if (h == 5) return true;
                else h = 0;
                for (int i = -2; i < 3; i++) {
                    if (b.equals(shelf.getCard(3 + i, 2 + i).getType())) {
                        h++;
                    }
                }
                if (h == 5) return true;
                else h = 0;
                for (int i = -2; i < 3; i++) {
                    if (b.equals(shelf.getCard(3 - i, 2 - i).getType())) {
                        h++;
                    }
                }
                return h == 5;
            }
            /* ho creato le classi maschera che ha un costruttore che genera una matrice maschera di 0/1 della shelf sulla base del tipo passato come parametro
              e una classe quadrato con attirbuti le cordinate delle quattro tessere che lo formano. a qusto punto con un po' di for annidati per oogni itemtypes
              creo la sua maschera e salvo su un set tutti i quadrati sulla maschera per poi con due cicli for che scorrono tutto il set cercare se ci sono due quadrati non
              sovrapposti e nel caso ritornare true altrimenti false
             */
            case TWO_SQUARES: {
                for (ItemType type : ItemType.values()) {
                    mask mask_tested = new mask(shelf, type);
                    Set<square> squares = new HashSet<>();
                    for (int i = 0; i < 6; i++) {
                        for (int j = 0; j < 5; j++) {
                            if (mask_tested.matrix_mask[i][j] == 1 && mask_tested.matrix_mask[i + 1][j] == 1 && mask_tested.matrix_mask[i][j + 1] == 1 && mask_tested.matrix_mask[i + 1][j + 1] == 1) {
                                square square_found = new square();
                                square_found.el1_x = i;
                                square_found.el1_y = j;
                                square_found.el2_x = i;
                                square_found.el2_x = j + 1;
                                square_found.el3_x = i;
                                square_found.el3_x = j + 1;
                                square_found.el4_x = i + 1;
                                square_found.el4_x = j + 1;
                                squares.add(square_found);
                            }
                            for (square e : squares) {
                                for (square sq : squares) {
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
                ItemType a = shelf.getCard(0, 0).getType();
                return a.equals(shelf.getCard(0, 4).getType()) &&
                        a.equals(shelf.getCard(5, 4).getType()) &&
                        a.equals(shelf.getCard(5, 0).getType());
            }
            case FOUR_QUARTETS: {

            }
            case TWO_RAINBOW_COLUMNS: {
                Set<ItemType> controll = new HashSet<>();
                for (ItemType type : ItemType.values()) {
                    controll.add(type);
                }
                int j, m = 0;
                for (int i = 0; i < 5; i++) {
                    j = 5;
                    for (ItemType type : ItemType.values()) {
                        if (!controll.contains(type)) controll.add(type);
                    }
                    int k = 0;
                    do {
                        controll.remove(shelf.getCard(i, j));
                        j = j - 1;
                        k++;
                    }
                    while (shelf.getCard(i, j) != null && j >= 0
                            && controll.contains(shelf.getCard(i, j)));
                    if (k == 6) m++;
                }
                return m > 1;

            }
            case TWO_RAINBOW_LINES: {
                Set<ItemType> controll = new HashSet<>();
                for (ItemType type : ItemType.values()) {
                    controll.add(type);
                }
                int j, m = 0;
                for (int i = 0; i < 6; i++) {
                    j = 4;
                    for (ItemType type : ItemType.values()) {
                        if (!controll.contains(type)) controll.add(type);
                    }
                    int k = 0;
                    do {
                        controll.remove(shelf.getCard(i, j));
                        j = j - 1;
                        k++;
                    }
                    while (shelf.getCard(i, j) != null && j >= 0
                            && controll.contains(shelf.getCard(i, j)));
                    if (k == 5) m++;
                }
                return m > 1;

            }
            case THREE_COLUMNS_MAX_THREE_TYPES: {

            }
            case EIGHT_EQUAL: {
                int p = 0, b = 0, g = 0, f = 0, c = 0, t = 0;
                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (shelf.getCard(i, j).getType().equals(ItemType.BOOKS)) b++;
                        else if (shelf.getCard(i, j).getType().equals(ItemType.PLANTS)) p++;
                        else if (shelf.getCard(i, j).getType().equals(ItemType.GAMES)) g++;
                        else if (shelf.getCard(i, j).getType().equals(ItemType.CATS)) c++;
                        else if (shelf.getCard(i, j).getType().equals(ItemType.TROPHIES)) t++;
                        else if (shelf.getCard(i, j).getType().equals(ItemType.FRAMES)) f++;
                    }
                }
                if (p > 7 || b > 7 || g > 7 || c > 7 || t > 7 || f > 7) return true;
                else return false;
            }
            case STAIR: {
                ItemCard[] increasing_stair = {
                        shelf.getCard(5, 0), shelf.getCard(4, 1),
                        shelf.getCard(3, 2), shelf.getCard(2, 3),
                        shelf.getCard(1, 4)};
                ItemCard[] decreasing_stair = {
                        shelf.getCard(1, 0), shelf.getCard(2, 1),
                        shelf.getCard(3, 2), shelf.getCard(4, 3),
                        shelf.getCard(5, 4)};
                ItemCard[] above_increasing_stair = {
                        shelf.getCard(6, 0), shelf.getCard(5, 1),
                        shelf.getCard(4, 2), shelf.getCard(3, 3),
                        shelf.getCard(2, 4)};
                ItemCard[] above_decreasing_stair = {
                        shelf.getCard(2, 0), shelf.getCard(3, 1),
                        shelf.getCard(4, 2), shelf.getCard(5, 3),
                        shelf.getCard(6, 4)};
                int k = 0, m = 0;
                for (int i = 0; i < 5; i++) {
                    if (above_decreasing_stair[i] != null) return false;
                    if (above_increasing_stair[i] != null) return false;
                    if (increasing_stair[i] != null) k++;
                    if (decreasing_stair[i] != null) m++;
                }
                if (m > 4 || k > 4) return true;
                else return false;
            }
        }
        return false;
    }

    private int number_of_pair_in_mask(mask mask) {
        int count = 0, counter = 0, small = 0;
        found_first_adjacent(mask);
        for (int i = 6; i >= 0; i--) {
            for (int j = 0; j < 5; j++) {
                if (has_adjacent(mask, i, j)) {

                    if (mask.matrix_adjacent[i + 1][j] == 0 && mask.matrix_adjacent[i - 1][j] == 0 && mask.matrix_adjacent[i][j + 1] == 0 && mask.matrix_adjacent[i][j - 1] == 0) {
                        count++;
                        counter++;
                        mask.matrix_adjacent[i][j] = count;
                    } else if (has_to_fix(mask, i, j)) {
                        counter--;
                        small = found_smallest(mask, i, j);
                        fix_matrix_adjacent(mask, mask.matrix_adjacent[i][j - 1], small);
                        fix_matrix_adjacent(mask, mask.matrix_adjacent[i - 1][j], small);
                        fix_matrix_adjacent(mask, mask.matrix_adjacent[i][j + 1], small);
                        fix_matrix_adjacent(mask, mask.matrix_adjacent[i + 1][j], small);
                        mask.matrix_adjacent[i][j] = small;

                    } else {
                        mask.matrix_adjacent[i][j] = found_smallest(mask, i, j);
                    }

                }
            }
        }
        return counter;
    }

    private boolean has_to_fix(mask mask, int i, int j) {
        if ((mask.matrix_adjacent[i][j - 1] != 0 && mask.matrix_adjacent[i - 1][j] != 0 && mask.matrix_adjacent[i][j - 1] != mask.matrix_adjacent[i - 1][j]) ||
                (mask.matrix_adjacent[i][j - 1] != 0 && mask.matrix_adjacent[i][j + 1] != 0 && mask.matrix_adjacent[i][j - 1] != mask.matrix_adjacent[i][j + 1]) ||
                (mask.matrix_adjacent[i][j - 1] != 0 && mask.matrix_adjacent[i + 1][j] != 0 && mask.matrix_adjacent[i][j - 1] != mask.matrix_adjacent[i + 1][j]) ||
                (mask.matrix_adjacent[i][j + 1] != 0 && mask.matrix_adjacent[i - 1][j] != 0 && mask.matrix_adjacent[i][j + 1] != mask.matrix_adjacent[i - 1][j]) ||
                (mask.matrix_adjacent[i + 1][j] != 0 && mask.matrix_adjacent[i - 1][j] != 0 && mask.matrix_adjacent[i + 1][j] != mask.matrix_adjacent[i - 1][j]) ||
                (mask.matrix_adjacent[i][j + 1] != 0 && mask.matrix_adjacent[i + 1][j] != 0 && mask.matrix_adjacent[i][j + 1] != mask.matrix_adjacent[i + 1][j]))
            return true;
        else return false;
    }

    private int found_smallest(mask mask, int i, int j) {
        int small = 100;
        if (mask.matrix_adjacent[i][j - 1] < small) small = mask.matrix_adjacent[i][j - 1];
        if (mask.matrix_adjacent[i - 1][j] < small) small = mask.matrix_adjacent[i - 1][j];
        if (mask.matrix_adjacent[i][j + 1] < small) small = mask.matrix_adjacent[i][j + 1];
        if (mask.matrix_adjacent[i + 1][j] < small) small = mask.matrix_adjacent[i + 1][j];
        if (small == 100) small = 0;
        return small;
    }

    private boolean has_adjacent(mask mask, int i, int j) {
        if (mask.matrix_mask[i][j] == mask.matrix_mask[i + 1][j] || mask.matrix_mask[i][j] == mask.matrix_mask[i][j + 1] || mask.matrix_mask[i][j] == mask.matrix_mask[i - 1][j] || mask.matrix_mask[i][j] == mask.matrix_mask[i][j - 1])
            return true;
        else return false;

    }

    private void fix_matrix_adjacent(mask mask, int l, int m) {
        if (l == 0) return;
        else {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 5; j++) {
                    if (mask.matrix_adjacent[i][j] == l)
                        mask.matrix_adjacent[i][j] = m;
                }
            }
        }

    }

    private void found_first_adjacent(mask mask) {
        for (int i = 6; i >= 0; i--) {
            for (int j = 0; j < 5; j++) {
                if (mask.matrix_mask[i][j] == mask.matrix_mask[i - 1][j] || mask.matrix_mask[i][j] == mask.matrix_mask[i][j + 1]) {
                    mask.matrix_adjacent[i][j] = 1;
                    return;
                }
            }
        }
        return;
    }

    private class square {
        int el1_x;
        int el1_y;
        int el2_x;
        int el2_y;
        int el3_x;
        int el3_y;
        int el4_x;
        int el4_y;
    }

    private class mask {
        int[][] matrix_mask = new int[6][5];
        int[][] matrix_adjacent = new int[6][5];

        mask(Shelf shelf, ItemType type) {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 5; j++) {
                    matrix_adjacent[i][j] = 0;
                    if (shelf.getCard(i, j).getType().equals(type)) this.matrix_mask[i][j] = 1;
                    else this.matrix_mask[i][j] = 0;
                }
            }
        }
    }
}