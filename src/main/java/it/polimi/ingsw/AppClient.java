package it.polimi.ingsw;

import it.polimi.ingsw.network.Client;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class AppClient {
    public static void main(String[] args) {

        Client client = new Client();

        Scanner keybd = new Scanner(System.in);
        System.out.println("Enter the IP address of the server:");
        String ip = keybd.nextLine();
        System.out.println("Enter the port of the server:");
        int port = keybd.nextInt();

        client.connectTo(new InetSocketAddress(ip, port));

        client.run();

    }
}
