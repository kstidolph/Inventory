package InventorySystemKelleyStidolph;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * <p>The Main class is a standard entry point to a JavaFX application.</p>
 * <p>FUTURE ENHANCEMENTS: A save and load function could be added here, so that the user does not lose all their data upon exiting the application. The program could save to a text file, or if multiple people need to access the information we could save to a central database. This would also necessitate building in periodic refreshing of the data, as inventory and parts/products available are changed by other users.</p>
 * <p>RUNTIME ERROR: The InventoryFXML.fxml file needs to stay in the folder with the rest of the files for the program. If it does not, the path name needs to be updated. I encountered this issue personally, and felt it was easier to put it in the same folder than to re-specify the file location.</p>
 */

//The Javadocs can be found in their own folder inside the src folder.
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("InventoryFXML.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        Application.launch(args);
    }
}
