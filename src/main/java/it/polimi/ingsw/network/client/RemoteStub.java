package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.serializable.GameViewMsg;
import it.polimi.ingsw.network.server.ServerImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

import static it.polimi.ingsw.utils.Constants.serverIpAddress;
import static it.polimi.ingsw.utils.Constants.serverPort;

/**
 * A class that implements the Server and Client Handler Interfaces.
 * This class represents the Client Handler and the Server on the client side,
 * establishing a socket communication channel between the Client and the server objects.
 */
public class RemoteStub implements Server, ClientHandler, Runnable {
    private Client client;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    /**
     * Sends the provided message to the client handler.
     *
     * @param message the message to be sent.
     * @throws RemoteException if something goes wrong with the RMI connection.
     */
    @Override
    public void receiveMessage(Object message) throws RemoteException {
        try {
            oos.writeObject(message);
            oos.flush();
            oos.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers the provided client to the server.
     * See {@link ServerImpl} for the registration procedure.
     *
     * @param client The client to be registered.
     * @return the client handler associated with the corresponding client.
     * @throws RemoteException if something goes wrong with the RMI connection.
     */
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

    /**
     * Waits for new Game View Messages to be received from the client handler
     * and sends it to the client.
     */
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
