<template>
  <div class="flight-list">
    <div class="card mb-2" :class="{ 'hovered': isHovered }" @mouseenter="handleMouseEnter"
      @mouseleave="handleMouseLeave">
      <div class="card-body">
        <div class="row align-items-center">
          <div class="col-md-3 text-center">
            <img :src="flightDetail.imageURL" :alt="flightDetail.airline" class="airline-logo mb-2" />
            <div class="airline-name">{{ flightDetail.airline }}</div>
            <div class="flight-number">{{ flightDetail.flightNumber }}</div>
          </div>
          <div class="col-md-6">
            <div class="flight-info d-flex justify-content-between align-items-center">
              <div class="departure ">
                <h5 class="mb-0">{{ formatTime(flightDetail.departureTime) }}</h5>
                <div class="city">{{ flightDetail.departureNameZh }}</div>
                <div class="airport">{{ flightDetail.departureAirport }}</div>
                <div class="terminal small text-muted">{{ flightDetail.departureTerminal }}</div>
              </div>
              <div class="duration text-center">
                <div class="time">{{ formatDuration(flightDetail.duration) }}</div>
                <div class="flight-line position-relative">
                  <i class="bi bi-airplane-fill"></i>
                </div>
                <div class="small text-muted">{{ flightDetail.flightclass }}</div>
                <div class="small text-muted">行李限重：{{ flightDetail.baggageAllowance }}kg</div>
              </div>
              <div class="arrival text-end">
                <h5 class="mb-0">{{ formatTime(flightDetail.arrivalTime) }}</h5>
                <div class="city">{{ flightDetail.arrivalNameZh }}</div>
                <div class="airport">{{ flightDetail.arrivalAirport }}</div>
                <div class="terminal small text-muted">{{ flightDetail.arrivalTerminal }}</div>
              </div>
            </div>
          </div>
          <div class="col-md-3 text-end">
            <div class="price mb-2">
              <span class="fs-4 fw-bold text-primary">NT$ {{ flightDetail.classPrice.toLocaleString() }}</span>
              <small class="text-muted">/人</small>
            </div>
            <button class="btn" :class="buttonClass" :disabled="isSelected" @click="bookFlight">
              {{ buttonLabel }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useRouter } from 'vue-router';

import { ref } from 'vue';



const router = useRouter();
const props = defineProps({
  flightDetail: Object,
  tripType: String,
  selectedRoundFlights: Array,
  selectedMultiFlights: Array,
});
const emit = defineEmits(['round-select']);


const formatDuration = (minutes) => {
  const hours = Math.floor(minutes / 60);
  const mins = minutes % 60;
  return `${hours}時${mins}分`;
};



const bookFlight = () => {
  emit('flight-select', props.flightDetail);
};





const isSelected = computed(() => {
  const id = `${props.flightDetail.flightNumber}-${props.flightDetail.flightclass}`;
  if (props.tripType === 'round') {
    return props.selectedRoundFlights?.some(f => f && `${f.flightNumber}-${f.flightclass}` === id);
  } else if (props.tripType === 'multi') {
    return props.selectedMultiFlights?.some(f => f && `${f.flightNumber}-${f.flightclass}` === id);
  }
  return false;
});


const buttonLabel = computed(() => {
  return isSelected.value ? '✅ 已選擇' : '選擇航班';
});

const buttonClass = computed(() => {
  return isSelected.value ? 'btn-primary' : 'btn-outline-primary';
});

// 格式化時間顯示
const formatTime = (datetimeStr) => {
  if (!datetimeStr) return '';
  const date = new Date(datetimeStr);
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  return `${month}/${day} ${hours}:${minutes}`;
};

// Hover effect for flight card
const hoveredId = ref(null);
const handleMouseEnter = () => {
  hoveredId.value = `${props.flightDetail.flightNumber}-${props.flightDetail.flightclass}`;
};
const handleMouseLeave = () => {
  hoveredId.value = null;
};
const isHovered = computed(() => {
  const id = `${props.flightDetail.flightNumber}-${props.flightDetail.flightclass}`;
  return hoveredId.value === id;
});


</script>

<style scoped>
.search-section {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.airline-logo {
  height: 40px;
  width: auto;
  object-fit: contain;
}

.flight-line {
  height: 2px;
  background: #dee2e6;
  width: 100px;
  margin: 8px auto;
}

.flight-line i {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: #0d6efd;
  background: white;
  padding: 0 8px;
}

.airport {
  color: #6c757d;
  font-size: 0.9rem;
}

.container {
  overflow: visible !important;
  position: static !important;
}

.card {
  transition: all 0.3s ease;
}

.hovered {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
}

@media (max-width: 768px) {
  .flight-info {
    flex-direction: column;
    text-align: center;
    gap: 1rem;
  }
}
</style>