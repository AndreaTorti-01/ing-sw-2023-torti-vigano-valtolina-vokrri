package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.serializable.GameViewMsg;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

import static it.polimi.ingsw.utils.Constants.serverIpAddress;
import static it.polimi.ingsw.utils.Constants.serverPort;

public class RemoteStub implements Server, ClientHandler, Runnable {
    private Client client;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    @Override
    public void receiveMessage(Object message) throws RemoteException {
        try {
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ClientHandler registerClient(Client client) throws RemoteException {
        this.client = client;

        try {
            Socket socket = new Socket(serverIpAddress, serverPort);
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(this).start();

        return this;
    }

    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                // receive message from clientSkeleton
                GameViewMsg message = (GameViewMsg) ois.readObject();
                this.client.receiveMessage(message);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
