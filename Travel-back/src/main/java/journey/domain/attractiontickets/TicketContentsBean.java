package journey.domain.attractiontickets;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "ticket_contents")
public class TicketContentsBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Integer contentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    @JsonBackReference
    private AttractionTicketsBean ticket;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 255)
    private String subtitle;

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String description;

    public TicketContentsBean() {
    }

    public TicketContentsBean(Integer contentId, AttractionTicketsBean ticket, String title, String subtitle,
            String description) {
        this.contentId = contentId;
        this.ticket = ticket;
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public AttractionTicketsBean getTicket() {
        return ticket;
    }

    public void setTicket(AttractionTicketsBean ticket) {
        this.ticket = ticket;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
