package Layer2.Managers;

import Layer1.Entities.AdminNotification;
import Layer1.Entities.Item;
import Layer1.Entities.User;
import Layer1.Enums.AdminNotificationType;
import Layer2.Interfaces.EntityRepo;
import Layer2.Interfaces.ObserverEntityRepo;

import java.util.*;

/**
 * A class containing various methods related to admin notifications.
 */
public class AdminNotificationManager extends Observable {

    private final ObserverEntityRepo<UUID, AdminNotification> adminNotificationRepo;
    private final EntityRepo<UUID, Item> itemsRepo;

    /**
     * Creates an user notification use case class instance.
     *
     * @param adminNotificationRepo An EntityRepo object containing AdminNotifications.
     * @param itemsRepo             An EntityRepo object containing Items.
     */
    public AdminNotificationManager(ObserverEntityRepo<UUID, AdminNotification> adminNotificationRepo,
                                    EntityRepo<UUID, Item> itemsRepo) {

        this.adminNotificationRepo = adminNotificationRepo;
        this.itemsRepo = itemsRepo;
    }

    /**
     * Gets the list of notifications related to approve items.
     *
     * @return A list of AdminNotification objects.
     */
    public List<AdminNotification> getApproveItemsNotificationList() {

        List<AdminNotification> itemApproveNotificationList = new ArrayList<>();

        for (AdminNotification adminNotification : adminNotificationRepo.getAll()) {
            if (adminNotification.getUnapprovedItemUUID() != null) {
                if (!itemsRepo.get(adminNotification.getUnapprovedItemUUID()).getIsApproved()) {
                    itemApproveNotificationList.add(adminNotification);
                }
            }
        }

        return itemApproveNotificationList;
    }

    /**
     * Creates a new admin notification.
     *
     * @param currentUser The User who is currently logged into the program.
     * @param type The type of AdminNotificationType instance. This will advise the program to deliver a particular
     *             administrative notification.
     * @param message A string representing the notification message
     */
    public void createNewAdminNotification(User currentUser, AdminNotificationType type, String message) {
            AdminNotification notification = new AdminNotification(currentUser.getUsername(),
                    type, message);
            addObserver(adminNotificationRepo);
            setChanged();
            notifyObservers(notification);
        }
    }
