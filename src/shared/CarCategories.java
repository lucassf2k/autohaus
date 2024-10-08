package shared;

import java.io.Serializable;
import java.util.function.Function;

public enum CarCategories implements Serializable {
    ECONOMIC, INTERMEDIATE, EXECUTIVE;

    public static String toPTBR(CarCategories carCategory) {
        Function<CarCategories, String> parse = (c) -> {
            if (c.equals(ECONOMIC)) return "Econômico";
            if (c.equals(INTERMEDIATE)) return "Intermediário";
            if (c.equals(EXECUTIVE)) return "Executivo";
            return "Econômico";
        };
        return parse.apply(carCategory);
//        return switch (carCategory) {
//            case ECONOMIC -> "Econômico";
//            case INTERMEDIATE -> "Intermediário";
//            case EXECUTIVE -> "Executivo";
//        };
    }

    public static CarCategories parseToCarCategory(int value) {
        Function<Integer, CarCategories> parseToCategory = (c) -> {
            if (c == 0) return CarCategories.ECONOMIC;
            if (c == 1) return CarCategories.INTERMEDIATE;
            if (c == 2) return  CarCategories.EXECUTIVE;
            return CarCategories.ECONOMIC;
        };
        return parseToCategory.apply(value);
    }
}
