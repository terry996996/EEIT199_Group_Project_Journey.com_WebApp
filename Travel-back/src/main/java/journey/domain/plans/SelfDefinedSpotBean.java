package journey.domain.plans;

import java.math.BigDecimal;
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
import jakarta.persistence.Table;
import journey.domain.locations.CityBean;
import journey.domain.locations.CountryBean;
import journey.domain.users.UserBean;
import journey.mapping.UserPlanCollectionAuditable;

@Entity
@Table(name = "self_defined_spots")
public class SelfDefinedSpotBean extends UserPlanCollectionAuditable {
    // ------------ actual columns ------------ //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "address")
    private String address;
    @Column(name = "lat", precision = 9, scale = 4)
    private BigDecimal lat;
    @Column(name = "lon", precision = 9, scale = 4)
    private BigDecimal lon;
    @Column(name = "delete_time")
    private LocalDateTime deleteTime;
    @Column(name = "delete_status", nullable = false)
    private Integer deleteStatus = 1;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private UserBean createdBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private CountryBean coutry;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city", referencedColumnName = "id")
    @JsonBackReference
    private CityBean city;
    // ------------ actual columns ------------ //

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
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

    public CountryBean getCoutry() {
        return coutry;
    }

    public void setCoutry(CountryBean coutry) {
        this.coutry = coutry;
    }

    public CityBean getCity() {
        return city;
    }

    public void setCity(CityBean city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "SelfDefinedSpotBean [id=" + id + ", name=" + name + ", description=" + description + ", address="
                + address + ", lat=" + lat + ", lon=" + lon + ", createdAt=" + createdAt + ", lastModified="
                + lastModified + ", deleteTime=" + deleteTime + ", deleteStatus=" + deleteStatus + ", createdBy="
                + createdBy.getAllUserBean().getUsername() + ", coutry=" + coutry.getName() + ", city=" + city.getName()
                + "]";
    }
}
