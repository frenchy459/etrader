package Layer2.Interfaces;
import java.util.List;

/**
 * The interface Entity repo.
 *
 * @param <U> the type parameter representing the either UUIDs for ItemsRepo and TransactionRepo or Strings for User
 * Repo
 * @param <O> the type parameter of objects stored in its respective Repo.
 */
public interface EntityRepo<U, O> {

    /**
     * Gets object from respective repo
     *
     * @param key the uuid/username
     * @return the object (Item/Transaction/User)
     */
    O get(U key);

    /**
     * Adds an object to respective repo
     *
     * @param object the object
     */
    void add(O object);

    /**
     * Remove object from respective repo
     *
     * @param uuid the uuid of the object
     */
    void remove(U uuid);

    /**
     * Gets all objects from respective repo as a list, no guarantee of order.
     *
     * @return List</O>
     */
    List<O> getAll();

    /**
     * Gets list of objects based on list of keys given.
     *
     * @param keys the keys of desired objects
     * @return a list with objects with desired keys.
     */
    List<O> getFilteredList(List<U> keys);

    /**
     * Reload the repo to reflect any changes made to .bin file
     */
    void reload();

    /**
     * Saves repo to file, must call when mutating object in repo or else changes will be lost.
     */
    void onExit();
}
