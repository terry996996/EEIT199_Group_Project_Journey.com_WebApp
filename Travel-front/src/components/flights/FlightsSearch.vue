<template>
  <div class="search-section card mb-4" style="overflow: visible">
    <div class="card-body" style="position: relative; overflow: visible">
      <h2 class="card-title mb-4">搜尋機票</h2>
      <form @submit.prevent="searchFlights">
        <div class="row g-3">
          <!-- 第一列：航程類型 -->
          <div class="col-md-12">
            <div class="btn-group w-100" role="group">
              <input type="radio" class="form-check-input" name="tripType" id="roundTrip" v-model="form.tripType"
                value="round" />
              <label class="form-check-label" for="roundTrip">來回</label>
              &emsp;
              <input type="radio" class="form-check-input" name="tripType" id="oneWay" v-model="form.tripType"
                value="one" />
              <label class="form-check-label" for="oneWay">單程</label>
              &emsp;
              <input type="radio" class="form-check-input" name="tripType" id="multi" v-model="form.tripType"
                value="multi" />
              <label class="form-check-label" for="multi">多行程</label>
            </div>
          </div>
        </div>


        <!-- 第二列：單程 / 來回顯示 -->
        <div class="row g-2 align-items-end mt-2" v-if="form.tripType !== 'multi'" ref="wrapper"
          style="position: relative; overflow: visible">
          <!-- 出發地 -->
          <CitySelectInput label="出發地" placeholder="輸入出發地" v-model.trim="form.departure" :tabs="tabs" />

          <!-- 交換按鈕 -->
          <div class="col-md-1 d-flex align-items-end justify-content-center">
            <button type="button" class="btn btn-outline-primary" @click="swapCities">
              <i class="bi bi-arrow-left-right"></i>
            </button>
          </div>

          <!-- 目的地 -->
          <CitySelectInput label="目的地" placeholder="輸入目的地" v-model.trim="form.destination" :tabs="tabs" />

          <!-- 出發日期 -->
          <div class="col-md-2">
            <label class="form-label">出發日期</label>
            <input type="date" class="form-control" v-model="form.departDate" />
          </div>

          <!-- 回程日期（來回用） -->
          <div class="col-md-2" v-if="form.tripType === 'round'">
            <label class="form-label">回程日期</label>
            <input type="date" class="form-control" v-model="form.returnDate" :min="form.departDate" />
          </div>
        </div>

        <!-- 多行程顯示 -->
        <div v-if="form.tripType === 'multi'" class="mt-3">
          <div v-for="(flight, index) in flights" :key="index" class="row g-2 align-items-end mt-2">
            <!-- 出發地 -->
            <CitySelectInput label="出發地" placeholder="輸入出發地" v-model="flight.departure" :tabs="tabs" />

            <!-- 交換按鈕 -->
            <div class="col-md-1 d-flex align-items-end justify-content-center">
              <button type="button" class="btn btn-outline-primary" @click="swapMultiCities(index)" style="height: 38px"
                title="交換">
                <i class="bi bi-arrow-left-right"></i>
              </button>
            </div>

            <!-- 目的地 -->
            <CitySelectInput label="目的地" placeholder="輸入目的地" v-model="flight.destination" :tabs="tabs" />

            <!-- 日期 -->
            <div class="col-md-2 d-flex flex-column">
              <label class="form-label">出發日期</label>
              <input type="date" class="form-control" v-model="flight.date"
                :min="index > 0 ? flights[index - 1].date : null" :disabled="index > 0 && !flights[index - 1].date" />

            </div>

            <!-- 刪除按鈕（第二段以後） -->
            <div class="col-md-1 d-flex align-items-end justify-content-center" v-if="index >= 1">
              <button type="button" class="btn btn-outline-danger" @click="removeFlightLeg(index)" style="height: 38px"
                title="刪除航段">
                <i class="bi bi-x-lg"></i>
              </button>
            </div>
          </div>

          <!-- 增加航班按鈕 -->
          <div class="row mt-2">
            <div class="col-12 text-center">
              <button type="button" class="btn btn-outline-primary" @click="addFlightLeg">
                <i class="bi bi-plus-circle"></i> 增加航班
              </button>
            </div>
          </div>
        </div>


        <!-- ✅ 第三列：艙等 -->
        <div class="row mt-2">
          <div class="col-md-2">
            <label class="form-label">艙等</label>
            <select class="form-select" v-model="form.cabinClass">
              <option value="經濟艙">經濟艙</option>
              <option value="商務艙">商務艙</option>
              <option value="頭等艙">頭等艙</option>
            </select>
          </div>

          <!-- 人數 -->
          <div class="col-md-3 position-relative" style="overflow: visible" ref="passengerRef">
            <label class="form-label">人數</label>
            <div class="dropdown">
              <button class="form-control text-start" type="button"
                @click="showPassengerDropdown = !showPassengerDropdown">
                {{ passengerSummary }}
              </button>
              <div class="dropdown-menu show p-3 shadow border" v-if="showPassengerDropdown"
                style="min-width: 250px; position: absolute; z-index: 1050">
                <div v-for="(label, type) in passengerTypes" :key="type"
                  class="d-flex justify-content-between align-items-center mb-2">
                  <span>{{ label }}</span>
                  <div class="btn-group">
                    <button type="button" class="btn btn-outline-secondary btn-sm" @click="updateCount(type, -1)"
                      :disabled="passenger[type] <= (type === 'adult' ? 1 : 0)">
                      -
                    </button>
                    <span class="mx-2">{{ passenger[type] }}</span>
                    <button type="button" class="btn btn-outline-secondary btn-sm" @click="updateCount(type, 1)">
                      +
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 搜尋按鈕 -->
        <div class="text-center mt-3">
          <div class="col-12">
            <button type="submit" class="btn btn-primary px-4">搜尋航班</button>
            <button v-if="props.showResetButton" type="button" class="btn btn-outline-secondary ms-2 px-4"
              @click="resetSearch">重新搜尋</button>

          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { watch, ref, computed, onMounted, onBeforeUnmount, getCurrentInstance, reactive, toRefs } from 'vue';
import { useRouter } from 'vue-router';
import { useRoute } from 'vue-router';
import Swal from 'sweetalert2';
import CitySelectInput from '../CitySelectInput.vue';


const route = useRoute();
const router = useRouter();
const instance = getCurrentInstance();
const emits = defineEmits(['search']);

const props = defineProps({
  form: {
    type: Object,
    default: () => ({
      tripType: 'round',
      departure: '',
      destination: '',
      departDate: '',
      returnDate: '',
      cabinClass: '經濟艙'
    }),
  },
  showResetButton: {
    type: Boolean,
    default: false
  },
});


// const internalForm = ref({
//   tripType: props.form?.tripType ?? 'round',
//   departure: props.form?.departure ?? '',
//   destination: props.form?.destination ?? '',
//   departDate: props.form?.departDate ?? '',
//   returnDate: props.form?.returnDate ?? '',
//   cabinClass: props.form?.cabinClass ?? '經濟艙',
// });
const form = reactive({
  tripType: props.form?.tripType ?? 'round',
  departure: props.form?.departure ?? '',
  destination: props.form?.destination ?? '',
  departDate: props.form?.departDate ?? '',
  returnDate: props.form?.returnDate ?? '',
  cabinClass: props.form?.cabinClass ?? '經濟艙'
});



// const form = internalForm.value;
const wrapper = ref(null);
const showPassengerDropdown = ref(false);
const passengerRef = ref(null);
const flights = ref([]);

const tabs = [
  { key: 'hot', label: '熱門', cities: ['台北', '東京', '首爾', '曼谷', '香港', '倫敦'] },
  { key: 'japan', label: '日韓', cities: ["京都", "沖繩", "神戶", "濟州島", "釜山", "札幌"]},
  { key: 'asia', label: '東南亞', cities: ["河內", "新加坡", "吉隆坡", "富國島", "峇里島", "清邁"] }
];

const passenger = ref({ adult: 1, child: 0, infant: 0 });
const passengerTypes = {
  adult: '成人 (12+)',
  child: '孩童 (2-11)',
  infant: '嬰兒 (<2)'
};

const passengerSummary = computed(() =>
  `${passenger.value.adult} 成人・${passenger.value.child} 孩童・${passenger.value.infant} 嬰兒`
);

function updateCount(type, delta) {
  const newVal = passenger.value[type] + delta;
  if (newVal >= 0) passenger.value[type] = newVal;
}

function swapCities() {
  const tmp = form.departure;
  form.departure = form.destination;
  form.destination = tmp;
}

function handleClickOutside(e) {
  const target = e.target;
  const isInsidePassenger = passengerRef.value?.contains(target);
  if (!isInsidePassenger) showPassengerDropdown.value = false;
}

function isLegValid(leg, index) {
  const departure = leg.departure?.trim();
  const destination = leg.destination?.trim();
  if (!departure || !destination || !leg.date) {
    Swal.fire({ icon: 'warning', title: '請完成所有航班搜尋資訊' });
    return false;
  }
  if (departure === destination) {
    Swal.fire({ icon: 'error', title: '出發地與目的地不能相同' });
    return false;
  }
  return true;
}

function searchFlights() {

  // 清理輸入：去除出發地與目的地空白
  form.departure = form.departure.trim();
  form.destination = form.destination.trim();

  // 若為多行程，清理每一段航班的城市名稱空白
  if (form.tripType === 'multi') {
    flights.value = flights.value.map(leg => ({
      ...leg,
      departure: leg.departure?.trim() ?? '',
      destination: leg.destination?.trim() ?? ''
    }));
  }
  // 檢查每一段航班的有效性
  let validTrips = [];

  if (form.tripType === 'multi') {
    for (let i = 0; i < flights.value.length; i++) {
      const leg = flights.value[i];
      if (isLegValid(leg, i)) {
        validTrips.push(leg);
      } else {
        return;
      }
    }
  } else {
    const departure = form.departure.trim();
    const destination = form.destination.trim();

    if (!departure) return Swal.fire({ icon: 'warning', title: '請輸入出發地' });
    if (!destination) return Swal.fire({ icon: 'warning', title: '請輸入目的地' });
    if (departure === destination) return Swal.fire({ icon: 'error', title: '出發地與目的地不能相同' });
    if (!form.departDate) return Swal.fire({ icon: 'warning', title: '請選擇出發日期' });

    if (form.tripType === 'round') {
      if (!form.returnDate) return Swal.fire({ icon: 'warning', title: '請選擇回程日期' });
      if (form.returnDate < form.departDate) {
        return Swal.fire({ icon: 'error', title: '回程日期不能早於出發日期' });
      }
    }
  }

  const totalPassengers = passenger.value.adult + passenger.value.child + passenger.value.infant;
  if (totalPassengers === 0) {
    Swal.fire({ icon: 'warning', title: '請選擇至少一位乘客' });
    return;
  }

  const payload = {
    tripType: form.tripType,
    departure: form.departure,
    destination: form.destination,
    departDate: form.departDate,
    returnDate: form.returnDate,
    cabinClass: form.cabinClass,
    passengers: passenger.value,
    trips: form.tripType === 'multi' ? validTrips : []
  };

  // if (instance?.vnode.props?.onSearch) {
  //   emits('search', payload);
  // } else {
  //   router.push({
  //     path: '/flights',
  //     query: {
  //       tripType: payload.tripType,
  //       departure: payload.departure,
  //       destination: payload.destination,
  //       departDate: payload.departDate,
  //       returnDate: payload.returnDate,
  //       cabinClass: payload.cabinClass,
  //       adult: payload.passengers.adult,
  //       child: payload.passengers.child,
  //       infant: payload.passengers.infant,
  //       trips: payload.tripType === 'multi' ? JSON.stringify(payload.trips) : undefined
  //     }
  //   });
  // }
  // localStorage.setItem('flightSearchForm', JSON.stringify(payload));

  // 🔽 顯示 0.5 秒的查詢中 loading
  Swal.fire({
    title: '查詢中',
    text: '請稍候...',
    timer: 500,
    allowOutsideClick: false,
    didOpen: () => {
      Swal.showLoading();
    },
  });

  // 🔽 0.5 秒後執行跳轉或 emit 查詢
  setTimeout(() => {
    if (instance?.vnode.props?.onSearch) {
      emits('search', payload);
    } else {
      router.push({
        path: '/flights',
        query: {
          tripType: payload.tripType,
          departure: payload.departure,
          destination: payload.destination,
          departDate: payload.departDate,
          returnDate: payload.returnDate,
          cabinClass: payload.cabinClass,
          adult: payload.passengers.adult,
          child: payload.passengers.child,
          infant: payload.passengers.infant,
          trips: payload.tripType === 'multi' ? JSON.stringify(payload.trips) : undefined
        }
      });
    }
    localStorage.setItem('flightSearchForm', JSON.stringify(payload));
  }, 500); // 延遲 0.5 秒


}

function addFlightLeg() {
  flights.value.push({
    departure: '',
    destination: '',
    date: ''
  });
}

function swapMultiCities(index) {
  const leg = flights.value[index];
  if (!leg) return;
  const temp = leg.departure;
  leg.departure = leg.destination;
  leg.destination = temp;
}

function removeFlightLeg(index) {
  flights.value.splice(index, 1);
}

// function resetSearch() {

//   // 清空所有欄位
//   form.tripType = 'round';
//   form.departure = '';
//   form.destination = '';
//   form.departDate = '';
//   form.returnDate = '';
//   form.cabinClass = '經濟艙';

//   // 清空乘客人數
//   passenger.value = {
//     adult: 1,
//     child: 0,
//     infant: 0
//   };

//   // 清空多行程資料
//   flights.value = [];

//   // 清除 localStorage 中儲存的查詢
//   localStorage.clear();

//   if (instance?.vnode.props?.onReset) {
//     emits('reset');
//   } else {
//     router.push({ path: '/flights' }); // 重新導向不帶查詢參數
//   }
// }

function resetSearch() {
  // 保留登入資訊
  const preservedKeys = ['token'];
  const preservedData = {};
  preservedKeys.forEach((key) => {
    const value = localStorage.getItem(key);
    if (value !== null) {
      preservedData[key] = value;
    }
  });

  // 清除所有 localStorage
  localStorage.clear();

  // 還原登入資訊
  Object.entries(preservedData).forEach(([key, value]) => {
    localStorage.setItem(key, value);
  });

  // 清空搜尋表單欄位
  form.tripType = 'one';
  form.departure = '';
  form.destination = '';
  form.departDate = '';
  form.returnDate = '';
  form.cabinClass = '經濟艙';

  passenger.value = {
    adult: 1,
    child: 0,
    infant: 0
  };

  flights.value = [];

  // 通知外部元件或重新導向
  if (instance?.vnode.props?.onReset) {
    emits('reset');
  } else {
    router.push({ path: '/flights' });
  }
}



onMounted(() => document.addEventListener('click', handleClickOutside));
onBeforeUnmount(() => document.removeEventListener('click', handleClickOutside));
// 監聽 props.form 的變化，並更新 internalForm 和 flights
// watch(() => form.tripType, (newVal) => {
//   if (newVal === 'multi' && flights.value.length === 0) {
//     flights.value.push({ departure: '', destination: '', date: '' });
//   }
// });

// 監聽 props.form 的變化，並更新 form
watch(() => props.form, (newVal) => {
  if (newVal) {
    form.tripType = newVal.tripType || 'round';
    form.departure = newVal.departure || '';
    form.destination = newVal.destination || '';
    form.departDate = newVal.departDate || '';
    form.returnDate = newVal.returnDate || '';
    form.cabinClass = newVal.cabinClass || '經濟艙';
  }
}, { deep: true, immediate: true });

// 監聽 props.form.passengers 的變化，並更新 passenger
watch(() => props.form.passengers, (newVal) => {
  if (newVal && typeof newVal.adult === 'number') {
    passenger.value = {
      adult: newVal.adult ?? 1,
      child: newVal.child ?? 0,
      infant: newVal.infant ?? 0
    };
  } else {
    // fallback 預設值
    passenger.value = { adult: 1, child: 0, infant: 0 };
  }
}, { immediate: true });


// 監聽 props.form.trips 的變化，並更新 flights
// watch(() => form.tripType, (newVal) => {
//   if (newVal === 'multi') {
//     if (props.form?.trips?.length > 0) {
//       flights.value = [...props.form.trips];
//     } else if (flights.value.length === 0) {
//       flights.value.push({ departure: '', destination: '', date: '' });
//     }
//   }
// }, { immediate: true });
watch(() => form.tripType, (newVal) => {
  if (newVal === 'multi') {
    if (props.form?.trips?.length > 0) {
      flights.value = [...props.form.trips];
    } else {
      // ➤ 總是給兩段預設空白資料（若未提供）
      if (flights.value.length < 2) {
        flights.value = [];
        flights.value.push({ departure: '', destination: '', date: '' });
        flights.value.push({ departure: '', destination: '', date: '' });
      }
    }
  } else {
    // 非 multi 模式時清空多航段資料（避免殘留）
    flights.value = [];
  }
}, { immediate: true });



</script>




<style scoped>
.city-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  /* 一列顯示4個 */
  gap: 8px 16px;
  /* 列間與欄間距 */
}

.city-grid>div {
  padding: 6px 8px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.city-grid>div:hover {
  background-color: #f1f1f1;
}
</style>