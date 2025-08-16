package journey.domain.attractiontickets;

import java.time.LocalDateTime;
import java.util.Set; // ✅ 必須引入

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
import journey.domain.comments.CommentsBean;
import journey.domain.locations.CityBean;
import journey.domain.locations.CountryBean;
import journey.domain.orders.AttractionTicketOrderItemsBean;
import journey.domain.plans.TripActivityBean;

@Entity
@Table(name = "attraction_tickets")

public class AttractionTicketsBean {

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

    @Column(nullable = false, length = 50, unique = true)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    @Column(name = "image_type", nullable = false, length = 50)
    private String imageType;

    @Column
    private Integer views = 0;

    @Column(nullable = false)
    private Boolean state;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @ManyToMany
    @JoinTable(name = "attraction_tickets_middle_types", joinColumns = {
            @JoinColumn(name = "ticket_id", referencedColumnName = "id") }, inverseJoinColumns = {
                    @JoinColumn(name = "attraction_type_id", referencedColumnName = "id") })
    @JsonManagedReference
    private Set<AttractionTypesBean> attractionTypes;

    @ManyToMany
    @JoinTable(name = "attraction_tickets_middle_attraction_tags", joinColumns = {
            @JoinColumn(name = "ticket_id", referencedColumnName = "id") }, inverseJoinColumns = {
                    @JoinColumn(name = "tag_id", referencedColumnName = "id") })
    @JsonManagedReference
    private Set<AttractionTagsBean> attractionTags;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<TicketPackagesBean> packages;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<TicketContentsBean> contents;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<TicketFaqsBean> faqs;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<TicketImagesBean> images;

    @OneToMany(mappedBy = "attraction")
    @JsonManagedReference
    private Set<AttractionTicketOrderItemsBean> attractionTicketOrderItems;

    @OneToMany(mappedBy = "attractionTicket")
    @JsonManagedReference
    private Set<TripActivityBean> tripActivities;

    @OneToMany(mappedBy = "attractionTicketId")
    @JsonManagedReference
    private Set<CommentsBean> comments;

    public AttractionTicketsBean() {
    }

    public Set<AttractionTypesBean> getAttractionTypes() {
        return attractionTypes;
    }

    public void setAttractionTypes(Set<AttractionTypesBean> attractionTypes) {
        this.attractionTypes = attractionTypes;
    }

    public Set<TicketPackagesBean> getPackages() {
        return packages;
    }

    public void setPackages(Set<TicketPackagesBean> packages) {
        this.packages = packages;
    }

    public Set<TicketContentsBean> getContents() {
        return contents;
    }

    public void setContents(Set<TicketContentsBean> contents) {
        this.contents = contents;
    }

    public Set<TicketFaqsBean> getFaqs() {
        return faqs;
    }

    public void setFaqs(Set<TicketFaqsBean> faqs) {
        this.faqs = faqs;
    }

    public Set<TicketImagesBean> getImages() {
        return images;
    }

    public void setImages(Set<TicketImagesBean> images) {
        this.images = images;
    }

    public AttractionTicketsBean(Integer id, CountryBean country, CityBean city, String address, String name,
            String description, String imageUrl, String imageType, Integer views, Boolean state,
            LocalDateTime startTime,
            LocalDateTime endTime, LocalDateTime createdAt, LocalDateTime updatedAt, Integer createdBy,
            Integer updatedBy) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.address = address;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.imageType = imageType;
        this.views = views;
        this.state = state;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
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

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Set<AttractionTagsBean> getAttractionTags() {
        return attractionTags;
    }

    public void setAttractionTags(Set<AttractionTagsBean> attractionTags) {
        this.attractionTags = attractionTags;
    }

    public Set<AttractionTicketOrderItemsBean> getAttractionTicketOrderItems() {
        return attractionTicketOrderItems;
    }

    public void setAttractionTicketOrderItems(Set<AttractionTicketOrderItemsBean> attractionTicketOrderItems) {
        this.attractionTicketOrderItems = attractionTicketOrderItems;
    }

    public Set<TripActivityBean> getTripActivities() {
        return tripActivities;
    }

    public void setTripActivities(Set<TripActivityBean> tripActivities) {
        this.tripActivities = tripActivities;
    }

    public Set<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(Set<CommentsBean> comments) {
        this.comments = comments;
    }

    @PrePersist
    public void prePersist() {
        if (this.startTime == null)
            this.startTime = LocalDateTime.now();
        if (this.createdAt == null)
            this.createdAt = LocalDateTime.now();
    }
}
