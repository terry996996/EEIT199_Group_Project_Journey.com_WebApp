<script setup>
import { ref, onMounted, watch, computed, nextTick, reactive } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router'

const router = useRouter();

// 控制全域載入狀態，以Journey.com的icon作為loading的圖標
const isLoading = ref(false);

const goHome = () => {
  isLoading.value = true;
  setTimeout(() => {
    router.push('/home');
    isLoading.value = false; // 在路由跳轉完成後，隱藏載入狀態
  }, 1000);
};

// 表情符號面板
const showEmojiPanel = ref(false); // 控制表情面板的顯示
// 控制輸入和其他互動是否被表情面板阻擋
const isInteractionBlocked = ref(false);
// 表情符號清單(含卷軸功能)
const emojis = [
  '😀', '😃', '😄', '😁', '😆', '😅', '😂', '🤣', '😊', '😇', '😉', '😍', '🥰', '😘', '😙', '😚', '😋',
  '😎', '🤩', '🥳', '😏', '😒', '😞', '😔', '😟', '😕', '🙁', '☹️', '😤', '😠', '😡', '🤬', '🤯', '😳',
  '🥵', '🥶', '😱', '😨', '😰', '😢', '😭', '🥺', '😤', '🤤', '😴', '😷', '🤒', '🤕', '🤮', '🤧', '😇',
  '🫠', '🫡', '🤨', '🤔', '🤫', '🫢', '🤭', '🫠', '🫡', '🤥', '🤓', '🧐', '😈', '👿', '👹', '👺', '🤡',
  '👻', '👽', '👾', '🤖', '😺', '😸', '😹', '😻', '😼', '😽', '🙀', '😿', '😾'
];

// 切換表情面板顯示
const toggleEmojiPanel = () => {
  showEmojiPanel.value = !showEmojiPanel.value;
  isInteractionBlocked.value = showEmojiPanel.value; // 當面板開啟時禁用互動
};

// 點擊空白區域關閉表情面板並解除互動禁用
const closeEmojiPanelAndUnblock = () => {
  showEmojiPanel.value = false;
  isInteractionBlocked.value = false;
};

// 插入表情符號到輸入框
const insertEmoji = (emoji) => {
  messageText.value += emoji;
  showEmojiPanel.value = false; // 點選表情後自動關閉選單
  isInteractionBlocked.value = false; // 解除互動禁用
};

// 傳送按鈕禁用狀態
const isSendDisabled = computed(() => {
  return isAwaitingBotResponse.value || isInteractionBlocked.value || messageText.value.trim() === '';
});


// 狀態變數 (State Variables)
const selectedTypeId = ref(null);      // 當前選中的主題類型ID
const questionTypes = ref([]);         // 所有問題類型（主題）
const questions = ref([]);             // 所有問題模板
const messages = ref([]);              // 聊天訊息列表
const messageText = ref('');           // 輸入框的訊息內容


const chatRoomId = ref(null);
const chatRooms = reactive({});

const messageList = ref(null);
const showTopicSelector = ref(true);
const selectedTopicName = ref('');

// 評點狀態儲存
const feedbackGiven = reactive({});
// 控制用戶是否可輸入/點擊(按讚/按倒讚)的狀態
const isAwaitingBotResponse = ref(false);


// Axios
axios.defaults.withCredentials = true;
axios.defaults.baseURL = 'http://localhost:8080';


// Computed 計算屬性
const allTopics = computed(() => {
  return questionTypes.value;
});
const filteredQuestions = computed(() => {
  if (selectedTypeId.value) {
    return questions.value.filter(q => q.type.typeId === selectedTypeId.value);
  }
  return [];
});

// 方法 
const getTopicIcon = (topicName) => {
  const iconMap = {
    '住宿': '🏨', '機票': '✈️', '巴士': '🚌', '機場接駁': '🚐', '租車': '🚗',
    '門票': '🎫', '套票': '🎟️', '飯店': '🏨', '交通票券': '🎫', '景點票券': '🎟️',
    '行程規劃': '🗺️', '稅務與費用': '💵', '平台相關': '⚙️'
  };
  return iconMap[topicName] || '❓';
};

const getCurrentTime = () => {
  return new Date().toLocaleTimeString('zh-TW', { hour: '2-digit', minute: '2-digit' });
};

const scrollToBottom = () => {
  if (messageList.value) {
    requestAnimationFrame(() => {
      messageList.value.scrollTop = messageList.value.scrollHeight;
    });
  }
};

const sendMessageLocally = (content, sender = 'user', messageType = 'text') => {
  const newMessage = {
    messageId: 'local-' + Date.now() + '-' + Math.random().toString(36).substr(2, 9), // 臨時的本地ID
    chatRoomId: chatRoomId.value,
    sender: sender,
    messageContent: content,
    sentTime: new Date().toISOString(),
    messageType: messageType,
  };
  if (!Array.isArray(messages.value)) {
    messages.value = [];
  }
  messages.value.push(newMessage);
  nextTick(scrollToBottom);
};
const showOrderSearch = () => {
  // 檢查是否正在等待客服回覆，如果是則不執行
  if (isAwaitingBotResponse.value || isInteractionBlocked.value) {
    console.log("客服正在回覆中或互動被阻擋，請稍候。");
    return;
  }
  console.log('顯示訂單搜尋');
  sendMessageLocally("我點擊了「新訂動態」。");
};


const addMessageToChat = (messageDto) => {
  if (!messageDto || !messageDto.messageId) {
    console.warn("無法添加訊息，缺少 messageId 或數據不完整。", messageDto);
    return;
  }

  let messageContent = messageDto.messageContent;
  let finalMessageType = messageDto.messageType;

  if (finalMessageType === 'quick_questions' && typeof messageContent === 'string') {
    try {
      messageContent = JSON.parse(messageContent);
    } catch (e) {
      console.error("解析 quick_questions 的 messageContent 失敗:", messageDto.messageContent, e);
      messageContent = "加載此快速問題模板失敗。";
      finalMessageType = 'text';
    }
  } else if (finalMessageType === 'quick_questions' && !Array.isArray(messageContent)) {
    console.warn("quick_questions 的 messageContent 格式不符合預期 (非字符串且非陣列):", messageContent);
  }

  const finalMessage = {
    messageId: messageDto.messageId,
    chatRoomId: chatRoomId.value,
    sender: messageDto.sender,
    messageContent: messageContent,
    sentTime: messageDto.sentTime || new Date().toISOString(),
    messageType: finalMessageType,
  };
  messages.value.push(finalMessage);
  nextTick(scrollToBottom);
};



const formatTime = (timeStr) => {
  if (!timeStr) return '';
  try {
    return new Date(timeStr).toLocaleTimeString('zh-TW', { hour: '2-digit', minute: '2-digit' });
  } catch (e) {
    console.error("無效的日期時間字串:", timeStr, e);
    return '';
  }
};

// API 相關方法
const fetchExistingFeedbacks = async () => {
  if (!chatRoomId.value) {
    console.warn("[Fetch Feedback] chatRoomId 為空，無法加載評點。");
    return;
  }
  console.log(`[Fetch Feedback] 嘗試從後端獲取 chatRoomId: ${chatRoomId.value} 的評點`);
  try {
    const response = await axios.get(`/api/feedback/chatroom/${chatRoomId.value}`);
    const fetchedFeedbacks = response.data.feedbacks;

    for (const key in feedbackGiven) {
      delete feedbackGiven[key];
    }
    Object.assign(feedbackGiven, fetchedFeedbacks);

    console.log('[Fetch Feedback] Existing feedbacks loaded:', feedbackGiven);
  } catch (error) {
    console.error('[Fetch Feedback] Error fetching existing feedbacks:', error.response ? error.response.data : error.message);
  }
};
const loadMessages = async () => {
  if (!chatRoomId.value) {
    console.warn("[Load Messages] chatRoomId 為空，無法加載訊息。");
    messages.value = [];
    return;
  }
  console.log(`[Load Messages] 嘗試從後端獲取 chatRoomId: ${chatRoomId.value} 的訊息`);
  try {
    const res = await axios.get(`/api/customer-service/messages/${chatRoomId.value}`);
    if (Array.isArray(res.data)) {
      messages.value = res.data.map(msg => {
        if (msg.messageType === 'quick_questions' && typeof msg.messageContent === 'string') {
          try {
            return { ...msg, messageContent: JSON.parse(msg.messageContent) };
          } catch (e) {
            console.error("解析 quick_questions 的 messageContent 失敗:", msg.messageContent, e);
            return { ...msg, messageType: 'text', messageContent: "加載此快速問題模板失敗。" };
          }
        }
        return msg;
      });
    } else {
      console.warn("[Load Messages] 後端 /api/customer-service/messages/{id} 返回的數據不是陣列:", res.data);
      messages.value = [];
    }
    await nextTick();
    scrollToBottom();

    await fetchExistingFeedbacks();

  } catch (e) {
    console.error('[Load Messages] 加載訊息失敗', e);
    messages.value = [];
    sendMessageLocally('加載歷史訊息失敗，請檢查網路連線。', 'bot');
  }
};

const initChatroom = async () => {
  if (!selectedTypeId.value) {
    console.warn("[Init Chatroom] 未選擇主題類型，無法初始化聊天室。");
    return;
  }

  console.log(`[Init Chatroom] 啟動，嘗試為 typeId: ${selectedTypeId.value} 處理聊天室`);

  messages.value = [];
  for (const key in feedbackGiven) {
    delete feedbackGiven[key];
  }

  // 在初始化聊天室時，確保用戶可以操作
  isAwaitingBotResponse.value = false;
  if (chatRooms[selectedTypeId.value]) {
    chatRoomId.value = chatRooms[selectedTypeId.value];
    console.log(`[Init Chatroom] 從前端快取獲取現有聊天室 ID: ${chatRoomId.value} for typeId: ${selectedTypeId.value}`);
  } else {
    console.log(`[Init Chatroom] 前端快取無此主題聊天室，呼叫後端 API 獲取/創建，typeId: ${selectedTypeId.value}`);
    try {
      const res = await axios.post('/api/customer-service/chatroom', { typeId: selectedTypeId.value });
      chatRoomId.value = res.data.chatRoomId;
      chatRooms[selectedTypeId.value] = chatRoomId.value;
      console.log(`[Init Chatroom] 後端返回聊天室 ID: ${chatRoomId.value} for typeId: ${selectedTypeId.value}`);
    } catch (e) {
      console.error('[Init Chatroom] 建立/獲取聊天室失敗', e);
      sendMessageLocally('無法建立/獲取聊天室，請稍後再試。', 'bot');
      chatRoomId.value = null;
      return;
    }
  }

  await loadMessages();
};

const loadTemplates = async () => {
  isLoading.value = true; // 開始載入模板時顯示載入動畫
  console.log('[Load Templates] 開始加載問題模板...');
  try {
    const res = await axios.get('/api/customer-service/question-templates');
    questions.value = res.data;
    const types = [...new Map(res.data.map(q => [q.type.typeId, q.type])).values()];
    questionTypes.value = types;
    if (types.length > 0 && selectedTypeId.value === null) {
      const defaultTopic = types.find(type => type.typeName === '機票') || types[0];
      selectedTypeId.value = defaultTopic.typeId;
      selectedTopicName.value = defaultTopic.typeName;
      console.log(`[Load Templates] 設定預設主題: ${selectedTopicName.value} (ID: ${selectedTypeId.value})`);
      await initChatroom();
    } else {
      console.log('[Load Templates] 已有主題或無可用主題。');
      if (selectedTypeId.value) {
        console.log(`[Load Templates] selectedTypeId.value 已有值 (${selectedTypeId.value})，觸發 initChatroom。`);
        await initChatroom();
      }
    }
  } catch (e) {
    console.error('[Load Templates] 加載問題模板失敗', e);
    sendMessageLocally('無法加載問題模板，請稍後再試。', 'bot');
  } finally {
    setTimeout(() => {
      isLoading.value = false;
    }, 500);
  }
};

const sendQuestion = async (q) => {
  // 檢查是否正在等待客服回覆或互動被阻擋
  if (isAwaitingBotResponse.value || isInteractionBlocked.value) {
    console.log("客服正在回覆中或互動被阻擋，請稍候。");
    return;
  }
  messageText.value = q.questionTemplate;
  await sendMessage(q.questionTemplateId);

  //  點擊快速問題模板後，關閉表情符號面板並解除互動禁用
  showEmojiPanel.value = false;
  isInteractionBlocked.value = false;
};

const sendMessage = async (questionId = null) => {
  const trimmedMessage = messageText.value.trim();
  if (!trimmedMessage || !chatRoomId.value) {
    console.warn("訊息內容為空或聊天室 ID 不存在。");
    sendMessageLocally('請先選擇一個主題來開始聊天。', 'bot');
    return;
  }
  // 檢查互動是否被阻擋
  if (isInteractionBlocked.value) {
    console.log("互動被阻擋，無法發送訊息。");
    return;
  }

  // 在發送訊息前，將狀態設為等待中
  isAwaitingBotResponse.value = true;

  // 1. 將用戶訊息立即顯示在聊天室中
  sendMessageLocally(trimmedMessage, 'user');
  messageText.value = '';
  let replyTypingId = null;
  let quickTypingId = null;

  try {
    let finalQuestionId = null;
    if (typeof questionId === 'number' || (typeof questionId === 'string' && !isNaN(parseInt(questionId)))) {
      finalQuestionId = parseInt(questionId);
    } else if (questionId && typeof questionId === 'object' && questionId.questionTemplateId) {
      finalQuestionId = parseInt(questionId.questionTemplateId);
    }

    let finalTypeId = selectedTypeId.value;

    const req = {
      chatRoomId: chatRoomId.value,
      content: trimmedMessage,
      questionId: finalQuestionId,
      typeId: finalTypeId
    };
    console.log("[Send Message] 準備發送訊息請求，Payload:", req);

    // 顯示主要回覆的「打字中」動畫
    replyTypingId = 'typing-reply-' + Date.now();
    messages.value.push({ messageId: replyTypingId, chatRoomId: chatRoomId.value, sender: 'bot', messageContent: '', sentTime: new Date().toISOString(), messageType: 'typing' });
    scrollToBottom();
    const res = await axios.post('/api/customer-service/message', req);

    const botReplyDto = res.data;
    if (botReplyDto && botReplyDto.sender === 'bot' && botReplyDto.messageId) {
      setTimeout(() => {
        // 移除主要回覆的「打字中」動畫
        const idx = messages.value.findIndex(m => m.messageId === replyTypingId);
        if (idx !== -1) messages.value.splice(idx, 1);
        addMessageToChat(botReplyDto);

        // 顯示「您可能要問」的「打字中」動畫
        quickTypingId = 'typing-quick-' + Date.now();
        messages.value.push({ messageId: quickTypingId, chatRoomId: chatRoomId.value, sender: 'bot', messageContent: '', sentTime: new Date().toISOString(), messageType: 'typing' });
        scrollToBottom();

        setTimeout(() => {
          // 移除「您可能要問」的「打字中」動畫
          const idx2 = messages.value.findIndex(m => m.messageId === quickTypingId);
          if (idx2 !== -1) messages.value.splice(idx2, 1);

          const quickQuestionsLocalMessageId = 'quick-questions-local-' + Date.now() + '-' + Math.random().toString(36).substr(2, 9);

          const defaultQuickQuestionsData = filteredQuestions.value
            .slice(0, 4)
            .map(q => ({
              questionTemplateId: q.questionTemplateId,
              questionTemplate: q.questionTemplate
            }));

          addMessageToChat({
            messageId: quickQuestionsLocalMessageId,
            chatRoomId: chatRoomId.value,
            sender: 'bot',
            messageContent: defaultQuickQuestionsData,
            sentTime: new Date().toISOString(),
            messageType: 'quick_questions',
          });

          console.log("「您可能要問」訊息已獨立顯示，ID為:", quickQuestionsLocalMessageId);
          // 兩個延遲都完成後，才解除等待狀態
          isAwaitingBotResponse.value = false;
          // 訊息發送成功後，關閉表情符號面板並解除互動禁用
          showEmojiPanel.value = false;
          isInteractionBlocked.value = false;

        }, 2000); // 「您可能要問」的打字動畫持續時間
      }, 2000); // 主要回覆的打字動畫持續時間
    } else {
      messages.value = messages.value.filter(m => m.messageId !== replyTypingId);
      sendMessageLocally('客服未能提供有效回覆，請稍後再試。', 'bot');
      // 如果客服回覆不符合預期，也要解除等待狀態
      isAwaitingBotResponse.value = false;
      // 即使出錯，也關閉表情符號面板並解除互動禁用
      showEmojiPanel.value = false;
      isInteractionBlocked.value = false;
    }

  } catch (e) {
    console.error('[Send Message] 發送訊息失敗或獲取回覆失敗', e);
    // 確保即使出錯也移除所有相關的打字動畫
    messages.value = messages.value.filter(m => !m.messageId.startsWith('typing-'));
    sendMessageLocally('網路錯誤或服務異常，請檢查您的連線。', 'bot');
    // 錯誤發生時，解除等待狀態
    isAwaitingBotResponse.value = false;
    // 錯誤發生時，關閉表情符號面板並解除互動禁用
    showEmojiPanel.value = false;
    isInteractionBlocked.value = false;
  } finally {
    await nextTick();
    scrollToBottom();
  }
};

// 事件處理器
const selectTopic = async (topic) => {
  isLoading.value = true; // 選擇主題時顯示載入動畫
  selectedTypeId.value = topic.typeId;
  selectedTopicName.value = topic.typeName;
  showTopicSelector.value = false;

  console.log("[Select Topic] Selected new topic, initializing chatroom for typeId:", selectedTypeId.value);
  await initChatroom();
  setTimeout(() => {
    isLoading.value = false;
  }, 500);
};


// 生命周期
onMounted(() => {
  console.log('Component mounted. Starting loadTemplates...');
  loadTemplates();
});
const handleFeedback = async (messageId, type) => {
  if (feedbackGiven[messageId]) {
    console.log(`Message ${messageId} already has feedback.`);
    return;
  }

  const isGood = type === 'like';

  try {
    const response = await axios.post('/api/feedback', {
      messageId: messageId,
      isGood: isGood
    });
    console.log('Feedback submitted successfully:', response.data);
    feedbackGiven[messageId] = type;

    const messageToUpdate = messages.value.find(msg => msg.messageId === messageId);
    if (messageToUpdate) {
      messageToUpdate.isBouncing = true;
      setTimeout(() => messageToUpdate.isBouncing = false, 400);
    }
  } catch (error) {
    console.error('Error submitting feedback:', error.response ? error.response.data : error.message);
    alert('提交回饋失敗，請稍後再試。');
  }
};
</script>

<template>
  <div class="chatroom-container">
    <div v-if="isLoading" class="loading-overlay">
      <img src="/image/full_blue.png" alt="Loading Logo" class="blinking-logo" />
      <p class="loading-text">載入中...</p>
    </div>

    <div v-if="showTopicSelector" class="topic-selector-overlay">
      <div class="topic-selector-modal">
        <div class="modal-header">
          <h2>哈囉！</h2>
          <p>請問需要什麼協助呢？</p>
        </div>

        <div class="topic-categories">
          <div class="category-section">
            <div class="topic-grid">
              <div v-for="type in allTopics" :key="type.typeId" class="topic-item" @click="selectTopic(type)">
                <div class="topic-icon">
                  <span v-html="getTopicIcon(type.typeName)"></span>
                </div>
                <span>{{ type.typeName }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="chatroom-body" v-if="!showTopicSelector">
      <div class="chat-section">
        <div v-if="showEmojiPanel" class="interaction-overlay" @click="closeEmojiPanelAndUnblock"></div>

        <div class="chat-header">
          <h3>{{ selectedTopicName }}聊天室</h3>
        </div>

        <div class="message-list" ref="messageList">
          <template v-if="messages.length === 0">
            <div class="welcome-message">
              <div class="bot-avatar">
                <img src="/image/full_blue.png" alt="客服頭像" class="bot-avatar-img" />
              </div>
              <div class="message-content">
                <div class="message-bubble bot-message">
                  <p>您好，Journey.com 會員，很高興能為您服務。</p>
                  <span class="message-time">{{ getCurrentTime() }}</span>
                </div>
              </div>
            </div>

            <div class="service-info" v-if="filteredQuestions.length > 0">
              <div class="bot-avatar">
                <img src="/image/full_blue.png" alt="客服頭像" class="bot-avatar-img" />
              </div>
              <div class="message-content">
                <div class="message-bubble bot-message">
                  <div class="topic-questions">
                    <p>您可能要問</p>
                    <div class="quick-questions">
                      <div v-for="qItem in filteredQuestions.slice(0, 4)" :key="qItem.questionTemplateId"
                        class="quick-question-item"
                        :class="{ 'disabled': isAwaitingBotResponse || isInteractionBlocked }"
                        @click="sendQuestion(qItem)">
                        {{ qItem.questionTemplate }} <span class="arrow">></span>
                      </div>
                    </div>
                    <!-- <div class="additional-options">
                      <div class="option-item" :class="{ 'disabled': isAwaitingBotResponse || isInteractionBlocked }"
                        @click="showOrderSearch">
                        <span class="option-icon">🔍</span>
                        新訂動態
                      </div>
                    </div> -->
                  </div>
                </div>
              </div>
            </div>
          </template>

          <div v-for="msg in messages" :key="msg.messageId" class="message-wrapper"
            :class="{ 'from-user': msg.sender === 'user', 'from-bot': msg.sender === 'bot' }">
            <div class="user-avatar" v-if="msg.sender === 'user'">
              <span>我</span>
            </div>
            <div class="bot-avatar" v-else-if="msg.sender === 'bot'">
              <img src="/image/full_blue.png" alt="客服頭像" class="bot-avatar-img" />
            </div>
            <div class="message-content">
              <template v-if="msg.messageType === 'typing'">
                <div class="typing-bubble">
                  <span class="dot"></span>
                  <span class="dot"></span>
                  <span class="dot"></span>
                </div>
              </template>

              <template v-else>
                <div :class="['message-bubble', msg.sender === 'user' ? 'user-message' : 'bot-message']">
                  <template v-if="msg.messageType === 'quick_questions' && msg.sender === 'bot'">
                    <div class="topic-questions">
                      <p>您可能要問</p>
                      <div class="quick-questions">
                        <div v-for="qItem in msg.messageContent" :key="qItem.questionTemplateId"
                          class="quick-question-item"
                          :class="{ 'disabled': isAwaitingBotResponse || isInteractionBlocked }"
                          @click="sendQuestion(qItem)">
                          {{ qItem.questionTemplate }} <span class="arrow">></span>
                        </div>
                      </div>
                      <!-- <div class="additional-options">
                        <div class="option-item" :class="{ 'disabled': isAwaitingBotResponse || isInteractionBlocked }"
                          @click="showOrderSearch">
                          <span class="option-icon">🔍</span>
                          新訂動態
                        </div>
                      </div> -->
                    </div>
                  </template>

                  <template v-else>
                    <p>{{ msg.messageContent }}</p>
                  </template>

                  <span class="message-time">{{ formatTime(msg.sentTime) }}</span>
                </div>
                <div v-if="msg.sender === 'bot' && msg.messageType !== 'quick_questions'">
                  <div class="feedback-buttons" v-if="!feedbackGiven[msg.messageId]">
                    <span class="feedback-icon like" :data-id="msg.messageId"
                      @click="handleFeedback(msg.messageId, 'like')" :class="{ 'bounce': msg.isBouncing }">👍</span>
                    <span class="feedback-icon dislike" :data-id="msg.messageId"
                      @click="handleFeedback(msg.messageId, 'dislike')" :class="{ 'bounce': msg.isBouncing }">👎</span>
                  </div>

                  <div class="feedback-buttons feedback-given" v-else>
                    <span :class="[
                      'feedback-icon',
                      {
                        'selected': feedbackGiven[msg.messageId] === 'like',
                        'inactive': feedbackGiven[msg.messageId] === 'dislike',
                        'bounce': msg.isBouncing
                      }
                    ]">👍</span>
                    <span :class="[
                      'feedback-icon',
                      {
                        'selected': feedbackGiven[msg.messageId] === 'dislike',
                        'inactive': feedbackGiven[msg.messageId] === 'like',
                        'bounce': msg.isBouncing
                      }
                    ]">👎</span>
                  </div>
                </div>

              </template>
            </div>
          </div>
        </div>


        <div class="input-section">
          <div v-if="showEmojiPanel" class="emoji-panel">
            <span v-for="emoji in emojis" :key="emoji" class="emoji-item" @click="insertEmoji(emoji)">
              {{ emoji }}
            </span>
          </div>

          <div class="input-wrapper">
            <button class="emoji-btn" @click="toggleEmojiPanel" :disabled="isAwaitingBotResponse">😊</button>
            <input v-model="messageText" type="text" placeholder="請輸入您的問題" @keyup.enter="sendMessage"
              class="message-input" :disabled="isAwaitingBotResponse || isInteractionBlocked" />
            <button @click="sendMessage" class="send-button" :disabled="isSendDisabled">傳送</button>
          </div>
        </div>
      </div>

    </div>

  </div>
</template>

<style scoped>
.chatroom-container {
  position: relative;
  min-height: 100vh;
  background-image: url('/image/spot.webp');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  display: flex;
  flex-direction: column;
}

.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(255, 255, 255, 0.9);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 9999;
  backdrop-filter: blur(5px);
}

.blinking-logo {
  width: 150px;
  height: auto;
  margin-bottom: 20px;
  animation: blink-animation 1.5s infinite alternate;
}

@keyframes blink-animation {
  0% {
    opacity: 0.2;
  }

  100% {
    opacity: 1;
  }

}

.loading-text {
  color: #333;
  font-size: 1.8em;
  font-weight: bold;
}


.go-home-button {
  padding: 8px 15px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1em;
  transition: background-color 0.3s ease;
}

.go-home-button:hover {
  background-color: #0056b3;
}

.chatroom-header {
  background: white;
  padding: 1rem 2rem;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1200px;
  margin: 0 auto;
}

.trip-logo {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.logo-circle {
  width: 50px;
  height: 50px;
  background: #6c5ce7;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
  font-size: 14px;
}

.trip-logo h2 {
  margin: 0;
  color: #2d3436;
  font-size: 1.5rem;
}

.qr-section {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.download-text {
  color: #636e72;
  font-size: 0.9rem;
}

.qr-code {
  width: 60px;
  height: 60px;
  background: #f8f9fa;
  border: 2px solid #dee2e6;
  border-radius: 8px;
  position: relative;
}

.qr-code::after {
  content: "QR";
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: #adb5bd;
  font-size: 12px;
}

.chatroom-body {
  max-width: 100vw;
  padding: 2rem;
  display: flex;
  justify-content: center;
}

.chat-section {
  margin-top: 120px;
  width: 100%;
  max-width: 800px;
  height: 800px;
  background: #fff;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  position: relative;
  margin-left: auto;
  margin-right: auto;
}

.chat-header {
  background: #e4d2e7;
  padding: 1.5rem;
  text-align: center;
  border-bottom: 1px solid #e9ecef;
  margin: 0;
  flex-shrink: 0;
}

.chat-header h3 {
  margin: 0;
  color: #040214;
  font-size: 2rem;
}

.question-type-selector {
  padding: 1rem 1.5rem;
  border-bottom: 1px solid #e9ecef;
}

.type-select {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #dee2e6;
  border-radius: 8px;
  font-size: 1rem;
}

.question-templates {
  padding: 1rem 1.5rem;
  border-bottom: 1px solid #e9ecef;
}

.template-item {
  background: #f1f3f4;
  padding: 0.75rem 1rem;
  margin: 0.5rem 0;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 0.9rem;
}

.template-item:hover {
  background: #6c5ce7;
  color: white;
  transform: translateY(-2px);
}

.message-list {
  flex: 1;
  overflow-y: auto;
  background: #cad4df;
  padding: 1.5rem 1.5rem 0 1.5rem;
  margin: 0;
  min-height: 0;
}



.welcome-message,
.service-info,
.message-wrapper {
  display: flex;
  margin-bottom: 1.5rem;
  align-items: flex-start;
  gap: 1rem;
}

.bot-avatar,
.user-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: bold;
  flex-shrink: 0;
}

.bot-avatar {
  background: #e1cab0;
}

.user-avatar {
  background: #33bbd3;
  color: white;
  order: 2;
}

.message-wrapper .user-avatar~.message-content {
  order: 1;
}

.message-content {
  flex: 1;
  max-width: 70%;
}

.message-bubble {
  padding: 1rem 1.25rem;
  border-radius: 18px;
  position: relative;
  word-wrap: break-word;
  font-size: larger;
}

.bot-message {
  background: rgb(230, 227, 187);
  border: 1px solid #e9ecef;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.user-message {
  background: #9be499;
  color: white;
  margin-left: auto;
}

.message-bubble p {
  margin: 0 0 0.5rem 0;
  line-height: 1.5;
  color: #2d3436;
  font-size: 1.2rem;
}

.message-time {
  font-size: 0.75rem;
  color: black;
  opacity: 0.7;
  display: block;
}

.order-suggestion {
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px solid #e9ecef;
}

.search-order-btn {
  background: #6c5ce7;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 25px;
  cursor: pointer;
  font-weight: 500;
  margin-top: 1rem;
  transition: all 0.3s ease;
}

.search-order-btn:hover {
  background: #5f3dc4;
  transform: translateY(-2px);
}

.input-section {
  padding: 1.5rem 1.5rem 1.5rem 1.5rem;
  border-top: 1px solid #e9ecef;
  background: rgb(223, 207, 207);
  position: relative;
  margin: 0 !important;
  flex-shrink: 0;
}

.input-wrapper {
  display: flex;
  gap: 1rem;
}

.message-input {
  flex: 1;
  padding: 1rem 1.25rem;
  border: 1px solid #dee2e6;
  border-radius: 25px;
  font-size: 1rem;
  outline: none;
  transition: border-color 0.3s ease;
}

.message-input:focus {
  border-color: #6c5ce7;
}

.send-button {
  background: #6c5ce7;
  color: white;
  border: none;
  padding: 1rem 2rem;
  border-radius: 25px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s ease;
}

.send-button:hover {
  background: #5f3dc4;
  transform: translateY(-2px);
}

.input-actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
}

.emoji-btn,
.attach-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 50%;
  transition: background 0.3s ease;
}

.emoji-btn:hover,
.attach-btn:hover {
  background: #f1f3f4;
}

.order-section {
  flex: 1;
  background: white;
  border-radius: 20px;
  padding: 2rem;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
  height: fit-content;
}

.order-header h3 {
  margin: 0 0 1.5rem 0;
  color: #2d3436;
  font-size: 1.3rem;
}

.order-list {
  border: 1px solid #e9ecef;
  border-radius: 12px;
  padding: 1.5rem;
  margin-bottom: 1rem;
  position: relative;
}

.order-status {
  position: absolute;
  top: 1rem;
  left: 1rem;
  font-size: 0.8rem;
  font-weight: 500;
}

.order-status.cancelled {
  color: #e17055;
}

.order-price {
  position: absolute;
  top: 1rem;
  right: 1rem;
  font-weight: bold;
  color: #2d3436;
  font-size: 1.1rem;
}

.order-details {
  margin-top: 2rem;
}

.order-details h4 {
  margin: 0 0 0.5rem 0;
  color: #2d3436;
}

.order-date {
  color: #636e72;
  margin: 0 0 1rem 0;
  font-size: 0.9rem;
}

.order-actions {
  display: flex;
  gap: 1rem;
}

.detail-btn,
.rebooking-btn {
  padding: 0.5rem 1rem;
  border-radius: 6px;
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.detail-btn {
  background: #f8f9fa;
  border: 1px solid #dee2e6;
  color: #495057;
}

.rebooking-btn {
  background: #6c5ce7;
  border: 1px solid #6c5ce7;
  color: white;
}

.detail-btn:hover {
  background: #e9ecef;
}

.rebooking-btn:hover {
  background: #5f3dc4;
}

.show-all {
  text-align: center;
  margin: 1.5rem 0;
}

.show-all-btn {
  background: none;
  border: none;
  color: #6c5ce7;
  cursor: pointer;
  font-size: 1rem;
  transition: color 0.3s ease;
}

.show-all-btn:hover {
  color: #5f3dc4;
}

.trust-section {
  margin-top: 2rem;
  padding-top: 2rem;
  border-top: 1px solid #e9ecef;
}

.trust-section h3 {
  margin: 0 0 1.5rem 0;
  color: #2d3436;
  font-size: 1.2rem;
}

.trust-features {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.feature-icon {
  width: 24px;
  height: 24px;
  background: #00b894;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: bold;
}

.feature-item span {
  color: #2d3434;
  font-size: 0.95rem;
}

/* 主題選擇彈窗樣式 */
.topic-selector-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url('/image/spot.webp');
  background-size: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.topic-selector-modal {
  background: rgb(244, 225, 225);
  border-radius: 20px;
  padding: 4rem;
  max-width: 900px;
  width: 95%;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.modal-header {
  text-align: center;
  margin-bottom: 2rem;
}

.modal-header h2 {
  color: #2d3436;
  font-size: 2.5rem;
  margin: 0 0 1rem 0;
}

.modal-header p {
  color: #636e72;
  font-size: 1.3rem;
  margin: 0;
}

.topic-categories {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.category-section h3 {
  color: #2d3436;
  font-size: 1.3rem;
  margin: 0 0 1rem 0;
}

.topic-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  /* 一排三個 */
  gap: 2rem;
  /* 格子間距加大 */
}

.topic-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 2rem 1rem;
  border: 2px solid #00080f;
  border-radius: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  text-align: center;
}

.topic-item:hover {
  border-color: #6c5ce7;
  background: #31aed4;
  transform: translateY(-5px);
  box-shadow: 0 10px 30px rgba(108, 92, 231, 0.2);
}

.topic-icon {
  font-size: 5rem;
  margin-bottom: 1rem;
  display: flex;
  align-items: center;
  justify-content: center;
}

.topic-icon span {
  display: inline-block;
  font-size: 3rem !important;
  line-height: 1;
}

.topic-item span:last-child {
  color: #2d3436;
  font-weight: 600;
  font-size: 1.3rem;
  margin-top: 0.5rem;
}

/* 快速問題樣式 */
.topic-questions {
  width: 100%;
}

.topic-questions>p {
  font-weight: 500;
  margin-bottom: 1rem;
  color: #2d3436;
  font-size: 1.2rem;
}

.quick-questions {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.quick-question-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem 1rem;
  background: #f1f1ed;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 1.2rem;
  color: #2d3436;
}

.quick-question-item:hover:not(.disabled) {
  /* 禁用狀態下不顯示 hover 效果 */
  background: #92bfec;
}

.arrow {
  color: #6c5ce7;
  font-weight: bold;
}

.additional-options {
  padding-top: 1rem;
  border-top: 1px solid #e9ecef;
}

.option-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1rem;
  background: #f1f3f4;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 0.9rem;
}

.option-item:hover:not(.disabled) {
  /* 禁用狀態下不顯示 hover 效果 */
  background: #e9ecef;
}

.option-icon {
  font-size: 1rem;
}

@media (max-width: 768px) {
  .chatroom-body {
    flex-direction: column;
    padding: 1rem;
  }

  .header-content {
    flex-direction: column;
    gap: 1rem;
    text-align: center;
  }

  .qr-section {
    order: -1;
  }
}

.go-home-button {
  background-color: #3498db;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 6px;
  cursor: pointer;
  font-weight: bold;
}

.go-home-button:hover {
  background-color: #2980b9;
}

/* 左邊對齊（客服） */
.from-bot {
  justify-content: flex-start;
}

/* 右邊對齊（使用者） */
.from-user {
  justify-content: flex-end;
}

.emoji-panel {
  display: flex;
  flex-wrap: wrap;
  padding: 8px;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
  max-width: 250px;
  max-height: 180px;
  overflow-y: auto;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);

  /* 定位調整 */
  position: absolute;
  bottom: 90px;
  left: 0px;
  right: auto;
  z-index: 10;
}

.emoji-item {
  font-size: 20px;
  padding: 6px;
  cursor: pointer;
  transition: transform 0.2s;
}

.emoji-item:hover {
  transform: scale(1.2);
}

.bot-message:contains("⋯⋯") {
  font-style: italic;
  color: #999;
  animation: blink 1s infinite;
}

@keyframes blink {
  0% {
    opacity: 1
  }

  50% {
    opacity: 0.3
  }

  100% {
    opacity: 1
  }
}

.typing-bubble {
  display: flex;
  align-items: center;
  justify-content: start;
  background-color: #f0f0f0;
  border-radius: 18px;
  padding: 8px 12px;
  width: 50px;
  height: 26px;
}

.typing-bubble .dot {
  width: 6px;
  height: 6px;
  margin: 0 2px;
  border-radius: 50%;
  background-color: #999;
  animation: blink 1.2s infinite;
}

.typing-bubble .dot:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-bubble .dot:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes blink {
  0% {
    opacity: 0.2;
  }

  20% {
    opacity: 1;
  }

  100% {
    opacity: 0.2;
  }
}

.feedback-buttons {
  display: flex;
  gap: 10px;
  margin-top: 5px;
  justify-content: flex-end;
  padding-right: 15px;
}

.message-wrapper.from-bot .feedback-buttons {
  justify-content: flex-start;
  padding-left: 15px;
  padding-right: 0;
}


.feedback-result {
  margin-top: 4px;
  display: flex;
  gap: 10px;
}

/* Feedback Icons */
.feedback-icon {
  font-size: 1.2em;
  cursor: pointer;
  opacity: 0.6;
  transition: opacity 0.3s ease, transform 0.3s ease, box-shadow 0.3s ease;
  user-select: none;
}

/* Feedback Buttons Container */
.feedback-buttons {
  display: flex;
  gap: 10px;
  margin-top: 5px;
  justify-content: flex-end;
  padding-right: 15px;
}

.message-wrapper.from-bot .feedback-buttons {
  justify-content: flex-start;
  padding-left: 15px;
  padding-right: 0;
}

/* Feedback Icons - 基礎樣式 */
.feedback-icon {
  font-size: 1.2em;
  opacity: 0.6;
  transition: opacity 0.3s ease, transform 0.3s ease, box-shadow 0.3s ease;
  user-select: none;
}

/* 未點擊前，可互動的狀態 */
/* 只有在尚未給予回饋時，才顯示 pointer 鼠標和 hover 效果 */
.feedback-buttons:not(.feedback-given) .feedback-icon {
  cursor: pointer;
}

.feedback-buttons:not(.feedback-given) .feedback-icon:hover {
  opacity: 1;
}


/* 點擊後，選中的圖示 (發光並恆亮) */
.feedback-icon.selected {
  opacity: 1;
  transform: scale(1.1);
  cursor: default;
}

/* 點擊後，未選中的圖示 (變灰且失去互動) */
.feedback-icon.inactive {
  opacity: 0.3;
  cursor: default;
  pointer-events: none;
}


.feedback-buttons.feedback-given .feedback-icon {
  cursor: default !important;
  pointer-events: none;
}

.interaction-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0);
  z-index: 9;
  cursor: default;
}

/* 禁用元素的樣式 */
.message-input:disabled,
.send-button:disabled {
  cursor: not-allowed;
  background-color: #e9ecef;
  color: #6c757d;
  border-color: #ced4da;
}

.quick-question-item.disabled,
.option-item.disabled {
  cursor: not-allowed;
  opacity: 0.6;
  background-color: #f1f3f4;
  pointer-events: none;
}

.quick-question-item.disabled:hover,
.option-item.disabled:hover {
  background-color: #f1f3f4;
}

.bot-avatar-img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
}

/* Bounce Animation */
@keyframes bounce {

  0%,
  100% {
    transform: scale(1);
  }

  50% {
    transform: scale(1.5);
    /* 增加按評點後彈跳的高度，從 1.3 改為 1.5 */
  }
}

.feedback-icon.bounce {
  animation: bounce 0.4s;
  /* 稍微縮短動畫時間，讓它更快完成，看起來更急促 */
}
</style>