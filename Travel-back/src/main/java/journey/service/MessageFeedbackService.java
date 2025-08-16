package journey.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import journey.domain.onlinecustomerservice.CustomerServiceMessageBean;
import journey.domain.onlinecustomerservice.MessageFeedbackBean;
import journey.dto.ChatroomFeedbacksDTO;
import journey.dto.MessageFeedbackDTO;
import journey.repository.onlinecustomerservice.CustomerServiceMessageRepository;
import journey.repository.onlinecustomerservice.MessageFeedbackRepository;

@Service
public class MessageFeedbackService {

    private final MessageFeedbackRepository messageFeedbackRepository;
    private final CustomerServiceMessageRepository customerServiceMessageRepository;

    @Autowired
    public MessageFeedbackService(MessageFeedbackRepository messageFeedbackRepository,
            CustomerServiceMessageRepository customerServiceMessageRepository) {
        this.messageFeedbackRepository = messageFeedbackRepository;
        this.customerServiceMessageRepository = customerServiceMessageRepository;
    }

    @Transactional
    public MessageFeedbackBean saveOrUpdateMessageFeedback(MessageFeedbackDTO feedbackDTO) {
        Integer messageId = feedbackDTO.getMessageId();
        Boolean isGood = feedbackDTO.getIsGood();

        CustomerServiceMessageBean message = customerServiceMessageRepository.findById(messageId)
                .orElseThrow(() -> new EntityNotFoundException("Message not found with ID: " + messageId));

        Optional<MessageFeedbackBean> existingFeedback = messageFeedbackRepository.findByMessage_MessageId(messageId);

        MessageFeedbackBean feedback;
        if (existingFeedback.isPresent()) {
            feedback = existingFeedback.get();
            feedback.setIsGood(isGood);
            feedback.setFeedbackAt(LocalDateTime.now());
            System.out.println("Updating existing feedback for message ID: " + messageId + " to isGood: " + isGood);
        } else {
            feedback = new MessageFeedbackBean();
            feedback.setMessage(message);
            feedback.setIsGood(isGood);
            feedback.setFeedbackAt(LocalDateTime.now());
            System.out.println("Saving new feedback for message ID: " + messageId + " with isGood: " + isGood);
        }

        return messageFeedbackRepository.save(feedback);
    }

    /**
     * 獲取指定聊天室中所有訊息的評點狀態。
     * 
     * @param chatRoomId 聊天室 ID。
     * @return 包含聊天室 ID 和一個訊息 ID 到評點類型 ("like" 或 "dislike") 的 Map。
     */
    @Transactional(readOnly = true)
    public ChatroomFeedbacksDTO getFeedbacksForChatroom(Integer chatRoomId) {
        // 從資料庫查詢該聊天室所有訊息的評點
        List<MessageFeedbackBean> feedbacks = messageFeedbackRepository.findByMessage_ChatRoom_ChatRoomId(chatRoomId);

        // 將查詢結果轉換為前端需要的 Map 格式
        Map<Integer, String> feedbackMap = new HashMap<>();
        for (MessageFeedbackBean feedback : feedbacks) {
            String feedbackType = feedback.getIsGood() ? "like" : "dislike";
            feedbackMap.put(feedback.getMessage().getMessageId(), feedbackType);
        }
        return new ChatroomFeedbacksDTO(chatRoomId, feedbackMap);
    }
}
