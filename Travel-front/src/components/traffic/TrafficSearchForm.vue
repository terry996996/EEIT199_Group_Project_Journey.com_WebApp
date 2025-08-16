<template>
  <div class="search-section card mb-4" style="overflow: visible; position: relative">
    <div class="card-body position-relative overflow-visible">
      <h2 class="card-title mb-4">搜尋交通票券</h2>
      <form @submit.prevent="searchTraffic">
        <div class="row g-3">
          <!-- 交通類型 -->
          <!-- <div class="col-md-12">
            <div class="btn-group w-100" role="group">
              <input type="radio" class="form-check-input" name="trafficType" id="train" v-model="localForm.type"
                value="train" checked />
              <label class="form-check-label" for="train">鐵路</label>
              &emsp;
              <input type="radio" class="form-check-input" name="trafficType" id="bus" v-model="localForm.type"
                value="bus" />
              <label class="form-check-label" for="bus">客運</label>
            </div>
          </div> -->

          <!-- 地點 -->
          <div class="col-md-4 position-relative" ref="dropdownWrapper">
            <label class="form-label">請輸入關鍵字</label>
            <input type="text" class="form-control" v-model="localForm.name" placeholder="請輸入交通票券關鍵字"
              @focus="showDropdown = true" />

            <!-- 下拉選單 -->
            <div class="dropdown-menu show w-100 mt-1 shadow" v-if="showDropdown"
              style="position: absolute; top: 100%; left: 0; z-index: 9999">
              <!-- 分類 Tabs -->
              <ul class="nav nav-tabs px-3 pt-2">
                <li class="nav-item" v-for="tab in tabs" :key="tab.key">
                  <a class="nav-link" :class="{ active: activeTab === tab.key }" href="#"
                    @click.prevent="activeTab = tab.key">{{ tab.label }}</a>
                </li>
              </ul>

              <!-- 城市清單 -->
              <div class="p-3">
                <div class="row">
                  <div class="col-3 mb-2" v-for="city in cityOptions[activeTab]" :key="city">
                    <button class="btn btn-link text-start p-0" @click="selectCity(city)">
                      {{ city }}
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>


          <!-- 搜尋按鈕 -->
          <div class="col-12 text-center">
            <button type="submit" class="btn btn-primary px-4">搜尋交通</button>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { reactive, watch, ref, onMounted, onBeforeUnmount } from 'vue';
import { useRouter } from 'vue-router';
import Swal from 'sweetalert2';

// 接收外部 v-model 的 props（可選加強）
const props = defineProps({
  modelValue: Object
});
const router = useRouter();
const showDropdown = ref(false);
const activeTab = ref('hot');
const tabs = [
  { key: 'hot', label: '熱門' },
  { key: 'japanKorea', label: '日韓' },
  { key: 'southeastAsia', label: '東南亞' }
];



const selectCity = (city) => {
  localForm.name = city;
  showDropdown.value = false;
};

const handleClickOutside = (event) => {
  if (dropdownWrapper.value && !dropdownWrapper.value.contains(event.target)) {
    showDropdown.value = false;
  }
};

const dropdownWrapper = ref(null);

const cityOptions = {
  hot: ['東京', '大阪', '首爾', '曼谷', '香港', '倫敦'],
  japanKorea: ['東京', '大阪', '札幌', '首爾', '釜山'],
  southeastAsia: ['曼谷', '新加坡', '吉隆坡', '河內', '胡志明市']
};

const emit = defineEmits(['update:modelValue', 'search']);

const localForm = reactive({
  type: props.modelValue?.type || 'train',
  name: props.modelValue?.name || ''
});

//送出關鍵字內容
function emitSearch() {
  emit('search', { ...localForm });
}


function searchTraffic() {
  // ✅ 防呆檢查：地點不能空白
  if (!localForm.name.trim()) {
    Swal.fire({
      icon: 'warning',
      title: '請輸入內容'
    });
    return;
  }

  // ✅ 驗證通過，送出搜尋
  emitSearch();

  // ✅ 如果有需要也保留跳轉
  router.push('/traffic');
}

watch(localForm, (newVal) => {
  emit('update:modelValue', { ...newVal });
});

onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});

onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside);
});
</script>
