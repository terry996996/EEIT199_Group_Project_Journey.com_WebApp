<!--
  飯店商家管理中心
  
  @author Max
  @since 2025-07-08
-->
<template>
  <div class="hotel-vendor-dashboard">
    <!-- 商家 Header -->
    <div class="vendor-header">
      <div class="container">
        <div class="vendor-header-content">
          <div class="vendor-info">
            <div class="vendor-icon">
              <i class="bi bi-building"></i>
            </div>
            <div class="vendor-details">
              <h1 class="vendor-title">飯店商家管理中心</h1>
              <p class="vendor-subtitle">
                歡迎回來，<span class="vendor-name-highlight">{{ username || '商家' }}</span>
              </p>
            </div>
          </div>
          <div class="vendor-actions">
            <button class="btn-home-vendor" @click="goHome">
              <i class="bi bi-house-door-fill"></i>
              首頁
            </button>
            <button @click="handleLogout" class="btn btn-logout-vendor">
              <i class="bi bi-box-arrow-right me-2"></i>
              登出
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="container">
      <!-- 主要功能卡片 -->
      <div class="main-features mb-5">
        <div class="row">
          <div class="col-md-6 mb-4">
            <div class="feature-card" :class="{ active: activeTab === 'upload' }" @click="activeTab = 'upload'">
              <div class="feature-icon">
                <i class="bi bi-cloud-upload"></i>
              </div>
              <div class="feature-content">
                <h5 class="feature-title">商品上傳</h5>
                <p class="feature-description">新增和管理您的飯店商品資訊</p>
              </div>
              <div class="feature-arrow">
                <i class="bi bi-chevron-right"></i>
              </div>
            </div>
          </div>

          <div class="col-md-6 mb-4">
            <div class="feature-card" :class="{ active: activeTab === 'roomTypes' }" @click="activeTab = 'roomTypes'">
              <div class="feature-icon">
                <i class="bi bi-house-door"></i>
              </div>
              <div class="feature-content">
                <h5 class="feature-title">房型管理</h5>
                <p class="feature-description">管理房型、價格和庫存設定</p>
              </div>
              <div class="feature-arrow">
                <i class="bi bi-chevron-right"></i>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 商品上傳頁面 -->
      <div v-if="activeTab === 'upload'" class="content-section">
        <div class="section-header">
          <h2 class="section-title"><i class="bi bi-cloud-upload me-3"></i>商品上傳</h2>
          <p class="section-subtitle">新增和管理您的飯店商品資訊</p>
        </div>
        <VendorUpload vendorType="hotel" :vendorId="vendorId" />
      </div>

      <!-- 房型管理頁面 -->
      <div v-if="activeTab === 'roomTypes'" class="content-section">
        <div class="section-header">
          <h2 class="section-title"><i class="bi bi-house-door me-3"></i>房型管理</h2>
          <p class="section-subtitle">管理您的飯店房型、價格和庫存設定</p>
        </div>
        <div v-if="loadingLodgings" class="loading-state">
          <div class="spinner-border text-primary" role="status">
            <span class="visually-hidden">載入中...</span>
          </div>
          <p class="mt-3 text-muted">正在載入飯店資料...</p>
        </div>
        <div v-else-if="myRoomTypes.length === 0" class="empty-state">
          <div class="empty-icon">
            <i class="bi bi-building"></i>
          </div>
          <h6 class="empty-title">尚未新增任何房型</h6>
          <p class="empty-description">請先新增房型資料</p>
        </div>
        <div v-else>
          <RoomTypeManager :lodgings="myLodgings" :vendorId="vendorId" @edit-room="editRoomType"
            @add-room="openAddRoomTypeModal" @delete-room="deleteRoomType" />
        </div>
      </div>

      <!-- Excel 批量匯入頁面 -->
      <div v-if="activeTab === 'excelImport'" class="content-section">
        <div class="section-header">
          <h2 class="section-title"><i class="bi bi-file-earmark-excel me-3"></i>Excel 批量匯入</h2>
          <p class="section-subtitle">透過 Excel 檔案批量新增房型資料</p>
        </div>
        <ExcelRoomTypeUpload :vendorId="vendorId" />
      </div>

      <!-- 移除訂單管理區塊 -->
    </div>

    <!-- 編輯飯店 Modal -->
    <div v-if="showEditLodgingModal" class="fullscreen-modal" @click="closeEditLodgingModal">
      <div class="fullscreen-modal-content" @click.stop>
        <div class="fullscreen-modal-header">
          <div class="modal-title-section">
            <h3 class="modal-title"><i class="bi bi-pencil-square me-3"></i>編輯飯店資訊</h3>
            <p class="modal-subtitle">修改飯店的基本資訊</p>
          </div>
          <button type="button" class="close-button" @click="closeEditLodgingModal">
            <i class="bi bi-x-lg"></i>
          </button>
        </div>

        <div class="fullscreen-modal-body">
          <form @submit.prevent="updateLodging" class="lodging-form">
            <div class="form-section">
              <h5 class="section-title"><i class="bi bi-info-circle me-2"></i>基本資訊</h5>
              <div class="row">
                <div class="col-lg-6 mb-4">
                  <label class="form-label fw-bold">飯店名稱 *</label>
                  <input v-model="editingLodging.lodgingName" type="text" class="form-control form-control-xl"
                    placeholder="請輸入飯店名稱" required />
                </div>
                <div class="col-lg-6 mb-4">
                  <label class="form-label fw-bold">飯店狀態</label>
                  <div class="form-check form-switch">
                    <input class="form-check-input" type="checkbox" id="lodgingStatus"
                      v-model="editingLodging.isActive" />
                    <label class="form-check-label" for="lodgingStatus">
                      {{ editingLodging.isActive ? '啟用' : '停用' }}
                    </label>
                  </div>
                </div>
              </div>
            </div>

            <div class="form-section">
              <h5 class="section-title"><i class="bi bi-geo-alt me-2"></i>位置資訊</h5>
              <div class="row">
                <div class="col-lg-6 mb-4">
                  <label class="form-label fw-bold">國家 *</label>
                  <input v-model="editingLodging.countryName" type="text" class="form-control form-control-xl"
                    placeholder="請輸入國家名稱" required />
                </div>
                <div class="col-lg-6 mb-4">
                  <label class="form-label fw-bold">城市 *</label>
                  <input v-model="editingLodging.cityName" type="text" class="form-control form-control-xl"
                    placeholder="請輸入城市名稱" required />
                </div>
              </div>
              <div class="row">
                <div class="col-12 mb-4">
                  <label class="form-label fw-bold">詳細地址</label>
                  <input v-model="editingLodging.address" type="text" class="form-control form-control-xl"
                    placeholder="請輸入詳細地址" />
                </div>
              </div>
            </div>
          </form>
        </div>

        <div class="fullscreen-modal-footer">
          <div class="footer-actions">
            <button type="button" class="btn btn-secondary btn-xl" @click="closeEditLodgingModal">
              <i class="bi bi-x-circle me-2"></i>取消
            </button>
            <button type="button" class="btn btn-primary btn-xl" @click="updateLodging" :disabled="submitting">
              <span v-if="submitting" class="spinner-border spinner-border-sm me-2"></span>
              <i v-else class="bi bi-check-circle me-2"></i>
              {{ submitting ? '更新中...' : '更新飯店' }}
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 新增房型 Modal -->
    <div v-if="showAddRoomTypeModal" class="fullscreen-modal" @click="closeAddRoomTypeModal">
      <div class="fullscreen-modal-content" @click.stop>
        <div class="fullscreen-modal-header">
          <div class="modal-title-section">
            <h3 class="modal-title"><i class="bi bi-plus-circle me-3"></i>新增房型</h3>
            <p class="modal-subtitle">請填寫房型的基本資訊</p>
          </div>
          <button type="button" class="close-button" @click="closeAddRoomTypeModal">
            <i class="bi bi-x-lg"></i>
          </button>
        </div>

        <div class="fullscreen-modal-body">
          <form @submit.prevent="addRoomType" class="room-type-form">
            <div class="form-section">
              <h5 class="section-title"><i class="bi bi-info-circle me-2"></i>基本資訊</h5>
              <div class="row">
                <div class="col-lg-6 mb-4">
                  <label class="form-label fw-bold">房型名稱 *</label>
                  <input v-model="newRoomType.name" type="text" class="form-control form-control-xl"
                    placeholder="例如：標準雙人房、豪華套房、總統套房" required />
                </div>
                <div class="col-lg-6 mb-4">
                  <label class="form-label fw-bold">每晚價錢 (NT$) *</label>
                  <div class="input-group input-group-xl">
                    <span class="input-group-text">NT$</span>
                    <input v-model="newRoomType.price" type="number" class="form-control" placeholder="請輸入每晚價格" min="0"
                      step="100" required />
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-lg-6 mb-4">
                  <label class="form-label fw-bold">住宿人數 *</label>
                  <input v-model="newRoomType.guestCount" type="number" class="form-control form-control-xl"
                    placeholder="可容納的住宿人數" min="1" max="10" required />
                </div>
                <div class="col-lg-6 mb-4">
                  <label class="form-label fw-bold">庫存數量 *</label>
                  <input v-model="newRoomType.stock" type="number" class="form-control form-control-xl"
                    placeholder="可用房間數量" min="0" required />
                </div>
              </div>
            </div>

            <div class="form-section">
              <h5 class="section-title"><i class="bi bi-gear me-2"></i>庫存與狀態</h5>
              <div class="row">
                <div class="col-lg-6 mb-4">
                  <label class="form-label fw-bold">庫存數量 *</label>
                  <input v-model="newRoomType.stock" type="number" class="form-control form-control-xl"
                    placeholder="可用房間數量" min="0" required />
                </div>
                <div class="col-lg-6 mb-4">
                  <label class="form-label fw-bold">房型狀態</label>
                  <div class="form-check form-switch">
                    <input class="form-check-input" type="checkbox" id="roomTypeStatus"
                      v-model="newRoomType.isActive" />
                    <label class="form-check-label" for="roomTypeStatus">
                      {{ newRoomType.isActive ? '啟用' : '停用' }}
                    </label>
                  </div>
                </div>
              </div>
            </div>

            <div class="form-section">
              <h5 class="section-title"><i class="bi bi-text-paragraph me-2"></i>房型介紹</h5>
              <div class="mb-4">
                <label class="form-label fw-bold">房型介紹</label>
                <textarea v-model="newRoomType.description" class="form-control form-control-xl" rows="4"
                  placeholder="請描述房型的特色、設施、適合的旅客類型等..."></textarea>
              </div>
            </div>
          </form>
        </div>

        <div class="fullscreen-modal-footer">
          <div class="footer-actions">
            <button type="button" class="btn btn-secondary btn-xl" @click="closeAddRoomTypeModal">
              <i class="bi bi-x-circle me-2"></i>取消
            </button>
            <button type="button" class="btn btn-primary btn-xl" @click="addRoomType" :disabled="submitting">
              <span v-if="submitting" class="spinner-border spinner-border-sm me-2"></span>
              <i v-else class="bi bi-check-circle me-2"></i>
              {{ submitting ? '新增中...' : '新增房型' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import VendorUpload from '../../components/vendor/VendorUpload.vue';
import RoomTypeManager from '../../components/vendor/RoomTypeManager.vue';
import ExcelRoomTypeUpload from '../../components/vendor/ExcelRoomTypeUpload.vue';
import { userAPI } from '../../services/api';
import { useStorageListener } from '@/composables/useStorageListener';
import { useCurrentUser } from '@/composables/useCurrentUser';


const router = useRouter();
const { logout } = useCurrentUser();
const vendorId = ref(localStorage.getItem('vendorId'));
const activeTab = ref('upload');
const username = ref('');
const vendorInfo = ref(null);

// 飯店資料相關
const myLodgings = ref([]);
const loadingLodgings = ref(false);

// 房型管理相關
const myRoomTypes = ref([]);
const availableRoomTypes = ref([]);
const selectedRoomTypes = ref([]);
const showEditModal = ref(false);
const showAddRoomTypeModal = ref(false);
const editingRoomType = ref({});
const filterStatus = ref('all');

// 飯店編輯相關
const showEditLodgingModal = ref(false);
const editingLodging = ref({});
const selectedLodgingId = ref(null);

// 每個飯店的過濾狀態
const lodgingFilters = ref({});

const newRoomType = ref({
  name: '',
  price: '',
  description: '',
  stock: '',
  guestCount: '', // 新增住宿人數欄位
  isActive: true
});

// 使用響應式的 localStorage
const {
  data: roomTypes,
  setData: setRoomTypes,
  removeData: removeRoomTypes
} = useStorageListener('roomTypes', []);

// 監聽 roomTypes 變化，同步到 myRoomTypes
watch(
  roomTypes,
  (newRoomTypes) => {
    if (newRoomTypes && Array.isArray(newRoomTypes)) {
      // 過濾當前商家的房型
      const vendorRoomTypes = newRoomTypes.filter((room) => room.vendorId == vendorId.value);
      myRoomTypes.value = vendorRoomTypes;
      availableRoomTypes.value = vendorRoomTypes.filter((room) => room.isActive);
      console.log('房型資料已同步更新:', vendorRoomTypes);
    }
  },
  { immediate: true }
);

// 監聽分頁切換，切到房型管理時強制同步一次 localStorage，解決第一次新增後需刷新問題
watch(activeTab, (newTab) => {
  if (newTab === 'roomTypes') {
    const roomTypesData = JSON.parse(localStorage.getItem('roomTypes') || '[]');
    setRoomTypes(roomTypesData);
  }
});

// 過濾房型
const filteredRoomTypes = computed(() => {
  switch (filterStatus.value) {
    case 'active':
      return myRoomTypes.value.filter((room) => room.isActive);
    case 'inactive':
      return myRoomTypes.value.filter((room) => !room.isActive);
    default:
      return myRoomTypes.value;
  }
});

// 獲取指定飯店的房型
const getLodgingRoomTypes = (lodgingId) => {
  return myRoomTypes.value.filter((room) => room.lodgingId === lodgingId);
};

// 獲取指定飯店的過濾狀態
const getLodgingFilter = (lodgingId) => {
  return lodgingFilters.value[lodgingId] || 'all';
};

// 設定指定飯店的過濾狀態
const setLodgingFilter = (lodgingId, status) => {
  lodgingFilters.value[lodgingId] = status;
};

// 獲取指定飯店的過濾後房型
const getFilteredLodgingRoomTypes = (lodgingId) => {
  const rooms = getLodgingRoomTypes(lodgingId);
  const filter = getLodgingFilter(lodgingId);

  switch (filter) {
    case 'active':
      return rooms.filter((room) => room.isActive);
    case 'inactive':
      return rooms.filter((room) => !room.isActive);
    default:
      return rooms;
  }
};

// 計算指定飯店的平均價格
const getAveragePrice = (lodgingId) => {
  const rooms = getLodgingRoomTypes(lodgingId);
  if (rooms.length === 0) return 0;

  const totalPrice = rooms.reduce((sum, room) => sum + (room.price || 0), 0);
  return Math.round(totalPrice / rooms.length);
};

const vendorTypeDisplay = computed(() => {
  return '飯店商家';
});

// 獲取商家資訊
const fetchVendorInfo = async () => {
  try {
    const response = await fetch(
      `http://localhost:8080/api/vendor/info?vendorId=${vendorId.value}`
    );
    if (response.ok) {
      const data = await response.json();
      vendorInfo.value = data;
      // vendorName.value = data.vendorName; // This line is removed as per the new_code
      localStorage.setItem('vendorName', data.vendorName);
      console.log('商家資訊載入完成:', data);
    } else {
      console.error('載入商家資訊失敗:', response.status);
      // vendorName.value = '飯店商家'; // This line is removed as per the new_code
    }
  } catch (error) {
    console.error('載入商家資訊錯誤:', error);
    // vendorName.value = '飯店商家'; // This line is removed as per the new_code
  }
};

// 獲取飯店資料
const fetchLodgings = async () => {
  loadingLodgings.value = true;
  try {
    const response = await fetch(
      `http://localhost:8080/api/vendor/lodging?vendorId=${vendorId.value}`
    );
    if (response.ok) {
      const data = await response.json();
      myLodgings.value = data;
      console.log('飯店資料載入完成:', data);
    } else {
      console.error('載入飯店資料失敗:', response.status);
      myLodgings.value = [];
    }
  } catch (error) {
    console.error('載入飯店資料錯誤:', error);
    myLodgings.value = [];
  } finally {
    loadingLodgings.value = false;
  }
};

// 獲取房型資料 (從 localStorage)
const fetchRoomTypes = async () => {
  try {
    // 房型資料現在由 useStorageListener 自動處理
    // 不需要手動載入，因為 watch 會自動同步
    console.log('房型資料載入完成 (響應式 localStorage)');
  } catch (error) {
    console.error('載入房型資料錯誤:', error);
  }
};

// 新增房型 (使用響應式的 localStorage)
const addRoomType = async () => {
  try {
    // 生成唯一ID
    const roomTypeId = Date.now() + Math.random().toString(36).substr(2, 9);

    const roomTypeData = {
      id: roomTypeId,
      name: newRoomType.value.name,
      price: parseInt(newRoomType.value.price),
      description: newRoomType.value.description,
      stock: parseInt(newRoomType.value.stock),
      guestCount: parseInt(newRoomType.value.guestCount),
      isActive: newRoomType.value.isActive,
      vendorId: vendorId.value,
      vendorName: localStorage.getItem('vendorName') || '商家',
      createdAt: new Date().toISOString()
    };

    // 使用響應式的 setData 更新 localStorage
    const existingRoomTypes = roomTypes.value || [];
    const updatedRoomTypes = [...existingRoomTypes, roomTypeData];
    setRoomTypes(updatedRoomTypes);

    // 新增：dispatch localStorageChanged 事件，讓前台即時同步
    window.dispatchEvent(
      new CustomEvent('localStorageChanged', {
        detail: { key: 'roomTypes', value: updatedRoomTypes }
      })
    );

    // 重置表單
    newRoomType.value = {
      name: '',
      price: '',
      description: '',
      stock: '',
      guestCount: '',
      isActive: true
    };

    console.log('新增房型成功 (響應式 localStorage):', roomTypeData);
    alert('房型新增成功！');
    closeAddRoomTypeModal();
  } catch (error) {
    console.error('新增房型錯誤:', error);
    alert('新增房型失敗，請重試');
  }
};

// 編輯房型
const editRoomType = (roomType) => {
  editingRoomType.value = { ...roomType };
  showEditModal.value = true;
};

// 更新房型 (使用響應式的 localStorage)
const updateRoomType = async () => {
  try {
    // 使用響應式的 localStorage 更新
    const existingRoomTypes = roomTypes.value || [];
    const roomIndex = existingRoomTypes.findIndex((room) => room.id === editingRoomType.value.id);

    if (roomIndex !== -1) {
      // 更新房型資料
      const updatedRoomTypes = [...existingRoomTypes];
      updatedRoomTypes[roomIndex] = { ...editingRoomType.value };
      setRoomTypes(updatedRoomTypes);

      console.log('更新房型成功 (響應式 localStorage):', editingRoomType.value);
      alert('房型更新成功！');
      closeEditModal();
    } else {
      alert('找不到要更新的房型');
    }
  } catch (error) {
    console.error('更新房型錯誤:', error);
    alert('更新房型失敗，請重試');
  }
};

// 刪除房型 (使用響應式的 localStorage)
const deleteRoomType = async (roomTypeId) => {
  if (confirm('確定要刪除此房型嗎？此操作無法復原。')) {
    try {
      // 使用響應式的 localStorage 更新
      const existingRoomTypes = roomTypes.value || [];
      const updatedRoomTypes = existingRoomTypes.filter((room) => room.id !== roomTypeId);
      setRoomTypes(updatedRoomTypes);

      // 從已選擇的房型中移除
      selectedRoomTypes.value = selectedRoomTypes.value.filter((id) => id !== roomTypeId);

      console.log('刪除房型成功 (響應式 localStorage):', roomTypeId);
      alert('房型刪除成功！');
    } catch (error) {
      console.error('刪除房型錯誤:', error);
      alert('刪除房型失敗，請重試');
    }
  }
};

// 關閉新增Modal
const closeAddRoomTypeModal = () => {
  showAddRoomTypeModal.value = false;
  // 重置表單
  newRoomType.value = {
    name: '',
    price: '',
    description: '',
    stock: '',
    guestCount: '',
    isActive: true
  };
};

// 關閉編輯Modal
const closeEditModal = () => {
  showEditModal.value = false;
  editingRoomType.value = {};
};

// 編輯飯店
const editLodging = (lodging) => {
  editingLodging.value = { ...lodging };
  showEditLodgingModal.value = true;
};

// 關閉編輯飯店Modal
const closeEditLodgingModal = () => {
  showEditLodgingModal.value = false;
  editingLodging.value = {};
};

// 開啟新增房型Modal
const openAddRoomTypeModal = (lodgingId) => {
  showAddRoomTypeModal.value = true;
  selectedLodgingId.value = lodgingId;
};

// 更新飯店資訊
const updateLodging = async () => {
  try {
    const response = await fetch(
      `http://localhost:8080/api/vendor/lodging/${editingLodging.value.id}`,
      {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(editingLodging.value)
      }
    );
    if (response.ok) {
      // 更新本地資料
      const updated = await response.json();
      const idx = myLodgings.value.findIndex((l) => l.id === updated.id);
      if (idx !== -1) myLodgings.value[idx] = updated;
      showEditLodgingModal.value = false;
      alert('飯店資料已更新！');
    } else {
      alert('更新失敗');
    }
  } catch (e) {
    alert('更新失敗');
  }
};

// 根據ID獲取房型名稱
const getRoomTypeName = (roomTypeId) => {
  const roomType = myRoomTypes.value.find((room) => room.id === roomTypeId);
  return roomType ? roomType.name : '未知房型';
};

// 取得用戶資訊（使用 localStorage 中的 vendorName）
const fetchUserInfo = async () => {
  // 優先使用 localStorage 中的 vendorName
  const vendorName = localStorage.getItem('vendorName');
  if (vendorName) {
    username.value = vendorName;
    console.log('使用 localStorage 中的商家名稱:', vendorName);
  } else {
    // 如果沒有 vendorName，則嘗試從 API 獲取
    try {
      const res = await userAPI.getUserInfo();
      if (res && res.data && res.data.username) {
        username.value = res.data.username;
        localStorage.setItem('username', res.data.username);
      } else {
        username.value = '商家';
      }
    } catch (e) {
      username.value = '商家';
    }
  }
};

const handleLogout = () => {
  // 使用 composable 中的統一登出函數
  logout();
  router.push('/vendor/login');
};

const goHome = () => {
  router.push('/');
};

// 直接用假資料讓 myLodgings 不為空，確保房型管理分頁顯示
myLodgings.value = [
  {
    id: 1,
    lodgingName: '我的飯店',
    countryName: '台灣',
    cityName: '台北',
    address: '測試地址',
    isActive: true
  }
];

// fetchRoomTypes 已經正確讀 localStorage，onMounted 時直接呼叫 fetchRoomTypes 即可
onMounted(() => {
  // 檢查是否已登入
  const isLoggedIn = localStorage.getItem('isVendorLoggedIn');
  vendorId.value = localStorage.getItem('vendorId');
  if (!isLoggedIn) {
    router.push('/vendor/login');
  }
  // 取得 username
  fetchUserInfo();
  // 載入商家資訊、飯店資料和房型資料
  Promise.all([fetchVendorInfo(), fetchLodgings(), fetchRoomTypes()]);
});
</script>

<style scoped>
.hotel-vendor-dashboard {
  min-height: 100vh;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
}

.vendor-header {
  background: linear-gradient(135deg, #0080ff 0%, #005bbd 100%);
  color: white;
  padding: 20px 0;
  margin-bottom: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.vendor-header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 64px;
}

.vendor-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.vendor-icon {
  width: 60px;
  height: 60px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(10px);
}

.vendor-icon i {
  font-size: 28px;
  color: white;
}

.vendor-title {
  font-size: 28px;
  font-weight: 700;
  margin: 0;
  margin-bottom: 4px;
}

.vendor-subtitle {
  color: #ffffff;
  font-size: 16px;
  margin: 0;
  opacity: 0.9;
}

.vendor-actions {
  display: flex;
  gap: 12px;
}

.btn-outline-light {
  border: 2px solid rgba(255, 255, 255, 0.3);
  color: white;
  background: transparent;
  transition: all 0.3s ease;
}

.btn-outline-light:hover {
  background: rgba(220, 53, 69, 0.9);
  border-color: rgba(220, 53, 69, 0.9);
  color: white;
  transform: translateY(-2px);
}

.container {
  padding-top: 0;
}

/* 測試選單樣式 */
.test-menu .card {
  border: 2px solid #ffc107;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(255, 193, 7, 0.2);
}

.test-menu .card-header {
  background: linear-gradient(135deg, #ffc107, #ff8f00);
  border-bottom: 2px solid #ffc107;
}

.test-menu .btn-group .btn {
  border-radius: 8px;
  margin: 0 2px;
  transition: all 0.3s ease;
}

.test-menu .btn-group .btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.test-menu .alert {
  border-radius: 8px;
  border: none;
}

/* 主要功能卡片樣式 */
.main-features {
  padding: 20px;
}

.feature-card {
  background: linear-gradient(135deg, #ffc107 0%, #ff8f00 100%);
  color: white;
  border: none;
  border-radius: 16px;
  padding: 30px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  gap: 20px;
  box-shadow: 0 8px 24px rgba(255, 193, 7, 0.3);
}

.feature-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 16px 40px rgba(255, 193, 7, 0.4);
}

.feature-card.active {
  background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
  box-shadow: 0 8px 24px rgba(0, 123, 255, 0.3);
}

.feature-card.active:hover {
  box-shadow: 0 16px 40px rgba(0, 123, 255, 0.4);
}

.feature-icon {
  width: 60px;
  height: 60px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  backdrop-filter: blur(10px);
  flex-shrink: 0;
}

.feature-content {
  flex: 1;
}

.feature-title {
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 8px;
}

.feature-description {
  color: #f9f9f9;
  font-size: 14px;
  opacity: 0.9;
  margin: 0;
}

.feature-arrow {
  width: 40px;
  height: 40px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(10px);
  flex-shrink: 0;
}

.feature-arrow i {
  font-size: 18px;
  color: white;
}

/* 內容區塊樣式 */
.content-section {
  padding: 20px;
}

.section-header {
  margin-bottom: 30px;
  text-align: center;
}

.section-title {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 12px;
  color: #2c3e50;
}

.section-subtitle {
  color: #6c757d;
  font-size: 16px;
  margin: 0;
}

/* 載入和空狀態樣式 */
.loading-state,
.empty-state {
  text-align: center;
  padding: 40px 20px;
}

.empty-icon {
  font-size: 64px;
  color: #dee2e6;
  margin-bottom: 16px;
}

.empty-title {
  font-size: 18px;
  font-weight: 600;
  color: #6c757d;
  margin-bottom: 8px;
}

.empty-description {
  color: #adb5bd;
  margin-bottom: 20px;
}

/* 漸層背景 */
.bg-gradient-primary {
  background: linear-gradient(135deg, #007bff 0%, #0056b3 100%) !important;
}

.bg-gradient-info {
  background: linear-gradient(135deg, #17a2b8 0%, #138496 100%) !important;
}

/* 房型總覽樣式 */
.room-overview-section {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border-radius: 20px;
  padding: 30px;
  margin-bottom: 30px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.overview-header {
  text-align: center;
  margin-bottom: 30px;
}

.overview-title {
  font-size: 24px;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 8px;
}

.overview-subtitle {
  color: #6c757d;
  font-size: 16px;
  margin: 0;
}

.overview-stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.overview-stat-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  border: none;
}

.overview-stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.overview-stat-card.total-hotels {
  border-left: 4px solid #007bff;
}

.overview-stat-card.total-rooms {
  border-left: 4px solid #28a745;
}

.overview-stat-card.active-rooms {
  border-left: 4px solid #ffc107;
}

.overview-stat-card.total-stock {
  border-left: 4px solid #17a2b8;
}

.overview-stat-card .stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  flex-shrink: 0;
}

.overview-stat-card.total-hotels .stat-icon {
  background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
  color: white;
}

.overview-stat-card.total-rooms .stat-icon {
  background: linear-gradient(135deg, #28a745 0%, #1e7e34 100%);
  color: white;
}

.overview-stat-card.active-rooms .stat-icon {
  background: linear-gradient(135deg, #ffc107 0%, #e0a800 100%);
  color: white;
}

.overview-stat-card.total-stock .stat-icon {
  background: linear-gradient(135deg, #17a2b8 0%, #138496 100%);
  color: white;
}

.overview-stat-card .stat-content {
  flex: 1;
}

.overview-stat-card .stat-number {
  font-size: 28px;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 4px;
}

.overview-stat-card .stat-label {
  font-size: 14px;
  color: #6c757d;
  font-weight: 500;
  margin: 0;
}

/* 飯店管理樣式 */
.hotel-management-section {
  margin-bottom: 20px;
}

.hotel-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: all 0.3s ease;
}

.hotel-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.hotel-card-header {
  background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
  color: white;
  padding: 24px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.hotel-info {
  flex: 1;
}

.hotel-name {
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 8px;
}

.hotel-location,
.hotel-address {
  font-size: 14px;
  opacity: 0.9;
  margin-bottom: 4px;
}

.hotel-actions {
  display: flex;
  flex-direction: column;
  gap: 12px;
  align-items: flex-end;
}

/* 迷你統計卡片 */
.mini-stat-card {
  border-radius: 12px;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.mini-stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
}

.mini-stat-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  backdrop-filter: blur(10px);
  flex-shrink: 0;
}

.mini-stat-content {
  flex: 1;
}

.mini-stat-number {
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 4px;
}

.mini-stat-label {
  font-size: 12px;
  opacity: 0.9;
  margin: 0;
}

/* 飯店統計區域樣式 */
.hotel-stats-section {
  padding: 24px;
  background: #f8f9fa;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
}

/* 房型區域樣式 */
.room-types-section {
  padding: 24px;
  background: white;
  border-top: 1px solid #e9ecef;
}

.room-section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 2px solid #f8f9fa;
}

.room-section-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.room-section-title h5 {
  color: #2c3e50;
  font-weight: 600;
  margin: 0;
}

.room-count-badge {
  background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
  color: white;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
}

.room-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.room-types-grid {
  margin-top: 16px;
}

.empty-room-types {
  text-align: center;
  padding: 40px 20px;
}

.room-type-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
}

.status-badge {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.status-badge.active {
  background: #d4edda;
  color: #155724;
}

.status-badge.inactive {
  background: #f8d7da;
  color: #721c24;
}

.lodging-location,
.lodging-address {
  margin-bottom: 12px;
}

.location-label,
.address-label {
  font-size: 12px;
  color: #6c757d;
  font-weight: 600;
  text-transform: uppercase;
  display: block;
  margin-bottom: 4px;
}

.location-value,
.address-value {
  font-size: 14px;
  color: #2c3e50;
  font-weight: 500;
}

/* 房型統計卡片樣式 */
.stats-section {
  margin-bottom: 30px;
}

.stat-card {
  border-radius: 16px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 20px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  border: none;
}

.stat-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 16px 40px rgba(0, 0, 0, 0.15);
}

.stat-primary {
  background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
  color: white;
}

.stat-success {
  background: linear-gradient(135deg, #28a745 0%, #1e7e34 100%);
  color: white;
}

.stat-warning {
  background: linear-gradient(135deg, #ffc107 0%, #e0a800 100%);
  color: white;
}

.stat-info {
  background: linear-gradient(135deg, #17a2b8 0%, #138496 100%);
  color: white;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  backdrop-filter: blur(10px);
  flex-shrink: 0;
}

.stat-content {
  flex: 1;
}

.stat-number {
  font-size: 32px;
  font-weight: 700;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
  margin: 0;
}

/* 房型網格樣式 */
.room-type-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
  padding: 10px 0;
}

.room-type-card {
  border: 1px solid #e9ecef;
  border-radius: 16px;
  background: white;
  transition: all 0.3s ease;
  overflow: hidden;
  cursor: pointer;
  position: relative;
}

.room-type-card:hover {
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
  transform: translateY(-6px);
  border-color: #007bff;
}

.room-type-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.room-type-card:hover::before {
  opacity: 1;
}

.room-type-card-header {
  padding: 16px 20px;
  border-bottom: 1px solid #f8f9fa;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.room-type-title h6 {
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 4px;
}

.room-type-actions {
  display: flex;
  gap: 8px;
}

.room-type-card-body {
  padding: 20px;
}

.room-type-price,
.room-type-stock {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.price-label,
.stock-label {
  font-size: 14px;
  color: #6c757d;
  font-weight: 500;
}

.price-value {
  font-size: 18px;
  font-weight: 700;
  color: #28a745;
}

.stock-value {
  font-size: 16px;
  font-weight: 600;
  color: #495057;
}

.room-type-description {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #f8f9fa;
}

.description-label {
  font-size: 14px;
  color: #6c757d;
  font-weight: 500;
  display: block;
  margin-bottom: 8px;
}

.description-text {
  font-size: 14px;
  color: #495057;
  line-height: 1.5;
  margin: 0;
}

/* 表單控制項樣式 */
.form-control-xl {
  padding: 12px 16px;
  font-size: 16px;
  border-radius: 10px;
  border: 2px solid #e9ecef;
  transition: all 0.3s ease;
}

.form-control-xl:focus {
  border-color: #007bff;
  box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
}

.input-group-xl .form-control {
  padding: 12px 16px;
  font-size: 16px;
}

.input-group-xl .input-group-text {
  padding: 12px 16px;
  font-size: 16px;
  background: #f8f9fa;
  border: 2px solid #e9ecef;
  border-right: none;
}

/* 全螢幕Modal樣式 */
.fullscreen-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, rgba(0, 0, 0, 0.8) 0%, rgba(0, 0, 0, 0.9) 100%);
  backdrop-filter: blur(10px);
  z-index: 9999;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  animation: modalFadeIn 0.3s ease-out;
}

.fullscreen-modal-content {
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  width: 100%;
  max-width: 900px;
  max-height: 90vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  animation: modalSlideIn 0.3s ease-out;
}

.fullscreen-modal-header {
  background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
  color: white;
  padding: 30px 40px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  position: relative;
}

.modal-title-section h3 {
  font-size: 28px;
  font-weight: 700;
  margin: 0;
  margin-bottom: 8px;
}

.modal-subtitle {
  font-size: 16px;
  opacity: 0.9;
  margin: 0;
}

.close-button {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  color: white;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.close-button:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: scale(1.1);
}

.fullscreen-modal-body {
  padding: 40px;
  overflow-y: auto;
  flex: 1;
}

.room-type-form {
  max-width: 100%;
}

.form-section {
  margin-bottom: 40px;
  padding: 30px;
  background: #f8f9fa;
  border-radius: 15px;
  border-left: 4px solid #007bff;
}

.section-title {
  color: #2c3e50;
  font-weight: 600;
  margin-bottom: 25px;
  font-size: 18px;
}

.form-text {
  margin-top: 8px;
  font-size: 14px;
  color: #6c757d;
}

/* 開發中提示樣式 */
.development-notice {
  text-align: center;
  padding: 40px 20px;
  background: #f8f9fa;
  border-radius: 12px;
  margin-top: 20px;
}

.notice-icon {
  font-size: 48px;
  color: #6c757d;
  margin-bottom: 12px;
}

.notice-title {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 8px;
}

.notice-description {
  font-size: 14px;
  color: #6c757d;
  margin: 0;
}

.fullscreen-modal-footer {
  background: #f8f9fa;
  padding: 30px 40px;
  border-top: 1px solid #e9ecef;
  display: flex;
  justify-content: flex-end;
}

.footer-actions {
  display: flex;
  gap: 15px;
}

.btn-xl {
  padding: 15px 30px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 10px;
  transition: all 0.3s ease;
}

.btn-xl:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

/* 動畫效果 */
@keyframes modalFadeIn {
  from {
    opacity: 0;
  }

  to {
    opacity: 1;
  }
}

@keyframes modalSlideIn {
  from {
    transform: translateY(-50px) scale(0.9);
    opacity: 0;
  }

  to {
    transform: translateY(0) scale(1);
    opacity: 1;
  }
}

/* 房型選擇樣式 */
.room-type-selection {
  max-height: 200px;
  overflow-y: auto;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  padding: 12px;
  background: #f8f9fa;
}

.form-check {
  margin-bottom: 8px;
}

.selected-rooms {
  min-height: 40px;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  padding: 8px;
  background: #f8f9fa;
}

/* 開發中提示樣式 */
.development-notice {
  text-align: center;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 12px;
  margin-top: 20px;
}

.notice-icon {
  font-size: 48px;
  color: #6c757d;
  margin-bottom: 12px;
}

.notice-title {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 8px;
}

.notice-description {
  font-size: 14px;
  color: #6c757d;
  margin: 0;
}

@media (max-width: 768px) {
  .vendor-header-content {
    flex-direction: column;
    gap: 20px;
    text-align: center;
  }

  .vendor-title {
    font-size: 24px;
  }

  .main-features {
    padding: 10px;
  }

  .feature-card {
    border-radius: 8px;
  }

  .room-type-grid {
    grid-template-columns: 1fr;
    gap: 15px;
  }

  .room-type-card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .room-type-actions {
    align-self: flex-end;
  }

  .fullscreen-modal {
    padding: 10px;
  }

  .fullscreen-modal-content {
    max-width: 100%;
    max-height: 95vh;
  }

  .fullscreen-modal-header {
    padding: 20px;
  }

  .modal-title-section h3 {
    font-size: 24px;
  }

  .fullscreen-modal-body {
    padding: 20px;
  }

  .form-section {
    padding: 20px;
    margin-bottom: 20px;
  }

  .section-title {
    font-size: 16px;
  }

  .form-control-xl {
    padding: 14px 16px;
    font-size: 16px;
  }

  .input-group-xl .form-control,
  .input-group-xl .input-group-text {
    padding: 14px 16px;
    font-size: 16px;
  }

  .fullscreen-modal-footer {
    padding: 20px;
  }

  .footer-actions {
    flex-direction: column;
    gap: 10px;
  }

  .btn-xl {
    width: 100%;
    padding: 12px 20px;
  }
}

.vendor-name-highlight {
  color: #ffffff;
  font-weight: bold;
  font-size: 1.2em;
  letter-spacing: 1px;
}

.btn-logout-vendor {
  background: linear-gradient(90deg, #ff5858 0%, #f09819 100%);
  color: #fff;
  border: none;
  font-size: 1.1em;
  font-weight: bold;
  padding: 10px 28px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(255, 88, 88, 0.15);
  transition: all 0.2s;
  margin-left: 16px;
}

.btn-logout-vendor:hover,
.btn-logout-vendor:focus {
  background: linear-gradient(90deg, #f09819 0%, #ff5858 100%);
  color: #fff;
  transform: translateY(-2px) scale(1.04);
  box-shadow: 0 4px 16px rgba(255, 88, 88, 0.25);
}


.btn-home-vendor {
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
  color: #ffffff;
  border: none;
  font-size: 1.1em;
  font-weight: bold;
  padding: 10px 28px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.2);
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  line-height: 1;
  /* 防止 icon 文字高低不齊 */
  cursor: pointer;
}

.btn-home-vendor:hover,
.btn-home-vendor:focus {
  background: linear-gradient(90deg, #764ba2 0%, #667eea 100%);
  transform: translateY(-2px) scale(1.04);
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.3);
}
</style>