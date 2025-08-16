package journey.domain.comments;

import java.time.LocalDateTime;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * 留言圖片實體類別
 * 
 * 功能說明：
 * - 儲存留言相關的圖片資訊
 * - 支援圖片排序和啟用/停用
 * - 支援軟刪除機制
 * - 自動記錄上傳時間
 * 
 * 圖片類型：
 * - 支援常見圖片格式（JPG, PNG, GIF等）
 * - 透過MIME類型進行格式驗證
 * 
 * @author Journey Team
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "comment_images")
@Where(clause = "is_active = 1 AND delete_status = 1")
public class CommentImagesBean {
    /** 圖片ID - 主鍵 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 所屬留言 - 必填 */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comment_id", nullable = false)
    @JsonBackReference
    private CommentsBean comment;

    /** 圖片URL - 必填，最大500字 */
    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    /** MIME類型 - 必填，最大100字（如：image/jpeg, image/png） */
    @Column(name = "mime_type", nullable = false, length = 100)
    private String mimeType;

    /** 排序順序 - 預設0，數字越小越靠前 */
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    /** 是否啟用 - 預設true */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    /** 刪除時間（軟刪除） */
    @Column(name = "deleted_time")
    private LocalDateTime deletedTime;

    /** 刪除狀態：1=正常，0=已刪除 */
    @Column(name = "delete_status", nullable = false)
    private Integer deleteStatus = 1;

    /** 上傳時間 */
    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    public CommentImagesBean() {
    }

    public CommentImagesBean(Integer id, CommentsBean comment, String imageUrl, String mimeType, Integer sortOrder,
            Boolean isActive, LocalDateTime deletedTime, Integer deleteStatus, LocalDateTime uploadedAt) {
        this.id = id;
        this.comment = comment;
        this.imageUrl = imageUrl;
        this.mimeType = mimeType;
        this.sortOrder = sortOrder;
        this.isActive = isActive;
        this.deletedTime = deletedTime;
        this.deleteStatus = deleteStatus;
        this.uploadedAt = uploadedAt;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getDeletedTime() {
        return deletedTime;
    }

    public void setDeletedTime(LocalDateTime deletedTime) {
        this.deletedTime = deletedTime;
    }

    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    @PrePersist
    protected void onCreate() {
        if (this.uploadedAt == null)
            this.uploadedAt = LocalDateTime.now();
    }
}