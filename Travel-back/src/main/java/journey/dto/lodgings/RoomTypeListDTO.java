package journey.dto.lodgings;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 房型列表資料傳輸物件（DTO）
 * 用於回傳飯店全部房型的簡歷資訊。
 * 
 * @author FAN
 * @since 250704
 */
public class RoomTypeListDTO {
    private Integer id;
    private String name;
    private String description;
    private Integer pricePerNight;
    private Integer maxGuests;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer lodgingId;
    private String lodgingName;
    private List<SimpleImageDTO> images;

    public RoomTypeListDTO() {
    }

    public RoomTypeListDTO(Integer id, String name, String description, Integer pricePerNight,
            Integer maxGuests, Boolean isActive, LocalDateTime createdAt,
            LocalDateTime updatedAt, Integer lodgingId, String lodgingName,
            List<SimpleImageDTO> images) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.maxGuests = maxGuests;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lodgingId = lodgingId;
        this.lodgingName = lodgingName;
        this.images = images;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Integer pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public Integer getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(Integer maxGuests) {
        this.maxGuests = maxGuests;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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

    public Integer getLodgingId() {
        return lodgingId;
    }

    public void setLodgingId(Integer lodgingId) {
        this.lodgingId = lodgingId;
    }

    public String getLodgingName() {
        return lodgingName;
    }

    public void setLodgingName(String lodgingName) {
        this.lodgingName = lodgingName;
    }

    public List<SimpleImageDTO> getImages() {
        return images;
    }

    public void setImages(List<SimpleImageDTO> images) {
        this.images = images;
    }
}