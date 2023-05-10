package it.polimi.ingsw.utils;

import it.polimi.ingsw.network.serializable.GameViewMsg;

import java.util.List;

/**
 * A class containing all the constant values of the game
 */
public class Constants {
    public static final int numberOfColumns = 5;
    public static final int numberOfRows = 6;
    public static final int numberOfBoardRows = 9;
    public static final int numberOfBoardColumns = 9;

    public static final int maxNumberOfPlayers = 4;
    public static final int minNumberOfPlayers = 2;
    public static final int numberOfItemCardsWithSameType = 22;
    public static final int numberOfItemCardTypes = 6;
    public static final int maxNumberOfItemCards = numberOfItemCardTypes * numberOfItemCardsWithSameType;
    public static final int numberOfCommonGoalCardsInGame = 2;
    public static final int numberOfPersonalGoalCardTypes = 12;
    public static final int boardSize = 9;
    public static final String serverIpAddress = "127.0.0.1";
    public static final int serverPort = 8888;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_GREY = "\u001B[90m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    /**
     * takes in coords of a card you want to take and returns true if you can take it
     * already takes care of out of bounds and null cards
     *
     * @param modelView
     * @param row
     * @param column
     * @param pickedCoords already picked cards
     * @return
     */
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
        if (pickedCoords.size() > 0) {
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
        if (pickedCoords.size() > 0) {
            for (List<Integer> coords : pickedCoords) {
                if (isAdjacent(row, column, coords.get(0), coords.get(1))) {
                    adjacent = true;
                    break;
                }
            }
        }else adjacent = true;

        return free && valid && adjacent;
    }

    public static boolean isAdjacent(int row, int column, int row2, int column2) {
        return Math.abs(row - row2) == 1 && column == column2 ||
                row == row2 && Math.abs(column - column2) == 1;
    }
}
