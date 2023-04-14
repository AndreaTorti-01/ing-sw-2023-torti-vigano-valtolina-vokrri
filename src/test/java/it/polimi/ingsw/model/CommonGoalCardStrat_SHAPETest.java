package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.FileUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommonGoalCardStrat_SHAPETest {
    String fileExtension = ".txt";
    int numberOfTests = 10;

    private void print_shelf(Shelf shelf) {
        // print shelf for debug like a matrix
        for (int j = 0; j < shelf.height(); j++) {
            for (int k = 0; k < shelf.width(); k++) {
                if (shelf.getCardAt(j, k) == null) System.out.print("*");
                else System.out.print(shelf.getCardAt(j, k).type().ordinal());
            }
            System.out.println();
        }
        System.out.println();
    }

    @Test
    void checkPattern_EQUAL_CORNERS() {
        String folderName = "/shelvesForTesting/testForEQUAL_CORNERS/";
        CommonGoalCardStrat_SHAPE commonGoalCardStrat = new CommonGoalCardStrat_SHAPE(CommonGoalCardType.EQUAL_CORNERS);

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

    @Test
    void checkPattern_CROSS() {
        String folderName = "/shelvesForTesting/testForCROSS/";
        CommonGoalCardStrat_SHAPE commonGoalCardStrat = new CommonGoalCardStrat_SHAPE(CommonGoalCardType.CROSS);

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

    @Test
    void checkPattern_DIAGONAL_FIVE() {
        String folderName = "/shelvesForTesting/testForDIAGONAL_FIVE/";
        CommonGoalCardStrat_SHAPE commonGoalCardStrat = new CommonGoalCardStrat_SHAPE(CommonGoalCardType.DIAGONAL_FIVE);

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