package journey.dto.Comments;

public class LikeStatusResponseDTO {
    private boolean likedByCurrentUser;
    private int likeCount;
    private String message;

    public LikeStatusResponseDTO() {
    }

    public LikeStatusResponseDTO(boolean likedByCurrentUser, int likeCount, String message) {
        this.likedByCurrentUser = likedByCurrentUser;
        this.likeCount = likeCount;
        this.message = message;
    }

    public boolean isLikedByCurrentUser() {
        return likedByCurrentUser;
    }

    public void setLikedByCurrentUser(boolean likedByCurrentUser) {
        this.likedByCurrentUser = likedByCurrentUser;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LikeStatusResponseDTO [likedByCurrentUser=" + likedByCurrentUser + ", likeCount=" + likeCount
                + ", message=" + message + "]";
    }
}