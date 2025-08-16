<template>
  <div class="hotel-detail-page">
    <!-- Header -->
    <Header />

    <div class="container my-5 pt-4">
      <!-- Loading state -->
      <div v-if="loading" class="text-center py-5">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">載入中...</span>
        </div>
        <p class="mt-3">正在載入住宿詳情...</p>
      </div>

      <!-- Error state -->
      <div v-else-if="error" class="text-center py-5">
        <div class="alert alert-danger" role="alert">
          <h5>載入失敗</h5>
          <p>{{ error }}</p>
          <div class="mt-3">
            <button class="btn btn-primary" @click="loadLodgingDetail">重新載入</button>
          </div>
        </div>
      </div>

      <!-- Success state -->
      <div v-else-if="hotel">
        <!-- 飯店基本資訊 -->
        <div class="hotel-info mb-4">
          <div class="row">
            <div class="col-md-8">
              <h2 class="mb-3 fw-bold">{{ hotel.lodgingName }}</h2>
              <div class="mb-3">
                <span v-for="n in Math.floor(hotel.avgRating || 0)" :key="n" class="text-warning">⭐</span>
                <span class="badge bg-success ms-2">{{ Number(hotel.avgRating ?? 0).toFixed(1) }}</span>
                <span class="ms-2">{{ hotel.reviewCount || 0 }}則評價</span>
              </div>
              <p class="mb-3">
                <i class="bi bi-geo-alt"></i>
                {{ hotel.address }}
              </p>
              <p class="mb-3 text-muted">
                <i class="bi bi-telephone"></i>
                {{ hotel.lodgingTel }}
                <span class="ms-3">
                  <i class="bi bi-envelope"></i>
                  {{ hotel.email }}
                </span>
              </p>
              <p class="mb-3">{{ hotel.description }}</p>
            </div>

          </div>
        </div>

        <!-- 飯店照片 -->
        <div class="hotel-gallery mb-4" v-if="hotel.lodgingImages && hotel.lodgingImages.length > 0">
          <h4 class="mb-3 fw-bold">飯店照片</h4>
          <div class="row g-3">
            <div class="col-md-6">
              <img :src="hotel.lodgingImages[0]?.imageUrl" class="img-fluid rounded w-100"
                style="height: 400px; object-fit: cover" :alt="hotel.lodgingName" />
            </div>
            <div class="col-md-6" v-if="hotel.lodgingImages.length > 1">
              <div class="row g-3">
                <div class="col-6" v-for="(image) in hotel.lodgingImages.slice(1, 5)" :key="image.imageId">
                  <img :src="image.imageUrl" class="img-fluid rounded w-100" style="height: 190px; object-fit: cover"
                    :alt="hotel.lodgingName" />
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 飯店設施 -->
        <div class="hotel-facilities card mb-4" v-if="hotel.facilities && hotel.facilities.length > 0">
          <div class="card-body">
            <h4 class="card-title mb-4 fw-bold">設施</h4>
            <div class="row">
              <div class="col-md-3 col-6 mb-3" v-for="facility in hotel.facilities" :key="facility.id">
                <div class="d-flex align-items-center">
                  <i class="bi bi-check-circle me-2"></i>
                  {{ facility.name }}
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 房型資訊 -->
        <div class="room-type-info card mb-4" v-if="hotel.roomTypeName">
          <div class="card-body">
            <h4 class="card-title mb-4 fw-bold">房型資訊</h4>
            <div class="row">
              <div class="col-md-4" v-if="hotel.roomTypeImages && hotel.roomTypeImages.length > 0">
                <img :src="hotel.roomTypeImages[0]?.imageUrl" class="img-fluid rounded w-100" :alt="hotel.roomTypeName"
                  style="height: 250px; object-fit: cover" />
              </div>
              <div class="col-md-8">
                <h5 class="card-title">{{ hotel.roomTypeName }}</h5>
                <p class="text-muted">{{ hotel.roomTypeDescription }}</p>

                <div class="room-features mb-3">
                  <div class="d-flex align-items-center mb-2">
                    <i class="bi bi-people me-2"></i>
                    可住{{ hotel.maxGuests }}人
                  </div>
                  <div class="d-flex align-items-center mb-2" v-if="hotel.bedTypes && hotel.bedTypes.length > 0">
                    <i class="bi bi-bed me-2"></i>
                    {{hotel.bedTypes.map(bed => `${bed.name}×${bed.quantity}`).join(', ')}}
                  </div>
                </div>

                <div class="mt-3">
                  <div class="fs-4 fw-bold text-primary mb-2">每晚 NT$ {{ hotel.pricePerNight }}</div>
                  <button class="btn btn-primary" @click="bookRoom">立即預訂</button>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 房型照片 -->
        <div class="room-gallery mb-4" v-if="hotel.roomTypeImages && hotel.roomTypeImages.length > 1">
          <h4 class="mb-3 fw-bold">房型照片</h4>
          <div class="row g-3">
            <div class="col-md-3 col-6" v-for="image in hotel.roomTypeImages.slice(1)" :key="image.imageId">
              <img :src="image.imageUrl" class="img-fluid rounded w-100" style="height: 200px; object-fit: cover"
                :alt="hotel.roomTypeName" />
            </div>
          </div>
        </div>

        <!-- 評論區 -->
        <AllComment :target="{ type: 'LODGING', id: hotel.lodgingId }" />
      </div>
    </div>
  </div>
</template>

<script setup>
  import { ref, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  import { lodgingAPI } from '@/services/api.js';
  import Header from '@/components/Header.vue';
  import AllComment from '@/components/comments/AllComment.vue';
  import dayjs from 'dayjs';
  import { UseOrderItemStore } from '@/stores/orderItem';


  // 定義父層傳進來的 props 的語法

  const orderItemStore = UseOrderItemStore();
  const router = useRouter();

  const props = defineProps({
    id: String,
    roomTypeId: String
  });

  // 響應式數據
  const hotel = ref(null);
  const loading = ref(true);
  const error = ref(null);

  // 載入住宿詳情
  const loadLodgingDetail = async () => {
    try {
      loading.value = true;
      error.value = null;

      console.log('正在載入住宿詳情，ID:', props.id);
      console.log('房型ID:', props.roomTypeId);

      console.log('API 端點:', `/api/lodgings/${props.id}/roomType/${props.roomTypeId}`);
      const response = await lodgingAPI.getRoomTypeDetail(props.id, props.roomTypeId);
      console.log('房型詳情回應:', response);
      hotel.value = response.data;

    } catch (err) {
      console.error('載入住宿詳情失敗:', err);
      console.error('錯誤詳情:', {
        message: err.message,
        status: err.response?.status,
        statusText: err.response?.statusText,
        data: err.response?.data,
        config: err.config
      });
      error.value = err.response?.data?.message || '載入住宿詳情失敗';
    } finally {
      loading.value = false;
    }
  };

  // 預訂房間
  const bookRoom = () => {
    // 從搜尋欄位獲取入住資訊，如果沒有則使用預設值
    const searchData = JSON.parse(sessionStorage.getItem('hotelSearchData') || '{}');
    const defaultCheckIn = dayjs().add(1, 'day').format('YYYY-MM-DD'); // 明天
    const defaultCheckOut = dayjs().add(3, 'day').format('YYYY-MM-DD'); // 後天

    // 跳轉到結帳頁面
    router.push({
      path: '/checkout',
      query: {
        type: 'hotel',
        hotelId: hotel.value.lodgingId,
        roomId: hotel.value.roomTypeId,
        checkIn: searchData.checkIn || defaultCheckIn,
        checkOut: searchData.checkOut || defaultCheckOut,
        guests: searchData.guests || '2'
      }
    });
  };

  // 頁面載入時執行
  onMounted(async () => {
    await loadLodgingDetail();
  });
</script>

<style scoped>
  .room-features i {
    width: 20px;
  }

  .hotel-gallery img,
  .room-gallery img {
    transition: transform 0.3s ease;
  }

  .hotel-gallery img:hover,
  .room-gallery img:hover {
    transform: scale(1.05);
  }

  @media (max-width: 768px) {
    .hotel-gallery .col-md-6:first-child img {
      height: 300px !important;
    }

    .hotel-gallery .col-6 img {
      height: 150px !important;
    }
  }
</style>
