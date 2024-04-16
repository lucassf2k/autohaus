package database;

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
            LocateRegistry.createRegistry(ImplDatabaseRemote.PORTS[0]);
            final var registry = LocateRegistry.getRegistry(ImplDatabaseRemote.PORTS[0]);
            registry.bind("database", skeleton);
            System.out.println("serviço de banco de dados rodando em " + ImplDatabaseRemote.PORTS[0]);
            
            
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
