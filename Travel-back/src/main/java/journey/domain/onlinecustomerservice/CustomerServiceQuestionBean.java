package journey.domain.onlinecustomerservice;

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
import journey.domain.payments.TypeEnumBean;

@Entity
@Table(name = "customer_service_question")
public class CustomerServiceQuestionBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_template_id")
    private Integer questionTemplateId;

    @Column(name = "question_template", nullable = false, unique = true, length = 500)
    private String questionTemplate;

    @Column(name = "reply_template", nullable = false)
    private String replyTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    @JsonBackReference
    private TypeEnumBean type;

    @OneToMany(mappedBy = "question")
    @JsonManagedReference
    private Set<CustomerServiceMessageBean> messages;

    public Integer getQuestionTemplateId() {
        return questionTemplateId;
    }

    public void setQuestionTemplateId(Integer questionTemplateId) {
        this.questionTemplateId = questionTemplateId;
    }

    public String getQuestionTemplate() {
        return questionTemplate;
    }

    public void setQuestionTemplate(String questionTemplate) {
        this.questionTemplate = questionTemplate;
    }

    public String getReplyTemplate() {
        return replyTemplate;
    }

    public void setReplyTemplate(String replyTemplate) {
        this.replyTemplate = replyTemplate;
    }

    public TypeEnumBean getType() {
        return type;
    }

    public void setType(TypeEnumBean type) {
        this.type = type;
    }

    public Set<CustomerServiceMessageBean> getMessages() {
        return messages;
    }

    public void setMessages(Set<CustomerServiceMessageBean> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "CustomerServiceQuestionBean [questionTemplateId=" + questionTemplateId + ", questionTemplate="
                + questionTemplate + ", replyTemplate=" + replyTemplate + ", type=" + type + ", messages=" + messages
                + "]";
    }

}
