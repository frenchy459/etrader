package Layer2.Managers;

import Layer1.Entities.*;
import Layer1.Enums.TradeEntrypoint;
import Layer2.Interfaces.EntityRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Represents a use case class responsible for user management
 */
public class UserManager {

    private final String currentUser;
    private final User currentUserObject;
    private final EntityRepo<String, BasicUser> basicUserRepo;
    private final EntityRepo<UUID, Item> itemsRepo;
    private final EntityRepo<String, DemoUser> demoUserRepo;

    /**
     * Creates an userManager user case class instance
     *
     * @param user          the currently logged in user
     * @param basicUserRepo an EntityRepo that stores a hashmap of String and BasicUser object
     * @param itemsRepo     an EntityRepo that stores a hashmap of UUID and Item object
     * @param demoUserRepo  the demo user repo
     */
    public UserManager(User user, EntityRepo<String, BasicUser> basicUserRepo, EntityRepo<UUID, Item> itemsRepo,
                       EntityRepo<String, DemoUser> demoUserRepo) {
        this.currentUserObject = user;
        this.currentUser = user.getUsername();
        this.basicUserRepo = basicUserRepo;
        this.itemsRepo = itemsRepo;
        this.demoUserRepo = demoUserRepo;
    }

    /**
     * Gets the current User object who is using the system
     *
     * @return the current user object
     */
    public User getCurrentUserObject() {
        return currentUserObject;
    }

    /**
     * Gets the current user of the system
     *
     * @return a String representing the username of the current user of the system.
     */
    public String getCurrentUser() {
        return this.currentUser;
    }

    /**
     * Gets the list of possible trade items along with its owner.
     *
     * @param tradeInitiation a String representing the trade type
     * @return a List containing HashMap<Item, BasicUser> objects.
     */
    public List<HashMap<Item, BasicUser>> getTradeList(TradeEntrypoint tradeInitiation) {
        BasicUser currentUser = (BasicUser) currentUserObject;
        List<UUID> itemListOfCurrentUser;
        List<UUID> itemListOfOtherUser;

        if (tradeInitiation.equals(TradeEntrypoint.WISHLIST)) {
            itemListOfCurrentUser = currentUser.getWishList();
        } else {
            itemListOfCurrentUser = currentUser.getLendList();
        }

        List<BasicUser> otherUsersList = basicUserRepo.getAll(); // first we get all users from the repo
        otherUsersList.remove(basicUserRepo.get(currentUser.getUsername())); // then we remove the current one

        List<HashMap<Item, BasicUser>> tradeList = new ArrayList<>();
        // For each item in the current user's itemList, we iterate over all other users and check whether they
        // want/have (depending on context) the item in our itemList. If so, we add a mapping of the item and the
        // other user to the tradeList
        for (UUID uuid : itemListOfCurrentUser) {
            for (BasicUser basicUser : otherUsersList) {
                // we get the other user's list
                if (tradeInitiation.equals(TradeEntrypoint.WISHLIST)) {
                    itemListOfOtherUser = basicUser.getLendList();
                } else {
                    itemListOfOtherUser = basicUser.getWishList();
                }
                if (itemListOfOtherUser.contains(uuid)) { // if current item is in itemListOfOtherUser
                    HashMap<Item, BasicUser> itemMap = new HashMap<>();
                    itemMap.put(itemsRepo.get(uuid), basicUser);
                    if (!(tradeList.contains(itemMap))) { // if tradeList does not contain map
                        tradeList.add(itemMap); // add item to tradeList
                    }
                }
            }
        }
        return tradeList;
    }

    /**
     * Gets the list of possible trade items along with its owner for demo users.
     *
     * @param tradeInitiation a String representing the trade type
     * @return a List containing HashMap<Item, BasicUser> objects.
     */
    public List<HashMap<Item, BasicUser>> getTradeListDemo(TradeEntrypoint tradeInitiation) {
        DemoUser demoUser = (DemoUser) currentUserObject;
        List<UUID> itemListOfCurrentUser;
        List<UUID> itemListOfOtherUser;

        List<HashMap<Item, BasicUser>> tradeList = new ArrayList<>();

        if (demoUser.getTransactionHistory().size() > 1)
            return tradeList;

        if (tradeInitiation.equals(TradeEntrypoint.WISHLIST))
            itemListOfCurrentUser = demoUser.getWishList();
        else {
            itemListOfCurrentUser = demoUser.getLendList();
            HashMap<Item, BasicUser> itemMap = new HashMap<>();

            if (demoUserRepo.get("demo2").getWishList().size() != 0) {
                itemMap.put(itemsRepo.get(itemListOfCurrentUser.get(0)), demoUserRepo.get("demo2"));
                tradeList.add(itemMap);
            }
            return tradeList;
        }

        List<BasicUser> userRepoList = basicUserRepo.getAll();

        for (UUID uuid : itemListOfCurrentUser) {
            for (BasicUser basicUser : userRepoList) {

                itemListOfOtherUser = basicUser.getLendList();

                if (itemListOfOtherUser.contains(uuid)) {
                    HashMap<Item, BasicUser> itemMap = new HashMap<>();
                    itemMap.put(itemsRepo.get(uuid), basicUser);
                    if (!(tradeList.contains(itemMap)))
                        tradeList.add(itemMap);
                }

            }
        }
        return tradeList;

    }

}
