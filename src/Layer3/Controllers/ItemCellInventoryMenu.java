package Layer3.Controllers;

import Layer1.Entities.Item;
import Layer1.Enums.WindowType;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class ItemCellInventoryMenu extends ItemCellMenu {

    @FXML
    Text itemStatus;

    public ItemCellInventoryMenu(String path, Item item) {
        super(path, item);
    }

    @Override
    protected void initExtra() {
        itemStatus.setText(item.getIsApproved() ? "Approved" : "Unapproved");
    }


    @Override
    protected void setDetailsButton() {
        detailsButton.setOnAction(event -> {
            ItemInfoMenu iim = loadFXML(event, json.getString("itemInfoMenuFXML"),
                    WindowType.POPUP).getController();
            iim.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                        demoUserRepo, javaKeyStoreRepo);
            iim.init(item, user);
        });
    }
}
