package Layer3.Controllers.Cells;

import Layer1.Entities.AdminNotification;
import Layer3.Controllers.AdminNotificationCellMenu;
import Layer3.Repos.*;
import Layer3.Repos.JavaKeyStore.JavaKeyStoreRepo;

/**
 * A cell class which intakes the AdminNotification entity
 */
public class AdminNotificationCell extends AbstractCell<AdminNotification> {

    /**
     * constructs a ListCell for adminNotifications
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
    public AdminNotificationCell(BasicUserRepo buRepo, AdminUserRepo auRepo, ItemsRepo iRepo,
                                 TransactionsRepo tRepo, AdminNotificationRepo anRepo, ImageRepo imageRepo,
                                 DemoUserRepo duRepo, JavaKeyStoreRepo jksRepo){
        super(buRepo, auRepo, iRepo, tRepo, anRepo, imageRepo, duRepo, jksRepo);
    }

    /**
     * overrides to display unique List Cell based on AdminNotification
     * @param object the AdminNotification entity that the listCell will intake
     */
    @Override
    protected void initializeCellMenu(AdminNotification object) {
        AdminNotificationCellMenu adminNotificationCellMenu =
                new AdminNotificationCellMenu(this.getClass().getSimpleName() + "Menu", object);
        adminNotificationCellMenu.setRepos(buRepo, auRepo, iRepo, tRepo, anRepo, imageRepo, duRepo, jksRepo);
        adminNotificationCellMenu.init();
        setGraphic(adminNotificationCellMenu.getDisplay());
    }
}
