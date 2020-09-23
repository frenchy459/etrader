package Layer3.Repos;

import Layer1.Entities.AdminNotification;
import Layer2.Interfaces.ObserverEntityRepo;

import java.io.*;
import java.util.*;

/**
 * The gateway class AdminNotificationRepo is responsible for reading and writing AdminNotification objects to a file.
 */
public class AdminNotificationRepo implements ObserverEntityRepo<UUID, AdminNotification> {

    private final HashMap<UUID, AdminNotification> adminNotificationHashmap = new HashMap<>();

    /**
     * Instantiates a new Admin notification repo.
     */
    public AdminNotificationRepo() {
        reload();
    }

    @Override
    public AdminNotification get(UUID uuid) {
        return adminNotificationHashmap.get(uuid);
    }

    @Override
    public void add(AdminNotification object) {
        adminNotificationHashmap.put(object.getMID(), object);
    }

    @Override
    public void remove(UUID uuid) {
        adminNotificationHashmap.remove(uuid);
    }

    @Override
    public List<AdminNotification> getAll() {
        return new ArrayList<>(adminNotificationHashmap.values());
    }

    @Override
    public List<AdminNotification> getFilteredList(List<UUID> keys) {
        ArrayList<AdminNotification> notifications = new ArrayList<>();
        for (UUID uuid : keys)
            notifications.add(adminNotificationHashmap.get(uuid));
        return notifications;
    }

    @Override
    public void reload() {
        try (FileInputStream fi = new FileInputStream("phase2/repoFiles/adminNotifications.bin")) {

            ObjectInputStream os = new ObjectInputStream(fi);

            Object obj;
            AdminNotification notification;

            try {
                while ((obj = os.readObject()) != null) {
                    if (obj instanceof AdminNotification) {
                        notification = (AdminNotification) obj;
                        adminNotificationHashmap.put(notification.getMID(), notification);
                    }
                }
            } catch (Exception ignored) { }

            os.close();

        } catch (IOException e) {
            System.out.println("[phase2/repoFiles/adminNotifications.bin] not found");
        }
    }

    @Override
    public void onExit() {
        try (FileOutputStream fs = new FileOutputStream("phase2/repoFiles/adminNotifications.bin")) {

            ObjectOutputStream os = new ObjectOutputStream(fs);

            for (AdminNotification notification : adminNotificationHashmap.values())
                os.writeObject(notification);
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void update(Observable o, Object arg) {
        add((AdminNotification) arg);
    }
}
