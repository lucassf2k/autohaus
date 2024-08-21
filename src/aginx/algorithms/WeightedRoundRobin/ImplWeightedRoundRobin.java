package aginx.algorithms.WeightedRoundRobin;

import aginx.algorithms.Protocol;
import database.DatabaseRemote;

import java.rmi.RemoteException;
import java.util.Map;

public class ImplWeightedRoundRobin implements Protocol {
    private static Integer requestNumber = 0;
    private Integer index = 0;
    private final Map<DatabaseRemote, Integer> dbs;

    public ImplWeightedRoundRobin(final Map<DatabaseRemote, Integer> dbs) {
        this.dbs = dbs;
    }

    @Override
    public DatabaseRemote execute() throws RemoteException {
        return this.getCurrent();
    }

    private DatabaseRemote getCurrent() {
        System.out.println("R" + requestNumber);
        requestNumber++;
        DatabaseRemote response = null;

        for (var i = 0; i < this.dbs.size(); i++) {

        }

        return response;
    }
}
