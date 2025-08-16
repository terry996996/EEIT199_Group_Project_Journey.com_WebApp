package journey.dto.user;

import journey.enums.UserTypeEnum;

public class JwtDTO {
    private Integer id;
    private String username;
    private String email;
    private UserTypeEnum userType;

    public JwtDTO(Integer id, String username, String email, UserTypeEnum userType) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.userType = userType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(UserTypeEnum userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "JwtDTO [id=" + id + ", username=" + username + ", email=" + email + ", userType=" + userType + "]";
    }

}
