package it.polimi.ingsw;

public class ServerApp {
    public static void main(String[] args) {
        int serverPort = 42069;

        for (int i = 0; i < args.length; i++) {
            if (args.length >= 2 && args[i].equals("--port")) {
                try {
                    serverPort = Integer.parseInt(args[i + 1]);
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid port. Using default one!");
                }
            }
        }
    }
}
