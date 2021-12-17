package InventorySystemKelleyStidolph;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class controls the Modify Part screen, allowing the users to alter a part's name, the amount in stock, the minimum and maximum of stock the company can have, the price, and (depending on whether it's in-house or outsourced) either the ID of the machine that makes it or the name of the company that makes it.
 */
public class ModifyPartScreenController {

    @FXML
    private RadioButton InHouseRadio, OutsourcedRadio;
    @FXML
    private TextField PartID, PartName, PartAmount, PartMin, PartMax, PartPrice, PartMachineID;
    @FXML
    private Button ModifyButton, CancelButton;
    @FXML
    private Label MachOrCompName;


    //This fills out the form with the initial information.
    public void initialize() {
        PartID.setText(String.valueOf(Inventory.getCurrPart().getId()));
        PartName.setText(Inventory.getCurrPart().getName());
        PartAmount.setText(String.valueOf(Inventory.getCurrPart().getStock()));
        PartMin.setText(String.valueOf(Inventory.getCurrPart().getMin()));
        PartMax.setText(String.valueOf(Inventory.getCurrPart().getMax()));
        PartPrice.setText(String.valueOf(Inventory.getCurrPart().getPrice()));

        if (Inventory.getCurrPart() instanceof InHouse) {
            PartMachineID.setText(String.valueOf(((InHouse) Inventory.getCurrPart()).getMachineID()));
            InHouseRadio.setSelected(true);
        } else if (Inventory.getCurrPart() instanceof Outsourced) {
            PartMachineID.setText(((Outsourced) Inventory.getCurrPart()).getCompanyName());
            OutsourcedRadio.setSelected(true);
        }

    }

    /**
     * RUNTIME ERROR: I originally tried to just modify the information of the part, by casting the modified part to the type of object the user had selected. However, that solution (or at least the way I tried to implement it) was overly-complicated and filled with mistakes. It was a much easier solution to just remove the old part, and replace it with a new one with the correct information.
     * @param event
     */
    @FXML
    void ModifyButtonClicked(ActionEvent event) {
        //Verify they want to save
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to save your changes to this part?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.NO) return;

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

            //Create a new Part with the desired attributes and put it in the array
            //Note that we do not just update the part because it may change from In House to Outsourced or vice versa
            //This route uses a bit more memory, but the code is simpler than checking to see if the part type is changing
            if (InHouseRadio.isSelected()) {
                InHouse correctPart = new InHouse(Integer.parseInt(PartID.getText()), PartName.getText(), Double.parseDouble(PartPrice.getText()), Integer.parseInt(PartAmount.getText()),
                        Integer.parseInt(PartMin.getText()), Integer.parseInt(PartMax.getText()), Integer.parseInt(PartMachineID.getText()));
                Inventory.updatePart(Integer.parseInt(PartID.getText()), correctPart);
            } else {
                Outsourced correctPart = new Outsourced(Integer.parseInt(PartID.getText()), PartName.getText(), Double.parseDouble(PartPrice.getText()), Integer.parseInt(PartAmount.getText()),
                        Integer.parseInt(PartMin.getText()), Integer.parseInt(PartMax.getText()), PartMachineID.getText());
                Inventory.updatePart(Integer.parseInt(PartID.getText()), correctPart);
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

        //Go back to the main screen after saving changes
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
    void CancelButtonClicked(ActionEvent event) {
        //Verify they want to cancel
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to return without saving changes?", ButtonType.YES, ButtonType.NO);
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

    //These update the title of the text field for the machine ID (for in-house parts) or the company name (for outsourced parts).
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

}
