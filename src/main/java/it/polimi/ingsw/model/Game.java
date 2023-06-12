package it.polimi.ingsw.model;

import it.polimi.ingsw.model.commonGoalCards.CommonGoalCard;
import it.polimi.ingsw.model.commonGoalCards.CommonGoalCardStrat;
import it.polimi.ingsw.model.commonGoalCards.CommonGoalCardType;
import it.polimi.ingsw.network.serializable.ChatMsg;
import it.polimi.ingsw.network.serializable.GameViewMsg;
import it.polimi.ingsw.utils.ObservableImpl;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import static it.polimi.ingsw.utils.Constants.*;

public class Game extends ObservableImpl {
    private final Bag bag;
    private final Stack<ChatMsg> messages;
    private List<CommonGoalCard> commonGoalCards;
    private List<Player> players;
    private Player currentPlayer;
    private Board board;
    private Game.Status gameStatus = Game.Status.WAITING;
    private Player winner;
    private int numberOfPlayers;
    private boolean isGameEnded;

    public Game() {
        this.bag = new Bag();
        this.players = new ArrayList<>();
        this.messages = new Stack<>();
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
            this.gameStatus = Game.Status.STARTED;
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
    public Game.Status getGameStatus() {
        return gameStatus;
    }

    /**
     * sets the game to ended
     */
    public void endGame() {
        this.gameStatus = Game.Status.ENDED;
        addFinalPoints();
        calculateWinner();
        notifyObservers(new GameViewMsg(this));
    }

    private void addFinalPoints() {
        // at the end of the game the personalGoal points are assigned
        System.err.println("setting PersonalGoalCard points...");
        for (Player player : players) {
            int currentScore = player.getScore();
            // calculates the score obtained from the personal goal card
            int personalGoalCardScore = player.getPersonalGoalCard().checkPattern(player.getShelf());

            // sets the final score of the player
            player.setScore(currentScore + personalGoalCardScore);
        }

        // is assigned a bonus point to the first player completing the shelf.
        // (in case of multiple full-shelves, the first one in the player list is necessarily the first-completed one)
        System.err.println("setting the final bonus point...");
        for (Player player : players) {
            if (player.getShelf().isFull()) {
                player.setScore(player.getScore() + 1);
                break;
            }
        }

        // assigning bonus points for the size of the tile aggregations - using headLiminate() function as in AGGAREGATE cgc strategy
        for (Player player : players) {
            Shelf shelfCopy = player.getShelf().getCopy();
            for (int i = 0; i < numberOfRows; i++)
                for (int j = 0; j < numberOfColumns; j++) {
                    int aggregateScore;
                    int aggregateSize = headLiminate(shelfCopy, shelfCopy.getCardAt(i, j));

                    switch (aggregateSize) {
                        case 0, 1, 2 -> aggregateScore = 0;
                        case 3 -> aggregateScore = 2;
                        case 4 -> aggregateScore = 3;
                        case 5 -> aggregateScore = 5;
                        default -> aggregateScore = 8;
                    }

                    player.setScore(player.getScore() + aggregateScore);
                }
        }
    }

    public void advancePlayerTurn() {
        // advances the player
        Player previousPlayer = currentPlayer;
        currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % numberOfPlayers);
        GameViewMsg currentGameView = new GameViewMsg(this);
        List<List<Integer>> emptyList = new ArrayList<>();

        // CommonGoalCard checking...
        // Must check the shelf of the previous player, who just played his move
        // CommonGoalCards points will be assigned at runtime
        for (int i = 0; i < commonGoalCards.size(); i++) {
            //if he didn't achieve it yet and now the pattern is respected, commonGC points will be assigned
            if (!previousPlayer.hasAchievedCommonGoalCard(i) && commonGoalCards.get(i).checkPattern(previousPlayer.getShelf())) {
                previousPlayer.setScore(previousPlayer.getScore() + commonGoalCards.get(i).popPoints());
                previousPlayer.setAchievedCommonGoalCard(i);
            }
        }

        // if the last moving player is at the bottom of the list, and there is at least a player
        // with a full shelf, the game is over
        if (previousPlayer.equals(players.get(players.size() - 1))) {
            for (Player player : players) {
                if (player.getShelf().isFull() && !this.gameStatus.equals(Status.ENDED)) { // ? ridondante
                    this.endGame();
                    return;
                }
            }
        }

        // if there are no takeable cards one next to each other, refill the board
        boolean needsRefill = true;
        for (int row = 0; row < boardSize && needsRefill; row++) {
            for (int column = 0; column < boardSize && needsRefill; column++) {

                // if the card considered is takeable...
                if (isTakeable(currentGameView, row, column, emptyList)) {

                    // check if one of the adjacent cards is takeable
                    if (row > 0 && isTakeable(currentGameView, row - 1, column, emptyList)) {
                        needsRefill = false;
                    } else if (row < boardSize - 1 && isTakeable(currentGameView, row + 1, column, emptyList)) {
                        needsRefill = false;
                    } else if (column > 0 && isTakeable(currentGameView, row, column - 1, emptyList)) {
                        needsRefill = false;
                    } else if (column < boardSize - 1 && isTakeable(currentGameView, row, column + 1, emptyList)) {
                        needsRefill = false;
                    }
                }
            }
        }

        if (needsRefill) this.refillBoard();

        // common points:
        // 8 - 4
        // 8 - 6 - 4
        // 8 - 6 - 4 - 2

        // personal
        // 1  2  3  4  5  6  -> numero di tessere rispettate
        // 1  2  4  6  9  12 -> punteggio assegnato

        // 3  4  5  6+  grandezza cluster
        // 2  3  5  8   punteggio

        notifyObservers(new GameViewMsg(this));
    }

    private void calculateWinner() {
        Player winner = null;
        int maxScore = 0;
        for (Player player : getPlayers()) {
            if (player.getScore() > maxScore) {
                maxScore = player.getScore();
                winner = player;
            }
        }
        setWinner(winner);
    }

    public Stack<ChatMsg> getMessages() {
        return this.messages;
    }

    public void addChatMessage(ChatMsg chatMsg) {
        this.messages.push(chatMsg);
        notifyObservers(new GameViewMsg(this));
    }

    public enum Status {
        WAITING,
        STARTED,
        ENDED
    }
}
