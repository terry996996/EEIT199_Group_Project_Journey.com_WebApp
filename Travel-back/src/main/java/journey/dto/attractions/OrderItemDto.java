
package journey.dto.attractions;

import java.time.LocalDateTime;

public class OrderItemDto {

    private Integer attractionId;
    private Integer ticketPackageId;
    private Integer optionId;
    private Integer quantity;
    private String imageUrl;
    public Integer getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(Integer attractionId) {
        this.attractionId = attractionId;
    }

    public Integer getTicketPackageId() {
        return ticketPackageId;
    }

    public void setTicketPackageId(Integer ticketPackageId) {
        this.ticketPackageId = ticketPackageId;
    }

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    private Integer unitPrice; // 價格

public Integer getUnitPrice() {
    return unitPrice;
}

public void setUnitPrice(Integer unitPrice) {
    this.unitPrice = unitPrice;
}
private LocalDateTime useDate;

public LocalDateTime getUseDate() {
    return useDate;
}

public void setUseDate(LocalDateTime useDate) {
    this.useDate = useDate;
}

public String getImageUrl() {
    return imageUrl;
}

public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
}
}
