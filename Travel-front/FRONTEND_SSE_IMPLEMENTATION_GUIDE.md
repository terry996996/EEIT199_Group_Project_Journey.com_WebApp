# 前端 SSE 實作說明

## 1. 概述
本文件說明如何在 Vue.js 前端專案中整合 SSE (Server-Sent Events) 實現即時通知，支援檢舉、留言、系統等多種通知場景。

## 2. 技術架構
- 框架：Vue.js 3（支援 Vue 2）
- UI：Bootstrap 5
- 狀態管理：原生 Composition API
- HTTP：Axios
- SSE：原生 EventSource

## 3. 安裝與設定
1. 將 `sse-notification-service.js` 放入 `src/services/`
2. 將 `vue-sse-plugin.js` 放入 `src/plugins/`
3. 在 `main.js` 註冊：
```js
import { createApp } from 'vue'
import App from './App.vue'
import { SSEPlugin } from './plugins/vue-sse-plugin.js'
const app = createApp(App)
app.use(SSEPlugin)
app.mount('#app')
```

## 4. 核心 SSE 服務（精簡版）
```js
// src/services/sse-notification-service.js
class SSENotificationService {
  constructor() { this.eventSource = null; }
  connect(token, userType, callbacks = {}) {
    if (!token) return;
    const url = `/api/sse/${userType}/connect?token=${encodeURIComponent(token)}`;
    this.eventSource = new EventSource(url);
    this.eventSource.onopen = () => callbacks.onConnect?.();
    this.eventSource.onerror = (e) => callbacks.onError?.(e);
    this.eventSource.onmessage = (e) => callbacks.onMessage?.('message', JSON.parse(e.data));
    // 可加自定義事件監聽
  }
  disconnect() { this.eventSource?.close(); this.eventSource = null; }
}
export default new SSENotificationService();
```

## 5. Vue.js 整合方式
### Options API
```js
import sseService from '@/services/sse-notification-service.js'
export default {
  mounted() {
    const token = localStorage.getItem('token');
    sseService.connect(token, 'admin', {
      onConnect: () => console.log('SSE 連接成功'),
      onMessage: (type, data) => console.log('通知:', type, data)
    });
  },
  beforeUnmount() { sseService.disconnect(); }
}
```
### Composition API
```js
import { onMounted, onUnmounted } from 'vue'
import sseService from '@/services/sse-notification-service.js'
onMounted(() => {
  sseService.connect(localStorage.getItem('token'), 'user', {
    onMessage: (type, data) => {/* 處理通知 */}
  });
});
onUnmounted(() => sseService.disconnect());
```

## 6. 測試與常見問題
- **測試**：登入後檢查 SSE 是否自動連接，提交檢舉/留言後觀察是否即時收到通知。
- **常見問題**：
  - 連接失敗：檢查 token、CORS、後端服務
  - 收不到通知：檢查事件監聽、後端推送
  - 頻繁斷線：檢查網路、重連機制

## 7. API 參考
- `connect(token, userType, callbacks)`：建立連接
- `disconnect()`：斷開連接
- 回調：`onConnect`、`onError`、`onMessage`
- 事件類型：`newReport`、`reportProcessed`、`notification` ...

## 8. 部署注意事項
- 生產環境請使用 HTTPS
- Nginx 代理需設 `proxy_buffering off;`
- 後端需支援 CORS 與 JWT 驗證

---
如需完整範例與進階功能，請參考專案內 `SSE_IMPLEMENTATION_REQUIREMENTS.md`。 