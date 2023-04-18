package it.polimi.ingsw.model;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.utils.Observable;
import it.polimi.ingsw.utils.Observer;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameView extends Observable implements Observer{
    private final ItemCard[][] board;
    private final boolean[][] boardValid;
    private final ArrayList<CommonGoalCard> commonGoalCards;
    private final ArrayList<Player> players;
    private final Player currentPlayer;
    private final boolean isBagEmpty;
    private final boolean isGameEnded;

    public GameView(Game model) {
        if (model == null) throw new IllegalArgumentException();

        this.board = model.getBoard().getTileMatrix();
        this.boardValid = model.getBoard().getValidMatrix();

        this.commonGoalCards = model.getCommonGoalCards();

        this.players = model.getPlayers();
        this.currentPlayer = model.getCurrentPlayer();

        this.isBagEmpty = model.getBag().getCardsInside().size() == 0;

        this.isGameEnded = model.isGameEnded();

        model.addObserver(this);
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException();
    }

    public ItemCard[][] getBoard() {
        return board;
    }

    public boolean[][] getBoardValid() {
        return boardValid;
    }

    public ItemCard[][] getShelfOf(Player player) {
        return players.get(players.indexOf(player)).getShelf().getItemsMatrix();
    }

    public ArrayList<CommonGoalCard> getCommonGoalCards() {
        return commonGoalCards;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public boolean isBagEmpty() {
        return isBagEmpty;
    }

    public boolean isGameEnded() {
        return isGameEnded;
    }

}
