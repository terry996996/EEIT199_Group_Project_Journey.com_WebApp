package journey.domain.plans;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
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
import journey.domain.comments.CommentsBean;
import journey.domain.orders.AttractionTicketsOrderBean;
import journey.domain.orders.BookingOrdersBean;
import journey.domain.orders.FlightsOrdersBean;
import journey.domain.orders.TicketOrderBean;
import journey.domain.users.UserBean;
import journey.mapping.UserPlanCollectionAuditable;

@Entity
@Table(name = "trip_plans")
public class TripPlanBean extends UserPlanCollectionAuditable {
    // ------------ actual columns ------------ //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = false;
    @Column(name = "views", nullable = false)
    private Integer views = 0;
    @Column(name = "delete_time")
    private LocalDateTime deleteTime;
    @Column(name = "delete_status", nullable = false)
    private Integer deleteStatus = 1;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private UserBean createdBy;
    // ------------ actual columns ------------ //

    // ------------ mappings ------------ //
    @OneToMany(mappedBy = "tripId", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private Set<TripDayBean> tripDayBeans;

    @OneToMany(mappedBy = "tripId", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private Set<TripSharedLinkBean> linkBeans;

    @OneToMany(mappedBy = "tripId")
    @JsonManagedReference
    private Set<FlightsOrdersBean> flightsOrders;

    @OneToMany(mappedBy = "tripplan")
    @JsonManagedReference
    private Set<TicketOrderBean> ticketOrders;

    @OneToMany(mappedBy = "tripPlan")
    @JsonManagedReference
    private Set<BookingOrdersBean> bookingOrders;

    @OneToMany(mappedBy = "trip")
    @JsonManagedReference
    private Set<AttractionTicketsOrderBean> attractionTicketsOrders;

    @OneToMany(mappedBy = "tripPlanId")
    @JsonManagedReference
    private Set<CommentsBean> comments;
    // ------------ mappings ------------ //

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public LocalDateTime getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(LocalDateTime deleteTime) {
        this.deleteTime = deleteTime;
    }

    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public UserBean getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserBean createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "TripPlanBean [id=" + id + ", title=" + title + ", description=" + description + ", startDate="
                + startDate + ", endDate=" + endDate + ", isPublic=" + isPublic + ", views=" + views + ", createdAt="
                + createdAt + ", lastModified=" + lastModified + ", deleteTime=" + deleteTime + ", deleteStatus="
                + deleteStatus + ", createdBy=" + createdBy.getAllUserBean().getUsername() + "]";
    }

    public Set<TripDayBean> getTripDayBeans() {
        return tripDayBeans;
    }

    public void setTripDayBeans(Set<TripDayBean> tripDayBeans) {
        this.tripDayBeans = tripDayBeans;
    }

    public Set<TripSharedLinkBean> getLinkBeans() {
        return linkBeans;
    }

    public void setLinkBeans(Set<TripSharedLinkBean> linkBeans) {
        this.linkBeans = linkBeans;
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

    public Set<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(Set<CommentsBean> comments) {
        this.comments = comments;
    }
}
