package journey.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * user password related class, generate password hash and validation
 * 
 * @author 乃文
 * @version 2.0 change to all String outputs
 * @since 07/03/2025
 */
public class PasswordHashUtils {

    /**
     * constant string, being encrpyted along with password and salt
     */
    private static final String PEPPER = "EEIT199Travel_PASSWORD_PEPPER";
    /**
     * constant integer, defines how many rounds to generate the salt
     */
    private static final int SALT_ROUND = 12;

    /**
     * genereates a fixed length binary array salt
     * 
     * @return a binary array with length = SALT_LEN
     */
    public static String getSalt() {
        return BCrypt.gensalt(SALT_ROUND);
    }

    /**
     * generates a password hash with salt and pepper
     * 
     * @param passwordString user enter password
     * @return the hashed password with salt and pepper
     */
    public static String getPasswordHash(String passwordString) {
        if (passwordString != null && passwordString.length() != 0) {
            String saltString = getSalt();

            return BCrypt.hashpw(passwordString + PEPPER, saltString);
        }
        return null;
    }

    /**
     * check if the input password string matches the stored password
     * 
     * @param input     user input for validation
     * @param storedPwd password hash stored in database
     * @return if matches then true, else then false
     */
    public static boolean isValidPassword(String input, String storedPwd) {
        if (input != null && input.length() != 0 && storedPwd != null
                && storedPwd.length() != 0) {
            return BCrypt.checkpw(input + PEPPER, storedPwd);
        }
        return false;
    }
}
