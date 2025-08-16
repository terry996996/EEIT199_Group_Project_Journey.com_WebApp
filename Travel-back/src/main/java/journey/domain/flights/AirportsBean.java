package journey.domain.flights;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import journey.domain.locations.CityBean;
import journey.domain.locations.CountryBean;

@Entity
@Table(name = "airports")
public class AirportsBean {

    @Id
    @Column(name = "airport_code", length = 6, nullable = false)
    private String airportCode;

    @Column(name = "airport_name", nullable = false, length = 100)
    private String airportName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    @JsonBackReference
    private CountryBean country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    @JsonIgnoreProperties("airports")
    private CityBean city;

    @OneToMany(mappedBy = "departureAirport")
    @JsonManagedReference
    private Set<FlightsBean> departureFlights;

    @OneToMany(mappedBy = "arrivalAirport")
    @JsonManagedReference
    private Set<FlightsBean> arrivalFlights;

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public CountryBean getCountry() {
        return country;
    }

    public void setCountry(CountryBean countryId) {
        this.country = countryId;
    }

    public CityBean getCity() {
        return city;
    }

    public void setCity(CityBean cityId) {
        this.city = cityId;
    }

    public Set<FlightsBean> getDepartureFlights() {
        return departureFlights;
    }

    public void setDepartureFlights(Set<FlightsBean> departureFlights) {
        this.departureFlights = departureFlights;
    }

    public Set<FlightsBean> getArrivalFlights() {
        return arrivalFlights;
    }

    public void setArrivalFlights(Set<FlightsBean> arrivalFlights) {
        this.arrivalFlights = arrivalFlights;
    }

    @Override
    public String toString() {
        return "AirportsBean [airportCode=" + airportCode + ", airportName=" + airportName + ", countryId=" + country
                + ", cityId=" + city + ", departureFlights=" + departureFlights + ", arrivalFlights=" + arrivalFlights
                + "]";
    }

}
