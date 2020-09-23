package InterfaceAdapters;

import ApplicationBusinessRules.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController extends AbstractController {

    @FXML
    private TextField userName;
    @FXML
    private PasswordField password;
    @FXML
    private TextField locationInput;
    @FXML
    private Button RegisterButton;
    @FXML
    private Button returnButton;

    private UserManager um;

    public void initialize(UserManager um){
        this.um = um;
    }

    @FXML
    public void registerNewAccount(ActionEvent actionEvent) {
        if (!um.registerUsernameExists(userName.getText())) {
            um.registerUser( userName.getText(), password.getText());
            showAlert(Alert.AlertType.INFORMATION,"Success", "Your account has been registered");
        }
        else{
            showAlert(Alert.AlertType.ERROR,"Error", "Username already exists");
        }
    }

    @FXML
    public void returnToPreviousMenu(ActionEvent actionEvent){
       LandingMenuController lmc = loadFXML(actionEvent, landingMenuFXML).getController();
        lmc.initialize(this.um);
    }
}
