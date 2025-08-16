<template>
  <div class="tickets-page">
    <!-- ✅ 頁首元件 -->
    <Header />

    <div class="container my-5 pt-4">
      <!-- ✅ 搜尋元件（已抽成獨立 component） -->
      <SearchBarAttraction :cityName="selectedCity?.name || '日本'" :title-override="titleOverride"
        @search="updateTickets" @locationSelected="handleLocationSelected" />

      <!-- ✅ 主內容區塊 -->
      <div class="row">
        <!-- 🔍 左欄：篩選條件 -->
        <div class="col-md-3">
          <div class="card no-hover no-shadow">
            <div class="card-body">
              <h6 class="card-title">篩選條件</h6>



              <!-- ✅ 類別：單選（radio） -->
              <div class="mb-3">
                <label class="form-label">景點類型</label>
                <div class="form-check" v-for="type in types" :key="type.value">
                  <input class="form-check-input" type="radio" name="typeRadio" :value="type.value"
                    v-model="filters.typeId" />
                  <label class="form-check-label">{{ type.label }}</label>
                </div>
              </div>

              <!-- ✅ 特色：多選（checkbox） -->
              <div class="mb-3">
                <label class="form-label">特色</label>
                <div class="form-check" v-for="feature in features" :key="feature.value">
                  <input class="form-check-input no-hover no-shadow" type="checkbox" :id="`feature-${feature.value}`"
                    :value="feature.value" v-model="filters.tagIds" />
                  <label class="form-check-label" :for="`feature-${feature.value}`">{{ feature.label }}</label>
                </div>
              </div>

            </div>
          </div>
        </div>

        <!-- 🎟️ 右欄：門票清單與排序 -->
        <div class="col-md-9">
          <!-- 結果數與排序選單 -->
          <div class="d-flex justify-content-between align-items-center mb-4">
            <div>找到 {{ tickets.length }} 個結果</div>
            <select class="form-select no-hover no-shadow" style="width: auto" v-model="sortBy">
              <option value="price_asc">價格由低到高</option>
              <option value="price_desc">價格由高到低</option>
              <option value="rating_desc">評分由高到低</option>
              <option value="popularity">熱門程度</option>
            </select>
          </div>

          <!-- 門票卡片列表 -->
          <div class="row row-cols-1 row-cols-md-2 g-4">
            <div v-for="ticket in tickets" :key="ticket.id" class="col">
              <TicketCard :ticket="ticket" @book="handleBook" @addToJourney="handleAddToJourney"
                @toggleFavorite="handleToggleFavorite" />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { ref, computed, onMounted, watch } from 'vue';
  import { useRouter, useRoute } from 'vue-router';
  import Header from '../components/Header.vue';
  import TicketCard from '../components/attractions/TicketCard.vue';
  import SearchBarAttraction from '../components/attractions/SearchBarAttraction.vue';
  import Footer from '../components/Footer.vue';
  import { searchSimpleTickets } from '@/services/attractions/ticketService';
  import { getAttractionTagsOptions } from '@/services/attractions/ticketService';
  import { getAttractionTypesOptions } from '@/services/attractions/ticketService';
  import { searchAttractionTickets } from '@/services/attractions/ticketService';
  import { getCountryOptions } from '@/services/attractions/ticketService';
  // ========== 1️⃣ 路由與搜尋關鍵字 ==========
  const route = useRoute();

  const router = useRouter();
  const selectedCity = ref(null);
  //==============呼叫tag選單 類型選單================
  const features = ref([]);
  const types = ref([]);

  const fetchTagOptions = async () => {
    try {
      const res = await getAttractionTagsOptions();
      features.value = res.data.map((tag) => ({
        label: tag.label,
        value: tag.id.toString()
      }));
    } catch (err) {
      console.error('❌ 無法取得 attraction tags', err);
    }
  };

  const fetchTypeOptions = async () => {
    try {
      const res = await getAttractionTypesOptions();
      const mapped = res.data.map((type) => ({
        label: type.label,
        value: type.id.toString()
      }));

      // 在最前面加上「全部」選項
      types.value = [
        { label: '全部', value: '' },
        ...mapped
      ];
    } catch (err) {
      console.error('❌ 無法取得 attraction types', err);
    }
  };


  // ========== 2️⃣ 篩選條件 ==========
  const filters = ref({
    keyword: '',
    countryId: null,
    cityId: null,
    typeIds: [],   // ✅ 類型（單選）
    tagIds: []     // ✅ 特色（多選）
  });
  const selectedTypeId = ref(null)
  //=========================勾選搜尋========================
  watch(() => filters.value.typeId, () => {
    filters.value.typeIds = filters.value.typeId ? [Number(filters.value.typeId)] : [];
    titleOverride.value = '在全球的熱門景點門票'; // ✅ reset 標題
    updateTickets(filters.value);
  });

  watch(() => filters.value.tagIds, () => {
    titleOverride.value = '在全球的熱門景點門票'; // ✅ reset 標題
    updateTickets(filters.value);
  });
  //============================
  const titleOverride = ref(null); // 若不為 null，就會取代 SearchBar 中的動態標題
  //=============================
  // ========== 3️⃣ 門票資料 ==========
  const tickets = ref([]);

  const handleLocationSelected = (city) => {
    selectedCity.value = city;
  };

  const fetchTickets = async () => {
    try {
      const res = await searchSimpleTickets({}); // ✅ 改成簡易查詢
      tickets.value = res.data.map((ticket) => ({
        id: ticket.id,
        name: ticket.name,
        location: ticket.address,
        image: ticket.imageUrl,
        price: ticket.minPrice || 0, // ✅ 使用最低價
        rating: (ticket.views / 10).toFixed(1),
        reviewCount: 0,
        features: [],
        highlight: ticket.description
      }));
    } catch (err) {
      console.error('❌ 查詢票券失敗', err);
    }
  };
  //============搜尋後傳啥資料 沒有預設全部====================
  // ========== 3️⃣ 查詢票券 ==========
  const updateTickets = async (dto) => {
    try {
      console.log('📦 查詢 DTO:', dto);
      const res = await searchAttractionTickets(dto);
      tickets.value = res.data.map((ticket) => ({
        id: ticket.id,
        name: ticket.name,
        location: ticket.address,
        image: ticket.imageUrl,
        price: ticket.minPrice || 0,
        rating: (ticket.views / 10).toFixed(1),
        reviewCount: 0,
        features: [],
        highlight: ticket.description
      }));
    } catch (err) {
      console.error('❌ 搜尋票券失敗', err);
    }
  };

  // ========== 4️⃣ 依據 query 組成 DTO 查詢 ==========
  const sortBy = ref('popularity');

  const searchFromQuery = async () => {
    const { keyword = '', countryId, cityId, tagIds, typeIds } = route.query;

    const hasQuery = keyword || countryId || cityId || tagIds || typeIds;

    if (hasQuery) {
      const parseId = (val) =>
        val && val !== 'null' && val !== 'undefined' && val !== '' ? Number(val) : null;

      const parseArrayIds = (val) =>
        val && typeof val === 'string' ? val.split(',').map(Number).filter(Boolean) : [];

      const dto = {
        keyword: keyword?.trim() || '',
        countryId: parseId(countryId),
        cityId: cityId && cityId !== 'null' ? cityId : null,
        tagIds: parseArrayIds(tagIds),
        typeIds: parseArrayIds(typeIds)
      };

      console.log('📦 有條件查詢:', dto);
      await updateTickets(dto);
    } else {
      await fetchTickets(); // 預設全部資料
    }
  };

  // ========== 5️⃣ 初始載入 + 排序監聽 ==========
  onMounted(async () => {
    await fetchTagOptions();
    await fetchTypeOptions();
    await getCountryOptions();
    await searchFromQuery();
  });

  watch(sortBy, async () => {
    await searchFromQuery();
  });
  // ========== 4️⃣ 互動功能 ==========
  const handleBook = (ticket) => {
    router.push({ path: `/ticket/${ticket.id}` });
  };

  const handleAddToJourney = (ticket) => {
    alert('已加入旅程！');
  };

  const handleToggleFavorite = ({ ticket, isFavorited }) => {
    alert(isFavorited ? '已加入收藏！' : '已取消收藏！');
  };
</script>
<style scoped>

  /* =============== 頁面樣式 =============== */
  .tickets-page {
    min-height: 100vh;
    background-color: #f8f9fa;
    /* 背景為淺灰色 */
  }

  /* =============== 卡片與表單元件通用樣式 =============== */
  .card,
  .form-select,
  .form-check-input {
    transition: none !important;
    box-shadow: none !important;
  }

  .card.no-shadow,
  .card.no-hover,
  .no-hover,
  .no-shadow {
    box-shadow: none !important;
    transition: none !important;
    transform: none !important;
  }

  /* =============== 搜尋區塊 =============== */
  .search-section {
    padding-top: 2rem;
    padding-bottom: 2rem;
  }

  /* 搜尋欄輸入框容器 */
  .search-wrapper {
    position: relative;
    width: 100%;
    margin-top: 60px;
    padding-left: 15px;
    padding-right: 15px;
  }

  /* 關鍵字輸入框 */
  .search-keyword-input {
    width: 70%;
    margin: 0 auto;
    font-size: 1rem;
    padding: 0.6rem 1rem;
    border-radius: 6px;
    border: 1px solid #ccc;
    box-shadow: none !important;
    transition: none !important;
  }

  /* 動態標題 */
  .search-page-title {
    font-size: 1.5rem;
    font-weight: bold;
    margin-bottom: 1rem;
  }

  /* =============== 下拉建議選單樣式 =============== */
  .search-dropdown.shadow-box {
    position: absolute;
    top: 100%;
    left: 15px;
    width: 70%;
    background-color: #fff;
    border-radius: 12px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    padding: 1.25rem;
    z-index: 1000;
  }

  /* 建議區塊 */
  .suggest-group {
    margin-bottom: 1.25rem;
  }

  .suggest-header {
    font-weight: bold;
    margin-bottom: 0.5rem;
    color: #222;
    font-size: 1rem;
  }

  .suggest-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 0.5rem;
  }

  .tag-pill {
    padding: 0.4rem 0.9rem;
    background-color: #f2f2f2;
    border-radius: 50px;
    font-size: 0.95rem;
    cursor: pointer;
    transition: background-color 0.2s;
  }

  .tag-pill:hover {
    background-color: #e0e0e0;
  }

  /* =============== 門票卡片樣式 =============== */
  .ticket-card,
  .ticket-card:hover {
    transition: none;
    transform: none;
    box-shadow: none;
  }

  .features .badge {
    font-weight: normal;
  }

  /* =============== 響應式圖片調整 =============== */
  @media (max-width: 768px) {
    .col-md-4 img {
      height: 200px !important;
      width: 100%;
    }
  }
</style>
