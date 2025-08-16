package journey.domain.plans;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "trip_days")
public class TripDayBean {
    // ------------ actual columns ------------ //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "day_num", nullable = false)
    private Integer dayNum;
    @Column(name = "actual_date", nullable = false)
    private LocalDateTime actualDate;
    @Column(name = "delete_time")
    private LocalDateTime deleteTime;
    @Column(name = "delete_status", nullable = false)
    private Integer deleteStatus = 1;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private TripPlanBean tripId;
    // ------------ actual columns ------------ //

    // ------------ mappings ------------ //
    @OneToMany(mappedBy = "dayId", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private Set<TripActivityBean> tripActivityBeans;

    @OneToMany(mappedBy = "dayId", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private Set<TripTransportations> transportations;
    // ------------ mappings ------------ //

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDayNum() {
        return dayNum;
    }

    public void setDayNum(Integer dayNum) {
        this.dayNum = dayNum;
    }

    public LocalDateTime getActualDate() {
        return actualDate;
    }

    public void setActualDate(LocalDateTime actualDate) {
        this.actualDate = actualDate;
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

    public TripPlanBean getTripId() {
        return tripId;
    }

    public void setTripId(TripPlanBean tripId) {
        this.tripId = tripId;
    }

    @Override
    public String toString() {
        return "TripDayBean [id=" + id + ", dayNum=" + dayNum + ", actualDate=" + actualDate + ", deleteTime="
                + deleteTime + ", deleteStatus=" + deleteStatus + ", tripId=" + tripId.getId() + "]";
    }

    public Set<TripActivityBean> getTripActivityBeans() {
        return tripActivityBeans;
    }

    public void setTripActivityBeans(Set<TripActivityBean> tripActivityBeans) {
        this.tripActivityBeans = tripActivityBeans;
    }

    public Set<TripTransportations> getTransportations() {
        return transportations;
    }

    public void setTransportations(Set<TripTransportations> transportations) {
        this.transportations = transportations;
    }

    @PrePersist
    public void prePersist() {
        if (this.deleteStatus == null)
            this.deleteStatus = 1;
    }
}
