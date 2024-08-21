package database;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Application {
    public static void main(String[] args) {
        for (int port : ImplDatabaseRemote.PORTS) {
            try {
                final var implDatabaseRemote = new ImplDatabaseRemote(port);
                final var skeleton = (DatabaseRemote) UnicastRemoteObject.exportObject(implDatabaseRemote, 0);
                LocateRegistry.createRegistry(port);
                final var registry = LocateRegistry.getRegistry(port);
                registry.bind("database", skeleton);
                System.out.println("Serviço de banco de dados rodando em " + port);
            
            }
         catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
        }
    }
}
