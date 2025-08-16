package journey.dto.attractions;

import java.time.LocalDateTime;

public class TicketTypeDto {

    private Integer id;               // 票種 ID（修改時用）
    private Integer packageId;        // 所屬套票 ID（外鍵）

    private String ticketName;        // 票種名稱
    private Integer price;            // 價格
    private Integer quantity;         // 數量（庫存）
    private LocalDateTime date;       // 使用日期（或起始日期）

    // ---------- Getter / Setter ----------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
