package Layer3.Controllers.Cells;

import Layer1.Entities.AdminNotification;
import Layer3.Repos.*;
import Layer3.Repos.JavaKeyStore.JavaKeyStoreRepo;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

/**
 * an admin notification cell factory class
 */
public class AdminNotificationCellFactory extends AbstractCellFactory<AdminNotification> {

    /**
     * constructs a ListCellFactory for adminNotifications
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
    public AdminNotificationCellFactory(BasicUserRepo buRepo, AdminUserRepo auRepo, ItemsRepo iRepo,
                                        TransactionsRepo tRepo, AdminNotificationRepo anRepo, ImageRepo imageRepo,
                                        DemoUserRepo duRepo, JavaKeyStoreRepo jksRepo){
        super(buRepo, auRepo, iRepo, tRepo, anRepo, imageRepo, duRepo, jksRepo);
    }

    /**
     * overrides call method that searches for the format so a custom AdminNotification listCell can be used
     * @param param represents the AdminNotification ListView type
     */
    @Override
    public ListCell<AdminNotification> call(ListView<AdminNotification> param) {
        return new AdminNotificationCell(buRepo, auRepo, iRepo, tRepo, anRepo, imageRepo, duRepo, jksRepo);
    }
}
