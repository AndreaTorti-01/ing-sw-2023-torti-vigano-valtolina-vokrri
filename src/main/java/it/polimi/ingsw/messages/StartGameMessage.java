package it.polimi.ingsw.messages;

public class StartGameMessage extends Message {
    private String[] names;

    private StartGameMessage(String[] names) {
        this.names = names;
    }

    private String[] getNames() {
        return this.names;
    }
}
