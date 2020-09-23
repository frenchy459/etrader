package Layer1.Enums;

/**
 * An enum representing the way the trade was initiated (from a user's wishlist, or a user's inventory)
 */
public enum TradeEntrypoint {
    /**
     * represents that for the BasicUser currently using the system, they initiated the trade from their wishlist
     */
    WISHLIST,
    /**
     * represents that for the BasicUser currently using the system, they initiated the trade from their inventory
     */
    INVENTORY
}
