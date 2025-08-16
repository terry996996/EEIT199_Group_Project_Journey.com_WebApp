package journey.domain.plans;

import java.time.LocalDateTime;
import java.util.List;

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
import jakarta.persistence.Table;
import journey.domain.attractiontickets.AttractionTicketsBean;
import journey.domain.attractiontickets.AttractionsBean;
import journey.domain.flights.FlightsBean;
import journey.domain.lodgings.RoomTypesBean;
import journey.domain.traffictickets.TrafficTicketsBean;
import journey.enums.ActivityTypeEnum;
import journey.mapping.UserPlanCollectionAuditable;

@Entity
@Table(name = "trip_activities")
public class TripActivityBean extends UserPlanCollectionAuditable {
    // ------------ actual columns ------------ //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "location_type", nullable = false)
    private ActivityTypeEnum locationType;
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;
    @Column(name = "ordinal", nullable = false)
    private Integer ordinal;
    @Column(name = "delete_time")
    private LocalDateTime deleteTime;
    @Column(name = "delete_status", nullable = false)
    private Integer deleteStatus = 1;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "day_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private TripDayBean dayId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight")
    @JsonBackReference
    private FlightsBean flight;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type")
    @JsonBackReference
    private RoomTypesBean roomType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "traffic")
    @JsonBackReference
    private TrafficTicketsBean traffic;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attraction_ticket")
    @JsonBackReference
    private AttractionTicketsBean attractionTicket;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attraction")
    @JsonBackReference
    private AttractionsBean attraction;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "self_defined", referencedColumnName = "id")
    @JsonBackReference
    private SelfDefinedSpotBean selfDefined;
    // ------------ actual columns ------------ //

    // ------------ mappings ------------ //
    @OneToMany(mappedBy = "fromActivity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private List<TripTransportations> fromTransportations;

    @OneToMany(mappedBy = "toActivity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private List<TripTransportations> toTransportations;
    // ------------ mappings ------------ //

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
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

    public FlightsBean getFlight() {
        return flight;
    }

    public void setFlight(FlightsBean flight) {
        this.flight = flight;
    }

    public RoomTypesBean getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypesBean roomType) {
        this.roomType = roomType;
    }

    public TrafficTicketsBean getTraffic() {
        return traffic;
    }

    public void setTraffic(TrafficTicketsBean traffic) {
        this.traffic = traffic;
    }

    public AttractionTicketsBean getAttractionTicket() {
        return attractionTicket;
    }

    public void setAttractionTicket(AttractionTicketsBean attractionTicket) {
        this.attractionTicket = attractionTicket;
    }

    public AttractionsBean getAttraction() {
        return attraction;
    }

    public void setAttraction(AttractionsBean attraction) {
        this.attraction = attraction;
    }

    public SelfDefinedSpotBean getSelfDefined() {
        return selfDefined;
    }

    public void setSelfDefined(SelfDefinedSpotBean selfDefined) {
        this.selfDefined = selfDefined;
    }

    @Override
    public String toString() {
        return "TripActivityBean [id=" + id + ", title=" + title + ", description=" + description + ", locationType="
                + locationType + ", startTime=" + startTime + ", endTime=" + endTime + ", ordinal=" + ordinal
                + ", createdAt=" + createdAt + ", lastModified=" + lastModified + ", deleteTime=" + deleteTime
                + ", deleteStatus=" + deleteStatus + ", dayId=" + dayId + ", flight=" + flight + ", roomType="
                + roomType + ", traffic=" + traffic + ", attractionTicket=" + attractionTicket + ", attraction="
                + attraction + ", selfDefined=" + selfDefined + "]";
    }

    public List<TripTransportations> getFromTransportations() {
        return fromTransportations;
    }

    public void setFromTransportations(List<TripTransportations> fromTransportations) {
        this.fromTransportations = fromTransportations;
    }

    public List<TripTransportations> getToTransportations() {
        return toTransportations;
    }

    public void setToTransportations(List<TripTransportations> toTransportations) {
        this.toTransportations = toTransportations;
    }

    public ActivityTypeEnum getLocationType() {
        return locationType;
    }

    public void setLocationType(ActivityTypeEnum locationType) {
        this.locationType = locationType;
    }

}
