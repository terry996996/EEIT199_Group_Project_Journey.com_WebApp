package journey.dto.attractions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import journey.dto.*;;

public class AttractionTicketDto {

    private Integer id;

    private String name;
    private String description;
    private String address;
    private String imageUrl;
    private String imageType;
    private Integer views;
    private Boolean state;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer createdBy;
    private Integer updatedBy;

    private Integer countryId;
    private Integer cityId;

    // 多對多
    private Set<Integer> tagIds;
    private Set<Integer> typeIds;

    // 一對多：圖片、內文、套票（套票下還會有票種）
    private List<TicketImageDto> images;
    private List<TicketContentDto> contents;
    private List<TicketPackageDto> packages;

    // ---------- Getter / Setter ----------
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Set<Integer> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<Integer> tagIds) {
        this.tagIds = tagIds;
    }

    public Set<Integer> getTypeIds() {
        return typeIds;
    }

    public void setTypeIds(Set<Integer> typeIds) {
        this.typeIds = typeIds;
    }

    public List<TicketImageDto> getImages() {
        return images;
    }

    public void setImages(List<TicketImageDto> images) {
        this.images = images;
    }

    public List<TicketContentDto> getContents() {
        return contents;
    }

    public void setContents(List<TicketContentDto> contents) {
        this.contents = contents;
    }

    public List<TicketPackageDto> getPackages() {
        return packages;
    }

    public void setPackages(List<TicketPackageDto> packages) {
        this.packages = packages;
    }

}
