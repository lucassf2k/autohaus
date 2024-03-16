package database;

import shared.Car;
import shared.CarCategories;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface DatabaseRemote extends Remote {
    Boolean save(Car input) throws RemoteException;
    List<Car> list() throws RemoteException;
    Boolean delele() throws RemoteException;
    Car get(String revavam) throws RemoteException;
    List<Car> getOfName(String name) throws RemoteException;
    List<Car> getOfCategory(CarCategories category) throws RemoteException;
    Boolean update(String renavam, Car updatedCar) throws RemoteException;
}
