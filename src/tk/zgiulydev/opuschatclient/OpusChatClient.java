package tk.zgiulydev.opuschatclient;

import tk.zgiulydev.opuschatclient.chat.Client;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpusChatClient {
    public static final Logger logger = Logger.getLogger(OpusChatClient.class.getName());

    public static void main(String[] args) {
        logger.setLevel(Level.ALL);
        try {
            connect();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private static void connect() throws Throwable {
        Scanner s = new Scanner(System.in);
        System.out.println("Inserisci ip");
        String ip = s.nextLine();
        System.out.println("Inserisci porta");
        int port = Integer.parseInt(s.nextLine());
        System.out.println("Inserisci nickname");
        String nickname = s.nextLine();

        new Client(ip, port, nickname).connect();
    }
}
