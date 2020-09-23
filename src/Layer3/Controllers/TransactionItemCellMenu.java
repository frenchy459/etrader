package Layer3.Controllers;

import Layer1.Entities.BasicUser;
import Layer1.Entities.Item;
import Layer1.Enums.WindowType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.List;

/**
 * A transaction item cell menu controller class
 */
public class TransactionItemCellMenu extends AbstractCellMenu {

    @FXML
    ImageView itemImage;
    @FXML
    Label itemTitle;
    @FXML
    Label itemDescription;
    @FXML
    Label lender;
    @FXML
    Label borrower;
    @FXML
    Button detailsButton;

    private final HashMap<Item, List<BasicUser>> itemDetails;

    public TransactionItemCellMenu(String path, HashMap<Item, List<BasicUser>> itemDetails) {
        super(path);
        this.itemDetails = itemDetails;
    }

    @Override
    public void init() {
        clickableIndicator();
        anchor.setFocusTraversable(false);
        setDisplayInfo();
        setDetailsButton();
    }

    private void setDisplayInfo() {
        Item item = (Item) itemDetails.keySet().toArray()[0];
        itemTitle.setText(item.getName());
        lender.setText(itemDetails.get(item).get(0).getUsername());
        borrower.setText(itemDetails.get(item).get(1).getUsername());
        itemDescription.setText(item.getDescription());
        itemImage.setImage(imageRepo.get(item.getUUID().toString()));
    }

    private void setDetailsButton() {
        Item item = (Item) itemDetails.keySet().toArray()[0];
        detailsButton.setOnAction(event -> {
            ItemInfoMenu iim = loadFXML(event, json.getString("itemInfoMenuFXML"), WindowType.POPUP).getController();
            iim.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                    demoUserRepo, javaKeyStoreRepo);

            iim.init(item, basicUserRepo.get(item.getOriginalOwner()));
        });
    }

}
