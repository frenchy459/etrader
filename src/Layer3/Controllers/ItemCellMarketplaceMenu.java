package Layer3.Controllers;

import Layer1.Enums.AccountStatus;
import Layer1.Entities.Item;
import Layer1.Enums.WindowType;
import Layer2.Managers.AccountStatusManager;
import Layer2.Managers.RatingManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;


public class ItemCellMarketplaceMenu extends ItemCellMenu {

    @FXML
    Label userName;
    @FXML
    Text rating;

    private final AccountStatusManager accountStatusManager = new AccountStatusManager();

    public ItemCellMarketplaceMenu(String path, Item item) {
        super(path, item);
    }

    @Override
    public void initExtra() {
        if (accountStatusManager.getAccountStatus(this.user) == AccountStatus.INACTIVE) {
            userName.setText(this.user.getUsername() + " - " + AccountStatus.INACTIVE.toString());
        } else {
            userName.setText(this.user.getUsername());
        }
        rating.setText(new RatingManager().formatRating(user));
    }

    @Override
    protected void setDetailsButton() {
        detailsButton.setOnAction(event -> {
            ItemInfoMenuMarketplace itemInfoMenuMarketplace =
                    loadFXML(event, json.getString("itemInfoMenuMarketplaceFXML"),
                    WindowType.POPUP).getController();
            itemInfoMenuMarketplace.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo,
                    adminNotificationRepo, imageRepo, demoUserRepo, javaKeyStoreRepo);
            itemInfoMenuMarketplace.init(item, user);
        });
    }
}
