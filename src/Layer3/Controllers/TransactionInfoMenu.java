package Layer3.Controllers;

import Layer1.Entities.BasicUser;
import Layer1.Entities.Item;
import Layer1.Entities.Transaction;
import Layer3.Controllers.Cells.TransactionItemCellFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.*;

/** A transaction info menu controller class responsible for displaying details about the transaction to the user. */
public class TransactionInfoMenu extends AbstractController {
    @FXML
    public AnchorPane anchor;
    @FXML
    public Text titleText;
    @FXML
    public Text tradeTypeField;
    @FXML
    public Text meetingLocationField;
    @FXML
    public Text returnLocationField;
    @FXML
    public Text meetingTimeField;
    @FXML
    public Text returnTimeField;
    @FXML
    private Label meetingPlace;
    @FXML
    private Label tradeType;
    @FXML
    private Label meetingTime;
    @FXML
    private Label returnPlace;
    @FXML
    private Label returnTime;
    @FXML
    private ListView<HashMap<Item, List<BasicUser>>> itemView;
    private Transaction transaction;
    private final ObservableList<HashMap<Item, List<BasicUser>>> tradedItems = FXCollections.observableArrayList();

    public void init(Transaction transaction) {
        this.transaction = transaction;
        setMeetingInfo();
        itemView.setFocusTraversable(false);
        if (transaction.getIsTemporary())
            setReturnInfo();
        populateTradedItems();
        tradeType.setText(transaction.getIsTemporary() ? "Temporary" : "Permanent" +
                (transaction.getIsOneWay() ?" one-way" : " two-way"));
    }

     private void setMeetingInfo() {
        meetingPlace.setText(transaction.getMeetingPlaceInitial());
        meetingTime.setText(transaction.getMeetingDateInitial().toString());
    }

    private void setReturnInfo() {
        returnPlace.setText(transaction.getMeetingPlaceReturn());
        returnTime.setText(transaction.getMeetingDateReturn().toString());
    }

    private void populateTradedItems() {
        HashMap<Item, List<BasicUser>> itemsDetails = new HashMap<>();

        for (String user : transaction.getLendersToItems().keySet()) {
            for (UUID id : transaction.getLendersToItems().get(user)) {
                List<BasicUser> users = new ArrayList<>();
                if ((basicUserRepo.get(user) == null)) {
                    users.add(demoUserRepo.get(user));
                } else {
                    users.add(basicUserRepo.get(user));
                }
                itemsDetails.put(itemsRepo.get(id), users);
            }
        }
        for (String user : transaction.getBorrowersToItems().keySet()) {
            for (UUID id : transaction.getBorrowersToItems().get(user)) {
                if ((basicUserRepo.get(user) == null)) {
                    itemsDetails.get(itemsRepo.get(id)).add(demoUserRepo.get(user));
                } else {
                    itemsDetails.get(itemsRepo.get(id)).add(basicUserRepo.get(user));
                }
            }
        }
        tradedItems.addAll(itemsDetails);

        itemView.setItems(tradedItems);
        itemView.setCellFactory(new TransactionItemCellFactory(basicUserRepo, adminUserRepo, itemsRepo,
                transactionsRepo, adminNotificationRepo, imageRepo, demoUserRepo, javaKeyStoreRepo));
    }
}
