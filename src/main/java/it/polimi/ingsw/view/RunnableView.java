package it.polimi.ingsw.view;

import it.polimi.ingsw.network.serializable.GameView;

public interface RunnableView extends Runnable {
    public void updateView(GameView modelView);
}
