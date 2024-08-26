package database;

import shared.Car;
import shared.CarCategories;

import java.util.HashMap;
import java.util.Map;

public class ALSql {
    public static final Map<String, Car> DB = new HashMap<>(){{
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
}
