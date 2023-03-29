package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.Constants;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    // costante per future espansioni
    private static final int NUMBER_OF_COMMON_GOAL_CARDS = 2;
    private final String[] players;
    private final Bag bag;
    private final Board board;
    private final CommonGoalCard[] commonGoalCards;
    private final boolean[][] commonGoalCardsAchieved;
    private final PersonalGoalCard[] personalGoalCards;
    private final boolean[] personalGoalCardsAchieved;
    private final Shelf[] shelves;
    private final int[] scores;

    public Game(String... playersNames) throws IllegalArgumentException {
        int numberOfPlayers = playersNames.length;

        if (numberOfPlayers < Constants.minNumberOfPlayers || numberOfPlayers > Constants.maxNumberOfPlayers)
            throw new IllegalArgumentException(
                    "provided number of players (" + numberOfPlayers + ") is out of range " + Constants.minNumberOfPlayers + "-" + Constants.maxNumberOfPlayers
            );

        Random random = new Random();

        // inizializzazione di una nuova bag
        bag = new Bag();
        try {
            board = new Board(numberOfPlayers);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // inizializzazione dei players
        players = new String[numberOfPlayers];
        System.arraycopy(playersNames, 0, players, 0, numberOfPlayers);

        // inizializzazione delle commonGoalCards
        commonGoalCards = new CommonGoalCard[NUMBER_OF_COMMON_GOAL_CARDS];
        for (int i = 0; i < NUMBER_OF_COMMON_GOAL_CARDS; i++) {
            commonGoalCards[i] = new CommonGoalCard(numberOfPlayers);
        }

        commonGoalCardsAchieved = new boolean[numberOfPlayers][2];
        personalGoalCards = new PersonalGoalCard[numberOfPlayers];
        // lista utilizzata per evitare di avere due personalGoalCard con lo stesso indice e, quindi, dello stesso tipo.
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < 12; i++) indexes.add(i);
        for (int i = 0; i < numberOfPlayers; i++) {
            int randomIndex = random.nextInt(0, indexes.size());
            // rimuove l'indice alla posizione randomIndex da indexes in modo che non si possa ripresentare
            indexes.remove(randomIndex);
            personalGoalCards[i] = new PersonalGoalCard(randomIndex);
        }

        personalGoalCardsAchieved = new boolean[numberOfPlayers];

        // inizializza le shelf per ogni player
        shelves = new Shelf[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            shelves[i] = new Shelf();
        }

        // inizializza gli score a zero per ogni player
        scores = new int[numberOfPlayers];
    }

    public String[] getPlayers() {
        return players;
    }

    public Bag getBag() {
        return bag;
    }

    // implementazione che non astrae board, e quindi rende gli oggetti di gioco trasparenti a controller
    public Board getBoard() {
        return board;
    }

    public CommonGoalCard[] getCommonGoalCards() {
        return commonGoalCards;
    }

    public boolean[][] getCommonGoalCardsAchieved() {
        return commonGoalCardsAchieved;
    }

    public PersonalGoalCard[] getPersonalGoalCards() {
        return personalGoalCards;
    }

    public boolean[] getPersonalGoalCardsAchieved() {
        return personalGoalCardsAchieved;
    }

    public Shelf[] getShelves() {
        return shelves;
    }

    public int[] getScores() {
        return scores;
    }

    public int getNumberOfPlayers() {
        return players.length;
    }
}
