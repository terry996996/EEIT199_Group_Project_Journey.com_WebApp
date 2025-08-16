package journey.domain.lodgings;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import journey.domain.users.VendorBean;

@Entity
@Table(name = "room_availability")
public class RoomAvailabilityBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "available_rooms", nullable = false)
    private Integer availableRooms;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private RoomTypesBean roomType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private VendorBean createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private VendorBean updatedBy;

    public RoomAvailabilityBean() {
    }

    public RoomAvailabilityBean(Integer id, LocalDateTime date, Integer availableRooms, LocalDateTime createdAt,
            LocalDateTime updatedAt, RoomTypesBean roomType, VendorBean createdBy, VendorBean updatedBy) {
        this.id = id;
        this.date = date;
        this.availableRooms = availableRooms;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.roomType = roomType;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(Integer availableRooms) {
        this.availableRooms = availableRooms;
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

    public RoomTypesBean getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypesBean roomType) {
        this.roomType = roomType;
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
}