package Layer3.Controllers;

import Layer1.Entities.BasicUser;
import Layer1.Entities.User;
import Layer1.Enums.WindowType;
import Layer2.Managers.LoginManager;
import Layer2.Managers.UserManager;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;

/** Represents a trader controller class*/
public class LoginMenu extends AbstractController{

    @FXML
    public JFXButton backButton;
    @FXML
    public JFXButton logInButton;
    @FXML
    public JFXButton demoUserButton;
    @FXML
    private TextField userName;
    @FXML
    private PasswordField password;


    //NOTE: this constructor will only ever get called by Main on startup
    public void init() {
        userName.requestFocus();
    }

    /**
     * A javafx button responsible for user login
     * @param actionEvent an actionEvent object
     */
    @FXML
    private void requestLogIn(ActionEvent actionEvent){
        LoginManager loginManager = new LoginManager();
        //checks user's credentials
        User currentUser = loginManager.validateLoggedInUser(userName.getText(), password.getText(),
                basicUserRepo, adminUserRepo, javaKeyStoreRepo, json.getString("encryptDecryptPath"));

        //if the user exists, logs in as user or admin depending on account type, otherwise error
        if (currentUser != null) {
            UserManager userManager = new UserManager(currentUser,
                    basicUserRepo, itemsRepo, demoUserRepo);
            if (currentUser instanceof BasicUser) {
                ((BasicUser) userManager.getCurrentUserObject()).setLastLogin(LocalDateTime.now());
                Layer3.Controllers.UserMenu um = loadFXML(actionEvent, json.getString("userMenuFXML"), WindowType.SCENE).getController();
                um.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                        demoUserRepo, javaKeyStoreRepo);
                um.init(userManager);
            } else {
                AdminMenu am = loadFXML(actionEvent, json.getString("adminMenuFXML"), WindowType.SCENE).getController();
                am.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                        demoUserRepo, javaKeyStoreRepo);
                am.init(userManager);
            }
        } else {
            showAlert(Alert.AlertType.ERROR,json.getString("sceneErrorTitle"),json.getString("invalidLogin"));
        }
    }

    /**
     * A javafx button responsible for taking user to the previous page
     * @param actionEvent an actionEvent object
     */
    @FXML
    private void requestBack(ActionEvent actionEvent){
        Trader trader = loadFXML(actionEvent, json.getString("landingMenuFXML"), WindowType.SCENE).getController();
        trader.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);
    }

    /**
     * A javafx button responsible for demo user login
     * @param actionEvent an actionEvent object
     */
    @FXML
    private void requestDemoLogin(ActionEvent actionEvent) {
        demoUserRepo.generate(itemsRepo, transactionsRepo);
        UserManager userManager = new UserManager(demoUserRepo.get("demo1"), basicUserRepo, itemsRepo,
                demoUserRepo);
        ((BasicUser) userManager.getCurrentUserObject()).setLastLogin(LocalDateTime.now());
        UserMenu um = loadFXML(actionEvent, json.getString("userMenuFXML"), WindowType.SCENE).getController();
        um.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);
        um.init(userManager);
    }

}


