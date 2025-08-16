<template>
  <div class="card mt-custom" id="section-description">
    <div class="card-body position-relative">

      <!-- ✅ 編輯/完成按鈕 -->
      <!-- ✅ 功能按鈕區塊，只有創建者才會看到 -->
<!-- ✅ 編輯/完成按鈕 -->
<div class="position-absolute edit-btn" v-if="editable">
  <template v-if="!isEditing">
    <!-- 功能按鈕 -->
    <div class="dropdown">
      <button
        class="btn btn-sm btn-outline-primary dropdown-toggle"
        type="button"
        data-bs-toggle="dropdown"
        aria-expanded="false"
      >
        功能
      </button>
      <ul class="dropdown-menu">
        <li>
          <a class="dropdown-item" href="#" @click.prevent="toggleEdit">✏️ 修改</a>
        </li>
        <li>
          <a class="dropdown-item text-danger" href="#" @click.prevent="handleDelete">🗑 刪除區塊</a>
        </li>
      </ul>
    </div>
  </template>

  <!-- ✅ 加上這段：編輯模式下的「確認」按鈕 -->
  <template v-else>
    <button class="btn btn-sm btn-primary" @click="toggleEdit">✅ 確認</button>
  </template>
</div>

      <!-- 🏷️ 標題區 -->
      <h4 class="card-title mb-4" v-if="!isEditing">{{ title }}</h4>
      <input
        v-else
        class="form-control mb-3"
        v-model="editableTitle"
        placeholder="請輸入標題"
      />

      <!-- 🛠️ 工具列 -->
      <div v-if="isEditing" class="toolbar mb-2">
        <button @click="editor.chain().focus().toggleBold().run()">粗體</button>
        <button @click="editor.chain().focus().toggleItalic().run()">斜體</button>
        <button @click="editor.chain().focus().toggleBulletList().run()">• 清單</button>
        <button @click="insertImage">🖼 圖片</button>
        <select @change="setFontSize($event.target.value)">
          <option disabled selected>字體大小</option>
          <option value="12">12px</option>
          <option value="16">16px</option>
          <option value="18">18px</option>
          <option value="20">px</option>
        </select>
      </div>

      <!-- ✏️ 編輯器內容 -->
      <EditorContent v-if="isEditing" :editor="editor" />

      <!-- ✅ 顯示模式 -->
      <div v-else class="description mb-4" v-html="description" />

    </div>
  </div>
</template>

<script setup>
import { ref, watch, onBeforeUnmount } from 'vue'
import { Editor, EditorContent } from '@tiptap/vue-3'
import StarterKit from '@tiptap/starter-kit'
import Image from '@tiptap/extension-image'
// ✅ 套用文字樣式（如字體大小）必須使用 TextStyle extension
import { TextStyle } from '@tiptap/extension-text-style'
import Swal from 'sweetalert2'

// ✅ 接收父層傳入的 props（包含標題、內容、是否可編輯）
const props = defineProps({
  contentId: Number,
  title: String,
  description: String,
  editable: Boolean
})
const emit = defineEmits(['updateDescription','deleteContent'])

// ✅ 控制區塊標題（v-model 綁定）
const editableTitle = ref(props.title)
watch(() => props.title, (newVal) => {
  editableTitle.value = newVal
})

// ✅ 控制是否進入編輯模式
const isEditing = ref(false)
const editor = ref(null)

// ✅ 客製化圖片樣式（固定大小、object-fit）
const CustomImage = Image.extend({
  addAttributes() {
    return {
      src: {
        default: null,
        parseHTML: el => el.getAttribute('src'),
        renderHTML: attrs => ({
          src: attrs.src,
          style: 'width: 815px; height: 500px; object-fit: cover;',
        })
      },
      alt: {
        default: null,
        parseHTML: el => el.getAttribute('alt'),
        renderHTML: attrs => ({
          alt: attrs.alt
        })
      }
    }
  },
  parseHTML() {
    return [{ tag: 'img[src]' }]
  }
})

// ✅ 初始化 Tiptap 編輯器
editor.value = new Editor({
  extensions: [
    StarterKit,
    // ✅ 支援文字樣式，必要才能改變字體大小
    TextStyle.extend({
      addAttributes() {
        return {
          style: {
            default: null,
            parseHTML: el => el.getAttribute('style'),
            renderHTML: attrs => attrs.style ? { style: attrs.style } : {}
          }
        }
      }
    }),
    CustomImage
  ],
  editorProps: {
    attributes: {
      class: 'tiptap-editor'
    }
  }
})

// ✅ 載入初始內容到編輯器（props.description）
watch(() => props.description, (val) => {
  if (val && editor.value) {
    editor.value.commands.setContent(val)
  }
}, { immediate: true })

// ✅ 切換編輯模式：從 HTML → 編輯器，或反之
const toggleEdit = () => {
  isEditing.value = !isEditing.value

  if (isEditing.value) {
    editor.value?.commands.setContent(props.description || '')
  } else {
    const html = editor.value.getHTML()
    emit('updateDescription', {
      title: editableTitle.value,
      description: html
    })
  }
}

// ✅ 插入圖片（壓縮為 base64）
const insertImage = () => {
  const input = document.createElement('input')
  input.type = 'file'
  input.accept = 'image/*'

  input.onchange = () => {
    const file = input.files?.[0]
    if (!file) return

    const reader = new FileReader()
    reader.onload = () => {
      const img = new window.Image()
      img.onload = () => {
        const MAX_WIDTH = 800
        const scale = MAX_WIDTH / img.width
        const canvas = document.createElement('canvas')
        canvas.width = MAX_WIDTH
        canvas.height = img.height * scale

        const ctx = canvas.getContext('2d')
        ctx.drawImage(img, 0, 0, canvas.width, canvas.height)

        const compressedBase64 = canvas.toDataURL('image/jpeg', 0.5)
        const success = editor.value.chain().focus().setImage({ src: compressedBase64 }).run()
        console.log("✅ 圖片插入成功：", success)
      }
      img.src = reader.result
    }

    reader.readAsDataURL(file)
  }

  input.click()
}

// ✅ 設定字體大小（同時可加入其他樣式）
const setFontSize = (size) => {
  editor.value.chain().focus().setMark('textStyle', {
    style: `font-size: ${size}px`
  }).run()
}

// ✅ 卸載元件時清除 editor
onBeforeUnmount(() => {
  editor.value?.destroy()
})

// ❗額外工具：清除 HTML 中的 <span> 標籤（尚未使用）
const cleanHTML = (rawHtml) => {
  return rawHtml
    .replace(/<span[^>]*>/g, '')
    .replace(/<\/span>/g, '')
}

// ✅ 刪除區塊（需輸入正確標題）
const handleDelete = async () => {
  const { value: inputTitle } = await Swal.fire({
    title: '⚠️ 確認刪除區塊',
    html: `請輸入標題 <strong>"${editableTitle.value}"</strong> 以確認刪除。`,
    input: 'text',
    inputPlaceholder: '請輸入完整標題',
    showCancelButton: true,
    confirmButtonText: '刪除',
    cancelButtonText: '取消',
    inputValidator: (value) => {
      if (!value) return '請輸入標題'
      return null
    }
  })

  if (inputTitle === editableTitle.value) {
    emit('deleteContent', props.contentId)
    Swal.fire('✅ 區塊已刪除', '', 'success')
  } else if (inputTitle) {
    Swal.fire('❌ 標題不符', '請輸入正確的標題才能刪除區塊。', 'error')
  }
}
</script>



<style scoped>
.card-title {
  font-weight: bold;
}
.edit-btn {
  position: absolute;
  top: 1rem;
  right: 1rem;
}

/* 工具列樣式 */
.toolbar button,
.toolbar select {
  margin-right: 8px;
  padding: 5px 10px;
  font-size: 14px;
}

.editor {
  min-height: 200px;
  background: #fff;
  overflow-y: auto;
}
.tiptap-editor,
.tiptap-editor * {
  color: #212529 !important; /* Bootstrap 預設文字色 */
}

/* 顯示與編輯圖片統一樣式 */
.tiptap-editor img,
.description img {
  width: 800px;
  height: 500px;
  object-fit: cover;
  display: block;
  margin: 10px auto;
}

.mt-custom {
  margin-top: 30px;
  margin-bottom: 1.5rem;
}

.toolbar {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 1rem;
}
.toolbar button,
.toolbar select {
  border: 1px solid #ccc;
  border-radius: 6px;
  background-color: #fff;
  padding: 6px 12px;
  font-size: 14px;
  font-weight: 500;
  color: #333;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
}
.toolbar button:hover,
.toolbar select:hover {
  border-color: #0d6efd;
  color: #0d6efd;
}
.description img {
  display: block;
  max-width: 100%;
  height: auto;
}
.description,
.description * {
  color: #212529 !important;
}
</style>
