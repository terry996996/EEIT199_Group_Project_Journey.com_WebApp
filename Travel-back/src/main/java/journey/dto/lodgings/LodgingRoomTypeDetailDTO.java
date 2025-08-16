package journey.dto.lodgings;

import java.math.BigDecimal;
import java.util.List;

/**
 * 旅館房型詳細資料傳輸物件（DTO）
 * 用於回傳特定旅館和房型的完整詳細資訊。
 * 
 * @author Journey Team
 * @since 2024
 */
public class LodgingRoomTypeDetailDTO {

    // 旅館基本資訊
    private Integer lodgingId;
    private String lodgingName;
    private String description;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String lodgingTel;
    private String email;

    // 旅館關聯資訊
    private Integer lodgingTypeId;
    private String lodgingType;
    private Integer cityId;
    private String cityName;
    private Integer countryId;
    private String countryName;

    // 房型基本資訊
    private Integer roomTypeId;
    private String roomTypeName;
    private String roomTypeDescription;
    private Integer pricePerNight;
    private Integer maxGuests;

    // 評分統計
    private Double avgRating;
    private Long reviewCount;

    // 圖片列表
    private List<SimpleImageDTO> lodgingImages;
    private List<SimpleImageDTO> roomTypeImages;

    // 床型配置
    private List<BedTypeDTO> bedTypes;

    // 設施配置
    private List<FacilityDTO> facilities;

    // 留言列表
    private List<journey.dto.Comments.CommentResponseDTO> comments;

    // 建構函數
    public LodgingRoomTypeDetailDTO() {
    }

    public LodgingRoomTypeDetailDTO(Integer lodgingId, String lodgingName, String description, String address,
            BigDecimal latitude, BigDecimal longitude, String lodgingTel, String email, Integer lodgingTypeId,
            String lodgingType, Integer cityId, String cityName, Integer countryId, String countryName,
            Integer roomTypeId, String roomTypeName, String roomTypeDescription,
            Integer pricePerNight, Integer maxGuests, Double avgRating, Long reviewCount,
            List<SimpleImageDTO> lodgingImages,
            List<SimpleImageDTO> roomTypeImages, List<BedTypeDTO> bedTypes, List<FacilityDTO> facilities,
            List<journey.dto.Comments.CommentResponseDTO> comments) {
        this.lodgingId = lodgingId;
        this.lodgingName = lodgingName;
        this.description = description;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lodgingTel = lodgingTel;
        this.email = email;
        this.lodgingTypeId = lodgingTypeId;
        this.lodgingType = lodgingType;
        this.cityId = cityId;
        this.cityName = cityName;
        this.countryId = countryId;
        this.countryName = countryName;
        this.roomTypeId = roomTypeId;
        this.roomTypeName = roomTypeName;
        this.roomTypeDescription = roomTypeDescription;
        this.pricePerNight = pricePerNight;
        this.maxGuests = maxGuests;
        this.avgRating = avgRating;
        this.reviewCount = reviewCount;
        this.lodgingImages = lodgingImages;
        this.roomTypeImages = roomTypeImages;
        this.bedTypes = bedTypes;
        this.facilities = facilities;
        this.comments = comments;
    }

    // Getters and Setters
    public Integer getLodgingId() {
        return lodgingId;
    }

    public void setLodgingId(Integer lodgingId) {
        this.lodgingId = lodgingId;
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

    public Integer getLodgingTypeId() {
        return lodgingTypeId;
    }

    public void setLodgingTypeId(Integer lodgingTypeId) {
        this.lodgingTypeId = lodgingTypeId;
    }

    public String getLodgingType() {
        return lodgingType;
    }

    public void setLodgingType(String lodgingType) {
        this.lodgingType = lodgingType;
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

    public Integer getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Integer roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public String getRoomTypeDescription() {
        return roomTypeDescription;
    }

    public void setRoomTypeDescription(String roomTypeDescription) {
        this.roomTypeDescription = roomTypeDescription;
    }

    public Integer getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Integer pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public Integer getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(Integer maxGuests) {
        this.maxGuests = maxGuests;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public Long getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Long reviewCount) {
        this.reviewCount = reviewCount;
    }

    public List<SimpleImageDTO> getLodgingImages() {
        return lodgingImages;
    }

    public void setLodgingImages(List<SimpleImageDTO> lodgingImages) {
        this.lodgingImages = lodgingImages;
    }

    public List<SimpleImageDTO> getRoomTypeImages() {
        return roomTypeImages;
    }

    public void setRoomTypeImages(List<SimpleImageDTO> roomTypeImages) {
        this.roomTypeImages = roomTypeImages;
    }

    public List<BedTypeDTO> getBedTypes() {
        return bedTypes;
    }

    public void setBedTypes(List<BedTypeDTO> bedTypes) {
        this.bedTypes = bedTypes;
    }

    public List<FacilityDTO> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<FacilityDTO> facilities) {
        this.facilities = facilities;
    }

    public List<journey.dto.Comments.CommentResponseDTO> getComments() {
        return comments;
    }

    public void setComments(List<journey.dto.Comments.CommentResponseDTO> comments) {
        this.comments = comments;
    }
}