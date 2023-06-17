package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.ItemCards.ItemType;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.network.serializable.GameViewMsg;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.utils.Constants.*;

public class Common {
    /**
     * @param numberOfPlayers the number of players.
     * @return a boolean matrix that represents the board's active tiles.
     */
    public static boolean[][] getLayoutFrom(int numberOfPlayers) {
        return switch (numberOfPlayers) {
            case 2 -> twoPlayersBoardLayout;
            case 3 -> threePlayersBoardLayout;
            case 4 -> fourPlayersBoardLayout;
            default -> new boolean[boardSize][boardSize];
        };
    }

    public static boolean isTakeable(GameViewMsg modelView, int row, int column, List<List<Integer>> pickedCoords) {
        // TODO diego fix this

        boolean free = false; //has a null adjacent card
        boolean valid = true; //is a valid card (not taken yet, in the same row or col as the others)
        boolean adjacent = false; //is adjacent to at least one of the other cards


        // out of bound check
        if (row < 0 || row >= numberOfBoardRows || column < 0 || column >= numberOfBoardColumns) return false;
        // non-existing card check
        if (!modelView.getBoardValid()[row][column] || modelView.getBoard()[row][column] == null) return false;

        // checking if the card is adjacent to a free space (necessary to be takeable)
        if (row == 0 || row == numberOfBoardRows - 1) free = true;
        else if (column == 0 || column == numberOfBoardColumns - 1) free = true;
        else if (modelView.getBoard()[row - 1][column] == null || modelView.getBoard()[row + 1][column] == null || modelView.getBoard()[row][column - 1] == null || modelView.getBoard()[row][column + 1] == null)
            free = true;
        else if (!modelView.getBoardValid()[row - 1][column] || !modelView.getBoardValid()[row + 1][column] || !modelView.getBoardValid()[row][column - 1] || !modelView.getBoardValid()[row][column + 1])
            free = true;

        // the cards must be adjacent to each other (in line or in column) -> valid is used to check this condition
        if (pickedCoords != null && pickedCoords.size() > 0) {
            boolean inRow = true;
            boolean inColumn = true;
            for (List<Integer> coords : pickedCoords) {
                if (row == coords.get(0) && column == coords.get(1)) valid = false; //already picked
                if (row != coords.get(0)) inRow = false;
                if (column != coords.get(1)) inColumn = false;
            }
            valid = valid && (inRow || inColumn);
        }

        // the card must be adjacent to at least one of the other cards
        if (pickedCoords != null && pickedCoords.size() > 0) {
            for (List<Integer> coords : pickedCoords) {
                if (isAdjacent(row, column, coords.get(0), coords.get(1))) {
                    adjacent = true;
                    break;
                }
            }
        } else adjacent = true;

        return free && valid && adjacent;
    }

    public static boolean isAdjacent(int row, int column, int row2, int column2) {
        return Math.abs(row - row2) == 1 && column == column2 ||
                row == row2 && Math.abs(column - column2) == 1;
    }

    public static String getTilePath(ItemCard itemCard) {
        String tilesPath = "/graphicalResources/itemTiles/";
        switch (itemCard.getType()) {
            case CATS -> tilesPath += "Gatti1.";
            case BOOKS -> tilesPath += "Libri1.";
            case PLANTS -> tilesPath += "Piante1.";
            case TROPHIES -> tilesPath += "Trofei1.";
            case FRAMES -> tilesPath += "Cornici1.";
            case GAMES -> tilesPath += "Giochi1.";
        }
        switch (itemCard.getSprite()) {
            case 0 -> tilesPath += "1.png";
            case 1 -> tilesPath += "2.png";
            case 2 -> tilesPath += "3.png";
        }
        return tilesPath;
    }

    public static int headLiminate(Shelf shelfCopy, ItemCard hotCard) {
        List<ItemCard> heads;
        int adjacentToHead = 0;

        if (hotCard == null) return 0;

        // finds the hotCard in the shelf
        for (int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {
                if (hotCard.equals(shelfCopy.getCardAt(row, column))) {
                    // retrieves all the heads starting from the current position
                    heads = getHeads(shelfCopy, row, column);

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


    public static List<ItemCard> getHeads(Shelf shelfCopy, int row, int column) {
        List<ItemCard> heads = new ArrayList<>();
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
