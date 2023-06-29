package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.client.ClientImpl;
import it.polimi.ingsw.network.serializable.GameViewMsg;
import it.polimi.ingsw.view.RunnableView;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class TuiTest {

    @Test
    void updateView() throws RemoteException {
        RunnableView tui = new Tui(new ClientImpl(true));
        Game g = new Game();
        tui.updateView(new GameViewMsg(g));
        g.addPlayer("andrea");
        tui.updateView(new GameViewMsg(g));
        g.initModel(2);
        tui.updateView(new GameViewMsg(g));
        g.addPlayer("fabio");
        tui.updateView(new GameViewMsg(g));
        g.advancePlayerTurn();
        g.getCurrentPlayer().getShelf().fill();
        g.advancePlayerTurn();
        tui.updateView(new GameViewMsg(g));

    }
}