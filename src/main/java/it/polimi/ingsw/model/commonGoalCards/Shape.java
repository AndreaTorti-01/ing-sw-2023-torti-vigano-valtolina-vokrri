package it.polimi.ingsw.model.commonGoalCards;

public record Shape(
        int width,
        int height,
        int[][] matrix,
        boolean mirror,
        int numberOfShapes
) {
}