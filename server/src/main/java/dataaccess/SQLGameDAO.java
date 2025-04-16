package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO {
    public SQLGameDAO() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE games";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public int createGame(GameData game) throws DataAccessException {
        var statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, gameJson) VALUES (?, ?, ?, ?)";
        var json = new Gson().toJson(game.game());
        return DatabaseManager.executeUpdate(statement, game.whiteUsername(), game.blackUsername(), game.gameName(), json);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, gameJson FROM games WHERE gameID=?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1, gameID);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return readGame(resultSet);
                    }
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error in getGame()");
        }
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, gameJson FROM games";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                try (var resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        result.add(readGame(resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error in listGames()");
        }
        return result;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        var statement = "UPDATE games SET whiteUsername = ?, blackUsername = ?, gameName = ?, gameJson = ? WHERE gameID = ?";
        var json = new Gson().toJson(game.game());
        int rowsUpdated = DatabaseManager.executeUpdate(statement, game.whiteUsername(), game.blackUsername(), game.gameName(), json, game.gameID());
        if (rowsUpdated == 0) {
            throw new DataAccessException("Game was not updated because gameID not found in db");
        }
    }

    private GameData readGame(ResultSet resultSet) throws SQLException {
        int gameID = resultSet.getInt("gameID");
        String whiteUsername = resultSet.getString("whiteUsername");
        String blackUsername = resultSet.getString("blackUsername");
        String gameName = resultSet.getString("gameName");

        var gameJson = resultSet.getString("gameJson");
        ChessGame chessGame = new Gson().fromJson(gameJson, ChessGame.class);
        GameData result = new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
        return result;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  games (
              `gameId` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `gameJson` TEXT NOT NULL,
              PRIMARY KEY (`gameId`),
              INDEX(gameName)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
