package aginx.algorithms.WeightedRoundRobin;

import aginx.algorithms.Protocol;
import aginx.algorithms.RoundRobin.ImplRoundRobin;
import database.DatabaseRemote;
import database.ImplDatabaseRemote;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class Application {
    public static void main(String[] args) {
        try {
            final Map<DatabaseRemote, Integer> databases = new HashMap<>();
            var registryDB = LocateRegistry.getRegistry(ImplDatabaseRemote.PORTS[0]);
            var databaseStub = (DatabaseRemote) registryDB.lookup("database");
            databases.put(databaseStub, 1);
            registryDB = LocateRegistry.getRegistry(ImplDatabaseRemote.PORTS[1]);
            databaseStub = (DatabaseRemote) registryDB.lookup("database");
            databases.put(databaseStub, 2);
            registryDB = LocateRegistry.getRegistry(ImplDatabaseRemote.PORTS[2]);
            databaseStub = (DatabaseRemote) registryDB.lookup("database");
            databases.put(databaseStub, 3);

            final var implRoundRobin = new ImplWeightedRoundRobin(databases);
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
