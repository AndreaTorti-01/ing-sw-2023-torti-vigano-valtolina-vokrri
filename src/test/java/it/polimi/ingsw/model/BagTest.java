package it.polimi.ingsw.model;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.ItemCards.ItemType;
import it.polimi.ingsw.utils.Constants;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BagTest {
    @Test
    void BagTest1() {
        Bag bag1 = new Bag();
        assertEquals(Constants.maxNumberOfItemCards, bag1.getCardsInside().size());
    }

    @Test
    void drawCard() {
        Bag bag = new Bag();
        ItemCard ic;

        // initialize a set with all the possible item types
        Set<ItemType> itemTypes = new HashSet<>();
        Collections.addAll(itemTypes, ItemType.values());

        // draw 1 card and check that the size of the bag is 131, then remove the type of the card from the set
        ic = bag.drawCard();
        ItemType extracted = ic.getType();
        assertEquals(131, bag.getCardsInside().size());
        itemTypes.remove(extracted);

        // check that the bag contains 22 cards of each type except the one that was drawn
        for (ItemType it : itemTypes) {
            assertEquals(Constants.numberOfItemCardsWithSameType, bag.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(it)).toList().size());
        }
        assertEquals(21, bag.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(extracted)).toList().size());

        // draw 131 cards and check the bag is empty
        for (int i = 0; i < 131; i++) {
            ic = bag.drawCard();
        }
        assertEquals(0, bag.getCardsInside().size());

        // draw a card from an empty bag and check the return value is null
        ic = bag.drawCard();
        assertNull(ic);
    }

    @Test
    void randomTest() {
        ItemCard ic_6;
        Bag Bag3 = new Bag();
        for (int k = 0; k < 5; k++) {
            ic_6 = Bag3.drawCard();
            System.out.println(ic_6.getType());
        }

    }

}