package Layer2.Sort;

import Layer1.Entities.BasicUser;
import Layer1.Entities.Item;
import Layer2.Interfaces.EntityRepo;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Represents a CitySort Use case class
 */
public class CitySort extends Sort{
    private final BasicUser currentUser;
    private final EntityRepo<String, BasicUser> basicUserEntityRepo;
    private final EntityRepo<UUID, Item> itemEntityRepo;

    /**
     * Creates a CitySort use case class instance
     * @param currentUser is the current User object
     * @param userRepo    is the repo of all the Basic Users
     * @param itemsRepo   is the repo of all the items
     */
    public CitySort(BasicUser currentUser, EntityRepo<String, BasicUser> userRepo, EntityRepo<UUID, Item> itemsRepo){
        this.currentUser = currentUser;
        this.basicUserEntityRepo = userRepo;
        this.itemEntityRepo = itemsRepo;

    }

    /**
     * A sorting method which display only the items that is in the same city as the current user.
     * @param items an arraylist of all approved items own by users.
     * @return a filtered arraylist of Items that is owned by users of the same city
     */
    public ArrayList<Item> sort(ArrayList<Item> items){
        ArrayList<Item> itemList = new ArrayList<>();

        for (BasicUser user: basicUserEntityRepo.getAll()){
            if (!(user.equals(currentUser)) && (currentUser.getCity().equals(user.getCity()))) {
                for (UUID item : user.getLendList()) {
                    Item itemObject = itemEntityRepo.get(item);
                    if (!currentUser.getWishList().contains(item) && itemObject.getIsApproved()) {
                        itemList.add(itemObject);

                    }
                }
            }
        }
        return itemList;
    }

}
