package it.polimi.ingsw.model;

public abstract class GameObject {
    private float height; // length in inches
    private float width;

    public int getHeight(float scale) { // scale is given in dpi
        return (int) (height * scale);
    }

    public int getWidth(float scale) {
        return (int) (width * scale);
    }
}
