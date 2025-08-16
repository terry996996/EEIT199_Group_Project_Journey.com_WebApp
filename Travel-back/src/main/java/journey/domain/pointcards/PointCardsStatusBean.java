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
import jakarta.validation.constraints.AssertTrue;
import journey.domain.payments.PaymentsRecordBean;

@Entity
@Table(name = "point_cards_status")
public class PointCardsStatusBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "change_record_id")
    private Long changeRecordId;

    @Column(name = "point_change", nullable = false)
    private Integer pointChange;

    @Column(name = "point_source", nullable = false)
    private String pointSource = "交易回饋";

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_card_id", nullable = false)
    @JsonBackReference
    private PointCardsBean pointCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    @JsonBackReference
    private PaymentsRecordBean payment;

    // Service還會再檢查
    @AssertTrue(message = "點數變動不能為 0")
    public boolean isValidPointChange() {
        return pointChange != null && pointChange != 0;
    }

    public Long getChangeRecordId() {
        return changeRecordId;
    }

    public void setChangeRecordId(Long changeRecordId) {
        this.changeRecordId = changeRecordId;
    }

    public Integer getPointChange() {
        return pointChange;
    }

    public void setPointChange(Integer pointChange) {
        this.pointChange = pointChange;
    }

    public String getPointSource() {
        return pointSource;
    }

    public void setPointSource(String pointSource) {
        this.pointSource = pointSource;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public PointCardsBean getPointCard() {
        return pointCard;
    }

    public void setPointCard(PointCardsBean pointCard) {
        this.pointCard = pointCard;
    }

    public PaymentsRecordBean getPayment() {
        return payment;
    }

    public void setPayment(PaymentsRecordBean payment) {
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "PointCardsStatusBean [changeRecordId=" + changeRecordId + ", pointChange=" + pointChange
                + ", pointSource=" + pointSource + ", updatedAt=" + updatedAt + ", pointCard=" + pointCard
                + ", payment=" + payment + "]";
    }

}
