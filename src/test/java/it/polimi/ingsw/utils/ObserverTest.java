package it.polimi.ingsw.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ObserverTest {
    @Test
    void update() {
        Observer observer = new Observer() {
            public void update(Integer message) {
                System.out.println("Message received: " + message);
            }
        };
        assertDoesNotThrow(() -> observer.update("hi"));
    }
}