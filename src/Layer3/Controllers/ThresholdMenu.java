package Layer3.Controllers;
import Layer1.Entities.BasicUser;
import Layer1.Enums.WindowType;
import Layer2.Managers.ThresholdManager;
import Layer2.Managers.UserManager;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


/**
 * The type Threshold menu.
 */
public class ThresholdMenu extends AbstractController {

    @FXML
    public JFXButton backButton;
    @FXML
    public JFXButton lentOverBorrowedButton;
    @FXML
    public JFXButton maxTransactionsButton;
    @FXML
    public JFXButton maxIncompleteTransactionButton;
    @FXML
    private TextField lentOverBorrowedInput;
    @FXML
    private TextField maxTransactionInput;
    @FXML
    private TextField maxIncompleteTransactionInput;
    @FXML
    private Text currentLentOverBorrowed;
    @FXML
    private Text currentMaxTransactions;
    @FXML
    private Text currentMaxIncompleteTransaction;

    private ThresholdManager thresholdManager;
    private UserManager userManager;
    private BasicUser basicUser = null;


    /**
     * Initialize the threshold menu object
     *
     * @param um  an userManager object
     * @param tm  a thresholdManager object
     */
    public void init(UserManager um, ThresholdManager tm){
        setUserManager(um);
        setThresholdManager(tm);
        displayCurrentThresholds();
    }

    /**
     * A javafx button responsible for changing the lent over borrowed threshold value
     */
    @FXML
    public void requestChangeLentOverBorrowed() {
        String input = lentOverBorrowedInput.getText();
        if (!validate(input)) {
            lentOverBorrowedInput.clear();
            return;
        }
        boolean valid;

        if (basicUser == null)
            valid = thresholdManager.thresholdEdit(Integer.parseInt(input), basicUserRepo, adminUserRepo);
        else
            valid = thresholdManager.thresholdEdit(Integer.parseInt(input), basicUser);

        if (valid)
            currentLentOverBorrowed.setText(input);

        displayAlert(valid);
        lentOverBorrowedInput.clear();
        displayCurrentThresholds();
    }

    /**
     * A javafx button responsible for changing the max transaction threshold value
     */
    @FXML
    public void requestChangeMaxTransaction() {
        String input = maxTransactionInput.getText();
        if (validInput(input)) {
            maxTransactionInput.clear();
            return;
        }
        boolean valid;

        if (basicUser == null)
            valid = thresholdManager.numOfTransactionsEdit(Integer.parseInt(input), basicUserRepo, adminUserRepo);
        else
            valid = thresholdManager.numOfTransactionsEdit(Integer.parseInt(input), basicUser);

        if (valid)
            currentMaxTransactions.setText(input);

        displayAlert(valid);
        maxTransactionInput.clear();
        displayCurrentThresholds();
    }

    /**
     * A javafx button responsible for changing the max Incomplete transaction threshold value
     */
    @FXML
    public void requestChangeMaxIncompleteTransaction() {
        String input = maxIncompleteTransactionInput.getText();
        if (validInput(input)) {
            maxIncompleteTransactionInput.clear();
            return;
        }
        boolean valid;

        if (basicUser == null)
            valid = thresholdManager.numOfIncompleteTransactionsEdit(Integer.parseInt(input), basicUserRepo, adminUserRepo);
        else
            valid = thresholdManager.numOfIncompleteTransactionsEdit(Integer.parseInt(input), basicUser);

        if (valid)
            currentMaxIncompleteTransaction.setText(input);

        displayAlert(valid);
        maxIncompleteTransactionInput.clear();
        displayCurrentThresholds();
    }

    /**
     * A javafx button responsible for taking user back to the previous page
     * @param actionEvent an actionEvent object
     */
    @FXML
    public void requestBack(ActionEvent actionEvent) {
        if (basicUser == null) {
            // if (basicUser instance of DemoUser) {
            AdminMenu am = loadFXML(actionEvent, json.getString("adminMenuFXML"), WindowType.SCENE).getController();
            am.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                    demoUserRepo, javaKeyStoreRepo);
            am.init(userManager);
        } else {
            AdministrateUserMenu aum = loadFXML(actionEvent, json.getString("administrateUserMenuFXML"), WindowType.SCENE).getController();
            aum.setRepos(basicUserRepo, adminUserRepo, itemsRepo, transactionsRepo, adminNotificationRepo, imageRepo,
                    demoUserRepo, javaKeyStoreRepo);
            aum.init(userManager, basicUser);
        }
    }

    /**
     * Sets the user whose threshold values we are changing.
     *
     * @param basicUser A BasicUser entity.
     */
    public void setBasicUser(BasicUser basicUser) {
        this.basicUser = basicUser;
    }

    private void setUserManager(UserManager um){
        this.userManager = um;
    }

    private void setThresholdManager(ThresholdManager tm){
        this.thresholdManager = tm;
    }

    private void displayCurrentThresholds() {
        if (basicUser == null) {
            currentLentOverBorrowed.setText(userManager.getCurrentUserObject().getThresholds().get(0).toString());
            currentMaxTransactions.setText(userManager.getCurrentUserObject().getThresholds().get(1).toString());
            currentMaxIncompleteTransaction.setText(userManager.getCurrentUserObject().getThresholds().get(2).toString());
        } else {
            currentLentOverBorrowed.setText(basicUser.getThresholds().get(0).toString());
            currentMaxTransactions.setText(basicUser.getThresholds().get(1).toString());
            currentMaxIncompleteTransaction.setText(basicUser.getThresholds().get(2).toString());
        }
    }

    private boolean validate(String input) {
        if (!input.matches("-?[0-9]+")) {
            showAlert(Alert.AlertType.ERROR, json.getString("sceneErrorTitle"), json.getString("invalidThreshold"));
            return false;
        }
        return true;
    }

    private boolean validInput(String input) {
        if (!input.matches("[0-9]+")) {
            showAlert(Alert.AlertType.ERROR, json.getString("sceneErrorTitle"), json.getString("invalidThreshold"));
            return true;
        }
        return false;
    }

    private void displayAlert(boolean valid) {
        if (!valid) {
            showAlert(Alert.AlertType.ERROR, json.getString("sceneErrorTitle"), json.getString("thresholdNoUsers"));
        }
        else {
            showAlert(Alert.AlertType.INFORMATION, json.getString("sceneSuccessTitle"), json.getString("thresholdSuccessful"));
        }
    }
}
