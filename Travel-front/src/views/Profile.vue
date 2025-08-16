<template>
  <div class="profile-page">
    <!-- Header -->
    <Header />

    <div class="container my-5 pt-4">
      <div class="row">
        <!-- 左側導航 -->
        <div class="col-md-3">
          <div class="profile-sidebar">
            <!-- 用戶資訊卡片 -->
            <div class="user-card mb-4">
              <div class="user-avatar">
                <img :src="userInfo.avatar ||
                  'https://via.placeholder.com/80x80/007bff/ffffff?text=' +
                  userInfo.name?.charAt(0)
                  " :alt="userInfo.name" class="avatar-img" />
              </div>
              <div class="user-info">
                <h6 class="user-name">{{ userInfo.name || '用戶' }}</h6>
                <p class="user-email">
                  {{ userInfo.email || 'user@example.com' }}
                </p>
                <div class="user-level">
                  <span class="level-badge">{{ userInfo.level || '一般會員' }}</span>
                </div>
                <!-- <div class="user-points mt-3">
                  <span class="points-badge">
                    <i class="bi bi-star-fill me-1 text-warning"></i>
                    我的點數: {{ userInfo.points?.toLocaleString() || 0 }}
                  </span>
                </div> -->
                <!-- 既有帳戶(帳號要叫user)會顯示 (user_id =1 寫死的點數)-->
                <div class="user-points mt-3" v-if="userInfo.name === 'user'">
                  <span class="points-badge">
                    <i class="bi bi-star-fill me-1 text-warning"></i>
                    我的點數: {{ userInfo.points?.toLocaleString() || 0 }}
                  </span>
                </div>
                <!-- 註冊的新帳戶會顯示(帳戶名字不能叫user) (0，不會從資料庫抓資料)-->
                <div class="user-points mt-3" v-if="userInfo.name !== 'user'">
                  <span class="points-badge">
                    <i class="bi bi-star-fill me-1 text-warning"></i>
                    我的點數: 0
                  </span>
                </div>
              </div>
            </div>

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
            <!-- 基本資料 -->
            <div v-if="activeTab === 'profile'" class="content-section">
              <div class="section-header position-relative">
                <h4>基本資料</h4>
                <p class="text-muted">管理您的個人資料</p>
                <span style="position: absolute; bottom: 10px; right: 0px; color: #a1a1a1; font-size: 12px;">
                  <i class=" bi bi-clock me-1"></i>上次修改：{{ userInfo.lastModified }}
                </span>
              </div>
              <ProfileForm v-model:userInfo="userInfo" :isUserForm="true" :skip-count="skipCount" />
            </div>

            <!-- 我的旅伴 -->
            <div v-if="activeTab === 'partners'" class="content-section">
              <div class="section-header position-relative">
                <h4>我的旅伴</h4>
                <p class="text-muted">新增、管理同行的旅伴資料</p>
                <button class="btn rounded-0 border-0 bg-transparent border-bottom border-dark no-hover new-partner"
                  style="position: absolute; bottom: 10px; right: 0px; z-index: 10" @click="addNewPartner">
                  <i class="bi bi-plus-lg me-1"></i>新增旅伴
                </button>
              </div>

              <div v-if="partners.length > 0">
                <div style="margin-bottom: 10px">點擊管理旅伴資料</div>
                <div class="accordion" id="userAccordion">
                  <div class="card mb-2" v-for="partner in sortedPartners" :key="partner.id">
                    <div class="card-header" :id="`heading-${partner.id}`">
                      <h2 class="mb-0">
                        <button v-if="partner.deleteStatus === 1"
                          class="btn text-start w-100 d-flex justify-content-between align-items-center text-decoration-none partner-profile-btn"
                          type="button" @click="toggle(partner.id)">
                          <div class="d-flex justify-content-between w-100">
                            <span>{{ partner.profile.givenName + ', ' +
                              partner.profile.familyName }}</span>
                            <div class="d-flex align-items-center" style="font-size: 12px; color: #919191;">
                              <i class="bi bi-clock me-1"></i>
                              <span>上次修改：{{ partner.profile.lastModified }}</span>
                            </div>
                          </div>
                          <span><i class="bi bi-chevron-right arrow-icon"
                              :class="{ rotated: expandedId === partner.id }"></i></span>
                        </button>

                        <button v-else disabled
                          class="btn text-start w-100 d-flex justify-content-between align-items-center text-decoration-none partner-profile-btn"
                          type="button" @click="toggle(partner.id)">
                          <div class="d-flex justify-content-between w-100">
                            <span>{{ partner.profile.givenName + ', ' +
                              partner.profile.familyName }}</span>
                            <div class="d-flex align-items-center" style="font-size: 12px; color: #919191;">
                              <button class="me-3 btn"
                                style="font-size: 14px; pointer-events: auto; background-color: red; padding: 2px 5px; color: white; font-weight: bold;"
                                @click="recover(partner.id)">復原</button>
                              <i class="bi bi-trash3 me-1"></i>
                              <span>刪除時間：{{ partner.profile.deleteTime }}</span>
                            </div>
                          </div>
                          <span><i class="bi bi-chevron-right arrow-icon" style="visibility: hidden;"
                              :class="{ rotated: expandedId === partner.id }"></i></span>
                        </button>
                      </h2>
                    </div>

                    <!-- <div :id="`collapse-${partner.id}`" class="collapse"
                      :class="{ show: expandedId === partner.id }"> -->
                    <transition name="accordion">
                      <!-- <div class="card-body"> -->
                      <div v-show="expandedId === partner.id" class="card-body">
                        <ProfileForm :is-partner-form="true" :id="partner.id" @removePartner="expandedId = null" />
                      </div>
                    </transition>
                    <!-- </div> -->
                  </div>
                </div>
              </div>
            </div>
            <!-- 新增旅伴 -->
            <NewPartner v-model:show-new-form="showNewForm" :userId="userId" />

            <!-- 我的旅程 -->
            <div v-if="activeTab === 'journey'" class="content-section">
              <div class="section-header position-relative">
                <h4>我的旅程</h4>
                <p class="text-muted">您規劃的旅程項目</p>
                <button class="btn rounded-0 border-0 bg-transparent border-bottom border-dark no-hover new-partner"
                  style="position: absolute; bottom: 10px; right: 0px; z-index: 10" @click="addNewPlan">
                  <i class="bi bi-plus-lg me-1"></i>新增旅程
                </button>
              </div>

              <div class="journey-list">
                <div v-if="plans.length > 0">
                  <div style="margin-bottom: 10px">點擊查看或管理旅程資料</div>
                  <div class="accordion" id="userAccordion">
                    <div class="card mb-2" v-for="plan in sortedPlans" :key="plan.id">
                      <h2 class="mb-0">
                        <div class="btn text-start w-100 p-3 plan-container" @click.stop="goToPlanDetail(plan.id)">
                          <div class="d-flex justify-content-between align-items-center mb-4">
                            <span class="plan-title" style="font-size: 20px;">{{ plan.title }}</span>
                            <button class="btn bi bi-pencil-square ms-2" @click.stop="" @mousedown.stop @mouseup.stop>
                              <!-- <i class="bi bi-pencil-square ms-2" style="color: #0095b3;"></i> -->
                            </button>
                            <div class="d-flex align-items-center ms-auto" style="font-size: 12px; color: #919191;">
                              <i class="bi bi-clock me-1"></i>
                              <span>上次修改：{{ plan.lastModified }}</span>
                            </div>
                          </div>

                          <!-- <div class="plan-dates">
                            <i class="bi bi-calendar-week me-2 text-primary"></i>
                            <span>{{ plan.startDate + ' ~ ' + plan.endDate }}</span>
                            <div class="plan-description">
                              <i class="bi bi-journal-text me-2 text-muted" style="font-size: 16px;"></i>
                              <span style="font-size: 14px; color: #808080;">{{ plan.description }}</span>
                            </div>
                          </div> -->
                          <div class="plan-dates">
                            <i class="bi bi-calendar-week me-2 text-primary"></i>
                            <span>{{ plan.startDate + ' ~ ' + plan.endDate }}</span>
                          </div>

                          <div class="plan-description">
                            <i class="bi bi-journal-text me-2 text-muted" style="font-size: 16px;"></i>
                            <span style="font-size: 14px; color: #808080;">{{ plan.description }}</span>
                          </div>

                        </div>
                      </h2>
                    </div>
                  </div>
                </div>
                <div v-else class="text-center py-5">
                  <i class="bi bi-bag-x fs-1 text-muted"></i>
                  <p class="mt-3 text-muted">您的旅程還是空的</p>
                  <!-- <router-link to="/" class="btn btn-primary"> 創建旅程 </router-link> -->
                  <button class="btn btn-primary" @click="addNewPlan"> 創建旅程 </button>
                </div>



                <!-- <div v-else>
                  <div v-for="item in journeyItems" :key="`${item.type}-${item.id}`" class="journey-item mb-3">
                    <div class="card">
                      <div class="row g-0">
                        <div class="col-md-3">
                          <img :src="item.image" :alt="item.name" class="journey-image" />
                        </div>
                        <div class="col-md-9">
                          <div class="card-body">
                            <div class="d-flex justify-content-between">
                              <h6 class="card-title">{{ item.name }}</h6>
                              <button class="btn btn-link text-danger" @click="removeFromJourney(item)">
                                <i class="bi bi-trash"></i>
                              </button>
                            </div>
                            <p class="card-text text-muted">
                              <i class="bi bi-geo-alt"></i>
                              {{ item.location || item.route }}
                            </p>
                            <div class="d-flex justify-content-between align-items-center">
                              <div class="quantity-control">
                                <button class="btn btn-outline-secondary btn-sm" @click="updateQuantity(item, -1)"
                                  :disabled="item.quantity <= 1">
                                  <i class="bi bi-dash"></i>
                                </button>
                                <span class="mx-2">{{ item.quantity }}</span>
                                <button class="btn btn-outline-secondary btn-sm" @click="updateQuantity(item, 1)">
                                  <i class="bi bi-plus"></i>
                                </button>
                              </div>
                              <span class="price">NT$ {{ (item.price * item.quantity).toLocaleString() }}</span>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="card mt-4">
                    <div class="card-body">
                      <div class="d-flex justify-content-between align-items-center">
                        <div>
                          <h6 class="mb-0">總計</h6>
                          <small class="text-muted">{{ totalItems }} 項商品</small>
                        </div>
                        <div class="text-end">
                          <h5 class="mb-0">NT$ {{ totalPrice.toLocaleString() }}</h5>
                        </div>
                      </div>
                      <button class="btn btn-primary w-100 mt-3" @click="proceedToCheckout">
                        前往結帳
                      </button>
                    </div>
                  </div>
                </div> -->
              </div>
            </div>
            <NewPlan v-model:show-new-plan="showNewPlan" :userId="userId" />

            <!-- 我的訂單 -->
            <!-- <div v-if="activeTab === 'orders'" class="content-section">
              <div class="section-header">
                <h4>我的訂單</h4>
                <p class="text-muted">查看您的所有訂單記錄</p>
              </div>

              <div class="order-filters mb-4">
                <div class="btn-group" role="group">
                  <button v-for="filter in orderFilters" :key="filter.value" type="button" class="btn" :class="filter.value === activeOrderFilter ? 'btn-primary' : 'btn-outline-primary'
                    " @click="activeOrderFilter = filter.value">
                    {{ filter.label }}
                  </button>
                </div>
              </div>

              <div class="orders-list">
                <div v-if="filteredOrders.length === 0" class="text-center py-5">
                  <i class="bi bi-receipt fs-1 text-muted"></i>
                  <p class="mt-3 text-muted">暫無訂單記錄</p>
                </div>

                <div v-else>
                  <div v-for="order in filteredOrders" :key="order.id" class="order-card mb-3">
                    <div class="card">
                      <div class="card-body">
                        <div class="row align-items-center">
                          <div class="col-md-2">
                            <img :src="order.image" :alt="order.title" class="order-image" />
                          </div>
                          <div class="col-md-4">
                            <h6 class="order-title">{{ order.title }}</h6>
                            <p class="order-date text-muted">
                              <i class="bi bi-calendar"></i>
                              {{ formatDate(order.date) }}
                            </p>
                            <p class="order-id text-muted">訂單號：{{ order.orderNumber }}</p>
                          </div>
                          <div class="col-md-2">
                            <span class="order-amount">NT$ {{ order.total.toLocaleString() }}</span>
                          </div>
                          <div class="col-md-2">
                            <span class="order-status" :class="getStatusClass(order.status)">
                              {{ getStatusText(order.status) }}
                            </span>
                          </div>
                          <div class="col-md-2">
                            <button class="btn btn-outline-primary btn-sm" @click="viewOrderDetail(order)">
                              查看詳情
                            </button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div> -->

            <!-- coupon -->
            <!-- 我的折價券 -->
            <div v-if="activeTab === 'coupon'" class="content-section">
              <div class="section-header">
                <h4>我的折價券</h4>
                <p class="text-muted">查看您可使用的折價券</p>
              </div>

              <div v-if="userInfo.name === 'user'" class="coupon-filters mb-4"> <!-- 寫死: 既有帳戶(帳號要叫user)會顯示 -->
                <div class="btn-group" role="group">
                  <button v-for="filter in couponFilters" :key="filter.value" type="button" class="btn" :class="filter.value === activeCouponFilter ? 'btn-primary' : 'btn-outline-primary'
                    " @click="activeCouponFilter = filter.value">
                    {{ filter.label }}
                  </button>
                </div>
              </div>

              <div class="coupons-list">
                <div v-if="loading" class="text-center py-5">
                  <div class="spinner-border text-primary" role="status">
                    <span class="visually-hidden">Loading...</span>
                  </div>
                  <p class="mt-3 text-muted">載入中...</p>
                </div>
                <div v-else-if="paginatedCoupons.length === 0 || userInfo.name !== 'user'" class="text-center py-5">
                  <!-- 寫死: 第二個條件: 新帳戶(帳號不叫user)會顯示 -->
                  <i class="bi bi-ticket-perforated fs-1 text-muted"></i>
                  <p class="mt-3 text-muted">目前沒有可用的折價券</p>
                </div>

                <div v-else-if="userInfo.name === 'user'"> <!-- 寫死: 既有帳戶(帳號要叫user)會顯示 -->
                  <div v-for="coupon in paginatedCoupons" :key="coupon.id" class="coupon-card mb-3" :class="{
                    'coupon-card-disabled':
                      coupon.status === 'used' || coupon.status === 'expired'
                  }">
                    <div class="card">
                      <div class="card-body">
                        <div class="row align-items-center">
                          <div class="col-md-3 text-center">
                            <img src="/image/coupon.svg" alt="Coupon icon" class="coupon-icon mt-2" />
                          </div>
                          <div class="col-md-5">
                            <h6 class="coupon-title">{{ coupon.title }}</h6>
                            <p class="coupon-text">
                              <i class="bi bi-clock"></i>&nbsp; 到期日：<span>{{
                                formatDate(coupon.expiryDate)
                              }}</span>
                            </p>
                            <p class="coupon-text">
                              <i class="bi bi-calendar-check"></i>&nbsp; 發放日：{{
                                formatDate(coupon.issuedTime)
                              }}
                            </p>
                            <p class="coupon-text" v-if="coupon.status === 'used'">
                              <i class="bi bi-calendar-check"></i>&nbsp; 使用日：{{
                                formatDate(coupon.useAt)
                              }}
                            </p>
                            <p class="coupon-text" v-if="coupon.minSpend && coupon.minSpend > 0">
                              <i class="bi bi-currency-dollar"></i>&nbsp; 可使用此券的低消金額：NT$
                              {{ coupon.minSpend.toLocaleString() }}
                            </p>
                            <button v-if="coupon.detail" class="btn btn-link btn-sm p-0 mt-2"
                              @click="toggleCouponDetail(coupon.id)"
                              :disabled="coupon.status === 'used' || coupon.status === 'expired'">
                              {{ showDetail[coupon.id] ? '隱藏詳細說明' : '查看詳細說明' }}
                              <i :class="showDetail[coupon.id] ? 'bi bi-chevron-up' : 'bi bi-chevron-down'
                                "></i>
                            </button>

                            <div v-if="showDetail[coupon.id]" class="coupon-detail mt-3 p-2 bg-light border rounded">
                              <p class="mb-0 text-muted">{{ coupon.detail }}</p>
                            </div>
                          </div>

                          <div class="col-md-2 text-end">
                            <span class="coupon-value">{{ getFormattedValue(coupon) }}</span>
                          </div>
                          <div class="col-md-2 text-end">
                            <button class="btn btn-sm" :class="{
                              'btn-primary': coupon.status === 'available',
                              'btn-secondary': coupon.status !== 'available'
                            }" :disabled="coupon.status !== 'available'">
                              {{ getButtonStatusText(coupon.status) }}
                            </button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="pagination d-flex justify-content-center mt-4 gap-2" v-if="totalPages > 1">
                    <button class="btn btn-outline-secondary" :disabled="currentPage === 1"
                      @click="goToPage(currentPage - 1)">
                      上一頁
                    </button>

                    <button v-for="page in totalPages" :key="page" class="btn"
                      :class="page === currentPage ? 'btn-primary' : 'btn-outline-primary'" @click="goToPage(page)">
                      {{ page }}
                    </button>

                    <button class="btn btn-outline-secondary" :disabled="currentPage === totalPages"
                      @click="goToPage(currentPage + 1)">
                      下一頁
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- coupon -->

          <!-- 我的收藏 -->
          <div v-if="activeTab === 'favorites'" class="content-section">
            <div class="section-header">
              <h4>我的收藏</h4>
              <p class="text-muted">您收藏的商品和景點</p>
            </div>

            <div class="favorites-grid">
              <div v-if="favorites.length === 0" class="text-center py-5">
                <i class="bi bi-heart fs-1 text-muted"></i>
                <p class="mt-3 text-muted">您還沒有收藏任何項目</p>
              </div>

              <div v-else class="row">
                <div v-for="item in favorites" :key="`${item.type}-${item.id}`" class="col-md-6 col-lg-4 mb-3">
                  <div class="favorite-card">
                    <div class="card h-100">
                      <img :src="item.image" :alt="item.name" class="card-img-top favorite-image" />
                      <div class="card-body">
                        <h6 class="card-title">{{ item.name }}</h6>
                        <p class="card-text text-muted">
                          <i class="bi bi-geo-alt"></i>
                          {{ item.location || item.route }}
                        </p>
                        <div class="d-flex justify-content-between align-items-center">
                          <span class="price">NT$ {{ item.price.toLocaleString() }}</span>
                          <button class="btn btn-outline-danger btn-sm" @click="removeFavorite(item)">
                            <i class="bi bi-heart-fill"></i>
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 設定 -->
          <div v-if="activeTab === 'settings'" class="content-section">
            <div class="section-header">
              <h4>帳戶設定</h4>
              <p class="text-muted">管理您的帳戶安全和偏好設定</p>
            </div>

            <div class="row">
              <div class="col-md-6">
                <div class="card mb-4">
                  <div class="card-header">
                    <h6 class="mb-0">修改密碼</h6>
                  </div>
                  <div class="card-body">
                    <form @submit.prevent="changePassword">
                      <div class="mb-3">
                        <label class="form-label">目前密碼</label>
                        <input type="password" class="form-control" v-model="passwordForm.currentPassword" required />
                      </div>
                      <div class="mb-3">
                        <label class="form-label">新密碼</label>
                        <input type="password" class="form-control" v-model="passwordForm.newPassword" required />
                      </div>
                      <div class="mb-3">
                        <label class="form-label">確認新密碼</label>
                        <input type="password" class="form-control" v-model="passwordForm.confirmPassword" required />
                      </div>
                      <button type="submit" class="btn btn-primary">更新密碼</button>
                    </form>
                  </div>
                </div>
              </div>
            </div>

            <div class="col-md-6">
              <div class="card mb-4">
                <div class="card-header">
                  <h6 class="mb-0">通知設定</h6>
                </div>
                <div class="card-body">
                  <div class="form-check mb-3">
                    <input class="form-check-input" type="checkbox" id="emailNotif"
                      v-model="notificationSettings.email" />
                    <label class="form-check-label" for="emailNotif"> 電子郵件通知 </label>
                  </div>
                  <div class="form-check mb-3">
                    <input class="form-check-input" type="checkbox" id="smsNotif" v-model="notificationSettings.sms" />
                    <label class="form-check-label" for="smsNotif"> 簡訊通知 </label>
                  </div>
                  <div class="form-check mb-3">
                    <input class="form-check-input" type="checkbox" id="promoNotif"
                      v-model="notificationSettings.promotions" />
                    <label class="form-check-label" for="promoNotif"> 促銷活動通知 </label>
                  </div>
                  <button class="btn btn-primary" @click="saveNotificationSettings">
                    儲存設定
                  </button>
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
  import { ref, computed, onMounted, onUnmounted, watch, nextTick, watchEffect } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import Header from '../components/Header.vue';
  import ProfileForm from '@/components/userprofiles/ProfileForm.vue';
  import Swal from 'sweetalert2';
  import NewPartner from '@/components/userprofiles/NewPartner.vue';
  import { jwtDecode } from 'jwt-decode';
  import { useCountriesStore } from '@/stores/countries';
  import axios from 'axios';
  import { usePartnersStore } from '@/stores/partners';
  import { usePlansStore } from '@/stores/plans';
  import NewPlan from '@/components/plans/NewPlan.vue';

  const router = useRouter();
  const route = useRoute();

  // 當前活動的標籤
  const activeTab = ref('profile');
  const activeOrderFilter = ref('all');


  watchEffect(() => {
    const newVal = route.query.activeTab;
    if (newVal) {
      activeTab.value = newVal;
      router.replace({ path: route.path });
    }
  });

  // -------------------------點數-------------------------
  const fetchUserPoints = async () => {
    try {
      // API 路徑不再包含 userId，因為後端固定為 1
      const response = await axios.get(`/api/points/total`);
      userInfo.value.points = response.data.totalPoints; // 更新 userInfo 中的點數
      console.log('成功獲取用戶點數:', userInfo.value.points);
    } catch (error) {
      console.error('獲取用戶點數失敗:', error);
      userInfo.value.points = 0; // 失敗時點數設為 0 或其他預設值
      Swal.fire('錯誤', '無法載入點數，請稍後再試。', 'error');
    }
  };

  const userInfo = ref({
    name: '',
    email: '',
    level: '一般會員',
    avatar: '',
    givenName: '',
    familyName: '',
    givenNameLatin: '',
    familyNameLatin: '',
    nationality: '',
    passportNo: '',
    passportExpire: '',
    birthday: '',
    residence: '',
    address: '',
    phoneCode: '',
    telNumber: '',
    gender: null,
    points: 0,
    lastModified: '',
  });

  // 導航選單
  const navItems = ref([
    { id: 'profile', title: '基本資料', icon: 'bi bi-person' },
    { id: 'partners', title: '我的旅伴', icon: 'bi bi-people' },
    { id: 'journey', title: '我的旅程', icon: 'bi bi-bag' },
    // { id: 'orders', title: '我的訂單', icon: 'bi bi-receipt', badge: '3' },
    { id: 'coupon', title: '我的折價券', icon: 'bi bi-ticket-detailed' },
    { id: 'favorites', title: '我的收藏', icon: 'bi bi-heart' },
    { id: 'settings', title: '設定', icon: 'bi bi-gear' }
  ]);


  // 訂單篩選器
  const orderFilters = ref([
    { value: 'all', label: '全部' },
    { value: 'pending', label: '待付款' },
    { value: 'confirmed', label: '已確認' },
    { value: 'completed', label: '已完成' },
    { value: 'cancelled', label: '已取消' }
  ]);

  const fetchCoupons = async () => {
    loading.value = true;
    try {
      const response = await axios.get('http://localhost:8080/coupons/my-coupons');
      // 遍歷資料並將日期字串轉換為 Date 物件
      coupons.value = response.data.map((coupon) => {
        return {
          ...coupon,
          expiryDate: coupon.expiryDate ? new Date(coupon.expiryDate) : null,
          issuedTime: coupon.issuedTime ? new Date(coupon.issuedTime) : null,
          useAt: coupon.useAt ? new Date(coupon.useAt) : null // 確保 useAt 也是 Date 物件
        };
      });
      showDetail.value = {};
      coupons.value.forEach((coupon) => {
        showDetail.value[coupon.id] = false;
      });
    } catch (error) {
      console.error('Error fetching coupons:', error);
      Swal.fire('錯誤', '無法載入折價券，請稍後再試。', 'error');
    } finally {
      loading.value = false;
    }
  };
  // 模擬訂單數據
  const orders = ref([
    {
      id: 1,
      title: '東京迪士尼樂園一日門票',
      date: '2024-01-15',
      orderNumber: 'ORD20240115001',
      total: 7500,
      status: 'completed',
      image: 'image/location/disney.webp'
    },
    {
      id: 2,
      title: '台北君悅酒店豪華客房',
      date: '2024-01-20',
      orderNumber: 'ORD20240120001',
      total: 12000,
      status: 'confirmed',
      image: 'https://images.unsplash.com/photo-1566073771259-6a8506099945'
    },
    {
      id: 3,
      title: '台北到東京來回機票',
      date: '2024-02-01',
      orderNumber: 'ORD20240201001',
      total: 15000,
      status: 'pending',
      image: 'https://images.unsplash.com/photo-1436491865332-7a61a109cc05'
    }
  ]);

  // 篩選後的訂單
  const filteredOrders = computed(() => {
    if (activeOrderFilter.value === 'all') {
      return orders.value;
    }
    return orders.value.filter((order) => order.status === activeOrderFilter.value);
  });

  // 折價券篩選器(不打算做搜尋了)
  const couponFilters = ref([
    { value: 'all', label: '全部' },
    { value: 'available', label: '可使用' },
    { value: 'used', label: '已使用' },
    { value: 'expired', label: '已過期' }
  ]);

  const activeCouponFilter = ref('all');

  // 分頁
  const currentPage = ref(1);
  const pageSize = 6;
  const totalPages = computed(() => Math.ceil(filteredCoupons.value.length / pageSize));
  const paginatedCoupons = computed(() => {
    const start = (currentPage.value - 1) * pageSize;
    return filteredCoupons.value.slice(start, start + pageSize);
  });
  const goToPage = (page) => {
    if (page >= 1 && page <= totalPages.value) currentPage.value = page;
    // 自動捲動回「折價券區塊」頂部
    nextTick(() => {
      const couponSection = document.querySelector('.coupons-list');
      if (couponSection) {
        window.scrollTo({ top: 0, behavior: 'smooth' });
      }
    });
  };

  watch(activeCouponFilter, () => (currentPage.value = 1));

  // 折價券數據 - 將由後端讀取
  const coupons = ref([]);
  const showDetail = ref({});
  const loading = ref(false); // New loading state

  // 篩選後的折價券
  const filteredCoupons = computed(() => {
    let filtered = coupons.value;
    const now = new Date();

    // 定義日期排序的輔助函數，避免重複代碼

    // 排序：可使用的折價券，到期日近到遠
    // 如果到期日為 null (不限)，則排在有到期日的後面
    const sortByExpiryDateAsc = (a, b) => {
      if (a.expiryDate === null && b.expiryDate !== null) return 1;
      if (a.expiryDate !== null && b.expiryDate === null) return -1;
      if (a.expiryDate === null && b.expiryDate === null) return 0;
      return a.expiryDate.getTime() - b.expiryDate.getTime();
    };

    // 排序：已使用的折價券，使用日期近到遠
    const sortByUseAtAsc = (a, b) => {
      const useAtA = a.useAt ? a.useAt.getTime() : 0;
      const useAtB = b.useAt ? b.useAt.getTime() : 0;
      return useAtB - useAtA; // <--- 這裡修正為降序，確保最早使用的排在前面
    };

    // 排序：已過期的折價券，過期日期近到遠
    const sortByExpiryDateAscForExpired = (a, b) => {
      const expiryDateA = a.expiryDate ? a.expiryDate.getTime() : 0;
      const expiryDateB = b.expiryDate ? b.expiryDate.getTime() : 0;
      // 對於已過期的券，我們希望最早過期的排在前面
      return expiryDateB - expiryDateA; // <--- 這裡修正為降序，確保最早過期的排在前面
    };

    // (全部頁籤中) 折價券排序顯示邏輯 可使用 --> 已使用 --> 已過期
    if (activeCouponFilter.value === 'all') {
      filtered = [...coupons.value].sort((a, b) => {
        const getCategory = (coupon) => {
          // 判斷是否為「可使用」：狀態為 available 且 (沒有到期日 或 到期日晚於現在)
          if (
            coupon.status === 'available' &&
            (coupon.expiryDate === null || coupon.expiryDate > now)
          ) {
            return 1; // 可使用
          }
          // 判斷是否為「已使用」：狀態為 used
          else if (coupon.status === 'used') {
            return 2; // 已使用
          }
          // 其他情況皆視為「已過期」 (包括 status 為 'expired' 或 status 為 'available' 但已過期)
          else {
            return 3; // 已過期
          }
        };

        const categoryA = getCategory(a);
        const categoryB = getCategory(b);

        if (categoryA !== categoryB) {
          return categoryA - categoryB; // 不同類別優先排序 (可使用 -> 已使用 -> 已過期)
        }

        // 各種類型(可使用、已使用、已過期)的折價券，再依各自的規則排序
        if (categoryA === 1) {
          // 可使用
          return sortByExpiryDateAsc(a, b); // 到期日近到遠
        } else if (categoryA === 2) {
          // 已使用
          return sortByUseAtAsc(a, b); // 使用日期遠到近 (先使用過的在前面)
        } else if (categoryA === 3) {
          // 已過期
          return sortByExpiryDateAscForExpired(a, b); // 過期日期遠到近 (先過期的在前面)
        }
        return 0;
      });
    } else {
      // 當前選中的不是「全部」頁籤時，先進行篩選
      filtered = coupons.value.filter((coupon) => {
        if (activeCouponFilter.value === 'available') {
          return (
            coupon.status === 'available' && (coupon.expiryDate === null || coupon.expiryDate > now)
          );
        } else if (activeCouponFilter.value === 'used') {
          return coupon.status === 'used';
        } else if (activeCouponFilter.value === 'expired') {
          return (
            coupon.status === 'expired' ||
            (coupon.status === 'available' &&
              coupon.expiryDate !== null &&
              coupon.expiryDate <= now)
          );
        }
        return true; // 如果 activeCouponFilter 是未知的，則不篩選
      });

      // 再根據當前選中的篩選器進行排序
      if (activeCouponFilter.value === 'available') {
        filtered.sort(sortByExpiryDateAsc); // 可使用頁籤，依到期日近到遠排
      } else if (activeCouponFilter.value === 'used') {
        filtered.sort(sortByUseAtAsc); // 已使用頁籤，依使用日期近到遠
      } else if (activeCouponFilter.value === 'expired') {
        filtered.sort(sortByExpiryDateAscForExpired); // 已過期頁籤，依過期日期近到遠排
      }
    }
    return filtered;
  });

  // const getCouponTypeClass = (type) => {
  //   // 根據 CouponDTO 的 type 屬性回傳對應的 CSS class
  //   // 後端 CouponService 邏輯將 type 設置為 "fixed", "percentage", "other"
  //   switch (type) {
  //     case 'fixed':
  //       return 'type-fixed'; // 假設 fixed 對應 type-fixed 的樣式
  //     case 'percentage':
  //       return 'type-percentage'; // 假設 percentage 對應 type-percentage 的樣式
  //     case 'other':
  //       return 'type-other'; // 假設 other 對應 type-other 的樣式
  //     default:
  //       return '';
  //   }
  // };

  const getFormattedValue = (coupon) => {
    // 根據折價券類型格式化顯示的值
    if (coupon.type === 'fixed') {
      return `NT$${coupon.value.toLocaleString()}`;
    } else if (coupon.type === 'percentage') {
      // 假設 value 是 BigDecimal 且表示百分比（例如 0.1 代表 10%）
      return `${(coupon.value * 1).toFixed(0)}% OFF`;
    }
    return `NT$${coupon.value.toLocaleString()}`; // 預設或其他類型
  };

  // const getExpiryStatus = (coupon) => {
  //   // 根據 CouponDTO 的 status 屬性判斷顯示的文字顏色
  //   if (coupon.status === 'used' || coupon.status === 'expired') {
  //     return 'text-danger'; // 已使用或已過期顯示紅色
  //   } else if (coupon.status === 'available') {
  //     return 'text-success'; // 可使用顯示綠色
  //   }
  //   return ''; // 預設
  // };

  const getButtonStatusText = (status) => {
    // 根據 CouponDTO 的 status 屬性回傳按鈕文字
    switch (status) {
      case 'available':
        return '可使用';
      case 'used':
        return '已使用';
      case 'expired':
        return '已過期';
      default:
        return '未知狀態';
    }
  };

  watch(
    activeTab,
    (newTab) => {
      if (newTab === 'coupon') {
        fetchCoupons();
      }
    },
    { immediate: true }
  );

  // --------------------coupon--------------------

  // 收藏和旅程數據
  const favorites = ref([]);
  const journeyItems = ref([]);

  // change between user or partner
  const isUserForm = ref(true);

  const toggleCouponDetail = (couponId) => {
    showDetail.value[couponId] = !showDetail.value[couponId];
  };
  // --------------------coupon--------------------

  // 獲取訂單狀態樣式

  const getExpiryStatus = (coupon) => {
    // 根據 CouponDTO 的 status 屬性判斷顯示的文字顏色
    if (coupon.status === 'used' || coupon.status === 'expired') {
      return 'text-danger'; // 已使用或已過期顯示紅色
    } else if (coupon.status === 'available') {
      return 'text-success'; // 可使用顯示綠色
    }
    return ''; // 預設
  };

  watch(
    activeTab,
    (newTab) => {
      if (newTab === 'coupon') {
        fetchCoupons();
      }
    },
    { immediate: true }
  );

  // --------------------coupon--------------------

  // detect profile changes
  const skipCount = ref(1);
  const callUpdate = () => {
    if (skipCount.value > 0) {
      console.log("skip = " + skipCount.value);
      skipCount.value--;
      return;
    }
    Swal.fire({
      title: '資料更新中',
      allowOutsideClick: false,
      allowEscapeKey: false,
      showConfirmButton: false,

      didOpen: () => {
        Swal.showLoading();
        const keysExclude = ['name', 'avatar', 'level', 'points', 'lastModified'];
        const dto = Object.keys(userInfo.value)
          .filter(k => !keysExclude.includes(k))
          .reduce((obj, key) => {
            obj[key] = userInfo.value[key];
            return obj;
          }, {});
        axios.put(api + userId + '/update', dto)
          .then(response => {
            userInfo.value.lastModified = response.data.lastModified;
            Swal.hideLoading();
            console.log(skipCount.value);
            Swal.update({
              icon: 'success',
              title: '資料已更新！',
              showConfirmButton: true,
              allowOutsideClick: true,
              allowEscapeKey: true,
              confirmButtonColor: '#47a8d1'
            });
          })
          .catch(error => {
            Swal.hideLoading();
            Swal.update({
              icon: 'error',
              title: '資料更新失敗！',
              text: `[${error.response.status}] ${error.response.data}`,
              showConfirmButton: true,
              allowOutsideClick: true,
              allowEscapeKey: true,
              confirmButtonColor: '#47a8d1'
            });
          })

      }
    });
  };

  // ------------------------- partners -------------------------
  // add a new partner
  const showNewForm = ref(false);
  const addNewPartner = () => {
    showNewForm.value = true;
  };

  const partners = computed(() => partnerStore.partners);
  const sortedPartners = computed(() => {
    return partners.value.slice().sort((a, b) => b.deleteStatus - a.deleteStatus);
  })
  const expandedId = ref(null);
  const toggle = (partnerId) => {
    expandedId.value = expandedId.value === partnerId ? null : partnerId;
  };

  const recover = async (id) => {
    Swal.fire({
      showConfirmButton: true,
      showCancelButton: true,
      allowEscapeKey: true,
      allowOutsideClick: true,
      title: '是否復原該旅伴資料？',
      confirmButtonText: '是',
      cancelButtonText: '否',
      confirmButtonColor: '#47a8d1',
      cancelButtonColor: '#d1476a',
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire({
          didOpen: async () => {
            Swal.showLoading();
            await partnerStore.recoverPartner(id);
          }
        });

        setTimeout(() => {
          Swal.hideLoading();
          Swal.update({
            title: '資料已復原！',
            showConfirmButton: true,
            allowOutsideClick: true,
            allowEscapeKey: true,
            confirmButtonColor: '#47a8d1'
          });
        })
          .catch((error) => {
            Swal.hideLoading();
            Swal.update({
              icon: 'error',
              title: '資料更新失敗！',
              text: `[${error.response.status}] ${error.response.data}`,
              showConfirmButton: true,
              allowOutsideClick: true,
              allowEscapeKey: true,
              confirmButtonColor: '#47a8d1'
            });
          });
      }
    });
  };

  watch(() => userInfo.value, () => {
    callUpdate();
  });

  // ------------------------- partners -------------------------

  const plans = computed(() => planStore.plans);
  const sortedPlans = computed(() => {
    return plans.value.slice().sort((a, b) => new Date(a.startDate) - new Date(b.startDate));
  })

  const showNewPlan = ref(false);
  const addNewPlan = () => {
    showNewPlan.value = true;
  };

  const goToPlanDetail = (planId) => {
    planStore.nowAtIndex = planId;
    router.push('/planDetails/' + planId);
  }
  // ------------------------- plans -------------------------


  // --------------------coupon--------------------
  // 格式化日期 修改更完整
  const formatDate = (dateString) => {
    if (!dateString) return '不限'; // null代表折價券沒有到期日
    const options = { year: 'numeric', month: 'numeric', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('zh-TW', options);
  };
  // --------------------coupon--------------------


  const passwordForm = ref({
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  });

  // 儲存通知設定
  const saveNotificationSettings = () => {
    // TODO: 實現儲存通知設定的邏輯
    alert('通知設定已儲存');
  };

  // 查看訂單詳情
  const viewOrderDetail = (order) => {
    // TODO: 實現查看訂單詳情的邏輯
    console.log('查看訂單:', order);
  };

  // 移除收藏
  const removeFavorite = (item) => {
    const index = favorites.value.findIndex((i) => i.id === item.id && i.type === item.type);
    if (index > -1) {
      favorites.value.splice(index, 1);
      localStorage.setItem('favorites', JSON.stringify(favorites.value));
      window.dispatchEvent(new Event('favoritesUpdated'));
    }
  };

  // 更新旅程項目數量
  const updateQuantity = (item, change) => {
    const newQuantity = (item.quantity || 1) + change;
    if (newQuantity < 1) return;

    item.quantity = newQuantity;
    localStorage.setItem('journey', JSON.stringify(journeyItems.value));
    window.dispatchEvent(new Event('journeyUpdated'));
  };

  // 從旅程中移除項目
  const removeFromJourney = (item) => {
    const index = journeyItems.value.findIndex((i) => i.id === item.id && i.type === item.type);
    if (index > -1) {
      journeyItems.value.splice(index, 1);
      localStorage.setItem('journey', JSON.stringify(journeyItems.value));
      window.dispatchEvent(new Event('journeyUpdated'));
    }
  };

  // 前往結帳
  const proceedToCheckout = () => {
    if (journeyItems.value.length > 0) {
      const checkoutData = {
        items: journeyItems.value,
        totalPrice: totalPrice.value,
        totalItems: totalItems.value,
        timestamp: new Date().toISOString()
      };
      localStorage.setItem('checkoutData', JSON.stringify(checkoutData));
      router.push('/checkout');
    }
  };

  // 組件掛載時初始化
  const counrtyStore = useCountriesStore();
  const partnerStore = usePartnersStore();
  const planStore = usePlansStore();


  const api = "http://localhost:8080/api/profile/"
  const token = localStorage.getItem('token');
  const payload = jwtDecode(token);
  const userId = payload.id;

  const loadStores = async () => {
    try {
      await Promise.all([
        counrtyStore.getData(),
        partnerStore.getData(userId),
        planStore.getData(userId),
      ])
      console.log(userId)
      console.log(planStore.plans);
      console.log("load country, partner, plan stores");
    } catch (error) {
      console.log("load store error: " + error);
    }
  };

  watch(() => userInfo.value.name, () => {
    userInfo.value.phoneCode = counrtyStore.countries.find(item => item.id === userInfo.value.residence).phoneCode;
  })

  onMounted(async () => {
    loadStores();
    await fetchUserPoints();
    axios.get(api + userId)
      .then(response => {
        // const userInfo = ref(response.data);
        console.log(response.data);
        userInfo.value = { ...userInfo.value, ...response.data };
        userInfo.value.name = payload.user;
        userInfo.value.avatar = userId !== 1 ? 'http://localhost:8080/' + payload.icon : payload.icon;
        console.log("userInfo.value.avatar " + userInfo.value.avatar);
      })


    // loadData();
    // window.addEventListener('journeyUpdated', handleJourneyUpdate);
    // window.addEventListener('favoritesUpdated', handleFavoritesUpdate);
  });


  // 組件卸載時清理
  // onUnmounted(() => {
  //   window.removeEventListener('journeyUpdated', handleJourneyUpdate);
  //   window.removeEventListener('favoritesUpdated', handleFavoritesUpdate);
  // });
</script>

<style scoped>
  .profile-page {
    min-height: 100vh;
    background-color: #f8f9fa;
  }

  /* 側邊欄樣式 */
  .profile-sidebar {
    position: sticky;
    top: 2rem;
  }

  .user-card {
    background: white;
    border-radius: 12px;
    padding: 1.5rem;
    text-align: center;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  }

  .user-avatar {
    margin-bottom: 1rem;
  }

  .avatar-img {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    object-fit: cover;
    border: 3px solid #007bff;
  }

  .user-name {
    font-weight: 600;
    margin-bottom: 0.25rem;
  }

  .user-email {
    color: #6c757d;
    font-size: 0.875rem;
    margin-bottom: 0.5rem;
  }

  .level-badge {
    background: linear-gradient(45deg, #007bff, #0056b3);
    color: white;
    padding: 0.25rem 0.75rem;
    border-radius: 20px;
    font-size: 0.75rem;
    font-weight: 500;
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

  /* 訂單卡片 */
  .order-image {
    width: 60px;
    height: 60px;
    object-fit: cover;
    border-radius: 8px;
  }

  .order-title {
    font-weight: 600;
    margin-bottom: 0.5rem;
  }

  .order-date,
  .order-id {
    font-size: 0.875rem;
    margin-bottom: 0.25rem;
  }

  .order-amount {
    font-weight: 600;
    color: #007bff;
  }

  .order-status {
    padding: 0.25rem 0.75rem;
    border-radius: 20px;
    font-size: 0.875rem;
    font-weight: 500;
  }

  .status-pending {
    background-color: #fff3cd;
    color: #856404;
  }

  .status-confirmed {
    background-color: #d1ecf1;
    color: #0c5460;
  }

  .status-completed {
    background-color: #d4edda;
    color: #155724;
  }

  .status-cancelled {
    background-color: #f8d7da;
    color: #721c24;
  }

  /* 折價券卡片 */
  .coupon-card {
    transition: transform 0.2s ease, box-shadow 0.2s ease;
  }

  .coupon-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  }

  /* 已使用和已過期部分的折價券，顯示上讓他更明顯區別一點，讓他整個項目灰掉 */
  .coupon-card-disabled {
    opacity: 0.6;
    pointer-events: none;
    /* 禁用所有互動 */
    background-color: #f0f0f0;
    /* 輕微灰色背景 */
  }

  .coupon-title {
    font-weight: 600;
    margin-bottom: 0.75rem;
    /* (1) 調整折價券標題下邊距 */
    color: #333;
  }

  .coupon-text {
    font-size: 0.875rem;
    margin-bottom: 0.75rem;
    /* (1) 統一並增加文字間距 */
    color: #6c757d;
  }

  .coupon-text:last-of-type {
    margin-bottom: 0;
    /* 確保最後一個 P 標籤沒有額外的下邊距 */
  }

  .coupon-value {
    font-size: 1.25rem;
    font-weight: bold;
    color: #007bff;
  }

  /* 折價券篩選器 */
  .coupon-filters .btn-group .btn {
    border-radius: 0;
  }

  .coupon-filters .btn-group .btn:first-child {
    border-top-left-radius: 0.375rem;
    border-bottom-left-radius: 0.375rem;
  }

  .coupon-filters .btn-group .btn:last-child {
    border-top-right-radius: 0.375rem;
    border-bottom-right-radius: 0.375rem;
  }

  /* 折價券圖片的樣式調整 */
  .coupon-icon {
    width: 200px;
    height: 100px;
    object-fit: contain;
  }

  /* 收藏卡片 */
  .favorite-image {
    height: 200px;
    object-fit: cover;
  }

  .favorite-card .card {
    transition: transform 0.2s ease;
  }

  .favorite-card .card:hover {
    transform: translateY(-4px);
  }

  /* 旅程項目 */
  .journey-image {
    height: 120px;
    object-fit: cover;
  }

  .quantity-control {
    display: flex;
    align-items: center;
  }

  .quantity-control button {
    width: 30px;
    height: 30px;
    padding: 0;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .price {
    font-weight: 600;
    color: #007bff;
  }

  .no-hover:hover,
  .no-hover:focus,
  .no-hover:active {
    box-shadow: none !important;
    transform: none !important;
  }

  .no-hover:hover {
    background-color: #b9b9b9 !important;
    border-color: #b9b9b9 !important;
    color: white;
  }

  .new-partner {
    padding: 0px 3px;
  }

  /* 響應式設計 */
  .coupon-type {
    padding: 0.4rem 0.8rem;
    border-radius: 20px;
    font-size: 0.8rem;
    font-weight: 600;
    text-align: center;
    margin-bottom: 0.5rem;
  }

  .type-fixed {
    background-color: #e6f7ff;
    color: #1890ff;
    border: 1px solid #91d5ff;
  }

  .type-percentage {
    background-color: #f6ffed;
    color: #52c41a;
    border: 1px solid #b7eb8f;
  }

  .type-other {
    background-color: #fff1e6;
    color: #fa8c16;
    border: 1px solid #ffd591;
  }

  .coupon-expiry,
  .coupon-code {
    font-size: 0.875rem;
    margin-bottom: 0;
    color: #6c757d;
  }

  /* 響應式設計 */
  @media (max-width: 768px) {
    .profile-sidebar {
      position: static;
      margin-bottom: 2rem;
    }

    .nav-menu {
      display: flex;
      overflow-x: auto;
      border-radius: 8px;
    }

    .nav-item {
      flex-shrink: 0;
      border-bottom: none;
      border-right: 1px solid #f8f9fa;
    }

    .nav-item:last-child {
      border-right: none;
    }

    .profile-content {
      padding: 1rem;
    }

    .order-amount {
      font-weight: 600;
      color: #007bff;
    }

    .order-status {
      padding: 0.25rem 0.75rem;
      border-radius: 20px;
      font-size: 0.875rem;
      font-weight: 500;
    }

    .status-pending {
      background-color: #fff3cd;
      color: #856404;
    }

    .status-confirmed {
      background-color: #d1ecf1;
      color: #0c5460;
    }

    .status-completed {
      background-color: #d4edda;
      color: #155724;
    }

    .status-cancelled {
      background-color: #f8d7da;
      color: #721c24;
    }

    /* 折價券卡片 */
    .coupon-card {
      transition: transform 0.2s ease, box-shadow 0.2s ease;
    }

    .coupon-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    }

    .coupon-type {
      padding: 0.4rem 0.8rem;
      border-radius: 20px;
      font-size: 0.8rem;
      font-weight: 600;
      text-align: center;
      margin-bottom: 0.5rem;
    }

    .type-fixed {
      background-color: #e6f7ff;
      color: #1890ff;
      border: 1px solid #91d5ff;
    }

    .type-percentage {
      background-color: #f6ffed;
      color: #52c41a;
      border: 1px solid #b7eb8f;
    }

    .type-other {
      background-color: #fff1e6;
      color: #fa8c16;
      border: 1px solid #ffd591;
    }

    .coupon-title {
      font-weight: 600;
      margin-bottom: 0.25rem;
      color: #333;
    }

    .coupon-expiry,
    .coupon-code {
      font-size: 0.875rem;
      margin-bottom: 0;
      color: #6c757d;
    }

    .coupon-value {
      font-size: 1.25rem;
      font-weight: bold;
      color: #007bff;
    }

    /* 折價券篩選器 */
    .coupon-filters .btn-group .btn {
      border-radius: 0;
    }

    .coupon-filters .btn-group .btn:first-child {
      border-top-left-radius: 0.375rem;
      border-bottom-left-radius: 0.375rem;
    }

    .coupon-filters .btn-group .btn:last-child {
      border-top-right-radius: 0.375rem;
      border-bottom-right-radius: 0.375rem;
    }

    /* 折價券圖片的樣式調整 */
    .coupon-icon {
      width: 200px;
      height: 100px;
      object-fit: contain;
    }

    /* 收藏卡片 */
    .favorite-image {
      height: 200px;
      object-fit: cover;
    }

    .favorite-card .card {
      transition: transform 0.2s ease;
    }

    .favorite-card .card:hover {
      transform: translateY(-4px);
    }

    /* 旅程項目 */
    .journey-image {
      height: 120px;
      object-fit: cover;
    }

    .quantity-control {
      display: flex;
      align-items: center;
    }

    .quantity-control button {
      width: 30px;
      height: 30px;
      padding: 0;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .price {
      font-weight: 600;
      color: #007bff;
    }

    .no-hover:hover,
    .no-hover:focus,
    .no-hover:active {
      box-shadow: none !important;
      transform: none !important;
    }

    .no-hover:hover {
      background-color: #b9b9b9 !important;
      border-color: #b9b9b9 !important;
      color: white;
    }

    .new-partner {
      padding: 0px 3px;
    }

    .partner-profile-btn:disabled {
      border: none;
      background-color: transparent;
      /* 可選，讓它變成透明 */
    }

    .plan-title {
      font-size: 20px;
      font-weight: bold;
    }

    .plan-dates {
      margin-top: 10px;
      font-size: 16px;
      text-align: left;
    }

    .plan-description {
      margin-top: 5px;
    }

    .plan-container:active:has(button:hover) {
      background-color: transparent !important;
      box-shadow: none !important;
      border-color: black;
    }

    .arrow-icon {
      display: inline-block;
      transition: transform 0.3s ease;
    }

    .rotated {
      transform: rotate(90deg);
    }

    .accordion-enter-active,
    .accordion-leave-active {
      transition: all 0.3s ease;
    }

    .accordion-enter-from,
    .accordion-leave-to {
      max-height: 0;
      opacity: 0;
      overflow: hidden;
    }

    .accordion-enter-to,
    .accordion-leave-from {
      max-height: 500px;
      /* 你可以調整，或用 auto + JS 做 */
      opacity: 1;
      overflow: hidden;
    }

    /* 響應式設計 */
    @media (max-width: 768px) {
      .profile-sidebar {
        position: static;
        margin-bottom: 2rem;
      }

      .nav-menu {
        display: flex;
        overflow-x: auto;
        border-radius: 8px;
      }

      .nav-item {
        flex-shrink: 0;
        border-bottom: none;
        border-right: 1px solid #f8f9fa;
      }

      .nav-item:last-child {
        border-right: none;
      }

      .profile-content {
        padding: 1rem;
      }

    }
  }
</style>
