package Layer1.Entities;

import Layer1.Enums.TradeDuration;
import Layer1.Enums.TradeDetail;
import Layer1.Enums.TransactionStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/** Represents a transaction of items between two users and all its relevant details. */
public class Transaction implements Serializable {
    private final UUID transactionUUID;             // UUID (unique universal identifier) for the transaction
    private TransactionStatus status;               // "new", "in progress", "cancelled", "open", "complete", "incomplete"
    private final LocalDateTime meetingDateInitial; // The time when the trade will take place (temporary/permanent)
    private LocalDateTime meetingDateReturn;        // The time when the return trade will take place (temporary)
    private final String meetingPlaceInitial;       // The place where the trade will take place (temporary/permanent)
    private String meetingPlaceReturn;              // The place where the return trade will take place (temporary)
    private final HashMap<String, List<UUID>> userNamesToOfferedItems;  // maps user names to offered items
    private final HashMap<String, List<Boolean>> meetingConfirmations;  // maps usernames to 2 booleans, indicating that
                                                                        // the initialMeeting & returnMeeting took place
    private final List<String> editors;             // ordered list of user names who edited the transaction
    private final HashMap<String, Double> reviews;  // rating given to user by trading partners after completed trade
    private final boolean isTemporary;              // whether the trade is temporary or not

    private final List<String> participants;                      // a list of all the participants in the trade
    private final HashMap<String, List<UUID>> lendersToItems;     // a mapping of lenders to items they're lending
    private final HashMap<String, List<UUID>> borrowersToItems;   // a mapping of borrowers to items they're borrowing

    /** Instantiates a new transaction with the given parameters.
     * @param tradeDetails a mapping of TradeDetails to their corresponding values
     * @param lendersToItems a mapping of user names to offered items
     * @param borrowersToItems a mapping of user names to received items
     * @param participants a list of user names participating in the trade
     */
    public Transaction (HashMap<TradeDetail, Object> tradeDetails,
                        HashMap<String, List<UUID>> lendersToItems,
                        HashMap<String, List<UUID>> borrowersToItems,
                        List<String> participants) {
        this.transactionUUID = UUID.randomUUID();
        // a mapping of TradeDetail to various transaction properties
        this.lendersToItems = lendersToItems;
        this.borrowersToItems = borrowersToItems;
        this.participants = participants;

        this.status = (TransactionStatus) tradeDetails.get(TradeDetail.STATUS);
        this.meetingPlaceInitial = (String) tradeDetails.get(TradeDetail.INITIAL_LOCATION);
        this.meetingDateInitial = LocalDateTime.parse((CharSequence) tradeDetails.get(TradeDetail.INITIAL_DATE));

        if (tradeDetails.get(TradeDetail.TYPE).equals(TradeDuration.PERMANENT)) {
            this.isTemporary = false;
        } else {
            this.isTemporary = true;
            this.meetingPlaceReturn = (String) tradeDetails.get(TradeDetail.RETURN_LOCATION);
            this.meetingDateReturn = LocalDateTime.parse((CharSequence) tradeDetails.get(TradeDetail.RETURN_DATE));
        }

        this.userNamesToOfferedItems = lendersToItems;

        List<Boolean> confirmationListUser1 = Arrays.asList(false, false);
        List<Boolean> confirmationListUser2 = Arrays.asList(false, false);
        this.meetingConfirmations = new HashMap<>();
        meetingConfirmations.put(participants.get(0), confirmationListUser1);
        meetingConfirmations.put(participants.get(1), confirmationListUser2);

        this.reviews = new HashMap<>();
        this.editors = new ArrayList<>();
    }

    /** Gets the transaction's ID
     * @return a UUID version 4 representing the transaction's ID.
     */
    public UUID getTransactionUUID() {
        return transactionUUID;
    }

    /** Gets the transaction's status.
     * @return an enum representing the transaction's status
     */
    public TransactionStatus getStatus() {
        return status;
    }

    /** Returns true iff all users have confirmed that the transaction has taken place
     * @return a boolean value
     */
    public boolean getMeetingConfirmations() {
        if (this.isTemporary) {
            for (String userName : meetingConfirmations.keySet()) {
                for (Boolean truthValue : meetingConfirmations.get(userName)) {
                    if (!truthValue) {
                        return false;
                    }
                }
            }
        } else {
            for (String userName : meetingConfirmations.keySet()) {
                if (!meetingConfirmations.get(userName).get(0)) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Returns true iff the user has confirmed the specific meeting
     * @param user Represents the String username of a BasicUser
     * @return A boolean representing whether a user has confirmed the meeting for a trade.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean getUserHasConfirmedMeeting(String user, int meetingNumber) {
        return this.meetingConfirmations.get(user).get(meetingNumber - 1);
    }

    /** Gets the transaction's meetup place.
     * @return A string representing the transaction' meetup's place.
     */
    public String getMeetingPlaceInitial() {
        return meetingPlaceInitial;
    }

    /** Gets the transaction's return meetup place.
     * @return A string representing the transaction's return meetup's place.
     */
    public String getMeetingPlaceReturn() {
        return meetingPlaceReturn;
    }

    /** Gets the transaction's meetup time.
     * @return A LocalDateTime type object representing the transaction's meetup time.
     */
    public LocalDateTime getMeetingDateInitial() {
        return meetingDateInitial;
    }

    /** Gets the transaction's return meetup time.
     * @return A LocalDateTime type object representing the transaction's return meetup time.
     */
    public LocalDateTime getMeetingDateReturn() {
        return meetingDateReturn;
    }

    /** Gets the transaction's item and detail.
     * @return A list of objects containing the item's IID, lender's username, borrower's username and
     * whether or not the transaction is temporary.
     */
    public HashMap<String, List<UUID>> getUserNamesToOfferedItems() {
        return userNamesToOfferedItems;
    }

    /** Returns the given user's trading partner's name as a string.
     * @param currentUserName The current username of a User instance
     * @return The username of the given user's trading partner as a String.
     */
    public String getTradingPartnerName(String currentUserName) {

        String tradingPartner = "";

        for (String username : participants) {
            if (!username.equals(currentUserName)) {
                tradingPartner = username;
            }
        }
        return tradingPartner;
    }

    /** Returns true iff the transaction is temporary.
     * @return A boolean representing whether the transaction is temporary.
     */
    public Boolean getIsTemporary() {
        return isTemporary;
    }

    /** Gets the name of the last editor.
     * @return The name of the last Transaction Edit setter, or "X" if the transaction hasn't been edited.
     */
    public String getLastEditor() {
        if (editors.size() > 0) {
            return editors.get(editors.size() - 1);
        } else {
            return null;
        }
    }

    /** Gets the number of edits for this transaction.
     * @return An integer representing the number of edits the transaction has undergone.
     */
    public int getNumberOfEdits() {
        return editors.size();
    }

    /** Gets the rating for a given user.
     * @param reviewedUserName The User whose review is desired. This user has conducted a trade with the other user.
     * @return The corresponding user's review given to the user in this transaction. -1 by default.
     */
    public Double getReview(String reviewedUserName){
        if (reviews.containsKey(reviewedUserName)){
            return this.reviews.get(reviewedUserName);
        } else {
            return -1.0;
        }
    }

    /** Returns true iff only one user in this transaction offered up items.
     * @return A boolean which is true iff only one user in this transaction offered up items.
     */
    public boolean getIsOneWay() {
        return lendersToItems.size() == 1;
    }

    /** Sets the transaction's status.
     * @param status An enum containing the status of the transaction.
     */
    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    /** Confirms that the transaction took place.
     * @param userName      Represents the name of the user doing the confirmation.
     * @param meetingNumber Represents the meeting number (initial = 1, return = 2).
     */
    public void setMeetingConfirmations(String userName, int meetingNumber) {
        this.meetingConfirmations.get(userName).set((meetingNumber - 1), true);
    }

    /** Adds a rating for the given user for this transaction
     * @param ratedUser The user name of the user whom we want to rate
     * @param review The rating given by the other user
     */
    public void setReviews(String ratedUser, Double review) {
        this.reviews.put(ratedUser, review);
    }

    /** Retrieves the offer item details.
     * @return The offer item details.
     */
    public HashMap<String, List<UUID>> getLendersToItems() {
        return lendersToItems;
    }

    /** Retrieves the item details.
     * @return The item details
     */
    public HashMap<String, List<UUID>> getBorrowersToItems() {
        return borrowersToItems;
    }

    /** Retrieves the participants of a particular trade.
     * @return The individuals participating in the trade.
     */
    public List<String> getParticipants() {
        return participants;
    }

    /** Checks that the transaction has been reviewed by the given user
     * @param userName self-explanatory
     * @return true iff the Transaction has been reviewed by the given user
     */
    public boolean hasBeenReviewed(String userName) {
        return reviews.containsKey(userName);
    }

    /** Sets the last editor as the given user if they aren't the last editor already, assuming they've not exceeded
     * their transaction editing threshold. Returns true iff the given editor hasn't exceeded their editing threshold.
     * @param givenUserName the name of the given user
     * @return true iff the threshold hasn't been exceeded
     */
    public boolean setLastEditor(String givenUserName) {
        if (Collections.frequency(this.editors, givenUserName) < 3) {
            try {
                if (!getLastEditor().equals(givenUserName)) {
                    this.editors.add(givenUserName);
                }
            } catch (NullPointerException nullPointerException) {
                this.editors.add(givenUserName);
            }
            return true;
        } else {
            return false;
        }
    }
}
