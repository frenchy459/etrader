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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an inventory menu controller class used for handling user inputs for the inventory menu page
 */
public class InventoryMenu extends AbstractController {
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
     * Initialize the inventory menu object
     * @param um an userManager object
     */
    //groups all the setters/additional initialization tasks under 1 method for easier use
    public void init(UserManager um, BasicUser currentUser) {
        this.userManager = um;
        this.currentUser = currentUser;
        if (!currentState.equals(AccountType.ADMIN))
            this.currentUser = (BasicUser) um.getCurrentUserObject();

        setNameText();
        listView.setFocusTraversable(false);
        populateInventory();
    }

    private void setNameText() {
        userName.setText(currentUser.getUsername());
    }

    /**
     * A javafx button responsible for taking user back to the previous page
     * @param actionEvent a user action event
     */
    @FXML
    public void requestBack(ActionEvent actionEvent) {
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

    private void populateInventory(){
        //acquires all items in User's lendList
        itemList.addAll(itemsRepo.getFilteredList(currentUser.getLendList()));
        listView.setItems(itemList);
        listView.setCellFactory(new ItemCellFactory(basicUserRepo, adminUserRepo, itemsRepo,
                transactionsRepo, adminNotificationRepo, imageRepo, demoUserRepo, javaKeyStoreRepo, CellType.INVENTORY));
        //this lets you multi-select items with shift + click or ctrl + click
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * A javafx button responsible for the removal of selected items
     */
    @FXML
    private void requestRemoveItems(){
        //gets the currently selectedItems items
        ObservableList<Item> selectedItems = listView.getSelectionModel().getSelectedItems();
        List<Item> itemsToBeRemoved = new ArrayList<>();

        for (Item item : selectedItems) {
            if (!item.getIsApproved()) {
                showAlert(Alert.AlertType.ERROR,
                        json.getString("sceneErrorTitle"), json.getString("deleteUnapproved"));
            }
            else {
                currentUser.getLendList().remove(item.getUUID());
                currentUser.setDeletedItem(item.getUUID());
                itemsToBeRemoved.add(item);
                itemsRepo.remove(item.getUUID());
            }
        }

        if (selectedItems.isEmpty()){
            showAlert(Alert.AlertType.ERROR,
                    json.getString("sceneErrorTitle"), json.getString("noSelectedItems"));
        } else if (!itemsToBeRemoved.isEmpty()) {
            itemList.removeAll(itemsToBeRemoved);
            showAlert(Alert.AlertType.INFORMATION,
                    json.getString("sceneSuccessTitle"), json.getString("removeItems"));
        }
    }

    /**
     * Sets the admin state of this inventory menu
     * @param state an enum AccountType object
     * @param user a basic user object
     */
    public void setAdminState(AccountType state, BasicUser user) {
        this.currentState = state;
        this.currentUser = user;
    }
}
