<template>
  <div class="container my-5" style="max-width: 400px">
    <h2 class="mb-4 text-center">會員登入</h2>
    <div class="card p-4">
      <!-- 社交媒體登入按鈕 -->
      <div class="social-login mb-4">
        <button @click="handleGoogleLogin" class="btn btn-outline-danger w-100 mb-2">
          <i class="bi bi-google me-2"></i>使用 Google 帳號登入
        </button>
        <button @click="handleFacebookLogin" class="btn btn-outline-primary w-100 mb-2">
          <i class="bi bi-facebook me-2"></i>使用 Facebook 帳號登入
        </button>
        <button @click="handleLineLogin" class="btn btn-outline-success w-100">
          <i class="bi bi-line me-2"></i>使用 LINE 帳號登入
        </button>
      </div>

      <div class="divider">
        <span>或</span>
      </div>

      <!-- 一般登入表單 -->
      <form @submit.prevent="handleLogin" class="mt-4">
        <div class="mb-3">
          <label class="form-label fw-bold">帳號</label>
          <input type="text" class="form-control" v-model="form.username" required placeholder="請輸入帳號" />
        </div>
        <div class="mb-3">
          <label for="password" class="form-label">密碼</label>
          <div class="input-group">
            <input :type="showPassword ? 'text' : 'password'" v-model="form.passwordString" class="form-control"
              id="password" placeholder="請輸入密碼" required />
            <span class="input-group-text" @click="showPassword = !showPassword" style="cursor: pointer">
              <i :class="showPassword ? 'bi bi-eye' : 'bi bi-eye-slash'"></i>
            </span>
          </div>
        </div>
        <button type="submit" class="btn btn-primary w-100 mt-3">登入</button>
      </form>
      <div class="text-center mt-3 small text-muted">
        <router-link to="/forgot-password">忘記密碼？</router-link>
        <span class="mx-2">|</span>
        <router-link to="/register">註冊新帳號</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
  import axios from 'axios';
  import Swal from 'sweetalert2';
  import { ref } from 'vue';
  import { useRouter } from 'vue-router';
  import { jwtDecode } from 'jwt-decode';

  const router = useRouter();
  // const loading = ref(false)

  const form = ref({
    username: '',
    passwordString: '',
    rememberMe: false
  });

  const password = ref('');
  const showPassword = ref(false);

  const api = 'http://localhost:8080/api';
  const handleLogin = () => {
    axios
      .post(api + '/login/', form.value)
      .then((response) => {
        // 乃文用
        let toPath = '';
        const headers = response.headers['authorization'];
        if (headers && headers.startsWith('Bearer ')) {
          const token = headers.substring(7);
          localStorage.setItem('token', token);
          const payload = jwtDecode(token);

          // 儲存用戶資訊到 localStorage
          localStorage.setItem('userType', payload.userType);
          localStorage.setItem('userRole', payload.userType.toLowerCase()); // 同時設定 userRole
          localStorage.setItem('userId', payload.id || payload.userId);
          localStorage.setItem('userName', payload.username || payload.user || '用戶');

          console.log('登入成功，儲存用戶資訊:', {
            userType: payload.userType,
            userId: payload.id || payload.userId,
            userName: payload.username || payload.user
          });


          const userType = payload.userType;
          if (userType === 'ADMIN') {
            toPath = '/admin';
          } else {
            toPath = '/';
          }
        }
        let timerInterval;
        let countdown = 5;
        Swal.fire({
          icon: 'success',
          position: 'center',
          timer: countdown * 1000,
          title: response.data,
          html: `將在 <b>${countdown}</b> 秒後跳轉至首頁，或是點擊按鈕立即跳轉`,
          showConfirmButton: true,
          confirmButtonText: '確認',
          confirmButtonColor: '#47a8d1',
          allowEscapeKey: false,
          allowOutsideClick: false,

          didOpen: () => {
            const b = Swal.getHtmlContainer().querySelector('b');
            timerInterval = setInterval(() => {
              countdown--;
              b.textContent = countdown;
            }, 1000);
          },

          willClose: () => {
            clearInterval(timerInterval);
          }
        }).then((result) => {
          if (result.isConfirmed) {
            router.push(toPath);
          } else if (result.dismiss === Swal.DismissReason.timer) {
            router.push(toPath);
          }
        });
      })
      .catch(error => {
        Swal.fire({
          icon: 'error',
          title: error.response.data.error,
          showConfirmButton: true,
          allowEscapeKey: true,
          allowOutsideClick: true,
        });
      })
    // console.log(response);

    // loading.value = true

    // try {
    //   console.log('嘗試登入:', form.value.id, form.value.password)

    //   // 管理員帳號
    //   if (form.value.id === 'admin' && form.value.password === '1234') {
    //     localStorage.setItem('userRole', 'admin')
    //     localStorage.setItem('userName', '超級管理者')
    //     console.log('登入成功 - 超級管理者')
    //     router.push('/admin')
    //     return
    //   }
    //   if (form.value.id === 'admin2' && form.value.password === '1234') {
    //     localStorage.setItem('userRole', 'admin')
    //     localStorage.setItem('userName', '一般管理者')
    //     console.log('登入成功 - 一般管理者')
    //     router.push('/admin')
    //     return
    //   }
    //   if (form.value.id === 'admin3' && form.value.password === '1234') {
    //     localStorage.setItem('userRole', 'merchant')
    //     localStorage.setItem('userName', '社群小編')
    //     console.log('登入成功 - 社群小編')
    //     router.push('/admin')
    //     return
    //   }

    //   // 一般用戶帳號
    //   if (form.value.id === 'user' && form.value.password === '1234') {
    //     localStorage.setItem('userRole', 'user')
    //     localStorage.setItem('userName', '一般會員')
    //     console.log('登入成功 - 一般會員')
    //     router.push('/')
    //     return
    //   }

    //   // 如果都不符合
    //   console.log('登入失敗 - 帳號密碼錯誤')
    //   alert('帳號或密碼錯誤！')
    // } catch (error) {
    //   console.error('登入失敗:', error)
    //   alert('登入失敗，請檢查您的帳號密碼')
    // } finally {
    //   loading.value = false
    // }
  }

  const handleGoogleLogin = async () => {
    alert('Google 登入功能開發中...')
  }

  const handleFacebookLogin = async () => {
    alert('Facebook 登入功能開發中...')
  }

  const handleLineLogin = async () => {
    alert('LINE 登入功能開發中...')
  }
</script>

<style scoped>
  .container {
    max-width: 400px;
  }

  .card {
    border-radius: 12px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  }

  a {
    color: #0080ff;
    text-decoration: none;
  }

  a:hover {
    text-decoration: underline;
  }

  .social-login button {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 10px;
    border-radius: 8px;
    font-weight: 500;
  }

  .social-login button.btn-outline-primary {
    color: #1877f2;
    border-color: #1877f2;
  }

  .social-login button.btn-outline-primary:hover {
    background-color: #1877f2;
    color: white;
  }

  .divider {
    display: flex;
    align-items: center;
    text-align: center;
    color: #666;
    margin: 20px 0;
  }

  .divider::before,
  .divider::after {
    content: '';
    flex: 1;
    border-bottom: 1px solid #ddd;
  }

  .divider span {
    padding: 0 10px;
  }
</style>
