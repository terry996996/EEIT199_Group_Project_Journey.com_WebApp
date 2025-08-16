package journey.dto.lodgings;

/**
 * 熱門旅宿搜尋回應資料傳輸物件（DTO）
 * 用於回傳熱門旅宿的基本資訊。
 * 
 * @author FAN
 * @since 250704
 */
public record LodgingHotSearchResponseDTO(
                Integer lodgingId,
                String lodgingName,
                String cityName,
                Double avgRating,
                Long reviewCount,
                String imageUrl // ★ 封面圖片
) {
}
