<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm fixed-top" v-if="shouldShowMainHeader">
    <div class="container">
      <!-- 左側固定區域 -->
      <div class="d-flex align-items-center" style="min-width: 200px;">
        <!-- 側邊欄按鈕 -->
        <button class="btn btn-link nav-link p-0 me-3" @click="toggleSidebar" v-if="!isAdminPage || isAdmin">
          <i class="bi bi-list fs-4"></i>
        </button>

        <!-- Logo -->
        <router-link class="navbar-brand d-flex align-items-center" :to="isAdmin ? '/admin' : '/'">
          <img src="/image/full_blue.png" alt="Journey Logo" class="nav-logo" />
          <span class="journey-text">Journey.com</span>
        </router-link>
      </div>

      <!-- 中間空白區域 -->
      <div class="flex-grow-1"></div>

      <!-- 右側選單 -->
      <ul class="navbar-nav align-items-center">
        <!-- 左側功能按鈕區域 -->
        <li class="nav-item" v-if="!isAdmin && !isVendor">
          <router-link class="nav-link" to="/customer-service" active-class="active">
            <i class="bi bi-headset me-1"></i>
            客服
          </router-link>
        </li>
        <li class="nav-item" v-if="!isAdmin && !isVendor">
          <router-link class="nav-link" to="/point-store" active-class="active">
            <i class="bi bi-shop-window me-1"></i>
            點數商城
          </router-link>
        </li>

        <!-- 收藏和旅程按鈕（已登入用戶） -->
        <li class="nav-item" v-if="isUserLoggedIn && !isAdmin && !isVendor">
          <div class="d-flex gap-2 align-items-center">
            <!-- 收藏按鈕 -->
            <button class="btn btn-outline-secondary btn-sm position-relative" @click="showFavorites" title="我的收藏">
              <i class="bi bi-heart"></i>
              <span v-if="favoritesCount > 0"
                class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                {{ favoritesCount }}
              </span>
            </button>

            <!-- 旅程按鈕 -->
            <button class="btn btn-outline-secondary btn-sm position-relative" @click="showJourney" title="我的旅程">
              <i class="bi bi-bag"></i>
              <span v-if="journeyCount > 0"
                class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-primary">
                {{ journeyCount }}
              </span>
            </button>
          </div>
        </li>

        <!-- 登入/註冊/商家按鈕 -->
        <li class="nav-item" v-else-if="!isAdmin && !isVendor">
          <div class="d-flex gap-2 align-items-center">
            <!-- 收藏按鈕 -->
            <button class="btn btn-outline-secondary btn-sm position-relative" @click="showFavorites" title="我的收藏">
              <i class="bi bi-heart"></i>
              <span v-if="favoritesCount > 0"
                class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                {{ favoritesCount }}
              </span>
            </button>

            <!-- 旅程按鈕 -->
            <button class="btn btn-outline-secondary btn-sm position-relative" @click="showJourney" title="我的旅程">
              <i class="bi bi-bag"></i>
              <span v-if="journeyCount > 0"
                class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-primary">
                {{ journeyCount }}
              </span>
            </button>

            <router-link class="btn btn-outline-primary btn-sm" to="/login">
              <i class="bi bi-box-arrow-in-right me-1"></i> 登入
            </router-link>
            <router-link class="btn btn-primary btn-sm" to="/register">
              <i class="bi bi-person-plus me-1"></i> 註冊
            </router-link>
            <router-link class="btn btn-warning btn-sm" to="/vendor/login">
              <i class="bi bi-shop me-1"></i> 我是商家
            </router-link>
          </div>
        </li>

        <!-- 用戶選單 - 固定在最右邊 -->
        <li class="nav-item dropdown" v-if="isUserLoggedIn">
          <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" id="navbarDropdown" role="button"
            data-bs-toggle="dropdown" aria-expanded="false">
            <div class="user-avatar">
              <img :src="userAvatar || 'https://cdn-icons-png.flaticon.com/512/149/149071.png'" :alt="username" />
            </div>
            <span class="ms-2">{{ username }}</span>
          </a>
          <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
            <!-- 一般用戶選單 -->
            <li v-if="isUser">
              <router-link class="dropdown-item" to="/profile">
                <i class="bi bi-person-circle me-2"></i>
                會員中心
              </router-link>
            </li>
            <li v-if="isUser">
              <router-link class="dropdown-item" :to="{
                path: '/profile',
                query: { activeTab: 'journey' }
              }">
                <i class="bi bi-bag me-2"></i>
                我的旅程
              </router-link>
            </li>
            
            <!-- 管理員選單 -->
            <li v-if="isAdmin">
              <router-link class="dropdown-item" to="/admin">
                <i class="bi bi-gear me-2"></i>
                後台管理
              </router-link>
            </li>
            
            <!-- 商家選單 -->
            <li v-if="isVendor">
              <router-link class="dropdown-item" :to="getVendorBackendUrl()">
                <i class="bi bi-upload me-2"></i>
                資料上傳
              </router-link>
            </li>
            <li v-if="isVendor">
              <router-link class="dropdown-item" :to="getVendorBackendUrl()">
                <i class="bi bi-shop me-2"></i>
                商家後台
              </router-link>
            </li>
            
            <!-- 分隔線 -->
            <li>
              <hr class="dropdown-divider" />
            </li>
            
            <!-- 登出選項 -->
            <li>
              <a class="dropdown-item" href="#" @click="logout">
                <i class="bi bi-box-arrow-right me-2"></i>
                登出
              </a>
            </li>
          </ul>
        </li>
      </ul>
    </div>
  </nav>

  <!-- 只給admin看到的Header -->
  <nav class="navbar navbar-light bg-white shadow-sm fixed-top" v-if="isAdmin">
    <div class="container">
      <!-- 左側固定區域 -->
      <div class="d-flex align-items-center" style="min-width: 200px;">
        <!-- 側邊欄按鈕 -->
        <button class="btn btn-link nav-link p-0 me-3" @click="toggleSidebar">
          <i class="bi bi-list fs-4"></i>
        </button>

        <!-- Logo -->
        <router-link class="navbar-brand d-flex align-items-center" to="/">
          <img src="/image/full_blue.png" alt="Journey Logo" class="nav-logo" />
          <span class="journey-text">Journey.com</span>
        </router-link>
      </div>

      <!-- 中間空白區域 -->
      <div class="flex-grow-1"></div>

      <!-- 右側：歡迎訊息、後台管理按鈕和登出按鈕 -->
      <div class="d-flex align-items-center">
        <span class="fw-bold me-3">歡迎 {{ username }}（系統管理員）</span>
        <router-link class="btn btn-primary btn-sm me-2" to="/admin">
          <i class="bi bi-gear me-1"></i>
          後台管理
        </router-link>
        <button class="btn btn-outline-danger btn-sm" @click="logout">登出</button>
      </div>
    </div>
  </nav>

  <!-- 側邊欄 -->
  <div class="sidebar" :class="{ show: isSidebarOpen }">
    <div class="sidebar-header">
      <h5 class="mb-0">選單</h5>
      <button class="btn-close" @click="toggleSidebar"></button>
    </div>
    <div class="sidebar-content" v-if="!isAdminPage || isAdmin">
      <ul class="nav flex-column">
        <li class="nav-item">
          <router-link class="nav-link" to="/" @click="toggleSidebar">
            <i class="bi bi-house-door me-2"></i>
            首頁
          </router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/flights" @click="toggleSidebar">
            <i class="bi bi-airplane me-2"></i>
            機票
          </router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/hotels" @click="toggleSidebar">
            <i class="bi bi-building me-2"></i>
            飯店
          </router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/tickets" @click="toggleSidebar">
            <i class="bi bi-ticket-perforated me-2"></i>
            門票
          </router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/traffic" @click="toggleSidebar">
            <i class="bi bi-car-front me-2"></i>
            交通
          </router-link>
        </li>
      </ul>
    </div>

    <!-- 一般用戶在Admin頁面時的側邊欄內容 -->
    <div class="sidebar-content" v-if="isAdminPage && !isAdmin">
      <ul class="nav flex-column">
        <li class="nav-item">
          <div class="nav-link text-muted">
            <i class="bi bi-info-circle me-2"></i>
            管理頁面
          </div>
        </li>
      </ul>
    </div>
  </div>

  <!-- 搜尋覆蓋層 -->
  <div v-if="searchVisible" class="search-overlay" @click="hideSearch">
    <div class="search-modal" @click.stop>
      <div class="search-header">
        <h5>搜尋旅遊</h5>
        <button class="btn-close" @click="hideSearch"></button>
      </div>
      <div class="search-content">
        <AdvancedSearch @search="handleSearch" />
      </div>
    </div>
  </div>
</template>

<script setup>
  import { ref, computed, onMounted, onUnmounted } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import AdvancedSearch from './AdvancedSearch.vue';
  import Swal from 'sweetalert2';
  import { usePartnersStore } from '@/stores/partners';
  import { usePlansStore } from '@/stores/plans';
  import { useCurrentUser } from '@/composables/useCurrentUser'

  const router = useRouter();
  const route = useRoute();
  const searchVisible = ref(false);
  const isSidebarOpen = ref(false);

  // 收藏和旅程計數
  const favoritesCount = ref(0);
  const journeyCount = ref(0);

  const {
    currentUser,
    isAdmin,
    isVendor,
    isUser,
    isAdminOrVendor,
    isVendorLoggedIn,
    isHotelVendor,
    isFlightVendor,
    isAttractionVendor,
    isTrafficVendor,
    userAvatar,
    logout: clearUserData,
    isLoggedIn: isUserLoggedIn
  } = useCurrentUser()


  const username = computed(() => {
    return currentUser.value.userName || currentUser.value.vendorName || '';
  });

  // 獲取商家後台 URL
  const getVendorBackendUrl = () => {
    if (isHotelVendor.value) return '/vendor/hotel';
    if (isFlightVendor.value) return '/vendor/flight';
    if (isAttractionVendor.value) return '/vendor/attraction';
    if (isTrafficVendor.value) return '/vendor/traffic';
    return '/vendor/hotel';
  };



  // 檢查是否在Admin頁面
  const isAdminPage = computed(() => {
    return route.path === '/admin';
  });

  // 控制主Header的顯示
  const shouldShowMainHeader = computed(() => {
    // 在商家頁面不顯示主Header
    if (route.path.startsWith('/vendor')) return false;
    // 在管理員頁面不顯示主Header，使用專用的Admin Header
    if (isAdminPage.value && isAdmin.value) return false;
    // 一般用戶頁面顯示
    return true;
  });

  // 計算收藏和旅程數量
  const updateCounts = () => {
    try {
      const favorites = JSON.parse(localStorage.getItem('favorites') || '[]');
      const journey = JSON.parse(localStorage.getItem('journey') || '[]');

      favoritesCount.value = favorites.length;
      journeyCount.value = journey.reduce((total, item) => total + (item.quantity || 1), 0);
    } catch (error) {
      console.error('Error parsing localStorage data:', error);
      favoritesCount.value = 0;
      journeyCount.value = 0;
    }
  };

  // 監聽 localStorage 變化
  const handleStorageChange = () => {
    updateCounts();
  };

  // 側邊欄控制
  const toggleSidebar = () => {
    isSidebarOpen.value = !isSidebarOpen.value;
    if (isSidebarOpen.value) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = 'auto';
    }
  };

  // 搜尋控制
  const showSearch = () => {
    searchVisible.value = true;
    document.body.style.overflow = 'hidden';
  };

  const hideSearch = () => {
    searchVisible.value = false;
    document.body.style.overflow = 'auto';
  };

  const handleSearch = (searchData) => {
    hideSearch();
    router.push({
      path: '/search',
      query: searchData
    });
  };

  // 顯示收藏
  const showFavorites = () => {
    if (isUserLoggedIn.value) {
      router.push('/my-journey?tab=favorites');
    } else {
      // 未登入時顯示收藏列表
      showFavoritesModal();
    }
  };

  // redirect to log in page
  const goToLogin = () => {
    Swal.fire({
      position: 'top',
      text: '您尚未登入，將導向至登入頁面',
      showConfirmButton: true,
      confirmButtonText: '確認',
      confirmButtonColor: '#47a8d1',
      showCancelButton: true,
      cancelButtonText: '取消',
      cancelButtonColor: '#d1476a',
      allowEscapeKey: true,
      allowOutsideClick: false,

      didOpen: () => {
        const cbtn = Swal.getConfirmButton();
        cbtn.style.fontWeight = 'bold';

        const xbtn = Swal.getCancelButton();
        xbtn.style.fontWeight = 'bold';
      }
    }).then((result) => {
      if (result.isConfirmed) {
        router.push('/login');
      }
    })
  };

  // 顯示旅程
  const showJourney = () => {
    if (isUserLoggedIn.value) {
      // showJourneyModal();
      router.push({
        path: '/profile',
        query: { activeTab: 'journey' }
      });
    } else {
      // redirect to login page when not logged in
      goToLogin();
    }
  };

  // 顯示收藏模態框
  const showFavoritesModal = () => {
    const favorites = JSON.parse(localStorage.getItem('favorites') || '[]');
    if (favorites.length === 0) {
      Swal.fire({
        position: 'top',
        text: '您還沒有收藏任何商品',
        showConfirmButton: true,
        confirmButtonText: '確認',
        confirmButtonColor: '#47a8d1',
        allowEscapeKey: true,
        allowOutsideClick: false,

        didOpen: () => {
          const cbtn = Swal.getConfirmButton();
          cbtn.style.fontWeight = 'bold';
        }
      });
      return;
    }

    // 這裡可以實現一個模態框來顯示收藏列表
    // 暫時使用簡單的 alert
    alert(`您收藏了 ${favorites.length} 個商品\n\n登入後可以同步到您的帳戶`);
  };

  // 顯示旅程模態框
  const showJourneyModal = () => {
    const journey = JSON.parse(localStorage.getItem('journey') || '[]');
    if (journey.length === 0) {
      Swal.fire({
        position: 'top',
        text: '您的旅程還是空的，快來添加一些商品吧！',
        showConfirmButton: true,
        confirmButtonText: '創建旅程',
        confirmButtonColor: '#47a8d1',
        showCancelButton: true,
        cancelButtonText: '取消',
        cancelButtonColor: '#d1476a',
        allowEscapeKey: true,
        allowOutsideClick: false,

        didOpen: () => {
          const cbtn = Swal.getConfirmButton();
          cbtn.style.fontWeight = 'bold';

          const xbtn = Swal.getCancelButton();
          xbtn.style.fontWeight = 'bold';
        }
      }).then((result) => {
        if (result.isConfirmed) {
          router.push('/my-journey');
        }
      });
      return;
    }

    // 這裡可以實現一個模態框來顯示旅程列表
    // 暫時使用簡單的 alert
    const totalItems = journey.reduce((total, item) => total + (item.quantity || 1), 0);
    const totalPrice = journey.reduce(
      (total, item) => total + item.price * (item.quantity || 1),
      0
    );

    alert(
      `您的旅程中有 ${totalItems} 個商品\n總計：NT$ ${totalPrice.toLocaleString()}\n\n登入後可以同步到您的帳戶`
    );
  };

  const partnerStore = usePartnersStore();
  const planStore = usePlansStore();
  // 登出處理
  const logout = () => {
    Swal.fire({
      position: 'center',
      text: '確定要登出嗎？',
      showConfirmButton: true,
      confirmButtonText: '是',
      confirmButtonColor: '#47a8d1',
      showCancelButton: true,
      cancelButtonText: '否',
      cancelButtonColor: '#d1476a',
      allowEscapeKey: true,
      allowOutsideClick: false,

      didOpen: () => {
        const cbtn = Swal.getConfirmButton();
        cbtn.style.fontWeight = 'bold';

        const xbtn = Swal.getCancelButton();
        xbtn.style.fontWeight = 'bold';
      }
    }).then((result) => {
      if (result.isConfirmed) {
        // 使用統一的登出函數清理所有 localStorage
        clearUserData();

        // 重置 store 狀態
        partnerStore.loaded = false;
        planStore.loaded = false;

        // 導向首頁
        if (route.path === '/home') {
          location.reload();
        } else {
          router.push('/');
        }
      }
    });
  };

  // 組件掛載時初始化計數
  onMounted(() => {
    updateCounts();

    // 監聽 localStorage 變化
    window.addEventListener('storage', handleStorageChange);

    // 監聽自定義事件（當其他組件更新 localStorage 時）
    window.addEventListener('favoritesUpdated', updateCounts);
    window.addEventListener('journeyUpdated', updateCounts);
  });

  // 組件卸載時清理事件監聽器
  const cleanup = () => {
    window.removeEventListener('storage', handleStorageChange);
    window.removeEventListener('favoritesUpdated', updateCounts);
    window.removeEventListener('journeyUpdated', updateCounts);
  };

  onUnmounted(() => {
    cleanup();
  });
</script>

<style scoped>
  .navbar {
    height: 64px;
    padding: 0;
  }

  .nav-logo {
    height: 35px;
    width: auto;
    object-fit: contain;
    margin-right: 8px;
  }

  .journey-text {
    color: #0d6efd;
    font-size: 1.2rem;
    font-weight: 600;
    letter-spacing: 0.5px;
  }

  /* 用戶頭像樣式 */
  .user-avatar {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #f8f9fa;
    border: 1px solid #dee2e6;
  }

  .user-avatar img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .nav-link.dropdown-toggle {
    padding: 0.5rem 0.75rem;
    border-radius: 8px;
    transition: background-color 0.2s ease;
  }

  .nav-link.dropdown-toggle:hover {
    background-color: #f8f9fa;
  }

  .dropdown-menu {
    border: none;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
  }

  .dropdown-item {
    padding: 0.5rem 1rem;
    color: #333;
    transition: all 0.2s ease;
  }

  .dropdown-item:hover {
    background-color: #f8f9fa;
    color: #0d6efd;
  }

  .dropdown-item i {
    color: #666;
  }

  .dropdown-item:hover i {
    color: #0d6efd;
  }

  /* 搜尋框樣式 */
  .search-bar-container {
    max-width: 500px;
  }

  .search-bar {
    display: flex;
    align-items: center;
    padding: 8px 16px;
    background-color: #f5f5f5;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.3s ease;
  }

  .search-bar:hover {
    background-color: #eeeeee;
  }

  .search-bar i {
    color: #666;
    margin-right: 8px;
  }

  .search-placeholder {
    color: #666;
    font-size: 0.9rem;
  }

  /* 側邊欄樣式 */
  .sidebar {
    position: fixed;
    top: 0;
    left: -280px;
    width: 280px;
    height: 100vh;
    background-color: white;
    box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
    z-index: 1050;
    transition: left 0.3s ease;
    overflow-y: auto;
  }

  .sidebar.show {
    left: 0;
  }

  .sidebar-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 1rem;
    border-bottom: 1px solid #eee;
  }

  .sidebar-content {
    padding: 1rem 0;
  }

  .sidebar .nav-link {
    padding: 0.75rem 1.5rem;
    color: #333;
    transition: all 0.3s ease;
  }

  .sidebar .nav-link:hover {
    background-color: #f8f9fa;
    color: #0d6efd;
  }

  .sidebar .nav-link.active {
    background-color: #e7f1ff;
    color: #0d6efd;
  }

  /* 搜尋覆蓋層樣式 */
  .search-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 1049;
    display: flex;
    align-items: flex-start;
    justify-content: center;
    padding-top: 80px;
  }

  .search-modal {
    width: 100%;
    max-width: 800px;
    background: white;
    border-radius: 12px;
    overflow: hidden;
  }

  .search-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 1rem;
    border-bottom: 1px solid #eee;
  }

  .search-content {
    padding: 1.5rem;
  }

  /* 響應式調整 */
  @media (max-width: 768px) {
    .search-bar-container {
      display: none;
    }

    .navbar-brand {
      margin-right: auto;
    }
  }

  /* 收藏和旅程按鈕樣式 */
  .btn-outline-secondary {
    border-color: #dee2e6;
    color: #6c757d;
    transition: all 0.2s ease;
  }

  .btn-outline-secondary:hover {
    background-color: #f8f9fa;
    border-color: #adb5bd;
    color: #495057;
  }

  .btn-outline-secondary:focus {
    box-shadow: 0 0 0 0.2rem rgba(108, 117, 125, 0.25);
  }

  /* 徽章樣式 */
  .badge {
    font-size: 0.75rem;
    padding: 0.25rem 0.5rem;
  }

  /* 按鈕圖標樣式 */
  .btn i {
    font-size: 1rem;
  }

  /* 按鈕間距 */
  .nav-item .d-flex.gap-2 {
    gap: 0.5rem !important;
  }
</style>