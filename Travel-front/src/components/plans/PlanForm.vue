<template>
  <div class="card prof-no-hover">
    <div class="card-body ">
      <form @submit.prevent="updatePlan">
        <div class="row g-3">
          <div class="col-md-12">
            <label class="form-label">旅程名稱<span style="color: red; margin-left: 2px;">*</span></label>
            <input type="text" class="form-control" v-model="form.title" required />
          </div>
          <div class="col-md-6">
            <label class="form-label">開始日期<span style="color: red; margin-left: 2px;">*</span></label>
            <input type="date" class="form-control" v-model="form.startDate" :max="form.endDate" />
          </div>
          <div class="col-md-6">
            <label class="form-label">結束日期<span style="color: red; margin-left: 2px;">*</span></label>
            <input type="date" class="form-control" v-model="form.endDate" :min="form.startDate" />
          </div>
          <div class="col-md-12">
            <label class="form-label">簡短描述</label>
            <input type="text" class="form-control" v-model="form.description" />
          </div>

          <div class="col-12 d-flex align-items-center">
            <button v-if="isNewPlan" type="submit" class="btn btn-outline-primary me-2" :disabled="!isEdited">
              <i class="bi bi-check-lg me-1"></i>新增旅程
            </button>
            <button type="submit" class="btn btn-outline-primary me-2" :disabled="!isEdited" v-else>
              <i class="bi bi-check-lg me-1"></i>更新旅程
            </button>

            <button type="button" class="btn btn-outline-danger" @click="noAdding" :disabled="!isEdited"
              v-if="isNewPlan">
              <i class="bi bi-x-lg me-1"></i>取消
            </button>
            <button type="button" class="btn btn-outline-danger" @click="resetProfile" :disabled="!isEdited" v-else>
              <i class="bi bi-x-lg me-1"></i>取消
            </button>
            <button type="button" class="btn btn-danger ms-auto" @click="removePlan" v-if="!isNewPlan">
              <i class="bi bi-x-lg me-1"></i>刪除旅程
            </button>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref, toRaw } from 'vue';
import { jwtDecode } from 'jwt-decode';
import { usePlansStore } from '@/stores/plans';

const props = defineProps(['isNewPlan', 'id']);
const emit = defineEmits(['newPlan', 'update:show-new-plan']);
// const isEdited = ref(true);

const planStore = usePlansStore();
const token = localStorage.getItem('token');

if (token) {
  const payload = jwtDecode(token);
  const userId = payload.id;
  const loadStores = async () => {
    try {
      await planStore.getData(userId),
        console.log("load plan store in form");
    } catch (error) {
      console.log("load store error: " + error);
    }
  };

  onMounted(() => {
    loadStores();
  })
}

const newForm = () => {
  return {
    id: null,
    title: '',
    startDate: '',
    endDate: '',
    description: '',
    createdBy: null,
    lastModified: '',
    deleteStatus: 1,
  };
}
const index = planStore.plans.findIndex(plan => plan.id === props.id);
const plan = computed(() => {
  return planStore.plans[index];
})

let planInfo = ref(null);
if (props.isNewPlan) {
  planInfo = ref(newForm());
}

const form = props.isNewPlan
  ? reactive(structuredClone(toRaw(planInfo.value)))
  : reactive(structuredClone(toRaw(plan.value)));


const editedFlag = ref(false);
const isEdited = computed({
  get() {
    const keyName = ['createdBy', 'lastModified', 'id'];
    if (props.isNewPlan) {
      const keys = Object.keys(form).filter(i => !keyName.includes(i) && i !== 'description' && i !== 'deleteStatus');
      editedFlag.value = keys.every(key => form[key] !== planInfo.value[key]);
    } else {
      const keys = Object.keys(form).filter(i => !keyName.includes(i));
      editedFlag.value = keys.some(key => form[key] !== plan.value[key]);
    }
    return editedFlag.value;
  },

  set(val) {
    editedFlag.value = val;
  }
});

const updatePlan = () => {
  isEdited.value = false;
  if (props.isNewPlan) {
    emit('newPlan', { ...form });
  }
}

const resetProfile = () => {
  Object.assign(form, toRaw(plan.value));
}

// close adding
const noAdding = () => {
  emit('update:show-new-plan', false);
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