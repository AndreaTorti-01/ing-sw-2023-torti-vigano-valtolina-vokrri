package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.serializable.ChatMsg;
import it.polimi.ingsw.network.serializable.MoveMsg;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
    GameController controller;

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
        Game model = new Game();
        controller = new GameController(model);
    }

    @Test
    public void testInitGame() throws Exception {
        assertDoesNotThrow(() -> controller.initGame(2));
        assertThrows(IllegalStateException.class, () -> controller.initGame(3));

        assertThrows(IllegalArgumentException.class, () -> new GameController(new Game()).initGame(0));
    }

    @Test
    void makeMove() {
        controller.initGame(2);
        controller.addPlayer(getRandomName());
        controller.addPlayer(getRandomName());

        List<List<Integer>> validMoveParam = new ArrayList<>();
        List<Integer> validMoveCoords = new ArrayList<>();
        validMoveCoords.add(4);
        validMoveCoords.add(1);
        validMoveParam.add(validMoveCoords);

        List<List<Integer>> validMoveParam2 = new ArrayList<>();
        List<Integer> validMoveCoords2 = new ArrayList<>();
        validMoveCoords2.add(5);
        validMoveCoords2.add(1);
        validMoveParam2.add(validMoveCoords2);

        List<List<Integer>> invalidMoveParam = new ArrayList<>();
        List<Integer> invalidMoveCoords = new ArrayList<>();
        invalidMoveCoords.add(10);
        invalidMoveCoords.add(5);
        invalidMoveParam.add(invalidMoveCoords);

        assertTrue(controller.makeMove(new MoveMsg(validMoveParam, 0)));
        assertFalse(controller.makeMove(new MoveMsg(validMoveParam2, 6)));
        assertFalse(controller.makeMove(new MoveMsg(invalidMoveParam, 0)));
    }

    @Test
    void addPlayer() {
        controller.initGame(2);
        controller.addPlayer("pippo");
        controller.addPlayer("pluto");
        assertThrows(IllegalStateException.class, () -> controller.addPlayer("paperino"));
    }

    @Test
    void addChatMessage() {
        assertDoesNotThrow(() -> controller.addChatMessage(new ChatMsg("pippo", null, true, "ciao")));
        assertDoesNotThrow(() -> controller.addChatMessage(new ChatMsg("pluto", "pippo", false, "ciao2")));
    }

    @Test
    void cheat() {
        controller.initGame(2);
        controller.addPlayer(getRandomName());
        controller.addPlayer(getRandomName());
        assertDoesNotThrow(() -> controller.cheat());
    }
}