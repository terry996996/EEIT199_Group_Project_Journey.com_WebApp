<template>
  <div class="carousel-container bg-white shadow-sm rounded-4 p-4 position-relative mb-5">
    <!-- 標題列 -->
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h5 class="fw-bold">{{ title }}</h5>
      <a href="/tickets" class="text-primary small">查看更多</a>
    </div>

    <!-- 左右箭頭按鈕 -->
    <button class="carousel-btn left" @click="prevCard" :disabled="scrollX === 0">
      <i class="bi bi-chevron-left"></i>
    </button>
    <button class="carousel-btn right" @click="nextCard" :disabled="scrollX >= maxScrollX">
      <i class="bi bi-chevron-right"></i>
    </button>

    <!-- 滑動區域 -->
    <div class="overflow-hidden">
      <div
        class="d-flex transition"
        :style="{
          transform: `translateX(-${scrollX}px)`,
          width: `${items.length * cardWidth}px`,
        }"
      >
        <div
          class="ticket-card card border-0 me-3"
          v-for="item in items"
          :key="item.id"
          :style="{ width: cardWidth + 'px' }"
        >
          <img :src="item.image" class="card-img-top" :alt="item.name" />
          <div class="card-body p-2">
            <h6 class="fw-bold text-truncate mb-1">{{ item.name }}</h6>
            <p class="text-muted small mb-1">
              <i class="bi bi-geo-alt"></i> {{ item.location }}
            </p>
            <div class="d-flex justify-content-between align-items-center">
              <div class="text-warning small">
                ★{{ item.rating || 0 }} ({{ item.reviewCount || 0 }})
              </div>
              <div class="text-primary fw-bold small">
                {{ item.price === 0 ? '免費' : 'NT$ ' + item.price.toLocaleString() }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

// 👉 接收父層傳入的 props
const props = defineProps({
  title: {
    type: String,
    default: '推薦行程',
  },
  items: {
    type: Array,
    required: true,
  },
})

// 👉 每張卡片寬度（px）
const cardWidth = 270

// 👉 可視區一次最多顯示幾張
const visibleCount = 4

// 👉 當前滑動的 px
const scrollX = ref(0)

// 👉 最大可滑動距離
const maxScrollX = computed(() => {
  const total = props.items.length * cardWidth
  const visible = visibleCount * cardWidth
  return total > visible ? total - visible : 0
})

// 👉 向右滑動（下一張）
const nextCard = () => {
  scrollX.value = Math.min(scrollX.value + cardWidth, maxScrollX.value)
}

// 👉 向左滑動（上一張）
const prevCard = () => {
  scrollX.value = Math.max(scrollX.value - cardWidth, 0)
}
</script>

<style scoped>
.carousel-container {
  max-width: 1200px;
  position: relative;
}

/* 卡片樣式 */
.ticket-card {
  flex: 0 0 auto;
  border-radius: 0.75rem;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
  background-color: white;
  transition: transform 0.2s ease;
}
.ticket-card:hover {
  transform: translateY(-4px);
}

/* 卡片圖片樣式 */
.card-img-top {
  height: 150px;
  object-fit: cover;
  border-top-left-radius: 0.75rem;
  border-top-right-radius: 0.75rem;
}

/* 滑動動畫 */
.transition {
  transition: transform 0.5s ease;
}

/* 左右滑動按鈕 */
.carousel-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  z-index: 10;
  background-color: #fff;
  border: none;
  border-radius: 50%;
  padding: 8px;
  font-size: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  display: flex;
  align-items: center;
  justify-content: center;
}
.carousel-btn.left {
  left: -20px;
}
.carousel-btn.right {
  right: -20px;
}
.carousel-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}
</style>
