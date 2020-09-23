package Layer2.Managers;

import Layer1.Entities.AdminUser;
import Layer1.Entities.BasicUser;
import Layer1.Entities.X509CertificateComponents.X509CertificateChainBuilder;
import Layer1.Enums.AccountType;
import Layer1.Enums.RegistrationReturn;
import Layer2.API.MapAPI.GeoApiContext;
import Layer2.API.MapAPI.GeocodingApi;
import Layer2.API.MapAPI.GeocodingApiRequest;
import Layer2.API.MapAPI.gson.Gson;
import Layer2.API.MapAPI.gson.GsonBuilder;
import Layer2.API.MapAPI.model.AddressType;
import Layer2.API.MapAPI.model.GeocodingResult;
import Layer2.API.MapAPI.model.LatLng;
import Layer2.Interfaces.EntityRepo;
import Layer2.Interfaces.KeyStoreRepo;
import Layer2.JavaKeyStore.DataModificationJKS;

import java.net.URLEncoder;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The use case class concerned with the registration of new users to the program.
 */
public class RegistrationManager {
    private EntityRepo<String, BasicUser> basicUserRepo;
    DataModificationJKS dataModificationJKS = new DataModificationJKS();

    /**
     * Return whether the user inputs registered an account.
     *
     * @param userName A String representing the username entered by the user.
     * @param password A String representing the password entered by the user.
     * @param address A String representing the address entered by the user.
     * @param basicUserEntityRepo An EntityRepo objects storing a hashmap of String and BasicUser
     * @param adminUserEntityRepo An EntityRepo objects storing a hashmap of String and AdminUser.
     * @param keyStoreRepo A KeyStore containing Keys of registered users.
     * @param accountType The type of account that is undergoing registration.
     * @param lentOverBorrow The ratio concerning lending-borrowing.
     * @param maxIncompleteTransactions The threshold value indicating the maximum incomplete transactions per week.
     * @param maxTransactionsPerWeek The threshold value indicating the maximum transactions per week.
     * @param path The path of the .jks file that will be modified during password reset. This is loaded from the .json.
     * @throws KeyStoreException If a type of KeyStoreException occurs.
     * @return The registration state concerning whether the registration attempt is valid or invalid.
     */
    public RegistrationReturn registerAccount(String userName, String password, String address,
                                              EntityRepo<String, BasicUser> basicUserEntityRepo,
                                              EntityRepo<String, AdminUser> adminUserEntityRepo, AccountType accountType,
                                              KeyStoreRepo keyStoreRepo,
                                              int lentOverBorrow, int maxTransactionsPerWeek,
                                              int maxIncompleteTransactions, String path)
            throws KeyStoreException {


        //only takes in --> digits Street_name Street_type City Province
        //ex: 123 Example Street Toronto Ontario

        if (userName.length() < 2) {
            return RegistrationReturn.NAME_TOO_SHORT;
        } else if ( basicUserEntityRepo.get(userName) != null || adminUserEntityRepo.get(userName) != null) {
            return RegistrationReturn.NAME_EXISTS;
        }

        if (accountType.equals(AccountType.ADMIN))
        {

            AdminUser adminUser = new AdminUser(userName, password, lentOverBorrow, maxTransactionsPerWeek,
                    maxIncompleteTransactions);
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

            adminUserEntityRepo.add(adminUser);

            // Store the .jks files.
            keyStoreRepo.storeKeyStore(encryptDecryptKeyStore, path);

            return RegistrationReturn.VALID;
        }
        else {
            //forces pattern: 123 Example Street, City, ON, Canada
            Pattern p = Pattern.compile("^\\d+(\\s[A-Za-z]+){2,},(\\s[A-Za-z.,]+)+,( ON, Canada)$");
            Matcher match = p.matcher(address);
            if (!match.matches()) {
                return RegistrationReturn.INVALID_ADDRESS;
            } else {
                List<Object> addressComponent = convertAddress(address);
                basicUserEntityRepo.add(new BasicUser(userName, password, addressComponent, lentOverBorrow,
                        maxTransactionsPerWeek, maxIncompleteTransactions));

                BasicUser basicUser = new BasicUser(userName, password, addressComponent, lentOverBorrow,
                        maxTransactionsPerWeek, maxIncompleteTransactions);
                KeyPair serverKeyPair = dataModificationJKS.generateKeyPair();

                KeyPair userKeyPair = dataModificationJKS.generateKeyPair();
                String encryptedPassword = dataModificationJKS.encrypt(password, userKeyPair.getPublic());

                basicUser.setPassword(encryptedPassword);

                X509CertificateChainBuilder x509Certificate = new X509CertificateChainBuilder();
                X509Certificate[] encDecCertificate = x509Certificate.x509CertificateEncDec(serverKeyPair);

                KeyStore encryptDecryptKeyStore = keyStoreRepo.load(path);

                // Add to .jks encrypt/decrypt for BasicUser.
                encryptDecryptKeyStore.setKeyEntry(basicUser.getUsername(), userKeyPair.getPrivate(),
                        dataModificationJKS.keyPasswordGenerator(basicUser).toCharArray(),
                        encDecCertificate);

                basicUserEntityRepo.add(basicUser);
                this.basicUserRepo = basicUserEntityRepo;

                // Store the .jks files.
                keyStoreRepo.storeKeyStore(encryptDecryptKeyStore, path);

                return RegistrationReturn.VALID;
            }
        }
    }


    /**
     * Converts the address input of the program to a list containing a latLng object and a String representation of
     * the city.
     *
     * @param address The address inputted for the BasicUser registration attempt.
     * @return Converts input address to a list containing a latLng object and a String representation of the city.
     */
    public List<Object> convertAddress(String address){
        try {
            String urlAddress = URLEncoder.encode(address, "UTF-8");
            @SuppressWarnings("SpellCheckingInspection")
            GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey(
                    "AIzaSyAbUgjaY2LjckSSljG6lZUHxJM98GHygN8").queryRateLimit(200).build();
            GeocodingResult[] results = GeocodingApi.geocode(geoApiContext, urlAddress).await();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            //getting lat and long from result
            String lat = gson.toJson(results[0].geometry.location.lat);
            String lng = gson.toJson(results[0].geometry.location.lng);
            //pass in lat and long as double and create a LatLng object
            LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            //Making a reverse geocoding request with latLng object
            GeocodingApiRequest reverseRequest = GeocodingApi.reverseGeocode(geoApiContext, latLng);
            //make the result type return city
            reverseRequest.resultType(AddressType.LOCALITY);
            GeocodingResult[] reverseResult = reverseRequest.await();
            String reverseAddress = gson.toJson(reverseResult[0].formattedAddress);
            String[] cityLine = reverseAddress.split(",");
            String city = cityLine[0];
            List<Object> addressComponent = new ArrayList<>();
            addressComponent.add(latLng);
            addressComponent.add(city);
            geoApiContext.shutdown();
            return addressComponent;
        } catch(Exception e) { return null; }
    }

    /**
     * Retrieves the updated BasicUser instance repository.
     * @return An updated version of the BasicUser instance repository.
     */
    public EntityRepo<String, BasicUser> getUpdatedBasicUserRepo() {
        return this.basicUserRepo;
    }
}
