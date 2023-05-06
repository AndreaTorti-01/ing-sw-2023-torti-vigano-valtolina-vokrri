package it.polimi.ingsw.view;

import it.polimi.ingsw.network.serializable.GameViewMsg;

/**
 * Common interface for Tui and Gui
 */
public interface RunnableView extends Runnable {
    /**
     * Updates the current view with the latest changes of the model via a new modelView
     *
     * @param modelView which contains a representation of the model state
     */
    void updateView(GameViewMsg modelView);
}
