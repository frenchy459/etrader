package Layer3.Repos;

import Layer1.Entities.BasicUser;
import Layer2.Interfaces.EntityRepo;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The gateway class BasicUserRepo is responsible for reading and writing BasicUsers objects to a file.
 */
public class BasicUserRepo implements EntityRepo<String, BasicUser> {

    private final HashMap<String, BasicUser> basicUserHashMap = new HashMap<>();

    /**
     * Instantiates a new Users repo.
     */
    public BasicUserRepo() {
        reload();
    }

    @Override
    public BasicUser get(String username) {
        return basicUserHashMap.get(username);
    }

    @Override
    public void add(BasicUser user) {
        basicUserHashMap.put(user.getUsername(), user);
    }

    @Override
    public void remove(String username) {
        basicUserHashMap.remove(username);
    }

    @Override
    public List<BasicUser> getAll() {
        return new ArrayList<>(basicUserHashMap.values());
    }

    @Override
    public List<BasicUser> getFilteredList(List<String> keys) {
        ArrayList<BasicUser> users = new ArrayList<>();
        for (String username : keys)
            users.add(basicUserHashMap.get(username));
        return users;
    }

    @Override
    public void reload() {
        try (FileInputStream fi = new FileInputStream("phase2/repoFiles/basicUsers.bin")) {

            ObjectInputStream os = new ObjectInputStream(fi);

            Object obj;
            BasicUser basicUser;

            try {
                while ((obj = os.readObject()) != null) {
                    if (obj instanceof BasicUser) {
                        basicUser = (BasicUser) obj;
                        basicUserHashMap.put(basicUser.getUsername(), basicUser);
                    }

                }
            } catch (Exception ignored) { }

            os.close();

        } catch (IOException e) {
            System.out.println("[phase2/repoFiles/basicUsers.bin] not found");
        }
    }

    // Writes all User objects in userHashMap back to file
    @Override
    public void onExit() {
        try (FileOutputStream fs = new FileOutputStream("phase2/repoFiles/basicUsers.bin")) {

            ObjectOutputStream os = new ObjectOutputStream(fs);

            for (BasicUser basicUser : basicUserHashMap.values())
                os.writeObject(basicUser);
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
