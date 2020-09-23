package Layer3.Controllers;

import Layer1.Entities.BasicUser;
import Layer1.Enums.AccountStanding;
import Layer1.Enums.AdminNotificationType;
import Layer1.Enums.WindowType;
import Layer2.API.MapAPI.errors.ApiException;
import Layer2.Managers.*;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

/** Represents an UserMenu controller class*/
public class UserMenu extends AbstractController {
    @FXML
    public JFXButton logOutButton;
    @FXML
    public Text usernameField;
    @FXML
    public Text ratingField;
    @FXML
    public Text standingField;
    @FXML
    public Text statusField;
    @FXML
    public JFXButton allItemsButton;
    @FXML
    public JFXButton wishlistButton;
    @FXML
    public JFXButton inventoryButton;
    @FXML
    public JFXButton newTradeButton;
    @FXML
    public JFXButton tradeReqButton;
    @FXML
    public JFXButton itemReqButton;
    @FXML
    public JFXButton transactHistButton;
    @FXML
    public JFXButton userProfileButton;
    @FXML
    private Text userName;
    @FXML
    private Text review;
    @FXML
    private Text state;
    @FXML
    private Text status;
    @FXML
    private Text currentCity;

    @FXML
    private ImageView userImage;

    private UserManager userManager;
    private BasicUser currentUser;
    private final AccountStateManager accountStateManager = new AccountStateManager();
    private TransactionManager transactionManager;
    private AdminNotificationManager adminNotificationManager;

    /**
     * A javafx button responsible for logging the current user out
     * @param actionEvent an actionEvent object
     */
    @FXML
    public void requestLogOut(ActionEvent actionEvent) {
        //builds an object from the Alert object that can listen for & output the button that was pressed
        //necessary for confirmation screens
        Optional<ButtonType> selected = showAlert(Alert.AlertType.CONFIRMATION,
                    json.getString("sceneConfirmTitle"), json.getString("logOut"));
        if (selected.isPresent() && selected.get() == ButtonType.OK) {
            demoUserRepo.empty(itemsRepo, basicUserRepo, transactionsRepo, imageRepo);
            Trader trader = loadFXML(actionEvent, json.getString("landingMenuFXML"), WindowType.SCENE).getController();
            trader.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                    demoUserRepo, javaKeyStoreRepo);
        }
    }

    /**
     * A javafx button responsible for taking the user to user profile page
     * @param actionEvent an actionEvent object
     * @throws InterruptedException an interrupted exception
     * @throws ApiException an API exception
     * @throws IOException an IO exception
     */
    @FXML
    private void controlUserProfile(ActionEvent actionEvent) throws InterruptedException, ApiException, IOException {
        UserProfileMenu upm = loadFXML(actionEvent, json.getString("userProfileMenuFXML"), WindowType.SCENE).getController();
        upm.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);
        upm.init(userManager);
    }

    /**
     * A javafx button responsible for taking user to the browsing items menu page
     * @param actionEvent an actionEvent object
     */
    @FXML
    private void controlBrowseItem(ActionEvent actionEvent) {
        MarketplaceMenu mm = loadFXML(actionEvent, json.getString("marketplaceMenuFXML"), WindowType.SCENE).getController();
        mm.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);
        mm.init(userManager);
    }

    /**
     * A javafx button responsible for taking user to the view wishlist page
     * @param actionEvent an actionEvent object
     */
    @FXML
    private void controlWishList(ActionEvent actionEvent) {
        WishlistMenu wm = loadFXML(actionEvent, json.getString("wishlistMenuFXML"), WindowType.SCENE).getController();
        wm.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);
        wm.init(userManager);
    }

    /**
     * A javafx button responsible for taking user to the create new item page
     * @param actionEvent an actionEvent object
     */
    @FXML
    private void controlNewItem(ActionEvent actionEvent) {
        if (checkNotFrozen()) {
            RequestItemMenu rim = loadFXML(actionEvent, json.getString("requestItemMenuFXML"), WindowType.SCENE).getController();
            rim.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                    demoUserRepo, javaKeyStoreRepo);
            rim.init(userManager);
        }
    }

    /**
     * A javafx button responsible for taking user to the view inventory page
     * @param actionEvent an actionEvent object
     */
    @FXML
    private void controlInventory(ActionEvent actionEvent) {
        InventoryMenu im = loadFXML(actionEvent, json.getString("inventoryMenuFXML"), WindowType.SCENE).getController();
        im.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);
        im.init(userManager, currentUser);
    }

    /**
     * A javafx button responsible for taking user to the create new trade page
     * @param actionEvent an actionEvent object
     */
    @FXML
    private void controlNewTrade(ActionEvent actionEvent) {
        if (checkNotFrozen()) {
            NewTradeMenu ntm = loadFXML(actionEvent, json.getString("newTradeMenuFXML"), WindowType.SCENE).getController();
            ntm.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                    demoUserRepo, javaKeyStoreRepo);
            ntm.init(userManager);
        }
    }

    /**
     * A javafx button responsible for taking user to the view transaction history page
     * @param actionEvent an actionEvent object
     */
    @FXML
    private void controlTransactionHistory(ActionEvent actionEvent) {
        TransactionHistoryMenu thm = loadFXML(actionEvent, json.getString("transactionHistoryMenuFXML"), WindowType.SCENE).getController();
        thm.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);
        thm.init(userManager, currentUser, true);
    }

    /**
     * A javafx button responsible for taking user to the incoming requests page
     * @param actionEvent an actionEvent object
     */
    @FXML
    private void controlIncomingRequests(ActionEvent actionEvent) {
        IncomingRequestsMenu incomingRequestsMenu = loadFXML
                (actionEvent, json.getString("incomingRequestsMenuFXML"), WindowType.SCENE).getController();
        incomingRequestsMenu.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo,
                imageRepo, demoUserRepo, javaKeyStoreRepo);
        incomingRequestsMenu.init(userManager, currentUser);
    }

    private void confirmTransactionPopUp() {
        //if there are pending meeting confirmations for the user, the popUp will show
        if (transactionManager.getPendingMeetingConfirmations(currentUser)) {
            ConfirmTransactionMenu ctm = loadFXML(null, json.getString("confirmTransactionMenuFXML"),
                    WindowType.POPUP).getController();
            ctm.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo,
                    imageRepo, demoUserRepo, javaKeyStoreRepo);
            ctm.init(userManager, currentUser, transactionManager);
        }
    }

    //groups all the setters/additional initialization tasks under 1 method for easier use
    public void init(UserManager um) {
        setUserManager(um);
        this.currentUser = (BasicUser) um.getCurrentUserObject();
        this.adminNotificationManager = new AdminNotificationManager(adminNotificationRepo, itemsRepo);
        setNameText();
        setImage();
        transactionManager = new TransactionManager(
                basicUserRepo, transactionsRepo, demoUserRepo, currentUser.getUsername());
        transactionManager.checkIfTransactionsAreIncomplete(currentUser);
        confirmTransactionPopUp();
        review.setText(new RatingManager().formatRating(currentUser));
        state.setText(currentUser.getAccountState().toString());
        status.setText(currentUser.getAccountStatus().toString());
        currentCity.setText(currentUser.getCity().replaceAll("^\"+|\"+$", ""));
    }

    /**
     * Sets the userManager of the userMenu object
     * @param userManager an userManager object
     */
    public void setUserManager(UserManager userManager) { this.userManager = userManager; }

    /**
     * Sets the Name displays on the user menu
     */
    public void setNameText() { userName.setText(currentUser.getUsername()); }

    /**
     * Sets the image of the current user
     */
    public void setImage() {
        if (imageRepo.get(currentUser.getUsername()) != null) {
            Image image = imageRepo.get(currentUser.getUsername());
            userImage.setImage(image);
        }
        else {
            try {
                userImage.setImage((new Image(new FileInputStream(json.getString("defaultUserImagePath")))));
            }
            catch (IOException e) {
                System.out.println(json.getString("defaultUserImageError"));
            }
        }
    }

    private boolean checkNotFrozen() {
        BasicUser currentUser = (BasicUser) userManager.getCurrentUserObject();
        if (accountStateManager.getAccountState(currentUser).equals(AccountStanding.FROZEN)) {
            if (!currentUser.getRequestUnfrozen()) {
                showAlert(Alert.AlertType.ERROR, json.getString("sceneErrorTitle"),
                        json.getString("accountFrozenMessage"));
                adminNotificationManager.createNewAdminNotification(currentUser, AdminNotificationType.UNFREEZE,
                        json.getString("unfreezeMessage"));
                currentUser.setRequestUnfrozen(true);
            }
            else {
                showAlert(Alert.AlertType.ERROR, json.getString("sceneErrorTitle"),
                        json.getString("accountAlreadyShownFrozenMessage"));
            }
            return false;
        }
        else {
            return true;
        }
    }
}
