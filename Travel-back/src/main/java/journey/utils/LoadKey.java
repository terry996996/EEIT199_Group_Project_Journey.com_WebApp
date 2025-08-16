package journey.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

/**
 * retrieve Private and Public key from a keypair in the keystore
 * 
 * @author 乃文
 * @version 1.0
 * @since 07/03/2025
 */
public class LoadKey {
    private static final String KEY_STORE_PATH = "src/main/java/journey/config/";

    private static KeyStore getKeyStore(String keyStoreName, String storePass) {
        if (keyStoreName != null && storePass != null) {
            Path storePath = Paths.get(KEY_STORE_PATH + keyStoreName);
            if (Files.exists(storePath)) {
                try {
                    return KeyStore.getInstance(storePath.toFile(), storePass.toCharArray());
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("KeyStore Not Exist!: " + keyStoreName);
            }
        }
        return null;
    }

    public static PrivateKey getPrivateKey(String keyStoreName, String storePass, String alias) {
        if (keyStoreName != null && storePass != null) {
            KeyStore keyStore = getKeyStore(keyStoreName, storePass);
            if (keyStore != null) {
                try {
                    return (PrivateKey) keyStore.getKey(alias, storePass.toCharArray());
                } catch (UnrecoverableKeyException e) {
                    e.printStackTrace();
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static PublicKey getPublicKey(String keyStoreName, String storePass, String alias) {
        if (keyStoreName != null && storePass != null) {
            KeyStore keyStore = getKeyStore(keyStoreName, storePass);
            if (keyStore != null) {
                try {
                    return keyStore.getCertificate(alias).getPublicKey();
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
