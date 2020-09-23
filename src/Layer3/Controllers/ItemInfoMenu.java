package Layer3.Controllers;

import Layer1.Entities.BasicUser;
import Layer1.Entities.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

/**
 * An item info menu controller used for handling user inputs for the item info menu
 */
public class ItemInfoMenu extends AbstractController {
    @FXML
    public AnchorPane anchor;
    @FXML
    public Text titleField;
    @FXML
    public Text descriptionField;
    @FXML
    public Text dateAddedField;
    @FXML
    protected ImageView itemImage;
    @FXML
    protected Label itemName;
    @FXML
    protected Label itemDesc;
    @FXML
    protected Label dateAdded;

    /**
     * Initialize the item info menu object
     * @param item an Item object representing the item of this class
     * @param user an basic user object representing the user of this class
     */
    public void init(Item item, BasicUser user) {
        setWindowTitle();
        setImageInfo(item);
    }

    /**
     * Displays the window title for the menu
     */
    void setWindowTitle() {
        Stage stage = (Stage) this.itemName.getScene().getWindow();
        stage.setTitle(json.getString("itemInformationTitle"));
    }

    /**
     * Sets the image of the item
     * @param item an item object
     */
    void setImageInfo(Item item){
        dateAdded.setText(DateTimeFormatter.ISO_DATE.format(item.getDateAdded()));
        itemName.setText(item.getName());
        itemDesc.setText(item.getDescription());
        itemImage.setImage(imageRepo.get(item.getUUID().toString()));
    }

}
