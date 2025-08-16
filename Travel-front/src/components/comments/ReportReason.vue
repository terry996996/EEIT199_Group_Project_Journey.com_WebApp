<template>
  <div>
    <select v-model="selectedReasonId" class="form-select mb-2">
      <option disabled value="">請選擇檢舉原因</option>
      <option v-for="reason in reasons" :key="reason.id" :value="reason.id">
        {{ reason.label }}
      </option>
    </select>
    <div class="text-end">
      <button class="btn btn-sm btn-secondary me-2" @click="closeModal">取消</button>
      <button class="btn btn-sm btn-danger" @click="submitReport">送出</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { commentAPI, apiClient } from '@/services/api'
import Swal from 'sweetalert2';
import { useCurrentUser } from '@/composables/useCurrentUser'
import { useCommentReportMonitor } from '@/composables/useCommentReportMonitor'


const props = defineProps({
  commentId: {
    type: Number,
    required: true
  },
  currentUserId: {
    type: Number,
    required: true
  }
});
const emit = defineEmits(['close', 'reported']);

const selectedReasonId = ref('');
const reasons = ref([]);

// 載入檢舉原因
onMounted(async () => {
  try {
    // 這裡應該從後端 API 取得檢舉原因列表
    const res = await apiClient.get('/api/options/comment-reasons');
    console.log('reasons', res.data);
    reasons.value = res.data;

  } catch (err) {
    console.error('載入檢舉原因失敗:', err)
  }
});

// 取得共用用戶資訊與權限
const { currentUser, isAdmin, isVendor, isAdminOrVendor } = useCurrentUser()

// 使用檢舉狀態監控
const { startMonitoring } = useCommentReportMonitor()

function closeModal() {
  selectedReasonId.value = '';
  emit('close');
}

async function submitReport() {
  // 1. 驗證是否選擇原因
  if (!selectedReasonId.value) {
    Swal.fire('請選擇檢舉原因', '', 'warning')
    return
  }

  // 2. 組 payload（按照 API 規格）
  const payload = {
    commentId: Number(props.commentId),
    reasonIds: [Number(selectedReasonId.value)]
    // 注意：不包含 userId，後端會從 Principal 中獲取
  }
  
  // 檢查 payload 格式
  console.log('📦 檢舉 payload 詳情:', {
    commentId: payload.commentId,
    commentIdType: typeof payload.commentId,
    reasonIds: payload.reasonIds,
    reasonIdsType: typeof payload.reasonIds[0],
    reasonIdsLength: payload.reasonIds.length
  })

  try {
    // 檢查認證狀態
    let token = localStorage.getItem('token')
    
    // 使用統一的用戶資訊，而不是 props.currentUserId
    const actualUserId = currentUser.value.userId
    const actualUserType = currentUser.value.userType
    const actualUserName = currentUser.value.userName
    
    console.log('🔍 檢舉用戶資訊:', {
      actualUserId,
      actualUserType,
      actualUserName,
      propsCurrentUserId: props.currentUserId,
      isAdmin: isAdmin.value,
      isVendor: isVendor.value
    })
    
    // 模擬登入：如果沒有 token，創建一個模擬 token
    if (!token) {
      console.log('🔧 模擬登入：創建模擬 token')
      token = 'mock-token-' + Date.now()
      localStorage.setItem('token', token)
      
      // 模擬用戶信息
      const mockUserInfo = {
        id: actualUserId || props.currentUserId,
        username: actualUserName || '測試用戶',
        userType: actualUserType || 'USER'
      }
      localStorage.setItem('userInfo', JSON.stringify(mockUserInfo))
      
      // 顯示開發模式提示
      console.log('🚧 開發模式：使用模擬認證')
    }
    
    console.log('🔍 準備發送檢舉請求:', {
      url: '/api/comment-reports',
      payload: payload,
      actualUserId: actualUserId,
      actualUserType: actualUserType,
      actualUserName: actualUserName,
      token: token ? '存在' : '不存在'
    })
    
    // 檢查是否為管理員
    if (isAdmin.value) {
      console.log('🚫 管理員嘗試檢舉，被前端阻止')
      Swal.fire({
        icon: 'warning',
        title: '權限不足',
        text: '管理員不能檢舉留言',
        confirmButtonText: '確定'
      })
      return
    }
    
    console.log('✅ 用戶權限檢查通過，允許檢舉')
    
    // 3. 送出
    console.log('🚀 發送 API 請求...')
     
    // 測試認證狀態
    console.log('🔐 認證測試：')
    console.log('  - Token:', localStorage.getItem('token'))
    console.log('  - Actual User ID:', actualUserId)
    console.log('  - Actual User Type:', actualUserType)
    console.log('  - Props Current User ID:', props.currentUserId)
    
    // const response = await apiClient.post('/api/comment-reports', payload)
    // 250711 實作身分驗證
    const response = await apiClient.post('/api/comment-reports', payload, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    console.log('✅ 檢舉成功:', response.data)

    // 4. 成功提示與事件
    emit('reported', props.commentId)
    
    // 5. 開始監控留言狀態
    startMonitoring(props.commentId)
    console.log(`🔄 開始監控留言 ${props.commentId} 的狀態變化`)
    
    Swal.fire({
      icon: 'success',
      title: '檢舉成功！',
      text: '我們將儘速處理，您將即時看到處理結果。',
      confirmButtonText: '確定'
    })
    closeModal()
  } catch (err) {
    console.error('❌ 檢舉錯誤：', err)
    console.error('❌ 錯誤詳情：', {
      status: err.response?.status,
      statusText: err.response?.statusText,
      data: err.response?.data,
      message: err.response?.data?.message,
      headers: err.response?.headers
    })
    
    // 顯示具體錯誤信息給用戶
    const errorMessage = err.response?.data?.message || '檢舉失敗，請稍後再試'
    
      // 判斷是否為重複檢舉
    const isDuplicateReport =
      errorMessage.includes('您已檢舉過此留言') ||
      errorMessage.includes('已檢舉') ||
      errorMessage.includes('重複');

    if (isDuplicateReport) {
      Swal.fire({
        icon: 'warning',
        title: '您已經檢舉過這則留言',
        text: errorMessage,
        confirmButtonText: '確定'
      }).then(() => {
        emit('close');
      });
    } else {
      Swal.fire({
        icon: 'error',
        title: '檢舉失敗',
        text: errorMessage,
        confirmButtonText: '確定'
      });
    }

    if (err.response?.status === 403) {
      console.log('🔍 403 錯誤分析：')
      console.log('  - Actual User ID:', actualUserId)
      console.log('  - Actual User Type:', actualUserType)
      console.log('  - Props Current User ID:', props.currentUserId)
      console.log('  - Token:', localStorage.getItem('token'))
      console.log('  - 請求 URL:', '/api/comment-reports')
      console.log('  - 請求 Payload:', payload)
      console.log('  - 後端錯誤數據:', err.response?.data)
      console.log('  - 後端錯誤消息:', err.response?.data?.message)
      console.log('  - 後端錯誤詳情:', err.response?.data?.details)
      
      // 檢查是否為管理員導致的 403
      if (isAdmin.value) {
        console.log('🚫 403 錯誤原因：用戶是管理員，後端拒絕檢舉')
      } else {
        console.log('❓ 403 錯誤原因：可能是認證問題或其他權限限制')
        
        // 建議後端檢查項目
        console.log('🔧 後端需要檢查：')
        console.log('  1. 認證中間件是否正確配置')
        console.log('  2. Principal 是否正確解析')
        console.log('  3. 用戶權限檢查邏輯')
        console.log('  4. 端點是否正確映射')
      }
    }
    
    Swal.fire({
      icon: 'error',
      title: '檢舉失敗',
      text: errorMessage,
      confirmButtonText: '確定'
    })
  }
}
</script>

<style scoped>
.modal {
  z-index: 1050;
  z-index: 1050;
}
</style>