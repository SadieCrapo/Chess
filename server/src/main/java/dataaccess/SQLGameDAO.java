package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class SQLGameDAO implements GameDAO {
    @Override
    public void clear() {

    }

    @Override
    public int createGame(GameData game) {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() {
        return null;
    }

    @Override
    public void updateGame(GameData game) {

    }
}
