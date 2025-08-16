<template>
  <div class="hotels-page">
    <!-- Header -->
    <Header />
    <!-- 主要內容 -->
    <main class="main-content">
      <div class="container" style="padding-top: 80px">
        <HotelsSearchBar class="search-panel" />
      </div>
      <div class="container pt-4">
        <!-- Loading state -->
        <div v-if="loading" class="text-center py-5">
          <div class="spinner-border text-primary" role="status">
            <span class="visually-hidden">載入中...</span>
          </div>
          <p class="mt-3">正在載入熱門旅宿...</p>
        </div>

        <!-- Error state -->
        <div v-else-if="error" class="text-center py-5">
          <div class="alert alert-danger" role="alert">
            <h5>載入失敗</h5>
            <p>{{ error.message }}</p>
            <button class="btn btn-primary" @click="refresh">重新載入</button>
          </div>
        </div>

        <!-- Success state -->
        <div v-else class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
          <div class="col" v-for="hotel in allHotels" :key="hotel.id">
            <HotelCard :hotel="hotel" @book="handleBookHotel" />
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import Header from '@/components/Header.vue';
import HotelCard from '@/components/lodging/HotelCard.vue';
import HotelsSearchBar from '@/components/lodging/HotelsSearchBar.vue';
import { useHotLodgings } from '@/composables/useHotLodgings';
import { computed, ref, onMounted } from 'vue';

/* ───────── composable ───────── */
const { hotels, loading, error, refresh } = useHotLodgings();

// 從 localStorage 讀取的房型資料
const roomTypes = ref([]);
const loadingRoomTypes = ref(false);

/* ───────── computed ───────── */
const filteredHotels = computed(() => hotels.value);

// 合併飯店和房型資料
const allHotels = computed(() => {
  const hotelData = hotels.value;
  const roomTypeData = roomTypes.value;

  // 將房型資料轉換為飯店格式
  // 【Max專用】圖片欄位優先順序：imageUrl > images[0] > image，HotelCard.vue 會自動抓 coverImageUrl/imageUrl
  const roomTypeHotels = roomTypeData.map((room) => ({
    id: `room_${room.id}`,
    name: room.name,
    location: `${room.vendorName} 飯店`,
    rating: 0,
    reviewCount: 0,
    price: room.price,
    coverImageUrl: room.imageUrl || (room.images && room.images[0]) || room.image || undefined,
    imageUrl: room.imageUrl || undefined,
    image: room.image || undefined,
    images: room.images || undefined,
    description: room.description,
    guestCount: room.guestCount,
    stock: room.stock,
    isRoomType: true,
    vendorName: room.vendorName
  }));

  return [...hotelData, ...roomTypeHotels];
});

/* ───────── 事件處理 ───────── */
function handleBookHotel(hotel) {
  console.log('Book hotel:', hotel);
  // TODO: 接入訂房邏輯
}

// 載入房型資料
const loadRoomTypes = () => {
  try {
    loadingRoomTypes.value = true;
    const roomTypesData = JSON.parse(localStorage.getItem('roomTypes') || '[]');
    roomTypes.value = roomTypesData.filter((room) => room.isActive);
    console.log('載入房型資料:', roomTypes.value);
  } catch (error) {
    console.error('載入房型資料錯誤:', error);
  } finally {
    loadingRoomTypes.value = false;
  }
};

onMounted(() => {
  loadRoomTypes();
});
</script>

<style scoped>
.hotels-page {
  min-height: 100vh;
  background-color: #f8f9fa;
}
</style>
