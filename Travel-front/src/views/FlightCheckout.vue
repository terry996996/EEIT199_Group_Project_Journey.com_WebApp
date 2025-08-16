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
              <h4 class="card-title mb-4">聯絡資訊</h4>
              <form @submit.prevent="handleSubmit">
                <!-- 聯絡人資訊 -->
                <div class="mb-4">
                  <div class="row g-3">
                    <div class="col-md-6">
                      <label class="form-label">姓名</label>
                      <input type="text" class="form-control" v-model="form.name" required />
                    </div>
                    <div class="col-md-6">
                      <label class="form-label">電話</label>
                      <input type="tel" class="form-control" v-model="form.phone" required />
                    </div>
                    <div class="col-12">
                      <label class="form-label">Email</label>
                      <input type="email" class="form-control" v-model="form.email" required />
                    </div>
                  </div>
                </div>

                <!-- 乘客資訊 -->
                <div class="mb-4">
                  <h5 class="mb-3">乘客資訊</h5>
                  <div v-for="(passenger, index) in form.passengers" :key="index" class="card mb-3">
                    <div class="card-body">
                      <div class="row g-3">
                        <div class="col-md-6">
                          <label class="form-label">姓名</label>
                          <input type="text" class="form-control" v-model="passenger.name" required />
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">身分證字號</label>
                          <input type="text" class="form-control" v-model="passenger.idNumber" required />
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">生日</label>
                          <input type="date" class="form-control" v-model="passenger.birthday" required />
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">乘客類型</label>
                          <select class="form-select" v-model="passenger.type" required>
                            <option value="adult">成人</option>
                            <option value="child">兒童</option>
                            <option value="senior">敬老</option>
                          </select>
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
                      <input class="form-check-input" type="radio" name="paymentMethod" id="creditCard"
                        value="creditCard" v-model="form.paymentMethod" />
                      <label class="form-check-label" for="creditCard">
                        <i class="bi bi-credit-card me-2"></i>
                        信用卡付款
                      </label>
                    </div>
                    <div class="form-check mb-2">
                      <input class="form-check-input" type="radio" name="paymentMethod" id="linePay" value="linePay"
                        v-model="form.paymentMethod" />
                      <label class="form-check-label" for="linePay">
                        <i class="bi bi-wallet2 me-2"></i>
                        LINE Pay
                      </label>
                    </div>
                    <div class="form-check mb-2">
                      <input class="form-check-input" type="radio" name="paymentMethod" id="transfer" value="transfer"
                        v-model="form.paymentMethod" />
                      <label class="form-check-label" for="transfer">
                        <i class="bi bi-bank me-2"></i>
                        銀行轉帳
                      </label>
                    </div>
                  </div>

                  <!-- 信用卡資訊 -->
                  <div v-if="form.paymentMethod === 'creditCard'" class="card mt-3">
                    <div class="card-body">
                      <div class="row g-3">
                        <div class="col-12">
                          <label class="form-label">卡號</label>
                          <input type="text" class="form-control" v-model="form.cardNumber"
                            placeholder="1234 5678 9012 3456" required />
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">有效期限</label>
                          <input type="text" class="form-control" v-model="form.cardExpiry" placeholder="MM/YY"
                            required />
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
                  <button type="submit" class="btn btn-primary btn-lg">確認付款</button>
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
              <div class="order-details mb-4">
                <div class="d-flex align-items-center mb-3">
                  <img :src="order.image" class="me-3" style="width: 60px; height: 60px; object-fit: cover"
                    :alt="order.title" />
                  <div>
                    <h6 class="mb-1">{{ order.title }}</h6>
                    <div class="text-muted">{{ order.date }}</div>
                  </div>
                </div>

                <!-- 訂單項目 -->
                <div class="order-items">
                  <div v-for="item in order.items" :key="item.id" class="d-flex justify-content-between mb-2">
                    <span>{{ item.name }} x {{ item.quantity }}</span>
                    <span>NT$ {{ item.price * item.quantity }}</span>
                  </div>
                </div>
              </div>

              <!-- 價格明細 -->
              <div class="price-details">
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
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import Header from '../components/Header.vue';
import Footer from '../components/Footer.vue';

const router = useRouter();

// 表單資料
const form = ref({
  name: '',
  phone: '',
  email: '',
  passengers: [
    {
      name: '',
      idNumber: '',
      birthday: '',
      type: 'adult'
    }
  ],
  paymentMethod: 'creditCard',
  cardNumber: '',
  cardExpiry: '',
  cardCVC: '',
  invoiceType: 'personal',
  taxId: '',
  donationUnit: ''
});

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
const order = ref({
  image: 'image/location/disney.webp',
  title: '東京迪士尼樂園一日門票',
  date: '2024-04-01',
  type: 'ticket', // 訂單類型
  items: [
    {
      id: 1,
      name: '成人票',
      quantity: 2,
      price: 2500
    },
    {
      id: 2,
      name: '兒童票',
      quantity: 1,
      price: 2000
    }
  ],
  subtotal: 7000,
  discount: 500,
  tax: 0,
  total: 6500
});

// 載入旅程數據
const loadJourneyData = () => {
  try {
    const checkoutData = JSON.parse(localStorage.getItem('checkoutData') || 'null');
    if (checkoutData && checkoutData.items && checkoutData.items.length > 0) {
      // 使用旅程數據更新訂單
      const firstItem = checkoutData.items[0];
      order.value = {
        image: firstItem.image,
        title: `${firstItem.name} 等 ${checkoutData.totalItems} 項商品`,
        date: new Date().toISOString().split('T')[0],
        type: firstItem.type || 'mixed',
        items: checkoutData.items.map((item) => ({
          id: item.id,
          name: item.name,
          quantity: item.quantity || 1,
          price: item.price
        })),
        subtotal: checkoutData.totalPrice,
        discount: 0, // 重置折扣，讓折價券重新計算
        tax: 0,
        total: checkoutData.totalPrice
      };
    }
  } catch (error) {
    console.error('Error loading journey data:', error);
  }
};

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
const calculateTotal = () => {
  let total = order.value.subtotal;

  // 減去折價券折扣
  if (appliedCoupon.value) {
    total -= appliedCoupon.value.discountAmount;
  }

  // 減去其他優惠
  if (order.value.discount) {
    total -= order.value.discount;
  }

  // 加上稅金
  if (order.value.tax) {
    total += order.value.tax;
  }

  return Math.max(0, total); // 確保總計不為負數
};

// 處理表單提交
const handleSubmit = () => {
  console.log('提交訂單：', form.value);
  console.log('使用的折價券：', appliedCoupon.value);
  console.log('最終總計：', calculateTotal());

  // TODO: 實現訂單提交邏輯
  router.push('/orders');
};

// 組件掛載時載入數據
onMounted(() => {
  loadJourneyData();
});
</script>

<style scoped>
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
