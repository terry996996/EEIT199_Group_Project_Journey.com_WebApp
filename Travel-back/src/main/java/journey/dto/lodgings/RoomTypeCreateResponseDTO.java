package journey.dto.lodgings;

import java.time.LocalDateTime;
import java.util.List;

import journey.dto.lodgings.ImageDTO;

/**
 * 房型建立回應資料傳輸物件（DTO）
 * 用於回傳新建立房型的詳細資訊。
 * 
 * @author FAN
 * @since 250704
 */
public class RoomTypeCreateResponseDTO {
    private Integer id;

    private String createdAt;

    private String updatedAt;

    private String roomTypeName;

    private String description;

    private Integer pricePerNight;

    private Integer maxGuests;

    private Boolean isActive;

    private Integer lodgingId;

    private String lodgingName;

    private Integer createdBy;

    private String createdByName;

    private Integer updatedBy;

    private String updatedByName;

    private List<BedTypeDTO> beds;

    private List<Integer> facilityIds;

    private List<ImageDTO> images;

    public RoomTypeCreateResponseDTO() {

    }

    public RoomTypeCreateResponseDTO(Integer id, String createdAt, String updatedAt, String roomTypeName,
            String description, Integer pricePerNight, Integer maxGuests, Boolean isActive, Integer lodgingId,
            String lodgingName, Integer createdBy, String createdByName, Integer updatedBy, String updatedByName,
            List<BedTypeDTO> beds, List<Integer> facilityIds, List<ImageDTO> images) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.roomTypeName = roomTypeName;
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.maxGuests = maxGuests;
        this.isActive = isActive;
        this.lodgingId = lodgingId;
        this.lodgingName = lodgingName;
        this.createdBy = createdBy;
        this.createdByName = createdByName;
        this.updatedBy = updatedBy;
        this.updatedByName = updatedByName;
        this.beds = beds;
        this.facilityIds = facilityIds;
        this.images = images;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
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

    public List<Integer> getFacilityIds() {
        return facilityIds;
    }

    public void setFacilityIds(List<Integer> facilityIds) {
        this.facilityIds = facilityIds;
    }

    public List<ImageDTO> getImages() {
        return images;
    }

    public void setImages(List<ImageDTO> images) {
        this.images = images;
    }

}
