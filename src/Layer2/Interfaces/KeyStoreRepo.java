package Layer2.Interfaces;

import java.security.KeyPair;
import java.security.KeyStore;

public interface KeyStoreRepo {

    /**
     * Creates a new Java KeyStore (.jks) file to a specified file path.
     *
     * @param filePath The file path that is desired to store the Java KeyStore.
     */
    void create(String filePath);
    /**
     * Loads the repo file corresponding to a particular Java Key Store for data access/modification.
     *
     * @param filePath The file path that is desired to store the Java KeyStore.
     * @return Return the currently loaded repo.
     */
    KeyStore load(String filePath);
    /**
     * Retrieves the KeyPair instance from a particular entry identified by an alias in a keystore.
     *
     * @param keyStore    The KeyStore instance.
     * @param alias       The nickname referencing associative keys found in the Java keystore.
     * @param KeyPassword Access password to a particular entry in the KeyStore instance.
     * @return The KeyPair instance associated with a particular entry in the .jks.
     */
    KeyPair getKeyPairFromKeyStore(KeyStore keyStore, String alias, String KeyPassword);
    /**
     * Updates a specified KeyStore at a particular file path with incoming information
     *
     * @param keyStore The KeyStore that is in need of updating.
     * @param filePath The file path that is desired to store the Java KeyStore.
     */
    void storeKeyStore(KeyStore keyStore, String filePath);

}
