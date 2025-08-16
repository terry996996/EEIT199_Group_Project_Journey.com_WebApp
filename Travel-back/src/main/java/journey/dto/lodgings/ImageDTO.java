package journey.dto.lodgings;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

import journey.enums.ImageTypeEnum;

/**
 * 圖片資料傳輸物件（DTO）
 * 用於統一旅宿與房型圖片的資料傳輸。
 * 
 * @author FAN
 * @since 250704
 */
public class ImageDTO {

    private Integer imageId; // 更新時使用，新增時為 null

    @NotBlank(message = "圖片類型不能為空")
    @Pattern(regexp = "^(lodging|room)$", message = "圖片類型必須是: lodging, room")
    private String imageType;

    private Integer lodgingId; // 所屬住宿ID
    private Integer roomTypeId; // 所屬房型ID（可選）

    @NotBlank(message = "圖片URL不能為空")
    @Size(max = 500, message = "圖片URL不能超過500個字符")
    private String imageUrl;

    @NotBlank(message = "MIME類型不能為空")
    @Size(max = 100, message = "MIME類型不能超過100個字符")
    private String mimeType;

    @NotNull(message = "排序順序不能為空")
    private Integer sortOrder;

    private Boolean isActive;

    private LocalDateTime uploadedAt; // 響應時使用

    // 無參構造函數
    public ImageDTO() {
    }

    // 請求用構造函數
    public ImageDTO(Integer imageId, String imageType, Integer lodgingId, Integer roomTypeId,
            String imageUrl, String mimeType, Integer sortOrder, Boolean isActive) {
        this.imageId = imageId;
        this.imageType = imageType;
        this.lodgingId = lodgingId;
        this.roomTypeId = roomTypeId;
        this.imageUrl = imageUrl;
        this.mimeType = mimeType;
        this.sortOrder = sortOrder;
        this.isActive = isActive;
    }

    // 響應用構造函數
    public ImageDTO(Integer imageId, String imageType, Integer lodgingId, Integer roomTypeId,
            String imageUrl, String mimeType, Integer sortOrder, Boolean isActive, LocalDateTime uploadedAt) {
        this.imageId = imageId;
        this.imageType = imageType;
        this.lodgingId = lodgingId;
        this.roomTypeId = roomTypeId;
        this.imageUrl = imageUrl;
        this.mimeType = mimeType;
        this.sortOrder = sortOrder;
        this.isActive = isActive;
        this.uploadedAt = uploadedAt;
    }

    // Getters and Setters
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

    /**
     * 驗證圖片類型是否有效
     */
    public boolean isValidImageType() {
        return ImageTypeEnum.isValid(this.imageType);
    }

    /**
     * 獲取圖片類型枚舉
     */
    public ImageTypeEnum getImageTypeEnum() {
        return ImageTypeEnum.fromValue(this.imageType);
    }

    public Integer getLodgingId() {
        return lodgingId;
    }

    public void setLodgingId(Integer lodgingId) {
        this.lodgingId = lodgingId;
    }

    public Integer getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Integer roomTypeId) {
        this.roomTypeId = roomTypeId;
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

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    @Override
    public String toString() {
        return "ImageDTO{" +
                "imageId=" + imageId +
                ", imageType='" + imageType + '\'' +
                ", lodgingId=" + lodgingId +
                ", roomTypeId=" + roomTypeId +
                ", imageUrl='" + imageUrl + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", sortOrder=" + sortOrder +
                ", isActive=" + isActive +
                ", uploadedAt=" + uploadedAt +
                '}';
    }
}