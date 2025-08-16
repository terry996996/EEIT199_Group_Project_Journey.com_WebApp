package journey.domain.pointcards;

import java.math.BigDecimal;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import journey.domain.payments.PaymentsRecordBean;

@Entity
@Table(name = "rewards")
public class RewardsBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reward_id")
    private Integer rewardId;

    @Column(name = "reward_name", nullable = false, unique = true, length = 500)
    private String rewardName;

    @Column(name = "point_required", nullable = false)
    private Integer pointRequired;

    @Column(name = "point_discount", nullable = false, precision = 38, scale = 5)
    private BigDecimal pointDiscount = BigDecimal.ZERO;

    @Column(name = "point_discount_percent", nullable = false, precision = 38, scale = 5)
    private BigDecimal pointDiscountPercent = BigDecimal.ZERO;

    @Column(name = "reward_description")
    private String rewardDescription;

    @OneToMany(mappedBy = "reward")
    @JsonManagedReference
    private Set<RewardsRecordBean> rewardRecords;

    @OneToMany(mappedBy = "reward")
    @JsonManagedReference
    private Set<PaymentsRecordBean> payments;

    public Integer getRewardId() {
        return rewardId;
    }

    public void setRewardId(Integer rewardId) {
        this.rewardId = rewardId;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public Integer getPointRequired() {
        return pointRequired;
    }

    public void setPointRequired(Integer pointRequired) {
        this.pointRequired = pointRequired;
    }

    public BigDecimal getPointDiscount() {
        return pointDiscount;
    }

    public void setPointDiscount(BigDecimal pointDiscount) {
        this.pointDiscount = pointDiscount;
    }

    public BigDecimal getPointDiscountPercent() {
        return pointDiscountPercent;
    }

    public void setPointDiscountPercent(BigDecimal pointDiscountPercent) {
        this.pointDiscountPercent = pointDiscountPercent;
    }

    public String getRewardDescription() {
        return rewardDescription;
    }

    public void setRewardDescription(String rewardDescription) {
        this.rewardDescription = rewardDescription;
    }

    public Set<RewardsRecordBean> getRewardRecords() {
        return rewardRecords;
    }

    public void setRewardRecords(Set<RewardsRecordBean> rewardRecords) {
        this.rewardRecords = rewardRecords;
    }

    public Set<PaymentsRecordBean> getPayments() {
        return payments;
    }

    public void setPayments(Set<PaymentsRecordBean> payments) {
        this.payments = payments;
    }

    @Override
    public String toString() {
        return "RewardsBean [rewardId=" + rewardId + ", rewardName=" + rewardName + ", pointRequired=" + pointRequired
                + ", pointDiscount=" + pointDiscount + ", pointDiscountPercent=" + pointDiscountPercent
                + ", rewardDescription=" + rewardDescription + ", rewardRecords=" + rewardRecords + ", payments="
                + payments + "]";
    }

}
