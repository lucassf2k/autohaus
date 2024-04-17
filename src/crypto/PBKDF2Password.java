package crypto;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

public class PBKDF2Password {
    public static String[] create(String password, String salt) {
        try {
            final var iteration = 500_000;
            final var passwordCh = password.toCharArray();
            String rawSalt = salt;
            if (Objects.isNull(salt)) {
                rawSalt = byte2hex(getSalt());
            }
            final var pbk = new PBEKeySpec(passwordCh, rawSalt.getBytes(), iteration, 512);
            final var skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            final var hash = skf.generateSecret(pbk).getEncoded();
            final var output = new String[2];
            output[0] = rawSalt;
            output[1] = byte2hex(hash);
            return output;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] getSalt() {
        try {
            final var secureRandom = SecureRandom.getInstance("SHA1PRNG");
            final var salt = new byte[16];
            secureRandom.nextBytes(salt);
            return salt;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String byte2hex(byte[] bytes) {
        StringBuilder strHex = new StringBuilder();
        for (byte b : bytes) {
            strHex.append(String.format("%02x", b));
        }
        return strHex.toString();
    }
}
