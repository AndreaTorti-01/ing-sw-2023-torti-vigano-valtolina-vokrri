package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShelfTest {

    @Test
    void ShelfTest() {
        Shelf s1 = new Shelf();
        ItemCard c1 = new ItemCard(ItemType.CATS);
        ItemCard c2 = new ItemCard(ItemType.BOOKS);

        assertNotNull(s1);
        assertNotNull(s1.getShelf());

        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 5; j++)
                assertNull(s1.getCardAt(i, j));

        s1.insert(0, c1);
        assertEquals(s1.getCardAt(5, 0), c1);
        s1.insert(0, c2);
        assertEquals(s1.getCardAt(4, 0), c2);

        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 5; j++)
                if (i != 5 && j != 0 || i != 4 && j != 0)
                    assertNull(s1.getCardAt(i, j));
    }
}