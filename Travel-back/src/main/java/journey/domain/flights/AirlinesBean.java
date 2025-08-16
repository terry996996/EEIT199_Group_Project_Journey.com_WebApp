package journey.domain.flights;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "airlines")
public class AirlinesBean {

    @Id
    @Column(name = "airline_code", nullable = false)
    private String airlineCode;

    @Column(name = "airline_name", length = 100, nullable = false)
    private String airlineName;

    @Lob
    @Column(name = "image_url", nullable = true)
    private String imageURL;

    @Column(name = "image_type", nullable = true)
    private String imageType;

    @OneToMany(mappedBy = "airline", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "airline" })
    private Set<FlightsBean> flights;

    public String getAirline() {
        return airlineCode;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public Set<FlightsBean> getFlights() {
        return flights;
    }

    public void setFlights(Set<FlightsBean> flights) {
        this.flights = flights;
    }

    @Override
    public String toString() {
        return "AirlinesBean [airlineCode=" + airlineCode + ", airlineName=" + airlineName + ", imageURL="
                + imageURL + ", imageType=" + imageType + ", flights=" + flights + "]";
    }

}
