package firewall;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProxyReverseProtocol extends Remote {
    String execute(final Package pack) throws RemoteException;
}
