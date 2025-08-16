package journey.dto.attractions;

import java.time.LocalDateTime;
import java.util.List;

public class TicketPackageDto {

    private Integer id;               // 主鍵 ID（修改用）
    private Integer ticketId;         // 所屬票券 ID

    private String name;              // 套票名稱
    private String description;       // 套票說明

    private String imageUrl;          // 圖片 URL
    private String imageType;         // 圖片類型（例如 cover/detail）

    private LocalDateTime startTime;  // 起始時間
    private LocalDateTime endTime;    // 結束時間

    private Integer createdBy;        // 建立者 ID
    private Integer updatedBy;        // 修改者 ID

    private LocalDateTime createdAt;  // 建立時間
    private LocalDateTime updatedAt;  // 修改時間

    private Boolean state;            // 上架狀態

    // Getter / Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
    private List<TicketTypeDto> types;

    public List<TicketTypeDto> getTypes() {
    return types;
}

    public void setTypes(List<TicketTypeDto> types) {
    this.types = types;
    }
}
