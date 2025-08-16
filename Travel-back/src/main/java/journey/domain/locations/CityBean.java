package journey.domain.locations;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
import journey.domain.flights.AirportsBean;
import journey.domain.lodgings.LodgingsBean;
import journey.domain.plans.SelfDefinedSpotBean;

@Entity
@Table(name = "cities")
public class CityBean {
    // ------------ actual columns ------------ //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "name_zh")
    private String nameZh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private CountryBean country;
    // ------------ actual columns ------------ //

    // ------------ mappings ------------//
    @OneToMany(mappedBy = "city")
    @JsonManagedReference
    private Set<SelfDefinedSpotBean> selfDefinedSpotBeans;

    @OneToMany(mappedBy = "city")
    @JsonManagedReference
    private Set<AirportsBean> airports;

    @OneToMany(mappedBy = "city")
    @JsonManagedReference
    protected Set<LodgingsBean> lodgings;

    @OneToMany(mappedBy = "city")
    @JsonManagedReference
    protected Set<AttractionTicketsBean> attractionTickets;

    @OneToMany(mappedBy = "city")
    @JsonManagedReference
    protected Set<AttractionsBean> attractions;
    // ------------ mappings ------------//

    @Override
    public String toString() {
        return "CityBean [id=" + id + ", name=" + name + ", nameZh=" + nameZh
                + ", country=" + country.getName() + "]";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    public CountryBean getCountry() {
        return country;
    }

    public void setCountry(CountryBean countryBean) {
        this.country = countryBean;
    }

    public Set<SelfDefinedSpotBean> getSelfDefinedSpotBeans() {
        return selfDefinedSpotBeans;
    }

    public void setSelfDefinedSpotBeans(Set<SelfDefinedSpotBean> selfDefinedSpotBeans) {
        this.selfDefinedSpotBeans = selfDefinedSpotBeans;
    }

    public Set<AirportsBean> getAirports() {
        return airports;
    }

    public void setAirports(Set<AirportsBean> airports) {
        this.airports = airports;
    }

    public Set<LodgingsBean> getLodgings() {
        return lodgings;
    }

    public void setLodgings(Set<LodgingsBean> lodgings) {
        this.lodgings = lodgings;
    }

    public Set<AttractionTicketsBean> getAttractionTickets() {
        return attractionTickets;
    }

    public void setAttractionTickets(Set<AttractionTicketsBean> attractionTickets) {
        this.attractionTickets = attractionTickets;
    }

    public Set<AttractionsBean> getAttractions() {
        return attractions;
    }

    public void setAttractions(Set<AttractionsBean> attractions) {
        this.attractions = attractions;
    }

}
