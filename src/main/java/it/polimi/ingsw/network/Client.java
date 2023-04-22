package it.polimi.ingsw.network;

import it.polimi.ingsw.utils.Observable;
import it.polimi.ingsw.utils.Observer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

public class Client extends Observable implements Observer, Runnable {

    private Socket socket;

    @Override
    public void run() {
        try {
            socket = new Socket();
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            while (true) {
                // ask the user if he wants to send string or int
                Scanner keybd = new Scanner(System.in);
                System.out.println("Do you want to send a String or an Int? (s/i)");
                String choice = keybd.nextLine().toLowerCase();
                if (choice.equals("s")) {
                    System.out.println("Enter a string:");
                    String s = keybd.nextLine();
                    out.write(s.getBytes());
                } else if (choice.equals("i")) {
                    System.out.println("Enter an int:");
                    int i = keybd.nextInt();
                    out.write(i);
                } else {
                    System.out.println("Invalid choice");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void connectTo(SocketAddress addr) {
        try {
            socket.connect(addr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}