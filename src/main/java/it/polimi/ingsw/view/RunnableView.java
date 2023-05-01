package it.polimi.ingsw.view;

import it.polimi.ingsw.model.GameView;

public interface RunnableView extends Runnable {
    public void updateView(GameView modelView);
}
