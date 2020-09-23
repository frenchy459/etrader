package Layer3.Controllers.Cells;

import Layer1.Entities.BasicUser;
import Layer1.Entities.Item;
import Layer3.Repos.*;
import Layer3.Repos.JavaKeyStore.JavaKeyStoreRepo;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.util.HashMap;
import java.util.List;

/**
 * A ListCellFactory that takes in Transaction Item - HashMap<Item, List<BasicUser>>
 */
public class TransactionItemCellFactory extends AbstractCellFactory<HashMap<Item, List<BasicUser>>> {

    /**
     * constructs a ListCellFactory for Transaction Item - HashMap<Item, List<BasicUser>>
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
    public TransactionItemCellFactory(BasicUserRepo buRepo, AdminUserRepo auRepo, ItemsRepo iRepo,
                                      TransactionsRepo tRepo, AdminNotificationRepo anRepo, ImageRepo imageRepo,
                                      DemoUserRepo duRepo, JavaKeyStoreRepo jksRepo) {
        super(buRepo, auRepo, iRepo, tRepo, anRepo, imageRepo, duRepo, jksRepo);
    }

    /**
     * overrides call method that searches for the format so a custom HashMap<Item, List<BasicUser>> listCell can be used
     * @param param represents the HashMap<Item, List<BasicUser>> ListView type
     */
    @Override
    public ListCell<HashMap<Item, List<BasicUser>>> call(ListView<HashMap<Item, List<BasicUser>>> param) {
        return new TransactionItemCell(buRepo, auRepo, iRepo, tRepo, anRepo, imageRepo, duRepo, jksRepo);
    }
}
