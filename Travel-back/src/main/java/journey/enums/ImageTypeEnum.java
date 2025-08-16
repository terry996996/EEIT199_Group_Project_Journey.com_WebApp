package journey.enums;

/**
 * 圖片類型枚舉
 * 定義住宿和房型圖片的標準類型
 * 
 * @author Journey Team
 * @version 1.0
 * @since 2024
 */
public enum ImageTypeEnum {
    
    /** 住宿圖片 */
    LODGING("lodging", "住宿圖片"),
    
    /** 房間圖片 */
    ROOM("room", "房間圖片");
    
    private final String value;
    private final String description;
    
    ImageTypeEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 根據值獲取枚舉
     */
    public static ImageTypeEnum fromValue(String value) {
        for (ImageTypeEnum type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的圖片類型: " + value);
    }
    
    /**
     * 檢查是否為有效的圖片類型
     */
    public static boolean isValid(String value) {
        try {
            fromValue(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return value;
    }
} 