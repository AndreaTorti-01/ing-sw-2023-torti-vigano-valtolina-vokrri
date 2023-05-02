package it.polimi.ingsw.network.serializable;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameStatus;
import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.commonGoalCards.CommonGoalCard;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class GameView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final ItemCard[][] board;
    private final boolean[][] boardValid;
    private final ArrayList<CommonGoalCard> commonGoalCards;
    private final ArrayList<Player> players;
    private final Player currentPlayer;
    private final boolean isBagEmpty;
    private final GameStatus status;

    public GameView(Game model) {
        if (model == null) throw new IllegalArgumentException();

        this.board = model.getBoard().getTileMatrix();
        this.boardValid = model.getBoard().getValidMatrix();
        this.commonGoalCards = model.getCommonGoalCards();
        this.players = model.getPlayers();
        this.currentPlayer = model.getCurrentPlayer();
        this.isBagEmpty = model.getBag().getCardsInside().size() == 0;
        this.status = model.getGameStatus();
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

    public GameStatus getGameStatus() {
        return this.status;
    }

}
