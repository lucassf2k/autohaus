package sdc;

import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SdcService extends Remote {
    SecretKey getAESKey() throws RemoteException, RemoteException;
    ImplSdcService.RSAKeys getRSAKeys() throws RemoteException;
    String getVernamKey() throws RemoteException;
    String encryptMessage(String message, String vernamKey, SecretKey AESKey) throws RemoteException;
    String decryptMessage(String message, String vernamKey, SecretKey AESKey) throws RemoteException;
    String signMessage(String secureMessage, String hmacKey, BigInteger rsaPrivateKey, BigInteger rsaModulus) throws RemoteException;
    String checkSignMessage(String secureMessage, BigInteger rsaPublicKey, BigInteger rsaModulus) throws RemoteException;
}
