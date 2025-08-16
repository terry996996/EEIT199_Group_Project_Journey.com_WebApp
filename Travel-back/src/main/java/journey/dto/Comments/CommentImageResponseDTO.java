package journey.dto.Comments;

public class CommentImageResponseDTO {
    private Integer imageId; // 圖片主鍵，用於前端拖曳排序
    private String commentImageUrl; // 修正欄位名稱
    private int sortOrder;
    private Boolean isActive; // 新增：是否啟用
    private Boolean isDeleted; // 新增：是否已刪除
    private String mimeType; // 新增：MIME類型

    public CommentImageResponseDTO() {
    }

    public CommentImageResponseDTO(Integer imageId, String commentImageUrl, int sortOrder) {
        this.imageId = imageId;
        this.commentImageUrl = commentImageUrl;
        this.sortOrder = sortOrder;
    }

    public CommentImageResponseDTO(Integer imageId, String commentImageUrl, int sortOrder, 
                                  Boolean isActive, Boolean isDeleted, String mimeType) {
        this.imageId = imageId;
        this.commentImageUrl = commentImageUrl;
        this.sortOrder = sortOrder;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.mimeType = mimeType;
    }

    @Override
    public String toString() {
        return "CommentImageResponseDTO [imageId=" + imageId + ", commentImageUrl=" + commentImageUrl + 
               ", sortOrder=" + sortOrder + ", isActive=" + isActive + ", isDeleted=" + isDeleted + 
               ", mimeType=" + mimeType + "]";
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getCommentImageUrl() {
        return commentImageUrl;
    }

    public void setCommentImageUrl(String commentImageUrl) {
        this.commentImageUrl = commentImageUrl;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
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

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
