package journey.domain.users;

/**
 * 管理員實體類別
 * 
 * @author Max
 * @since 2025-07-08
 */
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import journey.domain.comments.CommentReportsBean;
import journey.domain.consultationmessageboard.ReplyBean;
import journey.domain.onlinecustomerservice.CustomerServiceMessageBean;
import journey.domain.orders.AttractionTicketsOrderBean;
import journey.domain.orders.BookingOrdersBean;
import journey.domain.orders.FlightsOrdersBean;
import journey.domain.orders.TicketOrderBean;

@Entity
@Table(name = "admins")
public class AdminBean {
    // ------------ actual columns ------------ //
    @Id
    private Integer adminId;
    @Column(name = "role", nullable = false)
    private String role;
    // ------------ actual columns ------------ //

    // ------------ mappings ------------ //
    @OneToOne
    @MapsId
    @JoinColumn(name = "admin_id", nullable = false)
    @JsonManagedReference
    private AllUserBean allUserBean;

    @OneToMany(mappedBy = "updatedBy")
    @JsonManagedReference
    private Set<FlightsOrdersBean> flightsOrders;

    @OneToMany(mappedBy = "updatedBy")
    @JsonManagedReference
    private Set<TicketOrderBean> ticketOrders;

    @OneToMany(mappedBy = "updatedBy")
    @JsonManagedReference
    private Set<BookingOrdersBean> bookingOrders;

    @OneToMany(mappedBy = "updatedBy")
    @JsonManagedReference
    private Set<AttractionTicketsOrderBean> attractionTicketsOrders;

    @OneToMany(mappedBy = "admin")
    @JsonManagedReference
    private Set<ReplyBean> replies;

    @OneToMany(mappedBy = "admin")
    @JsonManagedReference
    private Set<CustomerServiceMessageBean> messages;

    @OneToMany(mappedBy = "reviewedBy")
    @JsonManagedReference
    private Set<CommentReportsBean> commentReports;

    // ------------ mappings ------------ //

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public AllUserBean getAllUserBean() {
        return allUserBean;
    }

    public void setAllUserBean(AllUserBean allUserBean) {
        this.allUserBean = allUserBean;
    }

    @Override
    public String toString() {
        return "AdminBean [adminId=" + adminId + ", role=" + role + ", Username="
                + allUserBean.getUsername()
                + "]";
    }

    public Set<FlightsOrdersBean> getFlightsOrders() {
        return flightsOrders;
    }

    public void setFlightsOrders(Set<FlightsOrdersBean> flightsOrders) {
        this.flightsOrders = flightsOrders;
    }

    public Set<TicketOrderBean> getTicketOrders() {
        return ticketOrders;
    }

    public void setTicketOrders(Set<TicketOrderBean> ticketOrders) {
        this.ticketOrders = ticketOrders;
    }

    public Set<BookingOrdersBean> getBookingOrders() {
        return bookingOrders;
    }

    public void setBookingOrders(Set<BookingOrdersBean> bookingOrders) {
        this.bookingOrders = bookingOrders;
    }

    public Set<AttractionTicketsOrderBean> getAttractionTicketsOrders() {
        return attractionTicketsOrders;
    }

    public void setAttractionTicketsOrders(Set<AttractionTicketsOrderBean> attractionTicketsOrders) {
        this.attractionTicketsOrders = attractionTicketsOrders;
    }

    public Set<ReplyBean> getReplies() {
        return replies;
    }

    public void setReplies(Set<ReplyBean> replies) {
        this.replies = replies;
    }

    public Set<CustomerServiceMessageBean> getMessages() {
        return messages;
    }

    public void setMessages(Set<CustomerServiceMessageBean> messages) {
        this.messages = messages;
    }

    public Set<CommentReportsBean> getCommentReports() {
        return commentReports;
    }

    public void setCommentReports(Set<CommentReportsBean> commentReports) {
        this.commentReports = commentReports;
    }

}
