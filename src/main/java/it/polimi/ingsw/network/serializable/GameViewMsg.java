package it.polimi.ingsw.network.serializable;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.commonGoalCards.CommonGoalCard;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Stack;

/**
 * A class that represents an unmodifiable Game View.
 * This class is a representation of the game state.
 */
public class GameViewMsg implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final ItemCard[][] board;
    private final boolean[][] boardValid;
    private final List<CommonGoalCard> commonGoalCards;
    private final List<Player> players;
    private final Player currentPlayer;
    private final boolean isBagEmpty;
    private final Game.Status status;
    private final Player winner;
    private final Stack<ChatMsg> messages;

    /**
     * Creates a new Game View Message from the provided model.
     *
     * @param model the model from which the Game View Message is created.
     */
    public GameViewMsg(Game model) {
        if (model == null) throw new IllegalArgumentException();

        if (model.getBoard() != null) {
            this.board = model.getBoard().getTiles();
            this.boardValid = model.getBoard().getLayout();
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
        this.messages = model.getMessages();
    }

    /**
     * @return the current state of the board.
     */
    public ItemCard[][] getBoard() {
        return board;
    }

    /**
     * @return the board valid tiles.
     */
    public boolean[][] getBoardValid() {
        return boardValid;
    }

    /**
     * @param player the player whose shelf to get of.
     * @return the shelf of the provided player.
     */
    public ItemCard[][] getShelfOf(Player player) {
        for (Player p : players) {
            if (p.getName().equals(player.getName())) {
                return p.getShelf().getItems();
            }
        }
        return null;
    }

    /**
     * @return the Common Goal Cards in the current game.
     */
    public List<CommonGoalCard> getCommonGoalCards() {
        return commonGoalCards;
    }

    /**
     * @return the player whose turn it is in the current game.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @return the list of players playing the current game.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * @return true if the bag of the current game has no more Item Cards, false otherwise.
     */
    public boolean isBagEmpty() {
        return isBagEmpty;
    }

    /**
     * @return the status of the current game.
     */
    public Game.Status getGameStatus() {
        return this.status;
    }

    /**
     * @return the player who won the current game.
     */
    public Player getWinner() {
        return this.winner;
    }

    /**
     * @return the messages sent during the current game.
     */
    public Stack<ChatMsg> getMessages() {
        return this.messages;
    }

}
