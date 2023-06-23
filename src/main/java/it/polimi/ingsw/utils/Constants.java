package it.polimi.ingsw.utils;

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
}
