package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.FileUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommonGoalCardStrat_STAIRTest {
    String folderName = "/shelvesForTesting/testForSTAIR/";
    String fileExtension = ".txt";
    int numberOfTests = 10;

    @Test
    void testCheckPatternMethod() {
        CommonGoalCardStrat_STAIR commonGoalCardStrat = new CommonGoalCardStrat_STAIR();

        for (int i = 0; i < numberOfTests; i++) {
            String fileName;
            if (i + 1 < 10) fileName = String.format(folderName + "/TEST_FALSE_0%d" + fileExtension, i + 1);
            else fileName = String.format(folderName + "/TEST_FALSE_%d" + fileExtension, i + 1);

            Shelf shelf = FileUtils.getShelfFromFile(fileName);

            assertFalse(commonGoalCardStrat.checkPattern(shelf));
        }

        for (int i = 0; i < numberOfTests; i++) {
            String fileName;
            if (i + 1 < 10) fileName = String.format(folderName + "TEST_TRUE_0%d" + fileExtension, i + 1);
            else fileName = String.format(folderName + "TEST_TRUE_%d" + fileExtension, i + 1);

            Shelf shelf = FileUtils.getShelfFromFile(fileName);

            assertTrue(commonGoalCardStrat.checkPattern(shelf));
        }
    }

}