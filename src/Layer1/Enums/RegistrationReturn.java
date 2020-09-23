package Layer1.Enums;

/**
 * Represents the various exceptions that can occur when registering a new account
 */
public enum RegistrationReturn {
    /**
     * VALID represents a successful registration of a new account
     */
    VALID,
    /**
     *  INVALID_ADDRESS represents a failure to input a valid address resulting an unsuccessful account registration
     */
    INVALID_ADDRESS,
    /**
     *  NAME_SHORT represents a failure to input a suitably long enough username resulting an unsuccessful account
     *  registration
     */
    NAME_TOO_SHORT,
    /**
     *  NAME_EXISTS represents a failure to input a unique username resulting an unsuccessful account registration
     */
    NAME_EXISTS
}
