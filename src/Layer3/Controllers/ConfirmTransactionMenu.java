package Layer3.Controllers;

import Layer1.Entities.*;
import Layer1.Enums.CellType;
import Layer1.Enums.TransactionStatus;
import Layer2.Managers.AccountStateManager;
import Layer2.Managers.BasicUserManager;
import Layer2.Managers.TransactionManager;
import Layer2.Managers.UserManager;
import Layer3.Controllers.Cells.TransactionCellFactory;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConfirmTransactionMenu extends AbstractController {
    @FXML
    public AnchorPane anchor;
    @FXML
    public Label infoLabel;
    @FXML
    public JFXButton confirmTransactionButton;
    @FXML
    public JFXButton cancelTransactionButton;
    @FXML
    private ListView<Transaction> transactionListView;

    private UserManager userManager;
    private BasicUser currentUser;
    private TransactionManager transactionManager;
    private BasicUserManager basicUserManager;
    private final AccountStateManager accountStateManager = new AccountStateManager();
    private final ObservableList<Transaction> transactionsList = FXCollections.observableArrayList();

    public void init(UserManager um, BasicUser basicUser, TransactionManager transactionManager) {
        this.userManager = um;
        this.currentUser = basicUser;
        this.transactionManager = transactionManager;
        this. basicUserManager = new BasicUserManager(adminNotificationRepo);
        populateTransactionHistory();
        denyExit();
    }

    private void populateTransactionHistory() {
        ArrayList<UUID> th = (ArrayList<UUID>) currentUser.getTransactionHistory();
        for (UUID id : th) {
            Transaction transaction = transactionsRepo.get(id);
            // Checks if the transaction is awaiting the initial meeting + the user hasn't confirmed it yet OR
            // if the transaction is awaiting the return meeting + user hasn't confirmed it yet
            // if it's either, add it to the list
            if ((transaction.getStatus().equals(TransactionStatus.PENDING_MEETING_CONFIRMATION)
                    && !transaction.getUserHasConfirmedMeeting(currentUser.getUsername(), 1))
                    | ((transaction.getStatus().equals(TransactionStatus.PENDING_RETURN_MEETING_CONFIRMATION))
                && !transaction.getUserHasConfirmedMeeting(currentUser.getUsername(), 2)))  {
                transactionsList.add(transaction);
            }
        }
        transactionListView.setItems(transactionsList);
        transactionListView.setCellFactory(new TransactionCellFactory(basicUserRepo, adminUserRepo, itemsRepo,
                transactionsRepo, adminNotificationRepo, imageRepo, demoUserRepo, javaKeyStoreRepo, userManager,
                currentUser.getUsername(), CellType.VIEW_DETAILS));
        transactionListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /** Confirms that all selected transactions took place. */
    @FXML
    public void requestConfirmTransactions() {
        ObservableList<Transaction> selected = transactionListView.getSelectionModel().getSelectedItems();
        if (selected.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, Alert.AlertType.ERROR.toString(),
                    json.getString("noSelectedTrade"));
        } else {
            List<Transaction> toRemove = new ArrayList<>();
            for (Transaction transaction : selected) {
                toRemove.add(transaction);
                //if the first meeting hasn't been confirmed (PERM MEET OR TEMP INIT MEET)
                if (!transaction.getUserHasConfirmedMeeting(currentUser.getUsername(), 1)) {
                    transactionManager.updateMeetingConfirmation(currentUser, transaction, 1);
                }
                //otherwise if the trade is a TEMP and the 2nd meeting hasn't been confirmed
                else if (transaction.getIsTemporary() &&
                        !transaction.getUserHasConfirmedMeeting(currentUser.getUsername(), 2)) {
                    transactionManager.updateMeetingConfirmation(currentUser, transaction, 2);
                }

                //if the transaction is confirmed, then update relevant trackers
                if (transactionManager.confirm(transaction)) {
                    for (String userName: transaction.getParticipants()) {
                        basicUserManager.updateUserAfterConfirmedTrade(basicUserRepo.get(userName));
                        accountStateManager.upgradeToTrusted(basicUserRepo.get(userName));
                    }
                }
            }
            transactionsList.removeAll(toRemove);
        }
    }

    /** Cancels all selected transactions. */
    @FXML
    public void requestCancelTransactions() {
        ObservableList<Transaction> selected = transactionListView.getSelectionModel().getSelectedItems();
        if (selected.isEmpty()){
            showAlert(Alert.AlertType.ERROR, json.getString("sceneErrorTitle"), json.getString("noSelectedTrade"));
        } else {
            List<Transaction> handledTransactions = new ArrayList<>();
            for (Transaction transaction : selected) {
                handledTransactions.add(transaction);
                transactionManager.cancel(transaction);
                //for each user, adds to their incomplete transactions amount & checks if they're over their threshold
                for (String userName: transaction.getParticipants()) {
                    BasicUser user  = basicUserRepo.get(userName);
                    basicUserManager.setNumIncompleteTransactions(user,
                            basicUserManager.getNumIncompleteTransactions(user) + 1);
                    basicUserManager.compareIncompleteTransactionWithThreshold(user);
                }
            }
            transactionsList.removeAll(handledTransactions);
        }
    }

    //stops the user from exiting the window before they've dealt w/ all the transactions
    private void denyExit() {
        Stage stage = (Stage) transactionListView.getScene().getWindow();
        stage.setOnCloseRequest(event -> {
            if (!transactionsList.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, json.getString("sceneErrorTitle"),
                        json.getString("denyExitTrade"));
                event.consume();
            } else {
                stage.close();
            }
        });
    }
}
