package journey.domain.plans;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "trip_transportations")
public class TripTransportations {
    // ------------ actual columns ------------ //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "mode", nullable = false)
    private String mode;
    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;
    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;
    @Column(name = "notes", nullable = false)
    private String notes;
    @Column(name = "delete_time")
    private LocalDateTime deleteTime;
    @Column(name = "delete_status", nullable = false)
    private Integer deleteStatus = 1;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "day_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private TripDayBean dayId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_activity_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private TripActivityBean fromActivity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_activity_id", referencedColumnName = "id", nullable = false)
    private TripActivityBean toActivity;
    // ------------ actual columns ------------ //

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public TripDayBean getDayId() {
        return dayId;
    }

    public void setDayId(TripDayBean dayId) {
        this.dayId = dayId;
    }

    public TripActivityBean getFromActivity() {
        return fromActivity;
    }

    public void setFromActivity(TripActivityBean fromActivity) {
        this.fromActivity = fromActivity;
    }

    public TripActivityBean getToActivity() {
        return toActivity;
    }

    public void setToActivity(TripActivityBean toActivity) {
        this.toActivity = toActivity;
    }

    @Override
    public String toString() {
        return "TripTransportations [id=" + id + ", mode=" + mode + ", departureTime=" + departureTime
                + ", arrivalTime=" + arrivalTime + ", notes=" + notes + ", deleteTime=" + deleteTime + ", deleteStatus="
                + deleteStatus + ", dayId=" + dayId.getActualDate() + ", fromActivity=" + fromActivity.getTitle()
                + ", toActivity=" + toActivity.getTitle()
                + "]";
    }

    @PrePersist
    public void prePersist() {
        if (this.deleteStatus == null)
            this.deleteStatus = 1;
    }

}
