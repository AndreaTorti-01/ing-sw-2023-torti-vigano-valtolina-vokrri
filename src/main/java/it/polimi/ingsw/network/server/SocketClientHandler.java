package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.utils.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClientHandler extends Observable implements ClientHandler {
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;

    public SocketClientHandler(Socket socket) {
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
            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Inoltra i messaggi ricevuti alla Lobby
     */
    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        do {
            try {
                System.err.println("waiting for messages...");
                Object msg = inputStream.readObject();
                System.err.println("message arrived");
                notifyObservers(msg);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } while (true);
    }
}
