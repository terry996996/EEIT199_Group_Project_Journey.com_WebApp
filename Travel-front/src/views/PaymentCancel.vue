<template>
    <div class="payment-cancel-page-wrapper d-flex align-items-center justify-content-center vh-100">
        <div class="background-animation">
            <div class="floating-icon" v-for="n in 8" :key="n" :style="{ '--delay': n * 0.5 + 's' }">
                <i :class="getRandomIcon(n)"></i>
            </div>
        </div>

        <div class="gradient-overlay"></div>

        <div class="cancel-container shadow-lg text-center" style="max-width: 500px; width: 90%;">
            <div class="cancel-icon">⚠️</div>
            <h1 class="cancel-title">付款已取消</h1>
            <p class="cancel-message">
                <span class="message-highlight">{{ message || '您已取消本次付款操作' }}</span>
            </p>

            <div class="btn-group">
                <router-link to="/" class="btn btn-primary-gradient">返回首頁</router-link>
                <button @click="goBack" class="btn btn-success-gradient">返回上頁</button>
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

<script>
export default {
    name: 'PaymentCancel',
    data() {
        return {
            message: '' // 如果綠界有傳參數回來，可以從 URL 獲取
        };
    },
    created() {
        const params = new URLSearchParams(window.location.search);
        this.message = params.get('message'); // 如果綠界有傳 message 參數
    },
    methods: {
        goBack() {
            window.history.back(); // 使用瀏覽器歷史記錄返回
        },
        // 隨機圖標函數，用於背景動畫
        getRandomIcon(index) {
            const icons = [
                'bi-airplane', 'bi-building', 'bi-camera', 'bi-compass',
                'bi-geo-alt', 'bi-suitcase', 'bi-star', 'bi-heart'
            ];
            return icons[(index % icons.length)];
        }
    }
};
</script>

<style scoped>
/* 頁面整體包裝器 - 包含背景漸層和居中佈局 */
.payment-cancel-page-wrapper {
    /* 橘黃色的漸層背景 */
    background: linear-gradient(135deg,
        #FFD700 0%, /* 黃金色 */
        #FFA500 50%, /* 橘色 */
        #FF8C00 100%); /* 暗橘色 */
    min-height: 100vh; /* 確保佔滿整個視窗高度 */
    width: 100%;
    display: flex;
    justify-content: center; /* 水平居中 */
    align-items: center; /* 垂直居中 */
    position: relative; /* 確保 z-index 能正常工作 */
    overflow: hidden; /* 防止內容溢出 */
}

/* 漸層覆蓋層 */
.gradient-overlay {
    position: fixed; /* 固定在視窗中 */
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    /* 兩個放射狀漸層，創造光影效果 */
    background: radial-gradient(circle at 30% 40%, rgba(255, 255, 255, 0.2) 0%, transparent 50%),
                radial-gradient(circle at 70% 80%, rgba(255, 255, 255, 0.1) 0%, transparent 50%);
    z-index: 1; /* 在背景圖標之上，主卡片之下 */
}

/* 背景動畫元素：浮動圖標的容器 */
.background-animation {
    position: fixed; /* 固定在視窗中 */
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    pointer-events: none; /* 讓其不阻擋滑鼠事件 */
    z-index: 0; /* 在所有內容和漸層之下，作為最底層的背景 */
}

/* 浮動圖標單個樣式 */
.floating-icon {
    position: absolute;
    font-size: 4rem; /* 圖標大小，與付款結果頁面保持一致 */
    opacity: 0.4; /* 透明度，與付款結果頁面保持一致 */
    color: #FFFFFF; /* 白色圖標，在橘黃色背景上依然清晰 */
    animation: float 6s ease-in-out infinite; /* 浮動動畫 */
    animation-delay: var(--delay); /* 延遲，讓每個圖標錯開 */
}

/* 浮動圖標的初始位置 */
.floating-icon:nth-child(1) { top: 10%; left: 10%; }
.floating-icon:nth-child(2) { top: 20%; right: 15%; }
.floating-icon:nth-child(3) { top: 50%; left: 5%; }
.floating-icon:nth-child(4) { top: 70%; right: 10%; }
.floating-icon:nth-child(5) { bottom: 20%; left: 20%; }
.floating-icon:nth-child(6) { bottom: 30%; right: 25%; }
.floating-icon:nth-child(7) { top: 80%; left: 50%; }
.floating-icon:nth-child(8) { top: 30%; left: 80%; }

/* 浮動動畫關鍵幀 */
@keyframes float {
    0%, 100% { transform: translateY(0px) rotate(0deg); }
    50% { transform: translateY(-20px) rotate(180deg); } /* 上下浮動並旋轉 */
}

/* 取消內容容器 - 頁面主要顯示的卡片區塊 */
.cancel-container {
    background: white; /* 白色背景 */
    padding: 40px; /* 內邊距 */
    border-radius: 12px; /* 圓角 */
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1); /* 柔和的陰影效果 */
    text-align: center; /* 文字居中 */
    box-sizing: border-box; /* 確保 padding 和 border 包含在 width 內 */
    margin: 20px; /* 外邊距，確保在小螢幕上有呼吸空間 */
    border: 1px solid #eee; /* 增加一個非常淺的邊框，提升質感 */
    z-index: 10; /* 確保在背景和漸層之上 */
    position: relative; /* 確保 z-index 能正常工作 */
    animation: fadeInScale 0.6s ease-out; /* 卡片入場動畫 */
}

/* 卡片入場動畫 */
@keyframes fadeInScale {
    from {
        opacity: 0;
        transform: scale(0.95) translateY(20px);
    }
    to {
        opacity: 1;
        transform: scale(1) translateY(0);
    }
}

/* 取消圖示 - 大黃色驚嘆號 */
.cancel-icon {
    color: #f39c12; /* 鮮明的黃色 */
    font-size: 3.5rem; /* 較大的字體大小 */
    margin-bottom: 25px; /* 圖示下方間距 */
    text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.05); /* 輕微的文字陰影，增加立體感 */
    display: inline-block; /* 確保能應用 transform */
    animation: iconBounce 0.8s ease-out; /* 彈跳動畫 */
}

/* 圖示彈跳動畫 */
@keyframes iconBounce {
    0% { transform: scale(0.8); opacity: 0; }
    50% { transform: scale(1.1); opacity: 1; }
    100% { transform: scale(1); }
}

/* 取消標題 */
.cancel-title {
    font-size: 2.2rem; /* 標題字體大小 */
    margin-bottom: 18px; /* 標題下方間距 */
    font-weight: 600; /* 較粗的字體 */
    color: #333; /* 深灰色文字，易於閱讀 */
}

/* 取消訊息文字 */
.cancel-message {
    color: #555; /* 訊息文字顏色 */
    margin-bottom: 35px; /* 訊息下方間距 */
    font-size: 1.15rem; /* 訊息字體大小 */
    line-height: 1.6; /* 增加行高，提升閱讀舒適度 */
    /* 背景色塊的 padding 和 border-radius */
    display: inline-block; /* 讓 span 能應用 padding 和 background */
    background-color: #eaa7b7; /* 淺灰色底色 */
    padding: 10px 15px; /* 內邊距 */
    border-radius: 8px; /* 圓角 */
    box-shadow: inset 0 1px 3px rgba(0,0,0,0.05); /* 輕微內陰影，增加立體感 */
}

/* 針對訊息文字本身，確保顏色清晰 */
.cancel-message .message-highlight {
    color: #333333;
}

/* 按鈕群組容器 */
.btn-group {
    display: flex; /* 使用 Flexbox 佈局 */
    gap: 12px; /* 按鈕之間的間距 */
    justify-content: center; /* 水平居中對齊 */
    margin-top: 30px; /* 按鈕上方間距 */
    flex-wrap: wrap; /* 小螢幕時換行 */
}

/* 通用按鈕基礎樣式 */
.btn {
    display: inline-block;
    padding: 12px 25px; /* 調整按鈕內邊距 */
    text-decoration: none; /* 移除下劃線 */
    border-radius: 25px; /* 更圓的按鈕 */
    font-weight: 600; /* 較粗的字體 */
    transition: all 0.3s ease; /* 平滑的過渡效果 */
    border: none; /* 移除預設邊框 */
    color: white; /* 確保文字顏色為白色 */
    position: relative;
    overflow: hidden; /* 確保漸層效果在按鈕範圍內 */
}

/* 按鈕懸停特效：白色光澤從左到右掃過 */
.btn::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
    transition: left 0.5s ease;
}

.btn:hover::before {
    left: 100%;
}

/* 按鈕懸停特效：輕微上浮和陰影 */
.btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
}

/* 紫色漸層按鈕 (返回首頁) */
.btn-primary-gradient {
    background: linear-gradient(45deg, #8A2BE2, #A020F0); /* 藍紫色到紫羅蘭色 */
}

.btn-primary-gradient:hover {
    background: linear-gradient(45deg, #7A1DD0, #9010E0); /* 懸停時顏色稍深 */
}

/* 綠色漸層按鈕 (返回上頁) */
.btn-success-gradient {
    background: linear-gradient(45deg, #28a745, #218838); /* 綠色到深綠色 */
}

.btn-success-gradient:hover {
    background: linear-gradient(45deg, #218838, #1a712e); /* 懸停時顏色稍深 */
}


/* 底部裝飾區塊 */
.bottom-decoration {
    margin-top: 3rem; /* 上方間距 */
    padding-top: 2rem; /* 內部上方間距 */
    border-top: 1px solid rgba(0, 0, 0, 0.1); /* 頂部淺色分隔線 */
}

/* 裝飾線條 */
.decoration-line {
    width: 100px;
    height: 2px;
    background: linear-gradient(90deg, transparent, #2196F3, transparent); /* 藍色漸層線條 */
    margin: 0 auto 1rem; /* 水平居中並下方間距 */
}

/* 旅行 icon 群組 */
.travel-icons {
    display: flex;
    justify-content: center; /* 水平居中 */
    gap: 2rem; /* icon 之間間距 */
    opacity: 0.6; /* 輕微透明 */
}

/* 單個旅行 icon 樣式 */
.travel-icons i {
    font-size: 1.5rem; /* icon 大小 */
    color: #2196F3; /* 藍色 */
    animation: iconFloat 3s ease-in-out infinite; /* 彈跳動畫 */
}

/* icon 彈跳動畫的延遲 */
.travel-icons i:nth-child(1) { animation-delay: 0s; }
.travel-icons i:nth-child(2) { animation-delay: 0.5s; }
.travel-icons i:nth-child(3) { animation-delay: 1s; }

/* icon 彈跳動畫關鍵幀 */
@keyframes iconFloat {
    0%, 100% { transform: translateY(0); }
    50% { transform: translateY(-10px); }
}


/* 響應式設計 */
@media (max-width: 768px) {
    .cancel-container {
        padding: 30px; /* 小螢幕時內邊距稍小 */
        margin: 15px; /* 小螢幕時外邊距稍小 */
    }

    .cancel-title {
        font-size: 2rem; /* 小螢幕時標題字體稍小 */
    }

    .cancel-message {
        font-size: 1rem; /* 小螢幕時訊息字體稍小 */
        padding: 8px 12px; /* 訊息底色塊內邊距 */
    }

    .btn-group {
        flex-direction: column; /* 小螢幕時按鈕垂直堆疊 */
        gap: 10px; /* 垂直間距 */
        margin-top: 20px; /* 調整間距 */
    }

    .btn {
        width: 100%; /* 小螢幕時按鈕佔滿寬度 */
        justify-content: center; /* 按鈕文字居中 */
    }

    .bottom-decoration {
        margin-top: 2rem; /* 調整底部裝飾間距 */
        padding-top: 1.5rem;
    }

    .travel-icons {
        gap: 1.5rem; /* 調整 icon 間距 */
    }

    .travel-icons i {
        font-size: 1.2rem; /* 調整 icon 大小 */
    }
}
</style>