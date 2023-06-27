package it.polimi.ingsw.model.commonGoalCards;

import it.polimi.ingsw.model.ItemCards.ItemType;
import it.polimi.ingsw.model.Shelf;

import java.io.*;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;


public class CommonGoalCardStrat_SHAPE implements CommonGoalCardStrat {
    @Serial
    private static final long serialVersionUID = 2165136835284717126L;
    /**
     * The shape to find in the Common Goal Card Strategy.
     */
    private final Shape shape;
    /**
     * The type of the Common Goal Card Strategy.
     */
    private final CommonGoalCardType type;

    /**
     * Creates a new SHAPE Common Goal Card Strategy of the provided type.
     *
     * @param cardType the type of the Common Goal Card Strategy.
     *                 Must be {@code TWO_SQUARES}, {@code DIAGONAL_FIVE}, {@code EQUAL_CORNERS} and {@code CROSS}.
     */
    public CommonGoalCardStrat_SHAPE(CommonGoalCardType cardType) {
        if (cardType != CommonGoalCardType.TWO_SQUARES && cardType != CommonGoalCardType.DIAGONAL_FIVE && cardType != CommonGoalCardType.EQUAL_CORNERS && cardType != CommonGoalCardType.CROSS)
            throw new IllegalArgumentException("The type of Common Goal Card Strategy must be " + CommonGoalCardType.TWO_SQUARES.name() + ", " + CommonGoalCardType.DIAGONAL_FIVE.name() + ", " + CommonGoalCardType.EQUAL_CORNERS.name() + " or " + CommonGoalCardType.CROSS.name());

        // load the shape from file based on the type
        try {
            this.shape = loadShape(cardType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.type = cardType;
    }

    /**
     * Checks if the pattern of the SHAPE Common Goal Card type is satisfied in the provided shelf.
     *
     * @param shelf the shelf to check the pattern in.
     * @return true if the pattern is satisfied, false otherwise.
     */
    @Override
    public boolean checkPattern(Shelf shelf) {
        int shapesFound = 0;

        for (ItemType type : ItemType.values()) {
            // create the shelf mask
            boolean[][] maskedShelf = new boolean[numberOfRows][numberOfColumns];
            for (int row = 0; row < numberOfRows; row++) {
                for (int column = 0; column < numberOfColumns; column++) {
                    if (shelf.getCardAt(row, column) != null && shelf.getCardAt(row, column).getType().equals(type))
                        maskedShelf[row][column] = true;
                }
            }

            shapesFound += checkPatternsNumber(maskedShelf, shape); // breaks masked shelf!

            if (shapesFound >= shape.numberOfShapes()) return true;
        }

        if (shape.mirror()) {
            // generate the inverted matrix
            int[][] invertedMatrix = new int[shape.height()][shape.width()];
            for (int row = 0; row < shape.height(); row++) {
                for (int column = 0; column < shape.width(); column++) {
                    invertedMatrix[row][column] = shape.matrix()[row][shape.width() - column - 1];
                }
            }

            // create the inverted shape
            Shape invertedShape = new Shape(shape.width(), shape.height(), invertedMatrix, true, shape.numberOfShapes());

            // do the same for the inverted shape
            for (ItemType type : ItemType.values()) {
                // create the shelf mask
                boolean[][] maskedShelf = new boolean[numberOfRows][numberOfColumns];
                for (int j = 0; j < numberOfRows; j++) {
                    for (int k = 0; k < numberOfColumns; k++) {
                        if (shelf.getCardAt(j, k) != null && shelf.getCardAt(j, k).getType().equals(type))
                            maskedShelf[j][k] = true;
                    }
                }

                shapesFound += checkPatternsNumber(maskedShelf, invertedShape); // breaks masked shelf!

                if (shapesFound >= shape.numberOfShapes()) return true;
            }
        }

        return false;
    }

    // TODO
    private int checkPatternsNumber(boolean[][] maskedShelf, Shape shape) {
        int shapesFound = 0;
        int row, column, shapeRow, shapeColumn, startingRow, startingColumn;

        // loop through the shape possible positions
        for (startingRow = 0; startingRow < maskedShelf.length - shape.height() + 1; startingRow++) {
            for (startingColumn = 0; startingColumn < maskedShelf[0].length - shape.width() + 1; startingColumn++) {

                // as soon as a shelf mask is invalid, the loop breaks
                boolean invalid = false;

                outerLoop:
                // loop through shape
                for (shapeRow = 0; shapeRow < shape.height(); shapeRow++) {
                    for (shapeColumn = 0; shapeColumn < shape.width(); shapeColumn++) {

                        // get the position of the shape in the shelf
                        row = startingRow + shapeRow;
                        column = startingColumn + shapeColumn;

                        // check if the shelf mask is invalidated by the shape
                        if (shape.matrix()[shapeRow][shapeColumn] == 1 && !maskedShelf[row][column]) {
                            // if the shelf mask is invalidated, the loop breaks
                            invalid = true;
                            break outerLoop;
                        }
                    }
                }

                sidesCheck:
                // tries to invalidate the shelf mask in the case of two_squares
                if (shape.numberOfShapes() == 2 && !invalid) {
                    row = startingRow;
                    column = startingColumn;
                    // check top side
                    if (row > 0) {
                        if (maskedShelf[row - 1][column] || maskedShelf[row - 1][column + 1]) {
                            invalid = true;
                            break sidesCheck;
                        }
                    }
                    // check right side
                    if (column < maskedShelf[0].length - 2) {
                        if (maskedShelf[row][column + 2] || maskedShelf[row + 1][column + 2]) {
                            invalid = true;
                            break sidesCheck;
                        }
                    }
                    // check bottom side
                    if (row < maskedShelf.length - 2) {
                        if (maskedShelf[row + 2][column] || maskedShelf[row + 2][column + 1]) {
                            invalid = true;
                            break sidesCheck;
                        }
                    }
                    // check left side
                    if (column > 0) {
                        if (maskedShelf[row][column - 1] || maskedShelf[row + 1][column - 1]) {
                            invalid = true;
                            break sidesCheck;
                        }
                    }
                }

                if (!invalid) {
                    shapesFound++;
                    // delete the shape from the shelf mask at the current position
                    for (shapeRow = 0; shapeRow < shape.height(); shapeRow++) {
                        for (shapeColumn = 0; shapeColumn < shape.width(); shapeColumn++) {
                            row = startingRow + shapeRow;
                            column = startingColumn + shapeColumn;
                            if (shape.matrix()[shapeRow][shapeColumn] == 1) {
                                maskedShelf[row][column] = false;
                            }
                        }
                    }
                }
            }
        }
        return shapesFound;
    }

    /**
     * Gets the type of this Common Goal Type.
     *
     * @return the type of this Common Goal Card.
     */
    @Override
    public CommonGoalCardType getType() {
        return type;
    }

    /**
     * Loads the shape of the Common Goal Card Strategy from a well formatted file via a custom parser.
     *
     * @param type the type of the Common Goal Card.
     * @return the shape parsed from the file.
     * @throws IOException in case the file is not found.
     */
    private Shape loadShape(CommonGoalCardType type) throws IOException {
        // open the file with the type name
        String fileName = type.toString();
        BufferedReader sc;

        InputStream inputStream = CommonGoalCardStrat_SHAPE.class.getResourceAsStream("/shapes/" + fileName + ".txt");
        assert inputStream != null;
        sc = new BufferedReader(new InputStreamReader(inputStream));

        // load width and height
        int width = sc.read() - 48;
        var ignored = sc.read(); // skip the space
        int height = sc.read() - 48;
        sc.readLine(); // skip the newline

        // create the matrix
        int[][] matrix = new int[height][width];

        // load the shape
        for (int row = 0; row < height; row++) {
            String line = sc.readLine();
            for (int column = 0; column < width; column++) {
                matrix[row][column] = line.charAt(column) - 48;
            }
        }

        // load the mirror flag
        boolean mirror = sc.read() == '1';
        ignored = sc.read(); // skip the space
        int numberOfShapes = sc.read() - 48;

        // create the shape
        Shape shape = new Shape(width, height, matrix, mirror, numberOfShapes);

        // close the file and return the shape
        sc.close();
        return shape;
    }
}
