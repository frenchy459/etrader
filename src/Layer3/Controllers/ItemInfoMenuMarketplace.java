package Layer3.Controllers;

import Layer1.Entities.BasicUser;
import Layer1.Entities.Item;
import Layer1.Enums.WindowType;
import Layer2.API.MapAPI.errors.ApiException;
import Layer2.Managers.RatingManager;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.io.IOException;

/**
 * An item info menu marketplace controller class for setting the menu page displays for the current user
 */
public class ItemInfoMenuMarketplace extends ItemInfoMenu {
    @FXML
    public Text ownerField;
    @FXML
    public Text standingField;
    @FXML
    public Text cityField;
    @FXML
    public Text ratingField;
    @FXML
    public JFXButton profileButton;
    @FXML
    private Label userName;
    @FXML
    private Label userLocation;
    @FXML
    private Label userState;
    @FXML
    private Label userReview;

    private BasicUser user;

    /**
     * Initialize the item info menu marketplace object
     * @param item an Item object representing the item of this class
     * @param user an basic user object representing the user of this class
     */
    public void init(Item item, BasicUser user) {
        setWindowTitle();
        this.user = user;
        setUserInfo(user);
        setImageInfo(item);
    }

    private void setUserInfo(BasicUser user){
        userName.setText(user.getUsername());
        userLocation.setText(user.getCity());
        userState.setText(user.getAccountState().name());
        userReview.setText(new RatingManager().formatRating(user));
    }

    @FXML
    private void requestProfile(ActionEvent actionEvent) throws InterruptedException, ApiException, IOException {
        UserProfileViewMenu userProfileViewMenu =
                loadFXML(actionEvent, json.getString("userProfileViewMenuFXML"), WindowType.POPUP).getController();
        userProfileViewMenu.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo,
                imageRepo, demoUserRepo, javaKeyStoreRepo);
        userProfileViewMenu.init(user);
    }
}
