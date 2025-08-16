<!--
  景點上傳表單組件
  
  @author Max
  @since 2025-07-08
-->
<template>
  <div class="attraction-upload-form">
    <div class="row">
      <div class="col-md-6 mb-3">
        <label for="attractionName" class="form-label">景點名稱 *</label>
        <input
          type="text"
          class="form-control"
          id="attractionName"
          v-model="formData.attractionName"
          placeholder="例：台北101觀景台"
        />
      </div>
      <div class="col-md-6 mb-3">
        <label for="description" class="form-label">描述</label>
        <input
          type="text"
          class="form-control"
          id="description"
          v-model="formData.description"
          placeholder="例：台北地標建築"
        />
      </div>
    </div>
    <div class="row">
      <div class="col-md-12 mb-3">
        <label for="address" class="form-label">地址 *</label>
        <input
          type="text"
          class="form-control"
          id="address"
          v-model="formData.address"
          placeholder="例：台北市信義區"
        />
      </div>
    </div>
    <div class="row">
      <div class="col-md-6 mb-3">
        <label for="imageUrl" class="form-label">圖片URL</label>
        <input
          type="url"
          class="form-control"
          id="imageUrl"
          v-model="formData.imageUrl"
          placeholder="例：https://example.com/101.jpg"
        />
      </div>
      <div class="col-md-6 mb-3">
        <label for="imageType" class="form-label">圖片類型</label>
        <select class="form-select" id="imageType" v-model="formData.imageType">
          <option value="">請選擇類型</option>
          <option value="jpg">JPG</option>
          <option value="png">PNG</option>
          <option value="gif">GIF</option>
          <option value="webp">WebP</option>
        </select>
      </div>
    </div>
    <div class="row">
      <div class="col-md-6 mb-3">
        <label for="openTime" class="form-label">營業開始時間</label>
        <input type="time" class="form-control" id="openTime" v-model="formData.openTime" />
      </div>
      <div class="col-md-6 mb-3">
        <label for="closeTime" class="form-label">營業結束時間</label>
        <input type="time" class="form-control" id="closeTime" v-model="formData.closeTime" />
      </div>
    </div>
    <div class="row">
      <div class="col-md-6 mb-3">
        <label for="country" class="form-label">國家 *</label>
        <input
          type="text"
          class="form-control"
          id="country"
          v-model="formData.country"
          placeholder="例：台灣"
        />
      </div>
      <div class="col-md-6 mb-3">
        <label for="city" class="form-label">城市 *</label>
        <input
          type="text"
          class="form-control"
          id="city"
          v-model="formData.city"
          placeholder="例：台北"
        />
      </div>
    </div>
    <div class="row">
      <div class="col-md-6 mb-3">
        <label for="price" class="form-label">價格 *</label>
        <input
          type="number"
          class="form-control"
          id="price"
          v-model="formData.price"
          placeholder="例：500"
        />
      </div>
      <div class="col-md-6 mb-3">
        <label for="stock" class="form-label">庫存數量</label>
        <input
          type="number"
          class="form-control"
          id="stock"
          v-model="formData.stock"
          placeholder="例：200"
        />
      </div>
    </div>
    <div class="text-center mt-4">
      <button @click="handleSubmit" class="btn btn-primary" :disabled="submitting">
        <span v-if="submitting" class="spinner-border spinner-border-sm me-2"></span>
        <i v-else class="bi bi-check-circle me-2"></i>
        {{ submitting ? '提交中...' : '提交資料' }}
      </button>
      <button @click="handleReset" class="btn btn-outline-secondary ms-2" :disabled="submitting">
        <i class="bi bi-arrow-clockwise me-2"></i> 重置表單
      </button>
    </div>
  </div>
</template>
<script setup>
  import { ref, defineProps, defineEmits, watch } from 'vue';
  const props = defineProps({
    modelValue: { type: Object, default: () => ({}) },
    submitting: { type: Boolean, default: false }
  });
  const emit = defineEmits(['update:modelValue', 'submit', 'reset']);
  const formData = ref({
    attractionName: '',
    description: '',
    address: '',
    imageUrl: '',
    imageType: '',
    openTime: '',
    closeTime: '',
    country: '',
    city: '',
    price: '',
    stock: ''
  });
  watch(
    () => props.modelValue,
    (newValue) => {
      if (newValue && Object.keys(newValue).length > 0) {
        formData.value = { ...newValue };
      }
    },
    { deep: true }
  );
  watch(
    formData,
    (newValue) => {
      emit('update:modelValue', newValue);
    },
    { deep: true }
  );
  const handleSubmit = () => {
    if (validateForm()) {
      emit('submit', formData.value);
    }
  };
  const handleReset = () => {
    formData.value = {
      attractionName: '',
      description: '',
      address: '',
      imageUrl: '',
      imageType: '',
      openTime: '',
      closeTime: '',
      country: '',
      city: '',
      price: '',
      stock: ''
    };
    emit('reset');
  };
  const validateForm = () => {
    const requiredFields = ['attractionName', 'address', 'country', 'city', 'price'];
    for (const field of requiredFields) {
      if (!formData.value[field]) {
        alert(`請填寫 ${getFieldLabel(field)}`);
        return false;
      }
    }
    return true;
  };
  const getFieldLabel = (field) => {
    const labels = {
      attractionName: '景點名稱',
      address: '地址',
      country: '國家',
      city: '城市',
      price: '價格'
    };
    return labels[field] || field;
  };
</script>
<style scoped>
  .attraction-upload-form {
    padding: 0;
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
  .btn-outline-secondary {
    border-color: #6c757d;
    color: #6c757d;
  }
  .btn-outline-secondary:hover {
    background-color: #6c757d;
    border-color: #6c757d;
  }
</style>
