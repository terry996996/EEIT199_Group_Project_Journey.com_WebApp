<!--
  景點商家管理中心
  
  @author Max
  @since 2025-07-08
-->
<template>
  <div class="vendor-info-section">
    <!-- 商家 Header -->
    <div class="vendor-header">
      <div class="container">
        <div class="vendor-header-content">
          <div class="vendor-info">
            <div class="vendor-icon">
              <i class="bi bi-geo-alt"></i>
            </div>
            <div class="vendor-details">
              <h1 class="vendor-title">景點商家管理中心</h1>
              <p class="vendor-subtitle">
                歡迎回來，<span class="vendor-name-highlight">{{
                  vendorName || vendorTypeDisplay
                }}</span>
              </p>
            </div>
          </div>
          <div class="vendor-actions">
            <router-link to="/" class="btn btn-outline-light me-2">
              <i class="bi bi-house-door me-2"></i>
              返回首頁
            </router-link>
            <button @click="handleLogout" class="btn btn-outline-danger">
              <i class="bi bi-box-arrow-right me-2"></i>
              登出
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="container">
      <VendorUpload vendorType="attraction" :vendorId="vendorId" />
    </div>
  </div>
</template>

<script setup>
  import { ref, computed, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  import VendorUpload from '../../components/vendor/VendorUpload.vue';
  import { useCurrentUser } from '@/composables/useCurrentUser';

  const router = useRouter();
  
  // 使用統一的身分判定
  const { logout } = useCurrentUser();
  
  const vendorId = ref(4);
  const vendorName = ref(localStorage.getItem('vendorName') || '');

  const vendorTypeDisplay = computed(() => {
    return '景點商家';
  });

  const handleLogout = () => {
    // 使用 composable 中的統一登出函數
    logout();
    router.push('/vendor/login');
  };

  onMounted(() => {
    // 檢查是否已登入
    const isLoggedIn = localStorage.getItem('isVendorLoggedIn');
    const vendorType = localStorage.getItem('vendorType');

    if (!isLoggedIn || vendorType !== 'attraction') {
      router.push('/vendor/login');
    }
    vendorName.value = localStorage.getItem('vendorName') || '';
  });
</script>

<style scoped>
  .vendor-info-section {
    min-height: 100vh;
    background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  }

  .vendor-header {
    background: linear-gradient(135deg, #0080ff 0%, #005bbd 100%);
    color: white;
    padding: 20px 0;
    margin-bottom: 30px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  .vendor-header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-top: 64px;
  }

  .vendor-info {
    display: flex;
    align-items: center;
    gap: 20px;
  }

  .vendor-icon {
    width: 60px;
    height: 60px;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    backdrop-filter: blur(10px);
  }

  .vendor-icon i {
    font-size: 28px;
    color: white;
  }

  .vendor-title {
    font-size: 28px;
    font-weight: 700;
    margin: 0;
    margin-bottom: 4px;
  }

  .vendor-subtitle {
    color: #ffffff;
    font-size: 16px;
    margin: 0;
    opacity: 0.9;
  }

  .vendor-name-highlight {
    font-weight: 700;
    color: #ffffff;
  }

  .vendor-actions {
    display: flex;
    gap: 12px;
  }

  .btn-outline-danger {
    border: 2px solid rgba(255, 255, 255, 0.3);
    color: white;
    background: transparent;
    transition: all 0.3s ease;
  }

  .btn-outline-danger:hover {
    background: rgba(220, 53, 69, 0.9);
    border-color: rgba(220, 53, 69, 0.9);
    color: white;
    transform: translateY(-2px);
  }

  .container {
    padding-top: 0;
  }

  @media (max-width: 768px) {
    .vendor-header-content {
      flex-direction: column;
      gap: 20px;
      text-align: center;
    }

    .vendor-title {
      font-size: 24px;
    }
  }
</style>
