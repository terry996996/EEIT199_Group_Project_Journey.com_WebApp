package journey.dto.attractions;

import java.time.LocalDateTime;

public class OrderItemResponseDto {

    private Integer itemId;
    private Integer quantity;
    private Integer unitPrice;
    private LocalDateTime useDate;

    private Integer attractionId;
    private String attractionName;

    private Integer ticketPackageId;
    private String ticketPackageName;
    private String imageUrl;
    private Integer optionId;
    private String optionName;
    public Integer getItemId() {
        return itemId;
    }
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public Integer getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }
    public LocalDateTime getUseDate() {
        return useDate;
    }
    public void setUseDate(LocalDateTime useDate) {
        this.useDate = useDate;
    }
    public Integer getAttractionId() {
        return attractionId;
    }
    public void setAttractionId(Integer attractionId) {
        this.attractionId = attractionId;
    }
    public String getAttractionName() {
        return attractionName;
    }
    public void setAttractionName(String attractionName) {
        this.attractionName = attractionName;
    }
    public Integer getTicketPackageId() {
        return ticketPackageId;
    }
    public void setTicketPackageId(Integer ticketPackageId) {
        this.ticketPackageId = ticketPackageId;
    }
    public String getTicketPackageName() {
        return ticketPackageName;
    }
    public void setTicketPackageName(String ticketPackageName) {
        this.ticketPackageName = ticketPackageName;
    }
    public Integer getOptionId() {
        return optionId;
    }
    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }
    public String getOptionName() {
        return optionName;
    }
    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    }
    
