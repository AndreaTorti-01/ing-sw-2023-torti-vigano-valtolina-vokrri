package it.polimi.ingsw.network;

import it.polimi.ingsw.utils.Observable;
import it.polimi.ingsw.utils.Observer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Observable implements Observer, Runnable {
    private static final int port = 42069;
    private ServerSocket serverSocket;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server port: " + serverSocket.getLocalPort());

            Socket socket = serverSocket.accept();

            ObjectInputStream objectInputStream;
            ObjectOutputStream objectOutputStream;

            objectInputStream = new ObjectInputStream(socket.getInputStream());

            Object os;
            try {
                os = objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            // cast the object received
            if (os instanceof String) {
                String message = (String) os;
            } else if (os instanceof Integer) {
                Integer message = (Integer) os;
            } else if (os instanceof Boolean) {
                Boolean message = (Boolean) os;
            } else {
                throw new RuntimeException("Message type not supported");
            }

            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject("Hello client!");

            objectOutputStream.close();
            objectInputStream.close();
        } catch (IOException ignored) {

        }
    }
}
