package it.polimi.ingsw.model;

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
    private boolean gameEnded;

    /**
     * @return the currentPlayer
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @return the players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * This method initializes all the model elements too
     *
     * @param playerNames
     * @throws IllegalArgumentException
     */
    public void setPlayers(String... playerNames) throws IllegalArgumentException {
        // initialization of a new Bag
        bag = new Bag();

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

        gameEnded = false;
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
     * @return the number of players
     */
    public int getNumberOfPlayers() {
        return players.size();
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

        return players;
    }

    /**
     * @return an arraylist of random CommonGoalCard
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

    public boolean isGameEnded() {
        return gameEnded;
    }

    public void setGameHasEnded() {
        this.gameEnded = true;
    }
}
