# 身分判定管理說明

## 📋 概述

本文件記錄了 Journey.com 旅遊網站的身分判定邏輯，統一管理所有用戶角色的權限控制。

## 🔐 用戶角色類型

### 1. **一般用戶 (USER)**
- **權限**: 瀏覽商品、收藏、購物車、下單、評論、按讚、檢舉
- **儲存方式**: `localStorage.userRole = 'user'` 或 `localStorage.userType = 'USER'`

### 2. **管理員 (ADMIN)**
- **權限**: 後台管理、數據統計、用戶管理、商品管理、評論回覆
- **儲存方式**: `localStorage.userRole = 'admin'` 或 `localStorage.userType = 'ADMIN'`

### 3. **商家 (VENDOR)**
- **權限**: 商品上傳、訂單管理、商家後台、評論回覆、按讚、檢舉
- **儲存方式**: 
  - `localStorage.userType = 'VENDOR'`
  - `localStorage.isVendorLoggedIn = 'true'`
  - `localStorage.vendorId = '商家ID'`
  - `localStorage.vendorType = '商家類型'`

## 🏗️ 商家類型細分

### 飯店商家 (hotel)
- **路徑**: `/vendor/hotel`
- **功能**: 飯店資訊上傳、房型管理

### 機票商家 (flight)
- **路徑**: `/vendor/flight`
- **功能**: 航班資訊上傳

### 景點商家 (attraction)
- **路徑**: `/vendor/attraction`
- **功能**: 景點門票上傳

### 交通商家 (traffic)
- **路徑**: `/vendor/traffic`
- **功能**: 交通服務上傳

## 🛠️ 統一使用方式

### 引入 useCurrentUser
```javascript
import { useCurrentUser } from '@/composables/useCurrentUser'

const { 
  currentUser, 
  isAdmin, 
  isVendor, 
  isUser, 
  isAdminOrVendor,
  isHotelVendor,
  isFlightVendor,
  isAttractionVendor,
  isTrafficVendor
} = useCurrentUser()
```

### 基本身分判定
```javascript
// 檢查是否為管理員
if (isAdmin.value) {
  // 管理員專用功能
}

// 檢查是否為商家
if (isVendor.value) {
  // 商家專用功能
}

// 檢查是否為一般用戶
if (isUser.value) {
  // 一般用戶功能
}
```

### 複合身分判定
```javascript
// 管理員或商家
if (isAdminOrVendor.value) {
  // 管理功能
}
```

### 商家類型判定
```javascript
// 飯店商家
if (isHotelVendor.value) {
  // 飯店相關功能
}

// 機票商家
if (isFlightVendor.value) {
  // 機票相關功能
}
```

## 📊 用戶資訊結構

```javascript
const currentUser = {
  userId: 1,                    // 用戶ID
  userType: 'ADMIN',           // 用戶類型 (大寫)
  userName: '管理員',           // 用戶名稱
  isVendorLoggedIn: false,     // 是否為商家登入
  vendorId: null,              // 商家ID
  vendorType: null,            // 商家類型
  vendorName: ''               // 商家名稱
}
```

## 🔄 登入流程

### 一般用戶登入
```javascript
// 登入成功後儲存
localStorage.setItem('userRole', 'user')
localStorage.setItem('userName', '用戶名稱')
localStorage.setItem('userId', '用戶ID')
localStorage.setItem('token', 'JWT Token')
```

### 管理員登入
```javascript
// 登入成功後儲存
localStorage.setItem('userRole', 'admin')
localStorage.setItem('userName', '管理員名稱')
localStorage.setItem('userId', '管理員ID')
localStorage.setItem('token', 'JWT Token')
```

### 商家登入
```javascript
// 登入成功後儲存
localStorage.setItem('userType', 'VENDOR')
localStorage.setItem('vendorId', '商家ID')
localStorage.setItem('isVendorLoggedIn', 'true')
localStorage.setItem('vendorType', 'hotel') // hotel/flight/attraction/traffic
localStorage.setItem('vendorName', '商家名稱')
localStorage.setItem('token', 'JWT Token')
```

## 🚫 登出流程

```javascript
// 清除所有用戶資訊
localStorage.removeItem('userRole')
localStorage.removeItem('userType')
localStorage.removeItem('userName')
localStorage.removeItem('userId')
localStorage.removeItem('token')
localStorage.removeItem('vendorType')
localStorage.removeItem('vendorId')
localStorage.removeItem('isVendorLoggedIn')
localStorage.removeItem('vendorName')
```

## ⚠️ 注意事項

1. **統一使用 useCurrentUser**: 所有身分判定都應該使用 `useCurrentUser` composable
2. **大小寫兼容**: 系統同時支援大小寫的角色名稱
3. **商家身分特殊**: 商家使用不同的 localStorage 鍵值
4. **權限檢查**: 重要功能必須進行權限檢查
5. **登出清理**: 登出時必須清除所有相關的 localStorage 項目

## 🔧 遷移指南

### 舊程式碼
```javascript
// ❌ 不要這樣做
const userRole = localStorage.getItem('userRole')
const isAdmin = ['admin', 'merchant'].includes(userRole)
```

### 新程式碼
```javascript
// ✅ 使用統一的身分判定
import { useCurrentUser } from '@/composables/useCurrentUser'
const { isAdmin, isAdminOrVendor } = useCurrentUser()
```

## 📝 更新記錄

- **2024-01-15**: 建立統一的身分判定系統
- **2024-01-15**: 整合商家身分支援
- **2024-01-15**: 更新 Header.vue 和 Admin.vue 使用統一邏輯 