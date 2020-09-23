package Layer3.Controllers;

import Layer1.Enums.AccountType;
import Layer1.Enums.RegistrationReturn;
import Layer1.Enums.WindowType;
import Layer2.Managers.RegistrationManager;
import Layer2.Managers.UserManager;
import Layer2.API.MapAPI.AutoCompleteAddressField;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import java.io.*;
import java.security.KeyStoreException;
import java.util.List;

/**
 * A register controller class responsible for handling user inputs at the registration menu page
 */
public class Register extends AbstractController {

    @FXML
    public JFXButton registerButton;
    @FXML
    public JFXButton returnButton;
    @FXML
    private AnchorPane anchor;
    @FXML
    private TextField userName;
    @FXML
    private PasswordField password;

    private final AutoCompleteAddressField addressInput = new AutoCompleteAddressField();
    private final RegistrationManager regM = new RegistrationManager();
    private UserManager userManager;
    private AccountType accountType;

    /**
     * Initialize the register object
     */
    public void init() {
        setAutoAddressInput();
        userName.requestFocus();
    }

    //configures the AutoCompleteAddressField
    private void setAutoAddressInput(){
        //places the field within the anchorPane
        anchor.getChildren().add(addressInput);
        addressInput.setLayoutX(73);addressInput.setLayoutY(444);
        addressInput.setPrefWidth(456);addressInput.setPrefHeight(31);
        addressInput.setPromptText("ADDRESS");
        File cssFile = new File(json.getString("cssPath"));
        addressInput.getStylesheets().add(cssFile.toURI().toString());
        addressInput.getStyleClass().add("textField");

        //Replaces whatever is currently written in the field with w/e auto-complete suggestion
        // the user selects from the drop down menu while typing
        addressInput.getEntryMenu().setOnAction((ActionEvent e) ->
                ((MenuItem) e.getTarget()).addEventHandler(Event.ANY, (Event event) ->
                {
                    if (addressInput.getLastSelectedObject() != null) {
                        addressInput.setText(addressInput.getLastSelectedObject().toString());
                    }
                }));
    }

    /**
     * A javafx button responsible for registering new account
     * @param actionEvent an actionEvent
     * @throws KeyStoreException a keyStore exception
     */
    @FXML
    public void registerNewAccount(ActionEvent actionEvent) throws KeyStoreException {
        //checks if username and address meet requirements, registers account if true
        List<Integer> thresholds = adminUserRepo.get("admin").getThresholds();
        RegistrationReturn registrationState;

        if (accountType.equals(AccountType.ADMIN))
            registrationState = regM.registerAccount(userName.getText(), password.getText(), "",
                    basicUserRepo, adminUserRepo, this.accountType, javaKeyStoreRepo,
                    thresholds.get(0), thresholds.get(1), thresholds.get(2), json.getString("encryptDecryptPath"));
        else
            registrationState = regM.registerAccount(userName.getText(), password.getText(), addressInput.getText(),
                    basicUserRepo, adminUserRepo, this.accountType, javaKeyStoreRepo,
                    thresholds.get(0), thresholds.get(1), thresholds.get(2), json.getString("encryptDecryptPath"));

        switch (registrationState) {
            case NAME_TOO_SHORT:
                showAlert(Alert.AlertType.ERROR, json.getString("sceneErrorTitle"), json.getString("usernameShort"));
                break;
            case NAME_EXISTS:
                showAlert(Alert.AlertType.ERROR, json.getString("sceneErrorTitle"), json.getString("usernameExists"));
                break;
            case INVALID_ADDRESS:
                showAlert(Alert.AlertType.ERROR, json.getString("sceneErrorTitle"), json.getString("invalidAddress"));
                break;
            case VALID:
                showAlert(Alert.AlertType.INFORMATION, json.getString("sceneSuccessTitle"), json.getString("registered"));
                break;
        }

        this.userName.clear();
        this.password.clear();
        if (accountType.equals(AccountType.BASIC_USER)) { this.addressInput.clear(); }
    }

    /**
     * A javafx button responsible for taking user back to the previous menu
     * @param actionEvent an ActionEvent object
     */
    @FXML
    public void returnToPreviousMenu(ActionEvent actionEvent) {
        if (accountType.equals(AccountType.BASIC_USER)) {
            Trader trader = loadFXML(actionEvent, json.getString("landingMenuFXML"), WindowType.SCENE).getController();
            trader.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                    demoUserRepo, javaKeyStoreRepo);
        }
        else if (accountType.equals(AccountType.ADMIN)) {
            AdminMenu am = loadFXML(actionEvent, json.getString("adminMenuFXML"), WindowType.SCENE).getController();
            am.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                    demoUserRepo, javaKeyStoreRepo);
            am.init(userManager);
        }
    }

    /**
     * Sets the account type of the new user
     * @param state an enum AccountType representing the account type
     */
    public void setAccountType(AccountType state) {
        this.accountType = state;

        if (state.equals(AccountType.ADMIN)) {
            addressInput.setVisible(false);
            addressInput.setDisable(true);
        }
    }

    /**
     * Sets the userManager of the register object
     * @param userManager an userManager object
     */
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}
