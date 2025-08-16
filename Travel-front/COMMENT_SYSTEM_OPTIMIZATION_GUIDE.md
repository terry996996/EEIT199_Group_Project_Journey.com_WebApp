# 評論系統優化指南

## 📊 目前狀況分析

### 主要問題
1. **按讚狀態載入效能問題** - 每個評論單獨呼叫 API
2. **重複 API 呼叫** - 多個組件重複載入相同資料
3. **圖片處理複雜度** - 多個狀態標記造成混亂
4. **權限檢查分散** - 邏輯分散，維護困難

## 🚀 優化方案

### 1. 後端 API 優化（優先級：高）

#### 1.1 批量按讚狀態 API
```javascript
// 建議新增的後端 API
POST /api/comments/batch-like-status
{
  "commentIds": [1, 2, 3, 4, 5],
  "userId": 123
}

// 回應格式
{
  "success": true,
  "data": {
    "1": { "likedByCurrentUser": true, "likeCount": 5 },
    "2": { "likedByCurrentUser": false, "likeCount": 3 },
    "3": { "likedByCurrentUser": true, "likeCount": 8 }
  }
}
```

#### 1.2 評論列表 API 優化
```javascript
// 建議修改現有 API
GET /api/comments/target/{targetType}/{targetId}?includeLikeStatus=true&userId={userId}

// 回應格式 - 直接包含按讚狀態
{
  "success": true,
  "data": [
    {
      "id": 1,
      "content": "評論內容",
      "author": { "id": 123, "name": "用戶名" },
      "likedByCurrentUser": true,
      "likeCount": 5,
      "images": [...]
    }
  ]
}
```

### 2. 前端優化（優先級：中）

#### 2.1 統一按讚狀態管理
```javascript
// 建議新增 composable: useCommentLikeStatus.js
import { ref, computed } from 'vue'

export function useCommentLikeStatus() {
  const likeStatusMap = ref(new Map())
  
  const getLikeStatus = (commentId) => {
    return likeStatusMap.value.get(commentId) || {
      likedByCurrentUser: false,
      likeCount: 0
    }
  }
  
  const updateLikeStatus = (commentId, status) => {
    likeStatusMap.value.set(commentId, status)
  }
  
  const batchLoadLikeStatus = async (commentIds, userId) => {
    // 使用新的批量 API
    const response = await commentAPI.getBatchLikeStatus(commentIds, userId)
    response.data.forEach((status, commentId) => {
      updateLikeStatus(commentId, status)
    })
  }
  
  return {
    getLikeStatus,
    updateLikeStatus,
    batchLoadLikeStatus
  }
}
```

#### 2.2 圖片狀態簡化
```javascript
// 建議簡化圖片狀態，只使用一個 isActive 欄位
function getActiveImages(comment) {
  if (!comment.images || !Array.isArray(comment.images)) {
    return []
  }
  
  // 簡化邏輯：只檢查 isActive
  return comment.images.filter(img => img.isActive !== false)
}
```

#### 2.3 權限檢查統一化
```javascript
// 建議新增 composable: useCommentPermissions.js
export function useCommentPermissions() {
  const { currentUser, isAdmin, isVendor } = useCurrentUser()
  
  const canCreateMainComment = computed(() => {
    return !isAdmin.value && !isVendor.value
  })
  
  const canReply = computed(() => {
    return true // 所有用戶都可以回覆
  })
  
  const canLike = computed(() => {
    return !isAdmin.value
  })
  
  const canReport = computed(() => {
    return !isAdmin.value
  })
  
  const canEdit = (comment) => {
    return comment.author?.id === currentUser.value?.userId
  }
  
  const canDelete = (comment) => {
    return comment.author?.id === currentUser.value?.userId
  }
  
  return {
    canCreateMainComment,
    canReply,
    canLike,
    canReport,
    canEdit,
    canDelete
  }
}
```

### 3. 效能優化（優先級：中）

#### 3.1 虛擬滾動（大量評論時）
```javascript
// 建議使用 vue-virtual-scroller
import { RecycleScroller } from 'vue-virtual-scroller'

// 當評論數量超過 100 筆時啟用虛擬滾動
const useVirtualScroll = computed(() => allComments.value.length > 100)
```

#### 3.2 圖片懶載入
```javascript
// 建議使用 Intersection Observer API
const lazyLoadImages = () => {
  const imageObserver = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        const img = entry.target
        img.src = img.dataset.src
        imageObserver.unobserve(img)
      }
    })
  })
  
  document.querySelectorAll('img[data-src]').forEach(img => {
    imageObserver.observe(img)
  })
}
```

#### 3.3 評論快取
```javascript
// 建議實作簡單的快取機制
const commentCache = new Map()

const getCachedComments = (targetType, targetId) => {
  const key = `${targetType}-${targetId}`
  const cached = commentCache.get(key)
  
  if (cached && Date.now() - cached.timestamp < 5 * 60 * 1000) {
    return cached.data // 5分鐘內的有效快取
  }
  
  return null
}
```

### 4. 使用者體驗優化（優先級：低）

#### 4.1 樂觀更新優化
```javascript
// 改進樂觀更新機制
const optimisticUpdate = (commentId, action, data) => {
  const comment = findComment(commentId)
  const originalState = { ...comment }
  
  // 立即更新 UI
  Object.assign(comment, data)
  
  return {
    rollback: () => Object.assign(comment, originalState)
  }
}
```

#### 4.2 錯誤處理統一化
```javascript
// 統一的錯誤處理
const handleCommentError = (error, action) => {
  console.error(`評論${action}失敗:`, error)
  
  const message = error.response?.data?.message || 
                 `評論${action}失敗，請稍後再試`
  
  Swal.fire({
    icon: 'error',
    title: '操作失敗',
    text: message,
    confirmButtonText: '確定'
  })
}
```

## 📋 實作優先級

### 第一階段（立即實作）
1. ✅ 後端批量按讚狀態 API
2. ✅ 評論列表 API 包含按讚狀態
3. ✅ 統一權限檢查邏輯

### 第二階段（短期實作）
1. ✅ 前端按讚狀態管理優化
2. ✅ 圖片狀態簡化
3. ✅ 錯誤處理統一化

### 第三階段（長期實作）
1. ✅ 虛擬滾動（如果需要）
2. ✅ 圖片懶載入
3. ✅ 評論快取機制

## 🔧 技術債務清理

### 需要清理的程式碼
1. `AllComment.vue` 中的重複按讚載入邏輯
2. `CommentList.vue` 中的重複權限檢查
3. 複雜的圖片狀態處理邏輯
4. 分散的錯誤處理邏輯

### 建議的重構步驟
1. 建立統一的 composables
2. 簡化組件邏輯
3. 統一 API 呼叫方式
4. 改善錯誤處理機制

## 📊 預期改善效果

### 效能改善
- **API 請求減少**：從 N 個請求減少到 1-2 個請求
- **載入速度提升**：預期提升 50-70%
- **記憶體使用優化**：減少重複資料儲存

### 維護性改善
- **程式碼重複減少**：統一邏輯，減少重複
- **錯誤處理改善**：統一的錯誤處理機制
- **權限管理簡化**：集中的權限檢查邏輯

### 使用者體驗改善
- **載入速度更快**：減少等待時間
- **操作更流暢**：樂觀更新機制
- **錯誤提示更友善**：統一的錯誤訊息 