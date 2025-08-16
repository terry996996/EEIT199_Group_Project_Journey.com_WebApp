<template>
  <div class="checkout-page">
    <!-- Header -->
    <Header />

    <div class="container my-5 pt-4">
      <div class="row">
        <!-- 結帳表單 -->
        <div class="col-md-8">
          <div class="card mb-4">
            <div class="card-body">
              <form @submit.prevent="handleSubmit">
                <div class="mb-4">
                  <label class="form-label">
                    <h5>選擇旅程</h5>
                  </label>
                  <select class="form-select" v-model="form.tripId" required>
                    <option v-for="plan in sortedPlans" :key="plan.id" :value="plan.id">
                      {{ plan.title }}
                    </option>
                  </select>
                </div>
                <h5 class="card-title mb-3">付款人資訊
                </h5>
                <!-- 聯絡人資訊 -->
                <div class="mb-5">
                  <div class="row g-3">
                    <div class="col-md-6">
                      <label class="form-label">姓名</label>
                      <input type="text" class="form-control" v-model="form.fullName" required />
                    </div>
                    <div class="col-md-6">
                      <label class="form-label">電話</label>
                      <input type="tel" class="form-control" v-model="form.telNumber" required />
                    </div>
                    <div class="col-12">
                      <label class="form-label">Email</label>
                      <input type="email" class="form-control" v-model="form.email" required />
                    </div>
                  </div>
                </div>

                <!-- 住房資訊 -->
                <div class="mb-4">
                  <h5 class="mb-3">住房資訊</h5>
                  <div class="card mb-3">
                    <div class="card-body">
                      <div class="row g-3">
                        <div class="col-md-6">
                          <div>
                            <h5 class="mb-1" style="font-weight: bold;">{{ details.lodgingName }}</h5>
                            <span>{{ details.roomTypeName }}</span>
                          </div>
                        </div>
                        <div class="col-md-6 ">
                          <div class="link-primary lnk mb-1" style="cursor: pointer;"
                            @click.stop.prevent="goToDetailPage">
                            <i class="bi bi-arrow-up-right-square me-2">
                            </i>
                            <span style="font-size: 14px;">點擊前往房型資訊頁面</span>
                          </div>
                          <span>每晚 NT$ {{ details.pricePerNight }}</span>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">入住日期</label>
                          <input type="date" class="form-control" v-model="form.checkIn" :min="startDate"
                            :max="endDate" />
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">退房日期</label>
                          <input type="date" class="form-control" v-model="form.checkOut" :min="startDate"
                            :max="endDate" />
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">入住人數</label>
                          <input type="number" class="form-control" v-model="form.guests" :max="details.maxGuests">
                        </div>
                      </div>
                    </div>
                  </div>
                </div>


                <!-- 票價總計 -->
                <div class="mb-4">
                  <h5 class="mb-3">價格明細</h5>
                  <div class="card mb-3">
                    <div class="card-body">
                      <div class="row g-3">
                        <div class="col-md-12">
                          <div class="section-header position-relative"
                            style=" padding-bottom: 0px; margin-bottom:10px;">
                            <h5 class="form-label">金額計算</h5>
                          </div>
                          <div>每晚 NT$ {{ details.pricePerNight }}</div>
                          <div>× {{ dateDiff }} 晚</div>
                          <div class="mt-4">小計：{{ form.subtotal }}</div>
                          <div>優惠：</div>
                          <hr class="mt-2">
                          <div class="mt-4" style="font-weight: bold;">總計：
                            <span class="text-primary">NT$ {{ form.total }}</span>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- 付款方式 -->
                <div class="mb-4">
                  <h5 class="mb-3">付款方式</h5>
                  <div class="payment-methods">
                    <div class="form-check mb-2">
                      <input class="form-check-input" type="radio" name="paymentMethod" id="creditCard" value=1
                        v-model="form.paymentMethod" />
                      <label class="form-check-label" for="creditCard">
                        <i class="bi bi-credit-card me-2"></i>
                        信用卡付款
                      </label>
                    </div>
                    <div class="form-check mb-2">
                      <input class="form-check-input" type="radio" name="paymentMethod" id="linePay" value=2
                        v-model="form.paymentMethod" />
                      <label class="form-check-label" for="linePay">
                        <i class="bi bi-wallet2 me-2"></i>
                        LINE Pay
                      </label>
                    </div>
                    <div class="form-check mb-2">
                      <input class="form-check-input" type="radio" name="paymentMethod" id="transfer" value=3
                        v-model="form.paymentMethod" />
                      <label class="form-check-label" for="transfer">
                        <i class="bi bi-bank me-2"></i>
                        銀行轉帳
                      </label>
                    </div>
                  </div>

                  <!-- 信用卡資訊 -->
                  <div v-if="form.paymentMethod === 1" class="card mt-3">
                    <div class="card-body">
                      <div class="row g-3">
                        <div class="col-12">
                          <label class="form-label">卡號</label>
                          <input type="text" class="form-control" v-model="form.cardNumber"
                            placeholder="1234 5678 9012 3456" required />
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">有效期限</label>
                          <input type="month" class="form-control" v-model="form.cardExpiry" :min="today.slice(0, 7)" />
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">安全碼</label>
                          <input type="text" class="form-control" v-model="form.cardCVC" placeholder="CVC" required />
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- 發票資訊 -->
                <div class="mb-4">
                  <h5 class="mb-3">發票資訊</h5>
                  <div class="row g-3">
                    <div class="col-md-6">
                      <label class="form-label">發票類型</label>
                      <select class="form-select" v-model="form.invoiceType">
                        <option value="personal">個人發票</option>
                        <option value="company">公司發票</option>
                        <option value="donation">捐贈發票</option>
                      </select>
                    </div>
                    <div class="col-md-6" v-if="form.invoiceType === 'company'">
                      <label class="form-label">統一編號</label>
                      <input type="text" class="form-control" v-model="form.taxId" />
                    </div>
                    <div class="col-12" v-if="form.invoiceType === 'donation'">
                      <label class="form-label">捐贈單位</label>
                      <select class="form-select" v-model="form.donationUnit">
                        <option value="1">創世基金會</option>
                        <option value="2">家扶基金會</option>
                        <option value="3">陽光基金會</option>
                      </select>
                    </div>
                  </div>
                </div>

                <div class="d-grid">
                  <button type="submit" class="btn btn-primary btn-lg">確認訂單</button>
                </div>
              </form>
            </div>
          </div>
        </div>

        <!-- 訂單摘要 -->
        <div class="col-md-4">
          <div class="card booking-summary sticky-top" style="top: 2rem">
            <div class="card-body">
              <h5 class="card-title mb-4">訂單摘要</h5>

              <!-- 折價券選擇 -->
              <div class="coupon-section mb-4">
                <h6 class="mb-3">折價券</h6>
                <div v-if="availableCoupons.length > 0" class="coupon-list">
                  <div v-for="coupon in availableCoupons" :key="coupon.id" class="coupon-item mb-2">
                    <div class="form-check">
                      <input class="form-check-input" type="radio" :id="'coupon-' + coupon.id" :value="coupon.id"
                        v-model="selectedCouponId" @change="applyCoupon(coupon)" />
                      <label class="form-check-label" :for="'coupon-' + coupon.id">
                        <div class="coupon-info">
                          <div class="coupon-title">{{ coupon.title }}</div>
                          <div class="coupon-desc">
                            {{ coupon.description }}
                          </div>
                          <div class="coupon-validity">
                            <small class="text-muted">
                              有效期至：{{ formatDate(coupon.validUntil) }}
                            </small>
                          </div>
                        </div>
                      </label>
                    </div>
                  </div>
                </div>
                <div v-else class="text-muted">
                  <small>暫無可用折價券</small>
                </div>
              </div>

              <!-- 訂單內容 -->
              <!-- <div class="order-details mb-4"> -->
              <!-- <div class="d-flex align-items-center mb-3">
                  <img :src="order.image" class="me-3" style="width: 60px; height: 60px; object-fit: cover"
                    :alt="order.title" />
                  <div>
                    <h6 class="mb-1">{{ order.title }}</h6>
                    <div class="text-muted">{{ order.date }}</div>
                  </div>
                </div> -->

              <!-- 訂單項目 -->
              <!-- <div class="order-items">
                  <div v-for="item in order.items" :key="item.id" class="d-flex justify-content-between mb-2">
                    <span>{{ item.name }} x {{ item.quantity }}</span>
                    <span>NT$ {{ item.price * item.quantity }}</span>
                  </div>
                </div> -->
              <!-- </div> -->

              <!-- 價格明細 -->
              <!-- <div class="price-details">
                <div class="d-flex justify-content-between mb-2">
                  <span>小計</span>
                  <span>NT$ {{ order.subtotal }}</span>
                </div>
                <div class="d-flex justify-content-between mb-2" v-if="appliedCoupon">
                  <span>折價券折扣</span>
                  <span class="text-danger">-NT$ {{ appliedCoupon.discountAmount }}</span>
                </div>
                <div class="d-flex justify-content-between mb-2" v-if="order.discount">
                  <span>其他優惠</span>
                  <span class="text-danger">-NT$ {{ order.discount }}</span>
                </div>
                <div class="d-flex justify-content-between mb-2" v-if="order.tax">
                  <span>稅金</span>
                  <span>NT$ {{ order.tax }}</span>
                </div>
                <hr />
                <div class="d-flex justify-content-between fw-bold">
                  <span>總計</span>
                  <span class="text-primary">NT$ {{ calculateTotal() }}</span>
                </div>
              </div> -->
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { jwtDecode } from 'jwt-decode';

import { ref, computed, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import Header from '../Header.vue';
import { usePlansStore } from '@/stores/plans';
import { useCountriesStore } from '@/stores/countries';
import axios from 'axios';
import { UseOrderItemStore } from '@/stores/orderItem';
const today = new Date().toISOString();
const orderItemStore = UseOrderItemStore();
const details = computed(() => orderItemStore.details);

const router = useRouter();
const selectedFlight = ref(null);

const plans = computed(() => planStore.plans);
const sortedPlans = computed(() => {
  return plans.value.slice().sort((a, b) => b.startDate - a.startDate);
})

// 表單資料
const form = ref({
  orderNo: '',
  tripId: null,
  fullName: '',
  telNumber: '',
  email: '',
  checkIn: '',
  checkOut: '',
  guests: null,
  subtotal: null,
  discount: 0,
  total: null,
  paymentMethod: 1,
  cardNumber: '',
  cardExpiry: '',
  cardCVC: '',
  invoiceType: 'personal',
  taxId: '',
  donationUnit: '',
  status: 'unpaid',
});

const goToDetailPage = () => {
  const path = '/hotel/' + details.value.lodgingId + '/room/' + details.value.roomTypeId;
  window.open(path, '_blank');
}

// 折價券相關
const selectedCouponId = ref(null);
const appliedCoupon = ref(null);

// 可用的折價券
const availableCoupons = ref([
  {
    id: 1,
    title: '新用戶專享',
    description: '首次購買享 9 折優惠',
    type: 'percentage',
    discountValue: 10, // 10% 折扣
    minAmount: 1000, // 最低消費金額
    validUntil: '2024-12-31',
    applicableProducts: ['ticket', 'hotel', 'traffic']
  },
  {
    id: 2,
    title: '滿額折抵',
    description: '消費滿 5000 元折抵 500 元',
    type: 'fixed',
    discountValue: 500, // 固定折抵金額
    minAmount: 5000, // 最低消費金額
    validUntil: '2024-12-31',
    applicableProducts: ['ticket', 'hotel', 'traffic']
  },
  {
    id: 3,
    title: '飯店專用券',
    description: '飯店預訂享 8 折優惠',
    type: 'percentage',
    discountValue: 20, // 20% 折扣
    minAmount: 2000, // 最低消費金額
    validUntil: '2024-12-31',
    applicableProducts: ['hotel']
  },
  {
    id: 4,
    title: '門票優惠券',
    description: '景點門票折抵 200 元',
    type: 'fixed',
    discountValue: 200, // 固定折抵金額
    minAmount: 1000, // 最低消費金額
    validUntil: '2024-12-31',
    applicableProducts: ['ticket']
  },
  {
    id: 5,
    title: '交通優惠券',
    description: '交通服務享 85 折優惠',
    type: 'percentage',
    discountValue: 15, // 15% 折扣
    minAmount: 500, // 最低消費金額
    validUntil: '2024-12-31',
    applicableProducts: ['traffic']
  }
]);

// 模擬訂單資料
// const order = ref({
//   image: 'https://images.unsplash.com/photo-1581309638082-877cb8132535',
//   title: '東京迪士尼樂園一日門票',
//   date: '2024-04-01',
//   type: 'ticket', // 訂單類型
//   items: [
//     {
//       id: 1,
//       name: '成人票',
//       quantity: 2,
//       price: 2500
//     },
//     {
//       id: 2,
//       name: '兒童票',
//       quantity: 1,
//       price: 2000
//     }
//   ],
//   subtotal: 7000,
//   discount: 500,
//   tax: 0,
//   total: 6500
// });

// // 載入旅程數據
// const loadJourneyData = () => {
//   try {
//     const checkoutData = JSON.parse(localStorage.getItem('checkoutData') || 'null');
//     if (checkoutData && checkoutData.items && checkoutData.items.length > 0) {
//       // 使用旅程數據更新訂單
//       const firstItem = checkoutData.items[0];
//       order.value = {
//         image: firstItem.image,
//         title: `${firstItem.name} 等 ${checkoutData.totalItems} 項商品`,
//         date: new Date().toISOString().split('T')[0],
//         type: firstItem.type || 'mixed',
//         items: checkoutData.items.map((item) => ({
//           id: item.id,
//           name: item.name,
//           quantity: item.quantity || 1,
//           price: item.price
//         })),
//         subtotal: checkoutData.totalPrice,
//         discount: 0, // 重置折扣，讓折價券重新計算
//         tax: 0,
//         total: checkoutData.totalPrice
//       };
//     }
//   } catch (error) {
//     console.error('Error loading journey data:', error);
//   }
// };

// 格式化日期
const formatDate = (dateString) => {
  const date = new Date(dateString);
  return date.toLocaleDateString('zh-TW');
};

// 應用折價券
const applyCoupon = (coupon) => {
  // 檢查最低消費金額
  if (order.value.subtotal < coupon.minAmount) {
    alert(`此折價券需要最低消費 NT$ ${coupon.minAmount.toLocaleString()} 元`);
    selectedCouponId.value = null;
    appliedCoupon.value = null;
    return;
  }

  // 檢查訂單類型是否適用
  if (!coupon.applicableProducts.includes(order.value.type)) {
    alert('此折價券不適用於當前商品類型');
    selectedCouponId.value = null;
    appliedCoupon.value = null;
    return;
  }

  // 計算折扣金額
  let discountAmount = 0;
  if (coupon.type === 'percentage') {
    discountAmount = Math.floor(order.value.subtotal * (coupon.discountValue / 100));
  } else if (coupon.type === 'fixed') {
    discountAmount = Math.min(coupon.discountValue, order.value.subtotal);
  }

  appliedCoupon.value = {
    ...coupon,
    discountAmount
  };

  console.log('應用折價券:', coupon.title, '折扣金額:', discountAmount);
};

// 計算總計
// const calculateTotal = () => {
//   let total = order.value.subtotal;

//   // 減去折價券折扣
//   if (appliedCoupon.value) {
//     total -= appliedCoupon.value.discountAmount;
//   }

//   // 減去其他優惠
//   if (order.value.discount) {
//     total -= order.value.discount;
//   }

//   // 加上稅金
//   if (order.value.tax) {
//     total += order.value.tax;
//   }

//   return Math.max(0, total); // 確保總計不為負數
// };

// const formatDuration = (minutes) => {
//   if (!minutes && minutes !== 0) return '無資料';
//   const hours = Math.floor(minutes / 60);
//   const mins = minutes % 60;
//   return `${hours}時 ${mins}分`;
// };

// 處理表單提交
const handleSubmit = () => {
  // console.log('提交訂單：', form.value);
  // console.log('使用的折價券：', appliedCoupon.value);
  // console.log('最終總計：', calculateTotal());

  // // TODO: 實現訂單提交邏輯
  // router.push('/orders');

  console.log('訂單資料：');
  console.log(form.value);
  localStorage.setItem('bookingOrder', JSON.stringify({ ...form.value }));
  router.push('/planDetails/' + form.value.tripId);
};

const generateOrderNo = () => {
  // 生成一個 16 位數的亂數字串
  let randomNumber = Math.floor(Math.random() * 1e16);  // 最大值 9999999999999999

  // 如果長度不夠 16 位，則在前面補 0
  return randomNumber.toString().padStart(16, '0');
}

form.value.orderNo = 'ORDBOOKING-' + generateOrderNo();

const startDate = computed(() => {
  const index = sortedPlans.value.findIndex(plan => plan.id === form.value.tripId);
  if (index >= 0) {
    return sortedPlans.value[index].startDate;
  } else {
    return '';
  }
});


const endDate = computed(() => {
  const index = sortedPlans.value.findIndex(plan => plan.id === form.value.tripId);
  if (index >= 0) {
    return sortedPlans.value[index].endDate;
  } else {
    return '';
  }
})


const dateDiff = computed(() => {
  if (!form.value.checkIn || !form.value.checkOut) {
    return 0;
  } else {
    return (new Date(form.value.checkOut) - new Date(form.value.checkIn)) / 86400 / 1000;
  }
})

watch(() => form.value.paymentMethod, (newVal) => {
  form.value.paymentMethod = Number(newVal);
})

watch(dateDiff, (newVal) => {
  form.value.subtotal = newVal * details.value.pricePerNight;
})


watch(() => form.value.subtotal, (newVal) => {
  form.value.total = newVal + form.value.discount;
})

watch(() => form.value.discount, (newVal) => {
  form.value.total = form.value.subtotal + newVal;
})

const counrtyStore = useCountriesStore();
const planStore = usePlansStore();

const token = localStorage.getItem('token');
const payload = jwtDecode(token);
const userId = payload.id;

const loadStores = async () => {
  try {
    await Promise.all([
      counrtyStore.getData(),
      planStore.getData(userId),
    ])
    console.log("load country, plan stores");
  } catch (error) {
    console.log("load store error: " + error);
  }
};


// 組件掛載時載入數據
onMounted(async () => {
  // loadJourneyData();
  loadStores();
  console.log(planStore.plans);
  axios.get('http://localhost:8080/api/profile/' + userId)
    .then(response => {
      // const userInfo = ref(response.data);
      console.log(response.data);
      const userInfo = response.data;
      const phoneCode = counrtyStore.countries.find(item => item.id === userInfo.residence).phoneCode;
      form.value.fullName = userInfo.familyName + userInfo.givenName;
      form.value.telNumber = '+' + phoneCode + ' ' + userInfo.telNumber;
      form.value.email = userInfo.email;
    })

  // const flightData = localStorage.getItem('checkoutFlight');
  // if (flightData) {
  //   selectedFlight.value = JSON.parse(flightData);
  // }
});
</script>

<style scoped>
.lnk:hover {
  text-decoration: underline;
}

.section-header {
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid #e9ecef;
}

.booking-summary {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.payment-methods .form-check-label {
  cursor: pointer;
}

/* 折價券樣式 */
.coupon-section {
  border-top: 1px solid #e9ecef;
  padding-top: 1rem;
}

.coupon-item {
  border: 1px solid #e9ecef;
  border-radius: 8px;
  padding: 0.75rem;
  transition: all 0.2s ease;
}

.coupon-item:hover {
  border-color: #007bff;
  background-color: #f8f9fa;
}

.coupon-item .form-check-input:checked+.form-check-label .coupon-info {
  background-color: #e3f2fd;
  border-radius: 6px;
  padding: 0.5rem;
}

.coupon-title {
  font-weight: 600;
  color: #007bff;
  margin-bottom: 0.25rem;
}

.coupon-desc {
  font-size: 0.875rem;
  color: #6c757d;
  margin-bottom: 0.25rem;
}

.coupon-validity {
  font-size: 0.75rem;
}

.form-check-label {
  cursor: pointer;
  width: 100%;
  margin-bottom: 0;
}

.form-check-input {
  margin-top: 0.25rem;
}

@media (max-width: 768px) {
  .booking-summary {
    position: static !important;
    margin-bottom: 2rem;
  }
}
</style>
