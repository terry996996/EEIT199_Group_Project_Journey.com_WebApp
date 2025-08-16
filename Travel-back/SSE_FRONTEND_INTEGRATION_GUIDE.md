# 前端 SSE 即時通知整合指南

## 📋 目錄
1. [概述](#概述)
2. [技術架構](#技術架構)
3. [安裝與設定](#安裝與設定)
4. [核心服務](#核心服務)
5. [Vue.js 整合](#vuejs-整合)
6. [使用範例](#使用範例)
7. [測試指南](#測試指南)
8. [常見問題](#常見問題)
9. [API 參考](#api-參考)

---

## 概述

本指南說明如何在前端應用程式中整合 SSE (Server-Sent Events) 即時通知功能，實現檢舉系統的即時通知。

### 功能特色
- ✅ 即時檢舉通知
- ✅ 檢舉處理狀態更新
- ✅ 自動重連機制
- ✅ 多用戶類型支援
- ✅ 連接狀態管理
- ✅ 錯誤處理

### 支援的通知類型
- **新檢舉通知**：用戶提交檢舉時通知管理員
- **檢舉處理通知**：管理員處理檢舉時通知檢舉者
- **系統通知**：一般系統訊息

---

## 技術架構

### 後端 SSE 端點
```
GET /api/sse/user/connect     - 一般用戶連接
GET /api/sse/admin/connect    - 管理員連接
DELETE /api/sse/user/disconnect  - 斷開連接
```

### 前端技術需求
- JavaScript ES6+
- Vue.js 2.x 或 3.x
- Axios (用於 API 調用)

---

## 安裝與設定

### 1. 複製 SSE 服務檔案

將以下檔案複製到您的專案中：

```bash
# 複製到 src/services/ 目錄
cp sse-notification-service.js src/services/

# 複製到 src/plugins/ 目錄 (Vue.js 專用)
cp vue-sse-plugin.js src/plugins/
```

### 2. 在 main.js 中註冊插件

```javascript
// src/main.js
import { createApp } from 'vue'
import App from './App.vue'
import { SSEPlugin } from './plugins/vue-sse-plugin.js'

const app = createApp(App)
app.use(SSEPlugin)
app.mount('#app')
```

---

## 核心服務

### SSE 通知服務 (sse-notification-service.js)

```javascript
/**
 * SSE 即時通知服務
 * 適用於任何 JavaScript 框架
 */
class SSENotificationService {
    constructor() {
        this.eventSource = null;
        this.isConnected = false;
        this.reconnectAttempts = 0;
        this.maxReconnectAttempts = 5;
        this.reconnectDelay = 3000;
        this.baseUrl = process.env.VUE_APP_API_BASE_URL || 'http://localhost:8080';
        this.onMessageCallback = null;
        this.onErrorCallback = null;
        this.onConnectCallback = null;
        this.onDisconnectCallback = null;
    }

    /**
     * 建立 SSE 連接
     * @param {string} userType 用戶類型 ('user' 或 'admin')
     * @param {string} token JWT token
     * @param {Object} callbacks 回調函數
     */
    connect(userType, token, callbacks = {}) {
        // 設定回調函數
        this.onMessageCallback = callbacks.onMessage || this.defaultMessageHandler;
        this.onErrorCallback = callbacks.onError || this.defaultErrorHandler;
        this.onConnectCallback = callbacks.onConnect || this.defaultConnectHandler;
        this.onDisconnectCallback = callbacks.onDisconnect || this.defaultDisconnectHandler;

        if (!token) {
            console.error('❌ SSE 連接失敗: 缺少 JWT Token');
            this.onErrorCallback(new Error('缺少 JWT Token'));
            return;
        }

        if (this.isConnected) {
            console.log('⚠️ SSE 連接已存在，先斷開舊連接');
            this.disconnect();
        }

        try {
            console.log('🔗 正在建立 SSE 連接...');
            
            // 建立 EventSource 連接
            const endpoint = userType === 'admin' ? '/api/sse/admin/connect' : '/api/sse/user/connect';
            const url = `${this.baseUrl}${endpoint}?token=${encodeURIComponent(token)}`;
            this.eventSource = new EventSource(url);

            // 連接建立成功
            this.eventSource.onopen = (event) => {
                console.log('✅ SSE 連接建立成功');
                this.isConnected = true;
                this.reconnectAttempts = 0;
                this.onConnectCallback();
            };

            // 連接錯誤
            this.eventSource.onerror = (error) => {
                console.error('❌ SSE 連接錯誤:', error);
                this.isConnected = false;
                this.onErrorCallback(error);
                
                // 自動重連機制
                this.handleReconnect(userType, token, callbacks);
            };

            // 設定事件監聽器
            this.setupEventListeners();

        } catch (error) {
            console.error('❌ 建立 SSE 連接失敗:', error);
            this.onErrorCallback(error);
        }
    }

    /**
     * 設定事件監聽器
     */
    setupEventListeners() {
        // 監聽檢舉相關事件
        this.eventSource.addEventListener('newReport', (event) => {
            this.handleEvent('newReport', event);
        });

        this.eventSource.addEventListener('reportProcessed', (event) => {
            this.handleEvent('reportProcessed', event);
        });

        this.eventSource.addEventListener('reportStatusUpdate', (event) => {
            this.handleEvent('reportStatusUpdate', event);
        });

        // 通用訊息處理
        this.eventSource.onmessage = (event) => {
            this.handleEvent('message', event);
        };
    }

    /**
     * 處理事件
     */
    handleEvent(eventType, event) {
        try {
            const data = JSON.parse(event.data);
            console.log(`📨 收到 ${eventType} 事件:`, data);
            
            if (this.onMessageCallback) {
                this.onMessageCallback(eventType, data);
            }
        } catch (error) {
            console.error('❌ 解析事件資料失敗:', error);
        }
    }

    /**
     * 自動重連機制
     */
    handleReconnect(userType, token, callbacks) {
        if (this.reconnectAttempts < this.maxReconnectAttempts) {
            this.reconnectAttempts++;
            console.log(`🔄 嘗試重連 (${this.reconnectAttempts}/${this.maxReconnectAttempts})...`);
            
            setTimeout(() => {
                this.connect(userType, token, callbacks);
            }, this.reconnectDelay);
        } else {
            console.error('❌ 重連次數已達上限，停止重連');
        }
    }

    /**
     * 斷開連接
     */
    disconnect() {
        if (this.eventSource) {
            this.eventSource.close();
            this.eventSource = null;
            this.isConnected = false;
            this.reconnectAttempts = 0;
            console.log('🔌 SSE 連接已斷開');
            
            if (this.onDisconnectCallback) {
                this.onDisconnectCallback();
            }
        }
    }

    /**
     * 檢查連接狀態
     */
    getConnectionStatus() {
        return {
            isConnected: this.isConnected,
            reconnectAttempts: this.reconnectAttempts,
            maxReconnectAttempts: this.maxReconnectAttempts
        };
    }

    // 預設回調函數
    defaultMessageHandler(eventType, data) {
        console.log(`📨 預設訊息處理: ${eventType}`, data);
    }

    defaultErrorHandler(error) {
        console.error('❌ 預設錯誤處理:', error);
    }

    defaultConnectHandler() {
        console.log('✅ 預設連接處理');
    }

    defaultDisconnectHandler() {
        console.log('🔌 預設斷開處理');
    }
}

// 建立單例實例
const sseService = new SSENotificationService();

export default sseService;
```

### Vue.js 插件 (vue-sse-plugin.js)

```javascript
/**
 * Vue.js SSE 插件
 * 支援 Vue 2 和 Vue 3
 */

// SSE 服務實例
import sseService from './sse-notification-service.js';

// Vue 3 插件
const SSEPlugin = {
    install(app, options = {}) {
        // 全域屬性
        app.config.globalProperties.$sse = sseService;
        
        // 提供給 Composition API
        app.provide('sseService', sseService);
        
        // 全域方法
        app.config.globalProperties.$connectSSE = (userType, token, callbacks) => {
            return sseService.connect(userType, token, callbacks);
        };
        
        app.config.globalProperties.$disconnectSSE = () => {
            return sseService.disconnect();
        };
        
        app.config.globalProperties.$getSSEStatus = () => {
            return sseService.getConnectionStatus();
        };
    }
};

// Vue 2 插件
const SSEPluginV2 = {
    install(Vue, options = {}) {
        // 全域屬性
        Vue.prototype.$sse = sseService;
        
        // 全域方法
        Vue.prototype.$connectSSE = (userType, token, callbacks) => {
            return sseService.connect(userType, token, callbacks);
        };
        
        Vue.prototype.$disconnectSSE = () => {
            return sseService.disconnect();
        };
        
        Vue.prototype.$getSSEStatus = () => {
            return sseService.getConnectionStatus();
        };
    }
};

// 自動檢測 Vue 版本
let SSEPluginFinal = SSEPlugin;
if (typeof Vue !== 'undefined' && Vue.version && Vue.version.startsWith('2')) {
    SSEPluginFinal = SSEPluginV2;
}

export { SSEPluginFinal as SSEPlugin };
export default SSEPluginFinal;
```

---

## Vue.js 整合

### 1. 登入組件整合

```vue
<!-- src/components/Login.vue -->
<template>
  <div class="login-container">
    <form @submit.prevent="handleLogin" class="login-form">
      <h2>用戶登入</h2>
      
      <div class="form-group">
        <label>用戶名</label>
        <input 
          v-model="username" 
          type="text" 
          required 
          placeholder="請輸入用戶名"
        />
      </div>
      
      <div class="form-group">
        <label>密碼</label>
        <input 
          v-model="password" 
          type="password" 
          required 
          placeholder="請輸入密碼"
        />
      </div>
      
      <button type="submit" :disabled="loading">
        {{ loading ? '登入中...' : '登入' }}
      </button>
      
      <div v-if="error" class="error-message">
        {{ error }}
      </div>
    </form>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      username: '',
      password: '',
      loading: false,
      error: ''
    }
  },
  
  methods: {
    async handleLogin() {
      this.loading = true;
      this.error = '';
      
      try {
        // 登入 API 調用
        const response = await this.$http.post('/api/auth/login', {
          username: this.username,
          password: this.password
        });
        
        const { token, userType } = response.data;
        
        // 儲存 token 和用戶資訊
        localStorage.setItem('token', token);
        localStorage.setItem('userType', userType);
        this.$store.commit('setUser', { token, userType });
        
        // 建立 SSE 連接
        this.connectSSE(token, userType);
        
        // 導向主頁面
        this.$router.push('/dashboard');
        
      } catch (error) {
        console.error('登入失敗:', error);
        this.error = error.response?.data?.message || '登入失敗，請檢查帳號密碼';
      } finally {
        this.loading = false;
      }
    },
    
    connectSSE(token, userType) {
      // 使用 SSE 插件建立連接
      this.$connectSSE(userType.toLowerCase(), token, {
        onConnect: () => {
          console.log('✅ SSE 連接成功');
          this.$notify.success('即時通知已啟用');
        },
        onError: (error) => {
          console.error('❌ SSE 連接錯誤:', error);
          this.$notify.error('即時通知連接失敗');
        },
        onMessage: (eventType, data) => {
          this.handleSSENotification(eventType, data);
        },
        onDisconnect: () => {
          console.log('🔌 SSE 連接斷開');
        }
      });
    },
    
    handleSSENotification(eventType, data) {
      switch (eventType) {
        case 'newReport':
          this.$notify.warning(`新檢舉通知: ${data.message}`);
          // 觸發全域事件，通知其他組件更新
          this.$root.$emit('new-report', data);
          break;
          
        case 'reportProcessed':
          this.$notify.success(`檢舉處理完成: ${data.message}`);
          this.$root.$emit('report-processed', data);
          break;
          
        case 'reportStatusUpdate':
          this.$notify.info(`檢舉狀態更新: ${data.message}`);
          this.$root.$emit('report-status-update', data);
          break;
          
        default:
          console.log('收到未知通知類型:', eventType, data);
      }
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f5f5;
}

.login-form {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
}

.form-group input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

button {
  width: 100%;
  padding: 0.75rem;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
}

button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.error-message {
  color: #dc3545;
  margin-top: 1rem;
  text-align: center;
}
</style>
```

### 2. 檢舉管理組件

```vue
<!-- src/components/ReportManagement.vue -->
<template>
  <div class="report-management">
    <div class="header">
      <h2>檢舉管理</h2>
      <div class="connection-status" :class="{ connected: isConnected }">
        <span class="status-dot"></span>
        {{ isConnected ? '即時通知已連接' : '即時通知未連接' }}
      </div>
    </div>
    
    <!-- 篩選器 -->
    <div class="filters">
      <select v-model="statusFilter" @change="loadReports">
        <option value="">全部狀態</option>
        <option value="PENDING">待處理</option>
        <option value="RESOLVED">已處理</option>
        <option value="REJECTED">已駁回</option>
      </select>
      
      <button @click="loadReports" :disabled="loading">
        {{ loading ? '載入中...' : '重新載入' }}
      </button>
    </div>
    
    <!-- 檢舉列表 -->
    <div class="reports-list">
      <div v-if="loading" class="loading">
        載入中...
      </div>
      
      <div v-else-if="reports.length === 0" class="empty-state">
        暫無檢舉資料
      </div>
      
      <div v-else>
        <div 
          v-for="report in reports" 
          :key="report.reportId"
          class="report-item"
          :class="report.status.toLowerCase()"
        >
          <div class="report-header">
            <h3>檢舉 #{{ report.reportId }}</h3>
            <span class="status-badge" :class="report.status.toLowerCase()">
              {{ getStatusLabel(report.status) }}
            </span>
          </div>
          
          <div class="report-content">
            <p><strong>檢舉原因:</strong> {{ report.reasonText }}</p>
            <p><strong>檢舉者:</strong> {{ report.reporter.username }}</p>
            <p><strong>檢舉時間:</strong> {{ formatDate(report.createdAt) }}</p>
            
            <div class="reported-comment">
              <h4>被檢舉留言:</h4>
              <p>{{ report.reportedComment.content }}</p>
              <small>作者: {{ report.reportedComment.author.username }}</small>
            </div>
            
            <div v-if="report.note" class="note">
              <strong>處理備註:</strong> {{ report.note }}
            </div>
          </div>
          
          <div class="report-actions">
            <button 
              v-if="report.status === 'PENDING'"
              @click="handleReport(report.reportId, 'RESOLVED')"
              class="btn-resolve"
            >
              確認檢舉
            </button>
            <button 
              v-if="report.status === 'PENDING'"
              @click="handleReport(report.reportId, 'REJECTED')"
              class="btn-reject"
            >
              駁回檢舉
            </button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 分頁 -->
    <div class="pagination">
      <button 
        @click="changePage(-1)" 
        :disabled="currentPage === 0"
      >
        上一頁
      </button>
      <span>第 {{ currentPage + 1 }} 頁</span>
      <button 
        @click="changePage(1)" 
        :disabled="reports.length < pageSize"
      >
        下一頁
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ReportManagement',
  data() {
    return {
      reports: [],
      loading: false,
      statusFilter: '',
      currentPage: 0,
      pageSize: 20,
      isConnected: false
    }
  },
  
  mounted() {
    this.loadReports();
    this.checkSSEConnection();
    
    // 監聽 SSE 通知
    this.$root.$on('new-report', this.handleNewReport);
    this.$root.$on('report-processed', this.handleReportProcessed);
    this.$root.$on('report-status-update', this.handleStatusUpdate);
  },
  
  beforeUnmount() {
    // 清理事件監聽器
    this.$root.$off('new-report', this.handleNewReport);
    this.$root.$off('report-processed', this.handleReportProcessed);
    this.$root.$off('report-status-update', this.handleStatusUpdate);
  },
  
  methods: {
    async loadReports() {
      this.loading = true;
      
      try {
        const params = {
          page: this.currentPage,
          size: this.pageSize
        };
        
        if (this.statusFilter) {
          params.status = this.statusFilter;
        }
        
        const response = await this.$http.get('/api/comment-reports/admin', { params });
        this.reports = response.data;
        
      } catch (error) {
        console.error('載入檢舉失敗:', error);
        this.$notify.error('載入檢舉失敗');
      } finally {
        this.loading = false;
      }
    },
    
    async handleReport(reportId, status) {
      try {
        const note = status === 'RESOLVED' ? '檢舉確認，留言已下架' : '檢舉駁回';
        
        await this.$http.put(`/api/comment-reports/admin/${reportId}`, {
          status: status,
          note: note
        });
        
        this.$notify.success('檢舉處理成功');
        this.loadReports(); // 重新載入列表
        
      } catch (error) {
        console.error('處理檢舉失敗:', error);
        this.$notify.error('處理檢舉失敗');
      }
    },
    
    changePage(delta) {
      this.currentPage += delta;
      this.loadReports();
    },
    
    checkSSEConnection() {
      const status = this.$getSSEStatus();
      this.isConnected = status.isConnected;
    },
    
    // SSE 事件處理
    handleNewReport(data) {
      console.log('收到新檢舉通知:', data);
      // 如果當前在檢舉列表頁面，重新載入資料
      if (this.$route.name === 'ReportManagement') {
        this.loadReports();
      }
    },
    
    handleReportProcessed(data) {
      console.log('收到檢舉處理通知:', data);
      this.loadReports();
    },
    
    handleStatusUpdate(data) {
      console.log('收到狀態更新通知:', data);
      this.loadReports();
    },
    
    // 工具方法
    getStatusLabel(status) {
      const labels = {
        'PENDING': '待處理',
        'RESOLVED': '已處理',
        'REJECTED': '已駁回'
      };
      return labels[status] || status;
    },
    
    formatDate(dateString) {
      return new Date(dateString).toLocaleString('zh-TW');
    }
  }
}
</script>

<style scoped>
.report-management {
  padding: 2rem;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.connection-status {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  background-color: #f8f9fa;
  border: 1px solid #dee2e6;
}

.connection-status.connected {
  background-color: #d4edda;
  border-color: #c3e6cb;
  color: #155724;
}

.status-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background-color: #6c757d;
}

.connection-status.connected .status-dot {
  background-color: #28a745;
}

.filters {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
}

.filters select {
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.filters button {
  padding: 0.5rem 1rem;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.filters button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.report-item {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 1.5rem;
  margin-bottom: 1rem;
  background-color: white;
}

.report-item.pending {
  border-left: 4px solid #ffc107;
}

.report-item.resolved {
  border-left: 4px solid #28a745;
}

.report-item.rejected {
  border-left: 4px solid #dc3545;
}

.report-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.status-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.875rem;
  font-weight: 500;
}

.status-badge.pending {
  background-color: #fff3cd;
  color: #856404;
}

.status-badge.resolved {
  background-color: #d4edda;
  color: #155724;
}

.status-badge.rejected {
  background-color: #f8d7da;
  color: #721c24;
}

.reported-comment {
  margin: 1rem 0;
  padding: 1rem;
  background-color: #f8f9fa;
  border-radius: 4px;
}

.note {
  margin-top: 1rem;
  padding: 0.75rem;
  background-color: #e7f3ff;
  border-radius: 4px;
}

.report-actions {
  display: flex;
  gap: 0.5rem;
  margin-top: 1rem;
}

.btn-resolve {
  background-color: #28a745;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.btn-reject {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  margin-top: 2rem;
}

.pagination button {
  padding: 0.5rem 1rem;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.pagination button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.loading, .empty-state {
  text-align: center;
  padding: 2rem;
  color: #6c757d;
}
</style>
```

### 3. 主應用程式組件

```vue
<!-- src/App.vue -->
<template>
  <div id="app">
    <!-- 導航欄 -->
    <nav class="navbar">
      <div class="nav-brand">檢舉管理系統</div>
      <div class="nav-menu">
        <router-link to="/dashboard">儀表板</router-link>
        <router-link to="/reports">檢舉管理</router-link>
        <button @click="logout" class="logout-btn">登出</button>
      </div>
    </nav>
    
    <!-- 主要內容 -->
    <main class="main-content">
      <router-view />
    </main>
    
    <!-- 通知容器 -->
    <div id="notifications"></div>
  </div>
</template>

<script>
export default {
  name: 'App',
  
  mounted() {
    // 檢查是否已登入
    this.checkAuth();
    
    // 設定全域通知
    this.setupNotifications();
  },
  
  methods: {
    checkAuth() {
      const token = localStorage.getItem('token');
      const userType = localStorage.getItem('userType');
      
      if (token && userType) {
        // 重新建立 SSE 連接
        this.$connectSSE(userType.toLowerCase(), token, {
          onConnect: () => {
            console.log('✅ 應用程式啟動時 SSE 連接成功');
          },
          onError: (error) => {
            console.error('❌ SSE 連接錯誤:', error);
          },
          onMessage: (eventType, data) => {
            this.handleGlobalNotification(eventType, data);
          }
        });
      }
    },
    
    setupNotifications() {
      // 建立通知容器
      if (!document.getElementById('notifications')) {
        const notificationsDiv = document.createElement('div');
        notificationsDiv.id = 'notifications';
        notificationsDiv.style.cssText = `
          position: fixed;
          top: 20px;
          right: 20px;
          z-index: 9999;
          max-width: 400px;
        `;
        document.body.appendChild(notificationsDiv);
      }
    },
    
    handleGlobalNotification(eventType, data) {
      // 全域通知處理
      switch (eventType) {
        case 'newReport':
          this.showNotification('新檢舉通知', data.message, 'warning');
          break;
        case 'reportProcessed':
          this.showNotification('檢舉處理完成', data.message, 'success');
          break;
        case 'reportStatusUpdate':
          this.showNotification('檢舉狀態更新', data.message, 'info');
          break;
      }
    },
    
    showNotification(title, message, type = 'info') {
      const notificationsDiv = document.getElementById('notifications');
      const notification = document.createElement('div');
      
      const typeColors = {
        success: '#28a745',
        error: '#dc3545',
        warning: '#ffc107',
        info: '#17a2b8'
      };
      
      notification.style.cssText = `
        background-color: white;
        border-left: 4px solid ${typeColors[type]};
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        padding: 1rem;
        margin-bottom: 0.5rem;
        border-radius: 4px;
        animation: slideIn 0.3s ease-out;
      `;
      
      notification.innerHTML = `
        <div style="font-weight: bold; margin-bottom: 0.5rem;">${title}</div>
        <div>${message}</div>
      `;
      
      notificationsDiv.appendChild(notification);
      
      // 自動移除通知
      setTimeout(() => {
        notification.style.animation = 'slideOut 0.3s ease-in';
        setTimeout(() => {
          if (notification.parentNode) {
            notification.parentNode.removeChild(notification);
          }
        }, 300);
      }, 5000);
    },
    
    logout() {
      // 斷開 SSE 連接
      this.$disconnectSSE();
      
      // 清除本地儲存
      localStorage.removeItem('token');
      localStorage.removeItem('userType');
      
      // 清除 Vuex 狀態
      this.$store.commit('clearUser');
      
      // 導向登入頁面
      this.$router.push('/login');
    }
  }
}
</script>

<style>
#app {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.navbar {
  background-color: #343a40;
  color: white;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.nav-brand {
  font-size: 1.5rem;
  font-weight: bold;
}

.nav-menu {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.nav-menu a {
  color: white;
  text-decoration: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.nav-menu a:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.logout-btn {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.main-content {
  padding: 2rem;
  min-height: calc(100vh - 80px);
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

@keyframes slideOut {
  from {
    transform: translateX(0);
    opacity: 1;
  }
  to {
    transform: translateX(100%);
    opacity: 0;
  }
}
</style>
```

---

## 使用範例

### 1. 基本使用

```javascript
// 在任何組件中使用
export default {
  mounted() {
    // 建立 SSE 連接
    this.$connectSSE('admin', 'your-jwt-token', {
      onConnect: () => {
        console.log('連接成功');
      },
      onMessage: (eventType, data) => {
        console.log('收到通知:', eventType, data);
      }
    });
  },
  
  beforeUnmount() {
    // 斷開連接
    this.$disconnectSSE();
  }
}
```

### 2. Composition API 使用 (Vue 3)

```javascript
import { inject, onMounted, onUnmounted } from 'vue';

export default {
  setup() {
    const sseService = inject('sseService');
    
    onMounted(() => {
      sseService.connect('admin', 'your-jwt-token', {
        onConnect: () => console.log('連接成功'),
        onMessage: (eventType, data) => console.log('收到通知:', eventType, data)
      });
    });
    
    onUnmounted(() => {
      sseService.disconnect();
    });
  }
}
```

---

## 測試指南

### 1. 本地測試

```bash
# 1. 啟動後端服務
cd backend
./mvnw spring-boot:run

# 2. 啟動前端服務
cd frontend
npm run dev

# 3. 開啟瀏覽器測試
# 訪問 http://localhost:5173
```

### 2. 測試步驟

1. **登入測試**
   - 使用管理員帳號登入
   - 確認 SSE 連接建立成功
   - 檢查瀏覽器控制台日誌

2. **通知測試**
   - 在另一個瀏覽器視窗提交檢舉
   - 觀察管理員頁面是否收到即時通知
   - 測試檢舉處理功能

3. **連接測試**
   - 測試網路中斷重連
   - 測試頁面切換保持連接
   - 測試登出時斷開連接

### 3. 瀏覽器開發者工具

```javascript
// 在瀏覽器控制台測試
// 檢查 SSE 連接狀態
console.log(window.$sse.getConnectionStatus());

// 手動建立連接
window.$sse.connect('admin', 'your-token', {
  onConnect: () => console.log('連接成功'),
  onMessage: (type, data) => console.log('通知:', type, data)
});
```

---

## 常見問題

### Q1: SSE 連接無法建立
**可能原因：**
- JWT Token 無效或過期
- 後端服務未啟動
- CORS 設定問題

**解決方案：**
- 檢查 Token 是否有效
- 確認後端服務狀態
- 檢查瀏覽器控制台錯誤

### Q2: 收不到通知
**可能原因：**
- SSE 連接已斷開
- 事件類型不匹配
- 後端未發送通知

**解決方案：**
- 檢查連接狀態
- 確認事件監聽器設定
- 查看後端日誌

### Q3: 連接頻繁斷開
**可能原因：**
- 網路不穩定
- 瀏覽器限制
- 後端超時設定

**解決方案：**
- 檢查網路連接
- 調整重連設定
- 檢查後端超時配置

### Q4: 記憶體洩漏
**可能原因：**
- 事件監聽器未清理
- 組件未正確卸載

**解決方案：**
- 在 beforeUnmount 中斷開連接
- 清理事件監聽器
- 使用 Vue DevTools 檢查記憶體

---

## API 參考

### SSE 服務方法

| 方法 | 參數 | 說明 |
|------|------|------|
| `connect(userType, token, callbacks)` | userType: string, token: string, callbacks: object | 建立 SSE 連接 |
| `disconnect()` | - | 斷開 SSE 連接 |
| `getConnectionStatus()` | - | 獲取連接狀態 |

### 回調函數

| 回調 | 參數 | 說明 |
|------|------|------|
| `onConnect` | - | 連接建立成功時調用 |
| `onError` | error: Error | 連接錯誤時調用 |
| `onMessage` | eventType: string, data: object | 收到通知時調用 |
| `onDisconnect` | - | 連接斷開時調用 |

### 事件類型

| 事件類型 | 說明 | 資料格式 |
|----------|------|----------|
| `newReport` | 新檢舉通知 | `{ message: string, reportId: number }` |
| `reportProcessed` | 檢舉處理完成 | `{ message: string, reportId: number, status: string }` |
| `reportStatusUpdate` | 檢舉狀態更新 | `{ message: string, reportId: number, status: string }` |

---

## 部署注意事項

### 1. 生產環境配置

```javascript
// 設定生產環境 API 地址
const baseUrl = process.env.NODE_ENV === 'production' 
  ? 'https://api.yourdomain.com' 
  : 'http://localhost:8080';
```

### 2. HTTPS 要求

生產環境必須使用 HTTPS，因為 SSE 在某些瀏覽器中需要安全連接。

### 3. 代理設定

如果使用 Nginx 代理，需要設定：

```nginx
location /api/sse/ {
    proxy_pass http://backend:8080;
    proxy_set_header Connection '';
    proxy_http_version 1.1;
    proxy_buffering off;
    proxy_cache off;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header Host $http_host;
    proxy_read_timeout 86400;
}
```

---

## 支援與維護

### 版本相容性
- Vue.js 2.x 和 3.x
- 現代瀏覽器 (Chrome 6+, Firefox 6+, Safari 5+, Edge 12+)
- Node.js 14+

### 更新日誌
- v1.0.0: 初始版本，支援基本 SSE 功能
- v1.1.0: 新增自動重連機制
- v1.2.0: 支援 Vue 3 Composition API

### 問題回報
如遇到問題，請提供：
1. 瀏覽器版本
2. Vue.js 版本
3. 錯誤訊息
4. 重現步驟

---

**文件版本：v1.2.0**  
**最後更新：2025-01-15**  
**維護者：後端開發團隊** 