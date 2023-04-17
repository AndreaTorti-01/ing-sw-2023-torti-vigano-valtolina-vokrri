package it.polimi.ingsw.messages;

import it.polimi.ingsw.model.Player;

public class PlayerExitMessage extends Message {
    private Player player;

    private PlayerExitMessage(Player player) {
        this.player = player;
    }

    private Player getPlayer() {
        return this.player;
    }
}
