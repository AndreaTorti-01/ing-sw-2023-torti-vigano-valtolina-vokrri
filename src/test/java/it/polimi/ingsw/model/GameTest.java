package it.polimi.ingsw.model;

import it.polimi.ingsw.network.serializable.ChatMsg;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    // TODO: test refillBoard method
    @Test
    void testGameWithCorrectNumberOfPlayers() {
        for (int numberOfPlayers = 2; numberOfPlayers <= 4; numberOfPlayers++) {
            Game model = newGameWith(numberOfPlayers);

            // all the game items should have been initialized.
            assertNotNull(model.getBag());
            assertNotNull(model.getBoard());
            assertNotNull(model.getCommonGoalCards());

            // the game should start after adding all the required players
            assertEquals(model.getGameStatus(), Game.Status.STARTED);

            for (int i = 0; i < numberOfPlayers; i++) {
                // the current player should be the player at index i in the list of players
                assertEquals(model.getCurrentPlayer(), model.getPlayers().get(i));
                // the player should have a personal goal card and a shelf
                assertNotNull(model.getCurrentPlayer().getPersonalGoalCard());
                assertNotNull(model.getCurrentPlayer().getShelf());

                // advance the turn
                model.advancePlayerTurn();
            }

            // the game should not accept new players after reaching the maximum
            assertThrows(IllegalStateException.class, () -> model.addPlayer(generateRandomString()));
        }
    }

    @Test
    void testGameWithIllegalNumberOfPlayers() {
        assertThrows(IllegalArgumentException.class, () -> newGameWith(1));
        assertThrows(IllegalArgumentException.class, () -> newGameWith(5));
    }

    @Test
    void testChat() {
        // arbitrary number of players
        Game model = newGameWith(4);

        List<String> senders = new ArrayList<>();
        List<String> messages = new ArrayList<>();

        // generates 1000 random messages
        int numberOfMessages = 1000;
        for (int i = 0; i < numberOfMessages; i++) {
            String randomSenderPlayer = model.getRandomPlayer().getName();
            String randomMessage = generateRandomString();

            senders.add(randomSenderPlayer);
            messages.add(randomMessage);

            model.addChatMessage(new ChatMsg(null, randomSenderPlayer, true, randomMessage));
        }

        assertEquals(numberOfMessages, model.getMessages().size());

        int i = 0;
        for (ChatMsg chatMessage : model.getMessages()) {
            assertEquals(chatMessage.getMessage(), messages.get(i));
            assertEquals(chatMessage.getSenderPlayer(), senders.get(i));

            i++;
        }
    }

    @Test
    void testWinner() {
        // arbitrary number of players
        Game model = newGameWith(4);

        Player randomPlayer = model.getRandomPlayer();
        model.setWinner(randomPlayer);
        assertEquals(randomPlayer, model.getWinner());
    }

    // TODO: something more?
    @Test
    void testEndGame() {
        Game model = newGameWith(4);
        model.endGame();

        assertEquals(Game.Status.ENDED, model.getGameStatus());
    }

    /*
    @Test
    void testGeneratePermutations() {
        List<String> playerNames = new ArrayList<>();
        playerNames.add("Asdrubale");
        playerNames.add("Bufalo");
        playerNames.add("Carlo");
        playerNames.add("Davide");

        Game model = Game.createGame();
        List<String> permutations = model.generatePermutations(playerNames);
        for (String permutation : permutations) {
            System.out.println(permutation);
        }
        System.out.println(permutations.size());
    }
    */

    private Game newGameWith(int numberOfPlayers) {
        List<String> playerNames = new ArrayList<>();

        for (int i = 0; i < numberOfPlayers; i++)
            playerNames.add(this.generateRandomString());

        // creates a new Game and initializes it.
        Game model = new Game();
        model.initModel(numberOfPlayers);

        // adds the players
        for (String playerName : playerNames) {
            model.addPlayer(playerName);
        }

        model.saveGame();
        return model;
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
