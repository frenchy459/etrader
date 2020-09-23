package Layer1.Enums;

/**
 * Represents the various sorting types available for the marketplace
 */
public enum Sorter {
    /**
     * represents the sorting type that shows all available items in the marketplace
     */
    ALL,
    /**
     * represents the sorting type that orders all items alphabetically
     */
    ALPHABET,
    /**
     * represents the sorting type that displays only the items from BasicUser's in the same city as you
     */
    CITY,
    /**
     * represents the sorting type that orders all items by their creation date (newest to oldest)
     */
    NEWEST,
    /**
     * represents the sorting type that displays all items within a 15km radius from your address
     */
    DISTANCE_15KM,
    /**
     * represents the sorting type that displays all items within a 30km radius from your address
     */
    DISTANCE_30KM,
    /**
     * represents the sorting type that displays all items within a 30 minute drive from your address
     */
    DRIVING_30MIN,
    /**
     * represents the sorting type that displays all items within a 1 hour drive from your address
     */
    DRIVING_1HR,
    /**
     * represents the sorting type that orders all items based on how many times they've been wish-listed (most to least)
     */
    WISHLIST
}
