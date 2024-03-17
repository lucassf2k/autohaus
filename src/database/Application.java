package database;

import authentication.AuthenticationRemote;
import authentication.ImplAuthenticationRemote;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Application {
    public static void main(String[] args) {
        try {
            final var implDatabaseRemote = new ImplDatabaseRemote();
            final var skeleton = (DatabaseRemote) UnicastRemoteObject
                    .exportObject(implDatabaseRemote, 0);
            LocateRegistry.createRegistry(ImplDatabaseRemote.PORT);
            final var registry = LocateRegistry.getRegistry(ImplDatabaseRemote.PORT);
            registry.bind("database", skeleton);
            System.out.println("serviço de banco de dados rodando em " + ImplDatabaseRemote.PORT);
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
