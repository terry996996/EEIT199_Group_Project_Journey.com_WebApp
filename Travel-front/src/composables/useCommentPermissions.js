import { computed } from 'vue'
import { useCurrentUser } from './useCurrentUser'

export function useCommentPermissions() {
  const { currentUser, isAdmin, isVendor, isAdminOrVendor } = useCurrentUser()
  
  // 檢查是否可以新增主評論
  const canCreateMainComment = computed(() => {
    // 管理員和廠商不能新增主評論
    return !isAdminOrVendor.value
  })
  
  // 檢查是否可以回覆評論
  const canReply = computed(() => {
    // 所有用戶都可以回覆
    return true
  })
  
  // 檢查是否可以按讚
  const canLike = computed(() => {
    // 管理員不能按讚
    return !isAdmin.value
  })
  
  // 檢查是否可以檢舉
  const canReport = computed(() => {
    // 管理員不能檢舉
    return !isAdmin.value
  })
  
  // 檢查是否可以編輯評論
  const canEdit = (comment) => {
    if (!comment || !currentUser.value) return false
    
    // 評論作者可以編輯
    if (comment.author?.id === currentUser.value.userId) {
      return true
    }
    
    // 管理員可以編輯任何評論
    if (isAdmin.value) {
      return true
    }
    
    return false
  }
  
  // 檢查是否可以刪除評論
  const canDelete = (comment) => {
    if (!comment || !currentUser.value) return false
    
    // 評論作者可以刪除
    if (comment.author?.id === currentUser.value.userId) {
      return true
    }
    
    // 管理員可以刪除任何評論
    if (isAdmin.value) {
      return true
    }
    
    return false
  }
  
  // 檢查是否可以管理評論（管理員專用）
  const canManageComments = computed(() => {
    return isAdmin.value
  })
  
  // 檢查是否可以查看檢舉列表
  const canViewReports = computed(() => {
    return isAdmin.value
  })
  
  // 檢查是否可以處理檢舉
  const canHandleReports = computed(() => {
    return isAdmin.value
  })
  
  // 取得用戶類型文字
  const getUserTypeText = computed(() => {
    if (isAdmin.value) return '管理員'
    if (isVendor.value) return '廠商'
    return '一般用戶'
  })
  
  // 檢查是否為一般用戶
  const isRegularUser = computed(() => {
    return !isAdminOrVendor.value
  })
  
  // 檢查評論是否為當前用戶所發
  const isOwnComment = (comment) => {
    if (!comment || !currentUser.value) return false
    return comment.author?.id === currentUser.value.userId
  }
  
  // 檢查評論是否為一般用戶所發
  const isUserComment = (comment) => {
    if (!comment || !comment.author) return false
    // 假設一般用戶的 userType 為 'USER' 或 null/undefined
    return !comment.author.userType || comment.author.userType === 'USER'
  }
  
  // 檢查評論是否為管理員所發
  const isAdminComment = (comment) => {
    if (!comment || !comment.author) return false
    return comment.author.userType === 'ADMIN'
  }
  
  // 檢查評論是否為廠商所發
  const isVendorComment = (comment) => {
    if (!comment || !comment.author) return false
    return comment.author.userType === 'VENDOR'
  }
  
  // 取得評論作者類型文字
  const getCommentAuthorType = (comment) => {
    if (isAdminComment(comment)) return '管理員'
    if (isVendorComment(comment)) return '廠商'
    return '一般用戶'
  }
  
  // 檢查是否可以顯示驗證標籤
  const canShowVerificationBadge = (comment) => {
    // 只有一般用戶的評論才顯示驗證標籤
    return isUserComment(comment)
  }
  
  // 檢查是否可以顯示管理員標籤
  const canShowAdminBadge = (comment) => {
    // 管理員評論顯示管理員標籤
    return isAdminComment(comment)
  }
  
  // 檢查是否可以顯示廠商標籤
  const canShowVendorBadge = (comment) => {
    // 廠商評論顯示廠商標籤
    return isVendorComment(comment)
  }
  
  // 取得權限摘要
  const getPermissionSummary = computed(() => {
    return {
      userType: getUserTypeText.value,
      canCreateMainComment: canCreateMainComment.value,
      canReply: canReply.value,
      canLike: canLike.value,
      canReport: canReport.value,
      canManageComments: canManageComments.value,
      canViewReports: canViewReports.value,
      canHandleReports: canHandleReports.value,
      isRegularUser: isRegularUser.value,
      isAdmin: isAdmin.value,
      isVendor: isVendor.value
    }
  })
  
  return {
    // 基本權限檢查
    canCreateMainComment,
    canReply,
    canLike,
    canReport,
    canEdit,
    canDelete,
    
    // 管理權限
    canManageComments,
    canViewReports,
    canHandleReports,
    
    // 用戶類型檢查
    getUserTypeText,
    isRegularUser,
    isOwnComment,
    isUserComment,
    isAdminComment,
    isVendorComment,
    getCommentAuthorType,
    
    // 顯示權限
    canShowVerificationBadge,
    canShowAdminBadge,
    canShowVendorBadge,
    
    // 摘要
    getPermissionSummary,
    
    // 原始狀態（備用）
    currentUser,
    isAdmin,
    isVendor,
    isAdminOrVendor
  }
} 