<template>

  <div id="homeBanner" class="carousel slide" data-bs-ride="carousel">
    <div class="carousel-indicators">
      <button
        type="button"
        data-bs-target="#homeBanner"
        data-bs-slide-to="0"
        class="active"
      ></button>
      <button
        type="button"
        data-bs-target="#homeBanner"
        data-bs-slide-to="1"
      ></button>
      <button
        type="button"
        data-bs-target="#homeBanner"
        data-bs-slide-to="2"
      ></button>
    </div>
    <div class="carousel-inner">
      <div class="carousel-item active">
        <img
          src="https://static.gltjp.com/glt/data/article/21000/20205/20230703_160551_f036a4c1_w1920.webp"
          class="d-block w-100"
          alt="旅遊景點1"
        />
        <div class="carousel-caption">
          <h2>探索世界的美好</h2>
          <p>讓我們帶您開啟精彩的旅程</p>
        </div>
      </div>
      <div class="carousel-item">
        <img
          src="https://plus.unsplash.com/premium_photo-1661964177687-57387c2cbd14?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8amFwYW58ZW58MHx8MHx8fDA%3D"
          class="d-block w-100"
          alt="旅遊景點2"
        />
        <div class="carousel-caption">
          <h2>精選住宿體驗</h2>
          <p>享受舒適的住宿環境</p>
        </div>
      </div>
      <div class="carousel-item">
        <img
          src="https://images.unsplash.com/photo-1528164344705-47542687000d?q=80&w=2384&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
          class="d-block w-100"
          alt="旅遊景點3"
        />
        <div class="carousel-caption">
          <h2>特色景點門票</h2>
          <p>暢遊各地知名景點</p>
        </div>
      </div>
    </div>
    <button
      class="carousel-control-prev"
      type="button"
      data-bs-target="#homeBanner"
      data-bs-slide="prev"
    >
      <span class="carousel-control-prev-icon"></span>
    </button>
    <button
      class="carousel-control-next"
      type="button"
      data-bs-target="#homeBanner"
      data-bs-slide="next"
    >
      <span class="carousel-control-next-icon"></span>
    </button>
  </div>

</template>

<script setup>
</script>

<style scoped>
.carousel-item {
  height: 500px;
}
.carousel-item img {
  object-fit: cover;
  height: 100%;
}
.carousel-caption {
  background: rgba(0, 0, 0, 0.5);
  padding: 20px;
  border-radius: 10px;
}
</style>