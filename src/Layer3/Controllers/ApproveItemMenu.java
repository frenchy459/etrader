package Layer3.Controllers;

import Layer1.Entities.AdminNotification;
import Layer1.Entities.Item;
import Layer1.Enums.CellType;
import Layer1.Enums.WindowType;
import Layer2.Managers.AdminNotificationManager;
import Layer2.Managers.ItemManager;
import Layer2.Managers.UserManager;
import Layer3.Controllers.Cells.ItemCellFactory;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * An approve item menu controller class responsible for handling the actions for the approving items menu
 */
public class ApproveItemMenu extends AbstractController {
    @FXML
    public AnchorPane anchor;
    @FXML
    public Text titleText;
    @FXML
    public JFXButton backButton;
    @FXML
    public JFXButton approveItemsButton;
    @FXML
    public JFXButton rejectItemButton;
    @FXML
    private Text userName;
    @FXML
    private ListView<Item> listView;

    private UserManager userManager;
    private AdminNotificationManager adminNotificationManager;
    private final ObservableList<Item> approveItemsList = FXCollections.observableArrayList();
    private final ItemManager itemManager = new ItemManager();

    /**
     * Initialize the approveItemMenu object
     * @param um a userManager object for the approveItemMenu
     * @param adminNotificationManager an adminNotificationManager object for the approveItemMenu
     */
    public void init(UserManager um, AdminNotificationManager adminNotificationManager){
        this.adminNotificationManager = adminNotificationManager;
        this.userManager = um;
        setAdminNotificationManager(adminNotificationManager);
        setNameText();
        listView.setFocusTraversable(false);
        populateApproveItemsList();
    }

    /**
     * Displays the name of the current user
     */
    public void setNameText (){
        userName.setText(userManager.getCurrentUser());
    }

    /**
     * Sets the adminNotificationManager of the approveItemMenu
     * @param anm an AdminNotificationManager object
     */
    public void setAdminNotificationManager(AdminNotificationManager anm){
        this.adminNotificationManager = anm;
    }

    /**
     * A javafx button responsible for taking the user to the previous page
     * @param actionEvent an actionEvent object
     */
    @FXML
    public void requestBack(ActionEvent actionEvent) {
        AdminMenu am = loadFXML(actionEvent, json.getString("adminMenuFXML"), WindowType.SCENE).getController();
        am.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);
        am.init(userManager);
    }

    /**
     * Displays the items needed to be approved by the admin user
     */
    public void populateApproveItemsList() {
        for (AdminNotification an: adminNotificationManager.getApproveItemsNotificationList()) {
            approveItemsList.add(itemsRepo.get(an.getUnapprovedItemUUID()));
        }
        //adds all items to the List View's item pool
        listView.setItems(approveItemsList);
        //sets the factory for the custom list cells
        listView.setCellFactory(new ItemCellFactory(basicUserRepo, adminUserRepo, itemsRepo,
                transactionsRepo, adminNotificationRepo, imageRepo, demoUserRepo, javaKeyStoreRepo, CellType.MARKETPLACE));
        //this lets you multi-select items with shift + click or ctrl + click
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


    /**
     * A javafx button responsible for approving the selected item objects
     */
    @FXML
    public void requestApproveItems() {
        ObservableList<Item> selected = listView.getSelectionModel().getSelectedItems();
        List<Item> toRemove = new ArrayList<>();

        if (selected.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, json.getString("sceneErrorTitle"), json.getString("noSelectedItems"));
        } else {
            for (Item item : selected) {
                itemManager.approveItem(item);
                toRemove.add(item);
            }

            for (AdminNotification adminNotification : adminNotificationRepo.getAll()) {
                if (toRemove.contains(itemsRepo.get(adminNotification.getUnapprovedItemUUID()))) {
                    adminNotificationRepo.remove(adminNotification.getMID());
                }
            }
            approveItemsList.removeAll(toRemove);
            showAlert(Alert.AlertType.INFORMATION,
                    json.getString("sceneSuccessTitle"), json.getString("approveItems"));
        }
    }

    /**
     * A javafx button responsible for rejecting selected items
     */
    @FXML
    public void requestRejectItems() {
        ObservableList<Item> selected = listView.getSelectionModel().getSelectedItems();
        List<Item> toRemove = new ArrayList<>();

        if (selected.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, json.getString("sceneErrorTitle"), json.getString("noSelectedItems"));
        } else {
            for (Item item : selected) {
                itemManager.rejectItem(item, itemsRepo, imageRepo, basicUserRepo);
                toRemove.add(item);
            }

            for (AdminNotification adminNotification : adminNotificationRepo.getAll()) {
                if (toRemove.contains(itemsRepo.get(adminNotification.getUnapprovedItemUUID()))) {
                    adminNotificationRepo.remove(adminNotification.getMID());
                }
            }
            approveItemsList.removeAll(toRemove);
            showAlert(Alert.AlertType.INFORMATION,
                    json.getString("sceneSuccessTitle"), json.getString("rejectItems"));
        }

    }
}
