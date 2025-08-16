<template>
  <div class="hotels-filter-page">
    <!-- Header -->
    <Header />
    <main class="main-content ">
      <!-- 上方搜尋列 -->
      <div class="container" style="padding-top:80px;">
        <HotelsSearchBar class="search-panel" @search="handleSearch" />
      </div>

      <div class="container mt-4">
        <div class="row">
          <!-- 左側 FilterBar -->
          <div class="col-md-4">
            <!-- <div class="d-flex justify-content-between align-items-center mb-3">
              <h3 class="fw-bold mb-0">自訂篩選</h3>

            </div> -->
            <HotelsFilterBar2 :bedTypes="filterOptions.bedTypes" :facilities="filterOptions.facilities"
              :lodgingsTypes="filterOptions.lodgingsTypes" :selectedScore="selectedScore" :priceRange="priceRange"
              :selectedLodgingTypes="selectedLodgingTypes" :selectedBedTypes="selectedBedTypes"
              :selectedFacilities="selectedFacilities" @filter-change="applyFilter" />
          </div>

          <!-- 右側搜尋結果 -->
          <div class="col-md-8">
            <!-- Loading state -->
            <div v-if="loading" class="text-center py-5">
              <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">載入中...</span>
              </div>
              <p class="mt-3">正在搜尋旅宿...</p>
            </div>

            <!-- Error state -->
            <div v-else-if="error" class="text-center py-5">
              <div class="alert alert-danger" role="alert">
                <h5>搜尋失敗</h5>
                <p>{{ error.message }}</p>
                <button class="btn btn-primary" @click="refresh">重新搜尋</button>
              </div>
            </div>

            <!-- Success state -->
            <div v-else>
              <div v-if="filteredHotels.length === 0" class="text-center py-5">
                <h5>沒有找到符合條件的旅宿</h5>
                <p class="text-muted">請嘗試調整搜尋條件</p>
              </div>
              <div v-else class="row row-cols-2 g-4">
                <div class="col" v-for="hotel in filteredHotels" :key="hotel.id + '-' + (hotel.roomTypeName || '')">
                  <HotelCard :hotel="hotel" />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
  import { ref, computed, onMounted } from 'vue';
  import { useRoute } from 'vue-router';
  import { useHotLodgings } from '@/composables/useHotLodgings';
  import Header from "@/components/Header.vue";
  import HotelsSearchBar from "@/components/lodging/HotelsSearchBar.vue";
  import HotelsFilterBar2 from '@/components/lodging/HotelsFilterBar2.vue'
  import HotelCard from '@/components/lodging/HotelCard.vue';


  const route = useRoute();

  // 使用 composable
  const { hotels, loading, error, refresh, searchLodgings, filterOptions } = useHotLodgings();

  // 本地端篩選條件
  const selectedScore = ref([])
  const priceRange = ref([0, 10000])
  const selectedLodgingTypes = ref([])
  const selectedBedTypes = ref([])
  const selectedFacilities = ref([])

  // 處理搜尋
  const handleSearch = async (searchParams) => {
    console.log('搜尋參數：', searchParams);
    await searchLodgings(searchParams);
    // 搜尋後自動重置篩選條件
    selectedScore.value = []
    priceRange.value = [0, 10000]
    selectedLodgingTypes.value = []
    selectedBedTypes.value = []
    selectedFacilities.value = []
  }

  const isPriceRangeDefault = computed(() => priceRange.value[0] === 0 && priceRange.value[1] === 10000)
  const isPriceRangeMax = computed(() => priceRange.value[1] === 10000)

  const filteredHotels = computed(() => {
    const allIds = hotels.value.map(h => h.id)
    const filteredIds = []

    const result = hotels.value.filter(hotel => {
      // 分數篩選
      if (selectedScore.value.length > 0) {
        const avg = hotel.rating
        let match = false
        for (const score of selectedScore.value) {
          if (score === '5分' && avg === 5) match = true
          if (score === '四分' && avg >= 4 && avg < 5) match = true
          if (score === '三分' && avg >= 3 && avg < 4) match = true
          if (score === '三以下' && avg < 3) match = true
        }
        if (!match) return false
      }
      // 床型
      if (selectedBedTypes.value.length > 0) {
        const bedTypeIds = (hotel.bedTypes || []).map(b => b.id)
        if (!selectedBedTypes.value.some(id => bedTypeIds.includes(id))) return false
      }
      // 住宿類型
      if (selectedLodgingTypes.value.length > 0) {
        if (!selectedLodgingTypes.value.includes(Number(hotel.lodgingTypeId))) return false
      }
      // 設施
      if (selectedFacilities.value.length > 0) {
        const facilityIds = (hotel.facilities || []).map(f => f.id)
        if (!selectedFacilities.value.some(id => facilityIds.includes(id))) return false
      }
      // 價格
      if (!isPriceRangeDefault.value && priceRange.value.length === 2 && hotel.pricePerNight !== undefined) {
        if (isPriceRangeMax.value) {
          if (hotel.pricePerNight < priceRange.value[0]) return false
        } else {
          if (hotel.pricePerNight < priceRange.value[0] || hotel.pricePerNight > priceRange.value[1]) return false
        }
      }
      filteredIds.push(hotel.id)
      return true
    })

    // debug: 顯示被過濾掉的 id
    const missing = allIds.filter(id => !filteredIds.includes(id))
    if (missing.length > 0) {
      console.log('被過濾掉的 id:', missing)
      hotels.value.forEach(h => {
        if (missing.includes(h.id)) console.log('被過濾掉的 hotel:', h)
      })
    }

    // log idList 與唯一 id 數量
    const idList = result.map(h => h.id)
    console.log('filteredHotels idList:', idList)
    const uniqueIdList = [...new Set(idList)]
    console.log('唯一 id 數量:', uniqueIdList.length, '總數:', idList.length)

    return result
  })

  function applyFilter(filterData) {
    selectedScore.value = filterData.selectedScore
    priceRange.value = filterData.priceRange
    selectedLodgingTypes.value = (filterData.selectedLodgingsTypes || []).map(id => Number(id))
    selectedBedTypes.value = (filterData.selectedBedTypes || []).map(id => Number(id))
    selectedFacilities.value = (filterData.selectedFacilities || []).map(id => Number(id))
  }

  function clearAllFilters() {
    selectedScore.value = []
    priceRange.value = [0, 10000]
    selectedLodgingTypes.value = []
    selectedBedTypes.value = []
    selectedFacilities.value = []
  }

  // 頁面載入時檢查是否有搜尋參數
  onMounted(() => {
    if (route.query.destination || route.query.checkIn) {
      const searchParams = {
        destination: route.query.destination,
        checkIn: route.query.checkIn,
        checkOut: route.query.checkOut,
        guests: route.query.guests
      };
      handleSearch(searchParams);
    }
  });
</script>

<style scoped>
  .hotels-filter-page {
    min-height: 100vh;
    background-color: #f8f9fa;
  }

  .fcc-btn {
    color: var(--bs-success);
  }

  .fcc-btn:hover,
  .fcc-btn:active {
    color: var(--bs-danger);
  }
</style>
