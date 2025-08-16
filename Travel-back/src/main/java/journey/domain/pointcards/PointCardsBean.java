package journey.domain.pointcards;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import journey.domain.payments.PaymentsRecordBean;
import journey.domain.users.UserBean;

@Entity
@Table(name = "point_cards")
public class PointCardsBean {

    @Id
    @Column(name = "point_card_id", nullable = false)
    private UUID pointCardId = UUID.randomUUID();

    @Column(name = "release_date", nullable = false)
    private LocalDateTime releaseDate = LocalDateTime.now();

    @Column(name = "point_card_rule")
    private String pointCardRule;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonManagedReference
    private UserBean user;

    @OneToMany(mappedBy = "pointCard")
    @JsonManagedReference
    private Set<PointCardsStatusBean> statusSet;

    @OneToMany(mappedBy = "pointCard")
    @JsonManagedReference
    private Set<RewardsRecordBean> rewardRecords;

    @OneToMany(mappedBy = "pointCard")
    @JsonManagedReference
    private Set<PaymentsRecordBean> payments;

    public UUID getPointCardId() {
        return pointCardId;
    }

    public void setPointCardId(UUID pointCardId) {
        this.pointCardId = pointCardId;
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPointCardRule() {
        return pointCardRule;
    }

    public void setPointCardRule(String pointCardRule) {
        this.pointCardRule = pointCardRule;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public Set<PointCardsStatusBean> getStatusSet() {
        return statusSet;
    }

    public void setStatusSet(Set<PointCardsStatusBean> statusSet) {
        this.statusSet = statusSet;
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
        return "PointCardsBean [pointCardId=" + pointCardId + ", releaseDate=" + releaseDate + ", pointCardRule="
                + pointCardRule + ", user=" + user + ", statusSet=" + statusSet + ", rewardRecords=" + rewardRecords
                + ", payments=" + payments + "]";
    }

}
