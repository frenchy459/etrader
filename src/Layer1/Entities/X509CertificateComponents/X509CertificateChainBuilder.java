package Layer1.Entities.X509CertificateComponents;

import java.security.KeyPair;
import java.security.cert.X509Certificate;

/**
 * The X509CertificateChainBuilder class is for the construction of X.509 Certificates using uniquely set
 * information form the X509CertificateInfo class.
 *
 * References:
 * https://www.pixelstech.net/article/1408524957-Generate-certificate-in-Java----Store-certificate-in-KeyStore
 */
public class X509CertificateChainBuilder {

    /**
     * The X509CertificateInfo class constructs descriptive elements describing the X.509 Certificate instance.
     */
    X509CertificateInfo x509CertificateInfo = new X509CertificateInfo();


    /**
     * Constructs an X.509 Certificate chain specified for encryption + decryption information.
     *
     * @param serverKeyPair The KeyPair representing the server-based key (used for password signatures and verification).
     * @return The X509Certificate[] ("X509Certificate chain") containing information regarding password
     * encryption + decryption.
     */
    public X509Certificate[] x509CertificateEncDec(KeyPair serverKeyPair) {
        X509Certificate serverCert = x509CertificateInfo.constructCertificate("CN=ROOT", serverKeyPair);

        X509Certificate[] chain = new X509Certificate[1];

        chain[0] = serverCert;

        return chain;
    }


}
