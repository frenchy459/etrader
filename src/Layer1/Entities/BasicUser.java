package Layer1.Entities;

import Layer1.Enums.AccountStanding;
import Layer1.Enums.AccountStatus;
import Layer2.API.MapAPI.model.LatLng;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Represents a user in the system.
 */
public class BasicUser extends User implements Serializable {

    private final List<UUID> wishList;
    private final List<UUID> lendList;
    private List<UUID> transactionHistory;
    private AccountStatus accountStatus;
    private AccountStanding accountState;
    private int numLent;
    private int numBorrowed;
    private int numOfTransactionsThisWeek;
    private int numOfIncompleteTransactions;
    private List<Object> addressComponent;
    private UUID deletedItem;
    private double rating = -1;
    private int numOfReview = 0;
    private double totalRatingScore = 0;
    private int trustedCount = 0;
    private LocalDateTime lastLogin;
    private boolean requestUnfrozen = false;

    /** Creates a user with the given name, password and level of access.
     * @param name A string representing the username of the user.
     * @param newPassword A string representing the password of the user.
     * @param address The address of the user represented as a list of object containing a LatLng Object of the address
     *                and a string of the city.
     * @param lentOverBorrow The lending-borrowing ratio for this particular User instance.
     * @param maxTransactionsPerWeek  The threshold regarding the maximum transactions a User may conduct per week.
     * @param maxIncompleteTransactions  The threshold regarding the maximum transactions a User may leave incomplete.
     */
    public BasicUser(String name, String newPassword, List<Object> address, int lentOverBorrow, int maxTransactionsPerWeek,
                     int maxIncompleteTransactions){
        //must be checked for uniqueness before passing into the constructor
        super(name, newPassword, lentOverBorrow, maxTransactionsPerWeek, maxIncompleteTransactions);
        wishList = new ArrayList<>();
        lendList = new ArrayList<>();
        transactionHistory = new ArrayList<>();
        numLent = 0;
        numBorrowed = 0;
        addressComponent = address;
        accountStatus = AccountStatus.ACTIVE;
        accountState = AccountStanding.DEFAULT;
        numOfIncompleteTransactions = 0;
        numOfTransactionsThisWeek = 0;
    }

    /** Gets the user's frozen status
     * @return an AccountS:tate enum representing the state of the account.
     */
    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public AccountStanding getAccountState() { return accountState; }

    /** Gets the user's lend list.
     * @return a list of UUID representing the user's lend list.
     */
    public List<UUID> getLendList() {
        return lendList;
    }

    /** Gets the user's wishlist.
     * @return a list of UUID representing the user's wishlist.
     */
    public List<UUID> getWishList() {
        return wishList;
    }

    /** Gets the user's transaction history.
     * @return a list of UUID representing the user's transaction history.
     */
    public List<UUID> getTransactionHistory() {
        return transactionHistory;
    }

    /** Gets the user's number of incomplete transactions.
     * @return an int representing the user's number of incomplete transactions.
     */
    public int getNumOfIncompleteTransactions() {
        return numOfIncompleteTransactions;
    }

    /** Gets the user's number of transactions this week.
     * @return an int representing the user's number of transactions this week.
     */
    public int getNumOfTransactionsThisWeek() {
        return numOfTransactionsThisWeek;
    }

    /**
     *sets the user's account status
     * @param accountStatus an enum representing a BasicUser's account status
     */
    public void setAccountStatus(AccountStatus accountStatus) { this.accountStatus = accountStatus; }

    /**
     *sets the user's account state
     * @param accountState an enum representing a BasicUser's account status
     */
    public void setAccountState(AccountStanding accountState) { this.accountState = accountState; }

    /** Adds the given transactionUUID to the user's transactionHistory
     * @param transactionUUID A UUID corresponding to a transaction
     */
    public void setTransactionHistory(UUID transactionUUID) {
        if (!transactionHistory.contains(transactionUUID)) {
            this.transactionHistory.add(transactionUUID);
        }
    }

    /** Sets the user's transaction history.
     * @param transactionHistory A list of UUID representing the user's transaction history.
     */
    public void setTransactionHistory(List<UUID> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    /** Sets the user's number of incomplete transaction.
     * @param numOfIncompleteTransactions An int representing the user's number of incomplete transactions.
     */
    public void setNumOfIncompleteTransactions(int numOfIncompleteTransactions) {
        this.numOfIncompleteTransactions = numOfIncompleteTransactions;
    }

    /** Sets the user's number of transactions this week.
     * @param numOfTransactionsThisWeek An int representing the user's number of transactions this week.
     */
    public void setNumOfTransactionsThisWeek(int numOfTransactionsThisWeek) {
        this.numOfTransactionsThisWeek = numOfTransactionsThisWeek;
    }

    /**
     * Gets the user's number of transactions this week.
     * @param numberOfTransactions Increments the number of transactions made this week for this BasicUser instance.
     */
    public void increaseNumOfTransactionsThisWeek(int numberOfTransactions) {
        this.numOfTransactionsThisWeek += numberOfTransactions;
    }

    /** Increases the number of lent item by the specified amount.
     * @param numberOfItemsLent the number of items the user is lending out in a trade
     */
    public void increaseNumLentBy(int numberOfItemsLent) {
        this.numLent += numberOfItemsLent;
    }

    /** Get the user's number of lent item.
     * @return The user's number of lent item.
     */
    public int getNumLent() {
        return this.numLent;
    }

    /** Increases the number of borrowed item by the specified amount.
     * @param numberOfItemsBorrowed the number of items the user is borrowing in a trade
     */
    public void increaseNumBorrowedBy(int numberOfItemsBorrowed) {
        this.numBorrowed += numberOfItemsBorrowed;
    }

    /** The user's number of borrowed items.
     * @return The user's number of borrowed item.
     */
    public int getNumBorrowed() {
        return this.numBorrowed;
    }

    /**
     * Retrieve the city (a String representation) of the BasicUser instance's address.
     * @return The city of the BasicUser instance's address.
     */
    public String getCity(){
        return (String) addressComponent.get(1);
    }

    /**
     * Retrieve the LatLng object of the BasicUser instance's address.
     * @return The LatLng object of the BasicUser instance's address.
     */
    public LatLng getLatLng(){
        return (LatLng) addressComponent.get(0);
    }

    /**
     * Retrieve the most recently deleted item from inventory.
     * @return The most recently deleted item from inventory.
     */
    public UUID getDeletedItem(){
        return deletedItem;
    }

    /**
     * Set the UUId of the most recently deleted item from inventory.
     * @param iID The UUID of most recently deleted item from inventory.
     */
    public void setDeletedItem(UUID iID){
        this.deletedItem = iID;
    }

    /**
     * Get the rating of this BasicUser instance. This is 0 by default.
     * @return The rating of this user.
     */
    public double getRating(){
        return this.rating;
    }

    /**
     * Sets the rating to this user according to the review by others.
     * @param rating The average of stars given by others.
     */
    public void setRating(double rating){this.rating = rating;}

    /**
     * Sets the total number of reviews that this BasicUser instance has received.
     * @param numOfReview The total number of reviews the BasicUser instance has received.
     */
    public void setNumOfReview(int numOfReview) {
        this.numOfReview = numOfReview;
    }

    /**
     * Retrieve the total number of reviews that this BasicUser instance has received.
     * @return The total number of reviews the BasicUser instance has received.
     */
    public int getNumOfReview() {
        return  numOfReview;
    }

    /**
     * Set the cumulative rating score of the BasicUser instance.
     * @param totalRatingScore The cumulative rating of this BasicUser instance.
     */
    public void setTotalRatingScore(double totalRatingScore) {
        this.totalRatingScore = totalRatingScore;
    }

    /**
     * Retrieve the total rating of a this BasicUser instance.
     * @return The total rating of this BasicUser instance.
     */
    public Double getTotalRatingScore() {
        return totalRatingScore;
    }

    /**
     * Sets the addressComponent of this user
     * @param addressComponent a list of LatLng object and a string representing the city
     */
    public void setAddressComponent(List<Object> addressComponent){
        this.addressComponent = addressComponent;
    }

    /**
     * Set the last time that this BasicUser instance has logged into their account.
     * @param lastLogin The LocalDateTime record of the last time this BasicUser has logged into their account.
     */
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * Retrieve the last time this BasicUser instance has logged into their account.
     * @return The last time the BasicUser instance has logged into their account.
     */
    public String getLastLogin() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (lastLogin == null) {
            return null;
        }
        else {
            return lastLogin.format(formatter);
        }
    }

    /**
     * Retrieve a request to be unfrozen.
     * @return Whether the BasicUser instance has request to have their account unfrozen.
     */
    public boolean getRequestUnfrozen() {
        return this.requestUnfrozen;
    }

    /**
     * Sets whether this BasicUser instance would like their account to be unfrozen or not.
     * @param requestUnfrozen A boolean representing whether the BasicUser instance has requested to have their
     * account unfrozen.
     */
    public void setRequestUnfrozen(boolean requestUnfrozen) {
        this.requestUnfrozen = requestUnfrozen;
    }

    /**
     * Retrieves the trusted count field associated with this BasicUser instance.
     * @return The current trusted count field of this BasicUser instance.
     */
    public int getTrustedCount() { return this.trustedCount; }

    /**
     * Updates the trusted count field associated with this BasicUser instance.
     * @param value The new trusted count to be associated with this BasicUser instance.
     */
    public void setTrustedCount(int value) { this.trustedCount = value;}
}
