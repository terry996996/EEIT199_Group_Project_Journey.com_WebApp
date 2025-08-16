package journey.dto.admin;

/**
 * 管理員登入回應 DTO
 * 
 * @author Max
 * @since 2025-07-08
 */
public class LoginResponseDTO {
    private String token;
    private Integer userId;
    private String username;
    private String email;
    private String userType;
    private String icon;
    private String vendorName;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String token, Integer userId, String username, String email, String userType, String icon, String vendorName) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.userType = userType;
        this.icon = icon;
        this.vendorName = vendorName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    @Override
    public String toString() {
        return "LoginResponseDTO [token=" + token + ", userId=" + userId + ", username=" + username + ", email=" + email + ", userType=" + userType + ", icon=" + icon + ", vendorName=" + vendorName + "]";
    }
} 