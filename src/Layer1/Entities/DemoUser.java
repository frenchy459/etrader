package Layer1.Entities;

import java.io.Serializable;
import java.util.List;

/**
 * The type Demo user extends BasicUser and is used for when someone wants to view different parts of the program
 * without actually communicating to the admin or other users in the system.
 */
public class DemoUser extends BasicUser implements Serializable {

    /**
     * Instantiates a new Demo user.
     *
     * @param name        The username of the DemoUser instance.
     * @param newPassword The password of the DemoUser instance.
     * @param address     The new address of the DemoUser instance.
     * @param lentOverBorrow The lending-borrowing ratio for this particular User instance.
     * @param maxTransactionsPerWeek  The threshold regarding the maximum transactions a User may conduct per week.
     * @param maxIncompleteTransactions  The threshold regarding the maximum transactions a User may leave incomplete.
     */
    public DemoUser(String name, String newPassword, List<Object>address, int lentOverBorrow, int maxTransactionsPerWeek,
                    int maxIncompleteTransactions) {
        super(name, newPassword, address, lentOverBorrow, maxTransactionsPerWeek, maxIncompleteTransactions);
    }
}

