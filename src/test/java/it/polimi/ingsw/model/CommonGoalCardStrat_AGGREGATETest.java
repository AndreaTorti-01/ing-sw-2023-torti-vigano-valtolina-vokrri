package it.polimi.ingsw.model;

import it.polimi.ingsw.model.commonGoalCardStrats.CommonGoalCardStrat_AGGREGATE;
import it.polimi.ingsw.model.commonGoalCardStrats.CommonGoalCardType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommonGoalCardStrat_AGGREGATETest {

    String fileExtension = ".txt";
    int numberOfTests = 10;

    @Test
    void testCheckPattern_SIX_PAIRS() {
        CommonGoalCardStrat_AGGREGATE commonGoalCardStrat = new CommonGoalCardStrat_AGGREGATE(CommonGoalCardType.SIX_PAIRS);
        String folderName = "/shelvesForTesting/testForSIX_PAIRS/";

        for (int i = 0; i < numberOfTests; i++) {
            String fileName;
            if (i + 1 < 10) fileName = String.format(folderName + "/TEST_FALSE_0%d" + fileExtension, i + 1);
            else fileName = String.format(folderName + "/TEST_FALSE_%d" + fileExtension, i + 1);

            Shelf shelf = new Shelf(fileName);

            assertFalse(commonGoalCardStrat.checkPattern(shelf));
        }

        for (int i = 0; i < numberOfTests; i++) {
            String fileName;
            if (i + 1 < 10) fileName = String.format(folderName + "TEST_TRUE_0%d" + fileExtension, i + 1);
            else fileName = String.format(folderName + "TEST_TRUE_%d" + fileExtension, i + 1);

            Shelf shelf = new Shelf(fileName);

            assertTrue(commonGoalCardStrat.checkPattern(shelf));
        }
    }


    @Test
    void testCheckPattern_FOUR_QUARTETS() {
        CommonGoalCardStrat_AGGREGATE commonGoalCardStrat = new CommonGoalCardStrat_AGGREGATE(CommonGoalCardType.FOUR_QUARTETS);
        String folderName = "/shelvesForTesting/testForFOUR_QUARTETS/";

        for (int i = 0; i < numberOfTests; i++) {
            String fileName;
            if (i + 1 < 10) fileName = String.format(folderName + "/TEST_FALSE_0%d" + fileExtension, i + 1);
            else fileName = String.format(folderName + "/TEST_FALSE_%d" + fileExtension, i + 1);

            Shelf shelf = new Shelf(fileName);

            assertFalse(commonGoalCardStrat.checkPattern(shelf));
        }

        for (int i = 0; i < numberOfTests; i++) {
            String fileName;
            if (i + 1 < 10) fileName = String.format(folderName + "TEST_TRUE_0%d" + fileExtension, i + 1);
            else fileName = String.format(folderName + "TEST_TRUE_%d" + fileExtension, i + 1);

            Shelf shelf = new Shelf(fileName);

            assertTrue(commonGoalCardStrat.checkPattern(shelf));
        }
    }
}