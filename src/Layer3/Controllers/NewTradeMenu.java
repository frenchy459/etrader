package Layer3.Controllers;

import Layer1.Entities.BasicUser;
import Layer1.Entities.DemoUser;
import Layer1.Entities.Item;
import Layer1.Enums.TradeEntrypoint;
import Layer1.Enums.EditTradeMenuState;
import Layer1.Enums.WindowType;
import Layer2.Managers.UserManager;
import Layer3.Controllers.Cells.PossibleTradeCellFactory;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import java.util.HashMap;

/**
 * A new trade menu controller class responsible for handling user inputs at the new trade menu page
 */
public class NewTradeMenu extends AbstractController {

    @FXML
    public AnchorPane anchor;
    @FXML
    public Text titleText;
    @FXML
    public JFXButton backButton;
    @FXML
    public JFXButton wishlistButton;
    @FXML
    public JFXButton inventoryButton;
    @FXML
    private Text userName;
    @FXML
    private ListView<HashMap<Item, BasicUser>> listViewWishlist;
    @FXML
    private ListView<HashMap<Item, BasicUser>> listViewInventory;

    private UserManager userManager;
    private BasicUser currentUser;
    private final ObservableList<HashMap<Item, BasicUser>> tradesWL = FXCollections.observableArrayList();
    private final ObservableList<HashMap<Item, BasicUser>> tradesI = FXCollections.observableArrayList();
    private HashMap<Item, BasicUser> chosenTrade;

    /**
     * A javafx button responsible for taking user to the previous page
     * @param actionEvent an action event
     */
    @FXML
    private void requestBack(ActionEvent actionEvent) {
        UserMenu um = loadFXML(actionEvent, json.getString("userMenuFXML"), WindowType.SCENE).getController();
        um.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);
        um.init(userManager);
    }

    /**
     * Initialize the new trade menu object
     * @param userManager an userManager object
     */
    public void init(UserManager userManager) {
        setUserManager(userManager);
        currentUser = (BasicUser) userManager.getCurrentUserObject();
        setUserName();
        listViewWishlist.setFocusTraversable(false);
        listViewInventory.setFocusTraversable(false);
        populateLists();
    }

    private void populateLists(){
        if (userManager.getCurrentUserObject() instanceof DemoUser) {
            tradesWL.addAll(userManager.getTradeListDemo(TradeEntrypoint.WISHLIST));
            tradesI.addAll(userManager.getTradeListDemo(TradeEntrypoint.INVENTORY));
        } else {
            tradesWL.addAll(userManager.getTradeList(TradeEntrypoint.WISHLIST));
            tradesI.addAll(userManager.getTradeList(TradeEntrypoint.INVENTORY));
        }

        listViewWishlist.setItems(tradesWL);
        listViewWishlist.setCellFactory(new PossibleTradeCellFactory(basicUserRepo, adminUserRepo, itemsRepo,
                transactionsRepo, adminNotificationRepo, imageRepo, demoUserRepo, javaKeyStoreRepo));

        listViewInventory.setItems(tradesI);
        listViewInventory.setCellFactory(new PossibleTradeCellFactory(basicUserRepo, adminUserRepo, itemsRepo,
                transactionsRepo, adminNotificationRepo, imageRepo, demoUserRepo, javaKeyStoreRepo));
    }

    /**
     * A javafx button responsible for initiating a new trade from the wishlist
     * @param actionEvent an actionEvent object
     */
    @FXML
    private void requestWishListTrade(ActionEvent actionEvent){
        chosenTrade = listViewWishlist.getSelectionModel().getSelectedItem();
        // can directly create a wishlist based transaction from here and pass it into EditTradeMenu
        // ie. a transaction that has the user's item as NULL
        initiateTrade(chosenTrade, actionEvent);
    }

    /**
     * A javafx button responsible for initiating a new trade from the inventory
     * @param actionEvent an actionEvent object
     */
    @FXML
    private void requestInventoryTrade(ActionEvent actionEvent){
        chosenTrade = listViewInventory.getSelectionModel().getSelectedItem();
        // can directly create an inventory based transaction from here and pass it into EditTradeMenu
        // ie. a transaction that has the trading partner's item as NULL
        initiateTrade(chosenTrade, actionEvent);
    }

    private void initiateTrade(HashMap<Item, BasicUser> chosenTrade, ActionEvent actionEvent) {
        if (chosenTrade != null){
            EditTradeMenu editTradeMenu = loadFXML(actionEvent, json.getString("editTradeMenuFXML"), WindowType.SCENE).getController();
            editTradeMenu.setMenuState(EditTradeMenuState.NEW);
            editTradeMenu.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                    demoUserRepo, javaKeyStoreRepo);
            editTradeMenu.setUser((BasicUser) userManager.getCurrentUserObject());
            editTradeMenu.setChosenTrade(chosenTrade);
            editTradeMenu.init(userManager);
        } else { showAlert(Alert.AlertType.ERROR, json.getString("sceneErrorTitle"), json.getString("noSelectedTrade")); }
    }

    private void setUserManager(UserManager userManager){ this.userManager = userManager; }

    private void setUserName() { userName.setText(currentUser.getUsername()); }
}
