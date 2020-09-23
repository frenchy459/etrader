package Layer2.Sort;

import Layer1.Entities.BasicUser;
import Layer1.Entities.Item;
import Layer2.Interfaces.EntityRepo;
import Layer2.API.MapAPI.DistanceMatrixApi;
import Layer2.API.MapAPI.DistanceMatrixApiRequest;
import Layer2.API.MapAPI.GeoApiContext;
import Layer2.API.MapAPI.errors.ApiException;
import Layer2.API.MapAPI.model.DistanceMatrix;
import Layer2.API.MapAPI.gson.Gson;
import Layer2.API.MapAPI.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Represents a DistanceSort use case class
 */
public class DurationSort extends Sort{
    private final BasicUser currentUser;
    private final EntityRepo<String, BasicUser> basicUserEntityRepo;
    private final EntityRepo<UUID, Item> itemEntityRepo;
    private int time;

    /**
     * Creates a DistanceSort use case class instance
     * @param currentUser is the current basic user object
     * @param userRepo    is the repo of all basic users
     * @param itemsRepo   is the repo of all items
     * @param time        is the duration of travel the current user inputted in minutes
     */
    public DurationSort(BasicUser currentUser,
                        EntityRepo<String, BasicUser> userRepo,
                        EntityRepo<UUID, Item> itemsRepo,
                        int time) {
        this.currentUser = currentUser;
        this.basicUserEntityRepo = userRepo;
        this.itemEntityRepo = itemsRepo;
        this.time = time;

    }

    /**
     * A sorting method which displays all the items within driving </duration> of the current user
     * @param items an arraylist of all approved items owned by users.
     * @return an arraylist of items owned by user that can be reach within a certain driving duration
     * @throws InterruptedException an exception
     * @throws ApiException a Google Map API exception
     * @throws IOException an exception
     */
    public ArrayList<Item> sort(ArrayList<Item> items) throws InterruptedException, ApiException, IOException {
            ArrayList<Item> itemList = new ArrayList<>();

            for (BasicUser user : basicUserEntityRepo.getAll()) {
                double duration = calcDuration(user);
                if (!(user.equals(currentUser)) && (duration > 0) && (duration <= time)) {
                    for (UUID item : user.getLendList()) {
                        Item itemObject = itemEntityRepo.get(item);
                        if (!(currentUser.getWishList().contains(item)) && itemObject.getIsApproved()) {
                            itemList.add(itemObject);
                        }
                    }
                }
            }
            return itemList;
        }


    private double calcDuration(BasicUser otherUser) throws InterruptedException, ApiException, IOException {
        @SuppressWarnings("SpellCheckingInspection")
        GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey(
                "AIzaSyAbUgjaY2LjckSSljG6lZUHxJM98GHygN8").queryRateLimit(200).build();
        String[] origin = {currentUser.getLatLng().toUrlValue()};
        String[] destination = {otherUser.getLatLng().toUrlValue()};
        DistanceMatrixApiRequest distanceRequest =
                DistanceMatrixApi.getDistanceMatrix(geoApiContext, origin, destination);
        DistanceMatrix result = distanceRequest.await();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String time = gson.toJson(result.rows[0].elements[0].duration.inSeconds);
        geoApiContext.shutdown();
        return Integer.parseInt(time)/60.0;
    }

    /**
     * sets the duration by driving.
     * @param time the time in minute the user inputted
     */
    public void setTime(int time) {
        this.time = time;
    }
}
