package it.polimi.ingsw.model;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.ItemCards.ItemType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ItemCardTest {
    @Test
    void testItemCard() {
        for (ItemType type : ItemType.values()) {
            ItemCard currentItemCard = new ItemCard(type, 0);
            assertEquals(currentItemCard.getType(), type);
            assertEquals(currentItemCard.getSprite(), 0);
        }

        for (int i = 0; i < 3; i++) {
            ItemCard currentItemCard = new ItemCard(ItemType.getRandomItemType(), i);
            assertEquals(currentItemCard.getSprite(), i);
        }

        assertThrows(IllegalArgumentException.class, () -> new ItemCard(ItemType.getRandomItemType(), -1));
        assertThrows(IllegalArgumentException.class, () -> new ItemCard(ItemType.getRandomItemType(), 3));
    }
}