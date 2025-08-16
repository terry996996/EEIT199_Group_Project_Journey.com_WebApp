<template>
  <div class="ticket-package card p-4 mb-4">
    <!-- 套票主資訊 -->
    <div class="d-flex" ref="ticketPackageRef">
      <img :src="image" class="cover me-3" alt="cover" />
      <div class="flex-grow-1">
        <div class="d-flex justify-content-between align-items-start mb-2">
          <div>
            <h5 class="fw-bold mb-1">{{ title }}</h5>
            <span class="badge bg-warning text-dark small">{{ tag }}</span>
          </div>
          <div class="text-end">
            <div class="text-primary fw-bold">NT${{ price.toLocaleString() }} 起</div>
            <button class="btn btn-outline-primary btn-sm mt-2 btn-custom-size" @click="handleSelect">
              {{ isOpen ? '收起' : '選擇' }}
            </button>
          </div>
        </div>
        <div class="mb-2">
          <i class="bi bi-calendar-event me-1 "></i> 最早可預訂日：{{ availableDate.substring(0, 10) }}
        </div>
        <div>{{ description }}</div>
      </div>
    </div>

    <!-- 展開內容區塊 -->
    <div v-if="isOpen" class="expanded mt-4">
      <div class="border-top pt-3 mt-3">
        <div class="mb-2 text-muted">費用包含</div>
        <ul class="small ps-3">
          <li v-for="(item, i) in inclusive" :key="i">{{ item }}</li>
        </ul>
      </div>
      <div class="border-top pt-3 mt-3">
        <div class="mb-2 text-muted">取消政策</div>
        <p class="small">{{ cancelPolicy }}</p>
      </div>
      <div class="border-top pt-3 mt-3">
        <div class="mb-2 text-muted">使用期限</div>
        <ul class="small ps-3">
          <li v-for="(item, i) in validityPeriodList" :key="i">{{ item }}</li>
        </ul>
      </div>

      <!-- 票券種類區塊 -->
      <div class="border-top pt-3 mt-3">
        <div class="mb-2 text-muted">選擇日期・選項</div>
        <input type="date" class="form-control w-25 mb-3" v-model="selectedDate" />

        <div class="row">
          <div class="col-md-4 mb-3" v-for="(t, i) in ticketTypes" :key="i">
            <div class="border p-3 rounded">
              <div class="fw-bold mb-1">{{ t.name }}</div>

              <div class="d-flex align-items-center justify-content-between">
                <div class="text-primary fw-bold">
                  NT${{ t.price.toLocaleString() }} 起/每人
                </div>
                <div class="d-flex align-items-center">
                  <button class="btn btn-sm btn-outline-secondary" @click="updateQuantity(t.id, -1)"
                    :disabled="quantities[t.id] <= 0">
                    -
                  </button>
                  <span class="mx-2">{{ quantities[t.id] }}</span>
                  <button class="btn btn-sm btn-outline-secondary" @click="updateQuantity(t.id, 1)">
                    +
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="text-end mt-4">
          <div class="mb-2">總計 NT${{ totalPrice }}</div>
          <!-- 立即預訂按鈕 -->
          <button class="btn btn-primary" @click="handleBooking">立即預訂</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import Swal from 'sweetalert2'
import { useOrderStore } from '@/stores/orderStore'

const props = defineProps({
  id: Number,
  title: String,
  image: String,
  tag: String,
  availableDate: String,
  price: Number,
  points: Array,
  description: String,
  attractionId: Number,
  ticketPackageId: Number,
  types: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['selectPackage'])
const isOpen = ref(false)

const handleSelect = () => {
  if (!isOpen.value) emit('selectPackage')
  isOpen.value = !isOpen.value
}

const ticketTypes = computed(() => {
  return props.types.map(t => ({
    name: t.ticketName,
    price: t.price,
    id: t.id
  }))
})

const quantities = ref({})
const selectedDate = ref('')

// 數量調整
const updateQuantity = (id, delta) => {
  const current = quantities.value[id] || 0
  const next = current + delta
  if (next >= 0) quantities.value[id] = next
}

// 計算總價
const totalPrice = computed(() =>
  ticketTypes.value.reduce((sum, t) => sum + t.price * (quantities.value[t.id] || 0), 0)
)

// 組裝選取的票種資料
const selectedOptions = computed(() => {
  return ticketTypes.value
    .filter(t => quantities.value[t.id] > 0)
    .map(t => ({
      optionId: t.id,
      optionName: t.name,
      unitPrice: t.price,
      quantity: quantities.value[t.id]
    }))
})

// 模擬資料
const inclusive = ref(['門票', '導覽解說', '專業解說員', '免費停車場'])
const cancelPolicy = ref('本商品可於預約日前15天免費取消，詳情請參閱退款規則。')
const validityPeriodList = ref([
  '本商品預訂後，請於預訂日期前完成付款。',
  '請於現場兌換門票，兌換後方可入園。',
  '若逾期未使用，視同放棄，恕不退款。'
])

// pinia 與 router
const orderStore = useOrderStore()
const router = useRouter()

// 預訂行為
const handleBooking = () => {
  if (selectedOptions.value.length === 0) {
    Swal.fire('請選擇至少一張票再預訂')
    return
  }
  if (!selectedDate.value) {
    Swal.fire('請選擇使用日期')
    return
  }

  orderStore.setOrderData({
    type: 'ticket',
    ticketId: props.id,
    ticketName: props.title,
    ticketImageUrl: props.image,
    packageId: props.ticketPackageId,
    packageName: props.title,
    selectedOptions: selectedOptions.value,
    useDate: selectedDate.value
  })

  showConfirmModal()
}

const showConfirmModal = () => {
  Swal.fire({
    title: '已加入旅程',
    text: '要繼續選購還是前往結帳？',
    showCancelButton: true,
    confirmButtonText: '前往結帳',
    cancelButtonText: '繼續選購'
  }).then((result) => {
    if (result.isConfirmed) {
      router.push({ path: '/checkout', query: { type: 'ticket' } })
    }
  })
}
</script>


<style scoped>
.ticket-package {
  border-radius: 12px;
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.cover {
  width: 120px;
  height: 90px;
  object-fit: cover;
  border-radius: 8px;
}

.btn-custom-size {
  padding: 0.5rem 1.5rem;
  font-size: 1.1rem;
  border-radius: 0.5rem;
  min-width: 120px;
}
</style>
