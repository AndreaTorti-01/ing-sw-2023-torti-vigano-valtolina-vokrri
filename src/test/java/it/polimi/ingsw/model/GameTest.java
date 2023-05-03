package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameTest {
    @Test
    void testGameWithTwoPlayers() {
        int numberOfPlayers = 2;
        List<String> playerNames = new LinkedList<>();

        for (int i = 0; i < numberOfPlayers; i++)
            playerNames.add(this.generateRandomString());

        Game model = new Game();
        assertDoesNotThrow(() -> model.startGame(playerNames));
    }

    @Test
    void testGameWithThreePlayers() {
        int numberOfPlayers = 3;
        List<String> playerNames = new LinkedList<>();

        for (int i = 0; i < numberOfPlayers; i++)
            playerNames.add(this.generateRandomString());

        Game model = new Game();
        assertDoesNotThrow(() -> model.startGame(playerNames));
    }

    @Test
    void testGameWithFourPlayers() {
        int numberOfPlayers = 4;
        List<String> playerNames = new LinkedList<>();

        for (int i = 0; i < numberOfPlayers; i++)
            playerNames.add(this.generateRandomString());

        Game model = new Game();
        assertDoesNotThrow(() -> model.startGame(playerNames));
    }

    @Test
    void testGameWithIllegalNumberOfPlayers() {
        final List<String> playerNames = new LinkedList<>();
        playerNames.add(this.generateRandomString());
        Game model = new Game();
        assertThrows(IllegalArgumentException.class, () -> model.startGame(playerNames));

        int numberOfPlayers = 5;
        final List<String> playerNames2 = new LinkedList<>();

        for (int i = 0; i < numberOfPlayers; i++)
            playerNames.add(this.generateRandomString());

        Game model2 = new Game();
        assertThrows(IllegalArgumentException.class, () -> model2.startGame(playerNames2));
    }

    private String generateRandomString() {
        Random random = new Random();
        String lettersAndNumbers = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_.";
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            int randomIndex = random.nextInt(lettersAndNumbers.length());
            randomString.append(lettersAndNumbers.charAt(randomIndex));
        }

        return randomString.toString();
    }
}
