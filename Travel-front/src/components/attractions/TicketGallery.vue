<template>
  <div class="ticket-gallery">
    <!-- 主圖 + 副圖 -->
    <div class="gallery-grid">
      <div class="main-image" @click="openModal(0)">
        <img :src="mainImage" alt="主圖" class="img-fluid rounded w-100" />
      </div>
      <div class="side-images-wrapper">
        <div class="side-images">
          <div
            class="side-image position-relative"
            v-for="(img, i) in sideImages"
            :key="i"
            @click="openModal(i + 1)"
          >
            <img :src="img" alt="副圖" class="img-fluid rounded w-100" />
            <div v-if="i === 3 && extraCount > 0" class="overlay">
              +{{ extraCount }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 相簿 Modal -->
    <div v-if="showModal" class="gallery-modal">
      <div class="gallery-panel">
        <div class="d-flex justify-content-between align-items-center mb-3">
          <h5>所有相片</h5>
          <button class="btn-close" @click="closeGallery"></button>
        </div>
        <div class="image-grid">
          <img
            v-for="(img, i) in allImages"
            :key="i"
            :src="img"
            class="img-fluid rounded"
            alt="gallery image"
            @click="openSlider(i)"
          />
        </div>
      </div>
    </div>

    <!-- 照片檢視 Modal -->
    <div v-if="showSlider" class="fullscreen-viewer">
      <div class="viewer-panel">
        <div class="d-flex justify-content-between align-items-center mb-3">
          <button class="btn btn-outline-secondary" @click="backToGallery">返回相簿</button>
          <button class="btn-close" @click="closeSlider"></button>
        </div>
        <div class="viewer-body">
          <button class="nav-button left" @click="prevImage">‹</button>
          <transition name="slide-fade" mode="out-in">
            <img
              :key="currentIndex"
              :src="allImages[currentIndex]"
              class="main-slider-image"
            />
          </transition>
          <button class="nav-button right" @click="nextImage">›</button>
        </div>
        <div class="my-2">{{ currentIndex + 1 }}/{{ allImages.length }}</div>
        <div class="thumbnail-slider">
          <img
            v-for="(img, i) in allImages"
            :key="i"
            :src="img"
            class="thumb"
            :class="{ active: i === currentIndex }"
            @click="currentIndex = i"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  images: {
    type: Array,
    required: true
  }
})

const showModal = ref(false)
const showSlider = ref(false)
const currentIndex = ref(0)

const openModal = (index) => {
  showModal.value = true
  currentIndex.value = index
}
const closeGallery = () => showModal.value = false
const openSlider = (index) => {
  currentIndex.value = index
  showSlider.value = true
  showModal.value = false
}
const closeSlider = () => showSlider.value = false
const backToGallery = () => {
  showSlider.value = false
  showModal.value = true
}

const nextImage = () => {
  currentIndex.value = (currentIndex.value + 1) % props.images.length
}
const prevImage = () => {
  currentIndex.value = (currentIndex.value - 1 + props.images.length) % props.images.length
}

const mainImage = computed(() => props.images[0])
const sideImages = computed(() => props.images.slice(1, 5))
const extraCount = computed(() => props.images.length > 5 ? props.images.length - 5 : 0)
const allImages = computed(() => props.images)
</script>

<style scoped>
.ticket-gallery {
  margin-bottom: 2rem;
}
.gallery-grid {
  display: flex;
  gap: 12px;
  cursor: pointer;
}
.main-image {
  flex: 0 0 66.666%;
  cursor: pointer;
}
.side-images-wrapper {
  flex: 0 0 33.333%;
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.side-images {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}
.main-image img {
  height: 400px;
  object-fit: cover;
}
.side-image img {
  height: 190px;
  object-fit: cover;
  cursor: pointer;
}
.overlay {
  position: absolute;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.5);
  color: #fff;
  font-weight: bold;
  font-size: 1.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 0.5rem;
}
.gallery-modal {
  position: fixed;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.4);
  backdrop-filter: blur(3px);
  z-index: 1050;
  display: flex;
  justify-content: center;
  align-items: center;
}
.gallery-panel {
  background: #fff;
  border-radius: 12px;
  padding: 2rem;
  width: 90%;
  max-width: 1000px;
  max-height: 90vh;
  overflow-y: auto;
}
.image-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 1rem;
}
.fullscreen-viewer {
  position: fixed;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.6);
  z-index: 1060;
  display: flex;
  justify-content: center;
  align-items: center;
}
.viewer-panel {
  background: #fff;
  padding: 2rem;
  border-radius: 12px;
  width: 90%;
  max-width: 960px;
  max-height: 90vh;
  overflow-y: auto;
  text-align: center;
  position: relative;
}
.viewer-body {
  position: relative;
}
.main-slider-image {
  width: 100%;
  height: 500px;
  object-fit: contain;
  border-radius: 8px;
}
.thumbnail-slider {
  display: flex;
  overflow-x: auto;
  gap: 10px;
  margin-top: 1rem;
  justify-content: center;
}
.thumb {
  height: 60px;
  border-radius: 6px;
  opacity: 0.6;
  cursor: pointer;
}
.thumb.active {
  opacity: 1;
  border: 2px solid #0d6efd;
}
.nav-button {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: white;
  border: none;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  font-size: 24px;
  cursor: pointer;
  z-index: 10;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}
.nav-button.left {
  left: -20px;
}
.nav-button.right {
  right: -20px;
}
.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: all 0.5s ease;
}
.slide-fade-enter-from {
  opacity: 0;
  transform: translateX(30px);
}
.slide-fade-leave-to {
  opacity: 0;
  transform: translateX(-30px);
}
</style>