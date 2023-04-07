package it.polimi.ingsw.model;

public record Shape(
        int width,
        int height,
        boolean[][] matrix,
        boolean mirror_hor,
        int num_of_shapes
) {
}