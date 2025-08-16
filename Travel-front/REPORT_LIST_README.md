# 檢舉列表組件使用說明

## 📋 概述

`ReportList.vue` 是一個完整的檢舉管理組件，提供管理員查看、篩選和處理檢舉的功能。該組件已整合到 `Admin.vue` 中，僅管理員可見。

## 🚀 功能特色

### 1. 統計面板
- 待處理檢舉數量
- 已處理檢舉數量  
- 已駁回檢舉數量
- 總檢舉數量

### 2. 檢舉列表
- 分頁顯示檢舉資料
- 狀態篩選（全部/待處理/已處理/已駁回）
- 檢舉詳情查看
- 快速處理/駁回按鈕

### 3. 檢舉詳情 Modal
- 完整的檢舉資訊顯示
- 被檢舉留言內容
- 被檢舉留言圖片顯示
- 檢舉者資訊
- 處理備註功能

## 📁 檔案結構

```
src/
├── components/
│   └── comments/
│       └── ReportList.vue      # 檢舉列表組件
├── views/
│   └── Admin.vue               # 管理員頁面（已整合）
└── router/
    └── index.js                # 路由配置
```

## 🔧 使用方法

### 1. 在 Admin.vue 中使用

檢舉列表組件已自動整合到管理員頁面中：

```vue
<!-- 檢舉管理 (僅管理員可見) -->
<div class="row" v-if="!isGeneralUser && isAdmin">
  <div class="col-12">
    <ReportList />
  </div>
</div>
```

### 2. 獨立使用

```vue
<template>
  <div>
    <ReportList />
  </div>
</template>

<script setup>
import ReportList from '@/components/comments/ReportList.vue'
</script>
```

### 3. 測試頁面

訪問 `/report-test` 路由可以獨立測試檢舉列表功能。

## 🔌 API 端點

組件使用以下 API 端點：

### 查詢檢舉列表
```http
GET /api/comment-reports/admin?status=PENDING&page=0&size=20
Authorization: Bearer <JWT_TOKEN>
```

### 處理檢舉
```http
PUT /api/comment-reports/admin/{reportId}
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "status": "RESOLVED",
  "note": "檢舉成立，已刪除留言"
}
```

## 📊 資料結構

### 檢舉項目結構
```javascript
{
  reportId: 1,
  status: "PENDING",
  statusLabel: "待處理",
  createdAt: "2025-07-11T20:34:26.317",
  reviewedAt: null,
  note: null,
  reasonId: 1,
  reasonText: "色情內容",
  reporter: {
    id: 3,
    username: "vendor",
    avatarUrl: null
  },
  reportedComment: {
    id: 13,
    content: "系統範例留言 #11：住宿體驗超棒！",
    rating: 3,
    createdAt: "2025-06-30T20:31:26.867",
    author: {
      id: 1,
      username: "user",
      avatarUrl: null
    },
    images: [
      {
        id: 1,
        imageId: 1,
        commentImageUrl: "https://example.com/image1.jpg",
        imageUrl: "https://example.com/image1.jpg",
        isActive: true,
        isDeleted: false,
        size: 102400,
        mimeType: "image/jpeg"
      }
    ]
  }
}
```

## 🎨 樣式特色

### 1. 響應式設計
- 支援桌面和行動裝置
- Bootstrap 5 網格系統
- 自適應卡片佈局

### 2. 視覺回饋
- 載入動畫
- 狀態標籤顏色區分
- 懸停效果
- 處理按鈕狀態

### 3. 一致性設計
- 與 Admin.vue 保持一致的視覺風格
- 使用相同的顏色主題
- 統一的按鈕和卡片樣式
- 圖片顯示與其他評論組件一致

## 🔐 權限控制

- 僅管理員可以訪問檢舉列表
- 自動檢查用戶權限
- 未授權用戶會被重定向

## 🛠️ 自訂配置

### 修改分頁大小
```javascript
const pageSize = ref(20) // 預設每頁 20 筆
```

### 修改統計計算
```javascript
const updateStatistics = async () => {
  // 自訂統計邏輯
}
```

### 修改狀態標籤樣式
```javascript
const getStatusBadgeClass = (status) => {
  // 自訂狀態標籤樣式
}
```

## 🐛 故障排除

### 1. 無法載入檢舉列表
- 檢查 API 端點是否正確
- 確認 JWT Token 是否有效
- 檢查網路連線

### 2. 權限錯誤
- 確認用戶是否為管理員
- 檢查 `isAdmin` 計算屬性
- 確認後端權限設定

### 3. 樣式問題
- 確認 Bootstrap 5 已正確載入
- 檢查 CSS 類別名稱
- 確認 Bootstrap Icons 已載入

## 📝 更新日誌

### v1.0.0 (2025-07-11)
- ✅ 初始版本發布
- ✅ 基本檢舉列表功能
- ✅ 統計面板
- ✅ 詳情 Modal
- ✅ 處理/駁回功能
- ✅ 分頁和篩選
- ✅ 響應式設計

## 🤝 貢獻

如需修改或擴展功能，請：

1. Fork 專案
2. 創建功能分支
3. 提交變更
4. 發起 Pull Request

## 📞 支援

如有問題或建議，請聯繫開發團隊或提交 Issue。 