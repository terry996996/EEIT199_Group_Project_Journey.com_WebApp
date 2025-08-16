<template>
  <div class="search-section card mb-4 position-relative overflow-visible no-hover no-shadow">
    <div class="card-body">

      <!-- 🔠 上方動態標題（例：在東京的熱門美術館） -->
      <h2 class="search-page-title">{{ dynamicTitle }}</h2>

      <!-- 🔍 搜尋欄主區塊（地點選擇 + 關鍵字輸入） -->
      <div class="search-bar-combined d-flex align-items-center">

        <!-- 📍 地點選擇器（按鈕 + 城市下拉選單） -->
        <div class="location-selector-combined" ref="dropdownRef">
          <button class="location-button-combined" @click="toggleDropdown">
            {{ selectedLabel }}
            <i class="bi bi-caret-down-fill ms-1"></i>
          </button>

          <!-- 🧭 城市下拉選單 -->
          <div v-show="dropdownVisible" class="dropdown-box full-width-box">
            <div class="dropdown-title mb-2">日本</div>
            <div class="city-grid">
              <div class="city-item" v-for="city in allJapaneseCities" :key="city.code" @click="selectCity(city)"
                :class="{ 'text-primary fw-bold': selectedCity?.code === city.code }">
                {{ city.name }}
              </div>
            </div>
          </div>
        </div>

        <!-- 🔤 關鍵字輸入區塊 -->
        <div class="search-input-wrapper flex-grow-1 position-relative">

          <!-- 🔍 放大鏡圖示 -->
          <i class="bi bi-search search-icon"></i>

          <!-- 🖊️ 輸入框 -->
          <input type="text" class="search-keyword-input" placeholder="想體驗什麼活動？" v-model="searchForm.displayKeyword"
            @focus="showDropdown = true" @blur="handleBlur" @input="onInputChange" @keyup.enter="handleSearch" />
          <button class="search-btn" @click="handleSearch">
            搜尋
          </button>
          <!-- 🔳 搜尋建議遮罩，點擊空白處會關閉 -->
          <div v-if="showSuggestions" class="search-mask" @click="showSuggestions = false"></div>

          <!-- 🎯 建議選單（固定定位於畫面中間） -->
          <div
            v-if="showDropdown && (suggestionsTypes.length || suggestionsCountries.length || suggestionsSpots.length)"
            class="search-dropdown shadow-box">
            <!-- ⛳ 建議區塊：景點類別 -->
            <div class="suggest-group">
              <div class="suggest-header">景點類別</div>
              <div class="suggest-tags">
                <div class="tag-pill" v-for="type in suggestionsTypes" :key="'type-' + type"
                  @mousedown.prevent="selectSuggestion(type, 'type')">
                  {{ type }}
                </div>
              </div>
            </div>

            <!-- ⛳ 建議區塊：國家 -->
            <div class="suggest-group">
              <div class="suggest-header">國家</div>
              <div class="suggest-tags">
                <div class="tag-pill" v-for="country in suggestionsCountries" :key="'country-' + country"
                  @mousedown.prevent="selectSuggestion(country, 'country')">
                  {{ country }}
                </div>
              </div>
            </div>

            <!-- ⛳ 建議區塊：標籤 -->
            <div class="suggest-group">
              <div class="suggest-header">標籤</div>
              <div class="suggest-tags">
                <div class="tag-pill" v-for="spot in suggestionsSpots" :key="'spot-' + spot"
                  @mousedown.prevent="selectSuggestion(spot, 'spot')">
                  {{ spot }}
                </div>
              </div>
            </div>

          </div>
        </div>

      </div> <!-- /.search-bar-combined -->

    </div>
  </div>
</template>



<script setup>
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { searchAttractionTickets } from '@/services/attractions/ticketService'
import {
  getAttractionTagsOptions,
  getAttractionTypesOptions,
  getCountryOptions
} from '@/services/attractions/ticketService'
const keywords = ref([])
// ==========================
// 📦 1. Props / Emits 設定
// ==========================

// 接收來自父元件的預設城市名稱（若未選擇地點，顯示這個）
const props = defineProps({
  cityName: {
    type: String,
    default: '全球'
  },
  titleOverride: { type: String, default: null }
})

// 可對外發出事件（例如搜尋、選取地點）
const emit = defineEmits(['search', 'locationSelected'])


// ==========================
// 🔍 2. 搜尋欄邏輯區
// ==========================

// 綁定輸入欄的關鍵字


// 控制是否顯示建議選單
const showDropdown = ref(false)
const showSuggestions = ref(true) // 可控制遮罩或提示顯示

// 用來操作路由導向
const router = useRouter()

//============== 當使用者按 Enter 搜尋或點選建議詞時執行======================
const handleSearch = () => {
  let keyword = searchForm.value.keyword.trim()
  if (!keyword) keyword = '' // ✅ keyword 可以為空，僅靠條件查詢

  // 👉 抓選單 label（type / tag / country / city）
  const typeLabel = selectedTags.value.type
  const tagLabel = selectedTags.value.spot
  const countryLabel = selectedTags.value.country
  const cityLabel = selectedCity.value?.name || ''

  // 👉 抓對應 ID（實際查詢要送）
  const typeId = typesList.value.find(t => t.label === typeLabel)?.id
  const tagId = tagsList.value.find(t => t.label === tagLabel)?.id
  const countryId = countriesList.value.find(c => c.label === countryLabel)?.id || null
  const cityId = selectedCity.value?.code !== 'all' ? selectedCity.value?.code : null

  // ✅ 組成查詢 DTO 並 emit 給父層
  const dto = {
    keyword,
    cityId,
    typeIds: typeId ? [typeId] : [],
    countryId,
    tagIds: tagId ? [tagId] : []
  }

  emit('search', dto)

  // ✅ 寫入網址（讓 dynamicTitle 能同步）
  router.push({
    path: '/tickets',
    query: {
      keyword,
      cityId,
      countryId,
      typeIds: typeId ? [typeId].join(',') : undefined,
      tagIds: tagId ? [tagId].join(',') : undefined
    }
  })

  // ✅ 手動更新 dynamicTitle 使用的 keywords（不等 router 再更新）
  keywords.value = keyword.split(' ')
}
const onInputChange = (e) => {
  let val = e.target.value || ''

  const removeWords = [
    selectedTags.value.type,
    selectedTags.value.country,
    selectedTags.value.spot,
    selectedCity.value?.name
  ].filter(Boolean)

  removeWords.forEach(word => {
    const pattern = new RegExp(`\\b${word}\\b`, 'gi')
    val = val.replace(pattern, '')
  })

  val = val.replace(/\s+/g, ' ').trim()

  // ✅ 更新實際查詢用的 keyword
  searchForm.value.keyword = val

  // ✅ 如果 displayKeyword 被清空，自動清除 ID（很關鍵）
  if (searchForm.value.displayKeyword.trim() === '') {
    selectedTags.value = {
      type: null,
      country: null,
      spot: null
    }
    selectedCity.value = null
  }
}
// 三類建議詞
const suggestionsTypes = ref([])
const suggestionsCountries = ref([])
const suggestionsSpots = ref([])

onMounted(async () => {
  try {
    // 1️⃣ 景點類型 → suggestionsTypes
    const typesRes = await getAttractionTypesOptions()
    suggestionsTypes.value = typesRes.data.map(t => t.label)

    // 2️⃣ 國家清單（✅ 全部）→ suggestionsCountries
    const countriesRes = await getCountryOptions()
    suggestionsCountries.value = countriesRes.data.map(c => c.label)

    // 3️⃣ 景點標籤（特色）→ suggestionsSpots
    const tagsRes = await getAttractionTagsOptions()
    suggestionsSpots.value = tagsRes.data.map(tag => tag.label)
  } catch (err) {
    console.error('❌ 取得建議詞失敗', err)
  }
})

// 用來記錄最後選取的關鍵字（可用於動態標題）
const selectedKeyword = ref(null)
//點選順序標籤
const updateKeywordFromTags = () => {
  const combined = [
    selectedTags.value.type,
    selectedTags.value.country,
    selectedTags.value.spot
  ].filter(Boolean).join(' ')

  searchForm.value.displayKeyword = combined // ✅ 改這裡！
}
const searchForm = ref({
  displayKeyword: '', // 👉 顯示用（含建議詞）
  keyword: ''         // 👉 查詢用（只剩下使用者手打的字）
})
// 點選建議詞時：同步更新關鍵字、記錄選取、導向搜尋
const selectSuggestion = (val, category) => {
  selectedTags.value[category] = val
  updateKeywordFromTags()
}

//選中項目欄
const selectedTags = ref({
  type: null,
  country: null,
  spot: null
})
// ==========================
// 📍 3. 地點下拉邏輯區
// ==========================

// 當前選中的城市
const selectedCity = ref(null)

// 控制地點下拉選單顯示
const dropdownVisible = ref(false)

// 取得元件區塊參考，用來監聽點擊外部自動關閉
const dropdownRef = ref(null)

// 切換地點下拉顯示/隱藏
const toggleDropdown = () => {
  dropdownVisible.value = !dropdownVisible.value
}

// 點擊選單外部時自動關閉
const handleClickOutside = (event) => {
  const target = event.target

  // 🔹 判斷是否點在城市下拉區域外（ref 要先正確綁定）
  const isOutsideCityDropdown =
    dropdownRef.value && !dropdownRef.value.contains(target)

  // 🔹 判斷是否點在建議區外（注意 class 名稱一致）
  const isOutsideSuggestions =
    !target.closest('.search-dropdown') &&
    !target.closest('.search-keyword-input') &&
    !target.closest('.search-btn')

  // ✅ 收起城市選單
  if (isOutsideCityDropdown) {
    dropdownVisible.value = false
  }

  // ✅ 收起搜尋建議選單
  if (isOutsideSuggestions) {
    showDropdown.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})
onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})

// 城市清單（含「所有城市」）
const allJapaneseCities = [
  { code: 'all', name: '所有城市' },
  { code: 'tokyo', name: '東京' },
  { code: 'osaka', name: '大阪' },
  { code: 'kyoto', name: '京都' },
  { code: 'hokkaido', name: '北海道' },
  { code: 'fukuoka', name: '福岡' },
  { code: 'okinawa', name: '沖繩本島' }
]

// 選取城市後：關閉下拉，更新選中，發送給父元件
const selectCity = (city) => {
  selectedCity.value = city
  dropdownVisible.value = false
  emit('locationSelected', city)
}

// 顯示地點按鈕上的城市文字
const selectedLabel = computed(() => {
  return selectedCity.value?.name || '全球'
})


// ==========================
// 🔠 4. 動態標題顯示（地點 + 關鍵字）
// ==========================


const route = useRoute()

// 從網址中讀取 ID（query 會是字串，要轉成 number）
const selectedTypeId = computed(() => {
  const raw = route.query.typeIds
  if (!raw) return null
  return Number(Array.isArray(raw) ? raw[0] : raw)
})

const selectedTagId = computed(() => {
  const raw = route.query.tagIds
  if (!raw) return null
  return Number(Array.isArray(raw) ? raw[0] : raw)
})

const selectedCountryId = computed(() => {
  return route.query.countryId ? Number(route.query.countryId) : null
})




// ✅ 從後端取得：類型、國家、標籤清單
const typesList = ref([])
const countriesList = ref([])
const tagsList = ref([])
const cityList = ref([])
onMounted(async () => {
  try {
    const [typeRes, countryRes, tagRes] = await Promise.all([
      getAttractionTypesOptions(),
      getCountryOptions(),
      getAttractionTagsOptions()
    ])
    typesList.value = typeRes.data       // [{ id, label }]
    countriesList.value = countryRes.data
    tagsList.value = tagRes.data
  } catch (err) {
    console.error('❌ 類型/國家/標籤載入失敗', err)
  }
})

// ✅ 根據 keywords 比對後端的 label
const selectedType = computed(() =>
  typesList.value.find(type => type.id === selectedTypeId.value)
)

const selectedCountry = computed(() =>
  countriesList.value.find(c => c.id === selectedCountryId.value)
)

const selectedTag = computed(() =>
  tagsList.value.find(tag => tag.id === selectedTagId.value)
)


// ✅ 動態標題（地點 + 類型 + 標籤）
const dynamicTitle = computed(() => {
  if (props.titleOverride) return props.titleOverride;
  const country = selectedCountry.value?.label || '全球'
  const type = selectedType.value?.label || '景點'
  const tag = selectedTag.value?.label

  return tag
    ? `在${country}的熱門${type}（${tag}）門票`
    : `在${country}的熱門${type}門票`
})

</script>




<style scoped>
/* 🔠 標題樣式 */
.search-page-title {
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 1rem;
}

/* 🔍 搜尋欄與地點選擇器整體容器 */
.search-bar-combined {
  border: 1px solid #ccc;
  border-radius: 50px;
  background-color: #f9f9f9;
  height: 48px;
  overflow: visible;
  /* ⛑ 讓 dropdown 不被遮住 */
  position: relative;
  /* ✅ dropdown 可以依此定位 */
  z-index: 1;
  max-width: 800px;
  margin: 0 auto;
  display: flex;
  align-items: center;
}

/* 📍 地點選擇器容器 */
.location-selector-combined {
  position: relative;
  /* ✅ 提供 dropdown 定位參考 */
  flex-shrink: 0;
  z-index: 50;
}

/* 📍 地點按鈕 */
.location-button-combined {
  background: transparent;
  border: none;
  padding: 0 16px;
  height: 100%;
  font-size: 1rem;
  display: flex;
  align-items: center;
  cursor: pointer;
}

/* 🧭 城市下拉選單（地點選單） */
.dropdown-box.full-width-box {
  position: absolute;
  top: 100%;
  /* ✅ 緊貼按鈕下方 */
  left: 0;
  z-index: 9999;
  min-width: 240px;
  background: white;
  border-radius: 12px;
  border: 1px solid #dee2e6;
  padding: 1rem;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  max-height: 400px;
  overflow-y: auto;
}

/* 🏙 城市選單標題 */
.dropdown-title {
  font-weight: bold;
  font-size: 1rem;
  margin-bottom: 0.5rem;
}

/* 🧩 城市項目網格 */
.city-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 0.5rem;
}

.city-item {
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 6px;
  transition: background-color 0.2s;
}

.city-item:hover {
  background-color: #f1f1f1;
}

/* 🔍 搜尋欄輸入區容器 */
.search-input-wrapper {
  position: relative;
  width: 100%;
}

/* 🔍 放大鏡圖示 */
.search-icon {
  position: absolute;
  top: 50%;
  left: 12px;
  transform: translateY(-50%);
  color: #888;
  font-size: 1rem;
}

/* 🔤 搜尋欄輸入框 */
.search-keyword-input {
  width: 100%;
  border: none;
  outline: none;
  height: 48px;
  padding: 0 16px 0 40px;
  /* 左側空出放大鏡 */
  background-color: transparent;
  font-size: 1rem;
  border-left: 1px solid #ccc;
}

/* 🎯 建議詞 dropdown */
.search-dropdown.shadow-box {
  position: absolute;
  /* ✅ 脫離所有父層定位 */
  /* ✅ 視覺上剛好在搜尋欄下方，自行微調 */
  left: 50%;
  transform: translateX(-50%);
  width: 80%;
  max-width: 900px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 16px rgba(0, 0, 0, 0.15);
  padding: 1.5rem;
  z-index: 99999;
  /* ✅ 高於所有元素 */
}

/* 💡 建議詞樣式群組 */
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

/* 🔎 搜尋按鈕樣式 */
.search-btn {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  background-color: #0d6efd;
  color: white;
  border: none;
  padding: 6px 16px;
  border-radius: 20px;
  font-size: 0.95rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.search-btn:hover {
  background-color: #0b5ed7;
}
</style>