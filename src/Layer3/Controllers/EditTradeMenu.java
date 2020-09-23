package Layer3.Controllers;

import Layer1.Entities.BasicUser;
import Layer1.Entities.DemoUser;
import Layer1.Entities.Item;
import Layer1.Entities.Transaction;
import Layer1.Enums.*;
import Layer2.API.MapAPI.AutoCompleteAddressField;
import Layer2.API.MapAPI.GeoApiContext;
import Layer2.API.MapAPI.GeocodingApi;
import Layer2.API.MapAPI.GeocodingApiRequest;
import Layer2.API.MapAPI.errors.ApiException;
import Layer2.API.MapAPI.gson.Gson;
import Layer2.API.MapAPI.gson.GsonBuilder;
import Layer2.API.MapAPI.model.GeocodingResult;
import Layer2.API.MapAPI.model.LatLng;
import Layer2.Managers.RegistrationManager;
import Layer2.Managers.TransactionManager;
import Layer2.Managers.UserManager;
import Layer3.Controllers.Cells.PossibleTradeCellFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An edit trade menu controller class responsible for handling user inputs to the edit trade menu
 */
public class EditTradeMenu extends AbstractController {

    @FXML
    public AnchorPane anchor;
    @FXML
    public Text tradingPartnerField;
    @FXML
    public Text itemOffersField;
    @FXML
    public Text tradeTypeField;
    @FXML
    public Text initialMeetingPlaceField;
    @FXML
    public Text initialMeetingDateField;
    @FXML
    public Text itemRequestsField;
    @FXML
    private Button backButton;
    @FXML
    private Text titleText;
    @FXML
    private Text userName;

    @FXML
    private Button tradingPartnerButton;
    @FXML
    private Label tradingPartnerLabel;

    @FXML
    private Button desiredItemsButton;
    @FXML
    private Label desiredItemsLabel;

    @FXML
    private Label offeredItemsLabel;
    @FXML
    private Button offeredItemsButton;

    @FXML
    private Button tradeTypeButton;
    @FXML
    private Label tradeTypeLabel;

    @FXML
    private Button meetingPlaceInitialButton;
    @FXML
    private Label meetingPlaceInitialLabel;

    @FXML
    private Button meetingDateInitialButton;
    @FXML
    private Label meetingDateInitialLabel;

    @FXML
    private Button meetingPlaceReturnButton;
    @FXML
    private Label meetingPlaceReturnLabel;

    @FXML
    public Button meetingDateReturnButton;
    @FXML
    private Label meetingDateReturnLabel;

    @FXML
    private Text returnPlaceText;
    @FXML
    private Text returnDateText;

    @FXML
    public Button modifyTradeButton;
    @FXML
    public Button submitTradeButton;
    @FXML
    public Button approveTradeButton;

    private BasicUser user;
    private BasicUser tradingPartnerUser;
    private HashMap<Item, BasicUser> chosenTrade;
    private UserManager userManager;
    private TransactionManager transactionManager;
    private final HashMap<TradeDetail, Object> tradeDetails = new HashMap<>();
    private HashMap<String, List<UUID>> lendersToItems = new HashMap<>();
    private HashMap<String, List<UUID>> borrowersToItems = new HashMap<>();
    private final List<String> participants = new ArrayList<>();
    private EditTradeMenuState menuState;
    private Transaction transaction;
    private boolean existingTradeHasBeenModified;
    private TradeDuration tradeDuration = TradeDuration.TEMPORARY;
    private int numberOfEdits = 0;

    /**
     * Initialize the editTradeMenu object
     * @param userManager an userManager object
     */
    public void init(UserManager userManager) {
        existingTradeHasBeenModified = false;
        setUserManager(userManager);
        toggleTradeProcessingButtons();
        setUser(basicUserRepo.get(userManager.getCurrentUser()));
        transactionManager = new TransactionManager(basicUserRepo, transactionsRepo, demoUserRepo, user.getUsername());
        if (menuState == EditTradeMenuState.NEW) {
            this.tradeTypeLabel.setText("Temporary");
            tradeDuration = TradeDuration.TEMPORARY;
            tradeDetails.put(TradeDetail.TYPE, TradeDuration.TEMPORARY);
            participants.add(userName.getText());
        }
        if (tradingPartnerUser.getAccountStatus().equals(AccountStatus.AWAY)) {
            showAlert(Alert.AlertType.WARNING, "Warning",
                    "Your trading partner has set themselves as away. " +
                            "It may take longer than usual to reply to your trade request!");
        }
    }

    /** Sets menu state to NEW or EXISTING
     *
     * @param menuState a TradeMenuState enum denoting the state of the menu
     */
    public void setMenuState(EditTradeMenuState menuState) {
        this.menuState = menuState;
    }

    /** Sets chosen trade
     *
     * @param chosenTrade a hashmap of an item to a user who either own or want the item
     */
    public void setChosenTrade(HashMap<Item, BasicUser> chosenTrade) {
        tradeDetails.put(TradeDetail.STATUS, TransactionStatus.NEW);
        this.chosenTrade = chosenTrade;
        for (Item item: chosenTrade.keySet()) {
            setTradingPartnerUser(chosenTrade.get(item)); // +
            if (!participants.contains(chosenTrade.get(item).getUsername())) {
               participants.add(chosenTrade.get(item).getUsername());
            }
            if (tradingPartnerUser.getLendList().contains(item.getUUID())) {
                setPartnerItem(item); // +
                List<UUID> tempList = new ArrayList<>();
                tempList.add(item.getUUID());
                lendersToItems.put(tradingPartnerUser.getUsername(), tempList); // +
                borrowersToItems.put(userName.getText(), tempList); // +
            } else {
                setUserItem(item); // +
                List<UUID> tempList = new ArrayList<>();
                tempList.add(item.getUUID());
                lendersToItems.put(userName.getText(), tempList);
                borrowersToItems.put(tradingPartnerUser.getUsername(), tempList);
            }
        }
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        populateDataFromTransaction(transaction);
    }

    public void setUser(BasicUser user) {
        this.user = user;
        userName.setText(user.getUsername());
    }

    private void setTradingPartnerUser(BasicUser user) {
        this.tradingPartnerUser = user;
        tradingPartnerLabel.setText(user.getUsername());
    }

    private void setPartnerItem(Item item) {
        desiredItemsLabel.setText(item.getName());
    }

    private void setUserItem(Item item) {
        offeredItemsLabel.setText(item.getName());
    }

    private void initializeTradingPartnerInfo(BasicUser basicUser) {
        tradingPartnerUser = basicUserRepo.get(transaction.getTradingPartnerName(basicUser.getUsername()));
        tradingPartnerLabel.setText(tradingPartnerUser.getUsername());
    }

    private void initializeTradedItemsInfo(HashMap<String, List<UUID>> tradersToItems) {
        // tradersToItems may be either borrowersToItems or lendersToItems
        for (String userName : tradersToItems.keySet()) {
            if (userName.equals(user.getUsername())) {
                // we get the traded (borrowed/lent) item list
                List<UUID> tradedItemsUUIDList = tradersToItems.get(userName);
                List<String> tradedItemsStringList = new ArrayList<>();
                for (UUID uuid : tradedItemsUUIDList) {
                    tradedItemsStringList.add(itemsRepo.get(uuid).getName());
                }
                // we convert it to a string
                String tradedItemsString = String.join(", ", tradedItemsStringList);
                if (tradersToItems == lendersToItems) {
                    offeredItemsLabel.setText(tradedItemsString);
                } else {
                    desiredItemsLabel.setText(tradedItemsString);
                }
            }
        }
    }

    public void populateDataFromTransaction(Transaction transaction) {
        this.lendersToItems = transaction.getLendersToItems();
        this.borrowersToItems = transaction.getBorrowersToItems();
        this.numberOfEdits = transaction.getNumberOfEdits();

        // initialize trading partner info
        initializeTradingPartnerInfo(user);
        // initialize your requests info
        initializeTradedItemsInfo(borrowersToItems);
        // initialize your offers info
        initializeTradedItemsInfo(lendersToItems);

        // set trade type info
        this.tradeTypeLabel.setText(transaction.getIsTemporary() ? "Temporary" : "Permanent");
        this.menuState = EditTradeMenuState.EXISTING;

        this.meetingPlaceInitialLabel.setText(transaction.getMeetingPlaceInitial());
        this.meetingDateInitialLabel.setText(transaction.getMeetingDateInitial().toString());

        if (transaction.getIsTemporary()) {
            this.tradeDuration = TradeDuration.TEMPORARY;
            this.meetingPlaceReturnLabel.setText(transaction.getMeetingPlaceReturn());
            this.meetingDateReturnLabel.setText(transaction.getMeetingDateReturn().toString());
            this.tradeDetails.put(TradeDetail.INITIAL_DATE, transaction.getMeetingDateInitial().toString());
            this.tradeDetails.put(TradeDetail.INITIAL_LOCATION, transaction.getMeetingPlaceInitial());
            this.tradeDetails.put(TradeDetail.RETURN_LOCATION, transaction.getMeetingDateReturn().toString());
            this.tradeDetails.put(TradeDetail.RETURN_DATE, transaction.getMeetingDateReturn().toString());
            enableReturnButtons();
        } else {
            this.tradeDuration = TradeDuration.PERMANENT;
            this.tradeDetails.put(TradeDetail.INITIAL_DATE, transaction.getMeetingDateInitial().toString());
            this.tradeDetails.put(TradeDetail.INITIAL_LOCATION, transaction.getMeetingPlaceInitial());
            disableReturnButtons();
        }

        this.tradeDetails.put(TradeDetail.STATUS, TransactionStatus.PENDING_APPROVAL);
        this.tradeDetails.put(TradeDetail.INITIAL_LOCATION, transaction.getMeetingPlaceInitial());
        this.tradeDetails.put(TradeDetail.INITIAL_DATE, transaction.getMeetingDateInitial().toString());
        this.tradeDetails.put(TradeDetail.TYPE, tradeDuration);

        this.participants.add(transaction.getTradingPartnerName(user.getUsername()));
    }

    @FXML
    private void changeReceivingItem(){
        indicateTradeModification(); // works
        createItemList(TradeEntrypoint.WISHLIST);
    }

    @FXML
    private void changeTradingItem(){
        indicateTradeModification(); // works
        createItemList(TradeEntrypoint.INVENTORY);
    }

    /** Creates an item list based on what item the user is changing (received or trading)
     * @param tradeEntrypoint an enum value representing the trade entrypoint (wishList or lendList)
     */
    private void createItemList(TradeEntrypoint tradeEntrypoint){
        //creates the window
        Stage window = new Stage ();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setWidth(720); window.setHeight(960);
        window.setTitle("Select an item");

        //creates the button
        Button button = new Button();
        button.setLayoutX(160); button.setLayoutY(305); button.setPrefWidth(150); button.setPrefHeight(30);
        button.setText("Select");

        //creates listView
        ListView<HashMap<Item, BasicUser>> listView = new ListView<>();
        listView.setLayoutX(8); listView.setPrefHeight(300); listView.setPrefWidth(683);

        List<HashMap<Item, BasicUser>> tradeList;
        if (user instanceof DemoUser)
            tradeList = userManager.getTradeListDemo(tradeEntrypoint);
        else
            tradeList = userManager.getTradeList(tradeEntrypoint);

        List<HashMap<Item, BasicUser>> filteredTradeList = new ArrayList<>();

        for (HashMap<Item, BasicUser> tradeMap : tradeList) {
            if (tradeMap.containsValue(tradingPartnerUser)) {
                filteredTradeList.add(tradeMap);
            }
        }

        //populates listView with item cells
        ObservableList<HashMap<Item, BasicUser>> trades = FXCollections.observableArrayList();
        trades.addAll(filteredTradeList);
        listView.setItems(trades);
        listView.setCellFactory(new PossibleTradeCellFactory(basicUserRepo, adminUserRepo, itemsRepo,
                transactionsRepo, adminNotificationRepo, imageRepo, demoUserRepo, javaKeyStoreRepo));

        //creates the scene with an anchorPane + its children
        AnchorPane anchor = new AnchorPane();
        anchor.getChildren().addAll(listView, button);
        Scene listScene = new Scene(anchor);

        window.setScene(listScene);
        window.show();

        //modifies the selected item in the trade on button press
        button.setOnAction(event -> {
            chosenTrade = listView.getSelectionModel().getSelectedItem();
            if(chosenTrade != null) {
                setChosenTrade(chosenTrade);
                window.close();
            } else {
                showAlert(Alert.AlertType.ERROR,
                        json.getString("sceneErrorTitle"), "You haven't selected an item!");
            }
        });
    }

    /**
     * A javafx button responsible for taking user to the previous page
     * @param actionEvent an actionEvent object
     */
    @FXML
    public void requestBack(ActionEvent actionEvent) {
        Optional<ButtonType> selected;
        if (menuState == EditTradeMenuState.NEW) {
            selected = showAlert(Alert.AlertType.CONFIRMATION, "Confirm",
                    "Are you sure you want to go back? \n\n " +
                            "Your current trade request data will not be saved.\n");
            if (selected.isPresent() && selected.get() == ButtonType.OK) {
                loadNewTradeMenu(actionEvent);
            }
        }
        else if (menuState == EditTradeMenuState.EXISTING) {
            selected = showAlert(Alert.AlertType.CONFIRMATION, "Confirm",
                    "Are you sure you want to go back? \n\n " +
                            "No changes will be saved.\n");
            if (selected.isPresent() && selected.get() == ButtonType.OK) {
                loadIncomingRequestsMenu(actionEvent);
            }
        }
    }

    private void loadIncomingRequestsMenu(ActionEvent actionEvent) {
        IncomingRequestsMenu incomingRequestsMenu =
                loadFXML(actionEvent, json.getString("incomingRequestsMenuFXML"), WindowType.SCENE).getController();
        incomingRequestsMenu.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo,
                imageRepo, demoUserRepo, javaKeyStoreRepo);
        incomingRequestsMenu.init(userManager, user);
    }

    private void loadNewTradeMenu(ActionEvent actionEvent) {
        NewTradeMenu newTradeMenu =
                loadFXML(actionEvent, json.getString("newTradeMenuFXML"), WindowType.SCENE).getController();
        newTradeMenu.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo,
                imageRepo, demoUserRepo, javaKeyStoreRepo);
        newTradeMenu.init(userManager);
    }

    private void setUserManager(UserManager userManager) { this.userManager = userManager; }

    private TradeDirection getTradeDirection() {
        TradeDirection tradeDirection;
        if (borrowersToItems.containsKey(userName.getText()) && lendersToItems.containsKey(userName.getText())) {
            tradeDirection = TradeDirection.LEND_AND_BORROW;
        } else if (borrowersToItems.containsKey(userName.getText())) {
            tradeDirection = TradeDirection.BORROW;
        } else {
            tradeDirection = TradeDirection.LEND;
        }
        return tradeDirection;
    }

    /** Is called on SUBMIT (new trade) and APPROVE (existing trade) */
    @FXML
    private void processTrade(ActionEvent actionEvent) {
        // first we check the threshold
        TradeDirection tradeDirection = getTradeDirection();
        if (!checkThresholdWeeklyTransaction()) {
            showAlert(Alert.AlertType.INFORMATION, "Threshold",
                    "You cannot approve/submit this request as you've reached your weekly transaction limit. " +
                            "\n\n Try again next week!");
        } else if (!checkThresholdLentVsBorrow(tradeDirection)) {
            showAlert(Alert.AlertType.INFORMATION, "Threshold",
                    "You cannot approve/submit this request as you've not lent out enough items. \n\n" +
                            "Try adding a lent item, or removing a borrowed item!");
        } else {
            // we try to create a trade
            TradeValidationResponse processTradeReturn =
                    transactionManager.checkTradeIsValid(tradeDetails, numberOfEdits);
            if (processTradeReturn == TradeValidationResponse.SUCCESS) {
                if (menuState == EditTradeMenuState.NEW) {
                    transactionManager.createNewTrade(tradeDetails, lendersToItems, borrowersToItems, participants);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "You have successfully sent a " +
                            "new trade request!");
                } else if (existingTradeHasBeenModified) {
                    transactionManager.modifyTrade(transaction);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "You have successfully modified " +
                            "an existing trade request!");
                } else if (transactionManager.canBeApproved(transaction, user.getUsername())) {
                    transactionManager.approve(transaction);
                    updateLoanNumbersForParticipants();
                    updateWeeklyTransactionNumbersForParticipants();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "You have successfully approved " +
                            "the trade request! \n\n Please proceed to the given meeting place on the given date!");
                }
                if (menuState == EditTradeMenuState.NEW) {
                    loadNewTradeMenu(actionEvent);
                } else {
                    loadIncomingRequestsMenu(actionEvent);
                }
            } else if (processTradeReturn == TradeValidationResponse.NO_SELECTION) {
                showAlert(Alert.AlertType.ERROR, json.getString("sceneErrorTitle"), "You haven't " +
                        "selected any items!");
            } else if (processTradeReturn == TradeValidationResponse.TOO_MANY_EDITS) {
                transactionManager.cancel(transaction);
                showAlert(Alert.AlertType.INFORMATION, "Cancelled", "Your trade has been cancelled due " +
                        "to too many edits.");
                loadIncomingRequestsMenu(actionEvent);
            } else if (processTradeReturn == TradeValidationResponse.INCOMPLETE) {
                showAlert(Alert.AlertType.ERROR, "Error", "You have empty fields in your trade request!");
            }
        }
    }

    private void indicateTradeModification() {
        existingTradeHasBeenModified = true;
        toggleTradeProcessingButtons();
    }

    /**
     * Displays a choice dialog and changed the reade type according to the selected option
     */
    @FXML
    public void changeTradeType() {
        indicateTradeModification(); // works
        List<TradeDuration> choices = new ArrayList<>();
        choices.add(TradeDuration.TEMPORARY);
        choices.add(TradeDuration.PERMANENT);

        ChoiceDialog<TradeDuration> dialog = new ChoiceDialog<>(TradeDuration.TEMPORARY, choices);
        dialog.setTitle("Trade Type");
        dialog.setHeaderText("Please select the Trade Type");
        dialog.setContentText("Choose your Trade Type:");

        Optional<TradeDuration> choice = dialog.showAndWait();
        choice.ifPresent(tradeType -> {
            if (tradeType.equals(TradeDuration.PERMANENT)) {
                disableReturnButtons();
                tradeTypeLabel.setText("Permanent");
                this.tradeDuration = TradeDuration.PERMANENT;
            }
            else {
                enableReturnButtons();
                tradeTypeLabel.setText("Temporary");
                this.tradeDuration = TradeDuration.TEMPORARY;
            }
            tradeDetails.replace(TradeDetail.TYPE, tradeType);
        });
    }

    @FXML
    private void requestInitMeetLocation() {
        indicateTradeModification(); // works
        createAddressChangeWindow(Meeting.INITIAL);
        meetingPlaceReturnLabel.setText(meetingPlaceInitialLabel.getText());
        tradeDetails.put(TradeDetail.INITIAL_LOCATION, meetingPlaceInitialLabel.getText());
    }

    @FXML
    private void requestReturnMeetLocation() {
        indicateTradeModification(); // works
        createAddressChangeWindow(Meeting.RETURN);
        tradeDetails.put(TradeDetail.RETURN_LOCATION, meetingPlaceReturnLabel.getText());
    }

    private void createAddressChangeWindow(Meeting meetType) {
        //creates a window
        Stage window = new Stage ();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setWidth(640); window.setHeight(480);
        window.setTitle("Meeting Location");

        //creates a button
        Button button = new Button();
        button.setLayoutX(110); button.setLayoutY(100);button.setPrefSize(100, 30);
        button.setText("Select");

        //creates auto-complete address input
        AutoCompleteAddressField addressInput = new AutoCompleteAddressField();
        addressInput.setLayoutX(55); addressInput.setLayoutY(60);
        addressInput.setPromptText("Address");

        //creates information label
        Label info = new Label();
        info.setText("Please input an address following the format:\n 123 Example Blvd, City, ON, Canada");
        info.setLayoutX(10); info.setLayoutY(10); info.setPrefWidth(300);
        info.isWrapText(); info.setTextAlignment(TextAlignment.CENTER);

        //creates scene with an anchorPane + its children
        AnchorPane anchor = new AnchorPane();
        anchor.getChildren().addAll(addressInput,button, info);
        Scene listScene = new Scene(anchor);

        window.setScene(listScene);
        window.show();

        //updates  AutoCompleteAddressField with selected auto-complete suggestion
        addressInput.getEntryMenu().setOnAction((ActionEvent e) ->
                ((MenuItem) e.getTarget()).addEventHandler(Event.ANY, (Event event) ->
                {
                    if (addressInput.getLastSelectedObject() != null) {
                        addressInput.setText(addressInput.getLastSelectedObject().toString());
                    }
                }));

        //checks address validity on button press and updates the chosen meeting location if valid
        button.setOnAction(event -> {
            RegistrationManager registrationManager = new RegistrationManager();
            Pattern pattern = Pattern.compile("^\\d+(\\s[A-Za-z]+){2,},(\\s[A-Za-z.,]+)+,( ON, Canada)$");
            Matcher match = pattern.matcher(addressInput.getText());
            if(match.matches()) {
                //reformats LatLong address into a string
                List<Object> addressComponent = registrationManager.convertAddress(addressInput.getText());
                String address  = convertLatLng(addressComponent);
                if(meetType.equals(Meeting.INITIAL)) {
                    tradeDetails.put(TradeDetail.INITIAL_LOCATION, address);
                    meetingPlaceInitialLabel.setText(address);
                }
                else if (meetType.equals(Meeting.RETURN)) {
                    tradeDetails.put(TradeDetail.RETURN_LOCATION, address);
                    meetingPlaceReturnLabel.setText(address);
                }
                window.close();
            } else {
                showAlert(Alert.AlertType.ERROR, json.getString("sceneErrorTitle"),
                        "The input did not follow the correct format.");
            }
        });
    }

    /**
     * converts the geoCoordinates to a string of formatted address
     * @return a string of formatted address
     */
    private String convertLatLng(List<Object> addressComponent ) {
        try {
            @SuppressWarnings("SpellCheckingInspection")
            GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey(
                    "AIzaSyAbUgjaY2LjckSSljG6lZUHxJM98GHygN8").queryRateLimit(200).build();
            GeocodingApiRequest reverseRequest =
                    GeocodingApi.reverseGeocode(geoApiContext, (LatLng) addressComponent.get(0));
            GeocodingResult[] reverseResult = reverseRequest.await();
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            if (gson.toJson(reverseResult).isEmpty()) {
                geoApiContext.shutdown();
                return " ";

            } else {
                String address = gson.toJson(reverseResult[0].formattedAddress);
                geoApiContext.shutdown();
                return address.replace("\"", "");
            }
        }
        catch (ApiException | InterruptedException | IOException e) { return null; }
    }

    /**
     * A javafx button responsible for requesting the initial meet up date
     */
    @FXML
    public void requestInitMeetDate() {
        indicateTradeModification(); // works
        LocalDateTime localDateTime = getDateTimeInput();
        if (localDateTime != null) {
            tradeDetails.put(TradeDetail.INITIAL_DATE, localDateTime.toString());
            meetingDateInitialLabel.setText(localDateTime.toString());
        }
    }

    /**
     * A javafx button responsible for requesting the return meet up date
     */
    @FXML
    public void requestReturnMeetDate() {
        indicateTradeModification(); // works
        LocalDateTime localDateTime = getDateTimeInput();
        if (localDateTime != null) {
            tradeDetails.put(TradeDetail.RETURN_DATE, localDateTime.toString());
            meetingDateReturnLabel.setText(localDateTime.toString());
        }
    }

    private LocalDateTime getDateTimeInput() {
        LocalDateTime validDateTime = null;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy'-'MM'-'dd' 'kk:mm");
        boolean parsed = false;
        do {
            // Do: display dateTime prompt
            TextInputDialog dialog = new TextInputDialog("yyyy-MM-dd hh:mm");
            dialog.setTitle("Meeting Date and Time");
            dialog.setHeaderText("Please input the date and time in the format yyyy-MM-dd hh:mm");
            dialog.setContentText("Enter the date and time of the meeting:");
            try {
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    validDateTime = LocalDateTime.parse(result.get(), dateTimeFormatter);
                }
                parsed = true;
            } catch (DateTimeParseException dateTimeParseException) {
                showAlert(Alert.AlertType.ERROR, json.getString("sceneErrorTitle"),
                        "The input did not follow the correct format.");
            }
        } while (!parsed);
            return validDateTime;
    }

    private void toggleTradeProcessingButtons() {
        boolean bool = menuState.equals(EditTradeMenuState.NEW);
        submitTradeButton.setDisable(!bool);
        submitTradeButton.setVisible(bool);

        modifyTradeButton.setDisable(!existingTradeHasBeenModified);
        modifyTradeButton.setVisible(!bool);

        approveTradeButton.setDisable(bool);
        approveTradeButton.setVisible(!bool);
    }

    private void disableReturnButtons() {
        meetingPlaceReturnLabel.setDisable(true);
        meetingPlaceReturnLabel.setVisible(false);

        meetingDateReturnLabel.setDisable(true);
        meetingDateReturnLabel.setVisible(false);

        meetingPlaceReturnButton.setDisable(true);
        meetingPlaceReturnButton.setVisible(false);

        meetingDateReturnButton.setDisable(true);
        meetingDateReturnButton.setVisible(false);

        returnDateText.setDisable(true);
        returnDateText.setVisible(false);

        returnPlaceText.setDisable(true);
        returnPlaceText.setVisible(false);
    }

    private void enableReturnButtons() {
        meetingPlaceReturnLabel.setDisable(false);
        meetingPlaceReturnLabel.setVisible(true);

        meetingDateReturnLabel.setDisable(false);
        meetingDateReturnLabel.setVisible(true);

        meetingPlaceReturnButton.setDisable(false);
        meetingPlaceReturnButton.setVisible(true);

        meetingDateReturnButton.setDisable(false);
        meetingDateReturnButton.setVisible(true);

        returnDateText.setDisable(false);
        returnDateText.setVisible(true);

        returnPlaceText.setDisable(false);
        returnPlaceText.setVisible(true);
    }

    private boolean checkThresholdLentVsBorrow(TradeDirection tradeDirection) {
        if (tradeDirection == TradeDirection.BORROW || tradeDirection == TradeDirection.LEND_AND_BORROW) {
            int numItemsBorrowedCurr = (borrowersToItems.get(user.getUsername())).size();
            int numItemsLentCurr;
            try {
                numItemsLentCurr = (lendersToItems.get(user.getUsername())).size();
            } catch (NullPointerException nullPointerException) {
                numItemsLentCurr = 0;
            }
            int numItemsBorrowedPrev = user.getNumBorrowed();
            int numItemsLentPrev = user.getNumLent();
            return numItemsLentPrev + numItemsLentCurr - numItemsBorrowedPrev - numItemsBorrowedCurr
                    >= user.getThresholds().get(0);
        }
        return true;
    }

    private boolean checkThresholdWeeklyTransaction() {
        return user.getNumOfTransactionsThisWeek() + 1 <= user.getThresholds().get(1);
    }

    private void updateWeeklyTransactionNumbersForParticipants() {
        for (String userName : participants) {
            basicUserRepo.get(userName).increaseNumOfTransactionsThisWeek(1);
        }
    }

    private void updateLoanNumbersForParticipants() {
        for (String userName : lendersToItems.keySet()) {
            int numberOfItemsLent = lendersToItems.get(userName).size();
            basicUserRepo.get(userName).increaseNumLentBy(numberOfItemsLent);
        }
        for (String userName : borrowersToItems.keySet()) {
            int numberOfItemsBorrowed = borrowersToItems.get(userName).size();
            basicUserRepo.get(userName).increaseNumBorrowedBy(numberOfItemsBorrowed);
        }
    }

    public void setIsTemporaryTrade(Boolean bool) {
        if (bool) {
            this.tradeTypeLabel.setText("Temporary");
            enableReturnButtons();
        } else {
            this.tradeTypeLabel.setText("Permanent");
            disableReturnButtons();
        }
    }
}
