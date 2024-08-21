package sdc;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Application {
    public static void main(String[] args) {
        //System.setProperty("java.rmi.server.hostname", "172.20.10.2");
        //System.setProperty("java.security.policy", "java.policy");
        try {
            final var sdc = new ImplSdcService();
            final var skeleton = (SdcService) UnicastRemoteObject.exportObject(sdc, 0);
            LocateRegistry.createRegistry(ImplSdcService.PORT);
            final var registry = LocateRegistry.getRegistry(ImplSdcService.PORT);
            registry.bind("sdc", skeleton);
            System.out.println("serviço de distribuíção de chaves rodando em " + ImplSdcService.PORT);
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }
}