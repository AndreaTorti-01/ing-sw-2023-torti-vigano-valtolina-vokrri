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

public class ClientHandlerImpl extends UnicastRemoteObject implements ClientHandler, Observable {

    @Serial
    private static final long serialVersionUID = -6793473008684706283L;
    private final Client client;
    private final Vector<Observer> observers = new Vector<>();


    protected ClientHandlerImpl(Client client) throws RemoteException {
        super();
        this.client = client;
    }

    public void sendMessage(GameViewMsg message) throws RemoteException {
        client.receiveMessage(message);
    }

    @Override
    public void receiveMessage(Object message) throws RemoteException {
        this.notifyObservers(message);
    }

    @Override
    public synchronized void addObserver(Observer o) {
        if (o == null) throw new NullPointerException();
        if (!observers.contains(o)) {
            observers.addElement(o);
        }
    }

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
