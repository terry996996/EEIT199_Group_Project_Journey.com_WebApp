<template>
  <div>
    <!-- 縮圖列表 -->
    <div class="row g-2">
      <div class="col-4" v-for="(img, index) in images" :key="index">
        <img :src="img" class="img-fluid rounded shadow-sm" style="cursor: pointer" @click="openGallery(index)" />
      </div>
    </div>

    <!-- 藝廊 Modal -->
    <div v-if="showGallery" class="gallery-modal">
      <div class="overlay" @click="closeGallery"></div>
      <div class="gallery-content">
        <img :src="images[currentIndex]" class="main-image" />
        <div class="controls">
          <button @click="prevImage">‹</button>
          <button @click="nextImage">›</button>
        </div>
        <button class="close-btn" @click="closeGallery">✕</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';


const props = defineProps({
  images: Array
});

const showGallery = ref(false);
const currentIndex = ref(0);

const openGallery = (index) => {
  currentIndex.value = index;
  showGallery.value = true;
};

const closeGallery = () => {
  showGallery.value = false;
};

const prevImage = () => {
  currentIndex.value = (currentIndex.value - 1 + props.images.length) % props.images.length;
};

const nextImage = () => {
  currentIndex.value = (currentIndex.value + 1) % props.images.length;
};
</script>

<style scoped>
.gallery-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1050;
}

.overlay {
  position: absolute;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.7);
}

.gallery-content {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 80%;
  max-width: 800px;
  transform: translate(-50%, -50%);
  background: white;
  padding: 1rem;
  border-radius: 8px;
  text-align: center;
}

.main-image {
  max-width: 100%;
  max-height: 70vh;
}

.controls button {
  margin: 0 1rem;
  font-size: 2rem;
  background: none;
  border: none;
  cursor: pointer;
}

.close-btn {
  position: absolute;
  top: 0.5rem;
  right: 1rem;
  font-size: 1.5rem;
  background: none;
  border: none;
  cursor: pointer;
}
</style>
