package it.polimi.ingsw.network.serializable;

public enum TuiCommands {
    CHAT("/chat"),
    PRIVATECHAT("/privatechat"),
    QUIT("/quit"),
    HELP("/help");


    private final String commandName;

    TuiCommands(String identifier){
        this.commandName = identifier;
    }

    public String getCommandName(){
        return this.commandName;
    }

    public void printList(){
        System.out.println("List of commands:");
        for(TuiCommands command : TuiCommands.values()){
            printCommandInfo(command);
        }
    }
    public void printCommandInfo(TuiCommands command){
        switch (command){
            case CHAT -> System.out.println("Type /chat <message> to send a message to the other players");
            case PRIVATECHAT -> System.out.println("Type /privatechat <player> <message> to send a private message to a player");
            case QUIT -> System.out.println("Type /quit to quit the game");
            case HELP -> System.out.println("Type /help to see the list of commands");

        }
    }
}
