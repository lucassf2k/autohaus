package aginx.algorithms.RoundRobin;

import aginx.algorithms.Protocol;
import database.DatabaseRemote;

import java.rmi.RemoteException;
import java.util.List;

public class ImplRoundRobin implements Protocol {
    public static int PORT = 6000;
    private static int serverIndex = 0;
    private final List<DatabaseRemote> dbs;

    public ImplRoundRobin(List<DatabaseRemote> dbs) {
        this.dbs = dbs;
    }

    @Override
    public DatabaseRemote execute() throws RemoteException {
        return this.getCurrent();
    }

    private DatabaseRemote getCurrent() throws RemoteException {
        final var response = this.dbs.get(serverIndex);
        System.out.println("Server que ta respondendo: " + response.getPort());
        ImplRoundRobin.serverIndex = (ImplRoundRobin.serverIndex + 1) % this.dbs.size();
        return response;
    }
}
