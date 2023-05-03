package it.polimi.ingsw.model;

import it.polimi.ingsw.model.commonGoalCards.CommonGoalCard;
import it.polimi.ingsw.model.commonGoalCards.CommonGoalCardStrat;
import it.polimi.ingsw.model.commonGoalCards.CommonGoalCardType;
import it.polimi.ingsw.network.serializable.GameView;
import it.polimi.ingsw.utils.Observable;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static it.polimi.ingsw.utils.Constants.*;

public class Game extends Observable {
    private ArrayList<CommonGoalCard> commonGoalCards;
    private ArrayList<Player> players;
    private Player currentPlayer;
    private Bag bag;
    private Board board;
    private GameStatus status;
    private Player winner;

    public Game() {
        this.status = GameStatus.waiting;
        this.bag = new Bag();
    }


    /**
     * This method initializes all the model elements too
     *
     * @param playerNames the name of the players
     * @throws IllegalArgumentException
     */

    public void initGame(String... playerNames) throws IllegalArgumentException {


        int numberOfPlayers = playerNames.length;

        if (numberOfPlayers < minNumberOfPlayers || numberOfPlayers > maxNumberOfPlayers)
            throw new IllegalArgumentException("provided number of players (" + numberOfPlayers + ") is out of range " + minNumberOfPlayers + "-" + maxNumberOfPlayers);
        // initialization of a new Board
        try {
            board = new Board(numberOfPlayers);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        // initialization of the players and corresponding personal goal cards
        players = this.initPlayers(playerNames);

        // initialization of the common goal cards
        commonGoalCards = this.getRandomCommonGoalCards();
        currentPlayer = players.get(0);
        status = GameStatus.started;

        // notifies listeners of the changes
        notifyObservers(new GameView(this));
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    /**
     * Creates and return an arraylist of players,
     * giving each of them a random PersonalGoalCard
     *
     * @param playerNames an array containing the name of each player
     * @return an arraylist of players
     */
    private ArrayList<Player> initPlayers(String[] playerNames) {
        Random random = new Random();
        ArrayList<Player> players = new ArrayList<>(playerNames.length);

        // initializes a list of available indexes
        List<Integer> indexes = this.getIndexes(numberOfPersonalGoalCardTypes);

        // for each player name, instantiates a new Player and gives him a random personal goal card
        for (String playerName : playerNames) {
            // chooses and removes a random index from the array of indexes in order not to have duplicates
            int randomIndex = random.nextInt(0, indexes.size());
            indexes.remove(randomIndex);

            PersonalGoalCard currentPersonalGoalCard = new PersonalGoalCard(randomIndex);
            Player player = new Player(playerName);

            // gives to the player the corresponding personal goal card
            player.setPersonalGoalCard(currentPersonalGoalCard);

            // adds the newly created player to the list
            players.add(player);
        }

        // notifies listeners of the changes
        notifyObservers(new GameView(this));

        return players;
    }


    /**
     * @return the players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * @return the currentPlayer
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @return the number of players
     */
    public int getNumberOfPlayers() {
        return players.size();
    }

    /**
     * @return the bag
     */
    public Bag getBag() {
        return bag;
    }

    /**
     * @return the board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * @return the common goal cards
     */
    public ArrayList<CommonGoalCard> getCommonGoalCards() {
        return commonGoalCards;
    }

    /**
     * @return an arraylist of random CommonGoalCards.
     */
    private ArrayList<CommonGoalCard> getRandomCommonGoalCards() {
        int numberOfPlayers = this.getNumberOfPlayers();
        ArrayList<CommonGoalCard> commonGoalCards = new ArrayList<>(numberOfCommonGoalCardsInGame);

        // initializes an array of indexes, then used to get two random indexes without duplicates
        ArrayList<Integer> indexes = this.getIndexes(numberOfItemCardTypes);

        for (int i = 0; i < numberOfCommonGoalCardsInGame; i++) {
            // chooses and removes a random index from the indexes array in order not to have duplicates
            int randomTypeIndex = indexes.remove(new Random().nextInt(0, indexes.size()));

            // gets a random type
            CommonGoalCardType randomType = CommonGoalCardType.values()[randomTypeIndex];

            // gets the strategy from the type
            CommonGoalCardStrat randomStrat = CommonGoalCardType.getStrategyFromType(randomType);

            // creates the common goal card
            CommonGoalCard currentCommonGoalCard = new CommonGoalCard(numberOfPlayers, randomStrat);

            // adds the newly created common goal card to the list
            commonGoalCards.add(currentCommonGoalCard);
        }

        return commonGoalCards;
    }

    /**
     * @param bound the upper bound (excluded)
     * @return an array list with all indexes in range 0 (included) - bound (excluded)
     */
    private ArrayList<Integer> getIndexes(int bound) {
        // initializes an arrayList that contains all indexes in range 0-bound
        ArrayList<Integer> indexes = new ArrayList<>(bound);
        for (int i = 0; i < bound; i++) indexes.add(i);

        return indexes;
    }

    /**
     * @return the game status
     */
    public GameStatus getGameStatus() {
        return status;
    }

    /**
     * sets the game to ended
     */
    public void endGame() {
        this.status = GameStatus.ended;

        // notifies listeners of the changes
        notifyObservers(new GameView(this));
    }
}
