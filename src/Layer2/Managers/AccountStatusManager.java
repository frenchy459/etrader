package Layer2.Managers;

import Layer1.Entities.BasicUser;
import Layer1.Entities.Transaction;
import Layer1.Enums.AccountStatus;
import Layer1.Enums.TransactionStatus;
import Layer2.Interfaces.EntityRepo;

import java.util.UUID;

/**
 * A class containing various methods related to account state representation and modification
 */
public class AccountStatusManager {

    /**
     * Retrieves the current AccountStatus ENUM associated with a BasicUser instance.
     * @param basicUser A BasicUser instance registered in the program.
     * @return The AccountStatus ENUM associated with the user at a given instance in time.
     */
    public AccountStatus getAccountStatus(BasicUser basicUser) {
        return basicUser.getAccountStatus();
    }

    /**
     * Sets the account state of a particular BasicUser instance.
     * @param basicUser A BasicUser instance registered in the program.
     * @param accountStatus The status of possible accounts that restrict or enables user privileges.
     */
    public void setAccountStatus(BasicUser basicUser, AccountStatus accountStatus) {
        basicUser.setAccountStatus(accountStatus);
    }

    /**
     * Check if participants contain currentUser, if so, then remove from transactionsRepo and for all users,
     * remove from transactionsHistory then create system notification and notify users.
     * @param transactionRepo The Transaction instance repository.
     * @param basicUserRepo The BasicUser instance repository.
     * @param basicUser The BasicUser that has an AccountState of INACTIVE.
     */
    public void inactiveCancelTransaction(EntityRepo<UUID, Transaction> transactionRepo,
                                          EntityRepo<String, BasicUser> basicUserRepo, BasicUser basicUser) {

        for (Transaction transaction: transactionRepo.getAll()) {
            if (transaction.getParticipants().contains(basicUser.getUsername())
                    && !transaction.getStatus().equals(TransactionStatus.COMPLETE)) {
                for (String username: transaction.getParticipants()) {
                    BasicUser currentUser = basicUserRepo.get(username);
                    currentUser.getTransactionHistory().remove(transaction.getTransactionUUID());
                }
                transactionRepo.remove(transaction.getTransactionUUID());
            }
        }

    }
}
