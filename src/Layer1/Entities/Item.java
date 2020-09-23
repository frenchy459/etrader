package Layer1.Entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents an item in real life that is tangible.
 * Items are added by Users.
 */
public class Item implements Serializable {

    /** IID uniquely represents each Item, cannot be modified.
     * Used UUID @version 4.
     */
    private final UUID itemUUID;
    private String name;
    private String description;
    private Boolean isApproved;
    private final LocalDateTime dateAdded;
    private final String originalOwner;
    private final List<String> wishListedBy = new ArrayList<>();

    /**
     * Creates an Item with the given name and description
     *
     * @param ItemName        The item's name.
     * @param ItemDescription The item's description.
     * @param originalOwnerName The original owner (User instance) of this particular Item instance.
     */
    public Item(String ItemName, String ItemDescription, String originalOwnerName){
        itemUUID = UUID.randomUUID();
        name = ItemName;
        description = ItemDescription;
        isApproved = false;
        dateAdded = LocalDateTime.now();
        originalOwner = originalOwnerName;
    }

    /**
     * Gets the item's IID.
     *
     * @return A UUID version 4 representing the item's ID.
     */
    public UUID getUUID() {
        return itemUUID;
    }

    /**
     * Gets the item's name.
     *
     * @return A string representing the item's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the item's description.
     *
     * @return A string representing the item's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the item's status
     *
     * @return A boolean representing whether the item is approved or not.
     */
    public Boolean getIsApproved() {
        return isApproved;
    }

    /**
     * Gets the date the item was added (NOT the date when item was approved).
     *
     * @return The date the item was added.
     */
    public LocalDateTime getDateAdded() {return dateAdded; }

    public String getOriginalOwner() {
        return originalOwner;
    }

    /**
     * Sets the item's name.
     *
     * @param ItemName A string containing the item's name.
     */
    public void setName(String ItemName) {
        name = ItemName;
    }

    /**
     * Sets the item's description.
     *
     * @param ItemDescription A string containing the item's description.
     */
    public void setDescription(String ItemDescription) {
        description = ItemDescription;
    }

    /**
     * Sets the item's approval status.
     *
     * @param isApproved A boolean containing the item's approval status.
     */
    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    /**
     * Retrieves a list of User instance usernames that have placed a specific user's item on their wishlist.
     * @return A list containing usernames of Users who have placed a specific user's item on their wishlist.
     */
    public List<String> getWishListedBy() {
        return wishListedBy;
    }

    /**
     * Removes a new user (by username) to the wish listed by list.
     * @param user A new user who has removed a specific item from the corresponding original item owner.
     */
    public void removeWishListedBy(String user) {
        wishListedBy.remove(user);
    }
}
