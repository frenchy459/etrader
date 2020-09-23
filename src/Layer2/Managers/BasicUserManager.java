package Layer2.Managers;

import Layer1.Entities.AdminNotification;
import Layer1.Entities.BasicUser;
import Layer1.Enums.AdminNotificationType;
import Layer2.Interfaces.ObserverEntityRepo;

import java.util.Observable;
import java.util.UUID;

/**
 * A BasicUser Manager that handles methods unique to the BasicUser
 */
public class BasicUserManager extends Observable {

    private final ObserverEntityRepo<UUID, AdminNotification> adminNotificationRepo;

    /**
     * Constructor for the BasicUserManager.
     * @param adminNotificationRepo The AdminNotification instance repository.
     */
    public BasicUserManager(ObserverEntityRepo<UUID, AdminNotification> adminNotificationRepo) {
        this.adminNotificationRepo = adminNotificationRepo;
    }

    /**
     * Updates the trusted count attribute
     * @param user A BasicUser instance.
     * @param value The updated trusted count number associated with the BasicUser instance.
     */
    public void setTrustedCount(BasicUser user, int value) {
        user.setTrustedCount(value);
    }

    /**
     * Gets the
     * @param user A BasicUser instance.
     * @return The BasicUser instance's trusted count attribute.
     */
    public int getTrustedCount(BasicUser user) {
        return user.getTrustedCount();
    }

    /**
     * sends FREEZE admin notification iff BasicUser's incomplete transactions are greater than their threshold
     * @param user a BasicUser object
     */
    public void compareIncompleteTransactionWithThreshold(BasicUser user) {
        if (user.getNumOfIncompleteTransactions() > user.getThresholds().get(2)) {
            AdminNotification notification = new AdminNotification(user.getUsername(),
                    AdminNotificationType.FREEZE, " account needs to be frozen.");
            addObserver(adminNotificationRepo);
            setChanged();
            notifyObservers(notification);
        }
    }

    /**
     * updates User's number of transactions this week & trusted count
     * @param user a BasicUser object
     */
    public void updateUserAfterConfirmedTrade(BasicUser user) {
        setNumTransactionsThisWeek(user,
                getNumTransactionThisWeek(user) + 1);
        //increases trusted counter by 1
        setTrustedCount(user, getTrustedCount(user) + 1);
        //if 10+ transactions have been reached, the user will become trusted
    }


    /**
     * Sets the number of incomplete transactions for a BasicUser instance.
     * @param user A BasicUser instance registered to the program.
     * @param value The int representing the new number of incomplete transactions for a BasicUser instance.
     */
    public void setNumIncompleteTransactions(BasicUser user, int value) { user.setNumOfIncompleteTransactions(value); }

    /**
     * Gets the current number of incomplete transactions for a particular BasicUser instance.
     * @param user A BasicUser instance registered to the program.
     * @return The user's current number of incomplete transactions.
     */
    public int getNumIncompleteTransactions(BasicUser user){ return user.getNumOfIncompleteTransactions(); }

    /**
     * Sets the number of transactions conducted during a particular week for a particular BasicUser instance.
     * @param user A BasicUser instance registered to the program.
     * @param value The int representing the cumulative number of transactions conducted during a week for a BasicUser
     * instance.
     */
    public void setNumTransactionsThisWeek(BasicUser user, int value) { user.setNumOfTransactionsThisWeek(value); }

    /**
     * Gets the current number of transactions performed during a week for a specific BasicUser instance.
     * @param user A BasicUser instance registered to the program.
     * @return The user's current cumulative number of transactions for a particular week.
     */
    public int getNumTransactionThisWeek(BasicUser user) { return user.getNumOfTransactionsThisWeek(); }
}
