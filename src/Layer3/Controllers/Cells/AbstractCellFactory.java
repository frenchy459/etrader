package Layer3.Controllers.Cells;

import Layer3.Repos.*;
import Layer3.Repos.JavaKeyStore.JavaKeyStoreRepo;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * an abstract class for CellFactories . Holds all the common functionally found in all CellFactories
 */
public abstract class AbstractCellFactory<T> implements Callback<ListView<T>, ListCell<T>> {

    protected BasicUserRepo buRepo;
    protected AdminUserRepo auRepo;
    protected ItemsRepo iRepo;
    protected TransactionsRepo tRepo;
    protected AdminNotificationRepo anRepo;
    protected ImageRepo imageRepo;
    protected DemoUserRepo duRepo;
    protected JavaKeyStoreRepo jksRepo;

    /**
     * constructor to be inherited by subclasses
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
    public AbstractCellFactory(BasicUserRepo buRepo, AdminUserRepo auRepo, ItemsRepo iRepo,
                               TransactionsRepo tRepo, AdminNotificationRepo anRepo, ImageRepo imageRepo,
                               DemoUserRepo duRepo, JavaKeyStoreRepo jksRepo) {
        this.buRepo = buRepo;
        this.auRepo = auRepo;
        this.iRepo = iRepo;
        this.tRepo = tRepo;
        this.anRepo = anRepo;
        this.imageRepo = imageRepo;
        this.duRepo = duRepo;
        this.jksRepo = jksRepo;
    }

    /**
     * overrides call method that searches for the format for the listCell's so that a custom format can be used
     * @param param the type of ListView
     */
    @Override
    public abstract ListCell<T> call(ListView<T> param);
}

