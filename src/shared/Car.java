package shared;

import java.io.Serializable;

public record Car(
        String revavam,
        String name,
        CarCategories category,
        String yearManufacture,
        Double price) implements Serializable {
}
