package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.Constants;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private final Player[] players;
    private final Bag bag;
    private final Board board;
    private final CommonGoalCard[] commonGoalCards;

    public Game(String... playersNames) throws IllegalArgumentException {
        int numberOfPlayers = playersNames.length;

        if (numberOfPlayers < Constants.minNumberOfPlayers || numberOfPlayers > Constants.maxNumberOfPlayers)
            throw new IllegalArgumentException(
                    "provided number of players (" + numberOfPlayers + ") is out of range " + Constants.minNumberOfPlayers + "-" + Constants.maxNumberOfPlayers
            );

        // inizializzazione di una nuova bag
        bag = new Bag();

        // inizializzazione di una nuova board
        try {
            board = new Board(numberOfPlayers);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // inizializzazione dei players
        players = new Player[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            players[i] = new Player(playersNames[i]);
        }

        // inizializzazione delle commonGoalCards
        commonGoalCards = new CommonGoalCard[Constants.numberOfCommonGoalCardsInGame];
        for (int i = 0; i < Constants.numberOfCommonGoalCardsInGame; i++) {
            commonGoalCards[i] = new CommonGoalCard(numberOfPlayers);
        }

        PersonalGoalCard[] personalGoalCards = this.getRandomPersonalGoalCards();
        for (int i = 0; i < numberOfPlayers; i++) {
            players[i].setPersonalGoalCard(personalGoalCards[i]);
        }
    }

    public Player[] getPlayers() {
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

    public int getNumberOfPlayers() {
        return players.length;
    }

    /**
     * Creates and returns an array of random PersonalGoalCards
     *
     * @return array of random PersonalGoalCards
     */
    private PersonalGoalCard[] getRandomPersonalGoalCards() {
        Random random = new Random();
        int numberOfPlayers = this.getNumberOfPlayers();

        // list of available indexes
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < Constants.numberOfPersonalGoalCardsTypes; i++) indexes.add(i);


        PersonalGoalCard[] personalGoalCards = new PersonalGoalCard[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            int randomIndex = random.nextInt(0, indexes.size());
            personalGoalCards[i] = new PersonalGoalCard(randomIndex);

            // rimuove l'indice alla posizione randomIndex da indexes in modo che non si possa ripresentare
            indexes.remove(randomIndex);
        }

        return personalGoalCards;
    }
}
