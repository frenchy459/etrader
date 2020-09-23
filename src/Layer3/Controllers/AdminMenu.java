package Layer3.Controllers;

import Layer1.Enums.AccountType;
import Layer1.Enums.WindowType;
import Layer2.Managers.AdminManager;
import Layer2.Managers.AdminNotificationManager;
import Layer2.Managers.ThresholdManager;
import Layer2.Managers.UserManager;
import Layer3.Repos.InitializeRepos;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;

import java.security.KeyStoreException;
import java.util.Optional;

/**
 * A controller class handling user input for calling various administrative options in the admin menu
 */
public class AdminMenu extends AbstractController{
    @FXML
    public JFXButton logOutButton;
    @FXML
    public JFXButton newAdminButton;
    @FXML
    public JFXButton changePasswordButton;
    @FXML
    public JFXButton globalThreshButton;
    @FXML
    public JFXButton modUsersButton;
    @FXML
    public JFXButton approveItemsButton;
    @FXML
    public JFXButton initializeRepos;
    @FXML
    private Button adminNotificationButton;
    @FXML
    private Text userName;
    @FXML
    private Button deleteAccountButton;

    protected final ThresholdManager thresholdManager = new ThresholdManager();
    private final AdminManager adminManager = new AdminManager();
    private AdminNotificationManager adminNotificationManager;
    private UserManager userManager;


    /**
     * An button for admin log out
     * @param actionEvent an actionEvent object
     */
    @FXML
    public void requestLogOut(ActionEvent actionEvent) {
        //builds an object from the Alert object that can listen for & output the button that was pressed
        //necessary for confirmation screens
        Optional<ButtonType> selected = showAlert(Alert.AlertType.CONFIRMATION,
                    json.getString("sceneConfirmTitle"), json.getString("logOut"));
        if (selected.isPresent() && selected.get() == ButtonType.OK) {
            Trader trader =
                    loadFXML(actionEvent, json.getString("landingMenuFXML"), WindowType.SCENE).getController();
            trader.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                    demoUserRepo, javaKeyStoreRepo);
        }
    }

    /**
     * Initialize the admin menu
     * @param um an userManager object
     */
    public void init(UserManager um){
        adminNotificationManager = new AdminNotificationManager(adminNotificationRepo, itemsRepo);
        this.userManager = um;
        userName.setText(userManager.getCurrentUser());
        setNotificationNumber();
        //removes the delete account button if you're using the first admin account
        if (userName.getText().equals(AccountType.ADMIN.toString().toLowerCase())) {
            deleteAccountButton.setDisable(true);
        }
    }

    @FXML
    private void initializeRepos(ActionEvent event) throws KeyStoreException {
        String path = json.getString("encryptDecryptPath");
        InitializeRepos initializeRepos = new InitializeRepos(basicUserRepo, itemsRepo, transactionsRepo, adminUserRepo,
                javaKeyStoreRepo, path);
        initializeRepos.run();
    }

    private void setNotificationNumber() {
        String currentText = adminNotificationButton.getText();
        adminNotificationButton.setText(currentText + " (" + adminNotificationRepo.getAll().size() + ")");
    }

    /**
     * A javafx button responsible for taking user to the new admin menu page
     * @param actionEvent an actionEvent object
     */
    @FXML
    public void controlNewAdmin(ActionEvent actionEvent) {
        Register rc = loadFXML(actionEvent, json.getString("registerMenuFXML"), WindowType.SCENE).getController();
        rc.setRepos(basicUserRepo, adminUserRepo,itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);
        rc.setAccountType(AccountType.ADMIN);
        rc.setUserManager(this.userManager);
    }

    /**
     * A javafx button responsible for taking user to the approve item page
     * @param actionEvent an actionEvent object
     */
    @FXML
    public void controlApproveItem(ActionEvent actionEvent) {
        ApproveItemMenu aim =
                loadFXML(actionEvent, json.getString("approveItemMenuFXML"), WindowType.SCENE).getController();
        aim.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);
        aim.init(userManager, adminNotificationManager);
    }

    /**
     * A javafx button responsible for taking user to the control threshold page
     * @param actionEvent an actionEvent object
     */
    @FXML
    public void controlThreshold(ActionEvent actionEvent) {
        ThresholdMenu tm =
                loadFXML(actionEvent, json.getString("thresholdMenuFXML"), WindowType.SCENE).getController();
        tm.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);

        tm.init(userManager, thresholdManager);
    }

    /**
     * A javafx button responsible for taking user to the change password page
     */
    @FXML
    public void requestChangePassword() throws KeyStoreException {
        TextInputDialog input = new TextInputDialog(json.getString("passwordInputPrompt"));
        input.setTitle(json.getString("passwordSceneTitle"));
        input.setHeaderText(json.getString("passwordHeaderText"));
        input.setContentText(json.getString("passwordContentText"));

        Optional<String> result = input.showAndWait();
        if (result.isPresent()) {
            if (result.get().length() < 2) {
                showAlert(Alert.AlertType.ERROR,json.getString("sceneErrorTitle"),
                        json.getString("passwordTooShort"));
            }
            else {
                adminManager.adminPasswordReset(result.get(), userManager.getCurrentUser(), adminUserRepo,
                        javaKeyStoreRepo, json.getString("encryptDecryptPath"));
                showAlert(Alert.AlertType.CONFIRMATION,json.getString("sceneSuccessTitle"),
                        json.getString("passwordChanged"));

            }
        }
    }

    /**
     * A javafx button responsible for taking user to the delete account menu page
     * @param actionEvent an actionEvent object
     */
    @FXML
    public void requestDeleteAccount(ActionEvent actionEvent) {
        Optional<ButtonType> selected = showAlert(Alert.AlertType.CONFIRMATION,
                    json.getString("sceneConfirmTitle"), json.getString("deleteAccount"));
        if (selected.isPresent() && selected.get() == ButtonType.OK) {
            adminUserRepo.remove(userManager.getCurrentUser());
            Trader trader =
                    loadFXML(actionEvent, json.getString("landingMenuFXML"), WindowType.SCENE).getController();
            trader.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                    demoUserRepo, javaKeyStoreRepo);
        }
    }

    /**
     * A javafx button responsible for taking user to the control admin users menu page
     * @param actionEvent an actionEvent object
     */
    @FXML
    public void controlAdminUsers(ActionEvent actionEvent) {
        AdminBrowseUsersMenu aum =
                loadFXML(actionEvent, json.getString("adminBrowseUsersMenuFXML"), WindowType.SCENE).getController();
        aum.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);
        aum.init(userManager);
    }

    /**
     * A javafx button responsible for taking user to the control admin notification menu page
     * @param actionEvent an actionEvent object
     */
    @FXML
    public void controlAdminNotification(ActionEvent actionEvent){
        AdminNotificationMenu anm = loadFXML(
                actionEvent, json.getString("adminNotificationMenuFXML"), WindowType.SCENE).getController();
        anm.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);
        anm.init(userManager);
    }
}
