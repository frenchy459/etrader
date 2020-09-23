package Layer2.JavaKeyStore;

import Layer1.Entities.User;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.Base64;
import java.util.Random;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * The DataModificationJKS class contains data modification-specific and cipher/decipher methods for password security.
 * References:
 * https://niels.nu/blog/2016/java-rsa.html
 */
public class DataModificationJKS {

    /**
     * Encrypts user passwords using RSA encryption and user's public key.
     *
     * @param plainUserPassword User password represented as a String form input
     * @param publicKeyUser The PublicKey instance of the user (client).
     * @return A ciphered (RSA-encrypted then Base64-encoded) user password as a String.
     */
    public String encrypt(String plainUserPassword, PublicKey publicKeyUser) {
        try {
            Cipher encryptedCipher = Cipher.getInstance("RSA");
            encryptedCipher.init(Cipher.ENCRYPT_MODE, publicKeyUser);

            byte[] cipherText = encryptedCipher.doFinal(plainUserPassword.getBytes(UTF_8));
            return Base64.getEncoder().encodeToString(cipherText);

        } catch(NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Decrypts the user's password using RSA encryption and user's private key.
     *
     * @param cipherText The ciphered (RSA-encrypted then Base64-encoded) user password as a String.
     * @param privateKeyUser The client side private key that corresponds to the encryption public key.
     * @return A plain text equivalent to the ciphered parameter cipherText.
     */
    public String decrypt(String cipherText, PrivateKey privateKeyUser) {
        try {
            byte[] bytes = Base64.getDecoder().decode(cipherText);

            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKeyUser);

            return new String(decryptCipher.doFinal(bytes), UTF_8);
        } catch(NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Generates a new KeyPair instance containing private and public keys.
     *
     * @return KeyPair of a newly generated pair of private and public keys.
     */
    public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024, new SecureRandom()); // Keys of 1024-bit length.
            return kpg.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Generates the password for accessing user information stored in a .jks entry.
     * The algorithm generates frequently consistent random int based on a seed equivalent to the
     * user's numerical-based UUID (represented as a long).
     *
     * @param user The instance of either the AdminUser or BasicUser instance.
     * @return The key password to some designated .jks file.
     */
    public String keyPasswordGenerator(User user) {
        long ID = user.getID();
        Random random = new Random();
        random.setSeed(ID);
        return Integer.toString(random.nextInt());
    }


}
