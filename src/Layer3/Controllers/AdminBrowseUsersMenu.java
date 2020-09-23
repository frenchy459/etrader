package Layer3.Controllers;

import Layer1.Entities.BasicUser;
import Layer1.Enums.WindowType;
import Layer2.Managers.UserManager;
import Layer3.Controllers.Cells.BasicUserCellFactory;
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

/**
 * Represents an admin browse user menu controller.
 */
public class AdminBrowseUsersMenu extends AbstractController {

    @FXML
    public AnchorPane anchor;
    @FXML
    public Text titleText;
    @FXML
    public JFXButton backButton;
    @FXML
    public JFXButton selectUserButton;
    @FXML
    private ListView<BasicUser> listView;
    @FXML
    private Text userName;

    private UserManager userManager;
    private final ObservableList<BasicUser> basicUserList = FXCollections.observableArrayList();

    /**
     * Initializes the admin browse users menu
     * @param um a userManager class containing methods related to User entities
     */
    public void init(UserManager um) {
        setUserManager(um);
        setUserName();
        listView.setFocusTraversable(false);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        populateUsers();

    }

    /**
     * Sets the userManager of the adminBrowseUserMenu
     * @param userManager  a userManager class containing methods related to User entities
     */
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    /**
     * Sets the user name of the admin browse user menu
     */
    public void setUserName() {
        userName.setText(userManager.getCurrentUser());
    }

    /**
     * A javafx button for taking the user back to the previous page
     * @param actionEvent the ActionEvent entity that signals to the method that an action has occurred
     */
    @FXML
    public void requestBack(ActionEvent actionEvent) {
        AdminMenu am = loadFXML(actionEvent, json.getString("adminMenuFXML"), WindowType.SCENE).getController();
        am.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);
        am.init(userManager);
    }

    /**
     * A javafx button for basic user selection by the current admin
     * @param actionEvent the ActionEvent entity that signals to the method that an action has occurred
     */
    @FXML
    public void requestSelectUser(ActionEvent actionEvent) {
        ObservableList<BasicUser> selected = listView.getSelectionModel().getSelectedItems();

        if (selected.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, json.getString("sceneErrorTitle"), json.getString("noUsersSelected"));
        } else {
            AdministrateUserMenu aum = loadFXML
                    (actionEvent, json.getString("administrateUserMenuFXML"), WindowType.SCENE).getController();
            aum.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                    demoUserRepo, javaKeyStoreRepo);
            aum.init(userManager, selected.get(0));
        }
    }

    //populates ListView with a custom ListCell for each BasicUser in the system
    private void populateUsers() {
        basicUserList.addAll(basicUserRepo.getAll());
        listView.setItems(basicUserList);
        listView.setCellFactory(new BasicUserCellFactory(basicUserRepo, adminUserRepo, itemsRepo,
                transactionsRepo, adminNotificationRepo, imageRepo, demoUserRepo, javaKeyStoreRepo, userManager));
    }
}
