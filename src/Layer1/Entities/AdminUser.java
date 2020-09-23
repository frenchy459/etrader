package Layer1.Entities;

import java.io.Serializable;

/**
 * The type Admin user. The Admin user cannot initiate new trades and does not have the functionality to trade.
 * Admin only has administrative functionality.
 */
public class AdminUser extends User implements Serializable {

    /**
     * Instantiates a new Admin user.
     *
     * @param username The AdminUser instance username.
     * @param password The AdminUser instance password.
     * @param lentOverBorrow The lending-borrowing ratio for this particular User instance.
     * @param maxTransactionsPerWeek  The threshold regarding the maximum transactions a User may conduct per week.
     * @param maxIncompleteTransactions  The threshold regarding the maximum transactions a User may leave incomplete.
     *
     */
    public AdminUser(String username, String password, int lentOverBorrow, int maxTransactionsPerWeek,
                     int maxIncompleteTransactions) {
        super(username, password, lentOverBorrow, maxTransactionsPerWeek, maxIncompleteTransactions);
    }

}
