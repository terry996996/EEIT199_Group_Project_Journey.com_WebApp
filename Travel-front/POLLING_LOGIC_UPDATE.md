# 輪詢邏輯修正說明

## 問題分析

原本的輪詢邏輯過於複雜，導致在檢舉留言後立即停止監控，無法檢測到管理員的後續處理。

## 修正後的邏輯

### 監控目標
根據用戶建議的邏輯：

- **`isActive: true` + `deleteStatus: 1`** → 留言正常，繼續監控
- **`isActive: false` 或 `deleteStatus: 0`** → 留言被處理，停止監控

### 停止監控的條件
1. `isActive === false` (留言被屏蔽)
2. `deleteStatus === 0` (留言被刪除)
3. API 返回 404 (留言被後端刪除)

### 不會停止監控的情況
- `reportable: false` (僅 UI 控制，不影響監控)
- 其他 API 錯誤 (只記錄，不中斷輪詢)
- 網路錯誤 (繼續重試)

### 核心判斷邏輯
```javascript
const isDeleted = comment.isActive === false || comment.deleteStatus === 0;
if (isDeleted) {
  // 停止輪詢
  stopMonitoring(commentId);
} else {
  // 繼續監控
}
```

## 測試方法

1. **檢舉留言**：在留言頁面檢舉留言
2. **觀察控制台**：查看輪詢狀態和事件日誌
3. **檢查網路**：在瀏覽器開發者工具中觀察 API 請求

## 預期行為

1. **檢舉後**：輪詢開始，每 5 秒檢查一次
2. **管理員處理前**：繼續監控，`isActive` 保持 `true`
3. **管理員屏蔽後**：`isActive` 變為 `false`，停止監控
4. **UI 更新**：前端立即收到狀態變化事件，更新顯示

## 調試工具

- **瀏覽器控制台**：詳細的輪詢日誌
- **網路面板**：觀察 API 請求狀態
- **Vue DevTools**：查看組件狀態

## 程式碼變更

### 主要修正
- 簡化 `checkStatusChange` 函數邏輯
- 統一 `checkSingleComment` 中的判斷條件
- 移除複雜的檢舉狀態檢查

### 新增功能
- 詳細的日誌記錄
- 錯誤重試機制
- 保底監控機制 