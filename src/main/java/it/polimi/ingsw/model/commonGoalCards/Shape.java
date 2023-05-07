package it.polimi.ingsw.model.commonGoalCards;

import java.io.Serializable;

public record Shape(
        int width,
        int height,
        int[][] matrix,
        boolean mirror,
        int numberOfShapes
) implements Serializable {
}