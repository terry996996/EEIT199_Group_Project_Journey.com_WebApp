<template>
  <div class="col-md-3 position-relative" style="overflow: visible" ref="inputWrapper">
    <label class="form-label">{{ label }}</label>
    <input type="text" class="form-control" :value="modelValue" :placeholder="placeholder" @focus="show = true"
      @input="$emit('update:modelValue', $event.target.value)" />
    <div v-if="show" class="dropdown-menu show shadow p-3 position-absolute"
      style="z-index: 1050; max-height: 300px; overflow-y: auto; min-width: 500px; max-width: 700px">
      <ul class="nav nav-tabs" style="white-space: nowrap">
        <li class="nav-item" v-for="(tab, index) in tabs" :key="index">
          <a class="nav-link text-nowrap" :class="{ active: activeTab === tab.key }"
            @click.prevent="activeTab = tab.key" href="#">
            {{ tab.label }}
          </a>
        </li>
      </ul>
      <div class="tab-content mt-2">
        <div v-for="tab in tabs" :key="tab.key" class="tab-pane fade" :class="{ 'show active': activeTab === tab.key }">
          <div class="city-grid">
            <div v-for="city in tab.cities" :key="city" @click="selectCity(city)" style="cursor: pointer">
              {{ city }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'

const props = defineProps({
  modelValue: String,
  label: String,
  placeholder: String,
  tabs: Array
})
const emit = defineEmits(['update:modelValue'])

const show = ref(false)
const activeTab = ref(props.tabs?.[0]?.key || 'hot')
const inputWrapper = ref(null)

function selectCity(city) {
  emit('update:modelValue', city)
  show.value = false
}

function handleClickOutside(e) {
  if (!inputWrapper.value?.contains(e.target)) {
    show.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})
onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style scoped>
.city-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 8px 16px;
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
