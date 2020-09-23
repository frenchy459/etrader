package Layer3.Controllers;

import Layer1.Entities.AdminUser;
import Layer1.Entities.BasicUser;
import Layer1.Entities.DemoUser;
import Layer1.Entities.Transaction;
import Layer1.Enums.CellType;
import Layer1.Enums.TransactionStatus;
import Layer1.Enums.WindowType;
import Layer2.Managers.RatingManager;
import Layer2.Managers.UserManager;
import Layer3.Controllers.Cells.BasicUserCellFactory;
import Layer3.Controllers.Cells.TransactionCellFactory;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import java.util.*;

/**
 * A transaction history menu controller class responsible for handling user inputs at the transaction history menu page
 */
public class TransactionHistoryMenu extends AbstractController {

    @FXML
    public AnchorPane anchor;
    @FXML
    private Text userName;
    @FXML
    public JFXButton backButton;
    @FXML
    public Text transactionHistoryHeader;
    @FXML
    private ListView<Transaction> transactionView;
    @FXML
    public Text tradingPartnerHeader;
    @FXML
    private ListView<BasicUser> tradingPartnerView;
    @FXML
    private Button reviewButton;

    private UserManager userManager;
    private BasicUser currentUser;
    private boolean isUserMenu;
    private final ObservableList<Transaction> transactionsList = FXCollections.observableArrayList();
    private final ObservableList<BasicUser> tradingPartnerList = FXCollections.observableArrayList();

    /**
     * Initialize the transaction history menu object
     * @param um an userManager object
     * @param basicUser a basic user object
     * @param isUserMenu a boolean representing whether the menu is for basic user/ admin user
     */
    public void init(UserManager um, BasicUser basicUser, boolean isUserMenu) {
        this.userManager = um;
        this.currentUser = basicUser;
        this.isUserMenu = isUserMenu;
        setUserNameText();
        if (um.getCurrentUserObject() instanceof AdminUser)
            reviewButton.setVisible(false);

        populateTransactionHistory();

        if (currentUser instanceof DemoUser)
            tradingPartnerList.add(demoUserRepo.get("demo2"));
        else
            generateTradingPartners();

        populateTradingPartners();
    }

    private void setUserNameText() {
        userName.setText(currentUser.getUsername());
    }

    /** Takes the current user back to the previous page. Distinguishes between normal users and admins and calls up
     * relevant menus.
     * @param actionEvent an actionEvent object */
    @FXML
    private void requestBack(ActionEvent actionEvent) {
        if (isUserMenu) {
            UserMenu um = loadFXML(actionEvent, json.getString("userMenuFXML"), WindowType.SCENE).getController();
            um.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                    demoUserRepo, javaKeyStoreRepo);
            um.init(userManager);
        } else {
            AdministrateUserMenu aum = loadFXML(actionEvent, json.getString("administrateUserMenuFXML"), WindowType.SCENE).getController();
            aum.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                    demoUserRepo, javaKeyStoreRepo);
            aum.init(userManager, currentUser);
        }
    }

    /** Requests that a transaction history item be rated / reviewed by the current user. */
    @FXML
    private void requestReview() {
        ObservableList<Transaction> selected = transactionView.getSelectionModel().getSelectedItems();

        if (selected.isEmpty() || !(selected.get(0).getStatus().equals(TransactionStatus.COMPLETE))) {
            showAlert(Alert.AlertType.ERROR, json.getString("sceneErrorTitle"), json.getString("selectCompleteTransaction"));
            return;
        }

        Transaction transaction = selected.get(0);
        String tradingPartner = transaction.getTradingPartnerName(currentUser.getUsername());

        if (transaction.hasBeenReviewed(tradingPartner)) {
            showAlert(Alert.AlertType.ERROR, json.getString("sceneErrorTitle"), json.getString("alreadyReviewed"));
        } else {
            RatingManager ratingManager = new RatingManager();
            ChoiceDialog<Double> input = new ChoiceDialog<>(0.0, ratingManager.getRatingChoices(0.0, 5.0, 0.5));
            input.setTitle(json.getString("reviewTitle"));
            input.setHeaderText(json.getString("reviewHeader"));
            input.setContentText(json.getString("reviewContext"));

            Optional<Double> result = input.showAndWait();
            result.ifPresent(aDouble -> transaction.setReviews(tradingPartner, aDouble));
            if (basicUserRepo.get(tradingPartner) == null)
                ratingManager.updateRating(demoUserRepo.get(tradingPartner), transaction);
            else
                ratingManager.updateRating(basicUserRepo.get(tradingPartner), transaction);
        }
    }

    private void populateTransactionHistory() {
        ArrayList<UUID> th = (ArrayList<UUID>) currentUser.getTransactionHistory();
        Transaction transaction;
        for (UUID id : th) {
            transaction = transactionsRepo.get(id);
            transactionsList.add(transaction);
        }

        transactionView.setItems(transactionsList);
        transactionView.setCellFactory(new TransactionCellFactory(basicUserRepo, adminUserRepo, itemsRepo,
                transactionsRepo, adminNotificationRepo, imageRepo, demoUserRepo, javaKeyStoreRepo, userManager,
                currentUser.getUsername(), CellType.VIEW_DETAILS));

    }

    private void generateTradingPartners() {
                HashMap<String, Integer> tradingCounter = new HashMap<>();
                String tradingPartner;
                Transaction transaction;

                for (UUID id: currentUser.getTransactionHistory()) {
                    transaction = transactionsRepo.get(id);

            if (!(transaction.getStatus().equals(TransactionStatus.COMPLETE)))
                continue;

            tradingPartner = transaction.getTradingPartnerName(currentUser.getUsername());
            tradingCounter.put(tradingPartner, tradingCounter.get(tradingPartner) == null ? 1 :
                    tradingCounter.get(tradingPartner) + 1);
        }

        int counter = 0;
        while (counter < 3 && !tradingCounter.isEmpty()) {
            Map.Entry<String, Integer> maxEntry = null;
            for (Map.Entry<String, Integer> entry : tradingCounter.entrySet())
                if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
                    maxEntry = entry;
            assert maxEntry != null;
            tradingPartnerList.add(basicUserRepo.get(maxEntry.getKey()));
            tradingCounter.remove(maxEntry.getKey());
            counter++;
        }
    }

    private void populateTradingPartners() {
        tradingPartnerView.setItems(tradingPartnerList);
        tradingPartnerView.setCellFactory(new BasicUserCellFactory(basicUserRepo, adminUserRepo, itemsRepo,
                transactionsRepo, adminNotificationRepo, imageRepo, demoUserRepo, javaKeyStoreRepo, userManager));
    }
}
