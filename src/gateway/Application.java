package gateway;

import authentication.AuthenticationRemote;
import authentication.ImplAuthenticationRemote;
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
            List<DatabaseRemote> databases = new ArrayList<>();
            final var registryAuth = LocateRegistry.getRegistry(ImplAuthenticationRemote.PORT);
            final var authenticationStub = (AuthenticationRemote) registryAuth.lookup("authentication");
            
            var registryDB = LocateRegistry.getRegistry(ImplDatabaseRemote.PORTS[0]);
            var databaseStub = (DatabaseRemote) registryDB.lookup("database1");
            databases.add(databaseStub);
            
            registryDB = LocateRegistry.getRegistry(ImplDatabaseRemote.PORTS[1]);
            databaseStub = (DatabaseRemote) registryDB.lookup("database2");
            databases.add(databaseStub);

            registryDB = LocateRegistry.getRegistry(ImplDatabaseRemote.PORTS[2]);
            databaseStub = (DatabaseRemote) registryDB.lookup("database3");
            databases.add(databaseStub);

            final var implGatewayRemote = new ImplGatewayRemote(authenticationStub, databases);
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
