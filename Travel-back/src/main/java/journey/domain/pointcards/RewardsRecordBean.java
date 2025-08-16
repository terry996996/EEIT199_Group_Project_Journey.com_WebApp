package journey.domain.pointcards;

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
import journey.domain.payments.PaymentsRecordBean;

@Entity
@Table(name = "rewards_record")
public class RewardsRecordBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reward_record_id")
    private Long rewardRecordId;

    @Column(name = "reward_date", nullable = false)
    private LocalDateTime rewardDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_card_id", nullable = false)
    @JsonBackReference
    private PointCardsBean pointCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_id", nullable = false)
    @JsonBackReference
    private RewardsBean reward;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    @JsonBackReference
    private PaymentsRecordBean payment;

    public Long getRewardRecordId() {
        return rewardRecordId;
    }

    public void setRewardRecordId(Long rewardRecordId) {
        this.rewardRecordId = rewardRecordId;
    }

    public LocalDateTime getRewardDate() {
        return rewardDate;
    }

    public void setRewardDate(LocalDateTime rewardDate) {
        this.rewardDate = rewardDate;
    }

    public PointCardsBean getPointCard() {
        return pointCard;
    }

    public void setPointCard(PointCardsBean pointCard) {
        this.pointCard = pointCard;
    }

    public RewardsBean getReward() {
        return reward;
    }

    public void setReward(RewardsBean reward) {
        this.reward = reward;
    }

    public PaymentsRecordBean getPayment() {
        return payment;
    }

    public void setPayment(PaymentsRecordBean payment) {
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "RewardsRecordBean [rewardRecordId=" + rewardRecordId + ", rewardDate=" + rewardDate + ", pointCard="
                + pointCard + ", reward=" + reward + ", payment=" + payment + "]";
    }

}
