package Layer3.Controllers;

import Layer1.Entities.BasicUser;
import Layer1.Entities.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.time.format.DateTimeFormatter;

//this is the Controller of the custom pane that will replace the default view of the ListCell
public abstract class ItemCellMenu extends AbstractCellMenu {

    @FXML
    Label itemTitle;
    @FXML
    Label itemDescription;
    @FXML
    ImageView itemImage;
    @FXML
    Text dateAdded;
    @FXML
    Button detailsButton;

    Item item;
    BasicUser user;

    public ItemCellMenu(String path, Item item) {
        super(path);
        this.item = item;
    }

    @Override
    public void init() {
        clickableIndicator();
        if (basicUserRepo.get(item.getOriginalOwner()) == null)
            this.user = demoUserRepo.get(item.getOriginalOwner());
        else
            this.user = basicUserRepo.get(item.getOriginalOwner());
        itemTitle.setText(item.getName());
        itemDescription.setText(item.getDescription());
        itemImage.setImage(imageRepo.get(item.getUUID().toString()));
        dateAdded.setText((DateTimeFormatter.ISO_DATE).format(item.getDateAdded()));
        initExtra();
        setDetailsButton();
    }

    protected abstract void initExtra();

    protected abstract void setDetailsButton();
}
