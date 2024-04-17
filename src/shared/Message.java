package shared;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.math.BigInteger;

public class Message implements Serializable {
    private final MessageTypes type;
    private final String content;
    private final String HMAC;
    private final String authenticationKey;
    private final BigInteger RSA_PUBLIC_KEY;
    private final BigInteger RSA_MODULUS;
    private final String HMAC_KEY;
    private final String VERNAM_KEY;
    private final SecretKey AES_KEY;

    public Message(
            MessageTypes type,
            String content,
            String HMAC,
            String authenticationKey,
            BigInteger RSA_PUBLIC_KEY,
            BigInteger RSA_MODULUS,
            String HMAC_KEY,
            String VERNAM_KEY,
            SecretKey AES_KEY) {
        this.type = type;
        this.content = content;
        this.HMAC = HMAC;
        this.authenticationKey = authenticationKey;
        this.RSA_PUBLIC_KEY = RSA_PUBLIC_KEY;
        this.RSA_MODULUS = RSA_MODULUS;
        this.HMAC_KEY = HMAC_KEY;
        this.VERNAM_KEY = VERNAM_KEY;
        this.AES_KEY = AES_KEY;
    }

    public MessageTypes getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getHMAC() {
        return HMAC;
    }

    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public BigInteger getRSA_PUBLIC_KEY() {
        return RSA_PUBLIC_KEY;
    }

    public BigInteger getRSA_MODULUS() {
        return RSA_MODULUS;
    }

    public String getHMAC_KEY() {
        return HMAC_KEY;
    }

    public String getVERNAM_KEY() {
        return VERNAM_KEY;
    }

    public SecretKey getAES_KEY() {
        return AES_KEY;
    }
}
