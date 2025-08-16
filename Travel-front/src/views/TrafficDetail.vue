<template>
  <div class="traffic-detail-page">
    <!-- Header -->
    <Header />

    <div class="container my-5 pt-4">
      <!-- 交通基本資訊 -->
      <div class="hotel-info mb-4">
        <div class="row">
          <div class="col-md-8">
            <h2 class="mb-3">{{ traffic.name }}</h2>
            <div class="mb-3">
              <span v-for="n in traffic.stars" :key="n" class="text-warning">⭐</span>
              <span class="badge bg-success ms-2">{{ traffic.rating }}</span>
              <span class="ms-2">{{ traffic.reviewCount }}則評價</span>
            </div>
          </div>
          <div class="col-md-4 text-end">
            <div class="fs-3 fw-bold text-primary">NT$ {{ traffic.price }}</div>
            <small class="text-muted">票券單價</small>
          </div>
        </div>
      </div>

      <!-- 交通照片 -->
      <div class="hotel-gallery mb-4">
        <div class="row g-3">
          <div class="col-md-6">
            <img
              :src="traffic.mainImage"
              class="img-fluid rounded w-100"
              style="height: 400px; object-fit: cover"
              :alt="traffic.name"
            />
          </div>
          <div class="col-md-6">
            <div class="row g-3">
              <div class="col-6" v-for="(image, index) in traffic.images" :key="index">
                <img
                  :src="image"
                  class="img-fluid rounded w-100"
                  style="height: 190px; object-fit: cover"
                  :alt="traffic.name"
                />
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="row">
        <div class="col-md-8">
          <!-- 票券特色 -->
          <div class="card mb-4">
            <div class="card-body">
              <h4 class="card-title mb-4">票券特色</h4>
              <div class="notices">
                <ul class="list-unstyled">
                  <li v-for="(notice, index) in traffic.notices" :key="index" class="mb-2">
                    <i class="bi bi-info-circle me-2"></i>
                    {{ notice }}
                  </li>
                </ul>
              </div>
            </div>
          </div>

          <div class="reviews">
            <AllComment />
          </div>
        </div>

        <!-- 預訂區塊 -->
        <div class="col-md-4">
          <div class="card booking-card">
            <div class="card-body">
              <h5 class="card-title mb-4">預定交通券</h5>

              <!-- 日期選擇 -->
              <div class="mb-3">
                <label class="form-label">乘車日期</label>
                <input type="date" class="form-control" v-model="bookingForm.date" />
              </div>
              <br />

              <hr class="my-3" />
              <br />

              <!-- 乘客人數 -->
              <div class="mb-4">
                <label class="form-label">乘客人數</label>
                <div class="passenger-types">
                  <div class="d-flex justify-content-between align-items-center">
                    <div>人數</div>
                    <div class="quantity">
                      <button
                        class="btn btn-sm btn-outline-secondary"
                        @click="updateQuantity(-1)"
                        :disabled="passengerCount <= 0"
                      >
                        -
                      </button>
                      <span class="mx-2">{{ passengerCount }}</span>
                      <button class="btn btn-sm btn-outline-secondary" @click="updateQuantity(1)">
                        +
                      </button>
                    </div>
                  </div>
                </div>
              </div>

              <!-- 總計 -->
              <div class="total-price mb-3">
                <div class="d-flex justify-content-between mb-2"></div>
                <div class="d-flex justify-content-between text-primary">
                  <span class="fw-bold">總計</span>
                  <span class="fw-bold">NT$ {{ calculateTotal() }}</span>
                </div>
              </div>

              <button
                class="btn btn-primary w-100"
                @click="proceedToCheckout"
                :disabled="!isValidBooking"
              >
                立即預訂
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
  import { useRouter } from 'vue-router';
  import Header from '../components/Header.vue';
  import Footer from '../components/Footer.vue';
  import AllComment from '../components/comments/AllComment.vue';

  const router = useRouter();
  const passengerCount = ref(0);

  // 新評論表單
  const newReview = ref({
    rating: 5,
    content: ''
  });

  // 預訂表單
  const bookingForm = ref({
    date: '',
    seatClass: ''
  });

  // 模擬交通數據
  const traffic = ref({
    id: 1,
    name: '全日本鐵路通票 JR Pass',
    stars: 5,
    price: 7500,
    rating: 9.4,
    luggageInfo: '每人可攜帶30公斤以內行李',
    reviewCount: 1280,
    notices: [
      'JR Pass全國版鐵路通票可於連續 7/14/21日內無限次搭乘日本全境JR列車、巴士、渡輪等交通工具。 ',
      'KKday購買全日本鐵路通票享有比官網更便宜的價格，提供郵寄服務，至指定窗口即可快速兌換使用。',
      'KKday提供JR Pass全國版鐵路通票不同座位方案：普通車廂/綠色車廂，可依個人需求自由選擇。',
      '更多訂票教學與購票資訊可參考 JR Pass日本鐵路通票專區。',
      '更多細節可參考 JR PASS全國版攻略。'
    ],

    mainImage:
      'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_22133/20241126072344_t939t/jpg',
    images: [
      'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_22133/20190307094323_tuOSb/jpeg',
      'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_22133/20190307094329_m5ks1/jpeg',
      'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_22133/20190307094338_txhRR/jpeg',
      'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_22133/20190307094317_rXZ5s/jpeg'
    ],
    reviews: [
      {
        id: 1,
        userName: '王小明',
        userAvatar: 'https://i.pravatar.cc/150?img=1',
        rating: 9.5,
        date: '2024-03-15',
        content: '準時準點，服務人員態度良好，座位舒適。'
      },
      {
        id: 2,
        userName: '李小華',
        userAvatar: 'https://i.pravatar.cc/150?img=2',
        rating: 9.0,
        date: '2024-03-10',
        content: '車廂乾淨，空調適中，整體體驗不錯。'
      },
      {
        id: 3,
        userName: '張小芳',
        userAvatar: 'https://i.pravatar.cc/150?img=3',
        rating: 9.8,
        date: '2024-03-05',
        content: '商務艙服務很好，餐點美味，值得推薦。'
      }
    ]
  });

  // 更新乘客數量
  const updateQuantity = (delta) => {
    const newCount = passengerCount.value + delta;
    if (newCount >= 0) passengerCount.value = newCount;
  };

  // 計算總價
  const calculateTotal = () => {
    const basePrice = traffic.value.price || 0;
    return passengerCount.value * basePrice;
  };

  // 前往結帳
  const proceedToCheckout = () => {
    router.push({
      path: '/checkout',
      query: {
        type: 'traffic',
        id: traffic.value.id,
        date: bookingForm.value.date,
        seatClass: bookingForm.value.seatClass,
        ppassengerCount: passengerCount.value
      }
    });
  };
</script>

<style scoped>
  .booking-card {
    border-radius: 12px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  }

  .time-line {
    position: relative;
    padding: 20px 0;
  }

  .time-line .line {
    position: absolute;
    left: 50%;
    top: 0;
    bottom: 0;
    width: 2px;
    background: #dee2e6;
    transform: translateX(-50%);
  }

  .time-line .time {
    background: white;
    padding: 5px;
    position: relative;
    z-index: 1;
  }

  .stops-timeline {
    position: relative;
    padding-left: 20px;
  }

  .stops-timeline::before {
    content: '';
    position: absolute;
    left: 76px;
    top: 10px;
    bottom: 10px;
    width: 2px;
    background: #dee2e6;
  }

  .stop-point {
    width: 12px;
    height: 12px;
    background: #0d6efd;
    border-radius: 50%;
    margin-top: 6px;
    position: relative;
    z-index: 1;
  }

  .rating-input i {
    font-size: 1.5rem;
    margin-right: 5px;
  }

  @media (max-width: 768px) {
    .booking-card {
      position: static !important;
      margin-top: 2rem;
    }
  }
</style>
