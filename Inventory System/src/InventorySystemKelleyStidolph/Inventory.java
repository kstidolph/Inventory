package InventorySystemKelleyStidolph;

import java.util.*;
import javafx.collections.*;

/**
 * This class can be thought of as the backbone of the program. Inventory maintains the lists of all parts and products.
 */
public class Inventory {
    private static ArrayList<Part> baseparts = new ArrayList<Part>();
    private static ArrayList<Product> baseproducts = new ArrayList<Product>();

    public static int numParts = 0;
    public static int numProducts = 0;

    private static ObservableList<Part> allParts = FXCollections.observableArrayList(baseparts);
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList(baseproducts);

    public static Part currPart;
    public static Product currProduct;

    public static Part getCurrPart() {return currPart;}
    public static Product getCurrProduct() {return currProduct;}
    public static void setCurrPart(Part newPart) {currPart = newPart;}
    public static void setCurrProduct(Product newProduct) {currProduct = newProduct;}

    /**
     * RUNTIME ERROR: Part IDs need to be unique. At first I was trying to make them contiguous - when you deleted a part, its ID became available for use. However, this quickly proved cumbersome, and led to more than one duplicate ID due to off-by-one errors when counting and extra complications trying to store the unused IDs. In the end, it was much easier to just keep a counter and not worry about the prior Part IDs.
     * @param newPart
     */
    public static void addPart(Part newPart) { //Adds part to list
        numParts++;
        allParts.add(newPart);
    }

    public static void addProduct(Product newProduct) { //Adds product to list
        numProducts++;
        allProducts.add(newProduct);
    }

    public static int getNumParts() {
        return numParts;
    }
    public static int getNumProducts() {
        return numProducts;
    }

    public static Part lookupPart(int findID) { //looks up part by ID, only one result
        for (int i=0; i<allParts.size(); i++) {
            if (allParts.get(i).getId() == findID) return allParts.get(i);
        }
        return null;
    }

    public static ObservableList<Part> lookupPart(String partialName) { //looks up part by part of name, potentially many results
        ArrayList<Part> resultsarray = new ArrayList<Part>();
        for (int i=0; i<allParts.size(); i++) {
            if (allParts.get(i).getName().contains(partialName)) resultsarray.add(allParts.get(i));
        }
        ObservableList<Part> results = FXCollections.observableArrayList(resultsarray);
        return results;
    }

    public static Product lookupProduct(int findID) { //looks up product by ID, only one result
        for (int i=0; i<allProducts.size(); i++) {
            if (allProducts.get(i).getID() == findID) return allProducts.get(i);
        }
        return null;
    }

    public static ObservableList<Product> lookupProduct(String partialName) { //looks up product by part of name, potentially many results
        ArrayList<Product> resultsarray = new ArrayList<Product>();
        for (int i=0; i<allProducts.size(); i++) {
            if (allProducts.get(i).getName().contains(partialName)) resultsarray.add(allProducts.get(i));
        }
        ObservableList<Product> results = FXCollections.observableArrayList(resultsarray);
        return results;
    }

    public static void updatePart(int index, Part newInfo) {
        allParts.remove(index);
        allParts.set(index, newInfo);
    }

    public static void updateProduct(int index, Product newInfo) {
        allProducts.remove(index);
        allProducts.set(index, newInfo);
    }

    public static boolean deletePart(Part unwantedPart) {
        if (allParts.contains(unwantedPart)) {
            allParts.remove(unwantedPart);
            return true;
        }
        else return false;
    }

    public static boolean deleteProduct(Product unwantedProduct) {
        if (allProducts.contains(unwantedProduct)) {
            allProducts.remove(unwantedProduct);
            return true;
        }
        else return false;
    }

    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
