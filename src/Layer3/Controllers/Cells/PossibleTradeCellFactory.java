package Layer3.Controllers.Cells;

import Layer1.Entities.BasicUser;
import Layer1.Entities.Item;
import Layer3.Repos.*;
import Layer3.Repos.JavaKeyStore.JavaKeyStoreRepo;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.util.HashMap;

/**
 * A cell Factory that intakes possible trades - HashMap<Item, BasicUser> for a BasicUser
 */
public class PossibleTradeCellFactory extends AbstractCellFactory<HashMap<Item, BasicUser>> {

    /**
     * constructs a ListCellFactory for Possible Trades - HashMap<Item, BasicUser>
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
     */
    public PossibleTradeCellFactory(BasicUserRepo buRepo, AdminUserRepo auRepo, ItemsRepo iRepo,
                                    TransactionsRepo tRepo, AdminNotificationRepo anRepo, ImageRepo imageRepo,
                                    DemoUserRepo duRepo, JavaKeyStoreRepo jksRepo) {
        super(buRepo, auRepo, iRepo, tRepo, anRepo, imageRepo, duRepo, jksRepo);
    }

    /**
     * overrides call method that searches for the format so a custom HashMap<Item, BasicUser> listCell can be used
     * @param param represents the HashMap<Item, BasicUser> ListView type
     */
    @Override
    public ListCell<HashMap<Item, BasicUser>> call(ListView<HashMap<Item, BasicUser>> param) {
        return new PossibleTradeCell(buRepo, auRepo, iRepo, tRepo, anRepo, imageRepo, duRepo, jksRepo);
    }
}
