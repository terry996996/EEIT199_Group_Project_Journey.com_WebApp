import { ref, onUnmounted, readonly } from 'vue'
import { commentAPI } from '@/services/api'

/**
 * 檢舉狀態監控 Composable
 * 用於監控已檢舉留言的狀態變化，實現即時更新
 */
export function useCommentReportMonitor() {
  // 監控中的留言ID列表
  const monitoredComments = ref(new Set())
  
  // 留言狀態快取（避免重複處理）
  const commentStatusCache = ref(new Map())
  
  // 錯誤計數器（防止無限重試）
  const commentErrorCount = ref(new Map())
  
  // 輪詢間隔（毫秒）
  const POLLING_INTERVAL = 5000 // 5秒
  
  // 最大錯誤次數
  const MAX_ERROR_COUNT = 3
  
  // 全域輪詢計時器（避免多實例衝突）
  if (!window._globalPollingTimer) {
    window._globalPollingTimer = null
  }
  
  // 全域延遲停止計時器
  if (!window._globalDelayedStopTimer) {
    window._globalDelayedStopTimer = null
  }
  
  // 保底監控計時器（防止輪詢意外停止）
  if (!window._globalHealthCheckTimer) {
    window._globalHealthCheckTimer = null
  }
  
  // 開始監控留言狀態
  const startMonitoring = (commentId) => {
    if (!commentId) {
      console.warn('⚠️ 無法監控：commentId 為空')
      return
    }
    
    const wasAlreadyMonitoring = monitoredComments.value.has(commentId)
    monitoredComments.value.add(commentId)
    
    console.log(`🔄 開始監控留言 ${commentId} 的狀態:`, {
      commentId,
      wasAlreadyMonitoring,
      totalMonitored: monitoredComments.value.size,
      timestamp: new Date().toLocaleTimeString()
    })
    
    // 如果還沒有開始輪詢，則開始輪詢
    if (!window._globalPollingTimer) {
      startPolling()
    }
    
    // 啟動保底監控機制
    startHealthCheck()
  }
  
  // 停止監控留言狀態
  const stopMonitoring = (commentId) => {
    if (!commentId) return
    
    const wasMonitoring = monitoredComments.value.has(commentId)
    monitoredComments.value.delete(commentId)
    
    console.log(`⏹️ 停止監控留言 ${commentId} 的狀態:`, {
      commentId,
      wasMonitoring,
      remainingMonitored: monitoredComments.value.size,
      timestamp: new Date().toLocaleTimeString()
    })
    
    // 如果沒有留言需要監控，延遲停止輪詢（避免立即停止）
    if (monitoredComments.value.size === 0) {
      console.log(`⏳ 沒有留言需要監控，將在 10 秒後停止輪詢`)
      
      // 清除之前的延遲停止計時器
      if (window._globalDelayedStopTimer) {
        clearTimeout(window._globalDelayedStopTimer)
      }
      
      window._globalDelayedStopTimer = setTimeout(() => {
        if (monitoredComments.value.size === 0) {
          stopPolling()
        } else {
          console.log(`🔄 發現新的監控需求，繼續輪詢`)
        }
        window._globalDelayedStopTimer = null
      }, 10000) // 延遲 10 秒
    }
  }
  
  // 開始輪詢
  const startPolling = () => {
    if (window._globalPollingTimer) {
      console.warn('⚠️ 全域輪詢已在進行中')
      return
    }
    
    console.log('🔄 開始全域輪詢機制:', {
      interval: `${POLLING_INTERVAL}ms`,
      monitoredCount: monitoredComments.value.size,
      timestamp: new Date().toLocaleTimeString()
    })
    
    window._globalPollingTimer = setInterval(async () => {
      await checkMonitoredComments()
    }, POLLING_INTERVAL)
  }
  
  // 停止輪詢
  const stopPolling = () => {
    if (window._globalPollingTimer) {
      clearInterval(window._globalPollingTimer)
      window._globalPollingTimer = null
      console.log('⏹️ 停止全域輪詢機制:', {
        reason: '沒有留言需要監控',
        timestamp: new Date().toLocaleTimeString()
      })
    }
  }
  
  // 檢查所有監控中的留言狀態
  const checkMonitoredComments = async () => {
    if (monitoredComments.value.size === 0) {
      return
    }
    
    const startTime = Date.now()
    console.log(`🔍 開始輪詢檢查 ${monitoredComments.value.size} 個留言的狀態:`, {
      commentIds: Array.from(monitoredComments.value),
      timestamp: new Date().toLocaleTimeString()
    })
    
    let successCount = 0
    let errorCount = 0
    
    for (const commentId of monitoredComments.value) {
      try {
        await checkSingleComment(commentId)
        successCount++
      } catch (error) {
        errorCount++
        console.error(`❌ 檢查留言 ${commentId} 狀態失敗:`, error)
      }
    }
    
    const endTime = Date.now()
    const duration = endTime - startTime
    
    console.log(`📊 輪詢完成統計:`, {
      totalComments: monitoredComments.value.size,
      successCount,
      errorCount,
      duration: `${duration}ms`,
      timestamp: new Date().toLocaleTimeString()
    })
  }
  
  // 檢查單個留言狀態
  const checkSingleComment = async (commentId) => {
    try {
      console.log(`🔍 檢查留言 ${commentId} 狀態`)
      
      // 使用一般留言API
      const response = await commentAPI.getComment(commentId)
      const comment = response.data
      
      console.log(`📊 留言 ${commentId} 查詢成功:`, {
        commentId,
        isActive: comment.isActive,
        deleteStatus: comment.deleteStatus,
        reportable: comment.reportable,
        timestamp: new Date().toLocaleTimeString()
      })
      
      // ✅ 判斷是否留言已被封鎖或刪除
      const isDeleted = comment.isActive === false || comment.deleteStatus === 0
      
      if (isDeleted) {
        console.log(`✅ 留言 ${commentId} 已被處理（屏蔽或刪除），停止輪詢`)
        
        // 觸發狀態變化事件
        emitStatusChange(commentId, comment)
        
        // 停止監控
        stopMonitoring(commentId)
        
        // 🔔 Bonus：可以顯示提醒訊息
        // toast.info(`留言「${truncate(comment.content, 20)}...」已被管理員屏蔽`)
        
      } else {
        // 留言仍有效，繼續監控
        console.log(`📋 留言 ${commentId} 狀態正常，繼續監控`)
      }
      
    } catch (error) {
      console.error(`❌ 檢查留言 ${commentId} 狀態失敗:`, {
        commentId,
        error: {
          status: error.response?.status,
          statusText: error.response?.statusText,
          message: error.message,
          data: error.response?.data
        },
        timestamp: new Date().toLocaleTimeString()
      })
      
      // 💥 留言被後端刪除，直接移除
      if (error.response?.status === 404) {
        console.log(`🗑️ 留言 ${commentId} 不存在，停止監控`)
        stopMonitoring(commentId)
        emitStatusChange(commentId, { isActive: false, deleteStatus: 0 })
        // 清除錯誤計數
        commentErrorCount.value.delete(commentId)
      } else {
        // ⚠️ 其他錯誤處理：累積錯誤次數，超過上限才停止監控
        const currentErrorCount = commentErrorCount.value.get(commentId) || 0
        const newErrorCount = currentErrorCount + 1
        commentErrorCount.value.set(commentId, newErrorCount)
        
        console.warn(`⚠️ 留言 ${commentId} 錯誤次數: ${newErrorCount}/${MAX_ERROR_COUNT}`, {
          status: error.response?.status,
          message: error.message
        })
        
        if (newErrorCount >= MAX_ERROR_COUNT) {
          console.error(`❌ 留言 ${commentId} 錯誤次數過多，停止監控`)
          stopMonitoring(commentId)
          commentErrorCount.value.delete(commentId)
        }
      }
    }
  }
  
  // 保底監控機制（防止輪詢意外停止）
  const startHealthCheck = () => {
    if (window._globalHealthCheckTimer) {
      return // 已經在運行
    }
    
    console.log('🛡️ 啟動保底監控機制')
    
    window._globalHealthCheckTimer = setInterval(() => {
      // 檢查是否有監控中的留言但輪詢已停止
      if (!window._globalPollingTimer && monitoredComments.value.size > 0) {
        console.warn('❗輪詢意外停止，自動重啟')
        startPolling()
      }
      
      // 檢查延遲停止計時器是否異常
      if (window._globalDelayedStopTimer && monitoredComments.value.size > 0) {
        console.warn('❗延遲停止計時器異常，清除並繼續輪詢')
        clearTimeout(window._globalDelayedStopTimer)
        window._globalDelayedStopTimer = null
      }
    }, 10000) // 每 10 秒檢查一次
  }
  
  // 停止保底監控
  const stopHealthCheck = () => {
    if (window._globalHealthCheckTimer) {
      clearInterval(window._globalHealthCheckTimer)
      window._globalHealthCheckTimer = null
      console.log('🛡️ 停止保底監控機制')
    }
  }
  
  // 觸發狀態變化事件
  const emitStatusChange = (commentId, comment) => {
    // 使用自定義事件通知組件
    const event = new CustomEvent('commentStatusChanged', {
      detail: {
        commentId,
        comment
      }
    })
    window.dispatchEvent(event)
  }
  
  // 組件卸載時清理（改為更安全的清理方式）
  onUnmounted(() => {
    // 注意：不要直接停止全域輪詢，因為其他組件可能還在監控
    console.log('🧹 組件卸載，清理本地資源')
    
    // 只清理延遲停止計時器（如果是由當前組件設置的）
    if (window._globalDelayedStopTimer) {
      // 檢查是否真的沒有留言需要監控
      if (monitoredComments.value.size === 0) {
        clearTimeout(window._globalDelayedStopTimer)
        window._globalDelayedStopTimer = null
      }
    }
  })
  
  return {
    // 狀態
    monitoredComments: readonly(monitoredComments),
    commentErrorCount: readonly(commentErrorCount),
    
    // 方法
    startMonitoring,
    stopMonitoring,
    startPolling,
    stopPolling,
    startHealthCheck,
    stopHealthCheck,
    checkMonitoredComments
  }
} 