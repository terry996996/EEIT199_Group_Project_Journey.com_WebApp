<template>
  <div ref="modal" class="modal fade" id="myFormModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">新增旅伴資料</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <ProfileForm :key="formKey" v-model:show-new-form="showNewForm" :is-new-partner="true"
            @new-partner="newPartner"></ProfileForm>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { Modal } from 'bootstrap/dist/js/bootstrap.bundle.min';
import { onBeforeUnmount, onMounted, ref, watch } from 'vue';
import ProfileForm from './ProfileForm.vue';
import Swal from 'sweetalert2';
import { usePartnersStore } from '@/stores/partners';

const props = defineProps(['userId']);
const formKey = ref(0);
const showNewForm = defineModel('show-new-form');
const emit = defineEmits(['update:show-new-form', 'update:partners']);
const modal = ref(null);
let modalInstance = null;
const partnerStore = usePartnersStore();

onMounted(async () => {
  // await partnerStore.getData();
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

watch(showNewForm, (newVal) => {
  if (newVal) {
    modalInstance?.show();
  } else {
    modalInstance?.hide();
    afterClose();
  }
})

const close = () => {
  emit('update:show-new-form', false);
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

const newPartner = (data) => {
  console.log("add new: ", data);
  // partners.value.push(data);
  // emit('update:partners', partners);
  formKey.value++;
  try {
    Swal.fire({
      allowOutsideClick: false,
      allowEscapeKey: false,
      showConfirmButton: false,

      didOpen: async () => {
        Swal.showLoading();
        await partnerStore.addNewPartner(data, props.userId);
        setTimeout(() => {
          Swal.hideLoading();
          Swal.update({
            title: '已成功新增旅伴！',
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
    console.log("error adding new partner: " + error);
  }
}

</script>

<style scoped></style>