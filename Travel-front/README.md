# Journey.com - 旅遊平台

一個仿 Trip.com 風格的現代化旅遊預訂平台，使用 Vue 3 + Vite + Bootstrap 5 開發。

## 🚀 專案特色

### 🎨 **全面優化的 UI/UX 設計**

- **Trip.com 風格設計系統**：完整的色彩、字體、間距、動畫設計規範
- **響應式設計**：完美適配桌面、平板、手機等各種設備
- **現代化動畫**：流暢的過渡動畫和互動效果
- **自定義滾動條**：美觀的滾動條設計
- **載入動畫**：專業的載入指示器

### 🔍 **進階搜尋與推薦系統**

- **智能搜尋**：支援目的地、日期、人數等多條件搜尋
- **即時建議**：輸入時提供智能搜尋建議
- **搜尋歷史**：記錄並快速重複搜尋
- **個人化推薦**：
  - 熱門目的地推薦
  - 季節性推薦
  - 相似用戶推薦
  - 最近瀏覽記錄
- **搜尋結果頁面**：完整的篩選、排序、分頁功能

### 💬 **客服聯繫與聊天室系統**

- **多種聯繫方式**：
  - 即時聊天（24/7）
  - 電話客服
  - 電子郵件
  - 社群媒體
- **智能聊天室**：
  - 即時訊息功能
  - 表情符號支援
  - 快速回覆按鈕
  - 輸入指示器
  - 自動回覆系統
- **常見問題 FAQ**：可展開的問答系統
- **聯繫表單**：完整的客服表單

### 🏠 **首頁設計**

- **英雄區域**：震撼的背景圖片和標語
- **特色服務展示**：安全、價格、客服、品質四大特色
- **熱門目的地**：視覺化的目的地卡片
- **用戶評價**：真實用戶評價展示
- **推薦系統整合**：個人化推薦內容

### 🧭 **導航與頁面結構**

- **固定導航欄**：現代化的導航設計
- **搜尋覆蓋層**：全螢幕搜尋體驗
- **用戶選單**：完整的用戶功能選單
- **頁腳設計**：豐富的連結和資訊
- **回到頂部按鈕**：便捷的頁面導航

## 🛠 技術架構

### 前端技術棧

- **Vue 3**：使用 Composition API
- **Vite**：快速的開發和建置工具
- **Bootstrap 5**：響應式 UI 框架
- **Vue Router**：單頁應用路由
- **CSS 變數**：統一的設計系統

### 專案結構

```
Travel-front/
├── src/
│   ├── components/          # 可重用元件
│   │   ├── AdvancedSearch.vue      # 進階搜尋
│   │   ├── RecommendationSystem.vue # 推薦系統
│   │   ├── CustomerService.vue     # 客服聯繫
│   │   ├── ChatRoom.vue           # 聊天室
│   │   ├── Header.vue             # 導航欄
│   │   └── Footer.vue             # 頁腳
│   ├── views/               # 頁面元件
│   │   ├── Home.vue               # 首頁
│   │   ├── CustomerService.vue    # 客服頁面
│   │   ├── SearchResults.vue      # 搜尋結果
│   │   └── ...                   # 其他頁面
│   ├── router/              # 路由配置
│   ├── assets/              # 靜態資源
│   └── main.js              # 應用入口
├── public/                  # 公共資源
└── package.json             # 專案配置
```

## 🎯 核心功能

### 1. 進階搜尋系統

- 多條件搜尋（目的地、日期、人數、預算）
- 即時搜尋建議
- 搜尋歷史記錄
- 篩選和排序功能

### 2. 推薦系統

- 基於用戶行為的個人化推薦
- 熱門目的地推薦
- 季節性推薦
- 相似用戶推薦

### 3. 客服系統

- 即時聊天功能
- 多種聯繫方式
- 常見問題解答
- 客服表單

### 4. 響應式設計

- 完美適配各種設備
- 流暢的動畫效果
- 直觀的用戶介面

## 🚀 快速開始

### 環境配置

1. **複製環境變數範例檔案**：
```bash
cp .env.example .env
```

2. **配置 API 基礎 URL**：
編輯 `.env` 檔案，設定您的 SpringBoot API 伺服器地址：
```env
VITE_API_BASE_URL=http://localhost:8080/api
```

### 安裝依賴

```bash
npm install
```

### 啟動開發伺服器

```bash
npm run dev
```

### 建置生產版本

```bash
npm run build
```

## 🔧 API 整合

### SpringBoot RESTful API

本專案已整合 SpringBoot RESTful API，支援以下功能：

#### 評論系統 API
- `GET /api/comments` - 取得評論列表
- `POST /api/comments` - 新增評論
- `PUT /api/comments/{id}` - 更新評論
- `DELETE /api/comments/{id}` - 刪除評論
- `POST /api/comments/{id}/like` - 按讚/取消按讚
- `POST /api/comments/{id}/report` - 檢舉評論

#### 用戶系統 API
- `GET /api/users/profile` - 取得用戶資訊
- `PUT /api/users/profile` - 更新用戶資訊
- `POST /api/auth/login` - 用戶登入
- `POST /api/auth/register` - 用戶註冊
- `POST /api/auth/logout` - 用戶登出

#### 住宿系統 API
- `GET /api/lodgings` - 取得住宿列表
- `GET /api/lodgings/{id}` - 取得住宿詳情
- `GET /api/lodgings/{id}/comments` - 取得住宿評論

#### 交通系統 API
- `GET /api/traffic` - 取得交通列表
- `GET /api/traffic/{id}` - 取得交通詳情
- `GET /api/traffic/{id}/comments` - 取得交通評論

#### 票券系統 API
- `GET /api/tickets` - 取得票券列表
- `GET /api/tickets/{id}` - 取得票券詳情
- `GET /api/tickets/{id}/comments` - 取得票券評論

#### 訂單系統 API
- `GET /api/orders` - 取得訂單列表
- `GET /api/orders/{id}` - 取得訂單詳情
- `POST /api/orders` - 建立訂單
- `PUT /api/orders/{id}/cancel` - 取消訂單
- `POST /api/orders/{id}/pay` - 支付訂單

### API 錯誤處理

專案已整合完整的錯誤處理機制：

- **401 未授權**：自動清除 token 並重定向到登入頁面
- **403 權限不足**：顯示權限不足提示
- **404 資源不存在**：顯示資源不存在提示
- **500 伺服器錯誤**：顯示伺服器錯誤提示
- **網路錯誤**：顯示網路連線錯誤提示

### 認證機制

- 使用 JWT Token 進行身份驗證
- Token 自動附加到 API 請求標頭
- Token 過期自動處理
- 支援 Bearer Token 格式

## 📱 響應式設計

- **桌面版**：完整功能展示
- **平板版**：適配中等螢幕
- **手機版**：優化的移動體驗

## 🎨 設計系統

### 色彩系統

- **主色調**：#0080ff（藍色）
- **輔助色**：#ff6b35（橙色）
- **成功色**：#28a745（綠色）
- **警告色**：#ffc107（黃色）
- **危險色**：#dc3545（紅色）

### 字體系統

- **字體家族**：系統字體堆疊
- **字體大小**：xs, sm, base, lg, xl, xxl
- **字重**：light, normal, medium, semibold, bold

### 間距系統

- **間距單位**：xs(4px), sm(8px), md(16px), lg(24px), xl(32px), xxl(48px)

### 動畫系統

- **過渡時間**：fast(0.15s), normal(0.3s), slow(0.5s)
- **動畫效果**：fadeIn, slideInUp, slideInDown, pulse

## 🔧 自定義配置

### 修改設計系統

編輯 `src/assets/styles.css` 中的 CSS 變數來調整設計系統。

### 新增頁面

1. 在 `src/views/` 建立新的 Vue 元件
2. 在 `src/router/index.js` 新增路由配置

### 新增元件

在 `src/components/` 建立可重用的 Vue 元件。

## 📄 授權

本專案僅供學習和展示使用。

## 🤝 貢獻

歡迎提交 Issue 和 Pull Request 來改善專案。

---

**Journey.com** - 讓您的旅程更加精彩！ ✈️🏨🎫
