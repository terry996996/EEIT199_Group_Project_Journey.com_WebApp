package journey.dto.attractions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class AttractionFullCreateDto {
    private String name;
    private String description;
    private String address;
    private String imageUrl;
    private String imageType;
    private LocalDateTime createdAt;
    private Integer createdBy;

    private Integer countryId;
    private Integer cityId;

    private Set<Integer> tagIds;
    private Set<Integer> typeIds;

    private List<AttractionImageCreateDto> images;
    private List<AttractionContentCreateDto> contents;
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
    public List<AttractionImageCreateDto> getImages() {
        return images;
    }
    public void setImages(List<AttractionImageCreateDto> images) {
        this.images = images;
    }
    public List<AttractionContentCreateDto> getContents() {
        return contents;
    }
    public void setContents(List<AttractionContentCreateDto> contents) {
        this.contents = contents;
    }

    // 👉 補上 getter/setter
    
}