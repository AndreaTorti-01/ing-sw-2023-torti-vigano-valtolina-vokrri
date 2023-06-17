package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.network.serializable.GameViewMsg;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

/**
 * A class that implements the Client Interface.
 * This class represents the Client on the server side,
 * establishing a socket communication channel between the Client Handler and the client objects.
 */
public class ClientSkeleton implements Client, Runnable {

    private ClientHandler clientHandler;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;


    /**
     * Creates a Client Skeleton from the provided socket.
     *
     * @param socket the communication socket.
     */
    public ClientSkeleton(Socket socket) {
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Receives the provided message from the client.
     *
     * @param message the message to be received.
     * @throws RemoteException if something goes wrong with the RMI connection.
     */
    @Override
    public void receiveMessage(GameViewMsg message) throws RemoteException {
        try {
            oos.writeObject(message);
            oos.flush();
            oos.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the Client Handler to the provided one.
     *
     * @param clientHandler the Client Handler to be set.
     */
    public void setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    /**
     * Waits for new messages to be received from the client
     * and sends it to the client handler.
     */
    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                Object message = ois.readObject();
                this.clientHandler.receiveMessage(message);
            } catch (IOException | ClassNotFoundException e) {
                try {
                    ois.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}
