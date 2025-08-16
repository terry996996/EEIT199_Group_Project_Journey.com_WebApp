package journey.domain.coupons;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CouponPlatformEmbeddableBean implements Serializable {

    @Column(name = "coupon_type_id")
    private Integer couponTypeId;

    @Column(name = "platform_id")
    private Integer platformId;

    public CouponPlatformEmbeddableBean() {
    }

    public CouponPlatformEmbeddableBean(Integer couponTypeId, Integer platformId) {
        this.couponTypeId = couponTypeId;
        this.platformId = platformId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CouponPlatformEmbeddableBean))
            return false;
        CouponPlatformEmbeddableBean that = (CouponPlatformEmbeddableBean) o;
        return Objects.equals(couponTypeId, that.couponTypeId) &&
                Objects.equals(platformId, that.platformId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(couponTypeId, platformId);
    }

    public Integer getCouponTypeId() {
        return couponTypeId;
    }

    public void setCouponTypeId(Integer couponTypeId) {
        this.couponTypeId = couponTypeId;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    @Override
    public String toString() {
        return "CouponPlatformEmbeddableBean [couponTypeId=" + couponTypeId + ", platformId=" + platformId + "]";
    }

}
