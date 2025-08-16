package journey.dto;

import jakarta.validation.constraints.NotNull;

public class MessageFeedbackDTO {

    @NotNull(message = "Message ID cannot be null")
    private Integer messageId;

    @NotNull(message = "isGood cannot be null")
    private Boolean isGood; // true for 'like', false for 'dislike'

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Boolean getIsGood() {
        return isGood;
    }

    public void setIsGood(Boolean isGood) {
        this.isGood = isGood;
    }

    @Override
    public String toString() {
        return "MessageFeedbackDTO{" +
                "messageId=" + messageId +
                ", isGood=" + isGood +
                '}';
    }
}
