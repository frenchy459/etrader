package Layer3.Controllers;

import Layer1.Entities.Transaction;
import Layer1.Enums.EditTradeMenuState;
import Layer1.Enums.CellType;
import Layer1.Enums.WindowType;
import Layer2.Managers.UserManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.time.format.DateTimeFormatter;

/**
 * A transaction cell menu controller class
 */
public class TransactionCellMenu extends AbstractCellMenu {

    @FXML
    Label tradeType;
    @FXML
    Label partnerName;
    @FXML
    Label meetingDate;
    @FXML
    Button detailsButton;
    @FXML
    Button handleTradeRequestButton;
    @FXML
    Label transactionStatus;

    private final Transaction transaction;
    private final String currentUserName;
    private final CellType cellType;
    private final UserManager userManager;

    public TransactionCellMenu(String path, Transaction transaction, String currentUserName, UserManager userManager,
                               CellType cellType) {
        super(path);
        this.transaction = transaction;
        this.currentUserName = currentUserName;
        this.cellType = cellType;
        this.transactionStatus.setText(transaction.getStatus().toString());
        this.userManager = userManager;
    }

    @Override
    public void init() {
        clickableIndicator();
        String tradeDuration = transaction.getIsTemporary() ? "Temporary" : "Permanent";
        String tradeDirection = transaction.getIsOneWay() ? " One-way" : " Two-way";
        tradeType.setText(tradeDuration + tradeDirection);

        partnerName.setText(transaction.getTradingPartnerName(currentUserName));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy'-'MM'-'dd' 'kk:mm");
        meetingDate.setText(transaction.getMeetingDateInitial().format(dateTimeFormatter));
        transactionStatus.setText(transaction.getStatus().toString().replace("_", " "));

        if (cellType.equals(CellType.HANDLE_TRADE_REQUEST)) {
            setButton(handleTradeRequestButton);
        } else {
            setButton(detailsButton);
        }
    }

    private void setButton(Button button) {
        button.setVisible(true);
        button.setOnAction(event -> {
            if (cellType == CellType.VIEW_DETAILS) {
                TransactionInfoMenu transactionInfoMenu =
                        loadFXML(event, json.getString("transactionInfoMenuFXML"), WindowType.POPUP).getController();
                transactionInfoMenu.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo,
                        adminNotificationRepo, imageRepo, demoUserRepo, javaKeyStoreRepo);
                transactionInfoMenu.init(transaction);
            } else {
                EditTradeMenu editTradeMenu =
                        loadFXML(event, json.getString("editTradeMenuFXML"), WindowType.SCENE).getController();
                editTradeMenu.setMenuState(EditTradeMenuState.EXISTING);
                editTradeMenu.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo,
                        imageRepo, demoUserRepo, javaKeyStoreRepo);
                editTradeMenu.setUser((basicUserRepo.get(currentUserName)));
                editTradeMenu.setTransaction(transaction);
                editTradeMenu.init(userManager);
                editTradeMenu.populateDataFromTransaction(transaction);
            }
        });
    }
}
