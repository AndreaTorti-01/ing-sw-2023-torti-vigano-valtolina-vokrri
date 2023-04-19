package it.polimi.ingsw.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static it.polimi.ingsw.utils.Constants.*;


public class CommonGoalCardStrat_SHAPE implements CommonGoalCardStrat {
    private final Shape shape;
    private final CommonGoalCardType type;

    public CommonGoalCardStrat_SHAPE(CommonGoalCardType type) {
        // load the shape from file based on the type
        try {
            this.shape = loadShape(type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.type = type;
    }

    @Override
    public boolean checkPattern(Shelf shelf) {

        int i_shelf, j_shelf, i_shape, j_shape, i_shape_start, j_shape_start;
        int found_shapes = 0;

        // loop through the 1 shelf masks through ItemType.values()
        for (int cardType = 0; cardType < numberOfItemCardTypes; cardType++) {

            // create the shelf mask
            boolean[][] masked_shelf = new boolean[numberOfRows][numberOfColumns];
            for (int j = 0; j < numberOfRows; j++) {
                for (int k = 0; k < numberOfColumns; k++) {
                    if (shelf.getCardAt(j, k) != null && shelf.getCardAt(j, k).type().equals(ItemType.values()[cardType])) {
                        masked_shelf[j][k] = true;
                    }
                }
            }

            found_shapes += checkPatternsNumber(masked_shelf, shape); // breaks masked shelf!

            if (found_shapes >= shape.numberOfShapes()) {
                return true;
            }
        }

        if (shape.mirror()) {
            // generate the inverted matrix
            int[][] inv_matrix = new int[shape.height()][shape.width()];
            for (int j = 0; j < shape.height(); j++) {
                for (int k = 0; k < shape.width(); k++) {
                    inv_matrix[j][k] = shape.matrix()[j][shape.width() - k - 1];
                }
            }

            // create the inverted shape
            Shape inv_shape = new Shape(shape.width(), shape.height(), inv_matrix, true, shape.numberOfShapes());

            // do the same for the inverted shape
            for (int cardType = 0; cardType < numberOfItemCardTypes; cardType++) {

                // create the shelf mask
                boolean[][] masked_shelf = new boolean[numberOfRows][numberOfColumns];
                for (int j = 0; j < numberOfRows; j++) {
                    for (int k = 0; k < numberOfColumns; k++) {
                        if (shelf.getCardAt(j, k) != null && shelf.getCardAt(j, k).type().equals(ItemType.values()[cardType])) {
                            masked_shelf[j][k] = true;
                        }
                    }
                }

                found_shapes += checkPatternsNumber(masked_shelf, inv_shape); // breaks masked shelf!

                if (found_shapes >= shape.numberOfShapes()) {
                    return true;
                }
            }

        }

        return false;
    }

    private int checkPatternsNumber(boolean[][] masked_shelf, Shape shape) {
        int found_shapes = 0;
        int i_shelf = 0, j_shelf = 0, i_shape, j_shape, i_shape_start, j_shape_start;

        // loop through the shape possible positions
        for (i_shape_start = 0; i_shape_start < masked_shelf.length - shape.height() + 1; i_shape_start++) {
            for (j_shape_start = 0; j_shape_start < masked_shelf[0].length - shape.width() + 1; j_shape_start++) {

                // as soon as a shelf mask is invalid, the loop breaks
                boolean invalid = false;

                outerloop:
                // loop through shape
                for (i_shape = 0; i_shape < shape.height(); i_shape++) {
                    for (j_shape = 0; j_shape < shape.width(); j_shape++) {

                        // get the position of the shape in the shelf
                        i_shelf = i_shape_start + i_shape;
                        j_shelf = j_shape_start + j_shape;

                        // check if the shelf mask is invalidated by the shape
                        if (shape.matrix()[i_shape][j_shape] == 1 && !masked_shelf[i_shelf][j_shelf]) {
                            // if the shelf mask is invalidated, the loop breaks
                            invalid = true;
                            break outerloop;
                        }
                    }
                }

                sidesCheck:
                // tries to invalidate the shelf mask in the case of two_squares
                if (shape.numberOfShapes() == 2 && !invalid) {
                    i_shelf = i_shape_start;
                    j_shelf = j_shape_start;
                    // check top side
                    if (i_shelf > 0) {
                        if (masked_shelf[i_shelf - 1][j_shelf] || masked_shelf[i_shelf - 1][j_shelf + 1]) {
                            invalid = true;
                            break sidesCheck;
                        }
                    }
                    // check right side
                    if (j_shelf < masked_shelf[0].length - 2) {
                        if (masked_shelf[i_shelf][j_shelf + 2] || masked_shelf[i_shelf + 1][j_shelf + 2]) {
                            invalid = true;
                            break sidesCheck;
                        }
                    }
                    // check bottom side
                    if (i_shelf < masked_shelf.length - 2) {
                        if (masked_shelf[i_shelf + 2][j_shelf] || masked_shelf[i_shelf + 2][j_shelf + 1]) {
                            invalid = true;
                            break sidesCheck;
                        }
                    }
                    // check left side
                    if (j_shelf > 0) {
                        if (masked_shelf[i_shelf][j_shelf - 1] || masked_shelf[i_shelf + 1][j_shelf - 1]) {
                            invalid = true;
                            break sidesCheck;
                        }
                    }
                }

                if (!invalid) {
                    found_shapes++;
                    // delete the shape from the shelf mask at the current position
                    for (i_shape = 0; i_shape < shape.height(); i_shape++) {
                        for (j_shape = 0; j_shape < shape.width(); j_shape++) {
                            i_shelf = i_shape_start + i_shape;
                            j_shelf = j_shape_start + j_shape;
                            if (shape.matrix()[i_shape][j_shape] == 1) {
                                masked_shelf[i_shelf][j_shelf] = false;
                            }
                        }
                    }
                }
            }
        }
        return found_shapes;
    }

    @Override
    public CommonGoalCardType getType() {
        return type;
    }

    private Shape loadShape(CommonGoalCardType type) throws IOException {

        // open the file with the type name
        String fileName = type.toString();
        BufferedReader sc = null;

        InputStream inputStream = CommonGoalCardStrat_SHAPE.class.getResourceAsStream("/shapes/" + fileName + ".txt");
        sc = new BufferedReader(new InputStreamReader(inputStream));

        // load width and height
        int width = sc.read() - 48;
        sc.read(); // skip the space
        int height = sc.read() - 48;
        sc.readLine(); // skip the newline

        // create the matrix
        int[][] matrix = new int[height][width];

        // load the shape
        for (int i = 0; i < height; i++) {
            String line = sc.readLine();
            for (int j = 0; j < width; j++) {
                matrix[i][j] = line.charAt(j) - 48;
            }
        }

        // load the mirror flag
        boolean mirror_hor = sc.read() == '1';
        sc.read(); // skip the space
        int num_of_shapes = sc.read() - 48;

        // create the shape
        Shape shape = new Shape(width, height, matrix, mirror_hor, num_of_shapes);

        // close the file and return the shape
        sc.close();
        return shape;
    }
}
