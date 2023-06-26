package it.polimi.ingsw.model;

import it.polimi.ingsw.model.commonGoalCards.CommonGoalCard;
import it.polimi.ingsw.model.commonGoalCards.CommonGoalCardStrat;
import it.polimi.ingsw.model.commonGoalCards.CommonGoalCardType;
import it.polimi.ingsw.network.serializable.ChatMsg;
import it.polimi.ingsw.network.serializable.GameViewMsg;
import it.polimi.ingsw.utils.ObservableImpl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import static it.polimi.ingsw.utils.Common.headLiminate;
import static it.polimi.ingsw.utils.Common.isTakeable;
import static it.polimi.ingsw.utils.Constants.*;

/**
 * A class that represents the Game.
 * This class contains the status of a game, with all the information about it.
 */
public class Game extends ObservableImpl implements Serializable {
    @Serial
    private static final long serialVersionUID = -5751410109027050560L;
    /**
     * The bag from which to take the Item Cards to refill the board.
     */
    private Bag bag;
    /**
     * The stack of messages sent by the players in this game.
     */
    private Stack<ChatMsg> messages;
    /**
     * The list of players in this game.
     */
    private List<Player> players;
    /**
     * The list of Common Goal Cards in this game.
     */
    private List<CommonGoalCard> commonGoalCards;
    /**
     * The current player of this game.
     */
    private Player currentPlayer;
    /**
     * The board of this game from which the players will take the Item Cards.
     */
    private Board board;
    /**
     * The status of this game. Set to WAITING by default.
     */
    private Game.Status gameStatus = Game.Status.WAITING;
    /**
     * The winner of this game.
     */
    private Player winner;
    /**
     * The number of players in this game. Set to -1 by default.
     */
    private int numberOfPlayers = -1; // !IMPORTANT

    /**
     * Creates a new Game, initializing all the game items.
     */
    public Game() {
        this.bag = new Bag();
        this.players = new ArrayList<>();
        this.messages = new Stack<>();
    }

    /**
     * Loads a game from a file.
     * For persistence purposes.
     *
     * @param fileName the name of the file to load the game from.
     */
    private void loadGame(String fileName) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
            Game game = (Game) ois.readObject();
            ois.close();

            this.bag = game.bag;
            this.messages = game.messages;
            this.players = game.players;
            this.commonGoalCards = game.commonGoalCards;
            this.currentPlayer = game.currentPlayer;
            this.board = game.board;
            this.gameStatus = game.gameStatus;
            this.winner = game.winner;
            this.numberOfPlayers = game.numberOfPlayers;

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves the current game state (model) to a file
     */
    public void saveGame() {

        // if there is no saves directory, create it
        File savesDir = new File("saves");
        if (!savesDir.exists()) {
            if (savesDir.mkdir()) {
                System.out.println("saves directory created");
            } else {
                throw new RuntimeException("saves directory creation failed");
            }
        }

        // create file name using player names .sav
        StringBuilder fileName = new StringBuilder();
        for (Player player : players) {
            fileName.append(player.getName());
        }
        fileName.append(".sav");

        // open file, write game state (model) and close it
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saves/" + fileName));
            oos.writeObject(this);
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteSave() {
        StringBuilder fileName = new StringBuilder();
        for (Player player : players) {
            fileName.append(player.getName());
        }
        fileName.append(".sav");

        File file = new File("saves/" + fileName);
        if (file.delete()) {
            System.out.println("save deleted");
        } else {
            throw new RuntimeException("save deletion failed");
        }
    }

    /**
     * Initializes all the model elements.
     */
    public void initModel(Integer numberOfPlayers) throws IllegalArgumentException {

        // check the model is not already initialized
        if (this.numberOfPlayers != -1) // !IMPORTANT
            throw new IllegalStateException("the model is already initialized");

        // checking the numberOfPlayers is valid and setting it in the model
        if (numberOfPlayers < minNumberOfPlayers || numberOfPlayers > maxNumberOfPlayers)
            throw new IllegalArgumentException("provided number of players (" + numberOfPlayers + ") is out of range " + minNumberOfPlayers + "-" + maxNumberOfPlayers);
        this.numberOfPlayers = numberOfPlayers;

        // initialization of a new Board
        board = new Board(numberOfPlayers);

        // refills the board
        this.refillBoard();

        // initialization of the common goal cards
        commonGoalCards = this.getRandomCommonGoalCards();

        // notifies listeners of the changes
        notifyObservers(new GameViewMsg(this));
    }

    /**
     * Adds a new player to the game with the provided name.
     *
     * @param playerName the name of the player to be inserted.
     */
    public void addPlayer(String playerName) {
        if (this.players.size() == numberOfPlayers) throw new IllegalStateException("the game is full");

        // check if player name is already taken
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                this.notifyObservers(new GameViewMsg(this, true));
                return;
            }
        }

        // init player
        Player newPlayer = new Player(playerName);

        // set a random personal goal card to the player
        int randomIndex = new Random().nextInt(0, numberOfPersonalGoalCardTypes);
        newPlayer.setPersonalGoalCard(new PersonalGoalCardFactory().createPersonalGoalCard(randomIndex));

        // add player to the list
        players.add(newPlayer);

        // if it's the first added player, it's the current one.
        if (players.size() == 1) this.currentPlayer = newPlayer;

        // check if the player cap is reached
        if (numberOfPlayers != 0 && players.size() == numberOfPlayers) {

            // create all player names permutations possible
            List<String> playerNames = new ArrayList<>();
            for (Player player : players) {
                playerNames.add(player.getName());
            }
            List<String> playerNamesPermutations = generatePermutations(playerNames);

            // check if a file with said name exists
            for (String playerNamesPermutation : playerNamesPermutations) {
                File file = new File("saves/" + playerNamesPermutation + ".sav");
                if (file.exists()) {
                    loadGame("saves/" + playerNamesPermutation + ".sav");
                    this.notifyObservers(new GameViewMsg(this));
                    return;
                }
            }

            // if no file exists, start the current game
            this.gameStatus = Game.Status.STARTED;
        }

        // notify lobby
        this.notifyObservers(new GameViewMsg(this));
    }

    /**
     * Generates a list of random permutations from the provided list of strings.
     *
     * @param strings the list to be permutated.
     * @return a list of permutated strings.
     */
    private List<String> generatePermutations(List<String> strings) {
        List<String> permutations = new ArrayList<>();
        if (strings.size() == 1) {
            permutations.add(strings.get(0));
            return permutations;
        }
        for (String string : strings) {
            List<String> subList = new ArrayList<>(strings);
            subList.remove(string);
            List<String> subPermutations = generatePermutations(subList);
            for (String subPermutation : subPermutations) {
                permutations.add(string + subPermutation);
            }
        }
        return permutations;
    }

    /**
     * @return the Player that won this game.
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * Sets the provided player as the winner of the game.
     *
     * @param playerName the player that won the game.
     */
    public void setWinner(Player playerName) {
        this.winner = playerName;
    }


    /**
     * Refills the board with the Item Cards drawn from the bag.
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
     * @return a list of players playing this game.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * @return the current player whose turn it is.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @return the bag of this game.
     */
    public Bag getBag() {
        return bag;
    }

    /**
     * @return the board of this game.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * @return a list of common goal cards in this game.
     */
    public List<CommonGoalCard> getCommonGoalCards() {
        return commonGoalCards;
    }

    /**
     * @return a list of random Common Goal Cards.
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
     * @param bound the upper bound (excluded).
     * @return a list with all indexes in range 0 (included) - bound (excluded)
     */
    private List<Integer> getIndexes(int bound) {
        // initializes an arrayList that contains all indexes in range 0-bound
        List<Integer> indexes = new ArrayList<>(bound);
        for (int i = 0; i < bound; i++) indexes.add(i);

        return indexes;
    }

    /**
     * @return the status of this game.
     */
    public Game.Status getGameStatus() {
        return gameStatus;
    }

    /**
     * Sets the game status to ended.
     * Calculates all the final points and the winner.
     */
    public void endGame() {
        this.gameStatus = Game.Status.ENDED;
        addFinalPoints();
        calculateWinner();
        this.deleteSave();
        notifyObservers(new GameViewMsg(this));
    }

    /**
     * For every player, adds the final points obtained by completing the Personal Goal Card pattern,
     * by filling the shelf and by counting the number of aggregations.
     */
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

        // a bonus point is assigned to the first player completing the shelf.
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

    /**
     * Advances the turn of this game.
     * Moreover, checks if the current player has achieved his Common Goal Card,
     * if any of the players completed his shelf in order to end the game and
     * if the board needs to be refilled.
     * Saves the game to the specific file.
     */
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
                if (player.getShelf().isFull() && !this.gameStatus.equals(Status.ENDED)) {
                    this.endGame();
                    return;
                }
            }
        }

        // if there are no take-able cards one next to each other, refill the board
        boolean needsRefill = true;
        for (int row = 0; row < boardSize && needsRefill; row++) {
            for (int column = 0; column < boardSize && needsRefill; column++) {

                // if the card considered is take-able...
                if (isTakeable(currentGameView, row, column, emptyList)) {

                    // check if one of the adjacent cards is take-able
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

        this.saveGame();

        notifyObservers(new GameViewMsg(this));
    }

    /**
     * Calculates the winner of this game.
     */
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

    /**
     * @return the messages sent during this game.
     */
    public Stack<ChatMsg> getMessages() {
        return this.messages;
    }

    /**
     * Adds the provided message in the chat.
     *
     * @param chatMsg the message to be added.
     */
    public void addChatMessage(ChatMsg chatMsg) {
        this.messages.push(chatMsg);
        this.saveGame();
        notifyObservers(new GameViewMsg(this));
    }

    /**
     * ***************************** <p>
     * * FOR TESTING PURPOSE ONLY! * <p>
     * ***************************** <p>
     *
     * @return a random player from the ones playing this game.
     */
    public Player getRandomPlayer() {
        Random random = new Random();
        int randomIndex = random.nextInt(this.numberOfPlayers);
        return this.getPlayers().get(randomIndex);
    }

    /**
     * An Enum representing the status of the game.
     */
    public enum Status {
        WAITING,
        STARTED,
        ENDED
    }
}
