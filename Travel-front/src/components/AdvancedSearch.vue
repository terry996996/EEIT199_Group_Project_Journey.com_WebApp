<template>
  <div class="advanced-search-container">
    <!-- 主要搜尋欄位 -->
    <div class="search-main">
      <div class="search-input-wrapper">
        <i class="bi bi-search search-icon"></i>
        <input
          v-model="searchQuery"
          type="text"
          class="form-control search-input"
          placeholder="搜尋目的地、飯店、景點..."
          @input="handleSearchInput"
          @focus="showSuggestions = true"
          @blur="handleBlur"
        />
        <button class="btn btn-primary search-btn" @click="handleSearch">搜尋</button>
      </div>

      <!-- 搜尋建議 -->
      <div v-if="showSuggestions && suggestions.length > 0" class="suggestions-dropdown">
        <div
          v-for="suggestion in suggestions"
          :key="suggestion.id"
          class="suggestion-item"
          @click="selectSuggestion(suggestion)"
        >
          <i :class="suggestion.icon"></i>
          <span>{{ suggestion.text }}</span>
          <small class="text-muted">{{ suggestion.type }}</small>
        </div>
      </div>
    </div>

    <!-- 進階篩選器 -->
    <div class="advanced-filters">
      <div class="filter-row">
        <!-- 日期選擇 -->
        <div class="filter-group">
          <label class="filter-label">出發日期</label>
          <input
            v-model="filters.startDate"
            type="date"
            class="form-control filter-input"
            @change="handleFilterChange"
          />
        </div>

        <div class="filter-group">
          <label class="filter-label">回程日期</label>
          <input
            v-model="filters.endDate"
            type="date"
            class="form-control filter-input"
            @change="handleFilterChange"
          />
        </div>

        <!-- 人數選擇 -->
        <div class="filter-group">
          <label class="filter-label">人數</label>
          <select
            v-model="filters.guests"
            class="form-select filter-input"
            @change="handleFilterChange"
          >
            <option value="">不限</option>
            <option value="1">1人</option>
            <option value="2">2人</option>
            <option value="3">3人</option>
            <option value="4">4人</option>
            <option value="5+">5人以上</option>
          </select>
        </div>

        <!-- 價格範圍 -->
        <div class="filter-group">
          <label class="filter-label">價格範圍</label>
          <div class="price-range">
            <input
              v-model="filters.minPrice"
              type="number"
              class="form-control price-input"
              placeholder="最低"
              @input="handleFilterChange"
            />
            <span class="price-separator">-</span>
            <input
              v-model="filters.maxPrice"
              type="number"
              class="form-control price-input"
              placeholder="最高"
              @input="handleFilterChange"
            />
          </div>
        </div>
      </div>

      <!-- 評分篩選 -->
      <div class="filter-row">
        <div class="filter-group">
          <label class="filter-label">最低評分</label>
          <div class="rating-filter">
            <div
              v-for="rating in 5"
              :key="rating"
              class="rating-star"
              :class="{ active: filters.minRating >= rating }"
              @click="setMinRating(rating)"
            >
              <i class="bi bi-star-fill"></i>
            </div>
            <span class="rating-text">{{ filters.minRating }}+</span>
          </div>
        </div>

        <!-- 設施篩選 -->
        <div class="filter-group">
          <label class="filter-label">設施</label>
          <div class="facilities-filter">
            <label v-for="facility in facilities" :key="facility.id" class="facility-checkbox">
              <input
                v-model="filters.facilities"
                type="checkbox"
                :value="facility.id"
                @change="handleFilterChange"
              />
              <i :class="facility.icon"></i>
              {{ facility.name }}
            </label>
          </div>
        </div>
      </div>

      <!-- 搜尋歷史 -->
      <div v-if="searchHistory.length > 0" class="search-history">
        <h6>最近搜尋</h6>
        <div class="history-items">
          <span
            v-for="item in searchHistory.slice(0, 5)"
            :key="item.id"
            class="history-item"
            @click="useHistoryItem(item)"
          >
            {{ item.query }}
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { ref, reactive, onMounted } from 'vue'
  import { useRouter } from 'vue-router'

  const router = useRouter()
  const emit = defineEmits(['search', 'filter'])

  // 響應式資料
  const searchQuery = ref('')
  const showSuggestions = ref(false)
  const suggestions = ref([])
  const searchHistory = ref([])

  // 篩選條件
  const filters = reactive({
    startDate: '',
    endDate: '',
    guests: '',
    minPrice: '',
    maxPrice: '',
    minRating: 0,
    facilities: []
  })

  // 設施選項
  const facilities = [
    { id: 'wifi', name: 'WiFi', icon: 'bi bi-wifi' },
    { id: 'parking', name: '停車場', icon: 'bi bi-p-square' },
    { id: 'pool', name: '游泳池', icon: 'bi bi-water' },
    { id: 'gym', name: '健身房', icon: 'bi bi-heart-pulse' },
    { id: 'restaurant', name: '餐廳', icon: 'bi bi-cup-hot' },
    { id: 'spa', name: 'SPA', icon: 'bi bi-flower1' }
  ]

  // 搜尋建議資料
  const suggestionData = [
    { id: 1, text: '台北101', type: '景點', icon: 'bi bi-building' },
    { id: 2, text: '台北君悅酒店', type: '飯店', icon: 'bi bi-house' },
    { id: 3, text: '台北松山機場', type: '機場', icon: 'bi bi-airplane' },
    { id: 4, text: '九份老街', type: '景點', icon: 'bi bi-building' },
    { id: 5, text: '台北車站', type: '交通', icon: 'bi bi-train-front' },
    { id: 6, text: '陽明山國家公園', type: '景點', icon: 'bi bi-tree' }
  ]

  // 處理搜尋輸入
  const handleSearchInput = () => {
    if (searchQuery.value.trim()) {
      // 模擬即時搜尋建議
      suggestions.value = suggestionData.filter((item) =>
        item.text.toLowerCase().includes(searchQuery.value.toLowerCase())
      )
    } else {
      suggestions.value = []
    }
  }

  // 選擇建議
  const selectSuggestion = (suggestion) => {
    searchQuery.value = suggestion.text
    showSuggestions.value = false
    handleSearch()
  }

  // 處理搜尋
  const handleSearch = () => {
    if (searchQuery.value.trim()) {
      const searchData = {
        query: searchQuery.value.trim(),
        filters: { ...filters },
        timestamp: new Date().toISOString()
      }

      // 儲存搜尋歷史
      addToHistory(searchQuery.value.trim())

      // 發送事件給父元件
      emit('search', searchData)

      // 導向搜尋結果頁面
      router.push({
        path: '/search',
        query: { q: searchQuery.value.trim() }
      })

      showSuggestions.value = false
    }
  }

  // 處理篩選變更
  const handleFilterChange = () => {
    emit('filter', { ...filters })
  }

  // 設定最低評分
  const setMinRating = (rating) => {
    filters.minRating = filters.minRating === rating ? 0 : rating
    handleFilterChange()
  }

  // 處理失焦
  const handleBlur = () => {
    setTimeout(() => {
      showSuggestions.value = false
    }, 200)
  }

  // 新增搜尋歷史
  const addToHistory = (query) => {
    const history = JSON.parse(localStorage.getItem('searchHistory') || '[]')
    const newHistory = [
      { id: Date.now(), query, timestamp: new Date().toISOString() },
      ...history.filter((item) => item.query !== query)
    ].slice(0, 10)

    localStorage.setItem('searchHistory', JSON.stringify(newHistory))
    searchHistory.value = newHistory
  }

  // 使用歷史項目
  const useHistoryItem = (item) => {
    searchQuery.value = item.query
    handleSearch()
  }

  // 載入搜尋歷史
  onMounted(() => {
    searchHistory.value = JSON.parse(localStorage.getItem('searchHistory') || '[]')
  })
</script>

<style scoped>
  .advanced-search-container {
    background: #fff;
    border-radius: 12px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    padding: 24px;
    margin-bottom: 24px;
  }

  .search-main {
    position: relative;
    margin-bottom: 20px;
  }

  .search-input-wrapper {
    display: flex;
    align-items: center;
    background: #f8f9fa;
    border-radius: 8px;
    padding: 8px 16px;
    border: 2px solid transparent;
    transition: all 0.3s ease;
  }

  .search-input-wrapper:focus-within {
    border-color: #0080ff;
    background: #fff;
  }

  .search-icon {
    color: #6c757d;
    margin-right: 12px;
    font-size: 1.2rem;
  }

  .search-input {
    border: none;
    background: transparent;
    flex: 1;
    font-size: 1rem;
    padding: 8px 0;
  }

  .search-input:focus {
    box-shadow: none;
    outline: none;
  }

  .search-btn {
    background: #0080ff;
    border: none;
    border-radius: 6px;
    padding: 8px 20px;
    font-weight: 500;
    transition: all 0.3s ease;
  }

  .search-btn:hover {
    background: #005bbd;
    transform: translateY(-1px);
  }

  .suggestions-dropdown {
    position: absolute;
    top: 100%;
    left: 0;
    right: 0;
    background: #fff;
    border: 1px solid #dee2e6;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    z-index: 1000;
    max-height: 300px;
    overflow-y: auto;
  }

  .suggestion-item {
    display: flex;
    align-items: center;
    padding: 12px 16px;
    cursor: pointer;
    transition: background 0.2s ease;
  }

  .suggestion-item:hover {
    background: #f8f9fa;
  }

  .suggestion-item i {
    margin-right: 12px;
    color: #0080ff;
    width: 20px;
  }

  .suggestion-item span {
    flex: 1;
    font-weight: 500;
  }

  .suggestion-item small {
    color: #6c757d;
  }

  .advanced-filters {
    border-top: 1px solid #dee2e6;
    padding-top: 20px;
  }

  .filter-row {
    display: flex;
    gap: 16px;
    margin-bottom: 16px;
    flex-wrap: wrap;
  }

  .filter-group {
    flex: 1;
    min-width: 200px;
  }

  .filter-label {
    display: block;
    font-weight: 500;
    margin-bottom: 8px;
    color: #495057;
    font-size: 0.9rem;
  }

  .filter-input {
    border: 1px solid #dee2e6;
    border-radius: 6px;
    padding: 8px 12px;
    font-size: 0.9rem;
    transition: border-color 0.3s ease;
  }

  .filter-input:focus {
    border-color: #0080ff;
    box-shadow: 0 0 0 0.2rem rgba(0, 128, 255, 0.25);
  }

  .price-range {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .price-input {
    flex: 1;
  }

  .price-separator {
    color: #6c757d;
    font-weight: 500;
  }

  .rating-filter {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .rating-star {
    cursor: pointer;
    color: #dee2e6;
    transition: color 0.2s ease;
  }

  .rating-star.active {
    color: #ffc107;
  }

  .rating-star:hover {
    color: #ffc107;
  }

  .rating-text {
    font-weight: 500;
    color: #495057;
    margin-left: 8px;
  }

  .facilities-filter {
    display: flex;
    flex-wrap: wrap;
    gap: 12px;
  }

  .facility-checkbox {
    display: flex;
    align-items: center;
    gap: 6px;
    cursor: pointer;
    font-size: 0.9rem;
    color: #495057;
  }

  .facility-checkbox input[type='checkbox'] {
    margin: 0;
  }

  .facility-checkbox i {
    color: #0080ff;
  }

  .search-history {
    border-top: 1px solid #dee2e6;
    padding-top: 16px;
  }

  .search-history h6 {
    font-weight: 600;
    margin-bottom: 12px;
    color: #495057;
  }

  .history-items {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
  }

  .history-item {
    background: #f8f9fa;
    border: 1px solid #dee2e6;
    border-radius: 20px;
    padding: 4px 12px;
    font-size: 0.85rem;
    color: #495057;
    cursor: pointer;
    transition: all 0.2s ease;
  }

  .history-item:hover {
    background: #0080ff;
    color: #fff;
    border-color: #0080ff;
  }

  @media (max-width: 768px) {
    .filter-row {
      flex-direction: column;
    }

    .filter-group {
      min-width: auto;
    }

    .search-input-wrapper {
      flex-direction: column;
      gap: 12px;
    }

    .search-btn {
      width: 100%;
    }
  }
</style>
