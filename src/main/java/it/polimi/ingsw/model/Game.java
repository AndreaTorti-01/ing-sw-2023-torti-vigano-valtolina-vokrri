package it.polimi.ingsw.model;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import it.polimi.ingsw.utils.Constants.*;

import static it.polimi.ingsw.utils.Constants.*;

public class Game {
    private final ArrayList<Player> players;
    private final Bag bag;
    private final Board board;
    private final ArrayList<CommonGoalCard> commonGoalCards;

    public Game(String... playersNames) throws IllegalArgumentException {
        int numberOfPlayers = playersNames.length;

        if (numberOfPlayers < minNumberOfPlayers || numberOfPlayers > maxNumberOfPlayers)
            throw new IllegalArgumentException(
                    "provided number of players (" + numberOfPlayers + ") is out of range " + minNumberOfPlayers + "-" + maxNumberOfPlayers
            );

        // initialization of a new Bag
        bag = new Bag();

        // initialization of a new Board
        try {
            board = new Board(numberOfPlayers);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // initialization of the players and corresponding personal goal cards
        players = this.initPlayers(playersNames);

        // initialization of the common goal cards
        commonGoalCards = this.getRandomCommonGoalCards();


    }

    /**
     * @return the players
     */
    public ArrayList<Player> getPlayers() {
        return players;
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
        ArrayList<Integer> indexes = this.getIndexes(numberOfCardTypes);

        for (int i = 0; i < numberOfCommonGoalCardsInGame; i++) {
            // chooses and removes a random index from the indexes array in order not to have duplicates
            int randomIndex = new Random().nextInt(0, indexes.size());
            indexes.remove(randomIndex);

            CommonGoalCard currentCommonGoalCard = new CommonGoalCard(numberOfPlayers);

            // instantiates the strategy from the given name, selected from the ones available,
            // and gives it to the current common goal card.
            String className = "CommonGoalCardStrat_" + CommonGoalCardType.values()[randomIndex].toString();
            try {
                Class<?> strategy = Class.forName(className);
                currentCommonGoalCard.setStrategy((CommonGoalCardStrat) strategy.newInstance());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            // adds the newly created common goal card to the list
            commonGoalCards.add(new CommonGoalCard(numberOfPlayers));
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
}
