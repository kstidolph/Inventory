package InventorySystemKelleyStidolph;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * This controls the Modify Product screen. It has buttons for adding and removing parts, searching, saving changes, and cancelling changes. Hitting the add part button adds the selected part to the list of parts in the product; hitting the remove part button removes the selected part from that list. The save button saves the changes to the part, and the cancel button returns the user to the main screen without saving any changes. Hitting the search button allows the user to search for parts by ID or partial or full name. Clearing the search bar causes the list of parts to show all parts, not just search results.
 */
public class ModifyProductScreenController {

    @FXML
    private TextField IDField, NameField, InventoryField, MinimumField, MaximumField, PriceField, SearchBar;

    @FXML
    private Button PartSearchButton, AddPartButton, RemovePartButton, SaveProductButton, CancelButton;

    @FXML
    private TableView<Part> ProductChoice;
    @FXML
    private TableColumn<Part, String> PartNameColumn;
    @FXML
    private TableColumn<Part, String> PartIDColumn;

    @FXML
    private TableView<Part> PartsInProduct;
    @FXML
    private TableColumn<Part, String> PartNameInProductColumn;
    @FXML
    private TableColumn<Part, String> PartIDInProductColumn;

    private ObservableList<Part> currentList = FXCollections.observableArrayList();

    /**
     * RUNTIME ERROR: I spent a while troubleshooting why the method to detect when the search bar text changed was never called. After some research, I found that I had simply forgotten to add a listener to the initialization.
     */
    @FXML
    public void initialize() {
        Product passedProduct = Inventory.getCurrProduct();
        for (int i = 0; i < Inventory.getCurrProduct().getAssociatedParts().size(); i++) {
            currentList.add(Inventory.getCurrProduct().getAssociatedParts().get(i));
        }

        IDField.setText(String.valueOf(passedProduct.getID()));
        NameField.setText(passedProduct.getName());
        InventoryField.setText(String.valueOf(passedProduct.getStock()));
        MinimumField.setText(String.valueOf(passedProduct.getMin()));
        MaximumField.setText(String.valueOf(passedProduct.getMax()));
        PriceField.setText(String.valueOf(passedProduct.getPrice()));

        ProductChoice.setItems(Inventory.getAllParts());
        PartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        PartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        PartsInProduct.setItems(currentList);
        PartNameInProductColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        PartIDInProductColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        SearchBar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals("")) {
                    ProductChoice.setItems(Inventory.getAllParts());
                }
            }
        });
    }

    @FXML
    void AddPartButtonClicked(ActionEvent event) {
        if (ProductChoice.getSelectionModel().getSelectedItem() != null) {
            currentList.add(ProductChoice.getSelectionModel().getSelectedItem());
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Please select the part you want to add before hitting Add Part.");
            alert.showAndWait();
        }
    }

    @FXML
    void RemovePartButtonClicked(ActionEvent event) {
        if (PartsInProduct.getSelectionModel().getSelectedItem() != null) {
            currentList.remove(PartsInProduct.getSelectionModel().getSelectedItem());
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Please select the part you want to add before hitting Add Part.");
            alert.showAndWait();
        }
    }

    @FXML
    void SearchButtonPressed(ActionEvent event) {
        //Set up the search
        String query = SearchBar.getText();
        Boolean isnum = true;
        try {
            Integer.parseInt(query);
        } catch (NumberFormatException nfe) {
            isnum = false;
        }

        //Execute the search
        ObservableList<Part> results = FXCollections.observableArrayList(Inventory.lookupPart(query));
        if (isnum) {
            results.add(Inventory.lookupPart(Integer.parseInt(query)));
        }

        //If there were no results...
        if (results.size() == 0) {
            Alert none = new Alert(Alert.AlertType.INFORMATION);
            none.setHeaderText(null);
            none.setContentText("No results found.");
            none.showAndWait();
            return;
        }

        //Display the results
        ProductChoice.setItems(results);
    }


    @FXML
    void SaveButtonClicked(ActionEvent event) {
        //Verify they want to save it; if not, then just return
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to save your changes to the product?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.NO) return;

        try {
            if (Integer.parseInt(MinimumField.getText()) > Integer.parseInt(MaximumField.getText()) ||
                    Integer.parseInt(InventoryField.getText()) > Integer.parseInt(MaximumField.getText()) ||
                    Integer.parseInt(MinimumField.getText()) > Integer.parseInt(InventoryField.getText())) {
                //If they didn't follow the rules
                Alert rulesviolation = new Alert(Alert.AlertType.INFORMATION);
                rulesviolation.setHeaderText(null);
                rulesviolation.setContentText("The minimum must be less than or equal to the maximum, and the inventory must be in between the two.");
                rulesviolation.showAndWait();
                return;
            }

            //Save the product with changes
            Product modified = new Product(Integer.parseInt(IDField.getText()), NameField.getText(), Double.parseDouble(PriceField.getText()), Integer.parseInt(InventoryField.getText()), Integer.parseInt(MinimumField.getText()), Integer.parseInt(MaximumField.getText()));
            Inventory.updateProduct(Inventory.getAllProducts().indexOf(Inventory.getCurrProduct()), modified);
            for (int i = 0; i < currentList.size(); i++) {
                modified.getAssociatedParts().add(currentList.get(i));
            }
        }
        catch (NumberFormatException e) {
            //If they didn't put numbers in the boxes that take numbers
            Alert wrong = new Alert(Alert.AlertType.INFORMATION);
            wrong.setHeaderText(null);
            wrong.setContentText("The minimum, maximum, and inventory must all be whole numbers. Price must also be a number.");
            wrong.showAndWait();
            return;
        }
    }

    @FXML
    void CancelButtonClicked(ActionEvent event) {
        //Verify they want to cancel
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel without saving?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();
        if (confirm.getResult() == ButtonType.NO) return;

        //Go back to the main screen without saving any changes
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("InventorySystemKelleyStidolph/InventoryFXML.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
