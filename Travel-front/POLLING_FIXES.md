# 輪詢異常中斷問題修正

## 問題概況

留言檢舉輪詢機制在實際運行中會異常中斷，導致無法持續監控留言狀態變化。

## 問題分析

### 已處理的狀況 ✅
- 留言已被封鎖/刪除時會正確停止監控

### 潛在問題點 🟡
1. **錯誤處理有漏**：非 404 錯誤會跳過但未停止，可能造成監控卡死
2. **Vue onUnmounted 事件**：組件卸載會觸發 stopPolling()，輪詢直接停止
3. **延遲停止計時器誤觸發**：意外錯誤可能導致提前清空監控列表
4. **開發環境熱更新**：pollingTimer 遺失（僅開發階段）

## 修正方案

### 1. 全域單例控制
```javascript
// 避免多實例衝突
if (!window._globalPollingTimer) {
  window._globalPollingTimer = null
}
```

### 2. 錯誤處理與重試機制
```javascript
// 錯誤計數器（防止無限重試）
const commentErrorCount = ref(new Map())
const MAX_ERROR_COUNT = 3

// 累積錯誤次數，超過上限才停止監控
const currentErrorCount = commentErrorCount.value.get(commentId) || 0
const newErrorCount = currentErrorCount + 1

if (newErrorCount >= MAX_ERROR_COUNT) {
  console.error(`❌ 留言 ${commentId} 錯誤次數過多，停止監控`)
  stopMonitoring(commentId)
}
```

### 3. 保底監控機制
```javascript
// 每 10 秒檢查一次輪詢狀態
window._globalHealthCheckTimer = setInterval(() => {
  // 檢查是否有監控中的留言但輪詢已停止
  if (!window._globalPollingTimer && monitoredComments.value.size > 0) {
    console.warn('❗輪詢意外停止，自動重啟')
    startPolling()
  }
}, 10000)
```

### 4. 安全的組件卸載清理
```javascript
onUnmounted(() => {
  // 不要直接停止全域輪詢，因為其他組件可能還在監控
  console.log('🧹 組件卸載，清理本地資源')
  
  // 只清理延遲停止計時器（如果是由當前組件設置的）
  if (window._globalDelayedStopTimer && monitoredComments.value.size === 0) {
    clearTimeout(window._globalDelayedStopTimer)
    window._globalDelayedStopTimer = null
  }
})
```

## 修正效果

### 解決的問題
- ✅ 防止輪詢意外停止
- ✅ 錯誤重試機制，避免無限重試
- ✅ 全域單例控制，避免多實例衝突
- ✅ 安全的組件卸載清理
- ✅ 保底監控機制，自動重啟異常停止的輪詢

### 新增功能
- 🛡️ 保底監控機制
- 🔄 自動重啟機制
- 🧹 安全的資源清理
- 📝 詳細日誌記錄

## 測試方法

1. **檢舉留言**：在留言頁面檢舉留言
2. **觀察控制台**：查看輪詢狀態和錯誤計數
3. **模擬錯誤**：在瀏覽器開發者工具中模擬網路錯誤
4. **組件切換**：切換頁面測試組件卸載情況

## 監控重點

### 日誌觀察重點
- `stopPolling()` 是否被非預期呼叫？
- `monitoredComments.value.size === 0` 是否在未處理完成時提前變為 0？
- 錯誤計數是否正常累積和清除？
- 保底監控是否正常檢測和重啟輪詢？

### 開發者工具檢查
- `window._globalPollingTimer` 是否存在？
- `window._globalHealthCheckTimer` 是否正常運行？
- `window._globalDelayedStopTimer` 是否異常觸發？

## 團隊說明範本

目前我們的檢舉留言監控機制會啟動輪詢去查留言狀態，不過實測時輪詢會在某些情況中斷。

**經分析可能原因包括：**
- Vue 組件卸載觸發 onUnmounted 停止 polling
- 非 404 錯誤未觸發 retry 或重試機制失效
- 延遲停止計時器異常觸發
- 開發環境熱更新導致計時器遺失

**修正方案：**
- 將 polling 改為全域管理，避免被任何單一組件關閉
- 加入監控續命機制，自動重啟異常停止的輪詢
- 實作保底錯誤 retry 計數，防止無限重試
- 優化組件卸載時的資源清理邏輯

現在輪詢機制更加穩定，能夠持續監控留言狀態直到真正被處理完成。 