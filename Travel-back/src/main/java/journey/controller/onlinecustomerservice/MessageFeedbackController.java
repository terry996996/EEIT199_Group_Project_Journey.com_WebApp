package journey.controller.onlinecustomerservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import journey.domain.onlinecustomerservice.MessageFeedbackBean;
import journey.dto.ChatroomFeedbacksDTO;
import journey.dto.MessageFeedbackDTO;
import journey.service.MessageFeedbackService;

@RestController
@RequestMapping("/api/feedback")
// @CrossOrigin(origins = "http://localhost:5173")
public class MessageFeedbackController {

    private final MessageFeedbackService messageFeedbackService;

    @Autowired
    public MessageFeedbackController(MessageFeedbackService messageFeedbackService) {
        this.messageFeedbackService = messageFeedbackService;
    }

    // 已存在的 submitMessageFeedback 方法保持不變
    @PostMapping
    public ResponseEntity<?> submitMessageFeedback(@Valid @RequestBody MessageFeedbackDTO feedbackDTO) {
        try {
            MessageFeedbackBean savedFeedback = messageFeedbackService.saveOrUpdateMessageFeedback(feedbackDTO);
            return ResponseEntity.ok(savedFeedback);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to submit feedback: " + e.getMessage());
        }
    }

    /*
     * 根據聊天室 ID 獲取所有訊息的評點狀態。
     * API 路徑範例: GET /api/feedback/chatroom/123
     * 
     * @param chatRoomId 聊天室 ID。
     * 
     * @return 包含訊息評點的 DTO。
     */
    @GetMapping("/chatroom/{chatRoomId}")
    public ResponseEntity<?> getFeedbacksForChatroom(@PathVariable Integer chatRoomId) {
        try {
            ChatroomFeedbacksDTO feedbacks = messageFeedbackService.getFeedbacksForChatroom(chatRoomId);
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve feedbacks: " + e.getMessage());
        }
    }
}
