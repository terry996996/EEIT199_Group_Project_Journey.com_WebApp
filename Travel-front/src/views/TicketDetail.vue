<template>
  <div class="ticket-detail-page">
    <!-- ✅ 首頁 Header -->
    <Header />

    <div class="container my-5 pt-4">
      <!-- ✅ 基本資訊 -->
      <div class="ticket-info mb-4" v-if="ticket">
        <div class="row">
          <!-- 左欄：標題與資訊 -->
          <div class="col-md-8">
            <h2 class="mb-3">{{ ticket.name }}</h2>
            <div class="mb-3">
              <span class="badge bg-success me-2">{{ ticket.rating }}</span>
              <span class="me-3">{{ ticket.reviews?.length || 0 }} 則評價</span>
              <span class="text-muted">
                <i class="bi bi-geo-alt"></i>{{ ticket.countryName }} {{ ticket.cityName }}
              </span>
            </div>
            <div class="features mb-3">
              <span
                v-for="feature in ticket.features || []"
                :key="feature"
                class="badge bg-light text-dark me-2"
              >
                {{ feature }}
              </span>
            </div>
          </div>

          <!-- 右欄：票價 -->
          <div class="col-md-4 text-end">
            <div class="fs-3 fw-bold text-primary">NT$ {{ ticketPrice }}</div>
            <small class="text-muted">起</small>
          </div>
        </div>
      </div>

      <!-- ✅ 圖片展示區 -->
      <TicketGallery
        v-if="ticket && ticket.images"
        :images="[ticket.imageUrl, ...ticket.images.map((img) => img.imageUrl)]"
      />

      <!-- ✅ 資訊區 -->
      <div v-if="ticket">
        <TicketInfoBar
          :rating="ticket.rating"
          :reviewCount="ticket.comments?.length || 0"
          
          :location="ticket.address"
          :price="ticketPrice"
          :description="ticket.description"
          @scrollToPackage="scrollToPackage"
        />
      </div>
      <!-- ✅ 套票區標題 -->
      <div class="ticket-package">
        <h4 class="align-bottom">選擇方案</h4>
      </div>

      <!-- ✅ 套票列表 -->
      <div v-if="ticket">
        <div v-for="pkg in ticket.packages" :key="pkg.id" class="mb-4">
          <Ticketpackage
            ref="ticketPackageRef"
            :attraction-id="ticket.id" 
            :ticket-package-id="pkg.id"
            :title="pkg.name"
            :image="pkg.imageUrl"
            :price="pkg.types?.[0]?.price || 0"
            :tag="pkg.tag"
            :refundLabel="pkg.refundPolicy"
            :availableDate="pkg.startTime"
            :description="pkg.description"
            :types="pkg.types"
          />
        </div>
      </div>

      <!-- ✅ 主體區塊 -->
      <div class="row">
        <div class="col-md-8">
          <!-- 景點說明 -->
          <div class="card border rounded p-4 py-5" id="section-ticket">
            <h4 class="fw-bold mb-4">景點門票</h4>
            <div class="row mb-3 border-bottom">
              <div class="col-sm-4 fw-bold text-muted">票券類型</div>
              <div class="col-sm-8">紙本票券（直接入場）</div>
            </div>
            <div class="row mb-3 border-bottom">
              <div class="col-sm-4 fw-bold text-muted">景點類型</div>
              <div class="col-sm-8" v-if="ticket?.typeNames?.length && ticket?.tagNames?.length">
              {{ ticket.typeNames[0] }}、{{ ticket.tagNames[0] }}
              </div>
            </div>
            <div class="row border-bottom">
              <div class="col-sm-4 fw-bold text-muted">導覽類型</div>
              <div class="col-sm-8">無導覽</div>
            </div>
          </div>

          <!-- 景點介紹內容 -->

          <div v-if="ticket?.contents?.length">
            <div
              v-for="content in ticket.contents"
              :key="content.contentId"
              :id="`section-${content.contentId}`"
            >
              <!-- 每個介紹區塊 -->
              <TicketContents
                :contentId="content.contentId"
                :title="content.title"
                :description="content.description"
                :editable="isOwner"  
                @updateDescription="(updated) => saveToBackend(content.contentId, updated.title, updated.description)"
                @deleteContent="(contentId) => handleDeleteContent(contentId)"
              />
            </div>
            <!-- ✅ 加入新增按鈕 -->
            <div class="text-center mt-3" v-if="isOwner">
              <button class="btn btn-outline-primary btn-sm" @click="addTicketContentSection">
                ➕ 新增介紹區塊
              </button>
            </div>
          </div>

          <!-- 評價區域 -->
          <div class="reviews" id="section-reviews">
            <!-- 評價表單 (未來要刪除)-->
            <div class="card mb-4" v-if="showReviewForm">
              <div class="card-body">
                <h5 class="card-title mb-3">撰寫評價</h5>
                <form @submit.prevent="submitReview">
                  <div class="mb-3">
                    <label class="form-label">評分</label>
                    <div class="rating-input">
                      <i
                        v-for="n in 5"
                        :key="n"
                        class="bi"
                        :class="n <= newReview.rating ? 'bi-star-fill' : 'bi-star'"
                        @click="newReview.rating = n"
                        style="cursor: pointer; color: #ffc107"
                      ></i>
                    </div>
                  </div>
                  <div class="mb-3">
                    <label class="form-label">評價內容</label>
                    <textarea
                      class="form-control"
                      v-model="newReview.content"
                      rows="3"
                      required
                    ></textarea>
                  </div>
                  <div class="text-end">
                    <button
                      type="button"
                      class="btn btn-outline-secondary me-2"
                      @click="showReviewForm = false"
                    >
                      取消
                    </button>
                    <button type="submit" class="btn btn-primary">提交評價</button>
                  </div>
                </form>
              </div>
            </div>

            <!-- 評價列表 -->
            <div>
              <AllComment v-if="ticket && ticket.id" :target="{ type: 'LODGING', id: ticket.id }" />
            </div>
          </div>
        </div>

        <!-- 右欄：快速導覽 -->
        <div class="col-md-4 d-none d-md-block">
          <div class="quicktour-sticky" v-if="ticket && ticket.contents">
            <QuickTour :contents="ticket.contents" />
          </div>
        </div>
      </div>

      <!-- 協助區 -->
      <div class="col-md-8 mt-4">
        <div class="card p-4 help-card" id="section-help">
          <h5 class="fw-bold mb-3">取得協助</h5>
          <a
            href="http://localhost:5173/customer-service"
            target="_blank"
            class="btn btn-outline-dark"
          >
            journey 幫助中心
          </a>
        </div>
      </div>
      <div class="mt-5">
  <TicketCarousel :title="'推薦景點'" :items="tickets" />
</div>
     
    </div>
  </div>
</template>

<script setup>
  import AllComment from '@/components/comments/AllComment.vue';
  import Footer from '../components/Footer.vue';
  import { ref, computed, onMounted } from 'vue';
  import { useRouter, useRoute } from 'vue-router';
  import Header from '../components/Header.vue';
  import TicketGallery from '../components/attractions/TicketGallery.vue';
  import TicketInfoBar from '../components/attractions/TicketInfoBar.vue';
  import Ticketpackage from '../components/attractions/Ticketpackage.vue';
  import QuickTour from '../components/attractions/QuickTour.vue';
  import TicketContents from '../components/attractions/TicketContents.vue';
  import {
    getTicketContent,
    addTicketContent,
    updateTicketContent,
    deleteTicketContent,
    getMainTicketById,
    getPackagesByTicketId,
    getImagesByTicketId,
    searchSimpleTickets
  } from '@/services/attractions/ticketService';

  import { getTicketById } from '@/services/attractions/ticketService';
  import Swal from 'sweetalert2';
  import TicketCarousel from '@/components/attractions/TicketCarousel.vue'//頁尾推薦
  import { jwtDecode } from 'jwt-decode'//抓取登入人ID

const tickets = ref([])

onMounted(async () => {
  const res = await searchSimpleTickets()
  tickets.value = res.data.map(ticket => ({
    id: ticket.id,
    name: ticket.name,
    location: ticket.address,
    image: ticket.imageUrl,
    price: ticket.minPrice,
    rating: (ticket.views / 10).toFixed(1),
    
  }))
})
// 
  const router = useRouter();
  const showReviewForm = ref(false);

  const ticket = ref(null);
  const ticketContent = ref({ id: null, title: '', description: '' });
  // 從資料庫來的介紹內容
  const route = useRoute();
  const ticketId = route.params.id;

  onMounted(async () => {
  try {
    Swal.fire({
      title: '載入中...',
      text: '正在取得票券資料',
      allowOutsideClick: false,
      allowEscapeKey: false,
      didOpen: () => {
        Swal.showLoading();
      }
    });

    // 取得主表資料
    const res = await getMainTicketById(ticketId);
    const mainData = res.data;

    // 🔁 取得內文
    const contentsRes = await getTicketContent(ticketId);
    const contents = contentsRes.data;
    //取得套票+票種資料
    const packagesRes = await getPackagesByTicketId(ticketId);
    const packages = packagesRes.data;
    //取得圖片
    const imagesRes = await getImagesByTicketId(ticketId);
    const images = imagesRes.data;
    // 👉 合併內容塞入 ticket.value
    ticket.value = {
      ...mainData,
      contents: contents.sort((a, b) => a.contentId - b.contentId),
      packages: packages,
      images: images
    };

    Swal.close();
  } catch (err) {
    console.error('❌ 載入票券失敗', err);
    Swal.fire('❌ 載入失敗', err.message || '無法取得資料', 'error');
  }
});

  // 新評論表單
  const newReview = ref({
    rating: 5,
    content: ''
  });

  // 預訂表單
  const bookingForm = ref({
    date: '',
    tickets: {}
  });
  //內文更新後傳送資料庫API

  const saveToBackend = async (contentId, title, newDescription) => {
    try {
      console.log('🚀 發送更新 API：', contentId, title, newDescription);
      await updateTicketContent(contentId, { title, description: newDescription });

      console.log('✅ 更新成功，準備取得內容列表...');
      const updatedContents = await getTicketContent(ticketId);

      console.log('📦 取得到資料：', updatedContents.data);

      ticket.value = {
        ...ticket.value,
        contents: [...updatedContents.data].sort((a, b) => a.contentId - b.contentId)
      };

      Swal.fire('✅ 更新成功', '', 'success');
    } catch (err) {
      console.error('❌ 更新流程出錯：', err);
      Swal.fire('❌ 更新失敗', err.response?.data?.message || err.message, 'error');
    }
  };

  // 更新票數
  const updateQuantity = (typeId, delta) => {
    const current = bookingForm.value.tickets[typeId] || 0;
    const newValue = current + delta;
    if (newValue >= 0) {
      bookingForm.value.tickets[typeId] = newValue;
    }
  };

  // 計算總價
  const calculateTotal = () => {
    return Object.entries(bookingForm.value.tickets).reduce((total, [typeId, quantity]) => {
      // 搜尋所有票種
      let foundType = null;
      for (const pkg of ticket.value.packages || []) {
        const match = pkg.types?.find((t) => t.id === Number(typeId));
        if (match) {
          foundType = match;
          break;
        }
      }

      return total + (foundType?.price || 0) * quantity;
    }, 0);
  };

  // 檢查預訂是否有效
  const isValidBooking = computed(() => {
    return (
      bookingForm.value.date &&
      Object.values(bookingForm.value.tickets).some((quantity) => quantity > 0)
    );
  });

  // 提交評論
  const submitReview = () => {
    // TODO: 實現評論提交邏輯
    console.log('提交評論：', newReview.value);
    showReviewForm.value = false;
    newReview.value = {
      rating: 5,
      content: ''
    };
  };

  // 前往結帳
  const proceedToCheckout = () => {
    router.push({
      path: '/checkout',
      query: {
        type: 'ticket',
        id: ticket.value.id,
        date: bookingForm.value.date,
        tickets: JSON.stringify(bookingForm.value.tickets)
      }
    });
  };

  // 用來捲動的區塊參考
  const ticketPackageRef = ref([]);

  // 捲動方法
  const scrollToPackage = () => {
    const el = ticketPackageRef.value?.[0]?.$el || ticketPackageRef.value?.[0];
    if (el) {
      const offsetTop = el.getBoundingClientRect().top + window.scrollY - 80;
      window.scrollTo({ top: offsetTop, behavior: 'smooth' });
    }
  };
  //最低價格顯示
  const ticketPrice = computed(() => {
    if (!ticket.value?.packages?.length) return 0;

    const prices = ticket.value.packages.flatMap(
      (pkg) => pkg.types?.map((type) => type.price) || []
    );

    return prices.length ? Math.min(...prices) : 0;
  });

  //
  ///新增內文區塊
  const addTicketContentSection = async () => {
  try {
    await addTicketContent({
      ticketId,
      title: '新增區塊',
      subtitle: '',
      description: '<p>請輸入內容</p>'
    });

    const updatedContents = await getTicketContent(ticketId);
    ticket.value.contents = [...updatedContents.data].sort((a, b) => a.contentId - b.contentId);

    Swal.fire('✅ 新增成功', '', 'success');
  } catch (err) {
    console.error('❌ 新增失敗', err);
    Swal.fire('❌ 新增失敗', err.response?.data?.message || err.message, 'error');
  }
};
//刪除內文
const handleDeleteContent = async (contentId) => {
  console.log('🔥 收到刪除請求 ID：', contentId)
  await deleteTicketContent(contentId)
  const updated = await getTicketContent(ticketId)
  ticket.value.contents = updated.data.sort((a, b) => a.contentId - b.contentId)
}
//內文編輯器出現條件
const token = localStorage.getItem('token')
const currentUserId = ref(null)

if (token) {
  try {
    const decoded = jwtDecode(token)
    currentUserId.value = decoded.id  // ✅ 使用 payload 的 "id"
    console.log('目前登入者 ID:', currentUserId.value)
  } catch (error) {
    console.error('JWT 解碼失敗:', error)
  }
}

// 判斷是否為創建者
const isOwner = computed(() => ticket.value?.createdBy === currentUserId.value)



</script>

<style scoped>
  .booking-card {
    border-radius: 12px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  }

  .ticket-gallery img {
    transition: transform 0.3s;
    cursor: pointer;
  }

  .ticket-gallery img:hover {
    transform: scale(1.02);
  }

  .features .badge {
    font-weight: normal;
  }

  .rating-input i {
    font-size: 1.5rem;
    margin-right: 5px;
  }

  .ticket-package-middle {
    display: flex;
    align-items: flex-end;
    height: 100px;
  }

  .align-bottom {
    margin-bottom: 10px;
    font-weight: bolder;
  }

  /* 快速導覽固定顯示 */
  .quicktour-sticky {
    position: sticky;
    top: 150px;
    /* 距離畫面頂端距離，可自行調整 */
    z-index: 10;
  }
  .help-card {
    background-color: #fff;
    border: 1px solid #ddd;
    border-radius: 10px;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
  }
  .help-card {
    background-color: #fff;
    border: 1px solid #dee2e6;
    border-radius: 12px;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
  }

  .help-card h5 {
    font-size: 1.25rem;
    margin-bottom: 1rem;
  }

  .help-card .btn {
    font-weight: 600;
    font-size: 1rem;
    color: #333;
    background-color: #fff;
    border-color: #ccc;
    transition: all 0.2s ease;
  }

  .help-card .btn:hover {
    color: #0d6efd;
    border-color: #0d6efd;
  }

  @media (max-width: 768px) {
    .booking-card {
      position: static !important;
      margin-top: 2rem;
    }
  }
</style>
