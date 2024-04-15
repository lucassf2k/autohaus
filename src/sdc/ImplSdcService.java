package sdc;

import crypto.AES128;
import crypto.HMAC;
import crypto.RSA;
import crypto.Vernam;

import javax.crypto.*;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class ImplSdcService implements SdcService {
    public static final int PORT = 5006;

    @Override
    public SecretKey getAESKey() throws RemoteException {
        return this.generateAESKey();
    }

    @Override
    public RSAKeys getRSAKeys() throws RemoteException {
        final var rsa = new RSA();
        rsa.generateKeys(1024);
        return new RSAKeys(rsa.getPublicKey(), rsa.getPrivateKey(), rsa.getModulus());
    }

    @Override
    public String getVernamKey() throws RemoteException {
        return generateRandomBytes(128);
    }

    @Override
    public String encryptMessage(String message, String vernamKey, SecretKey AESKey) throws RemoteException {
        String output = "";
        try {
            final var vernamMessage = Vernam.encrypt(message, vernamKey);
            output = AES128.encrypt(vernamMessage, AESKey);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    @Override
    public String decryptMessage(String message, String vernamKey, SecretKey AESKey) throws RemoteException {
        String output = "";
        try {
            final var aesMessage = AES128.decrypt(message, AESKey);
            output = Vernam.decrypt(aesMessage, vernamKey);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    @Override
    public String signMessage(String secureMessage, String hmacKey, BigInteger rsaPrivateKey, BigInteger rsaModulus) throws RemoteException {
        String output = "";
        try {
            final var hmacMessage = HMAC.hMac(hmacKey, secureMessage);
            output = RSA.sign(hmacMessage, rsaPrivateKey, rsaModulus);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    @Override
    public String checkSignMessage(String secureMessage, String hmacKey, BigInteger rsaPublicKey, BigInteger rsaModulus) throws RemoteException {
       return RSA.checkSignature(secureMessage, rsaPublicKey, rsaModulus);
    }

    private SecretKey generateAESKey() {
        KeyGenerator keyGenerator;
        SecretKey key = null;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
            key = keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return key;
    }

    private String generateRandomBytes(final int size) {
        final var randomBytes = new byte[size];
        final var secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);
        return bytesToHex(randomBytes);
    }

    private String bytesToHex(final byte[] bytes) {
        final var hexStringBuilder = new StringBuilder();
        for (var b : bytes) {
            hexStringBuilder.append(String.format("%02X", b));
        }
        return hexStringBuilder.toString();
    }


    public record RSAKeys(BigInteger publicKey, BigInteger privateKey, BigInteger modulus) implements Serializable {}
}
