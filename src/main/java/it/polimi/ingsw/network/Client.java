package it.polimi.ingsw.network;

import it.polimi.ingsw.network.serializable.ChatMsg;
import it.polimi.ingsw.network.serializable.MoveMsg;
import it.polimi.ingsw.utils.Observer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote, Observer {

    void update(Integer numberOfPlayers) throws RemoteException;

    void update(String playerName) throws RemoteException;

    void update(MoveMsg move) throws RemoteException;

    void update(ChatMsg message) throws RemoteException;

    void run() throws RemoteException;
}
