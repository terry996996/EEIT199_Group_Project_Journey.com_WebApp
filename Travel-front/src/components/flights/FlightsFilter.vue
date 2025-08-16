<template>
  <div class="card p-3">
    <!-- 篩選標題列 -->
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h5 class="mb-0">自訂篩選</h5>
      <button class="btn btn-link p-0 text-success" @click="clearAll">
        清除全部
      </button>
    </div>

    <!-- 航空公司區塊 -->
    <h6 class="mb-2">航空公司</h6>
    <div v-for="(airline, index) in airlines" :key="'airline-' + index" class="form-check mb-2">
      <input class="form-check-input" type="checkbox" :id="'airline-' + index" v-model="selectedAirlines"
        :value="airline.name" />
      <label class="form-check-label" :for="'airline-' + index">
        {{ airline.name }}
      </label>
    </div>
    <button class="btn btn-link p-0" @click="toggleMore">
      {{ showMore ? "收起" : "更多航空公司" }}
    </button>

    <hr class="my-3" />

    <!-- 價錢範圍 -->
    <div class="mb-4">
      <h6 class="mb-2">價錢</h6>
      <p>NT$1,000 - NT${{ formatCurrency(localPriceMax) }}</p>
      <input type="range" class="form-range" min="1000" max="30000" step="100" v-model="localPriceMax" />
    </div>

    <hr class="my-3" />

    <!-- 起飛機場區塊 -->
    <h6 class="mb-2">起飛機場</h6>
    <div v-for="(airport, index) in departureAirports" :key="'departure-' + index" class="form-check mb-2">
      <input class="form-check-input" type="checkbox" :id="'departure-' + index" v-model="selectedDepartureAirports"
        :value="airport.code" />
      <label class="form-check-label" :for="'departure-' + index">
        {{ airport.name }} ({{ airport.code }})
      </label>
    </div>

    <hr class="my-3" />

    <!-- 到達機場區塊 -->
    <h6 class="mb-2">到達機場</h6>
    <div v-for="(airport, index) in arrivalAirports" :key="'arrival-' + index" class="form-check mb-2">
      <input class="form-check-input" type="checkbox" :id="'arrival-' + index" v-model="selectedArrivalAirports"
        :value="airport.code" />
      <label class="form-check-label" :for="'arrival-' + index">
        {{ airport.name }} ({{ airport.code }})
      </label>
    </div>
    <hr class="my-3" />
    <!-- 起飛時間和到達時間範圍 -->
    <h6 class="mb-2">起飛時間</h6>
    <p>{{ formatTime(departureTimeRange[0]) }} - {{ formatTime(departureTimeRange[1]) }}</p>
    <VueSlider v-model="departureTimeRange" :min="0" :max="1439" :interval="1" :dot-options="[{}, {}]"
      :tooltip-formatter="formatTime" />
    <h6 class="mb-2">到達時間</h6>
    <p>{{ formatTime(returnTimeRange[0]) }} - {{ formatTime(returnTimeRange[1]) }}</p>
    <VueSlider v-model="returnTimeRange" :min="0" :max="1439" :interval="1" :dot-options="[{}, {}]"
      :tooltip-formatter="formatTime" />


  </div>
</template>

<script setup>
import { ref, computed, watch } from "vue";
import VueSlider from 'vue-slider-component'
import 'vue-slider-component/theme/antd.css'

const showMore = ref(false);
const selectedAirlines = ref([]);
const selectedDepartureAirports = ref([]);
const selectedArrivalAirports = ref([]);
const localPriceMax = ref(30000);

// 時間範圍
const departureTimeRange = ref([0, 1439])
const returnTimeRange = ref([0, 1439])

const emit = defineEmits([
  "update:selectedAirlines",
  "update:selectedDepartureAirports",
  "update:selectedArrivalAirports",
  "update:PriceMax",
  "update:departureTimeRange",
  "update:returnTimeRange"
]);

const allAirlines = [
  { name: "中華航空" },
  { name: "長榮航空" },
  { name: "日本航空" },
  { name: "韓亞航空" },
  { name: "星宇航空" },
  { name: "國泰航空" },
  { name: "樂桃航空" },

];
const departureAirports = [
  { name: "台北", code: "TPE" },
  { name: "高雄", code: "KHH" },
  { name: "東京", code: "HND" },
  { name: "成田", code: "NRT" },
  { name: "名古屋", code: "NGO" },
  { name: "關西", code: "KIX" },
  { name: "新千歲", code: "CTS" },
  { name: "仁川", code: "ICN" },
  { name: "首爾  ", code: "GMP" }
];
const arrivalAirports = [
  { name: "台北", code: "TPE" },
  { name: "高雄", code: "KHH" },
  { name: "東京", code: "HND" },
  { name: "成田", code: "NRT" },
  { name: "名古屋", code: "NGO" },
  { name: "關西", code: "KIX" },
  { name: "新千歲", code: "CTS" },
  { name: "仁川", code: "ICN" },
  { name: "首爾  ", code: "GMP" }
];

const airlines = computed(() => {
  return showMore.value ? allAirlines : allAirlines.slice(0, 5);
});



function toggleMore() {
  showMore.value = !showMore.value;
}

// 格式化金額顯示
function formatCurrency(number) {
  return Number(number).toLocaleString('en-US');
}


function clearAll() {
  selectedAirlines.value = [];
  selectedDepartureAirports.value = [];
  selectedArrivalAirports.value = [];
  localPriceMax.value = 30000;
  // departureTimeStart.value = 0;
  // departureTimeEnd.value = 1439;
  // returnTimeStart.value = 0;
  // returnTimeEnd.value = 1439;
  departureTimeRange.value = [0, 1439];
  returnTimeRange.value = [0, 1439];
}

// function formatTime(minutes) {
//   const h = String(Math.floor(minutes / 60)).padStart(2, '0');
//   const m = String(minutes % 60).padStart(2, '0');
//   return `${h}:${m}`;
// }
function formatTime(minutes) {
  const num = Number(minutes);
  if (isNaN(num) || num < 0 || num > 1439) return "00:00";
  const h = String(Math.floor(num / 60)).padStart(2, "0");
  const m = String(num % 60).padStart(2, "0");
  return `${h}:${m}`;
}

watch(localPriceMax, (val) => {
  emit("update:priceMax", val);
});
watch(selectedAirlines, (val) => {
  emit("update:selectedAirlines", val);
});
watch(selectedDepartureAirports, (val) => {
  emit("update:selectedDepartureAirports", val);
});
watch(selectedArrivalAirports, (val) => {
  emit("update:selectedArrivalAirports", val);
});
// 監聽時間範圍變化
watch(departureTimeRange, val => emit("update:departureTimeRange", val))
watch(returnTimeRange, val => emit("update:returnTimeRange", val))

watch(departureTimeRange, val => {
  console.log("departureTimeRange changed:", val)
  emit("update:departureTimeRange", val)
})

</script>

<style scoped>
.card {
  width: 100%;
  max-width: 280px;
}
</style>
