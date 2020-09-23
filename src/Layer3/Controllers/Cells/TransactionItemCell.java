package Layer3.Controllers.Cells;

import Layer1.Entities.BasicUser;
import Layer1.Entities.Item;
import Layer3.Controllers.TransactionItemCellMenu;
import Layer3.Repos.*;
import Layer3.Repos.JavaKeyStore.JavaKeyStoreRepo;

import java.util.HashMap;
import java.util.List;

/**
 * a ListCell that intakes a Transaction Item - HashMap<Item, List<BasicUser>>
 */
public class TransactionItemCell extends AbstractCell<HashMap<Item, List<BasicUser>>> {

    /**
     * constructs a ListCell for Transaction Item - HashMap<Item, List<BasicUser>>
     *
     * @param buRepo    a basic user repo object responsible for reading and writing BasicUsers objects to a file.
     * @param auRepo    an admin user repo object responsible for reading and writing adminUsers objects to a file.
     * @param iRepo     an item repo object responsible for reading and writing items objects to a file.
     * @param tRepo     a transaction repo object responsible for reading and writing transactions objects to a file.
     * @param anRepo    an admin notification repo object responsible for reading and writing admin notifications
     *                  objects to a file.
     * @param imageRepo an image repo object responsible for reading and writing images objects to a file.
     * @param duRepo    a demo user repo object responsible for reading and writing demoUsers objects to a file.
     * @param jksRepo   JavaKeyStore repository responsible for reading and writing Java key stores to a file.
     */
    public TransactionItemCell(BasicUserRepo buRepo, AdminUserRepo auRepo, ItemsRepo iRepo,
                               TransactionsRepo tRepo, AdminNotificationRepo anRepo, ImageRepo imageRepo,
                               DemoUserRepo duRepo, JavaKeyStoreRepo jksRepo){
        super(buRepo, auRepo, iRepo, tRepo, anRepo, imageRepo, duRepo, jksRepo);
    }

    /**
     * overrides to display unique List Cell based on a HashMap<Item, List<BasicUser>>
     * @param object the HashMap<Item, List<BasicUser>> entity that the listCell will intake
     */
    @Override
    protected void initializeCellMenu(HashMap<Item, List<BasicUser>> object) {
        TransactionItemCellMenu transactionItemCellMenu =
                new TransactionItemCellMenu(this.getClass().getSimpleName() + "Menu", object);
        transactionItemCellMenu.setRepos(buRepo, auRepo, iRepo, tRepo, anRepo, imageRepo, duRepo, jksRepo);
        transactionItemCellMenu.init();
        setGraphic(transactionItemCellMenu.getDisplay());
    }
}
