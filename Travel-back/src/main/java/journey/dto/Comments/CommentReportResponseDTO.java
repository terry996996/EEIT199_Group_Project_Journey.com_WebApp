package journey.dto.Comments;

import java.time.LocalDateTime;

import journey.dto.AuthorInfoDTO;

/**
 * 檢舉回應 DTO
 * 用於管理員查詢檢舉列表時回傳詳細資訊
 */
public class CommentReportResponseDTO {
    
    // 檢舉基本資訊
    private Integer reportId;
    private String status;
    private String statusLabel;
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;
    private String note;
    
    // 檢舉原因
    private Integer reasonId;
    private String reasonText;
    
    // 檢舉者資訊
    private AuthorInfoDTO reporter;
    
    // 被檢舉的留言資訊
    private CommentResponseDTO reportedComment;
    
    // 處理者資訊（管理員）
    private AuthorInfoDTO reviewer;

    public CommentReportResponseDTO() {
    }

    public CommentReportResponseDTO(Integer reportId, String status, String statusLabel, 
            LocalDateTime createdAt, LocalDateTime reviewedAt, String note,
            Integer reasonId, String reasonText,
            AuthorInfoDTO reporter, CommentResponseDTO reportedComment, AuthorInfoDTO reviewer) {
        this.reportId = reportId;
        this.status = status;
        this.statusLabel = statusLabel;
        this.createdAt = createdAt;
        this.reviewedAt = reviewedAt;
        this.note = note;
        this.reasonId = reasonId;
        this.reasonText = reasonText;
        this.reporter = reporter;
        this.reportedComment = reportedComment;
        this.reviewer = reviewer;
    }

    // Getters and Setters
    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getReasonId() {
        return reasonId;
    }

    public void setReasonId(Integer reasonId) {
        this.reasonId = reasonId;
    }

    public String getReasonText() {
        return reasonText;
    }

    public void setReasonText(String reasonText) {
        this.reasonText = reasonText;
    }

    public AuthorInfoDTO getReporter() {
        return reporter;
    }

    public void setReporter(AuthorInfoDTO reporter) {
        this.reporter = reporter;
    }

    public CommentResponseDTO getReportedComment() {
        return reportedComment;
    }

    public void setReportedComment(CommentResponseDTO reportedComment) {
        this.reportedComment = reportedComment;
    }

    public AuthorInfoDTO getReviewer() {
        return reviewer;
    }

    public void setReviewer(AuthorInfoDTO reviewer) {
        this.reviewer = reviewer;
    }

    @Override
    public String toString() {
        return "CommentReportResponseDTO{" +
                "reportId=" + reportId +
                ", status='" + status + '\'' +
                ", statusLabel='" + statusLabel + '\'' +
                ", createdAt=" + createdAt +
                ", reviewedAt=" + reviewedAt +
                ", note='" + note + '\'' +
                ", reasonId=" + reasonId +
                ", reasonText='" + reasonText + '\'' +
                ", reporter=" + reporter +
                ", reportedComment=" + reportedComment +
                ", reviewer=" + reviewer +
                '}';
    }
} 