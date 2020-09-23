package Layer3.Controllers.Cells;

import Layer3.Repos.*;
import Layer3.Repos.JavaKeyStore.JavaKeyStoreRepo;
import javafx.scene.control.ListCell;

/**
 * an abstract class for ListCells that extends the base ListCell so that it can intake the custom Cell pane
 */
public abstract class AbstractCell<T> extends ListCell<T> {

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
    public AbstractCell(BasicUserRepo buRepo, AdminUserRepo auRepo, ItemsRepo iRepo,
                        TransactionsRepo tRepo, AdminNotificationRepo anRepo, ImageRepo imageRepo,
                        DemoUserRepo duRepo, JavaKeyStoreRepo jksRepo){
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
     * overrides the base output of the ListCell for the custom cell/pane
     * @param object object entity that will be taken in by the listCell
     * @param empty boolean that checks if the listCell is empty
     */
    @Override
    protected void updateItem(T object, boolean empty) {
        super.updateItem(object, empty);
        if (empty) {
            setGraphic(null);
        } else {
            //place all the initialization methods for the controller of the custom cell/pane in initializeCellMenu
            initializeCellMenu(object);
        }
    }

    protected abstract void initializeCellMenu(T object);
}
