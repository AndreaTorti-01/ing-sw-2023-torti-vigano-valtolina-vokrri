package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameTest {
    @Test
    void testGameWithTwoPlayers() {
        int numberOfPlayers = 2;
        String[] playerNames = new String[numberOfPlayers];

        for (int i = 0; i < numberOfPlayers; i++)
            playerNames[i] = this.generateRandomString();

        Game model = new Game();
        assertDoesNotThrow(() -> model.setPlayers(playerNames));
    }

    @Test
    void testGameWithThreePlayers() {
        int numberOfPlayers = 3;
        String[] playerNames = new String[numberOfPlayers];

        for (int i = 0; i < numberOfPlayers; i++)
            playerNames[i] = this.generateRandomString();

        Game model = new Game();
        assertDoesNotThrow(() -> model.setPlayers(playerNames));
    }

    @Test
    void testGameWithFourPlayers() {
        int numberOfPlayers = 4;
        String[] playerNames = new String[numberOfPlayers];

        for (int i = 0; i < numberOfPlayers; i++)
            playerNames[i] = this.generateRandomString();

        Game model = new Game();
        assertDoesNotThrow(() -> model.setPlayers(playerNames));
    }

    @Test
    void testGameWithIllegalNumberOfPlayers() {
        String playerName = this.generateRandomString();
        Game model = new Game();
        assertThrows(IllegalArgumentException.class, () -> model.setPlayers(playerName));

        int numberOfPlayers = 5;
        String[] playerNames = new String[numberOfPlayers];

        for (int i = 0; i < numberOfPlayers; i++)
            playerNames[i] = this.generateRandomString();

        Game model2 = new Game();
        assertThrows(IllegalArgumentException.class, () -> model2.setPlayers(playerNames));
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
