<!--
  交通上傳表單組件
  
  @author Max
  @since 2025-07-08
-->
<template>
  <div class="traffic-upload-form">
    <div class="row">
      <div class="col-md-6 mb-3">
        <label for="ticketName" class="form-label">票券名稱 *</label>
        <input
          type="text"
          class="form-control"
          id="ticketName"
          v-model="formData.ticketName"
          placeholder="例：台北捷運一日券"
        />
      </div>
      <div class="col-md-6 mb-3">
        <label for="transportType" class="form-label">交通工具類型 *</label>
        <select class="form-select" id="transportType" v-model="formData.transportType">
          <option value="">請選擇類型</option>
          <option value="捷運">捷運</option>
          <option value="公車">公車</option>
          <option value="火車">火車</option>
          <option value="高鐵">高鐵</option>
          <option value="計程車">計程車</option>
        </select>
      </div>
    </div>
    <div class="row">
      <div class="col-md-6 mb-3">
        <label for="validDays" class="form-label">有效天數 *</label>
        <input
          type="number"
          class="form-control"
          id="validDays"
          v-model="formData.validDays"
          placeholder="例：1"
        />
      </div>
      <div class="col-md-6 mb-3">
        <label for="price" class="form-label">價格 *</label>
        <input
          type="number"
          class="form-control"
          id="price"
          v-model="formData.price"
          placeholder="例：150"
        />
      </div>
    </div>
    <div class="row">
      <div class="col-md-12 mb-3">
        <label for="description" class="form-label">描述</label>
        <textarea
          class="form-control"
          id="description"
          v-model="formData.description"
          rows="3"
          placeholder="例：台北捷運一日無限搭乘"
        ></textarea>
      </div>
    </div>
    <div class="row">
      <div class="col-md-6 mb-3">
        <label for="region" class="form-label">區域</label>
        <input
          type="text"
          class="form-control"
          id="region"
          v-model="formData.region"
          placeholder="例：台北市"
        />
      </div>
      <div class="col-md-6 mb-3">
        <label for="stock" class="form-label">庫存數量</label>
        <input
          type="number"
          class="form-control"
          id="stock"
          v-model="formData.stock"
          placeholder="例：1000"
        />
      </div>
    </div>
    <div class="row">
      <div class="col-md-6 mb-3">
        <label for="validStartTime" class="form-label">有效開始時間</label>
        <input
          type="datetime-local"
          class="form-control"
          id="validStartTime"
          v-model="formData.validStartTime"
        />
      </div>
      <div class="col-md-6 mb-3">
        <label for="validEndTime" class="form-label">有效結束時間</label>
        <input
          type="datetime-local"
          class="form-control"
          id="validEndTime"
          v-model="formData.validEndTime"
        />
      </div>
    </div>
    <div class="row">
      <div class="col-md-12 mb-3">
        <label for="country" class="form-label">國家 *</label>
        <input
          type="text"
          class="form-control"
          id="country"
          v-model="formData.country"
          placeholder="例：台灣"
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
    ticketName: '',
    transportType: '',
    validDays: '',
    description: '',
    region: '',
    price: '',
    stock: '',
    validStartTime: '',
    validEndTime: '',
    country: ''
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
      ticketName: '',
      transportType: '',
      validDays: '',
      description: '',
      region: '',
      price: '',
      stock: '',
      validStartTime: '',
      validEndTime: '',
      country: ''
    };
    emit('reset');
  };
  const validateForm = () => {
    const requiredFields = ['ticketName', 'transportType', 'validDays', 'price', 'country'];
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
      ticketName: '票券名稱',
      transportType: '交通工具類型',
      validDays: '有效天數',
      price: '價格',
      country: '國家'
    };
    return labels[field] || field;
  };
</script>
<style scoped>
  .traffic-upload-form {
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
