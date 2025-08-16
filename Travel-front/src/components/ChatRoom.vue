<template>
  <div class="chat-container" :class="{ 'chat-open': isOpen }">
    <!-- 聊天視窗 -->
    <div class="chat-window">
      <!-- 聊天標題欄 -->
      <div class="chat-header">
        <div class="chat-info">
          <div class="avatar">
            <img src="https://cdn-icons-png.flaticon.com/512/149/149071.png" alt="客服" />
            <span class="status-dot online"></span>
          </div>
          <div class="user-info">
            <h5 class="user-name">Journey.com 客服</h5>
            <span class="user-status">線上 • 即時回覆</span>
          </div>
        </div>
        <div class="chat-actions">
          <button class="btn-icon" @click="minimizeChat">
            <i class="bi bi-dash"></i>
          </button>
          <button class="btn-icon" @click="closeChat">
            <i class="bi bi-x"></i>
          </button>
        </div>
      </div>

      <!-- 聊天訊息區域 -->
      <div class="chat-messages" ref="messagesContainer">
        <div v-for="message in messages" :key="message.id" class="message" :class="message.type">
          <div class="message-avatar" v-if="message.type === 'received'">
            <img src="https://cdn-icons-png.flaticon.com/512/149/149071.png" alt="客服" />
          </div>
          <div class="message-content">
            <div class="message-bubble">
              <p class="message-text">{{ message.text }}</p>
              <span class="message-time">{{
                formatTime(message.timestamp)
              }}</span>
            </div>
          </div>
          <div class="message-avatar" v-if="message.type === 'sent'">
            <img src="https://cdn-icons-png.flaticon.com/512/149/149071.png" alt="用戶" />
          </div>
        </div>

        <!-- 輸入中指示器 -->
        <div v-if="isTyping" class="message received">
          <div class="message-avatar">
            <img src="https://cdn-icons-png.flaticon.com/512/149/149071.png" alt="客服" />
          </div>
          <div class="message-content">
            <div class="message-bubble typing">
              <div class="typing-indicator">
                <span></span>
                <span></span>
                <span></span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 快速回覆按鈕 -->
      <div class="quick-replies" v-if="showQuickReplies">
        <button v-for="reply in quickReplies" :key="reply.id" class="quick-reply-btn"
          @click="sendQuickReply(reply.text)">
          {{ reply.text }}
        </button>
      </div>

      <!-- 聊天輸入區域 -->
      <div class="chat-input-area">
        <div class="input-wrapper">
          <button class="btn-icon" @click="toggleEmoji">
            <i class="bi bi-emoji-smile"></i>
          </button>
          <input v-model="newMessage" type="text" class="chat-input" placeholder="輸入訊息..." @keyup.enter="sendMessage"
            @input="handleTyping" ref="messageInput" />
          <button class="btn-icon" @click="attachFile">
            <i class="bi bi-paperclip"></i>
          </button>
          <button class="send-btn" @click="sendMessage" :disabled="!newMessage.trim()">
            <i class="bi bi-send"></i>
          </button>
        </div>

        <!-- 表情符號選擇器 -->
        <div v-if="showEmoji" class="emoji-picker">
          <div class="emoji-grid">
            <button v-for="emoji in emojis" :key="emoji" class="emoji-btn" @click="addEmoji(emoji)">
              {{ emoji }}
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 聊天浮動按鈕 -->
    <div v-if="!isOpen" class="chat-float" @click="openChat">
      <div class="float-icon">
        <i class="bi bi-chat-dots"></i>
        <span class="notification-badge" v-if="unreadCount > 0">{{
          unreadCount
        }}</span>
      </div>
      <div class="float-text">
        <h6>需要協助？</h6>
        <p>點擊開始聊天</p>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { ref, reactive, nextTick, onMounted, watch } from "vue";

  const props = defineProps({
    isOpen: {
      type: Boolean,
      default: false,
    },
  });

  const emit = defineEmits(["close", "minimize", "open"]);

  // 響應式資料
  const messagesContainer = ref(null);
  const messageInput = ref(null);
  const newMessage = ref("");
  const isTyping = ref(false);
  const showEmoji = ref(false);
  const showQuickReplies = ref(true);
  const unreadCount = ref(0);

  // 訊息列表
  const messages = ref([
    {
      id: 1,
      type: "received",
      text: "您好！歡迎來到 Journey.com，我是您的專屬客服。請問有什麼可以協助您的嗎？",
      timestamp: new Date(Date.now() - 60000),
    },
  ]);

  // 快速回覆選項
  const quickReplies = [
    { id: 1, text: "我想預訂飯店" },
    { id: 2, text: "機票查詢" },
    { id: 3, text: "取消訂單" },
    { id: 4, text: "退款問題" },
    { id: 5, text: "其他問題" },
  ];

  // 表情符號
  const emojis = [
    "😊",
    "😄",
    "👍",
    "❤️",
    "🎉",
    "😍",
    "🤔",
    "😅",
    "😭",
    "😡",
    "👋",
    "💪",
    "🎯",
    "🔥",
    "💯",
    "✨",
  ];

  // 開啟聊天
  const openChat = () => {
    emit("open");
    unreadCount.value = 0;
    nextTick(() => {
      messageInput.value?.focus();
    });
  };

  // 關閉聊天
  const closeChat = () => {
    emit("close");
  };

  // 最小化聊天
  const minimizeChat = () => {
    emit("minimize");
  };

  // 發送訊息
  const sendMessage = () => {
    if (!newMessage.value.trim()) return;

    const message = {
      id: Date.now(),
      type: "sent",
      text: newMessage.value,
      timestamp: new Date(),
    };

    messages.value.push(message);
    newMessage.value = "";
    showQuickReplies.value = false;

    // 模擬客服回覆
    setTimeout(() => {
      isTyping.value = true;
      setTimeout(() => {
        isTyping.value = false;
        const reply = {
          id: Date.now() + 1,
          type: "received",
          text: generateAutoReply(),
          timestamp: new Date(),
        };
        messages.value.push(reply);
        scrollToBottom();
      }, 1500);
    }, 1000);

    scrollToBottom();
  };

  // 發送快速回覆
  const sendQuickReply = (text) => {
    newMessage.value = text;
    sendMessage();
  };

  // 處理輸入
  const handleTyping = () => {
    // 可以實作輸入指示器
  };

  // 切換表情符號
  const toggleEmoji = () => {
    showEmoji.value = !showEmoji.value;
  };

  // 添加表情符號
  const addEmoji = (emoji) => {
    newMessage.value += emoji;
    showEmoji.value = false;
    messageInput.value?.focus();
  };

  // 附加檔案
  const attachFile = () => {
    // 實作檔案上傳功能
    alert("檔案上傳功能開發中...");
  };

  // 格式化時間
  const formatTime = (timestamp) => {
    const now = new Date();
    const time = new Date(timestamp);
    const diff = now - time;

    if (diff < 60000) {
      return "剛剛";
    } else if (diff < 3600000) {
      return `${Math.floor(diff / 60000)}分鐘前`;
    } else {
      return time.toLocaleTimeString("zh-TW", {
        hour: "2-digit",
        minute: "2-digit",
      });
    }
  };

  // 滾動到底部
  const scrollToBottom = () => {
    nextTick(() => {
      if (messagesContainer.value) {
        messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
      }
    });
  };

  // 生成自動回覆
  const generateAutoReply = () => {
    const replies = [
      "好的，我來為您查詢相關資訊。",
      "了解您的需求，讓我為您提供詳細說明。",
      "謝謝您的詢問，我會盡快為您處理。",
      "這個問題我可以協助您解決，請稍等。",
      "我明白您的困擾，讓我來協助您。",
    ];
    return replies[Math.floor(Math.random() * replies.length)];
  };

  // 監聽訊息變化
  watch(
    messages,
    () => {
      scrollToBottom();
    },
    { deep: true }
  );

  // 監聽聊天開啟狀態
  watch(
    () => props.isOpen,
    (newVal) => {
      if (newVal) {
        nextTick(() => {
          scrollToBottom();
          messageInput.value?.focus();
        });
      }
    }
  );

  onMounted(() => {
    scrollToBottom();
  });
</script>

<style scoped>
  .chat-container {
    position: fixed;
    bottom: 20px;
    right: 20px;
    z-index: 1000;
    font-family: var(--font-family);
    pointer-events: none;
  }

  .chat-window {
    width: 350px;
    height: 500px;
    background: var(--bg-primary);
    border-radius: var(--radius-lg);
    box-shadow: var(--shadow-xl);
    display: flex;
    flex-direction: column;
    overflow: hidden;
    border: 1px solid var(--border-color);
    transform: translateY(100%);
    opacity: 0;
    transition: all var(--transition-normal);
    pointer-events: auto;
  }

  .chat-open .chat-window {
    transform: translateY(0);
    opacity: 1;
  }

  .chat-header {
    background: var(--primary-color);
    color: var(--white);
    padding: var(--spacing-md) var(--spacing-lg);
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .chat-info {
    display: flex;
    align-items: center;
    gap: var(--spacing-md);
  }

  .avatar {
    position: relative;
    width: 40px;
    height: 40px;
  }

  .avatar img {
    width: 100%;
    height: 100%;
    border-radius: 50%;
    object-fit: cover;
  }

  .status-dot {
    position: absolute;
    bottom: 2px;
    right: 2px;
    width: 12px;
    height: 12px;
    border-radius: 50%;
    border: 2px solid var(--white);
  }

  .status-dot.online {
    background: var(--success-color);
  }

  .user-name {
    font-size: var(--font-size-sm);
    font-weight: var(--font-weight-semibold);
    margin: 0;
    color: var(--white);
  }

  .user-status {
    font-size: var(--font-size-xs);
    opacity: 0.8;
  }

  .chat-actions {
    display: flex;
    gap: var(--spacing-xs);
  }

  .btn-icon {
    background: none;
    border: none;
    color: var(--white);
    padding: var(--spacing-xs);
    border-radius: var(--radius-sm);
    cursor: pointer;
    transition: background-color var(--transition-fast);
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .btn-icon:hover {
    background: rgba(255, 255, 255, 0.1);
  }

  .chat-messages {
    flex: 1;
    padding: var(--spacing-md);
    overflow-y: auto;
    background: var(--gray-50);
  }

  .message {
    display: flex;
    margin-bottom: var(--spacing-md);
    gap: var(--spacing-sm);
  }

  .message.sent {
    flex-direction: row-reverse;
  }

  .message-avatar {
    width: 32px;
    height: 32px;
    flex-shrink: 0;
  }

  .message-avatar img {
    width: 100%;
    height: 100%;
    border-radius: 50%;
    object-fit: cover;
  }

  .message-content {
    flex: 1;
    max-width: 70%;
  }

  .message-bubble {
    background: var(--white);
    padding: var(--spacing-sm) var(--spacing-md);
    border-radius: var(--radius-lg);
    box-shadow: var(--shadow-sm);
    position: relative;
  }

  .message.sent .message-bubble {
    background: var(--primary-color);
    color: var(--white);
  }

  .message-text {
    margin: 0 0 var(--spacing-xs) 0;
    font-size: var(--font-size-sm);
    line-height: var(--line-height-normal);
  }

  .message-time {
    font-size: var(--font-size-xs);
    opacity: 0.7;
    display: block;
  }

  .typing-indicator {
    display: flex;
    gap: 4px;
    padding: var(--spacing-sm) 0;
  }

  .typing-indicator span {
    width: 8px;
    height: 8px;
    background: var(--gray-400);
    border-radius: 50%;
    animation: typing 1.4s infinite ease-in-out;
  }

  .typing-indicator span:nth-child(1) {
    animation-delay: -0.32s;
  }

  .typing-indicator span:nth-child(2) {
    animation-delay: -0.16s;
  }

  @keyframes typing {

    0%,
    80%,
    100% {
      transform: scale(0.8);
      opacity: 0.5;
    }

    40% {
      transform: scale(1);
      opacity: 1;
    }
  }

  .quick-replies {
    padding: var(--spacing-md);
    background: var(--white);
    border-top: 1px solid var(--border-color);
  }

  .quick-reply-btn {
    background: var(--gray-100);
    border: 1px solid var(--border-color);
    border-radius: var(--radius-pill);
    padding: var(--spacing-xs) var(--spacing-md);
    margin: 0 var(--spacing-xs) var(--spacing-xs) 0;
    font-size: var(--font-size-xs);
    cursor: pointer;
    transition: all var(--transition-fast);
  }

  .quick-reply-btn:hover {
    background: var(--primary-color);
    color: var(--white);
    border-color: var(--primary-color);
  }

  .chat-input-area {
    background: var(--white);
    border-top: 1px solid var(--border-color);
    position: relative;
  }

  .input-wrapper {
    display: flex;
    align-items: center;
    gap: var(--spacing-sm);
    padding: var(--spacing-md);
  }

  .chat-input {
    flex: 1;
    border: 1px solid var(--border-color);
    border-radius: var(--radius-pill);
    padding: var(--spacing-sm) var(--spacing-md);
    font-size: var(--font-size-sm);
    outline: none;
    transition: border-color var(--transition-fast);
  }

  .chat-input:focus {
    border-color: var(--primary-color);
  }

  .send-btn {
    background: var(--primary-color);
    color: var(--white);
    border: none;
    border-radius: 50%;
    width: 36px;
    height: 36px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all var(--transition-fast);
  }

  .send-btn:hover:not(:disabled) {
    background: var(--primary-dark);
    transform: scale(1.1);
  }

  .send-btn:disabled {
    background: var(--gray-300);
    cursor: not-allowed;
  }

  .emoji-picker {
    position: absolute;
    bottom: 100%;
    left: 0;
    right: 0;
    background: var(--white);
    border: 1px solid var(--border-color);
    border-radius: var(--radius-lg);
    padding: var(--spacing-md);
    box-shadow: var(--shadow-lg);
  }

  .emoji-grid {
    display: grid;
    grid-template-columns: repeat(8, 1fr);
    gap: var(--spacing-xs);
  }

  .emoji-btn {
    background: none;
    border: none;
    font-size: 1.5rem;
    padding: var(--spacing-xs);
    border-radius: var(--radius-sm);
    cursor: pointer;
    transition: background-color var(--transition-fast);
  }

  .emoji-btn:hover {
    background: var(--gray-100);
  }

  .chat-float {
    background: var(--primary-color);
    color: var(--white);
    padding: var(--spacing-md);
    border-radius: var(--radius-lg);
    box-shadow: var(--shadow-lg);
    cursor: pointer;
    transition: all var(--transition-normal);
    display: flex;
    align-items: center;
    gap: var(--spacing-md);
    min-width: 200px;
    pointer-events: auto;
  }

  .chat-float:hover {
    transform: translateY(-2px);
    box-shadow: var(--shadow-xl);
  }

  .float-icon {
    position: relative;
    width: 48px;
    height: 48px;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 1.5rem;
  }

  .notification-badge {
    position: absolute;
    top: -5px;
    right: -5px;
    background: var(--danger-color);
    color: var(--white);
    border-radius: 50%;
    width: 20px;
    height: 20px;
    font-size: var(--font-size-xs);
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: var(--font-weight-bold);
  }

  .float-text h6 {
    margin: 0 0 var(--spacing-xs) 0;
    font-size: var(--font-size-sm);
    font-weight: var(--font-weight-semibold);
  }

  .float-text p {
    margin: 0;
    font-size: var(--font-size-xs);
    opacity: 0.8;
  }

  @media (max-width: 768px) {
    .chat-container {
      bottom: 10px;
      right: 10px;
      left: 10px;
    }

    .chat-window {
      width: 100%;
      height: 400px;
    }

    .chat-float {
      min-width: auto;
      width: 100%;
    }
  }
</style>
