package journey.domain.attractiontickets;

import jakarta.persistence.*;
import journey.domain.orders.AttractionTicketOrderItemsBean;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "ticket_packages")
public class TicketPackagesBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    @JsonBackReference
    private AttractionTicketsBean ticket;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    @Column(name = "image_type", nullable = false, length = 50)
    private String imageType;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Boolean state;

    @OneToMany(mappedBy = "ticketPackage", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<TicketTypesBean> types;

    @OneToMany(mappedBy = "ticketPackage")
    @JsonManagedReference
    private Set<AttractionTicketOrderItemsBean> attractionTicketOrderItems;

    public Set<TicketTypesBean> getTypes() {
        return types;
    }

    public void setTypes(Set<TicketTypesBean> types) {
        this.types = types;
    }

    public TicketPackagesBean() {
    }

    public TicketPackagesBean(Integer id, AttractionTicketsBean ticket, String name, String description,
            String imageUrl, String imageType,
            LocalDateTime startTime,
            LocalDateTime endTime, Integer createdBy,
            Integer updatedBy, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean state) {
        this.id = id;
        this.ticket = ticket;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.imageType = imageType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AttractionTicketsBean getTicket() {
        return ticket;
    }

    public void setTicket(AttractionTicketsBean ticket) {
        this.ticket = ticket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
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

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Set<AttractionTicketOrderItemsBean> getAttractionTicketOrderItems() {
        return attractionTicketOrderItems;
    }

    public void setAttractionTicketOrderItems(Set<AttractionTicketOrderItemsBean> attractionTicketOrderItems) {
        this.attractionTicketOrderItems = attractionTicketOrderItems;
    }

    @PrePersist
    public void prePersist() {
        if (this.startTime == null)
            this.startTime = LocalDateTime.now();
        if (this.createdAt == null)
            this.createdAt = LocalDateTime.now();
    }

}
