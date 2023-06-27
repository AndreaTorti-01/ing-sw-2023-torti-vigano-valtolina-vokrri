package it.polimi.ingsw.model;

import it.polimi.ingsw.model.commonGoalCards.CommonGoalCard;
import it.polimi.ingsw.model.commonGoalCards.CommonGoalCardStrat_SHAPE;
import it.polimi.ingsw.model.commonGoalCards.CommonGoalCardType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalCardStrat_SHAPETest {
    String fileExtension = ".txt";
    int numberOfTests = 10;

    @Test
    void checkPattern_EQUAL_CORNERS() {
        String folderName = "/shelvesForTesting/testForEQUAL_CORNERS/";
        CommonGoalCardStrat_SHAPE commonGoalCardStrat = new CommonGoalCardStrat_SHAPE(CommonGoalCardType.EQUAL_CORNERS);

        for (int i = 0; i < numberOfTests; i++) {
            String fileName;
            if (i + 1 < 10) fileName = String.format(folderName + "/TEST_FALSE_0%d" + fileExtension, i + 1);
            else fileName = String.format(folderName + "/TEST_FALSE_%d" + fileExtension, i + 1);

            Shelf shelf = new ShelfFactory().createShelf(fileName);

            assertFalse(commonGoalCardStrat.checkPattern(shelf));
        }

        for (int i = 0; i < numberOfTests; i++) {
            String fileName;
            if (i + 1 < 10) fileName = String.format(folderName + "TEST_TRUE_0%d" + fileExtension, i + 1);
            else fileName = String.format(folderName + "TEST_TRUE_%d" + fileExtension, i + 1);

            Shelf shelf = new ShelfFactory().createShelf(fileName);

            assertTrue(commonGoalCardStrat.checkPattern(shelf));
        }
    }

    @Test
    void checkPattern_CROSS() {
        String folderName = "/shelvesForTesting/testForCROSS/";
        CommonGoalCardStrat_SHAPE commonGoalCardStrat = new CommonGoalCardStrat_SHAPE(CommonGoalCardType.CROSS);

        for (int i = 0; i < numberOfTests; i++) {
            String fileName;
            if (i + 1 < 10) fileName = String.format(folderName + "/TEST_FALSE_0%d" + fileExtension, i + 1);
            else fileName = String.format(folderName + "/TEST_FALSE_%d" + fileExtension, i + 1);

            Shelf shelf = new ShelfFactory().createShelf(fileName);

            assertFalse(commonGoalCardStrat.checkPattern(shelf));
        }

        for (int i = 0; i < numberOfTests; i++) {
            String fileName;
            if (i + 1 < 10) fileName = String.format(folderName + "TEST_TRUE_0%d" + fileExtension, i + 1);
            else fileName = String.format(folderName + "TEST_TRUE_%d" + fileExtension, i + 1);

            Shelf shelf = new ShelfFactory().createShelf(fileName);

            assertTrue(commonGoalCardStrat.checkPattern(shelf));
        }
    }

    @Test
    void checkPattern_DIAGONAL_FIVE() {
        String folderName = "/shelvesForTesting/testForDIAGONAL_FIVE/";
        CommonGoalCardStrat_SHAPE commonGoalCardStrat = new CommonGoalCardStrat_SHAPE(CommonGoalCardType.DIAGONAL_FIVE);

        for (int i = 0; i < numberOfTests; i++) {
            String fileName;
            if (i + 1 < 10) fileName = String.format(folderName + "/TEST_FALSE_0%d" + fileExtension, i + 1);
            else fileName = String.format(folderName + "/TEST_FALSE_%d" + fileExtension, i + 1);

            Shelf shelf = new ShelfFactory().createShelf(fileName);

            assertFalse(commonGoalCardStrat.checkPattern(shelf));
        }

        for (int i = 0; i < numberOfTests; i++) {
            String fileName;
            if (i + 1 < 10) fileName = String.format(folderName + "TEST_TRUE_0%d" + fileExtension, i + 1);
            else fileName = String.format(folderName + "TEST_TRUE_%d" + fileExtension, i + 1);

            Shelf shelf = new ShelfFactory().createShelf(fileName);

            assertTrue(commonGoalCardStrat.checkPattern(shelf));
        }
    }

    @Test
    void checkPattern_TWO_SQUARES() {
        String folderName = "/shelvesForTesting/testForTWO_SQUARES/";
        CommonGoalCardStrat_SHAPE commonGoalCardStrat = new CommonGoalCardStrat_SHAPE(CommonGoalCardType.TWO_SQUARES);

        for (int i = 0; i < numberOfTests; i++) {
            String fileName;
            if (i + 1 < 10) fileName = String.format(folderName + "/TEST_FALSE_0%d" + fileExtension, i + 1);
            else fileName = String.format(folderName + "/TEST_FALSE_%d" + fileExtension, i + 1);

            Shelf shelf = new ShelfFactory().createShelf(fileName);

            assertFalse(commonGoalCardStrat.checkPattern(shelf));
        }

        for (int i = 0; i < numberOfTests; i++) {
            String fileName;
            if (i + 1 < 10) fileName = String.format(folderName + "TEST_TRUE_0%d" + fileExtension, i + 1);
            else fileName = String.format(folderName + "TEST_TRUE_%d" + fileExtension, i + 1);

            Shelf shelf = new ShelfFactory().createShelf(fileName);

            assertTrue(commonGoalCardStrat.checkPattern(shelf));
        }
    }

    @Test
    void testGetType() {
        CommonGoalCard commonGoalCard = new CommonGoalCard(4, new CommonGoalCardStrat_SHAPE(CommonGoalCardType.EQUAL_CORNERS));
        assertEquals(
                commonGoalCard.getType(),
                CommonGoalCardType.EQUAL_CORNERS
        );

        commonGoalCard = new CommonGoalCard(4, new CommonGoalCardStrat_SHAPE(CommonGoalCardType.CROSS));
        assertEquals(
                commonGoalCard.getType(),
                CommonGoalCardType.CROSS
        );

        commonGoalCard = new CommonGoalCard(4, new CommonGoalCardStrat_SHAPE(CommonGoalCardType.DIAGONAL_FIVE));
        assertEquals(
                commonGoalCard.getType(),
                CommonGoalCardType.DIAGONAL_FIVE
        );

        commonGoalCard = new CommonGoalCard(4, new CommonGoalCardStrat_SHAPE(CommonGoalCardType.TWO_SQUARES));
        assertEquals(
                commonGoalCard.getType(),
                CommonGoalCardType.TWO_SQUARES
        );

        assertThrows(IllegalArgumentException.class, () -> new CommonGoalCard(4, new CommonGoalCardStrat_SHAPE(CommonGoalCardType.TWO_RAINBOW_COLUMNS)));
    }
}