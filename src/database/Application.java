package database;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application {
    public static void main(String[] args) {
//        ExecutorService executor = Executors.newFixedThreadPool(3);
//
//        // Primeiro conjunto de portas
//        executor.submit(() -> registerServers(ImplDatabaseRemote.PORTS[0]));
//
//        // Segundo conjunto de portas
//        executor.submit(() -> registerServers(ImplDatabaseRemote.PORTS[1]));
//
//        // Terceiro conjunto de portas
//        executor.submit(() -> registerServers(ImplDatabaseRemote.PORTS[2]));
//
//        executor.shutdown();


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

    private static void registerServers(int port) {
        try {
            final var implDatabaseRemote = new ImplDatabaseRemote(port);
            final var skeleton = (DatabaseRemote) UnicastRemoteObject.exportObject(implDatabaseRemote, 0);
            LocateRegistry.createRegistry(port);
            final var registry = LocateRegistry.getRegistry(port);
            registry.bind("database", skeleton);
            System.out.println("Serviço de banco de dados rodando em " + port);
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
