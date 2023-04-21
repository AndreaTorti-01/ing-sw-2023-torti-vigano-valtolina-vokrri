package it.polimi.ingsw.model;

import it.polimi.ingsw.model.commonGoalCardStrats.CommonGoalCardStrat_RAINBOWS;
import it.polimi.ingsw.model.commonGoalCardStrats.CommonGoalCardType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommonGoalCardStrat_RAINBOWSTest {
    String fileExtension = ".txt";
    int numberOfTests = 10;

    @Test
    void testCheckPattern_TWO_RAINBOW_COLUMNS() {
        CommonGoalCardStrat_RAINBOWS commonGoalCardStrat = new CommonGoalCardStrat_RAINBOWS(CommonGoalCardType.TWO_RAINBOW_COLUMNS);
        String folderName = "/shelvesForTesting/testForTWO_RAINBOW_COLUMNS/";

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
    void testCheckPattern_TWO_RAINBOW_LINES() {
        CommonGoalCardStrat_RAINBOWS commonGoalCardStrat = new CommonGoalCardStrat_RAINBOWS(CommonGoalCardType.TWO_RAINBOW_LINES);
        String folderName = "/shelvesForTesting/testForTWO_RAINBOW_LINES/";

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