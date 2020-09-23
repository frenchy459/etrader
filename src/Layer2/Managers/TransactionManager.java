package Layer2.Managers;

import Layer1.Entities.BasicUser;
import Layer1.Entities.DemoUser;
import Layer1.Entities.Transaction;
import Layer1.Enums.*;
import Layer2.Interfaces.EntityRepo;

import java.time.LocalDateTime;
import java.util.*;

/**
 * A class containing various methods related to transactions
 */
public class TransactionManager {

    private final EntityRepo<String, BasicUser> basicUserRepo;
    private final EntityRepo<UUID, Transaction> transactionsRepo;
    private final EntityRepo<String, DemoUser> demoUserRepo;
    private final String currentUserName;

    /**
     * Instantiate TransactionManager
     *
     * @param basicUserRepo   the repository of all of basic users in the system
     * @param transactionRepo the repository of all transactions in the system
     * @param demoUserRepo    the repository of all demo users in the system
     */
    public TransactionManager(EntityRepo<String, BasicUser> basicUserRepo,
                              EntityRepo<UUID, Transaction> transactionRepo,
                              EntityRepo<String, DemoUser> demoUserRepo,
                              String currentUserName) {
        this.basicUserRepo = basicUserRepo;
        this.transactionsRepo = transactionRepo;
        this.demoUserRepo = demoUserRepo;
        this.currentUserName = currentUserName;
    }

    /** Creates a trade from the given parameters
     *
     * @param tradeDetails a mapping of strings to essential trade details
     * @return true iff the trade can be created
     */
    public TradeValidationResponse checkTradeIsValid(
            HashMap<TradeDetail, Object> tradeDetails, int numberOfEdits) {

        TradeDetail[] requiredKeys;

        if (tradeDetails.get(TradeDetail.TYPE).equals(TradeDuration.TEMPORARY)) {
            requiredKeys = new TradeDetail[]{TradeDetail.TYPE, TradeDetail.STATUS, TradeDetail.INITIAL_LOCATION,
                    TradeDetail.RETURN_LOCATION, TradeDetail.INITIAL_DATE, TradeDetail.RETURN_DATE};
        } else {
            requiredKeys = new TradeDetail[]{TradeDetail.TYPE, TradeDetail.STATUS,
                    TradeDetail.INITIAL_LOCATION, TradeDetail.INITIAL_DATE};
        }

        for (TradeDetail key : requiredKeys) {
            if (!tradeDetails.containsKey(key)) {
                return TradeValidationResponse.INCOMPLETE;
            }
        }

        if (numberOfEdits >= 5) {
            return TradeValidationResponse.TOO_MANY_EDITS;
        }
        return TradeValidationResponse.SUCCESS;
    }

    /**
     * Modifies a transaction that is in the process of being created. This is based on the last transaction edit made.
     *
     * @param transaction The Transaction instance from the previous edit.
     */
    public void modifyTrade(Transaction transaction) {
        transaction.setLastEditor(currentUserName);
    }

    /**
     * Creates a new Transaction instance with all appropriate credentials.
     *
     * @param tradeDetails All of the features describing the transaction that is in the process of creation.
     * @param offeredItems The personally offered items the user who is creating the trade would like to give to the
     * other participants of the transaction. This parameter associates the Item instance's unique identifier to the
     * User instance who the creator of this transaction would like to give their Item instances out to.
     * @param desiredItems The desired Item instances all User instances in the current trade may want. This parameter
     * associates the Item instance's unique identifier to the User instance that would like the item instance.
     * @param participants The names of User instances performing a particular trade.
     */
    public void createNewTrade(
                            HashMap<TradeDetail, Object> tradeDetails,
                            HashMap<String, List<UUID>> offeredItems,
                            HashMap<String, List<UUID>> desiredItems,
                            List<String> participants) {

        Transaction newTransaction = new Transaction(tradeDetails, offeredItems, desiredItems, participants);
        newTransaction.setStatus(TransactionStatus.PENDING_APPROVAL);

        if (newTransaction.setLastEditor(currentUserName)) {
            for (String username: participants) {
                if (basicUserRepo.get(username) == null)
                    // if current participant is not a basic user, add transaction to demo user's transaction history
                    demoUserRepo.get(username).getTransactionHistory().add(newTransaction.getTransactionUUID());
                else
                    // else add transaction to basic user's transaction history
                    basicUserRepo.get(username).getTransactionHistory().add(newTransaction.getTransactionUUID());
            }
        }
        transactionsRepo.add(newTransaction);
    }

    /** Checks whether the given transaction can be approved by the given user
     * @param currentTransaction a transaction object
     * @param currentUserName the username of a user
     * @return true iff the transaction can be approved by the given user
     */
    public boolean canBeApproved(Transaction currentTransaction, String currentUserName) {
        // return true iff (lastEditor is not currentUser) OR (it's not a "new" transaction)
        return (!currentTransaction.getLastEditor().equals(currentUserName)
                    && !currentTransaction.getStatus().equals(TransactionStatus.NEW));
    }

    /** Cancels the transaction for all participants and adds items back to respective user's wishList/lendList
     * @param transaction represents the current Transaction entity
     */
    public void cancel(Transaction transaction) {
        if (canBeCancelled(transaction)) {
            // if the transaction is PENDING_APPROVAL, the items weren't removed from the users' lists yet
            if (!(transaction.getStatus() == TransactionStatus.PENDING_APPROVAL)) {
                Map<String, List<UUID>> borrowersToItems = transaction.getBorrowersToItems();
                Map<String, List<UUID>> lendersToItems = transaction.getLendersToItems();
                // add items from borrowersToItems to respective borrowers' wishList
                for (String userName : borrowersToItems.keySet()) {
                    BasicUser basicUser = basicUserRepo.get(userName);
                    List<UUID> wishList = basicUser.getWishList();
                    wishList.addAll(borrowersToItems.get(userName));
                }
                // add items from lendersToItems to respective lenders' lendList
                for (String userName : lendersToItems.keySet()) {
                    BasicUser basicUser = basicUserRepo.get(userName);
                    List<UUID> lendList = basicUser.getLendList();
                    lendList.addAll(lendersToItems.get(userName));
                }
            }
            // change status to cancelled
            transaction.setStatus(TransactionStatus.CANCELLED);
        }
    }

    /** Approves an incoming transaction request.
     * @param transaction the given transaction
     */
    public void approve(Transaction transaction){
        transaction.setStatus(TransactionStatus.OPEN);

        for (String userName : transaction.getLendersToItems().keySet()) {
            // add to every loaner's loan score
            int numberOfItemsLent = transaction.getLendersToItems().get(userName).size();
            basicUserRepo.get(userName).increaseNumLentBy(numberOfItemsLent);
            // remove all items from every lender's lendList
            for (UUID itemName : transaction.getLendersToItems().get(userName)) {
                basicUserRepo.get(userName).getLendList().remove(itemName);
            }
        }

        for (String userName : transaction.getBorrowersToItems().keySet()) {
            // add to every borrower's borrow score
            int numberOfItemsBorrowed = transaction.getBorrowersToItems().get(userName).size();
            basicUserRepo.get(userName).increaseNumBorrowedBy(numberOfItemsBorrowed);
            // remove all items from every borrower's wishList
            for (UUID itemName : transaction.getBorrowersToItems().get(userName)) {
                basicUserRepo.get(userName).getWishList().remove(itemName);
            }
        }
    }

    /** Checks if the given transaction can be cancelled.
     * @param currentTransaction the given transaction
     * @return true iff transaction status is neither "new" nor "complete"
     */
    public boolean canBeCancelled(Transaction currentTransaction) {
        return  !currentTransaction.getStatus().equals(TransactionStatus.NEW) &&
                !currentTransaction.getStatus().equals(TransactionStatus.COMPLETE);
    }

    /**
     * Confirms that the transaction took place. removing the items in the transaction from their owner's lendList
     * and their receiver's wishlist.
     *
     * @param currentTransaction represents the current Transaction entity
     * @return a boolean signifying if the transaction was confirmed by all parties
     */
    public boolean confirm(Transaction currentTransaction) {
        if (currentTransaction.getMeetingConfirmations()) {
            currentTransaction.setStatus(TransactionStatus.COMPLETE);

            for (String user : currentTransaction.getLendersToItems().keySet()) {
                BasicUser basicUser = basicUserRepo.get(user);
                basicUser.getLendList().removeAll(currentTransaction.getLendersToItems().get(user));
                basicUser.getWishList().removeAll(currentTransaction.getBorrowersToItems().get(user));
            }
            return true;
        }
        return false;
    }

    /**
     * sets the status of user's Transactions to AWAITING_MEETING iff the current date & time is past the
     * initial meeting date & time of the Transaction, and the current Transaction status is OPEN.
     * Additionally, sets the user's Transactions to AWAITING_RETURN_MEETING iff current date & time is past
     * the return meeting date & time of the Transaction, and the current Transaction status is AWAITING_MEETING.
     *
     * @param user represents a BasicUser
     */
    public void checkIfTransactionsAreIncomplete(BasicUser user) {
        //gets all the transactions that the user is a part of
        List<Transaction> transactions = new ArrayList<>();
        for (UUID transID: user.getTransactionHistory()){
            transactions.add(transactionsRepo.get(transID));
        }
        for (Transaction transaction : transactions) {
            if (LocalDateTime.now().isAfter(transaction.getMeetingDateInitial())
                    && transaction.getStatus().equals(TransactionStatus.OPEN)) {
                transaction.setStatus(TransactionStatus.PENDING_MEETING_CONFIRMATION);
            }

            //temp trade only, checks if date has been passed for return meetings and if it's been confirmed yet
            if (transaction.getIsTemporary()) {
                if (LocalDateTime.now().isAfter(transaction.getMeetingDateReturn())
                        && transaction.getStatus().equals(TransactionStatus.PENDING_MEETING_CONFIRMATION)) {
                    transaction.setStatus(TransactionStatus.PENDING_RETURN_MEETING_CONFIRMATION);
                }
            }
        }
    }

    /**
     * returns true iff there exists a transaction in the currentUser's history that has a pending meeting
     * confirmation. (ie. current date is past the meeting date for the transaction & the user hasn't confirmed it)
     *
     * @param currentUser represents a BasicUser
     */
    public boolean getPendingMeetingConfirmations (BasicUser currentUser) {
        List<Transaction> temp = new ArrayList<>();
        for (UUID tUID: currentUser.getTransactionHistory()) {
            temp.add(transactionsRepo.get(tUID));
        }
        for (Transaction transaction : temp){
            if ( ((transaction.getStatus().equals(TransactionStatus.PENDING_MEETING_CONFIRMATION))
                    && !transaction.getUserHasConfirmedMeeting(currentUser.getUsername(), 1)) ||
                    (transaction.getStatus().equals(TransactionStatus.PENDING_RETURN_MEETING_CONFIRMATION) &&
                            !transaction.getUserHasConfirmedMeeting(currentUser.getUsername(), 2))){
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the meeting confirmation of a trade to confirm whether it has occurred.
     * @param currentUser The current BasicUser instance logged into the program.
     * @param transaction The Transaction instance the BasicUser instance would like to provide the confirmation for.
     * @param meetingNumber Represents the meeting number (initial = 1, return = 2).
     */
    public void updateMeetingConfirmation(BasicUser currentUser, Transaction transaction, int meetingNumber) {
        transaction.setMeetingConfirmations(currentUser.getUsername(), meetingNumber);
    }

}

