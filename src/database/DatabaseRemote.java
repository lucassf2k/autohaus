package database;

import shared.Car;
import shared.CarCategories;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface DatabaseRemote extends Remote {
    Boolean save(Car input) throws RemoteException;
    List<Car> list() throws RemoteException;
    Boolean delele(String revanam) throws RemoteException;
    Car get(String renavam) throws RemoteException;
    List<Car> getOfName(String name) throws RemoteException;
    List<Car> getOfCategory(CarCategories category) throws RemoteException;
    Boolean update(String renavam, Car updatedCar) throws RemoteException;
    int getPort() throws RemoteException;
}
