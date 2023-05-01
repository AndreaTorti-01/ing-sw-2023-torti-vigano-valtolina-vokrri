package it.polimi.ingsw.network.serializable;

public class RegisterMSG {
    private final String username;

    public RegisterMSG(String username) {
        this.username = username;
    }
    public String getUsername() { return username; }

}
