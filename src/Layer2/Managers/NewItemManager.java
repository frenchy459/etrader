package Layer2.Managers;

import Layer1.Entities.AdminNotification;
import Layer1.Entities.BasicUser;
import Layer1.Entities.DemoUser;
import Layer1.Entities.Item;
import Layer1.Enums.AdminNotificationType;
import Layer2.Interfaces.EntityRepo;
import Layer2.Interfaces.ImageEntityRepo;
import Layer2.Interfaces.ObserverEntityRepo;
import javafx.scene.image.Image;

import java.util.Observable;
import java.util.UUID;

/**
 * The use case class responsible for when a basic user is requesting to have an item created.
 */
public class NewItemManager extends Observable {

    private final UserManager userManager;
    private final ObserverEntityRepo<UUID, AdminNotification> adminNotificationRepo;

    /**
     * Instantiates a new Item manager.
     *
     * @param userManager The UserManager instance.
     * @param adminNotificationRepo The repository containing all AdminNotification instances.
     */
    public NewItemManager(UserManager userManager, ObserverEntityRepo<UUID, AdminNotification> adminNotificationRepo) {
        this.userManager = userManager;
        this.adminNotificationRepo = adminNotificationRepo;
    }

    /**
     * Creates the unapproved item and sends a notification to the admin.
     *
     * @param name        The name of the Item instance.
     * @param description The description of the Item instance.
     * @param image       Image of the Item instance.
     * @param imageRepo   Repository containing all Image instances associated with the users.
     * @param itemRepo    Repository containing all Item instances.
     * @param ownerName   Owner of the Item instance creation request.
     * @param message     The message associated with the particular admin notification.
     */
    public void createItem(String name, String description, Image image, EntityRepo<UUID, Item> itemRepo,
                           ImageEntityRepo<String, Image> imageRepo, String ownerName, String message) {

        Item newItem = new Item(name, description, ownerName);
        itemRepo.add(newItem);
        imageRepo.setCurrentItem(newItem);
        imageRepo.add(image);

        ((BasicUser) userManager.getCurrentUserObject()).getLendList().add(newItem.getUUID());

        if (!(userManager.getCurrentUserObject() instanceof DemoUser)) {
            AdminNotification notification = new AdminNotification(userManager.getCurrentUser(),
                    AdminNotificationType.ITEM_REQUEST,
                    newItem.getUUID(), message);
            addObserver(adminNotificationRepo);
            setChanged();
            notifyObservers(notification);
        }
    }
}
