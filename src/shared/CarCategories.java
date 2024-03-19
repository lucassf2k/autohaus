package shared;

import java.io.Serializable;

public enum CarCategories implements Serializable {
    ECONOMIC, INTERMEDIATE, EXECUTIVE;

    public static String toPTBR(CarCategories carCategory) {
        return switch (carCategory) {
            case ECONOMIC -> "Econômico";
            case INTERMEDIATE -> "Intermediário";
            case EXECUTIVE -> "Executivo";
        };
    }
}
