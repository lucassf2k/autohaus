package database;

import shared.Car;
import shared.CarCategories;

import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

public class ImplDatabaseRemote implements DatabaseRemote {
    public final static int[] PORTS = new int[]{20005,20006,20007};
    public static boolean isCoord = true;
    private final Map<String, Car> DB = new HashMap<>(){{
        put("54874150", new Car("54874150", "Fiat novo uno", CarCategories.ECONOMIC, "2010", 35_000.00));
        put("54874151", new Car("54874151", "Chevrolet onix", CarCategories.ECONOMIC, "2015", 48_000.00));
        put("54874152", new Car("54874152", "HB20", CarCategories.ECONOMIC, "2019", 68_000.00));
        put("54874153", new Car("54874153", "Nissan march", CarCategories.ECONOMIC, "2020", 78_000.00));
        put("54874154", new Car("54874154", "Ford ka sedan", CarCategories.INTERMEDIATE, "2014", 39_000.00));
        put("54874155", new Car("54874155", "Chevrolet onix plus", CarCategories.INTERMEDIATE, "2020", 87000.00));
        put("54874156", new Car("54874156", "HB20s", CarCategories.INTERMEDIATE, "2019", 92_000.00));
        put("54874157", new Car("54874157", "Logan", CarCategories.INTERMEDIATE, "2022", 110_000.00));
        put("54874158", new Car("54874158", "Toyota etios", CarCategories.INTERMEDIATE, "2020", 91_000.00));
        put("54874159", new Car("54874159", "Toyota corolla", CarCategories.EXECUTIVE, "2020", 123_000.00));
        put("54874160", new Car("54874160", "Honda civic", CarCategories.EXECUTIVE, "2021", 132_000.00));
        put("54874161", new Car("54874161", "Chevrolet cruze", CarCategories.EXECUTIVE, "2022", 118_000.00));
        put("54874162", new Car("54874162", "Audi a3", CarCategories.EXECUTIVE, "2023", 201_000.00));
    }};


    @Override
    public Boolean save(Car input) throws RemoteException {
        System.out.println("processando cadastro de carro...");
        final var car = get(input.getRevavam());
        if (!Objects.isNull(car)) return Boolean.FALSE;
        DB.put(input.getRevavam(), input);
        return Boolean.TRUE;
    }

    @Override
    public List<Car> list() throws RemoteException {
        System.out.println("processando listagem dos carros...");
        return DB.values()
                .stream()
                .sorted(Comparator.comparing(Car::getName))
                .collect(Collectors.toList());
    }

    @Override
    public Boolean delele(String renavam) throws RemoteException {
        System.out.println("processando remoção de carro...");
        final var car = DB.get(renavam);
        if (Objects.isNull(car)) return Boolean.FALSE;
        DB.remove(renavam);
        return Boolean.TRUE;
    }

    @Override
    public Car get(String renavam) throws RemoteException {
        System.out.println("processando busca por renavam...");
        return DB.get(renavam);

    }

    @Override
    public List<Car> getOfName(String name) throws RemoteException {
        System.out.println("processando busca por modelo do carro...");
        return DB.values()
                .stream()
                .filter(car -> car.getName().equals(name))
                .sorted(Comparator.comparing(Car::getName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Car> getOfCategory(CarCategories category) throws RemoteException {
        System.out.println("processando busca por categorias...");
        return DB.values()
                .stream()
                .filter(car -> car.getCategory().equals(category))
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
        DB.remove(renavam);
        DB.put(renavam, car);
        return Boolean.TRUE;
    }
}
