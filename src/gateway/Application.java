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

public class Application {
    public static void main(String[] args) {
        try {
            final var registryAuth = LocateRegistry.getRegistry(ImplAuthenticationRemote.PORT);
            final var authenticationStub = (AuthenticationRemote) registryAuth.lookup("authentication");
            final var registryDB = LocateRegistry.getRegistry(ImplDatabaseRemote.PORT);
            final var databaseStub = (DatabaseRemote) registryDB.lookup("database");
            final var implGatewayRemote = new ImplGatewayRemote(authenticationStub, databaseStub);
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
