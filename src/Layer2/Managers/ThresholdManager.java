package Layer2.Managers;

import Layer1.Entities.AdminUser;
import Layer1.Entities.BasicUser;
import Layer1.Entities.User;
import Layer2.Interfaces.EntityRepo;

/**
 * A class containing various methods related to threshold management
 */
public class ThresholdManager{

    /** Sets a specific user's new threshold value of how many item should be lent before borrowing
     * @param user the BasicUser object whose threshold value will be changed
     * @param threshold an int representing new user's threshold value
     * @return returns true iff the threshold edit was successful
     */
    public boolean thresholdEdit(int threshold, BasicUser user){
        user.setThreshold(threshold, 0);
        return true;
    }

    /** Sets new threshold value of how many item should be lent before borrowing for all users.
     * @param threshold an int representing new user's threshold value
     * @param basicUserRepo a basic user repo
     * @param adminRepo a basic user repo
     * @return true iff the number of transactions edit was successful.
     */
    public boolean thresholdEdit(int threshold,
                                 EntityRepo<String, BasicUser> basicUserRepo,
                                 EntityRepo<String, AdminUser> adminRepo){
        for (User user: basicUserRepo.getAll())
            user.setThreshold(threshold,0);
        for (User user : adminRepo.getAll())
            user.setThreshold(threshold, 0);

        return basicUserRepo.getAll().size() != 0;
    }

    /** Sets a specific user's new transaction limit per week.
     * @param user the BasicUser object whose threshold value will be changed
     * @param threshold an int representing the user's new limit of transactions per week
     * @return true iff the number of transactions edit was successful.
     */
    public boolean numOfTransactionsEdit(int threshold, BasicUser user){
        user.setThreshold(threshold, 1);
        return true;
    }

    /** Sets new weekly transaction limit for all users.
     * @param threshold an int representing the user's new limit of transactions per week
     * @param basicUserRepo a basic user repo
     * @param adminRepo a basic user repo
     * @return true iff the number of transactions edit was successful.
     */
    public boolean numOfTransactionsEdit(int threshold, EntityRepo<String, BasicUser> basicUserRepo,
                                         EntityRepo<String, AdminUser> adminRepo){
        for (User user: basicUserRepo.getAll())
            user.setThreshold(threshold,1);
        for (User user : adminRepo.getAll())
            user.setThreshold(threshold, 1);

        return basicUserRepo.getAll().size() != 0;
    }

    /** Sets a specific user's new limit of incomplete transactions
     * @param user the BasicUser object whose threshold value will be changed
     * @param threshold an int representing the user's new limit of incomplete transactions
     * @return returns true iff the edit was successful
    */
    public boolean numOfIncompleteTransactionsEdit(int threshold, BasicUser user){
        user.setThreshold(threshold, 2);
        return true;
    }

    /** Sets the new limit of incomplete transactions for all users
     * @param threshold an int representing the user's new limit of incomplete transactions
     * @param basicUserRepo a basic user repo
     * @param adminRepo a basic user repo
     * @return true iff the number of transactions edit was successful
     */
    public boolean numOfIncompleteTransactionsEdit(int threshold, EntityRepo<String, BasicUser> basicUserRepo,
                                                   EntityRepo<String, AdminUser> adminRepo){
        for (User user: basicUserRepo.getAll())
            user.setThreshold(threshold,2);
        for (User user : adminRepo.getAll())
            user.setThreshold(threshold, 2);

        return basicUserRepo.getAll().size() != 0;
    }
}
