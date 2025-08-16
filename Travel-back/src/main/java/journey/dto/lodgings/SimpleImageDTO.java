package journey.dto.lodgings;

/**
 * 簡化圖片資料傳輸物件（DTO）
 * 只包含前端需要的圖片資訊
 * 
 * @author Journey Team
 * @since 2024
 */
public class SimpleImageDTO {

    private String imageType;
    private String imageUrl;
    private Integer sortOrder;

    // 建構函數
    public SimpleImageDTO() {
    }

    public SimpleImageDTO(String imageType, String imageUrl, Integer sortOrder) {
        this.imageType = imageType;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
    }

    // Getters and Setters
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

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}