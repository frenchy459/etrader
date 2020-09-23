package InterfaceAdapters;

import ApplicationBusinessRules.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;

import java.util.Optional;

public class UserMenuController extends AbstractController {

    @FXML
    private Button allItemsButton;
    @FXML
    private Button wishlistButton;
    @FXML
    private Button InventoryButton;
    @FXML
    private Button newTradeButton;
    @FXML
    private Button tradeRequestsButton;
    @FXML
    private Button systemNotifButton;
    @FXML
    private Button itemReqButton;
    @FXML
    private Button transactionHistoryButton;
    @FXML
    private Button logOutButton;
    @FXML
    private Text userName;

    private UserManager userManager;

    public void initialize (String currentUser, UserManager userManager){
        this.userManager = userManager;
        userName.setText(currentUser);
    }

    @FXML
    private void requestLogOut(ActionEvent actionEvent) {
       Optional<ButtonType> selected = showAlert(Alert.AlertType.CONFIRMATION,
               "Confirm","Do you really want to log out?");
       if(selected.isPresent() && selected.get() == ButtonType.OK){
           LandingMenuController lmc = loadFXML(actionEvent, landingMenuFXML).getController();
           lmc.setUserManager(this.userManager);
       }
    }
}
