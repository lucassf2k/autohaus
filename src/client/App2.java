package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class App2 {
    public static void main(String[] args) {
        try {
            new ImplClient(56002);
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
