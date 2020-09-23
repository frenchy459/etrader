package Layer2.Interfaces;
import java.util.Observable;
import java.util.Observer;

/**
 * The interface Entity repo.
 *
 * @param <U> the type parameter representing the either UUIDs for ItemsRepo and TransactionRepo or Strings for User
 * Repo
 * @param <O> the type parameter of objects stored in its respective Repo.
 */

public interface ObserverEntityRepo<U, O> extends EntityRepo<U, O>, Observer {

    @Override
    void update(Observable o, Object arg);

}
