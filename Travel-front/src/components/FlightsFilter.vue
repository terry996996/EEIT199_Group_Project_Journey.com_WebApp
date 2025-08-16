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
    <div
      v-for="(airline, index) in airlines"
      :key="'airline-' + index"
      class="form-check mb-2"
    >
      <input
        class="form-check-input"
        type="checkbox"
        :id="'airline-' + index"
        v-model="selectedAirlines"
        :value="airline.name"
      />
      <label class="form-check-label" :for="'airline-' + index">
        {{ airline.name }}
      </label>
    </div>
    <button class="btn btn-link p-0" @click="toggleMore">
      {{ showMore ? "收起" : "更多航空公司" }}
    </button>

    <hr class="my-3" />

    <!-- 起飛機場區塊 -->
    <h6 class="mb-2">起飛機場</h6>
    <div
      v-for="(airport, index) in departureAirports"
      :key="'departure-' + index"
      class="form-check mb-2"
    >
      <input
        class="form-check-input"
        type="checkbox"
        :id="'departure-' + index"
        v-model="selectedDepartureAirports"
        :value="airport"
      />
      <label class="form-check-label" :for="'departure-' + index">
        {{ airport }}
      </label>
    </div>

    <hr class="my-3" />

    <!-- 到達機場區塊 -->
    <h6 class="mb-2">到達機場</h6>
    <div
      v-for="(airport, index) in arrivalAirports"
      :key="'arrival-' + index"
      class="form-check mb-2"
    >
      <input
        class="form-check-input"
        type="checkbox"
        :id="'arrival-' + index"
        v-model="selectedArrivalAirports"
        :value="airport"
      />
      <label class="form-check-label" :for="'arrival-' + index">
        {{ airport }}
      </label>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from "vue";

const showMore = ref(false);

const selectedAirlines = ref([]);
const selectedDepartureAirports = ref([]);
const selectedArrivalAirports = ref([]);

const emit = defineEmits([
  "update:selectedAirlines",
  "update:selectedDepartureAirports",
  "update:selectedArrivalAirports",
]);

const allAirlines = [
  { name: "中華航空" },
  { name: "長榮航空" },
  { name: "日本航空" },
  { name: "韓亞航空" },
  { name: "香港航空" },
  { name: "大韓航空" },
  { name: "星宇航空" },
  { name: "全日空" },
  { name: "新加坡航空" },
  { name: "國泰航空" },
  { name: "澳洲航空" },
  { name: "英國航空" },
  { name: "法國航空" },
  { name: "荷蘭皇家航空" },
  { name: "美國航空" },
  { name: "達美航空" },
  { name: "聯合航空" },
  { name: "阿聯酋航空" },
  { name: "卡塔爾航空" },
];

const departureAirports = [
  "台北 (TPE)",
  "台中 (RMQ)",
  "高雄 (KHH)",
  "香港 (HKG)",
  "東京 (NRT)",
  "大阪 (KIX)",
];

const arrivalAirports = [
  "東京 (NRT)",
  "大阪 (KIX)",
  "首爾 (ICN)",
  "新加坡 (SIN)",
  "倫敦 (LHR)",
  "紐約 (JFK)",
];

const airlines = computed(() => {
  return showMore.value ? allAirlines : allAirlines.slice(0, 5);
});

function toggleMore() {
  showMore.value = !showMore.value;
}

function clearAll() {
  selectedAirlines.value = [];
  selectedDepartureAirports.value = [];
  selectedArrivalAirports.value = [];
}

watch(selectedAirlines, (val) => {
  emit("update:selectedAirlines", val);
});
watch(selectedDepartureAirports, (val) => {
  emit("update:selectedDepartureAirports", val);
});
watch(selectedArrivalAirports, (val) => {
  emit("update:selectedArrivalAirports", val);
});
</script>

<style scoped>
.card {
  width: 100%;
  max-width: 280px;
}
</style>
