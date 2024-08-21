package aginx.algorithms.LeastConnections;

import aginx.algorithms.Protocol;
import database.DatabaseRemote;

import java.rmi.RemoteException;
import java.util.Map;

public class ImplLeastConnections implements Protocol {
    private static Integer roundConnectionNumber = 0;
    private static Integer countOfRound = 0;
    private static Integer requestNumber = 0;
    private final Map<DatabaseRemote, Integer> dbs;

    public ImplLeastConnections(final Map<DatabaseRemote, Integer> dbs) {
        this.dbs = dbs;
    }

    @Override
    public DatabaseRemote execute() throws RemoteException {
        return this.getCurrent();
    }

    private DatabaseRemote getCurrent() throws RemoteException {
        System.out.println("R" + requestNumber);
        requestNumber++;
        System.out.println("Número da menor conexão ativa: " + roundConnectionNumber);
        DatabaseRemote response = null;
        for (var db : this.dbs.entrySet()) {
            System.out.print("Número da conexão do anterior: " + db.getValue());
            System.out.println();
            if (db.getValue().equals(roundConnectionNumber)) {
                response = db.getKey();
                System.out.println("Servidor que ta respondendo: " + db.getKey().getPort());
                db.setValue(db.getValue() + 1);
                countOfRound++;
                break;
            }
        }
        if (countOfRound.equals(this.dbs.size())) {
            roundConnectionNumber++;
            countOfRound = 0;
        }
        return response;
    }
}
