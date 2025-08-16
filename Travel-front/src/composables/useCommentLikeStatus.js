import { ref, computed } from 'vue'
import { commentAPI } from '@/services/api'

export function useCommentLikeStatus() {
  // 按讚狀態快取
  const likeStatusMap = ref(new Map())
  
  // 載入狀態
  const loading = ref(false)
  
  // 取得單個評論的按讚狀態
  const getLikeStatus = (commentId) => {
    return likeStatusMap.value.get(commentId) || {
      likedByCurrentUser: false,
      likeCount: 0
    }
  }
  
  // 更新單個評論的按讚狀態
  const updateLikeStatus = (commentId, status) => {
    likeStatusMap.value.set(commentId, status)
  }
  
  // 批量載入按讚狀態（優化版本）
  const batchLoadLikeStatus = async (commentIds, userId) => {
    if (!commentIds || commentIds.length === 0 || !userId) {
      console.log('🔄 跳過按讚狀態載入：無評論或無用戶')
      return
    }
    
    loading.value = true
    
    try {
      console.log(`🔄 開始批量載入 ${commentIds.length} 個評論的按讚狀態`)
      
      // 使用新的批量 API
      const response = await commentAPI.getBatchLikeStatus(commentIds, userId)
      
      // 更新快取
      Object.entries(response.data).forEach(([commentId, status]) => {
        updateLikeStatus(parseInt(commentId), status)
      })
      
      console.log('✅ 批量 API 載入完成')
    } catch (error) {
      console.error('❌ 批量載入按讚狀態失敗:', error)
      
      // 如果批量 API 失敗，回退到批次處理
      console.log('🔄 回退到批次處理方案')
      const batchSize = 5
      const batches = []
      
      for (let i = 0; i < commentIds.length; i += batchSize) {
        batches.push(commentIds.slice(i, i + batchSize))
      }
      
      for (const batch of batches) {
        const promises = batch.map(commentId => loadSingleLikeStatus(commentId, userId))
        await Promise.all(promises)
        
        // 添加小延遲避免過度並發
        if (batches.length > 1) {
          await new Promise(resolve => setTimeout(resolve, 100))
        }
      }
      
      console.log('✅ 批次處理載入完成')
    } finally {
      loading.value = false
    }
  }
  
  // 載入單個評論的按讚狀態
  const loadSingleLikeStatus = async (commentId, userId) => {
    try {
      const response = await commentAPI.getLikeStatus(commentId, userId)
      updateLikeStatus(commentId, response.data)
      
      console.log(`📊 載入按讚狀態: 評論 ${commentId}`, response.data)
    } catch (error) {
      console.error(`❌ 載入按讚狀態失敗 (評論 ${commentId}):`, error)
      // 設定預設狀態
      updateLikeStatus(commentId, {
        likedByCurrentUser: false,
        likeCount: 0
      })
    }
  }
  
  // 切換按讚狀態（樂觀更新）
  const toggleLike = async (commentId, userId) => {
    const currentStatus = getLikeStatus(commentId)
    const originalStatus = { ...currentStatus }
    
    // 樂觀更新
    const newStatus = {
      likedByCurrentUser: !currentStatus.likedByCurrentUser,
      likeCount: currentStatus.likedByCurrentUser 
        ? currentStatus.likeCount - 1 
        : currentStatus.likeCount + 1
    }
    
    updateLikeStatus(commentId, newStatus)
    
    try {
      const response = await commentAPI.toggleLike(commentId, userId)
      
      // 更新實際狀態
      updateLikeStatus(commentId, response.data)
      
      console.log(`✅ 按讚操作成功: 評論 ${commentId}`, response.data)
      return response.data
    } catch (error) {
      console.error(`❌ 按讚操作失敗: 評論 ${commentId}`, error)
      
      // 恢復原始狀態
      updateLikeStatus(commentId, originalStatus)
      throw error
    }
  }
  
  // 清除快取
  const clearCache = () => {
    likeStatusMap.value.clear()
    console.log('🧹 按讚狀態快取已清除')
  }
  
  // 清除特定評論的快取
  const clearCommentCache = (commentId) => {
    likeStatusMap.value.delete(commentId)
    console.log(`🧹 評論 ${commentId} 的按讚狀態快取已清除`)
  }
  
  // 檢查是否有快取
  const hasCache = (commentId) => {
    return likeStatusMap.value.has(commentId)
  }
  
  // 取得快取統計
  const getCacheStats = computed(() => {
    return {
      totalCached: likeStatusMap.value.size,
      loading: loading.value
    }
  })
  
  return {
    // 狀態
    loading: computed(() => loading.value),
    cacheStats: getCacheStats,
    
    // 主要方法
    getLikeStatus,
    updateLikeStatus,
    batchLoadLikeStatus,
    toggleLike,
    
    // 快取管理
    clearCache,
    clearCommentCache,
    hasCache,
    
    // 單個載入（備用）
    loadSingleLikeStatus
  }
} 