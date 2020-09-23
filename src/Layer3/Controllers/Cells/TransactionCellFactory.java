package Layer3.Controllers.Cells;

import Layer1.Entities.Transaction;
import Layer1.Enums.CellType;
import Layer2.Managers.UserManager;
import Layer3.Repos.*;
import Layer3.Repos.JavaKeyStore.JavaKeyStoreRepo;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

/**
 * A ListCellFactory that intakes Transactions
 */
public class TransactionCellFactory extends AbstractCellFactory<Transaction> {

    private final String currentUserName;
    private final CellType cellType;
    private final UserManager userManager;

    /**
     * constructs a ListCellFactory for Transactions
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
    public TransactionCellFactory(BasicUserRepo buRepo, AdminUserRepo auRepo, ItemsRepo iRepo,
                                  TransactionsRepo tRepo, AdminNotificationRepo anRepo, ImageRepo imageRepo,
                                  DemoUserRepo duRepo, JavaKeyStoreRepo jksRepo, UserManager userManager, String currentUserName, CellType cellType){
        super(buRepo, auRepo, iRepo, tRepo, anRepo, imageRepo, duRepo, jksRepo);

        this.userManager = userManager;
        this.currentUserName = currentUserName;
        this.cellType = cellType;
    }

    /**
     * overrides call method that searches for the format so a custom Transactions listCell can be used
     * @param param represents the Transaction ListView type
     */
    @Override
    public ListCell<Transaction> call(ListView<Transaction> param) {
        return new TransactionCell(buRepo, auRepo, iRepo, tRepo, anRepo, imageRepo, duRepo, jksRepo, currentUserName, cellType, userManager);
    }
}
