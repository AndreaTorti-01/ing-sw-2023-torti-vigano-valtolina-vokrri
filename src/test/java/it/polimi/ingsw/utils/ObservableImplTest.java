package it.polimi.ingsw.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ObservableImplTest {
    private ObservableImpl observable;
    private List<Observer> twoObservers;

    @BeforeEach
    void setUp() {
        observable = new ObservableImpl() {
        };
        twoObservers = new ArrayList<>();
        twoObservers.add(new Observer() {
            public void update(String message) {
                System.out.println("Message received: " + message);
            }
        });
    }

    @Test
    void addObserver() {
        assertDoesNotThrow(() -> observable.addObserver(twoObservers.get(0)));
        assertDoesNotThrow(() -> observable.addObserver(twoObservers.get(0))); // again
        assertThrows(NullPointerException.class, () -> observable.addObserver(null));
    }

    @Test
    void notifyObservers() {
        for (Observer o : twoObservers) {
            assertDoesNotThrow(() -> observable.addObserver(o));
        }

        assertDoesNotThrow(() -> observable.notifyObservers("Test"));
        assertDoesNotThrow(() -> observable.notifyObservers(1)); // different output
    }
}