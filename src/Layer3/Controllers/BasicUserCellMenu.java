package Layer3.Controllers;

import Layer1.Entities.BasicUser;
import Layer1.Enums.WindowType;
import Layer2.API.MapAPI.errors.ApiException;
import Layer2.Managers.AccountStateManager;
import Layer2.Managers.AccountStatusManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;

/**
 * A basic user cell menu controller class
 */
public class BasicUserCellMenu extends AbstractCellMenu {

    @FXML
    private Label userName;
    @FXML
    private Label accountStatus;
    @FXML
    private Label accountState;
    @FXML
    private Text lastLogin;
    @FXML
    private ImageView userImage;
    @FXML
    private Button profileButton;

    private final BasicUser user;
    private final AccountStatusManager accountStatusManager = new AccountStatusManager();
    private final AccountStateManager accountStateManager = new AccountStateManager();

/**
 * Creates a basic user cell menu object
 */
    public BasicUserCellMenu(String path, BasicUser user) {
        super(path);
        this.user = user;
    }

    @Override
    public void init(){
        clickableIndicator();
        userName.setText(this.user.getUsername());
        accountStatus.setText(accountStatusManager.getAccountStatus(user).toString());
        accountState.setText(accountStateManager.getAccountState(user).toString());
        if (this.user.getLastLogin() == null) {
            lastLogin.setText("N/A");
        }
        else {
            lastLogin.setText(this.user.getLastLogin());
        }
        setImage(userImage, user);
        setProfileButton();
    }

    //NOTE: don't set the fx:id controller for the related FXML file for this
    //this will load the FXML and set its controller
    public void load() {
        //add the extra dash here at the start of the name of the fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Layer3/FXML/BasicUserCellMenu.fxml"));
        loader.setController(this);
        try { loader.load(); }
        catch (IOException e) { e.getStackTrace();}
    }

    private void setProfileButton() {
        profileButton.setOnAction(event -> {
            UserProfileViewMenu upvm = loadFXML(event, json.getString("userProfileViewMenuFXML"), WindowType.POPUP).getController();upvm.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                    demoUserRepo, javaKeyStoreRepo);
            try {
                upvm.init(user);
            } catch (InterruptedException | ApiException | IOException e) {
                e.printStackTrace();
            }
        });
    }
}
