package Layer2.Managers;

import Layer1.Entities.BasicUser;
import Layer1.Enums.AccountStanding;

/**
 * Accesses and modifies BasicUser instance states that enable/disable select user functionalities.
 */
public class AccountStateManager {


    /**
     * Retrieves the current AccountStanding ENUM associated with a BasicUser instance.
     * @param basicUser A BasicUser instance registered in the program.
     * @return The AccountState ENUM associated with the user at a given instance in time.
     */
    public AccountStanding getAccountState(BasicUser basicUser) {
        return basicUser.getAccountState();
    }

    /**
     * Sets the account state of a particular BasicUser instance.
     * @param basicUser A BasicUser instance registered in the program.
     * @param accountState One of the possible account states that modify user privileges.
     */
    public void setAccountState(BasicUser basicUser, AccountStanding accountState) {
        basicUser.setAccountState(accountState);
    }

    /** Changes a user's account state to TRUSTED iff they've completed 10+ trades without being LIMITED/FROZEN
     * @param user a BasicUser object */
    public void upgradeToTrusted(BasicUser user) {
        if (user.getTrustedCount() >= 10 && !user.getAccountState().equals(AccountStanding.TRUSTED)){
            setAccountState(user, AccountStanding.TRUSTED);
        }
    }
}
