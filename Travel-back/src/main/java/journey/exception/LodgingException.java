package journey.exception;

/**
 * 住宿相關異常類別
 * 
 * 用於處理住宿管理中的各種業務異常
 */
public class LodgingException extends RuntimeException {
    
    private final String errorCode;
    
    public LodgingException(String message) {
        super(message);
        this.errorCode = "LODGING_ERROR";
    }
    
    public LodgingException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public LodgingException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "LODGING_ERROR";
    }
    
    public LodgingException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * 住宿不存在異常
     */
    public static class LodgingNotFoundException extends LodgingException {
        public LodgingNotFoundException(Integer lodgingId) {
            super("LODGING_NOT_FOUND", "找不到 ID 為 " + lodgingId + " 的住宿");
        }
    }
    
    /**
     * 住宿驗證失敗異常
     */
    public static class LodgingValidationException extends LodgingException {
        public LodgingValidationException(String message) {
            super("LODGING_VALIDATION_ERROR", message);
        }
    }
    
    /**
     * 住宿權限不足異常
     */
    public static class LodgingAccessDeniedException extends LodgingException {
        public LodgingAccessDeniedException(String message) {
            super("LODGING_ACCESS_DENIED", message);
        }
    }
    
    /**
     * 圖片管理異常
     */
    public static class ImageManagementException extends LodgingException {
        public ImageManagementException(String message) {
            super("IMAGE_MANAGEMENT_ERROR", message);
        }
    }
} 