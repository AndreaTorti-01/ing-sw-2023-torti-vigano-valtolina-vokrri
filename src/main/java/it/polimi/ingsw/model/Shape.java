package it.polimi.ingsw.model;

public record Shape(
        int width,
        int height,
        int[][] matrix,
        boolean mirror,
        int numberOfShapes
) {
}