package journey.dto.Comments;

import java.time.LocalDateTime;
import java.util.Set;

import journey.domain.comments.CommentReasonsBean;
import journey.domain.comments.CommentReportsBean;

public class CommentReasonsDTO {

    private Integer id;
    private String reasonText;
    private Boolean isActive = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<CommentReportsBean> commentReports;

    public CommentReasonsDTO() {

    }

    public CommentReasonsDTO(Integer id, String reasonText) {
        this.id = id;
        this.reasonText = reasonText;
    }

    public CommentReasonsDTO(Integer id, String reasonText, Boolean isActive, LocalDateTime createdAt,
            LocalDateTime updatedAt, Set<CommentReportsBean> commentReports) {
        this.id = id;
        this.reasonText = reasonText;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.commentReports = commentReports;
    }

    @Override
    public String toString() {
        return "CommentReasonsDTO [id=" + id + ", reasonText=" + reasonText + ", isActive=" + isActive + ", createdAt="
                + createdAt + ", updatedAt=" + updatedAt + ", commentReports=" + commentReports + "]";
    }

    // getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReasonText() {
        return reasonText;
    }

    public void setReasonText(String reasonText) {
        this.reasonText = reasonText;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<CommentReportsBean> getCommentReports() {
        return commentReports;
    }

    public void setCommentReports(Set<CommentReportsBean> commentReports) {
        this.commentReports = commentReports;
    }

    public static CommentReasonsDTO fromEntity(CommentReasonsBean bean) {
        if (bean == null)
            return null;
        return new CommentReasonsDTO(
                bean.getId(),
                bean.getReasonText());
    }

}
