package InventorySystemKelleyStidolph;

import javafx.collections.*;

/**
 * The Product class contains a list of the parts that are associated with the product, and the product's ID, name, price, amount currently in stock, minimum stock, and maximum stock.
 */
public class Product {
    private ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public void setID(int newID) {
        id = newID;
    }
    public void setName(String newName) {
        name = newName;
    }
    public void setPrice(double newPrice) {
        price = newPrice;
    }
    public void setStock(int newStock) {
        stock = newStock;
    }
    public void setMin(int newMin) {
        min = newMin;
    }
    public void setMax(int newMax) {
        max = newMax;
    }

    public int getID() {
        return id;
    }
    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
    public int getStock() {
        return stock;
    }
    public int getMin() {
        return min;
    }
    public int getMax() {
        return max;
    }

    /**
     * RUNTIME ERROR: I made the mistake here of using the new keyword when instantiating the product. This led to not being able to add parts to the product, and was fixed by removing the new keyword so the program simply instantiated the list instead of creating a new one.
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.setID(id);
        this.setName(name);
        this.setPrice(price);
        this.setStock(stock);
        this.setMin(min);
        this.setMax(max);
        Inventory.addProduct(this);
        associatedParts = FXCollections.observableArrayList();
    }

    public void addAssociatedPart(Part newPart) {
        associatedParts.add(newPart);
    }

    public boolean deleteAssociatedPart(Part deleteThis) {
        for (int i = 0; i<associatedParts.size(); i++) {
            if (associatedParts.get(i) == deleteThis) {
                associatedParts.remove(deleteThis);
                return true;
            }
        }
        return false;
    }
    public ObservableList<Part> getAssociatedParts() {
        return associatedParts;
    }

    public void setAssociatedParts(ObservableList<Part> newList) {
        associatedParts = newList;
    }
}
