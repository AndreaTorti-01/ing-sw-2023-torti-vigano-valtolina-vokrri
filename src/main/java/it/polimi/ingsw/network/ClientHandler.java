package it.polimi.ingsw.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientHandler extends Remote {
    void sendMsg(Object msg) throws RemoteException;
}
