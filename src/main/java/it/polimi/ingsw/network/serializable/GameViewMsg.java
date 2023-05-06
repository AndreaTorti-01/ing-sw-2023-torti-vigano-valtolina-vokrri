package it.polimi.ingsw.network.serializable;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameStatus;
import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.commonGoalCards.CommonGoalCard;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class GameViewMsg implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final ItemCard[][] board;
    private final boolean[][] boardValid;
    private final List<CommonGoalCard> commonGoalCards;
    private final List<Player> players;
    private final Player currentPlayer;
    private final boolean isBagEmpty;
    private final GameStatus status;
    private final Player winner;

    public GameViewMsg(Game model) {
        if (model == null) throw new IllegalArgumentException();

        if (model.getBoard() != null) {
            this.board = model.getBoard().getTileMatrix();
            this.boardValid = model.getBoard().getValidMatrix();
        } else {
            board = null;
            boardValid = null;
        }

        this.commonGoalCards = model.getCommonGoalCards();
        this.players = model.getPlayers();
        this.currentPlayer = model.getCurrentPlayer();
        this.isBagEmpty = model.getBag().getCardsInside().size() == 0;
        this.status = model.getGameStatus();
        this.winner = model.getWinner();
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

    public List<CommonGoalCard> getCommonGoalCards() {
        return commonGoalCards;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean isBagEmpty() {
        return isBagEmpty;
    }

    public GameStatus getGameStatus() {
        return this.status;
    }

    public Player getWinner() {
        return this.winner;
    }

}
