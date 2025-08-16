<template>
  <div class="reviews">
    <div class="d-flex justify-content-between mb-3">
      <h4 class="fw-bold">住客評價</h4>
      <div v-if="!showForm">
        <!-- 一般用戶可以新增主留言 -->
        <button v-if="canCreateMainComment" class="btn btn-outline-primary" @click="showForm = true">
          撰寫評價
        </button>
        <!-- 管理員和廠商顯示特殊提示 -->
        <div v-else class="d-flex align-items-center">
          <button class="btn btn-outline-secondary me-2" disabled>
            {{ userTypeText }}無法新增評價
          </button>
          <small class="text-muted">但可以回覆其他留言</small>
        </div>
      </div>
      <!-- 如果表單已顯示，但用戶是管理員或廠商，則隱藏表單 -->
      <div v-else-if="!canCreateMainComment" class="d-flex align-items-center">
        <button class="btn btn-outline-secondary me-2" disabled>
          {{ userTypeText }}無法新增評價
        </button>
        <button class="btn btn-sm btn-outline-danger" @click="showForm = false">
          關閉
        </button>
      </div>
    </div>

    <!-- 載入中提示 -->
    <!-- <div v-if="loading" class="text-center py-4">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">載入中...</span>
      </div>
      <p class="mt-2 text-muted">載入評論中...</p>
    </div> -->

    <!-- 初次載入 spinner -->
    <div v-if="initialLoading" class="text-center py-4">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">載入中...</span>
      </div>
      <p class="mt-2 text-muted">載入評論中...</p>
    </div>

    <!-- 錯誤提示 -->
    <div v-else-if="error" class="alert alert-danger" role="alert">
      <i class="bi bi-exclamation-triangle me-2"></i>
      {{ error }}
      <button class="btn btn-sm btn-outline-danger ms-2" @click="fetchComments">
        重新載入
      </button>
    </div>

    <!-- 評論表單 -->
    <CommentForm v-if="showForm" mode="main" :currentUserId="user_id" @submit="addMainComment"
      @cancel="showForm = false" />

    <!-- 評論列表 -->
    <div v-if="!loading && !error">
      <CommentList :comments="allComments" :currentUserId="user_id" @submit="addReply" @delete="deleteComment"
        @update="updateComment" />

      <!-- 無評論提示 -->
      <div v-if="allComments.length === 0" class="text-center py-4 text-muted">
        <i class="bi bi-chat-dots" style="font-size: 2rem;"></i>
        <p class="mt-2">尚無評論，成為第一個分享心得的人吧！</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import CommentForm from './CommentForm.vue'
import CommentList from './CommentList.vue'
import { commentAPI } from '@/services/api'
import { useCurrentUser } from '@/composables/useCurrentUser'
import { useCommentLikeStatus } from '@/composables/useCommentLikeStatus'
import { useCommentPermissions } from '@/composables/useCommentPermissions'
import { useCommentErrorHandler } from '@/composables/useCommentErrorHandler'

// 使用新的 composables
const { currentUser, isAdmin, isVendor, isAdminOrVendor } = useCurrentUser()
const { batchLoadLikeStatus, toggleLike, getLikeStatus } = useCommentLikeStatus()
const { canCreateMainComment, getUserTypeText } = useCommentPermissions()
const { handleLoadError, handleCreateError, handleUpdateError, handleDeleteError } = useCommentErrorHandler()

// 用戶資訊
const user_id = computed(() => currentUser.value?.userId)
const userType = computed(() => currentUser.value?.userType)

// 用戶類型文字
const userTypeText = computed(() => getUserTypeText.value)

// 接收外部傳入的評論目標（五選一）
const props = defineProps({
  target: {
    type: Object,
    required: true,
    validator: (value) => {
      // 支援兩種格式：
      // 1. { type: 'LODGING', id: 123 }
      // 2. { lodging_id: 123 } (舊格式，自動轉換)
      return (value.type && value.id) || value.lodging_id
    }
  }
})

// 控制撰寫表單顯示
const showForm = ref(false)

// 所有留言資料
const allComments = ref([])

// 載入狀態
// const loading = ref(false)
const initialLoading = ref(false)  // 初次載入評論
const refreshing = ref(false)      // 新增／編輯／刪除後刷新

const error = ref(null)

// 組件載入時執行
onMounted(async () => {
  console.log('🚀 AllComment 組件載入')

  // 載入評論
  // await fetchComments()
  fetchComments(true)  // 首次進入頁面時使用 initialLoading
})

// 共用評論目標解析邏輯
function getCommentTarget() {
  const { type, id, lodging_id } = props.target

  // 支援兩種格式
  if (type && id) {
    // 新格式：{ type: 'LODGING', id: 123 }
    return { targetType: type, targetId: id }
  } else if (lodging_id) {
    // 舊格式：{ lodging_id: 123 }，自動轉換為新格式
    // 注意：lodging_id 在票券評論中實際上是 ticket_id
    return { targetType: 'TICKET', targetId: lodging_id }
  }

  return null
}

// 取得留言列表
// async function fetchComments() {
//   const target = getCommentTarget()
//   if (!target) {
//     error.value = '未指定評論目標'
//     return
//   }

//   loading.value = true
//   error.value = null

//   try {
//     const params = {
//       targetType: target.targetType,
//       targetId: target.targetId
//     }

//     console.log('🔍 發送 API 請求:', params)
//     const res = await commentAPI.getComments(params)

//     console.log('📦 API 回傳資料:', res.data)
//     console.log('📊 資料類型:', typeof res.data)
//     console.log('📋 是否為陣列:', Array.isArray(res.data))

//     if (res.data && Array.isArray(res.data)) {
//       allComments.value = res.data
//       console.log('✅ 成功載入評論:', allComments.value.length, '筆')

//       // 檢查是否有回覆評論
//       const replies = res.data.filter(c => c.parentCommentId)
//       console.log('🔍 回覆評論檢查:', replies.length, '筆回覆')
//       if (replies.length > 0) {
//         console.log('🔍 回覆評論詳情:', replies.map(r => `ID:${r.id}->父ID:${r.parentCommentId}`))
//       }

//       // 詳細檢查所有評論的 parentCommentId 欄位
//       console.log('🔍 所有評論的 parentCommentId 檢查:')
//       res.data.forEach((comment, index) => {
//         console.log(`  評論 ${index + 1}: ID=${comment.id}, parentCommentId=${comment.parentCommentId}, 類型=${typeof comment.parentCommentId}`)
//       })

//       // 檢查最新建立的評論
//       const latestComment = res.data[res.data.length - 1]
//       console.log('🔍 最新評論:', latestComment)

//       // 載入所有評論的按讚狀態
//       await loadAllLikeStatusOptimized()
//     } else {
//       allComments.value = []
//       console.log('⚠️ API 回傳非陣列資料，設為空陣列')
//     }
//   } catch (err) {
//     console.error('❌ 載入留言失敗:', err)
//     error.value = err.response?.data?.message || '載入評論失敗，請稍後再試'
//   } finally {
//     loading.value = false
//   }
// }
async function fetchComments(useInitialLoading = false) {
  const target = getCommentTarget()
  if (!target) {
    error.value = '未指定評論目標'
    return
  }

  if (useInitialLoading) {
    initialLoading.value = true
  } else {
    refreshing.value = true
  }
  error.value = null

  try {
    // 使用新的 API 參數，包含按讚狀態
    const params = {
      targetType: target.targetType,
      targetId: target.targetId,
      includeLikeStatus: true,
      userId: user_id.value
    }

    console.log('🔄 使用優化後的評論查詢:', params)
    const res = await commentAPI.getComments(params)
    
    if (res.data && Array.isArray(res.data)) {
      allComments.value = res.data
      console.log('✅ 成功載入評論:', allComments.value.length, '筆')
      
      // 檢查是否有回覆評論
      const replies = res.data.filter(c => c.parentCommentId)
      console.log('🔍 回覆評論檢查:', replies.length, '筆回覆')
      
      // 如果 API 沒有包含按讚狀態，則使用批量查詢
      if (!res.data[0]?.hasOwnProperty('likedByCurrentUser')) {
        console.log('🔄 API 未包含按讚狀態，使用批量查詢')
        const commentIds = res.data.map(comment => comment.id)
        await batchLoadLikeStatus(commentIds, user_id.value)
      } else {
        console.log('✅ API 已包含按讚狀態，無需額外查詢')
      }
    } else {
      allComments.value = []
      console.log('⚠️ API 回傳非陣列資料，設為空陣列')
    }
  } catch (err) {
    console.error('❌ 載入留言失敗:', err)
    const errorResult = handleLoadError(err)
    error.value = errorResult.message
  } finally {
    if (useInitialLoading) {
      initialLoading.value = false
    } else {
      refreshing.value = false
    }
  }
}


// 按讚/取消按讚功能（使用新的 composable）
async function handleLike(commentId) {
  const comment = allComments.value.find(c => c.id === commentId)
  if (!comment) return

  try {
    console.log(`🔄 發送按讚請求: 評論 ${commentId}`)
    await toggleLike(commentId, user_id.value)
    console.log('✅ 按讚操作成功')
  } catch (err) {
    console.error('❌ 按讚操作失敗:', err)
    // 錯誤處理已在 composable 中處理
  }
}

// 若 props.target 改變也會自動重新 fetch
watch(() => props.target, fetchComments, { deep: true })

// 監聽用戶類型變化，如果是管理員或廠商則隱藏表單
watch(() => isAdminOrVendor.value, (newValue) => {
  if (newValue && showForm.value) {
    showForm.value = false
    console.log('🔒 管理員或廠商無法新增主留言，已隱藏表單')
  }
})

// 發送留言（新增 / 回覆）
async function createComment(payload) {
  const target = getCommentTarget()

  if (!target) {
    Swal.fire({
      icon: 'warning',
      title: '錯誤',
      text: '未指定評論目標',
      confirmButtonText: '確定'
    })
    return
  }

  const { targetType, targetId } = target

  try {
    console.log('🔄 開始創建留言流程...')

    // 準備留言資料
    const commentData = {
      userId: user_id.value,
      content: payload.content,
      rating: payload.rating,
      parentCommentId: payload.parentCommentId,
      targetType: targetType.toUpperCase(), // 確保大寫格式
      targetId: targetId // 添加目標ID
    }

    // 準備圖片檔案 - 只處理新圖片
    let files = []
    if (payload.images && payload.images.length > 0) {
      console.log('🖼️ 準備圖片檔案...')

    for (const img of payload.images) {
      const isDeleted = img.isDeleted || img.isActive === false

      // 只處理新上傳且未被刪除的圖片
      if (img.isNew && img.file && !isDeleted) {
        console.log('🆕 新圖加入 files:', img)
        files.push(img.file)
      } else {
        console.log('⏭️ 跳過：舊圖或已刪除圖:', img)
      }
    }
      
      console.log('✅ 圖片檔案準備完成:', files.length, '張')
    }

    // 使用新版API：一步驟創建留言並上傳圖片
    console.log('📤 發送留言創建請求:', {
      commentData: commentData,
      filesCount: files.length
    })

    const response = await commentAPI.createCommentWithFiles(commentData, files)
    console.log('✅ 留言創建成功:', response.data)

    // 重新載入評論列表
    await fetchComments()
  } catch (err) {
    console.error('❌ 創建留言失敗:', err)
    handleCreateError(err)
    throw err
  }
}

// 刪除評論
async function deleteComment(commentId) {
  try {
    await commentAPI.deleteComment(commentId)

    // 在本地標記評論為已刪除，而不是重新載入
    const commentToDelete = allComments.value.find(c => c.id === commentId)
    if (commentToDelete) {
      // 設定為使用者刪除狀態
      commentToDelete.deleteStatus = 0
      commentToDelete.isActive = false
      commentToDelete.content = '作者刪除留言'
      commentToDelete.userName = '作者刪除留言'
      commentToDelete.rating = null
      commentToDelete.likeCount = 0
      commentToDelete.likedByCurrentUser = false
      commentToDelete.images = []
      commentToDelete.editable = false
      commentToDelete.replyable = false
      commentToDelete.reportable = false

      console.log('✅ 評論已標記為已刪除:', commentToDelete)
    } else {
      console.warn('⚠️ 找不到要刪除的評論:', commentId)
    }

    // Swal.fire({
    //   icon: 'success',
    //   title: '刪除成功',
    //   text: '評論已成功刪除',
    //   timer: 2000,
    //   showConfirmButton: false
    // })
  } catch (err) {
    console.error('刪除評論失敗:', err)
    // 錯誤處理已在 API 攔截器中處理
  }
}

// 更新評論
async function updateComment(commentId, payload) {
  try {
    console.log('🔄 開始更新留言流程...')

    // 準備留言資料
    const commentData = {
      content: payload.content,
      rating: payload.rating
    }

    // 準備圖片檔案 - 只處理新上傳的圖片
    let files = []
    if (payload.images && payload.images.length > 0) {
      console.log('🖼️ 準備圖片檔案...')
      
      for (const img of payload.images) {
        // 只處理新上傳的圖片
        if (img.isNew && img.file) {
          console.log('🔄 檢測到新上傳的檔案，直接使用...')
          files.push(img.file)
        } else {
          // 舊圖片（已存在於伺服器）不需要轉換檔案
          console.log('🔄 跳過舊圖片（已存在於伺服器）:', {
            id: img.id,
            imageId: img.imageId,
            isNew: img.isNew
          });
        }
      }
      
      console.log('✅ 圖片檔案準備完成:', files.length, '張')
    }

    // 如果有新圖片，使用新版API更新留言和圖片
    // if (files.length > 0) {
    //   console.log('📤 發送留言更新請求（包含新圖片）:', {
    //     commentData: commentData,
    //     filesCount: files.length
    //   })

    //   const response = await commentAPI.updateCommentWithFiles(commentId, commentData, files)
    //   console.log('✅ 留言更新成功:', response.data)
    // } else {
    //   // 如果沒有新圖片，但有舊圖片需要保留，也使用新版API
    //   const hasExistingImages = payload.images && payload.images.some(img => !img.isNew && img.isActive !== false);
      
    //   if (hasExistingImages) {
    //     console.log('📤 發送留言更新請求（保留舊圖片）:', {
    //       commentData: commentData,
    //       existingImagesCount: payload.images.filter(img => !img.isNew && img.isActive !== false).length
    //     })
        
    //     // ✅ 修正：當沒有新檔案時，使用舊版API只更新留言內容
    //     // 因為後端可能無法處理空的 files 欄位
    //     console.log('📤 發送留言更新請求（僅內容，保留舊圖片）:', commentData)
    //     await commentAPI.updateComment(commentId, commentData)
    //     console.log('✅ 留言內容更新成功')
    //   } else {
    //     // 如果完全沒有圖片，使用舊版API只更新留言內容
    //     console.log('📤 發送留言更新請求（僅內容）:', commentData)
    //     await commentAPI.updateComment(commentId, commentData)
    //     console.log('✅ 留言內容更新成功')
    //   }
    // }

    const hasActiveOldImages = payload.images?.some(img => !img.isNew && img.isActive !== false)
    const hasNewFiles = files.length > 0
    if (hasNewFiles) {
      // ✅ 有新圖：用含 files 的 API 上傳新圖＋更新留言內容
      console.log('📤 發送留言更新請求（包含新圖片）:', { commentData, filesCount: files.length })
      await commentAPI.updateCommentWithFiles(commentId, commentData, files)
      console.log('✅ 留言更新成功')
    } else if (hasActiveOldImages) {
      // ✅ 沒有新圖，但有舊圖要保留 → 更新文字即可
      console.log('📤 發送留言更新請求（僅內容，保留舊圖）:', commentData)
      await commentAPI.updateComment(commentId, commentData)
      console.log('✅ 留言內容更新成功')
    } else {
      // ✅ 沒有新圖、也沒有要保留的舊圖 → 只更新文字（不處理圖片）
      console.log('📤 發送留言更新請求（僅內容，無圖片）:', commentData)
      await commentAPI.updateComment(commentId, commentData)
      console.log('✅ 留言內容更新成功')
    }

    // 成功提示
    // Swal.fire({
    //   icon: 'success',
    //   title: '更新成功',
    //   text: '評論已成功更新',
    //   timer: 2000,
    //   showConfirmButton: false
    // })

    await fetchComments()
  } catch (err) {
    console.error('❌ 留言更新失敗:', err)
    console.error('❌ 錯誤詳情:', err.response?.data)

    // 顯示詳細錯誤訊息
    const errorMessage = err.response?.data?.message || err.message || '未知錯誤'
    Swal.fire({
      icon: 'error',
      title: '評論更新失敗',
      text: `錯誤詳情: ${errorMessage}`,
      confirmButtonText: '確定'
    })
  }
}

// 新增留言
function addMainComment(payload) {
  console.log('新增留言 payload:', payload)
  createComment(payload)
  showForm.value = false
}

// 新增回覆留言
function addReply(payload) {
  console.log('收到回覆留言 payload:', payload)
  createComment(payload)
}
</script>

<style scoped>
.reviews {
  max-width: 100%;
}

.spinner-border {
  width: 2rem;
  height: 2rem;
}

.alert {
  border-radius: 8px;
}

.bi {
  vertical-align: middle;
}
</style>
