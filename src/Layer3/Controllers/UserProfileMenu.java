package Layer3.Controllers;

import Layer1.Entities.BasicUser;
import Layer1.Enums.AccountStatus;
import Layer1.Enums.WindowType;
import Layer2.API.MapAPI.GeoApiContext;
import Layer2.API.MapAPI.GeocodingApi;
import Layer2.API.MapAPI.GeocodingApiRequest;
import Layer2.API.MapAPI.errors.ApiException;
import Layer2.API.MapAPI.gson.Gson;
import Layer2.API.MapAPI.gson.GsonBuilder;
import Layer2.API.MapAPI.model.GeocodingResult;
import Layer2.Managers.AccountStatusManager;
import Layer2.Managers.RegistrationManager;
import Layer2.Managers.UserManager;
import Layer2.API.MapAPI.AutoCompleteAddressField;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An user profile menu controller class responsible for user inputs at the user profile menu page
 */
public class UserProfileMenu extends UserProfileViewMenu {

    @FXML
    public JFXButton setAwayButton;
    @FXML
    public JFXButton browseImageButton;
    @FXML
    public JFXButton backButton;
    @FXML
    public Text usernameField;
    @FXML
    public Text statusField;
    @FXML
    public Text standingField;
    @FXML
    public Text ratingField;
    @FXML
    public Text locationField;
    @FXML
    private Button editLocationButton;
    @FXML
    private Button changeLocationButton;


    private final FileChooser fileChooser = new FileChooser();
    private UserManager userManager;
    private final AutoCompleteAddressField addressInput = new AutoCompleteAddressField();
    private final AccountStatusManager accountStatusManager = new AccountStatusManager();

    public void init(UserManager um) throws InterruptedException, ApiException, IOException {
        this.userManager = um;
        this.currentUser = (BasicUser) um.getCurrentUserObject();
        setImage(userImage, this.currentUser);
        setUserInfo();
        changeLocationButton.setVisible(false);
    }

    /**
     * A javafx button responsible for taking the user back to the previous page
     * @param actionEvent an actionEvent object
     */
    @FXML
    public void requestBack(ActionEvent actionEvent) {
        UserMenu um = loadFXML(actionEvent, json.getString("userMenuFXML"), WindowType.SCENE).getController();
        um.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                demoUserRepo, javaKeyStoreRepo);
        um.init(userManager);
    }

    /**
     * A javafx button responsible for setting the user to the way state
     */
    @FXML
    public void setAwayState(){
        if (accountStatusManager.getAccountStatus(currentUser) != AccountStatus.AWAY) {
            Optional<ButtonType> selected = showAlert(Alert.AlertType.CONFIRMATION,
                    json.getString("sceneConfirmTitle"), json.getString("setAway"));
            if (selected.isPresent() && selected.get() == ButtonType.OK) {
                accountStatusManager.setAccountStatus(currentUser, AccountStatus.AWAY);
                userStatus.setText(accountStatusManager.getAccountStatus(currentUser).toString());
            }
        }
        else if (accountStatusManager.getAccountStatus(currentUser) == AccountStatus.AWAY) {
            Optional<ButtonType> selected = showAlert(Alert.AlertType.CONFIRMATION,
                    json.getString("sceneConfirmTitle"), json.getString("removeAway"));
            if (selected.isPresent() && selected.get() == ButtonType.OK) {
                accountStatusManager.setAccountStatus(currentUser, AccountStatus.ACTIVE);
                userStatus.setText(accountStatusManager.getAccountStatus(currentUser).toString());
            }
        }
    }

    /**
     * A javafx button responsible for editing the address of the current basic user
     */
    @FXML
    public void requestEditLocation() {
        if (editLocationButton.getText().equals("CANCEL")) {
            editLocationButton.setText("EDIT");
            anchor.getChildren().remove(addressInput);
            changeLocationButton.setVisible(false);
            return;
        }

        anchor.getChildren().add(addressInput);
        addressInput.setLayoutX(345);
        addressInput.setLayoutY(532);
        addressInput.setPrefHeight(31);
        addressInput.setPrefWidth(250);
        addressInput.setPromptText("ADDRESS");
        editLocationButton.setText("CANCEL");
        File cssFile = new File(json.getString("cssPath"));
        addressInput.getStylesheets().add(cssFile.toURI().toString());
        addressInput.getStyleClass().add("textField");
        changeLocationButton.setVisible(true);

        addressInput.getEntryMenu().setOnAction((ActionEvent e) ->
                ((MenuItem) e.getTarget()).addEventHandler(Event.ANY, (Event event) ->
                {
                    if (addressInput.getLastSelectedObject() != null) {
                        addressInput.setText(addressInput.getLastSelectedObject().toString());
                    }
                }));
    }

    /**
     * A javafx button responsible for accepting user inputs and changing the location of the user
     * @throws InterruptedException an Interrupted exception
     * @throws ApiException an API exception
     * @throws IOException an IO exception
     */
    @FXML
    public void requestChangeLocation() throws InterruptedException, ApiException, IOException {
        RegistrationManager rm = new RegistrationManager();
        Pattern p = Pattern.compile("^\\d+(\\s[A-Za-z]+){2,},(\\s[A-Za-z.,]+)+,( ON, Canada)$");
        Matcher match = p.matcher(addressInput.getText());

        if (match.matches()) {
            List<Object> addressComponent = rm.convertAddress(addressInput.getText());
            if (addressComponent != null) {
                currentUser.setAddressComponent(addressComponent);
                userLocation.setText(currentUser.getCity());
                showAlert(Alert.AlertType.INFORMATION, json.getString("sceneSuccessTitle"),
                        json.getString("successChangeAddress"));
                requestEditLocation();
                this.userLocation.setText(convertLatLng(currentUser));
                addressInput.clear();
                return;
            }
        }
        addressInput.clear();
        showAlert(Alert.AlertType.ERROR, json.getString("sceneErrorTitle"), json.getString("invalidAddress"));
    }

    /**
     * A javafx button responsible for changing the user's profile picture
     * @param actionEvent an actionEvent object
     */
    @FXML
    public void requestBrowseImage(ActionEvent actionEvent) {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));
        File file = fileChooser.showOpenDialog(((Node) actionEvent.getSource()).getScene().getWindow());

        if (file != null) {
            Image image = new Image(file.toURI().toString());
            imageRepo.setCurrentUser(currentUser);
            imageRepo.add(image);
            setImage(userImage, currentUser);
        }
    }
    /**
     * converts the geoCoordinates to a string of formatted address
     * @return a string of formatted address
     * @throws InterruptedException an exception
     * @throws ApiException an exception from API
     * @throws IOException an IO exception
     */
    private String convertLatLng(BasicUser basicUser) throws InterruptedException, ApiException, IOException {
        @SuppressWarnings("SpellCheckingInspection")
        GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey(
                "AIzaSyAbUgjaY2LjckSSljG6lZUHxJM98GHygN8").queryRateLimit(200).build();
        GeocodingApiRequest reverseRequest = GeocodingApi.reverseGeocode(geoApiContext, basicUser.getLatLng());
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
}
