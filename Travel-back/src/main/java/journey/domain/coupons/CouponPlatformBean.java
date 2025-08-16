package journey.domain.coupons;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "coupon_platform")
public class CouponPlatformBean {
    @EmbeddedId
    private CouponPlatformEmbeddableBean id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("couponTypeId")
    @JoinColumn(name = "coupon_type_id")
    @JsonBackReference
    private CouponTypeBean couponType;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("platformId")
    @JoinColumn(name = "platform_id")
    @JsonBackReference
    private PlatformBean platform;

    public CouponPlatformEmbeddableBean getId() {
        return id;
    }

    public void setId(CouponPlatformEmbeddableBean id) {
        this.id = id;
    }

    public CouponTypeBean getCouponType() {
        return couponType;
    }

    public void setCouponType(CouponTypeBean couponType) {
        this.couponType = couponType;
    }

    public PlatformBean getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformBean platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return "CouponPlatformBean [id=" + id + ", couponType=" + couponType + ", platform=" + platform + "]";
    }

}
