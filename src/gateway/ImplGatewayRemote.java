package gateway;

import authentication.AuthenticationRemote;
import authentication.ImplAuthenticationRemote;
import authentication.User;
import authentication.UserTypes;
import database.DatabaseRemote;
import shared.Car;
import shared.CarCategories;

import javax.security.auth.login.CredentialNotFoundException;
import java.rmi.RemoteException;
import java.util.List;

public class ImplGatewayRemote implements GatewayRemote {
    public static final int PORT = 20006;
    private final AuthenticationRemote authenticationStub;
    private final DatabaseRemote carDatabaseStub;

    public ImplGatewayRemote(AuthenticationRemote authenticationStub, DatabaseRemote carDatabaseStub) {
        this.authenticationStub = authenticationStub;
        this.carDatabaseStub = carDatabaseStub;
    }

    @Override
    public ImplAuthenticationRemote.Credentials login(String email, String password) throws RemoteException {
        final var newUser = new User(email, password, UserTypes.CUSTOMER);
        return authenticationStub.login(newUser);
    }

    @Override
    public Boolean createCar(String revavam, String name, CarCategories category, String yearManufacture, Double price) throws RemoteException {
        final var newCar = new Car(revavam, name, category, yearManufacture, price);
        return carDatabaseStub.save(newCar);
    }

    @Override
    public List<Car> list() throws RemoteException {
        return carDatabaseStub.list();
    }

    @Override
    public Boolean deleteCar(String renanam) throws RemoteException {
        return carDatabaseStub.delele(renanam);
    }

    @Override
    public Car getCar(String renavam) throws RemoteException {
        return carDatabaseStub.get(renavam);
    }

    @Override
    public List<Car> getCarOfName(String name) throws RemoteException {
        return carDatabaseStub.getOfName(name);
    }

    @Override
    public List<Car> getCarOfCategory(int category) throws RemoteException {
        CarCategories carCategory = null;
        if (category == 0) {
            carCategory = CarCategories.ECONOMIC;
        }
        if (category == 1) {
            carCategory = CarCategories.INTERMEDIATE;
        }
        if (category == 2) {
            carCategory = CarCategories.EXECUTIVE;
        }
        return carDatabaseStub.getOfCategory(carCategory);
    }

    @Override
    public Boolean updateCar(String renavam, String name, CarCategories category, String yearManufacture, Double price) throws RemoteException {
        final var updatedCar = new Car(renavam, name, category, yearManufacture, price);
        return carDatabaseStub.update(renavam, updatedCar);
    }

    @Override
    public Car buyCar(String renavam, double price) throws RemoteException {
        final var carToBuy = getCar(renavam);
        if (!carToBuy.getPrice().equals(price)) return null;
        deleteCar(renavam);
        return carToBuy;
    }
}
