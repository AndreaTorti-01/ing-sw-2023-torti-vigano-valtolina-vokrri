package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.ItemCards.ItemType;
import it.polimi.ingsw.model.Shelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonTest {

    @Test
    void isTakeable() {

    }

    @Test
    void isAdjacent() {
        assertTrue(Common.isAdjacent(0, 0, 0, 1));
        assertTrue(Common.isAdjacent(0, 0, 1, 0));

        assertFalse(Common.isAdjacent(0, 0, 1, 1));
        assertFalse(Common.isAdjacent(0, 0, 2, 0));
    }

    @Test
    void headLiminate() {
        Shelf shelf = new Shelf().getCopy();
        ItemCard itemCard = new ItemCard(ItemType.TROPHIES,2);
        ItemCard itemCard1 = new ItemCard(ItemType.CATS,2);
        for(int row=0; row < Constants.numberOfRows; row++){
            for(int col=0; col< Constants.numberOfColumns; col++) {
                if((row<3 && col<2)||row==3){shelf.setCardAt(row,col,new ItemCard(ItemType.TROPHIES,2));}
                else{shelf.setCardAt(row,col,new ItemCard(ItemType.CATS,2));}
            }
        }
        assertEquals(11, 1+Common.headLiminate(shelf,shelf.getCardAt(0,0)));

    }

    @Test
    void getHeads() {
    }

    @Test
    void getCommonGoalCardPath() {
    }

    @Test
    void getCommonGoalCardPointPath() {
    }

    @Test
    void getPersonalGoalCardPath() {

    }

    @Test
    void getTilePath() {

    }
}