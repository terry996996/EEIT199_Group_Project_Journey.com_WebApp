package journey.controller.onlinecustomerservice;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import journey.domain.onlinecustomerservice.CustomerServiceChatroomBean;
import journey.dto.ChatroomRequestDTO;
import journey.dto.MessageRequestDTO;
import journey.dto.MessageResponseDTO;
import journey.dto.QuestionTemplateDTO;
import journey.service.CustomerServiceService;

@RestController
@RequestMapping("/api/customer-service")
public class CustomerServiceController {

    @Autowired
    private CustomerServiceService customerService;

    // 獲取(查出)或創建聊天室
    @PostMapping("/chatroom")
    public ResponseEntity<Map<String, Integer>> getOrCreateChatroom(@RequestBody ChatroomRequestDTO request) {
        try {
            CustomerServiceChatroomBean chatroom = customerService.getOrCreateChatroom(request.getTypeId());
            return ResponseEntity.ok(Map.of("chatRoomId", chatroom.getChatRoomId()));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", -1));
        }
    }

    // 獲取(查出)特定聊天室內的所有訊息
    @GetMapping("/messages/{chatRoomId}")
    public ResponseEntity<List<MessageResponseDTO>> getChatroomMessages(@PathVariable Integer chatRoomId) {
        try {
            List<MessageResponseDTO> messages = customerService.getChatroomMessages(chatRoomId);
            return ResponseEntity.ok(messages);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 發送訊息
    @PostMapping("/message")
    public ResponseEntity<MessageResponseDTO> sendMessage(@RequestBody MessageRequestDTO request) {
        try {
            MessageResponseDTO response = customerService.sendMessage(
                    request.getChatRoomId(),
                    request.getContent(),
                    request.getQuestionId(),
                    request.getTypeId() // 傳遞 typeId 給 Service
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // 獲取(查出)所有問題模板
    @GetMapping("/question-templates")
    public ResponseEntity<List<QuestionTemplateDTO>> getAllQuestionTemplates() {
        List<QuestionTemplateDTO> templates = customerService.getAllQuestionTemplates();
        return ResponseEntity.ok(templates);
    }
}