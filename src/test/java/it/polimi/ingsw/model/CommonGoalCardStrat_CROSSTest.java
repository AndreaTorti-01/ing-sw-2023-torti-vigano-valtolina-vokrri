package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommonGoalCardStrat_CROSSTest {
    String folderName = "/shelvesForTesting/testForCROSS/";
    String fileExtension = ".txt";

    int numberOfTests = 10;

    @Test
    void testCheckPatternMethod() {
        CommonGoalCardStrat_CROSS commonGoalCardStrat = new CommonGoalCardStrat_CROSS();

        for (int i = 0; i < numberOfTests; i++) {
            String fileName;
            if (i + 1 < 10) fileName = String.format(folderName + "TEST_FALSE_0%d" + fileExtension, i + 1);
            else fileName = String.format(folderName + "TEST_FALSE_%d" + fileExtension, i + 1);

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