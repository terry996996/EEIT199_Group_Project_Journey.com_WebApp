package journey.dto.lodgings;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 廠商旅宿詳細資料傳輸物件（DTO）
 * 繼承自 LodgingResponseDTO，專用於回傳廠商可管理的旅宿詳細資訊。
 * 額外包含 updatedBy 欄位以追蹤修改者。
 * 
 * @author FAN
 * @since 250704
 */
public class VendorLodgingDetailDTO extends LodgingResponseDTO {

    private Integer updatedBy;

    // 無參構造函數
    public VendorLodgingDetailDTO() {
        super();
    }

    // 完整構造函數
    public VendorLodgingDetailDTO(Integer id, String lodgingName, String description,
            String address, BigDecimal latitude, BigDecimal longitude,
            String lodgingTel, String email, Boolean isActive,
            LocalDateTime createdAt, LocalDateTime updatedAt, Integer updatedBy,
            Integer lodgingTypeId, String lodgingTypeName,
            Integer cityId, String cityName,
            Integer countryId, String countryName,
            List<ImageDTO> images) {
        super(id, lodgingName, description, address, latitude, longitude,
                lodgingTel, email, isActive, createdAt, updatedAt,
                lodgingTypeId, lodgingTypeName, cityId, cityName,
                countryId, countryName, images);
        this.updatedBy = updatedBy;
    }

    // 基於父類的構造函數
    public VendorLodgingDetailDTO(LodgingResponseDTO parent, Integer updatedBy) {
        super(parent.getId(), parent.getLodgingName(), parent.getDescription(),
                parent.getAddress(), parent.getLatitude(), parent.getLongitude(),
                parent.getLodgingTel(), parent.getEmail(), parent.getIsActive(),
                parent.getCreatedAt(), parent.getUpdatedAt(),
                parent.getLodgingTypeId(), parent.getLodgingTypeName(),
                parent.getCityId(), parent.getCityName(),
                parent.getCountryId(), parent.getCountryName(),
                parent.getImages());
        this.updatedBy = updatedBy;
    }

    // Getter and Setter for updatedBy
    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }
}