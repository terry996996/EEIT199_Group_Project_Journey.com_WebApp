package journey.domain.traffictickets;

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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import journey.domain.comments.CommentsBean;
import journey.domain.locations.CountryBean;
import journey.domain.orders.TicketOrderDetailBean;
import journey.domain.plans.TripActivityBean;

@Entity
@Table(name = "traffic_tickets")
public class TrafficTicketsBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Integer ticketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    @JsonBackReference
    private CountryBean country;

    @Column(name = "name", length = 225, nullable = false)
    private String name;

    @Column(name = "transport_type", length = 50, nullable = false)
    private String transportType;

    @Column(name = "valid_days", nullable = false)
    private Integer validDays;

    @Column(name = "description", columnDefinition = "nvarchar(max)", nullable = false)
    private String description;

    @Column(name = "area", length = 50, nullable = false)
    private String area;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    // @Column(name = "valid_start")
    // private LocalDateTime validStart;

    // @Column(name = "valid_end")
    // private LocalDateTime validEnd;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Lob
    @Column(name = "image_url", nullable = false)
    private String imageURL;

    // @Column(name = "image_type", length = 50)
    // private String imageType;

    @OneToMany(mappedBy = "trafficticket", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<TicketOrderDetailBean> orderDetails;

    @OneToMany(mappedBy = "traffic")
    @JsonManagedReference
    private Set<TripActivityBean> tripActivities;

    @OneToMany(mappedBy = "trafficTicketId")
    @JsonManagedReference
    private Set<CommentsBean> comments;

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public CountryBean getCountry() {
        return country;
    }

    public void setCountry(CountryBean country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public Integer getValidDays() {
        return validDays;
    }

    public void setValidDays(Integer validDays) {
        this.validDays = validDays;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Set<TicketOrderDetailBean> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(Set<TicketOrderDetailBean> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Set<TripActivityBean> getTripActivities() {
        return tripActivities;
    }

    public void setTripActivities(Set<TripActivityBean> tripActivities) {
        this.tripActivities = tripActivities;
    }

    public Set<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(Set<CommentsBean> comments) {
        this.comments = comments;
    }

}