<template>
  <div class="search-container">
    <div class="search-box">
      <div class="search-input-group">
        <i class="bi bi-search search-icon"></i>
        <input
          v-model="searchQuery"
          type="text"
          class="form-control search-input"
          placeholder="搜尋目的地、飯店、景點..."
          @keyup.enter="handleSearch"
        />
      </div>
      <button class="btn btn-primary search-btn" @click="handleSearch">
        搜尋
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

const emit = defineEmits(["search"]);

const searchQuery = ref("");

const handleSearch = () => {
  if (searchQuery.value.trim()) {
    emit("search", {
      query: searchQuery.value.trim(),
      timestamp: new Date().toISOString(),
    });
  }
};
</script>

<style scoped>
.search-bar {
  max-width: 1200px;
  margin: 0 auto;
}
</style>
