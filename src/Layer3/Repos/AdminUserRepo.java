package Layer3.Repos;

import Layer1.Entities.AdminUser;
import Layer1.Enums.AccountType;
import Layer2.Interfaces.EntityRepo;
import Layer2.Managers.RegistrationManager;
import Layer3.Repos.JavaKeyStore.JavaKeyStoreRepo;

import java.io.*;
import java.security.KeyStoreException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The gateway class AdminUserRepo is responsible for reading and writing AdminUser objects to a file.
 */
public class AdminUserRepo implements EntityRepo<String, AdminUser> {

    private final HashMap<String, AdminUser> adminUserHashMap = new HashMap<>();
    RegistrationManager registrationManager = new RegistrationManager();
    BasicUserRepo basicUserRepo = new BasicUserRepo();
    JavaKeyStoreRepo javaKeyStoreRepo = new JavaKeyStoreRepo();

    /**
     * Instantiates a new Admin user repo.
     */
    public AdminUserRepo() throws KeyStoreException {
        reload();

        if (adminUserHashMap.size() == 0) {  //after reload, if no admins
            registrationManager.registerAccount("admin", "12345",
                    "", basicUserRepo, this, AccountType.ADMIN,
                    javaKeyStoreRepo, 0, 3, 3,
                    "phase2/repoFiles/encryptDecrypt.jks");
        }
    }

    @Override
    public AdminUser get(String username) {
        return adminUserHashMap.get(username);
    }

    @Override
    public void add(AdminUser admin) {
        adminUserHashMap.put(admin.getUsername(), admin);
    }

    @Override
    public void remove(String username) {
        adminUserHashMap.remove(username);
    }

    @Override
    public List<AdminUser> getAll() {
        return new ArrayList<>(adminUserHashMap.values());
    }

    @Override
    public List<AdminUser> getFilteredList(List<String> keys) {
        ArrayList<AdminUser> admins = new ArrayList<>();
        for (String username : keys)
            admins.add(adminUserHashMap.get(username));
        return admins;
    }

    @Override
    public void reload() {
        try (FileInputStream fi = new FileInputStream("phase2/repoFiles/adminUsers.bin")) {

            ObjectInputStream os = new ObjectInputStream(fi);

            Object obj;
            AdminUser adminUser;

            try {
                while ((obj = os.readObject()) != null) {
                    if (obj instanceof AdminUser) {
                        adminUser = (AdminUser) obj;
                        adminUserHashMap.put(adminUser.getUsername(), adminUser);
                    }
                }
            } catch (Exception ignored) { }

            os.close();

        } catch (IOException e) {
            System.out.println("[phase2/repoFiles/adminUsers.bin] not found");
        }
    }

    @Override
    public void onExit() {
        try (FileOutputStream fs = new FileOutputStream("phase2/repoFiles/adminUsers.bin")) {

            ObjectOutputStream os = new ObjectOutputStream(fs);

            for (AdminUser adminUser : adminUserHashMap.values())
                os.writeObject(adminUser);
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
