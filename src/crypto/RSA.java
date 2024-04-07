package crypto;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSA {
    private BigInteger publicKey;
    private BigInteger privateKey;
    private BigInteger modulus;

    public void generateKeys(final int keySize) {
        SecureRandom random = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(keySize / 2, random);
        BigInteger q = BigInteger.probablePrime(keySize / 2, random);
        modulus = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        publicKey = BigInteger.probablePrime(keySize / 4, random); // Public exponent
        privateKey = publicKey.modInverse(phi);
    }
    public static String sign(
            final String message,
            final BigInteger privateKey,
            final BigInteger modulus
    ) {
        StringBuilder encrypted_message = new StringBuilder();
        for (char ch : message.toCharArray()) {
            final var chToBigInteger = new BigInteger(Integer.toString(ch));
            final var chToBigIntegerSigned = chToBigInteger.modPow(privateKey, modulus);
            encrypted_message.append(chToBigIntegerSigned).append(" ");
        }
        return encrypted_message.toString();
//        final var originalMessage = new BigInteger(message);
//        return originalMessage.modPow(privateKey, modulus).toString();
    }

    public static String checkSignature(
            final String encryptedMessage,
            final BigInteger publicKey,
            final BigInteger modulus
    ) {
        StringBuilder decryptedMessage = new StringBuilder();
        String[] chars = encryptedMessage.split(" ");
        for (final var ch : chars) {
            BigInteger chToBigInteger = new BigInteger(ch);
            BigInteger chToBigIntegerSigned = chToBigInteger.modPow(publicKey, modulus);
            final var convertedToChar = (char)(chToBigIntegerSigned.intValue());
            decryptedMessage.append(convertedToChar);
        }
        return decryptedMessage.toString();
//        final var message = new BigInteger(encryptedMessage.getBytes());
//        return new String(message.modPow(publicKey, modulus).toByteArray());
    }

    public BigInteger getPublicKey() {
        return publicKey;
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }
    public BigInteger getModulus() {
        return modulus;
    }
}