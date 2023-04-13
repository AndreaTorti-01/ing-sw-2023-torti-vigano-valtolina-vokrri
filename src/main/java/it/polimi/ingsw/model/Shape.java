package it.polimi.ingsw.model;

public record Shape(
        int width,
        int height,
        int[][] matrix,
        boolean mirror_hor,
        int num_of_shapes
) {
}