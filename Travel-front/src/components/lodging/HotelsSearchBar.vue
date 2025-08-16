<template>
  <div>
    <div class="search-bar" ref="wrapper">
      <form @submit.prevent="handleSearch">
        <div class="row g-3">
          <h2 class="card-title mb-4">住宿搜尋</h2>
          <div class="col-md-3 position-relative" @mouseenter="showDropdown = true" @mouseleave="showDropdown = false">
            <label class="form-label">目的地</label>
            <input type="text" class="form-control" v-model="searchData.destination" @input="stripLeadingSpace"
              placeholder="輸入目的地" />
            <!-- 下拉分類選單 -->
            <div v-show="showDropdown" class="border bg-white shadow p-3 position-absolute w-100" style="z-index: 1000">
              <ul class="nav nav-tabs">
                <li class="nav-item" v-for="(tab, index) in tabs" :key="index">
                  <a class="nav-link" :class="{ active: activeTab === tab.key }" href="#"
                    @click.prevent="activeTab = tab.key">
                    {{ tab.label }}
                  </a>
                </li>
              </ul>
              <div class="tab-content mt-2">
                <div v-for="tab in tabs" :key="tab.key" class="tab-pane fade"
                  :class="{ 'show active': activeTab === tab.key }">
                  <div class="row row-cols-3">
                    <div v-for="city in tab.cities" :key="city" class="col mb-2" style="cursor: pointer"
                      @click="selectCity(city)">
                      {{ city }}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- 入住 / 退房 / 人數 -->
          <div class="col-md-3">
            <label class="form-label">入住日期</label>
            <input type="date" class="form-control" v-model="searchData.checkIn" :min="today" />
          </div>
          <div class="col-md-3">
            <label class="form-label">退房日期</label>
            <input type="date" class="form-control" v-model="searchData.checkOut" :min="minCheckOut" />
          </div>
          <div class="col-md-3">
            <label class="form-label ">人數</label>
            <select class="form-select" v-model="searchData.guests">
              <option value="1">1人</option>
              <option value="2">2人</option>
              <option value="3">3人</option>
              <option value="4">4人</option>
            </select>
          </div>
        </div>
        <div class="text-center mt-3">
          <button type="submit" class="btn btn-primary px-4">搜尋</button>
          <button type="button" class="btn btn-outline-secondary ms-2 px-4" @click="clearSearch">
            清除
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import {
  watch,
  ref,
  onMounted,
  onBeforeUnmount,
  reactive,
  computed,
} from "vue";
import { useRouter, useRoute } from "vue-router";
import Swal from "sweetalert2";

// 取得 router
const router = useRouter();
// 目前路由內容
const route = useRoute();

// 定義 emits
const emit = defineEmits(['search']);

// 接收父層傳來的資料
const props = defineProps({
  initialData: {
    type: Object,
    default: () => ({}),
  },
});

const searchData = reactive({
  destination: "",
  checkIn: "",
  checkOut: "",
  guests: "2",
});

// 輸入城市防呆
// 任何時候字串發生變化（打字、貼上、候選字確定）都會進來
function stripLeadingSpace(e) {
  // 只修剪字首空白，不動中間的空格
  const trimmed = e.target.value.replace(/^\s+/, '');

  // 若有修剪，立刻回寫到 DOM 與 v-model
  if (trimmed !== e.target.value) {
    e.target.value = trimmed;      // 更新輸入框
    searchData.destination = trimmed;      // 同步 v-model
  }
}

function compactCityName(s) {
  return (s || '')
    .replace(/[\s\u3000]+/g, '');  // 把所有空白直接清掉
}

// 日期即時監控防呆
const today = new Date().toISOString().split("T")[0];
// 日期即時監控防呆
const minCheckOut = computed(() => {
  if (!searchData.checkIn) return today;
  const inDate = new Date(searchData.checkIn);
  inDate.setDate(inDate.getDate() + 1); // 加一天
  return inDate.toISOString().split("T")[0];
});
// 日期即時監控防呆
watch(
  () => [searchData.checkIn, searchData.checkOut],
  ([checkIn, checkOut]) => {
    if (checkIn && checkOut) {
      const inDate = new Date(checkIn);
      const outDate = new Date(checkOut);
      if (inDate >= outDate) {
        Swal.fire({
          icon: "error",
          title: "日期錯誤",
          text: "退房日期必須晚於入住日期",
          confirmButtonText: "知道了",
        });
        searchData.checkOut = ""; // 清空錯誤輸入
      }
    }
  }
);

// 下拉選單
const showDropdown = ref(false);
const activeTab = ref("hot");
const tabs = [
  {
    key: "hot",
    label: "熱門",
    cities: ["台北", "東京", "首爾", "曼谷", "香港", "倫敦"],
  },
  {
    key: "japan",
    label: "日韓",
    cities: ["京都", "沖繩", "神戶", "濟州島", "釜山", "札幌"],
  },
  {
    key: "asia",
    label: "東南亞",
    cities: ["河內", "新加坡", "吉隆坡", "富國島", "峇里島", "清邁"],
  },
];

// 點選城市時，更新 input 內容並關閉下拉
function selectCity(city) {
  searchData.destination = city;
  showDropdown.value = false;
}

// 點擊外面區域關閉下拉
const wrapper = ref(null);

function handleClickOutside(event) {
  if (wrapper.value && !wrapper.value.contains(event.target)) {
    showDropdown.value = false;
  }
}

// 父層若帶 initialData 進來就覆寫
watch(
  () => props.initialData,
  (newVal) => {
    if (newVal) {
      Object.assign(searchData, newVal);
    }
  },
  { immediate: true, deep: true }
);

onMounted(() => {
  document.addEventListener("click", handleClickOutside);
  // --- ★ 初始化回填順序：props > URL query > sessionStorage ---
  if (route.query.destination || route.query.checkIn) {
    // 這裡視需求挑你要的欄位；沒有就保持空字串
    Object.assign(searchData, {
      destination: (route.query.destination || '').trim(),
      checkIn: route.query.checkIn,
      checkOut: route.query.checkOut,
      guests: route.query.guests,
    });
  } else {
    const saved = sessionStorage.getItem("hotelSearchData");
    if (saved) {
      const parsed = JSON.parse(saved);
      parsed.destination = (parsed.destination || '').trim();  //修剪前後空白
      Object.assign(searchData, parsed);
    };
  }
});

onBeforeUnmount(() => {
  document.removeEventListener("click", handleClickOutside);
});

// ===== 核心：handleSearch 直接驗證＋導航 =====
function handleSearch() {
  searchData.destination = compactCityName(searchData.destination);
  const { destination, checkIn, checkOut, guests } = searchData;
  if (!destination || !checkIn || !checkOut || !guests) {
    Swal.fire({ icon: "warning", title: "請注意", text: "請完整填寫搜尋條件" });
    return;
  }
  if (new Date(checkIn) >= new Date(checkOut)) {
    Swal.fire({
      icon: "error",
      title: "日期錯誤",
      text: "退房日期必須晚於入住日期",
    });
    return;
  }

  // 儲存到 sessionStorage（也可改 Pinia）
  sessionStorage.setItem("hotelSearchData", JSON.stringify(searchData));

  // 發送搜尋事件給父元件
  emit('search', { ...searchData });

  // 跳轉到搜尋結果頁面
  router.push({
    name: "HotelsFilter",
    query: { ...searchData },
  });
}

// Search Clean
function clearSearch() {
  Object.assign(searchData, {
    destination: "",
    checkIn: "",
    checkOut: "",
    guests: "2",
  });

  // 同步移除暫存（若你用 Pinia 就改對應 store）
  sessionStorage.removeItem("hotelSearchData");

  // 若目前頁面在 HotelsFilter，可選擇把 query 也清掉
  if (route.name === "HotelsFilter") {
    router.replace({ name: "HotelsFilter", query: {} });
  }

  // 關掉城市下拉
  showDropdown.value = false;
}
</script>

<style scoped>
.search-bar {
  margin: 0 auto;

  font-size: 1.125rem;
  /* 文字大小與 HotelsFilterBar 相近（相當於 Bootstrap 的 fs-5） */
  font-weight: 500;
  /* 接近 fw-bold 的效果，但不會太粗 */
}

/* .search-bar label {
  font-size: 1.125rem;
  font-weight: 700;
} */

.search-bar input,
.search-bar select {
  font-size: 1.125rem;
  /* 輸入框與下拉選單字 */
}

.position-relative {
  position: relative;
}

/* 下拉選單最外層設定已在template裡加上 */
.nav-tabs .nav-link.active {
  background-color: #0d6efd;
  color: white;
  font-weight: 600;
}
</style>
