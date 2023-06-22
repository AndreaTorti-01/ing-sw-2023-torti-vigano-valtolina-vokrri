package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.commonGoalCards.CommonGoalCardType;
import javafx.scene.image.Image;

/**
 * A class containing all the constant values of the game.
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
    public static final int rmiServerPort = 1099;
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
    public static String getCommonGoalCardPath(CommonGoalCardType type) {
        switch (type) {
            case CROSS -> {
                return String.format("/graphicalResources/commonGoalCards/%d.jpg", 10);
            }
            case DIAGONAL_FIVE -> {
                return String.format("/graphicalResources/commonGoalCards/%d.jpg", 11);
            }
            case EIGHT_EQUAL -> {
                return String.format("/graphicalResources/commonGoalCards/%d.jpg", 9);
            }
            case EQUAL_CORNERS -> {
                return String.format("/graphicalResources/commonGoalCards/%d.jpg", 8);
            }
            case FOUR_LINES_MAX_THREE_TYPES -> {
                return String.format("/graphicalResources/commonGoalCards/%d.jpg", 7);
            }
            case FOUR_QUARTETS -> {
                return String.format("/graphicalResources/commonGoalCards/%d.jpg", 3);
            }
            case SIX_PAIRS -> {
                return String.format("/graphicalResources/commonGoalCards/%d.jpg", 4);
            }
            case STAIR -> {
                return String.format("/graphicalResources/commonGoalCards/%d.jpg", 12);
            }
            case THREE_COLUMNS_MAX_THREE_TYPES -> {
                return String.format("/graphicalResources/commonGoalCards/%d.jpg", 5);
            }
            case TWO_RAINBOW_COLUMNS -> {
                return String.format("/graphicalResources/commonGoalCards/%d.jpg", 2);
            }
            case TWO_RAINBOW_LINES -> {
                return String.format("/graphicalResources/commonGoalCards/%d.jpg", 6);
            }
            case TWO_SQUARES -> {
                return String.format("/graphicalResources/commonGoalCards/%d.jpg", 1);
            }
            default -> {
                return "";
            }
        }
    }

    public static String getCommonGoalCardPointPath(int i) {
        return String.format("/graphicalResources/scoringTokens/scoring_%d.jpg", i);
    }

    public static String getPersonalGoalCardPath(int id) {
        return String.format("/graphicalResources/personalGoalCards/%dPGC.png", id);
    }
}
