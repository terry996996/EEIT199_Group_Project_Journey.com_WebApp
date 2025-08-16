package journey.domain.lodgings;

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
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import journey.domain.comments.CommentsBean;
import journey.domain.orders.BookingOrderItemsBean;
import journey.domain.plans.TripActivityBean;
import journey.domain.users.VendorBean;

@Entity
@Table(name = "room_types")
public class RoomTypesBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "price_per_night", nullable = false)
    private Integer pricePerNight;

    @Column(name = "max_guests", nullable = false)
    private Integer maxGuests;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "deleted_time")
    private LocalDateTime deletedTime;

    @Column(name = "delete_status", nullable = false)
    private Integer deleteStatus = 1;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lodging_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private LodgingsBean lodging;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private VendorBean createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private VendorBean updatedBy;

    @OneToMany(mappedBy = "roomType")
    @JsonManagedReference
    private Set<RoomTypeBedsBean> roomTypeBeds;

    @OneToMany(mappedBy = "roomType")
    @JsonManagedReference
    private Set<RoomFacilitiesBean> roomFacilities;

    @OneToMany(mappedBy = "roomType")
    @JsonManagedReference
    private Set<RoomAvailabilityBean> roomAvailability;

    @OneToMany(mappedBy = "roomType")
    @OrderBy("sortOrder ASC") // 按照 sortOrder 升序排列
    @JsonManagedReference
    private Set<LodgingImagesBean> lodgingImages;

    @OneToMany(mappedBy = "roomType")
    @JsonManagedReference
    private Set<BookingOrderItemsBean> bookingOrderItems;

    @OneToMany(mappedBy = "roomType")
    @JsonManagedReference
    private Set<TripActivityBean> tripActivities;

    @OneToMany(mappedBy = "roomType")
    @JsonManagedReference
    private Set<CommentsBean> comments;

    public RoomTypesBean() {
    }

    public RoomTypesBean(Integer id, String name, String description, Integer pricePerNight, Integer maxGuests,
            Boolean isActive, LocalDateTime deletedTime, Integer deleteStatus, LocalDateTime createdAt,
            LocalDateTime updatedAt, LodgingsBean lodging, VendorBean createdBy, VendorBean updatedBy) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.maxGuests = maxGuests;
        this.isActive = isActive;
        this.deletedTime = deletedTime;
        this.deleteStatus = deleteStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lodging = lodging;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    // getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Integer pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public Integer getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(Integer maxGuests) {
        this.maxGuests = maxGuests;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getDeletedTime() {
        return deletedTime;
    }

    public void setDeletedTime(LocalDateTime deletedTime) {
        this.deletedTime = deletedTime;
    }

    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
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

    public LodgingsBean getLodging() {
        return lodging;
    }

    public void setLodging(LodgingsBean lodging) {
        this.lodging = lodging;
    }

    public VendorBean getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(VendorBean createdBy) {
        this.createdBy = createdBy;
    }

    public VendorBean getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(VendorBean updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Set<RoomTypeBedsBean> getRoomTypeBeds() {
        return roomTypeBeds;
    }

    public void setRoomTypeBeds(Set<RoomTypeBedsBean> roomTypeBeds) {
        this.roomTypeBeds = roomTypeBeds;
    }

    public Set<RoomFacilitiesBean> getRoomFacilities() {
        return roomFacilities;
    }

    public void setRoomFacilities(Set<RoomFacilitiesBean> roomFacilities) {
        this.roomFacilities = roomFacilities;
    }

    public Set<RoomAvailabilityBean> getRoomAvailability() {
        return roomAvailability;
    }

    public void setRoomAvailability(Set<RoomAvailabilityBean> roomAvailability) {
        this.roomAvailability = roomAvailability;
    }

    public Set<LodgingImagesBean> getLodgingImages() {
        return lodgingImages;
    }

    public void setLodgingImages(Set<LodgingImagesBean> lodgingImages) {
        this.lodgingImages = lodgingImages;
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null)
            this.createdAt = now;
        if (this.updatedAt == null)
            this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Set<BookingOrderItemsBean> getBookingOrderItems() {
        return bookingOrderItems;
    }

    public void setBookingOrderItems(Set<BookingOrderItemsBean> bookingOrderItems) {
        this.bookingOrderItems = bookingOrderItems;
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