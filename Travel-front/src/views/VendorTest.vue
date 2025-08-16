<template>
  <div class="container mt-5">
    <h2>商家登入狀態測試</h2>
    
    <div class="card">
      <div class="card-body">
        <h5>localStorage 內容：</h5>
        <pre>{{ localStorageData }}</pre>
        
        <h5>useCurrentUser 狀態：</h5>
        <pre>{{ currentUserData }}</pre>
        
        <h5>商家類型判斷：</h5>
        <ul>
          <li>isVendor: {{ isVendor }}</li>
          <li>isHotelVendor: {{ isHotelVendor }}</li>
          <li>isFlightVendor: {{ isFlightVendor }}</li>
          <li>isAttractionVendor: {{ isAttractionVendor }}</li>
          <li>isTrafficVendor: {{ isTrafficVendor }}</li>
        </ul>
        
        <h5>商家後台 URL：</h5>
        <p>{{ getVendorBackendUrl() }}</p>
        
        <div class="mt-3">
          <router-link :to="getVendorBackendUrl()" class="btn btn-primary">
            前往商家後台
          </router-link>
          <router-link to="/" class="btn btn-secondary ms-2">
            返回首頁
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useCurrentUser } from '@/composables/useCurrentUser';

const {
  currentUser,
  isVendor,
  isHotelVendor,
  isFlightVendor,
  isAttractionVendor,
  isTrafficVendor
} = useCurrentUser();

const localStorageData = computed(() => {
  return {
    userType: localStorage.getItem('userType'),
    userId: localStorage.getItem('userId'),
    userName: localStorage.getItem('userName'),
    vendorType: localStorage.getItem('vendorType'),
    vendorId: localStorage.getItem('vendorId'),
    isVendorLoggedIn: localStorage.getItem('isVendorLoggedIn'),
    vendorName: localStorage.getItem('vendorName'),
    token: localStorage.getItem('token') ? '存在' : '不存在'
  };
});

const currentUserData = computed(() => {
  return {
    userId: currentUser.value.userId,
    userType: currentUser.value.userType,
    userName: currentUser.value.userName,
    vendorType: currentUser.value.vendorType,
    vendorId: currentUser.value.vendorId,
    isVendorLoggedIn: currentUser.value.isVendorLoggedIn,
    vendorName: currentUser.value.vendorName
  };
});

const getVendorBackendUrl = () => {
  if (isHotelVendor.value) return '/vendor/hotel';
  if (isFlightVendor.value) return '/vendor/flight';
  if (isAttractionVendor.value) return '/vendor/attraction';
  if (isTrafficVendor.value) return '/vendor/traffic';
  
  // 預設返回飯店後台
  return '/vendor/hotel';
};
</script> 