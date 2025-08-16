import { ref, onMounted } from 'vue'
import { lodgingAPI } from '@/services/api'

/** 後端「熱門旅宿」回傳 DTO */
export interface LodgingHotSearchResponseDTO {
    lodgingId: number
    lodgingName: string
    cityName: string
    avgRating: number
    reviewCount: number
    imageUrl: string
}

/** 後端「搜尋結果」回傳 DTO */
export interface LodgingSearchResponseDTO {
    lodgingId: number
    lodgingName: string
    avgRating: number
    reviewCount: number
    roomTypeId: number
    roomTypeName: string
    roomTypeDescription: string
    maxGuests: number
    pricePerNight: number
    imageUrl: string
    lodgingTypeId: number
    lodgingType: string
    bedTypes: Array<{
        id: number
        name: string
    }>
    facilities: Array<{
        id: number
        name: string
    }>
}

/** 前端使用的旅宿資料格式 */
export interface LodgingDisplayDTO {
    id: number
    name: string
    pricePerNight?: number
    coverImageUrl: string
    location?: string
    rating?: number
    reviewCount?: number
    roomTypeId?: number
    roomTypeName?: string
    roomTypeDescription?: string
    maxGuests?: number
    lodgingType?: string
    bedTypes?: Array<{ id: number; name: string }>
    facilities?: Array<{ id: number; name: string }>
}

/** 取得熱門旅宿的 composable */
export function useHotLodgings() {
    const hotels = ref<LodgingDisplayDTO[]>([])
    const loading = ref(false)
    const error = ref<Error | null>(null)
    const filterOptions = ref({
        bedTypes: [],
        facilities: [],
        lodgingsTypes: []
    })

    /** 轉換後端資料為前端格式 */
    const transformLodgingData = (data: LodgingHotSearchResponseDTO[]): LodgingDisplayDTO[] => {
        return data.map(item => ({
            id: item.lodgingId,
            name: item.lodgingName,
            coverImageUrl: item.imageUrl,
            rating: item.avgRating,
            reviewCount: item.reviewCount,
            location: item.cityName,
            pricePerNight: undefined // 後端目前沒有提供價格資訊
        }))
    }

    const transformSearchData = (data: LodgingSearchResponseDTO[]): LodgingDisplayDTO[] => {
        return data.map(item => ({
            id: item.lodgingId,
            name: item.lodgingName,
            coverImageUrl: item.imageUrl,
            rating: item.avgRating,
            reviewCount: item.reviewCount,
            pricePerNight: item.pricePerNight,
            roomTypeId: item.roomTypeId,
            roomTypeName: item.roomTypeName,
            roomTypeDescription: item.roomTypeDescription,
            maxGuests: item.maxGuests,
            lodgingType: item.lodgingType,
            lodgingTypeId: item.lodgingTypeId,
            bedTypes: item.bedTypes,
            facilities: item.facilities
        }))
    }

    /** 重新抓取 */
    const fetchHot = async () => {
        loading.value = true
        error.value = null
        try {
            const { data } = await lodgingAPI.getHotLodgings()
            hotels.value = transformLodgingData(data)
            console.log('fetchHot 後 hotels.value.length:', hotels.value.length)
        } catch (err) {
            error.value = err as Error
        } finally {
            loading.value = false
        }
    }

    const searchLodgings = async (searchParams: any) => {
        loading.value = true
        error.value = null
        try {
            const backendSearchData = {
                cityName: searchParams.destination,
                checkInDate: searchParams.checkIn,
                checkOutDate: searchParams.checkOut,
                guestCount: parseInt(searchParams.guests) || 2
            }
            console.log('發送到後端的搜尋資料：', backendSearchData)
            const { data } = await lodgingAPI.searchLodgings(backendSearchData)
            console.log('搜尋 API 原始回應:', data)

            const rawResults = data.results || data
            console.log('API 回傳 results 長度:', rawResults.length)
            console.log('API 回傳 results 內容:', rawResults)

            const transformed = transformSearchData(rawResults)
            console.log('transformSearchData 後長度:', transformed.length)
            console.log('transformSearchData 後內容:', transformed)

            hotels.value = transformed
            filterOptions.value = data.filterOptions || {
                bedTypes: [],
                facilities: [],
                lodgingsTypes: []
            }
        } catch (err) {
            console.error('搜尋 API 調用失敗:', err)
            error.value = err as Error
        } finally {
            loading.value = false
        }
    }

    // 首次掛載自動呼叫
    onMounted(fetchHot)

    return {
        hotels,
        loading,
        error,
        refresh: fetchHot,
        searchLodgings,
        filterOptions
    }
}