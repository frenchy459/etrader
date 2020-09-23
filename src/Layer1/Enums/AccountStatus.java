package Layer1.Enums;

/** This enum contains various states that a user account can be in. */
public enum AccountStatus {

    /** The INACTIVE status represents a user that has not been active in the system for an extended
     * period of time. When triggered, pending transactions are automatically cancelled and all user's items are
     * removed from the Marketplace. Admins can activate this status for user accounts at any time. */
    INACTIVE,

    /** An Away account status represents a BasicUser that will not be active in the system for a definite period of
     * time. Attempting to initiate a trade with an Away status account will give the user a warning. This status can
     * be applied or removed by BasicUsers on their own account at any time. */
    AWAY,

    /** An ACTIVE account status represents the default account status for BasicUsers. */
    ACTIVE
}
