import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  },
  // ===========================================
  // 添加 server 和 proxy 配置
  server: {
    proxy: {
      // 當前端請求路徑以 '/api' 開頭時
      '/api': {
        // 將請求轉發到此目標地址
        target: 'http://localhost:8080', // <-- **重要：請將這裡的端口號改為您的 Spring Boot 後端實際運行的端口**
        // 必須設置為 true，Vite 會將請求頭中的 Host 設置為 target 的 Host，
        // 這樣後端伺服器才能正確識別請求來源。
        changeOrigin: true
        // rewrite: (path) => path.replace(/^\/api/, ''), // 如果您的後端 API 路徑本身沒有 '/api' 前綴，則啟用此行
        // 例如，如果前端請求是 /api/users，而後端實際是 /users，則需要移除 /api
        // 根據您提供的 Spring Boot Controller，您的後端路由是 /api/customer-service/...
        // 所以這裡通常不需要 rewrite。
      }
      // 您可能還有其他的 API 前綴，例如 /another-api
      // '/another-api': {
      //   target: 'http://localhost:8081',
      //   changeOrigin: true,
      //   // rewrite: (path) => path.replace(/^\/another-api/, ''),
      // },
    }
  }
  // ===========================================
})
