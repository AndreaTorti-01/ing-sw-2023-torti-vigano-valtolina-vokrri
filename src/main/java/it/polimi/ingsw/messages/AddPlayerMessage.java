package it.polimi.ingsw.messages;

public class AddPlayerMessage extends Message {
    private String name;

    public AddPlayerMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
