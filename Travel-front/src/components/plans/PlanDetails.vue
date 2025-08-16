<template>
  <div class="plan-detail">
    <!-- Header -->
    <Header />

    <div class="container my-5 pt-4">
      <div class="col-md-12 d-flex justify-content-between align-items-center">
        <div class="mb-2 mt-1 link-secondary" @click="goBackToPlan" style="cursor: pointer;">
          <i class="bi bi-arrow-left-circle me-2"></i>
          <span style="text-decoration: underline;">返回我的旅程</span>
        </div>
        <div class="mb-2 mt-2" style="font-size: 24px;">{{ title }}</div>
      </div>
      <div class="row">
        <!-- 左側導航 -->
        <div class="col-md-3">
          <div class="profile-sidebar">
            <!-- 導航選單 -->
            <div class="nav-menu">
              <div v-for="item in navItems" :key="item.id" class="nav-item" :class="{ active: activeTab === item.id }"
                @click="activeTab = item.id">
                <i :class="item.icon"></i>
                <span>{{ item.title }}</span>
                <span v-if="item.badge" class="badge">{{ item.badge }}</span>
              </div>
            </div>
          </div>
        </div>
        <!-- 右側內容 -->
        <div class="col-md-9">
          <div class="profile-content">

            <div v-if="activeTab === 'orders'" class="content-section">
              <div class="row">
                <div class="section-header position-relative">
                  <h4>訂單資訊</h4>
                  <p class="text-muted">檢視本旅程的各項訂單</p>
                </div>
                <div class="accordion mb-4" id="userAccordion">
                  <h5 class="section-header" style="padding-bottom: 5px; margin-bottom: 10px;">機票</h5>
                  <div class="card mb-2">
                    <h2 class="mb-0">
                      <div class="text-start w-100 p-3 plan-container" @click.stop="">
                        <div class="d-flex justify-content-between align-items-center">
                          <h6>一些訂單</h6>
                        </div>
                      </div>
                    </h2>
                  </div>
                </div>

                <div class="accordion mb-4" id="userAccordion">
                  <h5 class="section-header" style="padding-bottom: 5px; margin-bottom: 10px;">住宿</h5>
                  <div class="card mb-2">
                    <h2 class="mb-0">
                      <div class="text-start w-100 p-3 plan-container" @click.stop="">
                        <!-- <div class="d-flex justify-content-between align-items-center"> -->
                        <!-- <h6>一些訂單</h6> -->
                        <!-- </div> -->
                        <div class="card mb-3 order-card">

                        </div>
                      </div>
                    </h2>
                  </div>
                </div>

                <div class="accordion mb-4" id="userAccordion">
                  <h5 class="section-header" style="padding-bottom: 5px; margin-bottom: 10px;">票券</h5>
                  <div class="card mb-2">
                    <h2 class="mb-0">
                      <div class="text-start w-100 p-3 plan-container" @click.stop="">
                        <div class="d-flex justify-content-between align-items-center">
                          <h6>一些訂單</h6>
                        </div>
                      </div>
                    </h2>
                  </div>
                </div>
              </div>
            </div>

          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { ref, computed, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  import Header from '../Header.vue';
  import { usePlansStore } from '@/stores/plans';
  const planStore = usePlansStore();
  const router = useRouter();
  const activeTab = ref('orders');
  const order = ref({});
  const plans = computed(() => planStore.plans);

  // 導航選單
  const navItems = ref([
    { id: 'orders', title: '訂單資訊', icon: 'bi bi-receipt' },
    { id: 'activity', title: '行程規劃', icon: 'bi bi-geo-alt' },
  ]);

  const goBackToPlan = () => {
    router.push({
      path: '/profile',
      query: { activeTab: 'journey' }
    });
  }

  const title = computed(() => {
    return planStore.plans.find(plan => plan.id === planStore.nowAtIndex).title;
  })


  onMounted(() => {
    // order.value = localStorage.getItem('bookingOrder');
  })
</script>

<style scoped>
  .profile-sidebar {
    position: sticky;
    top: 2rem;
  }

  /* 導航選單 */
  .nav-menu {
    background: white;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  }

  .nav-item {
    display: flex;
    align-items: center;
    padding: 1rem 1.5rem;
    cursor: pointer;
    transition: all 0.2s ease;
    border-bottom: 1px solid #f8f9fa;
    position: relative;
  }

  .nav-item:last-child {
    border-bottom: none;
  }

  .nav-item:hover {
    background-color: #f8f9fa;
  }

  .nav-item.active {
    background-color: #e3f2fd;
    color: #007bff;
    border-left: 4px solid #007bff;
  }

  .nav-item i {
    margin-right: 0.75rem;
    font-size: 1.1rem;
  }

  .nav-item .badge {
    margin-left: auto;
    background-color: #dc3545;
    color: white;
    font-size: 0.75rem;
    padding: 0.25rem 0.5rem;
    border-radius: 10px;
  }

  /* 內容區域 */
  .profile-content {
    background: white;
    border-radius: 12px;
    padding: 2rem;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  }

  .section-header {
    margin-bottom: 2rem;
    padding-bottom: 1rem;
    border-bottom: 1px solid #e9ecef;
  }

  .order-card {
    transition: transform 0.2s;
  }

  .order-card:hover {
    transform: translateY(-2px);
  }

  .order-detail {
    animation: fadeIn 0.3s ease-in-out;
  }

  @keyframes fadeIn {
    from {
      opacity: 0;
      transform: translateY(-10px);
    }

    to {
      opacity: 1;
      transform: translateY(0);
    }
  }

  .table th {
    background: #f8f9fa;
  }

  @media (max-width: 768px) {
    .btn-group {
      display: flex;
      flex-direction: column;
      gap: 0.5rem;
    }

    .btn-group .btn {
      width: 100%;
    }
  }
</style>
