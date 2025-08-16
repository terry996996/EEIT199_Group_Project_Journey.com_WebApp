package journey.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

/**
 * 全域異常處理器
 * 
 * 統一處理所有 API 異常，提供一致的錯誤響應格式
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 處理住宿相關異常
     */
    @ExceptionHandler(LodgingException.class)
    public ResponseEntity<Map<String, Object>> handleLodgingException(LodgingException e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", e.getErrorCode());
        errorResponse.put("message", e.getMessage());
        errorResponse.put("timestamp", System.currentTimeMillis());
        
        HttpStatus status;
        switch (e.getErrorCode()) {
            case "LODGING_NOT_FOUND":
                status = HttpStatus.NOT_FOUND;
                break;
            case "LODGING_ACCESS_DENIED":
                status = HttpStatus.FORBIDDEN;
                break;
            case "LODGING_VALIDATION_ERROR":
            case "IMAGE_MANAGEMENT_ERROR":
                status = HttpStatus.BAD_REQUEST;
                break;
            default:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        
        return ResponseEntity.status(status).body(errorResponse);
    }

    /**
     * 處理參數驗證異常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "VALIDATION_ERROR");
        errorResponse.put("message", "參數驗證失敗");
        errorResponse.put("timestamp", System.currentTimeMillis());
        
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        errorResponse.put("fieldErrors", fieldErrors);
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 處理約束違反異常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "CONSTRAINT_VIOLATION");
        errorResponse.put("message", "約束違反");
        errorResponse.put("timestamp", System.currentTimeMillis());
        
        Map<String, String> violations = new HashMap<>();
        e.getConstraintViolations().forEach(violation -> {
            violations.put(violation.getPropertyPath().toString(), violation.getMessage());
        });
        errorResponse.put("violations", violations);
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 處理非法參數異常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "INVALID_ARGUMENT");
        errorResponse.put("message", e.getMessage());
        errorResponse.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 處理安全異常
     */
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, Object>> handleSecurityException(SecurityException e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "ACCESS_DENIED");
        errorResponse.put("message", e.getMessage());
        errorResponse.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    /**
     * 處理一般異常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "INTERNAL_SERVER_ERROR");
        errorResponse.put("message", "內部伺服器錯誤");
        errorResponse.put("timestamp", System.currentTimeMillis());
        
        // 在開發環境中可以包含詳細錯誤訊息
        if (isDevelopmentEnvironment()) {
            errorResponse.put("details", e.getMessage());
            errorResponse.put("stackTrace", e.getStackTrace());
        }
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * 判斷是否為開發環境
     */
    private boolean isDevelopmentEnvironment() {
        String profile = System.getProperty("spring.profiles.active");
        return "dev".equals(profile) || "development".equals(profile);
    }
} 