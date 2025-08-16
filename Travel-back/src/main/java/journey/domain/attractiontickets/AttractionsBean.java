package journey.domain.attractiontickets;

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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import journey.domain.locations.CityBean;
import journey.domain.locations.CountryBean;
import journey.domain.plans.TripActivityBean;

@Entity
@Table(name = "attraction")
public class AttractionsBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    @JsonBackReference
    private CountryBean country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city", nullable = false)
    @JsonBackReference
    private CityBean city;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @Column
    private Integer views = 0;

    @Column(name = "attraction_url")
    private String attractionUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    @Column(name = "image_type", nullable = false, length = 50)
    private String imageType;

    ////// 雙向連接
    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<AttractionImagesBean> images;

    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<AttractionContentsBean> contents;

    @ManyToMany
    @JoinTable(name = "attraction_middle_attraction_tags", joinColumns = {
            @JoinColumn(name = "attraction_id", referencedColumnName = "id") }, inverseJoinColumns = {
                    @JoinColumn(name = "attraction_tag_id", referencedColumnName = "id") })
    @JsonManagedReference
    private Set<AttractionTagsBean> tags;

    @ManyToMany
    @JoinTable(name = "attraction_middle_attraction_types", joinColumns = {
            @JoinColumn(name = "attraction_id", referencedColumnName = "id") }, inverseJoinColumns = {
                    @JoinColumn(name = "attraction_type_id", referencedColumnName = "id") })
    @JsonManagedReference
    private Set<AttractionTypesBean> types;

    @OneToMany(mappedBy = "attraction")
    @JsonManagedReference
    private Set<TripActivityBean> tripActivities;

    public AttractionsBean() {
    }

    public AttractionsBean(Integer id, CountryBean country, CityBean city, String address, String name,
            String description, Integer views, String attractionUrl, LocalDateTime createdAt, Integer createdBy,
            LocalDateTime updatedAt, Integer updatedBy, String imageUrl, String imageType, Set<AttractionTagsBean> tags,
            Set<AttractionTypesBean> types) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.address = address;
        this.name = name;
        this.description = description;
        this.views = views;
        this.attractionUrl = attractionUrl;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.imageUrl = imageUrl;
        this.imageType = imageType;
        this.tags = tags;
        this.types = types;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CountryBean getCountry() {
        return country;
    }

    public void setCountry(CountryBean country) {
        this.country = country;
    }

    public CityBean getCity() {
        return city;
    }

    public void setCity(CityBean city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public String getAttractionUrl() {
        return attractionUrl;
    }

    public void setAttractionUrl(String attractionUrl) {
        this.attractionUrl = attractionUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public Set<AttractionTagsBean> getTags() {
        return tags;
    }

    public void setTags(Set<AttractionTagsBean> tags) {
        this.tags = tags;
    }

    public Set<AttractionTypesBean> getTypes() {
        return types;
    }

    public void setTypes(Set<AttractionTypesBean> types) {
        this.types = types;
    }

    public Set<AttractionImagesBean> getImages() {
        return images;
    }

    public void setImages(Set<AttractionImagesBean> images) {
        this.images = images;
    }

    public Set<AttractionContentsBean> getContents() {
        return contents;
    }

    public void setContents(Set<AttractionContentsBean> contents) {
        this.contents = contents;
    }

    public Set<TripActivityBean> getTripActivities() {
        return tripActivities;
    }

    public void setTripActivities(Set<TripActivityBean> tripActivities) {
        this.tripActivities = tripActivities;
    }

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null)
            this.createdAt = LocalDateTime.now();
    }
}
