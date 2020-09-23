package Layer2.Managers;

import Layer1.Entities.AdminUser;
import Layer1.Entities.BasicUser;
import Layer1.Entities.User;
import Layer2.Interfaces.EntityRepo;
import Layer2.Interfaces.KeyStoreRepo;
import Layer2.JavaKeyStore.DataModificationJKS;

import java.security.KeyPair;
import java.security.KeyStore;

/**
 * The use case class concerning the Log In feature of the program. This is used by all User instances
 * excluding DemoUser.
 */
public class LoginManager {

    /** return if the user who tries to log in is a valid user.
     *
     * @param userName a string representing the user's input username.
     * @param userPassword a string representing the user's input password
     * @param basicUserEntityRepo The BasicUser instance repository.
     * @param adminUserEntityRepo The AdminUser instance repository.
     * @param keyStoreRepo a KeyStore containing registered user keys.
     * @param path The path of the .jks file that will be modified during password reset. This is loaded from the .json.
     * @return a boolean representing whether or not the user is valid.
     */
    public User validateLoggedInUser(String userName, String userPassword,
                                     EntityRepo<String, BasicUser> basicUserEntityRepo,
                                     EntityRepo<String, AdminUser> adminUserEntityRepo,
                                     KeyStoreRepo keyStoreRepo, String path) {
        try {
            BasicUser basicUser = basicUserEntityRepo.get(userName);

            String encryptedPassword = basicUser.getPassword();

            DataModificationJKS dataModificationJKS = new DataModificationJKS();

            KeyStore keyStore = keyStoreRepo.load(path);

            // Password Decryption Protocol
            String keyPassword = dataModificationJKS.keyPasswordGenerator(basicUser);
            KeyPair encryptionDecryptionKeyPair = keyStoreRepo.getKeyPairFromKeyStore(keyStore, userName, keyPassword);
            String decryptedPassword = dataModificationJKS.decrypt(encryptedPassword, encryptionDecryptionKeyPair.getPrivate());

            if (decryptedPassword.equals(userPassword))
                return basicUser;

        } catch (Exception e) {
            try {
                AdminUser adminUser = adminUserEntityRepo.get(userName);

                String encryptedPassword = adminUser.getPassword();

                DataModificationJKS dataModificationJKS = new DataModificationJKS();

                KeyStore keyStore = keyStoreRepo.load(path);

                // Password Decryption Protocol
                String keyPassword = dataModificationJKS.keyPasswordGenerator(adminUser);
                KeyPair encryptionDecryptionKeyPair = keyStoreRepo.getKeyPairFromKeyStore(keyStore, userName, keyPassword);
                String decryptedPassword = dataModificationJKS.decrypt(encryptedPassword, encryptionDecryptionKeyPair.getPrivate());


                if (decryptedPassword.equals(userPassword))
                    return adminUser;

            } catch (Exception e1) {
                return null;
            }
        }
        return null;
    }

}
