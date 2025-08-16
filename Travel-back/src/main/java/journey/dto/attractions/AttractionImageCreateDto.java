package journey.dto.attractions;

public class AttractionImageCreateDto {

    private String imageUrl; // 圖片網址
    private String imageType; // 圖片格式，例如 jpg、png
    private String altText; // 圖片替代文字
    private Integer sortOrder; // 顯示順序
    private Integer attractionId; // 對應景點 ID（外鍵）

    // Getter / Setter
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(Integer attractionId) {
        this.attractionId = attractionId;
    }
}