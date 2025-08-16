<template>
  <div class="consults-page">
    <Header />

    <div v-if="isPageLoading" class="loading-overlay">
      <img src="/image/full_blue.png" alt="Loading Logo" class="blinking-logo" />
      <p class="loading-text">載入中...</p>
    </div>

    <main v-else class="main-content">
      <div class="feedback-container">
        <div class="main-layout-wrapper">
          <div class="left-section-wrapper">
            <div class="header-section">
              <h1>專人諮詢與意見回饋</h1>
              <p>
                Journey.com十分重視您的寶貴意見與問題，我們將竭誠儘速為您服務
              </p>
              <p class="security-info">
                為維護資訊安全，訂單相關問題請聯絡客服處理，勿在此輸入您的訂單細節及資料，謝謝！（您可透過客戶服務中心的即時聊天與電話客服和我們聯繫，或來信我們，我們將在接到訊息後盡速為您處理。）
              </p>
              <br><br><br>
            </div>

            <div class="illustration-section">
              <img src="/image/consult&feedback.svg" alt="Feedback Illustration" />
            </div>
          </div>

          <div class="form-section">
            <form @submit.prevent="submitForm">
              <div class="form-group">
                <label for="feedbackType">您想諮詢哪方面的問題呢?*</label>
                <select id="feedbackType" v-model="formData.feedbackTypeId" required :disabled="isLoadingTypes">
                  <option :value="null" disabled selected v-if="formData.feedbackTypeId === null">
                    請選擇
                  </option>

                  <template v-if="!isLoadingTypes && consultTypes.length > 0">
                    <option v-for="type in consultTypes" :key="type.consultTypeId" :value="type.consultTypeId">
                      {{ type.consultTypeName }}
                    </option>
                  </template>

                  <template
                    v-else-if="!isLoadingTypes && consultTypes.length === 0 && formData.feedbackTypeId === null">
                    <option :value="null" disabled selected>無可用類型</option>
                  </template>
                </select>
              </div>

              <div class="form-group">
                <label for="details">告訴我們更多詳情*</label>
                <textarea id="details" v-model="formData.details" rows="6" required></textarea>
              </div>

              <div class="form-group contact-name">
                <label>聯絡人姓名*</label>
                <input type="text" v-model="formData.contactName" required />
                <div class="gender-radio">
                  <label><input type="radio" v-model="formData.gender" value="先生" /> 先生</label>
                  <label><input type="radio" v-model="formData.gender" value="女士" /> 女士</label>
                </div>
              </div>

              <div class="form-group">
                <label for="phoneNumber">聯絡電話*</label>
                <input type="text" id="phoneNumber" v-model="formData.phoneNumber" placeholder="請輸入不含符號的數字，市話分機請以#區隔"
                  required />
              </div>

              <div class="form-group">
                <label for="email">電子信箱*</label>
                <input type="email" id="email" v-model="formData.email" required />
              </div>

              <div class="form-actions">
                <button type="button" class="reset-button" @click="resetForm">重新填寫</button>
                <button type="submit" class="submit-button">確認送出</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useCountriesStore } from '@/stores/countries';
import Swal from 'sweetalert2';
import Header from '../../components/Header.vue';

const countriesStore = useCountriesStore();

const autoFilled = ref(true);

const formData = ref({
  feedbackTypeId: null, // 確保使用 feedbackTypeId，與 v-model 綁定一致
  details: '',
  contactName: '',
  gender: '先生',
  phoneNumber: '',
  email: '',
  userId: null
});

const consultTypes = ref([]);
const isLoadingTypes = ref(true); // 諮詢類型加載狀態，初始為 true
const isLoadingUser = ref(true); // 用戶資訊加載狀態，初始為 true

const isPageLoading = ref(true); // 整體頁面加載狀態，初始為 true


// 設定最小載入時間（毫秒）
const MIN_LOADING_TIME = 500; // 例如，設定為 0.5 秒


// 在組件掛載時獲取用戶資訊和諮詢類型
onMounted(async () => {
  // 設定一個定時器，在 MIN_LOADING_TIME 後隱藏整個頁面載入畫面
  setTimeout(() => {
    isPageLoading.value = false;
  }, MIN_LOADING_TIME);

  // 自動從Profil.vue基本資料頁面中抓用戶的資料(需要user_id寫死為1)
  (async () => {
    try {
      isLoadingUser.value = true;

      const profileRes = await fetch('/api/profile/1');
      if (!profileRes.ok) throw new Error('無法取得用戶基本資料');

      const userInfo = await profileRes.json();

      // 使用 Pinia store 抓國碼資料
      await countriesStore.getData();
      const countries = countriesStore.countries;

      const countryPhoneCode = countries.find(
        (c) => c.id === userInfo.residence
      )?.phoneCode || '';

      // 帶入表單欄位
      formData.value.contactName = `${userInfo.familyName}${userInfo.givenName}`;
      formData.value.gender = userInfo.gender === 1 ? '先生' : '女士';
      formData.value.phoneNumber = `${countryPhoneCode}#${userInfo.telNumber}`;
      formData.value.email = userInfo.email;
      formData.value.userId = 1; // user_id寫死為1

      autoFilled.value = true;
    } catch (error) {
      console.error('載入用戶資料失敗:', error);
    } finally {
      isLoadingUser.value = false;
    }
  })();

  // 獨立啟動獲取諮詢類型的異步操作
  (async () => {
    try {
      isLoadingTypes.value = true;
      const typesResponse = await fetch('/api/consult/types');
      if (typesResponse.ok) {
        consultTypes.value = await typesResponse.json();
      } else {
        console.error('Failed to fetch consult types:', typesResponse.statusText);
      }
    } catch (error) {
      console.error('Error fetching consult types:', error);
    } finally {
      isLoadingTypes.value = false;
    }
  })(); // 立即執行
});


const submitForm = async () => {
  try {
    // 將 formData 發送到後端
    const response = await fetch('/api/consults', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(formData.value) // feedbackTypeId 會傳 ID
    });

    if (response.ok) {
      await new Promise(resolve => setTimeout(resolve, 500));
      Swal.fire({
        icon: 'success',
        title: '我們已收到您的回覆',
        html: '<span style="font-size: 20px; color: black; font-weight: 500;">專人會盡快與您聯繫</span>',
        showConfirmButton: false,
        timer: 2000,
      }).then(() => {
        resetForm();
      });
    } else {
      const errorText = await response.text();
      Swal.fire({
        icon: 'error',
        title: '提交失敗',
        text: `錯誤: ${errorText}`,
        confirmButtonText: '確定'
      });
    }
  } catch (error) {
    console.error('Error submitting form:', error);
    Swal.fire({
      icon: 'error',
      title: '提交失敗',
      text: '網路錯誤或伺服器問題，請稍後再試。',
      confirmButtonText: '確定'
    });
  }
};

const resetForm = () => {
  formData.value = {
    feedbackTypeId: null, // 重置時也清空
    details: '',
    contactName: '',
    gender: '先生',
    phoneNumber: '',
    email: '',
    userId: null
  };
};



</script>

<style scoped>
.main-content {
  padding-top: 80px;
  padding-bottom: 20px;
  min-height: calc(100vh - 76px - 20px);
}

.consults-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.feedback-container {
  font-family: Arial, sans-serif;
  max-width: 1200px;
  margin: 55px auto;
  padding: 20px 40px;
  padding-bottom: 80px;
  /* 增加底部內邊距，放大 Container 區域，增加與 Footer 間距 */
  background-color: transparent;
  border-radius: 0px;
  box-shadow: none;
  transform: scale(1.05);
  transform-origin: top center;
}

/* 最外層的 Flex 容器，用於左右兩欄佈局 */
.main-layout-wrapper {
  display: flex;
  gap: 40px;
  min-height: 750px;
  align-items: stretch;
  flex-wrap: wrap;
}

/* 左側區塊的容器：包含標題和圖片，垂直排列 */
.left-section-wrapper {
  flex: 2;
  /* 佔據左側的空間 */
  min-width: 350px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  height: 100%;
}

/* 「問題與回饋」標題部分：在左側區塊內部 */
.header-section {
  width: 100%;
  text-align: left;
  padding-bottom: 0;
  border-bottom: none;
  margin-bottom: 30px;
}

.header-section h1 {
  color: #333;
  font-size: 34px;
  margin-bottom: 20px;
}

.header-section p {
  color: #666;
  font-size: 20px;
  line-height: 1.6;
}

.header-section .security-info {
  font-size: 16px;
  color: #999;
  margin-top: 20px;
}

/* 左下角插畫圖片區塊 */
.illustration-section {
  width: 100%;
  /* 確保圖片區塊在左側容器內佔滿寬度 */
  display: flex;
  justify-content: flex-start;
  margin-top: auto;
  padding-bottom: 0;
  align-self: flex-end;
}

.illustration-section img {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  display: block;
}

/* 右側表單區塊 */
.form-section {
  flex: 3;
  /* 佔據右側的空間，比例比左側大一些 */
  min-width: 450px;
  background-color: #f8f8f8;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
  min-height: 750px;
  height: auto;
}

.form-group {
  margin-bottom: 32px;
}

.form-group label {
  font-size: 18px;
  display: block;
  margin-bottom: 12px;
  font-weight: bold;
  color: #555;
}

.form-group input[type="text"],
.form-group input[type="email"],
.form-group select,
.form-group textarea {
  width: calc(100% - 20px);
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 18px;
  box-sizing: border-box;
}

.form-group textarea {
  resize: vertical;
  min-height: 130px;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
  border-color: #007bff;
  outline: none;
  box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
}

.contact-name {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 15px;
}

.contact-name label:first-child {
  flex-basis: 100%;
  margin-bottom: 11px;
}

.contact-name input[type="text"] {
  flex: 1;
  min-width: 150px;
  margin-top: -12px;
}

.gender-radio {
  display: flex;
  gap: 12px;
  align-items: center;
  flex-wrap: nowrap;
  white-space: nowrap;
}

.gender-radio label {
  display: flex;
  align-items: center;
  gap: 6px;
  flex-wrap: nowrap;
  font-weight: normal;
  color: #333;
  font-size: 18px;
}

.gender-radio input[type="radio"] {
  width: auto;
  margin-right: 5px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
  margin-top: 30px;
}

.form-actions button {
  padding: 12px 25px;
  border: none;
  border-radius: 5px;
  font-size: 17px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.reset-button {
  background-color: #6c757d;
  color: white;
}

.reset-button:hover {
  background-color: #5a6268;
}

.submit-button {
  background-color: #28a745;
  color: white;
}

.submit-button:hover {
  background-color: #218838;
}

.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(255, 255, 255, 0.9);
  /* 半透明白色背景 */
  display: flex;
  flex-direction: column;
  /* 讓內容垂直堆疊：Logo 在上，文字在下 */
  justify-content: center;
  /* 垂直居中 */
  align-items: center;
  /* 水平居中 */
  z-index: 9999;
  /* 確保它在最上層 */
  backdrop-filter: blur(5px);
  /* 添加模糊效果 */
}

.blinking-logo {
  width: 150px;
  /* 調整 Logo 大小 */
  height: auto;
  margin-bottom: 20px;
  /* Logo 和文字之間的間距 */
  animation: blink-animation 1.5s infinite alternate;
  /* 正確的動畫名稱 */
}

.loading-text {
  color: #333;
  font-size: 1.8em;
  font-weight: bold;
}

/* 將 @keyframes 的名稱從 blink 改為 blink-animation */
@keyframes blink-animation {
  0% {
    opacity: 1;
  }

  50% {
    opacity: 0.3;
  }

  100% {
    opacity: 1;
  }
}

/* RWD 調整 */
@media (max-width: 768px) {
  .main-layout-wrapper {
    flex-direction: column;
    /* 小螢幕時垂直堆疊 */
    align-items: center;
    /* 內容居中 */
    min-height: unset;
    /* 小螢幕時取消最小高度限制 */
  }

  .left-section-wrapper {
    width: 100%;
    min-width: unset;
    align-items: center;
    /* 小螢幕下左側內容居中 */
    height: auto;
    /* 小螢幕時高度自適應 */
    padding-bottom: 0;
  }

  .header-section {
    text-align: center;
    /* 小螢幕下標題居中 */
    width: 80%;
    /* 適當縮小寬度 */
    margin-bottom: 20px;
    /* 小螢幕時調整間距 */
  }

  .illustration-section {
    margin-top: 20px;
    /* 小螢幕時圖片與上方內容的間距 */
    justify-content: center;
    /* 小螢幕下圖片居中 */
    padding-bottom: 0;
    align-self: center;
    /* 小螢幕下圖片在交叉軸上居中 */
  }

  .illustration-section img {
    max-width: 80%;
  }

  .form-section {
    width: 100%;
    min-width: unset;
    padding: 20px;
    height: auto;
    /* 確保小螢幕時也是自動高度 */
    min-height: auto;
    /* 小螢幕時讓高度由內容決定，避免過高 */
    padding-bottom: 40px;
    /* 小螢幕時的底部填充 */
  }

  .contact-name {
    flex-direction: column;
    align-items: flex-start;
  }

  .contact-name input[type="text"] {
    width: 100%;
  }

  .gender-radio {
    margin-top: 10px;
  }

  .form-actions {
    flex-direction: column;
    align-items: stretch;
  }

  .form-actions button {
    width: 100%;
    margin-bottom: 10px;
  }

  .feedback-container {
    min-height: unset;
    /* 小螢幕時取消最小高度限制 */
    padding-bottom: 40px;
    /* 小螢幕時調整底部填充 */
  }
}
</style>