package InterfaceAdapters;

import ApplicationBusinessRules.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LandingMenuController extends AbstractController{

    private UserManager userManager = new UserManager();
    @FXML
    private TextField userName;
    @FXML
    private PasswordField password;
    @FXML
    private Button logInButton;
    @FXML
    private Button registerButton;

    @FXML
    private void requestLogIn(ActionEvent actionEvent) {
        if (userManager.validateLoggedInUser(userName.getText(), password.getText())) {
            UserMenuController umc = loadFXML(actionEvent, userMenuFXML).getController();
            umc.initialize(this.userName.getText(), this.userManager);
        } else {
            showAlert(Alert.AlertType.ERROR,"Error","Invalid username or password");
        }
    }

    @FXML
    private void requestRegister(ActionEvent actionEvent) {
        RegisterController rc = loadFXML(actionEvent, registerMenuFXML).getController();
        rc.initialize(this.userManager);
    }

    public void setUserManager(UserManager um) {this.userManager = um;}

    public void initialize(UserManager userManager){
        this.userManager = userManager;
    }
}
