<template>
    <div class="order-result-page d-flex align-items-center justify-content-center vh-100">
        <div class="background-animation">
            <div class="floating-icon" v-for="n in 8" :key="n" :style="{ '--delay': n * 0.5 + 's' }">
                <i :class="getRandomIcon(n)"></i>
            </div>
        </div>

        <div class="gradient-overlay"></div>

        <div class="result-card shadow-lg text-center" style="max-width: 600px; width: 100%;">

            <div v-if="displayStatus === 'success'" class="success-section">
                <div class="icon-container success-icon">
                    <div class="circle-bg"></div>
                    <i class="bi bi-check-circle-fill main-icon"></i>
                    <div class="success-particles">
                        <div class="particle" v-for="n in 6" :key="n" :style="{ '--delay': n * 0.1 + 's' }"></div>
                    </div>
                </div>

                <h2 class="success-title">付款成功！</h2>
                <p class="success-subtitle">感謝您的購買，我們已收到您的付款</p>

                <div class="travel-decoration">
                    <i class="bi bi-airplane travel-icon"></i>
                    <div class="travel-path"></div>
                    <i class="bi bi-geo-alt travel-icon"></i>
                </div>

                <div class="success-message-box">
                    <div class="message-header">
                        <i class="bi bi-calendar-check"></i>
                        <span>付款完成</span>
                    </div>
                    <p class="message-content">謝謝您，我們祝福您在旅程中有個美好的回憶！。</p>
                </div>
            </div>

            <div v-else class="failure-section">
                <div class="icon-container failure-icon">
                    <div class="circle-bg"></div>
                    <i class="bi bi-x-circle-fill main-icon"></i>
                    <div class="failure-waves">
                        <div class="wave" v-for="n in 3" :key="n" :style="{ '--delay': n * 0.2 + 's' }"></div>
                    </div>
                </div>

                <h2 class="failure-title">支付失敗</h2>
                <p class="failure-subtitle">支付過程中發生了錯誤，請再試一次</p>

                <div class="failure-message-box">
                    <div class="message-header">
                        <i class="bi bi-exclamation-triangle"></i>
                        <span>需要協助？</span>
                    </div>
                    <p class="message-content">請檢查您的付款方式或聯繫客服團隊</p>
                </div>
            </div>

            <!-- <div v-if="orderId || tradeAmt" class="payment-details">
                <div class="details-header">
                    <i class="bi bi-receipt"></i>
                    <span>付款詳情</span>
                </div>
                <div class="details-content">
                    <div v-if="orderId" class="detail-item">
                        <span class="label">訂單號碼：</span>
                        <span class="value">{{ orderId }}</span>
                    </div>
                    <div v-if="tradeAmt" class="detail-item">
                        <span class="label">付款金額：</span>
                        <span class="value">NT$ {{ tradeAmt }}</span>
                    </div>
                    <div v-if="paymentDate" class="detail-item">
                        <span class="label">付款時間：</span>
                        <span class="value">{{ paymentDate }}</span>
                    </div>
                </div>
            </div> -->

            <div class="button-section">
                <router-link to="/" class="btn btn-primary btn-travel">
                    <i class="bi bi-house-fill"></i>
                    回到首頁
                </router-link>
                <button v-if="displayStatus !== 'success'" @click="retryPayment"
                    class="btn btn-warning btn-travel retry-btn">
                    <i class="bi bi-arrow-clockwise"></i>
                    重新嘗試支付
                </button>
            </div>

            <div class="bottom-decoration">
                <div class="decoration-line"></div>
                <div class="travel-icons">
                    <i class="bi bi-building"></i>
                    <i class="bi bi-camera"></i>
                    <i class="bi bi-compass"></i>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();

const displayStatus = ref('');
const orderId = ref(null);
const tradeNo = ref('');
const tradeAmt = ref('');
const paymentDate = ref('');
const backendOrderStatus = ref('');
const paymentMethod = ref('');

const message = ref(''); // 用於Line Pay的訊息
const ecpayRtnMsg = ref(''); // 用於綠界的訊息
const ecpayRtnCode = ref(''); // 用於保存綠界的回傳代碼

const decodedMessage = computed(() => {
    try {
        return decodeURIComponent(message.value.replace(/\+/g, ' '));
    } catch (e) {
        console.error("Error decoding Line Pay message:", e);
        return message.value;
    }
});

const decodedEcpayRtnMsg = computed(() => {
    try {
        return decodeURIComponent(ecpayRtnMsg.value.replace(/\+/g, ' '));
    } catch (e) {
        console.error("Error decoding Ecpay message:", e);
        return ecpayRtnMsg.value;
    }
});

// 隨機圖標函數
const getRandomIcon = (index) => {
    const icons = [
        'bi-airplane', 'bi-building', 'bi-camera', 'bi-compass',
        'bi-geo-alt', 'bi-suitcase', 'bi-star', 'bi-heart'
    ];
    return icons[(index % icons.length)];
};

onMounted(() => {
    const query = route.query;

    console.log("從URL接收到的查詢參數:", query);

    if (query.paymentMethod === 'linepay') {
        paymentMethod.value = 'linepay';
        displayStatus.value = query.status || '';
        message.value = query.message || '';
        orderId.value = query.orderId || null;
        tradeNo.value = query.tradeNo || '';
        tradeAmt.value = query.tradeAmt || '';
        paymentDate.value = query.paymentDate || '';
        backendOrderStatus.value = query.orderStatus || '';
    } else { // 假定其他情況是綠界支付
        paymentMethod.value = 'ecpay';

        ecpayRtnCode.value = query.RtnCode || '';

        // 判斷綠界狀態
        // 根據綠界文件，'1' 表示交易成功，其他代碼表示失敗。
        // 確保 RtnCode 是字串 '1'，而不是數字 1
        if (ecpayRtnCode.value === '1') {
            displayStatus.value = 'success';
        } else {
            displayStatus.value = 'fail';
            console.error("綠界支付失敗，RtnCode:", ecpayRtnCode.value, "RtnMsg:", query.RtnMsg);
        }

        ecpayRtnMsg.value = query.RtnMsg || '';
        orderId.value = query.MerchantTradeNo || null;
        tradeAmt.value = query.TradeAmt || '';
        paymentDate.value = query.PaymentDate || ''; // 綠界可能傳 PaymentDate
        backendOrderStatus.value = query.orderStatus || '';
    }

    console.log("Combined Payment Result Page Loaded:");
    console.log("Payment Method:", paymentMethod.value);
    console.log("Display Status:", displayStatus.value);
});

const retryPayment = () => {
    router.push('/checkout');
};
</script>

<style scoped>
/* 基礎背景設定 */
.order-result-page {
    /* 調整為再深一點點的藍色與白色漸變 */
    background: linear-gradient(135deg,
            #4682B4 0%,
            /* 鋼藍色 (Steel Blue) - 比上次深，但比最初淺 */
            #87CEEB 50%,
            /* 天藍色 (Sky Blue) - 淺藍色，作為過渡 */
            #FFFFFF 100%);
    /* 純白色 */
    position: relative;
    overflow: hidden;
    min-height: 100vh;
}

/* 漸層覆蓋 */
.gradient-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: radial-gradient(circle at 30% 40%, rgba(255, 255, 255, 0.2) 0%, transparent 50%),
        radial-gradient(circle at 70% 80%, rgba(255, 255, 255, 0.1) 0%, transparent 50%);
    z-index: 1;
}

/* 背景動畫元素 */
.background-animation {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    pointer-events: none;
    z-index: 2;
}

.floating-icon {
    position: absolute;
    /* 調大 floating-icon 的 font-size */
    font-size: 4rem;
    /* 從 2rem 調整為 4rem，您可以根據需要再調整 */
    /* 調整透明度，讓顏色更明顯 */
    opacity: 0.4;
    /* 從 0.3 調整為 0.4，讓圖標更顯眼 */
    color: #FFFFFF;
    animation: float 6s ease-in-out infinite;
    animation-delay: var(--delay);
}

.floating-icon:nth-child(1) {
    top: 10%;
    left: 10%;
}

.floating-icon:nth-child(2) {
    top: 20%;
    right: 15%;
}

.floating-icon:nth-child(3) {
    top: 50%;
    left: 5%;
}

.floating-icon:nth-child(4) {
    top: 70%;
    right: 10%;
}

.floating-icon:nth-child(5) {
    bottom: 20%;
    left: 20%;
}

.floating-icon:nth-child(6) {
    bottom: 30%;
    right: 25%;
}

.floating-icon:nth-child(7) {
    top: 80%;
    left: 50%;
}

.floating-icon:nth-child(8) {
    top: 30%;
    left: 80%;
}

@keyframes float {

    0%,
    100% {
        transform: translateY(0px) rotate(0deg);
    }

    50% {
        transform: translateY(-20px) rotate(180deg);
    }
}

/* 主要卡片設計 */
.result-card {
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(20px);
    border-radius: 2rem;
    padding: 3rem;
    border: 1px solid rgba(255, 255, 255, 0.2);
    position: relative;
    z-index: 10;
    animation: slideUp 0.8s ease-out;
}

@keyframes slideUp {
    from {
        opacity: 0;
        transform: translateY(50px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

/* 圖標容器 */
.icon-container {
    position: relative;
    margin-bottom: 2rem;
    display: inline-block;
}

.circle-bg {
    width: 120px;
    height: 120px;
    border-radius: 50%;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    animation: pulse 2s infinite;
}

.success-icon .circle-bg {
    background: linear-gradient(45deg, #4CAF50, #8BC34A);
}

.failure-icon .circle-bg {
    background: linear-gradient(45deg, #F44336, #FF9800);
}

.main-icon {
    font-size: 4rem;
    position: relative;
    z-index: 2;
    animation: iconBounce 0.8s ease-out;
}

@keyframes iconBounce {
    0% {
        transform: scale(0);
    }

    50% {
        transform: scale(1.1);
    }

    100% {
        transform: scale(1);
    }
}

@keyframes pulse {
    0% {
        transform: translate(-50%, -50%) scale(1);
        opacity: 0.7;
    }

    50% {
        transform: translate(-50%, -50%) scale(1.05);
        opacity: 0.5;
    }

    100% {
        transform: translate(-50%, -50%) scale(1);
        opacity: 0.7;
    }
}

/* 成功狀態樣式 */
.success-section {
    color: #2E7D32;
}

.success-title {
    font-size: 2.5rem;
    font-weight: 700;
    margin-bottom: 1rem;
    background: linear-gradient(45deg, #4CAF50, #2E7D32);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
}

.success-subtitle {
    font-size: 1.2rem;
    margin-bottom: 2rem;
    color: #666;
}

/* 成功粒子動畫 */
.success-particles {
    position: absolute;
    width: 100%;
    height: 100%;
    pointer-events: none;
}

.particle {
    position: absolute;
    width: 8px;
    height: 8px;
    background: #4CAF50;
    border-radius: 50%;
    animation: particleFloat 2s infinite ease-out;
    animation-delay: var(--delay);
}

.particle:nth-child(1) {
    top: 20%;
    left: 20%;
}

.particle:nth-child(2) {
    top: 30%;
    right: 25%;
}

.particle:nth-child(3) {
    bottom: 25%;
    left: 30%;
}

.particle:nth-child(4) {
    bottom: 35%;
    right: 20%;
}

.particle:nth-child(5) {
    top: 50%;
    left: 10%;
}

.particle:nth-child(6) {
    top: 60%;
    right: 15%;
}

@keyframes particleFloat {
    0% {
        transform: translateY(0) scale(0);
        opacity: 0;
    }

    50% {
        transform: translateY(-30px) scale(1);
        opacity: 1;
    }

    100% {
        transform: translateY(-60px) scale(0);
        opacity: 0;
    }
}

/* 失敗狀態樣式 */
.failure-section {
    color: #D32F2F;
}

.failure-title {
    font-size: 2.5rem;
    font-weight: 700;
    margin-bottom: 1rem;
    background: linear-gradient(45deg, #F44336, #D32F2F);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
}

.failure-subtitle {
    font-size: 1.2rem;
    margin-bottom: 2rem;
    color: #666;
}

/* 失敗波浪動畫 */
.failure-waves {
    position: absolute;
    width: 100%;
    height: 100%;
    pointer-events: none;
}

.wave {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 60px;
    height: 60px;
    border: 2px solid #F44336;
    border-radius: 50%;
    transform: translate(-50%, -50%);
    animation: waveExpand 2s infinite ease-out;
    animation-delay: var(--delay);
}

@keyframes waveExpand {
    0% {
        transform: translate(-50%, -50%) scale(0.8);
        opacity: 0.8;
    }

    100% {
        transform: translate(-50%, -50%) scale(1.5);
        opacity: 0;
    }
}

/* 旅行裝飾 */
.travel-decoration {
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 2rem 0;
    position: relative;
}

.travel-icon {
    font-size: 1.5rem;
    color: #2196F3;
    z-index: 2;
    position: relative;
}

.travel-path {
    flex: 1;
    height: 2px;
    background: linear-gradient(90deg, transparent, #2196F3, transparent);
    margin: 0 1rem;
    position: relative;
    overflow: hidden;
}

.travel-path::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, white, transparent);
    animation: pathShine 3s infinite;
}

@keyframes pathShine {
    0% {
        left: -100%;
    }

    100% {
        left: 100%;
    }
}

/* 訊息框樣式 */
.success-message-box,
.failure-message-box {
    background: rgba(255, 255, 255, 0.8);
    border-radius: 1rem;
    padding: 1.5rem;
    margin: 2rem 0;
    border-left: 4px solid;
    backdrop-filter: blur(10px);
}

.success-message-box {
    border-left-color: #4CAF50;
    background: rgba(76, 175, 80, 0.1);
}

.failure-message-box {
    border-left-color: #F44336;
    background: rgba(244, 67, 54, 0.1);
}

.message-header {
    display: flex;
    align-items: center;
    font-weight: 600;
    margin-bottom: 0.5rem;
    color: #333;
}

.message-header i {
    margin-right: 0.5rem;
}

.message-content {
    color: #666;
    margin: 0;
}

/* 付款詳情 */
.payment-details {
    background: rgba(255, 255, 255, 0.6);
    border-radius: 1rem;
    padding: 1.5rem;
    margin: 2rem 0;
    backdrop-filter: blur(10px);
}

.details-header {
    display: flex;
    align-items: center;
    font-weight: 600;
    margin-bottom: 1rem;
    color: #333;
}

.details-header i {
    margin-right: 0.5rem;
}

.detail-item {
    display: flex;
    justify-content: space-between;
    padding: 0.5rem 0;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}

.detail-item:last-child {
    border-bottom: none;
}

.label {
    color: #666;
    font-weight: 500;
}

.value {
    color: #333;
    font-weight: 600;
}

/* 按鈕樣式 */
.button-section {
    display: flex;
    gap: 1rem;
    justify-content: center;
    margin-top: 2rem;
    flex-wrap: wrap;
}

.btn-travel {
    padding: 0.75rem 2rem;
    border-radius: 50px;
    font-weight: 600;
    text-decoration: none;
    display: inline-flex;
    align-items: center;
    gap: 0.5rem;
    transition: all 0.3s ease;
    border: none;
    position: relative;
    overflow: hidden;
}

.btn-travel::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
    transition: left 0.5s ease;
}

.btn-travel:hover::before {
    left: 100%;
}

.btn-travel:hover {
    transform: translateY(-2px);
    box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
}

.btn-primary {
    background: linear-gradient(45deg, #2196F3, #21CBF3);
    color: white;
}

.btn-warning {
    background: linear-gradient(45deg, #FF9800, #FFC107);
    color: white;
}

/* 底部裝飾 */
.bottom-decoration {
    margin-top: 3rem;
    padding-top: 2rem;
    border-top: 1px solid rgba(0, 0, 0, 0.1);
}

.decoration-line {
    width: 100px;
    height: 2px;
    background: linear-gradient(90deg, transparent, #2196F3, transparent);
    margin: 0 auto 1rem;
}

.travel-icons {
    display: flex;
    justify-content: center;
    gap: 2rem;
    opacity: 0.6;
}

.travel-icons i {
    font-size: 1.5rem;
    color: #2196F3;
    animation: iconFloat 3s ease-in-out infinite;
}

.travel-icons i:nth-child(1) {
    animation-delay: 0s;
}

.travel-icons i:nth-child(2) {
    animation-delay: 0.5s;
}

.travel-icons i:nth-child(3) {
    animation-delay: 1s;
}

@keyframes iconFloat {

    0%,
    100% {
        transform: translateY(0);
    }

    50% {
        transform: translateY(-10px);
    }

    100% {
        transform: translateY(0);
    }
}

/* 響應式設計 */
@media (max-width: 768px) {
    .result-card {
        padding: 2rem;
        margin: 1rem;
    }

    .success-title,
    .failure-title {
        font-size: 2rem;
    }

    .button-section {
        flex-direction: column;
        gap: 0.5rem;
    }

    .btn-travel {
        width: 100%;
        justify-content: center;
    }
}
</style>