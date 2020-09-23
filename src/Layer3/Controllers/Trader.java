package Layer3.Controllers;

import Layer1.Enums.AccountType;
import Layer1.Enums.WindowType;
import Layer3.Repos.*;
import Layer3.Repos.JavaKeyStore.JavaKeyStoreRepo;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.security.KeyStoreException;

/** Represents a trader controller class*/
public class Trader extends AbstractController{

    @FXML
    public Text titleText;
    @FXML
    public JFXButton loginButton;
    @FXML
    public JFXButton registerButton;
    @FXML
    public Button initializeReposButton;

    /**
     * Creates a trader object
     * @throws KeyStoreException a keyStore exception
     */
    //NOTE: this constructor will only ever get called by Main on startup
    public Trader() throws KeyStoreException {
        basicUserRepo = new BasicUserRepo();
        adminUserRepo = new AdminUserRepo();
        itemsRepo = new ItemsRepo();
        transactionsRepo = new TransactionsRepo();
        adminNotificationRepo = new AdminNotificationRepo();
        imageRepo = new ImageRepo();
        demoUserRepo = new DemoUserRepo();
        javaKeyStoreRepo = new JavaKeyStoreRepo();
    }

    /**
     * A javafx button responsible for taking the user to the login page
     * @param actionEvent an actionEvent object
     */
    @FXML
    private void requestLogin(ActionEvent actionEvent) {
      LoginMenu lm = loadFXML(actionEvent, json.getString("loginMenuFXML"), WindowType.SCENE).getController();
      lm.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
              demoUserRepo, javaKeyStoreRepo);
        lm.init();
    }

    /**
     * A javafx button responsible for taking the user to the register page
     * @param actionEvent an actionEvent object
     */
    @FXML
    private void requestRegister(ActionEvent actionEvent) {
        Register rc = loadFXML(actionEvent, json.getString("registerMenuFXML"), WindowType.SCENE).getController();
        rc.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);
        rc.init();
        rc.setAccountType(AccountType.BASIC_USER);
    }
}


