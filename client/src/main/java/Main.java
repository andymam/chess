import chess.*;
import serverFacade.ServerFacade;
import ui.*;
import java.net.*;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Main {

    private final ReplUI ui;

    public Main(String serverUrl) {
        ui = new ReplUI(serverUrl);
    }

    public static void main(String[] args) {
        System.out.println("♕ Welcome to 240 Chess ♕");
        Scanner scanner = new Scanner(System.in);

        String menu ="""
                - register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                - login <USERNAME> <PASSWORD> - to play chess
                - quit - playing chess
                - help - with possible commands
                """;

        var result = "";
        while (!result.equals("quit")) {
            try {
                System.out.println(SET_TEXT_COLOR_WHITE + menu);
                String line = scanner.nextLine();
                result = ui.eval(line);
                System.out.println(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }
}