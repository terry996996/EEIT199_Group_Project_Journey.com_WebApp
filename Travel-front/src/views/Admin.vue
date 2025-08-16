<!--
  管理員後台管理頁面
  
  @author Max
  @since 2025-07-08
-->
<template>
  <div class="admin-page">
    <!-- Header -->
    <Header />

    <div class="container my-5 pt-4">
      <div class="admin-container container py-4">
        <div class="d-flex justify-content-between align-items-center mb-3">
          <h1 class="text-primary">{{ isGeneralUser ? '個人中心' : '後台管理' }}</h1>
        </div>

        <!-- 數據統計面板 (僅管理員可見) -->
        <div class="row mb-4" v-if="!isGeneralUser">
          <div class="col-md-3 mb-3">
            <div class="card bg-primary text-white">
              <div class="card-body">
                <div class="d-flex justify-content-between">
                  <div>
                    <h4 class="mb-0">
                      {{ statistics.totalUsers.toLocaleString() }}
                    </h4>
                    <small>總用戶數</small>
                  </div>
                  <i class="bi bi-people fs-1 opacity-75"></i>
                </div>
              </div>
            </div>
          </div>
          <div class="col-md-3 mb-3">
            <div class="card bg-success text-white">
              <div class="card-body">
                <div class="d-flex justify-content-between">
                  <div>
                    <h4 class="mb-0">
                      {{ statistics.totalOrders.toLocaleString() }}
                    </h4>
                    <small>總訂單數</small>
                  </div>
                  <i class="bi bi-cart-check fs-1 opacity-75"></i>
                </div>
              </div>
            </div>
          </div>
          <div class="col-md-3 mb-3">
            <div class="card bg-warning text-white">
              <div class="card-body">
                <div class="d-flex justify-content-between">
                  <div>
                    <h4 class="mb-0">NT$ {{ (statistics.totalRevenue / 10000).toFixed(1) }}萬</h4>
                    <small>總營收</small>
                  </div>
                  <i class="bi bi-currency-dollar fs-1 opacity-75"></i>
                </div>
              </div>
            </div>
          </div>
          <div class="col-md-3 mb-3">
            <div class="card bg-info text-white">
              <div class="card-body">
                <div class="d-flex justify-content-between">
                  <div>
                    <h4 class="mb-0">{{ statistics.activeMerchants }}</h4>
                    <small>活躍商家</small>
                  </div>
                  <i class="bi bi-shop fs-1 opacity-75"></i>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 一般用戶個人資訊面板 -->
        <div class="row mb-4" v-if="isGeneralUser">
          <div class="col-12">
            <div class="card shadow-sm">
              <div class="card-header bg-primary text-white">
                <h5 class="mb-0"><i class="bi bi-person-circle me-2"></i>個人資訊</h5>
              </div>
              <div class="card-body">
                <div class="row">
                  <div class="col-md-6">
                    <div class="mb-3">
                      <label class="form-label fw-bold">用戶名稱</label>
                      <p class="form-control-plaintext">{{ userName }}</p>
                    </div>
                    <div class="mb-3">
                      <label class="form-label fw-bold">用戶角色</label>
                      <p class="form-control-plaintext">一般用戶</p>
                    </div>
                  </div>
                  <div class="col-md-6">
                    <div class="mb-3">
                      <label class="form-label fw-bold">註冊時間</label>
                      <p class="form-control-plaintext">2024年1月15日</p>
                    </div>
                    <div class="mb-3">
                      <label class="form-label fw-bold">帳戶狀態</label>
                      <span class="badge bg-success">正常</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="merchant-management mb-4">
          <button class="btn btn-primary me-2" @click="showAddHotelModal = true">
            新增飯店業主
          </button>
          <button class="btn btn-primary me-2" @click="showAddAirlineModal = true">
            新增航空公司
          </button>
          <button class="btn btn-primary me-2" @click="showAddTicketVendorModal = true">
            新增票卷商
          </button>
          <button class="btn btn-primary" @click="alert('新增交通票商功能開發中...')">
            新增交通票商
          </button>
        </div>

        <div class="row">
          <!-- 商家管理 -->
          <div class="col-lg-6 mb-4" v-if="canViewMerchant">
            <div class="card shadow-sm h-100">
              <div
                class="card-header bg-primary text-white d-flex justify-content-between align-items-center"
              >
                <span>商家管理</span>
                <div class="d-flex gap-1" v-if="canAddMerchant">
                  <button
                    class="btn btn-outline-light btn-sm me-2"
                    @click="loadVendors"
                    title="刷新商家列表"
                  >
                    <i class="bi bi-arrow-clockwise"></i>
                  </button>
                </div>
              </div>
              <div class="card-body">
                <div v-if="vendors.length === 0" class="text-muted">尚無商家資料</div>
                <div v-else>
                  <div v-for="vendor in vendors" :key="vendor.id" class="mb-3 border-bottom pb-2">
                    <div class="d-flex justify-content-between align-items-start">
                      <div class="flex-grow-1">
                        <div><strong>飯店名稱：</strong>{{ vendor.name || vendor.vendorName }}</div>
                        <div><strong>Email：</strong>{{ vendor.email }}</div>
                        <div v-if="vendor.username">
                          <strong>帳號：</strong>{{ vendor.username }}
                        </div>
                      </div>
                      <div class="ms-2">
                        <button
                          class="btn btn-outline-danger btn-sm"
                          @click="deleteVendor(vendor)"
                          title="刪除商家"
                        >
                          <i class="bi bi-trash"></i>
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- 用戶管理 -->
          <div class="col-lg-6 mb-4" v-if="canManageUser">
            <div class="card shadow-sm h-100">
              <div
                class="card-header bg-primary text-white d-flex justify-content-between align-items-center"
              >
                <span>用戶管理</span>
                <div class="d-flex gap-1">
                  <button
                    class="btn btn-outline-light btn-sm me-2"
                    @click="loadUsers"
                    title="刷新用戶列表"
                  >
                    <i class="bi bi-arrow-clockwise"></i>
                  </button>
                </div>
              </div>
              <div class="card-body">
                <div class="input-group mb-3">
                  <input
                    v-model="userSearch"
                    type="text"
                    class="form-control"
                    placeholder="搜尋用戶名稱或Email"
                  />
                  <button class="btn btn-outline-primary" @click="searchUser">搜尋</button>
                </div>
                <div v-if="users.length === 0" class="text-muted">尚無用戶資料</div>
                <div v-else>
                  <div v-for="user in filteredUsers" :key="user.id" class="mb-3 border-bottom pb-2">
                    <div class="d-flex justify-content-between align-items-start">
                      <div class="flex-grow-1">
                        <div><strong>用戶名稱：</strong>{{ user.username }}</div>
                        <div><strong>Email：</strong>{{ user.email }}</div>
                        <div><strong>註冊時間：</strong>{{ formatDate(user.createdAt) }}</div>
                      </div>
                      <div class="ms-2 d-flex gap-1">
                        <button
                          class="btn btn-outline-info btn-sm"
                          @click="viewUserDetails(user)"
                          title="查看詳細資訊"
                        >
                          <i class="bi bi-eye"></i>
                        </button>
                        <button
                          class="btn btn-outline-danger btn-sm"
                          @click="deleteUser(user)"
                          title="刪除用戶"
                        >
                          <i class="bi bi-trash"></i>
                        </button>
                      </div>
                    </div>
                    <!-- 用戶詳細資訊 -->
                    <div v-if="user.showDetails" class="mt-3 p-3 bg-light rounded">
                      <h6 class="mb-2">詳細資訊</h6>
                      <div class="row">
                        <div class="col-md-6">
                          <p><strong>用戶ID：</strong>{{ user.id }}</p>
                          <p>
                            <strong>帳號狀態：</strong><span class="badge bg-success">正常</span>
                          </p>
                        </div>
                        <div class="col-md-6">
                          <p><strong>最後登入：</strong>{{ formatDate(user.createdAt) }}</p>
                          <p>
                            <strong>訂單數量：</strong>{{ user.orders ? user.orders.length : 0 }}
                          </p>
                        </div>
                      </div>
                      <!-- 訂單列表 -->
                      <div v-if="user.orders && user.orders.length > 0" class="mt-3">
                        <h6 class="mb-2">訂單記錄</h6>
                        <div class="table-responsive">
                          <table class="table table-sm">
                            <thead>
                              <tr>
                                <th>訂單編號</th>
                                <th>產品</th>
                                <th>狀態</th>
                                <th>金額</th>
                              </tr>
                            </thead>
                            <tbody>
                              <tr v-for="order in user.orders" :key="order.id">
                                <td>{{ order.orderNo }}</td>
                                <td>{{ order.product }}</td>
                                <td>
                                  <span :class="getOrderStatusClass(order.status)">
                                    {{ order.status }}
                                  </span>
                                </td>
                                <td>NT$ {{ order.amount }}</td>
                              </tr>
                            </tbody>
                          </table>
                        </div>
                      </div>
                      <div v-else class="mt-3">
                        <p class="text-muted">尚無訂單記錄</p>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>


        <!-- 評論管理 (僅管理員和商家可見) -->
        <div class="row" v-if="!isGeneralUser">
          <div class="col-12">
            <div class="card shadow-sm">
              <div class="card-header bg-primary text-white">評論管理</div>
              <div class="card-body">
                <div v-for="comment in comments" :key="comment.id" class="mb-3 border-bottom pb-2">
                  <div class="d-flex justify-content-between align-items-center">
                    <div>
                      <strong>{{ comment.user }}</strong> 評論於
                      <span class="text-muted">{{ comment.product }}</span>
                      <div class="text-warning">
                        <span v-for="n in comment.rating" :key="n">★</span>
                        <span v-for="n in 5 - comment.rating" :key="'empty' + n">☆</span>
                      </div>
                      <div>{{ comment.text }}</div>
                      <div v-if="comment.images && comment.images.length" class="mt-1">
                        <img
                          v-for="img in comment.images"
                          :key="img"
                          :src="img"
                          alt="評論圖片"
                          style="
                            width: 90px;
                            height: 80px;
                            object-fit: cover;
                            margin-right: 8px;
                            border-radius: 8px;
                          "
                        />
                      </div>
                    </div>
                    <button
                      class="btn btn-outline-primary btn-sm"
                      @click="replyTo(comment)"
                      v-if="canReplyComment"
                    >
                      回覆
                    </button>
                  </div>
                  <div v-if="comment.showReply && canReplyComment" class="mt-2">
                    <textarea
                      v-model="comment.replyText"
                      class="form-control mb-2"
                      rows="2"
                      placeholder="輸入回覆內容"
                    ></textarea>
                    <button class="btn btn-primary btn-sm" @click="submitReply(comment)">
                      送出回覆
                    </button>
                  </div>
                  <div v-if="comment.reply" class="mt-1 bg-light p-2 rounded">
                    <strong class="text-primary">管理員回覆：</strong>
                    {{ comment.reply }}
                  </div>
                </div>
                <div v-if="!comments.length" class="text-muted">尚無評論</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 檢舉管理 (僅管理員可見) -->
    <div class="row" v-if="!isGeneralUser && isAdmin">
      <div class="col-12">
        <ReportList />
      </div>
    </div>

    <!-- 新增飯店業主 Modal -->
    <div v-if="showAddHotelModal" class="modal fade show d-block" @click="closeAddHotelForm">
      <div class="modal-dialog modal-lg modal-dialog-centered" @click.stop>
        <div class="modal-content">
          <div class="modal-header bg-primary text-white">
            <h5 class="modal-title" id="addHotelModalLabel">
              <i class="bi bi-building me-2"></i>新增飯店商家帳號
            </h5>
            <button
              type="button"
              class="btn-close btn-close-white"
              @click="closeAddHotelForm"
              aria-label="Close"
            ></button>
          </div>
          <div class="modal-body">
            <div class="row">
              <div class="col-md-6 mb-3">
                <label class="form-label">飯店名稱 *</label>
                <input
                  v-model="newHotel.vendor_name"
                  type="text"
                  class="form-control"
                  placeholder="請輸入飯店名稱"
                />
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label">商家帳號 *</label>
                <input
                  v-model="newHotel.username"
                  type="text"
                  class="form-control"
                  placeholder="請輸入商家帳號"
                />
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label">密碼 *</label>
                <input
                  v-model="newHotel.password"
                  type="password"
                  class="form-control"
                  placeholder="請輸入密碼"
                />
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label">確認密碼 *</label>
                <input
                  v-model="newHotel.confirmPassword"
                  type="password"
                  class="form-control"
                  placeholder="請再次輸入密碼"
                />
              </div>
              <div class="col-12 mb-3">
                <label class="form-label">Email *</label>
                <input
                  v-model="newHotel.email"
                  type="email"
                  class="form-control"
                  placeholder="請輸入Email"
                />
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" @click="closeAddHotelForm">取消</button>
            <button
              type="button"
              class="btn btn-primary"
              @click="submitAddHotel"
              :disabled="!isHotelFormValid"
            >
              <i class="bi bi-check-lg me-1"></i>確認新增
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 新增航空公司 Modal -->
    <div
      v-if="showAddAirlineModal"
      class="modal fade show d-block"
      tabindex="-1"
      aria-labelledby="addAirlineModalLabel"
      aria-hidden="true"
      style="background-color: rgba(0, 0, 0, 0.5)"
      @click="closeAddAirlineForm"
    >
      <div class="modal-dialog modal-lg modal-dialog-centered" @click.stop>
        <div class="modal-content">
          <div class="modal-header bg-primary text-white">
            <h5 class="modal-title" id="addAirlineModalLabel">
              <i class="bi bi-airplane me-2"></i>新增航空公司
            </h5>
            <button
              type="button"
              class="btn-close btn-close-white"
              @click="closeAddAirlineForm"
              aria-label="Close"
            ></button>
          </div>
          <div class="modal-body">
            <div class="row">
              <div class="col-md-6 mb-3">
                <label class="form-label">航空公司代碼 *</label>
                <input
                  v-model="newAirline.airline_code"
                  type="text"
                  class="form-control"
                  placeholder="請輸入航空公司代碼"
                />
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label">航空公司名稱 *</label>
                <input
                  v-model="newAirline.airline_name"
                  type="text"
                  class="form-control"
                  placeholder="請輸入航空公司名稱"
                />
              </div>
              <div class="col-12 mb-3">
                <label class="form-label">航空公司圖片 *</label>
                <input
                  type="file"
                  class="form-control"
                  accept="image/*"
                  @change="handleAirlineImageUpload"
                />
                <img
                  v-if="newAirline.imagePreview"
                  :src="newAirline.imagePreview"
                  style="max-width: 120px; margin-top: 8px"
                />
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" @click="closeAddAirlineForm">
              取消
            </button>
            <button
              type="button"
              class="btn btn-primary"
              @click="submitAddAirline"
              :disabled="!isAirlineFormValid"
            >
              <i class="bi bi-check-lg me-1"></i>確認新增
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 新增票卷商 Modal -->
    <div
      v-if="showAddTicketVendorModal"
      class="modal fade show d-block"
      tabindex="-1"
      aria-labelledby="addTicketVendorModalLabel"
      aria-hidden="true"
      style="background-color: rgba(0, 0, 0, 0.5)"
      @click="closeAddTicketVendorForm"
    >
      <div class="modal-dialog modal-lg modal-dialog-centered" @click.stop>
        <div class="modal-content">
          <div class="modal-header bg-primary text-white">
            <h5 class="modal-title" id="addTicketVendorModalLabel">
              <i class="bi bi-ticket-detailed me-2"></i>新增票卷商
            </h5>
            <button
              type="button"
              class="btn-close btn-close-white"
              @click="closeAddTicketVendorForm"
              aria-label="Close"
            ></button>
          </div>
          <div class="modal-body">
            <div class="row">
              <div class="col-12 mb-3">
                <label class="form-label">廠商名稱 *</label>
                <input
                  v-model="newTicketVendor.vendor_name"
                  type="text"
                  class="form-control"
                  placeholder="請輸入廠商名稱"
                />
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label">國家 *</label>
                <select v-model="newTicketVendor.country_id" class="form-select" required>
                  <option value="">請選擇國家</option>
                  <option v-for="country in countries" :key="country.id" :value="country.id">
                    {{ country.label }}
                  </option>
                </select>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label">城市 *</label>
                <select v-model="newTicketVendor.city_id" class="form-select" required>
                  <option value="">請選擇城市</option>
                  <option v-for="city in cities" :key="city.id" :value="city.id">
                    {{ city.label }}
                  </option>
                </select>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" @click="closeAddTicketVendorForm">
              取消
            </button>
            <button
              type="button"
              class="btn btn-primary"
              @click="submitAddTicketVendor"
              :disabled="!isTicketVendorFormValid"
            >
              <i class="bi bi-check-lg me-1"></i>確認新增
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { ref, computed, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import Header from '../components/Header.vue';
import ReportList from '../components/comments/ReportList.vue';
import sh from '@/assets/images/Sheraton1.jpeg';

  const router = useRouter();

  // 使用統一的身分判定
  import { useCurrentUser } from '@/composables/useCurrentUser'
  
  const { 
    currentUser, 
    isAdmin, 
    isVendor, 
    isUser, 
    isAdminOrVendor,
    logout
  } = useCurrentUser()

  // 用戶角色和權限
  const userRole = ref(currentUser.value.userType || '');
  const userName = ref(currentUser.value.userName || '');

  // 登出功能
  const handleLogout = () => {
    // 使用 composable 中的統一登出函數
    logout();
    
    // 跳轉到登入頁面
    router.push('/login');
  };

  // 權限檢查 (使用統一的身分判定)
  const canViewMerchant = computed(() => isAdminOrVendor.value);
  const canAddMerchant = computed(() => isAdmin.value);
  const canManageUser = computed(() => isAdmin.value);
  const canReplyComment = computed(() => isAdminOrVendor.value);
  const isGeneralUser = computed(() => isUser.value);

  // 角色顯示名稱
  const roleDisplayName = computed(() => {
    const roleNames = {
      ADMIN: '系統管理員',
      admin: '系統管理員',
      VENDOR: '商家',
      vendor: '商家',
      USER: '一般用戶',
      user: '一般用戶'
    };
    return `${userName.value} (${roleNames[currentUser.value.userType] || '未知角色'})`;
  });

  // 新增飯店業主表單控制
  const showAddHotelModal = ref(false);
  const newHotel = ref({
    vendor_name: '',
    username: '',
    password: '',
    confirmPassword: '',
    email: ''
  });

  // 飯店類型、城市、國家資料
  const lodgingTypes = ref([]);
  const cities = ref([]);
  const countries = ref([]);

  const isHotelFormValid = computed(() => {
    return (
      newHotel.value.vendor_name &&
      newHotel.value.username &&
      newHotel.value.password &&
      newHotel.value.confirmPassword &&
      newHotel.value.email
    );
  });

  // ESC鍵關閉Modal
  const handleEscKey = (event) => {
    if (event.key === 'Escape' && showAddHotelModal.value) {
      closeAddHotelForm();
    }
  };

  // 檢查權限
  onMounted(async () => {
    console.log('當前用戶角色:', currentUser.value.userType);
    console.log('當前用戶名稱:', currentUser.value.userName);

    if (!isAdmin.value) {
      console.log('權限不足，跳轉到首頁');
      alert('您沒有權限訪問此頁面');
      router.push('/');
      return;
    }

    console.log('權限檢查通過，可以訪問管理頁面');

    // 載入商家資料
    await loadVendors();

    // 載入用戶資料
    await loadUsers();

    // 載入所有下拉選單資料
    fetchLodgingTypes();
    fetchCities();
    fetchCountries();

    // 添加ESC鍵監聽
    document.addEventListener('keydown', handleEscKey);
  });

  onUnmounted(() => {
    // 移除ESC鍵監聽
    document.removeEventListener('keydown', handleEscKey);
  });

  // 商家管理數據
  const merchants = ref([
    {
      id: 1,
      name: '台北君悅酒店',
      owner: '張經理',
      rooms: [
        { id: 1, type: '標準雙人房', stock: 15 },
        { id: 2, type: '豪華套房', stock: 8 },
        { id: 3, type: '總統套房', stock: 2 }
      ]
    },
    {
      id: 2,
      name: '台中林酒店',
      owner: '李經理',
      rooms: [
        { id: 4, type: '商務房', stock: 20 },
        { id: 5, type: '家庭房', stock: 12 }
      ]
    }
  ]);

  // 用戶管理數據
  const users = ref([]);

  const userSearch = ref('');
  const filteredUsers = computed(() => {
    if (!userSearch.value) return users.value;
    return users.value.filter(
      (user) =>
        user.username.toLowerCase().includes(userSearch.value.toLowerCase()) ||
        user.email.toLowerCase().includes(userSearch.value.toLowerCase())
    );
  });

  // 評論管理數據
  const comments = ref([
    {
      id: 1,
      user: '王小明',
      product: '台北君悅酒店',
      rating: 5,
      text: '服務非常好，房間乾淨舒適，位置便利！',
      images: [sh],
      reply: '',
      replyText: '',
      showReply: false
    },
    {
      id: 2,
      user: '李小華',
      product: '台北101觀景台',
      rating: 4,
      text: '景色很棒，但人太多了，建議提前預約。',
      images: [],
      reply: '感謝您的建議，我們會加強人流管理。',
      replyText: '',
      showReply: false
    },
    {
      id: 3,
      user: '張小美',
      product: '台中林酒店',
      rating: 3,
      text: '房間還可以，但隔音不太好。',
      images: [],
      reply: '',
      replyText: '',
      showReply: false
    }
  ]);

  // 用戶管理功能
  const searchUser = () => {
    // 搜尋功能已通過 computed 實現
    console.log('搜尋用戶:', userSearch.value);
  };

  const viewOrders = (user) => {
    user.showOrders = !user.showOrders;
  };

  // 評論管理功能
  const replyTo = (comment) => {
    comment.showReply = !comment.showReply;
    if (comment.showReply) {
      comment.replyText = comment.reply || '';
    }
  };

  const submitReply = (comment) => {
    if (comment.replyText.trim()) {
      comment.reply = comment.replyText;
      comment.showReply = false;
      comment.replyText = '';

      // 這裡可以發送 API 請求保存回覆
      console.log('提交回覆:', comment.reply);
    }
  };

  // 商家管理功能
  const addHotelMerchant = () => {
    // 確保所有下拉選單資料已載入
    if (lodgingTypes.value.length === 0) {
      fetchLodgingTypes();
    }
    if (cities.value.length === 0) {
      fetchCities();
    }
    if (countries.value.length === 0) {
      fetchCountries();
    }
    showAddHotelModal.value = true;
  };

  const addAirlineMerchant = () => {
    // 實現新增商家功能
    console.log('新增航空公司');
    alert('新增航空公司功能開發中...');
  };

  const addTicketMerchant = () => {
    // 實現新增商家功能
    console.log('新增票卷商');
    alert('新增票卷商功能開發中...');
  };

  const addTrafficMerchant = () => {
    // 實現新增商家功能
    console.log('新增交通票商');
    alert('新增交通票商功能開發中...');
  };

  const editMerchant = (merchant) => {
    // 實現編輯商家功能
    console.log('編輯商家:', merchant.name);
    alert(`編輯商家: ${merchant.name}`);
  };

  // 數據統計
  const statistics = ref({
    totalUsers: 1250,
    totalOrders: 3420,
    totalRevenue: 1250000,
    activeMerchants: 45
  });

  const closeAddHotelForm = () => {
    showAddHotelModal.value = false;
    // 重置表單資料
    newHotel.value = {
      vendor_name: '',
      username: '',
      password: '',
      confirmPassword: '',
      email: ''
    };
  };

  const submitAddHotel = async () => {
    try {
      // 驗證密碼
      if (newHotel.value.password !== newHotel.value.confirmPassword) {
        alert('密碼與確認密碼不符');
        return;
      }

      // 準備請求資料
      const requestData = {
        vendorName: newHotel.value.vendor_name,
        username: newHotel.value.username,
        password: newHotel.value.password,
        email: newHotel.value.email
      };

      const response = await fetch('http://localhost:8080/api/admin/create-vendor', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify(requestData)
      });

      if (response.ok) {
        const result = await response.json();
        alert('商家帳號創建成功！');
        closeAddHotelForm();
        // 重新載入商家列表
        await loadVendors();
      } else {
        const errorData = await response.json();
        alert(`創建失敗：${errorData.message || '未知錯誤'}`);
      }
    } catch (error) {
      console.error('創建商家帳號時發生錯誤：', error);
      alert('創建商家帳號時發生錯誤，請稍後再試');
    }
  };

  // 從後端API抓取飯店類型資料
  const fetchLodgingTypes = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/options/lodgings-types');
      if (response.ok) {
        const data = await response.json();
        lodgingTypes.value = data;
        console.log('飯店類型資料:', data);
      } else {
        console.error('抓取飯店類型失敗:', response.status);
      }
    } catch (error) {
      console.error('抓取飯店類型錯誤:', error);
    }
  };

  // 從後端API抓取城市資料
  const fetchCities = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/options/cities');
      if (response.ok) {
        const data = await response.json();
        cities.value = data;
        console.log('城市資料:', data);
      } else {
        console.error('抓取城市資料失敗:', response.status);
      }
    } catch (error) {
      console.error('抓取城市資料錯誤:', error);
    }
  };

  // 從後端API抓取國家資料
  const fetchCountries = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/options/countries');
      if (response.ok) {
        const data = await response.json();
        countries.value = data;
        console.log('國家資料:', data);
      } else {
        console.error('抓取國家資料失敗:', response.status);
      }
    } catch (error) {
      console.error('抓取國家資料錯誤:', error);
    }
  };

  // 新增航空公司表單控制
  const showAddAirlineModal = ref(false);
  const newAirline = ref({
    airline_code: '',
    airline_name: '',
    image: '', // base64
    imagePreview: ''
  });

  const handleAirlineImageUpload = (event) => {
    const file = event.target.files[0];
    if (!file) return;
    const reader = new FileReader();
    reader.onload = (e) => {
      newAirline.value.image = e.target.result.split(',')[1]; // base64
      newAirline.value.imagePreview = e.target.result;
    };
    reader.readAsDataURL(file);
  };

  const isAirlineFormValid = computed(() => {
    return newAirline.value.airline_code && newAirline.value.airline_name && newAirline.value.image;
  });

  const closeAddAirlineForm = () => {
    showAddAirlineModal.value = false;
    newAirline.value = {
      airline_code: '',
      airline_name: '',
      image: '',
      imagePreview: ''
    };
  };

  const submitAddAirline = async () => {
    try {
      if (!isAirlineFormValid.value) {
        alert('請填寫所有必填欄位');
        return;
      }
      const requestData = {
        airlineCode: newAirline.value.airline_code,
        airlineName: newAirline.value.airline_name,
        image: newAirline.value.image // base64
      };
      const response = await fetch('http://localhost:8080/api/airlines/create', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(requestData)
      });
      if (response.ok) {
        alert('航空公司新增成功！');
        closeAddAirlineForm();
      } else {
        alert('航空公司新增失敗');
      }
    } catch (error) {
      alert('新增時發生錯誤');
    }
  };

  // 新增票卷商表單控制
  const showAddTicketVendorModal = ref(false);
  const newTicketVendor = ref({
    vendor_name: '',
    country_id: '',
    city_id: ''
  });

  const isTicketVendorFormValid = computed(() => {
    return (
      newTicketVendor.value.vendor_name &&
      newTicketVendor.value.country_id &&
      newTicketVendor.value.city_id
    );
  });

  const closeAddTicketVendorForm = () => {
    showAddTicketVendorModal.value = false;
    newTicketVendor.value = {
      vendor_name: '',
      country_id: '',
      city_id: ''
    };
  };

  const submitAddTicketVendor = async () => {
    try {
      if (!isTicketVendorFormValid.value) {
        alert('請填寫所有必填欄位');
        return;
      }
      const requestData = {
        vendorName: newTicketVendor.value.vendor_name,
        countryId: parseInt(newTicketVendor.value.country_id),
        cityId: parseInt(newTicketVendor.value.city_id)
      };
      const response = await fetch('http://localhost:8080/api/ticket-vendors/create', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(requestData)
      });
      if (response.ok) {
        alert('票卷商新增成功！');
        closeAddTicketVendorForm();
      } else {
        alert('票卷商新增失敗');
      }
    } catch (error) {
      alert('新增時發生錯誤');
    }
  };

  const vendors = ref([]);

  const loadVendors = async () => {
    try {
      console.log('正在載入商家資料...');
      const response = await fetch('http://localhost:8080/api/admin/vendors', {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      });
      if (response.ok) {
        const data = await response.json();
        vendors.value = data;
        console.log('商家資料載入成功:', data);
      } else {
        console.error('載入商家資料失敗:', response.status);
        vendors.value = [];
      }
    } catch (error) {
      console.error('載入商家資料錯誤:', error);
      vendors.value = [];
    }
  };

  const deleteVendor = async (vendor) => {
    if (
      !confirm(
        `確定要刪除商家 "${
          vendor.name || vendor.vendorName
        }" 嗎？\n\n此操作將永久刪除該商家的所有資料，無法復原。`
      )
    ) {
      return;
    }

    try {
      console.log('正在刪除商家:', vendor.id);
      const response = await fetch(`http://localhost:8080/api/admin/vendors/${vendor.id}`, {
        method: 'DELETE',
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      });

      if (response.ok) {
        alert('商家刪除成功！');
        // 重新載入商家列表
        await loadVendors();
      } else {
        const errorText = await response.text();
        alert(`刪除失敗：${errorText}`);
      }
    } catch (error) {
      console.error('刪除商家時發生錯誤：', error);
      alert('刪除商家時發生錯誤，請稍後再試');
    }
  };

  // 用戶管理功能
  const loadUsers = async () => {
    try {
      console.log('正在載入用戶資料...');
      const response = await fetch('http://localhost:8080/api/admin/users', {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      });
      if (response.ok) {
        const data = await response.json();
        users.value = data;
        console.log('用戶資料載入成功:', data);
      } else {
        console.error('載入用戶資料失敗:', response.status);
        users.value = [];
      }
    } catch (error) {
      console.error('載入用戶資料錯誤:', error);
      users.value = [];
    }
  };

  const deleteUser = async (user) => {
    if (
      !confirm(
        `確定要刪除用戶 "${user.username}" 嗎？\n\n此操作將永久刪除該用戶的所有資料，無法復原。`
      )
    ) {
      return;
    }

    try {
      console.log('正在刪除用戶:', user.id);
      const response = await fetch(`http://localhost:8080/api/admin/users/${user.id}`, {
        method: 'DELETE',
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      });

      if (response.ok) {
        alert('用戶刪除成功！');
        // 重新載入用戶列表
        await loadUsers();
      } else {
        const errorText = await response.text();
        alert(`刪除失敗：${errorText}`);
      }
    } catch (error) {
      console.error('刪除用戶時發生錯誤：', error);
      alert('刪除用戶時發生錯誤，請稍後再試');
    }
  };

  const viewUserDetails = async (user) => {
    user.showDetails = !user.showDetails;

    // 如果展開詳細資訊且還沒有訂單數據，則獲取訂單
    if (user.showDetails && (!user.orders || user.orders.length === 0)) {
      try {
        // 這裡可以調用API獲取用戶的訂單信息
        // 暫時使用模擬數據
        user.orders = [
          {
            id: 1,
            orderNo: 'ORD001',
            product: '台北君悅酒店 - 標準雙人房',
            status: '已確認',
            amount: 5000
          },
          { id: 2, orderNo: 'ORD002', product: '台北101觀景台門票', status: '已完成', amount: 1000 }
        ];
      } catch (error) {
        console.error('獲取用戶訂單失敗:', error);
        user.orders = [];
      }
    }
  };

  const formatDate = (timestamp) => {
    if (!timestamp) return '';
    const date = new Date(timestamp);
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
  };

  const getOrderStatusClass = (status) => {
    switch (status) {
      case '已確認':
        return 'badge bg-success';
      case '已完成':
        return 'badge bg-info';
      case '已取消':
        return 'badge bg-danger';
      default:
        return 'badge bg-secondary';
    }
  };
</script>

<style scoped>
  .admin-container {
    background: #f8fafd;
    min-height: 100vh;
  }

  .card {
    border-radius: 1rem;
    border: none;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  }

  .card-header {
    font-size: 1.1rem;
    font-weight: 600;
    border-radius: 1rem 1rem 0 0 !important;
  }

  /* 統計卡片樣式 */
  .card.bg-primary,
  .card.bg-success,
  .card.bg-warning,
  .card.bg-info {
    border-radius: 1rem;
    transition: transform 0.2s ease;
  }

  .card.bg-primary:hover,
  .card.bg-success:hover,
  .card.bg-warning:hover,
  .card.bg-info:hover {
    transform: translateY(-2px);
  }

  /* 商家管理樣式 */
  .merchant-item {
    transition: background-color 0.2s ease;
  }

  .merchant-item:hover {
    background-color: #f8f9fa;
  }

  /* 用戶管理樣式 */
  .user-item {
    transition: background-color 0.2s ease;
  }

  .user-item:hover {
    background-color: #f8f9fa;
  }

  /* 評論管理樣式 */
  .comment-item {
    transition: background-color 0.2s ease;
  }

  .comment-item:hover {
    background-color: #f8f9fa;
  }

  /* 按鈕樣式 */
  .btn-sm {
    font-size: 0.875rem;
    padding: 0.25rem 0.5rem;
  }

  /* 響應式調整 */
  @media (max-width: 768px) {
    .card-body {
      padding: 1rem;
    }

    .btn-sm {
      font-size: 0.8rem;
      padding: 0.2rem 0.4rem;
    }
  }

  .form-select {
    width: 100%;
    z-index: 1056;
    position: relative;
  }
</style>
