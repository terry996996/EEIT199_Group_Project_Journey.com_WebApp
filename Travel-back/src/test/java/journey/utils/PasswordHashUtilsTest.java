package journey.utils;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordHashUtilsTest {
    @Test
    void testGetPasswordHash() {
        String pwd = "user@321";
        System.out.println(PasswordHashUtils.getPasswordHash(pwd));
    }

    @Test
    void testGetSalt() {
        for (int i = 0; i < 5; i++) {
            String salt = BCrypt.gensalt(16);
            System.out.println(salt + ", " + salt.getBytes(StandardCharsets.UTF_8) + ", "
                    + new String(salt.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
        }
    }

    @Test
    void testIsValidPassword() {
        // String pwd = "";
        String stored = "$2a$12$HSr8R6gDfXF6vf9cl.kvOuVJtz8pGzqM1LNSiIfX4HdTY7MQHEg0S";
        String input = "Admin@Journey";
        System.out.println(PasswordHashUtils.isValidPassword(input, stored));
    }
}
