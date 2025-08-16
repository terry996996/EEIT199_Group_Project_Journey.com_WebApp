<template>

  <div class="comment-card mb-3">
    <div class="d-flex align-items-start">
      <img
        :src="comment.avatar"
        :alt="comment.username"
        class="rounded-circle me-3"
        style="width: 50px; height: 50px; object-fit: cover"
      />
      <div class="flex-grow-1">
        <div class="d-flex justify-content-between align-items-center mb-1">
          <h6 class="mb-0">{{ comment.username }}</h6>
          <small class="text-muted">{{ comment.date }}</small>
        </div>
        <div class="rating mb-2">
          <i
            v-for="star in 5"
            :key="star"
            class="fa-star"
            :class="star <= comment.rating ? 'fas text-warning' : 'far'"
            style="font-size: 0.9rem"
          ></i>
          <span class="ms-2 text-muted">{{ comment.rating }} 星</span>
        </div>
        <p class="mb-2">{{ comment.content }}</p>
        <div
          v-if="comment.images && comment.images.length > 0"
          class="comment-images"
        >
          <img
            v-for="(image, index) in comment.images"
            :key="index"
            :src="image"
            class="img-thumbnail me-2"
            style="
              width: 100px;
              height: 100px;
              object-fit: cover;
              cursor: pointer;
            "
            @click="showImage(image)"
          />
        </div>
      </div>
    </div>
  </div>

</template>

<script setup>
</script>

<style scoped>
.comment-card {
  background: #f8f9fa;
  padding: 15px;
  border-radius: 8px;
}

.comment-images {
  margin-top: 10px;
}

.comment-images img:hover {
  opacity: 0.9;
}
</style>