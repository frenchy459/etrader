package Layer3.Repos;

import Layer1.Entities.Transaction;
import Layer2.Interfaces.EntityRepo;

import java.io.*;
import java.util.*;

/**
 * The gateway class TransactionsRepo is responsible for reading and writing Transaction objects to a file.
 */
public class TransactionsRepo implements EntityRepo<UUID, Transaction> {

    private final HashMap<UUID, Transaction> transactionHashMap = new HashMap<>();

    /**
     * Instantiates a new Transactions repo.
     */
    public TransactionsRepo() {
        reload();
    }

    @Override
    public void reload() {
        try (FileInputStream fi = new FileInputStream("phase2/repoFiles/transactions.bin")) {

            ObjectInputStream os = new ObjectInputStream(fi);

            Object obj;
            Transaction transaction;

            try {
                while ((obj = os.readObject()) != null) {
                    if (obj instanceof Transaction) {
                        transaction = (Transaction) obj;
                        transactionHashMap.put(transaction.getTransactionUUID(), transaction);
                    }
                }
            } catch (Exception ignored) { }

            os.close();

        } catch (IOException e) {
            System.out.println("[phase2/repoFiles/transactions.bin] not found");
        }
    }

    @Override
    public Transaction get(UUID tid) {
        return transactionHashMap.get(tid);
    }

    @Override
    public void add(Transaction transaction) {
        if (!transactionHashMap.containsKey(transaction.getTransactionUUID())) {
            transactionHashMap.put(transaction.getTransactionUUID(), transaction);
        }
    }

    @Override
    public void remove(UUID tid) {
        transactionHashMap.remove(tid);
    }

    @Override
    public List<Transaction> getAll() {
        return new ArrayList<>(transactionHashMap.values());
    }

    @Override
    public List<Transaction> getFilteredList(List<UUID> keys) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        for (UUID uuid : keys)
            transactions.add(transactionHashMap.get(uuid));
        return transactions;
    }

    @Override
    public void onExit() {
        try (FileOutputStream fs = new FileOutputStream("phase2/repoFiles/transactions.bin")) {

            ObjectOutputStream os = new ObjectOutputStream(fs);

            for (Transaction transaction : transactionHashMap.values())
                os.writeObject(transaction);
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
