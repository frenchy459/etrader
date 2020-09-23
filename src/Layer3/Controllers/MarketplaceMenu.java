package Layer3.Controllers;

import Layer1.Entities.BasicUser;
import Layer1.Entities.DemoUser;
import Layer1.Entities.Item;
import Layer1.Enums.AccountStatus;
import Layer1.Enums.CellType;
import Layer1.Enums.Sorter;
import Layer1.Enums.WindowType;
import Layer2.API.MapAPI.errors.ApiException;
import Layer2.Managers.UserManager;
import Layer2.Sort.*;
import Layer3.Controllers.Cells.ItemCellFactory;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * A market place menu controller class responsible for handling marketplace displays
 */
public class MarketplaceMenu extends AbstractController {

    @FXML
    public Text titleText;
    @FXML
    public JFXButton backButton;
    @FXML
    public JFXButton filterButton;
    @FXML
    public JFXButton addItemButton;
    @FXML
    private AnchorPane anchor;
    @FXML
    private Text currentCity;
    @FXML
    private ListView<Item> listView;
    @FXML
    private final ChoiceBox<Sorter> filter = new ChoiceBox<>();

    private UserManager userManager;
    private BasicUser currentUser;
    private final ObservableList<Item> marketplaceItemList = FXCollections.observableArrayList();
    ArrayList<Item> showing = new ArrayList<>();

    /**
     * Initialize the market place menu object
     * @param um an userManager object
     */
    public void init(UserManager um) {
        this.userManager = um;
        populateFilter();
        setCityText();
        this.currentUser = (BasicUser) um.getCurrentUserObject();
        listView.setFocusTraversable(false);
        populateMarket();
    }

    @FXML
    private void populateFilter() {
        ObservableList<Sorter> sortList = FXCollections.observableArrayList();
        filter.setValue(Sorter.ALL);
        filter.setId("filter");
        filter.setLayoutX(165);
        filter.setLayoutY(168);
        filter.setPrefHeight(40);
        filter.setPrefWidth(220);
        anchor.getChildren().add(filter);

        // Set the list of SORT enums to the ChoiceBox
        sortList.addAll(Arrays.asList(Sorter.values()));
        filter.setItems(sortList);
        File cssFile = new File(json.getString("cssPath"));
        filter.getStylesheets().add(cssFile.toURI().toString());
        filter.getStyleClass().add("text");

    }

    /**
     * Gets the user's choice of filter and display the items that matches the filtering criteria
     * @throws InterruptedException an interrupted exception
     * @throws ApiException an map API exception
     * @throws IOException an IO exception
     */
    @FXML
    public void requestFilter() throws InterruptedException, ApiException, IOException {
        getItems(filter.getValue());

    }

    private void getItems(Sorter sort) throws InterruptedException, ApiException, IOException {
        showing.clear();
        showing.addAll(getMarketplaceItemList());
        ObservableList<Item> currentItemList = FXCollections.observableArrayList();
        listView.getItems().clear();
        currentItemList.clear();
        switch (sort) {
            case ALPHABET: {
                currentItemList.addAll(new AlphabeticalSort().sort(showing));
                break;
            }
            case CITY: {
                currentItemList.addAll(new CitySort(currentUser, basicUserRepo, itemsRepo).sort(showing));
                break;
            }
            case NEWEST: {
                currentItemList.addAll(new DateAddedSort().sort(showing));
                break;
            }
            case DISTANCE_15KM: {
                currentItemList.addAll(new DistanceSort(currentUser, basicUserRepo, itemsRepo, 15).sort(showing));
                break;
            }
            case DISTANCE_30KM: {
                currentItemList.addAll(new DistanceSort(currentUser, basicUserRepo, itemsRepo, 30).sort(showing));
                break;
            }
            case DRIVING_30MIN: {
                currentItemList.addAll(new DurationSort(currentUser, basicUserRepo, itemsRepo, 30).sort(showing));
                break;
            }
            case DRIVING_1HR: {
                currentItemList.addAll(new DurationSort(currentUser, basicUserRepo, itemsRepo, 60).sort(showing));
                break;
            }
            case WISHLIST:{
                currentItemList.addAll(new WishlistSort(basicUserRepo).sort(showing));
                break;
            }
            default:
                currentItemList = getMarketplaceItemList();
                break;
        }
        listView.setItems(currentItemList);
}

    /**
     * A javafx button responsible for reverse the current list
     * @throws InterruptedException an interrupted exception
     * @throws ApiException an API exception
     * @throws IOException an IO exception
     */
    @FXML
    private void reverse() throws InterruptedException, ApiException, IOException {
        Sorter sort = filter.getValue();
        showing.clear();
        showing.addAll(getMarketplaceItemList());
        ObservableList<Item> currentItemList = FXCollections.observableArrayList();
        listView.getItems().clear();
        currentItemList.clear();

        ArrayList<Item> sorted = new ArrayList<>(getMarketplaceItemList());

        switch (sort) {
            case ALPHABET: {
                AlphabeticalSort alphabeticalSort = new AlphabeticalSort();
                sorted = alphabeticalSort.sort(showing);
                alphabeticalSort.reverseSort(sorted);
                break;
            }
            case CITY: {
                CitySort citySort = new CitySort(currentUser, basicUserRepo, itemsRepo);
                sorted = citySort.sort(showing);
                citySort.reverseSort(sorted);
                break;
            }
            case NEWEST: {
                DateAddedSort dateAddedSort = new DateAddedSort();
                sorted = dateAddedSort.sort(showing);
                dateAddedSort.reverseSort(sorted);
                break;
            }
            case DISTANCE_15KM: {
                DistanceSort distanceSort = new DistanceSort(currentUser, basicUserRepo, itemsRepo, 15);
                sorted = distanceSort.sort(showing);
                distanceSort.reverseSort(sorted);
                break;
            }
            case DISTANCE_30KM: {
                DistanceSort distanceSort = new DistanceSort(currentUser, basicUserRepo, itemsRepo, 30);
                sorted = distanceSort.sort(showing);
                distanceSort.reverseSort(sorted);
                break;
            }
            case DRIVING_30MIN: {
                DurationSort durationSort = new DurationSort(currentUser, basicUserRepo, itemsRepo, 30);
                sorted = durationSort.sort(showing);
                durationSort.reverseSort(sorted);
                break;
            }
            case DRIVING_1HR: {
                DurationSort durationSort = new DurationSort(currentUser, basicUserRepo, itemsRepo, 60);
                sorted = durationSort.sort(showing);
                durationSort.reverseSort(sorted);
                break;
            }
            case WISHLIST: {
                WishlistSort wishlistSort = new WishlistSort(basicUserRepo);
                sorted = wishlistSort.sort(showing);
                wishlistSort.reverseSort(sorted);
                break;
            }
            default:
                sorted.addAll(getMarketplaceItemList());
                Collections.reverse(sorted);
                break;
        }
        currentItemList.addAll(sorted);
        listView.setItems(currentItemList);
    }

    /**
     * Display the current user's city
     */
    public void setCityText() {
        currentCity.setText(((BasicUser) userManager.getCurrentUserObject()).getCity().replaceAll("^\"+|\"+$", ""));
    }

    /**
     * A javafx button responsible for taking user to the previous page
     * @param actionEvent an action event representing a user action
     */
    @FXML
    public void requestBack(ActionEvent actionEvent) {
        UserMenu um = loadFXML(actionEvent, json.getString("userMenuFXML"), WindowType.SCENE).getController();
        um.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);
        um.init(userManager);
    }

    /**
     * an javafx button responsible for adding selected item objects to the current user's wishlist
     */
    public void requestAddItems() {
        //gets the currently selected items
        ObservableList<Item> selected = listView.getSelectionModel().getSelectedItems();
        List<Item> toRemove = new ArrayList<>();
        for (Item item : selected) {
            currentUser.getWishList().add(item.getUUID());
            if (currentUser instanceof DemoUser)
                break;
            item.getWishListedBy().add(currentUser.getUsername());
            toRemove.add(item);
        }
        if (selected.isEmpty()){
            showAlert(Alert.AlertType.ERROR, json.getString("sceneErrorTitle"), json.getString("noSelectedItems"));
        } else{
            //removes the item from the items currently shown in the marketplace
            //avoids needing to re-call populateMarket
            marketplaceItemList.removeAll(toRemove);
            showAlert(Alert.AlertType.INFORMATION,
                    json.getString("sceneSuccessTitle"), json.getString("addWishlist"));
            setCityText();
            populateMarket();
        }
    }

    private void populateMarket() {
        getMarketplaceItemList();
        //adds all items to the List View's item pool
        listView.setItems(marketplaceItemList);
        //sets the factory for the custom list cells
        //We'll have to carry the Repos so that we can access them in the custom Cell
        listView.setCellFactory(new ItemCellFactory(basicUserRepo, adminUserRepo, itemsRepo,
                transactionsRepo, adminNotificationRepo, imageRepo, demoUserRepo, javaKeyStoreRepo, CellType.MARKETPLACE));
        //this lets you multi-select items with shift + click or ctrl + click
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private ObservableList<Item> getMarketplaceItemList(){
        //extracts any item that isn't in the current user's wishlist or inventory
        marketplaceItemList.clear();

        for (Item item : itemsRepo.getAll()) {
            BasicUser basicUser = basicUserRepo.get(item.getOriginalOwner());
            if (basicUser != null && !basicUser.equals(currentUser)
                    && !basicUser.getAccountStatus().equals(AccountStatus.INACTIVE)
                    && !currentUser.getWishList().contains(item.getUUID()) && item.getIsApproved()) {
                marketplaceItemList.add(item);
            }
        }
        return marketplaceItemList;
    }
}
