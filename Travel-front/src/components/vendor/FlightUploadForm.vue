<!--
  航班上傳表單組件
  
  @author Max
  @since 2025-07-08
-->
<template>
  <div class="flight-upload-form">
    <div class="row">
      <div class="col-md-6 mb-3">
        <label for="flightNumber" class="form-label">航班號碼 *</label>
        <input
          type="text"
          class="form-control"
          id="flightNumber"
          v-model="formData.flightNumber"
          placeholder="例：BR123"
        />
      </div>
      <div class="col-md-6 mb-3">
        <label for="airlineCode" class="form-label">航空公司代碼 *</label>
        <input
          type="text"
          class="form-control"
          id="airlineCode"
          v-model="formData.airlineCode"
          placeholder="例：BR"
        />
      </div>
    </div>
    <div class="row">
      <div class="col-md-6 mb-3">
        <label for="departureAirport" class="form-label">出發機場代碼 *</label>
        <input
          type="text"
          class="form-control"
          id="departureAirport"
          v-model="formData.departureAirport"
          placeholder="例：TPE"
        />
      </div>
      <div class="col-md-6 mb-3">
        <label for="arrivalAirport" class="form-label">到達機場代碼 *</label>
        <input
          type="text"
          class="form-control"
          id="arrivalAirport"
          v-model="formData.arrivalAirport"
          placeholder="例：NRT"
        />
      </div>
    </div>
    <div class="row">
      <div class="col-md-6 mb-3">
        <label for="departureTime" class="form-label">出發時間 *</label>
        <input
          type="datetime-local"
          class="form-control"
          id="departureTime"
          v-model="formData.departureTime"
        />
      </div>
      <div class="col-md-6 mb-3">
        <label for="arrivalTime" class="form-label">到達時間 *</label>
        <input
          type="datetime-local"
          class="form-control"
          id="arrivalTime"
          v-model="formData.arrivalTime"
        />
      </div>
    </div>
    <div class="row">
      <div class="col-md-6 mb-3">
        <label for="duration" class="form-label">飛行時長(分鐘) *</label>
        <input
          type="number"
          class="form-control"
          id="duration"
          v-model="formData.duration"
          placeholder="例：240"
        />
      </div>
      <div class="col-md-6 mb-3">
        <label for="status" class="form-label">狀態 *</label>
        <select class="form-select" id="status" v-model="formData.status">
          <option value="">請選擇狀態</option>
          <option value="active">啟用</option>
          <option value="inactive">停用</option>
          <option value="cancelled">取消</option>
        </select>
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
          placeholder="例：5000"
        />
      </div>
      <div class="col-md-6 mb-3">
        <label for="stock" class="form-label">庫存數量</label>
        <input
          type="number"
          class="form-control"
          id="stock"
          v-model="formData.stock"
          placeholder="例：100"
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
    flightNumber: '',
    airlineCode: '',
    departureAirport: '',
    arrivalAirport: '',
    departureTime: '',
    arrivalTime: '',
    duration: '',
    status: '',
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
      flightNumber: '',
      airlineCode: '',
      departureAirport: '',
      arrivalAirport: '',
      departureTime: '',
      arrivalTime: '',
      duration: '',
      status: '',
      price: '',
      stock: ''
    };
    emit('reset');
  };
  const validateForm = () => {
    const requiredFields = [
      'flightNumber',
      'airlineCode',
      'departureAirport',
      'arrivalAirport',
      'departureTime',
      'arrivalTime',
      'duration',
      'status',
      'price'
    ];
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
      flightNumber: '航班號碼',
      airlineCode: '航空公司代碼',
      departureAirport: '出發機場代碼',
      arrivalAirport: '到達機場代碼',
      departureTime: '出發時間',
      arrivalTime: '到達時間',
      duration: '飛行時長',
      status: '狀態',
      price: '價格'
    };
    return labels[field] || field;
  };
</script>
<style scoped>
  .flight-upload-form {
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
