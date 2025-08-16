package journey.domain.lodgings;

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
 * 住宿圖片實體類別
 * 
 * 功能說明：
 * - 儲存住宿相關的圖片資訊
 * - 支援多種圖片類型（外觀、內部、設施等）
 * - 支援圖片排序和啟用/停用
 * - 支援軟刪除機制
 * - 自動記錄上傳時間
 * 
 * 圖片類型：
 * - EXTERIOR: 外觀圖片
 * - INTERIOR: 內部圖片
 * - FACILITY: 設施圖片
 * - ROOM: 房間圖片
 * - OTHER: 其他圖片
 * 
 * @author Journey Team
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "lodging_images")
@Where(clause = "is_active = 1 AND delete_status = 1")
public class LodgingImagesBean {
    /** 圖片ID - 主鍵 */
    @Id
    @Column(name = "image_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer imageId;

    /** 圖片類型 - 必填，最大50字（EXTERIOR, INTERIOR, FACILITY, ROOM, OTHER） */
    @Column(name = "image_type", nullable = false, length = 50)
    private String imageType;

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

    /** 所屬住宿 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lodging_id", referencedColumnName = "id")
    @JsonBackReference
    private LodgingsBean lodging;

    /** 所屬房型（可選，用於房型專屬圖片） */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", referencedColumnName = "id")
    @JsonBackReference
    private RoomTypesBean roomType;

    public LodgingImagesBean() {
        // 給 JPA 和 Jackson 使用
    }

    public LodgingImagesBean(Integer imageId, String imageType, String imageUrl, String mimeType, Integer sortOrder,
            Boolean isActive, LocalDateTime deletedTime, Integer deleteStatus, LocalDateTime uploadedAt,
            LodgingsBean lodging, RoomTypesBean roomType) {
        this.imageId = imageId;
        this.imageType = imageType;
        this.imageUrl = imageUrl;
        this.mimeType = mimeType;
        this.sortOrder = sortOrder;
        this.isActive = isActive;
        this.deletedTime = deletedTime;
        this.deleteStatus = deleteStatus;
        this.uploadedAt = uploadedAt;
        this.lodging = lodging;
        this.roomType = roomType;
    }

    // getters and setters
    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
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

    public LodgingsBean getLodging() {
        return lodging;
    }

    public void setLodging(LodgingsBean lodging) {
        this.lodging = lodging;
    }

    public RoomTypesBean getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypesBean roomType) {
        this.roomType = roomType;
    }

    @PrePersist
    protected void onCreate() {
        if (this.uploadedAt == null)
            this.uploadedAt = LocalDateTime.now();
    }

}