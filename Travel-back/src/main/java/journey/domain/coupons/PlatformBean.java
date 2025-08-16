package journey.domain.coupons;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "platform")
public class PlatformBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "platform_id")
    private Integer platformId;

    @Column(name = "platform_type", nullable = false, unique = true, length = 500)
    private String platformType;

    @ManyToMany(mappedBy = "platforms")
    @JsonIgnoreProperties("platforms")
    private Set<CouponTypeBean> couponTypes;

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public Set<CouponTypeBean> getCouponTypes() {
        return couponTypes;
    }

    public void setCouponTypes(Set<CouponTypeBean> couponTypes) {
        this.couponTypes = couponTypes;
    }

    @Override
    public String toString() {
        return "PlatformBean [platformId=" + platformId + ", platformType=" + platformType + ", couponTypes="
                + couponTypes + "]";
    }

}
