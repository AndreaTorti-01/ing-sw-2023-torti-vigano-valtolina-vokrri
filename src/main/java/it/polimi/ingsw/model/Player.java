package it.polimi.ingsw.model;

public class Player {
    private final String nickname;

    public Player(String nickname) throws IllegalArgumentException {
        if (nickname == null || nickname.isEmpty())
            throw new IllegalArgumentException("Nickname cannot be null or empty");
        else this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
