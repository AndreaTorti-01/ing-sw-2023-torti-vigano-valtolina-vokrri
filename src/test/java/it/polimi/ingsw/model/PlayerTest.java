package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player;
    private String name;


    private String getRandomName() {
        Random random = new Random();
        String lettersAndNumbers = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_.";
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            int randomIndex = random.nextInt(lettersAndNumbers.length());
            randomString.append(lettersAndNumbers.charAt(randomIndex));
        }

        return randomString.toString();
    }

    @BeforeEach
    void setUp() {
        name = this.getRandomName();
        player = new Player(name);
    }

    @Test
    void testGetScore() {
        int randomScore = new Random().nextInt(50);
        player.setScore(randomScore);

        assertEquals(player.getScore(), randomScore);
    }

    @Test
    void setScore() {
        int randomScore = new Random().nextInt(50);
        player.setScore(randomScore);

        assertEquals(player.getScore(), randomScore);
    }

    @Test
    void getPersonalGoalCard() {
        for (int i = 0; i < Constants.numberOfPersonalGoalCardTypes; i++) {
            PersonalGoalCard personalGoalCard = new PersonalGoalCardFactory().createPersonalGoalCard(i);
            player.setPersonalGoalCard(personalGoalCard);

            assertEquals(player.getPersonalGoalCard(), personalGoalCard);
        }
    }

    @Test
    void setPersonalGoalCard() {
        for (int i = 0; i < Constants.numberOfPersonalGoalCardTypes; i++) {
            PersonalGoalCard personalGoalCard = new PersonalGoalCardFactory().createPersonalGoalCard(i);
            player.setPersonalGoalCard(personalGoalCard);

            assertEquals(player.getPersonalGoalCard(), personalGoalCard);
        }
    }

    @Test
    void getShelf() {
        assertNotNull(player.getShelf());
        assertDoesNotThrow(() -> player.getShelf());
    }

    @Test
    void getName() {
        assertEquals(player.getName(), name);
    }

    @Test
    void hasAchievedCommonGoalCard() {
        assertFalse(player.hasAchievedCommonGoalCard(0));
        assertFalse(player.hasAchievedCommonGoalCard(1));

        player.setAchievedCommonGoalCard(0);
        assertTrue(player.hasAchievedCommonGoalCard(0));

        player.setAchievedCommonGoalCard(1);
        assertTrue(player.hasAchievedCommonGoalCard(1));

        assertThrows(IllegalArgumentException.class, () -> player.hasAchievedCommonGoalCard(-1));
        assertThrows(IllegalArgumentException.class, () -> player.hasAchievedCommonGoalCard(2));
    }

    @Test
    void setAchievedCommonGoalCard() {
        player.setAchievedCommonGoalCard(0);
        assertTrue(player.hasAchievedCommonGoalCard(0));

        player.setAchievedCommonGoalCard(1);
        assertTrue(player.hasAchievedCommonGoalCard(1));

        assertThrows(IllegalArgumentException.class, () -> player.setAchievedCommonGoalCard(-1));
        assertThrows(IllegalArgumentException.class, () -> player.setAchievedCommonGoalCard(2));
    }
}
