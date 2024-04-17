package authentication;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthenticationRemote extends Remote {
    ImplAuthenticationRemote.Credentials login(String email, String password) throws RemoteException;
    User get(String email) throws RemoteException;
}
