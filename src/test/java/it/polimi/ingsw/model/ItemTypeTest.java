package it.polimi.ingsw.model;

import it.polimi.ingsw.model.ItemCards.ItemType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTypeTest {
    @Test
    void testGetItemTypeFromAbbreviationMethod() {
        for (ItemType type : ItemType.values()) {
            char abbreviation = type.toString().charAt(0);
            assertEquals(ItemType.getItemTypeFromAbbreviation(abbreviation), type);
        }

        assertNull(ItemType.getItemTypeFromAbbreviation('*'));
    }

    @Test
    void testGetRandomTypeMethod() {
        ArrayList<ItemType> itemTypeValues = new ArrayList<>(Arrays.asList(ItemType.values()));
        assertTrue(itemTypeValues.contains(ItemType.getRandomItemType()));
    }
}
