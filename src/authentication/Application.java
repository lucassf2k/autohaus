package authentication;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Application {
    public static void main(String[] args) {
        try {
            final var implAuthenticationRemote = new ImplAuthenticationRemote();
            final var skeleton = (AuthenticationRemote) UnicastRemoteObject
                    .exportObject(implAuthenticationRemote, 0);
            LocateRegistry.createRegistry(ImplAuthenticationRemote.PORT);
            final var registry = LocateRegistry.getRegistry(ImplAuthenticationRemote.PORT);
            registry.bind("authentication", skeleton);
            System.out.println("serviço de autenticação rodando em " + ImplAuthenticationRemote.PORT);
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
