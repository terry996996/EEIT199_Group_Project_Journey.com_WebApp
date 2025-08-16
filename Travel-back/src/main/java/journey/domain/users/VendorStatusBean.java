package journey.domain.users;

/**
 * 商家狀態實體類別
 * 
 * @author Max
 * @since 2025-07-08
 */
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "vendor_status")
public class VendorStatusBean {
    // ------------ actual columns ------------ //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer status_id;
    @Column(name = "type", nullable = false)
    private String type;
    // ------------ actual columns ------------ //

    // ------------ mappings ------------ //
    @OneToMany(mappedBy = "status")
    @JsonManagedReference
    private Set<VendorBean> vendorBeans;
    // ------------ mappings ------------ //

    public Integer getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Integer status_id) {
        this.status_id = status_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "VendorStatusBean [status_id=" + status_id + ", type=" + type + "]";
    }

    public Set<VendorBean> getVendorBeans() {
        return vendorBeans;
    }

    public void setVendorBeans(Set<VendorBean> vendorBeans) {
        this.vendorBeans = vendorBeans;
    }

}
