package sdc;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SdcService extends Remote {
    String getAESKey() throws RemoteException, RemoteException;
    ImplSdcService.RSAKeys getRSAKeys() throws RemoteException;
    String getVernamKey() throws RemoteException;
}
