package it.polimi.ingsw.model;

public class Game {
    Bag bag;
    Board board;
    CommonGoalCard[] cgCards;
    boolean[][] cgAchieved;
    PersonalGoalCard[] pgCards;
    boolean[] pgArchieved;
    Shelf[] shelves;
    String[] players;
    int[] scores;
    int playersNum; // sarà sempre uguale a players.length

    // implementazione che astrae board (con altri metodi poi...)
    public ItemCard[][] getBoardItems() {
        ItemCard[][] itemRet;
        itemRet = new ItemCard[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board.isValid(i, j)) {
                    itemRet[i][j] = board.peekCard(i, j);
                }
            }
        }
        return itemRet;
    }

    public ItemCard peekCardAt(int i, int j) {
        if (board.isValid(i, j)) {
            return board.peekCard(i, j);
        } else return null;
    }

    // implementazione che non astrae board, e quindi rende gli oggetti di gioco trasparenti a controller
    public Board getBoard() {
        return board;
    }
}
