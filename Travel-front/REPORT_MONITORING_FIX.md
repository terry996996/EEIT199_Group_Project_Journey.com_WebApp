# 檢舉監控輪詢修正說明

## 問題描述

原本的檢舉監控系統存在一個問題：**檢舉完成後立即停止輪詢，導致無法監控到管理員的後續處理**。

### 問題現象

從控制台日誌可以看到：
```
✅ 檢舉成功: {message: '檢舉已提交'}
🔄 開始監控留言 81 的狀態
🔄 開始輪詢機制
⏹️ 停止輪詢機制: {reason: '沒有留言需要監控'}
```

這表示在檢舉提交後，系統立即停止了輪詢，無法監控到管理員後續的處理動作。

## 問題根源

### 1. 狀態判斷邏輯錯誤

原本的 `checkStatusChange` 函數：
```javascript
const isProcessed = !newComment.isActive || newComment.deleteStatus === 0
```

這個邏輯會導致**剛檢舉的留言被誤判為已處理**，因為：
- 剛檢舉的留言可能 `isActive` 為 `false`（被標記為已檢舉）
- 或者 `deleteStatus` 為 `0`（被標記為待處理）

### 2. 立即停止輪詢

原本的邏輯在沒有留言需要監控時會立即停止輪詢，沒有考慮到：
- 檢舉狀態變化不等於處理完成
- 需要持續監控直到真正處理完成

## 修正方案

### 1. 改善狀態判斷邏輯

```javascript
// 修正後的狀態判斷
const checkStatusChange = (commentId, newComment) => {
  // 檢查留言是否被真正處理（刪除或隱藏）
  const isActuallyProcessed = newComment.deleteStatus === 0 || 
                             (newComment.isActive === false && newComment.deleteStatus !== null)
  
  // 檢查是否有檢舉狀態變化
  const hasReportStatus = newComment.reportStatus || newComment.isReported
  
  if (isActuallyProcessed) {
    // 真正被處理才停止監控
    return true
  }
  
  if (hasReportStatus) {
    // 檢舉狀態變化，觸發事件但不停止監控
    return true
  }
  
  return false
}
```

### 2. 延遲停止輪詢機制

```javascript
// 延遲停止輪詢，避免立即停止
if (monitoredComments.value.size === 0) {
  console.log(`⏳ 沒有留言需要監控，將在 10 秒後停止輪詢`)
  
  // 清除之前的延遲停止計時器
  if (delayedStopTimer) {
    clearTimeout(delayedStopTimer)
  }
  
  delayedStopTimer = setTimeout(() => {
    if (monitoredComments.value.size === 0) {
      stopPolling()
    } else {
      console.log(`🔄 發現新的監控需求，繼續輪詢`)
    }
    delayedStopTimer = null
  }, 10000) // 延遲 10 秒
}
```

### 3. 改善停止監控條件

```javascript
// 只有當留言被真正處理時才停止監控
const isActuallyProcessed = comment.deleteStatus === 0 || 
                           (comment.isActive === false && comment.deleteStatus !== null)

if (isActuallyProcessed) {
  console.log(`🛑 留言 ${commentId} 已被真正處理，停止監控`)
  stopMonitoring(commentId)
} else {
  console.log(`📋 留言 ${commentId} 狀態變化但未處理完成，繼續監控`)
}
```

## 修正後的流程

### 1. 檢舉提交階段
```
✅ 檢舉成功提交
🔄 開始監控留言狀態
🔄 開始輪詢機制
📋 留言狀態變化但未處理完成，繼續監控
```

### 2. 持續監控階段
```
🔍 開始輪詢檢查 1 個留言的狀態
📊 輪詢完成統計: {totalComments: 1, successCount: 1, errorCount: 0, duration: "150ms"}
📋 留言狀態變化但未處理完成，繼續監控
```

### 3. 管理員處理階段
```
🔍 檢查留言狀態變化
✅ 留言已被真正處理
🛑 留言已被真正處理，停止監控
⏳ 沒有留言需要監控，將在 10 秒後停止輪詢
⏹️ 停止輪詢機制
```

## 新增功能

### 1. 狀態快取機制
```javascript
// 留言狀態快取（避免重複處理）
const commentStatusCache = ref(new Map())
```

### 2. 延遲停止計時器
```javascript
// 延遲停止計時器
let delayedStopTimer = null
```

### 3. 詳細狀態檢查
```javascript
console.log(`🔍 檢查留言 ${commentId} 狀態變化:`, {
  isActive: newComment.isActive,
  deleteStatus: newComment.deleteStatus,
  reportStatus: newComment.reportStatus,
  isReported: newComment.isReported,
  isActuallyProcessed,
  hasReportStatus
})
```

## 測試建議

### 1. 檢舉流程測試
1. 提交檢舉
2. 觀察是否開始監控
3. 確認輪詢持續進行
4. 檢查狀態變化日誌

### 2. 管理員處理測試
1. 管理員處理檢舉
2. 觀察狀態變化
3. 確認監控停止
4. 檢查最終狀態

### 3. 多留言監控測試
1. 檢舉多個留言
2. 觀察監控數量
3. 分別處理留言
4. 確認監控正確停止

## 預期效果

修正後，檢舉監控系統將：

1. **持續監控**：檢舉後持續監控直到真正處理完成
2. **智能停止**：只有真正處理完成才停止監控
3. **延遲停止**：避免立即停止，給新檢舉留出時間
4. **詳細日誌**：提供完整的狀態變化追蹤
5. **穩定運作**：避免因狀態判斷錯誤導致的問題

這樣可以確保用戶能夠即時看到檢舉的處理結果，提升用戶體驗。 