package journey.domain.onlinecustomerservice;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import journey.domain.users.AdminBean;

@Entity
@Table(name = "customer_service_message")
public class CustomerServiceMessageBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Integer messageId;

    @Column(name = "message_content", nullable = false)
    private String messageContent;

    @Column(name = "sent_time", nullable = false)
    private LocalDateTime sentTime = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    @JsonBackReference
    private CustomerServiceChatroomBean chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    @JsonBackReference
    private CustomerServiceQuestionBean question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    @JsonBackReference
    private AdminBean admin;

    @OneToMany(mappedBy = "message")
    @JsonManagedReference
    private Set<MessageFeedbackBean> feedbacks;

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public LocalDateTime getSentTime() {
        return sentTime;
    }

    public void setSentTime(LocalDateTime sentTime) {
        this.sentTime = sentTime;
    }

    public CustomerServiceChatroomBean getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(CustomerServiceChatroomBean chatRoom) {
        this.chatRoom = chatRoom;
    }

    public CustomerServiceQuestionBean getQuestion() {
        return question;
    }

    public void setQuestion(CustomerServiceQuestionBean question) {
        this.question = question;
    }

    public AdminBean getAdmin() {
        return admin;
    }

    public void setAdmin(AdminBean admin) {
        this.admin = admin;
    }

    public Set<MessageFeedbackBean> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(Set<MessageFeedbackBean> feedbacks) {
        this.feedbacks = feedbacks;
    }

    @Override
    public String toString() {
        return "CustomerServiceMessageBean [messageId=" + messageId + ", messageContent=" + messageContent
                + ", sentTime=" + sentTime + ", chatRoom=" + chatRoom + ", question=" + question + ", admin=" + admin
                + ", feedbacks=" + feedbacks + "]";
    }

}
