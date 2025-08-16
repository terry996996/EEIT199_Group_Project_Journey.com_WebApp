<template>
  <div class="filght-page">
    <!-- Header -->
    <Header />

    <!-- 主要內容 -->
    <main class="main-content">
      <!-- 英雄區域 -->
      <section class="hero-section">
        <div class="hero-background">
          <div class="hero-overlay"></div>
        </div>
        <div class="container">
          <div class="hero-content">
            <h1 class="hero-title">
              探索世界的美好
              <span class="text-primary">從這裡開始</span>
            </h1>
            <p class="hero-subtitle">發現最優惠的機票、飯店、門票和交通，讓您的旅程更加精彩</p>
          </div>

          <!-- 進階搜尋 -->
          <div class="container ">
            <nav>
              <div class="nav nav-tabs bg-white " id="nav-tab" role="tablist">
                <button class="nav-link active" id="nav-filght-tab" data-bs-toggle="tab" data-bs-target="#nav-filght"
                  type="button" role="tab" aria-controls="nav-filght" aria-selected="true">
                  機票
                </button>
                <button class="nav-link" id="nav-hotel-tab" data-bs-toggle="tab" data-bs-target="#nav-hotel"
                  type="button" role="tab" aria-controls="nav-hotel" aria-selected="false">
                  飯店
                </button>
                <button class="nav-link" id="nav-ticket-tab" data-bs-toggle="tab" data-bs-target="#nav-ticket"
                  type="button" role="tab" aria-controls="nav-ticket" aria-selected="false">
                  門票
                </button>
                <button class="nav-link" id="nav-traffic-tab" data-bs-toggle="tab" data-bs-target="#nav-traffic"
                  type="button" role="tab" aria-controls="nav-traffic" aria-selected="false">
                  交通
                </button>
              </div>
            </nav>
            <div class="tab-content bg-white rounded-bottom-4 " id="nav-tabContent">
              <div class="tab-pane fade show active" id="nav-filght" role="tabpanel" aria-labelledby="nav-filght-tab"
                tabindex="0">
                <FlightsSearch :form="searchForm" @search="updateFormAndSearch" />
              </div>
              <div class="tab-pane fade" id="nav-hotel" role="tabpanel" aria-labelledby="nav-hotel-tab" tabindex="0">
                <HotelsSearchBar />
              </div>
              <!-- 景點搜尋 -->
              <div class="tab-pane fade" id="nav-ticket" role="tabpanel" aria-labelledby="nav-ticket-tab" tabindex="0">
                <SearchBarAttraction />
              </div>
              <div class="tab-pane fade" id="nav-traffic" role="tabpanel" aria-labelledby="nav-traffic-tab"
                tabindex="0">
                <TrafficSearchForm />
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 推薦系統 -->
      <section class="recommendations-section">
        <div class="container">
          <RecommendationSystem />
        </div>
      </section>

      <!-- 特色服務 -->
      <section class="features-section">
        <div class="container">
          <div class="section-header text-center">
            <h2 class="section-title">為什麼選擇 Journey.com？</h2>
            <p class="section-subtitle">我們提供最優質的旅遊服務體驗</p>
          </div>

          <div class="features-grid">
            <div class="feature-card">
              <div class="feature-icon">
                <i class="bi bi-shield-check"></i>
              </div>
              <h4>安全可靠</h4>
              <p>所有交易都經過 SSL 加密保護，確保您的資料安全</p>
            </div>

            <div class="feature-card">
              <div class="feature-icon">
                <i class="bi bi-currency-exchange"></i>
              </div>
              <h4>最優價格</h4>
              <p>我們與全球合作夥伴合作，為您提供最優惠的價格</p>
            </div>

            <div class="feature-card">
              <div class="feature-icon">
                <i class="bi bi-headset"></i>
              </div>
              <h4>線上客服和專人諮詢</h4>
              <p>客服聊天室與多種意見回覆管道，隨時為您解決問題</p>
            </div>

            <div class="feature-card">
              <div class="feature-icon">
                <i class="bi bi-award"></i>
              </div>
              <h4>品質保證</h4>
              <p>嚴選合作夥伴，確保每個產品和服務的品質</p>
            </div>
          </div>
        </div>
      </section>

      <!-- 熱門目的地 -->
      <!-- <section class="destinations-section">
        <div class="container">
          <div class="section-header text-center">
            <h2 class="section-title">熱門目的地</h2>
            <p class="section-subtitle">探索最受歡迎的旅遊景點</p>
          </div>

          <div class="destinations-grid">
            <div v-for="destination in popularDestinations" :key="destination.id" class="destination-card"
              @click="goToDestination(destination)">
              <div class="destination-image">
                <img :src="destination.image" :alt="destination.name" />
                <div class="destination-overlay">
                  <div class="destination-info">
                    <h4>{{ destination.name }}</h4>
                    <p>{{ destination.description }}</p>
                    <span class="price">從 ${{ destination.price }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section> -->

      <!-- 用戶評價 -->
      <section class="testimonials-section">
        <div class="container">
          <div class="section-header text-center">
            <h2 class="section-title">用戶評價</h2>
            <p class="section-subtitle">聽聽其他旅客的心得</p>
          </div>

          <div class="testimonials-grid">
            <div v-for="testimonial in testimonials" :key="testimonial.id" class="testimonial-card">
              <div class="testimonial-content">
                <div class="stars">
                  <i v-for="n in 5" :key="n" class="bi"
                    :class="n <= testimonial.rating ? 'bi-star-fill' : 'bi-star'"></i>
                </div>
                <p class="testimonial-text">{{ testimonial.text }}</p>
                <div class="testimonial-author">
                  <img :src="testimonial.avatar" :alt="testimonial.name" />
                  <div>
                    <h5>{{ testimonial.name }}</h5>
                    <span>{{ testimonial.location }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
  import { ref } from 'vue';
  import { useRouter } from 'vue-router';
  import Header from '../components/Header.vue';
  import FlightsSearch from '@/components/flights/FlightsSearch.vue';
  import HotelsSearchBar from '@/components/lodging/HotelsSearchBar.vue';
  import SearchBarAttraction from '@/components/attractions/SearchBarAttraction.vue';
  import TrafficSearchForm from '@/components/traffic/TrafficSearchForm.vue';
  import RecommendationSystem from '@/components/RecommendationSystem.vue';

  const router = useRouter();
  // =============
  const handleSearchFromHome = (dto) => {
    router.push({
      path: '/tickets',
      query: {
        keyword: dto.keyword || '',
        countryId: dto.countryId || '',
        tagIds: dto.tagIds?.join(',') || '',
        typeIds: dto.typeIds?.join(',') || ''
      }
    });
  };

  // 點擊目的地卡片的處理函數
  const goToDestination = (destination) => {
    // 這裡可以根據需要導航到相關頁面
    console.log('前往目的地:', destination.name);
    // 例如：router.push(`/destination/${destination.id}`);
  };
  // =============
  // 導入圖片
  import taipei101Img from '@/assets/images/taipei-101.jpg';
  import jiufenImg from '@/assets/images/九分.jpg';
  import sunmoonlakeImg from '@/assets/images/日月潭.jpg';
  import alishanImg from '@/assets/images/阿里山.jpg';

  // 熱門目的地
  const popularDestinations = ref([
    {
      id: 1,
      name: '台北101',
      description: '台北地標，現代建築的典範',
      image: './assets/images/taipei-101.jpg',
      price: 899
    },
    {
      id: 2,
      name: '九份',
      description: '山城風光，懷舊老街之美',
      image: jiufenImg,
      price: 1299
    },
    {
      id: 3,
      name: '日月潭',
      description: '湖光山色，台灣最美麗的湖泊',
      image: sunmoonlakeImg,
      price: 1499
    },
    {
      id: 4,
      name: '阿里山',
      description: '日出雲海，神木群的故鄉',
      image: alishanImg,
      price: 1199
    }
  ]);

  // 用戶評價
  const testimonials = ref([
    {
      id: 1,
      name: '張小明',
      location: '台北',
      rating: 5,
      text: 'Journey.com 的服務真的很棒！機票價格比別家便宜很多，客服也很專業。',
      avatar: 'https://i.pravatar.cc/150?u=8'
    },
    {
      id: 2,
      name: '李小華',
      location: '高雄',
      rating: 5,
      text: '第一次出國就選擇 Journey.com，整個過程都很順利，推薦給大家！',
      avatar: 'https://i.pravatar.cc/150?u=2'
    },
    {
      id: 3,
      name: '王大明',
      location: '台中',
      rating: 4,
      text: '飯店的住宿品質很好，價格經濟實惠，下次還會繼續使用。',
      avatar: 'https://i.pravatar.cc/150?u=5'
    }
  ]);

</script>

<style scoped>
  .home-page {
    min-height: 100vh;
  }

  .main-content {
    padding-top: 76px;
    /* header 的高度 + 一些額外空間 */
  }

  /* 英雄區域 */
  .hero-section {
    position: relative;
    min-height: calc(100vh - 200px);
    /* 從 76px 改為 200px，減少約 124px */
    background: linear-gradient(135deg, #4285f4 0%, #2962ff 100%);
    /* 調整為更亮的藍色 */
    display: flex;
    align-items: center;
    padding: 3rem 0;
    /* 從 4rem 減少到 3rem */
  }

  .hero-background {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: -1;
  }

  .hero-overlay {
    display: none;
    /* 暫時隱藏覆蓋層，因為現在是純色背景 */
  }

  .hero-content {
    /* 不要設 color: #fff; 只在標題、副標題加 class */
    text-align: center;
    max-width: 800px;
    margin: 0 auto;
    padding: 2rem;
  }

  .hero-title,
  .hero-subtitle {
    color: #fff;
  }

  .hero-title .text-primary {
    color: #ffffff !important;
    /* 淺藍色 */
    font-weight: 900;
  }

  .hero-subtitle {
    font-size: 1.5rem;
    margin-bottom: 2.5rem;
    opacity: 0.95;
    color: #e3f2fd;
    font-weight: 500;
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
    line-height: 1.6;
  }

  .search-section {
    background: rgba(255, 255, 255, 0.98);
    /* border-radius: 1rem; */
    padding: 2rem;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  }

  /* 強力覆蓋 Bootstrap 的 padding */
  .search-section .tab-content,
  .search-section #nav-tabContent {
    padding: 0.25rem !important;
    /* 從 32px 減少到 4px */
  }

  .search-section .tab-pane {
    padding: 0.125rem !important;
    /* 進一步減少 */
  }

  /* 確保所有相關元素都被覆蓋 */
  .search-section .tab-content * {
    margin-bottom: 0.25rem !important;
  }

  .search-section .tab-content *:last-child {
    margin-bottom: 0 !important;
  }

  /* 推薦系統區域 */
  .recommendations-section {
    padding: var(--spacing-xxl) 0;
    background: var(--bg-secondary);
  }

  /* 特色服務區域 */
  .features-section {
    padding: var(--spacing-xxl) 0;
    background: var(--bg-primary);
  }

  .section-header {
    margin-bottom: var(--spacing-xxl);
  }

  .section-title {
    font-size: 2.5rem;
    font-weight: var(--font-weight-bold);
    color: var(--text-primary);
    margin-bottom: var(--spacing-md);
  }

  .section-subtitle {
    font-size: var(--font-size-lg);
    color: var(--text-secondary);
    margin: 0;
  }

  .features-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: var(--spacing-xl);
  }

  .feature-card {
    text-align: center;
    padding: var(--spacing-xl);
    background: var(--bg-primary);
    border-radius: var(--radius-lg);
    box-shadow: var(--shadow-sm);
    transition: all var(--transition-normal);
  }

  .feature-card:hover {
    transform: translateY(-5px);
    box-shadow: var(--shadow-lg);
  }

  .feature-icon {
    width: 80px;
    height: 80px;
    background: var(--primary-light);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto var(--spacing-lg);
    font-size: 2rem;
    color: var(--primary-color);
    transition: all var(--transition-normal);
  }

  .feature-card:hover .feature-icon {
    background: var(--primary-color);
    color: var(--white);
    transform: scale(1.1);
  }

  .feature-card h4 {
    font-weight: var(--font-weight-semibold);
    margin-bottom: var(--spacing-md);
    color: var(--text-primary);
  }

  .feature-card p {
    color: var(--text-secondary);
    margin: 0;
  }

  /* 目的地區域 */
  .destinations-section {
    padding: var(--spacing-xxl) 0;
    background: var(--bg-secondary);
  }

  .destinations-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: var(--spacing-lg);
  }

  .destination-card {
    background: var(--bg-primary);
    border-radius: var(--radius-lg);
    overflow: hidden;
    box-shadow: var(--shadow-sm);
    transition: all var(--transition-normal);
    cursor: pointer;
  }

  .destination-card:hover {
    transform: translateY(-5px);
    box-shadow: var(--shadow-xl);
  }

  .destination-image {
    position: relative;
    height: 250px;
    overflow: hidden;
  }

  .destination-image img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform var(--transition-normal);
  }

  .destination-card:hover .destination-image img {
    transform: scale(1.1);
  }

  .destination-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(to bottom, transparent 0%, rgba(0, 0, 0, 0.7) 100%);
    display: flex;
    align-items: flex-end;
    padding: var(--spacing-lg);
  }

  .destination-info {
    color: var(--white);
  }

  .destination-info h4 {
    font-size: var(--font-size-xl);
    font-weight: var(--font-weight-bold);
    margin-bottom: var(--spacing-sm);
  }

  .destination-info p {
    margin-bottom: var(--spacing-sm);
    opacity: 0.9;
  }

  .price {
    font-size: var(--font-size-lg);
    font-weight: var(--font-weight-semibold);
    color: var(--primary-color);
    background: var(--white);
    padding: var(--spacing-xs) var(--spacing-sm);
    border-radius: var(--radius-pill);
  }

  /* 評價區域 */
  .testimonials-section {
    padding: var(--spacing-xxl) 0;
    background: var(--bg-secondary);
  }

  .testimonials-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: var(--spacing-lg);
  }

  .testimonial-card {
    background: var(--bg-primary);
    border-radius: var(--radius-lg);
    padding: var(--spacing-xl);
    box-shadow: var(--shadow-sm);
    transition: all var(--transition-normal);
  }

  .testimonial-card:hover {
    transform: translateY(-2px);
    box-shadow: var(--shadow-md);
  }

  .stars {
    margin-bottom: var(--spacing-md);
  }

  .stars .bi-star-fill {
    color: #ffc107;
  }

  .stars .bi-star {
    color: var(--gray-300);
  }

  .testimonial-text {
    font-style: italic;
    color: var(--text-secondary);
    margin-bottom: var(--spacing-lg);
    line-height: var(--line-height-relaxed);
  }

  .testimonial-author {
    display: flex;
    align-items: center;
    gap: var(--spacing-md);
  }

  .testimonial-author img {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    object-fit: cover;
  }

  .testimonial-author h5 {
    margin: 0 0 var(--spacing-xs) 0;
    font-weight: var(--font-weight-semibold);
    color: var(--text-primary);
  }

  .testimonial-author span {
    font-size: var(--font-size-sm);
    color: var(--text-secondary);
  }

  @media (max-width: 768px) {
    .hero-title {
      font-size: 2.75rem;
    }

    .hero-subtitle {
      font-size: 1.25rem;
      margin-bottom: 2rem;
    }

    .section-title {
      font-size: 2rem;
    }

    .features-grid {
      grid-template-columns: 1fr;
    }

    .destinations-grid {
      grid-template-columns: 1fr;
    }

    .testimonials-grid {
      grid-template-columns: 1fr;
    }

    .search-section {
      padding: 1.5rem;
    }
  }

  .customer-service-btn {
    position: fixed;
    bottom: 20px;
    right: 20px;
    width: 60px;
    height: 60px;
    box-sizing: border-box;
    /* 確保尺寸包含 padding 和 border */
    padding: 0;
    /* 移除任何預設填充 */
    border-radius: 50%;
    background-color: #6c5ce7;
    color: white;
    border: none;
    font-size: 35px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
    cursor: pointer;
    z-index: 9999;
    transition: background-color 0.3s ease, transform 0.3s ease, box-shadow 0.3s ease;
  }

  .customer-service-btn:hover {
    background-color: #5f3dc4;
    transform: translateY(-2px);
  }

  .tab-content {
    min-height: 250px;
    /* 改回原本的 400px */
    /* 根據你表單內容高度自行調整，例如最大為 360px 就設 400px */
    transition: min-height 0.3s ease;
    padding: 2rem;
    /* 改回原本的 2rem */
  }

  /* 強制移除卡片外觀效果 */
  #nav-filght .card,
  #nav-filght .shadow,
  #nav-filght .border,
  #nav-filght .rounded,
  #nav-hotel .card,
  #nav-hotel .shadow,
  #nav-hotel .border,
  #nav-hotel .rounded,
  #nav-ticket .card,
  #nav-ticket .shadow,
  #nav-ticket .border,
  #nav-ticket .rounded,
  #nav-traffic .card,
  #nav-traffic .shadow,
  #nav-traffic .border,
  #nav-traffic .rounded {
    box-shadow: none !important;
    border: none !important;
    border-radius: 0 !important;
    background-color: transparent !important;
  }
</style>
