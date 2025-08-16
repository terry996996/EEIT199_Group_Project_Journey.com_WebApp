<template>
  <div id="app">
    <!-- 路由視圖 -->
    <router-view />

    <button v-show="shouldShowButtons" class="customer-service-btn" @click="goToCustomerService">
      <i class="bi bi-headset"></i>
    </button>

    <button v-show="shouldShowButtons && showBackToTop" class="back-to-top" @click="scrollToTop" title="回到頂部">
      <i class="bi bi-arrow-up"></i>
    </button>



    <TheFooter v-if="shouldShowButtons" />
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue';
import { useRoute } from 'vue-router';
import TheFooter from './components/Footer.vue';

const route = useRoute();



// 計算屬性：判斷是否應該顯示按鈕
const shouldShowButtons = computed(() => {
  // vendor頁面一律不顯示footer與浮動按鈕
  if (route.path.startsWith('/vendor')) return false;
  return !route.meta.hideButtons;
});

const goToCustomerService = () => {
  window.open('/chatroom', '_blank'); // 修改為在新分頁中打開
};

// --- 回到頂部按鈕相關邏輯 ---
const showBackToTop = ref(false);

const handleScroll = () => {
  showBackToTop.value = window.scrollY > 300;
};

const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  });
};

onMounted(() => {
  window.addEventListener('scroll', handleScroll);
  // 在掛載時立即檢查一次滾動位置，以防頁面載入時就已經有滾動
  handleScroll();
});

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
});
</script>

<style>
/* 全域樣式 */
body {
  padding-top: 76px;
}

/* 確保聊天室在其他元素之上 */
#app {
  position: relative;
  min-height: 100vh;
}

/* 自定義滾動條 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: var(--gray-100);
  border-radius: var(--radius-sm);
}

::-webkit-scrollbar-thumb {
  background: var(--gray-300);
  border-radius: var(--radius-sm);
}

::-webkit-scrollbar-thumb:hover {
  background: var(--gray-400);
}

/* 載入動畫 */
.loading {
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 3px solid var(--gray-200);
  border-radius: 50%;
  border-top-color: var(--primary-color);
  animation: spin 1s ease-in-out infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

/* 工具提示 */
.tooltip {
  position: relative;
  display: inline-block;
}

.tooltip .tooltiptext {
  visibility: hidden;
  width: 120px;
  background-color: var(--gray-800);
  color: var(--white);
  text-align: center;
  border-radius: var(--radius-sm);
  padding: var(--spacing-xs) var(--spacing-sm);
  position: absolute;
  z-index: 1;
  bottom: 125%;
  left: 50%;
  margin-left: -60px;
  opacity: 0;
  transition: opacity var(--transition-fast);
  font-size: var(--font-size-xs);
}

.tooltip:hover .tooltiptext {
  visibility: visible;
  opacity: 1;
}

/* 響應式設計 */
@media (max-width: 768px) {
  body {
    padding-top: 70px;
  }
}

.customer-service-btn {
  position: fixed;
  bottom: 20px;
  right: 20px;
  width: 60px;
  height: 60px;
  box-sizing: border-box;
  padding: 0;
  border-radius: 50%;
  background-color: #6c5ce7;
  color: white;
  border: none;
  font-size: 35px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  cursor: pointer;
  z-index: 9999;
  transition: background-color 0.3s ease, transform 0.3s ease, box-shadow 0.3s ease;
}

.customer-service-btn:hover {
  background-color: #5f3dc4;
  transform: translateY(-2px);
}

.back-to-top {
  position: fixed;
  bottom: 90px;
  right: 20px;
  width: 60px;
  height: 60px;
  background: var(--primary-color, #007bff);
  color: var(--white);
  border: none;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all var(--transition-normal, 0.3s ease);
  z-index: 1000;
  box-shadow: var(--shadow-lg, 0 4px 6px rgba(0, 0, 0, 0.1));
  /* 確保在小螢幕時不會因為重複定義而丟失這些屬性 */
}

.back-to-top:hover {
  background: var(--primary-dark, #0056b3);
  transform: translateY(-2px);
  box-shadow: var(--shadow-xl, 0 8px 16px rgba(0, 0, 0, 0.2));
}

.back-to-top i {
  font-size: 28px;
}

/* 響應式設計：針對小螢幕調整 back-to-top 按鈕的位置 */
@media (max-width: 768px) {
  .back-to-top {
    right: 10px;
    /* 在小螢幕時，將右側距離調整為 10px */
    /* bottom, width, height 不需要重複定義，因為它們在預設樣式中已經有了 */
  }
}
</style>
