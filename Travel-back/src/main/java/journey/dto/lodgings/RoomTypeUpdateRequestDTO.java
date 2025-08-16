package journey.dto.lodgings;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import journey.dto.lodgings.ImageDTO;

/**
 * 房型更新請求資料傳輸物件（DTO）
 * 用於接收更新房型時的請求資料。
 * 
 * @author FAN
 * @since 250704
 */
public class RoomTypeUpdateRequestDTO {

    @NotBlank(message = "房型名稱不能為空")
    private String roomTypeName;

    @NotBlank(message = "房型描述不能為空")
    private String description;

    @NotNull(message = "每晚價格不能為空")
    private Integer pricePerNight;

    @NotNull(message = "最大入住人數不能為空")
    private Integer maxGuests;

    private Boolean isActive;

    @NotEmpty(message = "床型不能為空")
    private List<BedTypeDTO> beds;

    @NotEmpty(message = "設施不能為空")
    private List<Integer> facilityIds;

    @Size(max = 5, message = "圖片數量不能超過5張")
    private List<ImageDTO> images;

    public RoomTypeUpdateRequestDTO() {
    }

    public RoomTypeUpdateRequestDTO(
            @NotBlank(message = "房型名稱不能為空") String roomTypeName,
            @NotBlank(message = "房型描述不能為空") String description,
            @NotNull(message = "每晚價格不能為空") Integer pricePerNight,
            @NotNull(message = "最大入住人數不能為空") Integer maxGuests,
            Boolean isActive,
            @NotEmpty(message = "床型不能為空") List<BedTypeDTO> beds,
            @NotEmpty(message = "設施不能為空") List<Integer> facilityIds,
            @Size(max = 5, message = "圖片數量不能超過5張") List<ImageDTO> images) {
        this.roomTypeName = roomTypeName;
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.maxGuests = maxGuests;
        this.isActive = isActive;
        this.beds = beds;
        this.facilityIds = facilityIds;
        this.images = images;
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