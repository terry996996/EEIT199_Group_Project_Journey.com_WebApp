package journey.dto.attractions;

public class TicketImageDto {

    private Integer id;           // 圖片 ID（編輯或刪除時用）
    private String imageUrl;      // 圖片網址
    private String imageType;     // 圖片類型（如：main、detail）
    private Integer sortOrder;    // 顯示順序
    private Integer ticketId;     // 所屬票券 ID

    // ---------- Getter / Setter ----------
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }
}