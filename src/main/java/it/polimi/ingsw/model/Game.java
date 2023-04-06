package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.Constants;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private final ArrayList<Player> players;
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
        players = new ArrayList<>(numberOfPlayers);
        for (String playersName : playersNames) {
            players.add(new Player(playersName));
        }

        // inizializzazione delle commonGoalCards
        commonGoalCards = new CommonGoalCard[Constants.numberOfCommonGoalCardsInGame];

        // creo un array di due numeri randomici diversi tra 0 e 11 (inefficient...)
        List<Integer> randomNumbers = new ArrayList<>();
        for (int i = 0; i < Constants.numberOfCommonGoalCardsInGame; i++) {
            int random;
            do {
                random = new Random().nextInt(Constants.CommonGoalTypes.length);
            } while (randomNumbers.contains(random));
            randomNumbers.add(random);
        }

        for (int i = 0; i < Constants.numberOfCommonGoalCardsInGame; i++) {
            // scelgo uno dei randomNumbers per generare il nome di classe e lo elimino
            String className = "CommonGoalCardStrat_" + Constants.CommonGoalTypes[randomNumbers.get(0)];
            randomNumbers.remove(0);

            // istanzio la commongoalcard
            commonGoalCards[i] = new CommonGoalCard(numberOfPlayers);

            // istanzio la strategia e la imposto
            try {
                Class<?> clazz = Class.forName(className);
                commonGoalCards[i].setStrat((CommonGoalCardStrat) clazz.newInstance());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }

        PersonalGoalCard[] personalGoalCards = this.getRandomPersonalGoalCards();
        for (int i = 0; i < numberOfPlayers; i++) {
            players.get(i).setPersonalGoalCard(personalGoalCards[i]);
        }
    }

    public ArrayList<Player> getPlayers() {
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
        return players.size();
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
