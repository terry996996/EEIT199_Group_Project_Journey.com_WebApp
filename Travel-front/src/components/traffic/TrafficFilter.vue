<template>
  <div class="card p-3">
    <!-- 商品類別 -->
    <!-- <div class="mb-4">
      <h6 class="fw-bold">商品類別</h6>
      <div class="form-check" v-for="type in ticketTypes" :key="type">
        <input class="form-check-input" type="checkbox" :id="type" :value="type" v-model="filters.selectedTypes" />
        <label class="form-check-label" :for="type">{{ type }}</label>
      </div>
    </div> -->




    <hr class="my-3" />
    <!-- 國家篩選 -->
    <div class="mb-4">
      <h6 class="fw-bold">國家</h6>
      <div class="form-check" v-for="region in regions" :key="region">
        <input class="form-check-input" type="checkbox" :id="region" :value="region"
          v-model="filters.selectedRegions" />
        <label class="form-check-label" :for="region">{{ region }}</label>
      </div>
    </div>

    <hr class="my-3" />

    <!-- 價錢範圍 -->
    <div class="mb-4">
      <h6 class="fw-bold">價錢（NT$）</h6>
      <p>NT$1000 - NT${{ localPriceMax }}</p>
      <input type="range" class="form-range" min="1000" max="30000" step="100" v-model="localPriceMax" />
    </div>



  </div>
</template>

<script setup>
import { ref, watch } from "vue";
import { reactive } from 'vue'

// 接收 priceMax prop
const props = defineProps({
  priceMax: {
    type: Number,
    default: 30000
  }
})

const emit = defineEmits(['update:priceMax'])

// 建立本地雙向綁定
const localPriceMax = ref(props.priceMax)

const filters = reactive({
  selectedRegions: props.modelValue?.selectedRegions || [],
  priceMin: props.modelValue?.priceMin || 0,
  priceMax: props.modelValue?.priceMax || 1000
})



// 當 filters 任一欄位變動時，通知父層
watch(localPriceMax, (val) => {
  emit('update:priceMax', val)
})

</script>

<style scoped>
.form-check-inline {
  margin-right: 12px;
}
</style>
