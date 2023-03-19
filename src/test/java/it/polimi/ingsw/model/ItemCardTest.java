package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemCardTest {

    @Test
    void getTypeCats() {
        ItemCard itemCard_1 = new ItemCard(ItemType.CATS);
        assertEquals(ItemType.CATS,itemCard_1.getType());
        assertNotEquals(ItemType.BOOKS,itemCard_1.getType());
        ItemCard itemCard_2 = new ItemCard(ItemType.GAMES);
        assertEquals(ItemType.GAMES,itemCard_2.getType());
        assertNotEquals(ItemType.CATS,itemCard_2.getType());
        ItemCard itemCard_3 = new ItemCard(ItemType.BOOKS);
        assertEquals(ItemType.BOOKS,itemCard_3.getType());
        assertNotEquals(ItemType.CATS,itemCard_3.getType());
        ItemCard itemCard_4 = new ItemCard(ItemType.FRAMES);
        assertEquals(ItemType.FRAMES,itemCard_4.getType());
        assertNotEquals(ItemType.CATS,itemCard_4.getType());
        ItemCard itemCard_5 = new ItemCard(ItemType.TROPHIES);
        assertEquals(ItemType.TROPHIES,itemCard_5.getType());
        assertNotEquals(ItemType.CATS,itemCard_5.getType());
        ItemCard itemCard_6 = new ItemCard(ItemType.PLANTS);
        assertEquals(ItemType.PLANTS,itemCard_6.getType());
        assertNotEquals(ItemType.CATS,itemCard_6.getType());
    }







}