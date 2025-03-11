package dataaccess;

import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;

public interface GameDAO {
    void clear() throws DataAccessException;
    int createGame(GameData game) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    ArrayList<GameData> listGames();
    void updateGame(GameData game);
}
