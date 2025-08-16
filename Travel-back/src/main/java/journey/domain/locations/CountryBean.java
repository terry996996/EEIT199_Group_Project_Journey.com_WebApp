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
import journey.domain.traffictickets.TrafficTicketsBean;
import journey.domain.users.PartnerBean;
import journey.domain.users.UserBean;

@Entity
@Table(name = "countries")
public class CountryBean {
    // ------------ actual columns ------------ //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    @Column(name = "name_zh", unique = true, nullable = false)
    private String nameZh;
    @Column(name = "phone_code", unique = true, nullable = false)
    private String phoneCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "continent", referencedColumnName = "name", nullable = false)
    @JsonBackReference
    private ContinentBean continent;
    // ------------ actual columns ------------ //

    // ------------ mappings ------------//
    @OneToMany(mappedBy = "country")
    @JsonManagedReference
    private Set<CityBean> cityBeans;

    @OneToMany(mappedBy = "residence")
    @JsonManagedReference
    private Set<UserBean> userResidences;

    @OneToMany(mappedBy = "nationality")
    @JsonManagedReference
    private Set<UserBean> userNationalities;

    @OneToMany(mappedBy = "residence")
    @JsonManagedReference
    private Set<PartnerBean> partnerBeans;

    @OneToMany(mappedBy = "residence")
    @JsonManagedReference
    private Set<PartnerBean> partnerResidences;

    @OneToMany(mappedBy = "nationality")
    @JsonManagedReference
    private Set<UserBean> partnerNationalities;

    @OneToMany(mappedBy = "coutry")
    @JsonManagedReference
    private Set<SelfDefinedSpotBean> selfDefinedSpotBeans;

    @OneToMany(mappedBy = "country")
    @JsonManagedReference
    private Set<AirportsBean> airports;

    @OneToMany(mappedBy = "country")
    @JsonManagedReference
    private Set<TrafficTicketsBean> trafficTickets;

    @OneToMany(mappedBy = "country")
    @JsonManagedReference
    private Set<LodgingsBean> lodgings;

    @OneToMany(mappedBy = "country")
    @JsonManagedReference
    private Set<AttractionTicketsBean> attractionTickets;

    @OneToMany(mappedBy = "country")
    @JsonManagedReference
    protected Set<AttractionsBean> attractions;
    // ------------ mappings ------------//

    @Override
    public String toString() {
        return "CountryBean [id=" + id + ", name=" + name + ", nameZh=" + nameZh + ", phoneCode=" + phoneCode
                + ", continent=" + continent.getName() + "]";
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

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public ContinentBean getContinentBean() {
        return continent;
    }

    public void setContinentBean(ContinentBean continent) {
        this.continent = continent;
    }

    public ContinentBean getContinent() {
        return continent;
    }

    public void setContinent(ContinentBean continent) {
        this.continent = continent;
    }

    public Set<CityBean> getCityBeans() {
        return cityBeans;
    }

    public void setCityBeans(Set<CityBean> cityBeans) {
        this.cityBeans = cityBeans;
    }

    public Set<UserBean> getUserResidences() {
        return userResidences;
    }

    public void setUserResidences(Set<UserBean> userResidences) {
        this.userResidences = userResidences;
    }

    public Set<UserBean> getUserNationalities() {
        return userNationalities;
    }

    public void setUserNationalities(Set<UserBean> userNationalities) {
        this.userNationalities = userNationalities;
    }

    public Set<PartnerBean> getPartnerResidences() {
        return partnerResidences;
    }

    public void setPartnerResidences(Set<PartnerBean> partnerResidences) {
        this.partnerResidences = partnerResidences;
    }

    public Set<UserBean> getPartnerNationalities() {
        return partnerNationalities;
    }

    public void setPartnerNationalities(Set<UserBean> partnerNationalities) {
        this.partnerNationalities = partnerNationalities;
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

    public Set<TrafficTicketsBean> getTrafficTickets() {
        return trafficTickets;
    }

    public void setTrafficTickets(Set<TrafficTicketsBean> trafficTickets) {
        this.trafficTickets = trafficTickets;
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
