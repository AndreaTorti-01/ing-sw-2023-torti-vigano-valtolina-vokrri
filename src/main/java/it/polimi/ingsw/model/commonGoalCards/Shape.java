package it.polimi.ingsw.model.commonGoalCards;

import java.io.Serializable;

// TODO

/**
 * A class representing the shape that can be found in a Common Goal Card.
 *
 * @param width          the width of the shape.
 * @param height         the height of the shape.
 * @param matrix         the matrix representing the shape.
 * @param mirror         true if the shape is mirrored, false otherwise.
 * @param numberOfShapes the number of shapes in the Common Goal Card.
 */
public record Shape(
        int width,
        int height,
        int[][] matrix,
        boolean mirror,
        int numberOfShapes
) implements Serializable {
}