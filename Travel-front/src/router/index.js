import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue';
import Flights from '../views/Flights.vue';
import Hotels from '../views/lodging/Hotels.vue';
import Tickets from '../views/Tickets.vue';
import Orders from '../views/Orders.vue';
import Login from '../views/Login.vue';
import Register from '../views/Register.vue';
import SearchResults from '../views/SearchResults.vue';
import CustomerService from '../views/CustomerService.vue';
import HotelsFilter from '../views/lodging/HotelsFilter.vue';
import PaymentResult from '../views/PaymentResult.vue'; // 0624新增
import PaymentCancel from '../views/PaymentCancel.vue'; // 0624新增
import CustomerChatRoom from '@/views/CustomerChatRoom.vue'; // 0626新增
// import FlightCheckout from '../components/flights/FlightCheckout.vue';
import Consults from '../views/consults/Consults.vue'; // 0627新增
import PointStore from '../views/PointStore.vue'; // 0701新增
import VendorLogin from '../views/VendorLogin.vue';

const routes = [
  { path: '/', redirect: '/home' },
  { path: '/home', component: Home },
  { path: '/flights', component: Flights },
  { path: '/hotels', component: Hotels },
  { path: '/hotels/filter', name: 'HotelsFilter', component: HotelsFilter },
  {
    path: '/hotel/:id',
    name: 'HotelDetail',
    component: () => import('../views/lodging/HotelDetail.vue'),
    props: true
  },
  {
    path: '/hotel/:id/room/:roomTypeId',
    name: 'HotelDetailWithRoom',
    component: () => import('../views/lodging/HotelDetail.vue'),
    props: true
  },
  { path: '/tickets', component: Tickets },
  { path: '/ticket/:id', component: () => import('../views/TicketDetail.vue') },
  { path: '/traffic', component: () => import('../views/Traffic.vue') },
  { path: '/traffic/:id', component: () => import('../views/TrafficDetail.vue') },
  { path: '/my-journey', component: () => import('../views/MyJourney.vue') },
  { path: '/profile', component: () => import('../views/Profile.vue') },
  { path: '/planDetails/:id', component: () => import('../components/plans/PlanDetails.vue') },
  { path: '/hotelCheckout', component: () => import('../components/lodging/HotelCheckout.vue') },
  { path: '/orders', component: Orders },
  {
    path: '/login',
    component: Login,
    meta: {
      hideButtons: true
    }
  }, // 不顯示回到頂部和客服按鈕
  {
    path: '/register',
    component: Register,
    meta: {
      hideButtons: true
    }
  }, // 不顯示回到頂部和客服按鈕
  {
    path: '/admin',
    component: () => import('../views/Admin.vue'),
    meta: {
      hideButtons: true
    }
  }, // 不顯示回到頂部和客服按鈕
  {
    path: '/vendor-upload',
    component: () => import('../components/vendor/VendorUpload.vue')
  },
  { path: '/checkout', component: () => import('../views/Checkout.vue') },
  { path: '/search', component: SearchResults },
  { path: '/customer-service', component: CustomerService },
  // { path: '/flightCheckout', name: 'FlightCheckout', component: FlightCheckout },

  {
    path: '/payment-result',
    name: 'payment-result',
    component: PaymentResult,
    meta: {
      hideButtons: true
    }
  }, // 0624新增  // 不顯示回到頂部和客服按鈕
  {
    path: '/payment-cancel',
    name: 'payment-cancel',
    component: PaymentCancel,
    meta: {
      hideButtons: true
    }
  }, // 0624新增  // 不顯示回到頂部和客服按鈕
  {
    path: '/chatroom',
    name: 'CustomerChatRoom',
    component: CustomerChatRoom,
    meta: {
      hideButtons: true
    }
  }, // 0626新增 // 不顯示回到頂部和客服按鈕
  {
    path: '/consults',
    name: 'Consults',
    component: Consults
  }, // 0627新增
  {
    path: '/point-store',
    name: 'PointStore',
    component: PointStore
  },
  { path: '/vendor/login', component: VendorLogin },
  { path: '/vendor/flight', component: () => import('../views/vendor/FlightVendor.vue') },
  { path: '/vendor/hotel', component: () => import('../views/vendor/HotelVendor.vue') },
  { path: '/vendor/traffic', component: () => import('../views/vendor/TrafficVendor.vue') },
  { path: '/vendor/attraction', component: () => import('../views/vendor/AttractionVendor.vue') },
  { path: '/vendor-test', component: () => import('../views/VendorTest.vue') }
];

export default createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    // 始終滾動到頂部
    return { top: 0 };
  }
});
