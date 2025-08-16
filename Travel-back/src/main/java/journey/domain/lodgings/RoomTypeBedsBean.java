package journey.domain.lodgings;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "room_type_beds")
@IdClass(RoomTypeBedsBean.RoomTypeBedsId.class)
public class RoomTypeBedsBean {
    @Id
    @Column(name = "room_type_id")
    private Integer roomTypeId;

    @Id
    @Column(name = "bed_type_id")
    private Integer bedTypeId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", referencedColumnName = "id")
    @JsonBackReference
    private RoomTypesBean roomType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bed_type_id", referencedColumnName = "id")
    @JsonBackReference
    private BedTypesBean bedType;

    public RoomTypeBedsBean() {
    }

    public static class RoomTypeBedsId implements Serializable {
        private Integer roomTypeId;
        private Integer bedTypeId;

        // equals and hashCode
        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof RoomTypeBedsId))
                return false;
            RoomTypeBedsId that = (RoomTypeBedsId) o;
            return Objects.equals(roomTypeId, that.roomTypeId) &&
                    Objects.equals(bedTypeId, that.bedTypeId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(roomTypeId, bedTypeId);
        }
    }

    // getters and setters
    public Integer getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Integer roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public Integer getBedTypeId() {
        return bedTypeId;
    }

    public void setBedTypeId(Integer bedTypeId) {
        this.bedTypeId = bedTypeId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public RoomTypesBean getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypesBean roomType) {
        this.roomType = roomType;
    }

    public BedTypesBean getBedType() {
        return bedType;
    }

    public void setBedType(BedTypesBean bedType) {
        this.bedType = bedType;
    }

}