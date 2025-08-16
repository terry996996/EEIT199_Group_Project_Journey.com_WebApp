package journey.domain.coupons;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import journey.domain.payments.PaymentsRecordBean;
import journey.domain.users.UserBean;

@Entity
@Table(name = "coupon_instances")
public class CouponInstancesBean {
    @Id
    @Column(name = "coupon_instance_id", nullable = false)
    private UUID couponInstanceId = UUID.randomUUID();

    @Column(name = "coupon_name", nullable = false)
    private String couponName = "優惠券";

    @Column(name = "get_from", nullable = false)
    private String getFrom;

    @Column(name = "use_at")
    private LocalDateTime useAt;

    @Column(name = "issued_time", nullable = false)
    private LocalDateTime issuedTime = LocalDateTime.now();

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime = LocalDateTime.now();

    @Column(name = "expire_time")
    private LocalDateTime expireTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_type_id", nullable = false)
    @JsonBackReference
    private CouponTypeBean couponType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private UserBean user;

    @OneToMany(mappedBy = "couponInstance")
    @JsonManagedReference
    private Set<PaymentsRecordBean> payments;

    public UUID getCouponInstanceId() {
        return couponInstanceId;
    }

    public void setCouponInstanceId(UUID couponInstanceId) {
        this.couponInstanceId = couponInstanceId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getGetFrom() {
        return getFrom;
    }

    public void setGetFrom(String getFrom) {
        this.getFrom = getFrom;
    }

    public LocalDateTime getUseAt() {
        return useAt;
    }

    public void setUseAt(LocalDateTime useAt) {
        this.useAt = useAt;
    }

    public LocalDateTime getIssuedTime() {
        return issuedTime;
    }

    public void setIssuedTime(LocalDateTime issuedTime) {
        this.issuedTime = issuedTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public CouponTypeBean getCouponType() {
        return couponType;
    }

    public void setCouponType(CouponTypeBean couponType) {
        this.couponType = couponType;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public Set<PaymentsRecordBean> getPayments() {
        return payments;
    }

    public void setPayments(Set<PaymentsRecordBean> payments) {
        this.payments = payments;
    }

    @Override
    public String toString() {
        return "CouponInstancesBean [couponInstanceId=" + couponInstanceId + ", couponName=" + couponName + ", getFrom="
                + getFrom + ", useAt=" + useAt + ", issuedTime=" + issuedTime + ", startTime=" + startTime
                + ", expireTime=" + expireTime + ", couponType=" + couponType + ", user=" + user + ", payments="
                + payments + "]";
    }

}
