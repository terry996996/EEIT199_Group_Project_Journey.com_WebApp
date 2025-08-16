package journey.domain.coupons;

import java.math.BigDecimal;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

// 優惠券類型資料表 (coupon_type)
@Entity
@Table(name = "coupon_type")
public class CouponTypeBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_type_id")
    private Integer couponTypeId;

    @Column(name = "coupon_type", nullable = false)
    private String couponType = "一般";

    @Column(name = "discount_percent", nullable = false, precision = 38, scale = 5)
    private BigDecimal discountPercent = BigDecimal.ZERO;

    @Column(name = "discount_amount", nullable = false, precision = 38, scale = 5)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "order_amount_limit", nullable = false, precision = 38, scale = 5)
    private BigDecimal orderAmountLimit = BigDecimal.ZERO;

    @Column(name = "detail")
    private String detail;

    @OneToMany(mappedBy = "couponType")
    @JsonManagedReference
    private Set<CouponInstancesBean> couponInstances;

    @ManyToMany
    @JoinTable(name = "coupon_platform", joinColumns = @JoinColumn(name = "coupon_type_id"), inverseJoinColumns = @JoinColumn(name = "platform_id"))
    @JsonIgnoreProperties("couponTypes")
    private Set<PlatformBean> platforms;

    public Integer getCouponTypeId() {
        return couponTypeId;
    }

    public void setCouponTypeId(Integer couponTypeId) {
        this.couponTypeId = couponTypeId;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getOrderAmountLimit() {
        return orderAmountLimit;
    }

    public void setOrderAmountLimit(BigDecimal orderAmountLimit) {
        this.orderAmountLimit = orderAmountLimit;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Set<CouponInstancesBean> getCouponInstances() {
        return couponInstances;
    }

    public void setCouponInstances(Set<CouponInstancesBean> couponInstances) {
        this.couponInstances = couponInstances;
    }

    public Set<PlatformBean> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(Set<PlatformBean> platforms) {
        this.platforms = platforms;
    }

    @Override
    public String toString() {
        return "CouponTypeBean [couponTypeId=" + couponTypeId + ", couponType=" + couponType + ", discountPercent="
                + discountPercent + ", discountAmount=" + discountAmount + ", orderAmountLimit=" + orderAmountLimit
                + ", detail=" + detail + ", couponInstances=" + couponInstances + ", platforms=" + platforms + "]";
    }

}
