package authentication;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthenticationRemote extends Remote {
    ImplAuthenticationRemote.Credentials login(User user) throws RemoteException;
}
