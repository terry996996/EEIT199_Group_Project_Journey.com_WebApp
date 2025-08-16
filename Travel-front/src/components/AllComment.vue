<template>
    <div class="reviews">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h4>住客評價</h4>
            <button v-if="!showReviewForm" class="btn btn-outline-primary" @click="openForm">
                撰寫評價
            </button>
        </div>

        <!-- 評論表單 -->
        <div class="card mb-4" v-show="showReviewForm" style="z-index: 99;">
            <div class="card-body">
                <h5 class="card-title mb-3">撰寫評價</h5>
                <div>
                    <div class="mb-3">
                        <label class="form-label">評分</label>
                        <div class="rating-input">
                            <i v-for="n in 5" :key="n" class="bi" :class="starIcon(n)" @click="setRating(n, $event)"
                                style="cursor:pointer;color:#ffc107;font-size:1.6rem;"></i>
                            <span class="ms-2">{{ rating }}/5</span>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">評價內容</label>
                        <textarea class="form-control" v-model="newReview.content" rows="3"
                            placeholder="請輸入你的心得..."></textarea>
                    </div>

                    <div class="text-end">
                        <button type="button" class="btn btn-outline-secondary me-2" @click="showReviewForm = false">
                            取消
                        </button>
                        <button class="btn btn-primary" @click="submitReview">提交評價</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- 評論列表 -->
        <div class="review-list">
            <div class="card mb-3" v-for="review in hotel.reviews" :key="review.id">
                <div class="card-body">
                    <div class="d-flex justify-content-between mb-2">
                        <div class="d-flex align-items-center">
                            <img :src="review.userAvatar" class="rounded-circle me-2" width="40" height="40"
                                :alt="review.userName" />
                            <div>
                                <h6 class="mb-0">{{ review.userName }}</h6>
                                <small class="text-muted">{{ review.date }}</small>
                            </div>
                        </div>
                        <div class="rating">
                            <span class="badge bg-success">{{ review.rating }}</span>
                        </div>
                    </div>
                    <p class="card-text">{{ review.content }}</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { reactive, computed, ref, watch } from 'vue'
console.log("AllComment.vue loaded")

// 假資料：目前旅館下已有的留言，實務上應從後端取得
const hotel = ref({
    reviews: [
        {
            id: 1,
            userName: "王小明",
            userAvatar: "https://i.pravatar.cc/150?img=1",
            rating: 4.0,
            date: "2024-03-15",
            content: "服務非常好，房間乾淨舒適，地理位置優越，值得推薦！",
        },
        {
            id: 2,
            userName: "李小華",
            userAvatar: "https://i.pravatar.cc/150?img=2",
            rating: 4.0,
            date: "2024-03-10",
            content: "早餐種類豐富，健身房設備完善，唯一缺點是check-in時間稍久。",
        },
        {
            id: 3,
            userName: "張小芳",
            userAvatar: "https://i.pravatar.cc/150?img=3",
            rating: 4.8,
            date: "2024-03-05",
            content: "無可挑剔的住宿體驗，從入住到退房都非常完美，一定會再來！",
        },

    ],
});

// 模擬目前登入中的使用者 ID，實務中會從登入狀態或 token 中取得
const user_id = 123

// 接收父元件傳入的留言對象資訊（例如要留言給哪間旅館或哪個房型）
const props = defineProps<{
    target: {
        lodging_id?: number
        room_type_id?: number
        attraction_ticket_id?: number
        traffic_ticket_id?: number
        trip_plan_id?: number
    }
}>()

console.log("props.target = ", props.target)

// 使用 reactive 建立新留言的資料物件，包括評分與文字內容
const newReview = reactive({
    rating: 5,
    content: ''
})

// 使用 computed 建立評分的 getter/setter，可以與 UI 綁定星星點擊
const rating = computed({
    get: () => newReview.rating,
    set: v => (newReview.rating = v)
})

// 控制是否顯示「撰寫評價表單」
const showReviewForm = ref(false)

/**
 *   星星圖示邏輯：
 * - 如果目前評分 >= n，顯示實心星星
 * - 如果評分 >= n - 0.5，顯示半星
 * - 否則顯示空心星星
 */
function starIcon(n: number) {
    if (rating.value >= n) return 'bi-star-fill'
    if (rating.value >= n - 0.5) return 'bi-star-half'
    return 'bi-star'
}

/**
 *   當使用者點擊星星時：
 * - 透過 e.clientX 判斷是否點在左半邊，來決定是整星還是半星
 */
function setRating(n: number, e: MouseEvent) {
    const rect = (e.currentTarget as HTMLElement).getBoundingClientRect()
    const half = e.clientX - rect.left < rect.width / 2
    rating.value = half ? n - 0.5 : n
}

/**
 *  開啟評價表單時，初始化內容與預設評分
 */
function openForm() {
    console.log("撰寫評價表單開啟")
    newReview.rating = 5
    newReview.content = ''
    showReviewForm.value = true
}

/**
 *   送出評價時：
 * - 驗證內容是否有填寫
 * - 組合 JSON 格式 payload（符合後端規格）
 * - 在 console.log 顯示送出的格式
 * - Demo 模擬新增一筆到列表中
 */
function submitReview() {
    if (!newReview.content.trim()) {
        alert('請輸入評價內容')
        return
    }
    if (newReview.rating === 0) {
        alert('請選擇評分')
        return
    }
    //  組成符合後端接收格式的 JSON 物件
    const payload = {
        user_id,
        target: {
            lodging_id: props.target.lodging_id ?? null,
            room_type_id: props.target.room_type_id ?? null,
            attraction_ticket_id: props.target.attraction_ticket_id ?? null,
            traffic_ticket_id: props.target.traffic_ticket_id ?? null,
            trip_plan_id: props.target.trip_plan_id ?? null
        },
        content: newReview.content,
        rating: newReview.rating,
        parent_comment_id: null,
        images: [] // 可擴充支援圖片上傳
    }

    console.log('送出 JSON：', payload)
    // Demo 模擬立即新增到畫面上
    hotel.value.reviews.unshift({
        id: Date.now(),
        userName: '匿名旅客',
        userAvatar: 'https://i.pravatar.cc/150',
        rating: newReview.rating,
        date: new Date().toISOString().slice(0, 10),
        content: newReview.content
    })

    console.log("模擬新增一筆成功")

    // 清空並關閉表單
    showReviewForm.value = false
    newReview.content = ''
    newReview.rating = 5
}

</script>

<style>
.rating-input i {
    font-size: 1.5rem;
    margin-right: 5px;
}
</style>