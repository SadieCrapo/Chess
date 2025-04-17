package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {
    public ChessGame game;

    public LoadGameMessage(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoadGameMessage)){
            return false;
        }

        LoadGameMessage that = (LoadGameMessage) o;
        return this.getServerMessageType() == that.getServerMessageType();
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (!(o instanceof LoadGameMessage)) {
//            return false;
//        }
//        ServerMessage that = (ServerMessage) o;
//        return getServerMessageType() == that.getServerMessageType();
//    }
}