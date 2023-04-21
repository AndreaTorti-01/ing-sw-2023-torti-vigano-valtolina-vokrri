package it.polimi.ingsw.model;

import it.polimi.ingsw.model.commonGoalCardStrats.CommonGoalCardStrat_MAX_THREE_TYPES;
import it.polimi.ingsw.model.commonGoalCardStrats.CommonGoalCardType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommonGoalCardStrat_MAX_THREE_TYPESTest {
    String folderName1 = "/shelvesForTesting/testForFOUR_LINES_MAX_THREE_TYPES/";
    String folderName = "/shelvesForTesting/testForTHREE_COLUMNS_MAX_THREE_TYPES/";
    String fileExtension = ".txt";
    int numberOfTests = 10;

    @Test
    void testCheckPatternMethod() {
        CommonGoalCardStrat_MAX_THREE_TYPES commonGoalCardStrat = new CommonGoalCardStrat_MAX_THREE_TYPES(CommonGoalCardType.THREE_COLUMNS_MAX_THREE_TYPES);

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
    void testCheckPatternMethod1() {
        CommonGoalCardStrat_MAX_THREE_TYPES commonGoalCardStrat = new CommonGoalCardStrat_MAX_THREE_TYPES(CommonGoalCardType.FOUR_LINES_MAX_THREE_TYPES);

        for (int i = 0; i < numberOfTests; i++) {
            String fileName;
            if (i + 1 < 10) fileName = String.format(folderName1 + "/TEST_FALSE_0%d" + fileExtension, i + 1);
            else fileName = String.format(folderName1 + "/TEST_FALSE_%d" + fileExtension, i + 1);

            Shelf shelf = new Shelf(fileName);

            assertFalse(commonGoalCardStrat.checkPattern(shelf));
        }

        for (int i = 0; i < numberOfTests; i++) {
            String fileName;
            if (i + 1 < 10) fileName = String.format(folderName1 + "TEST_TRUE_0%d" + fileExtension, i + 1);
            else fileName = String.format(folderName1 + "TEST_TRUE_%d" + fileExtension, i + 1);

            Shelf shelf = new Shelf(fileName);

            assertTrue(commonGoalCardStrat.checkPattern(shelf));
        }
    }


}