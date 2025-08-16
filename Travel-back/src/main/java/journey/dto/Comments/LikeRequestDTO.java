package journey.dto.Comments;

public class LikeRequestDTO {
    private Integer userId;

    public LikeRequestDTO() {
    }

    public LikeRequestDTO(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "LikeRequestDTO [userId=" + userId + "]";
    }
}