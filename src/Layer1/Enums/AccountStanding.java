package Layer1.Enums;

/** An Enum representing a user's possible account standings. */
public enum AccountStanding {

    /** A negative standing where a user is unable to initiate new trades, handle trade requests or request new items.
     * A user with this standing can alert an admin if they wish to be unfrozen. */
    FROZEN,

    /** A negative standing where a user has stricter thresholds. It is applied to accounts that are unfrozen and can
     * be removed manually by an admin. */
    LIMITED,

    /** A positive standing where a user has more lenient thresholds. It is applied to accounts that have completed
     * 10 consecutive trades without being frozen. */
    TRUSTED,

    /** A neutral, default standing. */
    DEFAULT
}
