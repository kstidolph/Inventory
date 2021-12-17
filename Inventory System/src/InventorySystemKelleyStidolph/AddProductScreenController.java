package InventorySystemKelleyStidolph;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.*;
import java.io.IOException;

/**
 * This class controls the Add Product screen. It allows the user to enter the product's name, the amount in inventory, the minimum and maximum inventory, and the price, as well as to search for parts to add to the product.
 */
public class AddProductScreenController {

    @FXML
    private TextField IDField, NameField, InventoryField, MinimumField, MaximumField, PriceField, SearchBar;

    @FXML
    private Button PartSearchButton, AddPartButton, RemovePartButton, SaveProductButton, CancelButton;

    @FXML
    private TableView<Part> ProductChoice;
    @FXML
    private TableColumn<Part, String> PartInInventoryColumn;
    @FXML
    private TableColumn<Part, String> PartInInventoryIDColumn;

    @FXML
    private TableView<Part> PartsInProduct;
    @FXML
    private TableColumn<Part, String> PartInProductNamesColumn;
    @FXML
    private TableColumn<Part, String> PartInProductIDColumn;

    private ObservableList<Part> currentParts = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        IDField.setEditable(true);
        IDField.setText(String.valueOf(Inventory.getNumProducts()));
        IDField.setEditable(false);

        PartsInProduct.setItems(currentParts);
        PartInProductNamesColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        PartInProductIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        ProductChoice.setItems(Inventory.getAllParts());
        PartInInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        PartInInventoryIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    }

    @FXML
    void AddPartButtonClicked(ActionEvent event) {
        if (ProductChoice.getSelectionModel().getSelectedItem() != null) {
            currentParts.add(ProductChoice.getSelectionModel().getSelectedItem());
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Please select the part you want to add before hitting Add Part.");
            alert.showAndWait();
        }
    }

    @FXML
    void CancelClicked(ActionEvent event) {
        //Verify they want to cancel; if not, then just stay
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Cancel making this new product?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.NO) return;

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

    @FXML
    void RemovePartButtonClicked(ActionEvent event) {
        if (ProductChoice.getSelectionModel().getSelectedItem() != null) {
            currentParts.remove(ProductChoice.getSelectionModel().getSelectedItem());
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Please select the part you want to remove before hitting Remove Part.");
            alert.showAndWait();
        }
    }

    @FXML
    void SaveProductButtonClicked(ActionEvent event) {
        //Verify they want to save it; if not, then just return
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to save the product?", ButtonType.YES, ButtonType.NO);
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

            //Save the new product
            Product product = new Product(Integer.parseInt(IDField.getText()), NameField.getText(), Double.parseDouble(PriceField.getText()), Integer.parseInt(InventoryField.getText()), Integer.parseInt(MinimumField.getText()), Integer.parseInt(MaximumField.getText()));
            for (int i = 0; i < currentParts.size(); i++) {
                product.addAssociatedPart(currentParts.get(i));
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

        //Go back to the main screen
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

    /**
     *RUNTIME ERROR: The first time I wrote this code, I wrote it as though the part search by integer was a list as well, and wrote to create two lists then add the contents of one to the other. Needless to say, that did not compile let alone run properly. This was quickly fixed by correcting the code to add the single instance returned by the integer search to the already-created list. If the integer search were altered to find multiple instances (i.e. all parts with IDs that contain 101), then my prior method would be appropriate.
     * @param event
     */
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
}
