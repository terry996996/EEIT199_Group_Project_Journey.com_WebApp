package journey.domain.users;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import journey.converter.GenderEnumConverter;
import journey.domain.locations.CountryBean;
import journey.domain.orders.FlightOrderDetailsBean;
import journey.enums.GenderEnum;
import journey.mapping.UserPlanCollectionAuditable;

@Entity
@Table(name = "partners")
public class PartnerBean extends UserPlanCollectionAuditable {
    // ------------ actual columns ------------ //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
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
    @Column(name = "tel_number")
    private String telNumber;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "delete_time")
    private LocalDateTime deleteTime;
    @Column(name = "delete_status", nullable = false)
    private Integer deleteStatus = 1;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relates_to", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private UserBean relatesTo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nationality", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private CountryBean nationality;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "residence", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private CountryBean residence;
    // ------------ actual columns ------------ //

    // ------------ mappings ------------ //
    @OneToMany(mappedBy = "partnerId")
    @JsonManagedReference
    private Set<FlightOrderDetailsBean> flightOrderDetails;
    // ------------ mappings ------------ //

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public UserBean getRelatesTo() {
        return relatesTo;
    }

    public void setRelatesTo(UserBean relatesTo) {
        this.relatesTo = relatesTo;
    }

    public CountryBean getResidence() {
        return residence;
    }

    public void setResidence(CountryBean residence) {
        this.residence = residence;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "PartnerBean [id=" + id + ", givenName=" + givenName + ", familyName=" + familyName + ", givenNameLatin="
                + givenNameLatin + ", familyNameLatin=" + familyNameLatin + ", passportNo=" + passportNo + ", gender="
                + gender.toString() + ", telNumber=" + telNumber + ", email=" + email + ", createdAt=" + createdAt
                + ", lastModified=" + lastModified + ", deleteTime=" + deleteTime + ", deleteStatus=" + deleteStatus
                + ", relatesTo=" + relatesTo.getAllUserBean().getUsername() + ", residence=" + residence.getName()
                + "]";
    }

    public Set<FlightOrderDetailsBean> getFlightOrderDetails() {
        return flightOrderDetails;
    }

    public void setFlightOrderDetails(Set<FlightOrderDetailsBean> flightOrderDetails) {
        this.flightOrderDetails = flightOrderDetails;
    }

    public String getPassportExpire() {
        return passportExpire;
    }

    public void setPassportExpire(String passportExpire) {
        this.passportExpire = passportExpire;
    }

    public CountryBean getNationality() {
        return nationality;
    }

    public void setNationality(CountryBean nationality) {
        this.nationality = nationality;
    }

}
