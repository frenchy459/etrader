package Layer3.Controllers.Cells;

import Layer1.Entities.Item;
import Layer1.Enums.CellType;
import Layer3.Controllers.ItemCellInventoryMenu;
import Layer3.Controllers.ItemCellMarketplaceMenu;
import Layer3.Controllers.ItemCellMenu;
import Layer3.Repos.*;
import Layer3.Repos.JavaKeyStore.JavaKeyStoreRepo;

/**
 * An item cell controller class
 */
public class ItemCell extends AbstractCell<Item> {

    private final CellType cellType;

    /**
     * constructs a ListCell for Items
     *
     * @param buRepo    a basic user repo object responsible for reading and writing BasicUsers objects to a file.
     * @param auRepo    an admin user repo object responsible for reading and writing adminUsers objects to a file.
     * @param iRepo     an item repo object responsible for reading and writing items objects to a file.
     * @param tRepo     a transaction repo object responsible for reading and writing transactions objects to a file.
     * @param anRepo    an admin notification repo object responsible for reading and writing admin notifications
     *                  objects to a file.
     * @param imageRepo an image repo object responsible for reading and writing images objects to a file.
     * @param duRepo    a demo user repo object responsible for reading and writing demoUsers objects to a file.
     * @param jksRepo   JavaKeyStore repository responsible for reading and writing Java key-stores to a file.
     * @param cellType  an enum representing a type of Cell
     */
    public ItemCell(BasicUserRepo buRepo, AdminUserRepo auRepo, ItemsRepo iRepo,
                    TransactionsRepo tRepo, AdminNotificationRepo anRepo, ImageRepo imageRepo,
                    DemoUserRepo duRepo, JavaKeyStoreRepo jksRepo, CellType cellType){
        super(buRepo, auRepo, iRepo, tRepo, anRepo, imageRepo, duRepo, jksRepo);
        this.cellType = cellType;
    }

    /**
     * overrides to display unique List Cell based on Item
     * @param object the Item entity that the listCell will intake
     */
    @Override
    protected void initializeCellMenu(Item object) {
        ItemCellMenu icm = cellType.equals(CellType.INVENTORY) ? new ItemCellInventoryMenu(
                this.getClass().getSimpleName() + "InventoryMenu", object)
                : new ItemCellMarketplaceMenu(this.getClass().getSimpleName() + "MarketplaceMenu", object);
        icm.setRepos(buRepo, auRepo, iRepo, tRepo, anRepo, imageRepo, duRepo, jksRepo);
        icm.init();
        setGraphic(icm.getDisplay());
    }

}
