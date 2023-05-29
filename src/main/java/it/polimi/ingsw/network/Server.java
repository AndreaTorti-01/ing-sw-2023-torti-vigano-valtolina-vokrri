package it.polimi.ingsw.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {
    ClientHandler registerClient(Client client) throws RemoteException;

}
