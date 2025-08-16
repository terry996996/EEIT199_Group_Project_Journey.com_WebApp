<!--
  房型管理組件
  
  @author Max
  @since 2025-07-08
-->
<template>
  <div class="room-type-manager">
    <!-- 房型總覽統計 -->
    <div class="room-overview-section">
      <div class="overview-header">
        <h3 class="overview-title"><i class="bi bi-bar-chart me-2"></i>房型總覽</h3>
        <p class="overview-subtitle">快速查看所有飯店的房型統計資訊</p>
      </div>

      <div class="overview-stats-grid">
        <div class="overview-stat-card total-hotels">
          <div class="stat-icon">
            <i class="bi bi-building"></i>
          </div>
          <div class="stat-content">
            <div class="stat-number">{{ lodgings.length }}</div>
            <div class="stat-label">總飯店數</div>
          </div>
        </div>

        <div class="overview-stat-card total-rooms">
          <div class="stat-icon">
            <i class="bi bi-house-door"></i>
          </div>
          <div class="stat-content">
            <div class="stat-number">{{ myRoomTypes.length }}</div>
            <div class="stat-label">總房型數</div>
          </div>
        </div>

        <div class="overview-stat-card active-rooms">
          <div class="stat-icon">
            <i class="bi bi-check-circle"></i>
          </div>
          <div class="stat-content">
            <div class="stat-number">{{ activeRoomTypesCount }}</div>
            <div class="stat-label">啟用房型</div>
          </div>
        </div>

        <div class="overview-stat-card total-stock">
          <div class="stat-icon">
            <i class="bi bi-boxes"></i>
          </div>
          <div class="stat-content">
            <div class="stat-number">
              {{ totalStock }}
            </div>
            <div class="stat-label">總庫存</div>
          </div>
        </div>
      </div>
    </div>
    <!-- 房型卡片區 -->
    <div class="room-types-grid">
      <div v-if="myRoomTypes.length === 0" class="empty-rooms">
        <div class="empty-icon">
          <i class="bi bi-house-door"></i>
        </div>
        <h6>尚未新增任何房型</h6>
        <p>請先新增房型資料</p>
      </div>
      <div v-else class="room-cards">
        <div v-for="room in myRoomTypes" :key="room.id" class="room-card" :class="{ 'inactive-room': !room.isActive }">
          <div class="room-image" v-if="room.imageUrl || (room.images && room.images.length > 0)"
            style="overflow: hidden">
            <img :src="room.imageUrl || (room.images && room.images.length > 0 ? room.images[0] : '')" alt="房型圖片" style="
                width: 100%;
                height: 140px;
                object-fit: cover;
                border-radius: 18px 18px 0 0;
                box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
                transition: transform 0.2s;
              " @mouseover="(e) => (e.target.style.transform = 'scale(1.04)')"
              @mouseleave="(e) => (e.target.style.transform = 'scale(1)')" />
          </div>
          <div class="room-body mt-2" style="padding: 18px 20px 16px 20px">
            <div class="d-flex justify-content-between align-items-center mb-2">
              <h6 style="font-size: 1.1em; font-weight: 700; color: #2c3e50; margin-bottom: 0">
                {{ room.name }}
              </h6>
              <div class="d-flex gap-2">
                <span class="status-badge" :class="room.isActive ? 'active' : 'inactive'">
                  {{ room.isActive ? '啟用' : '停用' }}
                </span>
                <button class="btn btn-sm" :class="room.isActive ? 'btn-outline-warning' : 'btn-outline-success'"
                  @click="toggleRoomStatus(room)" :title="room.isActive ? '關閉房型' : '啟用房型'">
                  <i :class="room.isActive ? 'bi bi-pause-circle' : 'bi bi-play-circle'"></i>
                  {{ room.isActive ? '關閉' : '啟用' }}
                </button>
              </div>
            </div>
            <div class="mb-2" style="color: #28a745; font-size: 1.1em; font-weight: 700">
              <i class="bi bi-currency-dollar"></i> NT$ {{ room.price }}
            </div>
            <div class="mb-1">
              <i class="bi bi-people me-1"></i><strong>住宿人數：</strong>{{ room.guestCount }}
            </div>
            <div class="mb-1">
              <i class="bi bi-boxes me-1"></i><strong>庫存：</strong>{{ room.stock }}
            </div>
            <div class="mb-2 room-description" style="
                border-top: 1px solid #f1f3f4;
                margin-top: 10px;
                padding-top: 10px;
                color: #495057;
                font-size: 0.98em;
              ">
              <i class="bi bi-info-circle me-1"></i><strong>介紹：</strong>{{ room.description }}
            </div>
            <div class="d-flex justify-content-end">
              <button class="btn btn-sm btn-outline-danger" @click="deleteRoomType(room.id)">
                <i class="bi bi-trash"></i> 刪除
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useStorageListener } from '@/composables/useStorageListener';

const props = defineProps({
  lodgings: {
    type: Array,
    default: () => []
  },
  vendorId: {
    type: String,
    default: ''
  }
});

const emit = defineEmits(['edit-room', 'add-room', 'delete-room']);

// 使用響應式的 localStorage
const { data: roomTypes, setData: setRoomTypes } = useStorageListener('roomTypes', []);

// 過濾當前商家的房型
const myRoomTypes = computed(() => {
  return roomTypes.value.filter((room) => room.vendorId == props.vendorId);
});

// 過濾狀態
const filters = ref({});

// 計算屬性
const activeRoomTypesCount = computed(() => {
  return myRoomTypes.value.filter((room) => room.isActive).length;
});

const totalStock = computed(() => {
  return myRoomTypes.value.reduce((sum, room) => sum + (room.stock || 0), 0);
});

// 方法
const getLodgingRoomCount = (lodgingId) => {
  return myRoomTypes.value.filter((room) => room.lodgingId === lodgingId).length;
};

const getActiveRoomCount = (lodgingId) => {
  return myRoomTypes.value.filter((room) => room.lodgingId === lodgingId && room.isActive).length;
};

const getTotalStock = (lodgingId) => {
  return myRoomTypes.value
    .filter((room) => room.lodgingId === lodgingId)
    .reduce((sum, room) => sum + (room.stock || 0), 0);
};

const getAveragePrice = (lodgingId) => {
  const rooms = myRoomTypes.value.filter((room) => room.lodgingId === lodgingId);
  if (rooms.length === 0) return 0;

  const totalPrice = rooms.reduce((sum, room) => sum + (room.price || 0), 0);
  return Math.round(totalPrice / rooms.length);
};

const getCurrentFilter = (lodgingId) => {
  return filters.value[lodgingId] || 'all';
};

const setFilter = (lodgingId, filter) => {
  filters.value[lodgingId] = filter;
};

const getFilteredRooms = (lodgingId) => {
  const rooms = myRoomTypes.value.filter((room) => room.lodgingId === lodgingId);
  const filter = getCurrentFilter(lodgingId);

  switch (filter) {
    case 'active':
      return rooms.filter((room) => room.isActive);
    case 'inactive':
      return rooms.filter((room) => !room.isActive);
    default:
      return rooms;
  }
};

const editRoomType = (room) => {
  emit('edit-room', room);
};

const addRoomType = (lodgingId) => {
  emit('add-room', lodgingId);
};

// 切換房型狀態
function toggleRoomStatus(room) {
  const action = room.isActive ? '關閉' : '啟用';
  if (!confirm(`確定要${action}房型「${room.name}」嗎？`)) return;

  try {
    // 直接使用響應式的 localStorage 更新
    const existingRoomTypes = roomTypes.value || [];
    const updatedRoomTypes = existingRoomTypes.map((r) => {
      if (r.id === room.id) {
        return { ...r, isActive: !r.isActive };
      }
      return r;
    });
    setRoomTypes(updatedRoomTypes);

    console.log(`房型${action}成功 (響應式 localStorage):`, room.id);
  } catch (error) {
    console.error(`${action}房型錯誤:`, error);
    alert(`${action}房型失敗，請重試`);
  }
}

// 刪除房型，直接使用響應式的 localStorage
function deleteRoomType(roomId) {
  if (!confirm('確定要刪除此房型？')) return;

  try {
    // 直接使用響應式的 localStorage 更新
    const existingRoomTypes = roomTypes.value || [];
    const updatedRoomTypes = existingRoomTypes.filter((r) => r.id !== roomId);
    setRoomTypes(updatedRoomTypes);

    console.log('房型刪除成功 (響應式 localStorage):', roomId);
  } catch (error) {
    console.error('刪除房型錯誤:', error);
    alert('刪除房型失敗，請重試');
  }
}
</script>

<style scoped>
.room-type-manager {
  max-width: 1200px;
  margin: 0 auto;
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

/* 飯店卡片樣式 */
.hotel-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  margin-bottom: 24px;
  transition: all 0.3s ease;
}

.hotel-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.hotel-header {
  background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
  color: white;
  padding: 24px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
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

/* 飯店統計樣式 */
.hotel-stats {
  padding: 24px;
  background: #f8f9fa;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
}

.stat-item {
  background: white;
  border-radius: 12px;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.stat-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.stat-item .stat-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: rgba(0, 123, 255, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  color: #007bff;
  flex-shrink: 0;
}

.stat-item .stat-content {
  flex: 1;
}

.stat-item .stat-number {
  font-size: 20px;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 4px;
}

.stat-item .stat-label {
  font-size: 12px;
  color: #6c757d;
  margin: 0;
}

/* 房型區域樣式 */
.room-types-section {
  padding: 24px;
  background: white;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 2px solid #f8f9fa;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.section-title h5 {
  color: #2c3e50;
  font-weight: 600;
  margin: 0;
}

.room-count {
  background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
  color: white;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
}

.section-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.filter-buttons {
  display: flex;
  gap: 8px;
}

/* 房型卡片樣式 */
.room-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 16px;
}

.room-card {
  border: 1px solid #e9ecef;
  border-radius: 16px;
  background: white;
  transition: all 0.3s ease;
  overflow: hidden;
  cursor: pointer;
  position: relative;
}

.room-card:hover {
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
  transform: translateY(-6px);
  border-color: #007bff;
}

.room-card::before {
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

.room-card:hover::before {
  opacity: 1;
}

/* 關閉房型的樣式 */
.room-card.inactive-room {
  opacity: 0.6;
  filter: grayscale(0.3);
  background: #f8f9fa;
}

.room-card.inactive-room:hover {
  opacity: 0.8;
  filter: grayscale(0.1);
  transform: translateY(-2px);
}

.room-card.inactive-room .room-body {
  background: #f8f9fa;
}

.room-card.inactive-room .status-badge.inactive {
  background: #f8d7da;
  color: #721c24;
}

.room-header {
  padding: 16px 20px;
  border-bottom: 1px solid #f8f9fa;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.room-title h6 {
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 4px;
}

.room-actions {
  display: flex;
  gap: 8px;
}

.room-body {
  padding: 20px;
}

.room-info {
  margin-bottom: 16px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.info-item .label {
  font-size: 14px;
  color: #6c757d;
  font-weight: 500;
}

.info-item .value {
  font-size: 16px;
  font-weight: 600;
  color: #495057;
}

.info-item .value.price {
  color: #28a745;
  font-weight: 700;
}

.room-description {
  padding-top: 16px;
  border-top: 1px solid #f8f9fa;
}

.room-description .label {
  font-size: 14px;
  color: #6c757d;
  font-weight: 500;
  display: block;
  margin-bottom: 8px;
}

.room-description .description {
  font-size: 14px;
  color: #495057;
  line-height: 1.5;
  margin: 0;
}

/* 空狀態樣式 */
.empty-rooms {
  text-align: center;
  padding: 40px 20px;
}

.empty-icon {
  font-size: 64px;
  color: #dee2e6;
  margin-bottom: 16px;
}

.empty-rooms h6 {
  font-size: 18px;
  font-weight: 600;
  color: #6c757d;
  margin-bottom: 8px;
}

.empty-rooms p {
  color: #adb5bd;
  margin-bottom: 20px;
}

/* 響應式設計 */
@media (max-width: 768px) {
  .overview-stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .room-cards {
    grid-template-columns: 1fr;
  }

  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .section-actions {
    width: 100%;
    justify-content: space-between;
  }
}
</style>