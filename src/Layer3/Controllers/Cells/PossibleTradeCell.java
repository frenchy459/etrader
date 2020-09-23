package Layer3.Controllers.Cells;

import Layer1.Entities.BasicUser;
import Layer1.Entities.Item;
import Layer3.Controllers.PossibleTradeCellMenu;
import Layer3.Repos.*;
import Layer3.Repos.JavaKeyStore.JavaKeyStoreRepo;

import java.util.HashMap;

/**
 * a ListCell that intakes the possible trades available to a BasicUser
 */
public class PossibleTradeCell extends AbstractCell<HashMap<Item, BasicUser>> {

    /**
     * constructs a ListCell for Possible Trades - HashMap<Item, BasicUser>
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
    public PossibleTradeCell(BasicUserRepo buRepo, AdminUserRepo auRepo, ItemsRepo iRepo,
                             TransactionsRepo tRepo, AdminNotificationRepo anRepo, ImageRepo imageRepo,
                             DemoUserRepo duRepo, JavaKeyStoreRepo jksRepo) {

        super(buRepo, auRepo, iRepo, tRepo, anRepo, imageRepo, duRepo, jksRepo);
    }

    /**
     * overrides to display unique List Cell based on a HashMap<Item, BasicUser>
     * @param object the HashMap<Item, BasicUser> entity that the listCell will intake
     */
    @Override
    protected void initializeCellMenu(HashMap<Item, BasicUser> object) {
        PossibleTradeCellMenu possibleTradeCellMenu =
                new PossibleTradeCellMenu(this.getClass().getSimpleName() + "Menu", object);
        possibleTradeCellMenu.setRepos(buRepo, auRepo, iRepo, tRepo, anRepo, imageRepo, duRepo, jksRepo);
        possibleTradeCellMenu.init();
        setGraphic(possibleTradeCellMenu.getDisplay());
    }
}
