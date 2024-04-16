package firewall;

import gateway.Response;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProxyReverseProtocol extends Remote {
    Response execute(final Package pack) throws RemoteException;
}
