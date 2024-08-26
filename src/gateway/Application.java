package gateway;

import aginx.algorithms.RoundRobin.ImplRoundRobin;
import aginx.algorithms.Protocol;
import authentication.AuthenticationRemote;
import authentication.ImplAuthenticationRemote;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Application {
    public static void main(String[] args) {
        try {
            //List<DatabaseRemote> databases = new ArrayList<>();
            final var registryAuth = LocateRegistry.getRegistry(ImplAuthenticationRemote.PORT);
            final var authenticationStub = (AuthenticationRemote) registryAuth.lookup("authentication");

            final var registryAginx = LocateRegistry.getRegistry(ImplRoundRobin.PORT);
            final var aginxStub = (Protocol) registryAginx.lookup("aginx");

//            var registryDB = LocateRegistry.getRegistry(ImplDatabaseRemote.PORTS[0]);
//            var databaseStub = (DatabaseRemote) registryDB.lookup("database");
//            databases.add(databaseStub);
//
//            registryDB = LocateRegistry.getRegistry(ImplDatabaseRemote.PORTS[1]);
//            databaseStub = (DatabaseRemote) registryDB.lookup("database");
//            databases.add(databaseStub);
//
//            registryDB = LocateRegistry.getRegistry(ImplDatabaseRemote.PORTS[2]);
//            databaseStub = (DatabaseRemote) registryDB.lookup("database");
//            databases.add(databaseStub);

            final var implGatewayRemote = new ImplGatewayRemote(authenticationStub, aginxStub,true);
            final var skeleton = (GatewayRemote) UnicastRemoteObject
                    .exportObject(implGatewayRemote, 0);
            LocateRegistry.createRegistry(ImplGatewayRemote.PORT);
            final var registryGateway = LocateRegistry.getRegistry(ImplGatewayRemote.PORT);
            registryGateway.bind("gateway", skeleton);
            System.out.println("serviço de gateway rodando em " + ImplGatewayRemote.PORT);
        } catch (RemoteException | AlreadyBoundException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
