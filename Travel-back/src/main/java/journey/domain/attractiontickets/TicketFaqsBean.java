package journey.domain.attractiontickets;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "ticket_faqs")
public class TicketFaqsBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faq_id")
    private Integer faqId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    @JsonBackReference
    private AttractionTicketsBean ticket;

    @Column(nullable = false, length = 500)
    private String question;

    @Column(nullable = false, length = 1000)
    private String answer;

    @Column(name = "sort_order")
    private Integer sortOrder;

    public TicketFaqsBean() {
    }

    public TicketFaqsBean(Integer faqId, AttractionTicketsBean ticket, String question, String answer,
            Integer sortOrder) {
        this.faqId = faqId;
        this.ticket = ticket;
        this.question = question;
        this.answer = answer;
        this.sortOrder = sortOrder;
    }

    public Integer getFaqId() {
        return faqId;
    }

    public void setFaqId(Integer faqId) {
        this.faqId = faqId;
    }

    public AttractionTicketsBean getTicket() {
        return ticket;
    }

    public void setTicket(AttractionTicketsBean ticket) {
        this.ticket = ticket;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

}
