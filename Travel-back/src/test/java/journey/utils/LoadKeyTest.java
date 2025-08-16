package journey.utils;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import org.junit.jupiter.api.Test;

public class LoadKeyTest {

    private static final String STORE_NAME = "jwtkeystore.jks";
    private static final String STORE_PASS = "P@ssw0rd";
    private static final String ALIAS = "EEIT199Travel_JWT";

    @Test
    void testGetPrivateKey() {
        PrivateKey key = LoadKey.getPrivateKey(STORE_NAME, STORE_PASS, ALIAS);
        byte[] encoded = key.getEncoded();
        String base64 = Base64.getEncoder().encodeToString(encoded);
        for (int i = 0; i < base64.length(); i += 64) {
            System.out.println(base64.substring(i, Math.min(i + 64, base64.length())));
        }
    }

    @Test
    void testGetPublicKey() {
        PublicKey key = LoadKey.getPublicKey(STORE_NAME, STORE_PASS, ALIAS);
        byte[] encoded = key.getEncoded();
        String base64 = Base64.getEncoder().encodeToString(encoded);
        for (int i = 0; i < base64.length(); i += 64) {
            System.out.println(base64.substring(i, Math.min(i + 64, base64.length())));
        }
    }
}
