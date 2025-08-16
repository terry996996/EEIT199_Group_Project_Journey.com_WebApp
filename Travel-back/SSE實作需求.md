# SSE (Server-Sent Events) 實作需求文件

## 📋 概述

本文件定義了前端所需的 SSE (Server-Sent Events) 實作需求，用於實現即時通知和狀態更新功能。

## 🛠️ 前端技術棧資訊

### 前端框架
- **主要框架**：Vue.js 3 (Composition API)
- **UI 框架**：Bootstrap 5 + Bootstrap Icons
- **狀態管理**：Vue 3 原生狀態管理 (ref, reactive, computed)
- **路由**：Vue Router 4
- **HTTP 客戶端**：Axios
- **通知庫**：SweetAlert2

### 前端專案結構
- **專案根目錄**：`/c%3A/Users/User/Desktop/Travel/Travel-front/Travel-front`
- **API 服務**：`src/services/api.js` (Axios 配置)
- **認證機制**：JWT Token 儲存在 localStorage
- **開發環境**：Vite 開發伺服器 (端口 5173)

### 認證機制
- **Token 儲存**：`localStorage.setItem('token', token)`
- **用戶資訊**：`localStorage.setItem('userType', 'ADMIN/USER/VENDOR')`
- **用戶 ID**：`localStorage.setItem('userId', userId)`

## 🔗 後端整合需求

### SSE 通知場景
1. **檢舉通知**：
   - 用戶提交檢舉時通知管理員
   - 管理員處理檢舉時通知檢舉者
   - 檢舉狀態變更通知

2. **留言通知**：
   - 新留言通知
   - 留言被按讚通知
   - 留言被回覆通知

3. **系統通知**：
   - 系統維護通知
   - 重要更新通知

### 用戶類型與通知需求
1. **一般用戶 (USER)**：
   - 自己的留言被回覆/按讚通知
   - 自己提交的檢舉處理結果通知
   - 系統維護通知

2. **管理員 (ADMIN)**：
   - 新檢舉通知
   - 檢舉處理完成通知
   - 系統統計通知
   - 所有用戶相關通知

3. **商家 (VENDOR)**：
   - 自己商品的留言通知
   - 訂單相關通知
   - 商品審核通知

## ⚙️ 技術實作偏好

### 連接管理
- **自動重連**：需要自動重連機制
- **連接狀態顯示**：需要在前端顯示連接狀態
- **多頁面連接**：用戶切換頁面時保持連接

### 通知處理
- **通知樣式**：Toast 通知 + 頁面內通知彈窗
- **通知持久化**：需要儲存通知歷史到 localStorage
- **通知分類**：按類型分類顯示（檢舉、留言、系統）

## 🔒 安全性需求

### 認證方式
- **Token 傳遞**：偏好 URL 參數方式 (`/api/sse/connect?token={JWT_TOKEN}`)
- **權限控制**：不同用戶類型接收不同通知
- **連接限制**：每個用戶最多 1 個同時連接

### 資料安全
- **事件過濾**：只發送用戶有權限接收的事件
- **Token 驗證**：每次連接都驗證 JWT Token
- **連接清理**：用戶登出時清理連接

## 📊 效能考量

### 連接管理
- **連接池**：預期最多 100 個同時連接
- **超時設定**：連接超時 120 秒，讀取超時 60 秒
- **心跳機制**：每 30 秒發送一次心跳

### 通知頻率
- **預期通知量**：每天約 100-500 個通知
- **通知大小**：單個通知資料量 < 1KB
- **事件頻率**：平均每分鐘 1-5 個事件

## 🧪 測試需求

### 測試環境
- **開發環境**：`http://localhost:8080` (後端), `http://localhost:5173` (前端)
- **測試資料**：需要測試用的用戶和管理員帳號
- **測試工具**：Postman、瀏覽器開發者工具

### 測試場景
- 連接建立/斷開測試
- 事件發送/接收測試
- 重連機制測試
- 權限控制測試

## 🚀 部署考量

### 環境配置
- **生產環境**：後端 API 的生產環境 URL (待定)
- **HTTPS**：生產環境使用 HTTPS
- **代理設定**：通過 Nginx 代理伺服器

### 監控需求
- 連接數量監控
- 事件發送頻率監控
- 錯誤率監控

## 💻 現有程式碼資訊

### 後端現狀
- **檢舉相關 API**：`POST /api/comments/{id}/report`
- **用戶認證**：JWT Token 認證機制
- **資料庫結構**：檢舉表、用戶表、留言表

### 前端現狀
- **現有組件**：`src/components/comments/ReportList.vue`
- **API 調用**：`src/services/api.js` 中的 commentAPI
- **狀態管理**：使用 Vue 3 Composition API

## 🌐 API 端點

### 1. SSE 連接端點

```
GET /api/sse/connect
```

**認證方式**：
- 使用 URL 參數傳遞 token：`/api/sse/connect?token={JWT_TOKEN}`
- 或使用 Authorization Header：`Authorization: Bearer {JWT_TOKEN}`

**回應格式**：
```
Content-Type: text/event-stream
Cache-Control: no-cache
Connection: keep-alive
```

## 📊 事件類型與格式

### 1. 連接建立事件

**事件類型**：`connect`

**資料格式**：
```json
{
  "type": "connect",
  "data": {
    "userId": 123,
    "userType": "ADMIN",
    "userName": "管理員",
    "timestamp": "2025-07-15T08:00:00Z",
    "message": "SSE 連接已建立"
  }
}
```

### 2. 檢舉狀態更新事件

**事件類型**：`report_update`

**資料格式**：
```json
{
  "type": "report_update",
  "data": {
    "reportId": 456,
    "commentId": 789,
    "status": "PROCESSED", // PENDING, PROCESSED, REJECTED, RESOLVED
    "processedBy": "admin123",
    "processedByUserName": "管理員",
    "timestamp": "2025-07-15T08:30:00Z",
    "message": "檢舉已處理"
  }
}
```

### 3. 新檢舉通知事件

**事件類型**：`new_report`

**資料格式**：
```json
{
  "type": "new_report",
  "data": {
    "reportId": 456,
    "commentId": 789,
    "commentContent": "檢舉的留言內容...",
    "reporterName": "檢舉者名稱",
    "reason": "檢舉原因",
    "timestamp": "2025-07-15T08:35:00Z",
    "message": "有新的檢舉需要處理"
  }
}
```

### 4. 留言更新事件

**事件類型**：`comment_update`

**資料格式**：
```json
{
  "type": "comment_update",
  "data": {
    "commentId": 789,
    "action": "CREATED", // CREATED, UPDATED, DELETED, LIKED, UNLIKED
    "targetType": "lodging", // lodging, flight, attraction, traffic
    "targetId": 123,
    "userId": 456,
    "userName": "用戶名稱",
    "timestamp": "2025-07-15T08:40:00Z",
    "message": "有新的留言"
  }
}
```

### 5. 一般通知事件

**事件類型**：`notification`

**資料格式**：
```json
{
  "type": "notification",
  "data": {
    "id": 789,
    "title": "通知標題",
    "message": "通知內容",
    "category": "SYSTEM", // SYSTEM, USER, VENDOR, ADMIN
    "priority": "NORMAL", // LOW, NORMAL, HIGH, URGENT
    "targetUserId": 123,
    "timestamp": "2025-07-15T08:45:00Z",
    "actionUrl": "/admin/reports" // 可選，點擊通知後跳轉的 URL
  }
}
```

### 6. 心跳事件

**事件類型**：`heartbeat`

**資料格式**：
```json
{
  "type": "heartbeat",
  "data": {
    "timestamp": "2025-07-15T08:50:00Z",
    "message": "ping"
  }
}
```

### 7. 連接斷開事件

**事件類型**：`disconnect`

**資料格式**：
```json
{
  "type": "disconnect",
  "data": {
    "userId": 123,
    "reason": "SERVER_SHUTDOWN", // SERVER_SHUTDOWN, USER_LOGOUT, TIMEOUT
    "timestamp": "2025-07-15T08:55:00Z",
    "message": "SSE 連接已斷開"
  }
}
```

## 🔐 認證與權限

### 用戶權限控制

1. **管理員 (ADMIN)**：
   - 接收所有事件類型
   - 特別關注檢舉相關事件

2. **一般用戶 (USER)**：
   - 接收個人相關通知
   - 接收留言相關事件（自己的留言）

3. **商家 (VENDOR)**：
   - 接收商家相關通知
   - 接收自己商品的留言事件

### 事件過濾規則

- 根據用戶類型過濾事件
- 根據用戶 ID 過濾個人相關事件
- 管理員可接收所有事件

## 🔄 連線管理

### 重連機制

1. **自動重連**：
   - 斷線後 3 秒開始重連
   - 最大重連次數：10 次
   - 重連間隔：3 秒、6 秒、12 秒...（指數退避）

2. **手動重連**：
   - 提供手動重連按鈕
   - 重置重連計數器

### 心跳機制

1. **心跳頻率**：
   - 每 30 秒發送一次心跳
   - 如果 90 秒內沒有收到心跳，視為斷線

2. **超時處理**：
   - 連接超時：120 秒
   - 讀取超時：60 秒

## 🚨 錯誤處理

### 錯誤回應格式

```json
{
  "type": "error",
  "data": {
    "code": "AUTH_FAILED", // AUTH_FAILED, PERMISSION_DENIED, SERVER_ERROR
    "message": "認證失敗",
    "timestamp": "2025-07-15T09:00:00Z"
  }
}
```

### 常見錯誤代碼

- `AUTH_FAILED`：認證失敗
- `PERMISSION_DENIED`：權限不足
- `SERVER_ERROR`：伺服器錯誤
- `RATE_LIMIT_EXCEEDED`：請求頻率過高
- `INVALID_TOKEN`：無效的 token

## 📱 前端整合需求

### 事件處理

1. **即時通知顯示**：
   - 瀏覽器通知（如果用戶允許）
   - 頁面內通知彈窗
   - 通知計數更新

2. **狀態同步**：
   - 檢舉列表即時更新
   - 留言列表即時更新
   - 用戶狀態同步

### 連接狀態管理

1. **狀態指示**：
   - 連接中
   - 已連接
   - 連接中斷
   - 重連中
   - 錯誤

2. **狀態顯示**：
   - 在頁面顯示連接狀態
   - 提供手動重連按鈕
   - 顯示最後連接時間

## 🔧 技術規格

### 請求標頭

```
GET /api/sse/connect?token={JWT_TOKEN}
Accept: text/event-stream
Cache-Control: no-cache
Connection: keep-alive
```

### 回應標頭

```
Content-Type: text/event-stream
Cache-Control: no-cache
Connection: keep-alive
Access-Control-Allow-Origin: http://localhost:5173
Access-Control-Allow-Credentials: true
```

### 事件格式

每個事件必須遵循標準 SSE 格式：

```
event: {event_type}
data: {json_data}
id: {event_id}
retry: {retry_interval}

```

## 📋 實作檢查清單

### 後端需要實作

- [ ] SSE 連接端點 (`GET /api/sse/connect`)
- [ ] 用戶認證和權限驗證
- [ ] 事件發送機制
- [ ] 連接管理和清理
- [ ] 心跳機制
- [ ] 錯誤處理
- [ ] 事件過濾邏輯

### 前端需要實作

- [ ] SSE 客戶端連接
- [ ] 事件監聽和處理
- [ ] 重連機制
- [ ] 狀態管理
- [ ] 通知顯示
- [ ] 錯誤處理

## 📞 聯絡資訊

如有任何問題或需要進一步說明，請聯絡前端開發團隊。

---

**文件版本**：1.0  
**最後更新**：2025-07-15  
**狀態**：待實作 