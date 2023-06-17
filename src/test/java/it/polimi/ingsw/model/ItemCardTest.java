package it.polimi.ingsw.model;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.ItemCards.ItemType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemCardTest {
    @Test
    void testItemCard() {
        for (ItemType type : ItemType.values()) {
            ItemCard currentItemCard = new ItemCard(type, 2);
            assertEquals(currentItemCard.getType(), type);
            assertEquals(currentItemCard.getSprite(), 2);
        }
    }
}