package it.polimi.ingsw.model;

import it.polimi.ingsw.model.commonGoalCards.CommonGoalCard;
import it.polimi.ingsw.model.commonGoalCards.CommonGoalCardStrat_EIGHT_EQUAL;
import it.polimi.ingsw.model.commonGoalCards.CommonGoalCardType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalCardStrat_EIGHT_EQUALTest {
    String folderName = "/shelvesForTesting/testForEIGHT_EQUAL/";
    String fileExtension = ".txt";
    int numberOfTests = 10;

    @Test
    void testCheckPattern() {
        CommonGoalCardStrat_EIGHT_EQUAL commonGoalCardStrat = new CommonGoalCardStrat_EIGHT_EQUAL();

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
        CommonGoalCard commonGoalCard = new CommonGoalCard(4, new CommonGoalCardStrat_EIGHT_EQUAL());
        assertEquals(
                commonGoalCard.getType(),
                CommonGoalCardType.EIGHT_EQUAL
        );
    }

}