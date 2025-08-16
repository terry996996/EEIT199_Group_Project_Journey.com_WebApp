package journey.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 通用圖片資料傳輸物件（DTO）
 * 用於統一所有圖片相關的資料傳輸格式，支援 lodgings、comments 等不同類型的圖片。
 * 
 * @author Journey Team
 * @version 1.0
 * @since 2025-07-08
 */
public class BaseImageDTO {

    private Integer imageId; // 更新時使用，新增時為 null

    @NotBlank(message = "圖片類型不能為空")
    @Size(max = 50, message = "圖片類型不能超過50個字符")
    private String imageType; // "lodging", "room", "comment"

    private Integer targetId; // 目標ID（住宿ID、房型ID、留言ID等）

    @NotBlank(message = "目標類型不能為空")
    @Size(max = 50, message = "目標類型不能超過50個字符")
    private String targetType; // "lodging", "room_type", "comment"

    @NotBlank(message = "圖片URL不能為空")
    @Size(max = 500, message = "圖片URL不能超過500個字符")
    private String imageUrl;

    @NotBlank(message = "MIME類型不能為空")
    @Size(max = 100, message = "MIME類型不能超過100個字符")
    private String mimeType;

    @NotNull(message = "排序順序不能為空")
    private Integer sortOrder;

    private Boolean isActive = true;

    private Boolean isDeleted = false; // 新增：是否已刪除

    private LocalDateTime uploadedAt; // 響應時使用

    // 額外欄位（用於向後相容）
    private Integer lodgingId; // 向後相容 lodgings
    private Integer roomTypeId; // 向後相容 lodgings
    private Boolean isNew; // 向後相容 comments

    // 無參構造函數
    public BaseImageDTO() {
    }

    // 基本構造函數
    public BaseImageDTO(Integer imageId, String imageType, Integer targetId, String targetType,
            String imageUrl, String mimeType, Integer sortOrder, Boolean isActive) {
        this.imageId = imageId;
        this.imageType = imageType;
        this.targetId = targetId;
        this.targetType = targetType;
        this.imageUrl = imageUrl;
        this.mimeType = mimeType;
        this.sortOrder = sortOrder;
        this.isActive = isActive;
        this.isDeleted = false;
    }

    // 完整構造函數
    public BaseImageDTO(Integer imageId, String imageType, Integer targetId, String targetType,
            String imageUrl, String mimeType, Integer sortOrder, Boolean isActive, LocalDateTime uploadedAt) {
        this.imageId = imageId;
        this.imageType = imageType;
        this.targetId = targetId;
        this.targetType = targetType;
        this.imageUrl = imageUrl;
        this.mimeType = mimeType;
        this.sortOrder = sortOrder;
        this.isActive = isActive;
        this.isDeleted = false;
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

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    // 向後相容的 getters 和 setters
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

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    /**
     * 驗證圖片類型是否有效
     */
    public boolean isValidImageType() {
        return "lodging".equals(imageType) || "room".equals(imageType) || "comment".equals(imageType);
    }

    /**
     * 驗證目標類型是否有效
     */
    public boolean isValidTargetType() {
        return "lodging".equals(targetType) || "room_type".equals(targetType) || "comment".equals(targetType);
    }

    /**
     * 從 lodgings ImageDTO 轉換
     */
    public static BaseImageDTO fromLodgingImage(journey.dto.lodgings.ImageDTO lodgingImage) {
        BaseImageDTO baseImage = new BaseImageDTO();
        baseImage.setImageId(lodgingImage.getImageId());
        baseImage.setImageType(lodgingImage.getImageType());
        baseImage.setTargetId(lodgingImage.getLodgingId());
        baseImage.setTargetType("lodging");
        baseImage.setImageUrl(lodgingImage.getImageUrl());
        baseImage.setMimeType(lodgingImage.getMimeType());
        baseImage.setSortOrder(lodgingImage.getSortOrder());
        baseImage.setIsActive(lodgingImage.getIsActive());
        baseImage.setIsDeleted(lodgingImage.getIsActive() != null && !lodgingImage.getIsActive());
        baseImage.setUploadedAt(lodgingImage.getUploadedAt());
        
        // 向後相容
        baseImage.setLodgingId(lodgingImage.getLodgingId());
        baseImage.setRoomTypeId(lodgingImage.getRoomTypeId());
        
        return baseImage;
    }

    /**
     * 從 comments CommentImageRequestDTO 轉換
     */
    public static BaseImageDTO fromCommentImage(journey.dto.Comments.CommentImageRequestDTO commentImage, Integer commentId) {
        BaseImageDTO baseImage = new BaseImageDTO();
        baseImage.setImageType("comment");
        baseImage.setTargetId(commentId);
        baseImage.setTargetType("comment");
        baseImage.setImageUrl(commentImage.getImageUrl());
        baseImage.setMimeType(commentImage.getMimeType());
        baseImage.setSortOrder(commentImage.getSortOrder());
        baseImage.setIsActive(true);
        baseImage.setIsDeleted(false);
        
        // 向後相容
        baseImage.setIsNew(commentImage.getIsNew());
        
        return baseImage;
    }

    /**
     * 轉換為 lodgings ImageDTO
     */
    public journey.dto.lodgings.ImageDTO toLodgingImage() {
        journey.dto.lodgings.ImageDTO lodgingImage = new journey.dto.lodgings.ImageDTO();
        lodgingImage.setImageId(this.imageId);
        lodgingImage.setImageType(this.imageType);
        lodgingImage.setLodgingId(this.targetId);
        lodgingImage.setRoomTypeId(this.roomTypeId);
        lodgingImage.setImageUrl(this.imageUrl);
        lodgingImage.setMimeType(this.mimeType);
        lodgingImage.setSortOrder(this.sortOrder);
        lodgingImage.setIsActive(this.isActive);
        lodgingImage.setUploadedAt(this.uploadedAt);
        return lodgingImage;
    }

    /**
     * 轉換為 comments CommentImageRequestDTO
     */
    public journey.dto.Comments.CommentImageRequestDTO toCommentImage() {
        journey.dto.Comments.CommentImageRequestDTO commentImage = new journey.dto.Comments.CommentImageRequestDTO();
        commentImage.setImageUrl(this.imageUrl);
        commentImage.setMimeType(this.mimeType);
        commentImage.setSortOrder(this.sortOrder);
        commentImage.setIsNew(this.isNew);
        return commentImage;
    }

    @Override
    public String toString() {
        return "BaseImageDTO{" +
                "imageId=" + imageId +
                ", imageType='" + imageType + '\'' +
                ", targetId=" + targetId +
                ", targetType='" + targetType + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", sortOrder=" + sortOrder +
                ", isActive=" + isActive +
                ", isDeleted=" + isDeleted +
                ", uploadedAt=" + uploadedAt +
                '}';
    }
} 