import Swal from 'sweetalert2'

export function useCommentErrorHandler() {
  // 統一的錯誤處理
  const handleError = (error, action = '操作', showAlert = true) => {
    console.error(`❌ 評論${action}失敗:`, error)
    
    // 解析錯誤訊息
    const message = getErrorMessage(error, action)
    
    // 顯示錯誤提示
    if (showAlert) {
      showErrorAlert(message, action)
    }
    
    return {
      message,
      originalError: error,
      action
    }
  }
  
  // 解析錯誤訊息
  const getErrorMessage = (error, action) => {
    // 網路錯誤
    if (error.request) {
      return '網路連線失敗，請檢查網路設定'
    }
    
    // HTTP 錯誤
    if (error.response) {
      const status = error.response.status
      const data = error.response.data
      
      // 根據狀態碼返回對應訊息
      switch (status) {
        case 400:
          return data?.message || '請求格式錯誤'
        case 401:
          return '請先登入後再操作'
        case 403:
          return '權限不足，無法執行此操作'
        case 404:
          return '評論不存在或已被刪除'
        case 409:
          return data?.message || '操作衝突，請稍後再試'
        case 422:
          return data?.message || '資料驗證失敗'
        case 429:
          return '操作過於頻繁，請稍後再試'
        case 500:
          return '伺服器錯誤，請稍後再試'
        default:
          return data?.message || `評論${action}失敗`
      }
    }
    
    // 其他錯誤
    return error.message || `評論${action}失敗，請稍後再試`
  }
  
  // 顯示錯誤提示
  const showErrorAlert = (message, action) => {
    Swal.fire({
      icon: 'error',
      title: '操作失敗',
      text: message,
      confirmButtonText: '確定',
      timer: action === '載入' ? 3000 : undefined,
      timerProgressBar: action === '載入'
    })
  }
  
  // 顯示成功提示
  const showSuccessAlert = (message, action) => {
    Swal.fire({
      icon: 'success',
      title: '操作成功',
      text: message,
      timer: 2000,
      showConfirmButton: false
    })
  }
  
  // 顯示確認對話框
  const showConfirmDialog = (title, text, confirmText = '確定', cancelText = '取消') => {
    return Swal.fire({
      title,
      text,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: confirmText,
      cancelButtonText: cancelText,
      reverseButtons: true
    })
  }
  
  // 特定操作的錯誤處理
  
  // 載入評論錯誤
  const handleLoadError = (error) => {
    return handleError(error, '載入', true)
  }
  
  // 新增評論錯誤
  const handleCreateError = (error) => {
    return handleError(error, '新增', true)
  }
  
  // 更新評論錯誤
  const handleUpdateError = (error) => {
    return handleError(error, '更新', true)
  }
  
  // 刪除評論錯誤
  const handleDeleteError = (error) => {
    return handleError(error, '刪除', true)
  }
  
  // 按讚錯誤
  const handleLikeError = (error) => {
    return handleError(error, '按讚', true)
  }
  
  // 檢舉錯誤
  const handleReportError = (error) => {
    return handleError(error, '檢舉', true)
  }
  
  // 圖片上傳錯誤
  const handleImageError = (error) => {
    return handleError(error, '圖片上傳', true)
  }
  
  // 權限錯誤
  const handlePermissionError = (action) => {
    const message = `權限不足，無法${action}`
    showErrorAlert(message, action)
    return { message, action }
  }
  
  // 網路連線錯誤
  const handleNetworkError = () => {
    const message = '網路連線失敗，請檢查網路設定'
    showErrorAlert(message, '網路連線')
    return { message, action: '網路連線' }
  }
  
  // 伺服器錯誤
  const handleServerError = (error) => {
    return handleError(error, '伺服器', true)
  }
  
  // 驗證錯誤
  const handleValidationError = (errors) => {
    const message = Array.isArray(errors) 
      ? errors.join('\n') 
      : '資料驗證失敗，請檢查輸入內容'
    
    showErrorAlert(message, '驗證')
    return { message, action: '驗證' }
  }
  
  // 樂觀更新錯誤處理
  const handleOptimisticUpdateError = (error, rollback) => {
    // 執行回滾
    if (rollback && typeof rollback === 'function') {
      try {
        rollback()
        console.log('🔄 樂觀更新已回滾')
      } catch (rollbackError) {
        console.error('❌ 回滾失敗:', rollbackError)
      }
    }
    
    return handleError(error, '操作', true)
  }
  
  // 批量操作錯誤處理
  const handleBatchError = (error, successCount, totalCount) => {
    const message = `部分操作失敗 (${successCount}/${totalCount} 成功)`
    showErrorAlert(message, '批量操作')
    return { message, successCount, totalCount, originalError: error }
  }
  
  // 錯誤重試機制
  const retryOperation = async (operation, maxRetries = 3, delay = 1000) => {
    let lastError
    
    for (let attempt = 1; attempt <= maxRetries; attempt++) {
      try {
        return await operation()
      } catch (error) {
        lastError = error
        console.warn(`⚠️ 操作失敗，第 ${attempt} 次重試:`, error)
        
        if (attempt < maxRetries) {
          await new Promise(resolve => setTimeout(resolve, delay * attempt))
        }
      }
    }
    
    throw lastError
  }
  
  return {
    // 基本錯誤處理
    handleError,
    getErrorMessage,
    showErrorAlert,
    showSuccessAlert,
    showConfirmDialog,
    
    // 特定操作錯誤處理
    handleLoadError,
    handleCreateError,
    handleUpdateError,
    handleDeleteError,
    handleLikeError,
    handleReportError,
    handleImageError,
    
    // 特殊錯誤處理
    handlePermissionError,
    handleNetworkError,
    handleServerError,
    handleValidationError,
    handleOptimisticUpdateError,
    handleBatchError,
    
    // 重試機制
    retryOperation
  }
} 