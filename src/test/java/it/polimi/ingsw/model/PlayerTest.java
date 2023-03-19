package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class PlayerTest {

    @Test
    void playerTest() {
        assertThrows(IllegalArgumentException.class, () -> new Player(null));
    }
}