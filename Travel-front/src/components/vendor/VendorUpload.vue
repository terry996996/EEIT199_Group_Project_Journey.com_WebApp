<!--
  通用商家上傳組件
  
  @author Max
  @since 2025-07-08
-->
<template>
  <div class="vendor-upload-container">
    <div class="container my-5">
      <div class="row">
        <div class="col-12">
          <!-- 頁面標題 -->
          <div class="page-header">
            <div class="page-title-section">
              <div class="title-icon">
                <i :class="getTypeIcon()"></i>
              </div>
              <div class="title-content">
                <h2 class="page-title">{{ pageTitle }}</h2>
                <p class="page-subtitle">管理您的 {{ typeDisplayName }} 商品資料</p>
              </div>
            </div>
          </div>

          <!-- 上傳方式選擇 -->
          <div class="upload-method-card">
            <div class="card-header">
              <h5 class="mb-0">
                <i class="bi bi-gear me-2"></i>
                選擇上傳方式
              </h5>
            </div>
            <div class="card-body">
              <div class="method-options">
                <div class="method-option" :class="{ active: uploadMethod === 'single' }"
                  @click="uploadMethod = 'single'">
                  <div class="method-icon">
                    <i class="bi bi-plus-circle"></i>
                  </div>
                  <div class="method-content">
                    <h6>單筆輸入</h6>
                    <p>逐筆新增商品資料</p>
                  </div>
                </div>
                <div class="method-option" :class="{ active: uploadMethod === 'excel' }"
                  @click="uploadMethod = 'excel'">
                  <div class="method-icon">
                    <i class="bi bi-file-earmark-excel"></i>
                  </div>
                  <div class="method-content">
                    <h6>Excel 批次上傳</h6>
                    <p>大量匯入商品資料</p>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 單筆輸入表單 -->
          <div class="upload-form-card" v-if="uploadMethod === 'single'">
            <div class="card-header">
              <h5 class="mb-0">
                <i class="bi bi-pencil-square me-2"></i>
                單筆 {{ typeDisplayName }} 資料輸入
              </h5>
            </div>
            <div class="card-body">
              <component :is="formComponent" v-model="singleData" @submit="submitSingleData" @reset="resetForm"
                :submitting="submitting" />
            </div>
          </div>

          <!-- Excel 上傳相關 -->
          <div v-if="uploadMethod === 'excel'" class="excel-upload-section">
            <!-- Excel 範本下載 -->
            <div class="template-card">
              <div class="card-header">
                <h5 class="mb-0">
                  <i class="bi bi-download me-2"></i>
                  Excel 範本
                </h5>
              </div>
              <div class="card-body">
                <div class="template-content">
                  <div class="template-info">
                    <p class="template-description">
                      請下載對應的 Excel 範本，按照格式填寫資料後上傳
                    </p>
                    <div class="template-features">
                      <span class="feature-tag">
                        <i class="bi bi-check-circle me-1"></i>
                        標準格式
                      </span>
                      <span class="feature-tag">
                        <i class="bi bi-check-circle me-1"></i>
                        批次處理
                      </span>
                      <span class="feature-tag">
                        <i class="bi bi-check-circle me-1"></i>
                        快速匯入
                      </span>
                    </div>
                  </div>
                  <button @click="downloadTemplate" class="btn btn-outline-primary download-btn">
                    <i class="bi bi-download me-2"></i>
                    下載 {{ typeDisplayName }} 範本
                  </button>
                </div>
              </div>
            </div>

            <!-- Excel 檔案上傳 -->
            <div class="upload-card">
              <div class="card-header">
                <h5 class="mb-0">
                  <i class="bi bi-upload me-2"></i>
                  上傳 Excel 檔案
                </h5>
              </div>
              <div class="card-body">
                <div class="upload-area" :class="{ 'has-file': selectedFile }">
                  <div class="upload-content">
                    <div class="upload-icon">
                      <i class="bi bi-file-earmark-excel"></i>
                    </div>
                    <div class="upload-text">
                      <h6>選擇 Excel 檔案</h6>
                      <p>支援 .xlsx 和 .xls 格式</p>
                    </div>
                    <input type="file" class="file-input" accept=".xlsx,.xls" @change="handleFileSelect"
                      :disabled="uploading" />
                  </div>
                  <div v-if="selectedFile" class="file-info">
                    <i class="bi bi-file-earmark-check text-success me-2"></i>
                    <span>{{ selectedFile.name }}</span>
                  </div>
                </div>
                <button @click="uploadFile" class="btn btn-primary upload-btn" :disabled="!selectedFile || uploading">
                  <span v-if="uploading" class="spinner-border spinner-border-sm me-2"></span>
                  <i v-else class="bi bi-upload me-2"></i>
                  {{ uploading ? '上傳中...' : '開始上傳' }}
                </button>
              </div>
            </div>
          </div>

          <!-- 上傳結果 -->
          <div class="results-card" v-if="uploadResults.length > 0">
            <div class="card-header">
              <h5 class="mb-0">
                <i class="bi bi-clipboard-check me-2"></i>
                上傳結果
              </h5>
            </div>
            <div class="card-body">
              <div class="results-summary" :class="getAlertClass()">
                <div class="summary-icon">
                  <i :class="getSummaryIcon()"></i>
                </div>
                <div class="summary-content">
                  <h6>上傳完成！</h6>
                  <p>共處理 {{ uploadResults.length }} 筆資料</p>
                </div>
              </div>
              <div class="upload-results">
                <div v-for="(result, index) in uploadResults" :key="index" class="result-item"
                  :class="getResultClass(result)">
                  <i :class="getResultIcon(result)" class="me-2"></i>
                  {{ result }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, defineProps, defineEmits } from 'vue';
import { apiClient } from '../../services/api';
import FlightUploadForm from './FlightUploadForm.vue';
import HotelUploadForm from './HotelUploadForm.vue';
import TrafficUploadForm from './TrafficUploadForm.vue';
import AttractionUploadForm from './AttractionUploadForm.vue';
import * as XLSX from 'xlsx';

const props = defineProps({
  vendorType: {
    type: String,
    required: true,
    validator: (value) => ['flight', 'hotel', 'traffic', 'attraction'].includes(value)
  },
  vendorId: {
    type: Number,
    required: true
  }
});

const emit = defineEmits(['upload-success', 'upload-error']);
const selectedFile = ref(null);
const uploading = ref(false);
const uploadResults = ref([]);
const uploadMethod = ref('single');
const submitting = ref(false);
const singleData = ref({});

const typeDisplayName = computed(() => {
  const typeNames = {
    flight: '機票',
    hotel: '飯店',
    traffic: '交通票',
    attraction: '景點票'
  };
  return typeNames[props.vendorType] || '';
});

const pageTitle = computed(() => {
  return `${typeDisplayName.value}商家資料上傳`;
});

const formComponent = computed(() => {
  const componentMap = {
    flight: FlightUploadForm,
    hotel: HotelUploadForm,
    traffic: TrafficUploadForm,
    attraction: AttractionUploadForm
  };
  return componentMap[props.vendorType];
});

const getTypeIcon = () => {
  const icons = {
    flight: 'bi bi-airplane',
    hotel: 'bi bi-building',
    traffic: 'bi bi-train-front',
    attraction: 'bi bi-geo-alt'
  };
  return icons[props.vendorType] || 'bi bi-shop';
};

const resetForm = () => {
  singleData.value = {};
};

const submitSingleData = async (data) => {
  submitting.value = true;
  uploadResults.value = [];
  try {
    // 不呼叫 API，直接顯示成功
    uploadResults.value = ['房型上傳成功！'];
    emit('upload-success', '房型上傳成功！');
    resetForm();
  } catch (error) {
    console.error('提交失敗:', error);
    const errorMessage = '提交失敗: ' + (error.response?.data || error.message);
    uploadResults.value = [errorMessage];
    emit('upload-error', errorMessage);
  } finally {
    submitting.value = false;
  }
};

const handleFileSelect = (event) => {
  const file = event.target.files[0];
  if (file) {
    selectedFile.value = file;
  }
};

const downloadTemplate = async () => {
  try {
    const response = await apiClient.get(`/api/vendor/upload-template/${props.vendorType}`);
    const template = response.data;
    const blob = new Blob([template], { type: 'text/plain' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `${props.vendorType}_template.txt`;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
  } catch (error) {
    console.error('下載範本失敗:', error);
    alert('下載範本失敗，請稍後再試');
  }
};

const uploadFile = async () => {
  if (!selectedFile.value) {
    alert('請選擇檔案');
    return;
  }
  uploading.value = true;
  uploadResults.value = [];
  try {
    // 讀取 Excel 檔案
    const reader = new FileReader();
    reader.onload = (e) => {
      try {
        const data = new Uint8Array(e.target.result);
        const workbook = XLSX.read(data, { type: 'array' });
        const sheetName = workbook.SheetNames[0];
        const worksheet = workbook.Sheets[sheetName];
        const jsonData = XLSX.utils.sheet_to_json(worksheet, { header: 1 });
        if (jsonData.length < 2) {
          uploadResults.value = ['Excel 檔案格式錯誤，至少需要標題列和一筆資料'];
          uploading.value = false;
          return;
        }
        // 欄位對應
        const headers = jsonData[0].map((h) => (h ? h.toString().trim() : ''));
        // 直接用欄位順序對應
        const requiredHeaders = ['房型名稱', '每晚價格', '住宿人數', '庫存', '房型介紹'];
        if (headers.length < 6) {
          uploadResults.value = ['Excel 欄位數不足，請確認格式'];
          uploading.value = false;
          return;
        }
        const dataRows = jsonData.slice(1);
        const validRows = [];
        const results = [];
        dataRows.forEach((row, i) => {
          // 空行自動跳過
          if (
            row.every(
              (cell) => cell === undefined || cell === null || cell.toString().trim() === ''
            )
          )
            return;
          const price = Number(row[1]);
          const guestCount = Number(row[2]);
          const stock = Number(row[3]);
          const rowData = {
            name: (row[0] ?? '').toString().trim(),
            price,
            guestCount,
            stock,
            description: (row[4] ?? '').toString().trim(),
            imageUrl: (row[5] ?? '').toString().trim(),
            isActive: true,
            vendorId: props.vendorId,
            vendorName: localStorage.getItem('vendorName') || '商家',
            createdAt: new Date().toISOString()
          };
          // 驗證
          const errors = [];
          if (!rowData.name.trim()) errors.push('房型名稱不能為空');
          if (!Number.isFinite(price) || price <= 0) errors.push('價格必須為正數');
          if (!Number.isFinite(guestCount) || guestCount <= 0) errors.push('住宿人數必須為正數');
          if (!Number.isFinite(stock) || stock < 0) errors.push('庫存不能為負數');
          if (rowData.description.length > 500) errors.push('介紹不能超過 500 字元');
          if (
            rowData.imageUrl &&
            typeof rowData.imageUrl === 'string' &&
            !/^https?:\/\//.test(rowData.imageUrl)
          )
            errors.push('圖片必須為有效的網址');
          if (errors.length === 0) {
            rowData.id = Date.now() + Math.random().toString(36).substr(2, 9);
            validRows.push(rowData);
            results.push(`第${i + 2}行：成功`);
          } else {
            results.push(`第${i + 2}行：失敗 - ${errors.join('、')}`);
          }
        });
        // 寫入 localStorage
        if (validRows.length > 0) {
          const existing = JSON.parse(localStorage.getItem('roomTypes') || '[]');
          const updated = [...existing, ...validRows];
          localStorage.setItem('roomTypes', JSON.stringify(updated));
          window.dispatchEvent(
            new CustomEvent('localStorageChange', {
              detail: { key: 'roomTypes', value: updated }
            })
          );
        }
        uploadResults.value = results;
      } catch (err) {
        uploadResults.value = ['Excel 解析失敗: ' + err.message];
      }
      uploading.value = false;
    };
    reader.readAsArrayBuffer(selectedFile.value);
  } catch (error) {
    uploadResults.value = ['上傳失敗: ' + error.message];
    uploading.value = false;
  }
};

const getAlertClass = () => {
  const hasError = uploadResults.value.some(
    (result) => result.includes('失敗') || result.includes('錯誤') || result.includes('找不到')
  );
  return hasError ? 'alert-warning' : 'alert-success';
};

const getSummaryIcon = () => {
  const hasError = uploadResults.value.some(
    (result) => result.includes('失敗') || result.includes('錯誤') || result.includes('找不到')
  );
  return hasError ? 'bi bi-exclamation-triangle' : 'bi bi-check-circle';
};

const getResultClass = (result) => {
  if (result.includes('成功')) {
    return 'text-success';
  } else if (result.includes('失敗') || result.includes('錯誤') || result.includes('找不到')) {
    return 'text-danger';
  } else {
    return 'text-info';
  }
};

const getResultIcon = (result) => {
  if (result.includes('成功')) {
    return 'bi bi-check-circle';
  } else if (result.includes('失敗') || result.includes('錯誤') || result.includes('找不到')) {
    return 'bi bi-x-circle';
  } else {
    return 'bi bi-info-circle';
  }
};
</script>

<style scoped>
.vendor-upload-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
}

.page-header {
  margin-bottom: 30px;
}

.page-title-section {
  display: flex;
  align-items: center;
  gap: 20px;
}

.title-icon {
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 16px rgba(102, 126, 234, 0.3);
}

.title-icon i {
  font-size: 28px;
  color: white;
}

.page-title {
  font-size: 32px;
  font-weight: 700;
  color: #2d3748;
  margin: 0;
  margin-bottom: 4px;
}

.page-subtitle {
  color: #718096;
  font-size: 16px;
  margin: 0;
}

.upload-method-card,
.upload-form-card,
.template-card,
.upload-card,
.results-card {
  background: white;
  border: none;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  margin-bottom: 24px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.upload-method-card:hover,
.upload-form-card:hover,
.template-card:hover,
.upload-card:hover,
.results-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.card-header {
  background: linear-gradient(135deg, #f7fafc, #edf2f7);
  border-bottom: 1px solid #e2e8f0;
  padding: 20px 24px;
}

.card-header h5 {
  color: #2d3748;
  font-weight: 600;
  margin: 0;
}

.card-body {
  padding: 24px;
}

.method-options {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.method-option {
  display: flex;
  align-items: center;
  padding: 20px;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #f7fafc;
}

.method-option:hover {
  border-color: #667eea;
  background: #f0f4ff;
  transform: translateY(-2px);
}

.method-option.active {
  border-color: #667eea;
  background: linear-gradient(135deg, #f0f4ff, #e6f3ff);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.2);
}

.method-icon {
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
}

.method-icon i {
  font-size: 24px;
  color: white;
}

.method-content h6 {
  font-weight: 600;
  color: #2d3748;
  margin: 0;
  margin-bottom: 4px;
}

.method-content p {
  color: #718096;
  font-size: 14px;
  margin: 0;
}

.excel-upload-section {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.template-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 24px;
}

.template-description {
  color: #4a5568;
  margin-bottom: 12px;
}

.template-features {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.feature-tag {
  background: #e6fffa;
  color: #2c7a7b;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
  display: flex;
  align-items: center;
}

.download-btn {
  border: 2px solid #667eea;
  color: #667eea;
  background: transparent;
  padding: 12px 24px;
  border-radius: 12px;
  font-weight: 600;
  transition: all 0.3s ease;
  white-space: nowrap;
}

.download-btn:hover {
  background: #667eea;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.upload-area {
  border: 2px dashed #cbd5e0;
  border-radius: 12px;
  padding: 40px;
  text-align: center;
  transition: all 0.3s ease;
  background: #f7fafc;
  position: relative;
}

.upload-area:hover {
  border-color: #667eea;
  background: #f0f4ff;
}

.upload-area.has-file {
  border-color: #48bb78;
  background: #f0fff4;
}

.upload-content {
  position: relative;
}

.upload-icon {
  width: 64px;
  height: 64px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
}

.upload-icon i {
  font-size: 32px;
  color: white;
}

.upload-text h6 {
  color: #2d3748;
  font-weight: 600;
  margin-bottom: 8px;
}

.upload-text p {
  color: #718096;
  margin: 0;
}

.file-input {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0;
  cursor: pointer;
}

.file-info {
  margin-top: 16px;
  padding: 12px;
  background: white;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.upload-btn {
  width: 100%;
  margin-top: 16px;
  padding: 16px;
  border-radius: 12px;
  font-weight: 600;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border: none;
  transition: all 0.3s ease;
}

.upload-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 16px rgba(102, 126, 234, 0.3);
}

.upload-btn:disabled {
  opacity: 0.6;
  transform: none;
}

.results-summary {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  border-radius: 12px;
  margin-bottom: 20px;
}

.results-summary.alert-success {
  background: #f0fff4;
  border: 1px solid #9ae6b4;
  color: #2f855a;
}

.results-summary.alert-warning {
  background: #fffbeb;
  border: 1px solid #fbd38d;
  color: #c05621;
}

.summary-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.results-summary.alert-success .summary-icon {
  background: #48bb78;
  color: white;
}

.results-summary.alert-warning .summary-icon {
  background: #ed8936;
  color: white;
}

.summary-content h6 {
  font-weight: 600;
  margin: 0;
  margin-bottom: 4px;
}

.summary-content p {
  margin: 0;
  opacity: 0.8;
}

.upload-results {
  max-height: 300px;
  overflow-y: auto;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  background: #f7fafc;
}

.result-item {
  padding: 12px 16px;
  border-bottom: 1px solid #e2e8f0;
  display: flex;
  align-items: center;
  font-size: 14px;
}

.result-item:last-child {
  border-bottom: none;
}

.result-item.text-success {
  color: #2f855a;
  background: #f0fff4;
}

.result-item.text-danger {
  color: #c53030;
  background: #fed7d7;
}

.result-item.text-info {
  color: #2b6cb0;
  background: #ebf8ff;
}

@media (max-width: 768px) {
  .method-options {
    grid-template-columns: 1fr;
  }

  .template-content {
    flex-direction: column;
    text-align: center;
  }

  .page-title-section {
    flex-direction: column;
    text-align: center;
  }

  .page-title {
    font-size: 24px;
  }
}
</style>
