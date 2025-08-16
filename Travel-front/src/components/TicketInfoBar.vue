<template>
  <div class="ticket-info-wrapper">
    <div class="ticket-info-top d-flex flex-column flex-md-row justify-content-between align-items-start bg-white p-3 mb-0">   
      <!-- 上半部：評價與地點 -->
      <div class="info-left">
        <div class="d-flex align-items-center mb-2 mb-md-0">
          <span class="text-success fw-bold me-2">{{ rating }}</span>
          <div class="text-muted small">
            ({{ reviewCount }} 則評價) · 已售出 {{ soldCount }}
          </div>
        </div>
        <div class="text-muted small">
          <i class="bi bi-geo-alt-fill me-1"></i>{{ location }}
        </div>
        <ul class="small ps-3 info">
          <li v-for="(item, i) in info" :key="i">{{ item }}</li>
        </ul>
      </div>

      <!-- 下半部：價格與選擇按鈕 -->
      <div class="info-select-box">
        <div class="price-wrapper">
          <div class="info-select-box-price">NT$ {{ price }}起</div>
        </div>
        <button class="btn custom-select-btn"  @click="$emit('scrollToPackage')">選擇方案</button>
        <div class="instant-confirm d-flex align-items-center mt-2">
          <i class="bi bi-lightning-charge-fill text-primary me-1"></i>
          <span class="text-muted small">立即確認</span>
        </div>
      </div>
      <!-- 選擇方案浮動版本 -->
          <div
            v-if="showFloating"
            class="info-select-box-floating d-flex justify-content-between align-items-center"
          >
            <div class="slide-box-price">NT$ {{ price }} 起</div>
            <button class="custom-select-btn-slide"  @click="$emit('scrollToPackage')">選擇方案</button>
          </div>
    </div>
  </div>
</template>

  
<script setup>
import { ref,onMounted, onBeforeUnmount } from 'vue'
//<!-- 選擇方案浮動版本 -->


const showFloating = ref(false) // 控制浮動框是否顯示

const handleScroll = () => {
  const section = document.getElementById('section-ticket')
  if (!section) return

  const rect = section.getBoundingClientRect()
  // 當「景點說明區」快貼到畫面頂部（或超過）時，顯示浮動框
  showFloating.value = rect.top < 100
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})
onBeforeUnmount(() => {
  window.removeEventListener('scroll', handleScroll)
})
const props = defineProps({
  rating: Number,
  reviewCount: Number,
  soldCount: String,
  location: String,
  price: Number
})

const info = ref([
  '日本環球影城官方授權門票，中文介面讓您輕鬆訂購。',
  '在訂購日本環球影城門票後可立即拿到電子門票，現場掃 QR code 即可入園遊玩。',
  '有效期限內，可依照日本環球影城月曆中相同票種之日期入園，保有行程絕佳彈性。',
  '日本環球影城門票＋超級任天堂世界™ 園區保證入場套票，輕鬆玩轉瑪利歐世界。',
  '超級任天堂世界™區域入場號碼券取得方式之參考網站及影片。'
])
</script>

  
  <style scoped>
  .ticket-info-top {
    background-color: #fff;
  }
  
  .info-select-box {
    padding: 1rem;
    border: 1px solid #ddd;
    border-radius: 0.25rem;
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  }
  
  .ticket-info-wrapper {
    margin-bottom: 1.5rem;
  }
  .info {
  font-size: 1rem;      /* 字體大小，可依需求調整 ex: 1.1rem, 18px */
  line-height: 2;     /* 行距，1.6~2 都不錯看 */
}
.custom-select-btn {
  background-color: #00AEEF;  /* 自訂主色 */
  color: #fff;                /* 字體白色 */
  width: 350px;
  height: 48px;
  border-radius: 6px;
  font-size: 1rem;
  font-weight: 500;
  border: none;
  margin:auto;
}

.custom-select-btn:hover {
  background-color: #008fc6;
}
.info-select-box-price{
  font-size: 1.5rem;
  font-weight: bold;
  color: black;
  margin-left:10px;
}
.price-wrapper{
    align-self: flex-start;
}

/* 浮動選擇方案區塊（電腦版用） */
.info-select-box-floating {
  position: fixed;              /* 固定在畫面上，不隨頁面捲動 */
  top: 58px;                    /* 距離畫面頂部 70px（根據 Header 高度可調整） */
  left: 0;                      /* 從左側貼齊 */
  right: 0;                     /* 撐滿右側寬度，讓整排寬度 = 畫面寬度 */
  background-color: #fff;       /* 背景色為白色 */
  /* box-shadow: 0 2px 8px rgba(0,0,0,0.1);  下方陰影，讓它浮起來的感覺 */
  padding: 0.75rem;             /* 內距（上下左右） */
  z-index: 999;                 /* 層級很高，蓋在大多數元素上面 */
  border-bottom: 1px solid #eee; /* 底部邊框，讓它與下方有區隔感 */
  border-top: 1px solid #eee;    /* NEW：加上頂部邊框 */
  border-bottom: 1px solid #eee; /* 原本就有的底部邊框 */
}
/* 浮動價格設置 */
.slide-box-price{
  margin-left: 438px;
  font-weight: bolder;
  color: #000000;
  font-size: large;
}
/* 浮動選擇方案格式 */
.custom-select-btn-slide{
  background-color: #00AEEF;  /* 自訂主色 */
  color: #fff;                /* 字體白色 */
  width: 200px;
  height: 40px;
  border-radius: 6px;
  font-size: 1rem;
  font-weight: 500;
  border: none;
  margin-right:300px;

}
.custom-select-btn-slide:hover {
  background-color: #008fc6;
}
/* 響應式設計：當螢幕寬度小於 768px（手機、平板）時隱藏浮動欄 */
@media (max-width: 768px) {
  .info-select-box-floating {
    display: none;              /* 小螢幕上不顯示，避免擋住畫面 */
  }
}
  </style>
  