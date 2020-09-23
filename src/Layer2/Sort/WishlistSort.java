package Layer2.Sort;

import Layer1.Entities.BasicUser;
import Layer1.Entities.Item;
import Layer2.Interfaces.EntityRepo;

import java.util.*;

/**
 * The type Wishlist sort. Sorts an ArrayList of Item objects in order of the number of times the Item
 * has been wish-listed by Basic Users. Default sort puts items in increasing order.
 */
public class WishlistSort extends Sort {

    private final EntityRepo<String, BasicUser> basicUserEntityRepo;

    /**
     * Instantiates a new Wishlist sort.
     *
     * @param basicUserEntityRepo the basic user entity repo
     */
    public WishlistSort(EntityRepo<String, BasicUser> basicUserEntityRepo) {
        this.basicUserEntityRepo = basicUserEntityRepo;
    }

    @Override
    public ArrayList<Item> sort(ArrayList<Item> items) {
        HashMap<Item, Integer> wishlistCounter = new HashMap<>();

        for (Item item : items) {
            wishlistCounter.put(item, 0);

            for (BasicUser user : basicUserEntityRepo.getAll())
                if (user.getWishList().contains(item.getUUID()))
                    wishlistCounter.put(item, wishlistCounter.get(item) + 1);
        }

        ArrayList<Map.Entry<Item, Integer>> wishlistCounterSorted =
                (ArrayList<Map.Entry<Item, Integer>>) entriesSortedByValues(wishlistCounter);

        ArrayList<Item> sorted = new ArrayList<>();
        for (Map.Entry<Item, Integer> entry : wishlistCounterSorted)
            sorted.add(entry.getKey());

        return sorted;
    }

    private List<Map.Entry<Item, Integer>> entriesSortedByValues(Map<Item, Integer> map) {
        List<Map.Entry<Item, Integer>> sortedEntries = new ArrayList<>(map.entrySet());

        sortedEntries.sort(Map.Entry.comparingByValue());

        return sortedEntries;
    }

}
