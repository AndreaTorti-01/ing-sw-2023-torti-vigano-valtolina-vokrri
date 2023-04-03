package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {
    @Test
    void testPlayer() {
        String randomName = this.generateRandomString();
        assertDoesNotThrow(() -> new Player(randomName));
    }

    void testGetName() {
        String randomName = this.generateRandomString();
        Player player = new Player(randomName);

        assertEquals(player.getName(), randomName);
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
