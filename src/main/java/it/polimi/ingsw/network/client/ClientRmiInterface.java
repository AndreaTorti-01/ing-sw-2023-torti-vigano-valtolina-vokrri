package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.RunnableView;

public interface ClientRmiInterface extends Client {
    RunnableView getView();
}
