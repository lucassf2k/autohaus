package aginx.algorithms.WeightedRoundRobin;

import aginx.algorithms.Protocol;
import database.DatabaseRemote;
import java.rmi.RemoteException;
import java.util.Map;

public class ImplWeightedRoundRobin implements Protocol {
    private static Integer requestNumber = 0;
    private Integer index = 0;
    private Integer currentWeight = 0;
    private final Map<DatabaseRemote, Integer> dbs;

    public ImplWeightedRoundRobin(final Map<DatabaseRemote, Integer> dbs) {
        this.dbs = dbs;
    }

    @Override
    public DatabaseRemote execute() throws RemoteException {
        return this.getCurrent();
    }

    private DatabaseRemote getCurrent() throws RemoteException {
        System.out.println("Request " + requestNumber);
        requestNumber++;
        // Total weight of all servers
        final int totalWeight = this.dbs
                .values()
                .stream()
                .reduce(0, Integer::sum);
        // Increment current weight
        currentWeight = (currentWeight + 1) % totalWeight;
        int weightSum = 0;
        DatabaseRemote response = null;
        for (final var db : this.dbs.entrySet()) {
            weightSum += db.getValue();
            if (currentWeight < weightSum) {
                response = db.getKey();
                System.out.println("Servidor: " + db.getKey().getPort());
                break;
            }
        }
        if (response != null) {
            return response;
        }
        // Fallback if no server is found (shouldn't happen with valid weights)
        throw new RemoteException("No server selected, check the configuration.");
    }
}