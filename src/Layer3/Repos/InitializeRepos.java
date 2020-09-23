package Layer3.Repos;

import Layer1.Entities.BasicUser;
import Layer1.Entities.Item;
import Layer1.Entities.Transaction;
import Layer1.Enums.*;
import Layer2.Managers.RegistrationManager;
import Layer3.Repos.JavaKeyStore.JavaKeyStoreRepo;

import java.security.KeyStoreException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

/** A Repo initialization class */
@SuppressWarnings("SpellCheckingInspection")
public class InitializeRepos {
    private BasicUserRepo basicUserRepo;
    private final ItemsRepo itemsRepo;
    private final TransactionsRepo transactionsRepo;
    private final AdminUserRepo adminUserRepo;
    private final JavaKeyStoreRepo javaKeyStoreRepo;
    private final String jsonPath;

    /** Class constructor
     * @param basicUserRepo the user repo
     * @param itemsRepo the items repo
     */
    public InitializeRepos(BasicUserRepo basicUserRepo, ItemsRepo itemsRepo, TransactionsRepo transactionsRepo,
                           AdminUserRepo adminUserRepo, JavaKeyStoreRepo javaKeyStoreRepo, String path) {
        this.basicUserRepo = basicUserRepo;
        this.itemsRepo = itemsRepo;
        this.transactionsRepo = transactionsRepo;
        this.adminUserRepo = adminUserRepo;
        this.javaKeyStoreRepo = javaKeyStoreRepo;
        this.jsonPath = path;
    }

    /** Flatten a 2-dimensional array into a 1-dimensional one and returns it as a stream.
     *
     * This method was originally written by Holger@Stackoverflow
     * https://stackoverflow.com/users/2711488/holger
     * https://stackoverflow.com/a/44909768/2701497
     *
     * @param array an array of objects
     * @return a stream of objects
     */
    private static Stream<Object> flatten(Object[] array) {
        return Arrays.stream(array)
                .flatMap(o -> o instanceof Object[]? flatten((Object[])o): Stream.of(o));
    }

    /** Initializes the repos with some users, items, and transactions */

    public void run() throws KeyStoreException {

        RegistrationManager registrationManager = new RegistrationManager();
        String addressString = "22 Ashglen Court, Toronto, ON, Canada";

        String[][] userInformation = {
                {"al", "12345", addressString},
                {"bob", "12345", addressString},
                {"cora", "12345", addressString},
                {"dana", "12345", addressString},
                {"earl", "12345", addressString},
        };

        List<BasicUser> basicUsersArray = new ArrayList<>();
        for (String[] userEntry : userInformation) {
            List<Integer> thresholds = adminUserRepo.get("admin").getThresholds();
            RegistrationReturn registerReturn = registrationManager.registerAccount(
                    userEntry[0], userEntry[1], userEntry[2], basicUserRepo, adminUserRepo, AccountType.BASIC_USER, javaKeyStoreRepo,
                    thresholds.get(0), thresholds.get(1), thresholds.get(2), jsonPath);
            if (registerReturn.equals(RegistrationReturn.VALID)) {
                basicUserRepo = (BasicUserRepo) registrationManager.getUpdatedBasicUserRepo();
                basicUsersArray.add(basicUserRepo.get(userEntry[0]));
            } else if (registerReturn.equals(RegistrationReturn.NAME_EXISTS)){
                System.out.println("This user already exists!");
            } else {
                System.out.println("There was an error creating this user!");
            }
        }

        Item ai1 = new Item("Webcam", "a Logitech Webcam with a 720p max resolution", "al");
        Item ai2 = new Item("Blue Yeti", "a large, black, high quality USB microphone", "al");
        Item ai3 = new Item("Orange glasses", "a pair of glasses that filter out blue light", "al");
        Item ai4 = new Item("Ear plugs", "a set of disposable Stanley earplugs", "al");
        Item ai5 = new Item("The Red and the Black", "a book by Stendhal", "al");

        Item bi1 = new Item("Glitch", "a Guardian tool that can transform into useful things", "bob");
        Item bi2 = new Item("Keys", "a set of house keys", "bob");
        Item bi3 = new Item("Jane Eyre", "a book by Charlotte Bronte", "bob");
        Item bi4 = new Item("Autobiography", "a book by John Stuart Mill", "bob");
        Item bi5 = new Item("White Tea", "Imperial white tea by Earl Grey", "bob");

        Item ci1 = new Item("Middlemarch", "a book by George Elliott", "cora");
        Item ci2 = new Item("Tea Mug", "A red mug that can hold 500 ml of liquids", "cora");
        Item ci3 = new Item("Computer Speakers", "a set of two small Logitech speakers", "cora");
        Item ci4 = new Item("TI-84 Plus Calculator", "a useful scientific calculator", "cora");
        Item ci5 = new Item("Eraser", "a white Staedler Mars eraser", "cora");

        Item di1 = new Item("Jump Rope", "a black speed rope for rapid jumping", "dana");
        Item di2 = new Item("Exercise Book", "Hilroy exercise book, 32 pages, containing CS notes", "dana");
        Item di3 = new Item("Fan", "a black fan useful for humid summer days", "dana");
        Item di4 = new Item("Teaspoon", "a metallic teaspoon useful for stirring tea", "dana");
        Item di5 = new Item("Protein Bar", "a vegan protein bar that may have melted a bit", "dana");

        Item ei1 = new Item("Cardboard Box", "a large Amazon box that was used to deliver things", "earl");
        Item ei2 = new Item("Ruler", "a metallic ruler great for use with pen-knives", "earl");
        Item ei3 = new Item("Collected Dialogues 1", "a book by Plato", "earl");
        Item ei4 = new Item("Collected Dialogues 2", "another book by Plato", "earl");
        Item ei5 = new Item("Pride and Prejudice", "a book by Jane Austen", "earl");

        Item[][] allItems = {
                {ai1, ai2, ai3, ai4, ai5},
                {bi1, bi2, bi3, bi4, bi5},
                {ci1, ci2, ci3, ci4, ci5},
                {di1, di2, di3, di4, di5},
                {ei1, ei2, ei3, ei4, ei5}
        };

        for (int i = 0; i < basicUsersArray.size(); i++) {
            List<UUID> lendList = new ArrayList<>();
            for (int a = 0; a < 5; a++) {
                allItems[i][a].setIsApproved(true);
                itemsRepo.add(allItems[i][a]);
                lendList.add(allItems[i][a].getUUID());
            }
            basicUsersArray.get(i).getLendList().addAll(lendList);
        }

        for (BasicUser user : basicUsersArray) {
            while (user.getWishList().size() < 12) {
                // flattens 2d to 1d array
                Object[] itemArray = flatten(allItems).toArray();
                // picks a random item between 0 and array length
                Item item = (Item) itemArray[ThreadLocalRandom.current().nextInt(0, itemArray.length)];
                if (!user.getLendList().contains(item.getUUID()) && !user.getWishList().contains(item.getUUID())) {
                    user.getWishList().add(item.getUUID());
                }
            }
        }

        for (BasicUser basicUser : basicUsersArray) {
            basicUserRepo.add(basicUser);
        }

        HashMap<BasicUser, List<Item>> mappingOfUsersToItemsTheyWant = new HashMap<>();

        for (BasicUser basicUser : basicUsersArray) {
            List<Item> itemList = new ArrayList<>();
            for (UUID itemUUID : basicUser.getWishList()) {
                itemList.add(itemsRepo.get(itemUUID));
            }
            mappingOfUsersToItemsTheyWant.put(basicUser, itemList);
        }

        // creates a one-way "in progress" transaction between two users for every desired item
        for (BasicUser trader2 : mappingOfUsersToItemsTheyWant.keySet()) {
            for (Item item : mappingOfUsersToItemsTheyWant.get(trader2)) {
                UUID item1UUID = item.getUUID();
                for (BasicUser trader1 : basicUsersArray) {
                    if (trader1.getLendList().contains(item1UUID)) {

                        LocalDateTime initialMeetingDate =
                                LocalDateTime.now().plusMonths(ThreadLocalRandom.current().nextInt(-3, 4));
                        LocalDateTime returnMeetingDate = initialMeetingDate.plusMonths(1);
                        String initialMeetingLocation = "Coffee Time";
                        boolean randomBoolean2 = ThreadLocalRandom.current().nextInt(0, 2) == 1;
                        TradeDuration transactionDuration = randomBoolean2 ? TradeDuration.TEMPORARY : TradeDuration.PERMANENT;

                        boolean randomBoolean1 = ThreadLocalRandom.current().nextInt(0, 2) == 1;
                        TransactionStatus transactionStatus =
                                randomBoolean1 ? TransactionStatus.PENDING_APPROVAL : TransactionStatus.COMPLETE;

                        HashMap<TradeDetail, Object> tradeDetails = new HashMap<>();
                        tradeDetails.put(TradeDetail.TYPE, transactionDuration);
                        tradeDetails.put(TradeDetail.STATUS, transactionStatus);
                        tradeDetails.put(TradeDetail.INITIAL_LOCATION, initialMeetingLocation);
                        tradeDetails.put(TradeDetail.RETURN_LOCATION, initialMeetingLocation);
                        tradeDetails.put(TradeDetail.INITIAL_DATE, initialMeetingDate.toString());
                        tradeDetails.put(TradeDetail.RETURN_DATE, returnMeetingDate.toString());

                        HashMap<String, List<UUID>> offeredItemDetails = new HashMap<>();
                        HashMap<String, List<UUID>> receivedItemDetails = new HashMap<>();

                        List<UUID> offeredItemList = new ArrayList<>();
                        List<UUID> receivedItemList = new ArrayList<>();
                        offeredItemList.add(item1UUID);
                        receivedItemList.add(item1UUID);
                        offeredItemDetails.put(trader1.getUsername(), offeredItemList);
                        receivedItemDetails.put(trader2.getUsername(), receivedItemList);
                        List<String> participants = new ArrayList<>();
                        participants.add(trader1.getUsername());
                        participants.add(trader2.getUsername());

                        Transaction transaction =
                                new Transaction(tradeDetails, offeredItemDetails, receivedItemDetails, participants);

                        if (transaction.getStatus() == TransactionStatus.COMPLETE) {
                            transaction.setMeetingConfirmations(trader1.getUsername(), 1);
                            transaction.setMeetingConfirmations(trader2.getUsername(), 1);
                            if (transactionDuration == TradeDuration.TEMPORARY) {
                                transaction.setMeetingConfirmations(trader1.getUsername(), 2);
                                transaction.setMeetingConfirmations(trader2.getUsername(), 2);
                            }
                        }

                        transaction.setLastEditor(trader1.getUsername());
                        trader1.setTransactionHistory(transaction.getTransactionUUID());
                        trader2.setTransactionHistory(transaction.getTransactionUUID());
                        transactionsRepo.add(transaction);

                    }
                }
            }
        }
    System.out.println("Repos initialized!");
    System.out.println("Repos initialized!");
    }
}
