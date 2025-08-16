package journey.dto.lodgings;

/**
 * 床型資料傳輸物件（DTO）
 * 用於描述房型內的床型資訊。
 * 
 * @author FAN
 * @since 250704
 */
public record BedTypeDTO(
        Integer id,
        String name,
        Integer quantity) {
}