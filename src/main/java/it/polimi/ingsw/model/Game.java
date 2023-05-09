package it.polimi.ingsw.model;

import it.polimi.ingsw.model.commonGoalCards.CommonGoalCard;
import it.polimi.ingsw.model.commonGoalCards.CommonGoalCardStrat;
import it.polimi.ingsw.model.commonGoalCards.CommonGoalCardType;
import it.polimi.ingsw.network.serializable.GameViewMsg;
import it.polimi.ingsw.utils.Observable;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static it.polimi.ingsw.utils.Constants.*;

public class Game extends Observable {
    private final Bag bag;
    private List<CommonGoalCard> commonGoalCards;
    private List<Player> players;
    private Player currentPlayer;
    private Board board;
    private GameStatus gameStatus = GameStatus.WAITING;
    private Player winner;
    private int numberOfPlayers;

    public Game() {
        this.bag = new Bag();
        this.players = new ArrayList<>();
    }

    /**
     * This method initializes all the model elements
     */
    public void initModel(Integer numberOfPlayers) throws IllegalArgumentException {

        // checking the numberOfPlayers is valid and setting it in the model
        if (numberOfPlayers < minNumberOfPlayers || numberOfPlayers > maxNumberOfPlayers)
            throw new IllegalArgumentException("provided number of players (" + numberOfPlayers + ") is out of range " + minNumberOfPlayers + "-" + maxNumberOfPlayers);
        this.numberOfPlayers = numberOfPlayers;

        // initialization of a new Board
        try {
            board = new Board(numberOfPlayers);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // refills the board
        this.refillBoard();

        // initialization of the common goal cards
        commonGoalCards = this.getRandomCommonGoalCards();

        // current player is first player
        this.currentPlayer = players.get(0);

        // notifies listeners of the changes
        notifyObservers(new GameViewMsg(this));
    }

    public void addPlayer(String playerName) {
        // init player
        Player newPlayer = new Player(playerName);

        //set a random personalgoalcard to the player
        int randomIndex = new Random().nextInt(0, numberOfPersonalGoalCardTypes);
        newPlayer.setPersonalGoalCard(new PersonalGoalCardFactory().createPersonalGoalCard(randomIndex));

        // add player to the list
        players.add(newPlayer);

        // check if the player cap is reached and eventually start the game!
        if (numberOfPlayers != 0 && numberOfPlayers == players.size()) {
            this.gameStatus = GameStatus.STARTED;
        }

        // notify lobby
        this.notifyObservers(new GameViewMsg(this));
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
    private List<Player> initPlayers(List<String> playerNames) {
        Random random = new Random();

        // initializes a list of available indexes
        List<Integer> indexes = this.getIndexes(numberOfPersonalGoalCardTypes);

        players = new ArrayList<>();

        // for each player name, instantiates a new Player and gives him a random personal goal card
        for (String playerName : playerNames) {
            // chooses and removes a random index from the array of indexes in order not to have duplicates
            int randomIndex = random.nextInt(0, indexes.size());
            indexes.remove(randomIndex);

            PersonalGoalCardFactory personalGoalCardFactory = new PersonalGoalCardFactory();
            PersonalGoalCard currentPersonalGoalCard = personalGoalCardFactory.createPersonalGoalCard(randomIndex);
            Player player = new Player(playerName);

            // gives to the player the corresponding personal goal card
            player.setPersonalGoalCard(currentPersonalGoalCard);

            // adds the newly created player to the list
            players.add(player);
        }

        return players;
    }

    /**
     * Refills the board
     */
    public void refillBoard() {
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                if (this.board.isValid(row, column) && this.board.peekCard(row, column) == null) {
                    this.board.setTile(bag.drawCard(), row, column);
                }
            }
        }
    }


    /**
     * @return the players
     */
    public List<Player> getPlayers() {
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
    public List<CommonGoalCard> getCommonGoalCards() {
        return commonGoalCards;
    }

    /**
     * @return an arraylist of random CommonGoalCards.
     */
    private List<CommonGoalCard> getRandomCommonGoalCards() {
        List<CommonGoalCard> commonGoalCards = new ArrayList<>(numberOfCommonGoalCardsInGame);

        // initializes an array of indexes, then used to get two random indexes without duplicates
        List<Integer> indexes = this.getIndexes(numberOfItemCardTypes);

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
    private List<Integer> getIndexes(int bound) {
        // initializes an arrayList that contains all indexes in range 0-bound
        List<Integer> indexes = new ArrayList<>(bound);
        for (int i = 0; i < bound; i++) indexes.add(i);

        return indexes;
    }

    /**
     * @return the game status
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * sets the game to ended
     */
    public void endGame() {
        this.gameStatus = GameStatus.ENDED;

        // notifies listeners of the changes
        notifyObservers(new GameViewMsg(this));
    }

    public void advancePlayerTurn() {
        currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % numberOfPlayers);
        // TODO consider refilling board
        notifyObservers(new GameViewMsg(this));
    }
}
