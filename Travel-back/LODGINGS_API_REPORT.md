# Lodgings API 完整報告

## 📋 概述

本文檔提供住宿管理系統的完整 API 規格，包含所有端點、請求/響應格式、錯誤處理和前端使用指南。

## 🏗️ 架構說明

住宿管理系統已按功能拆分為兩個主要模組：

- **一般用戶功能** (`/api/lodgings/**`) - 搜尋和查看旅宿
- **商家管理功能** (`/api/vendor/lodgings/**`) - 商家後台管理

### 🎯 圖片 DTO 統一架構

系統已實現統一的圖片 DTO 架構，所有住宿和房型相關的圖片操作都使用統一的 `ImageDTO`：

#### 整合前後對比

| 組件                        | 整合前                               | 整合後        |
| --------------------------- | ------------------------------------ | ------------- |
| `LodgingRequestDTO`         | `LodgingImageDTO` (內部類別)         | ✅ `ImageDTO` |
| `LodgingResponseDTO`        | `LodgingImageResponseDTO` (內部類別) | ✅ `ImageDTO` |
| `RoomTypeCreateRequestDTO`  | `RoomTypeImageDTO` (內部類別)        | ✅ `ImageDTO` |
| `RoomTypeCreateResponseDTO` | `LodgingImageResponseDTO` (內部類別) | ✅ `ImageDTO` |
| 服務層                      | `ImageDTO`                           | ✅ `ImageDTO` |

#### 統一架構優勢

- ✅ **一致性**: 所有 DTO 使用相同的圖片結構
- ✅ **維護性**: 只需維護一套圖片 DTO 程式碼
- ✅ **效能**: 減少 DTO 轉換開銷
- ✅ **向後相容**: JSON 格式保持穩定，僅新增 `lodgingId` 欄位

## 🚀 API 端點總覽

### 一般用戶功能 (LodgingsController)

| 方法 | 端點                   | 描述         | 狀態 |
| ---- | ---------------------- | ------------ | ---- |
| POST | `/api/lodgings/search` | 搜尋旅宿     | ✅   |
| GET  | `/api/lodgings/hot`    | 獲取熱門旅宿 | ✅   |

### 商家管理功能 (VendorLodgingController)

| 方法   | 端點                                   | 描述                       | 狀態 |
| ------ | -------------------------------------- | -------------------------- | ---- |
| GET    | `/api/vendor/lodgings`                 | 獲取商家旅宿列表（在售）   | ✅   |
| GET    | `/api/vendor/lodgings?status=inactive` | 獲取商家旅宿列表（停業）   | ✅   |
| GET    | `/api/vendor/lodgings?status=deleted`  | 獲取商家旅宿列表（已下架） | ✅   |
| GET    | `/api/vendor/lodgings?status=all`      | 獲取商家旅宿列表（全部）   | ✅   |
| POST   | `/api/vendor/lodgings`                 | 創建新住宿                 | ✅   |
| GET    | `/api/vendor/lodgings/{id}`            | 獲取住宿詳情               | ✅   |
| PUT    | `/api/vendor/lodgings/{id}`            | 更新住宿                   | ✅   |
| DELETE | `/api/vendor/lodgings/{id}`            | 軟刪除住宿                 | ✅   |
| POST   | `/api/vendor/lodgings/{id}/deactivate` | 停業旅宿                   | ✅   |
| POST   | `/api/vendor/lodgings/{id}/activate`   | 重新上架旅宿               | ✅   |
| POST   | `/api/vendor/lodgings/{id}/restore`    | 軟刪還原旅宿               | ✅   |

### 房型管理 API (VendorRoomTypeController)

| 方法   | 端點                                           | 描述                   | 狀態 |
| ------ | ---------------------------------------------- | ---------------------- | ---- |
| POST   | `/api/vendor/lodgings/{lodgingId}/room-types`  | 新增房型               | ✅   |
| GET    | `/api/vendor/lodgings/{lodgingId}/room-types`  | 查詢飯店全部房型簡歷   | ✅   |
| GET    | `/api/vendor/lodgings/{lodgingId}/room-types/{roomTypeId}` | 查詢單筆房型詳細 | ✅   |
| PUT    | `/api/vendor/lodgings/{lodgingId}/room-types/{roomTypeId}` | 更新房型資料     | ✅   |
| DELETE | `/api/vendor/lodgings/{lodgingId}/room-types/{roomTypeId}` | 刪除房型資料   | ✅   |

### 選項 API (OptionController)

| 方法 | 端點                          | 描述             | 狀態 |
| ---- | ----------------------------- | ---------------- | ---- |
| GET  | `/api/options/lodgings-types` | 獲取住宿類型選項 | ✅   |
| GET  | `/api/options/cities`         | 獲取城市選項     | ✅   |
| GET  | `/api/options/countries`      | 獲取國家選項     | ✅   |
| GET  | `/api/options/facilities`     | 獲取設施選項     | ✅   |
| GET  | `/api/options/bed-types`      | 獲取床型選項     | ✅   |

## 📊 資料庫對應關係

### 核心表格

- `lodgings` - 住宿主表
- `lodging_images` - 住宿圖片表
- `lodgings_type` - 住宿類型表
- `room_types` - 房型主表
- `room_type_beds` - 房型床型關聯表
- `room_facilities` - 房型設施關聯表
- `cities` - 城市表
- `countries` - 國家表
- `facilities` - 設施表
- `bed_types` - 床型表

### 外鍵關係

```sql
lodgings.lodging_type -> lodgings_type.id
lodgings.city_id -> cities.id
lodgings.country_id -> countries.id
lodging_images.lodging_id -> lodgings.id
```

## 📋 DTO 完整對照表

### 概述

本節提供 `dto/lodgings` 目錄內所有 16 個 DTO 檔案的完整對照，包含檔案類型、描述、JSON 格式和範例資料。

### DTO 分類

#### 1. 基礎 DTO (Basic DTOs)
- `BedTypeDTO` - 床型資料傳輸物件
- `FacilityDTO` - 設施資料傳輸物件
- `ImageDTO` - 統一圖片資料傳輸物件

#### 2. 搜尋 DTO (Search DTOs)
- `LodgingSearchRequestDTO` - 住宿搜尋請求
- `LodgingSearchResponseDTO` - 住宿搜尋回應
- `SearchResponseDTO` - 通用搜尋回應
- `FilterOptionsDTO` - 篩選選項
- `LodgingHotSearchResponseDTO` - 熱門搜尋回應

#### 3. 商家 DTO (Vendor DTOs)
- `VendorLodgingListDTO` - 商家住宿列表
- `VendorLodgingDetailDTO` - 商家住宿詳情
- `VendorRoomTypeListDTO` - 商家房型列表

#### 4. 核心 DTO (Core DTOs)
- `LodgingRequestDTO` - 住宿請求（創建/更新）
- `LodgingResponseDTO` - 住宿回應
- `LodgingTypeDTO` - 住宿類型

#### 5. 房型 DTO (Room Type DTOs)
- `RoomTypeCreateRequestDTO` - 房型創建請求
- `RoomTypeCreateResponseDTO` - 房型創建回應

### 完整 JSON 對照表

```json
{
  "dtoFiles": [
    {
      "fileName": "BedTypeDTO.java",
      "type": "record",
      "description": "床型資料傳輸物件，用於表示住宿的床型資訊",
      "jsonFormat": {
        "id": "Integer",
        "name": "String"
      },
      "example": {
        "id": 1,
        "name": "雙人床"
      },
      "usage": "基礎 DTO，用於床型選項和房型資訊"
    },
    {
      "fileName": "FacilityDTO.java",
      "type": "record",
      "description": "設施資料傳輸物件，用於表示住宿提供的設施",
      "jsonFormat": {
        "id": "Integer",
        "name": "String"
      },
      "example": {
        "id": 1,
        "name": "WiFi"
      },
      "usage": "基礎 DTO，用於設施選項和住宿資訊"
    },
    {
      "fileName": "ImageDTO.java",
      "type": "class",
      "description": "統一圖片資料傳輸物件，用於住宿和房型圖片管理",
      "jsonFormat": {
        "id": "Integer",
        "imageUrl": "String",
        "imageOrder": "Integer",
        "lodgingId": "Integer"
      },
      "example": {
        "id": 1,
        "imageUrl": "https://cdn.example.com/imgs/lodging1.jpg",
        "imageOrder": 1,
        "lodgingId": 123
      },
      "usage": "統一圖片 DTO，用於所有圖片相關操作"
    },
    {
      "fileName": "LodgingSearchRequestDTO.java",
      "type": "record",
      "description": "住宿搜尋請求 DTO，包含搜尋條件和篩選選項",
      "jsonFormat": {
        "cityName": "String",
        "checkInDate": "LocalDate",
        "checkOutDate": "LocalDate",
        "guestCount": "Integer",
        "filterOptions": "FilterOptionsDTO"
      },
      "example": {
        "cityName": "大阪",
        "checkInDate": "2024-02-01",
        "checkOutDate": "2024-02-03",
        "guestCount": 2,
        "filterOptions": {
          "bedTypeIds": [1, 2],
          "facilityIds": [1, 3],
          "lodgingTypeIds": [1]
        }
      },
      "usage": "搜尋 DTO，用於一般用戶搜尋住宿"
    },
    {
      "fileName": "LodgingSearchResponseDTO.java",
      "type": "record",
      "description": "住宿搜尋回應 DTO，包含搜尋結果和篩選選項",
      "jsonFormat": {
        "lodgings": "List<LodgingSearchResponseDTO.LodgingInfo>",
        "filterOptions": "FilterOptionsDTO"
      },
      "example": {
        "lodgings": [
          {
            "lodgingId": 123,
            "lodgingName": "大阪試營運飯店",
            "avgRating": 4.5,
            "reviewCount": 128,
            "roomTypeName": "標準雙人房",
            "roomTypeDescription": "舒適的雙人房",
            "maxGuests": 2,
            "pricePerNight": 8000.0,
            "imageUrl": "https://cdn.example.com/imgs/osa1.jpg",
            "lodgingTypeId": 1,
            "lodgingType": "飯店",
            "bedTypes": [{"id": 1, "name": "雙人床"}],
            "facilities": [{"id": 1, "name": "WiFi"}]
          }
        ],
        "filterOptions": {
          "bedTypes": [{"id": 1, "name": "雙人床"}],
          "facilities": [{"id": 1, "name": "WiFi"}],
          "lodgingTypes": [{"id": 1, "name": "飯店"}]
        }
      },
      "usage": "搜尋 DTO，用於返回搜尋結果"
    },
    {
      "fileName": "SearchResponseDTO.java",
      "type": "record",
      "description": "通用搜尋回應 DTO，用於標準化搜尋結果格式",
      "jsonFormat": {
        "lodgings": "List<SearchResponseDTO.LodgingInfo>",
        "filterOptions": "FilterOptionsDTO"
      },
      "example": {
        "lodgings": [
          {
            "lodgingId": 123,
            "lodgingName": "大阪試營運飯店",
            "avgRating": 4.5,
            "reviewCount": 128,
            "roomTypeName": "標準雙人房",
            "maxGuests": 2,
            "pricePerNight": 8000.0,
            "imageUrl": "https://cdn.example.com/imgs/osa1.jpg",
            "lodgingTypeId": 1,
            "lodgingType": "飯店"
          }
        ],
        "filterOptions": {
          "bedTypes": [{"id": 1, "name": "雙人床"}],
          "facilities": [{"id": 1, "name": "WiFi"}],
          "lodgingTypes": [{"id": 1, "name": "飯店"}]
        }
      },
      "usage": "搜尋 DTO，通用搜尋回應格式"
    },
    {
      "fileName": "FilterOptionsDTO.java",
      "type": "record",
      "description": "篩選選項 DTO，包含各種篩選條件",
      "jsonFormat": {
        "bedTypeIds": "List<Integer>",
        "facilityIds": "List<Integer>",
        "lodgingTypeIds": "List<Integer>"
      },
      "example": {
        "bedTypeIds": [1, 2],
        "facilityIds": [1, 3],
        "lodgingTypeIds": [1]
      },
      "usage": "搜尋 DTO，用於搜尋條件篩選"
    },
    {
      "fileName": "LodgingHotSearchResponseDTO.java",
      "type": "record",
      "description": "熱門搜尋回應 DTO，用於返回熱門住宿列表",
      "jsonFormat": {
        "lodgings": "List<LodgingHotSearchResponseDTO.LodgingInfo>"
      },
      "example": {
        "lodgings": [
          {
            "lodgingId": 123,
            "lodgingName": "大阪試營運飯店",
            "avgRating": 4.5,
            "reviewCount": 128,
            "imageUrl": "https://cdn.example.com/imgs/osa1.jpg"
          }
        ]
      },
      "usage": "搜尋 DTO，用於熱門住宿列表"
    },
    {
      "fileName": "VendorLodgingListDTO.java",
      "type": "class",
      "description": "商家住宿列表 DTO，用於商家後台管理",
      "jsonFormat": {
        "lodgingId": "Integer",
        "lodgingName": "String",
        "cityName": "String",
        "countryName": "String",
        "lodgingType": "String",
        "status": "String",
        "createdAt": "LocalDateTime",
        "updatedAt": "LocalDateTime",
        "imageUrl": "String"
      },
      "example": {
        "lodgingId": 123,
        "lodgingName": "大阪試營運飯店",
        "cityName": "大阪",
        "countryName": "日本",
        "lodgingType": "飯店",
        "status": "active",
        "createdAt": "2024-01-15T10:30:00",
        "updatedAt": "2024-01-20T14:45:00",
        "imageUrl": "https://cdn.example.com/imgs/osa1.jpg"
      },
      "usage": "商家 DTO，用於商家住宿列表顯示"
    },
    {
      "fileName": "VendorLodgingDetailDTO.java",
      "type": "class",
      "description": "商家住宿詳情 DTO，繼承自 LodgingResponseDTO，專用於商家後台管理",
      "jsonFormat": {
        "id": "Integer",
        "lodgingName": "String",
        "description": "String",
        "address": "String",
        "cityId": "Integer",
        "cityName": "String",
        "countryId": "Integer",
        "countryName": "String",
        "lodgingTypeId": "Integer",
        "lodgingTypeName": "String",
        "latitude": "BigDecimal",
        "longitude": "BigDecimal",
        "lodgingTel": "String",
        "email": "String",
        "isActive": "Boolean",
        "createdAt": "LocalDateTime",
        "updatedAt": "LocalDateTime",
        "images": "List<ImageDTO>",
        "updatedBy": "Integer"
      },
      "example": {
        "id": 123,
        "lodgingName": "大阪試營運飯店",
        "description": "位於大阪市中心的豪華飯店",
        "address": "大阪市中央區1-1-1",
        "cityId": 48,
        "cityName": "大阪",
        "countryId": 183,
        "countryName": "日本",
        "lodgingTypeId": 1,
        "lodgingTypeName": "飯店",
        "latitude": 34.6937,
        "longitude": 135.5023,
        "lodgingTel": "+81-6-1234-5678",
        "email": "info@osaka-hotel.com",
        "isActive": true,
        "createdAt": "2024-01-15T10:30:00",
        "updatedAt": "2024-01-20T14:45:00",
        "images": [
          {
            "id": 1,
            "imageUrl": "https://cdn.example.com/imgs/osa1.jpg",
            "imageOrder": 1,
            "lodgingId": 123
          }
        ],
        "updatedBy": 3
      },
      "usage": "商家 DTO，繼承 LodgingResponseDTO，額外包含 updatedBy 欄位追蹤修改者"
    },
    {
      "fileName": "VendorRoomTypeListDTO.java",
      "type": "record",
      "description": "商家房型列表 DTO，用於房型管理",
      "jsonFormat": {
        "roomTypeId": "Integer",
        "roomTypeName": "String"
      },
      "example": {
        "roomTypeId": 1,
        "roomTypeName": "標準雙人房"
      },
      "usage": "商家 DTO，用於房型列表顯示"
    },
    {
      "fileName": "LodgingRequestDTO.java",
      "type": "class",
      "description": "住宿請求 DTO，用於創建和更新住宿資訊",
      "jsonFormat": {
        "lodgingName": "String",
        "description": "String",
        "address": "String",
        "cityId": "Integer",
        "countryId": "Integer",
        "lodgingTypeId": "Integer",
        "images": "List<ImageDTO>",
        "facilityIds": "List<Integer>"
      },
      "example": {
        "lodgingName": "大阪試營運飯店",
        "description": "位於大阪市中心的豪華飯店",
        "address": "大阪市中央區1-1-1",
        "cityId": 48,
        "countryId": 183,
        "lodgingTypeId": 1,
        "images": [
          {
            "imageUrl": "https://cdn.example.com/imgs/osa1.jpg",
            "imageOrder": 1
          }
        ],
        "facilityIds": [1, 2, 3]
      },
      "usage": "核心 DTO，用於住宿創建和更新操作"
    },
    {
      "fileName": "LodgingResponseDTO.java",
      "type": "class",
      "description": "住宿回應 DTO，包含完整的住宿資訊",
      "jsonFormat": {
        "lodgingId": "Integer",
        "lodgingName": "String",
        "description": "String",
        "address": "String",
        "cityId": "Integer",
        "cityName": "String",
        "countryId": "Integer",
        "countryName": "String",
        "lodgingTypeId": "Integer",
        "lodgingType": "String",
        "status": "String",
        "createdAt": "LocalDateTime",
        "updatedAt": "LocalDateTime",
        "images": "List<ImageDTO>",
        "facilities": "List<FacilityDTO>"
      },
      "example": {
        "lodgingId": 123,
        "lodgingName": "大阪試營運飯店",
        "description": "位於大阪市中心的豪華飯店",
        "address": "大阪市中央區1-1-1",
        "cityId": 48,
        "cityName": "大阪",
        "countryId": 183,
        "countryName": "日本",
        "lodgingTypeId": 1,
        "lodgingType": "飯店",
        "status": "active",
        "createdAt": "2024-01-15T10:30:00",
        "updatedAt": "2024-01-20T14:45:00",
        "images": [
          {
            "id": 1,
            "imageUrl": "https://cdn.example.com/imgs/osa1.jpg",
            "imageOrder": 1,
            "lodgingId": 123
          }
        ],
        "facilities": [
          {"id": 1, "name": "WiFi"},
          {"id": 2, "name": "停車場"}
        ]
      },
      "usage": "核心 DTO，用於住宿資訊回應"
    },
    {
      "fileName": "LodgingTypeDTO.java",
      "type": "class",
      "description": "住宿類型 DTO，用於住宿類型管理",
      "jsonFormat": {
        "id": "Integer",
        "name": "String",
        "description": "String",
        "iconUrl": "String",
        "isActive": "Boolean"
      },
      "example": {
        "id": 1,
        "name": "飯店",
        "description": "提供完整服務的住宿設施",
        "iconUrl": "https://cdn.example.com/icons/hotel.svg",
        "isActive": true
      },
      "usage": "核心 DTO，用於住宿類型選項"
    },
    {
      "fileName": "RoomTypeCreateRequestDTO.java",
      "type": "class",
      "description": "房型創建請求 DTO，用於創建新房型",
      "jsonFormat": {
        "roomTypeName": "String",
        "description": "String",
        "maxGuests": "Integer",
        "pricePerNight": "BigDecimal",
        "lodgingId": "Integer",
        "images": "List<ImageDTO>",
        "bedTypeIds": "List<Integer>",
        "facilityIds": "List<Integer>"
      },
      "example": {
        "roomTypeName": "標準雙人房",
        "description": "舒適的雙人房，配備現代化設施",
        "maxGuests": 2,
        "pricePerNight": 8000.0,
        "lodgingId": 123,
        "images": [
          {
            "imageUrl": "https://cdn.example.com/imgs/room1.jpg",
            "imageOrder": 1
          }
        ],
        "bedTypeIds": [1],
        "facilityIds": [1, 2]
      },
      "usage": "房型 DTO，用於房型創建操作"
    },
    {
      "fileName": "RoomTypeCreateResponseDTO.java",
      "type": "class",
      "description": "房型創建回應 DTO，包含完整的房型資訊",
      "jsonFormat": {
        "roomTypeId": "Integer",
        "roomTypeName": "String",
        "description": "String",
        "maxGuests": "Integer",
        "pricePerNight": "BigDecimal",
        "lodgingId": "Integer",
        "lodgingName": "String",
        "status": "String",
        "createdAt": "LocalDateTime",
        "updatedAt": "LocalDateTime",
        "images": "List<ImageDTO>",
        "bedTypes": "List<BedTypeDTO>",
        "facilities": "List<FacilityDTO>"
      },
      "example": {
        "roomTypeId": 1,
        "roomTypeName": "標準雙人房",
        "description": "舒適的雙人房，配備現代化設施",
        "maxGuests": 2,
        "pricePerNight": 8000.0,
        "lodgingId": 123,
        "lodgingName": "大阪試營運飯店",
        "status": "active",
        "createdAt": "2024-01-15T10:30:00",
        "updatedAt": "2024-01-20T14:45:00",
        "images": [
          {
            "id": 1,
            "imageUrl": "https://cdn.example.com/imgs/room1.jpg",
            "imageOrder": 1,
            "lodgingId": 123
          }
        ],
        "bedTypes": [{"id": 1, "name": "雙人床"}],
        "facilities": [
          {"id": 1, "name": "WiFi"},
          {"id": 2, "name": "停車場"}
        ]
      },
      "usage": "房型 DTO，用於房型創建回應"
    }
  ],
  "summary": {
    "totalFiles": 16,
    "categories": {
      "basic": 3,
      "search": 5,
      "vendor": 3,
      "core": 3,
      "roomType": 2
    },
    "types": {
      "class": 8,
      "record": 8
    },
    "keyFeatures": [
      "統一使用 ImageDTO 處理所有圖片操作",
      "LodgingRequestDTO 和 LodgingResponseDTO 用於創建和更新操作",
      "完整的搜尋和篩選功能支援",
      "商家後台管理專用 DTO",
      "房型管理完整支援",
      "VendorLodgingDetailDTO 繼承 LodgingResponseDTO 避免代碼重複"
    ]
  }
}
```

### DTO 使用指南

#### 1. 圖片處理統一化
- 所有住宿和房型圖片都使用 `ImageDTO`
- 支援圖片排序和關聯住宿 ID
- 向後相容現有 JSON 格式

#### 2. 搜尋功能完整化
- `LodgingSearchRequestDTO` 支援多種搜尋條件
- `FilterOptionsDTO` 提供靈活的篩選選項
- 搜尋結果包含完整的住宿和篩選資訊

#### 3. 商家管理專用化
- `VendorLodgingListDTO` 和 `VendorLodgingDetailDTO` 專為商家後台設計
- 包含狀態管理和時間戳記
- 支援完整的 CRUD 操作

#### 4. 房型管理獨立化
- `RoomTypeCreateRequestDTO` 和 `RoomTypeCreateResponseDTO` 專用於房型操作
- 支援床型和設施關聯
- 包含價格和容量資訊

#### 5. 繼承關係優化
- `VendorLodgingDetailDTO` 繼承自 `LodgingResponseDTO`
- 避免代碼重複，提高維護性
- 商家後台額外包含 `updatedBy` 欄位追蹤修改者

## 🔧 選項 API 端點

### 1. 獲取住宿類型選項

```http
GET /api/options/lodgings-types
```

**響應格式：**

```json
[
  { "id": 1, "label": "飯店" },
  { "id": 2, "label": "民宿" },
  { "id": 3, "label": "青年旅館" }
]
```

### 2. 獲取城市選項

```http
GET /api/options/cities
```

**響應格式：**

```json
[
  { "id": 48, "label": "大阪" },
  { "id": 49, "label": "東京" },
  { "id": 50, "label": "京都" }
]
```

### 3. 獲取國家選項

```http
GET /api/options/countries
```

**響應格式：**

```json
[
  { "id": 183, "label": "日本" },
  { "id": 184, "label": "韓國" },
  { "id": 185, "label": "台灣" }
]
```

## 🔍 一般用戶功能 API 端點

### 1. 搜尋旅宿

```http
POST /api/lodgings/search
Content-Type: application/json
```

**請求格式：**

```json
{
  "cityName": "大阪",
  "checkInDate": "2024-02-01",
  "checkOutDate": "2024-02-03",
  "guestCount": 2
}
```

**成功響應 (200 OK)：**

```json
{
  "lodgings": [
    {
      "lodgingId": 123,
      "lodgingName": "大阪試營運飯店",
      "avgRating": 4.5,
      "reviewCount": 128,
      "roomTypeName": "標準雙人房",
      "roomTypeDescription": "舒適的雙人房，配備現代化設施",
      "maxGuests": 2,
      "pricePerNight": 8000.0,
      "imageUrl": "https://cdn.example.com/imgs/osa1.jpg",
      "lodgingTypeId": 1,
      "lodgingType": "飯店",
      "bedTypes": [{ "id": 1, "name": "雙人床" }],
      "facilities": [
        { "id": 1, "name": "WiFi" },
        { "id": 2, "name": "停車場" }
      ]
    }
  ],
  "filterOptions": {
    "bedTypes": [
      { "id": 1, "name": "雙人床" },
      { "id": 2, "name": "單人床" }
    ],
    "facilities": [
      { "id": 1, "name": "WiFi" },
      { "id": 2, "name": "停車場" }
    ],
    "lodgingTypes": [
      { "id": 1, "name": "飯店" },
      { "id": 2, "name": "民宿" }
    ]
  }
}
```

### 2. 獲取熱門旅宿

```http
GET /api/lodgings/hot
```

**成功響應 (200 OK)：**

```json
[
  {
    "id": 123,
    "lodgingName": "大阪試營運飯店",
    "cityName": "大阪",
    "avgScore": 4.5,
    "reviewCnt": 128,
    "imageUrl": "https://cdn.example.com/imgs/osa1.jpg"
  }
]
```

## 🏨 商家管理功能 API 端點

### 1. 創建住宿

```http
POST /api/vendor/lodgings
Content-Type: application/json
```

**請求格式：**

```json
{
  "lodgingName": "大阪試營運飯店",
  "lodgingTypeId": 1,
  "description": "位於心齋橋站附近，交通便利",
  "address": "大阪市中央區測試路100號",
  "cityId": 48,
  "countryId": 183,
  "latitude": 34.6937,
  "longitude": 135.5023,
  "lodgingTel": "06-1234-5678",
  "email": "hotel_osaka@example.com",
  "isActive": true,
  "images": [
    {
      "imageType": "lodging",
      "roomTypeId": null,
      "imageUrl": "https://cdn.example.com/imgs/osa1.jpg",
      "mimeType": "image/jpeg",
      "sortOrder": 0
    }
  ]
}
```

**成功響應 (200 OK)：**

```json
{
  "id": 123,
  "lodgingName": "大阪試營運飯店",
  "description": "位於心齋橋站附近，交通便利",
  "address": "大阪市中央區測試路100號",
  "latitude": 34.6937,
  "longitude": 135.5023,
  "lodgingTel": "06-1234-5678",
  "email": "hotel_osaka@example.com",
  "isActive": false,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00",
  "lodgingTypeId": 1,
  "lodgingTypeName": "飯店",
  "cityId": 48,
  "cityName": "大阪",
  "countryId": 183,
  "countryName": "日本",
  "images": [
    {
      "imageId": 456,
      "imageType": "lodging",
      "roomTypeId": null,
      "imageUrl": "https://cdn.example.com/imgs/osa1.jpg",
      "mimeType": "image/jpeg",
      "sortOrder": 0,
      "isActive": true,
      "uploadedAt": "2024-01-15T10:30:00"
    }
  ]
}
```

**創建後狀態說明：**

- 新創建的住宿**強制為停業狀態**
- `isActive`: false (前台不顯示，忽略請求中的 isActive 值)
- `deleteStatus`: 1 (正常狀態)
- 商家需要手動啟用才能在前台顯示
- **注意**: 即使請求中包含 `"isActive": true`，也會被強制設為 `false`

## 🛏️ 房型管理功能 API 端點

### 1. 新增房型

```http
POST /api/vendor/lodgings/{lodgingId}/room-types
Content-Type: application/json
```

**請求格式：**

```json
{
  "roomTypeName": "標準雙人房",
  "description": "舒適的雙人房，配備現代化設施",
  "pricePerNight": 8000,
  "maxGuests": 2,
  "isActive": true,
  "beds": [
    {
      "id": 1,
      "name": "雙人床",
      "quantity": 1
    }
  ],
  "facilityIds": [1, 2, 3],
  "images": [
    {
      "imageType": "room",
      "imageUrl": "https://cdn.example.com/imgs/room1.jpg",
      "mimeType": "image/jpeg",
      "sortOrder": 0
    }
  ]
}
```

**成功響應 (201 Created)：**

```json
{
  "id": 456,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00",
  "roomTypeName": "標準雙人房",
  "description": "舒適的雙人房，配備現代化設施",
  "pricePerNight": 8000,
  "maxGuests": 2,
  "isActive": false,
  "lodgingId": 123,
  "lodgingName": "大阪試營運飯店",
  "createdBy": 3,
  "createdByName": "測試商家",
  "updatedBy": 3,
  "updatedByName": "測試商家",
  "beds": [
    {
      "id": 1,
      "name": "雙人床",
      "quantity": 1
    }
  ],
  "facilityIds": [1, 2, 3],
  "images": [
    {
      "imageId": 789,
      "imageType": "room",
      "roomTypeId": 456,
      "imageUrl": "https://cdn.example.com/imgs/room1.jpg",
      "mimeType": "image/jpeg",
      "sortOrder": 0,
      "isActive": true,
      "uploadedAt": "2024-01-15T10:30:00"
    }
  ]
}
```

**創建後狀態說明：**

- 新創建的房型**強制為停業狀態**
- `isActive`: false (前台不顯示，忽略請求中的 isActive 值)
- `deleteStatus`: 1 (正常狀態)
- 商家需要手動啟用才能在前台顯示

### 2. 查詢飯店全部房型簡歷

```http
GET /api/vendor/lodgings/{lodgingId}/room-types
```

**成功響應 (200 OK)：**

```json
[
  {
    "id": 456,
    "name": "標準雙人房",
    "description": "舒適的雙人房，配備現代化設施",
    "pricePerNight": 8000,
    "maxGuests": 2,
    "isActive": true,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-20T14:45:00",
    "lodgingId": 123,
    "lodgingName": "大阪試營運飯店",
    "images": [
      {
        "imageType": "room",
        "imageUrl": "https://cdn.example.com/imgs/room1.jpg",
        "sortOrder": 0
      }
    ]
  }
]
```

### 3. 查詢單筆房型詳細

```http
GET /api/vendor/lodgings/{lodgingId}/room-types/{roomTypeId}
```

**成功響應 (200 OK)：**

```json
{
  "id": 456,
  "name": "標準雙人房",
  "description": "舒適的雙人房，配備現代化設施",
  "pricePerNight": 8000,
  "maxGuests": 2,
  "isActive": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-20T14:45:00",
  "lodgingId": 123,
  "lodgingName": "大阪試營運飯店",
  "createdBy": 3,
  "createdByName": "測試商家",
  "updatedBy": 3,
  "updatedByName": "測試商家",
  "beds": [
    {
      "id": 1,
      "name": "雙人床",
      "quantity": 1
    }
  ],
  "facilities": [
    {
      "id": 1,
      "name": "WiFi"
    },
    {
      "id": 2,
      "name": "停車場"
    }
  ],
  "images": [
    {
      "imageType": "room",
      "imageUrl": "https://cdn.example.com/imgs/room1.jpg",
      "sortOrder": 0
    }
  ]
}
```

### 4. 更新房型資料

```http
PUT /api/vendor/lodgings/{lodgingId}/room-types/{roomTypeId}
Content-Type: application/json
```

**請求格式：**

```json
{
  "roomTypeName": "豪華雙人房",
  "description": "升級版雙人房，配備更高級設施",
  "pricePerNight": 12000,
  "maxGuests": 2,
  "isActive": true,
  "beds": [
    {
      "id": 1,
      "name": "雙人床",
      "quantity": 1
    }
  ],
  "facilityIds": [1, 2, 3, 4],
  "images": [
    {
      "imageType": "room",
      "imageUrl": "https://cdn.example.com/imgs/room1_updated.jpg",
      "mimeType": "image/jpeg",
      "sortOrder": 0
    }
  ]
}
```

**成功響應 (200 OK)：**

回傳更新後的房型詳細資料，格式同查詢單筆房型詳細。

### 5. 刪除房型資料

```http
DELETE /api/vendor/lodgings/{lodgingId}/room-types/{roomTypeId}
```

**成功響應 (204 No Content)：**

無回應內容

**錯誤響應：**

- `404 Not Found`: 房型不存在
- `403 Forbidden`: 權限不足
- `400 Bad Request`: 房型已經被刪除

### 2. 獲取住宿詳情

```http
GET /api/vendor/lodgings/{id}
```

**成功響應 (200 OK)：**

```json
{
  "id": 123,
  "lodgingName": "大阪試營運飯店",
  "description": "位於心齋橋站附近，交通便利",
  "address": "大阪市中央區測試路100號",
  "latitude": 34.6937,
  "longitude": 135.5023,
  "lodgingTel": "06-1234-5678",
  "email": "hotel_osaka@example.com",
  "isActive": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00",
  "updatedBy": 3,
  "lodgingTypeId": 1,
  "lodgingTypeName": "飯店",
  "cityId": 48,
  "cityName": "大阪",
  "countryId": 183,
  "countryName": "日本",
  "images": [
    {
      "imageId": 456,
      "imageType": "lodging",
      "roomTypeId": null,
      "imageUrl": "https://cdn.example.com/imgs/osa1.jpg",
      "mimeType": "image/jpeg",
      "sortOrder": 0,
      "isActive": true
    }
  ]
}
```

### 3. 更新住宿

```http
PUT /api/vendor/lodgings/{id}
Content-Type: application/json
```

**請求格式：**

```json
{
  "lodgingName": "大阪試營運飯店 (已更新)",
  "lodgingTypeId": 1,
  "description": "位於心齋橋站附近，交通便利 (已更新)",
  "address": "大阪市中央區測試路100號",
  "cityId": 48,
  "countryId": 183,
  "latitude": 34.6937,
  "longitude": 135.5023,
  "lodgingTel": "06-1234-5678",
  "email": "hotel_osaka@example.com",
  "isActive": true,
  "images": [
    {
      "imageId": 456,
      "imageType": "lodging",
      "roomTypeId": null,
      "imageUrl": "https://cdn.example.com/imgs/osa1_updated.jpg",
      "mimeType": "image/jpeg",
      "sortOrder": 0
    },
    {
      "imageType": "lodging",
      "roomTypeId": null,
      "imageUrl": "https://cdn.example.com/imgs/osa2.jpg",
      "mimeType": "image/jpeg",
      "sortOrder": 1
    }
  ]
}
```

**成功響應 (200 OK)：**

```json
{
  "id": 123,
  "lodgingName": "大阪試營運飯店 (已更新)",
  "description": "位於心齋橋站附近，交通便利 (已更新)",
  "address": "大阪市中央區測試路100號",
  "latitude": 34.6937,
  "longitude": 135.5023,
  "lodgingTel": "06-1234-5678",
  "email": "hotel_osaka@example.com",
  "isActive": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T11:00:00",
  "lodgingTypeId": 1,
  "lodgingTypeName": "飯店",
  "cityId": 48,
  "cityName": "大阪",
  "countryId": 183,
  "countryName": "日本",
  "images": [
    {
      "imageId": 456,
      "imageType": "lodging",
      "roomTypeId": null,
      "imageUrl": "https://cdn.example.com/imgs/osa1_updated.jpg",
      "mimeType": "image/jpeg",
      "sortOrder": 0,
      "isActive": true,
      "uploadedAt": "2024-01-15T10:30:00"
    },
    {
      "imageId": 458,
      "imageType": "lodging",
      "roomTypeId": null,
      "imageUrl": "https://cdn.example.com/imgs/osa2.jpg",
      "mimeType": "image/jpeg",
      "sortOrder": 1,
      "isActive": true,
      "uploadedAt": "2024-01-15T11:00:00"
    }
  ]
}
```

### 4. 獲取供應商住宿列表（支援狀態篩選）

#### 4.1 在售旅宿

```http
GET /api/vendor/lodgings
或
GET /api/vendor/lodgings?status=active
```

#### 4.2 停業旅宿

```http
GET /api/vendor/lodgings?status=inactive
```

#### 4.3 已下架旅宿

```http
GET /api/vendor/lodgings?status=deleted
```

#### 4.4 全部旅宿

```http
GET /api/vendor/lodgings?status=all
```

**成功響應 (200 OK)：**

```json
[
  {
    "id": 123,
    "lodgingName": "大阪試營運飯店",
    "countryName": "日本",
    "cityName": "大阪",
    "address": "大阪市中央區測試路100號",
    "isActive": true,
    "deleteStatus": 1,
    "statusText": "在售"
  },
  {
    "id": 124,
    "lodgingName": "東京商務飯店",
    "countryName": "日本",
    "cityName": "東京",
    "address": "東京都新宿區測試路200號",
    "isActive": false,
    "deleteStatus": 1,
    "statusText": "停業"
  }
]
```

**狀態說明：**

- `isActive`: true/false (前台是否上架)
- `deleteStatus`: 1(正常)/0(軟刪)/-1(實際刪除)
- `statusText`: 狀態文字說明

### 5. 軟刪除住宿

```http
DELETE /api/vendor/lodgings/{id}
```

**成功響應 (204 No Content)：**

```
(無內容)
```

**錯誤響應 (404 Not Found)：**

```json
{
  "error": "Lodging not found"
}
```

**錯誤響應 (403 Forbidden)：**

```json
{
  "error": "Access denied"
}
```

**業務邏輯：**

- 設置 `is_active = 0`
- 設置 `delete_status = 0`
- 設置 `deleted_time = NOW()`
- 同時軟刪除相關的圖片
- 保留資料用於審計和恢復

### 6. 停業旅宿

```http
POST /api/vendor/lodgings/{id}/deactivate
```

**路徑參數：**

- `id`: 旅宿 ID

**查詢參數：**

- `vendorId` (可選): 商家 ID

**成功響應 (200 OK)：**

```json
{
  "message": "旅宿已停業"
}
```

**錯誤響應 (400 Bad Request)：**

```json
{
  "error": "旅宿不存在或無權限操作"
}
```

**業務邏輯：**

- 檢查旅宿是否存在且屬於當前商家
- 設置 `is_active = false`
- 更新 `updated_by` 和 `updated_at`
- 旅宿在前台不再顯示，但資料保留

### 7. 重新上架旅宿

```http
POST /api/vendor/lodgings/{id}/activate
```

**路徑參數：**

- `id`: 旅宿 ID

**查詢參數：**

- `vendorId` (可選): 商家 ID

**成功響應 (200 OK)：**

```json
{
  "message": "旅宿已重新上架"
}
```

**錯誤響應 (400 Bad Request)：**

```json
{
  "error": "旅宿不存在或無權限操作"
}
```

```json
{
  "error": "軟刪除的旅宿無法重新上架，請先還原旅宿"
}
```

**業務邏輯：**

- 檢查旅宿是否存在且屬於當前商家
- **檢查 `delete_status != 0`（不是軟刪除狀態）**
- 設置 `is_active = true`
- 更新 `updated_by` 和 `updated_at`
- 旅宿重新在前台顯示

**限制條件：**

- 只能對停業旅宿 (`delete_status = 1, is_active = false`) 進行重新上架
- **不能對軟刪除旅宿 (`delete_status = 0`) 直接重新上架**
- 軟刪除旅宿必須先還原 (`restore`) 才能重新上架

**狀態變更：**

- 停業 → 在售：`(1,false) → (1,true)`

### 8. 軟刪還原旅宿

```http
POST /api/vendor/lodgings/{id}/restore
```

**路徑參數：**

- `id`: 旅宿 ID

**查詢參數：**

- `vendorId` (可選): 商家 ID

**成功響應 (200 OK)：**

```json
{
  "message": "旅宿已還原"
}
```

**錯誤響應 (400 Bad Request)：**

```json
{
  "error": "旅宿不存在或無權限操作"
}
```

**業務邏輯：**

- 檢查旅宿是否存在且屬於當前商家
- 設置 `delete_status = 1`
- 設置 `is_active = false` (預設停業狀態)
- 清除 `deleted_time`
- 更新 `updated_by` 和 `updated_at`
- 從已下架狀態還原為停業狀態

## 📝 欄位規格說明

### 必填欄位

| 欄位名          | 類型    | 說明        | 範例             |
| --------------- | ------- | ----------- | ---------------- |
| `lodgingName`   | String  | 住宿名稱    | "大阪試營運飯店" |
| `lodgingTypeId` | Integer | 住宿類型 ID | 1                |
| `cityId`        | Integer | 城市 ID     | 48               |
| `countryId`     | Integer | 國家 ID     | 183              |

### 可選欄位

| 欄位名        | 類型       | 預設值 | 說明                       |
| ------------- | ---------- | ------ | -------------------------- |
| `description` | String     | null   | 住宿描述                   |
| `address`     | String     | null   | 詳細地址                   |
| `latitude`    | BigDecimal | null   | 緯度座標                   |
| `longitude`   | BigDecimal | null   | 經度座標                   |
| `lodgingTel`  | String     | null   | 聯絡電話                   |
| `email`       | String     | null   | 電子郵件                   |
| `isActive`    | Boolean    | false  | 是否啟用（預設為停業狀態） |
| `images`      | Array      | []     | 圖片陣列                   |

### 圖片欄位規格 (統一 ImageDTO)

| 欄位名       | 類型          | 必填 | 說明                                              |
| ------------ | ------------- | ---- | ------------------------------------------------- |
| `imageId`    | Integer       | ❌   | 圖片 ID (更新時需要，新增時為 null)               |
| `imageType`  | String        | ✅   | 圖片類型: "lodging" (住宿圖片), "room" (房間圖片) |
| `lodgingId`  | Integer       | ❌   | 所屬住宿 ID (響應時自動填充)                      |
| `roomTypeId` | Integer       | ❌   | 所屬房型 ID (房型圖片時使用)                      |
| `imageUrl`   | String        | ✅   | 圖片 URL                                          |
| `mimeType`   | String        | ✅   | MIME 類型                                         |
| `sortOrder`  | Integer       | ✅   | 排序順序 (0 開始)                                 |
| `isActive`   | Boolean       | ❌   | 圖片啟用狀態 (預設: true)                         |
| `uploadedAt` | LocalDateTime | ❌   | 上傳時間 (響應時自動填充)                         |

## 🖼️ 圖片狀態管理

### 圖片操作類型

#### 1. 新增圖片

```json
{
  "imageId": null,
  "imageType": "lodging",
  "imageUrl": "https://cdn.example.com/new1.jpg",
  "mimeType": "image/jpeg",
  "sortOrder": 0,
  "isActive": true
}
```

#### 2. 軟刪除圖片

```json
{
  "imageId": 15,
  "isActive": false
}
```

#### 3. 調整圖片排序

```json
{
  "imageId": 22,
  "sortOrder": 0,
  "isActive": true
}
```

### 混合操作範例

在同一個請求中可以同時進行多種圖片操作：

```json
{
  "lodgingName": "大阪試營運飯店",
  "lodgingTypeId": 1,
  "description": "位於心齋橋站附近，交通便利",
  "address": "大阪市中央區測試路100號",
  "cityId": 48,
  "countryId": 183,
  "latitude": 34.6937,
  "longitude": 135.5023,
  "lodgingTel": "06-1234-5678",
  "email": "hotel_osaka@example.com",
  "isActive": true,
  "images": [
    {
      "imageId": 22,
      "sortOrder": 0,
      "isActive": true
    },
    {
      "imageId": null,
      "imageType": "lodging",
      "imageUrl": "https://cdn.example.com/new.jpg",
      "mimeType": "image/jpeg",
      "sortOrder": 1,
      "isActive": true
    },
    {
      "imageId": 15,
      "isActive": false
    }
  ]
}
```

### 圖片狀態說明

| 狀態       | 說明                 | 資料庫欄位                           |
| ---------- | -------------------- | ------------------------------------ |
| **活躍**   | 圖片正常顯示         | `isActive: true`, `deleteStatus: 1`  |
| **軟刪除** | 圖片已刪除但保留記錄 | `isActive: false`, `deleteStatus: 0` |
| **排序**   | 控制圖片顯示順序     | `sortOrder: 0, 1, 2, ...`            |

### 圖片管理業務邏輯

1. **新增圖片**

   - `imageId` 必須為 `null`
   - 必須提供完整的圖片資訊
   - 自動設置 `deleteStatus: 1`

2. **軟刪除圖片**

   - 設置 `isActive: false`
   - 系統自動設置 `deleteStatus: 0`
   - 保留圖片記錄用於審計

3. **調整排序**

   - 更新現有圖片的 `sortOrder`
   - 排序值從 0 開始
   - 支援任意順序調整

4. **混合操作**
   - 同一個請求可包含多種操作
   - 系統按順序處理每個圖片
   - 保證資料一致性

## 🔍 常用 ID 參考表

### 住宿類型 (lodging_type)

| ID  | 名稱     |
| --- | -------- |
| 1   | 飯店     |
| 2   | 民宿     |
| 3   | 青年旅館 |

### 城市 (cities)

| ID  | 名稱 |
| --- | ---- |
| 48  | 大阪 |
| 49  | 東京 |
| 50  | 京都 |

### 國家 (countries)

| ID  | 名稱 |
| --- | ---- |
| 183 | 日本 |
| 184 | 韓國 |
| 185 | 台灣 |

## ⚠️ 錯誤處理

### 400 Bad Request

```json
{
  "error": "驗證錯誤",
  "message": "圖片數量不能超過5張"
}
```

### 404 Not Found

```json
{
  "error": "住宿不存在",
  "message": "找不到 ID 為 999 的住宿"
}
```

### 500 Internal Server Error

```json
{
  "error": "內部伺服器錯誤",
  "message": "資料庫連接失敗"
}
```

## 💻 前端使用指南

### 1. 初始化下拉選單

```javascript
// 載入所有選項資料
const initializeOptions = async () => {
  try {
    const [lodgingTypes, cities, countries] = await Promise.all([
      fetch("/api/options/lodgings-types").then((r) => r.json()),
      fetch("/api/options/cities").then((r) => r.json()),
      fetch("/api/options/countries").then((r) => r.json()),
    ]);

    // 填充下拉選單
    populateSelect("lodgingTypeSelect", lodgingTypes);
    populateSelect("citySelect", cities);
    populateSelect("countrySelect", countries);

    return { lodgingTypes, cities, countries };
  } catch (error) {
    console.error("載入選項失敗:", error);
    throw error;
  }
};

const populateSelect = (selectId, options) => {
  const select = document.getElementById(selectId);
  select.innerHTML = '<option value="">請選擇...</option>';
  options.forEach((option) => {
    const opt = document.createElement("option");
    opt.value = option.id;
    opt.textContent = option.label;
    select.appendChild(opt);
  });
};
```

### 2. 創建住宿

```javascript
const createLodging = async (formData) => {
  try {
    const response = await fetch("/api/vendor/lodgings", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`API 錯誤: ${response.status} - ${errorText}`);
    }

    return await response.json();
  } catch (error) {
    console.error("創建住宿失敗:", error);
    throw error;
  }
};
```

### 3. 更新住宿

```javascript
const updateLodging = async (lodgingId, formData) => {
  try {
    const response = await fetch(`/api/vendor/lodgings/${lodgingId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`API 錯誤: ${response.status} - ${errorText}`);
    }

    return await response.json();
  } catch (error) {
    console.error("更新住宿失敗:", error);
    throw error;
  }
};
```

### 4. 圖片管理操作

#### 新增圖片

```javascript
const addImage = (images, newImageUrl) => {
  const newImage = {
    imageId: null,
    imageType: "lodging",
    imageUrl: newImageUrl,
    mimeType: "image/jpeg",
    sortOrder: images.length,
    isActive: true,
  };
  return [...images, newImage];
};
```

#### 刪除圖片

```javascript
const deleteImage = (images, imageId) => {
  return images.map((img) =>
    img.imageId === imageId ? { ...img, isActive: false } : img
  );
};
```

#### 調整圖片排序

```javascript
const reorderImages = (images, fromIndex, toIndex) => {
  const reordered = [...images];
  const [movedImage] = reordered.splice(fromIndex, 1);
  reordered.splice(toIndex, 0, movedImage);

  // 更新排序值
  return reordered.map((img, index) => ({
    ...img,
    sortOrder: index,
  }));
};
```

#### 拖曳排序範例

```javascript
// 使用 HTML5 拖曳 API
const setupImageDragAndDrop = () => {
  const imageContainer = document.getElementById("imageContainer");

  imageContainer.addEventListener("dragover", (e) => {
    e.preventDefault();
  });

  imageContainer.addEventListener("drop", (e) => {
    e.preventDefault();
    const fromIndex = parseInt(e.dataTransfer.getData("text/plain"));
    const toIndex = getDropIndex(e);

    const updatedImages = reorderImages(currentImages, fromIndex, toIndex);
    updateImageDisplay(updatedImages);
  });
};

const getDropIndex = (e) => {
  const images = document.querySelectorAll(".image-item");
  for (let i = 0; i < images.length; i++) {
    const rect = images[i].getBoundingClientRect();
    if (e.clientX < rect.left + rect.width / 2) {
      return i;
    }
  }
  return images.length;
};
```

### 4. 獲取住宿列表（支援狀態篩選）

```javascript
const getVendorLodgings = async (status = "active") => {
  try {
    const url =
      status === "active"
        ? "/api/vendor/lodgings"
        : `/api/vendor/lodgings?status=${status}`;

    const response = await fetch(url);
    if (!response.ok) {
      throw new Error(`API 錯誤: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    console.error("獲取住宿列表失敗:", error);
    throw error;
  }
};

// 狀態篩選按鈕範例
const setupStatusFilter = () => {
  const statusButtons = {
    active: document.getElementById("btn-active"),
    inactive: document.getElementById("btn-inactive"),
    deleted: document.getElementById("btn-deleted"),
    all: document.getElementById("btn-all"),
  };

  Object.entries(statusButtons).forEach(([status, button]) => {
    if (button) {
      button.addEventListener("click", async () => {
        try {
          // 更新按鈕狀態
          Object.values(statusButtons).forEach((btn) =>
            btn.classList.remove("active")
          );
          button.classList.add("active");

          // 載入對應狀態的旅宿列表
          const lodgings = await getVendorLodgings(status);
          displayLodgingList(lodgings);
        } catch (error) {
          console.error("篩選失敗:", error);
        }
      });
    }
  });
};

// 顯示旅宿列表
const displayLodgingList = (lodgings) => {
  const container = document.getElementById("lodging-list");
  container.innerHTML = "";

  lodgings.forEach((lodging) => {
    const card = document.createElement("div");
    card.className = "lodging-card";
    card.innerHTML = `
      <h3>${lodging.lodgingName}</h3>
      <p>${lodging.countryName} - ${lodging.cityName}</p>
      <p>${lodging.address}</p>
      <span class="status-badge status-${lodging.statusText.toLowerCase()}">${
      lodging.statusText
    }</span>
    `;
    container.appendChild(card);
  });
};
```

### 5. 獲取住宿詳情

```javascript
const getLodgingDetail = async (lodgingId) => {
  try {
    const response = await fetch(`/api/vendor/lodgings/${lodgingId}`);
    if (!response.ok) {
      throw new Error(`API 錯誤: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    console.error("獲取住宿詳情失敗:", error);
    throw error;
  }
};
```

### 6. 軟刪除住宿

```javascript
const deleteLodging = async (lodgingId) => {
  try {
    const response = await fetch(`/api/vendor/lodgings/${lodgingId}`, {
      method: "DELETE",
    });

    if (response.status === 204) {
      console.log("住宿刪除成功");
      return true;
    } else if (response.status === 404) {
      const error = await response.json();
      throw new Error(error.error || "住宿不存在");
    } else if (response.status === 403) {
      const error = await response.json();
      throw new Error(error.error || "無權限刪除");
    } else {
      throw new Error(`API 錯誤: ${response.status}`);
    }
  } catch (error) {
    console.error("刪除住宿失敗:", error);
    throw error;
  }
};

// 使用範例
const handleDeleteLodging = async (lodgingId) => {
  if (confirm("確定要刪除此住宿嗎？此操作無法復原。")) {
    try {
      await deleteLodging(lodgingId);
      alert("住宿已刪除");
      // 重新載入列表或從列表中移除
      loadLodgingList();
    } catch (error) {
      alert(`刪除失敗: ${error.message}`);
    }
  }
};
```

## 🔒 安全性注意事項

### 1. 資料驗證

- 前端必須驗證所有必填欄位
- 圖片數量限制：最多 5 張
- ID 必須為正整數

### 2. 身份驗證

- 目前暫時不需要身份驗證
- vendor_id 固定為 3
- 後續會實作完整的身份驗證機制

### 3. 資料一致性

- 使用外鍵約束確保資料完整性
- 實作軟刪除機制
- 所有操作都有審計記錄

## 📊 效能優化

### 1. 快取機制

- 選項資料使用 `@Cacheable` 快取
- 減少重複的資料庫查詢

### 2. 批次操作

- 圖片使用 `saveAll()` 批次儲存
- 提升資料庫操作效能

### 3. 查詢優化

- 使用 `JOIN FETCH` 避免 N+1 問題
- 只查詢必要的欄位

## 🧪 測試建議

### 1. API 測試

- 使用 Postman 測試所有端點
- 驗證請求/響應格式
- 測試錯誤處理機制

### 2. 前端測試

- 測試表單驗證
- 測試下拉選單載入
- 測試錯誤提示

### 3. 整合測試

- 測試完整的 CRUD 流程
- 驗證資料一致性
- 測試圖片管理功能

### 4. 圖片管理測試

#### 新增圖片測試

```javascript
// 測試新增圖片
const testAddImage = async () => {
  const lodgingData = {
    lodgingName: "測試飯店",
    lodgingTypeId: 1,
    // ... 其他欄位
    images: [
      {
        imageId: null,
        imageType: "lodging",
        imageUrl: "https://example.com/test1.jpg",
        mimeType: "image/jpeg",
        sortOrder: 0,
        isActive: true,
      },
    ],
  };

  const result = await createLodging(lodgingData);
  console.log("新增圖片成功:", result.images);
};
```

#### 軟刪除圖片測試

```javascript
// 測試軟刪除圖片
const testDeleteImage = async (lodgingId, imageId) => {
  const lodging = await getLodgingDetail(lodgingId);
  const updatedImages = lodging.images.map((img) =>
    img.imageId === imageId ? { ...img, isActive: false } : img
  );

  const updateData = { ...lodging, images: updatedImages };
  const result = await updateLodging(lodgingId, updateData);
  console.log("軟刪除圖片成功:", result.images);
};
```

#### 圖片排序測試

```javascript
// 測試圖片排序調整
const testReorderImages = async (lodgingId) => {
  const lodging = await getLodgingDetail(lodgingId);
  const reorderedImages = lodging.images.map((img, index) => ({
    ...img,
    sortOrder: lodging.images.length - 1 - index, // 反轉排序
  }));

  const updateData = { ...lodging, images: reorderedImages };
  const result = await updateLodging(lodgingId, updateData);
  console.log("排序調整成功:", result.images);
};
```

#### 混合操作測試

```javascript
// 測試混合圖片操作
const testMixedImageOperations = async (lodgingId) => {
  const lodging = await getLodgingDetail(lodgingId);

  const mixedImages = [
    // 保留並調整排序
    { ...lodging.images[0], sortOrder: 2, isActive: true },
    // 新增圖片
    {
      imageId: null,
      imageType: "lodging",
      imageUrl: "https://example.com/new.jpg",
      mimeType: "image/jpeg",
      sortOrder: 0,
      isActive: true,
    },
    // 軟刪除圖片
    { ...lodging.images[1], isActive: false },
  ];

  const updateData = { ...lodging, images: mixedImages };
  const result = await updateLodging(lodgingId, updateData);
  console.log("混合操作成功:", result.images);
};
```

## 🔧 圖片 DTO 統一整合詳情

### 📊 整合成果

本次整合成功統一了所有住宿和房型相關的圖片 DTO 結構，實現了以下目標：

#### 移除的重複程式碼

- ✅ `LodgingRequestDTO.LodgingImageDTO` (約 80 行)
- ✅ `LodgingResponseDTO.LodgingImageResponseDTO` (約 80 行)
- ✅ `RoomTypeCreateRequestDTO.RoomTypeImageDTO` (約 80 行)
- ✅ `RoomTypeCreateResponseDTO.LodgingImageResponseDTO` (約 80 行)

**總計移除約 320 行重複程式碼！**

#### 統一的 ImageDTO 結構

```java
public class ImageDTO {
    private Integer imageId;        // 圖片ID (更新時使用)
    private String imageType;       // 圖片類型
    private Integer lodgingId;      // 所屬住宿ID
    private Integer roomTypeId;     // 所屬房型ID (可選)
    private String imageUrl;        // 圖片URL
    private String mimeType;        // MIME類型
    private Integer sortOrder;      // 排序順序
    private Boolean isActive;       // 啟用狀態
    private LocalDateTime uploadedAt; // 上傳時間 (響應時)
}
```

### 🎯 整合優勢

#### 1. 一致性

- 所有 DTO 使用相同的圖片結構
- 消除了不同 DTO 之間的結構差異
- 簡化了 API 契約

#### 2. 維護性

- 只需要維護一套圖片 DTO 結構
- 修改圖片欄位時只需要更新一個地方
- 減少了程式碼重複

#### 3. 效能

- 減少了 DTO 轉換的開銷
- 簡化了服務層的邏輯

#### 4. 向後相容性

- JSON 格式保持穩定
- 僅新增 `lodgingId` 欄位，不影響現有功能

### 📋 JSON 格式變化

#### 整合前

```json
{
  "images": [
    {
      "imageId": 1,
      "imageType": "lodging",
      "roomTypeId": null,
      "imageUrl": "https://example.com/image1.jpg",
      "mimeType": "image/jpeg",
      "sortOrder": 1,
      "isActive": true,
      "uploadedAt": "2024-01-01T10:00:00"
    }
  ]
}
```

#### 整合後

```json
{
  "images": [
    {
      "imageId": 1,
      "imageType": "lodging",
      "lodgingId": 123,
      "roomTypeId": null,
      "imageUrl": "https://example.com/image1.jpg",
      "mimeType": "image/jpeg",
      "sortOrder": 1,
      "isActive": true,
      "uploadedAt": "2024-01-01T10:00:00"
    }
  ]
}
```

#### 關鍵差異

- **新增欄位**: `lodgingId` - 提供更多上下文資訊
- **圖片類型標準化**: 使用標準化的圖片類型枚舉 (lodging, room)
- **保持相容**: 所有原有欄位保持不變

### 🔄 服務層整合

#### 統一的 ImageService

所有圖片操作現在都通過統一的 `ImageService` 處理：

```java
@Service
public class ImageService {
    public List<ImageDTO> saveImages(List<ImageDTO> images);
    public List<ImageDTO> updateImages(List<ImageDTO> images);
    public void validateImageCount(List<ImageDTO> images, int maxCount);
}
```

#### 簡化的服務邏輯

```java
// 整合前：需要複雜的 DTO 轉換
List<LodgingImageResponseDTO> savedImages = new ArrayList<>();
for (ImageDTO savedImageDto : savedImageDtos) {
    savedImages.add(new LodgingImageResponseDTO(
        savedImageDto.getImageId(),
        savedImageDto.getImageType(),
        // ... 更多轉換邏輯
    ));
}

// 整合後：直接使用統一 DTO
List<ImageDTO> savedImages = imageService.saveImages(imageDtos);
```

### 🧪 測試建議

#### 圖片 DTO 整合測試

```javascript
// 測試統一的圖片 DTO 結構
const testUnifiedImageDTO = async () => {
  const lodgingData = {
    lodgingName: "測試飯店",
    lodgingTypeId: 1,
    // ... 其他欄位
    images: [
      {
        imageId: null,
        imageType: "lodging", // 使用標準化類型
        imageUrl: "https://example.com/lodging.jpg",
        mimeType: "image/jpeg",
        sortOrder: 0,
        isActive: true,
      },
      {
        imageId: null,
        imageType: "room", // 使用標準化類型
        imageUrl: "https://example.com/room.jpg",
        mimeType: "image/jpeg",
        sortOrder: 1,
        isActive: true,
      },
    ],
  };

  const result = await createLodging(lodgingData);
  console.log("統一 DTO 測試成功:", result.images);

  // 驗證響應包含 lodgingId
  result.images.forEach((img) => {
    console.assert(img.lodgingId !== undefined, "響應應包含 lodgingId");
  });
};
```

## 📈 監控指標

### 1. 效能指標

- API 響應時間
- 資料庫查詢次數
- 快取命中率

### 2. 錯誤指標

- API 錯誤率
- 驗證錯誤次數
- 資料庫連接錯誤

### 3. 業務指標

- 住宿創建數量
- 圖片上傳數量
- 用戶操作頻率

---

**報告版本：** v2.3  
**更新日期：** 2025-01-27
**維護者：** 後端開發團隊
**更新內容：**

- 完成 Lodging 功能拆分，分離一般用戶和商家功能
- 更新所有 API 端點路徑
- 新增架構說明和模組分類
- 新增商家旅宿狀態篩選功能（在售/停業/已下架/全部）
- 更新 VendorLodgingListDTO 包含狀態資訊
- **🎯 完成圖片 DTO 統一整合**
  - 移除重複的內部圖片 DTO 類別 (約 320 行程式碼)
  - 統一使用 `ImageDTO` 處理所有圖片操作
  - 提升程式碼一致性和維護性
  - 保持 100% 向後相容性
- **🔄 DTO 命名優化**
  - `LodgingCreateRequestDTO` → `LodgingRequestDTO` (建立/編輯通用)
  - `LodgingCreateResponseDTO` → `LodgingResponseDTO` (建立/編輯通用)
  - 提升 API 語意清晰度和維護性

## 4. 圖片管理

### 4.1 統一圖片服務 (UnifiedImageService)

#### 功能特點
- **統一管理**: 支援 lodging、room_type、comment 三種目標類型
- **自動壓縮**: 根據目標類型自動調整圖片尺寸和品質
- **格式統一**: 統一轉換為 JPEG 格式
- **軟刪除**: 支援圖片軟刪除機制

#### 圖片配置
```java
// 評論圖片配置
COMMENT_MAX_SIZE = 400px      // 最大尺寸
COMMENT_QUALITY = 0.6f        // 壓縮品質 (60%)
COMMENT_MAX_SIZE_BYTES = 500KB // 最大檔案大小

// 商家圖片配置  
VENDOR_MAX_SIZE = 800px       // 最大尺寸
VENDOR_QUALITY = 0.8f         // 壓縮品質 (80%)
VENDOR_MAX_SIZE_BYTES = 2MB   // 最大檔案大小
```

#### 自動處理流程
1. **尺寸檢查**: 檢查圖片是否超過最大尺寸限制
2. **尺寸調整**: 按比例縮放圖片，保持寬高比
3. **品質壓縮**: 根據目標類型設定壓縮品質
4. **格式轉換**: 統一轉換為 JPEG 格式
5. **檔案儲存**: 儲存處理後的圖片

#### 處理效果
- **評論圖片**: 400px 最大尺寸，60% 壓縮品質，500KB 限制
- **商家圖片**: 800px 最大尺寸，80% 壓縮品質，2MB 限制
- **自動優化**: 大幅減少檔案大小，提升載入速度

### 4.2 圖片上傳端點

#### 單一圖片上傳
```http
POST /api/images/upload
Content-Type: multipart/form-data

Parameters:
- file: 圖片檔案
- targetType: 目標類型 (lodging|room_type|comment)
- targetId: 目標ID
```

#### 批次圖片上傳
```http
POST /api/images/upload/batch
Content-Type: multipart/form-data

Parameters:
- files: 圖片檔案陣列
- targetType: 目標類型
- targetId: 目標ID
```

### 4.3 圖片資料格式

#### BaseImageDTO (統一格式)
```json
{
  "imageId": 123,
  "imageType": "comment",
  "targetId": 456,
  "targetType": "comment",
  "imageUrl": "http://localhost:8080/uploads/comments/456/image.jpg",
  "mimeType": "image/jpeg",
  "sortOrder": 0,
  "isActive": true,
  "uploadedAt": "2025-07-08T10:30:00"
}
```

### 4.4 圖片管理功能

#### 查詢圖片
- `getActiveImagesByTarget()`: 查詢有效圖片
- `getAllImagesByTarget()`: 查詢所有圖片（包括無效的）

#### 更新圖片
- `updateImages()`: 批次更新圖片（新增、修改、刪除）
- 支援軟刪除機制

#### 驗證功能
- `validateImageCount()`: 驗證圖片數量限制
- `validateImageData()`: 驗證圖片資料完整性
