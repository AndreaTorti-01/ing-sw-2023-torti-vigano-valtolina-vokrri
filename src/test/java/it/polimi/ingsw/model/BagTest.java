package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {
    @Test
    void BagTest(){
        Bag bag1 = new Bag();
        assertEquals(132,bag1.getCardsInside().size());
    }

    @Test
    void drawCard(){
        Bag bag2 = new Bag();
        ItemCard ic_3;
        ic_3 = bag2.drawCard(bag2.getCardsInside());
        assertEquals(131, bag2.getCardsInside().size());
        if (ic_3.getType().equals(ItemType.BOOKS)) {
            int a=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.CATS)).toList().size();
            assertEquals(22,a);
            int b=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.FRAMES)).toList().size();
            assertEquals(22,b);
            int c=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.BOOKS)).toList().size();
            assertEquals(21,c);
            int d=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.TROPHIES)).toList().size();
            assertEquals(22,d);
            int e=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.GAMES)).toList().size();
            assertEquals(22,e);
            int f=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.PLANTS)).toList().size();
            assertEquals(22,f);
        }
        else if (ic_3.getType().equals(ItemType.CATS)){
            int a=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.CATS)).toList().size();
            assertEquals(21,a);
            int b=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.FRAMES)).toList().size();
            assertEquals(22,b);
            int c=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.BOOKS)).toList().size();
            assertEquals(22,c);
            int d=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.TROPHIES)).toList().size();
            assertEquals(22,d);
            int e=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.GAMES)).toList().size();
            assertEquals(22,e);
            int f=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.PLANTS)).toList().size();
            assertEquals(22,f);
        }
        else if (ic_3.getType().equals(ItemType.TROPHIES)){
            int a=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.CATS)).toList().size();
            assertEquals(22,a);
            int b=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.FRAMES)).toList().size();
            assertEquals(22,b);
            int c=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.BOOKS)).toList().size();
            assertEquals(22,c);
            int d=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.TROPHIES)).toList().size();
            assertEquals(21,d);
            int e=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.GAMES)).toList().size();
            assertEquals(22,e);
            int f=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.PLANTS)).toList().size();
            assertEquals(22,f);
        }
        else if (ic_3.getType().equals(ItemType.PLANTS)){
            int a=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.CATS)).toList().size();
            assertEquals(22,a);
            int b=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.FRAMES)).toList().size();
            assertEquals(22,b);
            int c=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.BOOKS)).toList().size();
            assertEquals(22,c);
            int d=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.TROPHIES)).toList().size();
            assertEquals(22,d);
            int e=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.GAMES)).toList().size();
            assertEquals(22,e);
            int f=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.PLANTS)).toList().size();
            assertEquals(21,f);
        }
        else if (ic_3.getType().equals(ItemType.GAMES)){
            int a=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.CATS)).toList().size();
            assertEquals(22,a);
            int b=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.FRAMES)).toList().size();
            assertEquals(22,b);
            int c=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.BOOKS)).toList().size();
            assertEquals(22,c);
            int d=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.TROPHIES)).toList().size();
            assertEquals(22,d);
            int e=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.GAMES)).toList().size();
            assertEquals(21,e);
            int f=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.PLANTS)).toList().size();
            assertEquals(22,f);
        }
        else if (ic_3.getType().equals(ItemType.FRAMES)) {
            int a=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.CATS)).toList().size();
            assertEquals(22,a);
            int b=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.FRAMES)).toList().size();
            assertEquals(21,b);
            int c=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.BOOKS)).toList().size();
            assertEquals(22,c);
            int d=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.TROPHIES)).toList().size();
            assertEquals(22,d);
            int e=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.GAMES)).toList().size();
            assertEquals(22,e);
            int f=bag2.getCardsInside().stream().filter(itemCard -> itemCard.getType().equals(ItemType.PLANTS)).toList().size();
            assertEquals(22,f);
        }
        for (int i=0; i<131;i++){
            ItemCard ic_4;
            ic_4= bag2.drawCard(bag2.getCardsInside());
        }
        assertEquals(0,bag2.getCardsInside().size());
        ItemCard ic_5;
        ic_5=bag2.drawCard(bag2.getCardsInside());
        assertNull(ic_5);
    }
}