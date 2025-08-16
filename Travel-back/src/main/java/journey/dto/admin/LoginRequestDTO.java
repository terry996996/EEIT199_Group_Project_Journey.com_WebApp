package journey.dto.admin;

/**
 * 管理員登入請求 DTO
 * 
 * @author Max
 * @since 2025-07-08
 */
public class LoginRequestDTO {
    private String username;
    private String password;

    public LoginRequestDTO() {
    }

    public LoginRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
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

    @Override
    public String toString() {
        return "LoginRequestDTO [username=" + username + ", password=" + password + "]";
    }
} 