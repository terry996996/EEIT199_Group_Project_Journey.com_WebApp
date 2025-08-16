import axios from 'axios';
import Swal from 'sweetalert2';

// API 基礎配置
// 設定正確的後端 API 地址
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

// 建立 axios 實例
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json'
  },
  withCredentials: true,
  timeout: 30000 // 增加超時時間
});

// 請求攔截器
// apiClient.interceptors.request.use(
//   (config) => {
//     // 添加認證 token
//     let token = localStorage.getItem('token');

//     // 模擬認證：如果沒有 token，創建一個模擬 token
//     if (!token) {
//       console.log('🔧 API 模擬認證：創建模擬 token');
//       // 使用更真實的 JWT 格式
//       const mockPayload = {
//         sub: 'mock-user',
//         iat: Math.floor(Date.now() / 1000),
//         exp: Math.floor(Date.now() / 1000) + 3600
//       };
//       token =
//         'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.' +
//         btoa(JSON.stringify(mockPayload)) +
//         '.mock-signature';
//       localStorage.setItem('token', token);
//     }

//     if (token) {
//       config.headers.Authorization = `Bearer ${token}`;
//     }

//     console.log('API Request:', config.method?.toUpperCase(), config.url, config.data);
//     console.log('🔐 認證 Header:', config.headers.Authorization ? '已設置' : '未設置');
//     return config;
//   },
//   (error) => {
//     console.error('Request Error:', error);
//     return Promise.reject(error);
//   }
// );

// 回應攔截器
apiClient.interceptors.response.use(
  (response) => {
    console.log('API Response:', response.status, response.statusText);
    console.log('API Data:', response.data);
    return response;
  },
  (error) => {
    console.error('API Error:', error);

    // 處理常見錯誤
    if (error.response) {
      const status = error.response.status;
      const message = error.response.data?.message || '請求失敗';

      if (status === 403) {
        Swal.fire({
          icon: 'error',
          title: '權限不足',
          text: '您沒有權限執行此操作',
          confirmButtonText: '確定'
        });
      } else if (status === 404) {
        Swal.fire({
          icon: 'error',
          title: '資源不存在',
          text: '請求的資源不存在',
          confirmButtonText: '確定'
        });
      } else if (status === 500) {
        Swal.fire({
          icon: 'error',
          title: '伺服器錯誤',
          text: '伺服器發生錯誤，請稍後再試',
          confirmButtonText: '確定'
        });
      } else {
        Swal.fire({
          icon: 'error',
          title: `錯誤 ${status}`,
          text: message,
          confirmButtonText: '確定'
        });
      }
    } else if (error.request) {
      Swal.fire({
        icon: 'error',
        title: '網路錯誤',
        text: '無法連接到伺服器，請檢查網路連線',
        confirmButtonText: '確定'
      });
    } else if (error.response?.status === 429) {
      // 速率限制錯誤
      const retryAfter = error.response.headers['retry-after'] || 60;
      Swal.fire({
        icon: 'warning',
        title: '請求過於頻繁',
        text: `請${retryAfter}秒後再試`,
        confirmButtonText: '確定'
      });
    } else if (error.response?.status === 400 && error.response.data?.errors) {
      // 驗證錯誤
      const errorMessages = Object.values(error.response.data.errors).join('\n');
      Swal.fire({
        icon: 'error',
        title: '輸入驗證失敗',
        text: errorMessages,
        confirmButtonText: '確定'
      });
    } else {
      Swal.fire({
        icon: 'error',
        title: '請求錯誤',
        text: error.message,
        confirmButtonText: '確定'
      });
    }

    return Promise.reject(error);
  }
);

// 評論相關 API
export const commentAPI = {
  // 取得評論列表（支援按讚狀態查詢）
  getComments: (params) => {
    const { targetType, targetId, includeLikeStatus, userId } = params;
    
    let url = `/api/comments/target/${targetType}/${targetId}`;
    const queryParams = [];
    
    if (includeLikeStatus) {
      queryParams.push(`includeLikeStatus=true`);
      if (userId) {
        queryParams.push(`userId=${userId}`);
      }
    }
    
    if (queryParams.length > 0) {
      url += `?${queryParams.join('&')}`;
    }
    
    console.log('🔄 查詢評論列表:', { targetType, targetId, includeLikeStatus, userId });
    
    return apiClient.get(url);
  },

  // 新增評論
  createComment: (data) => apiClient.post('/api/comments', data),

  // 新增評論並上傳圖片（新版API）
  createCommentWithFiles: (commentData, files) => {
    const formData = new FormData();

    // 留言資料 - 使用 'comment' 作為 key，包含完整的 comment 物件
    formData.append('comment', JSON.stringify(commentData));

    // 圖片檔案 - 使用 'files' 作為 key
    if (files && files.length > 0) {
      files.forEach((file, index) => {
        formData.append('files', file);
        console.log(`📁 添加檔案 ${index + 1}:`, {
          name: file.name,
          size: file.size,
          type: file.type
        });
      });
    }

    // 調試：檢查 FormData 內容
    console.log('📋 FormData 內容檢查:');
    for (let [key, value] of formData.entries()) {
      if (value instanceof File) {
        console.log(`  ${key}: File(${value.name}, ${value.size} bytes, ${value.type})`);
      } else {
        console.log(`  ${key}:`, value);
      }
    }

    console.log('📤 發送 FormData 請求:', {
      comment: JSON.stringify(commentData),
      filesCount: files ? files.length : 0
    });

    return apiClient.post('/api/comments', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
  },

  // 更新評論
  updateComment: (id, data) => apiClient.put(`/api/comments/${id}`, data),

  // 刪除評論
  deleteComment: (id) => apiClient.delete(`/api/comments/${id}`),

  // 按讚/取消按讚
  toggleLike: (commentId, userId) => apiClient.post(`/api/comments/${commentId}/like`, { userId }),

  // 查詢按讚狀態
  getLikeStatus: (commentId, userId) =>
    apiClient.get(`/api/comments/${commentId}/like-status`, {
      params: { userId }
    }),

  // 檢查用戶按讚狀態
  checkLikedByUser: (commentId, userId) =>
    apiClient.get(`/api/comments/${commentId}/liked-by/${userId}`),

  // 查詢按讚數量
  getLikeCount: (commentId) => apiClient.get(`/api/comments/${commentId}/like-count`),

  // 批量查詢按讚狀態（後端已實作）
  getBatchLikeStatus: (commentIds, userId) => {
    if (!commentIds || commentIds.length === 0) {
      return Promise.reject(new Error('留言ID列表不能為空'));
    }
    
    console.log('🔄 批量查詢按讚狀態:', { commentIds, userId });
    
    return apiClient.post('/api/comments/batch-like-status', {
      commentIds,
      userId
    });
  },

  // 批量操作評論（新增、更新、刪除）
  batchUpdateComments: (operations) =>
    apiClient.post('/api/comments/batch', { operations }),

  // 取得評論統計資訊
  getCommentStats: (targetType, targetId) =>
    apiClient.get(`/api/comments/stats/${targetType}/${targetId}`),

  // 檢舉評論
  reportComment: (commentId, reasonId) =>
    apiClient.post(`/api/comments/${commentId}/report`, { reasonId }),

  // 取得單筆留言詳情
  getComment: (commentId) => apiClient.get(`/api/comments/${commentId}`),

  // 取得留言檢舉狀態（建議後端實作）
  getCommentReportStatus: (commentId) => 
    apiClient.get(`/api/comments/${commentId}/report-status`),

  // 更新評論並上傳圖片（新版API）
  updateCommentWithFiles: (commentId, commentData, files = []) => {
    const formData = new FormData();

    // 留言資料 - 使用 'comment' 作為 key，包含完整的 comment 物件
    formData.append('comment', JSON.stringify(commentData));

    // 圖片檔案 - 使用 'files' 作為 key
    if (files && files.length > 0) {
      files.forEach((file, index) => {
        formData.append('files', file);
        console.log(`📁 添加檔案 ${index + 1}:`, {
          name: file.name,
          size: file.size,
          type: file.type
        });
      });
    } else {
      // 如果沒有新檔案，添加一個空的 files 欄位，確保後端知道這是圖片更新請求
      formData.append('files', '');
      console.log('📁 沒有新檔案，添加空的 files 欄位');
    }

    // 調試：檢查 FormData 內容
    console.log('📋 FormData 內容檢查:');
    for (let [key, value] of formData.entries()) {
      if (value instanceof File) {
        console.log(`  ${key}: File(${value.name}, ${value.size} bytes, ${value.type})`);
      } else {
        console.log(`  ${key}:`, value);
      }
    }

    console.log('📤 發送更新 FormData 請求:', {
      comment: JSON.stringify(commentData),
      filesCount: files ? files.length : 0
    });

    return apiClient.put(`/api/comments/${commentId}`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
  }
};

// 圖片上傳相關 API
export const imageAPI = {
  // 上傳圖片檔案
  uploadImage: (formData) =>
    apiClient.post('/api/images/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    }),

  // 批量上傳圖片
  uploadImages: (formData) =>
    apiClient.post('/api/images/batch-upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    }),

  // 刪除圖片
  deleteImage: (imageId) => apiClient.delete(`/api/images/${imageId}`),

  // 取得圖片列表
  getImages: (targetType, targetId) => apiClient.get(`/api/images/${targetType}/${targetId}`),

  // 更新圖片排序
  updateImageOrder: (commentId, images) => {
    console.log('📤 發送圖片排序更新請求:', {
      commentId,
      imagesCount: images.length,
      images: images.map((img) => ({
        imageId: img.imageId,
        sortOrder: img.sortOrder,
        isActive: img.isActive,
        isDeleted: img.isDeleted
      }))
    });

    return apiClient.put(`/api/comments/${commentId}/images`, {
      images
    });
  },

  // 更新單張圖片
  updateImage: (imageId, data) => apiClient.put(`/api/images/${imageId}`, data)
};

// 用戶相關 API
export const userAPI = {
  // 取得用戶資訊
  getUserInfo: () => apiClient.get('/api/users/profile'),

  // 更新用戶資訊
  updateUserInfo: (data) => apiClient.put('/api/users/profile', data),

  // 登入
  login: (credentials) => apiClient.post('/api/auth/login', credentials),

  // 註冊
  register: (userData) => apiClient.post('/api/auth/register', userData),

  // 登出
  logout: () => apiClient.post('/api/auth/logout'),

  // 驗證token
  verifyToken: (token) => apiClient.post('/api/auth/verify', { token })
};

// 住宿相關 API
export const lodgingAPI = {
  // 取得住宿列表
  getLodgings: (params) => apiClient.get('/api/lodgings', { params }),

  // 取得住宿詳情
  getLodgingDetail: (id) => apiClient.get(`/api/lodgings/${id}`),

  // 取得房型詳情
  getRoomTypeDetail: (lodgingId, roomTypeId) =>
    apiClient.get(`/api/lodgings/${lodgingId}/roomType/${roomTypeId}`),

  // 取得住宿評論
  getLodgingComments: (id, params) => apiClient.get(`/api/lodgings/${id}/comments`, { params }),

  // 取得熱門住宿
  getHotLodgings: () => apiClient.get('/api/lodgings/hot'),

  // 搜尋住宿 - 使用 POST 發送 JSON 格式
  searchLodgings: (searchData) => apiClient.post('/api/lodgings/search', searchData)
};

// 交通相關 API
export const trafficAPI = {
  // 取得交通列表
  getTraffic: (params) => apiClient.get('/api/traffic', { params }),

  // 取得交通詳情
  getTrafficDetail: (id) => apiClient.get(`/api/traffic/${id}`),

  // 取得交通評論
  getTrafficComments: (id, params) => apiClient.get(`/api/traffic/${id}/comments`, { params })
};

// 票券相關 API
export const ticketAPI = {
  // 取得票券列表
  getTickets: (params) => apiClient.get('/api/tickets', { params }),

  // 取得票券詳情
  getTicketDetail: (id) => apiClient.get(`/api/tickets/${id}`),

  // 取得票券評論
  getTicketComments: (id, params) => apiClient.get(`/api/tickets/${id}/comments`, { params })
};

// 訂單相關 API
export const orderAPI = {
  // 取得訂單列表
  getOrders: (params) => apiClient.get('/api/orders', { params }),

  // 取得訂單詳情
  getOrderDetail: (id) => apiClient.get(`/api/orders/${id}`),

  // 建立訂單
  createOrder: (data) => apiClient.post('/api/orders', data),

  // 取消訂單
  cancelOrder: (id) => apiClient.put(`/api/orders/${id}/cancel`),

  // 支付訂單
  payOrder: (id, paymentData) => apiClient.post(`/api/orders/${id}/pay`, paymentData)
};

export default {
  commentAPI,
  userAPI,
  lodgingAPI,
  trafficAPI,
  ticketAPI,
  orderAPI
};
export { apiClient };
