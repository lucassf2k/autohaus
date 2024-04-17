package gateway;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.math.BigInteger;

public record Response(BigInteger RSA_PUBLIC_KEY, BigInteger RSA_MODULUS, String content, String HMAC, String HMAC_KEY, String VERNAM_KEY, SecretKey AES_KEY) implements Serializable {
}
