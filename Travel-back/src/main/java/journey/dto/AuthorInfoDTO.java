package journey.dto;

import journey.enums.UserTypeEnum;

public class AuthorInfoDTO {
    private Integer id;
    private String username;
    private String avatarUrl;
    private UserTypeEnum userType;

    public AuthorInfoDTO() {
    }

    public AuthorInfoDTO(Integer id, String username, String avatarUrl) {
        this.id = id;
        this.username = username;
        this.avatarUrl = avatarUrl;
    }

    public AuthorInfoDTO(Integer id, String username, String avatarUrl, UserTypeEnum userType) {
        this.id = id;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "AuthorInfoDTO [id=" + id + ", username=" + username + ", avatarUrl=" + avatarUrl + ", userType="
                + userType + "]";
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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public UserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(UserTypeEnum userType) {
        this.userType = userType;
    }

}
