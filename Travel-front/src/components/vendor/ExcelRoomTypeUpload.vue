<!--
  Excel 批量匯入房型組件
  
  @author Max
  @since 2025-01-XX
-->
<template>
  <div class="excel-upload-container">
    <div class="upload-section">
      <div class="upload-header">
        <h5 class="upload-title">
          <i class="bi bi-file-earmark-excel me-2"></i>
          Excel 批量匯入房型
        </h5>
        <p class="upload-subtitle">透過 Excel 檔案批量新增房型資料</p>
      </div>

      <!-- 下載範本 -->
      <div class="template-section mb-4">
        <div class="template-card">
          <div class="template-icon">
            <i class="bi bi-download"></i>
          </div>
          <div class="template-content">
            <h6 class="template-title">下載 Excel 範本</h6>
            <p class="template-description">包含所有必要欄位的標準格式</p>
            <button @click="downloadTemplate" class="btn btn-outline-primary btn-sm">
              <i class="bi bi-file-earmark-excel me-1"></i>
              下載範本
            </button>
          </div>
        </div>
      </div>

      <!-- 檔案上傳 -->
      <div class="file-upload-section mb-4">
        <div
          class="upload-area"
          :class="{ 'drag-over': isDragOver }"
          @drop="handleFileDrop"
          @dragover.prevent="isDragOver = true"
          @dragleave.prevent="isDragOver = false"
        >
          <div class="upload-content">
            <i class="bi bi-cloud-upload upload-icon"></i>
            <h6 class="upload-text">拖拽 Excel 檔案到這裡或點擊選擇檔案</h6>
            <p class="upload-hint">支援 .xlsx, .xls 格式</p>
            <input
              ref="fileInput"
              type="file"
              accept=".xlsx,.xls"
              @change="handleFileSelect"
              class="file-input"
            />
            <button @click="$refs.fileInput.click()" class="btn btn-primary">
              <i class="bi bi-folder2-open me-2"></i>
              選擇檔案
            </button>
          </div>
        </div>
      </div>

      <!-- 預覽資料 -->
      <div v-if="previewData.length > 0" class="preview-section">
        <div class="preview-header">
          <h6 class="preview-title">
            <i class="bi bi-eye me-2"></i>
            預覽資料 ({{ previewData.length }} 筆)
          </h6>
          <div class="preview-actions">
            <button @click="clearPreview" class="btn btn-outline-secondary btn-sm">
              <i class="bi bi-x-circle me-1"></i>
              清除
            </button>
          </div>
        </div>

        <div class="table-responsive">
          <table class="table table-striped table-hover">
            <thead class="table-dark">
              <tr>
                <th>房型名稱</th>
                <th>每晚價格</th>
                <th>住宿人數</th>
                <th>庫存</th>
                <th>房型介紹</th>
                <th>房型圖片</th>
                <th>驗證</th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="(row, index) in previewData"
                :key="index"
                :class="{
                  'table-danger': row.errors.length > 0,
                  'table-success': row.errors.length === 0
                }"
              >
                <td>{{ row.name }}</td>
                <td>{{ row.price }}</td>
                <td>{{ row.guestCount }}</td>
                <td>{{ row.stock }}</td>
                <td>{{ row.description }}</td>
                <td>
                  <img
                    v-if="row.imageUrl"
                    :src="row.imageUrl"
                    alt="房型圖片"
                    style="max-width: 60px; max-height: 40px; object-fit: cover; border-radius: 4px"
                  />
                  <span v-else class="text-muted">無</span>
                </td>
                <td>
                  <div v-if="row.errors.length > 0" class="error-list">
                    <small v-for="error in row.errors" :key="error" class="text-danger d-block">
                      {{ error }}
                    </small>
                  </div>
                  <span v-else class="text-success">
                    <i class="bi bi-check-circle"></i>
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- 匯入按鈕 -->
        <div class="import-actions">
          <div class="import-summary">
            <span class="text-success">
              <i class="bi bi-check-circle me-1"></i>
              有效資料: {{ validCount }} 筆
            </span>
            <span v-if="errorCount > 0" class="text-danger ms-3">
              <i class="bi bi-exclamation-triangle me-1"></i>
              錯誤資料: {{ errorCount }} 筆
            </span>
          </div>
          <button @click="importData" :disabled="validCount === 0" class="btn btn-success">
            <i class="bi bi-upload me-2"></i>
            匯入有效資料 ({{ validCount }} 筆)
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import * as XLSX from 'xlsx';

  export default {
    name: 'ExcelRoomTypeUpload',
    props: {
      vendorId: {
        type: [String, Number],
        required: true
      }
    },
    data() {
      return {
        isDragOver: false,
        previewData: [],
        vendorName: localStorage.getItem('vendorName') || '商家'
      };
    },
    computed: {
      validCount() {
        return this.previewData.filter((row) => row.errors.length === 0).length;
      },
      errorCount() {
        return this.previewData.filter((row) => row.errors.length > 0).length;
      }
    },
    methods: {
      // 下載 Excel 範本
      downloadTemplate() {
        const templateData = [
          {
            name: '標準雙人房',
            price: 2000,
            guestCount: 2,
            stock: 10,
            description: '舒適的雙人房，配備基本設施',
            isActive: true
          },
          {
            name: '豪華套房',
            price: 5000,
            guestCount: 4,
            stock: 5,
            description: '寬敞的套房，適合家庭入住',
            isActive: true
          }
        ];

        const ws = XLSX.utils.json_to_sheet(templateData);
        const wb = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(wb, ws, '房型範本');

        // 設定欄位標題
        const headers = ['房型名稱', '價格', '住宿人數', '庫存', '描述', '狀態'];
        XLSX.utils.sheet_add_aoa(ws, [headers], { origin: 'A1' });

        XLSX.writeFile(wb, '房型匯入範本.xlsx');
      },

      // 處理檔案拖拽
      handleFileDrop(event) {
        this.isDragOver = false;
        const files = event.dataTransfer.files;
        if (files.length > 0) {
          this.processFile(files[0]);
        }
      },

      // 處理檔案選擇
      handleFileSelect(event) {
        const files = event.target.files;
        if (files.length > 0) {
          this.processFile(files[0]);
        }
      },

      // 處理 Excel 檔案
      processFile(file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          try {
            const data = new Uint8Array(e.target.result);
            const workbook = XLSX.read(data, { type: 'array' });
            const sheetName = workbook.SheetNames[0];
            const worksheet = workbook.Sheets[sheetName];

            // 轉換為 JSON
            const jsonData = XLSX.utils.sheet_to_json(worksheet, { header: 1 });

            // 處理資料
            this.parseExcelData(jsonData);
          } catch (error) {
            console.error('處理 Excel 檔案錯誤:', error);
            alert('處理 Excel 檔案時發生錯誤，請確認檔案格式正確');
          }
        };
        reader.readAsArrayBuffer(file);
      },

      // 解析 Excel 資料
      parseExcelData(jsonData) {
        if (jsonData.length < 2) {
          alert('Excel 檔案格式錯誤，至少需要標題列和一筆資料');
          return;
        }

        const headers = jsonData[0];
        const dataRows = jsonData.slice(1);

        // 新欄位對應
        const idx = {
          name: headers.indexOf('房型名稱'),
          price: headers.indexOf('每晚價格'),
          guestCount: headers.indexOf('住宿人數'),
          stock: headers.indexOf('庫存'),
          description: headers.indexOf('房型介紹'),
          imageUrl: headers.indexOf('房型圖片')
        };

        // 驗證標題列
        const requiredHeaders = ['房型名稱', '每晚價格', '住宿人數', '庫存', '房型介紹'];
        const missingHeaders = requiredHeaders.filter(
          (h) => idx[h.replace('每晚價格', 'price').replace('房型介紹', 'description')] === -1
        );

        if (missingHeaders.length > 0) {
          alert(`Excel 檔案缺少必要欄位: ${missingHeaders.join(', ')}`);
          return;
        }

        // 處理每一行資料
        this.previewData = dataRows.map((row, index) => {
          const rowData = {
            name: row[idx.name] || '',
            price: row[idx.price] || 0,
            guestCount: row[idx.guestCount] || 0,
            stock: row[idx.stock] || 0,
            description: row[idx.description] || '',
            imageUrl: row[idx.imageUrl] || '',
            isActive: true, // 預設啟用
            errors: []
          };

          // 驗證資料
          this.validateRow(rowData, index + 2);

          return rowData;
        });
      },

      // 解析布林值
      parseBoolean(value) {
        if (typeof value === 'boolean') return value;
        if (typeof value === 'string') {
          const lowerValue = value.toLowerCase();
          return (
            lowerValue === 'true' ||
            lowerValue === '是' ||
            lowerValue === '啟用' ||
            lowerValue === '1'
          );
        }
        if (typeof value === 'number') return value === 1;
        return true; // 預設為啟用
      },

      // 驗證單行資料
      validateRow(rowData, rowNumber) {
        if (!rowData.name || rowData.name.trim() === '') {
          rowData.errors.push('房型名稱不能為空');
        }

        if (!rowData.price || isNaN(rowData.price) || rowData.price <= 0) {
          rowData.errors.push('價格必須為正數');
        }

        if (!rowData.guestCount || isNaN(rowData.guestCount) || rowData.guestCount <= 0) {
          rowData.errors.push('住宿人數必須為正數');
        }

        if (!rowData.stock || isNaN(rowData.stock) || rowData.stock < 0) {
          rowData.errors.push('庫存不能為負數');
        }

        if (rowData.description && rowData.description.length > 500) {
          rowData.errors.push('介紹不能超過 500 字元');
        }

        if (
          rowData.imageUrl &&
          typeof rowData.imageUrl === 'string' &&
          !/^https?:\/\//.test(rowData.imageUrl)
        ) {
          rowData.errors.push('圖片必須為有效的網址');
        }
      },

      // 匯入資料
      importData() {
        const validData = this.previewData.filter((row) => row.errors.length === 0);

        if (validData.length === 0) {
          alert('沒有有效的資料可以匯入');
          return;
        }

        try {
          // 獲取現有的房型資料
          const existingRoomTypes = JSON.parse(localStorage.getItem('roomTypes') || '[]');

          // 準備新資料
          const newRoomTypes = validData.map((row) => ({
            id: Date.now() + Math.random().toString(36).substr(2, 9),
            name: row.name.trim(),
            price: parseInt(row.price),
            description: row.description.trim(),
            stock: parseInt(row.stock),
            guestCount: parseInt(row.guestCount),
            imageUrl: row.imageUrl,
            isActive: true,
            vendorId: this.vendorId,
            vendorName: this.vendorName,
            createdAt: new Date().toISOString()
          }));

          // 合併資料
          const updatedRoomTypes = [...existingRoomTypes, ...newRoomTypes];
          localStorage.setItem('roomTypes', JSON.stringify(updatedRoomTypes));

          // 觸發自定義事件通知其他組件
          window.dispatchEvent(
            new CustomEvent('localStorageChanged', {
              detail: { key: 'roomTypes', value: updatedRoomTypes }
            })
          );

          alert(`成功匯入 ${validData.length} 筆房型資料！`);
          this.clearPreview();

          // 清空檔案輸入
          if (this.$refs.fileInput) {
            this.$refs.fileInput.value = '';
          }
        } catch (error) {
          console.error('匯入資料錯誤:', error);
          alert('匯入資料時發生錯誤，請重試');
        }
      },

      // 清除預覽
      clearPreview() {
        this.previewData = [];
      }
    }
  };
</script>

<style scoped>
  .excel-upload-container {
    background: #fff;
    border-radius: 12px;
    padding: 24px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }

  .upload-header {
    margin-bottom: 24px;
    text-align: center;
  }

  .upload-title {
    color: #2c3e50;
    margin-bottom: 8px;
  }

  .upload-subtitle {
    color: #6c757d;
    margin-bottom: 0;
  }

  .template-section {
    margin-bottom: 24px;
  }

  .template-card {
    display: flex;
    align-items: center;
    padding: 16px;
    background: #f8f9fa;
    border-radius: 8px;
    border: 1px solid #dee2e6;
  }

  .template-icon {
    margin-right: 16px;
    font-size: 24px;
    color: #28a745;
  }

  .template-content {
    flex: 1;
  }

  .template-title {
    margin-bottom: 4px;
    color: #2c3e50;
  }

  .template-description {
    margin-bottom: 8px;
    color: #6c757d;
    font-size: 14px;
  }

  .file-upload-section {
    margin-bottom: 24px;
  }

  .upload-area {
    border: 2px dashed #dee2e6;
    border-radius: 12px;
    padding: 40px;
    text-align: center;
    transition: all 0.3s ease;
    background: #f8f9fa;
  }

  .upload-area.drag-over {
    border-color: #007bff;
    background: #e3f2fd;
  }

  .upload-content {
    position: relative;
  }

  .upload-icon {
    font-size: 48px;
    color: #6c757d;
    margin-bottom: 16px;
  }

  .upload-text {
    color: #2c3e50;
    margin-bottom: 8px;
  }

  .upload-hint {
    color: #6c757d;
    margin-bottom: 16px;
    font-size: 14px;
  }

  .file-input {
    position: absolute;
    opacity: 0;
    pointer-events: none;
  }

  .preview-section {
    margin-top: 24px;
  }

  .preview-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
  }

  .preview-title {
    color: #2c3e50;
    margin-bottom: 0;
  }

  .preview-actions {
    display: flex;
    gap: 8px;
  }

  .table-responsive {
    margin-bottom: 16px;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }

  .table th {
    background: #343a40;
    color: white;
    border: none;
    padding: 12px 8px;
    font-size: 14px;
  }

  .table td {
    padding: 12px 8px;
    vertical-align: middle;
    font-size: 14px;
  }

  .error-list {
    font-size: 12px;
  }

  .import-actions {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px;
    background: #f8f9fa;
    border-radius: 8px;
    border: 1px solid #dee2e6;
  }

  .import-summary {
    display: flex;
    align-items: center;
  }

  .import-summary span {
    font-size: 14px;
    font-weight: 500;
  }

  .btn {
    border-radius: 6px;
    font-weight: 500;
    padding: 8px 16px;
  }

  .btn-success {
    background: #28a745;
    border-color: #28a745;
  }

  .btn-success:hover {
    background: #218838;
    border-color: #1e7e34;
  }

  .btn-outline-primary {
    border-color: #007bff;
    color: #007bff;
  }

  .btn-outline-primary:hover {
    background: #007bff;
    border-color: #007bff;
  }

  .btn-outline-secondary {
    border-color: #6c757d;
    color: #6c757d;
  }

  .btn-outline-secondary:hover {
    background: #6c757d;
    border-color: #6c757d;
  }

  @media (max-width: 768px) {
    .excel-upload-container {
      padding: 16px;
    }

    .upload-area {
      padding: 24px 16px;
    }

    .import-actions {
      flex-direction: column;
      gap: 12px;
      align-items: stretch;
    }

    .import-summary {
      justify-content: center;
    }
  }
</style>
