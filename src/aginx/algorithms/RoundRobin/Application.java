package aginx.algorithms.RoundRobin;

import aginx.algorithms.Protocol;
import database.DatabaseRemote;
import database.ImplDatabaseRemote;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        try {
            final List<DatabaseRemote> databases = new ArrayList<>();
            var registryDB = LocateRegistry.getRegistry(ImplDatabaseRemote.PORTS[0]);
            var databaseStub = (DatabaseRemote) registryDB.lookup("database");
            databases.add(databaseStub);
            registryDB = LocateRegistry.getRegistry(ImplDatabaseRemote.PORTS[1]);
            databaseStub = (DatabaseRemote) registryDB.lookup("database");
            databases.add(databaseStub);
            registryDB = LocateRegistry.getRegistry(ImplDatabaseRemote.PORTS[2]);
            databaseStub = (DatabaseRemote) registryDB.lookup("database");
            databases.add(databaseStub);

            final var implRoundRobin = new ImplRoundRobin(databases);
            final var skeleton = (Protocol) UnicastRemoteObject
                    .exportObject(implRoundRobin, 0);
            LocateRegistry.createRegistry(ImplRoundRobin.PORT);
            final var registry = LocateRegistry.getRegistry(ImplRoundRobin.PORT);
            registry.bind("aginx", skeleton);
            System.out.println("serviço de agnix rodando em " + ImplRoundRobin.PORT);
        } catch (RemoteException | AlreadyBoundException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
