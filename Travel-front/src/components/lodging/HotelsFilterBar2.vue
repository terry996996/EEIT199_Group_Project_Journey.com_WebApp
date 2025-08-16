<template>
  <div class="card p-5">
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h5 class="mb-0">自訂篩選</h5>
      <button class="btn btn-link p-0 fcc-btn" @click="clearAll">
        清除全部
      </button>
    </div>
    <!-- 評分區塊 -->
    <h6 class="mb-2">評分</h6>
    <div v-for="(type, index) in score" :key="index">
      <div class="form-check">
        <input class="form-check-input" type="checkbox" :id="'check-' + index" :value="type" v-model="selectedScore">
        <label class="form-check-label" :for="'check-' + index">
          {{ type }}
        </label>
      </div>
    </div>
    <hr class="my-3" />

    <!-- 平均每晚含稅價區塊 -->
    <h6 class="mb-2">平均每晚含稅價</h6>
    <label class="form-label fw-bold">價格範圍</label>
    <vue-slider v-model="priceRange" :min="0" :max="10000" :interval="100" :tooltip="'always'" :use-range="true"
      :lazy="true" :dot-size="18" :tooltip-formatter="formatPrice" />
    <div class="mt-2">
      NT${{ priceRange[0] }} ~ {{ priceRange[1] >= 10000 ? '10000+' : 'NT$' + priceRange[1] }}
    </div>
    <hr class="my-3" />

    <!-- 住宿類型區塊 -->
    <h6 class="mb-2">住宿類型</h6>
    <div v-for="(lodgingsType, index) in lodgingsTypes" :key="lodgingsType.id" :value="lodgingsType.id">
      <div class="form-check">
        <input class="form-check-input" type="checkbox" :id="'check-ltype-' + index" :value="lodgingsType.id"
          v-model="selectedLodgingsTypes" />
        <label class="form-check-label" :for="'check-ltype-' + index">
          {{ lodgingsType.name }}
        </label>
      </div>
    </div>
    <hr class="my-3" />

    <!-- 床型區塊 -->
    <h6 class="mb-2">床型</h6>
    <div v-for="(bedType, index) in uniqueBedTypes" :key="bedType.id" :value="bedType.id">
      <div class="form-check">
        <input class="form-check-input" type="checkbox" :id="'check-bed-' + index" :value="bedType.id"
          v-model="selectedBedTypes" />
        <label class="form-check-label" :for="'check-bed-' + index">
          {{ bedType.name }}
        </label>
      </div>
    </div>
    <hr class="my-3" />

    <!-- 住宿設施區塊 -->
    <h6 class="mb-2">住宿設施</h6>
    <div v-for="(facilitie, index) in visibleUniqueFacilities" :key="facilitie.id" :value="facilitie.id">
      <div class="form-check">
        <input class="form-check-input" type="checkbox" :id="'check-fac-' + index" :value="facilitie.id"
          v-model="selectedFacilities" />
        <label class="form-check-label" :for="'check-fac-' + index">
          {{ facilitie.name }}
        </label>
      </div>
    </div>
    <!-- 展開/收合按鈕 -->
    <div v-if="uniqueFacilities.length > showCount">
      <a href="#" class="fcc-btn" @click.prevent="toggleShowAll">
        {{ showAllFacilities ? '收合' : `顯示全部${uniqueFacilities.length}項` }}
        <span :class="showAllFacilities ? 'bi-chevron-up' : 'bi-chevron-down'"></span>
      </a>
    </div>

  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import VueSlider from 'vue-slider-component'
import 'vue-slider-component/theme/default.css'

const emit = defineEmits(['filter-change'])

const props = defineProps({
  bedTypes: {
    type: Array,
    default: () => []
  },
  facilities: {
    type: Array,
    default: () => []
  },
  lodgingsTypes: {
    type: Array,
    default: () => []
  }
})

const score = ['5分', '四分', '三分', '三以下']
const selectedScore = ref([])
const priceRange = ref([0, 10000]) // 初始價格範圍
const selectedLodgingsTypes = ref([])
const selectedBedTypes = ref([])
const selectedFacilities = ref([])

// 住宿區域顯示數量控制
const showCount = 5 // 預設顯示數量
const showAllFacilities = ref(false)

// 床型去重處理
const uniqueBedTypes = computed(() => {
  const seen = new Set()
  return props.bedTypes.filter(bedType => {
    if (seen.has(bedType.id)) {
      return false
    }
    seen.add(bedType.id)
    return true
  })
})

// 設施去重處理
const uniqueFacilities = computed(() => {
  const seen = new Set()
  return props.facilities.filter(facility => {
    if (seen.has(facility.id)) {
      return false
    }
    seen.add(facility.id)
    return true
  })
})

// 可見設施（去重後）
const visibleUniqueFacilities = computed(() => 
  showAllFacilities.value ? uniqueFacilities.value : uniqueFacilities.value.slice(0, showCount)
)

watch([selectedScore, priceRange, selectedLodgingsTypes, selectedBedTypes, selectedFacilities], emitFilterChange)

function toggleShowAll() {
  showAllFacilities.value = !showAllFacilities.value
}

function formatPrice(value) {
  return value >= 10000 ? 'NT$10000+' : `NT$${value}`
}

function emitFilterChange() {
  emit('filter-change', {
    selectedScore: selectedScore.value,
    priceRange: priceRange.value,
    selectedLodgingsTypes: selectedLodgingsTypes.value,
    selectedBedTypes: selectedBedTypes.value,
    selectedFacilities: selectedFacilities.value
  })
}

// 清除全部搜尋條件
function clearAll() {
  selectedScore.value = [];
  priceRange.value = [0, 10000];
  selectedLodgingsTypes.value = [];
  selectedBedTypes.value = [];
  selectedFacilities.value = [];
  emitFilterChange();
}
</script>

<style scoped>
.filter-card {
  background-color: white;
  border: 1px solid #dee2e6;
  border-radius: 0.375rem;
  padding: 1rem;
  margin-bottom: 0.25rem;
  /* 可依需求調整 */
}

/* 基本顯示維持綠色（或照你的 text-success） */
.fcc-btn {
  color: var(--bs-success);
  /* = text-success */
}

/* 滑鼠滑過或聚焦 → 變紅色 */
.fcc-btn:hover,
.fcc-btn:active {
  color: var(--bs-danger);
  /* = text-danger */
}
</style>