<template>
  <div class="card h-100 position-relative">
    <!-- 收藏按鈕 -->
    <button class="favorite-btn" @click="toggleFavorite" :class="{ 'is-favorited': isFavorited }">
      <i class="bi" :class="isFavorited ? 'bi-heart-fill' : 'bi-heart'"></i>
    </button>

    <router-link :to="'/ticket/' + ticket.id" class="text-decoration-none">
      <img :src="ticket.image" class="card-img-top" :alt="ticket.name" />
      <div class="card-body">
        <h5 class="card-title">{{ ticket.name }}</h5>
        <p class="card-text text-muted">
          <i class="bi bi-geo-alt"></i> {{ ticket.location }}
        </p>
        <div class="d-flex justify-content-between align-items-center">
          <div class="rating">
            <i v-for="star in 5" :key="star" :class="[
              'bi',
              star <= ticket.rating ? 'bi-star-fill' : 'bi-star',
              'text-warning',
            ]"></i>
            <span class="ms-1">{{ ticket.views }}</span>
          </div>
          <div class="price">
            <span class="text-primary fw-bold">免費景點</span>
          </div>
        </div>
      </div>
    </router-link>
    <div class="card-footer bg-white border-top-0">
      <div class="d-flex gap-2">
        <button class="btn btn-outline-primary flex-grow-1" @click="addToJourney">
          <i class="bi bi-plus-circle me-1"></i>加入旅程
        </button>
        <button class="btn btn-primary flex-grow-1" @click="handleBook">
          立即預訂
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

// 👉 建立 Router 實例
const router = useRouter()

// 👉 收藏狀態（本地端判斷）
const isFavorited = ref(false)

// 👉 接收父層傳入的 ticket 物件
const props = defineProps({
  ticket: {
    type: Object,
    required: true,
  },
})

// 👉 定義可觸發的事件（讓父層可以監聽）
const emit = defineEmits(['book', 'addToJourney', 'toggleFavorite'])

/* ------------------------- 收藏邏輯 ------------------------- */

// ✅ 檢查目前票券是否已被收藏
const checkFavoriteStatus = () => {
  try {
    const favorites = JSON.parse(localStorage.getItem('favorites') || '[]')
    isFavorited.value = favorites.some(
      (item) => item.id === props.ticket.id && item.type === 'ticket'
    )
  } catch (error) {
    console.error('🔴 無法讀取收藏清單', error)
    isFavorited.value = false
  }
}

// ✅ 收藏或取消收藏
const toggleFavorite = () => {
  try {
    const favorites = JSON.parse(localStorage.getItem('favorites') || '[]')
    const existingIndex = favorites.findIndex(
      (fav) => fav.id === props.ticket.id && fav.type === 'ticket'
    )

    if (existingIndex > -1) {
      // 已存在 → 取消收藏
      favorites.splice(existingIndex, 1)
      isFavorited.value = false
    } else {
      // 不存在 → 加入收藏
      favorites.push({
        id: props.ticket.id,
        type: 'ticket',
        name: props.ticket.name,
        image: props.ticket.image,
        price: props.ticket.price,
        location: props.ticket.location,
        rating: props.ticket.rating,
        features: props.ticket.features,
        highlight: props.ticket.highlight,
      })
      isFavorited.value = true
    }

    // 更新 localStorage
    localStorage.setItem('favorites', JSON.stringify(favorites))

    // 通知其他元件收藏狀態變更
    window.dispatchEvent(new Event('favoritesUpdated'))

    // 通知父元件
    emit('toggleFavorite', {
      ticket: props.ticket,
      isFavorited: isFavorited.value,
    })
  } catch (error) {
    console.error('🔴 收藏操作錯誤', error)
  }
}

// ✅ 監聽收藏更新事件（例如外部操作時自動更新）
const handleFavoritesUpdate = () => {
  checkFavoriteStatus()
}

/* ------------------------- 旅程加入邏輯 ------------------------- */

const addToJourney = () => {
  try {
    const journey = JSON.parse(localStorage.getItem('journey') || '[]')
    const existingIndex = journey.findIndex(
      (item) => item.id === props.ticket.id && item.type === 'ticket'
    )

    if (existingIndex > -1) {
      // 已存在 → 數量 +1
      journey[existingIndex].quantity =
        (journey[existingIndex].quantity || 1) + 1
    } else {
      // 不存在 → 加入
      journey.push({
        id: props.ticket.id,
        type: 'ticket',
        name: props.ticket.name,
        image: props.ticket.image,
        location: props.ticket.location,
        rating: props.ticket.views,
        features: props.ticket.features,
        highlight: props.ticket.highlight,
        quantity: 1,
      })
    }

    // 更新 localStorage 並通知其他元件
    localStorage.setItem('journey', JSON.stringify(journey))
    window.dispatchEvent(new Event('journeyUpdated'))

    // 通知父元件
    emit('addToJourney', props.ticket)
  } catch (error) {
    console.error('🔴 加入旅程失敗', error)
  }
}

/* ------------------------- 頁面與事件註冊 ------------------------- */

// 👉 點擊卡片跳轉到詳情頁（非必需，若 router-link 已處理）
const goToDetail = () => {
  router.push(`/ticket-detail/${props.ticket.id}`)
}

// 👉 當元件載入時，執行初始化
onMounted(() => {
  checkFavoriteStatus()
  window.addEventListener('favoritesUpdated', handleFavoritesUpdate)
})

// 👉 元件卸載時，移除監聽
onUnmounted(() => {
  window.removeEventListener('favoritesUpdated', handleFavoritesUpdate)
})

// 👉 點擊「立即預訂」按鈕
const handleBook = () => {
  emit('book', props.ticket)
}
</script>

<style scoped>
.card {
  transition: transform 0.2s;
  position: relative;
}

.card:hover {
  transform: translateY(-5px);
}

.card-img-top {
  height: 200px;
  object-fit: cover;
}

.rating {
  font-size: 1rem;
}

.price {
  font-weight: 600;
  color: #0d6efd;
}

.favorite-btn {
  position: absolute;
  top: 12px;
  right: 12px;
  background: white;
  border: none;
  border-radius: 50%;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  z-index: 2;
  cursor: pointer;
  transition: all 0.2s ease;
}

.favorite-btn:hover {
  transform: scale(1.1);
}

.favorite-btn i {
  font-size: 1.2rem;
  color: #dc3545;
}

.favorite-btn.is-favorited {
  background-color: #dc3545;
}

.favorite-btn.is-favorited i {
  color: white;
}

.card-footer {
  background-color: transparent;
  border-top: 1px solid rgba(0, 0, 0, 0.08);
  padding: 1rem;
}

.btn {
  font-size: 0.9rem;
  padding: 0.5rem 1rem;
}

.btn-outline-primary {
  border-color: #0d6efd;
  color: #0d6efd;
}

.btn-outline-primary:hover {
  background-color: #0d6efd;
  color: white;
}

.btn-primary {
  background-color: #0d6efd;
  border-color: #0d6efd;
}

.btn-primary:hover {
  background-color: #0b5ed7;
  border-color: #0b5ed7;
}
</style>