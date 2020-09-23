package Layer2.Managers;

import Layer1.Entities.AdminUser;
import Layer1.Entities.BasicUser;
import Layer1.Entities.X509CertificateComponents.X509CertificateChainBuilder;
import Layer2.Interfaces.EntityRepo;
import Layer2.Interfaces.KeyStoreRepo;
import Layer2.JavaKeyStore.DataModificationJKS;

import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.UUID;

/**
 * A use case class containing various methods for AdminUsers
 */
public class AdminManager {

    DataModificationJKS dataModificationJKS = new DataModificationJKS();

    /** Create a new password for the admin.
     * @param password the intended password the admin would like the change the admin account to.
     * @param username the username of the corresponding admin.
     * @param repo a hashmap of AdminUser's to their corresponding username.
     * @param keyStoreRepo The KeyStore instance repository.
     * @param path The path of the .jks file that will be modified during password reset. This is loaded from the .json.
     */
    public void adminPasswordReset(String password, String username, EntityRepo<String, AdminUser> repo,
                                   KeyStoreRepo keyStoreRepo, String path) throws KeyStoreException {
        AdminUser adminUser = repo.get(username);
        if (adminUser == null) {
            return;
        }

        KeyPair serverKeyPair = dataModificationJKS.generateKeyPair();
        KeyPair userKeyPair = dataModificationJKS.generateKeyPair();

        String encryptedPassword = dataModificationJKS.encrypt(password, userKeyPair.getPublic());
        adminUser.setPassword(encryptedPassword);

        X509CertificateChainBuilder x509Certificate = new X509CertificateChainBuilder();
        X509Certificate[] encDecCertificate = x509Certificate.x509CertificateEncDec(serverKeyPair);

        KeyStore encryptDecryptKeyStore = keyStoreRepo.load(path);

        // Add to .jks encrypt/decrypt for BasicUser.
        encryptDecryptKeyStore.setKeyEntry(adminUser.getUsername(), userKeyPair.getPrivate(),
                dataModificationJKS.keyPasswordGenerator(adminUser).toCharArray(),
                encDecCertificate);

        // Add to .bin sign/verify for BasicUser.
        repo.add(adminUser);

        // Store the .jks files.
        keyStoreRepo.storeKeyStore(encryptDecryptKeyStore, path);

    }

    /**
     * Undoes the action of deleting the last item.
     *
     * @param basicUser The BasicUser instance who had their item deleted.
     */
    public void undoLastDeletedItem(BasicUser basicUser) {
        List<UUID> basicUserLendList = basicUser.getLendList();
        basicUserLendList.add(basicUser.getDeletedItem());
        basicUser.setDeletedItem(null);
    }
}
