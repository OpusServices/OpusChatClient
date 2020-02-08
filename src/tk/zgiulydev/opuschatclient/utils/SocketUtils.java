package tk.zgiulydev.opuschatclient.utils;

import javax.crypto.Cipher;
import java.io.PrintWriter;
import java.util.Base64;

public class SocketUtils {
    public static void sendMessage(PrintWriter out, Cipher cipher, String message) throws Throwable {
        byte[] data = Base64.getEncoder().encode(message.getBytes());
        byte[] crypt = cipher.doFinal(data);
        byte[] finalCrypt = Base64.getEncoder().encode(crypt);

        out.println(new String(finalCrypt));
        out.flush();
    }
}
