<template>
  <div class="quick-nav">
    <ul>
      <li v-for="item in navItems" :key="item.id" @click="scrollToSection(item.id)">
        <span :class="{ active: currentSection === item.id }">
          {{ item.label }}
        </span>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch } from 'vue'

const props = defineProps({
  contents: Array
})

const currentSection = ref(null)
const navItems = ref([])

watch(
  () => props.contents,
  (val) => {
    if (Array.isArray(val)) {
      const dynamicItems = val.map(content => ({
        id: `section-${content.contentId}`,
        label: content.title
      }))

      navItems.value = [
        { id: 'section-ticket', label: '景點門票' }, // ✅ 前固定
        ...dynamicItems,                              // 🔁 中動態
        { id: 'section-reviews', label: '評論' }, // ✅ 後固定
        { id: 'section-help', label: '取得協助' }
      ]
    }
  },
  { immediate: true }
)

const scrollToSection = (id) => {
  const target = document.getElementById(id)
  const offset = 150
  if (target) {
    const top = target.getBoundingClientRect().top + window.scrollY - offset
    window.scrollTo({ top, behavior: 'smooth' })
    currentSection.value = id
  }
}

const handleScroll = () => {
  const scrollY = window.scrollY
  const offset = 150
  for (const item of navItems.value) {
    const el = document.getElementById(item.id)
    if (el) {
      const top = el.offsetTop - offset
      const bottom = top + el.offsetHeight
      if (scrollY >= top && scrollY < bottom) {
        currentSection.value = item.id
        break
      }
    }
  }
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})
onBeforeUnmount(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>


<style scoped>
.quick-nav ul {
  list-style: none;
  padding: 0;
}

.quick-nav li {
  margin-bottom: 10px;
  cursor: pointer;
}

.quick-nav span {
  display: inline-block;
  padding-left: 4px;
}

.quick-nav .active {
  color: #00a6a6;
  font-weight: bold;
  border-left: 3px solid #00a6a6;
  padding-left: 6px;
}
</style>
