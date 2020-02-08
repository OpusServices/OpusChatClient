package tk.zgiulydev.opuschatclient.utils;

import tk.zgiulydev.opuschatclient.OpusChatClient;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class SecuredConnection {
    private final PrintWriter out;
    private final BufferedReader in;
    private Cipher rsaCrypt;
    private Cipher rsaDecrypt;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private PublicKey serverPublicKey;

    public SecuredConnection(PrintWriter out, BufferedReader in) throws Throwable {
        this.out = out;
        this.in = in;
        generateKey();
        getPublicKey();
    }


    private void getPublicKey() throws Throwable {
        String key = in.readLine();

        if(key.isEmpty()) throw new NullPointerException("Chiave asimmetrica non valida");

        byte[] decoded = Base64.getDecoder().decode(key.getBytes());

        KeyFactory factory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);

        this.serverPublicKey = factory.generatePublic(keySpec);
        rsaCrypt = Cipher.getInstance("RSA");
        rsaCrypt.init(Cipher.ENCRYPT_MODE, this.serverPublicKey);

        rsaDecrypt = Cipher.getInstance("RSA");
        rsaDecrypt.init(Cipher.DECRYPT_MODE, privateKey);

        OpusChatClient.logger.info("Chiave asimmetrica inserita!");
    }

    public Cipher getRsaCrypt() {
        return rsaCrypt;
    }

    public Cipher getRsaDecrypt() {
        return rsaDecrypt;
    }

    private void generateKey() throws Throwable {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        KeyPair pair = generator.generateKeyPair();
        publicKey = pair.getPublic();
        privateKey = pair.getPrivate();

        out.println(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        out.flush();
    }
}
