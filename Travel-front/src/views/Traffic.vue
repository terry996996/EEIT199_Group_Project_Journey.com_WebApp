<template>
  <div class="traffic-page">
    <!-- Header -->
    <Header />

    <div class="container my-5 pt-4">
      <TrafficSearchForm v-model="searchForm" @search="handleSearch" />

      <div class="row">
        <div class="col-md-3">
          <TrafficFilter :priceMax="priceMax" @update:priceMax="priceMax = $event" />
        </div>

        <!-- 交通列表 -->
        <div class="col-md-9">
          <div class="row row-cols-1 row-cols-md-2 g-4">
            <div v-for="item in paginatedTraffic" :key="item.id" class="col">
              <TrafficCard
                :traffic="item"
                @book="handleBook"
                @addToJourney="handleAddToJourney"
                @toggleFavorite="handleToggleFavorite"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="d-flex justify-content-center my-4">
      <nav>
        <ul class="pagination">
          <li class="page-item" :class="{ disabled: currentPage === 1 }">
            <a class="page-link" href="#" @click.prevent="currentPage--">上一頁</a>
          </li>
          <li
            class="page-item"
            v-for="page in totalPages"
            :key="page"
            :class="{ active: page === currentPage }"
          >
            <a class="page-link" href="#" @click.prevent="currentPage = page">{{ page }}</a>
          </li>
          <li class="page-item" :class="{ disabled: currentPage === totalPages }">
            <a class="page-link" href="#" @click.prevent="currentPage++">下一頁</a>
          </li>
        </ul>
      </nav>
    </div>
  </div>
</template>

<script setup>
  import { ref, onMounted, computed } from 'vue';
  import { useRouter } from 'vue-router';
  import axios from 'axios';
  import Header from '../components/Header.vue';
  import TrafficCard from '../components/traffic/TrafficCard.vue';
  import TrafficSearchForm from '../components/traffic/TrafficSearchForm.vue';
  import Footer from '../components/Footer.vue';
  import TrafficFilter from '../components/traffic/TrafficFilter.vue';

  const router = useRouter();

  // 搜尋表單
  const searchForm = ref({ name: '' });

  // 模擬交通數據
  const traffic = ref([]);

  const filterForm = ref({
    selectedTypes: [],
    selectedRegions: [],
    minPrice: 0,
    maxPrice: 10000
  });

  const priceMax = ref(30000);

  //分頁功能
  const currentPage = ref(1);
  const itemsPerPage = 6;

  const paginatedTraffic = computed(() => {
    const start = (currentPage.value - 1) * itemsPerPage;
    return filteredTraffic.value.slice(start, start + itemsPerPage);
  });
  const totalPages = computed(() => {
    return Math.ceil(filteredTraffic.value.length / itemsPerPage);
  });

  const allData = ref([]); // 所有資料 or 搜尋結果

  //mounted 時向後端請求資料
  onMounted(async () => {
    try {
      const res = await axios.get('http://localhost:8080/traffictickets/all');
      traffic.value = res.data;
    } catch (error) {
      console.error('取得交通票券資料失敗：', error);
    }
  });

  // 處理搜尋
  const handleSearch = async (form) => {
    try {
      const res = await axios.get('http://localhost:8080/traffictickets/search', {
        params: {
          name: form.name // ✅ 傳關鍵字
        }
      });
      traffic.value = res.data;
    } catch (e) {
      console.error('搜尋失敗', e);
    }
  };

  // 處理篩選
  const filteredTraffic = computed(() => {
    return traffic.value.filter((item) => item.price >= 1000 && item.price <= priceMax.value);
  });

  // 處理預訂
  const handleBook = (traffic) => {
    router.push(`/traffic-detail/${traffic.id}`);
  };

  // 處理加入旅程
  const handleAddToJourney = (traffic) => {
    // 顯示成功提示
    alert('已加入旅程！');
  };

  // 處理收藏
  const handleToggleFavorite = ({ traffic, isFavorited }) => {
    // 顯示成功提示
    alert(isFavorited ? '已加入收藏！' : '已取消收藏！');
  };
</script>

<style scoped>
  .traffic-page {
    min-height: 100vh;
    background-color: #f8f9fa;
  }

  .search-section {
    border: none;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  }

  .card {
    border: none;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  }

  .btn-check:checked + .btn-outline-primary {
    background-color: #0d6efd;
    border-color: #0d6efd;
    color: white;
  }

  .pagination {
    margin-bottom: 0;
    /* 清掉預設間距 */
  }
</style>
