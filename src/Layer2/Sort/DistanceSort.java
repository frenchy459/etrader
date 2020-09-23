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
public class DistanceSort extends Sort{
    private final BasicUser currentUser;
    private final EntityRepo<String, BasicUser> basicUserEntityRepo;
    private final EntityRepo<UUID, Item> itemEntityRepo;
    private int distance;

    /**
     * Creates a DistanceSort use case class instance
     * @param currentUser is the current basic user object
     * @param userRepo    is the repo of all basic users
     * @param itemsRepo   is the repo of all items
     * @param distance    is the distance in km the user inputted
     */
    public DistanceSort(BasicUser currentUser,
                        EntityRepo<String, BasicUser> userRepo,
                        EntityRepo<UUID, Item> itemsRepo,
                        int distance) {
        this.currentUser = currentUser;
        this.basicUserEntityRepo = userRepo;
        this.itemEntityRepo = itemsRepo;
        this.distance = distance;

    }

    /**
     * A sorting method which returns all the items within </distance> of the current user
     * @param items the list of all approved items owned by users
     * @return an arraylist of Item that is owned by users within a certain distance of the current user.
     * @throws InterruptedException an exception
     * @throws ApiException a Google Map API exception
     * @throws IOException an exception
     */
    public ArrayList<Item> sort(ArrayList<Item> items) throws ApiException, IOException, InterruptedException {
        ArrayList<Item> itemList = new ArrayList<>();
        for (BasicUser user : basicUserEntityRepo.getAll()) {
            double diffDistance = calcDistance(user);
            if (!(user.equals(currentUser)) && (diffDistance > 0) && (diffDistance <= distance)) {
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


    private double calcDistance(BasicUser otherUser) throws InterruptedException, ApiException, IOException {
        @SuppressWarnings("SpellCheckingInspection")
        GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey(
                "AIzaSyAbUgjaY2LjckSSljG6lZUHxJM98GHygN8").queryRateLimit(200).build();
        String[] origin = {currentUser.getLatLng().toUrlValue()};
        String[] destination = {otherUser.getLatLng().toUrlValue()};
        DistanceMatrixApiRequest distanceRequest =
                DistanceMatrixApi.getDistanceMatrix(geoApiContext, origin, destination);
        DistanceMatrix resultDis = distanceRequest.await();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String distance = gson.toJson(resultDis.rows[0].elements[0].distance.inMeters);

        geoApiContext.shutdown();
        return Integer.parseInt(distance)/1000.0;

    }

    /**
     * Sets the distance inputted by the user.
     * @param distance an int in km.
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }
}
