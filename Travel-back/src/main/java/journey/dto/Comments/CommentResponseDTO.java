package journey.dto.Comments;

import java.time.LocalDateTime;
import java.util.List;

import journey.dto.AuthorInfoDTO;

public class CommentResponseDTO {

    // 留言基本內容
    private Integer id;
    private String userName;
    private String content;
    private Byte rating;
    private LocalDateTime createdAt;

    // 回覆關係
    private Integer parentCommentId; // 父留言 ID，如果是主留言則為 null

    // 留言狀態
    private Boolean isActive; // 留言是否有效
    private Boolean isVerified; // 是否已驗證（管理員驗證）
    private Integer deleteStatus; // 刪除狀態：1=正常, 0=使用者刪除, 1=系統下架
    private String statusMessage; // 狀態說明：null=正常, "作者刪除留言", "系統下架留言"

    // 使用者資訊
    private AuthorInfoDTO author;

    // 留言圖片
    private List<CommentImageResponseDTO> images;

    // 按讚狀態與總數
    private boolean canEdit;
    private boolean canDelete;
    private boolean canReport;
    private boolean canLike;
    private boolean likedByCurrentUser;
    private int likeCount;

    private List<CommentResponseDTO> replies;

    // 前端操作權限控制。
    private boolean editable; // 登入者是否可以編輯/刪除這則留言
    private boolean replyable; // 是否可以回覆此留言
    private boolean reportable; // 是否可以檢舉此留言

    public CommentResponseDTO() {
    }

    public CommentResponseDTO(Integer id, String userName, String content, Byte rating, LocalDateTime createdAt,
            Integer parentCommentId, Boolean isActive, Boolean isVerified, Integer deleteStatus, String statusMessage, AuthorInfoDTO author,
            List<CommentImageResponseDTO> images,
            boolean likedByCurrentUser, int likeCount,
            List<CommentResponseDTO> replies, boolean editable, boolean replyable, boolean reportable) {
        this.id = id;
        this.userName = userName;
        this.content = content;
        this.rating = rating;
        this.createdAt = createdAt;
        this.parentCommentId = parentCommentId;
        this.isActive = isActive;
        this.isVerified = isVerified;
        this.deleteStatus = deleteStatus;
        this.statusMessage = statusMessage;
        this.author = author;
        this.images = images;
        this.likedByCurrentUser = likedByCurrentUser;
        this.likeCount = likeCount;
        this.replies = replies;
        this.editable = editable;
        this.replyable = replyable;
        this.reportable = reportable;
    }

    @Override
    public String toString() {
        return "CommentResponseDTO [id=" + id + ", userName=" + userName + ", content=" + content + ", rating=" + rating
                + ", createdAt=" + createdAt + ", parentCommentId=" + parentCommentId + ", isActive=" + isActive
                + ", isVerified=" + isVerified + ", deleteStatus=" + deleteStatus + ", statusMessage=" + statusMessage + ", author=" + author
                + ", images=" + images + ", likedByCurrentUser="
                + likedByCurrentUser + ", likeCount=" + likeCount + ", replies=" + replies + ", editable=" + editable
                + ", replyable=" + replyable + ", reportable=" + reportable + "]";
    }

    // getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Byte getRating() {
        return rating;
    }

    public void setRating(Byte rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Integer parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public AuthorInfoDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorInfoDTO author) {
        this.author = author;
    }

    public List<CommentImageResponseDTO> getImages() {
        return images;
    }

    public void setImages(List<CommentImageResponseDTO> images) {
        this.images = images;
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

    public List<CommentResponseDTO> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentResponseDTO> replies) {
        this.replies = replies;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isReplyable() {
        return replyable;
    }

    public void setReplyable(boolean replyable) {
        this.replyable = replyable;
    }

    public boolean isReportable() {
        return reportable;
    }

    public void setReportable(boolean reportable) {
        this.reportable = reportable;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public boolean isCanReport() {
        return canReport;
    }

    public void setCanReport(boolean canReport) {
        this.canReport = canReport;
    }

    public boolean isCanLike() {
        return canLike;
    }

    public void setCanLike(boolean canLike) {
        this.canLike = canLike;
    }
}
