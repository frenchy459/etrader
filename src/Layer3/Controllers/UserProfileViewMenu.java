package Layer3.Controllers;

import Layer1.Entities.BasicUser;
import Layer2.API.MapAPI.GeoApiContext;
import Layer2.API.MapAPI.GeocodingApi;
import Layer2.API.MapAPI.GeocodingApiRequest;
import Layer2.API.MapAPI.errors.ApiException;
import Layer2.API.MapAPI.gson.Gson;
import Layer2.API.MapAPI.gson.GsonBuilder;
import Layer2.API.MapAPI.model.GeocodingResult;
import Layer2.Managers.RatingManager;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;

/**
 * An user profile view menu controller class
 */
public class UserProfileViewMenu extends AbstractController {

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
    AnchorPane anchor;
    @FXML
    Text userName;
    @FXML
    Text userState;
    @FXML
    Text userStatus;
    @FXML
    Text userReviewScore;
    @FXML
    Text userLocation;
    @FXML
    ImageView userImage;


    protected BasicUser currentUser;


    public void init(BasicUser currentUser) throws InterruptedException, ApiException, IOException {
        this.currentUser = currentUser;
        setImage(userImage, currentUser);
        setUserInfo();
    }

    /**
     * Displays the user info on the menu
     * @throws InterruptedException an exception
     * @throws ApiException an exception from API
     * @throws IOException an IO exception
     */
    public void setUserInfo() throws InterruptedException, ApiException, IOException {
        this.userName.setText(currentUser.getUsername());
        this.userState.setText(currentUser.getAccountState().name());
        this.userStatus.setText(currentUser.getAccountStatus().name());
        this.userReviewScore.setText(new RatingManager().formatRating(currentUser));
        this.userLocation.setText(convertLatLng(currentUser));
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
        if (gson.toJson(reverseResult).isEmpty()){
            geoApiContext.shutdown();
            return " ";
        }
        else {
            String address = gson.toJson(reverseResult[0].formattedAddress);
            geoApiContext.shutdown();
            return address.replace("\"", "");
        }
    }
}
