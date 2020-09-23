package Layer3.Repos.JavaKeyStore;

import Layer2.Interfaces.KeyStoreRepo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * The JavaKeyStoreRepo class is responsible for the creation, instantiation and retrieval of information from the
 * Java Keystore (.jks).
 *
 * References:
 * https://niels.nu/blog/2016/java-rsa.html
 * https://www.baeldung.com/java-keystore
 *
 * NOTE: All other methods regarding .jks information updating is possible through the KeyStore class after being loaded
 * by the load() method in this class.
 */
public class JavaKeyStoreRepo implements KeyStoreRepo {

    private final char[] masterPassword = "password".toCharArray();

    @Override
    public void create(String filePath) {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(null, masterPassword);
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            keyStore.store(fileOutputStream, masterPassword);
            fileOutputStream.close();
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }
    }

    @Override
    public KeyStore load(String filePath) {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(filePath), masterPassword);
            return keyStore;
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public KeyPair getKeyPairFromKeyStore(KeyStore keyStore, String userName, String KeyPassword) {

        try {
            KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(KeyPassword.toCharArray());

            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(userName, keyPassword);

            java.security.cert.Certificate cert = keyStore.getCertificate(userName);
            PublicKey publicKey = cert.getPublicKey();
            PrivateKey privateKey = privateKeyEntry.getPrivateKey();

            return new KeyPair(publicKey, privateKey);
        } catch (UnrecoverableEntryException | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void storeKeyStore(KeyStore keyStore, String filePath) {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            keyStore.store(fos, masterPassword);
        } catch (CertificateException | NoSuchAlgorithmException | IOException | KeyStoreException e) {
            e.printStackTrace();
        }
    }
}
