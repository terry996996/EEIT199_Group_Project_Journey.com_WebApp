package journey.dto.lodgings;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 旅宿回應資料傳輸物件（DTO）
 * 用於回傳旅宿的詳細資訊。
 * 
 * @author FAN
 * @since 250704
 */
public class LodgingResponseDTO {

    private Integer id;
    private String lodgingName;
    private Integer lodgingTypeId;
    private String lodgingTypeName;
    private String description;
    private Integer countryId;
    private String countryName;
    private Integer cityId;
    private String cityName;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String lodgingTel;
    private String email;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ImageDTO> images;

    public LodgingResponseDTO() {
    }

    public LodgingResponseDTO(Integer id, String lodgingName, String description,
            String address, BigDecimal latitude, BigDecimal longitude,
            String lodgingTel, String email, Boolean isActive,
            LocalDateTime createdAt, LocalDateTime updatedAt,
            Integer lodgingTypeId, String lodgingTypeName,
            Integer cityId, String cityName,
            Integer countryId, String countryName,
            List<ImageDTO> images) {
        this.id = id;
        this.lodgingName = lodgingName;
        this.description = description;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lodgingTel = lodgingTel;
        this.email = email;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lodgingTypeId = lodgingTypeId;
        this.lodgingTypeName = lodgingTypeName;
        this.cityId = cityId;
        this.cityName = cityName;
        this.countryId = countryId;
        this.countryName = countryName;
        this.images = images;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLodgingName() {
        return lodgingName;
    }

    public void setLodgingName(String lodgingName) {
        this.lodgingName = lodgingName;
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

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getLodgingTel() {
        return lodgingTel;
    }

    public void setLodgingTel(String lodgingTel) {
        this.lodgingTel = lodgingTel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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

    public Integer getLodgingTypeId() {
        return lodgingTypeId;
    }

    public void setLodgingTypeId(Integer lodgingTypeId) {
        this.lodgingTypeId = lodgingTypeId;
    }

    public String getLodgingTypeName() {
        return lodgingTypeName;
    }

    public void setLodgingTypeName(String lodgingTypeName) {
        this.lodgingTypeName = lodgingTypeName;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public List<ImageDTO> getImages() {
        return images;
    }

    public void setImages(List<ImageDTO> images) {
        this.images = images;
    }
}