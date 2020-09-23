package Layer2.Managers;

import Layer1.Entities.BasicUser;
import Layer1.Entities.DemoUser;
import Layer1.Entities.Item;
import Layer1.Entities.Transaction;
import Layer1.Enums.TradeDuration;
import Layer1.Enums.TradeDetail;
import Layer1.Enums.TransactionStatus;
import Layer2.Interfaces.EntityRepo;
import javafx.scene.image.Image;

import java.util.*;

/**
 * The use case class for DemoUser instances.
 */
public class DemoUserManager {

    private final RegistrationManager rm = new RegistrationManager();

    /**
     * Reset the DemoUserRepo to the default state.
     *
     * @param demoUserRepo     The DemoUser instance repository.
     * @param itemsRepo        The Item instance repository.
     * @param transactionsRepo The Transaction instance repository.
     */
    public void generate(EntityRepo<String, DemoUser> demoUserRepo,
                            EntityRepo<UUID, Item> itemsRepo,
                            EntityRepo<UUID, Transaction> transactionsRepo) {
        createDemoUsers(demoUserRepo);
        createPlaceholdersItem(itemsRepo, demoUserRepo);
        createPlaceholderTransactions(transactionsRepo, itemsRepo, demoUserRepo);
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void createDemoUsers(EntityRepo<String, DemoUser> demoUserRepo) {
        DemoUser demo1 = new DemoUser("demo1", "12345", rm.convertAddress("56 Harbord st, toronto, ontario"), 0, 3, 3);
        DemoUser demo2 = new DemoUser("demo2", "12345", rm.convertAddress("100 Charles st. east, toronto, ontario"),  0, 3, 3);
        demoUserRepo.add(demo1);
        demoUserRepo.add(demo2);
    }

    private void createPlaceholdersItem(EntityRepo<UUID, Item> itemsRepo, EntityRepo<String, DemoUser> demoUserRepo) {
        Item newItem1 = new Item("Reusable Water Bottle", "A grey stainless steel water bottle.", "demo1");
        Item newItem2 = new Item("Vintage Hat", "A vintage baseball hat from the 90's.", "demo1");
        demoUserRepo.get("demo1").getLendList().add(newItem1.getUUID());
        demoUserRepo.get("demo1").getLendList().add(newItem2.getUUID());
        newItem1.setIsApproved(true);
        newItem2.setIsApproved(true);
        itemsRepo.add(newItem1);
        itemsRepo.add(newItem2);

        demoUserRepo.get("demo2").getWishList().add(newItem1.getUUID());
    }

    private void createPlaceholderTransactions(EntityRepo<UUID, Transaction> transactionsRepo,
                                              EntityRepo<UUID, Item> itemsRepo,
                                              EntityRepo<String, DemoUser> demoUserRepo) {
        Item item = new Item("Backpack", "A designer backpack imported from Italy.", "demo2");
        itemsRepo.add(item);

        HashMap<TradeDetail, Object> tradeDetails = new HashMap<>();
        tradeDetails.put(TradeDetail.TYPE, TradeDuration.PERMANENT);
        tradeDetails.put(TradeDetail.STATUS, TransactionStatus.COMPLETE);
        //noinspection SpellCheckingInspection
        tradeDetails.put(TradeDetail.INITIAL_LOCATION, "111 Yonge Street, Toronto, Ontario");
        tradeDetails.put(TradeDetail.INITIAL_DATE, "2020-08-10T12:00:00");

        List<UUID> items = new ArrayList<>();
        items.add(item.getUUID());

        HashMap<String, List<UUID>> offerDetails = new HashMap<>();
        offerDetails.put("demo1", new ArrayList<>());
        offerDetails.put("demo2", items);

        HashMap<String, List<UUID>> receiveDetails = new HashMap<>();
        receiveDetails.put("demo1", items);
        receiveDetails.put("demo2", new ArrayList<>());

        List<String> participants = new ArrayList<>();
        participants.add("demo1");
        participants.add("demo2");

        Transaction transaction = new Transaction(tradeDetails, offerDetails, receiveDetails, participants);

        transaction.setReviews("demo1", 4.0);
        new RatingManager().updateRating(demoUserRepo.get("demo1"), transaction);

        transactionsRepo.add(transaction);
        demoUserRepo.get("demo1").getTransactionHistory().add(transaction.getTransactionUUID());
        demoUserRepo.get("demo2").getTransactionHistory().add(transaction.getTransactionUUID());

    }

    /**
     * Completely empties all repos represented in the method's parameters.
     *
     * @param itemsRepo         The items repository.
     * @param demoUserRepo      The DemoUser repository.
     * @param basicUserRepo     The BasicUser repository.
     * @param transactionRepo   The Transaction repository.
     * @param imageRepo         The Image repository.
     */
    public void empty(EntityRepo<UUID, Item> itemsRepo, EntityRepo<String, DemoUser> demoUserRepo,
                       EntityRepo<String, BasicUser> basicUserRepo, EntityRepo<UUID, Transaction> transactionRepo,
                      EntityRepo<String, Image> imageRepo) {
        if (demoUserRepo.get("demo1") != null) {
            removeDemoTransactions(transactionRepo, basicUserRepo, demoUserRepo);
            removeDemoImages(demoUserRepo, imageRepo);
            removeDemoItems(itemsRepo, demoUserRepo);
        }
    }

    private void removeDemoItems(EntityRepo<UUID, Item> itemsRepo, EntityRepo<String, DemoUser> demoUserRepo) {
        for (DemoUser demoUser : demoUserRepo.getAll())
            for (UUID id : demoUser.getLendList())
                itemsRepo.remove(id);
    }

    private void removeDemoTransactions(EntityRepo<UUID, Transaction> transactionsRepo,
                                        EntityRepo<String, BasicUser> basicUserRepo,
                                        EntityRepo<String, DemoUser> demoUserRepo) {
        for (UUID transactionUUID : demoUserRepo.get("demo1").getTransactionHistory()) {

            Transaction transaction = transactionsRepo.get(transactionUUID);
            if (transaction == null)
                continue;
            HashMap<String, List<UUID>> itemDetails = transaction.getUserNamesToOfferedItems();

            for (Map.Entry<String, List<UUID>> mapEntry : itemDetails.entrySet()) {
                if (mapEntry.getValue().size() > 0) {
                    if (!mapEntry.getKey().contains("demo")) {
                        BasicUser currentUser = basicUserRepo.get(mapEntry.getKey());
                        currentUser.getLendList().addAll(mapEntry.getValue());
                        currentUser.getTransactionHistory().remove(transactionUUID);
                    }
                }
            }

            transactionsRepo.remove(transactionUUID);
        }

        for (UUID tid : demoUserRepo.get("demo2").getTransactionHistory())
            transactionsRepo.remove(tid);
    }

    private void removeDemoImages(EntityRepo<String, DemoUser> demoUserRepo, EntityRepo<String, Image> imageRepo) {
        for (DemoUser demoUser : demoUserRepo.getAll()) {
            imageRepo.remove(demoUser.getUsername());
            for (UUID id : demoUser.getLendList())
                imageRepo.remove(id.toString());
        }
    }

}
