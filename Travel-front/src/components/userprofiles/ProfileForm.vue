<template>
  <div class="card prof-no-hover">
    <div class="card-body ">
      <form @submit.prevent="updateProfile">
        <div class="row g-3">
          <div class="col-md-6">
            <label class="form-label">姓氏<span style="color: red; margin-left: 2px;">*</span></label>
            <input type="text" class="form-control" v-model="profileForm.familyName" required />
          </div>
          <div class="col-md-6">
            <label class="form-label">名字<span style="color: red; margin-left: 2px;">*</span></label>
            <input type="text" class="form-control" v-model="profileForm.givenName" required />
          </div>
          <div>
            <div class="vertical-cent-align-label">
              <span>外文別名</span>
              <span style="font-weight: bold;font-size: 12px; color: #b12f56; margin-left: 5px;">
                姓名非拉丁字母者才需填寫
              </span>
            </div>
          </div>
          <div class="col-md-6" style="margin-top: 0px">
            <label class="form-label">姓氏 Family Name</label>
            <input type="text" class="form-control" v-model="profileForm.familyNameLatin" />
          </div>
          <div class="col-md-6" style="margin-top: 0px">
            <label class="form-label">名字 Given Name</label>
            <input type="text" class="form-control" v-model="profileForm.givenNameLatin" />
          </div>
          <div class="col-md-6">
            <label class="form-label">生日<span style="color: red; margin-left: 2px;">*</span></label>
            <input type="date" class="form-control" v-model="profileForm.birthday" :max="today.split('T')[0]"
              required />
          </div>
          <div class="col-md-6">
            <label class="form-label">國籍<span style="color: red; margin-left: 2px;">*</span></label>
            <select class="form-select" v-model="profileForm.nationality" required>
              <option v-for="nation in counrtyStore.countries" :key="nation.id" :value="nation.id">
                {{ nation.nameZh }}
              </option>
            </select>
          </div>
          <div class="col-md-6">
            <label class="form-label vertical-cent-align-label">護照號碼<span style="color: red; margin-left: 2px;">*</span>
            </label>
            <input type="text" class="form-control" v-model="profileForm.passportNo" />
          </div>
          <div class="col-md-6">
            <label class="form-label">護照有效日期<span style="color: red; margin-left: 2px;">*</span></label>
            <input type="month" class="form-control" v-model="profileForm.passportExpire" :min="today.slice(0, 7)" />
          </div>
          <div class="col-md-6">
            <label class="form-label vertical-cent-align-label">
              <span>電話<span style="color: red; margin-left: 2px;">*</span></span>
              <span style="font-weight: bold;font-size: 12px; color: #b12f56; margin-left: 5px;">
                依照居住地自動改變國碼
              </span>
              <span class="ms-auto" v-if="!isValidPhone"
                style="font-size: 12px; color: red; margin-left: 15px; align-self: flex-end;">
                ※ 不須填入開頭的 0
              </span>
            </label>
            <div class="input-group">
              <span class="input-group-text me-2" style="cursor: default;" title="自動帶入居住地國碼">+{{
                profileForm.phoneCode }}</span>
              <input type="tel" class="form-control" v-model="profileForm.telNumber" />
            </div>
          </div>
          <div class="col-md-2" v-if="!isUserForm">
            <label class="form-label">居住國家<span style="color: red; margin-left: 2px;">*</span></label>
            <select class="form-select" v-model="profileForm.residence">
              <option v-for="nation in counrtyStore.countries" :key="nation.id" :value="nation.id">
                {{ nation.nameZh }}
              </option>
            </select>
          </div>
          <div class="col-md-4">
            <div class="form-label">性別<span style="color: red; margin-left: 2px;">*</span></div>
            <div class="form-control d-flex">
              <div class="form-check form-check-inline flex-fill" v-for="gender in genders" :key="gender.id">
                <input class="form-check-input" type="radio" :id=gender.id :value="gender.code" name="gender"
                  v-model="profileForm.gender">
                <label class="form-check-label" :for="gender.id">{{ gender.id }}</label>
              </div>
            </div>
          </div>
          <div class="col-md-12" v-if="isUserForm || isNewUser">
            <label class="form-label email-label">
              <span>Email</span>
              <button type="button" class="btn btn-sm btn-change-email" @click="">變更電子信箱</button>
            </label>
            <input type="email" class="form-control bg-light profile-email" title="無法編輯" v-model="profileForm.email"
              required readonly />
          </div>
          <div class="col-md-12" v-else>
            <label class="form-label email-label">
              <span>Email<span style="color: red; margin-left: 2px;">*</span></span>
            </label>
            <input type="email" class="form-control" v-model="profileForm.email" required />
          </div>
          <div class="col-2" v-if="isUserForm">
            <label class="form-label">居住國家<span style="color: red; margin-left: 2px;">*</span></label>
            <select class="form-select" v-model="profileForm.residence">
              <option v-for="nation in counrtyStore.countries" :key="nation.id" :value="nation.id">
                {{ nation.nameZh }}
              </option>
            </select>
          </div>
          <div class="col-10" v-if="isUserForm || isNewUser">
            <label class="form-label">詳細地址<span style="color: red; margin-left: 2px;">*</span></label>
            <input type="text" class="form-control" v-model="profileForm.address" />
          </div>
          <div class="col-12 d-flex align-items-center">
            <button v-if="isNewUser" type="submit" class="btn btn-outline-primary me-2" :disabled="!isEdited">
              <i class="bi bi-check-lg me-1"></i>註冊
            </button>
            <button type="submit" class="btn btn-outline-primary me-2" :disabled="!isEdited"
              v-else-if="isUserForm || isPartnerForm">
              <i class="bi bi-check-lg me-1"></i>更新資料
            </button>
            <button v-else-if="isNewPartner" type="submit" class="btn btn-outline-primary me-2" :disabled="!isEdited">
              <i class="bi bi-check-lg me-1"></i>新增旅伴
            </button>

            <button type="button" class="btn btn-outline-danger" @click="resetProfile" :disabled="!isEdited"
              v-if="isUserForm || isPartnerForm">
              <i class="bi bi-x-lg me-1"></i>取消
            </button>
            <button type="button" class="btn btn-outline-danger" @click="noAdding" :disabled="!isEdited" v-else>
              <i class="bi bi-x-lg me-1"></i>取消
            </button>

            <button type="button" class="btn btn-danger ms-auto" @click="removePartner" v-if="isPartnerForm">
              <i class="bi bi-x-lg me-1"></i>刪除旅伴
            </button>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
  import { jwtDecode } from 'jwt-decode';
  import { useCountriesStore } from '@/stores/countries';
  import { usePartnersStore } from '@/stores/partners';
  import { computed, onMounted, reactive, ref, toRaw, watch } from 'vue';
  import Swal from 'sweetalert2';

  // read country data from pinia store
  const counrtyStore = useCountriesStore();
  const partnerStore = usePartnersStore();
  const token = localStorage.getItem('token');

  if (token) {
    const payload = jwtDecode(token);
    const userId = payload.id;
    const loadStores = async () => {
      try {
        await Promise.all([
          counrtyStore.getData(),
          partnerStore.getData(userId),
        ])
        console.log("load country and partner stores in form");
      } catch (error) {
        console.log("load store error: " + error);
      }
    };
    onMounted(() => {
      loadStores();
    })
  } else {
    onMounted(() => {
      counrtyStore.getData().then();
      console.log("load country store");
    })
  }


  // set today
  const today = new Date().toISOString();

  const newForm = () => {
    return {
      // id: crypto.randomUUID(),
      email: '',
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
      lastModified: '',
    };
  }

  const props = defineProps(['isUserForm', 'isNewPartner', 'isPartnerForm', 'id', 'isNewUser', 'skipCount']);
  const emit = defineEmits(['update:userInfo', 'newPartner', 'update:show-new-form', 'updatePartner', 'removePartner', 'newUser']);
  // const partner = defineModel('partner');
  const index = partnerStore.partners.findIndex(partner => partner.id === props.id);
  const partner = computed(() => {
    return partnerStore.partners[index].profile;
  });
  let userInfo = defineModel('userInfo');

  if (props.isNewPartner) {
    userInfo = ref(newForm());
  };

  const profileForm =
    props.isPartnerForm
      ? reactive(structuredClone(toRaw(partner.value)))
      : reactive(structuredClone(toRaw(userInfo.value)));
  const isValidPhone = ref(true);

  // detect if the userInfo has changed
  watch(() => userInfo.value, () => {
    Object.assign(profileForm, toRaw(userInfo.value));
  }, { deep: true });

  // gender data
  const genders = [
    { id: '男', code: 1 },
    { id: '女', code: 2 },
    { id: '其他', code: 3 },
  ];


  // change phone code as residence changes
  watch(() => profileForm.residence, (newVal) => {
    const newNation = counrtyStore.countries.find(i => i.id === newVal);
    profileForm.phoneCode = newNation.phoneCode;
  });

  // check if profile changes //
  // if no changes then can't submit

  const editedFlag = ref(false);
  const isEdited = computed({
    get() {
      if (props.skipCount > 0) {
        editedFlag.value = false;
        return false; // skip initial
      }

      // ensure phone num doesnt start with 0
      if (String(profileForm.telNumber).substring(0, 1) === '0') {
        isValidPhone.value = false;
      } else {
        isValidPhone.value = true;
      }

      if (props.isPartnerForm) {
        const keys = Object.keys(profileForm).filter(i => i !== 'phoneCode' && i !== 'lastModified');
        editedFlag.value = keys.some(key => profileForm[key] !== partner.value[key]) && isValidPhone.value;
      } else if (props.isNewPartner) {
        const keys = Object.keys(profileForm).filter(i => i !== 'phoneCode' && i !== 'address' && i !== 'lastModified');
        editedFlag.value = keys.every(key => profileForm[key] !== userInfo.value[key]) && isValidPhone.value;
      } else if (props.isNewUser) {
        const newKeys = Object.keys(profileForm).filter(i => i !== 'email' && i !== 'phoneCode' && i !== 'lastModified' && i !== 'givenNameLatin' && i !== 'familyNameLatin');
        editedFlag.value = newKeys.every(newKeys => profileForm[newKeys] !== userInfo.value[newKeys]) && isValidPhone.value;
      } else {
        const keys = Object.keys(profileForm).filter(i => i !== 'phoneCode' && i !== 'lastModified');
        editedFlag.value = keys.some(key => profileForm[key] !== userInfo.value[key]) && isValidPhone.value;
      }
      return editedFlag.value;
    },

    set(val) {
      editedFlag.value = val;
    }
  });

  // update to profiles when pressing the button
  const updateProfile = async () => {
    isEdited.value = false;
    if (props.isUserForm) {
      emit('update:userInfo', { ...profileForm });
    } else if (props.isNewPartner) {
      emit('newPartner', { ...profileForm });
    } else if (props.isPartnerForm) {
      // emit('update:partner', { ...profileForm });
      Swal.fire({
        title: '資料更新中',
        allowOutsideClick: false,
        allowEscapeKey: false,
        showConfirmButton: false,

        didOpen: async () => {
          Swal.showLoading();
          try {
            await partnerStore.updatePartner(props.id, { ...profileForm });
            Swal.hideLoading();
            Swal.update({
              icon: 'success',
              title: '資料已更新！',
              showConfirmButton: true,
              allowOutsideClick: true,
              allowEscapeKey: true,
              confirmButtonColor: '#47a8d1'
            });
          } catch (error) {
            Swal.hideLoading();
            Swal.update({
              icon: 'error',
              title: '資料更新失敗！',
              text: error,
              showConfirmButton: true,
              allowOutsideClick: true,
              allowEscapeKey: true,
              confirmButtonColor: '#47a8d1'
            });
          }
        }
      });
      // emit("updatePartner")

    } else if (props.isNewUser) {
      emit('update:userInfo', { ...profileForm });
      emit('newUser');
    }
  };

  // remove a partner
  const removePartner = () => {
    Swal.fire({
      showConfirmButton: true,
      showCancelButton: true,
      allowEscapeKey: true,
      allowOutsideClick: true,
      title: '是否確認刪除該筆旅伴資料？',
      confirmButtonText: '是',
      cancelButtonText: '否',
      customClass: {
        confirmButton: 'btn btn-danger',
        cancelButton: 'btn btn-secondary'
      }
    }).then((result) => {
      // partners.value.splice(index, 1);
      if (result.isConfirmed) {
        Swal.fire({
          didOpen: async () => {
            Swal.showLoading();
            await partnerStore.removePartner(props.id);
            emit('removePartner');
            console.log(partnerStore.partners[index]);
          }
        });

        setTimeout(() => {
          Swal.hideLoading();
          Swal.update({
            title: '資料已刪除！',
            showConfirmButton: true,
            allowOutsideClick: false,
            allowEscapeKey: false,
            confirmButtonColor: '#47a8d1'
          });
        }, 500);
      }
    });
  };

  // reset to userInfo
  const resetProfile = () => {
    if (props.isUserForm) {
      Object.assign(profileForm, toRaw(userInfo.value));

    } else if (props.isPartnerForm) {
      Object.assign(profileForm, toRaw(partner.value));
    }
  }

  // close adding
  const noAdding = () => {
    emit('update:show-new-form', false);
  }
</script>

<style scoped>
  .profile-email {
    cursor: not-allowed;
  }

  .prof-no-hover:hover,
  .prof-no-hover:focus,
  .prof-no-hover:active {
    transform: none !important;
  }

  .vertical-cent-align-label {
    display: flex;
    align-items: center;
  }

  .email-label {
    display: flex;
    align-items: center;
    gap: 10px;
  }

  .btn-change-email {
    padding: 3px 5px;
    font-size: 12px;
    background: linear-gradient(to bottom, #ffffff, #dadada);
    border: 1px solid #ccc;
  }

  .btn-change-email:hover,
  .btn-change-email:focus,
  .btn-change-email:active {
    transform: none !important;
  }

  .btn-change-email:active {
    box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.3);
    background-color: #4e4e4e;
    border: 1px solid rgb(133, 133, 133);
  }
</style>