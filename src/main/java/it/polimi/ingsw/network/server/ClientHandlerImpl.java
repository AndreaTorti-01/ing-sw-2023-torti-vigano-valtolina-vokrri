package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.network.serializable.GameViewMsg;
import it.polimi.ingsw.utils.Observable;
import it.polimi.ingsw.utils.Observer;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

/**
 * A class that implements the Client Handler Interface.
 * This class represents both the Socket and RMI Client Handler.
 * <p>
 * Its only task is to make the Client and the Server/Lobby communicate.
 */
public class ClientHandlerImpl extends UnicastRemoteObject implements ClientHandler, Observable {

    @Serial
    private static final long serialVersionUID = -6793473008684706283L;
    /**
     * The Client associated with this Client Handler.
     */
    private final Client client;

    /**
     * The list of observers of this observable.
     */
    private final Vector<Observer> observers = new Vector<>();

    /**
     * Creates a new Client Handler that communicates with the provided Client.
     *
     * @param client the client with which the client handler communicates with.
     * @throws RemoteException if something goes wrong with the RMI connection.
     */
    protected ClientHandlerImpl(Client client) throws RemoteException {
        super();
        this.client = client;
    }

    /**
     * Sends the provided message to the client.
     *
     * @param message the message to be sent.
     * @throws RemoteException if something goes wrong with the RMI connection.
     */
    public void sendMessage(GameViewMsg message) throws RemoteException {
        client.receiveMessage(message);
    }

    /**
     * Receives the provided message from the client.
     *
     * @param message the message received.
     * @throws RemoteException if something goes wrong with the RMI connection.
     */
    @Override
    public void receiveMessage(Object message) throws RemoteException {
        this.notifyObservers(message);
    }

    /**
     * Implementation of the observable interface {@code addObserver} method.
     *
     * @param o the observer to be added in the list of observers.
     */
    @Override
    public synchronized void addObserver(Observer o) {
        if (o == null) throw new NullPointerException();
        if (!observers.contains(o)) observers.addElement(o);
    }

    /**
     * Implementation of the observable interface {@code notifyObserver} method.
     *
     * @param message the message with which to notify the observers.
     */
    @Override
    public void notifyObservers(Object message) {
        for (Observer o : observers) {
            try {
                Method m = o.getClass().getMethod("update", message.getClass());
                m.invoke(o, message);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                System.err.println(message.getClass().getSimpleName());
                System.err.println(o.getClass().getSimpleName());
                System.err.println(e.getClass());
                System.err.println(e.getMessage());
            }
        }
    }
}
