package journey.domain.comments;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import journey.converter.CommentReportStatusEnumConverter;
import journey.domain.users.AdminBean;
import journey.domain.users.AllUserBean;
import journey.enums.CommentReportStatusEnum;

@Entity
@Table(name = "comment_reports", uniqueConstraints = @UniqueConstraint( // ★ 新增
        name = "uq_comment_user", // 與 DDL 一致
        columnNames = { "comment_id", "created_by" } // 只鎖這兩欄
))
public class CommentReportsBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comment_id", nullable = false)
    @JsonBackReference
    private CommentsBean comment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reason_id", nullable = false)
    @JsonBackReference
    private CommentReasonsBean reason;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    @JsonBackReference
    private AllUserBean createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // ========== 新增欄位 ==========
    @Convert(converter = CommentReportStatusEnumConverter.class)
    @Column(name = "status", nullable = false)
    private CommentReportStatusEnum status = CommentReportStatusEnum.PENDING; // 使用 enum 預設值

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    @JsonBackReference
    private AdminBean reviewedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "note")
    private String note;

    public CommentReportsBean() {
    }

    public CommentReportsBean(Integer id, CommentsBean comment, CommentReasonsBean reason, AllUserBean createdBy,
            LocalDateTime createdAt, CommentReportStatusEnum status, AdminBean reviewedBy, LocalDateTime reviewedAt,
            String note) {
        this.id = id;
        this.comment = comment;
        this.reason = reason;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.status = status;
        this.reviewedBy = reviewedBy;
        this.reviewedAt = reviewedAt;
        this.note = note;
    }

    @Override
    public String toString() {
        return "CommentReportsBean [id=" + id + ", comment=" + comment + ", reason=" + reason + ", createdBy="
                + createdBy + ", createdAt=" + createdAt + ", status=" + status + ", reviewedBy=" + reviewedBy
                + ", reviewedAt=" + reviewedAt + ", note=" + note + "]";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CommentsBean getComment() {
        return comment;
    }

    public void setComment(CommentsBean comment) {
        this.comment = comment;
    }

    public CommentReasonsBean getReason() {
        return reason;
    }

    public void setReason(CommentReasonsBean reason) {
        this.reason = reason;
    }

    public AllUserBean getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AllUserBean createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public CommentReportStatusEnum getStatus() {
        return status;
    }

    public void setStatus(CommentReportStatusEnum status) {
        this.status = status;
    }

    public AdminBean getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(AdminBean reviewedBy) {
        this.reviewedBy = reviewedBy;
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

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null)
            this.createdAt = LocalDateTime.now();
        if (this.status == null)
            this.status = CommentReportStatusEnum.PENDING;
    }

}