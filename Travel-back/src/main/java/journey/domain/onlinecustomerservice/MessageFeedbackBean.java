package journey.domain.onlinecustomerservice;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "message_feedback")
public class MessageFeedbackBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Integer feedbackId;

    @Column(name = "is_good", nullable = false)
    private Boolean isGood;

    @Column(name = "feedback_at", nullable = false)
    private LocalDateTime feedbackAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    @JsonBackReference
    private CustomerServiceMessageBean message;

    public Integer getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Integer feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Boolean getIsGood() {
        return isGood;
    }

    public void setIsGood(Boolean isGood) {
        this.isGood = isGood;
    }

    public LocalDateTime getFeedbackAt() {
        return feedbackAt;
    }

    public void setFeedbackAt(LocalDateTime feedbackAt) {
        this.feedbackAt = feedbackAt;
    }

    public CustomerServiceMessageBean getMessage() {
        return message;
    }

    public void setMessage(CustomerServiceMessageBean message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageFeedbackBean [feedbackId=" + feedbackId + ", isGood=" + isGood + ", feedbackAt=" + feedbackAt
                + ", message=" + message + "]";
    }

}
