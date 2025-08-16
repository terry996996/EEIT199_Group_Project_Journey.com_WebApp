<template>
  <div class="card h-100 position-relative">
    <!-- 收藏按鈕 -->
    <button class="favorite-btn" @click="toggleFavorite" :class="{ 'is-favorited': isFavorited }">
      <i class="bi" :class="isFavorited ? 'bi-heart-fill' : 'bi-heart'"></i>
    </button>

    <router-link :to="'/traffic/' + traffic.id" class="text-decoration-none">
      <img :src="traffic.imageURL" class="card-img-top" :alt="traffic.name" />
      <div class="card-body">
        <h5 class="card-title">{{ traffic.name }}</h5>
        <p class="card-text text-muted"><i class="bi bi-geo-alt"></i> {{ traffic.area }}</p>
        <div class="d-flex justify-content-between align-items-center">
          <div class="rating">
            <i v-for="star in 5" :key="star"
              :class="['bi', star <= traffic.rating ? 'bi-star-fill' : 'bi-star', 'text-warning']"></i>
            <span class="ms-1">{{ traffic.rating }}</span>
          </div>
          <div class="price">
            <span class="text-primary fw-bold">NT$ {{ traffic.price }}</span>
          </div>
        </div>
      </div>
    </router-link>
    <div class="card-footer bg-white border-top-0">
      <div class="d-flex gap-2">
        <button class="btn btn-outline-primary flex-grow-1" @click="addToJourney">
          <i class="bi bi-plus-circle me-1"></i>加入旅程
        </button>
        <button class="btn btn-primary flex-grow-1" @click="handleBook">立即預訂</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const isFavorited = ref(false);

const props = defineProps({
  traffic: {
    type: Object,
    required: true
  }, traffic: Object


});

const emit = defineEmits(['book', 'addToJourney', 'toggleFavorite']);

// 檢查收藏狀態
const checkFavoriteStatus = () => {
  try {
    const favorites = JSON.parse(localStorage.getItem('favorites') || '[]');
    isFavorited.value = favorites.some(
      (item) => item.id === props.traffic.id && item.type === 'traffic'
    );
  } catch (error) {
    console.error('Error checking favorite status:', error);
    isFavorited.value = false;
  }
};

// 監聽收藏更新事件
const handleFavoritesUpdate = () => {
  checkFavoriteStatus();
};

onMounted(() => {
  checkFavoriteStatus();

  // 監聽收藏更新事件
  window.addEventListener('favoritesUpdated', handleFavoritesUpdate);
});

onUnmounted(() => {
  window.removeEventListener('favoritesUpdated', handleFavoritesUpdate);
});

const goToDetail = () => {
  router.push(`/traffic-detail/${props.traffic.id}`);
};

const handleBook = () => {
  emit('book', props.traffic);
};

const toggleFavorite = () => {
  try {
    const favorites = JSON.parse(localStorage.getItem('favorites') || '[]');
    const existingIndex = favorites.findIndex(
      (fav) => fav.id === props.traffic.id && fav.type === 'traffic'
    );

    if (existingIndex > -1) {
      // 取消收藏
      favorites.splice(existingIndex, 1);
      isFavorited.value = false;
    } else {
      // 加入收藏
      favorites.push({
        id: props.traffic.id,
        type: 'traffic',
        name: props.traffic.name,
        image: props.traffic.image,
        price: props.traffic.price,
        route: props.traffic.route,
        rating: props.traffic.rating,
        provider: props.traffic.provider,
        duration: props.traffic.duration,
        features: props.traffic.features
      });
      isFavorited.value = true;
    }

    localStorage.setItem('favorites', JSON.stringify(favorites));

    // 觸發事件通知其他組件更新
    window.dispatchEvent(new Event('favoritesUpdated'));

    emit('toggleFavorite', {
      traffic: props.traffic,
      isFavorited: isFavorited.value
    });
  } catch (error) {
    console.error('Error toggling favorite:', error);
  }
};

const addToJourney = () => {
  try {
    const journey = JSON.parse(localStorage.getItem('journey') || '[]');
    const existingIndex = journey.findIndex(
      (item) => item.id === props.traffic.id && item.type === 'traffic'
    );

    if (existingIndex > -1) {
      // 如果已存在，增加數量
      journey[existingIndex].quantity = (journey[existingIndex].quantity || 1) + 1;
    } else {
      // 新增到旅程
      journey.push({
        id: props.traffic.id,
        type: 'traffic',
        name: props.traffic.name,
        image: props.traffic.image,
        price: props.traffic.price,
        route: props.traffic.route,
        rating: props.traffic.rating,
        provider: props.traffic.provider,
        duration: props.traffic.duration,
        features: props.traffic.features,
        quantity: 1
      });
    }

    localStorage.setItem('journey', JSON.stringify(journey));

    // 觸發事件通知其他組件更新
    window.dispatchEvent(new Event('journeyUpdated'));

    emit('addToJourney', props.traffic);
  } catch (error) {
    console.error('Error adding to journey:', error);
  }
};
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
