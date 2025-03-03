package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private HashMap<Integer, GameData> gameDatabase;

    public MemoryGameDAO() {
        gameDatabase = new HashMap<>();
    }

    @Override
    public void clear() {
        gameDatabase.clear();
    }

    @Override
    public int createGame(GameData game) {
        gameDatabase.put(game.gameID(), game);
        return game.gameID();
    }

    @Override
    public GameData getGame(int gameID) {
        return gameDatabase.get(gameID);
    }

    @Override
    public ArrayList<GameData> listGames() {
        return (ArrayList<GameData>) gameDatabase.values();
    }

    @Override
    public void updateGame(GameData game) {
        gameDatabase.replace(game.gameID(), game);
    }

    @Override
    public void deleteGame(int gameID) {
        gameDatabase.remove(gameID);
    }
}
