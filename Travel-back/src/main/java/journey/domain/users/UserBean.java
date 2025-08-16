package journey.domain.users;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import journey.converter.GenderEnumConverter;
import journey.domain.collections.CollectionBean;
import journey.domain.consultationmessageboard.ConsultsBean;
import journey.domain.coupons.CouponInstancesBean;
import journey.domain.locations.CountryBean;
import journey.domain.onlinecustomerservice.CustomerServiceChatroomBean;
import journey.domain.plans.SelfDefinedSpotBean;
import journey.domain.plans.TripPlanBean;
import journey.domain.pointcards.PointCardsBean;
import journey.enums.GenderEnum;

@Entity
@Table(name = "users")
public class UserBean {
    // ------------ actual columns ------------ //
    @Id
    private Integer id;
    @Column(name = "given_name", nullable = false)
    private String givenName;
    @Column(name = "family_name", nullable = false)
    private String familyName;
    @Column(name = "given_name_latin")
    private String givenNameLatin;
    @Column(name = "family_name_latin")
    private String familyNameLatin;
    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;
    @Column(name = "passport_no", nullable = false)
    private String passportNo;
    @Column(name = "passport_expire", nullable = false)
    private String passportExpire;
    @Convert(converter = GenderEnumConverter.class)
    @Column(name = "gender", nullable = false)
    private GenderEnum gender;
    @Column(name = "tel_number", nullable = false)
    private String telNumber;
    @Column(name = "address", nullable = false)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nationality", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private CountryBean nationality;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "residence", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private CountryBean residence;
    // ------------ actual columns ------------ //

    // ------------ mappings ------------//
    @OneToOne
    @MapsId
    @JoinColumn(name = "id", referencedColumnName = "id")
    @JsonManagedReference
    private AllUserBean allUserBean;

    @OneToMany(mappedBy = "relatesTo")
    @JsonManagedReference
    private Set<PartnerBean> partnerBeans;

    @OneToMany(mappedBy = "userId")
    @JsonManagedReference
    private Set<CollectionBean> userCollections;

    @OneToMany(mappedBy = "createdBy")
    @JsonManagedReference
    private Set<SelfDefinedSpotBean> selfDefinedSpotBeans;

    @OneToMany(mappedBy = "createdBy")
    @JsonManagedReference
    private Set<TripPlanBean> tripPlanBeans;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<ConsultsBean> consultsBeans;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<CustomerServiceChatroomBean> chatrooms;

    @OneToOne(mappedBy = "user")
    @JsonBackReference
    private PointCardsBean pointCard;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<CouponInstancesBean> couponInstances;
    // ------------ mappings ------------//

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenNameLatin() {
        return givenNameLatin;
    }

    public void setGivenNameLatin(String givenNameLatin) {
        this.givenNameLatin = givenNameLatin;
    }

    public String getFamilyNameLatin() {
        return familyNameLatin;
    }

    public void setFamilyNameLatin(String familyNameLatin) {
        this.familyNameLatin = familyNameLatin;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public AllUserBean getAllUserBean() {
        return allUserBean;
    }

    public void setAllUserBean(AllUserBean allUserBean) {
        this.allUserBean = allUserBean;
    }

    public CountryBean getResidence() {
        return residence;
    }

    public void setResidence(CountryBean residence) {
        this.residence = residence;
    }

    public CountryBean getNationality() {
        return nationality;
    }

    public void setNationality(CountryBean nationality) {
        this.nationality = nationality;
    }

    public Set<PartnerBean> getPartnerBeans() {
        return partnerBeans;
    }

    public void setPartnerBeans(Set<PartnerBean> partners) {
        this.partnerBeans = partners;
    }

    public Set<CollectionBean> getUserCollections() {
        return userCollections;
    }

    public void setUserCollections(Set<CollectionBean> userCollections) {
        this.userCollections = userCollections;
    }

    public Set<SelfDefinedSpotBean> getSelfDefinedSpotBeans() {
        return selfDefinedSpotBeans;
    }

    public void setSelfDefinedSpotBeans(Set<SelfDefinedSpotBean> selfDefinedSpotBeans) {
        this.selfDefinedSpotBeans = selfDefinedSpotBeans;
    }

    public Set<TripPlanBean> getTripPlanBeans() {
        return tripPlanBeans;
    }

    public void setTripPlanBeans(Set<TripPlanBean> tripPlanBeans) {
        this.tripPlanBeans = tripPlanBeans;
    }

    public Set<ConsultsBean> getConsultsBeans() {
        return consultsBeans;
    }

    public void setConsultsBeans(Set<ConsultsBean> consultsBeans) {
        this.consultsBeans = consultsBeans;
    }

    public Set<CustomerServiceChatroomBean> getChatrooms() {
        return chatrooms;
    }

    public void setChatrooms(Set<CustomerServiceChatroomBean> chatrooms) {
        this.chatrooms = chatrooms;
    }

    public PointCardsBean getPointCard() {
        return pointCard;
    }

    public void setPointCard(PointCardsBean pointCard) {
        this.pointCard = pointCard;
    }

    public Set<CouponInstancesBean> getCouponInstances() {
        return couponInstances;
    }

    public void setCouponInstances(Set<CouponInstancesBean> couponInstances) {
        this.couponInstances = couponInstances;
    }

    @Override
    public String toString() {
        return "UserBean [id=" + id + ", givenName=" + givenName + ", familyName=" + familyName + ", givenNameLatin="
                + givenNameLatin + ", familyNameLatin=" + familyNameLatin + ", birthday=" + birthday + ", passportNo="
                + passportNo + ", gender=" + gender + ", telNumber=" + telNumber + ", address=" + address
                + ", residence=" + residence + ", nationality=" + nationality
                + "]";
    }

    public String getPassportExpire() {
        return passportExpire;
    }

    public void setPassportExpire(String passportExpire) {
        this.passportExpire = passportExpire;
    }

}
