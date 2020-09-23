package Layer3.Controllers;

import Layer1.Entities.BasicUser;
import Layer1.Enums.AccountStanding;
import Layer1.Enums.AccountStatus;
import Layer1.Enums.AccountType;
import Layer1.Enums.WindowType;
import Layer2.API.MapAPI.errors.ApiException;
import Layer2.Managers.AccountStatusManager;
import Layer2.Managers.AccountStateManager;
import Layer2.Managers.ThresholdManager;
import Layer2.Managers.UserManager;
import Layer2.Managers.*;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Optional;

/**
 * A controller class which allow handling of basic user's profiles by the current admin user
 */
public class AdministrateUserMenu extends AbstractController {

    @FXML
    public JFXButton backButton;
    @FXML
    public Text currentCity;
    @FXML
    public Text userNameField;
    @FXML
    public Text lastLoginField;
    @FXML
    public Text standingField;
    @FXML
    public Text statusField;
    @FXML
    public JFXButton userProfileButton;
    @FXML
    public JFXButton deleteUserButton;
    @FXML
    public JFXButton wishlistButton;
    @FXML
    public JFXButton inventoryButton;
    @FXML
    public JFXButton transactHistButton;
    @FXML
    public JFXButton freezeUnfreezeButton;
    @FXML
    public JFXButton inactiveStateButton;
    @FXML
    public JFXButton thresholdButton;
    @FXML
    public JFXButton undoDeletedItemButton;
    @FXML
    private Button limitedAttributeButton;
    @FXML
    private Text userName;
    @FXML
    private Text userState;
    @FXML
    private Text userStatus;
    @FXML
    private Text lastLogin;
    @FXML
    private ImageView userImage;

    private BasicUser basicUser;
    private UserManager userManager;
    private BasicUserManager basicUserManager;
    private final AccountStatusManager accountStatusManager = new AccountStatusManager();
    private final AccountStateManager accountStateManager = new AccountStateManager();
    private final ThresholdManager thresholdManager = new ThresholdManager();

    /**
     * Initialize the administrate user menu
     * @param um an UserManager object
     * @param basicUser a basic user entity
     */
    public void init(UserManager um, BasicUser basicUser) {
        this.basicUser = basicUser;
        this.userManager = um;
        this.basicUserManager = new BasicUserManager(adminNotificationRepo);
        userName.setText(basicUser.getUsername());
        if (basicUser.getLastLogin() == null) {
            lastLogin.setText("N/A");
        }
        else {
            lastLogin.setText(basicUser.getLastLogin());
        }
        userState.setText(accountStateManager.getAccountState(this.basicUser).toString());
        userStatus.setText(accountStatusManager.getAccountStatus(this.basicUser).toString());
        setImage(userImage, basicUser);

        if (!accountStateManager.getAccountState(basicUser).equals(AccountStanding.LIMITED)) {
            limitedAttributeButton.setDisable(true);
            limitedAttributeButton.setVisible(false);
        }
    }

    /**
     * redirects admin to the the basic user's user profile
     * @param actionEvent an actionEvent object
     * @throws InterruptedException an interrupted exception
     * @throws ApiException an map API exception
     * @throws IOException an IO exception
     */
    @FXML
    public void controlUserProfile(ActionEvent actionEvent) throws InterruptedException, ApiException, IOException {
        UserProfileViewMenu userProfileViewMenu =
                loadFXML(actionEvent, json.getString("userProfileViewMenuFXML"), WindowType.POPUP).getController();
        userProfileViewMenu.setRepos(
                basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);
        userProfileViewMenu.init(basicUser);
    }

    /**
     * redirects admin to the transaction history menu of the basic user
     * @param actionEvent an actionEvent object
     */
    @FXML
    public void controlTransactionHistory(ActionEvent actionEvent) {
        TransactionHistoryMenu thm = loadFXML(actionEvent, json.getString("transactionHistoryMenuFXML"),
                WindowType.SCENE).getController();
        thm.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);
        thm.init(userManager, basicUser, false);
    }

    /**
     * redirects admin to the the wishlist menu of the basic user
     * @param actionEvent an actionEvent object
     */
    @FXML
    public void controlWishList(ActionEvent actionEvent) {
        WishlistMenu wm =
                loadFXML(actionEvent, json.getString("wishlistMenuFXML"), WindowType.SCENE).getController();
        wm.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);
        wm.setAdminState(AccountType.ADMIN, basicUser);
        wm.init(userManager);
    }

    /**
     * redirects admin to the the inventory menu of the basic user
     * @param actionEvent an actionEvent object
     */
    @FXML
    public void controlInventory(ActionEvent actionEvent) {
        InventoryMenu im =
                loadFXML(actionEvent, json.getString("inventoryMenuFXML"), WindowType.SCENE).getController();
        im.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);
        im.setAdminState(AccountType.ADMIN, basicUser);
        im.init(userManager, basicUser);
    }

    /**
     * Freeze/ Unfreeze a basic user object
     */
    @FXML
    public void requestFreezeUnfreeze() {
        if (!accountStateManager.getAccountState(basicUser).equals(AccountStanding.FROZEN)) {
            Optional<ButtonType> selected = showAlert(Alert.AlertType.CONFIRMATION,
                    json.getString("sceneConfirmTitle"), json.getString("setFreeze"));
            if (selected.isPresent() && selected.get() == ButtonType.OK) {
                accountStateManager.setAccountState(basicUser, AccountStanding.FROZEN);
                basicUserManager.setTrustedCount(basicUser, 0);
                userState.setText(accountStateManager.getAccountState(basicUser).toString());
                limitedAttributeButton.setDisable(true);
                limitedAttributeButton.setVisible(false);
            }
        }
        else if (accountStateManager.getAccountState(basicUser).equals(AccountStanding.FROZEN)) {
            Optional<ButtonType> selected = showAlert(Alert.AlertType.CONFIRMATION,
                    json.getString("sceneConfirmTitle"), json.getString("setUnfreeze"));
            if (selected.isPresent() && selected.get() == ButtonType.OK) {
                accountStateManager.setAccountState(basicUser, AccountStanding.LIMITED);
                thresholdManager.numOfTransactionsEdit(1, basicUser);
                thresholdManager.numOfIncompleteTransactionsEdit(1, basicUser);
                userState.setText(accountStateManager.getAccountState(basicUser).toString());
                limitedAttributeButton.setDisable(false);
                limitedAttributeButton.setVisible(true);
            }
        }
    }

    /**
     * a JavaFX button that will only appear if the BasicUser account is currently LIMITED.
     * when selecting, it will de-activate the LIMITED account state
     */
    @FXML
    public void requestLimitedAttribute() {
        Optional<ButtonType> selected = showAlert(Alert.AlertType.CONFIRMATION,
                    json.getString("sceneConfirmTitle"), json.getString("deactivateLimited"));
        if (selected.isPresent() && selected.get() == ButtonType.OK) {
            accountStateManager.setAccountState(basicUser, AccountStanding.DEFAULT);
            thresholdManager.numOfTransactionsEdit(1, basicUser);
            thresholdManager.numOfIncompleteTransactionsEdit(1, basicUser);
            userState.setText(accountStateManager.getAccountState(basicUser).toString());
            limitedAttributeButton.setDisable(true);
            limitedAttributeButton.setVisible(false);
        }
    }

    /**
     * a JavaFX button that can set a BasicUser's state to INACTIVE or ACTIVE
     */
    @FXML
    public void requestInactiveState() {
        if (accountStatusManager.getAccountStatus(basicUser) != AccountStatus.INACTIVE) {
            Optional<ButtonType> selected = showAlert(Alert.AlertType.CONFIRMATION,
                    json.getString("sceneConfirmTitle"), json.getString("setInactive"));
            if (selected.isPresent() && selected.get() == ButtonType.OK) {
                accountStatusManager.setAccountStatus(basicUser, AccountStatus.INACTIVE);
                accountStatusManager.inactiveCancelTransaction(transactionsRepo, basicUserRepo, basicUser);
                userStatus.setText(accountStatusManager.getAccountStatus(basicUser).toString());
            }
        }
        else if (accountStatusManager.getAccountStatus(basicUser) == AccountStatus.INACTIVE) {
            Optional<ButtonType> selected = showAlert(Alert.AlertType.CONFIRMATION,
                    json.getString("sceneConfirmTitle"), json.getString("setActive"));
            if (selected.isPresent() && selected.get() == ButtonType.OK) {
                accountStatusManager.setAccountStatus(basicUser, AccountStatus.ACTIVE);
                userStatus.setText(accountStatusManager.getAccountStatus(basicUser).toString());
            }
        }
    }

    /**
     * a JavaFX button that undo the most recently deleted item of the BasicUser
     */
    @FXML
    public void requestUndoDeletedItem() {
        AdminManager adminManager = new AdminManager();
        if (basicUser.getDeletedItem() != null) {
            adminManager.undoLastDeletedItem(basicUser);
            showAlert(Alert.AlertType.INFORMATION,
                    json.getString("sceneSuccessTitle"), json.getString("undoMessage"));
        } else {
            showAlert(Alert.AlertType.INFORMATION,
                    json.getString("sceneInformationTitle"), json.getString("noUndoItemsMessage"));
        }
    }

    /**
     * Sets the threshold values of a basic user
     * @param actionEvent an actionEvent object
     */
    @FXML
    public void controlThreshold(ActionEvent actionEvent) {
        ThresholdMenu tm =
                loadFXML(actionEvent, json.getString("thresholdMenuFXML"), WindowType.SCENE).getController();
        tm.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo,adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);
        tm.setBasicUser(basicUser);
        tm.init(userManager, thresholdManager);
    }

    /**
     * Delete a basic user from the system
     * @param actionEvent an actionEvent object
     */
    @FXML
    public void requestDeleteUser(ActionEvent actionEvent) {
        Optional<ButtonType> selected = showAlert(Alert.AlertType.CONFIRMATION,
                    json.getString("sceneConfirmTitle"), json.getString("deleteAccount"));
        if (selected.isPresent() && selected.get() == ButtonType.OK) {
            basicUserRepo.remove(basicUser.getUsername());
            requestBack(actionEvent);
        }
    }

    /**
     * a button allowing the current admin user to go back to the previous page
     * @param actionEvent an actionEvent object
     */
    @FXML
    public void requestBack(ActionEvent actionEvent) {
        AdminBrowseUsersMenu adminBrowseUsersMenu =
                loadFXML(actionEvent, json.getString("adminBrowseUsersMenuFXML"), WindowType.SCENE).getController();
        adminBrowseUsersMenu.setRepos(
                basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);
        adminBrowseUsersMenu.init(userManager);
    }
}
