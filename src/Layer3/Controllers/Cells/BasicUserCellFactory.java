package Layer3.Controllers.Cells;

import Layer1.Entities.BasicUser;
import Layer2.Managers.UserManager;
import Layer3.Repos.*;
import Layer3.Repos.JavaKeyStore.JavaKeyStoreRepo;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

/**
 * A basic user cell factory class
 */
public class BasicUserCellFactory extends AbstractCellFactory<BasicUser> {

    private final UserManager userManager;

    /**
     * constructs a ListCellFactory for BasicUsers
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
     * @param userManager  a userManager class containing methods related to User entities
     */
    public BasicUserCellFactory(BasicUserRepo buRepo, AdminUserRepo auRepo, ItemsRepo iRepo,
                                TransactionsRepo tRepo, AdminNotificationRepo anRepo, ImageRepo imageRepo,
                                DemoUserRepo duRepo, JavaKeyStoreRepo jksRepo, UserManager userManager){
        super(buRepo, auRepo, iRepo, tRepo, anRepo, imageRepo, duRepo, jksRepo);

        this.userManager = userManager;
    }

    /**
     * overrides call method that searches for the format so a custom BasicUser listCell can be used
     * @param param represents the BasicUser ListView type
     */
    @Override
    public ListCell<BasicUser> call(ListView<BasicUser> param) {
        return new BasicUserCell(buRepo, auRepo, iRepo, tRepo, anRepo, imageRepo, duRepo, jksRepo, userManager);
    }
}
