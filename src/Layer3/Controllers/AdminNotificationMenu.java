package Layer3.Controllers;

import Layer1.Entities.AdminNotification;
import Layer1.Enums.WindowType;
import Layer2.Managers.UserManager;
import Layer3.Controllers.Cells.AdminNotificationCellFactory;
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
 * An controller class handling admin notification menu actions
 */
public class AdminNotificationMenu extends AbstractController {

    @FXML
    public AnchorPane anchor;
    @FXML
    public Text titleText;
    @FXML
    public JFXButton backButton;
    @FXML
    public JFXButton deleteButton;
    @FXML
    private ListView<AdminNotification> listView;
    @FXML
    private Text userName;

    private UserManager userManager;
    private final ObservableList<AdminNotification> notificationList = FXCollections.observableArrayList();

    /**
     * initialize the admin notification menu
     * @param um a userManager object
     */
    //groups all the setters/additional initialization tasks under 1 method for easier use
    public void init(UserManager um) {
        setUserManager(um);
        setUserName();
        listView.setFocusTraversable(false);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        populateAdminNotifications();
    }

    /**
     * a javafx button taking the user to the previous page
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
     * a javafx button for deleting selected notification objects
     */
    @FXML
    public void requestDelete() {
        ObservableList<AdminNotification> selected = listView.getSelectionModel().getSelectedItems();
        List<AdminNotification> toRemove = new ArrayList<>();
        for (AdminNotification adminNotification : selected) {
            adminNotificationRepo.remove(adminNotification.getMID());
            toRemove.add(adminNotification);
        }
        if (selected.isEmpty()){
            showAlert(Alert.AlertType.ERROR, json.getString("sceneErrorTitle"), json.getString("noSelectedNotification"));
        } else{
            notificationList.removeAll(toRemove);
            showAlert(Alert.AlertType.INFORMATION,
                    json.getString("sceneSuccessTitle"), json.getString("deleteNotification"));
        }
    }

    private void populateAdminNotifications() {
        //acquires all notifications
        notificationList.addAll(adminNotificationRepo.getAll());
        listView.setItems(notificationList);
        listView.setCellFactory(new AdminNotificationCellFactory(basicUserRepo, adminUserRepo, itemsRepo,
                transactionsRepo, adminNotificationRepo, imageRepo, demoUserRepo, javaKeyStoreRepo));
    }

    private void setUserManager(UserManager userManager){ this.userManager = userManager; }

    private void setUserName() { userName.setText(userManager.getCurrentUser());}
}

