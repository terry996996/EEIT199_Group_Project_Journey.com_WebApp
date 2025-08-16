<template>
  <div class="container mt-5 pt-5">
    <div class="row justify-content-center">
      <div class="col-md-6">
        <div class="card">
          <div class="card-body">
            <h2 class="text-center mb-4">註冊帳號</h2>
            <form @submit.prevent="editProfile" @input="test">
              <!-- 帳號 -->
              <div class="mb-3">
                <label for="name" class="form-label">帳號
                  <span class="err" v-if="!isNameValid && !isInitName">帳號只允許英文字母、數字或底線，至少6字元，不能是純數字！</span>
                  <span class="ok" v-if="!isSameName && isNameValid && !isChecking">{{ msgName }}</span>
                  <span class="err" v-if="isSameName && isNameValid && !isChecking">{{ msgName }}</span>
                </label>
                <input type="text" class="form-control" id="name" v-model="form.username" required />
              </div>

              <!-- 電子郵件 -->
              <div class="mb-3">
                <label for="email" class="form-label">電子郵件
                  <span class="err" v-if="!isEmailValid && !isInitEmail">電子郵件格式有誤！</span>
                  <span class="ok" v-if="!isSameEmail && isEmailValid">{{ msgEmail }}</span>
                  <span class="err" v-if="isSameEmail && isEmailValid">{{ msgEmail }}</span>
                </label>
                <input type="email" class="form-control" id="email" v-model="form.email" required />
              </div>

              <!-- 密碼 -->
              <div class="mb-3">
                <label for="password" class="form-label">密碼
                  <span class="err" v-if="!isPasswordValid && !isInitPwd">密碼長度至少 8 個字元，需包含英文字母和數字、不得有非法字元！</span>
                </label>
                <input type="password" class="form-control" id="password" v-model="form.passwordString" required />
                <div class="form-text">
                  密碼長度至少 8 個字元，需包含英文字母和數字
                </div>
              </div>

              <!-- 確認密碼 -->
              <div class="mb-3">
                <label for="confirmPassword" class="form-label">確認密碼
                  <span class="err" v-if="!isMatched">密碼不一致！</span>
                </label>
                <input type="password" class="form-control" id="confirmPassword" v-model="confirmPassword" required />
              </div>

              <!-- 同意條款 -->
              <div class="mb-3 form-check">
                <input type="checkbox" class="form-check-input" id="agreeTerms" v-model="agreeTerms" required />
                <label class="form-check-label" for="agreeTerms">
                  我已閱讀並同意
                  <a href="#" @click.prevent="showTerms">服務條款</a>
                  和
                  <a href="#" @click.prevent="showPrivacy">隱私權政策</a>
                </label>
              </div>

              <!-- 註冊按鈕 -->
              <button type="submit" class="btn btn-primary w-100 mb-3" :disabled="!isValid">
                下一步
              </button>

              <!-- 登入連結 -->
              <div class="text-center">
                已經有帳號？
                <router-link to="/login">立即登入</router-link>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div ref="modal" class="modal fade" id="myFormModal" tabindex="-1" v-show="showNewForm">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">註冊使用者基本資料</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <ProfileForm v-if="isInit" :is-new-user="true" v-model:userInfo="userInfo" @newUser="handleRegister">
          </ProfileForm>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { Modal } from 'bootstrap/dist/js/bootstrap.bundle.min';
  import axios from 'axios';
  import Swal from 'sweetalert2';
  import { computed, nextTick, onBeforeUnmount, onMounted, reactive, ref, watch } from 'vue';
  import { useRouter } from 'vue-router';
  import ProfileForm from '@/components/userprofiles/ProfileForm.vue';

  const isInit = ref(false);
  const router = useRouter();
  const api = 'http://localhost:8080/api';
  const userInfo = ref(null);

  const form = reactive({
    username: '',
    passwordString: '',
    email: '',
    userType: 1,
  });

  const confirmPassword = ref('');
  const agreeTerms = ref(false);
  const isInitName = ref(true);
  const isInitPwd = ref(true);
  const isInitEmail = ref(true);
  const msgName = ref('');
  const msgEmail = ref('');

  // 帳號只允許英數+底線，至少6字元，不能是純數字
  const isNameValid = computed(() => {
    const regex = /^(?!\d+$)[A-Za-z0-9_]{6,}$/;
    return regex.test(form.username);
  });

  // 密碼長度至少8個字元，需包含英文字母和數字，不允許任何引號
  const isPasswordValid = computed(() => {
    const regex = /^(?=.*[A-Za-z])(?=.*\d)[^'"`]{8,}$/;
    return regex.test(form.passwordString);
  })


  const isEmailValid = computed(() => {
    const regex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
    return regex.test(form.email);
  });

  const isSameName = ref(false);
  const isSameEmail = ref(false);

  const isConfirmValid = computed(() => confirmPassword.value.trim().length > 0);
  const isMatched = computed(() => confirmPassword.value === form.passwordString);

  const isValid = computed(() => {
    return (
      isNameValid.value &&
      isPasswordValid.value &&
      !isSameEmail.value &&
      !isSameName.value &&
      isNameValid.value &&
      isEmailValid.value &&
      isPasswordValid.value &&
      isConfirmValid.value &&
      isMatched.value &&
      agreeTerms.value
    )
  });

  const isChecking = ref(false);
  watch(() => form.username, (newVal) => {
    isInitName.value = false;

    if (isNameValid.value) {
      axios.post(api + '/registrate/checkUserName', newVal, {
        headers: {
          'Content-Type': 'text/plain',
        }

      })
        .then(response => {
          isSameName.value = false;
          msgName.value = `${response.data} ✔️`;
        })
        .catch(error => {
          isSameName.value = true;
          msgName.value = `${error.response.data} ❌`;
        })
    }
  });

  watch(() => form.passwordString, () => {
    isInitPwd.value = false;
  });

  watch(() => form.email, (newVal) => {
    isInitEmail.value = false;

    if (isEmailValid.value) {
      axios.post(api + '/registrate/checkEmail', newVal, {
        headers: {
          'Content-Type': 'text/plain',
        }
      })
        .then(response => {
          isSameEmail.value = false;
          msgEmail.value = `${response.data} ✔️`;
        })
        .catch(error => {
          isSameEmail.value = true;
          msgEmail.value = `${error.response.data} ❌`;
        })
    }
  });

  const handleRegister = () => {
    modalInstance?.hide();
    close();
    const dto = {
      form: form,
      profile: userInfo.value,
    };

    axios.post(api + '/registrate/submit', dto)
      .then(response => {
        const headers = response.headers['authorization'];
        if (headers && headers.startsWith('Bearer ')) {
          const token = headers.substring(7);
          localStorage.setItem('token', token);
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
            router.push("/");
          } else if (result.dismiss === Swal.DismissReason.timer) {
            router.push("/");
          }
        });
      })
      .catch(error => {
        Swal.fire({
          icon: 'error',
          title: error.response.data,
          showConfirmButton: true,
          allowEscapeKey: true,
          allowOutsideClick: true,
        });
      })
  }


  const getNewForm = () => {
    return {
      email: form.email,
      givenName: '',
      familyName: '',
      givenNameLatin: '',
      familyNameLatin: '',
      nationality: '',
      passportNo: '',
      passportExpire: '',
      birthday: '',
      residence: '',
      address: '',
      phoneCode: '',
      telNumber: '',
      gender: null,
    };
  }

  const showNewForm = ref(false);
  const editProfile = () => {
    userInfo.value = getNewForm();
    isInit.value = true;
    showNewForm.value = true;
    console.log(showNewForm.value);
  }

  const modal = ref(null);
  let modalInstance = null;

  onMounted(() => {
    // modalInstance = new Modal(modal.value, {
    //   backdrop: 'static',
    //   keyboard: false,
    // });
    // modal.value.addEventListener('hide.bs.modal', close);
    nextTick(() => {
      if (modal.value) {
        modalInstance = new Modal(modal.value, {
          backdrop: 'static',
          keyboard: false,
        });
        modal.value.addEventListener('hide.bs.modal', close);
      }
    });
  });

  onBeforeUnmount(() => {
    modalInstance?.dispose();
    modal.value.removeEventListener('hide.bs.modal', close);
  });

  watch(showNewForm, (newVal) => {
    if (newVal) {
      modalInstance?.show();
    } else {
      modalInstance?.hide();
      afterClose();
    }
  });

  const close = () => {
    showNewForm.value = false;
    afterClose();
  };

  // deal with aria-label warnings
  const afterClose = () => {
    if (modal.value && modal.value.contains(document.activeElement)) {
      if (document.activeElement instanceof HTMLElement) {
        document.activeElement.blur();
      }
    }
  }
</script>

<style scoped>
  .card {
    border: none;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  }

  .form-control:focus {
    border-color: #0080ff;
    box-shadow: 0 0 0 0.2rem rgba(0, 128, 255, 0.25);
  }

  .btn-primary {
    background-color: #0080ff;
    border-color: #0080ff;
  }

  .btn-primary:hover {
    background-color: #005bbd;
    border-color: #005bbd;
  }

  a {
    color: #0080ff;
    text-decoration: none;
  }

  a:hover {
    text-decoration: underline;
  }

  .err {
    font-weight: bold;
    font-size: 12px;
    color: red;
    margin-left: 10px;
  }

  .ok {
    font-weight: bold;
    font-size: 12px;
    color: rgb(6, 150, 6);
    margin-left: 10px;
  }

  .check {
    font-weight: bold;
    font-size: 12px;
    margin-left: 10px;
  }
</style>