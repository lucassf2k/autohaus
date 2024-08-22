package database;

import shared.Car;
import shared.CarCategories;

import java.rmi.RemoteException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ImplDatabaseRemote implements DatabaseRemote {
    public final static int[] PORTS = new int[]{20005,20006,20007};
    public static boolean isCoord = true;
    private final int port;

    public ImplDatabaseRemote(final int port) {
        this.port = port;
    }

    @Override
    public Boolean save(Car input) throws RemoteException {
        System.out.println("processando cadastro de carro...");
        final var car = get(input.getRevavam());
        if (!Objects.isNull(car)) return Boolean.FALSE;
        ALSql.DB.put(input.getRevavam(), input);
        return Boolean.TRUE;
    }

    @Override
    public List<Car> list() throws RemoteException {
        System.out.println("processando listagem dos carros...");
        return  ALSql.DB.values()
                .stream()
                .sorted(Comparator.comparing(Car::getName))
                .collect(Collectors.toList());
    }

    @Override
    public Boolean delele(String renavam) throws RemoteException {
        System.out.println("processando remoção de carro...");
        final var car =  ALSql.DB.get(renavam);
        if (Objects.isNull(car)) return Boolean.FALSE;
        ALSql.DB.remove(renavam);
        return Boolean.TRUE;
    }

    @Override
    public Car get(String renavam) throws RemoteException {
        System.out.println("processando busca por renavam...");
        return  ALSql.DB.get(renavam);

    }

    @Override
    public List<Car> getOfName(String name) throws RemoteException {
        System.out.println("processando busca por modelo do carro...");
        final Predicate<Car> filterByName = (c) -> c.getName().equals(name);
        return  ALSql.DB.values()
                .stream()
                .filter(filterByName)
                .sorted(Comparator.comparing(Car::getName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Car> getOfCategory(CarCategories category) throws RemoteException {
        System.out.println("processando busca por categorias...");
        final Predicate<Car> filterByCategory = (c) -> c.getCategory().equals(category);
        return  ALSql.DB.values()
                .stream()
                .filter(filterByCategory)
                .sorted(Comparator.comparing(Car::getName))
                .collect(Collectors.toList());
    }

    @Override
    public Boolean update(String renavam, Car updatedCar) throws RemoteException {
        System.out.println("processando atualização de carro...");
        final var car = get(renavam);
        if (Objects.isNull(car)) return Boolean.FALSE;
        car.setName(updatedCar.getName());
        car.setPrice(updatedCar.getPrice());
        car.setCategory(updatedCar.getCategory());
        car.setYearManufacture(updatedCar.getYearManufacture());
        ALSql.DB.remove(renavam);
        ALSql.DB.put(renavam, car);
        return Boolean.TRUE;
    }

    @Override
    public int getPort() throws RemoteException {
        return this.port;
    }
}
