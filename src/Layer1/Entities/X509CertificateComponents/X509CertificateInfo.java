package Layer1.Entities.X509CertificateComponents;

import sun.security.x509.*;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

/**
 * The X509CertificateInfo class for gathering information to produce an X.509 Certificate. Information
 * consists of:
 * - Creation Date
 * - Expiration Date
 * - Total Certificate Validity Duration
 * - Serial Number
 * - Distinguished name ("dn")
 * - Algorithm ID.
 * - Certificate Serial Number
 * - Certificate X.509 Key
 * - Certificate Version
 * - Certificate Algorithm ID.
 *
 * References:
 * https://www.pixelstech.net/article/1408524957-Generate-certificate-in-Java----Store-certificate-in-KeyStore
 */
class X509CertificateInfo {

    private Date declareCreationDate() {
        return new Date();
    }

    private Date declareExpirationDate() {
        return new Date(declareCreationDate().getTime() + 365);
    }

    private CertificateValidity declareCertificateValidityDuration(Date from, Date to) {
        return new CertificateValidity(from, to);
    }

    private BigInteger declareSerialNumber() {
        return new BigInteger(64, new SecureRandom());
    }

    private X500Name declareOwner(String dn) throws IOException {
        return new X500Name(dn);
    }

    private AlgorithmId declareAlgorithmId() {
        return new AlgorithmId(AlgorithmId.sha256WithRSAEncryption_oid);
    }

    private CertificateSerialNumber declareCertificateSerialNumber(BigInteger serialNumber) {
        return new CertificateSerialNumber(serialNumber);
    }

    private CertificateX509Key declareCertificateX509Key(PublicKey publicKey) {
        return new CertificateX509Key(publicKey);
    }

    private CertificateVersion declareCertificateVersion() throws IOException {
        return new CertificateVersion(CertificateVersion.V3);
    }

    private CertificateAlgorithmId declareCertificateAlgorithmId() {
        return new CertificateAlgorithmId(declareAlgorithmId());
    }

    /**
     * Pairs identification-related information to the X.509 Certificate.
     *
     * @param publicKey The PublicKey instance of some KeyPair instance.
     * @param dn The distinguished name.
     * @return The information intended to characterize the X.509 Certificate.
     */
    private X509CertInfo setX509CertInfo(PublicKey publicKey, String dn) {
        try {
            X509CertInfo certInfo = new X509CertInfo();

            Date from = declareCreationDate();
            Date to = declareExpirationDate();
            CertificateValidity certificateValidityDuration = declareCertificateValidityDuration(from, to);
            BigInteger serialNumber = declareSerialNumber();
            X500Name owner = declareOwner(dn);
            CertificateX509Key x509Key = declareCertificateX509Key(publicKey);

            certInfo.set(X509CertInfo.VALIDITY, certificateValidityDuration);
            certInfo.set(X509CertInfo.SERIAL_NUMBER, declareCertificateSerialNumber(serialNumber));
            certInfo.set(X509CertInfo.SUBJECT, owner);
            certInfo.set(X509CertInfo.ISSUER, owner);
            certInfo.set(X509CertInfo.KEY, x509Key);
            certInfo.set(X509CertInfo.VERSION, declareCertificateVersion());
            certInfo.set(X509CertInfo.ALGORITHM_ID, declareCertificateAlgorithmId());

            return certInfo;
        } catch (IOException | CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Signs the X.509 Certificate with a PrivateKey instance.
     *
     * @param certificateInfo The characteristics describing the X.509 Certificate's credentials.
     * @param privateKey The PrivateKey instance of some KeyPair instance.
     * @return The signed X.509 Certificate.
     */
    private X509Certificate signCertificate(X509CertInfo certificateInfo, PrivateKey privateKey) {
        try {
            X509CertImpl certificate = new X509CertImpl(certificateInfo);
            certificate.sign(privateKey, "SHA256WithRSA");

            AlgorithmId sigAlgId = (AlgorithmId) certificate.get(X509CertImpl.SIG_ALG);
            certificateInfo.set(CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM, sigAlgId);
            certificate = new X509CertImpl(certificateInfo);
            certificate.sign(privateKey, "SHA256WithRSA");

            return certificate;
        } catch(NoSuchProviderException | CertificateException | NoSuchAlgorithmException | InvalidKeyException | SignatureException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Builds a signed X.509 Certificate with information about itself.
     *
     * @param dn The distinguished name.
     * @param keyPair The KeyPair instance containing a PublicKey and PrivateKey instance.
     * @return The signed X.509 Certificate with associative descriptive credentials.
     */
    public X509Certificate constructCertificate(String dn, KeyPair keyPair) {
        X509CertInfo x509CertInfo = setX509CertInfo(keyPair.getPublic(), dn);
        return signCertificate(x509CertInfo, keyPair.getPrivate());
    }

}
