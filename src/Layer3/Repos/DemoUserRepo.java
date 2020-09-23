package Layer3.Repos;

import Layer1.Entities.DemoUser;
import Layer2.Interfaces.EntityRepo;
import Layer2.Managers.DemoUserManager;

import java.io.*;
import java.util.*;

/**
 * The gateway class DemoUserRepo is responsible for reading and writing DemoUser objects to a file..
 */
public class DemoUserRepo implements EntityRepo<String, DemoUser> {

    private final HashMap<String, DemoUser> demoUserHashMap = new HashMap<>();
    private final DemoUserManager dum = new DemoUserManager();

    public void empty(ItemsRepo itemsRepo, BasicUserRepo basicUserRepo, TransactionsRepo transactionsRepo,
                      ImageRepo imageRepo) {
        dum.empty(itemsRepo, this, basicUserRepo, transactionsRepo, imageRepo);
    }

    public void generate(ItemsRepo itemsRepo, TransactionsRepo transactionsRepo) {
        dum.generate(this, itemsRepo, transactionsRepo);
    }

    @Override
    public DemoUser get(String key) {
        return demoUserHashMap.get(key);
    }

    @Override
    public void add(DemoUser object) {
        demoUserHashMap.put(object.getUsername(), object);
    }

    @Override
    public void remove(String key) {
        demoUserHashMap.remove(key);
    }

    @Override
    public List<DemoUser> getAll() {
        return new ArrayList<>(demoUserHashMap.values());
    }

    // Don't bother implementing getFilteredList since it will never be used
    @Override
    public List<DemoUser> getFilteredList(List<String> keys) {
        return null;
    }

    @Override
    public void reload() {
        try (FileInputStream fi = new FileInputStream("phase2/repoFiles/demoUsers.bin")) {

            ObjectInputStream os = new ObjectInputStream(fi);

            Object obj;
            DemoUser demoUser;

            try {
                while ((obj = os.readObject()) != null) {
                    if (obj instanceof DemoUser) {
                        demoUser = (DemoUser) obj;
                        demoUserHashMap.put(demoUser.getUsername(), demoUser);
                    }
                }
            } catch (Exception ignored) { }

            os.close();

        } catch (IOException e) {
            System.out.println("[phase2/repoFiles/demoUsers.bin] not found");
        }
    }

    @Override
    public void onExit() {
        try (FileOutputStream fs = new FileOutputStream("phase2/repoFiles/demoUsers.bin")) {

            ObjectOutputStream os = new ObjectOutputStream(fs);

            for (DemoUser demoUser : demoUserHashMap.values())
                os.writeObject(demoUser);
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
