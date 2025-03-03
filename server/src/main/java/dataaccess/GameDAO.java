package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void clear();
    int createGame(GameData game);
    GameData getGame(gameID);
    ArrayList<GameData> listGames();
    void updateGame(GameData); // or maybe this should return a boolean??
    void deleteGame(int gameID); // or maybe this should return a boolean??
}
