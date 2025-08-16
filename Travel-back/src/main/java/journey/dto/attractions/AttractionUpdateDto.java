// ✅ DTO：AttractionUpdateDto.java
package journey.dto.attractions;

import java.time.LocalDateTime;
import java.util.Set;

public class AttractionUpdateDto {
    private String name;
    private String description;
    private String address;
    private LocalDateTime updatedAt;
    private Integer updatedBy;
    private Integer countryId;
    private Integer cityId;
    private Set<Integer> tagIds;
    private Set<Integer> typeIds;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Integer getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Integer updatedBy) { this.updatedBy = updatedBy; }

    public Integer getCountryId() { return countryId; }
    public void setCountryId(Integer countryId) { this.countryId = countryId; }

    public Integer getCityId() { return cityId; }
    public void setCityId(Integer cityId) { this.cityId = cityId; }

    public Set<Integer> getTagIds() { return tagIds; }
    public void setTagIds(Set<Integer> tagIds) { this.tagIds = tagIds; }

    public Set<Integer> getTypeIds() { return typeIds; }
    public void setTypeIds(Set<Integer> typeIds) { this.typeIds = typeIds; }
}

