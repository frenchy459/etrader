package Layer1.Enums;

/**
 * An enum representing the direction of the trade from the standpoint of a given user.
 */
public enum TradeDirection {
    /**
     * represents that for the BasicUser currently using the system, they will be lending during this transaction
     */
    LEND,
    /**
     * represents that for the BasicUser currently using the system, they will be borrowing during this transaction
     */
    BORROW,
    /**
     * represents that for the BasicUser currently using the system, they will be lending & borrowing during this
     * transaction
     */
    LEND_AND_BORROW
}
