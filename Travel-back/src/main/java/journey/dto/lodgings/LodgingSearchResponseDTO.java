package journey.dto.lodgings;

import java.math.BigDecimal;
import java.util.List;

/**
 * 旅宿搜尋回應資料傳輸物件（DTO）
 * 用於回傳旅宿搜尋的單筆結果。
 * 
 * @author FAN
 * @since 250704
 */
public record LodgingSearchResponseDTO(
                Integer lodgingId,
                String lodgingName,
                Double avgRating,
                Long reviewCount,
                Integer roomTypeId,
                String roomTypeName,
                String roomTypeDescription,
                Integer maxGuests,
                BigDecimal pricePerNight,
                String imageUrl,
                Integer lodgingTypeId,
                String lodgingType,
                List<BedTypeDTO> bedTypes,
                List<FacilityDTO> facilities) {
}
