package Layer1.Entities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * The abstract type User contains all attributes and methods that any kind of child of User needs.
 */
public abstract class User implements Serializable {

    private final String username;
    private String password;
    private final long ID;

    /**
     * A list of integers representing a non-admin user's trading thresholds.
     *
     * Descriptions of the indices follow:
     *
     * 1: corresponds to how many more items they must lend out than borrow before they're allowed to trade
     * 2: corresponds to the maximum number of transactions they're allowed to conduct per week
     * 3: corresponds to the maximum number of transactions they're allowed to leave incomplete
     */
    private final List<Integer> thresholds;

    /**
     * Instantiates a new User.
     *
     * @param username The User instance's username.
     * @param password The User instance's password.
     * @param lentOverBorrow The lending-borrowing ratio for this particular User instance.
     * @param maxTransactionsPerWeek  The threshold regarding the maximum transactions a User may conduct per week.
     * @param maxIncompleteTransactions  The threshold regarding the maximum transactions a User may leave incomplete.
     */
    User(String username, String password, int lentOverBorrow, int maxTransactionsPerWeek, int maxIncompleteTransactions) {
        this.username = username;
        this.password = password;
        this.ID = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        thresholds = Arrays.asList(lentOverBorrow, maxTransactionsPerWeek, maxIncompleteTransactions);
    }

    /**
     * Gets user's username.
     *
     * @return a String representing the user's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets user's password.
     *
     * @return a string representing the user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets user's ID.
     *
     * @return A long representing the user's ID.
     */
    public long getID() {
        return ID;
    }

    /**
     * Sets the user's password.
     *
     * @param password a string containing the user's password.
     */
    public void setPassword(String password) {
        this.password = password;
    }


    /** Gets the user's threshold values. See member documentation for details.
     * @return a list of integers representing the user's threshold values.
     */
    public List<Integer> getThresholds() {
        return thresholds;
    }

    /** Sets the user's threshold value.
     * @param threshold an int representing the user's threshold value)
     * @param index a value denoting a threshold value,
     */
    public void setThreshold(int threshold, int index) {
        this.thresholds.set(index, threshold);
    }

}
