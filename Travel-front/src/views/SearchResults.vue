<template>
  <div class="search-results-container">
    <!-- Header -->
    <Header />

    <div class="container mt-5 pt-5">
      <!-- 搜尋結果標題 -->
      <div class="results-header">
        <div class="search-info">
          <h2>搜尋結果</h2>
          <p class="text-muted">
            找到 {{ filteredResults.length }} 個結果
            <span v-if="searchQuery">關於 "{{ searchQuery }}"</span>
          </p>
        </div>

        <!-- 排序選項 -->
        <div class="sort-options">
          <label class="form-label">排序方式：</label>
          <select v-model="sortBy" class="form-select" @change="handleSort">
            <option value="relevance">相關性</option>
            <option value="price-low">價格由低到高</option>
            <option value="price-high">價格由高到低</option>
            <option value="rating">評分</option>
            <option value="popularity">熱門程度</option>
          </select>
        </div>
      </div>

      <!-- 篩選側邊欄 -->
      <div class="row">
        <div class="col-lg-3">
          <div class="filters-sidebar">
            <h5>篩選條件</h5>

            <!-- 價格範圍 -->
            <div class="filter-section">
              <h6>價格範圍</h6>
              <div class="price-range">
                <input
                  v-model="priceRange.min"
                  type="number"
                  class="form-control mb-2"
                  placeholder="最低價格"
                  @input="applyFilters"
                />
                <input
                  v-model="priceRange.max"
                  type="number"
                  class="form-control"
                  placeholder="最高價格"
                  @input="applyFilters"
                />
              </div>
            </div>

            <!-- 評分篩選 -->
            <div class="filter-section">
              <h6>最低評分</h6>
              <div class="rating-filter">
                <label v-for="rating in 5" :key="rating" class="rating-option">
                  <input
                    v-model="selectedRating"
                    type="radio"
                    :value="rating"
                    @change="applyFilters"
                  />
                  <span class="stars">
                    <i
                      v-for="star in 5"
                      :key="star"
                      :class="[
                        'bi',
                        star <= rating ? 'bi-star-fill' : 'bi-star',
                        'text-warning',
                      ]"
                    ></i>
                  </span>
                  <span class="rating-text">{{ rating }}+</span>
                </label>
              </div>
            </div>

            <!-- 類型篩選 -->
            <div class="filter-section">
              <h6>商品類型</h6>
              <div class="type-filter">
                <label
                  v-for="type in productTypes"
                  :key="type.id"
                  class="type-option"
                >
                  <input
                    v-model="selectedTypes"
                    type="checkbox"
                    :value="type.id"
                    @change="applyFilters"
                  />
                  <i :class="type.icon"></i>
                  {{ type.name }}
                  <span class="count">({{ getTypeCount(type.id) }})</span>
                </label>
              </div>
            </div>

            <!-- 清除篩選 -->
            <button
              class="btn btn-outline-secondary w-100 mt-3"
              @click="clearFilters"
            >
              清除所有篩選
            </button>
          </div>
        </div>

        <!-- 搜尋結果 -->
        <div class="col-lg-9">
          <div v-if="filteredResults.length > 0" class="results-grid">
            <div
              v-for="item in paginatedResults"
              :key="item.id"
              class="result-card"
            >
              <div class="result-image">
                <img :src="item.image" :alt="item.name" />
                <div class="result-badge" :class="getTypeClass(item.type)">
                  {{ getTypeName(item.type) }}
                </div>
              </div>
              <div class="result-content">
                <h5 class="result-title">{{ item.name }}</h5>
                <p class="result-description">{{ item.description }}</p>
                <div class="result-meta">
                  <div class="rating">
                    <i
                      v-for="star in 5"
                      :key="star"
                      :class="[
                        'bi',
                        star <= item.rating ? 'bi-star-fill' : 'bi-star',
                        'text-warning',
                      ]"
                    ></i>
                    <span class="rating-text">{{ item.rating }}</span>
                  </div>
                  <div class="location">
                    <i class="bi bi-geo-alt"></i>
                    {{ item.location }}
                  </div>
                </div>
                <div class="result-price">
                  <span class="price"
                    >NT$ {{ item.price.toLocaleString() }}</span
                  >
                  <span v-if="item.originalPrice" class="original-price">
                    NT$ {{ item.originalPrice.toLocaleString() }}
                  </span>
                </div>
                <div class="result-actions">
                  <button class="btn btn-primary" @click="viewDetails(item)">
                    查看詳情
                  </button>
                  <button
                    class="btn btn-outline-primary"
                    @click="addToCart(item)"
                  >
                    加入購物車
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- 分頁 -->
          <div v-if="totalPages > 1" class="pagination-wrapper">
            <nav>
              <ul class="pagination justify-content-center">
                <li class="page-item" :class="{ disabled: currentPage === 1 }">
                  <button
                    class="page-link"
                    @click="changePage(currentPage - 1)"
                    :disabled="currentPage === 1"
                  >
                    上一頁
                  </button>
                </li>
                <li
                  v-for="page in visiblePages"
                  :key="page"
                  class="page-item"
                  :class="{ active: page === currentPage }"
                >
                  <button class="page-link" @click="changePage(page)">
                    {{ page }}
                  </button>
                </li>
                <li
                  class="page-item"
                  :class="{ disabled: currentPage === totalPages }"
                >
                  <button
                    class="page-link"
                    @click="changePage(currentPage + 1)"
                    :disabled="currentPage === totalPages"
                  >
                    下一頁
                  </button>
                </li>
              </ul>
            </nav>
          </div>

          <!-- 無結果 -->
          <div v-else class="no-results">
            <div class="no-results-content">
              <i class="bi bi-search text-muted"></i>
              <h4>沒有找到相關結果</h4>
              <p>請嘗試調整搜尋條件或篩選器</p>
              <button class="btn btn-primary" @click="clearFilters">
                清除篩選
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import Header from "../components/Header.vue";

const route = useRoute();
const router = useRouter();

// 響應式資料
const searchQuery = ref("");
const sortBy = ref("relevance");
const selectedRating = ref(0);
const selectedTypes = ref([]);
const priceRange = ref({ min: "", max: "" });
const currentPage = ref(1);
const itemsPerPage = 12;

// 商品類型
const productTypes = [
  { id: "hotel", name: "飯店", icon: "bi bi-house" },
  { id: "flight", name: "機票", icon: "bi bi-airplane" },
  { id: "ticket", name: "門票", icon: "bi bi-ticket" },
  { id: "traffic", name: "交通", icon: "bi bi-train-front" },
];

// 模擬搜尋結果資料
const allResults = ref([
  {
    id: 1,
    name: "台北君悅酒店",
    description: "五星級豪華飯店，位於信義區精華地段",
    type: "hotel",
    price: 4500,
    originalPrice: 5200,
    rating: 4.5,
    location: "台北市信義區",
    image: "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=400",
  },
  {
    id: 2,
    name: "台北松山機場 - 高雄小港機場",
    description: "國內航線，飛行時間約1小時",
    type: "flight",
    price: 2800,
    rating: 4.2,
    location: "台北松山機場",
    image: "https://images.unsplash.com/photo-1436491865332-7a61a109cc05?w=400",
  },
  {
    id: 3,
    name: "台北101觀景台門票",
    description: "台北地標建築，俯瞰全城美景",
    type: "ticket",
    price: 600,
    rating: 4.6,
    location: "台北市信義區",
    image: "https://images.unsplash.com/photo-1555992336-03a23c7b20ee?w=400",
  },
  {
    id: 4,
    name: "台灣高鐵票 - 台北到高雄",
    description: "快速舒適的高鐵服務",
    type: "traffic",
    price: 1490,
    rating: 4.4,
    location: "台北車站",
    image: "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?w=400",
  },
]);

// 過濾後的結果
const filteredResults = computed(() => {
  let results = [...allResults.value];

  // 搜尋關鍵字過濾
  if (searchQuery.value) {
    results = results.filter(
      (item) =>
        item.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
        item.description
          .toLowerCase()
          .includes(searchQuery.value.toLowerCase()) ||
        item.location.toLowerCase().includes(searchQuery.value.toLowerCase())
    );
  }

  // 價格範圍過濾
  if (priceRange.value.min) {
    results = results.filter(
      (item) => item.price >= Number(priceRange.value.min)
    );
  }
  if (priceRange.value.max) {
    results = results.filter(
      (item) => item.price <= Number(priceRange.value.max)
    );
  }

  // 評分過濾
  if (selectedRating.value > 0) {
    results = results.filter((item) => item.rating >= selectedRating.value);
  }

  // 類型過濾
  if (selectedTypes.value.length > 0) {
    results = results.filter((item) => selectedTypes.value.includes(item.type));
  }

  // 排序
  results.sort((a, b) => {
    switch (sortBy.value) {
      case "price-low":
        return a.price - b.price;
      case "price-high":
        return b.price - a.price;
      case "rating":
        return b.rating - a.rating;
      case "popularity":
        return b.id - a.id; // 模擬熱門程度
      default:
        return 0;
    }
  });

  return results;
});

// 分頁結果
const paginatedResults = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return filteredResults.value.slice(start, end);
});

// 總頁數
const totalPages = computed(() => {
  return Math.ceil(filteredResults.value.length / itemsPerPage);
});

// 可見頁碼
const visiblePages = computed(() => {
  const pages = [];
  const maxVisible = 5;
  let start = Math.max(1, currentPage.value - Math.floor(maxVisible / 2));
  let end = Math.min(totalPages.value, start + maxVisible - 1);

  if (end - start + 1 < maxVisible) {
    start = Math.max(1, end - maxVisible + 1);
  }

  for (let i = start; i <= end; i++) {
    pages.push(i);
  }

  return pages;
});

// 獲取類型名稱
const getTypeName = (type) => {
  const typeObj = productTypes.find((t) => t.id === type);
  return typeObj ? typeObj.name : type;
};

// 獲取類型樣式類別
const getTypeClass = (type) => {
  return `badge-${type}`;
};

// 獲取類型數量
const getTypeCount = (type) => {
  return allResults.value.filter((item) => item.type === type).length;
};

// 處理排序
const handleSort = () => {
  currentPage.value = 1;
};

// 應用篩選
const applyFilters = () => {
  currentPage.value = 1;
};

// 清除篩選
const clearFilters = () => {
  selectedRating.value = 0;
  selectedTypes.value = [];
  priceRange.value = { min: "", max: "" };
  sortBy.value = "relevance";
  currentPage.value = 1;
};

// 切換頁面
const changePage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page;
  }
};

// 查看詳情
const viewDetails = (item) => {
  const routes = {
    hotel: `/hotel/${item.id}`,
    flight: `/flight/${item.id}`,
    ticket: `/ticket/${item.id}`,
    traffic: `/traffic/${item.id}`,
  };

  const route = routes[item.type];
  if (route) {
    router.push(route);
  }
};

// 加入購物車
const addToCart = (item) => {
  console.log("加入購物車:", item);
  // TODO: 實作購物車功能
};

// 監聽路由參數變化
watch(
  () => route.query.q,
  (newQuery) => {
    if (newQuery) {
      searchQuery.value = newQuery;
    }
  },
  { immediate: true }
);

onMounted(() => {
  // 從路由參數獲取搜尋關鍵字
  if (route.query.q) {
    searchQuery.value = route.query.q;
  }
});
</script>

<style scoped>
.search-results-container {
  min-height: 100vh;
  background: #f8f9fa;
}

.results-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
  padding: 20px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.search-info h2 {
  margin: 0 0 8px 0;
  color: #333;
}

.sort-options {
  display: flex;
  align-items: center;
  gap: 12px;
}

.sort-options .form-select {
  width: 200px;
}

.filters-sidebar {
  background: #fff;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 100px;
}

.filter-section {
  margin-bottom: 24px;
}

.filter-section h6 {
  font-weight: 600;
  margin-bottom: 12px;
  color: #495057;
}

.price-range input {
  font-size: 0.9rem;
}

.rating-filter {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.rating-option {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 0;
}

.rating-option input[type="radio"] {
  margin: 0;
}

.stars {
  display: flex;
  gap: 2px;
}

.rating-text {
  font-size: 0.9rem;
  color: #495057;
}

.type-filter {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.type-option {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 0;
}

.type-option input[type="checkbox"] {
  margin: 0;
}

.type-option i {
  color: #0080ff;
  width: 16px;
}

.count {
  font-size: 0.8rem;
  color: #6c757d;
  margin-left: auto;
}

.results-grid {
  display: grid;
  gap: 20px;
}

.result-card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease;
}

.result-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.result-image {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.result-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.result-badge {
  position: absolute;
  top: 12px;
  right: 12px;
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 500;
  color: #fff;
}

.badge-hotel {
  background: #0080ff;
}

.badge-flight {
  background: #28a745;
}

.badge-ticket {
  background: #ffc107;
  color: #000;
}

.badge-traffic {
  background: #dc3545;
}

.result-content {
  padding: 20px;
}

.result-title {
  font-size: 1.2rem;
  font-weight: 600;
  margin: 0 0 8px 0;
  color: #333;
}

.result-description {
  color: #6c757d;
  font-size: 0.9rem;
  margin: 0 0 12px 0;
  line-height: 1.4;
}

.result-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.rating {
  display: flex;
  align-items: center;
  gap: 4px;
}

.rating-text {
  font-weight: 500;
  color: #495057;
}

.location {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #6c757d;
  font-size: 0.9rem;
}

.result-price {
  display: flex;
  align-items: baseline;
  gap: 8px;
  margin-bottom: 16px;
}

.price {
  font-size: 1.3rem;
  font-weight: 700;
  color: #0080ff;
}

.original-price {
  font-size: 1rem;
  color: #6c757d;
  text-decoration: line-through;
}

.result-actions {
  display: flex;
  gap: 12px;
}

.result-actions .btn {
  flex: 1;
  padding: 8px 16px;
  font-size: 0.9rem;
}

.pagination-wrapper {
  margin-top: 32px;
}

.no-results {
  text-align: center;
  padding: 60px 20px;
}

.no-results-content i {
  font-size: 4rem;
  margin-bottom: 16px;
  display: block;
}

.no-results-content h4 {
  margin: 0 0 8px 0;
  color: #495057;
}

.no-results-content p {
  color: #6c757d;
  margin: 0 0 20px 0;
}

@media (max-width: 768px) {
  .results-header {
    flex-direction: column;
    gap: 16px;
  }

  .sort-options {
    width: 100%;
  }

  .sort-options .form-select {
    width: 100%;
  }

  .filters-sidebar {
    position: static;
    margin-bottom: 20px;
  }

  .result-actions {
    flex-direction: column;
  }
}
</style>
