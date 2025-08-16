# 後端 SSE 實作指南

## 📋 概述
本文件說明如何在 Spring Boot 後端實作 SSE (Server-Sent Events) 功能，支援即時通知和狀態更新。

## 🛠️ 技術架構
- **框架**：Spring Boot 3.x
- **SSE**：Spring WebFlux SseEmitter
- **認證**：JWT Token 驗證
- **資料庫**：SQL Server
- **連接管理**：ConcurrentHashMap

## 📁 檔案結構
```
src/main/java/journey/
├── controller/
│   └── SSEController.java              # SSE 端點控制器
├── service/
│   ├── SSENotificationService.java     # SSE 通知服務
│   └── SSEConnectionManager.java       # 連接管理器
├── config/
│   └── SSESecurityConfig.java          # SSE 安全配置
└── dto/
    └── SSENotificationDTO.java         # SSE 通知 DTO
```

## 🔧 實作步驟

### 1. SSE 通知 DTO

```java
// src/main/java/journey/dto/SSENotificationDTO.java
package journey.dto;

import java.time.LocalDateTime;

public class SSENotificationDTO {
    private String type;
    private Object data;
    private LocalDateTime timestamp;
    private String message;

    public SSENotificationDTO(String type, Object data, String message) {
        this.type = type;
        this.data = data;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
```

**注意**：您的專案已經有完整的 JWT Token 實作（`JwtUtil.java`、`JwtDTO.java`、`JwtAuthenticationFilter.java`），所以 SSE 控制器可以直接使用現有的 Token 驗證機制。

### 2. SSE 連接管理器

```java
// src/main/java/journey/service/SSEConnectionManager.java
package journey.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import journey.dto.SSENotificationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SSEConnectionManager {
    
    private final Map<Integer, SseEmitter> userConnections = new ConcurrentHashMap<>();
    private final Map<String, List<SseEmitter>> adminConnections = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 建立用戶 SSE 連接
     */
    public SseEmitter createUserConnection(Integer userId) {
        SseEmitter emitter = new SseEmitter(120000L); // 120秒超時
        
        // 設定超時處理
        emitter.onTimeout(() -> {
            System.out.println("用戶 " + userId + " SSE 連接超時");
            removeUserConnection(userId);
        });
        
        // 設定完成處理
        emitter.onCompletion(() -> {
            System.out.println("用戶 " + userId + " SSE 連接完成");
            removeUserConnection(userId);
        });
        
        // 設定錯誤處理
        emitter.onError((ex) -> {
            System.err.println("用戶 " + userId + " SSE 連接錯誤: " + ex.getMessage());
            removeUserConnection(userId);
        });
        
        // 儲存連接
        userConnections.put(userId, emitter);
        
        // 發送連接建立通知
        sendToUser(userId, new SSENotificationDTO("connect", Map.of(
            "userId", userId,
            "message", "SSE 連接已建立"
        ), "SSE 連接已建立"));
        
        System.out.println("✅ 用戶 " + userId + " SSE 連接已建立");
        return emitter;
    }
    
    /**
     * 建立管理員 SSE 連接
     */
    public SseEmitter createAdminConnection(String adminId) {
        SseEmitter emitter = new SseEmitter(120000L); // 120秒超時
        
        // 設定超時處理
        emitter.onTimeout(() -> {
            System.out.println("管理員 " + adminId + " SSE 連接超時");
            removeAdminConnection(adminId);
        });
        
        // 設定完成處理
        emitter.onCompletion(() -> {
            System.out.println("管理員 " + adminId + " SSE 連接完成");
            removeAdminConnection(adminId);
        });
        
        // 設定錯誤處理
        emitter.onError((ex) -> {
            System.err.println("管理員 " + adminId + " SSE 連接錯誤: " + ex.getMessage());
            removeAdminConnection(adminId);
        });
        
        // 儲存連接
        adminConnections.computeIfAbsent(adminId, k -> new CopyOnWriteArrayList<>()).add(emitter);
        
        // 發送連接建立通知
        sendToAdmin(adminId, new SSENotificationDTO("connect", Map.of(
            "adminId", adminId,
            "message", "管理員 SSE 連接已建立"
        ), "管理員 SSE 連接已建立"));
        
        System.out.println("✅ 管理員 " + adminId + " SSE 連接已建立");
        return emitter;
    }
    
    /**
     * 發送通知給特定用戶
     */
    public void sendToUser(Integer userId, SSENotificationDTO notification) {
        SseEmitter emitter = userConnections.get(userId);
        if (emitter != null) {
            try {
                String jsonData = objectMapper.writeValueAsString(notification);
                emitter.send(SseEmitter.event()
                    .name(notification.getType())
                    .data(jsonData));
                System.out.println("📨 已發送通知給用戶 " + userId + ": " + notification.getType());
            } catch (IOException e) {
                System.err.println("❌ 發送通知給用戶 " + userId + " 失敗: " + e.getMessage());
                removeUserConnection(userId);
            }
        } else {
            System.out.println("⚠️ 用戶 " + userId + " 沒有活躍的 SSE 連接");
        }
    }
    
    /**
     * 發送通知給所有管理員
     */
    public void sendToAllAdmins(SSENotificationDTO notification) {
        adminConnections.forEach((adminId, emitters) -> {
            emitters.removeIf(emitter -> {
                try {
                    String jsonData = objectMapper.writeValueAsString(notification);
                    emitter.send(SseEmitter.event()
                        .name(notification.getType())
                        .data(jsonData));
                    return false; // 保留連接
                } catch (IOException e) {
                    System.err.println("❌ 發送通知給管理員 " + adminId + " 失敗: " + e.getMessage());
                    return true; // 移除連接
                }
            });
        });
        System.out.println("📨 已發送通知給所有管理員: " + notification.getType());
    }
    
    /**
     * 發送通知給特定管理員
     */
    public void sendToAdmin(String adminId, SSENotificationDTO notification) {
        List<SseEmitter> emitters = adminConnections.get(adminId);
        if (emitters != null) {
            emitters.removeIf(emitter -> {
                try {
                    String jsonData = objectMapper.writeValueAsString(notification);
                    emitter.send(SseEmitter.event()
                        .name(notification.getType())
                        .data(jsonData));
                    return false; // 保留連接
                } catch (IOException e) {
                    System.err.println("❌ 發送通知給管理員 " + adminId + " 失敗: " + e.getMessage());
                    return true; // 移除連接
                }
            });
            System.out.println("📨 已發送通知給管理員 " + adminId + ": " + notification.getType());
        }
    }
    
    /**
     * 移除用戶連接
     */
    private void removeUserConnection(Integer userId) {
        SseEmitter emitter = userConnections.remove(userId);
        if (emitter != null) {
            emitter.complete();
        }
    }
    
    /**
     * 移除管理員連接
     */
    private void removeAdminConnection(String adminId) {
        List<SseEmitter> emitters = adminConnections.remove(adminId);
        if (emitters != null) {
            emitters.forEach(SseEmitter::complete);
        }
    }
    
    /**
     * 獲取連接統計
     */
    public Map<String, Object> getConnectionStats() {
        Map<String, Object> stats = new ConcurrentHashMap<>();
        stats.put("userConnections", userConnections.size());
        stats.put("adminConnections", adminConnections.size());
        stats.put("totalConnections", userConnections.size() + adminConnections.size());
        return stats;
    }
    
    /**
     * 發送心跳
     */
    public void sendHeartbeat() {
        SSENotificationDTO heartbeat = new SSENotificationDTO("heartbeat", 
            Map.of("timestamp", System.currentTimeMillis()), "ping");
        
        // 發送給所有用戶
        userConnections.keySet().forEach(userId -> sendToUser(userId, heartbeat));
        
        // 發送給所有管理員
        sendToAllAdmins(heartbeat);
    }
}
```

### 3. SSE 通知服務

```java
// src/main/java/journey/service/SSENotificationService.java
package journey.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import journey.dto.SSENotificationDTO;
import java.util.Map;

@Service
public class SSENotificationService {
    
    @Autowired
    private SSEConnectionManager connectionManager;
    
    /**
     * 發送新檢舉通知
     */
    public void sendNewReportNotification(Integer reportId, Integer commentId, 
                                        String commentContent, String reporterName, String reason) {
        Map<String, Object> data = Map.of(
            "reportId", reportId,
            "commentId", commentId,
            "commentContent", commentContent,
            "reporterName", reporterName,
            "reason", reason,
            "timestamp", System.currentTimeMillis()
        );
        
        SSENotificationDTO notification = new SSENotificationDTO("new_report", data, "有新的檢舉需要處理");
        
        // 發送給所有管理員
        connectionManager.sendToAllAdmins(notification);
        
        System.out.println("📢 已發送新檢舉通知 - 檢舉ID: " + reportId);
    }
    
    /**
     * 發送檢舉狀態更新通知
     */
    public void sendReportStatusUpdateNotification(Integer reportId, Integer commentId, 
                                                 String status, String processedBy, String processedByUserName) {
        Map<String, Object> data = Map.of(
            "reportId", reportId,
            "commentId", commentId,
            "status", status,
            "processedBy", processedBy,
            "processedByUserName", processedByUserName,
            "timestamp", System.currentTimeMillis()
        );
        
        SSENotificationDTO notification = new SSENotificationDTO("report_update", data, "檢舉狀態已更新");
        
        // 發送給所有管理員
        connectionManager.sendToAllAdmins(notification);
        
        System.out.println("📢 已發送檢舉狀態更新通知 - 檢舉ID: " + reportId + ", 狀態: " + status);
    }
    
    /**
     * 發送留言更新通知
     */
    public void sendCommentUpdateNotification(Integer commentId, String action, String targetType, 
                                            Integer targetId, Integer userId, String userName) {
        Map<String, Object> data = Map.of(
            "commentId", commentId,
            "action", action,
            "targetType", targetType,
            "targetId", targetId,
            "userId", userId,
            "userName", userName,
            "timestamp", System.currentTimeMillis()
        );
        
        SSENotificationDTO notification = new SSENotificationDTO("comment_update", data, "有新的留言活動");
        
        // 發送給所有管理員
        connectionManager.sendToAllAdmins(notification);
        
        System.out.println("📢 已發送留言更新通知 - 留言ID: " + commentId + ", 動作: " + action);
    }
    
    /**
     * 發送一般通知
     */
    public void sendGeneralNotification(String title, String message, String category, 
                                      String priority, Integer targetUserId) {
        Map<String, Object> data = Map.of(
            "title", title,
            "message", message,
            "category", category,
            "priority", priority,
            "targetUserId", targetUserId,
            "timestamp", System.currentTimeMillis()
        );
        
        SSENotificationDTO notification = new SSENotificationDTO("notification", data, message);
        
        if (targetUserId != null) {
            // 發送給特定用戶
            connectionManager.sendToUser(targetUserId, notification);
        } else {
            // 發送給所有管理員
            connectionManager.sendToAllAdmins(notification);
        }
        
        System.out.println("📢 已發送一般通知 - 標題: " + title);
    }
    
    /**
     * 發送錯誤通知
     */
    public void sendErrorNotification(String code, String message) {
        Map<String, Object> data = Map.of(
            "code", code,
            "message", message,
            "timestamp", System.currentTimeMillis()
        );
        
        SSENotificationDTO notification = new SSENotificationDTO("error", data, message);
        
        // 發送給所有管理員
        connectionManager.sendToAllAdmins(notification);
        
        System.out.println("❌ 已發送錯誤通知 - 代碼: " + code + ", 訊息: " + message);
    }
}
```

### 4. SSE 控制器

```java
// src/main/java/journey/controller/SSEController.java
package journey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import journey.service.SSEConnectionManager;
import journey.service.SSENotificationService;
import journey.utils.JwtUtil;
import journey.dto.user.JwtDTO;
import journey.enums.UserTypeEnum;

import java.util.Map;

@RestController
@RequestMapping("/api/sse")
@CrossOrigin(origins = {"http://localhost:5173", "https://yourdomain.com"})
public class SSEController {

    @Autowired
    private SSEConnectionManager connectionManager;
    
    @Autowired
    private SSENotificationService notificationService;

    /**
     * 建立 SSE 連接
     * 支援 URL 參數和 Authorization Header 兩種認證方式
     */
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(
            @RequestParam(value = "token", required = false) String tokenParam,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        try {
            // 優先使用 URL 參數，如果沒有則使用 Authorization Header
            String token = null;
            if (tokenParam != null && !tokenParam.trim().isEmpty()) {
                token = tokenParam;
            } else if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = JwtUtil.extractToken(authHeader); // 使用現有的 extractToken 方法
            }
            
            if (token == null) {
                throw new RuntimeException("缺少或格式錯誤的 Token");
            }

            // 使用現有的 JWT 驗證機制
            JwtDTO jwt = JwtUtil.verfiy(token);
            if (jwt == null) {
                throw new RuntimeException("Token 驗證失敗");
            }

            System.out.println("🔗 用戶 " + jwt.getUsername() + " (ID: " + jwt.getId() + ", 類型: " + jwt.getUserType() + ") 請求 SSE 連接");
            
            // 根據用戶類型建立連接
            if (jwt.getUserType() == UserTypeEnum.ADMIN) {
                return connectionManager.createAdminConnection(jwt.getUsername());
            } else {
                return connectionManager.createUserConnection(jwt.getId());
            }

        } catch (Exception e) {
            System.err.println("❌ 建立 SSE 連接失敗: " + e.getMessage());
            throw new RuntimeException("建立 SSE 連接失敗: " + e.getMessage());
        }
    }

    /**
     * 斷開 SSE 連接
     */
    @DeleteMapping("/disconnect")
    public ResponseEntity<?> disconnect(
            @RequestParam(value = "token", required = false) String tokenParam,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        try {
            String token = null;
            if (tokenParam != null && !tokenParam.trim().isEmpty()) {
                token = tokenParam;
            } else if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = JwtUtil.extractToken(authHeader); // 使用現有的 extractToken 方法
            }
            
            if (token == null) {
                return ResponseEntity.badRequest().body("缺少 Token");
            }

            // 使用現有的 JWT 驗證機制
            JwtDTO jwt = JwtUtil.verfiy(token);
            if (jwt == null) {
                return ResponseEntity.status(401).body("Token 驗證失敗");
            }

            // 這裡可以實作斷開連接的邏輯
            System.out.println("🔌 用戶 " + jwt.getUsername() + " 請求斷開 SSE 連接");
            
            return ResponseEntity.ok().body("連接已斷開");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("斷開連接失敗: " + e.getMessage());
        }
    }

    /**
     * 獲取連接統計（僅管理員可存取）
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getStats(
            @RequestHeader("Authorization") String authHeader) {
        
        try {
            // 使用現有的 JWT 驗證機制
            String token = JwtUtil.extractToken(authHeader);
            if (token == null) {
                return ResponseEntity.status(401).body("缺少或格式錯誤的 Authorization Header");
            }

            JwtDTO jwt = JwtUtil.verfiy(token);
            if (jwt == null) {
                return ResponseEntity.status(401).body("Token 驗證失敗");
            }

            // 檢查管理員權限
            if (jwt.getUserType() != UserTypeEnum.ADMIN) {
                return ResponseEntity.status(403).body("權限不足: 只有管理員可以查看統計");
            }

            Map<String, Object> stats = connectionManager.getConnectionStats();
            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("獲取統計失敗: " + e.getMessage());
        }
    }

    /**
     * 測試發送通知（僅管理員可存取）
     */
    @PostMapping("/test/notification")
    public ResponseEntity<?> testNotification(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> request) {
        
        try {
            // 使用現有的 JWT 驗證機制
            String token = JwtUtil.extractToken(authHeader);
            if (token == null) {
                return ResponseEntity.status(401).body("缺少或格式錯誤的 Authorization Header");
            }

            JwtDTO jwt = JwtUtil.verfiy(token);
            if (jwt == null) {
                return ResponseEntity.status(401).body("Token 驗證失敗");
            }

            // 檢查管理員權限
            if (jwt.getUserType() != UserTypeEnum.ADMIN) {
                return ResponseEntity.status(403).body("權限不足: 只有管理員可以測試通知");
            }

            String title = request.get("title");
            String message = request.get("message");
            String type = request.get("type");

            if (title == null || message == null || type == null) {
                return ResponseEntity.badRequest().body("缺少必要參數: title, message, type");
            }

            // 根據類型發送測試通知
            switch (type) {
                case "new_report":
                    notificationService.sendNewReportNotification(999, 888, "測試留言內容", "測試用戶", "測試原因");
                    break;
                case "report_update":
                    notificationService.sendReportStatusUpdateNotification(999, 888, "PROCESSED", "admin", "管理員");
                    break;
                case "comment_update":
                    notificationService.sendCommentUpdateNotification(888, "CREATED", "lodging", 123, 456, "測試用戶");
                    break;
                case "notification":
                    notificationService.sendGeneralNotification(title, message, "SYSTEM", "NORMAL", null);
                    break;
                default:
                    return ResponseEntity.badRequest().body("不支援的通知類型: " + type);
            }

            return ResponseEntity.ok().body("測試通知已發送");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("發送測試通知失敗: " + e.getMessage());
        }
    }
}
```

### 5. 更新現有安全配置

在您現有的 `SecurityConfig.java` 中加入 SSE 端點權限：

```java
// 在 SecurityConfig.java 的 authorizeHttpRequests 中加入
.requestMatchers("/api/sse/**").permitAll()  // 允許 SSE 端點
```

您的 CORS 配置已經很完整，支援前端開發環境 `http://localhost:5173`，所以不需要額外的 CORS 設定。

## 🔄 整合現有服務

### 1. 在檢舉服務中整合 SSE 通知

```java
// 在 CommentReportsService.java 中加入
@Autowired
private SSENotificationService sseNotificationService;

// 在 create 方法中加入
public void create(CommentReportRequestDTO req, Integer userId, UserTypeEnum userType) {
    // ... 現有邏輯 ...
    
    // 發送新檢舉通知
    sseNotificationService.sendNewReportNotification(
        report.getId(),
        req.commentId(),
        comment.getContent(),
        user.getUsername(),
        reason.getReasonText()
    );
}

// 在 processReport 方法中加入
public void processReport(Integer reportId, String status, String note, Integer adminId) {
    // ... 現有邏輯 ...
    
    // 發送檢舉狀態更新通知
    sseNotificationService.sendReportStatusUpdateNotification(
        reportId,
        report.getComment().getId(),
        status,
        admin.getUsername(),
        admin.getAllUserBean().getUsername()
    );
}
```

### 2. 在留言服務中整合 SSE 通知

```java
// 在 CommentService.java 中加入
@Autowired
private SSENotificationService sseNotificationService;

// 在 createComment 方法中加入
public CommentResponseDTO createComment(CommentRequestDTO request, Integer userId) {
    // ... 現有邏輯 ...
    
    // 發送留言更新通知
    sseNotificationService.sendCommentUpdateNotification(
        comment.getId(),
        "CREATED",
        request.targetType(),
        request.targetId(),
        userId,
        user.getUsername()
    );
    
    return convertToDTO(comment);
}
```

## ⏰ 心跳機制

```java
// 在 AppConfig.java 中加入
@Bean
@Scheduled(fixedRate = 30000) // 每30秒執行一次
public void sendHeartbeat() {
    connectionManager.sendHeartbeat();
}
```

## 🧪 測試端點

### 1. 測試連接
```bash
curl -N "http://localhost:8080/api/sse/connect?token=YOUR_JWT_TOKEN"
```

### 2. 測試通知（管理員）
```bash
curl -X POST "http://localhost:8080/api/sse/test/notification" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"測試通知","message":"這是一個測試通知","type":"notification"}'
```

### 3. 查看統計（管理員）
```bash
curl -H "Authorization: Bearer ADMIN_JWT_TOKEN" \
  "http://localhost:8080/api/sse/stats"
```

## 📋 部署檢查清單

- [ ] 實作 SSE 控制器和服務
- [ ] 整合到現有的檢舉和留言服務
- [ ] 設定 CORS 配置
- [ ] 實作心跳機制
- [ ] 測試連接和通知功能
- [ ] 設定生產環境的代理配置
- [ ] 監控連接數量和效能

---

**文件版本**：1.0  
**最後更新**：2025-01-15  
**適用版本**：Spring Boot 3.x 