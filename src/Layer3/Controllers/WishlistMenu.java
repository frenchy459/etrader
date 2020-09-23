package Layer3.Controllers;

import Layer1.Entities.BasicUser;
import Layer1.Entities.Item;
import Layer1.Enums.AccountType;
import Layer1.Enums.CellType;
import Layer1.Enums.WindowType;
import Layer2.Managers.UserManager;
import Layer3.Controllers.Cells.ItemCellFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A wishlist menu controller class responsible for taking user inputs at the wishlist menu page
 */
public class WishlistMenu extends AbstractController {
    @FXML
    public Button backButton;
    @FXML
    public Text titleText;
    @FXML
    public Button removeItemButton;
    @FXML
    public AnchorPane anchor;
    @FXML
    private Text userName;
    @FXML
    private ListView<Item> listView;


    private UserManager userManager;
    private BasicUser currentUser;
    private final ObservableList<Item> itemList = FXCollections.observableArrayList();
    private AccountType currentState = AccountType.BASIC_USER;

    /**
     * Initialize the wishlist menu object
     * @param um an userManager object
     */
    //groups all the setters/additional initialization tasks under 1 method for easier use
    public void init(UserManager um) {
        setUserManager(um);
        if (!currentState.equals(AccountType.ADMIN))
            this.currentUser = (BasicUser) um.getCurrentUserObject();
        setNameText();
        listView.setFocusTraversable(false);
        populateWishlist();
    }

    private void setUserManager(UserManager userManager) { this.userManager = userManager; }

    private void setNameText() { userName.setText(currentUser.getUsername()); }

    /**
     * A javafx button responsible for taking the user back to the previous page
     * @param actionEvent an actionEvent object
     */
    @FXML
    private void requestBack(ActionEvent actionEvent) {
        if (currentState.equals(AccountType.ADMIN)) {
            AdministrateUserMenu aum = loadFXML
(actionEvent, json.getString("administrateUserMenuFXML"), WindowType.SCENE).getController();
            aum.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                    demoUserRepo, javaKeyStoreRepo);
            aum.init(userManager, currentUser);
        }
        else {
            UserMenu um = loadFXML(actionEvent, json.getString("userMenuFXML"), WindowType.SCENE).getController();
            um.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                    demoUserRepo, javaKeyStoreRepo);
            um.init(userManager);
        }
    }

    private void populateWishlist(){
        //acquires all items in User's wishlist
        for (UUID item: currentUser.getWishList()) { itemList.add(itemsRepo.get(item)); }
        listView.setItems(itemList);
        listView.setCellFactory(new ItemCellFactory(basicUserRepo, adminUserRepo, itemsRepo,
                transactionsRepo, adminNotificationRepo, imageRepo, demoUserRepo, javaKeyStoreRepo, CellType.MARKETPLACE));
        //this lets you multi-select items with shift + click or ctrl + click
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * A javafx button responsible for removing the selected item objects
     */
    @FXML
    private void requestRemoveItems() {
        //gets the currently selected items
        ObservableList<Item> selected = listView.getSelectionModel().getSelectedItems();
        List<Item> toRemove = new ArrayList<>();
        for (Item item : selected) {
            currentUser.getWishList().remove(item.getUUID());
            item.removeWishListedBy(currentUser.getUsername());
            toRemove.add(item);
        }
        if (selected.isEmpty()){
            showAlert(Alert.AlertType.ERROR, json.getString("sceneErrorTitle"), json.getString("noSelectedItems"));
        } else {
            itemList.removeAll(toRemove);
            showAlert(Alert.AlertType.INFORMATION,
                    json.getString("sceneSuccessTitle"), json.getString("wishlistRemove"));
        }
    }

    public void setAdminState(AccountType state, BasicUser user) {
        this.currentState = state;
        this.currentUser = user;
    }
}
