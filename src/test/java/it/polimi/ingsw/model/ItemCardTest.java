package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemCardTest {

    @Test
    void testItemCard() {
        for (ItemType type : ItemType.values()) {
            ItemCard currentItemCard = new ItemCard(type);
            assertEquals(currentItemCard.type(), type);
        }
    }
}