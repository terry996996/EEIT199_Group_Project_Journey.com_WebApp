<template>
  <div class="recommendation-container">
    <!-- 個人化推薦區塊 -->
    <div class="recommendation-section">
      <div class="section-header">
        <h3 class="section-title">
          <i class="bi bi-star-fill text-warning me-2"></i>
          為您推薦
        </h3>
        <!-- <p class="section-subtitle">基於您的搜尋歷史和偏好</p> -->
      </div>

      <div class="recommendation-tabs">
        <button v-for="tab in tabs" :key="tab.id" class="tab-button" :class="{ active: activeTab === tab.id }"
          @click="activeTab = tab.id">
          <i :class="tab.icon"></i>
          {{ tab.name }}
        </button>
      </div>

      <!-- 推薦內容 -->
      <div class="recommendation-content">
        <!-- 熱門目的地 -->
        <div v-if="activeTab === 'popular'" class="tab-content">
          <div class="destination-grid">
            <div v-for="destination in popularDestinations" :key="destination.id" class="destination-card"
              @click="selectDestination(destination)">
              <div class="destination-image">
                <img :src="destination.image" :alt="destination.name" />
                <div class="destination-overlay">
                  <span class="popular-badge">熱門</span>
                </div>
              </div>
              <div class="destination-info">
                <h5>{{ destination.name }}</h5>
                <p class="destination-description">
                  {{ destination.description }}
                </p>
                <div class="destination-stats">
                  <span class="stat">
                    <i class="bi bi-star-fill text-warning"></i>
                    {{ destination.rating }}
                  </span>
                  <span class="stat">
                    <i class="bi bi-people-fill text-primary"></i>
                    {{ destination.visitors }}人訪問
                  </span>
                </div>
                <div class="destination-price">
                  <span class="price-from"></span>
                  <span class="price">NT$ {{ destination.startPrice }} 起</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 季節性推薦 -->
        <div v-if="activeTab === 'seasonal'" class="tab-content">
          <div class="seasonal-banner">
            <div class="seasonal-info">
              <h4>{{ currentSeason.name }}</h4>
              <p>{{ currentSeason.description }}</p>
            </div>
            <div class="seasonal-icon">
              <i :class="currentSeason.icon"></i>
            </div>
          </div>
          <div class="destination-grid">
            <div v-for="destination in seasonalDestinations" :key="destination.id" class="destination-card seasonal"
              @click="selectDestination(destination)">
              <div class="destination-image">
                <img :src="destination.image" :alt="destination.name" />
                <div class="destination-overlay">
                  <span class="seasonal-badge">{{ currentSeason.name }}</span>
                </div>
              </div>
              <div class="destination-info">
                <h5>{{ destination.name }}</h5>
                <p class="destination-description">
                  {{ destination.description }}
                </p>
                <div class="destination-stats">
                  <span class="stat">
                    <i class="bi bi-thermometer-half text-danger"></i>
                    {{ destination.temperature }}°C
                  </span>
                  <span class="stat">
                    <i class="bi bi-calendar-event text-success"></i>
                    {{ destination.bestTime }}
                  </span>
                </div>
                <div class="destination-price">
                  <span class="price-from"></span>
                  <span class="price">NT$ {{ destination.startPrice }} 起</span>
                  <span class="discount">-{{ destination.discount }}%</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 相似用戶推薦 -->
        <div v-if="activeTab === 'similar'" class="tab-content">
          <div class="similar-users-info">
            <p>基於與您相似用戶的選擇</p>
          </div>
          <div class="destination-grid">
            <div v-for="destination in similarUserDestinations" :key="destination.id" class="destination-card similar"
              @click="selectDestination(destination)">
              <div class="destination-image">
                <img :src="destination.image" :alt="destination.name" />
                <div class="destination-overlay">
                  <span class="similar-badge">相似用戶選擇</span>
                </div>
              </div>
              <div class="destination-info">
                <h5>{{ destination.name }}</h5>
                <p class="destination-description">
                  {{ destination.description }}
                </p>
                <div class="destination-stats">
                  <span class="stat">
                    <i class="bi bi-heart-fill text-danger"></i>
                    {{ destination.similarity }}% 相似度
                  </span>
                  <span class="stat">
                    <i class="bi bi-people-fill text-primary"></i>
                    {{ destination.similarUsers }}位相似用戶
                  </span>
                </div>
                <div class="destination-price">
                  <span class="price-from"></span>
                  <span class="price">NT$ {{ destination.startPrice }} 起</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 最近瀏覽 -->
        <div v-if="activeTab === 'recent'" class="tab-content">
          <div v-if="recentViews.length > 0" class="destination-grid">
            <div v-for="item in recentViews" :key="item.id" class="destination-card recent"
              @click="selectDestination(item)">
              <div class="destination-image">
                <img :src="item.image" :alt="item.name" />
                <div class="destination-overlay">
                  <span class="recent-badge">最近瀏覽</span>
                </div>
              </div>
              <div class="destination-info">
                <h5>{{ item.name }}</h5>
                <p class="destination-description">{{ item.description }}</p>
                <div class="destination-stats">
                  <span class="stat">
                    <i class="bi bi-clock text-info"></i>
                    {{ formatTimeAgo(item.viewTime) }}
                  </span>
                </div>
                <div class="destination-price">
                  <span class="price-from"></span>
                  <span class="price">NT$ {{ item.startPrice }} 起</span>
                </div>
              </div>
            </div>
          </div>
          <div v-else class="empty-state">
            <i class="bi bi-eye-slash text-muted"></i>
            <p>還沒有瀏覽記錄</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import taipei101Img from '@/assets/images/taipei-101.jpg';
import jiufenImg from '@/assets/images/九分.jpg';
import sunmoonlakeImg from '@/assets/images/日月潭.jpg';
import alishanImg from '@/assets/images/阿里山.jpg';

const emit = defineEmits(['select']);

// 響應式資料
const activeTab = ref('popular');
const recentViews = ref([]);

// 標籤頁
const tabs = [
  { id: 'popular', name: '熱門目的地', icon: 'bi bi-fire' },
  { id: 'seasonal', name: '季節推薦', icon: 'bi bi-calendar3' },
  { id: 'similar', name: '相似用戶', icon: 'bi bi-people' },
  { id: 'recent', name: '最近瀏覽', icon: 'bi bi-clock-history' }
];

// 當前季節
const currentSeason = computed(() => {
  const month = new Date().getMonth();
  if (month >= 3 && month <= 5) {
    return {
      name: '春季',
      description: '春暖花開，適合賞花踏青',
      icon: 'bi bi-flower1'
    };
  } else if (month >= 6 && month <= 8) {
    return {
      name: '夏季',
      description: '夏日炎炎，適合海邊避暑',
      icon: 'bi bi-sun'
    };
  } else if (month >= 9 && month <= 11) {
    return {
      name: '秋季',
      description: '秋高氣爽，適合戶外活動',
      icon: 'bi bi-tree'
    };
  } else {
    return {
      name: '冬季',
      description: '冬日暖陽，適合溫泉泡湯',
      icon: 'bi bi-snow'
    };
  }
});

// 熱門目的地資料
const popularDestinations = [
  {
    id: 1,
    name: '台北101',
    description: '台北地標建築，觀景台俯瞰全城',
    image: taipei101Img, // 改為import變數
    rating: 4.5,
    visitors: '2.3萬',
    startPrice: 600
  },
  {
    id: 2,
    name: '九份老街',
    description: '山城小鎮，懷舊風情濃厚',
    image: jiufenImg, // 改為import變數
    rating: 4.3,
    visitors: '1.8萬',
    startPrice: 300
  },
  {
    id: 3,
    name: '日月潭',
    description: '台灣最美湖泊，湖光山色',
    image: sunmoonlakeImg, // 改為import變數
    rating: 4.6,
    visitors: '1.5萬',
    startPrice: 800
  },
  {
    id: 4,
    name: '阿里山',
    description: '雲海日出，森林鐵路',
    image: alishanImg, // 改為import變數
    rating: 4.4,
    visitors: '1.2萬',
    startPrice: 1200
  }
];

// 季節性推薦資料
const seasonalDestinations = [
  {
    id: 5,
    name: '陽明山國家公園',
    description: '春季賞櫻，杜鵑花海',
    image: 'image/location/a.jpg',
    temperature: 18,
    bestTime: '3-5月',
    startPrice: 400,
    discount: 15
  },
  {
    id: 6,
    name: '墾丁國家公園',
    description: '夏日海灘，水上活動',
    image: 'image/location/b.jpg',
    temperature: 28,
    bestTime: '6-8月',
    startPrice: 600,
    discount: 20
  },
  {
    id: 7,
    name: '太魯閣峽谷',
    description: '秋日健行，峽谷風光',
    image: 'image/location/c.jpg',
    temperature: 22,
    bestTime: '9-11月',
    startPrice: 500,
    discount: 10
  },
  {
    id: 8,
    name: '北投溫泉',
    description: '冬日泡湯，暖身暖心',
    image: 'image/location/d.jpg',
    temperature: 15,
    bestTime: '12-2月',
    startPrice: 800,
    discount: 25
  }
];

// 相似用戶推薦資料
const similarUserDestinations = [
  {
    id: 9,
    name: '淡水老街',
    description: '河岸風光，美食小吃',
    image: 'image/location/e.jpg',
    similarity: 85,
    similarUsers: 156,
    startPrice: 200
  },
  {
    id: 10,
    name: '平溪天燈',
    description: '傳統文化，祈福許願',
    image: 'image/location/f.jpg',
    similarity: 78,
    similarUsers: 89,
    startPrice: 350
  },
  {
    id: 11,
    name: '野柳地質公園',
    description: '奇岩怪石，海岸風光',
    image: 'image/location/g.jpg',
    similarity: 72,
    similarUsers: 134,
    startPrice: 250
  },
  {
    id: 12,
    name: '基隆廟口夜市',
    description: '海港美食，夜市文化',
    image: 'image/location/h.jpg',
    similarity: 68,
    similarUsers: 203,
    startPrice: 150
  }
];

// 選擇目的地
const selectDestination = (destination) => {
  // 記錄瀏覽歷史
  addToRecentViews(destination);

  // 發送事件
  emit('select', destination);
};

// 新增瀏覽記錄
const addToRecentViews = (destination) => {
  const viewRecord = {
    ...destination,
    viewTime: new Date().toISOString()
  };

  const views = JSON.parse(localStorage.getItem('recentViews') || '[]');
  const newViews = [viewRecord, ...views.filter((item) => item.id !== destination.id)].slice(
    0,
    8
  );

  localStorage.setItem('recentViews', JSON.stringify(newViews));
  recentViews.value = newViews;
};

// 格式化時間
const formatTimeAgo = (timestamp) => {
  const now = new Date();
  const time = new Date(timestamp);
  const diff = now - time;

  const minutes = Math.floor(diff / 60000);
  const hours = Math.floor(diff / 3600000);
  const days = Math.floor(diff / 86400000);

  if (minutes < 60) {
    return `${minutes}分鐘前`;
  } else if (hours < 24) {
    return `${hours}小時前`;
  } else {
    return `${days}天前`;
  }
};

// 載入瀏覽記錄
onMounted(() => {
  recentViews.value = JSON.parse(localStorage.getItem('recentViews') || '[]');
});
</script>

<style scoped>
.recommendation-container {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  padding: 24px;
  margin-bottom: 24px;
}

.section-header {
  text-align: center;
  margin-bottom: 24px;
}

.section-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #333;
  margin-bottom: 8px;
}

.section-subtitle {
  color: #6c757d;
  font-size: 0.9rem;
  margin: 0;
}

.recommendation-tabs {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.tab-button {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  border: 2px solid #e9ecef;
  background: #fff;
  border-radius: 25px;
  font-weight: 500;
  color: #495057;
  cursor: pointer;
  transition: all 0.3s ease;
}

.tab-button:hover {
  border-color: #0080ff;
  color: #0080ff;
}

.tab-button.active {
  background: #0080ff;
  border-color: #0080ff;
  color: #fff;
}

.tab-button i {
  font-size: 1.1rem;
}

.seasonal-banner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  padding: 20px;
  border-radius: 12px;
  margin-bottom: 20px;
}

.seasonal-info h4 {
  margin: 0 0 8px 0;
  font-weight: 600;
}

.seasonal-info p {
  margin: 0;
  opacity: 0.9;
  color: white;
}

.seasonal-icon i {
  font-size: 3rem;
  opacity: 0.8;
}

.destination-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
}

.destination-card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  cursor: pointer;
  transition: all 0.3s ease;
}

.destination-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.destination-image {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.destination-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.destination-card:hover .destination-image img {
  transform: scale(1.05);
}

.destination-overlay {
  position: absolute;
  top: 12px;
  right: 12px;
}

.popular-badge,
.seasonal-badge,
.similar-badge,
.recent-badge {
  background: rgba(0, 0, 0, 0.7);
  color: #fff;
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 500;
}

.seasonal-badge {
  background: rgba(255, 193, 7, 0.9);
  color: #000;
}

.similar-badge {
  background: rgba(220, 53, 69, 0.9);
}

.recent-badge {
  background: rgba(13, 202, 240, 0.9);
}

.destination-info {
  padding: 16px;
}

.destination-info h5 {
  font-size: 1.1rem;
  font-weight: 600;
  margin: 0 0 8px 0;
  color: #333;
}

.destination-description {
  color: #6c757d;
  font-size: 0.9rem;
  margin: 0 0 12px 0;
  line-height: 1.4;
}

.destination-stats {
  display: flex;
  gap: 16px;
  margin-bottom: 12px;
}

.stat {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 0.85rem;
  color: #495057;
}

.stat i {
  font-size: 0.9rem;
}

.destination-price {
  display: flex;
  align-items: baseline;
  gap: 4px;
}

.price-from {
  font-size: 0.8rem;
  color: #6c757d;
}

.price {
  font-size: 1.2rem;
  font-weight: 700;
  color: #0080ff;
}

.discount {
  background: #dc3545;
  color: #fff;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 0.75rem;
  font-weight: 500;
}

.similar-users-info {
  text-align: center;
  margin-bottom: 20px;
  color: #6c757d;
  font-style: italic;
}

.empty-state {
  text-align: center;
  padding: 40px 20px;
  color: #6c757d;
}

.empty-state i {
  font-size: 3rem;
  margin-bottom: 16px;
  display: block;
}

.empty-state p {
  margin: 0;
  font-size: 1.1rem;
}

@media (max-width: 768px) {
  .recommendation-tabs {
    justify-content: flex-start;
    overflow-x: auto;
    padding-bottom: 8px;
  }

  .tab-button {
    white-space: nowrap;
  }

  .destination-grid {
    grid-template-columns: 1fr;
  }

  .seasonal-banner {
    flex-direction: column;
    text-align: center;
    gap: 16px;
  }
}
</style>
