package journey.dto.lodgings;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 房型詳細資料傳輸物件（DTO）
 * 用於回傳單筆房型的完整詳細資訊。
 * 
 * @author FAN
 * @since 250704
 */
public class RoomTypeDetailDTO {
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
    private Integer createdBy;
    private String createdByName;
    private Integer updatedBy;
    private String updatedByName;
    private List<BedTypeDTO> beds;
    private List<FacilityDTO> facilities;
    private List<SimpleImageDTO> images;

    public RoomTypeDetailDTO() {
    }

    public RoomTypeDetailDTO(Integer id, String name, String description, Integer pricePerNight,
            Integer maxGuests, Boolean isActive, LocalDateTime createdAt,
            LocalDateTime updatedAt, Integer lodgingId, String lodgingName,
            Integer createdBy, String createdByName, Integer updatedBy, String updatedByName,
            List<BedTypeDTO> beds, List<FacilityDTO> facilities, List<SimpleImageDTO> images) {
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
        this.createdBy = createdBy;
        this.createdByName = createdByName;
        this.updatedBy = updatedBy;
        this.updatedByName = updatedByName;
        this.beds = beds;
        this.facilities = facilities;
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

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }

    public List<BedTypeDTO> getBeds() {
        return beds;
    }

    public void setBeds(List<BedTypeDTO> beds) {
        this.beds = beds;
    }

    public List<FacilityDTO> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<FacilityDTO> facilities) {
        this.facilities = facilities;
    }

    public List<SimpleImageDTO> getImages() {
        return images;
    }

    public void setImages(List<SimpleImageDTO> images) {
        this.images = images;
    }
}