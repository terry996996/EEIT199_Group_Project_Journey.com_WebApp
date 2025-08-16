<template>
    <div class="customer-service-page">
        <!-- Header -->
        <Header />

        <!-- 頁面標題 -->
        <!-- <div class="page-header">
            <div class="container">
                <h1 class="page-title">
                    <i class="bi bi-headset text-primary me-3"></i>
                    客戶服務
                </h1>
                <p class="page-subtitle">我們致力於為您提供最優質的旅遊服務體驗</p>
            </div>
        </div> -->

        <!-- 主要內容 -->
        <div class="page-content">
            <div class="container">
                <!-- 客服聯繫元件 -->
                <CustomerService @open-chat="openChat" />
            </div>
        </div>


    </div>
</template>

<script setup>
import { ref } from "vue";
import CustomerService from "@/components/CustomerService.vue";
import Header from "../components/Header.vue";


</script>

<style scoped>
.customer-service-page {
    min-height: 100vh;
    background: var(--bg-secondary);
}

/* .page-header {
    background: linear-gradient(135deg,
            var(--primary-color) 0%,
            var(--primary-dark) 100%);
    color: var(--white);
    padding: var(--spacing-xxl) 0;
    text-align: center;
} */

/* .page-title {
    font-size: 3rem;
    font-weight: var(--font-weight-bold);
    margin-bottom: var(--spacing-md);
    color: var(--white);
} */

/* .page-subtitle {
    font-size: var(--font-size-lg);
    opacity: 0.9;
    margin: 0;
    max-width: 600px;
    margin: 0 auto;
} */

.page-content {
    padding: var(--spacing-xxl) 0;
    margin-top: 30px;
}

.container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 var(--spacing-md);
}

@media (max-width: 768px) {
    .page-title {
        font-size: 2rem;
    }

    .page-subtitle {
        font-size: var(--font-size-base);
    }

    .page-header {
        padding: var(--spacing-xl) 0;
    }

    .page-content {
        padding: var(--spacing-xl) 0;
    }
}
</style>
