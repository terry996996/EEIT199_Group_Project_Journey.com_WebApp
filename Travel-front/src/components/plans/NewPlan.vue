<template>
  <div ref="modal" class="modal fade" id="myFormModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">新增旅程</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <PlanForm :key="formKey" v-model:show-new-plan="showNewPlan" :is-new-plan="true" @new-plan="newPlan">
          </PlanForm>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import PlanForm from './PlanForm.vue';
import { Modal } from 'bootstrap/dist/js/bootstrap.bundle.min';
import { onBeforeUnmount, onMounted, ref, watch } from 'vue';
import Swal from 'sweetalert2';
import { usePlansStore } from '@/stores/plans';

const props = defineProps(['userId']);
const formKey = ref(0);
const showNewPlan = defineModel('show-new-plan');
const emit = defineEmits(['update:show-new-plan']);
const modal = ref(null);
let modalInstance = null;
const planStore = usePlansStore();

onMounted(() => {
  modalInstance = new Modal(modal.value, {
    backdrop: 'static',
    keyboard: false,
  });
  modal.value.addEventListener('hide.bs.modal', close);
});

onBeforeUnmount(() => {
  modalInstance?.dispose();
  modal.value.removeEventListener('hide.bs.modal', close());
});

watch(showNewPlan, (newVal) => {
  if (newVal) {
    modalInstance?.show();
  } else {
    modalInstance?.hide();
    afterClose();
  }
})

const close = () => {
  emit('update:show-new-plan', false);
  afterClose();
}

// deal with aria-label warnings
const afterClose = () => {
  if (modal.value && modal.value.contains(document.activeElement)) {
    if (document.activeElement instanceof HTMLElement) {
      document.activeElement.blur();
    }
  }
}

const newPlan = (data) => {
  console.log("add new: ", data);
  formKey.value++;
  try {
    Swal.fire({
      allowOutsideClick: false,
      allowEscapeKey: false,
      showConfirmButton: false,

      didOpen: async () => {
        Swal.showLoading();
        await planStore.addNewPlan(data, props.userId);
        setTimeout(() => {
          Swal.hideLoading();
          Swal.update({
            title: '已成功新增旅程！',
            showConfirmButton: true,
            allowOutsideClick: true,
            allowEscapeKey: true,
            confirmButtonColor: '#47a8d1',
          })
        }, 500);
      },
    }).then(result => {
      if (result.isConfirmed) {
        close();
      }
    })

  } catch (error) {
    console.log("error adding new plan: " + error);
  }
}

</script>