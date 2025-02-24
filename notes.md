# My notes
Server:
 - receive HTTP request and send to correct handler
 - handle all unhandled exceptions and return correct status code

Handler:
 - classes act as translator from HTTP to Java

Service:
 - classes to implement the logic associated with the web endpoints.
 - receives Request and returns Result

```
UserService {
    register(RegisterRequest) -> RegisterResult
    login(LoginRequest) -> LoginResult
    logout(LogoutRequest)
}
```
```
GameService {
    listGames(ListRequest) -> ListResult
    createGame(CreateRequest) -> CreateResult
    joinGame(JoinRequest) -> JoinResult
}
```
```
ClearService {
    clear() -> ClearResult
}
```
Data Access:
- initially interfaces
- classes responsible for storing and retrieving data
- parameters and return values are often Data Model Classes

```
UserDAO {
    clear() ?
    createUser(UserData) => UserData in db
    getUser(String username) -> UserData
    updateUser() ?
    deleteUser(String username) ?
}
```
```
AuthDAO {
    clear() ?
    createAuth(AuthData) => AuthData in db #maybe return the AuthToken?
    getAuth(AuthToken) -> String username #UserData #maybe returns true/false? 
    updateAuth() ?
    deleteAuth(AuthToken?) => remove AuthData from db
}
```
```
GameDAO {
    clear() ?
    createGame(GameData) -> int gameID => GameData in db
    getGame(int gameID) -> GameData
    listGames() -> [all GameData in db]
    updateGame(GameData) => update GameData in db
    deleteGame(int gameID) =>remove GameData from db
}
```


db:

Data Model Classes:
 - record classes
```
UserData {
    username
    password
    email
}
```
```
GameData {
    gameID
    whiteUsername
    blackUsername
    gameName
    game
}
```
```
AuthData {
    authToken
    username
}
```

Request and Result Classes:
 - record classes from JSON representation of service class input/output
