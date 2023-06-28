package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.ItemCards.ItemType;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.commonGoalCards.CommonGoalCardType;
import it.polimi.ingsw.network.serializable.GameViewMsg;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommonTest {

    @Test
    void isTakeable() {
        Game g = new Game();
        g.initModel(2);
        g.addPlayer("diego");
        g.addPlayer("cristiano");
        List<List<Integer>> pickedCards = new ArrayList<>();
        List<Integer> cords = new ArrayList<>();
        cords.add(1);
        cords.add(0);
        pickedCards.add(cords);
        Common.isTakeable(new GameViewMsg(g),2,3, pickedCards );
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
        Shelf shelf = new Shelf().getCopy();
        ItemCard itemCard = new ItemCard(ItemType.TROPHIES,2);
        ItemCard itemCard1 = new ItemCard(ItemType.CATS,2);
        for(int row=0; row < Constants.numberOfRows; row++){
            for(int col=0; col< Constants.numberOfColumns; col++) {
               shelf.setCardAt(row,col,new ItemCard(ItemType.TROPHIES,2));
            }
        }
        assertEquals(30,1+Common.headLiminate(shelf,shelf.getCardAt(3,3)));

    }

    @Test
    void getCommonGoalCardPath() {
        for (CommonGoalCardType type : CommonGoalCardType.values())
            assertDoesNotThrow(()->Common.getCommonGoalCardPath(type));


    }

    @Test
    void getCommonGoalCardPointPath() {
        assertDoesNotThrow(()->Common.getCommonGoalCardPointPath(0));
        assertDoesNotThrow(()->Common.getCommonGoalCardPointPath(2));
    }

    @Test
    void getPersonalGoalCardPath() {
        assertDoesNotThrow(()->Common.getPersonalGoalCardPath(8));

    }

    @Test
    void getTilePath() {
    for (int i=0; i<3; i++){
        for(ItemType item: ItemType.values()){
            ItemCard ic = new ItemCard(item,i);
            assertDoesNotThrow(()->Common.getTilePath(ic));
        }
    }
    }
}