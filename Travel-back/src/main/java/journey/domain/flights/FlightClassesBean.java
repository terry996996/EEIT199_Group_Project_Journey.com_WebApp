package journey.domain.flights;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "flight_classes")
public class FlightClassesBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id", nullable = false)
    private Integer classId;

    public Integer getClassId() {
        return classId;
    }

    public Set<FlightsDetailsBean> getFlightDetails() {
        return flightDetails;
    }

    public void setFlightDetails(Set<FlightsDetailsBean> flightDetails) {
        this.flightDetails = flightDetails;
    }

    @Column(name = "class_type", nullable = false, unique = true, length = 10)
    private String classType;

    @OneToMany(mappedBy = "flightclass", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<FlightsDetailsBean> flightDetails;

    public Integer getFlightclass() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    @Override
    public String toString() {
        return "FlightClassesBean [classId=" + classId + ", classType=" + classType + "]";
    }

}
