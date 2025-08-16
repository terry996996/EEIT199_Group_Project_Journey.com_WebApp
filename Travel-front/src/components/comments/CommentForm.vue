<template>
  <div class="comment-form">
    <!-- DEBUG 區塊：顯示權限判斷相關值 -->
    <!-- <div class="mb-2 p-2 bg-light border rounded">
      <small>
        <b>DEBUG</b>｜isAdmin: {{ isAdmin }} ｜isVendor: {{ isVendor }} ｜isAdminOrVendor: {{ isAdminOrVendor }} ｜currentUser: {{ currentUser }}
      </small>
    </div> -->

    <!-- 權限檢查提示 -->
    <div v-if="showPermissionWarning" class="alert alert-warning mb-3" role="alert">
      <i class="bi bi-exclamation-triangle me-2"></i>
      <strong>權限限制：</strong>{{ permissionWarningMessage }}
    </div>

    <div v-if="mode === 'main' || mode === 'edit'" class="mb-2">
      <label class="form-label">評分</label>
      <div class="rating-input">
        <i v-for="n in 5" :key="n" class="bi" :class="starIcon(n)" @click="setRating(n)"
          style="cursor: pointer; color: #ffc107; font-size: 1.4rem"></i>
        <span class="ms-2">{{ rating }}/5</span>
      </div>
    </div>

    <div v-if="props.replyPrefix" class="mb-1 text-muted">
      {{ props.replyPrefix }}
    </div>

    <div class="mb-2">
      <textarea class="form-control" v-model="content" :rows="mode === 'main' ? 3 : 2"
        :placeholder="mode === 'main' ? '請輸入你的心得...' : '輸入回覆內容...'"
        :disabled="isDisabled"></textarea>
    </div>

    <div class="mb-2">
      <label class="form-label">上傳圖片</label>
      <ImageUploader
        :key="mode"
        type="COMMENT"
        :disabled="isDisabled"
        :images="deepCloneImages(localImages)"
        :comment-id="commentId"
        :auto-sync-order="mode === 'edit'"
        @update:images="handleImagesChange"
        @error="handleImageError"
        @orderUpdated="handleOrderUpdated"
      />
    </div>

    <div class="text-end">
      <button class="btn btn-sm btn-primary" @click="submit" :disabled="isDisabled">
        {{ submitButtonText }}
      </button>
      <button class="btn btn-sm btn-outline-secondary me-2" @click="$emit('cancel')">取消</button>
    </div>
  </div>

</template>

<script setup>
import { ref, watch, computed, nextTick, onMounted, onUnmounted } from 'vue';
import Swal from 'sweetalert2'
import ImageUploader from '@/components/common/ImageUploader.vue'
import { useCurrentUser } from '@/composables/useCurrentUser'

// props / emits 定義
const props = defineProps({
  mode: {
    type: String,
    required: true,
    validator: (value) => ['main', 'reply', 'edit'].includes(value)
  },
  parentId: {
    type: Number,
    default: null
  },
  initialContent: {
    type: String,
    default: ''
  },
  initialRating: {
    type: Number,
    default: 5
  },
  initialImages: {
    type: Array,
    default: () => []
  },
  replyPrefix: {
    type: String,
    default: ''
  },
  commentId: {
    type: Number,
    default: null
  }
});

const emit = defineEmits(['submit', 'cancel', 'images-changed']);

const content = ref(props.initialContent || ''); // 文字內容
const rating = ref(props.initialRating ?? 5);// 星等 (預設 5)
const localImages = ref([]); // 本地圖片狀態，避免直接修改 props



// 安全的圖片深拷貝函數 - 避免遞迴巢狀結構
function deepCloneImages(images) {
  try {
    if (!images || !Array.isArray(images)) {
      return [];
    }
    
    return images.map((img, index) => {
      try {
        if (!img || typeof img !== 'object') {
          return img;
        }
        
        // 排除無法序列化的屬性
        const { file, ...safeImg } = img;
        
        // 創建安全的拷貝，只保留必要欄位
        const clonedImg = {
          id: safeImg.id || safeImg.imageId || `temp-${index}`,
          name: safeImg.name || `圖片 ${index + 1}`,
          previewUrl: safeImg.previewUrl || safeImg.commentImageUrl || safeImg.imageUrl,
          size: safeImg.size || 0,
          imageId: safeImg.imageId,
          commentImageUrl: safeImg.commentImageUrl,
          imageUrl: safeImg.imageUrl,
          isActive: safeImg.isActive !== undefined ? safeImg.isActive : true,
          isDeleted: safeImg.isDeleted || false,
          sortOrder: safeImg.sortOrder || index,
          mimeType: safeImg.mimeType,
          processing: safeImg.processing || false,
          error: safeImg.error || null,
          progress: safeImg.progress || 100,
          deleteStatus: safeImg.deleteStatus,
          isNew: safeImg.isNew || false
        };
        
        // 如果有 file 屬性，保留引用（不進行深拷貝）
        if (file) {
          clonedImg.file = file;
        }
        
        // 如果有 originalImg 屬性，只保留必要欄位避免遞迴
        if (safeImg.originalImg && typeof safeImg.originalImg === 'object') {
          const { 
            id, name, previewUrl, size, imageId, commentImageUrl, 
            imageUrl, isActive, isDeleted, sortOrder, mimeType, 
            processing, error, progress, deleteStatus
          } = safeImg.originalImg;
          
          clonedImg.originalImg = {
            id, name, previewUrl, size, imageId, commentImageUrl,
            imageUrl, isActive, isDeleted, sortOrder, mimeType,
            processing, error, progress, deleteStatus
          };
        }
        
        return clonedImg;
      } catch (error) {
        console.error(`❌ 處理圖片 ${index} 時發生錯誤:`, error);
        return null;
      }
    }).filter(img => img !== null); // 過濾掉 null 值
  } catch (error) {
    console.error('❌ deepCloneImages 函數發生錯誤:', error);
    return [];
  }
}



// 取得共用用戶資訊與權限
const { currentUser, isAdmin, isVendor, isAdminOrVendor } = useCurrentUser()

// 檢查是否應該顯示權限警告
const showPermissionWarning = computed(() => {
  return props.mode === 'main' && isAdminOrVendor.value
})

// 權限警告訊息
const permissionWarningMessage = computed(() => {
  if (isAdmin.value) {
    return '管理員不能新增主留言，但可以回覆其他留言。'
  } else if (isVendor.value) {
    return '廠商不能新增主留言，但可以回覆其他留言。'
  }
  return ''
})

// 檢查是否應該禁用表單
const isDisabled = computed(() => {
  return props.mode === 'main' && isAdminOrVendor.value
})

// 送出按鈕文字
const submitButtonText = computed(() => {
  if (props.mode === 'main' && isAdminOrVendor.value) {
    return isAdmin.value ? '管理員無法新增留言' : '廠商無法新增留言'
  }
  return '送出'
})

// 處理圖片變化 - 只在明確操作時同步，避免無限迴圈
function handleImagesChange(newImages) {
  console.log('🖼️ 圖片變化:', {
    mode: props.mode,
    commentId: props.commentId,
    newImagesCount: newImages?.length || 0,
    newImages: newImages?.map(img => ({
      id: img.id,
      imageId: img.imageId,
      isActive: img.isActive,
      isDeleted: img.isDeleted,
      isNew: img.isNew,
      previewUrl: img.previewUrl,
      commentImageUrl: img.commentImageUrl,
      imageUrl: img.imageUrl
    }))
  });
  
  // 檢查是否為相同的引用，避免無限迴圈
  if (newImages === localImages.value) {
    console.log('🔄 檢測到相同引用，跳過更新以避免無限迴圈');
    return;
  }
  
  try {
    // 更新本地圖片狀態
    localImages.value = deepCloneImages(newImages);
    
    // ✅ 只在明確操作時同步，避免無限迴圈
    // 檢查是否為用戶操作（如刪除、排序、上傳新圖片）
    const hasUserAction = newImages.some(img => 
      img.isNew || // 新上傳的圖片
      img.isDeleted !== undefined || // 刪除操作
      img.sortOrder !== undefined // 排序操作
    );
    
    if (hasUserAction && props.mode === 'edit' && props.commentId) {
      console.log('🔄 檢測到用戶操作，同步圖片狀態到父組件');
      
      // 發送圖片變化事件到父組件（使用深拷貝避免引用共享）
      emit('images-changed', props.commentId, deepCloneImages(newImages));
    } else {
      console.log('🔄 跳過同步：沒有用戶操作或不是編輯模式');
    }
  } catch (error) {
    console.error('❌ 處理圖片變化時發生錯誤:', error);
    // 如果處理失敗，至少保持原有狀態
    localImages.value = [];
  }
}



// 監聽初始圖片變化 - 只在組件初始化時執行一次
watch(() => props.initialImages, (newImages) => {
  console.log('🖼️ CommentForm 接收初始圖片:', {
    mode: props.mode,
    commentId: props.commentId,
    newImagesCount: newImages?.length || 0,
    newImages: newImages?.map(img => ({
      id: img.id,
      imageId: img.imageId,
      name: img.name,
      previewUrl: img.previewUrl,
      commentImageUrl: img.commentImageUrl,
      imageUrl: img.imageUrl
    }))
  });
  
  try {
    if (newImages && newImages.length > 0) {
      // ✅ 過濾掉已刪除的圖片
      const cleanImages = newImages.filter(img => !img.isDeleted);
      console.log(`✅ 過濾後保留 ${cleanImages.length} 張圖片（原本 ${newImages.length} 張）`);
      
      localImages.value = deepCloneImages(cleanImages);
      console.log('✅ CommentForm 本地圖片已更新:', {
        localImagesCount: localImages.value.length,
        localImages: localImages.value.map(img => ({
          id: img.id,
          imageId: img.imageId,
          name: img.name,
          previewUrl: img.previewUrl
        }))
      });
    } else {
      localImages.value = [];
      console.log('📝 CommentForm 沒有初始圖片');
    }
  } catch (error) {
    console.error('❌ 處理初始圖片時發生錯誤:', error);
    localImages.value = [];
  }
}, { immediate: true, flush: 'post' });

// 處理圖片錯誤
function handleImageError(errors) {
  if (errors && errors.length > 0) {
    Swal.fire({
      icon: 'error',
      title: '圖片上傳錯誤',
      text: errors.join('\n'),
      confirmButtonText: '確定',
    });
  }
}

// 處理圖片排序更新
function handleOrderUpdated(result) {
  if (result.success) {
    console.log('✅ 圖片排序更新成功')
  } else {
    console.error('❌ 圖片排序更新失敗:', result.error)
    console.error('❌ 詳細錯誤信息:', result)
    
    let errorMessage = '圖片排序未能同步到伺服器，但本地排序已更新'
    if (result.error) {
      errorMessage += `\n\n錯誤詳情: ${result.error}`
    }
    
    // 如果有 HTTP 狀態碼，添加到錯誤信息
    if (result.details?.status) {
      errorMessage += `\nHTTP 狀態: ${result.details.status}`
    }
    
    // 如果有後端錯誤信息，添加到錯誤信息
    if (result.details?.data?.message) {
      errorMessage += `\n後端錯誤: ${result.details.data.message}`
    }
    
    Swal.fire({
      icon: 'warning',
      title: '排序更新失敗',
      text: errorMessage,
      confirmButtonText: '確定',
    });
  }
}

// 星星工具（顯示）
function starIcon(n) {
  if (rating.value >= n) return "bi-star-fill";
  return "bi-star";
};

// 星星工具（點擊設定評分）
function setRating(n) {
  if (isDisabled.value) return
  rating.value = n;
};

// 送出表單
async function submit() {
  // 權限檢查
  if (props.mode === 'main' && isAdminOrVendor.value) {
    const userTypeText = isAdmin.value ? '管理員' : '廠商'
    await Swal.fire({
      icon: 'warning',
      title: '權限不足',
      text: `${userTypeText}不能新增主留言，但可以回覆其他留言。`,
      confirmButtonText: '確定',
    });
    return;
  }

  // 基本驗證檢查內容是否為空
  const trimmed = content.value.trim();
  if (!trimmed) {
    await Swal.fire({
      icon: 'warning',
      title: '請輸入留言內容',
      confirmButtonText: '確定',
    });
    return;
  }

  if ((props.mode === 'main' || props.mode === 'edit') && (rating.value < 1)) {
    await Swal.fire({
      icon: 'warning',
      title: '請選擇評分（最少 1 顆星）',
      confirmButtonText: '確定',
    });
    return;
  }

  // 圖片數量限制檢查
  if (localImages.value && localImages.value.length > 5) {
    await Swal.fire({
      icon: 'warning',
      title: '圖片數量超限',
      text: '最多只能上傳 5 張圖片',
      confirmButtonText: '確定',
    });
    return;
  }

  let finalContent = trimmed;
  if (props.mode === 'reply' && props.replyPrefix) {
    finalContent = `${props.replyPrefix} ${finalContent}`;
  }

  // 組 payload
  const payload = {
    content: finalContent,
    rating: props.mode === 'reply' ? null : rating.value,
    parentCommentId: props.mode === 'reply'
      ? props.parentId ?? null
      : null,
    images: localImages.value.map((img, i) => ({
      id: img.id,
      imageId: img.imageId,
      imageUrl: img.imageUrl,
      commentImageUrl: img.commentImageUrl,
      mimeType: img.mimeType,
      sortOrder: i,
      isNew: img.isNew || false,
      isActive: img.isActive,
      isDeleted: img.isDeleted,
      file: img.file, // 新上傳的檔案
      previewUrl: img.previewUrl
    })),
  };

  // 除錯用：顯示即將送出的留言內容
  console.log('🔼 留言送出 payload:', payload);

  emit('submit', payload);

  // 若非編輯模式，送出後重置表單
  if (props.mode !== 'edit') {
    content.value = '';
    rating.value = 5;
    localImages.value = [];
  }
}

// 組件卸載時清理資源
onUnmounted(() => {
  console.log('🧹 CommentForm 組件已清理');
});

</script>

<style scoped>
.rating-input i {
  font-size: 1.5rem;
  margin-right: 5px;
}

textarea {
  resize: vertical;
}

.alert {
  border-radius: 8px;
}
</style>