package journey.dto.Comments;

public class CommentImageRequestDTO {
    private String imageUrl;
    private String mimeType;
    private Integer sortOrder;
    private Boolean isNew;

    public CommentImageRequestDTO() {
    }

    public CommentImageRequestDTO(String imageUrl, String mimeType, Integer sortOrder, Boolean isNew) {
        this.imageUrl = imageUrl;
        this.mimeType = mimeType;
        this.sortOrder = sortOrder;
        this.isNew = isNew;
    }

    @Override
    public String toString() {
        return "CommentImageRequestDTO [imageUrl=" + imageUrl + ", mimeType=" + mimeType + ", sortOrder=" + sortOrder
                + ", isNew=" + isNew + "]";
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

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

}
