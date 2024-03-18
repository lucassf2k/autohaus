package shared;

import java.io.Serializable;

public class Car implements Serializable {
    private String revavam;
    private String name;
    private CarCategories category;
    private String yearManufacture;
    private Double price;

    public Car(String revavam, String name, CarCategories category, String yearManufacture, Double price) {
        this.revavam = revavam;
        this.name = name;
        this.category = category;
        this.yearManufacture = yearManufacture;
        this.price = price;
    }

    public String getRevavam() {
        return revavam;
    }

    public void setRevavam(String revavam) {
        this.revavam = revavam;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CarCategories getCategory() {
        return category;
    }

    public void setCategory(CarCategories category) {
        this.category = category;
    }

    public String getYearManufacture() {
        return yearManufacture;
    }

    public void setYearManufacture(String yearManufacture) {
        this.yearManufacture = yearManufacture;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Car{" +
                "revavam='" + revavam + '\'' +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", yearManufacture='" + yearManufacture + '\'' +
                ", price=" + price +
                '}';
    }
}
