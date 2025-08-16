<template>
  <div class="my-journey-page">
    <!-- Header -->
    <Header />

    <div class="container my-5 pt-4">
      <div class="row">
        <!-- 左側：我的旅程 -->
        <div class="col-md-8">
          <div class="card">
            <div class="card-header bg-white">
              <h5 class="mb-0">我的旅程</h5>
            </div>
            <div class="card-body">
              <div v-if="journeyItems.length === 0" class="text-center py-5">
                <i class="bi bi-bag-x fs-1 text-muted"></i>
                <p class="mt-3 text-muted">您的旅程還是空的</p>
                <router-link to="/" class="btn btn-primary">
                  開始規劃旅程
                </router-link>
              </div>

              <div v-else>
                <!-- 旅程項目列表 -->
                <div v-for="item in journeyItems" :key="`${item.type}-${item.id}`" class="journey-item mb-3">
                  <div class="card">
                    <div class="row g-0">
                      <div class="col-md-4">
                        <img :src="item.image" class="img-fluid rounded-start" :alt="item.name" />
                      </div>
                      <div class="col-md-8">
                        <div class="card-body">
                          <div class="d-flex justify-content-between">
                            <h5 class="card-title">{{ item.name }}</h5>
                            <button class="btn btn-link text-danger" @click="removeFromJourney(item)">
                              <i class="bi bi-trash"></i>
                            </button>
                          </div>
                          <p class="card-text">
                            <small class="text-muted">
                              <i class="bi bi-geo-alt"></i>
                              {{ item.location || item.route }}
                            </small>
                            <!-- 旅館專屬資訊 -->
                            <div v-if="item.type === 'hotel'" class="mt-2">
                              <small class="text-primary" v-if="item.roomTypeName">
                                <i class="bi bi-house"></i> {{ item.roomTypeName }}
                              </small>
                              <small class="text-muted ms-2" v-if="item.maxGuests">
                                <i class="bi bi-people"></i> 可住 {{ item.maxGuests }} 人
                              </small>
                            </div>
                          </p>
                          <div class="d-flex justify-content-between align-items-center">
                            <div class="quantity-control">
                              <button class="btn btn-outline-secondary btn-sm" @click="updateQuantity(item, -1)"
                                :disabled="item.quantity <= 1">
                                <i class="bi bi-dash"></i>
                              </button>
                              <span class="mx-2">{{ item.quantity }}</span>
                              <button class="btn btn-outline-secondary btn-sm" @click="updateQuantity(item, 1)">
                                <i class="bi bi-plus"></i>
                              </button>
                            </div>
                            <div class="price">
                              NT$
                              {{
                                (item.price * item.quantity).toLocaleString()
                              }}
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- 總計和結帳按鈕 -->
                <div class="card mt-4">
                  <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                      <div>
                        <h6 class="mb-0">總計</h6>
                        <small class="text-muted">{{ totalItems }} 項商品</small>
                      </div>
                      <div class="text-end">
                        <h5 class="mb-0">
                          NT$ {{ totalPrice.toLocaleString() }}
                        </h5>
                      </div>
                    </div>
                    <button class="btn btn-primary w-100 mt-3" @click="proceedToCheckout">
                      前往結帳
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 右側：我的收藏 -->
        <div class="col-md-4">
          <div class="card">
            <div class="card-header bg-white">
              <h5 class="mb-0">我的收藏</h5>
            </div>
            <div class="card-body">
              <div v-if="favorites.length === 0" class="text-center py-5">
                <i class="bi bi-heart fs-1 text-muted"></i>
                <p class="mt-3 text-muted">您還沒有收藏任何項目</p>
              </div>

              <div v-else class="favorites-list">
                <div v-for="item in favorites" :key="`${item.type}-${item.id}`" class="favorite-item mb-3">
                  <div class="card">
                    <img :src="item.image" class="card-img-top" :alt="item.name" />
                    <div class="card-body">
                      <h6 class="card-title">{{ item.name }}</h6>
                      <p class="card-text">
                        <small class="text-muted">
                          <i class="bi bi-geo-alt"></i>
                          {{ item.location || item.route }}
                        </small>
                      </p>
                      <div class="d-flex justify-content-between align-items-center">
                        <span class="price">NT$ {{ item.price.toLocaleString() }}</span>
                        <button class="btn btn-outline-primary btn-sm" @click="addToJourneyFromFavorites(item)">
                          加入旅程
                        </button>
                      </div>
                    </div>
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
  import { ref, computed, onMounted, onUnmounted } from "vue";
  import { useRouter } from "vue-router";
  import Header from "../components/Header.vue";


  const myPlan = {
    id: crypto.randomUUID(),
    title: '五天四夜東京自由行',
    description: '大學畢業旅行',
    startDate: '2025-07-12',
    endDate: '2025-07-16',
    isPublic: false,
    views: 0,
    items: [],
  };


  const Flight = {
    go: {
      code: 'CI220',
      from: 'TSA',
      fromT: 'T1',
      to: 'HND',
      toT: 'T1',
      departTime: '2025-07-12 09:00',
      arriveTime: '2025-07-12 12:10',
    },

    back: {
      code: 'JX803',
      from: 'NRT',
      fromT: 'T2',
      to: 'TPE',
      toT: 'T1',
      departTime: '2025-07-16 16:20',
      arriveTime: '2025-07-16 19:00',
    },
  };

  const Hotel = {
    h1: {
      name: 'Tマークシティホテル東京大森',
      lat: '35.5873196',
      lon: '139.7357135',
      address: '1 Chome-2-10 Omorihoncho, Ota City, Tokyo 143-0011日本',
      checkin: '2025-07-12 15:00',
      checkout: '2025-07-14 11:00',
    },

    h2: {
      name: '東横INN千葉駅東口',
      lat: '35.6127588',
      lon: '140.1135096',
      address: '1 Chome-9-12 Fujimi, Chuo Ward, Chiba, 260-0015日本',
      checkin: '2025-07-14 15:00',
      checkout: '2025-07-16 11:00',
    }
  };

  class spots {
    constructor(title, description, lat, lon, address, startTime, endTime) {
      this.title = title;
      this.description = description;
      this.lat = lat;
      this.lon = lon;
      this.address = address;
      this.startTime = startTime;
      this.endTime = endTime;
    }
  };

  const router = useRouter();
  const journeyItems = ref([]);
  const favorites = ref([]);

  // 載入數據
  const loadJourneyItems = () => {
    try {
      const items = JSON.parse(localStorage.getItem("journey") || "[]");
      journeyItems.value = items;
    } catch (error) {
      console.error("Error loading journey items:", error);
      journeyItems.value = [];
    }
  };

  // 載入收藏項目
  const loadFavorites = () => {
    try {
      const items = JSON.parse(localStorage.getItem("favorites") || "[]");
      favorites.value = items;
    } catch (error) {
      console.error("Error loading favorites:", error);
      favorites.value = [];
    }
  };

  // 計算總價
  const totalPrice = computed(() => {
    return journeyItems.value.reduce((total, item) => {
      return total + item.price * (item.quantity || 1);
    }, 0);
  });

  // 計算總數量
  const totalItems = computed(() => {
    return journeyItems.value.reduce((total, item) => {
      return total + (item.quantity || 1);
    }, 0);
  });

  // 更新數量
  const updateQuantity = (item, change) => {
    const newQuantity = (item.quantity || 1) + change;
    if (newQuantity < 1) return;

    item.quantity = newQuantity;
    localStorage.setItem("journey", JSON.stringify(journeyItems.value));

    // 觸發事件通知 Header 組件更新計數
    window.dispatchEvent(new Event("journeyUpdated"));
  };

  // 從旅程中移除項目
  const removeFromJourney = (item) => {
    const index = journeyItems.value.findIndex(
      (i) => i.id === item.id && i.type === item.type
    );
    if (index > -1) {
      journeyItems.value.splice(index, 1);
      localStorage.setItem("journey", JSON.stringify(journeyItems.value));

      // 觸發事件通知 Header 組件更新計數
      window.dispatchEvent(new Event("journeyUpdated"));
    }
  };

  // 從收藏加入旅程
  const addToJourneyFromFavorites = (item) => {
    const existingIndex = journeyItems.value.findIndex(
      (i) => i.id === item.id && i.type === item.type
    );

    if (existingIndex > -1) {
      // 如果已存在，增加數量
      journeyItems.value[existingIndex].quantity =
        (journeyItems.value[existingIndex].quantity || 1) + 1;
    } else {
      // 新增到旅程
      journeyItems.value.push({
        ...item,
        quantity: 1,
      });
    }

    localStorage.setItem("journey", JSON.stringify(journeyItems.value));

    // 觸發事件通知 Header 組件更新計數
    window.dispatchEvent(new Event("journeyUpdated"));
  };

  // 前往結帳
  const proceedToCheckout = () => {
    if (journeyItems.value.length > 0) {
      // 將旅程數據存儲到 localStorage，供 Checkout 頁面使用
      const checkoutData = {
        items: journeyItems.value,
        totalPrice: totalPrice.value,
        totalItems: totalItems.value,
        timestamp: new Date().toISOString(),
      };
      localStorage.setItem("checkoutData", JSON.stringify(checkoutData));

      router.push("/checkout");
    }
  };

  // 監聽數據更新事件
  const handleJourneyUpdate = () => {
    loadJourneyItems();
  };

  const handleFavoritesUpdate = () => {
    loadFavorites();
  };

  // 組件掛載時初始化
  onMounted(() => {
    loadJourneyItems();
    loadFavorites();

    // 監聽數據更新事件
    window.addEventListener("journeyUpdated", handleJourneyUpdate);
    window.addEventListener("favoritesUpdated", handleFavoritesUpdate);
  });

  // 組件卸載時清理
  onUnmounted(() => {
    window.removeEventListener("journeyUpdated", handleJourneyUpdate);
    window.removeEventListener("favoritesUpdated", handleFavoritesUpdate);
  });
</script>

<style scoped>
  .my-journey-page {
    min-height: 100vh;
    background-color: #f8f9fa;
  }

  .journey-item img {
    height: 200px;
    object-fit: cover;
  }

  .favorites-list img {
    height: 150px;
    object-fit: cover;
  }

  .quantity-control {
    display: flex;
    align-items: center;
  }

  .quantity-control button {
    width: 30px;
    height: 30px;
    padding: 0;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .price {
    font-weight: 600;
    color: #0d6efd;
  }

  .card {
    border: none;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  }

  .card-header {
    border-bottom: 1px solid rgba(0, 0, 0, 0.08);
  }

  .favorite-item .card:hover {
    transform: translateY(-2px);
    transition: transform 0.2s;
  }
</style>
