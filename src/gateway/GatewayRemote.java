package gateway;

import authentication.ImplAuthenticationRemote;
import authentication.User;
import shared.Car;
import shared.CarCategories;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GatewayRemote extends Remote {
    ImplAuthenticationRemote.Credentials login(String email, String password) throws RemoteException;
    Boolean createCar(
        String revavam,
        String name,
        CarCategories category,
        String yearManufacture,
        Double price
    ) throws RemoteException;
    List<Car> list() throws RemoteException;
    Boolean deleteCar(String renanam) throws RemoteException;
    Car getCar(String renavam) throws RemoteException;
    List<Car> getCarOfName(String name) throws RemoteException;
    List<Car> getCarOfCategory(int category) throws RemoteException;
    Boolean updateCar(
        String renavam,
        String revavam,
        String name,
        CarCategories category,
        String yearManufacture,
        Double price
    ) throws RemoteException;
    Car buyCar(String renavam, double price) throws RemoteException;
}
