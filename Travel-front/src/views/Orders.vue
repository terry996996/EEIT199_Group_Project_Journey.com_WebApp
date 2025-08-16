<template>
  <div class="orders-page">
    <!-- Header -->
    <Header />

    <div class="container my-5 pt-4">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h4>我的訂單</h4>
        <div class="d-flex gap-2">
          <select class="form-select" style="width: auto" v-model="filter.type">
            <option value="">所有類型</option>
            <option value="ticket">景點門票</option>
            <option value="hotel">住宿</option>
            <option value="traffic">交通</option>
          </select>
          <select class="form-select" style="width: auto" v-model="filter.status">
            <option value="">所有狀態</option>
            <option value="pending">待付款</option>
            <option value="paid">已付款</option>
            <option value="completed">已完成</option>
            <option value="cancelled">已取消</option>
          </select>
        </div>
      </div>

      <!-- 訂單列表 -->
      <div class="orders-list">
        <div v-for="order in filteredOrders" :key="order.id" class="card mb-3 order-card">
          <div class="card-body">
            <div class="row">
              <!-- 訂單基本資訊 -->
              <div class="col-md-8">
                <div class="d-flex">
                  <img :src="order.image" class="me-3" style="width: 80px; height: 80px; object-fit: cover"
                    :alt="order.title" />
                  <div>
                    <div class="d-flex align-items-center mb-2">
                      <h5 class="mb-0 me-2">{{ order.title }}</h5>
                      <span :class="getStatusBadgeClass(order.status)">{{
                        getStatusText(order.status)
                      }}</span>
                    </div>
                    <div class="text-muted mb-2">
                      訂單編號：{{ order.orderNumber }}
                    </div>
                    <div class="text-muted">使用日期：{{ order.date }}</div>
                  </div>
                </div>
              </div>

              <!-- 價格和操作按鈕 -->
              <div class="col-md-4 text-end">
                <div class="fs-5 fw-bold text-primary mb-3">
                  NT$ {{ order.total }}
                </div>
                <div class="btn-group">
                  <button class="btn btn-outline-primary" @click="viewOrderDetail(order)">
                    查看詳情
                  </button>
                  <button v-if="order.status === 'pending'" class="btn btn-primary" @click="proceedPayment(order)">
                    立即付款
                  </button>
                  <button v-if="order.status === 'paid'" class="btn btn-outline-secondary"
                    @click="downloadVoucher(order)">
                    下載憑證
                  </button>
                  <button v-if="canCancel(order)" class="btn btn-outline-danger" @click="deleteOrder(order.id)">
                    取消訂單
                  </button>
                </div>
              </div>
            </div>

            <!-- 訂單詳情（展開時顯示） -->
            <div v-if="order.showDetail" class="order-detail mt-4 pt-4 border-top">
              <!-- 訂單項目 -->
              <div class="mb-4">
                <h6 class="mb-3">訂單項目</h6>
                <div class="table-responsive">
                  <table class="table">
                    <thead>
                      <tr>
                        <th>項目</th>
                        <th>單價</th>
                        <th>數量</th>
                        <th class="text-end">小計</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="item in order.items" :key="item.id">
                        <td>{{ item.name }}</td>
                        <td>NT$ {{ item.price }}</td>
                        <td>{{ item.quantity }}</td>
                        <td class="text-end">
                          NT$ {{ item.price * item.quantity }}
                        </td>
                      </tr>
                    </tbody>
                    <tfoot>
                      <tr>
                        <td colspan="3" class="text-end">小計</td>
                        <td class="text-end">NT$ {{ order.subtotal }}</td>
                      </tr>
                      <tr v-if="order.discount">
                        <td colspan="3" class="text-end">優惠折扣</td>
                        <td class="text-end text-danger">
                          -NT$ {{ order.discount }}
                        </td>
                      </tr>
                      <tr>
                        <td colspan="3" class="text-end fw-bold">總計</td>
                        <td class="text-end fw-bold">NT$ {{ order.total }}</td>
                      </tr>
                    </tfoot>
                  </table>
                </div>
              </div>

              <!-- 聯絡資訊 -->
              <div class="mb-4">
                <h6 class="mb-3">聯絡資訊</h6>
                <div class="row">
                  <div class="col-md-4">
                    <div class="text-muted mb-1">聯絡人</div>
                    <div>{{ order.contact.name }}</div>
                  </div>
                  <div class="col-md-4">
                    <div class="text-muted mb-1">電話</div>
                    <div>{{ order.contact.phone }}</div>
                  </div>
                  <div class="col-md-4">
                    <div class="text-muted mb-1">Email</div>
                    <div>{{ order.contact.email }}</div>
                  </div>
                </div>
              </div>

              <!-- 使用須知 -->
              <div class="mb-4">
                <h6 class="mb-3">使用須知</h6>
                <ul class="list-unstyled">
                  <li v-for="(notice, index) in order.notices" :key="index" class="mb-2">
                    <i class="bi bi-info-circle me-2"></i>
                    {{ notice }}
                  </li>
                </ul>
              </div>

              <!-- 付款資訊 -->
              <div v-if="order.payment">
                <h6 class="mb-3">付款資訊</h6>
                <div class="row">
                  <div class="col-md-4">
                    <div class="text-muted mb-1">付款方式</div>
                    <div>{{ getPaymentMethodText(order.payment.method) }}</div>
                  </div>
                  <div class="col-md-4">
                    <div class="text-muted mb-1">付款時間</div>
                    <div>{{ order.payment.time }}</div>
                  </div>
                  <div class="col-md-4">
                    <div class="text-muted mb-1">交易編號</div>
                    <div>{{ order.payment.transactionId }}</div>
                  </div>
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
import { ref, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import Header from "../components/Header.vue";
import { getOrdersByTripId, deleteOrder as deleteOrderById } from '@/services/attractions/ticketService'

const router = useRouter();

// 篩選條件
const filter = ref({
  type: "",
  status: "",
});

// 模擬訂單數據
// const orders = ref([
//   {
//     id: 1,
//     orderNumber: "ORD202403150001",
//     type: "ticket",
//     title: "東京迪士尼樂園一日門票",
//     image: "image/location/disney.webp",
//     date: "2024-04-01",
//     status: "pending",
//     total: 6500,
//     subtotal: 7000,
//     discount: 500,
//     items: [
//       {
//         id: 1,
//         name: "成人票",
//         price: 2500,
//         quantity: 2,
//       },
//       {
//         id: 2,
//         name: "兒童票",
//         price: 2000,
//         quantity: 1,
//       },
//     ],
//     contact: {
//       name: "王小明",
//       phone: "0912345678",
//       email: "wang@example.com",
//     },
//     notices: [
//       "請於使用日期當天出示電子憑證",
//       "憑證僅限使用一次，使用後即失效",
//       "如需更改使用日期，請提前3天申請",
//     ],
//   },
//   {
//     id: 2,
//     orderNumber: "ORD202403150002",
//     type: "hotel",
//     title: "東京灣希爾頓酒店",
//     image: "https://images.unsplash.com/photo-1566073771259-6a8506099945",
//     date: "2024-04-01",
//     status: "paid",
//     total: 12000,
//     subtotal: 12000,
//     discount: 0,
//     items: [
//       {
//         id: 1,
//         name: "豪華雙人房",
//         price: 6000,
//         quantity: 2,
//       },
//     ],
//     contact: {
//       name: "李小華",
//       phone: "0923456789",
//       email: "lee@example.com",
//     },
//     notices: [
//       "入住時間為下午3點後",
//       "退房時間為上午11點前",
//       "需攜帶身分證件辦理入住",
//     ],
//     payment: {
//       method: "creditCard",
//       time: "2024-03-15 14:30",
//       transactionId: "TRX202403150001",
//     },
//   },
//   {
//     id: 3,
//     orderNumber: "ORD202403150003",
//     type: "traffic",
//     title: "台灣高鐵票",
//     image:
//       "https://upload.wikimedia.org/wikipedia/zh/thumb/6/6c/Taiwan_High_Speed_Rail_Logo.svg/1200px-Taiwan_High_Speed_Rail_Logo.svg.png",
//     date: "2024-03-20",
//     status: "completed",
//     total: 2700,
//     subtotal: 2700,
//     discount: 0,
//     items: [
//       {
//         id: 1,
//         name: "台北-台中 標準座艙",
//         price: 1350,
//         quantity: 2,
//       },
//     ],
//     contact: {
//       name: "張小芳",
//       phone: "0934567890",
//       email: "chang@example.com",
//     },
//     notices: [
//       "請於搭乘前30分鐘完成取票",
//       "需攜帶訂票時使用的證件",
//       "可於網路或車站櫃台取票",
//     ],
//     payment: {
//       method: "linePay",
//       time: "2024-03-10 10:15",
//       transactionId: "TRX202403100001",
//     },
//   },
// ]);

// 根據篩選條件過濾訂單
const filteredOrders = computed(() => {
  return orders.value.filter((order) => {
    if (filter.value.type && order.type !== filter.value.type) return false;
    if (filter.value.status && order.status !== filter.value.status)
      return false;
    return true;
  });
});
//===============================================
const mockOrders = [
  {
    id: 2,
    orderNumber: "ORD202403150002",
    type: "hotel",
    title: "東京灣希爾頓酒店",
    image: "https://images.unsplash.com/photo-1566073771259-6a8506099945",
    date: "2024-04-01",
    status: "paid",
    total: 12000,
    subtotal: 12000,
    discount: 0,
    items: [
      { id: 1, name: "豪華雙人房", price: 6000, quantity: 2 }
    ],
    contact: {
      name: "李小華",
      phone: "0923456789",
      email: "lee@example.com"
    },
    notices: [
      "入住時間為下午3點後",
      "退房時間為上午11點前",
      "需攜帶身分證件辦理入住"
    ],
    payment: {
      method: "creditCard",
      time: "2024-03-15 14:30",
      transactionId: "TRX202403150001"
    }
  },
  {
    id: 3,
    orderNumber: "ORD202403150003",
    type: "traffic",
    title: "台灣高鐵票",
    image: "https://upload.wikimedia.org/.../Taiwan_High_Speed_Rail_Logo.svg.png",
    date: "2024-03-20",
    status: "completed",
    total: 2700,
    subtotal: 2700,
    discount: 0,
    items: [
      { id: 1, name: "台北-台中 標準座艙", price: 1350, quantity: 2 }
    ],
    contact: {
      name: "張小芳",
      phone: "0934567890",
      email: "chang@example.com"
    },
    notices: [
      "請於搭乘前30分鐘完成取票",
      "需攜帶訂票時使用的證件",
      "可於網路或車站櫃台取票"
    ],
    payment: {
      method: "linePay",
      time: "2024-03-10 10:15",
      transactionId: "TRX202403100001"
    }
  }
]

//===============================================
const orders = ref([])

onMounted(async () => {
  try {
    const tripId = 1 //根據使用者的旅程
    const res = await getOrdersByTripId(tripId)
    const backendOrders = res.data.map(transformSingleOrder)

    // ✅ 合併後端資料與假資料
    orders.value = [...backendOrders, ...mockOrders]
  } catch (err) {
    console.error("❌ 查詢訂單失敗:", err)
    // 即使後端失敗，也顯示假資料
    orders.value = [...mockOrders]
  }
})


const transformSingleOrder = (order) => {
  const items = Array.isArray(order.items)
    ? order.items.map((item) => ({
      id: item.optionId,               // ✅ optionId → id
      name: item.optionName,           // ✅ optionName → name
      price: item.unitPrice,           // ✅ unitPrice → price
      quantity: item.quantity,         // ✅ quantity → quantity
    }))
    : []

  const subtotal = items.reduce(
    (sum, item) => sum + item.price * item.quantity,
    0
  )

  return {
    id: order.orderId,                                // ✅ 訂單主鍵
    orderNumber: order.orderNo,                       // ✅ 訂單編號
    type: "ticket",                                   // ✅ 固定類型
    title: order.items?.[0]?.ticketPackageName || "", // ✅ 套票名稱（取第一筆）
    image: order.items?.[0]?.imageUrl || "",          // ✅ 主圖（取第一筆）
    date: order.items?.[0]?.useDate?.substring(0, 10) || "", // ✅ 使用日期（只取日期部分）
    status: order.status ? "paid" : "pending",        // ✅ 狀態轉字串
    total: subtotal,                                  // ✅ 總金額
    subtotal,                                          // ✅ 小計
    discount: 0,                                       // ✅ 先固定為 0，如有優惠可動態計算

    items,                                             // ✅ 明細項目

    contact: {
      name: "王小明",                // ✅ 假資料
      phone: "0912345678",
      email: "wang@example.com"
    },

    notices: [
      "請於使用日期當天出示電子憑證",
      "憑證僅限使用一次，使用後即失效",
      "如需更改使用日期，請提前3天申請"
    ]
  }
}
//===========================================================
const deleteOrder = async (orderId) => {
  try {
    const result = await deleteOrderById(orderId) // ✅ 改這裡
    console.log('✅ 成功刪除訂單', orderId)

    // ✅ 從畫面中移除該筆訂單
    orders.value = orders.value.filter(o => o.id !== orderId)

    alert('訂單已成功取消')
  } catch (err) {
    console.error('❌ 刪除失敗', err)
    alert('刪除失敗，請稍後再試')
  }
}

//=========================================================== 

// 獲取狀態標籤樣式
const getStatusBadgeClass = (status) => {
  const classes = {
    pending: "badge bg-warning",
    paid: "badge bg-info",
    completed: "badge bg-success",
    cancelled: "badge bg-secondary",
  };
  return classes[status] || "badge bg-secondary";
};

// 獲取狀態文字
const getStatusText = (status) => {
  const texts = {
    pending: "待付款",
    paid: "已付款",
    completed: "已完成",
    cancelled: "已取消",
  };
  return texts[status] || "未知狀態";
};

// 獲取付款方式文字
const getPaymentMethodText = (method) => {
  const texts = {
    creditCard: "信用卡",
    linePay: "LINE Pay",
    transfer: "銀行轉帳",
  };
  return texts[method] || "未知方式";
};

// 檢查是否可以取消訂單
const canCancel = (order) => {
  return ["pending", "paid"].includes(order.status);
};

// 查看訂單詳情
const viewOrderDetail = (order) => {
  order.showDetail = !order.showDetail;
};

// 前往付款
const proceedPayment = (order) => {
  router.push({
    path: "/checkout",
    query: { orderId: order.id },
  });
};

// 下載憑證
const downloadVoucher = (order) => {
  console.log("下載憑證：", order.orderNumber);
  // TODO: 實現下載憑證邏輯
};

// 取消訂單
const cancelOrder = (order) => {
  if (confirm("確定要取消此訂單嗎？")) {
    console.log("取消訂單：", order.orderNumber);
    // TODO: 實現取消訂單邏輯
  }
};
</script>

<style scoped>
.order-card {
  transition: transform 0.2s;
}

.order-card:hover {
  transform: translateY(-2px);
}

.order-detail {
  animation: fadeIn 0.3s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.table th {
  background: #f8f9fa;
}

@media (max-width: 768px) {
  .btn-group {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
  }

  .btn-group .btn {
    width: 100%;
  }
}
</style>
