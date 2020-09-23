package Layer3.Controllers;

import Layer1.Entities.BasicUser;
import Layer1.Entities.Transaction;
import Layer1.Enums.TransactionStatus;
import Layer1.Enums.CellType;
import Layer1.Enums.WindowType;
import Layer2.Managers.UserManager;
import Layer3.Controllers.Cells.TransactionCellFactory;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Represents an incoming request menu controller class
 */
public class IncomingRequestsMenu extends AbstractController {

    @FXML
    public AnchorPane anchor;
    @FXML
    public Text transactionTitle;
    @FXML
    public JFXButton backButton;
    @FXML
    private ListView<Transaction> transactionView;
    @FXML
    private Text userName;

    private UserManager userManager;
    private BasicUser currentUser;
    private final ObservableList<Transaction> transactionsList = FXCollections.observableArrayList();

    /**
     * Initialize an incoming request menu
     *
     * @param userManager an userManager object
     * @param basicUser   a basic user object
     */
    public void init(UserManager userManager, BasicUser basicUser) {
        setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo,
                adminNotificationRepo, imageRepo, demoUserRepo, javaKeyStoreRepo);
        this.userManager = userManager;
        this.currentUser = basicUser;
        transactionView.setFocusTraversable(false);
        setUserNameText();
        populateIncomingRequests();
    }

    private void setUserNameText() {
        userName.setText(currentUser.getUsername());
    }

    /**
     * A javafx button responsible for taking user to the previous page
     *
     * @param actionEvent an actionEvent object
     */
    @FXML
    public void requestBack(ActionEvent actionEvent) {
        UserMenu userMenu = loadFXML(actionEvent, json.getString("userMenuFXML"), WindowType.SCENE).getController();
        userMenu.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);
        userMenu.init(userManager);
    }

    private void populateIncomingRequests() {

        ArrayList<UUID> transactionHistory = (ArrayList<UUID>) currentUser.getTransactionHistory();
        Transaction transaction;

        for (int i = transactionHistory.size() - 1; i >= 0; i--) {
            transaction = transactionsRepo.get(transactionHistory.get(i));
            if (transaction.getStatus().equals(TransactionStatus.PENDING_APPROVAL)) {
                boolean condition1 = transaction.getStatus() == TransactionStatus.PENDING_APPROVAL;
                boolean condition2 = transaction.getLastEditor().equals(currentUser.getUsername());
                if (condition1 && !condition2) {
                    transactionsList.add(transaction);
                }
            }

            transactionView.setItems(transactionsList);
            transactionView.setCellFactory(new TransactionCellFactory(basicUserRepo, adminUserRepo, itemsRepo,
                    transactionsRepo, adminNotificationRepo, imageRepo, demoUserRepo, javaKeyStoreRepo, userManager,
                    currentUser.getUsername(), CellType.HANDLE_TRADE_REQUEST));
        }
    }
}
