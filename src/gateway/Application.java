package gateway;

import authentication.AuthenticationRemote;
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
            final var registry = LocateRegistry.getRegistry();
            final var authenticationStub = (AuthenticationRemote) registry.lookup("authentication");
            final var databaseStub = (DatabaseRemote) registry.lookup("database");
            final var implGatewayRemote = new ImplGatewayRemote(authenticationStub, databaseStub);
            final var skeleton = (GatewayRemote) UnicastRemoteObject
                    .exportObject(implGatewayRemote, 0);
            final var registryGateway = LocateRegistry.getRegistry(ImplGatewayRemote.PORT);
            LocateRegistry.createRegistry(ImplGatewayRemote.PORT);
            registryGateway.bind("gateway", skeleton);
            System.out.println("serviço de gateway rodando em " + ImplGatewayRemote.PORT);
        } catch (RemoteException | AlreadyBoundException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
