package Layer3.Controllers;

import Layer1.Entities.BasicUser;
import Layer1.Enums.WindowType;
import Layer3.Gateway.JSONGateway;
import Layer3.Repos.*;
import Layer3.Repos.JavaKeyStore.JavaKeyStoreRepo;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONObject;
import java.io.*;
import java.util.Arrays;
import java.util.Optional;

/**
 * An abstract class representing the abstract controller that all Controller Menu classes will extend
 */
public abstract class AbstractController {
    //repos need to be in every controller, as the user can exit the program at any time
    //requiring us to run saveAll from any window
    protected BasicUserRepo basicUserRepo;
    protected AdminUserRepo adminUserRepo;
    protected ItemsRepo itemsRepo;
    protected TransactionsRepo transactionsRepo;
    protected AdminNotificationRepo adminNotificationRepo;
    protected ImageRepo imageRepo;
    protected DemoUserRepo demoUserRepo;
    protected JavaKeyStoreRepo javaKeyStoreRepo;
    protected Alert alert = new Alert(Alert.AlertType.NONE);
    protected static JSONObject json;

    /**
     * Loads a new FXML file related to a particular menu/controller and then sets it as a new scene on the same window
     * or as a pop-up window. It returns the loader, in case any setter/instantiation methods need to be called
     * @param actionEvent the event entity that signals to the method that an action has occurred
     * @param fxmlFile String form of the path associated with a particular FXML file
     * @param windowType WindowType enum corresponding to the various formats that a JavaFX menu can be instantiated
     */
    protected FXMLLoader loadFXML(Event actionEvent, String fxmlFile, WindowType windowType) {

        FXMLLoader loader = new FXMLLoader();
        //loads the FXML file resource for the particular menu
        loader.setLocation(getClass().getClassLoader().getResource(fxmlFile));
        try {
            //next few lines extract the current stage(window basically)
            Parent root = loader.load();
            Scene newMenu = new Scene(root);
            Stage stage = null;
            //either set a new scene to the current window, or create a popUp window for the new scene
            if (windowType.equals(WindowType.SCENE)) {
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(newMenu);
                //this triggers the saveAll method anytime the X button is pressed on the currently opened window
                stage.setOnCloseRequest(event -> saveAll());
            } else if (windowType.equals(WindowType.POPUP)) {
                stage = new Stage();
                stage.setTitle("Trader");
                //forces user to exit this new window before being able to interact with the previous one
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(newMenu);
            }
            assert stage != null;
            stage.show();
        } catch (IOException e) { e.printStackTrace(); }
        return loader;
    }

    /**
     * Displays a new Alert window that can be customized through its parameters.
     * @param type AlertType enum that represents the various types of Alert windows that can be displayed
     * @param title String representing to the title of the alert window
     * @param content String representing main information of the alert window
     * @return returns the particular buttonType enum that was selected upon closing the window
     */
    protected Optional<ButtonType> showAlert(Alert.AlertType type, String title, String content) {
        alert.setAlertType(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert.showAndWait();
    }


    /**
     * Sets the repos for reading and writing objects to a file.
     *
     * @param buRepo       a basic user repo object responsible for reading and writing BasicUsers objects to a file.
     * @param auRepo       an admin user repo object responsible for reading and writing adminUsers objects to a file.
     * @param iRepo        an item repo object responsible for reading and writing items objects to a file.
     * @param tRepo        a transaction repo object responsible for reading and writing transactions objects to a file.
     * @param anRepo       an admin notification repo object responsible for reading and writing admin notifications
     *                     objects to a file.
     * @param imageRepo    an image repo object responsible for reading and writing images objects to a file.
     * @param demoUserRepo a demo user repo object responsible for reading and writing demoUsers objects to a file.
     */
    public void setRepos(BasicUserRepo buRepo, AdminUserRepo auRepo, ItemsRepo iRepo,
                         TransactionsRepo tRepo, AdminNotificationRepo anRepo, ImageRepo imageRepo,
                         DemoUserRepo demoUserRepo, JavaKeyStoreRepo javaKeyStoreRepo) {
        this.basicUserRepo = buRepo;
        this.adminUserRepo = auRepo;
        this.itemsRepo = iRepo;
        this.transactionsRepo = tRepo;
        this.adminNotificationRepo = anRepo;
        this.imageRepo = imageRepo;
        this.demoUserRepo = demoUserRepo;
        this.javaKeyStoreRepo = javaKeyStoreRepo;

    }

    /**
     * Saves the objects to the repos and clears out any demo user related objects
     */
    public void saveAll() {
        demoUserRepo.empty(itemsRepo, basicUserRepo, transactionsRepo, imageRepo);
        basicUserRepo.onExit();
        adminUserRepo.onExit();
        itemsRepo.onExit();
        transactionsRepo.onExit();
        adminNotificationRepo.onExit();
        imageRepo.onExit();
    }

    /**
     * sets the user's profile picture
     * @param basicUser a BasicUser entity corresponding to the image
     * @param userImage an ImageView entity that's used to display the image in the GUI
     */
    public void setImage(ImageView userImage, BasicUser basicUser) {
        if (imageRepo.imageExists(basicUser.getUsername()))
            userImage.setImage(imageRepo.get(basicUser.getUsername()));
        else {
            try {
                userImage.setImage((new Image(new FileInputStream(json.getString("defaultUserImagePath")))));
            }
            catch (IOException e2) {
                System.out.println(json.getString("defaultUserImageError"));
            }
        }
    }

    public static void setJson() {
        //get JSON first
        final JSONGateway jsonGateway = new JSONGateway();

        try{
            json = jsonGateway.getJson();
        }
        catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    public static String getJson(String key) {
        return json.getString(key);
    }
}
