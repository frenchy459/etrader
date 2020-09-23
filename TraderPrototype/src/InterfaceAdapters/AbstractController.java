package InterfaceAdapters;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public abstract class AbstractController {

    protected final String landingMenuFXML = "InterfaceAdapters/LandingMenu.fxml";
    protected final String registerMenuFXML = "InterfaceAdapters/RegisterMenu.fxml";
    protected final String userMenuFXML = "InterfaceAdapters/UserMenu.fxml";

    protected Alert alert = new Alert(Alert.AlertType.NONE);

    /*
     * Loads a new FXML file related to a particular menu screen and then sets it as the new scene
     * It returns the loader, in case any setter/instantiation methods need to be called
     */
    protected FXMLLoader loadFXML(ActionEvent actionEvent, String fxmlFile) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource(fxmlFile));
        try {
            Parent root = loader.load();
            Scene newMenu = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(newMenu);
            stage.show();
        } catch (IOException e) {e.printStackTrace();}
        return loader;
    }

    /*
     * Displays an alert window
     * it returns what button was pressed by the user, useful for CONFIRMATION screens
     */
    protected Optional<ButtonType> showAlert(Alert.AlertType type, String title, String content){
        alert.setAlertType(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
//  ---- This was just to test if visually modifying the alert object was possible, which it is ----
//        DialogPane dialogPane = alert.getDialogPane();
//        dialogPane.getStylesheets().add(getClass().getResource("UserMenu.css").toExternalForm());
//        dialogPane.getStyleClass().add("myDialog");
        return alert.showAndWait();
    }
}
