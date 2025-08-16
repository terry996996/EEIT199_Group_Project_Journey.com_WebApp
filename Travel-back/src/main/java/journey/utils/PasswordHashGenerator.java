package journey.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        
        // 為 admin 產生密碼 hash
        String adminPassword = "admin123";
        String adminHash = encoder.encode(adminPassword);
        System.out.println("Admin password hash: " + adminHash);
        
        // 為 hyatt 產生密碼 hash
        String hyattPassword = "hyatt@Journey";
        String hyattHash = encoder.encode(hyattPassword);
        System.out.println("Hyatt password hash: " + hyattHash);
        
        // 驗證密碼
        System.out.println("Admin password matches: " + encoder.matches(adminPassword, adminHash));
        System.out.println("Hyatt password matches: " + encoder.matches(hyattPassword, hyattHash));
    }
}
