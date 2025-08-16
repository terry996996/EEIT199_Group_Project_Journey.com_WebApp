package journey.dto.admin;

/**
 * 創建商家請求 DTO
 * 
 * @author Max
 * @since 2025-07-08
 */
public class CreateVendorRequestDTO {
    private String vendorName;
    private String username;
    private String password;
    private String email;

    // Getters and Setters
    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
} 