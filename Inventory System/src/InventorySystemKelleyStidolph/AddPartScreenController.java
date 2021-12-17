package InventorySystemKelleyStidolph;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * This class controls the Add Part screen, where users can specify the name, amount in stock, minimum stock, maximum stock, and price of parts, as well as the name of the company that makes outsourced parts or the number of the machine that makes in-house parts.
 */
public class AddPartScreenController {

    @FXML
    private RadioButton InHouseRadio, OutsourcedRadio;
    @FXML
    private TextField PartID, PartName, PartAmount, PartMin, PartMax, PartPrice, PartMachineID;
    @FXML
    private Button AddButton, CancelButton;
    @FXML
    private Label MachOrCompName;

    @FXML
    public void initialize() {
        InHouseRadio.setSelected(true);
        PartID.setText(String.valueOf(Inventory.getNumParts()));
    }

    @FXML
    void InHouseSelected(ActionEvent event) {
        MachOrCompName.setText("Machine ID");
        OutsourcedRadio.setSelected(false);
    }

    @FXML
    void OutsourcedSelected(ActionEvent event) {
        MachOrCompName.setText("Company");
        InHouseRadio.setSelected(false);
    }

    /**
     * RUNTIME ERROR: I initially tried to check whether minimum was less than maximum etc. outside the try-catch block to catch non-numeric inputs, but quickly found that made my program crash whenever there was a letter entered into the min/max/stock boxes. I fixed that issue by placing the rule-checking within the try-catch block.
     * @param event
     */
    @FXML
    void AddPartButtonClicked(ActionEvent event) {
        try {
            //If they broke the rules...
            if (Integer.parseInt(PartMin.getText()) > Integer.parseInt(PartMax.getText()) ||
                    Integer.parseInt(PartAmount.getText()) > Integer.parseInt(PartMax.getText()) ||
                    Integer.parseInt(PartMin.getText()) > Integer.parseInt(PartAmount.getText())) {
                Alert rulesviolation = new Alert(Alert.AlertType.INFORMATION);
                rulesviolation.setHeaderText(null);
                rulesviolation.setContentText("The minimum must be less than or equal to the maximum, and the inventory must be in between the two.");
                rulesviolation.showAndWait();
                return;
            }

            //Verify they want to save it; if not, then just return
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to save this part?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.NO) return;

            //Add the part, either as In House or as Outsourced
            if (InHouseRadio.isSelected()) {
                Part newPart = new InHouse(Inventory.getNumParts(), PartName.getText(), Double.parseDouble(PartPrice.getText()), Integer.parseInt(PartAmount.getText()),
                        Integer.parseInt(PartMin.getText()), Integer.parseInt(PartMax.getText()), Integer.parseInt(PartMachineID.getText()));

                //Close this window
                Parent root;
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("InventorySystemKelleyStidolph/InventoryFXML.fxml"));
                    root = loader.load();
                    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (OutsourcedRadio.isSelected()) {
                new Outsourced(Inventory.getNumParts(), PartName.getText(), Double.parseDouble(PartPrice.getText()), Integer.parseInt(PartAmount.getText()),
                        Integer.parseInt(PartMin.getText()), Integer.parseInt(PartMax.getText()), PartMachineID.getText());
                //Close this window
                Parent root;
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("InventorySystemKelleyStidolph/InventoryFXML.fxml"));
                    root = loader.load();
                    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setHeaderText(null);
                alert2.setContentText("Please select in house or outsourced for this part.");
                alert2.showAndWait();
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel this new part?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.NO) return;

        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("InventorySystemKelleyStidolph/InventoryFXML.fxml"));
            root = loader.load();
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
