package firewall;

import gateway.GatewayRemote;
import gateway.ImplGatewayRemote;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        try {
            final var proxyReverse = new ProxyReverse();
            proxyReverse.setFilters(List.of(56001, 56002, 56003, ImplGatewayRemote.PORT));
            final var skeleton = (ProxyReverseProtocol) UnicastRemoteObject.exportObject(proxyReverse, 0);
            LocateRegistry.createRegistry(ProxyReverse.PORT);
            final var registry = LocateRegistry.getRegistry(ProxyReverse.PORT);
            registry.bind("proxy", skeleton);
            System.out.println("proxy executando na porta: " + ProxyReverse.PORT);
        } catch (RemoteException | NotBoundException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
