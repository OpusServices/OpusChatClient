package tk.zgiulydev.opuschatclient.chat;

import tk.zgiulydev.opuschatclient.OpusChatClient;
import tk.zgiulydev.opuschatclient.utils.ParameterSpecGenerator;
import tk.zgiulydev.opuschatclient.utils.SecuredConnection;
import tk.zgiulydev.opuschatclient.utils.SocketUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class ClientHandler implements Runnable {
    private final BufferedReader in;
    private final PrintWriter out;
    private final String nickname;
    private Cipher encryptCipher;
    private Cipher decryptCipher;

    public ClientHandler(BufferedReader in, PrintWriter out, String nickname) {
        this.in = in;
        this.out = out;
        this.nickname = nickname;
    }

    @Override
    public void run() {
        try {
            SecuredConnection connection = new SecuredConnection(out, in);

            encryptCipher = connection.getRsaCrypt();
            decryptCipher = connection.getRsaDecrypt();

            OpusChatClient.logger.info("Invio nickname");

            SocketUtils.sendMessage(out, encryptCipher, nickname);

            while (true) {
                String data = in.readLine();

                byte[] stage1 = Base64.getDecoder().decode(data.getBytes());
                byte[] stage2 = decryptCipher.doFinal(stage1, 0, 128);
                byte[] finalString = Base64.getDecoder().decode(stage2);

                System.out.println(new String(finalString));
            }
        } catch (Throwable e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public Cipher getDecryptCipher() {
        return decryptCipher;
    }

    public Cipher getEncryptCipher() {
        return encryptCipher;
    }
}
