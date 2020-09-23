package Layer3.Controllers;

import Layer1.Enums.WindowType;
import Layer2.Managers.NewItemManager;
import Layer2.Managers.UserManager;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * A request item menu controller class responsible for handling user inputs at the request item menu page
 */
public class RequestItemMenu extends AbstractController {

    @FXML
    public JFXButton browseImageButton;
    @FXML
    public JFXButton sendRequestButton;
    @FXML
    public JFXButton backButton;
    @FXML
    private TextArea itemName;
    @FXML
    private TextArea itemDescription;
    @FXML
    private ImageView itemImage;

    private UserManager userManager;

    private NewItemManager newItemManager;

    private final FileChooser fileChooser = new FileChooser();

    private Image image;


    /**
     * Initialize the request item menu object
     * @param um a userManager object
     */
    public void init(UserManager um) {
        setUserManager(um);
        setDefaultImage();
        this.newItemManager = new NewItemManager(userManager, adminNotificationRepo);
        itemName.setWrapText(true);
        itemDescription.setWrapText(true);

    }

    /**
     * Sets the userManager for the requestItemMenu object
     * @param userManager an userManager object
     */
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    /**
     * Sets the default image of an Item object
     */
    public void setDefaultImage() {
        try {
            itemImage.setImage(new Image(new FileInputStream(json.getString("placeholderImagePath"))));
        }
        catch (IOException e) {
            System.out.println(json.getString("placeholderImageError"));
        }
    }

    /**
     * A javafx button responsible for taking user back to the previous page
     * @param actionEvent an actionEvent object
     */
    @FXML
    public void requestBack(ActionEvent actionEvent) {
        UserMenu um = loadFXML(actionEvent, json.getString("userMenuFXML"), WindowType.SCENE).getController();
        um.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);
        um.init(userManager);
    }

    /**
     * A javafx button responsible for initiate a new item based on user inputs
     */
    @FXML
    public void requestNewItem() {

        if (image == null) {
            try {
                this.image = new Image(new FileInputStream(json.getString("placeholderImagePath")));
            }
            catch (IOException e) {
                System.out.println(json.getString("placeholderImageError"));
            }
        }
        newItemManager.createItem(itemName.getText(), itemDescription.getText(), image,
                itemsRepo, imageRepo, userManager.getCurrentUser(), json.getString("itemRequestMessage"));
        showAlert(Alert.AlertType.INFORMATION, json.getString("sceneSuccessTitle"), json.getString("requestItem"));
        itemName.clear();
        itemDescription.clear();
        setDefaultImage();

    }

    /**
     * A javafx button responsible for browsing new item image by the user
     * @param actionEvent an actionEvent object
     */
    @FXML
    public void requestBrowseImage(ActionEvent actionEvent) {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG Files", "*.jpg", "*.png"));
        File file = fileChooser.showOpenDialog(((Node) actionEvent.getSource()).getScene().getWindow());

        if (file != null) {
            Image image = new Image(file.toURI().toString());
            itemImage.setImage(image);
            this.image = image;
        }
    }
}
