package Layer2.Interfaces;

import Layer1.Entities.Item;
import Layer1.Entities.User;

/**
 * The interface Image Entity repo.
 *
 * @param <U> the type parameter representing the either UUIDs for ImageRepo
 * @param <O> the type parameter of objects stored in its respective Repo.
 */
public interface ImageEntityRepo<U, O> extends EntityRepo<U, O> {

    /**
     * Sets the current item as the Item object
     *
     * @param item an Item object
     */

    void setCurrentItem(Item item);

    /**
     * Sets the current user as the User object
     *
     * @param user a User object
     */
    void setCurrentUser(User user);


    /**
     * Returns whether the image exists
     *
     * @param key the key of the desired image
     * @return true if the image exists and false otherwise
     */
    boolean imageExists(String key);

}
