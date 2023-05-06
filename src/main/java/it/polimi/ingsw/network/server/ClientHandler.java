package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.serializable.ChatMsg;
import it.polimi.ingsw.network.serializable.MoveMsg;
import it.polimi.ingsw.utils.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Observable implements Runnable {
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;

    public ClientHandler(Socket socket) {
        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Manda il modelView al client a cui è accoppiato
     *
     * @param msg GameViewMsg
     */
    public void sendMsg(Object msg) {
        try {
            outputStream.writeObject(msg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Inoltra i messaggi ricevuti alla Lobby
     */
    @Override
    public void run() {
        do {
            try {
                System.err.println("waiting for messages...");
                Object msg = inputStream.readObject();
                System.err.println("message arrived");
                switch (msg.getClass().getSimpleName()) {
                    case "MoveMsg" -> notifyObservers((MoveMsg) msg);
                    case "ChatMsg" -> notifyObservers((ChatMsg) msg);
                    case "String" -> notifyObservers((String) msg);
                    case "Integer" -> notifyObservers((Integer) msg);
                    default -> System.out.println("unsupported");
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } while (true);
    }
}
