<template>
  <footer class="footer">
    <div class="footer-content">
      <div class="container">
        <div class="footer-grid">
          <!-- 公司資訊 -->
          <div class="footer-section">
            <div class="footer-logo">
              <img src="/src/assets/images/FullLogo_Transparent.png" alt="Journey.com" height="40" />
            </div>
            <p class="footer-description">
              Journey.com 是您值得信賴的旅遊夥伴，提供最優質的機票、飯店、門票和交通服務。
            </p>
            <div class="social-links">
              <a href="#" class="social-link" title="Facebook">
                <i class="bi bi-facebook"></i>
              </a>
              <a href="#" class="social-link" title="Instagram">
                <i class="bi bi-instagram"></i>
              </a>
              <a href="#" class="social-link" title="Twitter">
                <i class="bi bi-twitter"></i>
              </a>
              <a href="#" class="social-link" title="YouTube">
                <i class="bi bi-youtube"></i>
              </a>
            </div>
          </div>

          <!-- 產品服務 -->
          <div class="footer-section">
            <h5 class="footer-title">產品服務</h5>
            <ul class="footer-links">
              <li><router-link to="/flights">機票預訂</router-link></li>
              <li><router-link to="/hotels">飯店預訂</router-link></li>
              <li><router-link to="/tickets">景點門票</router-link></li>
              <li><router-link to="/traffic">交通票券</router-link></li>
              <li><router-link to="/packages">旅遊套裝</router-link></li>
              <li><router-link to="/insurance">旅遊保險</router-link></li>
            </ul>
          </div>

          <!-- 客戶服務 -->
          <div class="footer-section">
            <h5 class="footer-title">客戶服務</h5>
            <ul class="footer-links">
              <li>
                <router-link to="/customer-service">聯絡我們</router-link>
              </li>
              <li><a href="#" @click="openChat">即時客服</a></li>
              <li><router-link to="/help">幫助中心</router-link></li>
              <li><router-link to="/faq">常見問題</router-link></li>
              <li><router-link to="/feedback">意見回饋</router-link></li>
              <li><router-link to="/complaints">申訴管道</router-link></li>
            </ul>
          </div>

          <!-- 關於我們 -->
          <div class="footer-section">
            <h5 class="footer-title">關於我們</h5>
            <ul class="footer-links">
              <li><router-link to="/about">公司介紹</router-link></li>
              <li><router-link to="/careers">徵才資訊</router-link></li>
              <li><router-link to="/press">新聞中心</router-link></li>
              <li><router-link to="/partners">合作夥伴</router-link></li>
              <li><router-link to="/investors">投資人關係</router-link></li>
              <li><router-link to="/sustainability">永續發展</router-link></li>
            </ul>
          </div>

          <!-- 法律條款 -->
          <div class="footer-section">
            <h5 class="footer-title">法律條款</h5>
            <ul class="footer-links">
              <li><router-link to="/terms">使用條款</router-link></li>
              <li><router-link to="/privacy">隱私政策</router-link></li>
              <li><router-link to="/cookies">Cookie 政策</router-link></li>
              <li><router-link to="/accessibility">無障礙聲明</router-link></li>
              <li><router-link to="/security">安全中心</router-link></li>
              <li><router-link to="/legal">法律聲明</router-link></li>
            </ul>
          </div>
        </div>

        <!-- 下載 App -->
        <div class="app-download">
          <div class="app-content">
            <div class="app-info">
              <h5>下載 Journey.com App</h5>
              <p>隨時隨地預訂您的完美旅程</p>
            </div>
            <div class="app-buttons">
              <a href="#" class="app-btn">
                <i class="bi bi-apple"></i>
                <div>
                  <span class="app-label">下載於</span>
                  <span class="app-store">App Store</span>
                </div>
              </a>
              <a href="#" class="app-btn">
                <i class="bi bi-google-play"></i>
                <div>
                  <span class="app-label">下載於</span>
                  <span class="app-store">Google Play</span>
                </div>
              </a>
            </div>
          </div>
        </div>

        <!-- 底部資訊 -->
        <div class="footer-bottom">
          <div class="footer-bottom-content">
            <div class="copyright">
              <p>&copy; 2024 Journey.com. 保留所有權利。</p>
            </div>
            <div class="footer-bottom-links">
              <a href="#">網站地圖</a>
              <a href="#">語言設定</a>
              <a href="#">幣別設定</a>
              <a href="#">地區設定</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup>
import { ref } from 'vue'

// 開啟聊天
const openChat = () => {
  if (window.openChat) {
    window.openChat()
  }
}

</script>

<style scoped>
.footer {
  background: var(--gray-800);
  color: var(--white);
  position: relative;
}

.footer-content {
  padding: var(--spacing-xxl) 0 var(--spacing-xl);
}

.footer-grid {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr 1fr;
  gap: var(--spacing-xl);
  margin-bottom: var(--spacing-xxl);
}

.footer-section {
  display: flex;
  flex-direction: column;
}

.footer-logo {
  margin-bottom: var(--spacing-lg);
}

.footer-description {
  color: var(--gray-300);
  line-height: var(--line-height-relaxed);
  margin-bottom: var(--spacing-lg);
}

.social-links {
  display: flex;
  gap: var(--spacing-md);
}

.social-link {
  width: 40px;
  height: 40px;
  background: var(--gray-700);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--white);
  text-decoration: none;
  transition: all var(--transition-fast);
}

.social-link:hover {
  background: var(--primary-color);
  color: var(--white);
  transform: translateY(-2px);
}

.footer-title {
  font-weight: var(--font-weight-semibold);
  margin-bottom: var(--spacing-lg);
  color: var(--white);
}

.footer-links {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-links li {
  margin-bottom: var(--spacing-sm);
}

.footer-links a {
  color: var(--gray-300);
  text-decoration: none;
  transition: color var(--transition-fast);
  font-size: var(--font-size-sm);
}

.footer-links a:hover {
  color: var(--primary-color);
}

.app-download {
  background: var(--gray-700);
  border-radius: var(--radius-lg);
  padding: var(--spacing-xl);
  margin-bottom: var(--spacing-xl);
}

.app-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.app-info h5 {
  margin: 0 0 var(--spacing-sm) 0;
  font-weight: var(--font-weight-semibold);
}

.app-info p {
  margin: 0;
  color: var(--gray-300);
}

.app-buttons {
  display: flex;
  gap: var(--spacing-md);
}

.app-btn {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  background: var(--gray-600);
  color: var(--white);
  text-decoration: none;
  padding: var(--spacing-sm) var(--spacing-md);
  border-radius: var(--radius-md);
  transition: all var(--transition-fast);
}

.app-btn:hover {
  background: var(--primary-color);
  color: var(--white);
  transform: translateY(-1px);
}

.app-btn i {
  font-size: 1.5rem;
}

.app-label {
  display: block;
  font-size: var(--font-size-xs);
  color: var(--gray-300);
}

.app-store {
  display: block;
  font-weight: var(--font-weight-semibold);
}

.footer-bottom {
  border-top: 1px solid var(--gray-700);
  padding-top: var(--spacing-lg);
}

.footer-bottom-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.copyright p {
  margin: 0;
  color: var(--gray-400);
  font-size: var(--font-size-sm);
}

.footer-bottom-links {
  display: flex;
  gap: var(--spacing-lg);
}

.footer-bottom-links a {
  color: var(--gray-400);
  text-decoration: none;
  font-size: var(--font-size-sm);
  transition: color var(--transition-fast);
}

.footer-bottom-links a:hover {
  color: var(--primary-color);
}

@media (max-width: 1024px) {
  .footer-grid {
    grid-template-columns: 1fr 1fr;
    gap: var(--spacing-lg);
  }
}

@media (max-width: 768px) {
  .footer-grid {
    grid-template-columns: 1fr;
    gap: var(--spacing-lg);
  }

  .app-content {
    flex-direction: column;
    text-align: center;
    gap: var(--spacing-lg);
  }

  .app-buttons {
    justify-content: center;
  }

  .footer-bottom-content {
    flex-direction: column;
    gap: var(--spacing-md);
    text-align: center;
  }

  .footer-bottom-links {
    flex-wrap: wrap;
    justify-content: center;
  }

}
</style>
