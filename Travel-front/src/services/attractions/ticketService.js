import {apiClient} from '../api'

// 🔍 查詢所有票券（可搭配搜尋條件）
export const searchTickets = (searchDto = {}) => {
  return apiClient.post('/api/attractions/tickets/search', searchDto)
}

// 📊 計算票券總數（分頁用）
export const countTickets = (searchDto = {}) => {
  return apiClient.post('/api/attractions/tickets/count', searchDto)
}

// 🔎 查詢單一票券（主表 + 子表）
export const getTicketById = (id) => {
  return apiClient.get(`/api/attractions/tickets/${id}`)
}
// ➕ 新增票券（含圖片、內文、套票與票種）
export const createFullTicket = (ticketData) => {
  return apiClient.post('/api/attractions/tickets/full', ticketData)
}

// ➕ 新增主表（不含子表）
export const createTicket = (ticketData) => {
  return apiClient.post('/api/attractions/tickets/createTicket', ticketData)
}

// ✏️ 修改票券主表
export const updateTicket = (id, ticketData) => {
  return apiClient.put(`/api/attractions/tickets/${id}`, ticketData)
}

// ❌ 刪除票券
export const deleteTicket = (id) => {
  return apiClient.delete(`/api/attractions/tickets/${id}`)
}
// 🔍 查詢簡易票券（主表 + 最低票價）
export const searchSimpleTickets = (searchDto = {}) => {
  return apiClient.post('/api/attractions/tickets/simple', searchDto)
}
// ✏️ 修改指定票券內文（根據 contentId）
export const updateTicketContent = (contentId, dto) => {
  return apiClient.put(`/api/tickets/contents/${contentId}`, dto)
  // ➡️ 對應後端 PUT /api/tickets/contents/{id}
  // ⬆️ 例如：/api/tickets/contents/2004，內容為 { title: '新標題', description: '新內容' }
}

// ➕ 新增一筆票券內文（需附帶 ticketId）
export const addTicketContent = (dto) => {
  return apiClient.post(`/api/tickets/contents`, dto)
  // ➡️ 對應後端 POST /api/tickets/contents
  // ⬆️ 傳入格式為 { ticketId: 2006, title: '標題', description: '內容' }
}

// ❌ 根據內文 ID 刪除一筆票券內文
export const deleteTicketContent = (contentId) => {
  console.log('🚀 發送刪除 API：', `/api/tickets/contents/${contentId}`)
  return apiClient.delete(`/api/tickets/contents/${contentId}`)
}
  // ➡️ 對應後端 DELETE /api/tickets/contents/{id}
  // ⬆️ 例如：/api/tickets/contents/2005


// 🔍 查詢某票券（主票）底下的所有內文列表
export const getTicketContent = (ticketId) => {
  return apiClient.get(`/api/tickets/contents/ticket/${ticketId}`)
  // ➡️ 對應後端 GET /api/tickets/contents/ticket/{ticketId}
  // ⬆️ 例如：/api/tickets/contents/ticket/2006
  
}
// ✅ 查詢所有票券標籤（for 下拉選單用）
export const getAttractionTagsOptions = () => {
  return apiClient.get('/api/options/attraction-tags')}
  // ⬆️ 對應 Controller: @GetMapping("/attraction-tags")

  // ✅ 查詢所有景點類型（for 下拉選單用）
export const getAttractionTypesOptions = () => {
  return apiClient.get('/api/options/attraction-types')
}
// ⬆️ 對應 Controller: @GetMapping("/attraction-types")


// ✅ 查詢所有國家（for 下拉選單用）
export const getCountryOptions = () => {
  return apiClient.get('/api/options/countries')
}
// ⬆️ 對應 Controller: @GetMapping("/countries")

// ✅ 查詢城市清單（可選擇國家篩選）
export const getCityOptions = (countryId = null) => {
  const url = countryId
    ? `/api/options/cities?countryId=${countryId}`
    : `/api/options/cities`
  return apiClient.get(url)
}
// ⬆️ 對應 Controller: @GetMapping("/cities") with @RequestParam(required = false) Integer countryId



  // 🔍 查詢簡易票券資訊（主表 + 最低票價）
// ➡️ 對應後端 POST /api/attractions/tickets/simple
// ⬆️ 例如：searchSimpleTickets({ keyword: '博物館', countryId: 1 })
export const searchAttractionTickets = (searchDto = {}) => {
  return apiClient.post('/api/attractions/tickets/simple', searchDto)
}

/**
 * 🧾 查詢主表資訊用於商品內頁展示（不含圖片、FAQ、套票等子資料）
 * ➡️ 對應後端 POST /api/attractions/main-search
 * ⬆️ 傳入格式：{ keyword, countryId, cityId, typeIds, tagIds }
 */
export const getMainTicketById = (id) => {
  return apiClient.get(`/api/attractions/tickets/main/${id}`)
}


// 查詢景點票券底下(套票+票種)
export const getPackagesByTicketId=(id) =>{
  return apiClient.get(`/api/tickets/packages/ticket/${id}`)
}

//查詢景點票券底下圖片
export const getImagesByTicketId=(id)=>{
  return apiClient.get(`/api/tickets/images/ticket/${id}`)
}

// 🔍 查詢完整訂單（含主表與明細）
export const getFullOrderByOrderNo = (orderNo) => {
  return apiClient.get(`/api/attractions/order/full/${orderNo}`)
  // ⬆️ 對應後端 Controller:
  // @GetMapping("/full/{orderNo}")
}

// ➕ 儲存訂單主表與明細
export const saveOrderWithItems = (orderDto) => {
  return apiClient.post('/api/attractions/order/save', orderDto)
  // ⬆️ 對應 @PostMapping("/save")
}

// 🔍 根據 tripId 查詢未結帳訂單（主表＋明細）
export const getOrdersByTripId = (tripId) => {
  return apiClient.get(`/api/attractions/order/full/by-trip/${tripId}`)
  // ⬆️ 對應後端 @GetMapping("/full/by-trip/{tripId}")
}
//根據ORDEWRID 刪除訂單
export const deleteOrder=(orderId)=>{
  return apiClient.delete(`/api/attractions/order/${orderId}`)
// ⬆️ 對應後端 @DeleteMapping("/{orderId}")
}
