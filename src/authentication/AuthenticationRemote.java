package authentication;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthenticationRemote extends Remote {
    Boolean login(User user) throws RemoteException;
}
