package Layer1.Enums;

/**
 * the AccountType enum represents the various account types in the system
 */
public enum AccountType {
    /**
     * an ADMIN account type represents an administrative account whose purpose is to monitor BasicUser accounts.
     * They can modify various aspects of the system and BasicUser accounts.
     */
    ADMIN,
    /**
     * a BASIC_USER account type represents the default account type within the system.
     */
    BASIC_USER
}
