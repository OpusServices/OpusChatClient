package tk.zgiulydev.opuschatclient.chat;

import tk.zgiulydev.opuschatclient.OpusChatClient;
import tk.zgiulydev.opuschatclient.utils.SocketUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final String ip;
    private final int port;
    private final String nickname;
    private Socket socket;

    public Client(String ip, int port, String nickname) {
        this.ip = ip;
        this.port = port;
        this.nickname = nickname;
    }

    public void connect() throws Throwable {
        socket = new Socket();
        socket.connect(new InetSocketAddress(ip, port), 120);
        OpusChatClient.logger.info("Connesso con successo!");
        OpusChatClient.logger.info("Setup connessione in corso..");

        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        ClientHandler handler = new ClientHandler(in, out, nickname);

        Thread t1 = new Thread(handler);
        t1.start();

        OpusChatClient.logger.info("Ora puoi inviare messaggi: ");
        Scanner scanner = new Scanner(System.in);
        while(true) {
            String input = scanner.nextLine();
            if(!input.isEmpty() && handler.getEncryptCipher() != null) {
                OpusChatClient.logger.info("Messaggio " + input + " inviato");
                SocketUtils.sendMessage(out, handler.getEncryptCipher(), input);
            }
        }
    }
}
