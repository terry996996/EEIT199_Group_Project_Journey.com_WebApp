<template>
  <div class="customer-service-container">
    <div class="service-header">
      <h2 class="service-title">
        <i class="bi bi-headset text-primary me-2"></i>
        客戶服務中心
      </h2>
      <p class="service-subtitle">我們隨時為您提供專業的旅遊諮詢服務</p>
    </div>

    <div class="contact-methods">
      <div class="row g-4">
        <div class="col-md-6 col-lg-3">
          <div class="contact-card" @click="openChat">
            <div class="contact-icon">
              <i class="bi bi-chat-dots"></i>
            </div>
            <h5>線上客服</h5>
            <p>即時客服聊天室</p>
            <span class="status online">線上</span>
          </div>
        </div>

        <div class="col-md-6 col-lg-3">
          <div class="contact-card" @click="callService">
            <div class="contact-icon">
              <i class="bi bi-telephone"></i>
            </div>
            <h5>電話客服</h5>
            <p>0800-123-456</p>
            <span class="status online">平日服務時間: 09:00-18:00</span>
          </div>
        </div>

        <div class="col-md-6 col-lg-3">
          <div class="contact-card" @click="sendEmail">
            <div class="contact-icon">
              <i class="bi bi-envelope"></i>
            </div>
            <h5>電子郵件</h5>
            <p>service@journey.com</p>
            <span class="status">24小時內回覆</span>
          </div>
        </div>

        <div class="col-md-6 col-lg-3">
          <div class="contact-card" @click="openConsult">
            <div class="contact-icon">
              <i class="bi bi-pencil-square"></i>
            </div>
            <h5>專人諮詢與意見回饋</h5>
            <p>填寫表單與我們聯繫</p>
            <span class="status">將由專人與您聯繫</span>
          </div>
        </div>
      </div>
    </div>

    <div class="service-hours">
      <h3 class="hours-title">專人服務時間</h3>
      <div class="hours-grid">
        <div class="hours-item">
          <div class="hours-day">週一至週五</div>
          <div class="hours-time">09:00 - 18:00</div>
        </div>
        <div class="hours-item">
          <div class="hours-day">週六</div>
          <div class="hours-time">10:00 - 16:00</div>
        </div>
        <div class="hours-item">
          <div class="hours-day">週日</div>
          <div class="hours-time">10:00 - 16:00</div>
        </div>
        <div class="hours-item">
          <div class="hours-day">緊急服務</div>
          <div class="hours-time">請撥打電話，我們接到通知將立即與您聯繫</div>
        </div>
      </div>
    </div>

    <div class="faq-section">
      <h3 class="faq-title">常見問題</h3>
      <div class="faq-list">
        <div v-for="(faq, index) in faqs" :key="index" class="faq-item" :class="{ active: activeFaq === index }">
          <div class="faq-question" @click="toggleFaq(index)">
            <span>{{ faq.question }}</span>
            <i class="bi" :class="activeFaq === index ? 'bi-chevron-up' : 'bi-chevron-down'"></i>
          </div>
          <div class="faq-answer" v-show="activeFaq === index">
            <p>{{ faq.answer }}</p>
          </div>
        </div>
      </div>
    </div>

    <div class="app-download-promo-trip-like">
      <div class="row align-items-center">
        <div class="col-lg-5 col-md-6 mobile-mockup-col">
          <img src="/image/CRM.png" alt="App Screenshot" class="app-screenshot img-fluid">
        </div>
        <div class="col-lg-7 col-md-6 promo-content-col">
          <h3 class="promo-title-trip-like">下載 App 享受快速與個人化的客服協助！</h3>
          <div class="qr-download-area">
            <div class="qr-code-wrapper">
              <img src="/image/qrcode.png" alt="QR Code" class="qr-code-trip-like">
              <p class="market-hint">掃描行動條碼</p>
            </div>
            <ul class="feature-list">
              <li><i class="bi bi-check-circle me-2"></i>一鍵獲得幫助</li>
              <li><i class="bi bi-check-circle me-2"></i>輕鬆管理訂單</li>
              <li><i class="bi bi-check-circle me-2"></i>App 內免費通話</li>
              <li><a href="#" class="more-reasons">更多下載理由<i class="bi bi-chevron-right ms-2"></i></a></li>
            </ul>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, reactive } from "vue";
import { useRouter } from 'vue-router';

const emit = defineEmits(["open-chat"]);

const router = useRouter();

// 響應式資料
const activeFaq = ref(null);

// 常見問題
const faqs = [
  {
    question: "如何取消或修改訂單？",
    answer:
      "您可以登入會員中心，在「我的訂單」中查看訂單詳情並進行取消或修改。部分訂單可能需要聯繫客服協助處理。",
  },
  {
    question: "退款需要多久時間？",
    answer:
      "退款處理時間通常為 3-7 個工作天，實際到帳時間可能因銀行作業而有所不同。",
  },
  {
    question: "可以提前多久預訂？",
    answer:
      "飯店通常可提前 1 年預訂，機票可提前 11 個月預訂，門票和交通票可提前 6 個月預訂。",
  },
  {
    question: "如何獲得優惠券？",
    answer:
      "您可以透過註冊會員、完成訂單評價、關注我們的社群媒體等方式獲得優惠券。",
  },
  {
    question: "支援哪些付款方式？",
    answer:
      "我們支援信用卡、金融卡、行動支付、銀行轉帳等多種付款方式，確保您的付款安全。",
  },
  {
    question: "如何申請發票？",
    answer:
      "訂單完成後，您可以在會員中心的「發票管理」中申請電子發票或紙本發票。",
  },
];

// 切換 FAQ
const toggleFaq = (index) => {
  activeFaq.value = activeFaq.value === index ? null : index;
};

// 開啟聊天室
const openChat = () => {
  window.open('/chatroom', '_blank');
};

// 撥打電話
const callService = () => {
  window.open("tel:0800-123-456");
};

// 發送郵件
const sendEmail = () => {
  window.open("mailto:service@journey.com?subject=客戶服務諮詢");
};

// 開啟專人諮詢與意見回饋
const openConsult = () => {
  router.push("/consults");
};
</script>

<style scoped>
/* 既有樣式 (省略重複部分，只保留關鍵或有調整的) */
.customer-service-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: var(--spacing-lg) var(--spacing-sm);
}

.service-header {
  text-align: center;
  margin-bottom: var(--spacing-xxl);
}

.service-title {
  font-size: 2.5rem;
  font-weight: var(--font-weight-bold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-md);
}

.service-subtitle {
  font-size: var(--font-size-lg);
  color: var(--text-secondary);
  margin: 0;
}

.contact-methods {
  margin-bottom: var(--spacing-xxl);
}

.contact-card {
  background: var(--bg-primary);
  border-radius: var(--radius-lg);
  padding: var(--spacing-xl);
  text-align: center;
  box-shadow: var(--shadow-md);
  transition: all var(--transition-normal);
  cursor: pointer;
  height: 100%;
  border: 2px solid transparent;
}

.contact-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--shadow-xl);
  border-color: var(--primary-color);
}

.contact-icon {
  width: 80px;
  height: 80px;
  background: var(--primary-light);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto var(--spacing-lg);
  transition: all var(--transition-normal);
}

.contact-card:hover .contact-icon {
  background: var(--primary-color);
  color: var(--white);
}

.contact-icon i {
  font-size: 2rem;
  color: var(--primary-color);
  transition: color var(--transition-normal);
}

.contact-card:hover .contact-icon i {
  color: var(--white);
}

.contact-card h5 {
  font-weight: var(--font-weight-semibold);
  margin-bottom: var(--spacing-sm);
  color: var(--text-primary);
}

.contact-card p {
  color: var(--text-secondary);
  margin-bottom: var(--spacing-md);
}

.status {
  display: inline-block;
  padding: var(--spacing-xs) var(--spacing-sm);
  background: var(--gray-100);
  color: var(--text-secondary);
  border-radius: var(--radius-pill);
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-medium);
}

.status.online {
  background: rgba(40, 167, 69, 0.1);
  color: var(--success-color);
}

.faq-section {
  margin-bottom: var(--spacing-xxl);
}

.faq-title {
  text-align: center;
  margin-bottom: var(--spacing-xl);
  font-weight: var(--font-weight-bold);
  color: var(--text-primary);
}

.faq-list {
  max-width: 800px;
  margin: 0 auto;
}

.faq-item {
  background: var(--bg-primary);
  border-radius: var(--radius-lg);
  margin-bottom: var(--spacing-md);
  box-shadow: var(--shadow-sm);
  overflow: hidden;
  transition: all var(--transition-normal);
}

.faq-item:hover {
  box-shadow: var(--shadow-md);
}

.faq-item.active {
  box-shadow: var(--shadow-lg);
}

.faq-question {
  padding: var(--spacing-lg);
  cursor: pointer;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: var(--font-weight-semibold);
  color: var(--text-primary);
  transition: background-color var(--transition-fast);
}

/* 調整 FAQ 問題文字大小 */
.faq-question span {
  font-size: 1.15rem;
}

.faq-question i {
  color: var(--primary-color);
  transition: transform var(--transition-fast);
}

.faq-item.active .faq-question i {
  transform: rotate(180deg);
}

.faq-answer {
  padding: 0 var(--spacing-lg) var(--spacing-lg);
  color: var(--text-secondary);
  line-height: var(--line-height-relaxed);
}

/* 調整 FAQ 答案文字大小 */
.faq-answer p {
  font-size: 1.05rem;
}


.service-hours {
  margin-bottom: var(--spacing-xxl);
}

.hours-title {
  text-align: center;
  margin-bottom: var(--spacing-xl);
  font-weight: var(--font-weight-bold);
  color: var(--text-primary);
}

.hours-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: var(--spacing-lg);
  max-width: 600px;
  margin: 0 auto;
}

.hours-item {
  background: var(--bg-primary);
  padding: var(--spacing-lg);
  border-radius: var(--radius-lg);
  text-align: center;
  box-shadow: var(--shadow-sm);
  transition: all var(--transition-normal);
}

.hours-item:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.hours-day {
  font-weight: var(--font-weight-semibold);
  font-size: 1.3rem;
  margin-bottom: var(--spacing-sm);
}

.hours-time {
  font-size: 1.1rem;
  font-weight: var(--font-weight-medium);
}

/* App 下載推廣區域  */
.app-download-promo-trip-like {
  background-color: var(--white);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-lg);
  padding: var(--spacing-l);
  margin-top: var(--spacing-xxl);
  display: flex;
  align-items: center;
  justify-content: center;
}

.mobile-mockup-col {
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url('image_9e4141.png');
  background-size: cover;
  background-position: center;
  border-radius: var(--radius-lg);
  overflow: hidden;
  height: 450px;
}

.app-screenshot {
  max-height: 95%;
  max-width: 90%;
  object-fit: contain;
  filter: drop-shadow(0 10px 20px rgba(0, 0, 0, 0.1));
  border-radius: var(--radius-lg);
}

.promo-content-col {
  padding-left: var(--spacing-xxl);
  text-align: left;
}

.promo-title-trip-like {
  font-size: 1.8rem;
  font-weight: var(--font-weight-bold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-xl);
  line-height: 1.4;
}

.qr-download-area {
  display: flex;
  align-items: flex-start;
  gap: var(--spacing-xxl);
}

.qr-code-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  flex-shrink: 0;
}

.download-icon {
  font-size: 1.5rem;
  color: var(--primary-color);
  margin-bottom: var(--spacing-xs);
}

.download-text {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
  margin-bottom: var(--spacing-sm);
}

.qr-code-trip-like {
  width: 150px;
  height: 150px;
  border: 1px solid var(--gray-200);
  border-radius: var(--radius-sm);
  background-color: var(--white);
  margin-bottom: var(--spacing-sm);
}

.market-hint {
  font-size: var(--font-size-base);
  color: var(--text-secondary);
  margin: 0;
}

.feature-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.feature-list li {
  font-size: 1.1rem;
  color: var(--text-primary);
  margin-bottom: var(--spacing-md);
  display: flex;
  align-items: center;
}

.feature-list li i {
  color: var(--success-color);
  font-size: 1.3rem;
}

.more-reasons {
  color: var(--primary-color);
  text-decoration: none;
  font-weight: var(--font-weight-semibold);
  display: inline-flex;
  align-items: center;
  transition: color var(--transition-normal);
  font-size: 1.1rem;
}

.more-reasons:hover {
  color: var(--primary-dark);
}

/* 響應式調整 */
@media (max-width: 991.98px) {
  .app-download-promo-trip-like {
    padding: var(--spacing-lg);
  }

  .mobile-mockup-col {
    height: 400px;
    margin-bottom: var(--spacing-xl);
  }

  .promo-content-col {
    padding-left: 0;
    text-align: center;
  }

  .promo-title-trip-like {
    font-size: 1.8rem;
  }

  .qr-download-area {
    flex-direction: column;
    align-items: center;
    gap: var(--spacing-xl);
  }

  .feature-list {
    text-align: left;
  }

  /* 響應式調整 FAQ 問題文字大小 */
  .faq-question span {
    font-size: 1.1rem;
  }

  /* 響應式調整 FAQ 答案文字大小 */
  .faq-answer p {
    font-size: 1rem;
  }

  /* 響應式調整服務時間框框內文字大小 */
  .hours-day {
    font-size: 1rem;
  }

  .hours-time {
    font-size: 1.2rem;
  }
}

@media (max-width: 767.98px) {
  .service-title {
    font-size: 2rem;
  }

  .contact-card {
    padding: var(--spacing-lg);
  }

  .contact-icon {
    width: 60px;
    height: 60px;
  }

  .contact-icon i {
    font-size: 1.5rem;
  }

  .hours-grid {
    grid-template-columns: 1fr;
  }

  .app-download-promo-trip-like {
    padding: var(--spacing-md);
  }

  .mobile-mockup-col {
    height: 350px;
  }

  .promo-title-trip-like {
    font-size: 1.6rem;
  }

  .qr-code-trip-like {
    width: 130px;
    height: 130px;
  }

  .feature-list li {
    font-size: 1rem;
  }

  .feature-list li i {
    font-size: 1.2rem;
  }

  .market-hint {
    font-size: var(--font-size-sm);
  }

  /* 響應式調整 FAQ 問題文字大小 */
  .faq-question span {
    font-size: 1rem;
  }

  /* 響應式調整 FAQ 答案文字大小 */
  .faq-answer p {
    font-size: 0.95rem;
  }

  /* 響應式調整服務時間框框內文字大小 */
  .hours-day {
    font-size: 0.95rem;
  }

  .hours-time {
    font-size: 1.1rem;
  }
}
</style>