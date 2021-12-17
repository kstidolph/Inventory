package InventorySystemKelleyStidolph;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLOutput;

/**
 * This class manages the main screen, in which users can search for parts or products, add parts or products, select parts or products within the tables displayed, and modify selected parts or products.
 */
public class MainScreenController {

    @FXML
    private TableView<Part> partTable;
    @FXML
    private TableColumn<Part, String> PartID;
    @FXML
    private TableColumn<Part, String> PartName;
    @FXML
    private TableColumn<Part, String> PartAmount;
    @FXML
    private TableColumn<Part, String> PartCost;
    @FXML
    private TableView<Product> ProductTable = new TableView<Product>(Inventory.getAllProducts());
    @FXML
    private TableColumn<Product, String> ProductID;
    @FXML
    private TableColumn<Product, String> ProductName;
    @FXML
    private TableColumn<Product, String> ProductAmount;
    @FXML
    private TableColumn<Product, String> ProductCost;

    @FXML
    private MenuItem Close, About, FutureUpdates;

    @FXML
    private Button AddPartButton, ModifyPartButton, DeletePartButton, AddProductButton, ModifyProductButton, DeleteProductButton, ExitButton, SearchPartsButton, SearchProductsButton;

    @FXML
    private TextField PartSearchField, ProductSearchField;

    @FXML
    private void initialize() {
        PartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        PartAmount.setCellValueFactory(new PropertyValueFactory<>("stock"));
        PartCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        partTable.setItems(Inventory.getAllParts());

        ProductID.setCellValueFactory(new PropertyValueFactory<>("iD"));
        ProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        ProductAmount.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ProductCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        ProductTable.setItems(Inventory.getAllProducts());

        PartSearchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals("")) {
                    partTable.setItems(Inventory.getAllParts());
                }
            }
        });

        ProductSearchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals("")) {
                    ProductTable.setItems(Inventory.getAllProducts());
                }
            }
        });
    }

    @FXML
    void AboutPressed(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(null);
        alert.setContentText("This program is meant to keep track of \n" +
                "the number of each part in inventory, and what\n" +
                "we need to make each of our products." );
        alert.showAndWait();
    }

    @FXML
    void AddPartPressed(ActionEvent event) {
        //Load up the Add Part screen!
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader();
            root = loader.load(getClass().getClassLoader().getResource("InventorySystemKelleyStidolph/AddPartScreen.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void AddProductPressed(ActionEvent event) {
        //Load up the Add Product screen!
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader();
            root = loader.load(getClass().getClassLoader().getResource("InventorySystemKelleyStidolph/AddProductScreen.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ClosePressed(ActionEvent event) {
        //Verify they want to leave
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit the program?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.NO) return;

        //Close out of the program
        Stage stage = (Stage) ExitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void DeletePartPressed(ActionEvent event) {
        //Verify they want to delete
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the selected part?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.NO) return;

        Inventory.deletePart(partTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    void DeleteProductPressed(ActionEvent event) {
        //If the product they want to delete has parts attached to it...
        if (ProductTable.getSelectionModel().getSelectedItem().getAssociatedParts().size() != 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("You cannot delete this product right now; there are parts associated with it. Use Modify Product to remove the parts first.");
            alert.showAndWait();
            return;
        }

        //Verify they want to delete
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the selected product?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.NO) return;

        Inventory.deleteProduct(ProductTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    void ExitPressed(ActionEvent event) {
        //Verify they want to exit
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit the program?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.NO) return;

        //Close out of the program
        Stage stage = (Stage) ExitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void FutureUpdatesPressed(ActionEvent event) {
        //Displays a simple message box
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Future Updates Planned");
        alert.setHeaderText(null);
        alert.setContentText("Future updates include:\n" +
            "A save function,\n" +
            "A load function,\n" +
            "A warning message when a part is not associated with any products,\n" +
            "The ability to add a product description,\n" +
            "The ability to add images of a product, and\n" +
            "A function to generate reports.\n\n" +
            "Please email suggestions for additional features to:\n" +
            "kelley.stidolph@gmail.com\n" +
            "Thank you!" );
        alert.showAndWait();
    }

    @FXML
    void ModifyPartPressed(ActionEvent event) {
        //If there's no part selected, just alert the user
        if (partTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Please select a part before clicking Modify Part.");
            alert.showAndWait();
            return;
        }
        
        //If there's some part selected, load up the Modify Part screen
        try {
            //Save the part and load the screen
            Inventory.setCurrPart(partTable.getSelectionModel().getSelectedItem());
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getClassLoader().getResource("InventorySystemKelleyStidolph/ModifyPartScreen.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ModifyProductPressed(ActionEvent event) {
        //If there's no product selected, just alert the user
        if (ProductTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Please select a product before clicking Modify Product.");
            alert.showAndWait();
            return;
        }

        //If there's some product selected, load up the Modify Product screen
        try {
            Inventory.setCurrProduct(ProductTable.getSelectionModel().getSelectedItem());
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getClassLoader().getResource("InventorySystemKelleyStidolph/ModifyProductScreen.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * RUNTIME ERROR: When writing this search, I spent some time investigating why it seemed to return all parts, no matter what I searched for. It turned out that I had mixed up parts and products, and had been searching for and filtering the table for products instead of parts, leaving the parts table changed. Lessons learned - do not try to code at 2 in the morning, and choose class names that are easily distinguished at a glance.
     * @param event
     */
    @FXML
    void SearchPartsButton(ActionEvent event) {
        //Set up the search
        String query = PartSearchField.getText();
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
        partTable.setItems(results);
    }

    @FXML
    void SearchProductsButton(ActionEvent event) {
        //Set up the search
        String query = ProductSearchField.getText();
        Boolean isnum = true;
        try {
            Integer.parseInt(query);
        } catch (NumberFormatException nfe) {
            isnum = false;
        }

        //Execute the search
        ObservableList<Product> results = FXCollections.observableArrayList(Inventory.lookupProduct(query));
        if (isnum) {
            results.add(Inventory.lookupProduct(Integer.parseInt(query)));
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
        ProductTable.setItems(results);
    }
}
