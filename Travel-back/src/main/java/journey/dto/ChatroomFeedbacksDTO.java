package journey.dto;

import java.util.Map;

public class ChatroomFeedbacksDTO {
    private Integer chatRoomId;
    private Map<Integer, String> feedbacks; // "like" 或 "dislike"

    public ChatroomFeedbacksDTO(Integer chatRoomId, Map<Integer, String> feedbacks) {
        this.chatRoomId = chatRoomId;
        this.feedbacks = feedbacks;
    }

    public Integer getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Integer chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public Map<Integer, String> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(Map<Integer, String> feedbacks) {
        this.feedbacks = feedbacks;
    }
}
