package Layer2.Managers;

import Layer1.Entities.BasicUser;
import Layer1.Entities.Item;
import Layer2.Interfaces.EntityRepo;
import javafx.scene.image.Image;

import java.util.UUID;

/**
 * The use case class responsible for determining the menuState from user input and uses Presentable to display
 * the menu for the item approval menu.
 */
public class ItemManager {

    /**
     * Determines the menuState regarding item approval. This will always approve the item.
     *
     * @param item The Item instance.
     */
    public void approveItem(Item item) {
        item.setIsApproved(true);
    }

    /**
     * Declares the menuState regarding item approval. This will always reject the item.
     *
     * @param item                   The Item instance.
     * @param itemEntityRepo         The Item instance repository.
     * @param imageEntityRepo        The Image instance repository.
     * @param basicUserEntityRepo    The BasicUser instance repository.
     */
    public void rejectItem(Item item, EntityRepo<UUID, Item> itemEntityRepo, EntityRepo<String, Image> imageEntityRepo,
                           EntityRepo<String, BasicUser> basicUserEntityRepo) {
        for (BasicUser basicUser : basicUserEntityRepo.getAll())
            if (basicUser.getLendList().contains(item.getUUID())) {
                basicUser.getLendList().remove(item.getUUID());
                break;
            }

        itemEntityRepo.remove(item.getUUID());
        imageEntityRepo.remove(item.getUUID().toString());
    }
}
