package Layer3.Controllers.Cells;

import Layer1.Entities.Transaction;
import Layer1.Enums.CellType;
import Layer2.Managers.UserManager;
import Layer3.Controllers.TransactionCellMenu;
import Layer3.Repos.*;
import Layer3.Repos.JavaKeyStore.JavaKeyStoreRepo;

/**
 * a ListCell that intakes a Transaction
 */
public class TransactionCell extends AbstractCell<Transaction> {

    private final String currentUserName;
    private final CellType cellType;
    private final UserManager userManager;

    /**
     * constructs a ListCell for Transactions
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
     * @param currentUserName String representation of the username of the current User
     * @param cellType  an enum representing a type of Cell
     * @param userManager   a userManager class containing methods related to User entities
     */
    public TransactionCell(BasicUserRepo buRepo, AdminUserRepo auRepo, ItemsRepo iRepo,
                           TransactionsRepo tRepo, AdminNotificationRepo anRepo, ImageRepo imageRepo,
                           DemoUserRepo duRepo, JavaKeyStoreRepo jksRepo, String currentUserName, CellType cellType,
                           UserManager userManager){
        super(buRepo, auRepo, iRepo, tRepo, anRepo, imageRepo, duRepo, jksRepo);

        this.currentUserName = currentUserName;
        this.cellType = cellType;
        this.userManager = userManager;
    }

    /**
     * overrides to display unique List Cell based on a Transaction
     * @param object the Transaction entity that the listCell will intake
     */
    @Override
    protected void initializeCellMenu(Transaction object) {
        TransactionCellMenu tcm = new TransactionCellMenu(this.getClass().getSimpleName() + "Menu", object,
                currentUserName, userManager, cellType);
        tcm.setRepos(buRepo, auRepo, iRepo, tRepo, anRepo, imageRepo, duRepo, jksRepo);
        tcm.init();
        setGraphic(tcm.getDisplay());
    }
}
