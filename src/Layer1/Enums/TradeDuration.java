package Layer1.Enums;

/**
 * An enum representing whether the trade is temporary or permanent.
 */
public enum TradeDuration {
    /**
     * represents a temporary transaction, meaning users will have both an initial meeting and return meeting
     */
    TEMPORARY,
    /**
     * represents a permanent transaction, meaning users will only have an initial meeting
     */
    PERMANENT
}
