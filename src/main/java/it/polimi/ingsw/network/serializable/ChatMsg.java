package it.polimi.ingsw.network.serializable;

import java.io.Serial;
import java.io.Serializable;

public final class ChatMsg implements Serializable {

    @Serial
    private static final long serialVersionUID = 3783938319635665538L;
    private int destPlayer;
    private int sourcePlayer;
    private boolean isMsgPublic;

    private String message;

    private void ChatMsg(int destPlayer,int sourcePlayer, boolean isMsgPublic, String message){
        this.destPlayer= destPlayer;
        this.sourcePlayer=sourcePlayer;
        this.isMsgPublic=isMsgPublic;
        this.message=message;
    }

}
