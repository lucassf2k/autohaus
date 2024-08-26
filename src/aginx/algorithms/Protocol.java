package aginx.algorithms;

import database.DatabaseRemote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Protocol extends Remote {
    DatabaseRemote execute() throws RemoteException;
}
