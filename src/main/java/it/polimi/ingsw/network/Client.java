package it.polimi.ingsw.network;

import it.polimi.ingsw.network.serializable.GameViewMsg;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {

    // void update(Integer numberOfPlayers) throws RemoteException;

    // void update(String playerName) throws RemoteException;

    // void update(MoveMsg move) throws RemoteException;

    // void update(ChatMsg message) throws RemoteException;

    void receiveMessage(GameViewMsg message) throws RemoteException;
}
