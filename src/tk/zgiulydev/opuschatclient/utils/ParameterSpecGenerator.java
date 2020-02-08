package tk.zgiulydev.opuschatclient.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;

public class ParameterSpecGenerator {
    public static IvParameterSpec generate(Cipher cipher) {
        try {
            byte[] iv = new byte[cipher.getBlockSize()];
            SecureRandom.getInstance("SHA1PRNG").nextBytes(iv);
            return new IvParameterSpec(iv);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
