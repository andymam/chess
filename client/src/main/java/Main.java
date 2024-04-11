import chess.*;
import exception.ResponseException;
import serverFacade.ServerFacade;
import ui.*;
import webSocketMessages.serverMessages.NotificationMessage;

import java.net.*;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Main {

    private static ReplUI ui;

    public static void main(String[] args) throws ResponseException {
        System.out.println("♕ Welcome to 240 Chess ♕");
        Scanner scanner = new Scanner(System.in);
        ui = new ReplUI();

        var result = "";
        while (!result.equals("quit")) {
            System.out.println(SET_TEXT_COLOR_WHITE + ui.print_menu());
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = ui.eval(line);
                System.out.println(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }


    private static void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + SET_TEXT_COLOR_GREEN);
    }
}