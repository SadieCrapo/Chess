package dataaccess;

import com.google.gson.Gson;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLGameDAO implements GameDAO {
    public SQLGameDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE games";
        executeUpdate(statement);
    }

    @Override
    public int createGame(GameData game) throws DataAccessException {
        var statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, gameJson) VALUES (?, ?, ?, ?)";
        var json = new Gson().toJson(game);
        return executeUpdate(statement, game.whiteUsername(), game.blackUsername(), game.gameName(), json);
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
                    throw new SQLException("No game with this id");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error in getGame()");
//            throw new ResponseException(String.format("Unable to read data: %s", e.getMessage()));
        }
//        return null;
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
    public void updateGame(GameData game) {

    }

    private GameData readGame(ResultSet resultSet) throws SQLException {
//        var gameID = resultSet.getInt("gameID");
        var gameJson = resultSet.getString("gameJson");
        return new Gson().fromJson(gameJson, GameData.class);
//        return gameData;
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) {
                        preparedStatement.setString(i + 1, p);
                    } else if (param instanceof Integer p) {
                        preparedStatement.setInt(i + 1, p);
                    } else if (param == null) {
                        preparedStatement.setNull(i + 1, NULL);
                    }
                }
                preparedStatement.executeUpdate();

                var resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
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


    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }
}
