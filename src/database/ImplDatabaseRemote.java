package database;

import shared.Car;
import shared.CarCategories;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ImplDatabaseRemote implements DatabaseRemote {
    private final Map<String, Car> DB = new HashMap<>();


    @Override
    public Boolean save(Car input) throws RemoteException {
        final var car = get(input.getRevavam());
        if (Objects.isNull(car)) return Boolean.FALSE;
        DB.put(input.getRevavam(), input);
        return Boolean.TRUE;
    }

    @Override
    public List<Car> list() throws RemoteException {
        return DB.values()
                .stream()
                .toList();
    }

    @Override
    public Boolean delele(String renavam) throws RemoteException {
        final var car = DB.get(renavam);
        if (Objects.isNull(car)) return Boolean.FALSE;
        DB.remove(renavam);
        return Boolean.TRUE;
    }

    @Override
    public Car get(String renavam) throws RemoteException {
        return DB.get(renavam);
    }

    @Override
    public List<Car> getOfName(String name) throws RemoteException {
        return DB.values()
                .stream()
                .filter(it -> it.getName().equals(name))
                .toList();
    }

    @Override
    public List<Car> getOfCategory(CarCategories category) throws RemoteException {
        return DB.values()
                .stream()
                .filter(it -> it.getCategory().equals(category))
                .toList();
    }

    @Override
    public Boolean update(String renavam, Car updatedCar) throws RemoteException {
        final var car = get(renavam);
        if (Objects.isNull(car)) return Boolean.FALSE;
        car.setName(updatedCar.getName());
        car.setPrice(updatedCar.getPrice());
        car.setCategory(updatedCar.getCategory());
        car.setYearManufacture(updatedCar.getYearManufacture());
        DB.remove(renavam);
        DB.put(renavam, car);
        return Boolean.TRUE;
    }
}
