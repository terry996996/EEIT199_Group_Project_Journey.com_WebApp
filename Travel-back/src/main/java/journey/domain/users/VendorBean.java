package journey.domain.users;

/**
 * 商家實體類別
 * 
 * @author Max
 * @since 2025-07-08
 */
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import journey.domain.lodgings.LodgingsBean;
import journey.domain.lodgings.RoomAvailabilityBean;
import journey.domain.lodgings.RoomTypesBean;

@Entity
@Table(name = "vendors")
public class VendorBean {
    // ------------ actual columns ------------ //
    @Id
    private Integer id;
    @Column(name = "vendor_name", nullable = false)
    private String vendorName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", referencedColumnName = "status_id", nullable = false)
    @JsonBackReference
    private VendorStatusBean status;
    // ------------ actual columns ------------ //

    // ------------ mappings ------------ //
    @OneToOne
    @MapsId
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    @JsonManagedReference
    private AllUserBean allUserBean;

    @OneToMany(mappedBy = "vendor")
    @JsonManagedReference
    private Set<LodgingsBean> lodgings;

    @OneToMany(mappedBy = "updatedBy")
    @JsonManagedReference
    private Set<LodgingsBean> updatedLodgings;

    @OneToMany(mappedBy = "createdBy")
    @JsonManagedReference
    private Set<RoomTypesBean> roomTypes;

    @OneToMany(mappedBy = "updatedBy")
    @JsonManagedReference
    private Set<RoomTypesBean> updatedRoomTypes;

    @OneToMany(mappedBy = "createdBy")
    @JsonManagedReference
    private Set<RoomAvailabilityBean> roomAvailabilities;

    @OneToMany(mappedBy = "updatedBy")
    @JsonManagedReference
    private Set<RoomAvailabilityBean> updatedRoomAvailabilities;
    // ------------ mappings ------------ //

    public Set<RoomAvailabilityBean> getRoomAvailabilities() {
        return roomAvailabilities;
    }

    public void setRoomAvailabilities(Set<RoomAvailabilityBean> roomAvailabilities) {
        this.roomAvailabilities = roomAvailabilities;
    }

    public Set<RoomAvailabilityBean> getUpdatedRoomAvailabilities() {
        return updatedRoomAvailabilities;
    }

    public void setUpdatedRoomAvailabilities(Set<RoomAvailabilityBean> updatedRoomAvailabilities) {
        this.updatedRoomAvailabilities = updatedRoomAvailabilities;
    }

    public Set<RoomTypesBean> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(Set<RoomTypesBean> roomTypes) {
        this.roomTypes = roomTypes;
    }

    public Set<RoomTypesBean> getUpdatedRoomTypes() {
        return updatedRoomTypes;
    }

    public void setUpdatedRoomTypes(Set<RoomTypesBean> updatedRoomTypes) {
        this.updatedRoomTypes = updatedRoomTypes;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public VendorStatusBean getStatus() {
        return status;
    }

    public void setStatus(VendorStatusBean status) {
        this.status = status;
    }

    public AllUserBean getAllUserBean() {
        return allUserBean;
    }

    public void setAllUserBean(AllUserBean allUserBean) {
        this.allUserBean = allUserBean;
    }

    @Override
    public String toString() {
        return "VendorBean [vendorId=" + id + ", vendorName=" + vendorName + ", status="
                + status.getType() + ", Username=" + allUserBean.getUsername() + "]";
    }

    public Set<LodgingsBean> getLodgings() {
        return lodgings;
    }

    public void setLodgings(Set<LodgingsBean> lodgings) {
        this.lodgings = lodgings;
    }

    public Set<LodgingsBean> getUpdatedLodgings() {
        return updatedLodgings;
    }

    public void setUpdatedLodgings(Set<LodgingsBean> updatedLodgings) {
        this.updatedLodgings = updatedLodgings;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}