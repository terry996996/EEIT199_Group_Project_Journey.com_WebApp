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
@Table(name = "room_facilities")
@IdClass(RoomFacilitiesBean.RoomFacilitiesId.class)
public class RoomFacilitiesBean {
    @Id
    @Column(name = "room_type_id")
    private Integer roomTypeId;

    @Id
    @Column(name = "facility_id")
    private Integer facilityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", referencedColumnName = "id")
    @JsonBackReference
    private RoomTypesBean roomType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", referencedColumnName = "id")
    @JsonBackReference
    private FacilitiesBean facility;

    public RoomFacilitiesBean() {
    }

    public static class RoomFacilitiesId implements Serializable {
        private Integer roomTypeId;
        private Integer facilityId;

        // equals and hashCode
        // equals 必須覆寫，判斷主鍵是否相等
        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof RoomFacilitiesId))
                return false;
            RoomFacilitiesId that = (RoomFacilitiesId) o;
            return Objects.equals(roomTypeId, that.roomTypeId) &&
                    Objects.equals(facilityId, that.facilityId);
        }

        // hashCode 必須覆寫，確保hash一致性
        @Override
        public int hashCode() {
            return Objects.hash(roomTypeId, facilityId);
        }
    }
    // getters and setters

    public Integer getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Integer roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public Integer getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Integer facilityId) {
        this.facilityId = facilityId;
    }

    public RoomTypesBean getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypesBean roomType) {
        this.roomType = roomType;
    }

    public FacilitiesBean getFacility() {
        return facility;
    }

    public void setFacility(FacilitiesBean facility) {
        this.facility = facility;
    }

}