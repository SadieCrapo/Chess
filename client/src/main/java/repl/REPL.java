package repl;

import client.Client;
import client.PostLoginClient;
import client.PreLoginClient;
import client.ServerFacade;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class REPL {
    private Client client;
    private PreLoginClient preLoginClient;
    private PostLoginClient postLoginClient;

    private final ServerFacade server;

    public REPL(String serverUrl) {
        server = new ServerFacade(serverUrl);
        preLoginClient = new PreLoginClient(serverUrl, server, this);
        postLoginClient = new PostLoginClient(serverUrl, server, this);
        client = preLoginClient;
    }

    public void run() {
        System.out.println("\u2654 Welcome to chess. Sign in to start. \u265A");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    public void setClientToPostLogin(String authToken) {
        client = postLoginClient;
        postLoginClient.setAuthToken(authToken);
    }

    public void setClientToPreLogin() {
        client = preLoginClient;
    }

}
