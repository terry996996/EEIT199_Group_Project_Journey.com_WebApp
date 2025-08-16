<!--
  飯店上傳表單組件
  
  @author Max
  @since 2025-07-08
-->
<template>
  <div class="hotel-upload-form">
    <div class="hotel-info-card mb-4">
      <h4 class="mb-3"><i class="bi bi-door-open me-2"></i>房型上傳</h4>
      <form @submit.prevent="submitRoomType">
        <div class="row">
          <div class="col-md-6 mb-3">
            <label class="form-label">房型名稱 *</label>
            <input v-model="roomTypeForm.name" type="text" class="form-control" required />
          </div>
          <div class="col-md-6 mb-3">
            <label class="form-label">每晚價錢 (NT$) *</label>
            <input v-model="roomTypeForm.price" type="number" class="form-control" min="0" required />
          </div>
        </div>
        <div class="row">
          <div class="col-md-6 mb-3">
            <label class="form-label">住宿人數 *</label>
            <input v-model="roomTypeForm.guestCount" type="number" class="form-control" min="1" required />
          </div>
          <div class="col-md-6 mb-3">
            <label class="form-label">庫存 *</label>
            <input v-model="roomTypeForm.stock" type="number" class="form-control" min="0" required />
          </div>
        </div>
        <div class="row">
          <div class="col-md-12 mb-3">
            <label class="form-label">房型介紹 *</label>
            <textarea v-model="roomTypeForm.description" class="form-control" rows="2" required></textarea>
          </div>
        </div>
        <!-- 新增：圖片上傳欄位 -->
        <div class="row">
          <div class="col-md-12 mb-3">
            <label class="form-label">房型圖片（最多5張）</label>
            <input type="file" class="form-control" accept="image/*" multiple @change="handleImageUpload"
              :disabled="roomTypeForm.images.length >= 5" />
            <div class="mt-2 d-flex flex-wrap gap-2">
              <div v-for="(img, idx) in roomTypeForm.images" :key="idx" style="position: relative">
                <img :src="img" alt="預覽" style="
                    width: 80px;
                    height: 80px;
                    object-fit: cover;
                    border-radius: 8px;
                    border: 1px solid #ccc;
                  " />
                <button type="button" class="btn-close btn-sm" style="position: absolute; top: 0; right: 0"
                  @click="removeImage(idx)"></button>
              </div>
            </div>
          </div>
        </div>
        <div class="text-end">
          <button type="submit" class="btn btn-success" :disabled="submitting">
            <span v-if="submitting" class="spinner-border spinner-border-sm me-2"></span>
            <i v-else class="bi bi-check-circle me-2"></i>
            {{ submitting ? '上傳中...' : '送出房型資料' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const props = defineProps({
  submitting: Boolean
});
const emit = defineEmits(['submit', 'reset']);

const roomTypeForm = ref({
  name: '',
  price: '',
  guestCount: '',
  stock: '',
  description: '',
  images: [] // base64 字串陣列
});

const submitting = ref(false);

function handleImageUpload(e) {
  const files = Array.from(e.target.files).slice(0, 5 - roomTypeForm.value.images.length);
  files.forEach((file) => {
    const reader = new FileReader();
    reader.onload = (event) => {
      if (roomTypeForm.value.images.length < 5) {
        roomTypeForm.value.images.push(event.target.result);
      }
    };
    reader.readAsDataURL(file);
  });
  // 清空 input value 以便重複選同一張圖
  e.target.value = '';
}

function removeImage(idx) {
  roomTypeForm.value.images.splice(idx, 1);
}

function submitRoomType() {
  submitting.value = true;
  // 產生唯一 id
  const id = Date.now() + Math.random().toString(36).substr(2, 9);
  const vendorId = localStorage.getItem('vendorId') || '';
  const vendorName = localStorage.getItem('vendorName') || '';
  const newRoomType = {
    id,
    name: roomTypeForm.value.name,
    price: parseInt(roomTypeForm.value.price),
    guestCount: parseInt(roomTypeForm.value.guestCount),
    stock: parseInt(roomTypeForm.value.stock),
    description: roomTypeForm.value.description,
    images: [...roomTypeForm.value.images],
    isActive: true,
    vendorId,
    vendorName,
    createdAt: new Date().toISOString()
  };
  // 存進 localStorage
  const roomTypes = JSON.parse(localStorage.getItem('roomTypes') || '[]');
  roomTypes.push(newRoomType);
  localStorage.setItem('roomTypes', JSON.stringify(roomTypes));
  // 重置表單
  roomTypeForm.value = {
    name: '',
    price: '',
    guestCount: '',
    stock: '',
    description: '',
    images: []
  };
  submitting.value = false;
  alert('房型上傳成功！');
  emit('submit', newRoomType);
}
</script>

<style scoped>
.hotel-upload-form {
  padding: 0;
}

.hotel-info-card {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  padding: 20px;
  border-radius: 10px;
  border: 1px solid #dee2e6;
}

.room-management-header {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  padding: 20px;
  border-radius: 10px;
  border: 1px solid #dee2e6;
}

.room-card {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  border: 1px solid #dee2e6;
}

.room-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.room-details {
  margin-top: 15px;
}

.detail-item {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  font-size: 0.9rem;
}

.facilities-tags {
  margin-top: 15px;
}

.empty-state {
  background: #f8f9fa;
  border-radius: 10px;
  border: 2px dashed #dee2e6;
}

.form-control:focus,
.form-select:focus {
  border-color: #0080ff;
  box-shadow: 0 0 0 0.2rem rgba(0, 128, 255, 0.25);
}

.btn-primary {
  background-color: #0080ff;
  border-color: #0080ff;
}

.btn-primary:hover {
  background-color: #005bbd;
  border-color: #005bbd;
}

.modal-header {
  border-bottom: none;
}

.modal-footer {
  border-top: none;
}
</style>