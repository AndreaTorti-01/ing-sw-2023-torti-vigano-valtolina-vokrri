package it.polimi.ingsw.messages;

import it.polimi.ingsw.model.ItemType;
import it.polimi.ingsw.model.Player;

public class StdMoveMessage extends Message {
    private Player player;
    private boolean[][] selected;
    private int columnSelected;
    private ItemType[] order;

    public StdMoveMessage(Player player, boolean[][] selected, int columnSelected, ItemType[] order) {
        this.player = player;
        this.selected = selected;
        this.columnSelected = columnSelected;
        this.order = order;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getColumnSelected() {
        return this.columnSelected;
    }

    public ItemType[] getOrder() {
        return order;
    }

    public boolean[][] getSelected() {
        return selected;
    }

}
